#!/bin/bash


self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(cd $self_dir/../.. && pwd)
lib_dir=soda-lib

run_gradlew()
{
    docker run --rm -t \
        --network host \
        --user $(id -u):$(id -g) \
        -v "/etc/passwd:/etc/passwd:ro" \
        -v "/etc/group:/etc/group:ro" \
        -v /home/$USER/.m2:/home/$USER/.m2 \
        -v /home/$USER/.gradle:/home/$USER/.gradle \
        -v $(pwd):/task \
        -w /task \
        missingjs/soda-groovy ./gradlew "$@"
}

cd $self_dir

[ -e $lib_dir ] && rm -r $lib_dir

mkdir $lib_dir

run_gradlew clean && run_gradlew build

cp build/libs/*.jar $lib_dir/

