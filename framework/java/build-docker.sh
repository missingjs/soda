#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
common_dir=$framework_dir/common

source $self_dir/setup_env.sh || exit

dcfile=Dockerfile.0

cp Dockerfile-template $dcfile

echo "EXPOSE $server_port" >> $dcfile

$common_dir/build-image.sh $dcfile soda-java

rm $dcfile
