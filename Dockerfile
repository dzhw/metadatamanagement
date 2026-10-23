FROM eclipse-temurin:21-jre-ubi10-minimal

LABEL org.opencontainers.image.authors="René Reitmann <reitmann@dzhw.eu>"

VOLUME /tmp

# use unpacked spring boot jar to avoid file io
COPY target/app /app/
COPY run.sh run.sh
COPY jvmkill.so.sha512 jvmkill.so.sha512

RUN curl https://java-buildpack.cloudfoundry.org/jvmkill/bionic/x86_64/jvmkill-1.16.0_RELEASE.so --output /app/jvmkill.so\
 && echo "$(cut -d' ' -f1 /jvmkill.so.sha512) /app/jvmkill.so" | sha512sum --check

ENTRYPOINT ["/run.sh"]
