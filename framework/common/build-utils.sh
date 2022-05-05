#!/bin/bash

usage()
{
    local cmd=$(basename $0) 
    cat << EOF
usage: $cmd <command> [options]

commands:
    proxy-arg|build-proxy     show proxy arguments in 'docker build', activated by http_proxy
    run-proxy                 show proxy arguments in 'docker run', activated by http_proxy
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

docker_run_proxy_option()
{
    local proxy_opt=
    if [ -n "$HTTP_PROXY" ]; then
        proxy_opt="$proxy_opt --env HTTP_PROXY=$HTTP_PROXY"
        proxy_opt="$proxy_opt --env HTTPS_PROXY=$HTTPS_PROXY"
        proxy_opt="$proxy_opt --env NO_PROXY=$NO_PROXY"
        proxy_opt="$proxy_opt --env http_proxy=$http_proxy"
        proxy_opt="$proxy_opt --env https_proxy=$https_proxy"
        proxy_opt="$proxy_opt --env no_proxy=$no_proxy"
    fi
    echo $proxy_opt
}

case $cmd in
    proxy-arg | build-proxy)
        show_proxy_args
        ;;
    run-proxy)
        docker_run_proxy_option
        ;;
    *)
        usage
        ;;
esac
