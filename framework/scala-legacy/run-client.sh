#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)

source $self_dir/setup_env.sh || exit

classname=$1

url="http://localhost:$server_port/soda/java/job"

runpath=$(pwd)
post_content=$(python3 -c "import json; import sys; content = sys.stdin.read(); print(json.dumps({'runpath':'$runpath', 'jobclass':'$classname', 'request':content}))")

curl --connect-timeout 2 -d "$post_content" -s $url
