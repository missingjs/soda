#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

source $soda_dir/framework/common/jvm-like.sh || exit

cd $self_dir

reset_lib_directory

dku=$soda_dir/bin/docker-utils.sh 

$dku start scala || exit

$dku invoke scala -w /soda/framework/scala -- bash -c "sbt clean && sbt pack"

cp target/pack/lib/*.jar $runtime_lib_dir/

