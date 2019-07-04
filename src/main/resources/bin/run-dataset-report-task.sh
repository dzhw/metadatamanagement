#!/bin/bash
# this script gives an example for running the dataset report generation
# in the docker container connecting to localhost
docker run --network=host dzhw/dataset-report-task java -jar /app/dataset-report-task.jar --task.dataSetId=$1 --task.version=$2 --task.onBehalfOf=$3 --mdm.username=${MDM_TASK_USER} --mdm.password=${MDM_TASK_PASSWORD} --mdm.endpoint=http://127.0.0.1:8080
