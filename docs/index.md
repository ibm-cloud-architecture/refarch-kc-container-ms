# Container management microservice

This project is part of the container shipment implementation solution, and address the Reefer container management microservice implmentaiton. You can read more about the end to end solution [in this chapter.](https://ibm-cloud-architecture.github.io/refarch-kc/)

## TL;TR

The goal of this Container management service is to support the reefer containers inventory management and to process all the events related to the container entity. We are proposing three different implementations:

* Springboot and spring kafka template and spring postgreSQL. [See this note.](./springboot/README.md)
* Python with Flask and Confluent Kafka API for Python. See [this description.](./flask/README.md) - NOT DONE YET
* Microprofile 2.2 using Kafka Streams. See [this description.](./kstreams/README.md) - NOT DONE YET

We are demonstrating in this project how to transform an event storming analysis to an event-driven microservice implementation and how to address ['reversibility'](https://www.ibm.com/cloud/garage/practices/run/reversibility-in-the-cloud) between the different platform. The service is packaged via dockerfile, and helm release is defined to deploy to kubernetes.

## Analysis

We have a dedicated article on how to transform event storming analysis to microservice, [here](ES2DDD2MS). 

## Component view

As the service needs to offer some basic APIs while consuming and producing events, the code has at least three main components: a kafka consumer, a kafka producer, and a HTTP server exposing the REST APIs. The following diagram illustrates the componentes involved in this container manager microservice:

![](comp-view.png)

* A first component is responsible to define and expose APIs via RESTful protocol. It uses the services to delegate business logic implementation.
* The service component(s) addresses the business logic implementation and references data access object, and event producer.
* The event handler is a kafka consumer which runs continuously to get container events from the `container` topic. It invokes the service component.
* The event producer, produces events of interest to the business function
* The DAO implement the persistence for the data received as part of the event payload, or API.

## Container inventory

We are providing a tool to publish `container created` events to the Kafka `containers` topic. The python code is under the `tools` folder. It can be executed using our Python docker image with the command:

```shell
docker run -e KAFKA_BROKERS=$KAFKA_BROKERS -v $(pwd):/home --network=docker_default -ti ibmcase/python bash
root@2f049cb7b4f2:/ cd home
root@2f049cb7b4f2:/ python ProduceContainerCreatedEvent.py 
```


## Assign container to order

The implementation will search the list of containers closed to the source location. We simplify the implementation by assuming mapping container (longitude, latitude) position to be in an area closed to the harbor close to the pickup location. We do not manage the time when the container will be there. We assume containers is at location at the time of the order is processed, is the same as the time of the pickup. We may fine tune that if we can make it simple.

The output of this assignment processing is an event to the `orders` topic.


## Compendium

* [Postgresql tutorial](http://postgresguide.com/sql/select.html)
* [psql commands](https://www.postgresql.org/docs/9.2/app-psql.html)
* [Spring boot kafka documentation](https://docs.spring.io/spring-kafka/reference/)


