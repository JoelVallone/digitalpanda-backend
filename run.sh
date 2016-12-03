#!/bin/bash

SCRIPT_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

mvn clean install
pkill -f .*backend.*.jar || true
java -jar "${SCRIPT_FOLDER}/target/"*backend*.jar &> ${SCRIPT_FOLDER}/target/backend.log &

