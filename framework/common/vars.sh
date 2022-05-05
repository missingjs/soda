this_dir=$(cd -P $(dirname ${BASH_SOURCE[0]}) >/dev/null 2>&1 && pwd)

proxy_arg=
if [ -n "$HTTP_PROXY" ]; then
    proxy_arg="$proxy_arg --build-arg HTTP_PROXY=$HTTP_PROXY"
    proxy_arg="$proxy_arg --build-arg HTTPS_PROXY=$HTTPS_PROXY"
    proxy_arg="$proxy_arg --build-arg NO_PROXY=$NO_PROXY"
    proxy_arg="$proxy_arg --build-arg http_proxy=$http_proxy"
    proxy_arg="$proxy_arg --build-arg https_proxy=$https_proxy"
    proxy_arg="$proxy_arg --build-arg no_proxy=$no_proxy"
fi
