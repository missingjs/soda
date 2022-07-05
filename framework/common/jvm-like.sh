#!/bin/bash

# ****
# This file should only be sourced in other script
# ****

this_dir=$(cd -P $(dirname ${BASH_SOURCE[0]}) >/dev/null 2>&1 && pwd)

cur_dir=$(cd $(dirname $0) && pwd)

runtime_lib_dir=$cur_dir/rt-lib

function assert_library()
{
    local lang=$1
    [ -e $runtime_lib_dir ] && ls $runtime_lib_dir/*.jar >/dev/null 2>&1 \
        || { echo "$lang framework need to build. Please run make.sh to do it" >&2; exit 3; }
}

function get_classpath()
{
    ls $runtime_lib_dir/*.jar | tr '\n' ':'
}

function reset_lib_directory()
{
    [ -e $runtime_lib_dir ] || mkdir -p $runtime_lib_dir
    rm -rvf $runtime_lib_dir/* 2>/dev/null
}

remote_run()
{
# args:
#   lang:   java, scala, ...
#   key:    key of current test case, normally the absolute path of it
#   class:  name of entry class
#   file:   file of test case, use '-' as stdin
    local lang=$1
    local key=$2
    local entry_class=$3
    local file=$4
    local content_type=multipart
#    local content_type=json

    local address=$SODA_SERVER_ADDRESS
    [ -z "$address" ] && { echo "SODA_SERVER_ADDRESS is empty" >&2; exit 3; }

    local work_url="http://$address:$server_port/soda/$lang/work"

    if [ "$content_type" == "multipart" ]; then
        curl --connect-timeout 2 -sf -X POST \
            -F "key=$key" \
            -F "entry_class=$entry_class" \
            -F "test_case=@$file" \
            "$work_url"
    elif [ "$content_type" == "json" ]; then
local pycode=$(cat << EOF
import json
info = {
  "key": "$key",
  "entryClass": "$entry_class",
  "testCase" : """$(cat $file)"""
}
print(json.dumps(info))
EOF
)

        local post_content="$(python3 -c "$pycode")"
        curl --connect-timeout 2 -sf -X POST \
            -H 'Content-Type: application/json; charset=utf-8' \
            -d "$post_content" \
            "$work_url"
    else
        echo "error of remote run: unknown content type: $content_type" >&2
        exit 10
    fi
}

remote_setup()
{
# args:
#   lang:   java, scala, ...
#   key:    key of current test case, normally the absolute path of it
#   file:   bootstrap file, normally a jar, or a script
    local lang=$1
    local key=$2
    local file=$3

    local address=$SODA_SERVER_ADDRESS
    local echo_url="http://$address:$server_port/soda/$lang/echo?a=b"
    if ! curl --connect-timeout 2 -s "$echo_url" >/dev/null; then
        echo "server not open" >&2
        exit 2
    fi

    local boot_url="http://$address:$server_port/soda/$lang/bootstrap"
    local local_md5="$(md5sum $file | awk '{print $1}')"
    local remote_md5="$(curl --connect-timeout 2 -s "${boot_url}?key=$key&format=text")"

    if [ "$local_md5" != "$remote_md5" ]; then  
        curl --connect-timeout 2 -X POST -sf \
            -F "key=$key" \
            -F "binary=@$file" \
            "$boot_url" >/dev/null
    fi
}

