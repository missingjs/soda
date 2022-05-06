#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)

cd $self_dir

docker run --rm -t \
    --user $(id -u):$(id -g) \
    -v "/etc/passwd:/etc/passwd:ro" \
    -v "/etc/group:/etc/group:ro" \
    -v /home/$USER/.nuget:/home/$USER/.nuget \
    -v /home/$USER/.dotnet:/home/$USER/.dotnet \
    -v $(pwd):/task \
    -w /task \
    missingjs/soda-csharp /task/make-lib.sh
