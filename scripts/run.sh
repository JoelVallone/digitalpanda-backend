#!/bin/bash

SCRIPT_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ $# -gt 0 ] && [ $1 = "-c" ]; then
    ${SCRIPT_FOLDER}/start-cassandra.sh
    echo "Wait 20 seconds for cassandra to initialise : "
    sleep 20
fi

mvn clean install


pkill -f .*backend.*.jar || true
java -jar "${SCRIPT_FOLDER}/../digitalpanda-backend-application/target/"*backend*.jar &> ${SCRIPT_FOLDER}/../backend.log &
