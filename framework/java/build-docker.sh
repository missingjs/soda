#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
common_dir=$framework_dir/common

$common_dir/build-image.sh Dockerfile-template soda-java

