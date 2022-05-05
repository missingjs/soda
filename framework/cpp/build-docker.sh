#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
build_cmd=$(realpath $self_dir/../common/2phase-build.sh)

cd $self_dir
$build_cmd soda-cpp-base soda-cpp
