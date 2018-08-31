#!/bin/bash

SCRIPT_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CASSANDRA_CONTAINER="${SCRIPT_FOLDER}/../../digitalpanda-infrastructure/docker/images/cassandra"

${SCRIPT_FOLDER}/stop-cassandra.sh

ORIGIN_DIR=$(pwd)
cd ${CASSANDRA_CONTAINER}
sudo docker build -t cassandra:latest .
sudo docker run -d --net=host -t cassandra:latest
cd ${ORIGIN_DIR}
