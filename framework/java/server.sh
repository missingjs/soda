#!/bin/bash

usage()
{
    cat>&2 << EOF
usage:
    server.sh start|stop
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

start_server()
{
    old_path=$(pwd)
    cd $work_dir
    logfile=./soda-server.log
    errfile=./soda-server.err
    nohup java -cp $(get_classpath) $server_class $port >>$logfile 2>&1 &
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
    curl --connect-timeout 2 "$prefix/soda/java/echo?a=x" >/dev/null 2>&1
}

case $cmd in
    start)
        test_server || { start_server || exit; }
        ;;
    stop)
        curl --connect-timeout 2 "$prefix/soda/java/stop" && echo
        ;;
    start-fg)
        start_server_fg
        ;;
    *)
        usage
        ;;
esac

