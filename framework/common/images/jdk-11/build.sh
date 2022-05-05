#!/bin/bash

source ../../vars.sh || exit

docker build --network host $proxy_arg -t soda-jdk:11 .

