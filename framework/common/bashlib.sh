this_dir=$(cd -P $(dirname ${BASH_SOURCE[0]}) >/dev/null 2>&1 && pwd)
framework_dir=$(dirname $this_dir)

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

    local work_url="http://localhost:$server_port/soda/$lang/work"

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

    local echo_url="http://localhost:$server_port/soda/$lang/echo?a=b"
    if ! curl --connect-timeout 2 -s "$echo_url" >/dev/null; then
        echo "server not open" >&2
        exit 2
    fi

    local boot_url="http://localhost:$server_port/soda/$lang/bootstrap"
    local local_md5="$(md5sum $file | awk '{print $1}')"
    local remote_md5="$(curl --connect-timeout 2 -s "${boot_url}?key=$key&format=text")"

    if [ "$local_md5" != "$remote_md5" ]; then  
        curl --connect-timeout 2 -X POST -sf \
            -F "key=$key" \
            -F "binary=@$file" \
            "$boot_url" >/dev/null
    fi
}

