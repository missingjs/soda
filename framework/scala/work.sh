#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    $cmd <command> [options]

options:
    new <testname>
        create source file with name <testname>.scala

    source <testname>
        show source file name

    make <testname> 
        compile test case
        
    run <testname> [--remote]
        run test case

    clean <testname>
        remove all class files in class output directory (./scala)

    remote-setup
        reset remote server, drop old work class

EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit
source $framework_dir/common/jvm-like.sh || exit

source_setup_env $self_dir

cmd=$1
[ -z $cmd ] && usage

testname=$2
testname=$(python3 -c "print('$testname'.capitalize())")
srcfile=${testname}.scala
output_dir=./scala
jarfile=work.jar

function create_work()
{
    assert_testname
    target_file=$srcfile
    template_file=$self_dir/src/main/scala/soda/scala/unittest/__Bootstrap__.scala
    create_source_file $template_file $target_file
    classname=$testname
    echo "import soda.scala.unittest._" > ${classname}.tmp
    cat $target_file \
        | grep -v '^package ' \
        | sed "s/__Bootstrap__/$classname/g" \
        >> ${classname}.tmp
    mv ${classname}.tmp $target_file
}

function build_work()
{
    assert_testname
    [ -e $output_dir ] || mkdir $output_dir
    classfile=$output_dir/${testname}.class
    if [[ ! -e $classfile ]] || [[ $srcfile -nt $classfile ]]; then
        assert_library scala
        echo "Compiling $srcfile ..."
        classpath=$(get_classpath)
        set -x
        scalac -d $output_dir $SODA_SCALA_COMPILE_OPTION -cp $classpath $srcfile || exit
        set +x
        echo "Compile $srcfile OK"
        (cd $output_dir && jar cf $jarfile *.class)
    fi
}

function run_work()
{
    local run_mode=$1
    local classname=$testname

    assert_testname
    if [ "$run_mode" == "--remote" ]; then
        local key=$(cd $output_dir && pwd)
        remote_run scala "$key" "$classname" -
    else
        assert_library scala
        scala -cp $(get_classpath):$output_dir $classname
    fi
}

case $cmd in
    new)
        create_work
        ;;
    source)
        echo $srcfile
        ;;
    make)
        build_work
        ;;
    run)
        shift; shift
        run_work "$@"
        ;;
    clean)
        assert_testname
        [ -e $output_dir ] && rm -rfv $output_dir || true
        ;;
    remote-setup)
        runkey=$(cd $output_dir && pwd)
        remote_setup scala "$runkey" "$output_dir/$jarfile"
        ;;
    *)
        usage
        ;;
esac

