#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)

cd $self_dir/soda && dotnet pack && dotnet nuget locals global-packages --clear

