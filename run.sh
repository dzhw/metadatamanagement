#!/bin/sh
exec java -XX:ActiveProcessorCount=$(nproc) -Djava.security.egd=file:/dev/./urandom -classpath /app.war org.springframework.boot.loader.WarLauncher ${@}
