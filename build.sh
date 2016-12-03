#!/bin/bash
SCRIPTS_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
JAR_OUTPUT_FOLDER="${SCRIPTS_FOLDER}/../digitalpanda-infrastructure/docker/images/backend-java"

mvn clean install
cp "target/"*backend*.jar "${JAR_OUTPUT_FOLDER}/"
