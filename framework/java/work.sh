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

    run <testname> [--remote]
        run test case

    clean <testname>
        remove all class files under current directory

    server (start|stop|restart)
        server management

    server start --fg
        start server foreground

    remote-setup
        reset remote server, drop old work class

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

remote_run()
{
    # $input must be in valid json format
    local input="$(</dev/stdin)"
    local classname=$1
    local classpath=$(pwd)
    pycode=$(cat << EOF
import json; import sys;
content = sys.stdin.read()
info = {
  "classpath": "$classpath",
  "bootClass": "$classname",
  "testCase" : content
}
print(json.dumps(info))
EOF
)
    post_content=$(echo "$input" | python3 -c "$pycode")
    local url="http://localhost:$server_port/soda/java/work"
    curl --connect-timeout 2 -X POST -d "$post_content" -s "$url"
}

remote_setup()
{
    local classpath=$(pwd)
    local echo_url="http://localhost:$server_port/soda/java/echo?a=b"
    curl --connect-timeout 2 -s "$echo_url" >/dev/null || { echo "server not open" >&2; exit 2; }
    local url="http://localhost:$server_port/soda/java/reset"
    curl --connect-timeout 2 -X POST -d "classpath=$classpath" -s "$url" && echo
}

#exec_test()
#{
#    local classname=$1
#    [ -z $classname ] && usage
#    if [ "$server_mode" == 'yes' ]; then
#        set -e
#        $self_dir/server.sh start
#        runpath=$(pwd)
#        curl -d "runpath=$runpath" "http://localhost:$server_port/soda/java/setup" && echo
#        export SODA_JAVA_SERVER_MODE=yes
#        set +e
#    fi
#    run_test java "$@"
#}

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
            classpath=$(get_classpath)
            set -x
            javac -cp $classpath $SODA_JAVA_COMPILE_OPTION $srcfile
            set +x
            echo "Compile $srcfile OK"
        fi
        ;;
    run)
        assert_testname
        classname=$testname
        run_mode=$3
        if [ "$run_mode" == "--remote" ]; then
            remote_run $classname <&0
#            runpath=$(pwd)
#            curl --connect-timeout 2 -s "http://localhost:$server_port/soda/java/echo?a=b" >/dev/null || { echo "Unable to detect server" >&2; exit 2; }
#            curl -d "runpath=$runpath" -s "http://localhost:$server_port/soda/java/setup" >/dev/null && echo
#            url="http://localhost:$server_port/soda/java/job"
#            post_content=$(python3 -c "import json; import sys; content = sys.stdin.read(); print(json.dumps({'runpath':'$runpath', 'jobclass':'$classname', 'request':content}))")
#            curl --connect-timeout 2 -d "$post_content" -s $url
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
        cmd=$self_dir/server.sh
        if [ "$operation" == "start" ]; then
            fore=$3
            [ "$fore" == "--fg" ] && { $cmd start-fg; exit; }
            $cmd start
        elif [ "$operation" == "stop" ]; then
            $cmd stop
        elif [ "$operation" == "restart" ]; then
            $cmd stop
            $cmd start
        fi
        ;;
    remote-setup)
        remote_setup
        ;;
    *)
        usage
        ;;
esac

