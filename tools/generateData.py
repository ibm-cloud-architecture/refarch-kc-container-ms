import csv
import json

containerData = []
def buildJSON(csvfile):
    with open(csvfile, newline='') as csvfile:
        dataReader = csv.DictReader(csvfile)
        for row in dataReader:
            x = json.dumps(row)
            containerData.append(x)
        return containerData
