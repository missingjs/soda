#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat << EOF
usage: 
    $cmd start <lang>
    $cmd stop  <lang>

    $cmd play  <lang> <command> [args...]
        just for framework/{lang}/work.sh

    $cmd exec  <lang> [-w <dir>] <command> [args...]
        for common commands

    $cmd sync-file <lang>      sync local file to container

    $cmd force-purge <lang>    stop and remove

    $cmd drop-project <lang>   remove whole content of project in container

    $cmd show <lang> <container|workdir>
EOF
    exit 1
}

subcmd=$1
lang=$2

[ -z "$subcmd" -o -z "$lang" ] && usage

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(dirname $self_dir)
framework_dir=$soda_dir/framework
loadconf=$self_dir/support/loadconf.sh

docker_image="missingjs/soda-$lang"
container="soda-task-$lang"
workdir=/task$(pwd)
proxy_option=$($framework_dir/common/build-utils.sh run-proxy)

docker_start()
{
    # check if container exist
    if ! docker container ls --all | grep -q $container; then
        set -e
        echo "create docker container $container"
        docker run -d \
            --privileged \
            --name $container \
            --network host $proxy_option \
            -v $soda_dir:/soda \
            $docker_image tail -f /dev/null

        # initialize container
        user_id=$(id -u)
        user_name=$(id -un)
        group_id=$(id -g)
        group_name=$(id -gn)
        echo "add user $user_name"
        docker container exec $container \
            bash -c "
                [ -e /task ] || mkdir /task
                chmod 777 /task
                groupadd -g $group_id $group_name || true
                useradd -rm -d /home/$user_name -s /bin/bash -g $group_id -u $user_id $user_name || true
            "

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
    echo "remote container $container ..."
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
        *)
            usage
            ;;
    esac
}

sync_file_to_container()
{
    local file=$1
    [ -e $file ] || { echo "file not exist: $file"; exit 2; }

    local host_file_mod_time=$(date -r $file "+%s")
    local up_to_date="
        [ -e $workdir ] || mkdir -p $workdir
        cd $workdir
        [ -e $file ] || exit 1
        guest_file_mod_time=\$(date -r $file '+%s')
        [ $host_file_mod_time -gt \$guest_file_mod_time ] && exit 1
        exit 0
    "
    docker exec -u $(id -u) $container bash -c "$up_to_date" \
        || { set -x; docker cp $file $container:$workdir/$file; }
}

function exec_command()
{
    if [ "$1" == '-w' ]; then
        local dir=$2
        shift; shift
        docker exec -i -u $(id -u) -w $dir $proxy_option $container "$@"
    else
        docker exec -i -u $(id -u) $proxy_option $container "$@"
    fi
}

function drop_project_in_container()
{
    [ -e soda.prj.yml ] && docker exec $container rm -rfv $workdir
}

case $subcmd in
    start)
        docker_start
        ;;
    stop)
        docker stop -t 1 $container
        ;;
    play)
        shift; shift
        exec_command -w $workdir /soda/framework/$lang/work.sh "$@"
        ;;
    exec)
        shift; shift
        exec_command "$@"
        ;;
    sync-file)
        file=$3
        [ -z "$file" ] && usage
        sync_file_to_container $file
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
    *)
        usage
        ;;
esac

