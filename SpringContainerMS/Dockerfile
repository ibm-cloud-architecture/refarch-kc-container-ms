FROM ibmjava:8-sfj
LABEL maintainer="IBM Java Engineering at IBM Cloud"

COPY target/SpringContainerMS-1.0-SNAPSHOT.jar /app.jar
COPY postgresql.crt.der /postgresql.crt.der
RUN  \
    echo yes | keytool -importcert -alias postgresql -keystore \
      $JAVA_HOME/lib/security/cacerts -storepass changeit -file /postgresql.crt.der

ENV JAVA_OPTS="" 
ENV POSTGRESQL_URL=""
ENV POSTGRESQL_USER=""
ENV POSTGRESQL_PWD=""
ENTRYPOINT [ "sh", "-c", "java  $JAVA_OPTS  -jar /app.jar" ]
