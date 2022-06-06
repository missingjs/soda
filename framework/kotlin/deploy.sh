#!/bin/bash


self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)
lib_dir=soda-lib

set -e

cd $self_dir

[ -e $lib_dir ] && rm -r $lib_dir

mkdir $lib_dir

dku=$soda_dir/bin/docker-utils.sh 
$dku start kotlin

$dku exec kotlin -w /soda/framework/kotlin bash -c "
    ./gradlew clean && ./gradlew build
"

# cp build/libs/*.jar $lib_dir/

zipfile=$(cd build/distributions && ls soda-kotlin*.zip | head -n 1)
fruit=${zipfile%.zip}

unzip build/distributions/$zipfile -d $lib_dir

cd $lib_dir

mv $fruit/lib/*.jar ./

rm -rf $fruit kotlin-stdlib-*.jar annotations-*.jar

