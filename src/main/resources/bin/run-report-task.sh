#!/bin/bash
# this script gives an example for running the report generation
# in the docker container connecting to localhost
docker run --network=host 347729458675.dkr.ecr.eu-central-1.amazonaws.com/dzhw/report-task:latest-dev java -jar /app/report-task.jar --task.id=$1 --task.version=$2 --task.language=$3 --task.onBehalfOf=$4 --task.type=$5 --spring.security.oauth2.client.provider.fdz.issuer-uri=${FDZ_ISSUER_URI} --spring.security.oauth2.client.registration.fdz.client-id=${FDZ_CLIENT_ID} --spring.security.oauth2.client.registration.fdz.client-secret=${FDZ_CLIENT_SECRET} --mdm.endpoint=http://127.0.0.1:8080
