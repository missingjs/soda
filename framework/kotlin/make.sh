#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

source $soda_dir/framework/common/jvm-like.sh || exit

cd $self_dir

reset_lib_directory

dku=$soda_dir/bin/docker-utils.sh 

$dku start kotlin || exit

$dku invoke kotlin -w /soda/framework/kotlin -- bash -c "
    ./gradlew clean && ./gradlew build
"

# cp target/pack/lib/*.jar $runtime_lib_dir/

artifact=$(grep '^rootProject.name' settings.gradle.kts | cut -d '=' -f 2 | tr '"' ' ' | xargs)
version=$(grep -m 1 '^version' build.gradle.kts | awk -F = '{print $2}' | tr '"' ' ' | xargs)
# zipfile=$(cd build/distributions && ls soda-kotlin*.zip | head -n 1)
zipfile="$artifact-$version.zip"
fruit=${zipfile%.zip}

unzip build/distributions/$zipfile -d $runtime_lib_dir

cd $runtime_lib_dir
mv $fruit/lib/*.jar ./
rm -rf $fruit kotlin-stdlib-jdk*.jar

echo DONE

