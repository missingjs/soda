#!/bin/bash

# ****
# This file should only be sourced in other script
# ****

this_dir=$(cd -P $(dirname ${BASH_SOURCE[0]}) >/dev/null 2>&1 && pwd)
framework_dir=$(dirname $this_dir)

cur_dir=$(cd $(dirname $0) && pwd)

runtime_lib_dir=$cur_dir/rt-lib

function assert_library()
{
    local lang=$1
    [ -e $runtime_lib_dir ] && ls $runtime_lib_dir/*.jar >/dev/null 2>&1 \
        || { echo "$lang framework need to build. Please run make.sh to do it" >&2; exit 3; }
}

function get_classpath()
{
    ls $runtime_lib_dir/*.jar | tr '\n' ':'
}

function reset_lib_directory()
{
    [ -e $runtime_lib_dir ] || mkdir -p $runtime_lib_dir
    rm -rvf $runtime_lib_dir/* 2>/dev/null
}

