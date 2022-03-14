#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    soda ruby [options]

options:
    new <testname>
        create source file with name <testname>.js

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

case $cmd in
    new)
        template_file=$self_dir/src/bootstrap.ts
        create_source_file $template_file $srcfile
        ;;
    make)
        assert_testname
        [ -e $output_dir ] || mkdir $output_dir
        jsfile=$output_dir/$exefile
        if [[ ! -e $jsfile ]] || [[ $srcfile -nt $jsfile ]]; then
            echo "Compiling $srcfile ..."
            set -x
            tsc --baseUrl $dist_dir --outDir $output_dir --target es3 $srcfile || exit
            set +x
            echo "Compile $srcfile OK"
        fi
        ;;
    run)
        export NODE_PATH="$dist_dir:$self_dir/node_modules:$NODE_PATH"
        (cd $output_dir && node $exefile)
        ;;
    clean)
        [ -e $output_dir ] && rm -v -r $output_dir
        ;;
    *)
        usage
        ;;
esac

