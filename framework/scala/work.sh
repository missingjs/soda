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
        
    run <testname> [--remote]
        run test case

    clean <testname>
        remove all class files in class output directory (./scala)

    server (start|stop|restart)
        server management

    server start --fg
        start server foreground

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

remote_run()
{
    # $input must be in valid json format
    local input="$(</dev/stdin)"
    local classname=$1
    local classpath=$(cd $output_dir && pwd)
    local url="http://localhost:$server_port/soda/scala"
    curl --connect-timeout 2 -s "$url/echo?a=b" >/dev/null || { echo "server not open" >&2; exit 2; }
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
    curl --connect-timeout 2 -X POST -d "$post_content" -s "$url/work"
}

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
            classpath=$(get_classpath)
            set -x
            scalac -d $output_dir $SODA_SCALA_COMPILE_OPTION -cp $classpath $srcfile || exit
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
        else
            assert_framework
            scala -cp $(get_classpath):$output_dir $classname
        fi
        ;;
    clean)
        assert_testname
        [ -e $output_dir ] && rm -v -r $output_dir
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
    *)
        usage
        ;;
esac

