#!/bin/sh

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../data && pwd )
dataFile="$DIR/container_matrix_door_open.csv"
echo $dataFile
python3 containerProducer.py $dataFile