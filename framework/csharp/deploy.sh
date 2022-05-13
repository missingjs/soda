#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

dku=$soda_dir/bin/docker-utils.sh 
$dku start csharp

$dku exec csharp bash -c "cd /soda/framework/csharp && ./make-lib.sh"

