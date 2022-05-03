#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(cd $self_dir/../.. && pwd)

cd $self_dir

[ -e soda-lib ] && rm -r soda-lib

docker run \
    --user $USER \
    --rm \
    -v /home/$USER/.m2:/home/$USER/.m2 \
    -v $(pwd):/task \
    -w /task \
    soda-java mvn clean package

cp target/soda-java-*.jar soda-lib/

