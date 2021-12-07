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

    make <testname> 
        build executable

    run <testname>
        run executable

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

exec_test()
{
    shift; shift  # skip <cmd> <testname>
    [ -e $execfile ] || { echo "$execfile not exist" >&2; exit; }
    run_test go $execfile "$@"
}

assert_testname()
{
    [ -z $testname ] && usage
}

package_name="missingjs.com/soda"
package_version="v0.0.1"

assert_testname
case $cmd in
    new)
        create_source_file $self_dir/bootstrap.go $srcfile
        suffix=$(basename $(pwd))-$(date +%Y%m%d%H%M%S)
        go mod init "$package_name/coding/$suffix"
        go mod edit -require "$package_name@$package_version"
        go mod edit -replace "$package_name=$self_dir/src"
        ;;
    make)
#        [ -e $execfile ] && rm $execfile
        if [[ ! -e $execfile ]] || [[ $srcfile -nt $execfile ]]; then
            go build -o $execfile $srcfile && echo "Build success."
        fi
        ;;
    run)
        ./$execfile
        ;;
    *)
        usage
        ;;
esac

