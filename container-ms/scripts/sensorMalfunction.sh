#!/bin/sh

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../data && pwd )
dataFile="$DIR/container_matrix_sensor_malfunction.csv"
echo $dataFile
python3 containerProducer.py $dataFile