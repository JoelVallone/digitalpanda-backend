#!/bin/bash

JAR_OUTPUT_FOLDER="../digitalpanda-infrastructure/docker/images/backend-java"

mvn clean install
cp "target/"*backend*.jar "${JAR_OUTPUT_FOLDER}/"
