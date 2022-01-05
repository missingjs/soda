#!/bin/bash

usage()
{
    cat << EOF
usage: $(basename $0) <lang>

lang: all, cpp|python|java|go
EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
prj_file=soda.prj.yml

languages=$1
[ -z $languages ] && usage
[ "$languages" == "all" ] && languages="cpp python java go"

while read path; do
    directory=$(dirname $path)
    cd $directory && echo "Enter $directory"
    name=$(grep work_name $prj_file | awk '{print $2}')
    for lang in $languages; do
        soda run $lang || exit
    done
    cd .. && echo "Leave $directory"
done < <(find $self_dir -name $prj_file)
