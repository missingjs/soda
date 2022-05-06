#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)

out_dir=./dist

cd $self_dir
[ -d $out_dir ] && rm -r $out_dir

set -x
docker run --rm -t \
    --user $(id -u):$(id -g) \
    -v "/etc/passwd:/etc/passwd:ro" \
    -v "/etc/group:/etc/group:ro" \
    -v $(pwd):/task \
    -w /task \
    missingjs/soda-ts tsc --outDir $out_dir
set +x

echo '{"type":"module"}' > dist/soda/package.json

