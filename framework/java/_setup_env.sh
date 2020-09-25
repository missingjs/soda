this_dir=$(cd -P $(dirname ${BASH_SOURCE[0]}) >/dev/null 2>&1 && pwd)

#export JAVA_HOME=$JAVA_HOME

export SODA_JAVA_COMPILE_OPTION=

tell_to_build()
{
    echo "Java framework need to build. Please run $this_dir/deploy.sh to do it" >&2
    exit 3
}

assert_framework()
{
    local lib_dir=$this_dir/soda-lib
    [ -e $lib_dir ] || tell_to_build
    ls $lib_dir/*.jar >/dev/null 2>&1 || tell_to_build
}

get_classpath()
{
    local lib_dir=$this_dir/soda-lib
    local classpath=.
    for jar in $(ls $lib_dir); do
        classpath="$classpath:$lib_dir/$jar"
    done
    echo $classpath
}
