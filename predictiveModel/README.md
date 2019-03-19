# Predictive maintainence for reefer containers

In this section, we have a model trained to predict whether maintainence is required fore the reefer conatiner

## Getting Started

The model uses the generated data from two scenarios: 1) when the container's door is open for a longer time - this gives a false positive that maintainence is required 2) when sensors are malfunctioning, it records arbitrary readings, 3) when the readings are normal. We have currently trained our model on 3000 datapoints from the three scenarios above. 

### Code execution
The simulator continuosly generates container metrics, publishes it to Kafka and run the predictMaintainence.ipynb to predict if maintainence is sought at this point in time. 


###Author

Shilpi Bhattacharyya
