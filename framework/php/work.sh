#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    soda python [options]

options:
    new <testname>
        create source file with name <testname>.php

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
execfile=${testname}.php

case $cmd in
    new)
        template_file=$self_dir/src/Soda/Unittest/bootstrap.php
        create_source_file $template_file $execfile
        ;;
    make | clean)
        # Don't remove. Just for interface compatible
        ;;
    run)
        include_path=.:$self_dir/src
        php -d include_path=$include_path $execfile
        ;;
    *)
        usage
        ;;
esac

