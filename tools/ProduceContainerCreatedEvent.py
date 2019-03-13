"""
Kafka producer to publish new container created event, to simulate inventory update from another backend
"""
from confluent_kafka import KafkaError, Producer
import json, time, os

try:
    KAFKA_BROKERS = os.environ['KAFKA_BROKERS']
except KeyError:
    print("The KAFKA_BROKERS environment variable needs to be set.")
    exit

def delivery_report(err, msg):
    """ Called once for each message produced to indicate delivery result.
        Triggered by poll() or flush(). """
    if err is not None:
        print('Message delivery failed: {}'.format(err))
    else:
        print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))

def postContainerCreated(containerID):
    containerProducer = Producer({'bootstrap.servers': KAFKA_BROKERS})
    data = {"timestamp": int(time.time()),
        "type":"ContainerCreated",
        "version":"1",
        "payload": {"containerID": containerID,
            "brand": "Reefer Best Inc",
            "type": "reefer 18",
            "capacity": 100,
            "latitude": 37.8000,
            "longitude": -122.25,
            "status": "available"
            }
        }
    dataStr = json.dumps(data)
    containerProducer.produce('containers',value=dataStr.encode('utf-8'),key= containerID.encode('utf-8'), callback=delivery_report)
    containerProducer.flush()


if __name__ == '__main__':
    cid=input("constainer ID")
    postContainerCreated(cid)