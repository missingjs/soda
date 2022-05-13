#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat << EOF
usage: 
    $cmd start <lang>
    $cmd stop  <lang>
    $cmd play  <lang> <command> [args...]

    $cmd exec  <lang> <command> [args...]

    $cmd sync-file <lang>      sync local file to container

    $cmd force-purge <lang>    stop and remove

    $cmd rm-proj <lang>        remove working dir of project

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

docker_start()
{
    local proxy_option=$($framework_dir/common/build-utils.sh run-proxy)

    # check if container exist
    if ! docker container ls --all | grep -q $container; then
        set -e
        echo "create docker container $container"
        docker run -d \
            --privileged \
            --name $container \
            --network host $proxy_option \
            -v $soda_dir:/soda \
            $docker_image bash -c "while true; do sleep 5; done"

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
                groupadd -g $group_id $group_name
                useradd -rm -d /home/$user_name -s /bin/bash -g $group_id -u $user_id $user_name
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
    if ! docker exec -w $workdir $container bash -c "[ -e $file ]"; then
        docker cp $file $container:$workdir/$file
    else
        t1=$(docker exec -w $workdir $container date -r $file "+%s")
        t2=$(date -r $file "+%s")
        if [ $t2 -gt $t1 ]; then
            docker cp $file $container:$workdir/$file
        fi
    fi
}

case $subcmd in
    start)
        docker_start
        ;;
    stop)
        docker stop -t 1 $container
        ;;
    play)
        proxy_option=$($framework_dir/common/build-utils.sh run-proxy)
        shift; shift;
        docker exec -i --user $(id -un) -w $workdir $proxy_option $container "$@"
        ;;
    exec)
        proxy_option=$($framework_dir/common/build-utils.sh run-proxy)
        shift; shift;
        docker exec -i --user $(id -un) $proxy_option $container "$@"
        ;;
    sync-file)
        file=$3
        [ -z "$file" ] && usage
        sync_file_to_container $file
        ;;
    force-purge)
        force_purge
        ;;
    rm-proj)
        docker exec $container rm -rfv $workdir
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
