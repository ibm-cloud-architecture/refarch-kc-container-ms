#!/bin/sh

#Generate Data
echo 'Start Data Generation \n'
dataDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../data && pwd )
dataFile="$dataDIR/container_matrix_door_open.csv"
toolsDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../tools && pwd )
python $toolsDIR/generateData_door_open.py $dataFile
echo '\n Done Generating \n'

#Publish to Kafka 
echo 'Publish Kafka \n'
python containerProducer.py $dataFile
echo 'Kafka Done \n'

#Test the Model
echo 'Testing Model'
modelDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../predictiveModel && pwd )
#sudo pip install --ignore-installed numpy
#sudo pip install --ignore-installed pandas
#sudo pip install --ignore-installed scipy
#sudo pip install --ignore-installed sklearn
#sudo pip install --ignore-installed IPython
testFile="$dataDIR/container_matrix_test.csv"
testFile="$modelDIR/model.pkl"
#jupyter nbconvert --to python $modelDIR/predictMaintainence.ipynb
python $modelDIR/predictMaintainence.py $testFile $modelFile
echo 'Model Tested'
