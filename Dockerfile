FROM openjdk:11.0.5-jre-slim

MAINTAINER René Reitmann <reitmann@dzhw.eu>

VOLUME /tmp

# use unpacked spring boot jar to avoid file io
COPY target/app /app/
COPY run.sh run.sh

ENTRYPOINT ["/run.sh"]
