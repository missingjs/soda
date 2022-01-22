#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat << EOF
usage:
    soda scala <cmd> [options]

options:
    new <testname>
        create source file with name <testname>.scala

    make <testname> 
        compile test case

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
assert_testname() {
    [ -z $testname ] && usage
}
testname=$(python3 -c "print('$testname'.capitalize())")
output_dir=./scala

case $cmd in
    new)
        assert_testname
        source_file=${testname}.scala
        template_file=$self_dir/src/main/scala/soda/scala/unittest/__Bootstrap__.scala
        create_source_file $template_file $source_file
        classname=$testname
        echo "import soda.scala.unittest._" > ${classname}.tmp
        cat $source_file | grep -v '^package ' | sed "s/__Bootstrap__/$classname/g" >> ${classname}.tmp
        mv ${classname}.tmp $source_file
        ;;
    make)
        assert_testname
        srcfile=${testname}.scala
        [ -e $output_dir ] || mkdir $output_dir
        classfile=$output_dir/${testname}.class
        if [[ ! -e $classfile ]] || [[ $srcfile -nt $classfile ]]; then
            assert_framework
            echo "Compiling $srcfile ..."
            scalac -d $output_dir -deprecation -cp $(get_classpath) $srcfile && echo "Compile $srcfile OK"
        fi
        ;;
    run)
        assert_testname
        classname=$testname
        assert_framework
        scala -cp $(get_classpath):$output_dir $classname
        ;;
    *)
        usage
        ;;
esac

