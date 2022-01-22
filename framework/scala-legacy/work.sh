#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat << EOF
usage:
    soda scala [-s] [options]

    -s: use server mode

options:
    new <testname>
        create source file with name <testname>.scala

    compile <testname> 
        compile test case
    $command_run_help

    go <testname> [options] 
        compile && run, options same as command 'run'

    exec <classname> [options]
        run executable file, options same as command 'run'

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

server_mode=no
if [ "$cmd" == "-s" -o "$cmd" == '-r' ]; then
    server_mode=yes
    [ "$cmd" == "-r" ] && $self_dir/server.sh stop
    shift
    cmd=$1
fi

exec_test()
{
    local classname=$1
    [ -z $classname ] && usage
    if [ "$server_mode" == 'yes' ]; then
        set -e
        $self_dir/server.sh start
        runpath=$(pwd)
        curl -d "runpath=$runpath" "http://localhost:$server_port/soda/java/setup" && echo
        export SODA_SCALA_SERVER_MODE=yes
        set +e
    fi
    run_test scala "$@"
}

do_compile()
{
    testname=$1
    [ -z $testname ] && usage
    srcfile=${testname}.scala
    assert_framework
    echo "Compiling $srcfile ..."
    scalac -deprecation -cp $(get_classpath) $srcfile && echo "Compile $srcfile OK"
}

case $cmd in
    new)
        testname=$2
        [ -z $testname ] && usage
        testname=${testname%.scala}
        target_file=${testname}.scala
        template_file=$self_dir/src/main/scala/soda/scala/unittest/__Bootstrap__.scala
        create_source_file $template_file $target_file
        classname=$testname
        # echo "import soda.unittest._;" > ${classname}.tmp
        cat $target_file | grep -v '^package ' | sed "s/__Bootstrap__/$classname/g" >> ${classname}.tmp
        mv ${classname}.tmp $target_file
        ;;
    compile)
        testname=$2
        do_compile $testname
        ;;
    run)
        shift
        exec_test "$@"
        ;;
    go)
        testname=$2
        do_compile $testname || exit
        shift
        exec_test "$@"
        ;;
    exec)
        shift
        exec_test "$@"
        ;;
    *)
        usage
        ;;
esac

