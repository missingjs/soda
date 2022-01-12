#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    soda java <cmd> [options]

options:
    new <testname>
        create source file with name <testname>.java

    make <testname> 
        compile test case

    run <testname> [-server]
        run test case

    clean <testname>
        remove all class files under current directory

    server (start|stop|restart)
        server management
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

exec_test()
{
    local classname=$1
    [ -z $classname ] && usage
    if [ "$server_mode" == 'yes' ]; then
        set -e
        $self_dir/server.sh start
        runpath=$(pwd)
        curl -d "runpath=$runpath" "http://localhost:$server_port/soda/java/setup" && echo
        export SODA_JAVA_SERVER_MODE=yes
        set +e
    fi
    run_test java "$@"
}

case $cmd in
    new)
        assert_testname
        target_file=${testname}.java
        template_file=$self_dir/src/main/java/soda/unittest/__Bootstrap__.java
        create_source_file $template_file $target_file
        classname=$testname
        echo "import soda.unittest.*;" > ${classname}.tmp
        cat $target_file | grep -v '^package ' | sed "s/__Bootstrap__/$classname/g" >> ${classname}.tmp
        mv ${classname}.tmp $target_file
        ;;
    make)
        assert_testname
        srcfile=${testname}.java
        classfile=${testname}.class
        if [[ ! -e $classfile ]] || [[ $srcfile -nt $classfile ]]; then
            assert_framework
            echo "Compiling $srcfile ..."
            javac -cp $(get_classpath) $SODA_JAVA_COMPILE_OPTION $srcfile && echo "Compile $srcfile OK"
        fi
        ;;
    run)
        assert_testname
        classname=$testname
        run_mode=$3
        if [ "$run_mode" == "-server" ]; then
            runpath=$(pwd)
            curl --connect-timeout 2 -s "http://localhost:$server_port/soda/java/echo?a=b" >/dev/null || { echo "Unable to detect server" >&2; exit 2; }
            curl -d "runpath=$runpath" -s "http://localhost:$server_port/soda/java/setup" >/dev/null && echo
            url="http://localhost:$server_port/soda/java/job"
            post_content=$(python3 -c "import json; import sys; content = sys.stdin.read(); print(json.dumps({'runpath':'$runpath', 'jobclass':'$classname', 'request':content}))")
            curl --connect-timeout 2 -d "$post_content" -s $url
        else
            assert_framework
            java -cp $(get_classpath) $classname
        fi
        ;;
    clean)
        assert_testname
        classfile=${testname}.class
        [ -e $classfile ] && rm -v *.class
        ;;
    server)
        operation=$2
        if [ "$operation" == "start" ]; then
            $self_dir/server.sh start
        elif [ "$operation" == "stop" ]; then
            $self_dir/server.sh stop
        elif [ "$operation" == "restart" ]; then
            $self_dir/server.sh stop
            $self_dir/server.sh start
        fi
        ;;
    *)
        usage
        ;;
esac

