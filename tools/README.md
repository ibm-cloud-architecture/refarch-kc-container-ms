# Tools

See explanation for the predictive maintainence in [this note](http://https://ibm-cloud-architecture.github.io/refarch-kc-container-ms/metrics/#data-set)

## Generate data as file with the simulator

### Start python env

```
 ./startPythonEnv 

root@03721594782f: cd home
```

### Generate power off metrics

root@03721594782f: python reefer_simulator.py poweroff 101 1000 4 testdata yes

Generating  1000  poweroff metrics

Timestamp   ID  Temperature(celsius) Target_Temperature(celsius)      Power  PowerConsumption ContentType  O2 CO2  Time_Door_Open Maintenance_Required Defrost_Cycle
1.000000  2019-06-30 T15:43 Z  101              3.416766                           4  17.698034          6.662044           1  11   1        8.735273                    0             6
1.001001  2019-06-30 T15:43 Z  101              4.973630                           4   3.701072          8.457314           1  13   3        5.699655                    0             6
1.002002  2019-06-30 T15:43 Z  101              1.299275                           4   7.629094          

### Generate Co2 sensor malfunction in same file

In the same way as above the simualtor can generate data for Co2 sensor malfunction as well using the below command,
```
python reefer_simulator.py co2sensor 101 1000 4 testdata no
```

## To publish containerCreated event. 
