#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    $cmd <command> [options]

options:
    new <testname>
        create source file with name <testname>.kt

    source <testname>
        show source file name

    make <testname> 
        compile test case
        
    run <testname> [--remote]
        run test case

    clean <testname>
        remove all class files in class output directory (./kotlin)

    remote-setup
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
testname=$(python3 -c "print('$testname'.capitalize())")
output_dir=./kotlin
srcfile=${testname}.kt
jarfile=${testname}.jar

function create_work()
{
    assert_testname
    local template_file="$self_dir/src/main/kotlin/bootstrap.kt"
    set -e
    create_source_file $template_file $srcfile
    sed -i "s/__Bootstrap__/$testname/g" $srcfile
    set +e
}

function build_work()
{
    assert_testname
    [ -e $output_dir ] || mkdir $output_dir
    local jar="$output_dir/$jarfile"
    if [[ ! -e $jar ]] || [[ $srcfile -nt $jar ]]; then
        assert_library kotlin
        echo "Compiling $srcfile ..."
        classpath=$(get_classpath)
        set -ex
        kotlinc -jvm-target 11 -d $jar -cp $classpath $srcfile
        set +ex
        echo "Compile $srcfile OK"
    fi
}

function run_work()
{
    local run_mode="$1"

    assert_testname
    if [ "$run_mode" == "--remote" ]; then
        local classname=$testname
        local key=$(cd $output_dir && pwd)
        remote_run kotlin "$key" "$classname" -
    else
        local classname="${testname}Kt"
        assert_library kotlin
        java -cp $(get_classpath):$output_dir/$jarfile $classname
    fi
}

case $cmd in
    new)
        create_work
        ;;
    source)
        echo ${testname}.kt
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
        remote_setup kotlin "$runkey" "$output_dir/$jarfile"
        ;;
    *)
        usage
        ;;
esac

