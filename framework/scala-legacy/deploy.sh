#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(cd $self_dir/../.. && pwd)

cd $self_dir

[ -e soda-lib ] && rm -r soda-lib

mvn clean -DskipTests=true package

cp target/soda-scala-*.jar soda-lib/

