#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

source $soda_dir/framework/common/jvm-like.sh || exit

cd $self_dir

reset_lib_directory

dku=$soda_dir/bin/docker-utils.sh 

$dku start java || exit

if [ -n "$maven_setting_file" -a -f "$maven_setting_file" ]; then
    $dku invoke java bash -c "[ -e ~/.m2 ] || mkdir ~/.m2"
    cat $maven_setting_file | $dku invoke java bash -c "cat > ~/.m2/settings.xml"
fi

$dku invoke java -w /soda/framework/java -- bash -c "
    source /soda/framework/common/jvm-like.sh || exit
    export SODA_RT_LIB_DIR=\$runtime_lib_dir
    mvn clean package
"

jar_name=$(python3 <<EOF
import xml.etree.ElementTree as ET

nsmap = {}
for _, v in ET.iterparse('pom.xml', events=['start-ns']):
    key, url = v
    nsmap[key] = url

tree = ET.parse('pom.xml')
root = tree.getroot()
ns = nsmap['']
artifact = root.findall(f'./{{{ns}}}artifactId')[0].text
version = root.findall(f'./{{{ns}}}version')[0].text
print(f'{artifact}-{version}.jar')
EOF
)

cp target/$jar_name $runtime_lib_dir/

