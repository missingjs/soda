#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
common_dir=$framework_dir/common

image_name=soda-csharp

docker build -t ${image_name}-part . || exit

$common_dir/make-image.sh $image_name

