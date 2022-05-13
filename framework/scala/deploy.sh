#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(cd $self_dir/../.. && pwd)

set -e

cd $self_dir

[ -e soda-lib ] && rm -r soda-lib

dku=$soda_dir/bin/docker-utils.sh 
$dku start scala

container=$($dku show scala container)
user=$(id -un)

$dku exec scala bash -c "cd /soda/framework/scala; sbt clean && sbt pack"

[ -e soda-lib ] || mkdir soda-lib

cp target/pack/lib/*.jar soda-lib/

