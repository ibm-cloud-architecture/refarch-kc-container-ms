# Environment pre-requisites

## Deployment prerequisites

Regardless of specific deployment targets (OCP, IKS, k8s), the following prerequisite Kubernetes artifacts need to be created to support the deployments of application components.  These artifacts need to be created once per unique deployment of the entire application and can be shared between application components in the same overall application deployment.

1. Create `kafka-brokers` ConfigMap

  - Command: `kubectl create configmap kafka-brokers --from-literal=brokers='<replace with comma-separated list of brokers>' -n <namespace>`
  - Example: `kubectl create configmap kafka-brokers --from-literal=brokers='broker-3-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-2-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-1-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-5-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-0-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-4-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093' -n eda-refarch`

2. Create optional `eventstreams-apikey` Secret, if you are using Event Streams as your Kafka broker provider

  - Command: `kubectl create secret generic eventstreams-apikey --from-literal=binding='<replace with api key>' -n <namespace>`
  - Example: `kubectl create secret generic eventstreams-apikey --from-literal=binding='z...12345...notanactualkey...67890...a' -n eda-refarch`

3. If you are using Event Streams as your Kafka broker provider and it is deployed via the IBM Cloud Pak for Integration (ICP4I), you will need to create an additional Secret to store the generated Certificates & Truststores.

  - From the "Connect to this cluster" tab on the landing page of your Event Streams installation, download both the **Java truststore** and the **PEM certificate**.
  - Create the Java truststore Secret:
    - Command: `kubectl create secret generic <secret-name> --from-file=/path/to/downloaded/file.jks`
    - Example: `kubectl create secret generic es-truststore-jks --from-file=/Users/osowski/Downloads/es-cert.jks`
  - Create the PEM certificate Secret:
    - Command: `kubectl create secret generic <secret-name> --from-file=/path/to/downloaded/file.pem`
    - Example: `kubectl create secret generic es-ca-pemfile --from-file=/Users/osowski/Downloads/es-cert.pem`

4. Create `postgresql-ca-pem` Secret

  - Install the IBM Cloud Database CLI Plugin:
    - `ibmcloud plugin install cloud-databases`
  - Get the certificate using the name of the postgresql service:
    - `ibmcloud cdb deployment-cacert <name of postgresql instance> --save`
    - `export CERT_FILE=$(ibmcloud cdb deployment-cacert gse-eda-sandbox-db --save | grep "Certificate written to" | sed "s/Certificate written to //")`
  - Add it into an environment variable
    - `export POSTGRESQL_CA_PEM="$(cat CERT_FILE)"`
  - Create the secret:
    - `kubectl create secret generic postgresql-ca-pem --from-literal=binding="$POSTGRESQL_CA_PEM" -n browncompute`

5. Create `postgresql-url` Secret
  - Command: `kubectl create secret generic postgresql-url --from-literal=binding='<replace with postgresql url>' -n <namespace>`
  - Example: `kubectl create secret generic postgresql-url --from-literal=binding='jdbc:postgresql://58...77f.databases.appdomain.cloud:32569/ibmclouddb?sslmode=verify-full&sslfactory=org.postgresql.ssl.NonValidatingFactory' -n eda-refarch`

6. Create `postgresql-user` Secret
  - Command: `kubectl create secret generic postgresql-user --from-literal=binding='<replace with postgresql user>' -n <namespace>`
  - Example: `kubectl create secret generic postgresql-user --from-literal=binding='postgresqlUser' -n eda-refarch`

7. Create `postgresql-pwd` Secret
  - Command: `kubectl create secret generic postgresql-pwd --from-literal=binding='<replace with postgresql password>' -n <namespace>`
  - Example: `kubectl create secret generic postgresql-pwd --from-literal=binding='postgresqlPassword' -n eda-refarch`

## Local deployment

### Prepare minikube or docker edge / kubernetes

 - The solution can run on minikube, and once you have a minikube or docker community edition with kubernetes enabled, you can follow the instructions from [this note](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/minikube/).

### Or use docker compose

See how to use docker-compose to run backend service and the solution in [this article.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/local/)


## Public cloud deployment

### Prepare an IBM Kubernetes service cluster

See [this note on how to deploy a IKS cluster](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#kubernetes-cluster-service).

Define a docker image private registry using [this note.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#define-an-image-private-repository)

Define a namespace to deploy your solution. We used `greencompute` as namespace but can be anything.

```
kubectl create namespace greencompute
```

### Prepare Event Stream service on IBM Cloud

See [this note to deploy IBM kafka based product as a service on IBM Cloud](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#event-streams-service-on-ibm-cloud).

Get API Key and brokers URL. Then defines secrets for the api key as described [in this note.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#event-stream-api-key)

Add the private token to access the docker image registry, see [details in this note.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#private-registry-token)

### Prepare Postgresql service  on IBM Cloud

Follow [product documentation.](https://cloud.ibm.com/catalog/services/databases-for-postgresql)

Then add secrets for postgresql by following the procedure described [in this note.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#postgresql-url-user-pwd-and-ca-certificate-as-secrets)

## Private Cloud

For [ IBM Cloud Private deployments go to this article.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/icp/)

## Deploy to OpenShift Container Platform (OCP)

### Deploy to OCP 3.11

**Cross-component deployment prerequisites:** _(needs to be done once per unique deployment of the entire application)_
1. If desired, create a non-default Service Account for usage of deploying and running the K Container reference implementation.  This will become more important in future iterations, so it's best to start small:
  - Command: `oc create serviceaccount -n <target-namespace> kcontainer-runtime`
  - Example: `oc create serviceaccount -n eda-refarch kcontainer-runtime`
2. The target Service Account needs to be allowed to run containers as `anyuid` for the time being:
  - Command: `oc adm policy add-scc-to-user anyuid -z <service-account-name> -n <target-namespace>`
  - Example: `oc adm policy add-scc-to-user anyuid -z kcontainer-runtime -n eda-refarch`
  - NOTE: This requires `cluster-admin` level privileges.

**Perform the following for the `SpringContainerMS` microservice:**
1. Build and push the Docker image by one of the two options below:
  - Create a Jenkins project, pointing to the remote GitHub repository for the `voyages-ms` microservice, and manually creating the necessary parameters.  Refer to the individual microservice's [`Jenkinsfile.NoKubernetesPlugin`](https://github.com/ibm-cloud-architecture/refarch-kc-container-ms/blob/master/SpringContainerMS/Jenkinsfile.NoKubernetesPlugin) for appropriate parameter values.
  - Manually build the Docker image and push it to a registry that is accessible from your cluster (Docker Hub, IBM Cloud Container Registry, manually deployed Quay instance):
    - `docker build -t <private-registry>/<image-namespace>/kc-spring-container-ms:latest SpringContainerMS/`
    - `docker login <private-registry>`
    - `docker push <private-registry>/<image-namespace>/kc-spring-container-ms:latest`
3. Generate application YAMLs via `helm template`:
  - Parameters:
    - `--set image.repository=<private-registry>/<image-namespace>/<image-repository>`
    - `--set image.pullSecret=<private-registry-pullsecret>` (only required if pulling from an external private registry)
    - `--set kafka.brokersConfigMap=<kafka brokers ConfigMap name>`
    - `--set eventstreams.enabled=(true/false)` (`true` when connecting to Event Streams of any kind, `false` when connecting to Kafka directly)
    - `--set eventstreams.apikeyConfigMap=<kafka api key Secret name>`
    - `--set eventstreams.truststoreRequired=(true/false)` (`true` when connecting to Event Streams via ICP4I)
    - `--set eventstreams.truststoreSecret=<eventstreams jks file secret name>` (only used when connecting to Event Streams via ICP4I)
    - `--set eventstreams.truststorePassword=<eventstreams jks password>` (only used when connecting to Event Streams via ICP4I)
    - `--set postgresql.capemRequired=(true/false)` (`true` when connecting to Postgresql Services requiring SSL and CA PEM-secured communication)
    - `--set postgresql.capemSecret=<postgresql CA pem certificate Secret name>`
    - `--set postgresql.urlSecret=<postgresql url Secret name>`
    - `--set postgresql.userSecret=<postgresql user Secret name>`
    - `--set postgresql.passwordSecret=<postgresql password Secret name>`
    - `--set serviceAccountName=<service-account-name>`
    - `--namespace <target-namespace>`
    - `--output-dir <local-template-directory>`
  - Example using Event Streams via ICP4I:
   ```shell
   helm template --set image.repository=rhos-quay.internal-network.local/browncompute/kc-spring-container-ms --set kafka.brokersConfigMap=es-kafka-brokers --set eventstreams.enabled=true --set eventstreams.apikeyConfigMap=es-eventstreams-apikey --set eventstreams.truststoreRequired=true --set eventstreams.truststoreSecret=es-truststore-jks --set eventstreams.truststorePassword=password --set postgresql.capemRequired=true --set postgresql.capemSecret=postgresql-ca-pem --set postgresql.urlSecret=postgresql-url --set postgresql.userSecret=postgresql-user --set postgresql.passwordSecret=postgresql-pwd --set serviceAccountName=kcontainer-runtime --output-dir templates --namespace eda-refarch chart/springcontainerms
   ```
  - Example using Event Streams hosted on IBM Cloud:
  ```shell
  helm template --set image.repository=rhos-quay.internal-network.local/browncompute/kc-spring-container-ms --set kafka.brokersConfigMap=es-kafka-brokers --set eventstreams.enabled=true --set eventstreams.apikeyConfigMap=es-eventstreams-apikey --set postgresql.capemRequired=true --set postgresql.capemSecret=postgresql-ca-pem --set postgresql.urlSecret=postgresql-url --set postgresql.userSecret=postgresql-user --set postgresql.passwordSecret=postgresql-pwd --set serviceAccountName=kcontainer-runtime --output-dir templates --namespace eda-refarch chart/springcontainerms
  ```

4. Deploy application using `oc apply`:
  - `oc apply -f templates/springcontainerms/templates`
