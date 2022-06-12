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

[ -e $self_dir/setup_env.sh ] || cp $self_dir/_setup_env.sh $self_dir/setup_env.sh
source $self_dir/setup_env.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
testname=$(python3 -c "print('$testname'.capitalize())")
output_dir=./kotlin
srcfile=${testname}.kt
jarfile=${testname}.jar

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
    local url="http://localhost:$server_port/soda/kotlin/work"
    curl --connect-timeout 2 -X POST -d "$post_content" -s "$url"
}

remote_setup()
{
    local classpath=$(cd $output_dir && pwd)
    local echo_url="http://localhost:$server_port/soda/kotlin/echo?a=b"
    curl --connect-timeout 2 -s "$echo_url" >/dev/null \
        || { echo "server not open" >&2; exit 2; }
    local url="http://localhost:$server_port/soda/kotlin/setup"
    jar_b64=$(python3 << EOF
import base64
with open("$output_dir/$jarfile", "rb") as fp:
    b64 = base64.urlsafe_b64encode(fp.read()).decode('utf-8')
    print(b64)
EOF
)
    curl --connect-timeout 2 -X POST -d "key=$classpath&jar=$jar_b64" -s "$url" && echo
}

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
        assert_framework
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
    assert_testname

    local run_mode="$1"
    if [ "$run_mode" == "--remote" ]; then
        local classname=$testname
        remote_run $classname <&0
    else
        local classname="${testname}Kt"
        assert_framework
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
        remote_setup
        ;;
    *)
        usage
        ;;
esac

