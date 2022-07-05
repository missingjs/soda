#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

source $soda_dir/framework/common/jvm-like.sh || exit

cd $self_dir

reset_lib_directory

dku=$soda_dir/bin/docker-utils.sh 

$dku start groovy || exit

$dku invoke groovy -w /soda/framework/groovy -- bash -c "
    ./gradlew clean && ./gradlew build
"

artifact=$(grep '^rootProject.name' settings.gradle | cut -d '=' -f 2 | tr "'" ' ' | xargs)
version=$(grep -m 1 '^version' build.gradle | awk '{print $2}' | tr "'" ' ' | xargs)
jar_file="$artifact-$version.jar"

cp -v build/libs/$jar_file $runtime_lib_dir/

