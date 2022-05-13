#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

set -e

cd $self_dir

[ -e soda-lib ] && rm -r soda-lib

dku=$soda_dir/bin/docker-utils.sh 
$dku start scala

$dku exec scala -w /soda/framework/scala \
    bash -c "sbt clean && sbt pack"

[ -e soda-lib ] || mkdir soda-lib

cp target/pack/lib/*.jar soda-lib/

