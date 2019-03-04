import json
from confluent_kafka import KafkaError, Producer


KAFKA_BROKERS=os.environ['KAFKA_BROKERS']

containerProducer = Producer({
    'bootstrap.servers': KAFKA_BROKERS
})


def delivery_report(err, msg):
    """ Called once for each message produced to indicate delivery result.
        Triggered by poll() or flush(). """
    if err is not None:
        print('Message delivery failed: {}'.format(err))
    else:
        print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))


def publishEvent():
    data = {"timestamp": int(time.time()), "type": "OrderContainerAllocated", "version": "1", "payload": {"containerID": "c10", "orderID": orderID}}
    dataStr = json.dumps(data)
    containerProducer.produce('container', dataStr.encode('utf-8'), callback=delivery_report)
    containerProducer.flush()



container.flush()
