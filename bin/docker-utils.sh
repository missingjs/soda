#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat << EOF
usage: 
    $cmd start <lang>
    $cmd stop  <lang>
    $cmd play  <lang> <command> [args...]

    $cmd sync-dir <lang>       map current directory to docker container by sshfs

    $cmd force-purge <lang>    stop and remove
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

        echo "initialize ssh keys"
        docker container exec --user $user_name $container \
            bash -c "
                [ -e ~/.ssh ] || mkdir ~/.ssh
                cd ~/.ssh
                echo 'StrictHostKeyChecking no' >> config
                echo 'UserKnownHostsFile /dev/null' >> config
            "

        # setup ssh key
        pub_key=$($loadconf public_key)
        priv_key=$($loadconf private_key)
        pk=$(cat $pub_key)
        auth_file=~/.ssh/authorized_keys
        [ -e $auth_file ] || { touch $auth_file && chmod 600 $auth_file; }
        grep -q "$pk" $auth_file || echo "$pk" >> $auth_file
        cat $priv_key \
            | docker exec -i --user $user_name $container \
                bash -c "cat > ~/.ssh/id_rsa && chmod 600 ~/.ssh/id_rsa"

        echo "docker container $container created"
        set +e
    fi

    # check if container running
    if ! docker container ls | grep -q $container; then
        echo "start container $container"
        docker container start $container || exit
    fi
}

sync_current_dir()
{
    local ip=$(ifconfig | grep -A 1 docker0 | grep inet | awk '{print $2}')
    local cur_dir="$(pwd)"
    docker container exec --user $(id -un) $container bash -c "
        [ -e /task$cur_dir ] || mkdir -p /task$cur_dir
        [ -e /task$cur_dir/soda.prj.yml ] || sshfs $(id -un)@$ip:$cur_dir /task$cur_dir
    "
}

force_purge()
{
    echo "stop container $container ..."
    docker stop -t 1 $container
    echo "----"
    echo "remote container $container ..."
    docker container rm $container
}

case $subcmd in
    start)
        docker_start
        ;;
    stop)
        docker stop -t 1 $container
        ;;
    play)
        shift; shift;
        docker exec -i --user $(id -un) -w /task$(pwd) $container "$@"
        ;;
    sync-dir)
        sync_current_dir
        ;;
    force-purge)
        force_purge
        ;;
    *)
        usage
        ;;
esac
