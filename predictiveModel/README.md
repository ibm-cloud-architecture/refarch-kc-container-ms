# Predictive maintainence for reefer containers

In this section, we have a model trained to predict whether maintainence is required for the reefer container.

## Getting Started

The model uses the generated data from three scenarios: 1) when the container's door is open for a longer time - this gives a false positive that maintainence is required 2) when sensors are malfunctioning, it records arbitrary readings, 3) when the readings are normal. We have currently trained our model on 3000 datapoints from the three scenarios above. 

### Code execution
The simulator continuosly generates container metrics, publishes it to Kafka and run the predictMaintainence.ipynb to predict if maintainence is sought at this point in time. 

## Dataset

We do not have datset for reefer containers now. We have manually created the training and testing data. The container metrics genertaed by the simulator is also manually created. The dataset has the following format.

Timestamp,ID,Temperature(celsius),Target_Temperature(celsius),Amp,CumulativePowerConsumption,ContentType,Humidity,CO2,Door_Open,Maintainence_Required,Defrost_Cycle
====================================================================================================================================================================



## Result (prediction)

We are using Root Mean Squared Error (RMSE) for evaluating the model performance.

Root Mean Squared Error (RMSE) is the square root of the mean of the squared errors: 

<img src="https://latex.codecogs.com/svg.latex?\Large&space;x=\frac{-b\pm\sqrt{b^2-4ac}}{2a}" title="\Large x=\frac{-b\pm\sqrt{b^2-4ac}}{2a}" />

### Author

Shilpi Bhattacharyya
