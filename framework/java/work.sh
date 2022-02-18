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
output_dir=./java

remote_run()
{
    # $input must be in valid json format
    local input="$(</dev/stdin)"
    local classname=$1
    local classpath=$(cd $output_dir && pwd)
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
    local classpath=$(cd $output_dir && pwd)
    local echo_url="http://localhost:$server_port/soda/java/echo?a=b"
    curl --connect-timeout 2 -s "$echo_url" >/dev/null || { echo "server not open" >&2; exit 2; }
    local url="http://localhost:$server_port/soda/java/reset"
    curl --connect-timeout 2 -X POST -d "classpath=$classpath" -s "$url" && echo
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
        [ -e $output_dir ] || mkdir $output_dir
        classfile=$output_dir/${testname}.class
        if [[ ! -e $classfile ]] || [[ $srcfile -nt $classfile ]]; then
            assert_framework
            tmpdir=$(mktemp -d)
            perl -pe 's/GenericTestWork.create(\w+)\(new (.*)\(\)::(.*)\)/GenericTestWork.create\1(\2.class, "\3", new \2()::\3)/g' $srcfile > $tmpdir/$srcfile
            cp $tmpdir/$srcfile $output_dir/${testname}__gen.java
            echo "Compiling $srcfile ..."
            classpath=$(get_classpath)
            set -x
            javac -d $output_dir -cp $classpath $SODA_JAVA_COMPILE_OPTION $tmpdir/$srcfile
            set +x
            rm -rf $tmpdir
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
            java -cp $(get_classpath):$output_dir $classname
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
    remote-setup)
        remote_setup
        ;;
    *)
        usage
        ;;
esac

