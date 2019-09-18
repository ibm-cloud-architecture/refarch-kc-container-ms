#!/usr/bin/env bash

# Script we are executing
echo -e " \e[32m@@@ Excuting script: \e[1;33mstart_psql.sh \e[0m"

## Variables

# Get the absolute path for this file
SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
# Get the absolute path for the refarch-kc-container-ms project
MAIN_DIR=`echo ${SCRIPTPATH} | sed 's/\(.*refarch-kc-container-ms\).*/\1/g'`
# Absolute path to the main EDA reference architecture repository
MAIN_REPO="${MAIN_DIR}/../refarch-kc"

# Check if psql tool is available
PSQL=$(which psql)
if [ "$?" != "0" ]
then
  echo -e "\e[31m [ERROR] - The tool psql could not be found.\e[0m"
  echo -e "\e[31m           Please, make sure you install it before running the script.The tool psql could not be found.\e[0m"
  exit 1
fi

# Get the environment we are going to install to
if [[ $# -eq 0 ]];then
  kcenv="LOCAL"
else
  kcenv=$1
fi

# Set environment variables
source ${MAIN_REPO}/scripts/setenv.sh $kcenv
PGPASSWORD=$POSTGRESQL_PWD psql --host=localhost --port=5432 --username=$POSTGRESQL_USER  -d postgres
