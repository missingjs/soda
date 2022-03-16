#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)

out_dir=./dist

cd $self_dir
[ -d $out_dir ] && rm -r $out_dir

set -x
tsc --outDir $out_dir || exit
set +x

echo '{"type":"module"}' > dist/soda/package.json

