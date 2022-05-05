#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(cd $self_dir/../.. && pwd)

run_sbt()
{
    docker run --rm -t \
        --network host \
        --user $(id -u):$(id -g) \
        -v /home/$USER/.m2:/home/$USER/.m2 \
        -v /home/$USER/.sbt:/home/$USER/.sbt \
        -v /home/$USER/.ivy2:/home/$USER/.ivy2 \
        -v /home/$USER/.cache:/home/$USER/.cache \
        -v $(pwd):/task \
        -w /task \
        soda-scala sbt "$@"
}

cd $self_dir

[ -e soda-lib ] && rm -r soda-lib

# sbt clean && sbt pack || exit
run_sbt clean && run_sbt pack || exit

[ -e soda-lib ] || mkdir soda-lib

cp target/pack/lib/*.jar soda-lib/

