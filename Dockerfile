FROM openjdk:8-jdk-alpine
MAINTAINER René Reitmann <reitmann@dzhw.eu>

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.war"]

VOLUME /tmp

# Add the service itself
ARG JAR_FILE
COPY ${JAR_FILE} app.war
