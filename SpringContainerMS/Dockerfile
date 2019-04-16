# Build stage
FROM maven:3.3-jdk-8 as builder
# FROM ibmcase/java8build as builder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
ADD . /usr/src/app
ARG POSTGRESQL_URL
ARG POSTGRESQL_USER
ARG POSTGRESQL_PWD
ARG KAFKA_BROKERS
ARG KAFKA_APIKEY
ARG KAFKA_ENV
RUN bash -c "export POSTGRESQL_URL=${POSTGRESQL_URL} \
	&&  export POSTGRESQL_USER=${POSTGRESQL_URL}\
	&& export POSTGRESQL_PWD=${POSTGRESQL_URL} \
	&& mvn clean install -DskipITs"

# Deploy stage
# Use jre small footpring image. See https://hub.docker.com/_/ibmjava/
FROM ibmjava:8-sfj
COPY --from=builder /usr/src/app/target/SpringContainerMS-1.0-SNAPSHOT.jar $APP_HOME/app.jar
LABEL maintainer="IBM Java Engineering at IBM Cloud"


ENV APP_HOME=/app
ENV POSTGRESQL_URL=""
ENV POSTGRESQL_USER=""
ENV POSTGRESQL_PWD=""
# Install Extra Packages
RUN apt-get update \
 && apt-get install -y jq bash bc ca-certificates curl \
 && update-ca-certificates \
 && mkdir -p $APP_HOME/scripts

WORKDIR $APP_HOME

COPY script/startup.sh startup.sh
COPY scripts/add_certificates.sh scripts/

ENV JAVA_OPTS="" 


# Create user, chown, and chmod
RUN adduser -u 2000 -G root -D blue \
	&& chown -R 2000:0 $APP_HOME \
	&& chmod -R u+x $APP_HOME/app.jar
	
ENTRYPOINT ["./startup.sh"]

