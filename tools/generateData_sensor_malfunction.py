import csv
import json
from random import gauss
import random
import datetime
import numpy as np
import sys
import pandas as pd

containerData = []
class jsonBuilder:
    def buildJSON(df):
        
        d = [dict([
            (colname, row[i]) 
            for i,colname in enumerate(df.columns)]) for row in df.values]
        return json.dumps(d)


df = pd.DataFrame(columns=['Timestamp', 'ID', 'Temperature(celsius)', 'Target_Temperature(celsius)', 'Amp', 'CumulativePowerConsumption', 'ContentType', 'Humidity', 'CO2', 'Time_Door_Open', 
'Maintainence_Required', 'Defrost_Cycle'])
#faulty sensor data
id = random.randint(1001,2000)
Today= datetime.datetime.today()
date_list = [Today + datetime.timedelta(minutes=15*x) for x in range(0, 1000)]
range_list=np.linspace(1,2,1000)
index=0
for i in range_list:

    timestamp = date_list[index].strftime('%Y-%m-%d T%H:%M Z')
    df.loc[i] = [timestamp, id, gauss(5.0, 2.0), 4.4, gauss(2.5,1.0), gauss(10.0,2.0), random.randint(1,5),gauss(10.5, 5.5), gauss(10.5, 5.0), gauss(8.0, 2.0), 1, 6]
    index=index+1


JB=jsonBuilder()
JB.buildJSON(df)
#print(buildJSON(df))
#print(buildJSON(sys.argv[1]))

