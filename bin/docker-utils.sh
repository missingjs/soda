#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat << EOF
usage: 
    $cmd <command> <lang> [options]

For container management

    $cmd build <lang>
        Build image with framework/<lang>/Dockerfile.

    $cmd start <lang>
        Start language specific container, it will be created if not exist.

    $cmd stop  <lang>
        Stop container

    $cmd force-purge <lang>
        Stop container forcely then remove

Project management

    $cmd drop-project <lang>
        Remove whole content of project in container

    $cmd run-work  <lang> <command> [args...]
        Call framework/{lang}/work.sh in container

    $cmd invoke <lang> [exec-options... --] <commands>
        Execute generic commands in container, options for 'docker exec' and the commands your would like to run separated by '--'

    $cmd sync-file <lang>
        Sync local file with container

    $cmd clear-cache <lang>
        Invalidate file tag cache in local

Information query

    $cmd show <lang> <container|workdir|ip>
        container: container name
        workdir:   working director in container
        ip:        ip of container

    $cmd assert-running <lang>
        Check whether the container is running. If not, show error message then exit with code 2

EOF
    exit 1
}

subcmd=$1
lang=$2

[ -z "$subcmd" -o -z "$lang" ] && usage

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(dirname $self_dir)
framework_dir=$soda_dir/framework
volume_dir=~/.soda/volumes/$lang
src_tag_log=_source_tag.log

docker_image="missingjs/soda-$lang"
container="soda-task-$lang"
workdir=/task$(pwd)
proxy_option=$($framework_dir/common/build-utils.sh run-proxy)

build_image()
{
    local base_image="$docker_image-base"
    echo "Building image $base_image..."
    cd $framework_dir/$lang

    # proxy options for docker build
    local proxy_option="$($self_dir/support/build-utils.sh proxy-arg)"

    docker build --network host $proxy_option -t $base_image . || exit

    # install common software, or do some common configurations
    local tmpdir=$(mktemp -d)
    cd $tmpdir
    cat >Dockerfile <<EOF
FROM $base_image
EOF
    
    set -x
    docker build --network host $proxy_option -t $docker_image .

    cd - >/dev/null
    rm -rf $tmpdir
}

docker_start()
{
    # check if container exist
    if ! docker container ls --all | grep -q $container; then
        local home_vol="$volume_dir/home"
        local task_vol="$volume_dir/task"
        local user="$(id -un)"

        set -e
        [ -d $home_vol ] || mkdir -p $home_vol
        [ -d $task_vol ] || mkdir -p $task_vol

        echo "create docker container $container"
        docker run -d \
            --privileged \
            --name $container \
            --network bridge $proxy_option \
            -v "$soda_dir/bin:/soda/bin:ro" \
            -v "$soda_dir/framework/common:/soda/framework/common:ro" \
            -v "$soda_dir/framework/$lang:/soda/framework/$lang" \
            -v "/etc/group:/etc/group:ro" \
            -v "/etc/passwd:/etc/passwd:ro" \
            -v "/etc/timezone:/etc/timezone:ro" \
            -v "/etc/localtime:/etc/localtime:ro" \
            -v "$home_vol:/home/$user" \
            -v "$task_vol:/task" \
            $docker_image tail -f /dev/null


        # initialize container
#        docker container exec $container \
#            bash -c "
#                [ -e /task ] || mkdir /task
#                chmod 777 /task
#            "

#        user_id=$(id -u)
#        user_name=$(id -un)
#        group_id=$(id -g)
#        group_name=$(id -gn)
#        echo "add user $user_name"
#        docker container exec $container \
#            bash -c "
#                [ -e /task ] || mkdir /task
#                chmod 777 /task
#                groupadd -g $group_id $group_name || true
#                useradd -rm -d /home/$user_name -s /bin/bash -g $group_id -u $user_id $user_name || true
#            "

        echo "docker container $container created"
        set +e
    fi

    # check if container running
    if ! docker container ls | grep -q $container; then
        echo "start container $container"
        docker container start $container || exit
    fi
}

force_purge()
{
    echo "stop container $container ..."
    docker stop -t 1 $container
    echo "----"
    echo "remove container $container ..."
    docker container rm $container
}

show_info()
{
    local key=$1
    case $key in
        container)
            echo $container
            ;;
        workdir)
            echo $workdir
            ;;
        ip)
            local ip=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $container)
            echo ${ip:-127.0.0.1}
            ;;
        *)
            usage
            ;;
    esac
}

sync_file_to_container()
{
    local file=$1
    [ -e $file ] || { echo "file not exist: $file"; exit 2; }

    local guest_mt=$(grep "^$lang " $src_tag_log 2>/dev/null | awk '{print $2}')
    local host_mt=$(date -r $file "+%s")
    
    if [ "$host_mt" != "$guest_mt" ]; then
        docker exec -u $(id -u) $container \
            bash -c "[ -e $workdir ] || mkdir -p $workdir"

        set -x; docker cp $file $container:$workdir/$file; set +x

        if [ -n "$guest_mt" ]; then
            sed -i "s/^$lang .*\$/$lang $host_mt/g" $src_tag_log
        else
            echo "$lang $host_mt" >> $src_tag_log
        fi
    fi
}

sync_file_to_container_old()
{
    local file=$1
    [ -e $file ] || { echo "file not exist: $file"; exit 2; }

    local host_mt=$(date -r $file "+%s")
    local up_to_date="
        [ -e $workdir ] || mkdir -p $workdir

        cd $workdir
        [ ! -e $file ] && { echo no; exit; }

        guest_mt=\$(date -r $file '+%s')
        [ $host_mt -gt \$guest_mt ] && { echo no; exit; }

        echo yes
    "

    local res=$(docker exec -u $(id -u) $container bash -c "$up_to_date")
    case "$res" in
        no)
            set -x; docker cp $file $container:$workdir/$file
            ;;
        yes)
            # nothing to do
            ;;
        *)
            # docker exec failed
            exit 2
            ;;
    esac
}

function clear_cache()
{
    [ -e $src_tag_log ] && sed -i "s/^$lang .*\$/$lang 0/g" $src_tag_log
}

__options=
__commands=
function collect_options_and_commands()
{
    local index=0
    local args=("$@")

    __options=()
    __commands=()
    while [ $index -lt ${#args[@]} ]; do
        [ "${args[$index]}" == "--" ] && break
        ((index++))
    done

    if [ $index -lt ${#args[@]} ]; then
        __options+=("${args[@]:0:$index}")
        let p=index+1
        __commands+=("${args[@]:$p}")
    else
        # no options
        __commands+=("${args[@]}")
    fi
}

function invoke_command()
{
    collect_options_and_commands "$@"
    docker exec -i -u $(id -u) $proxy_option "${__options[@]}" $container "${__commands[@]}"
}

function run_work()
{
    collect_options_and_commands "$@"
    invoke_command "${__options[@]}" -w $workdir -- /soda/framework/$lang/work.sh "${__commands[@]}"
}

function drop_project_in_container()
{
    [ -e soda.prj.yml ] && docker exec $container rm -rfv $workdir
}

function assert_running()
{
    local status=$(docker inspect -f '{{.State.Running}}' $container)
    [ "$status" == "true" ] \
        || { echo "docker container not running, you can execute \`$(basename $0) start $lang\` to start it" >&2; exit 2; }
}

case $subcmd in
    build)
        build_image
        ;;
    start)
        docker_start
        ;;
    stop)
        docker stop -t 1 $container
        ;;
    run-work)
        shift; shift
        run_work "$@"
        ;;
    invoke)
        shift; shift
        invoke_command "$@"
        ;;
    sync-file)
        file=$3
        [ -z "$file" ] && usage
        sync_file_to_container $file
        ;;
    clear-cache)
        clear_cache
        exit 0
        ;;
    force-purge)
        force_purge
        ;;
    drop-project)
        drop_project_in_container
        ;;
    show)
        key=$3
        [ -z $key ] && usage
        show_info $key
        ;;
    assert-running)
        assert_running
        ;;
    *)
        usage
        ;;
esac

