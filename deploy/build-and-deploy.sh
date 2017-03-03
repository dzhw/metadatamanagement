#!/bin/bash
# this script builds the project with a given profile and deploys it
# to the correct space in cloudfoundry
if [[ $0 != ./deploy/* ]]; then
  echo "Please run this script from the root of our repository!"
  exit -1
fi
PROFILE="$1"
if [ -z ${PROFILE} ]; then
  echo "Please provide a valid profile e.g.: $0 dev"
  exit -1
fi
echo "Going to run maven build and cloudfoundry deployment with profile: ${PROFILE}"
mvn -P${PROFILE} clean package
if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit -1
fi
cf login -o DZHW -s ${PROFILE}
if [ $? -ne 0 ]; then
    echo "cf login failed!"
    exit -1
fi
cf push -f ./deploy/manifest-${PROFILE}.yml
if [ $? -ne 0 ]; then
    echo "cf push failed!"
    exit -1
fi
