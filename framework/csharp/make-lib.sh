#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)

[ -e $self_dir/setup_env.sh ] || cp $self_dir/_setup_env.sh $self_dir/setup_env.sh
source $self_dir/setup_env.sh || exit
source $self_dir/common.sh || exit

check_package

set -e

cd $self_dir/soda

[ -d bin ] && rm -rfv bin || true
[ -d obj ] && rm -rfv obj || true

dotnet clean

dotnet pack --configuration Debug

dotnet pack --configuration Release

# clear this package in global cache
global_pack_dir=~/.nuget/packages/$package_id/$package_version
[ -e $global_pack_dir ] && rm -rv $global_pack_dir

# !!! OR clear all global package caches
# dotnet nuget locals global-packages --clear
