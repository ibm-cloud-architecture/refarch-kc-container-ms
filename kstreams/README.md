# Reefer container management microservice done with kafka streams and microprofile

We recommend to read how we build this service explanations in [this book view](https://ibm-cloud-architecture.github.io/refarch-kc-container-ms/kstreams/)

## Build

Build with maven
```
mvn package
```

Compile, tests and build container
```
./scripts/buildDocker.sh LOCAL | IBMCLOUD | ICP
```

## Run

Use our script  `./script/startLocalLiberty` or use maven `mvn liberty:run-server`