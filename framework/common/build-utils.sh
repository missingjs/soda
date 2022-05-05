#!/bin/bash

usage()
{
    local cmd=$(basename $0) 
    cat << EOF
usage: $cmd <command> [options]

commands:
    proxy-arg         show proxy arguments activated by http_proxy
EOF
    exit 1
}

cmd=$1

[ -z $cmd ] && usage

show_proxy_args()
{
    local proxy_arg=
    if [ -n "$HTTP_PROXY" ]; then
        proxy_arg="$proxy_arg --build-arg HTTP_PROXY=$HTTP_PROXY"
        proxy_arg="$proxy_arg --build-arg HTTPS_PROXY=$HTTPS_PROXY"
        proxy_arg="$proxy_arg --build-arg NO_PROXY=$NO_PROXY"
        proxy_arg="$proxy_arg --build-arg http_proxy=$http_proxy"
        proxy_arg="$proxy_arg --build-arg https_proxy=$https_proxy"
        proxy_arg="$proxy_arg --build-arg no_proxy=$no_proxy"
    fi
    echo $proxy_arg
}

case $cmd in
    proxy-arg)
        show_proxy_args
        ;;
    *)
        usage
        ;;
esac
