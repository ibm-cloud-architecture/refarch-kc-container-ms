# Environment pre-requisites

## Deployment prerequisites

Regardless of specific deployment targets (OCP, IKS, k8s), the following prerequisite Kubernetes artifacts need to be created to support the deployments of application components.  These artifacts need to be created once per unique deployment of the entire application and can be shared between application components in the same overall application deployment.

1. Create `kafka-brokers` ConfigMap
  - Command: `kubectl create configmap kafka-brokers --from-literal=brokers='<replace with comma-separated list of brokers>' -n <namespace>`
  - Example: `kubectl create configmap kafka-brokers --from-literal=brokers='broker-3-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-2-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-1-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-5-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-0-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093,broker-4-j7fxtxtp5fs84205.kafka.svc01.us-south.eventstreams.cloud.ibm.com:9093' -n eda-refarch`
2. Create optional `eventstreams-apikey` Secret, if you are using Event Streams as your Kafka broker provider
  - Command: `kubectl create secret generic eventstreams-apikey --from-literal=binding='<replace with api key>' -n <namespace>`
  - Example: `kubectl create secret generic eventstreams-apikey --from-literal=binding='z...12345...notanactualkey...67890...a' -n eda-refarch`

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
1. Build and push Docker image
  1. Create a Jenkins project, pointing to the remote GitHub repository for the Order Microservices, creating the necessary parameters.  Refer to the individual microservice's `Jenkinsfile.NoKubernetesPlugin` for appropriate parameter values.
    - Create a String parameter named `REGISTRY` to determine a remote registry that is accessible from your cluster.
    - Create a String parameter named `REGISTRY_NAMESPACE` to describe the registry namespace to push image into.
    - Create a String parameter named `IMAGE_NAME` which should be self-expalantory.
    - Create a String parameter named `CONTEXT_DIR` to determine the correct working directory to work from inside the source code, with respect to the root of the repository.
    - Create a String parameter named `DOCKERFILE` to determine the desired Dockerfile to use to build the Docker image.  This is determined with respect to the `CONTEXT_DIR` parameter.
    - Create a Credentials parameter named `REGISTRY_CREDENTIALS` and assign the necessary credentials to allow Jenkins to push the image to the remote repository.
  2. Manually build the Docker image and push it to a registry that is accessible from your cluster (Docker Hub, IBM Cloud Container Registry, manually deployed Quay instance):
    - `docker build -t <private-registry>/<image-namespace>/kc-spring-container-ms:latest order-command-ms/`
    - `docker login <private-registry>`
    - `docker push <private-registry>/<image-namespace>/kc-spring-container-ms:latest`
3. Generate application YAMLs via `helm template`:
  - Parameters:
    - `--set image.repository=<private-registry>/<image-namespace>/<image-repository>`
    - `--set image.tag=latest`
    - `--set image.pullSecret=<private-registry-pullsecret>` (optional or set to blank)
    - `--set image.pullPolicy=Always`
    - `--set eventstreams.env=ICP`
    - `--set eventstreams.brokersConfigMap=<kafka brokers ConfigMap name>`
    - `--set serviceAccountName=<service-account-name>`
    - `--namespace <target-namespace>`
    - `--output-dir <local-template-directory>`
  - Example: `helm template --set image.repository=rhos-quay.internal-network.local/browncompute/kc-spring-container-ms --set image.tag=latest --set image.pullSecret= --set image.pullPolicy=Always --set eventstreams.env=ICP --set eventstreams.brokersConfigMap=kafka-brokers --set serviceAccountName=kcontainer-runtime --output-dir templ --namespace eda-refarch chart/springcontainerms/`
4. Deploy application using `oc apply`:
  - `oc apply -f templates/springcontainerms/templates`
