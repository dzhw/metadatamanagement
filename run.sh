#!/bin/sh
exec java -noverify -XX:+PrintFlagsFinal -XX:-TieredCompilation -XX:ActiveProcessorCount=$(nproc) -Djava.security.egd=file:/dev/./urandom -classpath /app.war org.springframework.boot.loader.WarLauncher ${@}
