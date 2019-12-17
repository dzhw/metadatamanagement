#!/bin/sh
exec java -noverify -XX:ActiveProcessorCount=$(nproc) -Djava.security.egd=file:/dev/./urandom -classpath /app.war org.springframework.boot.loader.WarLauncher ${@}
