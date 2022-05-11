#!/bin/bash

usage()
{
    cat << EOF
usage:
    $(basename $0) <base-image> <final-image> [options]

options:
    options for 'docker build'
EOF
    exit 1
}

self_dir="$(cd "$(dirname $0)" && pwd)"
framework_dir=$(dirname $self_dir)
python_dir=$framework_dir/python

base_image=$1
shift

final_image=$1
shift

[ -z "$base_image" -o -z "$final_image" ] && usage

[[ "$base_image" =~ ^missingjs/.* ]] || base_image=missingjs/$base_image
[[ "$final_image" =~ ^missingjs/.* ]] || final_image=missingjs/$final_image

build_options="$@"

proxy_arg=$($self_dir/build-utils.sh proxy-arg)

# build base image
docker build --network host $proxy_arg -t $base_image . || exit

tmpdir=$(mktemp -d)
cd $tmpdir
cat >>Dockerfile <<EOF
FROM $base_image
EOF

docker build --network host $proxy_arg -t $final_image .

cd -
rm -r $tmpdir

