#!/bin/sh
echo 'Start Data Generation \n'
toolsDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../tools && pwd )

dataFrames=$(python -c 'import sys;import os;sys.path.insert(0,os.path.abspath("../../"));import tools.generateData_sensor_malfunction as m; print m.buildJSON()')
echo "$dataFrames"
echo "I M HERE"
echo '\n Done Generating \n'

#Publish to Kafka 
echo 'Publish Kafka \n'
python -c 'import ContainerProducer as m; dataFrames=$(python -c 'import sys;import os;sys.path.insert(0,os.path.abspath("../../"));import tools.generateData_sensor_malfunction as m; print m.buildJSON()'); m.publishEvent(dataFrames)'
#python containerProducer.py "echo $dataFrames"
echo 'Kafka Done \n'

#Test the Model
echo 'Testing Model'
modelDIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../predictiveModel && pwd )
jupyter nbconvert --to python "$modelDIR/predictMaintainence.ipynb"
python $modelDIR/predictMaintainence.py $testFile $modelFile 
echo 'Model Tested'


