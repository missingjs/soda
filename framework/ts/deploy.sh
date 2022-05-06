#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)

out_dir=./dist

cd $self_dir
[ -d $out_dir ] && rm -r $out_dir

run_image()
{
    docker run --rm -t \
        --user $(id -u):$(id -g) \
        -v "/etc/passwd:/etc/passwd:ro" \
        -v "/etc/group:/etc/group:ro" \
        -v /home/$USER:/home/$USER \
        -v $(pwd):/task \
        -w /task \
        missingjs/soda-ts "$@"
}

set -e

echo "running npm install..."
run_image npm install

echo "compiling project source code with tsc..."
run_image tsc --outDir $out_dir

echo '{"type":"module"}' > $out_dir/soda/package.json

