#!/bin/bash
SCRIPTS_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
JAR_OUTPUT_FOLDER="${SCRIPTS_FOLDER}/../../digitalpanda-infrastructure/docker/images/backend-java"
cd ${SCRIPTS_FOLDER}/../
mvn clean install
cp "${SCRIPTS_FOLDER}/../digitalpanda-backend-application/target/"*backend*.jar "${JAR_OUTPUT_FOLDER}/"
