#!/bin/sh
echo 'Start Data Generation \n'
toolsDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../tools && pwd )
python $toolsDIR/generateData_sensor_malfunction.py
echo '\n Done Generating \n'

#Publish to Kafka 
dataDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../data && pwd )
dataFile="$dataDIR/container_matrix_sensor_malfunction.csv"
echo $dataFile
python3 containerProducer.py $dataFile

#Test the Model
#ipython nbconvert --to python predictMaintainence.ipynb
#data = pd.read_csv('../data/container_matrix_door_open.csv', delimiter=",")
#ipython nbconvert --to python ../../predictiveModel/predictMaintainence.ipynb