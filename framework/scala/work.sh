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
jarfile=work.jar

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
    local url="http://localhost:$server_port/soda/scala/work"
    curl --connect-timeout 2 -X POST -d "$post_content" -s "$url"
}

remote_setup()
{
    local classpath=$(cd $output_dir && pwd)
    local echo_url="http://localhost:$server_port/soda/scala/echo?a=b"
    curl --connect-timeout 2 -s "$echo_url" >/dev/null \
        || { echo "server not open" >&2; exit 2; }

    local setup_url="http://localhost:$server_port/soda/scala/setup"
    local pathkey=$(cd $output_dir && pwd)
    curl --connect-timeout 2 -s -f -X POST \
        -F "key=$pathkey" \
        -F "jar=@$output_dir/$jarfile" \
        "$setup_url" >/dev/null
}

function create_work()
{
    assert_testname
    source_file=${testname}.scala
    template_file=$self_dir/src/main/scala/soda/scala/unittest/__Bootstrap__.scala
    create_source_file $template_file $source_file
    classname=$testname
    echo "import soda.scala.unittest._" > ${classname}.tmp
    cat $source_file \
        | grep -v '^package ' \
        | sed "s/__Bootstrap__/$classname/g" \
        >> ${classname}.tmp
    mv ${classname}.tmp $source_file
}

function build_work()
{
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
        (cd $output_dir && jar cf $jarfile *.class)
    fi
}

function run_work()
{
    local run_mode=$1
    assert_testname
    classname=$testname
    if [ "$run_mode" == "--remote" ]; then
        remote_run $classname <&0
    else
        assert_framework
        scala -cp $(get_classpath):$output_dir $classname
    fi
}

case $cmd in
    new)
        create_work
        ;;
    source)
        echo ${testname}.scala
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
        remote_setup
        ;;
    *)
        usage
        ;;
esac

