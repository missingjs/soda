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
