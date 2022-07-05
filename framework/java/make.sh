#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

source $soda_dir/framework/common/jvm-like.sh || exit

cd $self_dir

reset_lib_directory

dku=$soda_dir/bin/docker-utils.sh 

$dku start java || exit

if [ -n "$maven_setting_file" -a -f "$maven_setting_file" ]; then
    $dku invoke java bash -c "[ -e ~/.m2 ] || mkdir ~/.m2"
    cat $maven_setting_file | $dku invoke java bash -c "cat > ~/.m2/settings.xml"
fi

$dku invoke java -w /soda/framework/java -- bash -c "
    source /soda/framework/common/jvm-like.sh || exit
    export SODA_RT_LIB_DIR=\$runtime_lib_dir
    mvn clean package
"

cp target/soda-java-*.jar $runtime_lib_dir/

