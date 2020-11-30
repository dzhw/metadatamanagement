#!/bin/bash
# this script installs all dependencies for executing robotframework tests
sudo apt-get update -qq
sudo apt-get -y install python-pip python-dev --allow-unauthenticated
pip install urllib3[secure]==1.24.1 -Iv
pip install robotframework
pip install robotframework-extendedselenium2library
pip install robotframework-httplibrary
pip install pyyaml
pip install six
