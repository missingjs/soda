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

force_rebuild=$3

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
        [ "$force_rebuild" == "-f" ] && [ -e $execfile ] && rm $execfile
        if [[ ! -e $execfile ]] || [[ $srcfile -nt $execfile ]]; then
            go build -o $execfile $srcfile && echo "Build success."
        fi
        ;;
    run)
        ./$execfile
        ;;

    clean)
        [ -e $execfile ] && rm -v $execfile
        ;;
    *)
        usage
        ;;
esac

