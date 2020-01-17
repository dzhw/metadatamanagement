#!/bin/bash
# this script builds the project with a given profile and deploys it
# to Amazon ECR and AWS Fargate
if [[ $0 != ./deploy/* ]]; then
  echo "Please run this script from the root of our repository!"
  exit -1
fi
PROFILE="$1"
TRAVIS_BRANCH="$2"
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
echo "Going to run maven build with profile: ${PROFILE}"
mvn --settings .travis.settings.xml --no-transfer-progress -P${PROFILE} clean package
if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit -1
fi
