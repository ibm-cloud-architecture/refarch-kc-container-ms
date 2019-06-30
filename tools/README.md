# Tools


## How to generate data from simulator

We are generating data for two use cases now:

### 1. Poweroff: When the reefer containers lose power at some instance, the temperature within the container starts rising. To generate data for this scenario, use the following command;

 Enter into the docker container by executing the script - startPythonEnv.sh

 ./startPythonEnv 

root@03721594782f:/refarch-kc-container-ms/tools# python reefer_simulator.py 
Need to have at least 3 arguments: 
	the simulation type one of (poweroff, co2sensor)
	the number of records to generate
	expected temperature for the goods
	the filename to create (without .csv)
root@03721594782f:/refarch-kc-container-ms/tools# 
root@03721594782f:/refarch-kc-container-ms/tools# 
root@03721594782f:/refarch-kc-container-ms/tools# python reefer_simulator.py poweroff 1000 4 testdata

Generating  1000  poweroff metrics
                    Timestamp   ID  Temperature(celsius) Target_Temperature(celsius)      Power  PowerConsumption ContentType  O2 CO2  Time_Door_Open Maintenance_Required Defrost_Cycle
1.000000  2019-06-30 T15:43 Z  101              3.416766                           4  17.698034          6.662044           1  11   1        8.735273                    0             6
1.001001  2019-06-30 T15:43 Z  101              4.973630                           4   3.701072          8.457314           1  13   3        5.699655                    0             6
1.002002  2019-06-30 T15:43 Z  101              1.299275                           4   7.629094          7.174810           1  15   1        7.146702                    0             6
1.003003  2019-06-30 T15:43 Z  101              5.371080                           4   5.857190          6.542220           1   0   3        4.305004                    0             6
1.004004  2019-06-30 T15:43 Z  101              5.371080                           4   0.000000          7.145957           1   0   0        8.305748                    0             6
1.005005  2019-06-30 T15:43 Z  101              6.971080                           4   0.000000          6.438677           1   1   0        6.495389                    0             6
1.006006  2019-06-30 T15:43 Z  101              9.371080                           4   0.000000          5.993471           1   6   1        8.902084                    0             6
1.007007  2019-06-30 T15:43 Z  101              9.371080                           4   0.000000          8.821998           1   1   1        3.798932                    0             6
1.008008  2019-06-30 T15:43 Z  101             13.371080                           4   0.000000          5.868300           1   9   3        6.758773                    0             6
1.009009  2019-06-30 T15:43 Z  101             18.171080                           4   0.000000          7.438063           1   2   2        8.709629                    0             6
1.010010  2019-06-30 T15:43 Z  101             23.771080                           4   0.000000          6.565068           1  14   2        7.046192                    1             6
1.011011  2019-06-30 T15:43 Z  101              3.020879                           4   6.583316          6.849119           1   5   1        7.395089                    1             6
1.012012  2019-06-30 T15:43 Z  101              3.912796                           4  10.775266          9.598412           1  15   1        6.988813                    1             6
1.013013  2019-06-30 T15:43 Z  101              1.344214                           4   1.441938          6.860493           1   9   1       10.431439                    1             6
1.014014  2019-06-30 T15:43 Z  101              7.974819                           4  20.239473          5.566383           1  14   3        4.254404                    1             6
1.015015  2019-06-30 T15:43 Z  101              3.850651                           4  17.594946          8.252717           1   3   2        8.054185                    1             6
1.016016  2019-06-30 T15:43 Z  101              5.477895                           4  16.259322          8.728972           1   9   2        8.082536                    1             6


### 2. Co2 sensor malfunction: In the same way as above the simualtor can generate data for Co2 sensor malfunction as well using the below command,

python reefer_simulator.py co2sensor 1000 4 testdata


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
