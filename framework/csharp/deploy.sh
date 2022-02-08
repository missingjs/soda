#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)

set -e

cd $self_dir/soda

dotnet pack

global_pack_dir=~/.nuget/packages/soda-csharp

[ -e $global_pack_dir ] && rm -rv $global_pack_dir

# !!! OR clear all global package caches
# dotnet nuget locals global-packages --clear
