#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

cd $self_dir

source setup_env.sh || exit

[ -e soda-lib ] && rm -r soda-lib

dku=$soda_dir/bin/docker-utils.sh 

$dku start java || exit

if [ -n "$maven_setting_file" -a -f "$maven_setting_file" ]; then
    $dku exec java bash -c "[ -e ~/.m2 ] || mkdir ~/.m2"
    cat $maven_setting_file | $dku exec java bash -c "cat > ~/.m2/settings.xml"
fi

$dku exec java -w /soda/framework/java mvn clean package

cp target/soda-java-*.jar soda-lib/

