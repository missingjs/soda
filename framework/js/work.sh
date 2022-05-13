#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    soda ruby [options]

options:
    new <testname>
        create source file with name <testname>.js

    source <testname>
        show source file name

    make <testname> 
        do nothing

    run <testname>
        run test case

    clean <testname>
        do nothing

EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
execfile=${testname}.js

[ -z $testname ] && usage
case $cmd in
    new)
        template_file=$self_dir/src/soda/unittest/bootstrap.js
        create_source_file $template_file $execfile
        ;;
    source)
        echo $execfile
        ;;
    make | clean)
        # Don't remove. Just for interface compatible
        ;;
    run)
        src_dir=$self_dir/src
        export NODE_PATH=$src_dir:/app/node_modules:$NODE_PATH
        node $execfile
        ;;
    *)
        usage
        ;;
esac

