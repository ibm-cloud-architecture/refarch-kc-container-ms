#!/bin/sh
echo 'Start Data Generation \n'
toolsDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../tools && pwd )
dataFrames=python $toolsDIR/generateData_sensor_malfunction.py 
echo $dataFrames
echo '\n Done Generating \n'

#Publish to Kafka 
echo 'Publish Kafka \n'
python containerProducer.py $dataFrames
echo 'Kafka Done \n'

#Test the Model
echo 'Testing Model'
modelDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../predictiveModel && pwd )
jupyter nbconvert --to python "$modelDIR/predictMaintainence.ipynb"
python $modelDIR/predictMaintainence.py $testFile $modelFile 
echo 'Model Tested'