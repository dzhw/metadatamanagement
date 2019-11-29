FROM openjdk:11.0.5-slim
MAINTAINER René Reitmann <reitmann@dzhw.eu>

ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -noverify -jar /app.jar

VOLUME /tmp

# Add the service itself
ARG JAR_FILE
COPY ${JAR_FILE} app.war
