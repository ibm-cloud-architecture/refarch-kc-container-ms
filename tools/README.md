# Tools

## To create dataset
We do not have real-world reefer container metrics. So, we are creating the dataset with certain assumptions as below:

In this ection, we discuss our strategy on how we generate datset in three scenarios, which we are considering at this point in time.

1. When the door of the reefer container is open

    We are using exponential distribution here to generate data which varies at a very fast rate.

2. When there is a sensor malfunction (or sensors are incorrectly calibrated)

    We are using random gaussian distribution here to generate data with arbitrary variations

3. When the reefer container is functioning as expected (baseline/groundtruth for our model)

    We are using uniform distribution here to generate expected data.

## To publish containerCreated event. 
