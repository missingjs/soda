#!/bin/bash


self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)
lib_dir=soda-lib

set -e

cd $self_dir

[ -e $lib_dir ] && rm -r $lib_dir

mkdir $lib_dir

dku=$soda_dir/bin/docker-utils.sh 
$dku start groovy

$dku exec groovy bash -c "
    cd /soda/framework/groovy && ./gradlew clean && ./gradlew build
"

cp build/libs/*.jar $lib_dir/

