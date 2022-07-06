#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit

source_setup_env $self_dir

lib_dir=$self_dir/lib

subcmd=$1

[ -d $lib_dir ] || { mkdir -p $lib_dir || exit; }
rm -rfv lib/* 2>/dev/null

set -e

mods="leetcode unittest"

for mod in $mods; do
    cd $self_dir/src/soda/$mod
    case "$subcmd" in
        clean)
            make clean
            ;;
        *)
            make clean && make || exit
            cp $self_dir/src/soda/$mod/lib*.so $lib_dir/
            ;;
    esac
    cd - >/dev/null
done

