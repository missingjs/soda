#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat << EOF
usage: 
    $cmd start <lang>
    $cmd stop  <lang>
    $cmd play  <lang> <command> [args...]

    $cmd sync-dir <lang>    map current directory to docker container by sshfs
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

docker_start()
{
    local lang=$1
    local image="missingjs/soda-$lang"
    local cont_name="soda-task-$lang"
    local proxy_option=$($framework_dir/common/build-utils.sh run-proxy)

    # check if container exist
    if ! docker container ls --all | grep $cont_name >/dev/null; then
        set -e
        docker run -d \
            --privileged \
            --name $cont_name \
            --network host $proxy_option \
            -v $soda_dir:/soda \
            -w /task \
            $image bash -c "while true; do sleep 5; done"

        # initialize container
        docker container exec $cont_name \
            bash -c "
                [ -e /task ] || mkdir /task
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
        docker cp $priv_key $cont_name:/root/.ssh/id_rsa

        echo "docker container $cont_name created"
        set +e
    fi

    # check if container running
    docker container ls | grep $cont_name >/dev/null \
        || docker container start $cont_name \
        || exit
}

docker_stop()
{
    local lang=$1
    local cont_name="soda-task-$lang"
    docker container stop -t 1 $cont_name
}

docker_play()
{
    local lang=$1
    local cont_name="soda-task-$lang"
    shift
    docker container exec -t -w /task$(pwd) $cont_name "$@"
}

sync_current_dir()
{
    local lang=$1
    local cont_name="soda-task-$lang"
    local ip=$(ifconfig | grep -A 1 docker0 | grep inet | awk '{print $2}')
    local cur_dir="$(pwd)"
    docker container exec $cont_name bash -c "
        [ -e /task$cur_dir ] || mkdir -p /task$cur_dir
        [ -e /task$cur_dir/soda.prj.yml ] || sshfs $(id -un)@$ip:$cur_dir /task$cur_dir
    "
}

case $subcmd in
    start)
        docker_start $lang
        ;;
    stop)
        docker_stop $lang
        ;;
    play)
        shift; shift;
        docker_play $lang "$@"
        ;;
    sync-dir)
        sync_current_dir $lang
        ;;
    *)
        usage
        ;;
esac
