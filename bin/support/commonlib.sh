this_dir=$(cd -P $(dirname ${BASH_SOURCE[0]}) >/dev/null 2>&1 && pwd)
soda_dir=$(realpath $this_dir/../..)
framework_dir=$soda_dir/framework
dku=$self_dir/docker-utils.sh

export SODA_FRAMEWORK_DIR=$framework_dir

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

