#!/bin/bash

uid=$(id | grep -oP '(?<=uid=)\d+')
gid=$(id | grep -oP '(?<=gid=)\d+')
group_name=$(id | grep -oP "(?<=gid=$gid[(])[a-zA-Z0-9_]+")

cat >Dockerfile << EOF
FROM soda-python
RUN apt-get update; apt-get install -y gcc-9
RUN groupadd -g $gid $group_name
RUN useradd $USER -u $uid -g $gid -m -s /bin/bash
USER $USER
EOF

docker build -t soda-cpp .

rm Dockerfile
