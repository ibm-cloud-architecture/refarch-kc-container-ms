# Reefer Container Metric as IoT

The reefer container is a Internet of Thing device that run motor, compressor to maintain cold inside the container.

![Reefer](images/reefer.png)

The fleetms microservice is generating simulated container metrics events to emulate the container on ship. But this project will add capabilities to generate a lot of data to create training and test set for machine learning exercises for predictive maintentane and cold chain scoring.

## Data set creation for container metrics

As we are not in the business of reefer container shipment we do not have data set. 


## Predictive maintenance
The success of predictive maintenance models depend on three main components: 

* having the right data
* framing the problem appropriately 
* evaluating the predictions properly

### Reefer problem types:

There are multiple different potential of issues that could happen to a refrigerator container. We are choosing to model the "Sensor Malfunctions: Sensors in the refrigeration unit need to be calibrated and cotnuously operational. An example of failure may come from the air sensor making inaccurate readings of temperatures, which lead to sploiled content. A potential reason may come from a faulty calibration, which can go unnoticed for a good time period. It may be diffiult to know if there is an issue. 

Other potential issues are:
* Fluid leaks, like engine oil, coolant liquid. The preassure sensors added to the circuit may help identify preassure lost over time.
* Faulty belts and hoses.
* Faulty calibration: A non-calibrated reefer can cool at a slower or faster rate than desired.
* Damaged Air Chute.
* Condenser Issues like broken or damaged coils, clamps or bolts missing, and leaks.
* Door Seals damaged. 
* Blocked air passage: to keep the temperature homogenous inside the reefer.

### Data set

Well we do not have data. But we may be able to simulate them. As this is not production work, we should be able to get the end to end story still working from a solution point of view.

The historical data need to represent failure, and represent the characteristics of a Reefer container. We can imagine it includes a lot of sensors to get interesting correlated or independant features.

We propose to code a simulator to create the training and test sets so we can build the model inside Jupiter notebook and with sklearn library. The simulator will be also use as an injector to real time event on loaded containers, used to travel goods, so we can trigger a maintenance order process.

Here is a diagram for the data scientist environment:

![](images/ds-env.png)


For modeling predictive maintenance we found [this article](https://medium.com/bigdatarepublic/machine-learning-for-predictive-maintenance-where-to-start-5f3b7586acfb) from BigData Republique, on Medium, very interesting. 


