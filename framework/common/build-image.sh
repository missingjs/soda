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
uid=$(id | grep -oP '(?<=uid=)\d+')
gid=$(id | grep -oP '(?<=gid=)\d+')
group_name=$(id | grep -oP "(?<=gid=$gid[(])[a-zA-Z0-9_]+")

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
RUN groupadd -g $gid $group_name
RUN useradd $USER -u $uid -g $gid -m -s /bin/bash
USER $USER
EOF

docker build -t $image_name .

cd -

rm -r $tmpdir

