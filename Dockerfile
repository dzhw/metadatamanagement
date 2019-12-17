FROM openjdk:11.0.5-jre-slim

MAINTAINER René Reitmann <reitmann@dzhw.eu>

ENTRYPOINT ["/run.sh"]

VOLUME /tmp

# Add the service itself
ARG JAR_FILE
COPY ${JAR_FILE} app.war
COPY run.sh run.sh
