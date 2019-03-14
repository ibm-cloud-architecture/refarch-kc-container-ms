import os, time, sys
sys.path.append(os.path.join(os.path.dirname(os.path.dirname(sys.path[0])),'tools'))
import json
import csv
from confluent_kafka import KafkaError, Producer
from generateData import buildJSON

#KAFKA_BROKERS = os.environ['KAFKA_BROKERS']

containerProducer = Producer({
    'bootstrap.servers': 'localhost:9092'
})

class ContainerPublish:
    def delivery_report(this,err, msg):
         """ Called once for each message produced to indicate delivery result.
             Triggered by poll() or flush(). """
         if err is not None:
             print('Message delivery failed: {}'.format(err))
         else:
             print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))

    def publishEvent(self,data):
        print('PED', data)
        print('PUBLISH')
        dataStr = json.dumps(data)
        containerProducer.produce('ContainerMetrics', dataStr.encode('utf-8'), callback=ContainerPublisher.delivery_report)
        containerProducer.flush()


ContainerPublisher = ContainerPublish()

data = buildJSON(os.path.join(os.path.dirname(os.path.dirname(sys.path[0])),'data','container_matrix_door_open.csv'))

for i in data:
    print(i)
    ContainerPublisher.publishEvent(i)
    time.sleep(3)
