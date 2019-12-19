#!/bin/sh
cd app
exec java -noverify -XX:ActiveProcessorCount=$(nproc) -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.WarLauncher ${@}
