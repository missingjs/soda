#!/bin/bash

# ****
# This file should only be sourced in other script
# ****

this_dir=$(cd -P $(dirname ${BASH_SOURCE[0]}) >/dev/null 2>&1 && pwd)
soda_dir=$(realpath $this_dir/../..)
framework_dir=$soda_dir/framework
dku=$soda_dir/bin/docker-utils.sh

export SODA_FRAMEWORK_DIR=$framework_dir

command_run_help=\
"    run <testname> [--testcase <files> [--delim <DELIM>]] [--verbose] [--timeout <SEC>]
        run test case
        
        --testcase <files>    test case files, separated by --delim, default is 'test_data'
        --delim <DELIM>       delimeter of file list, default ','
        --timeout <SEC>       timeout in seconds, use decimal
        --verbose             show test request & response"

create_source_file()
{
    local template_file=$1
    local target_file=$2
    [ -z $target_file ] && usage
    [ -e $target_file ] && { echo "Error: $target_file exists" >&2; exit 2; }
    cat $template_file > $target_file && echo "$target_file OK"
}

run_test()
{
    export PYTHONPATH="$framework_dir/python/src:$PYTHONPATH"
    runner=soda.unittest.tester
    python3 -m $runner "$@"
}

assert_testname()
{
    [ -z $testname ] && usage
}

function check_sdk()
{
    local lang=$1
    local sdk_dir=$framework_dir/$lang
    [ -e $sdk_dir/work.sh -a -e $sdk_dir/build-docker.sh ] \
        || { echo "language '$lang' not supported"; exit 3; }
}

function trans_lang_name()
{
    local name=$1
    case $name in
        cs)
            name=csharp
            ;;
        py)
            name=python
            ;;
        *)
            ;;
    esac
    echo $name
}

function nano_time()
{
    date +%s%N
}

function ms_since()
{
    local start_nano=$1
    local end_nano=$(nano_time)
    echo $((($end_nano - $start_nano) / 1000000))
}

function report_elapse_ms()
{
    local start_nano=$1
    echo "time elapse: $(ms_since $start_nano) ms"
}

