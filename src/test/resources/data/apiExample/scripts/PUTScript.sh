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
put="-X PUT"
header="-H '"'Content-Type: application/json"'""
#Tupels, where the elements are separated by commata and the tupels by line breaks.
files="dataAcquisitionProjectExample.json,data_acquisition_projects/ATestProject
surveyExample.json,surveys/FDZID001
variableExample.json,variables/FDZID_Variable_001
questionnaireExample.json,questionnaires/QuestionnaireId001
dataSetExample.json,data_sets/DataSet_001
atomicQuestionExample.json,atomic_questions/AtomicQuestion_001
conceptExample.json,concepts/ConceptId001
bibliographicalReferenceExample.json,bibliographical_references/Reference001"

#Simulates line break for IFS at files array.
OLDIFS=$IFS
IFS='
';
for f in $files
do
  #extract tupel from $f and split to $1 and $2
  IFS=','
  set -- $f
  IFS=$OLDIFS
  serverPath="$server$2"
  filePath="-d @./../$1"

  #create curl put command
  curl $user -H "Content-Type: application/json" $filePath $put "$serverPath"
done
