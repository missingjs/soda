#!/bin/bash

usage()
{
    cat << EOF
usage: $(basename $0) <location> <lang> [--clean]

lang: all, cpp|python|java|go
EOF
    exit 1
}

self_dir=$(cd $(dirname $0) && pwd)
prj_file=soda.prj.yml

location=$1
languages=$2
shift; shift;
[ -z "$location" -o -z "$languages" ] && usage
[ "$languages" == "all" ] && languages="cpp python java go"
options="$@"

for lang in $languages; do
    while read path; do
        directory=$(dirname $path)
        cd $directory && echo "[$lang] Enter $directory"
        soda run $lang $options || exit
        cd .. && echo -e "[$lang] Leave $directory\n"
    done < <(find $self_dir/$location -name $prj_file)
    echo -e "[$lang] DONE\n"
done
