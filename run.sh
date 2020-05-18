#!/bin/sh
cd app
exec java -agentpath:$PWD/jvmkill.so -noverify -XX:ActiveProcessorCount=$(nproc) -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.WarLauncher ${@}
