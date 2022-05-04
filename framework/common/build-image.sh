#!/bin/bash

usage()
{
    cat << EOF
usage: docker-footer.sh <dockerfile> <image-name>
EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
framework_dir=$(dirname $self_dir)
python_dir=$framework_dir/python

docker_file=$1
image_name=$2
[ -z $docker_file -o -z $image_name ] && usage

tmpdir=$(mktemp -d)
cat $docker_file > $tmpdir/Dockerfile

cd $tmpdir

cp $python_dir/requirements.txt ./

cat >>Dockerfile << EOF
RUN apt-get update
RUN apt-get install -y python3-pip
COPY requirements.txt /python/
RUN pip3 install -r /python/requirements.txt
RUN apt-get install -y curl
EOF

docker build -t $image_name .

cd -

rm -r $tmpdir

