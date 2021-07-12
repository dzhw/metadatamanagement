#!/bin/bash
# this script gives an example for running the report generation
# in the docker container connecting to localhost
docker run --network=host 347729458675.dkr.ecr.eu-central-1.amazonaws.com/dzhw/report-task:latest-dev java -jar /app/report-task.jar --task.id=$1 --task.version=$2 --task.language=$3 --task.onBehalfOf=$4 --task.type=$5 --mdm.username=${MDM_TASK_USER} --mdm.password=${MDM_TASK_PASSWORD} --mdm.endpoint=http://127.0.0.1:8080
