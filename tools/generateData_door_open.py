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


with open('../../data/container_matrix_door_open.csv', mode='w') as container_file:
    container_writer = csv.writer(container_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL, lineterminator='\n')

    container_writer.writerow(['Timestamp', 'ID', 'Temperature(celsius)', 'Target_Temperature(celsius)', 'Amp', 'CumulativePowerConsumption', 'ContentType', 'Humidity', 'CO2', 'Door_Open', 
    'Maintainence_Required', 'Defrost_Cycle'])

    #door open sensor data
    id = random.randint(1001,2000)
    Today= datetime.datetime.today()
    date_list = [Today + datetime.timedelta(minutes=15*x) for x in range(0, 1000)]
    range_list=np.linspace(1,5,1000)
    index=0
    for i in  range_list:

        timestamp = date_list[index].strftime('%Y-%m-%d T%H:%M Z')
    	container_writer.writerow([timestamp, id, np.exp(np.random.uniform(i+0.1, i+0.5)), 4.4, gauss(2.5,1.0), gauss(10.0,2.0), random.randint(1,5), gauss(10.5, 5.5), gauss(10.5, 5.0), 1, 0, 6])
        index=index+1

print(buildJSON('../../data/container_matrix_door_open.csv'))


