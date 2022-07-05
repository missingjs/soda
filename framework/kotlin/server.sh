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
framework_dir=$(dirname $self_dir)

source $framework_dir/common/jvm-like.sh || exit
source $self_dir/setup_env.sh || exit

server_class="soda.kotlin.web.SodaServerKt"

cmd=$1
[ -z $cmd ] && usage

start_server_fg()
{
    classpath=$(get_classpath)
    set -x
    java -cp $classpath $server_class -p $server_port
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

