#!/bin/bash

proxy_arg=$(../../build-utils.sh build-proxy)

set -x
docker build --network host $proxy_arg -t missingjs/soda-jdk:11 .

