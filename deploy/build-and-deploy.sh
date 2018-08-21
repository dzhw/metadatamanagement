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
COVERALLS_TOKEN="$5"
TRAVIS_EVENT_TYPE="$6"
if [ "${TRAVIS_EVENT_TYPE}" = "cron" ]; then
  echo "Skipping build and deploy steps..."
  export PYTHONWARNINGS="ignore"
  echo "Testing E2E with Chrome:"
  ROBOT_CHROME="robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/chrome -v USE_SAUCELABS:TRUE -v BROWSER:chrome --exclude smoketest --exclude firefoxonly ./src/test/robotframework"
  ROBOT_FIREFOX="robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/firefox -v USE_SAUCELABS:TRUE -v BROWSER:firefox --exclude smoketest --exclude chromeonly ./src/test/robotframework"
  ROBOT_IE11="robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/ie11 -v USE_SAUCELABS:TRUE -v BROWSER:ie --exclude smoketest --exclude firefoxonly --exclude chromeonly ./src/test/robotframework"
  ROBOT_EDGE="robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs/edge -v USE_SAUCELABS:TRUE -v BROWSER:edge --exclude smoketest --exclude firefoxonly --exclude chromeonly ./src/test/robotframework"
  parallel ::: "${ROBOT_CHROME}" "${ROBOT_FIREFOX}" "${ROBOT_IE11}" "${ROBOT_EDGE}"
  if [ $? -ne 0 ]; then
      echo "At least one E2E test failed!"
      exit -1
  fi
  echo "All E2E Tests passed."
  exit 0
fi
if [ -n "${TRAVIS_BRANCH}" ]; then
  PROFILE="dev"
fi
if [ "${TRAVIS_BRANCH}" = "master" ]; then
  PROFILE="prod"
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
if [ -z ${COVERALLS_TOKEN} ]; then
  mvn -P${PROFILE} clean package
else
  mvn -P${PROFILE} clean -DrepoToken=$COVERALLS_TOKEN package coveralls:report
fi
if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit -1
fi
cf push -f ./deploy/manifest-${PROFILE}.yml
if [ $? -ne 0 ]; then
    echo "cf push failed!"
    exit -1
fi
if [ "${PROFILE}" = "dev" ]; then
  export PYTHONWARNINGS="ignore"
  echo "Smoke Testing E2E with Chrome:"
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs -v USE_SAUCELABS:TRUE -v BROWSER:chrome --include smoketest --exclude firefoxonly ./src/test/robotframework
  if [ $? -ne 0 ]; then
      echo "E2E test with Chrome failed!"
      exit -1
  fi
  echo "Smoke Testing E2E with Firefox:"
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs -v USE_SAUCELABS:TRUE -v BROWSER:firefox --include smoketest --exclude chromeonly ./src/test/robotframework
  if [ $? -ne 0 ]; then
      echo "E2E test with Firefox failed!"
      exit -1
  fi
  echo "Smoke Testing E2E with IE11:"
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs -v USE_SAUCELABS:TRUE -v BROWSER:ie --include smoketest --exclude firefoxonly --exclude chromeonly --exclude noslowpoke ./src/test/robotframework
  if [ $? -ne 0 ]; then
      echo "E2E test with IE11 failed!"
      exit -1
  fi
  echo "Smoke Testing E2E with Edge:"
  robot -P ./src/test/robotframework/libs -d target/test/robotframework/logs -v USE_SAUCELABS:TRUE -v BROWSER:edge --include smoketest --exclude firefoxonly --exclude chromeonly --exclude noslowpoke ./src/test/robotframework
  if [ $? -ne 0 ]; then
      echo "E2E test with Edge failed!"
      exit -1
  fi
fi
