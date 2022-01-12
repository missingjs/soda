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

for lang in $languages; do
    while read path; do
        directory=$(dirname $path)
        cd $directory && echo "[$lang] Enter $directory"
        if [ "$2" == "--clean" ]; then
            soda clean $lang
        fi
        soda run $lang || exit
        cd .. && echo -e "[$lang] Leave $directory\n"
    done < <(find $self_dir -name $prj_file)
done
