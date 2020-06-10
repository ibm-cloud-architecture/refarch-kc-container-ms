#!/usr/bin/env bash

# Script we are executing
echo -e " \e[32m@@@ Excuting script: \e[1;33mrun.sh \e[0m"

## Variables

# Get the absolute path for this file
SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
# Get the absolute path for the refarch-kc-container-ms project
MAIN_DIR=`echo ${SCRIPTPATH} | sed 's/\(.*refarch-kc-container-ms\).*/\1/g'`
# Absolute path for the SpringBoot version of the microservice
MAIN_DIR_SPRING="${MAIN_DIR}/SpringContainerMS"
# Absolute path to the main EDA reference architecture repository
MAIN_REPO="${MAIN_DIR}/../refarch-kc"
# Name of the application
APP_NAME="SpringContainerMS-1.0-SNAPSHOT.jar"



# Checking the main EDA referecnce architecture repository is at the expected location
if [ ! -d "${MAIN_REPO}" ]
then
  echo -e "\e[31m [ERROR] - This script expects the main EDA reference architure repository (i.e refarch-kc) cloned outside the actual project at the same level.\e[0m"
  echo -e "\e[31m           That is:\e[0m"
  echo -e "\e[31m           - Working directory\e[0m"
  echo -e "\e[31m                  |- refarch-kc\e[0m"
  echo -e "\e[31m                  |- refarch-kc-container-ms\e[0m"
  echo -e "\e[31m                  |- ...\e[0m"
  echo -e "           Please clone the main EDA reference architecture repository in the appropriate location \e[36m(https://github.com/ibm-cloud-architecture/refarch-kc)\e[0m"

  exit -1
fi

# Read the environment we are going to deploy the application to.
if [[ $# -eq 0 ]];then
  kcenv="LOCAL"
else
  kcenv=$1
fi

# Read environment variables
source ${MAIN_REPO}/scripts/setenv.sh $kcenv

# Checking the application executable exists
if [ ! -f "${MAIN_DIR_SPRING}/target/${APP_NAME}" ]
then
  echo -e "\e[31m [ERROR] - The application executable file (${MAIN_DIR_SPRING}/target/${APP_NAME}) does not exist.\e[0m"
  echo -e "\e[31m           Please, build the file first. That is execute:\e[0m"
  echo -e "             pushd ${MAIN_DIR_SPRING}/target"
  echo -e "             mvn clean package"
  echo -e "             popd"

  exit -1
fi

# Set basic java options
export JAVA_OPTS="${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Djavax.net.ssl.trustStore=$JKS_LOCATION -Djavax.net.ssl.trustStorePassword=$TRUSTSTORE_PWD"
# Execute the application
java ${JAVA_OPTS} -jar  ${MAIN_DIR_SPRING}/target/${APP_NAME} application.SBApplication
