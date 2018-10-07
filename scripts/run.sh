#!/bin/bash

SCRIPT_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

mvn clean install

if [ $# -gt 0 ] && [ $1 = "-c" ]; then
    ${SCRIPT_FOLDER}/start-cassandra.sh
    echo "Wait 20 seconds for cassandra to initialise : "
    sleep 20
fi

pkill -f .*backend.*.jar || true
java -jar "${SCRIPT_FOLDER}/../digitalpanda-backend-application/target/"*backend*.jar &> ${SCRIPT_FOLDER}/../backend.log &
echo "Started backend with PID=$! logs available at: ${SCRIPT_FOLDER}/../backend.log"