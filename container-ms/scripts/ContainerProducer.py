import os, time, sys
sys.path.append(os.path.join(os.path.dirname(os.path.dirname(sys.path[0])),'tools'))
import json
import csv
from confluent_kafka import KafkaError, Producer
from generateData import buildJSON

#KAFKA_BROKERS = os.environ['KAFKA_BROKERS']

containerProducer = Producer({
    'bootstrap.servers': 'localhost:2181'
})

data = buildJSON(os.path.join(os.path.dirname(os.path.dirname(sys.path[0])),'data','containerData.csv'))
print('Data', json.dumps(data, indent=4, sort_keys=True))

class KafkaPublish:
    def delivery_report(err, msg):
         """ Called once for each message produced to indicate delivery result.
             Triggered by poll() or flush(). """
         if err is not None:
             print('Message delivery failed: {}'.format(err))
         else:
             print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))

    def publishEvent(data):
        print('pE')
        dataStr = json.dumps(data)
        containerProducer.produce('ContainerMetrics', dataStr.encode('utf-8'), callback=delivery_report)
        containerProducer.flush()
