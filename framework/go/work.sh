#!/bin/bash

usage()
{
    local cmd=$(basename $0)
    cat>&2 << EOF
usage:
    $cmd <command> [options]

options:
    new <testname>
        create source file with name <testname>.go

    source <testname>
        show source file name

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

[ -e $self_dir/setup_env.sh ] || cp $self_dir/_setup_env.sh $self_dir/setup_env.sh
source $self_dir/setup_env.sh || exit

cmd=$1
[ -z $cmd ] && usage

testname=$2
srcfile=${testname}.go
execfile=${srcfile}.out
output_dir=./go

package_name="missingjs.com/soda"
package_version="v0.0.1"

function create_project()
{
    [ -e $output_dir/go.mod ] && { echo "$output_dir/go.mod already exists">&2; exit 2; }

    set -e
    create_source_file $self_dir/src/bootstrap.go $srcfile

    mkdir $output_dir
    cd $output_dir
    suffix=$(basename $(pwd))-$(date +%Y%m%d%H%M%S)
    go mod init "$package_name/coding/$suffix"
    go mod edit -require "$package_name@$package_version"
    go mod edit -replace "$package_name=$self_dir/src"
}

function build_project()
{
    [ -e $output_dir/go.mod ] || { echo "$output_dir/go.mod not found" >&2; exit 2; }
    if [[ ! -e $output_dir/$execfile ]] || [[ $srcfile -nt $output_dir/$execfile ]]; then
        [ -n "$go_proxy" ] && export GOPROXY="$go_proxy"
        set -e
        cp $srcfile $output_dir/main__gen.go
        cd $output_dir
        go mod edit -replace "$package_name=$self_dir/src"
        go mod tidy
        go build -o $execfile main__gen.go
        echo "Build success."
    fi
}

[ -z $testname ] && usage
case $cmd in
    new)
        create_project
        ;;
    source)
        echo $srcfile
        ;;
    make)
        build_project
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

