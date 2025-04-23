FROM eclipse-temurin:17-jre-focal

MAINTAINER René Reitmann <reitmann@dzhw.eu>

VOLUME /tmp

# use unpacked spring boot jar to avoid file io
COPY target/app /app/
COPY run.sh run.sh
COPY jvmkill.so.sha512 jvmkill.so.sha512

# install all available package updates
RUN apt-get update && apt-get upgrade -y -q && apt-get dist-upgrade -y -q

# add kill agent for correct OutOfMemory Handling
RUN apt update \
 &&  apt install -y --no-install-recommends curl\
 &&  rm -rf /var/lib/apt/lists/*\
 && curl https://java-buildpack.cloudfoundry.org/jvmkill/bionic/x86_64/jvmkill-1.16.0_RELEASE.so --output /app/jvmkill.so\
 && echo "$(cut -d' ' -f1 /jvmkill.so.sha512) /app/jvmkill.so" | sha512sum --check\
 && apt remove curl -y\
 && apt autoremove -y

ENTRYPOINT ["/run.sh"]
