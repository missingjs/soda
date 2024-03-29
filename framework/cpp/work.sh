#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    $cmd <command> [options]

options:
    new <testname>
        create source file with name <testname>.cpp

    source <testname>
        show source file name

    make <testname> 
        build executable

    run <testname>
        run executable

    clean <testname>
        clean intermediate files

EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit

source_setup_env $self_dir

cmd=$1
[ -z $cmd ] && usage

testname=$2
srcfile=${testname}.cpp
makefile=Makefile.gen.$testname
output_dir=./cpp

assert_testname()
{
    [ -z $testname ] && usage
}

filter_comment()
{
    grep -vP '^\s*//' $1
}

gen_source()
{
    local srcfile=$1
    local classname=$(filter_comment $srcfile | grep -oP '(?<=WorkFactory::forStruct<)\w+')
    local param_types=$(filter_comment $srcfile | grep -oP "(?<=${classname}\()[^)]+" | tr ',' '\n' | perl -pe 's/\w+\s*$/,/g')
    [ -z "$param_types" ] || param_types=$(perl -pe 's/,\s*$//' <<< "$param_types")
    local tt=tt
    
    local mf_flag='// @member_function'
    local addfn=$(grep "$mf_flag" -A 1 $srcfile \
            | grep -v "$mf_flag" \
            | grep -vP '[-]{2}' \
            | grep -oP '\w+(?=[(])' \
            | python3 -c "import sys; print(';'.join(map(lambda x: f'ADD_FUNCTION($tt,{x.strip()})', sys.stdin.readlines())))")
    local f1=WorkFactory::forStruct
    local f2=WorkFactory::createStructTester
    local tmpl_args="$classname,$param_types"
    [ -z "$param_types" ] && tmpl_args="$classname"
    filter_comment $srcfile | perl -pe "s/$f1<.*?>\(\)/[](){auto $tt=$f2<$tmpl_args>();$addfn;return $f1($tt);}()/g"
}

assert_testname
case $cmd in
    new)
        template_file=$self_dir/src/soda/unittest/bootstrap.cpp
        create_source_file $template_file $srcfile
        ;;
    source)
        echo $srcfile
        ;;
    make)
        [ -e $output_dir ] || mkdir $output_dir
        exefile=$output_dir/${testname}.out
        if [[ ! -e $exefile ]] || [[ $srcfile -nt $exefile ]]; then
            gen_source $srcfile > $output_dir/${testname}__gen.cpp
            bash $self_dir/gen-makefile.sh -c $testname > $output_dir/$makefile
            # return error code from make
            (cd $output_dir; make -f $makefile)
        fi
        ;;
    run)
        execfile=$output_dir/${testname}.out
        [ -e $execfile ] || { echo "Error: no executable file" >&2; exit 2; }
        export LD_LIBRARY_PATH=$self_dir/lib:$LD_LIBRARY_PATH
        ASAN_OPTIONS="detect_leaks=0" $execfile
        ;;
    clean)
        [ -e $output_dir ] && rm -rfv $output_dir || true
        ;;
    *)
        usage
        ;;
esac

