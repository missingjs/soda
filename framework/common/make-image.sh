#!/bin/bash

usage()
{
    cat << EOF
usage: $(basename $0) <image-name>
EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
python_dir=$framework_dir/python

image_name=$1
[ -z $image_name ] && usage

source $self_dir/vars.sh || exit

tmpdir=$(mktemp -d)

cd $tmpdir

cp $python_dir/requirements.txt ./

cat >>Dockerfile << EOF
FROM ${image_name}-part
RUN apt-get update
RUN apt-get install -y python3-pip
COPY requirements.txt /build/
RUN pip3 install -r /build/requirements.txt
RUN rm /build/requirements.txt
EOF

docker build --network host $proxy_arg -t $image_name .

cd -

rm -r $tmpdir

