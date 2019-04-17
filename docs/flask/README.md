# Python Flask implementation of the container inventory management

In this chapter we will implement a python based container inventory management service using kafka and flask. The component can be represented in the figure below:

![](../images/flask-container.png)

The container topics includes all events about container life cycle.  Each event published to the container topic will have a corresponding action that will update the container's status.  

##Container Actions

* ContainerAddedToInventory

* ContainerRemovedFromInventory

* ContainerAtLocation

* ContainerOnMaintenance

* ContainerOffMaintenance

* ContainerAssignedToOrder

* ContainerReleasedFromOrder

* ContainerGoodLoaded

* ContainerGoodUnLoaded

* ContainerOnShip

* ContainerOffShip

* ContainerOnTruck

* ContainerOffTruck


##Container States

* OnShip

* OffShip

* AtDock

* OnTruck

* OffTruck

* Loaded

* Empty
