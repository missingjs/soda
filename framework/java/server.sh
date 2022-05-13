#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    $cmd start [-d]
    $cmd stop | restart
EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)

source $self_dir/setup_env.sh || exit

work_dir=$self_dir/server
server_class="soda.web.SodaServer"
port=$server_port
prefix="http://localhost:$port"

cmd=$1
[ -z $cmd ] && usage

start_server_bg()
{
    old_path=$(pwd)
    cd $work_dir
    logfile=./soda-server.log
    classpath=$(get_classpath)

    # all log in java code written to stderr
    nohup java -cp $classpath $server_class -p $port >>$logfile 2>&1 &

    cd $old_path
    for i in 1 2 3 4 5; do
        sleep 1
        test_server && { echo "Server started at port $server_port"; return 0; }
    done
    echo "Failed to start server"
    return 1
}

start_server_fg()
{
    classpath=$(get_classpath)
    set -x
    java -cp $classpath $server_class -p $port
}

test_server()
{
    curl -s --connect-timeout 2 "$prefix/soda/java/echo?a=x"
}

function start_server()
{
    local bg=$1
    if [ "$bg" == '-d' ]; then
        test_server || start_server_bg
    else
        start_server_fg
    fi
}

function stop_server()
{
    curl -s --connect-timeout 2 "$prefix/soda/java/stop" && echo
}

case $cmd in
    start)
        shift
        start_server "$@"
        ;;
    stop)
        stop_server
        ;;
    restart)
        stop_server
        start_server
        ;;
    *)
        usage
        ;;
esac

