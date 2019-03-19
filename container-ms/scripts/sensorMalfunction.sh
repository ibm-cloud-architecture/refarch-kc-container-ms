#!/bin/sh
echo 'Start Data Generation \n'
toolsDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../tools && pwd )
python $toolsDIR/generateData_sensor_malfunction.py
echo '\n Done Generating \n'

#Publish to Kafka 
echo 'Publish Kafka \n'
dataDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../data && pwd )
dataFile="$dataDIR/container_matrix_sensor_malfunction.csv"
echo $dataFile
python3 containerProducer.py $dataFile
echo 'Kafka Done \n'

#Test the Model
echo 'Testing Model'
modelDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../predictiveModel && pwd )
jupyter nbconvert --to python "$modelDIR/predictMaintainence.ipynb"
data=pd.read_csv("$dataDIR/container_matrix_sensor_malfunction.csv", delimiter=",")
echo 'Model Tested'