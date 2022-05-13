#!/bin/bash


self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)
lib_dir=soda-lib

#run_gradlew()
#{
#    docker run --rm -t \
#        --network host \
#        --user $(id -u):$(id -g) \
#        -v "/etc/passwd:/etc/passwd:ro" \
#        -v "/etc/group:/etc/group:ro" \
#        -v /home/$USER/.m2:/home/$USER/.m2 \
#        -v /home/$USER/.gradle:/home/$USER/.gradle \
#        -v $(pwd):/task \
#        -w /task \
#        missingjs/soda-groovy ./gradlew "$@"
#}

set -e

cd $self_dir

[ -e $lib_dir ] && rm -r $lib_dir

mkdir $lib_dir

dku=$soda_dir/bin/docker-utils.sh 
$dku start groovy

$dku exec groovy bash -c "
    cd /soda/framework/groovy && ./gradlew clean && ./gradlew build
"

#run_gradlew clean && run_gradlew build

cp build/libs/*.jar $lib_dir/

