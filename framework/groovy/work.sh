#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    soda groovy [options]

options:
    new <testname>
        create source file with name <testname>.groovy

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

[ -e $self_dir/setup_env.sh ] || cp $self_dir/_setup_env.sh $self_dir/setup_env.sh
source $self_dir/setup_env.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
execfile=${testname}.groovy

case $cmd in
    new)
        template_file=$self_dir/src/main/groovy/bootstrap.groovy
        create_source_file $template_file $execfile
        ;;
    make | clean)
        # Don't remove. Just for interface compatible
        ;;
    run)
        assert_testname
        assert_framework
        classpath=$(get_classpath)
        groovy -cp $classpath $execfile
        ;;
    *)
        usage
        ;;
esac

