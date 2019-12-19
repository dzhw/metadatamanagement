#!/bin/bash
# this script builds the project with a given profile
JAR="$1"
mkdir -p target/app
cd target/app
jar -xf ../${JAR}
