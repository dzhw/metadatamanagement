#!/bin/bash
# this script builds the project with a given profile and deploys it
# to the correct space in cloudfoundry
if [[ $0 != ./deploy/* ]]; then
  echo "Please run this script from the root of our repository!"
  exit -1
fi
PROFILE="$1"
USERNAME="$2"
PASSWORD="$3"
TRAVIS_BRANCH="$4"
if [ -n "${TRAVIS_BRANCH}" ]; then
  PROFILE="dev"
fi
if [ "${TRAVIS_BRANCH}" = "master" ]; then
  PROFILE="prod"
fi
if [ "${TRAVIS_BRANCH}" = "test" ]; then
  PROFILE="test"
fi
if [ -z ${PROFILE} ]; then
  echo "Please provide a valid profile e.g.: $0 dev"
  exit -1
fi
echo "Going to run maven build and cloudfoundry deployment with profile: ${PROFILE}"
if [ -z ${USERNAME} ] || [ -z ${PASSWORD} ]; then
  cf login -o DZHW -s ${PROFILE} -a https://api.run.pivotal.io
else
  cf login -o DZHW -s ${PROFILE} -u ${USERNAME} -p ${PASSWORD} -a https://api.run.pivotal.io
fi
if [ $? -ne 0 ]; then
    echo "cf login failed!"
    exit -1
fi
mvn --no-transfer-progress -P${PROFILE} clean package
if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit -1
fi
cf push -f ./deploy/manifest-${PROFILE}.yml -s cflinuxfs3
if [ $? -ne 0 ]; then
    echo "cf push failed!"
    exit -1
fi
