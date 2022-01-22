#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat << EOF
usage:
    soda scala [options]

options:
    new <testname>
        create source file with name <testname>.scala

EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
assert_testname() {
    [ -z $testname ] && usage
}
testname=$(python3 -c "print('$testname'.capitalize())")

case $cmd in
    new)
        assert_testname
        source_file=${testname}.scala
        template_file=$self_dir/src/main/scala/soda/scala/unittest/__Bootstrap__.scala
        create_source_file $template_file $source_file
        classname=$testname
        cat $source_file | grep -v '^package ' | sed "s/__Bootstrap__/$classname/g" >> ${classname}.tmp
        mv ${classname}.tmp $source_file
        ;;
    *)
        usage
        ;;
esac

