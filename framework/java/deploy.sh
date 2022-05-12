#!/bin/bash

self_dir=$(cd $(dirname $0) && pwd)
soda_dir=$(realpath $self_dir/../..)

cd $self_dir

source setup_env.sh || exit

[ -e soda-lib ] && rm -r soda-lib

dku=$soda_dir/bin/docker-utils.sh 

$dku start java || exit

container=$($dku show java container)

user=$(id -un)

if [ -n "$maven_setting_file" -a -f "$maven_setting_file" ]; then
    docker exec -it -u $user $container bash -c "
        [ -e ~/.m2 ] || mkdir ~/.m2
    "
    docker cp $maven_setting_file $container:/home/$user/.m2/settings.xml
fi

docker exec -it -u $user -w /soda/framework/java $container \
    bash -c "cd /soda/framework/java && mvn clean package"

# docker run --rm -t \
#     --network host \
#     --user $(id -u):$(id -g) \
#     -v "/etc/passwd:/etc/passwd:ro" \
#     -v "/etc/group:/etc/group:ro" \
#     -v /home/$USER/.m2:/home/$USER/.m2 \
#     -v $(pwd):/task \
#     -w /task \
#     missingjs/soda-java mvn clean package

cp target/soda-java-*.jar soda-lib/

