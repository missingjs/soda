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
