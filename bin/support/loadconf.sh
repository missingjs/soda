#!/bin/bash

usage()
{
    echo "$(basename $0) <key>"
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)
conf_dir=$soda_dir/conf
conf_file=$conf_dir/soda.conf

key="$1"

[ -z "$key" ] && usage

grep -P "^$key" $conf_file | python3 -c "import sys; print(sys.stdin.read().split('=',1)[1].strip())"

