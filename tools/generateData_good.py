import csv
import json
from random import gauss
import random
import datetime
import numpy as np

containerData = []
def buildJSON(csvfile):
    with open(csvfile) as csvfile:
        dataReader = csv.DictReader(csvfile)
        for row in dataReader:
            x = json.dumps(row)
            containerData.append(x)
        return containerData


with open('../data/container_matrix.csv', mode='w') as container_file:
    container_writer = csv.writer(container_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL, lineterminator='\n')

    container_writer.writerow(['Timestamp', 'ID', 'Temperature(celsius)', 'Target_Temperature(celsius)', 'Amp', 'CumulativePowerConsumption', 'ContentType', 'Humidity', 'CO2', 'Door_Open', 
    'Maintainence_Required', 'Defrost_Cycle'])

    #good sensor data
    id = random.randint(1001,2000)
    Today= datetime.datetime.today()
    date_list = [Today + datetime.timedelta(minutes=15*x) for x in range(0, 1000)]

    for i in range(0, 1000):

        timestamp = date_list[i].strftime('%Y-%m-%d T%H:%M Z')
    	container_writer.writerow([timestamp, id, gauss(4.0,0.4), 4.4, gauss(2.5,1.0), gauss(10.0,2.0), random.randint(1,5), 
        gauss(10.5, 5.5), gauss(10.5, 5.0), 0, 0, 6])

print(buildJSON('../data/container_matrix.csv'))


