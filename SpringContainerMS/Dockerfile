# Build stage - could use maven or our image
FROM maven:3.3-jdk-8 as builder
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
# this is to reuse local .m2. if not all the dependencies will be always downloaded
#  it can be removed 
VOLUME ${HOME}/.m2:/root/.m2
ADD . /usr/src/app

ARG POSTGRESQL_URL
ARG POSTGRESQL_USER
ARG POSTGRESQL_PWD
ARG KAFKA_BROKERS
ARG KAFKA_APIKEY
ARG KAFKA_ENV
# postgresql ssl certicates
ARG POSTGRESQL_CA_PEM 
# and event streams one
ARG ES_CA_PEM
RUN bash -c " export POSTGRESQL_URL=${POSTGRESQL_URL} \
	&&  export POSTGRESQL_USER=${POSTGRESQL_USER}\
	&& export POSTGRESQL_PWD=${POSTGRESQL_PWD} \
	&& export KAFKA_ENV=${KAFKA_ENV} \
	&& export KAFKA_BROKERS=${KAFKA_BROKERS} \
	&& export KAFKA_APIKEY=${KAFKA_APIKEY} \
	&& export ES_CA_PEM=\"${ES_CA_PEM}\" \
	&& export POSTGRESQL_CA_PEM=\"${POSTGRESQL_CA_PEM}\" \
	&& ./scripts/add_certificates.sh \
	&& mvn clean install -DskipITs -Djavax.net.ssl.trustStore=$JAVA_HOME/jre/lib/security/cacerts -Djavax.net.ssl.trustStorePassword=changeit"

# Deploy stage
# Use jre small foot print image. See https://hub.docker.com/_/ibmjava/
FROM ibmjava:8-sfj as runner
ENV APP_HOME=/app
COPY --from=builder /usr/src/app/target/SpringContainerMS-1.0-SNAPSHOT.jar $APP_HOME/app.jar
LABEL maintainer="IBM Java Engineering at IBM Cloud"

ENV POSTGRESQL_URL=""
ENV POSTGRESQL_USER=""
ENV POSTGRESQL_PWD=""
ENV KAFKA_BROKERS=""
ENV KAFKA_APIKEY=""
ENV POSTGRESQL_CA_PEM=""
ENV KAFKA_ENV=""
ENV ES_CA_PEM=""
# Install Extra Packages
RUN apt-get update \
 && apt-get install -y jq bash bc ca-certificates curl \
 && update-ca-certificates \
 && mkdir -p $APP_HOME/scripts

WORKDIR $APP_HOME

COPY scripts/startup.sh startup.sh
COPY scripts/add_certificates.sh scripts/

ENV JAVA_OPTS="" 

EXPOSE 8080:8080
ENTRYPOINT ["./startup.sh"]

