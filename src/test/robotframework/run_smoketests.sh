#!/bin/bash
# this script executes the robotframework tests tagged with 'smoketest' on
# all supported browsers against the dev stage, the tests are executed
# sequentially
if [[ $0 != ./src/* ]]; then
  echo "Please run this script from the root of our repository!"
  exit -1
fi
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
