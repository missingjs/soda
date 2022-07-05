#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    $cmd <command> [options]

options:
    new <testname>
        create source file with name <testname>.groovy

    source <testname>
        show source file name

    make <testname> 
        do nothing

    run <testname>
        run test case

    clean <testname>
        do nothing

    remote-setup <testname>
        reset remote server, drop old work class

EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit
source $framework_dir/common/jvm-like.sh || exit

[ -e $self_dir/setup_env.sh ] || cp $self_dir/_setup_env.sh $self_dir/setup_env.sh
source $self_dir/setup_env.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
workclass=$(python3 -c "print('$testname'.capitalize())")
workclass="${workclass}Work"
output_dir=.
execfile=${testname}.groovy

function create_work()
{
    assert_testname
    template_file=$self_dir/src/main/groovy/bootstrap.groovy
    create_source_file $template_file $execfile
    classname=$workclass
    cat $execfile | sed "s/__Bootstrap__/$classname/g" > $classname.tmp
    mv $classname.tmp $execfile
}

function run_work()
{
    local run_mode=$1
    assert_testname
    if [ "$run_mode" == "--remote" ]; then
        cur_dir=$(pwd)
        local key=$(cd $output_dir && pwd)
        remote_run groovy "$key" "$workclass" -
    else
        assert_library groovy
        local classpath=$(get_classpath)
        groovy -cp $classpath $execfile
    fi
}

case $cmd in
    new)
        create_work
        ;;
    source)
        echo $execfile
        ;;
    make | clean)
        # Don't remove. Just for interface compatible
        ;;
    run)
        shift; shift
        run_work "$@"
        ;;
    remote-setup)
        runkey=$(cd $output_dir && pwd)
        remote_setup groovy "$runkey" "$execfile"
        ;;
    *)
        usage
        ;;
esac

