#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)

[ -e $self_dir/setup_env.sh ] || cp $self_dir/_setup_env.sh $self_dir/setup_env.sh
source $self_dir/setup_env.sh || exit

lib_dir=$self_dir/lib

[ -d $lib_dir ] || { mkdir -p $lib_dir || exit; }
rm -rfv lib/* 2>/dev/null

set -e

mods="leetcode unittest"

for mod in $mods; do
    cd $self_dir/src/soda/$mod
    make clean && make || exit
    cd -
    cp $self_dir/src/soda/$mod/lib*.so $lib_dir/
done

