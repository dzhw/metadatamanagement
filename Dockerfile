FROM openjdk:11.0.5-jre-slim

MAINTAINER René Reitmann <reitmann@dzhw.eu>

VOLUME /tmp

# use unpacked spring boot jar to avoid file io
COPY target/app /app/
COPY run.sh run.sh

# add kill agent for correct OutOfMemory Handling
RUN apt update \
 &&  apt install -y --no-install-recommends curl\
 &&  rm -rf /var/lib/apt/lists/*
RUN curl https://java-buildpack.cloudfoundry.org/jvmkill/bionic/x86_64/jvmkill-1.16.0-RELEASE.so --output /app/jvmkill.so

ENTRYPOINT ["/run.sh"]
