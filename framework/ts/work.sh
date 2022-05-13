#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    $cmd <command> [options]

options:
    new <testname>
        create source file with name <testname>.js

    source <testname>
        show source file name

    make <testname> 
        do nothing

    run <testname>
        run test case

    clean <testname>
        do nothing

EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
srcfile=${testname}.ts
exefile=${testname}.js
output_dir=./ts
dist_dir=$self_dir/dist
build_target=es2022

function create_work()
{
    template_file=$self_dir/src/bootstrap.ts
    create_source_file $template_file $srcfile
    sed -i 's/\.\/soda/soda/g' $srcfile
}

function build_work()
{
    [ -e $output_dir ] || mkdir $output_dir
    jsfile=$output_dir/$exefile
    if [[ ! -e $jsfile ]] || [[ $srcfile -nt $jsfile ]]; then
        cp $srcfile $output_dir/
        cd $output_dir
        echo '{"type": "module"}' > package.json

        [ -e node_modules ] || mkdir node_modules
        cd node_modules
        rm -rf *
        ln -s $dist_dir/soda
        for d in $(ls -d $self_dir/node_modules/*); do
            ln -s $d
        done
        cd ..

        echo "Compiling $srcfile ..."
        set -x
        tsc --target $build_target --moduleResolution node $srcfile || exit
        set +x
        echo "Compile $srcfile OK"
    fi
}

assert_testname
case $cmd in
    new)
        create_work
        ;;
    source)
        echo ${testname}.ts
        ;;
    make)
        build_work
        ;;
    run)
        cd $output_dir && node --experimental-specifier-resolution=node $exefile
        ;;
    clean)
        [ -e $output_dir ] && rm -rv $output_dir
        ;;
    *)
        usage
        ;;
esac

