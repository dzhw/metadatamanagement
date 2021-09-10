#!/bin/sh
cd app
exec java -agentpath:$PWD/jvmkill.so -noverify -XX:ActiveProcessorCount=$(nproc) -Djava.security.egd=file:/dev/./urandom --add-opens java.base/java.lang.reflect=ALL-UNNAMED org.springframework.boot.loader.WarLauncher ${@}
