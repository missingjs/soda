#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    soda groovy [options]

options:
    new <testname>
        create source file with name <testname>.groovy

    make <testname> 
        do nothing

    run <testname>
        run test case

    clean <testname>
        do nothing

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
execfile=${testname}.groovy

remote_run()
{
    # $input must be in valid json format
    local input="$(</dev/stdin)"
    local classname="$1"
    local script_file="$2"
    local classpath=$(cd $output_dir && pwd)
    pycode=$(cat << EOF
import json; import sys;
content = sys.stdin.read()
info = {
  "scriptFile": "$script_file",
  "classpath": "$classpath",
  "bootClass": "$classname",
  "testCase" : content
}
print(json.dumps(info))
EOF
)
    post_content=$(echo "$input" | python3 -c "$pycode")
    local url="http://localhost:$server_port/soda/groovy/work"
    curl --connect-timeout 2 -X POST -d "$post_content" -s "$url"
}

remote_setup()
{
    local classpath=$(cd $output_dir && pwd)
    local echo_url="http://localhost:$server_port/soda/groovy/echo?a=b"
    curl --connect-timeout 2 -s "$echo_url" >/dev/null || { echo "server not open" >&2; exit 2; }
    local url="http://localhost:$server_port/soda/groovy/reset"
    curl --connect-timeout 2 -X POST -d "classpath=$classpath" -s "$url" && echo
}

case $cmd in
    new)
        template_file=$self_dir/src/main/groovy/bootstrap.groovy
        create_source_file $template_file $execfile
        ;;
    make | clean)
        # Don't remove. Just for interface compatible
        ;;
    run)
        assert_testname
        assert_framework
        classpath=$(get_classpath)
        groovy -cp $classpath $execfile
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

