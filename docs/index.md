# Container management microservice

This project is part of the container shipment implementation solution you can read detail [here.](https://ibm-cloud-architecture.github.io/refarch-kc/).

The goal of this Container management service is to support the reefer containers inventory management and to process all the events related to the container entity. We want to support the following events:

* ContainerAddedToInventory, ContainerRemovedFromInventory
* ContainerAtLocation
* ContainerOnMaintenance, ContainerOffMaintenance, 
* ContainerAssignedToOrder, ContainerReleasedFromOrder
* ContainerGoodLoaded, ContainerGoodUnLoaded
* ContainerOnShip, ContainerOffShip
* ContainerOnTruck, ContainerOffTruck

This repository illustrate how to implement the assignContainerToOrder(order) function in different ways:

* As a REST API end point calleable from other services.
* As a kafka streams consumer of orderCreated event published in the Kafka `orders` topic: the code will look at the good pickup location and search in the container inventory the container close to this location. 
* As a kafka streams agent consuming container events from the `containers` topic and managing a stateful Ktable to keep container inventory in memory.


The implementation will search the list of containers closed to the source location. We simplify the implementation by assuming mapping container (longitude, latitude) position to be in an area closed to the harbor close to the pickup location. We do not manage the time when the container will be there. We assume containers is at location at the time of the order is processed, is the same as the time of the pickup. We may fine tune that if we can make it simple.

The output of this assignment processing is an event to the `orders` topic.