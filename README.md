# Reefer Container management microservice

This project is part of the container shipment implementation solution, and address the Reefer container management microservice implmentation. You can read more about the end to end solution [in this chapter.](https://ibm-cloud-architecture.github.io/refarch-kc/)

The goal of this Container management service is to support the reefer containers inventory management and to process all the events related to the container entity. 

We are documenting the best practices related to the different implementations of this service in [a book format](https://ibm-cloud-architecture.github.io/refarch-kc-container-ms) to make the navigation easier. 

Updated 08/18/2019.

### Building this booklet locally

The content of this repository is written with markdown files, packaged with [MkDocs](https://www.mkdocs.org/) and can be built into a book-readable format by MkDocs build processes.

1. Install MkDocs locally following the [official documentation instructions](https://www.mkdocs.org/#installation).
1. Install Material plugin for mkdocs:  `pip install mkdocs-material` 
2. `git clone https://github.com/ibm-cloud-architecture/refarch-kc-container-ms.git` _(or your forked repository if you plan to edit)_
3. `cd refarch-kc-container-ms`
4. `mkdocs serve`
5. Go to `http://127.0.0.1:8000/` in your browser.

### Pushing the book to GitHub Pages

1. Ensure that all your local changes to the `master` branch have been committed and pushed to the remote repository.
   1. `git push origin master`
2. Ensure that you have the latest commits to the `gh-pages` branch, so you can get others' updates.
	```bash
	git checkout gh-pages
	git pull origin gh-pages
	
	git checkout master
	```
3. Run `mkdocs gh-deploy` from the root refarch-da directory.

--- 

## Contribute

We welcome your contributions. There are multiple ways to contribute: report bugs and improvement suggestion, improve documentation and contribute code.
We really value contributions and to maximize the impact of code contributions we request that any contributions follow these guidelines:

The [contributing guidelines are in this note.](./CONTRIBUTING.md)

## Contributors

* [Shilpi Bhattacharyya](https://www.linkedin.com/in/shilpibhattacharyya/)
* [Hemankita Perabathini](https://www.linkedin.com/in/hemankita-perabathini/)
* [Jerome Boyer](https://www.linkedin.com/in/jeromeboyer/)

## Springboot - kafka - postgres container management microservice

### TODO - update as Appsody project from java-spring-boot2

For better documentation [read the book format.](https://ibm-cloud-architecture.github.io/refarch-kc-container-ms/springboot/)

The code was started using IBM Cloud Microservice Starter for [Spring](https://spring.io/)

## Build

### Requirements

* [Maven](https://maven.apache.org/install.html)
* [Docker](https://www.docker.com/products/docker-desktop)
* [Our root project to get access to the needed local backend services](https://github.com/ibm-cloud-architecture/refarch-kc)
* Java 8: Any compliant JVM should work.
  * [Java 8 JDK from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  * [Java 8 JDK from IBM (AIX, Linux, z/OS, IBM i)](http://www.ibm.com/developerworks/java/jdk/)

* Prepare your environment and modify the `scripts/setenv.tmpl.sh` with the service credentials and rename it as `scripts/setenv.sh`.
* Start the back end services in the `refarch-kc` project, in the `docker` folder: `docker-compose -f backbone-compose.yml up`, you should see the following message and then a long trace:

```
Starting docker_zookeeper1_1  ... done
Creating docker_postgres-db_1 ... done
Starting docker_kafka1_1      ... done
```

### Build

Use maven if you already have it on your laptop. 

* In one terminal set the different environment variables by using: `./scripts/setenv.sh LOCAL`
* `mvn install`
If you do not want to install Maven locally you can use `Dockerfile-tools` to build a container with Maven installed.

Or use our dockerfile and scripts to build and prepare the image:

* `./scripts/buildDocker.sh LOCAL

The script uses a multi-stage dockerfile to build and run the app.

## Run Locally

To run the application:

* If not done already run `./scripts/setenv.sh LOCAL`
* `java -jar ./target/SpringContainerMS-1.0-SNAPSHOT.jar`

## Deploy to IBM Cloud

To deploy this application to IBM Cloud using a toolchain click the **Create Toolchain** button.
[![Create Toolchain](https://console.ng.bluemix.net/devops/graphics/create_toolchain_button.png)](https://console.ng.bluemix.net/devops/setup/deploy/)


### Project contents
The ports are set to the defaults of 8080 for http and 8443 for https and are exposed to the CLI in the cli-config.yml file.

The project contains IBM Cloud specific files that are used to deploy the application as part of a IBM Cloud DevOps flow. The `.bluemix` directory contains files used to define the IBM Cloud toolchain and pipeline for your application. The `manifest.yml` file specifies the name of your application in IBM Cloud, the timeout value during deployment and which services to bind to.

This microservice application is configured to connect to the following services :

Credentials are either taken from the VCAP_SERVICES environment variable that IBM Cloud provides or from environment variables passed in by the config file `src/main/resources/localdev-config.json`.


### Endpoints

The application exposes the following endpoints:

*  Health    : http://localhost:8080/actuator/health
* Application: http://localhost:8080/containers/ 

The ports are set in the pom.xml file and exposed to the CLI in the cli-config.yml file.


