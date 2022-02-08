#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    soda go [options]

options:
    new <testname>
        create source file with name <testname>.go

    make <testname> [-f]
        build executable, force rebuild if -f present

    run <testname>
        run executable

    clean <testname>
        remove executable file

EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
source $framework_dir/common/bashlib.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
srcfile=${testname}.go
execfile=${srcfile}.out
output_dir=./go

assert_testname()
{
    [ -z $testname ] && usage
}

package_name="missingjs.com/soda"
package_version="v0.0.1"

assert_testname
case $cmd in
    new)
        # check go project directory
        [ -e $output_dir ] && { echo "$output_dir already exists">&2; exit 2; }
        create_source_file $self_dir/bootstrap.go $srcfile
        mkdir $output_dir
        cd $output_dir || exit
        suffix=$(basename $(pwd))-$(date +%Y%m%d%H%M%S)
        go mod init "$package_name/coding/$suffix"
        go mod edit -require "$package_name@$package_version"
        go mod edit -replace "$package_name=$self_dir/src"
        ;;
    make)
        if [[ ! -e $output_dir/$execfile ]] || [[ $srcfile -nt $output_dir/$execfile ]]; then
            set -e
            cp $srcfile $output_dir/main__gen.go
            cd $output_dir
            go build -o $execfile main__gen.go
            echo "Build success."
        fi
        ;;
    run)
        ./$output_dir/$execfile
        ;;

    clean)
        [ -e $output_dir/$execfile ] && rm -v $output_dir/$execfile
        ;;
    *)
        usage
        ;;
esac

