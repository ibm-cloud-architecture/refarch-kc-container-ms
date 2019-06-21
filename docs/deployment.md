# Environment pre-requisites

## Local deployment

### Prepare minikube or docker edge / kubernetes

 - The solution can run on minikube, and once you have a minikube or docker community edition with kubernetes enabled, you can follow the instructions from [this note](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/minikube/).

### Or use docker compose

See how to use docker-compose to run backend service and the solution in [this article.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/local/)


## Public cloud deployment

### Prepare an IBM Kubernetes service cluster

See [this note on how to deploy a IKS cluster](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#kubernetes-cluster-service). 

Define a docker image private registry using [this note.](https://ibm-cloud-architecture.github.io/refarch-kc/deployments/iks/#define-an-image-private-repository)

Define a namespace to deploy your solution. We used `browncompute` as namespace but can be anything.

```
kubectl create namespace browncompute
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


