#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(cd $self_dir/../.. && pwd)

cd $self_dir

[ -e soda-lib ] && rm -r soda-lib

sbt clean && sbt pack || exit

[ -e soda-lib ] || mkdir soda-lib

cp target/pack/lib/*.jar soda-lib/

