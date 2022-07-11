#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit

source_setup_env $self_dir

lib_dir=$self_dir/lib

subcmd=$1

cd $self_dir

# prepare directories
for dir in build $lib_dir; do
    [ -d $dir ] || { mkdir -p $dir || exit; }
    rm -rfv $dir/* 2>/dev/null
done

set -e

cmake -H. -B build

cd build && cmake --build . && cd - >/dev/null

cp build/libsoda-cpp.so $lib_dir/

