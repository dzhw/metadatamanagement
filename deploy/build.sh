#!/bin/bash
# this script builds the project with a given profile
if [[ $0 != ./deploy/* ]]; then
  echo "Please run this script from the root of our repository!"
  exit -1
fi
PROFILE="$1"
TRAVIS_BRANCH="$2"
if [ "${PROFILE}" = "unused" ]; then
  PROFILE="dev"
fi
if [ "${TRAVIS_BRANCH}" = "test" ]; then
  PROFILE="test"
fi
if [ "${TRAVIS_BRANCH}" = "master" ]; then
  PROFILE="prod"
fi
if [ -z ${PROFILE} ]; then
  echo "Please provide a valid profile e.g.: $0 dev"
  exit -1
fi
echo "Going to run maven build with profile: ${PROFILE}"
mvn --settings .travis.settings.xml --no-transfer-progress -P${PROFILE} clean install
if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit -1
fi
if [ "${TRAVIS_BRANCH}" = "rreitmann/aws-deployment" ]; then
  pip install --user awscli
  sudo apt-get -y install jq
  mkdir ~/.aws
  cp ./deploy/aws/* ~/.aws/
  echo "aws_secret_access_key = $AWS_SECRET_ACCESS_KEY" >> ~/.aws/credentials
  echo "aws_access_key_id = $AWS_ACCESS_KEY_ID" >> ~/.aws/credentials
  $(aws ecr get-login --no-include-email --region eu-central-1 --profile mdm)
  mvn dockerfile:push dockerfile:push@push-image-latest
  aws ecs list-services --cluster metadatamanagement-dev --profile mdm | jq -r ".serviceArns[]" | awk '{print "aws ecs update-service --cluster metadatamanagement-dev --profile mdm --force-new-deployment  --service \""$0"\""}' | sh
fi
