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

[ -e $self_dir/setup_env.sh ] || cp $self_dir/_setup_env.sh $self_dir/setup_env.sh
source $self_dir/setup_env.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
workclass=$(python3 -c "print('$testname'.capitalize())")
workclass="${workclass}Work"
output_dir=.
execfile=${testname}.groovy

remote_run()
{
    # $input must be in valid json format
    local input="$(</dev/stdin)"
    local classname="$1"
#    local script_file="$2"
    local pathkey=$(pwd)

local pycode=$(cat << EOF
import json
info = {
  "key": "$pathkey",
  "bootClass": "$classname",
  "testCase" : """$input"""
}
print(json.dumps(info))
EOF
)

#    local classpath=$(pwd)
#    pycode=$(cat << EOF
#import json; import sys;
#content = sys.stdin.read()
#info = {
#  "classpath": "$classpath",
#  "bootClass": "$classname",
#  "testCase" : content
#}
#print(json.dumps(info))
#EOF
#)
#    post_content=$(echo "$input" | python3 -c "$pycode")
    local post_content="$(python3 -c "$pycode")"
    local url="http://localhost:$server_port/soda/groovy/work"
    curl --connect-timeout 2 -X POST -d "$post_content" -s "$url"
}

remote_setup()
{
    local classpath=$(pwd)
    local echo_url="http://localhost:$server_port/soda/groovy/echo?a=b"
    curl --connect-timeout 2 -s "$echo_url" >/dev/null \
        || { echo "server not open" >&2; exit 2; }

    local pathkey=$(cd $output_dir && pwd)
    local boot_url="http://localhost:$server_port/soda/groovy/bootstrap"

    local_md5="$(md5sum $execfile | awk '{print $1}')"
    remote_md5="$(curl --connect-timeout 2 -s "${boot_url}?key=$pathkey&format=text")"

    if [ "$local_md5" != "$remote_md5" ]; then  
        curl --connect-timeout 2 -s -f -X POST \
            -F "key=$pathkey" \
            -F "script=@$execfile" \
            "$boot_url" >/dev/null
    fi

#    local setup_url="http://localhost:$server_port/soda/groovy/setup"
#    local pathkey=$(pwd)
#    curl --connect-timeout 2 -s -f -X POST \
#        -F "key=$pathkey" \
#        -F "script=@$execfile" \
#        "$setup_url" >/dev/null
}

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
    classpath=$(get_classpath)
    if [ "$run_mode" == "--remote" ]; then
        cur_dir=$(pwd)
#        remote_run $workclass "$cur_dir/$execfile" <&0
        remote_run $workclass <&0
    else
        assert_framework
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
        assert_testname
        remote_setup
        ;;
    *)
        usage
        ;;
esac

