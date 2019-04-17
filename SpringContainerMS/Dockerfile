# Build stage - could use maven or our image
# FROM maven:3.3-jdk-8 as builder
FROM ibmcase/java8build as builder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
# this is to reuse local .m2 - can be remove. if not all the dependencies will be always downloaded
VOLUME ${HOME}/.m2:/root/.m2
ADD . /usr/src/app

ARG POSTGRESQL_URL
ARG POSTGRESQL_USER
ARG POSTGRESQL_PWD
ARG KAFKA_BROKERS
ARG KAFKA_APIKEY
ARG KAFKA_ENV
ARG ES_CA_PEM
RUN bash -c " export POSTGRESQL_URL=${POSTGRESQL_URL} \
	&&  export POSTGRESQL_USER=${POSTGRESQL_USER}\
	&& export POSTGRESQL_PWD=${POSTGRESQL_PWD} \
	&& export KAFKA_ENV=${KAFKA_ENV} \
	&& export KAFKA_BROKERS=${KAFKA_BROKERS} \
	&& export KAFKA_APIKEY=${KAFKA_APIKEY} \
	&& export  ES_CA_PEM=\"${ES_CA_PEM}\" \
	&& ./scripts/add_certificates.sh \
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
ENV KAFKA_BROKERS=""
ENV KAFKA_APIKEY=""
ENV KAFKA_ENV=""
ENV  ES_CA_PEM=""
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

USER 2000
EXPOSE 8080:8080
ENTRYPOINT ["./startup.sh"]

