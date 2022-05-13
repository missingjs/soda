#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    $cmd start   start server foreground

    $cmd port    show server port
EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
source $self_dir/setup_env.sh || exit

server_class="soda.groovy.web.SodaServer"

[ -z "$server_port" ] && { echo "server port not configured"; exit 2; }

cmd=$1
[ -z $cmd ] && usage

start_server_fg()
{
    classpath=$(get_classpath)
    local tmp_file=$(tempfile)
    echo "$server_class.main(args)" >$tmp_file
    trap "rm $tmp_file" INT
    set -x
    groovy -cp $classpath $tmp_file -p $server_port 
    rm $tmp_file
}

case $cmd in
    start)
        start_server_fg
        ;;
    port)
        echo $server_port
        ;;
    *)
        usage
        ;;
esac

