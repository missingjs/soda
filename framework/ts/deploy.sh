#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

out_dir=./dist

cd $self_dir
[ -d $out_dir ] && rm -r $out_dir

dku=$soda_dir/bin/docker-utils.sh 
$dku start ts || exit

$dku exec ts bash -c "
    set -e
    cd /soda/framework/ts
    echo 'running npm install...'
    npm install
    echo 'compiling project source code with tsc...'
    tsc --outDir $out_dir
    echo '{\"type\":\"module\"}' > $out_dir/soda/package.json
"

