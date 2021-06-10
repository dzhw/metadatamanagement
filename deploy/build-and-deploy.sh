#!/bin/bash
# this script builds the project with a given profile and deploys it
# to Amazon ECR and AWS Fargate
if [[ $0 != ./deploy/* ]]; then
  echo "Please run this script from the root of our repository!"
  exit -1
fi
PROFILE="$1"
BRANCH_NAME="$2"
if [ -n "${BRANCH_NAME}" ]; then
  PROFILE="dev"
fi
if [ "${BRANCH_NAME}" = "master" ]; then
  PROFILE="prod"
fi
if [ "${BRANCH_NAME}" = "test" ]; then
  PROFILE="test"
fi
if [ -z ${PROFILE} ]; then
  echo "Please provide a valid profile e.g.: $0 dev"
  exit -1
fi
echo "Going to run maven build with profile: ${PROFILE}"
mvn --settings ./.github/workflows/.github.settings.xml --no-transfer-progress -P${PROFILE} clean install javadoc:javadoc
if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit -1
fi
AWS_CRED=($(aws sts assume-role --role-arn arn:aws:iam::347729458675:role/Admin --role-session-name github-deployment --query 'Credentials.[AccessKeyId,SecretAccessKey,SessionToken]' --output text))
export AWS_ACCESS_KEY_ID=${AWS_CRED[0]}
export AWS_SECRET_ACCESS_KEY=${AWS_CRED[1]}
export AWS_SESSION_TOKEN=${AWS_CRED[2]}
$(aws ecr get-login --no-include-email --region eu-central-1)
mvn -P${PROFILE} dockerfile:push dockerfile:push@push-image-latest
if [ $? -ne 0 ]; then
    echo "Maven Docker push failed!"
    exit -1
fi
aws ecs list-tasks --cluster ${PROFILE} --service metadatamanagement | jq -r ".taskArns[]" | awk -v profile=${PROFILE} '{print "aws ecs stop-task --cluster "profile " --task \""$0"\""}' | sh
aws ecs list-tasks --cluster ${PROFILE} --service metadatamanagement-worker | jq -r ".taskArns[]" | awk -v profile=${PROFILE} '{print "aws ecs stop-task --cluster "profile " --task \""$0"\""}' | sh
if [ $? -ne 0 ]; then
    echo "Task redeployment failed!"
    exit -1
fi
