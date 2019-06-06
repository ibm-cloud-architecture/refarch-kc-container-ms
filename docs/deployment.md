# Environment pre-requisites

## Local deployment

### Prepare minikube or docker edge / kubernetes

<TBD>

### Or use docker compose

See how to use docker-compose to run backend service and the solution in [this article.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/local/)


## Public cloud deployment

### Prepare an IBM Kubernetes service cluster

See [this note](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#kubernetes-cluster-service). 

Define a docker image private registry using [this note.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#define-an-image-private-repository)

Define a namespace to deploy your solution. We used `browncompute` but can be anything.

### Prepare Event Stream service on IBM Cloud

See [this note](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#event-streams-service-on-ibm-cloud).

Get API Key and brokers URL. Then defines secrets for the api key as described [here.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#event-stream-api-key)

Add the private token to access the image registry, see [this note.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#private-registry-token)

### Prepare Postgresql service  on IBM Cloud

Follow [product documentation.](https://cloud.ibm.com/catalog/services/databases-for-postgresql)

Then add secrets for postgresql by following the commands described [here.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#postgresql-url-user-pwd-and-ca-certificate-as-secrets)

## Private Cloud 

See [this note.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/icp/)


