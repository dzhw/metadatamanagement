#!/bin/bash
# this script installs all dependencies for executing robotframework tests
export CXX=g++-4.9
export PYTHONWARNINGS="ignore"
sudo apt-get update -qq
sudo apt-get -y install python-pip python-dev --allow-unauthenticated
pip install urllib3[secure]==1.24.1 --user -Iv
pip install robotframework --user
pip install robotframework-extendedselenium2library --user
pip install robotframework-httplibrary --user
pip install pyyaml --user
pip install six --user
