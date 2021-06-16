#!/bin/bash
# this script installs all dependencies for executing robotframework tests
sudo apt-get update -qq
sudo apt-get -y install python-pip python-dev --allow-unauthenticated
pip install robotframework
pip install robotframework-seleniumlibrary==3.2.0
pip install robotframework-angularjs
pip install robotframework-requests
pip install pyyaml
pip install six
