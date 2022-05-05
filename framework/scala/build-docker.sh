#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
common_dir=$framework_dir/common

source $common_dir/vars.sh || exit

image_name=soda-scala

set -x

docker build \
    --network host \
    $proxy_arg \
    -t ${image_name}-part . || exit

$common_dir/make-image.sh $image_name

