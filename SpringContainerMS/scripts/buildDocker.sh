
echo "##########################################"
echo " Build Spring boot based Container microservice war and docker image  "
echo "##########################################"

log=$(ibmcloud target | grep "Not logged in")
if [[ -n "$log" ]]
then
  echo "You must login to IBMCLOUD before building"
  exit
fi
root_folder=$(cd $(dirname $0); cd ..; pwd)

if [[ $# -eq 0 ]];then
  kcenv="LOCAL"
else
  kcenv=$1
fi

. ./scripts/setenv.sh

   
ev=$(ibmcloud cdb deployables-show 2>&1 | grep "not a registered command")
if [[ -z "$ev" ]]
then
   ibmcloud plugin install cloud-databases   
fi
ibmcloud cdb deployment-cacert $ic_postgres_serv > postgresql.crt

find target -iname "*SNAPSHOT*" -print | xargs rm -rf
# rm -rf target/liberty/wlp/usr/servers/defaultServer/apps/expanded
tools=$(docker images | grep javatools)
if [[ -z "$tools" ]]
then
   mvn clean install -DskipITs
else
   docker run -v $(pwd):/home -ti ibmcase/javatools bash -c "cd /home &&  mvn clean install -DskipITs"
fi


docker build  -t ibmcase/$kname .
if [[ $kcenv == "IBMCLOUD" ]]
then
   # image for private registry in IBM Cloud
   echo "Tag docker image for $kname to deploy on $kcenv"
   docker tag ibmcase/$kname  us.icr.io/ibmcaseeda/$kname 
fi

if [[ $kcenv == "ICP" ]]
then
   # image for private registry in IBM Cloud Private
   echo "Tag docker image for $kname to deploy on $kcenv"
   docker tag ibmcase/$kname  $CLUSTER_NAME:8500/$ns/$kname 
fi