#!/bin/bash
set -e

SCRIPTS_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
JAR_OUTPUT_FOLDER="${SCRIPTS_FOLDER}/../../digitalpanda-infrastructure/docker/images/backend-java"

echo "=> Build backend"
cd ${SCRIPTS_FOLDER}/../
export spring.env=local
mvn clean install

echo "=> Copy backend binary to docker image external folder"
rm -f "${JAR_OUTPUT_FOLDER}/"*backend*.jar
cp "${SCRIPTS_FOLDER}/../digitalpanda-backend-application/target/"*backend*.jar "${JAR_OUTPUT_FOLDER}/"

echo "=>Build and push image to registry"
IMAGE_NAME=localhost:5000/digitalpanda-backend:latest
docker build -t ${IMAGE_NAME} ${SCRIPT_FOLDER}/../
docker push ${IMAGE_NAME}
