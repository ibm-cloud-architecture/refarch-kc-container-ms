import os
import json
import csv
from confluent_kafka import KafkaError, Producer
import generateData

KAFKA_BROKERS = os.environ['KAFKA_BROKERS']

containerProducer = Producer({
    'bootstrap.servers': KAFKA_BROKERS
})

data = generateData.buildJSON('containerData.csv')
print('Data', data[0])

def delivery_report(err, msg):
    """ Called once for each message produced to indicate delivery result.
        Triggered by poll() or flush(). """
    if err is not None:
        print('Message delivery failed: {}'.format(err))
    else:
        print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))


def publishEvent():
    dataStr = json.dumps(data)
    containerProducer.produce('containers', dataStr.encode('utf-8'), callback=delivery_report)
    containerProducer.flush()
