#!/bin/bash
#@Author: Daniel Katzberg

# This script deletes all example files.

#Check for parameter
if [ $# -eq 0 ]
  then
    echo "No parameter supplied. Allowed parameters are: dev, local"
    exit 1
fi

#Check for correct parameter and set server path
server=""
if [ "$1" = "local" ]
  then
    server="http://localhost:8080/api/"
fi

if [ "$1" = "dev" ]
  then
    server="https://metadatamanagement.cfapps.io/api/"
fi

if [ ${#server} -eq 0 ]
  then
    echo "Only allowed parameters are: dev, local"
    exit 1
fi

#Configurations
user="-u admin:admin"
delete="-X DELETE"
files="data-acquisition-projects/ATestProject
data-acquisition-projects/bibliographical-references/Reference001
concepts/ConceptId001"


#delete test data in the api paths:
#data-acquisition-projects, bibliographical-references and concepts
for i in $files
do
  curl $user $delete $server$i
done
