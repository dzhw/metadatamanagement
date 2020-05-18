#!/bin/bash
# unjar the spring boot fat jar
JAR="$1"
mkdir -p target/app
cd target/app
jar -xf ../${JAR}
