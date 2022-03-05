#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(cd $self_dir/../.. && pwd)
lib_dir=soda-lib

cd $self_dir

[ -e $lib_dir ] && rm -r $lib_dir

mkdir $lib_dir

./gradlew clean && ./gradlew build

cp build/libs/*.jar $lib_dir/

