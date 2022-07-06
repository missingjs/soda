#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
lib_dir=$self_dir/lib
soda_dir=$(realpath $self_dir/../..)
dku=$soda_dir/bin/docker-utils.sh 

cd $self_dir

set -e

$dku start cpp

$dku invoke cpp -w /soda/framework/cpp -- bash do-make.sh "$@"

ls $lib_dir/

echo DONE

