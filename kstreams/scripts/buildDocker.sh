
echo "##########################################"
echo " Build Container Streams microservice war and docker image  "
echo "##########################################"

if [[ $PWD = */scripts ]]; then
 cd ..
fi
if [[ $# -eq 0 ]];then
  kcenv="local"
else
  kcenv=$1
fi

. ./scripts/setenv.sh



find target -iname "*SNAPSHOT*" -print | xargs rm -rf
# rm -rf target/liberty/wlp/usr/servers/defaultServer/apps/expanded
tools=$(docker images | grep javatools)
if [[ -z "$tools" ]]
then
   mvn clean package -DskipITs
else
   docker run -v $(pwd):/home -ti ibmcase/javatools bash -c "cd /home &&  mvn clean install -DskipITs"
fi

if [[ $kcenv == "ICP" ]]
then
  if [ -f  es-cert.jks ]
  then
  	 mkdir -p target/liberty/wlp/usr/servers/defaultServer/resources/security
     cp es-cert.jks target/liberty/wlp/usr/servers/defaultServer/resources/security
  fi 
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