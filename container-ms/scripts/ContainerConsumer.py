import os,json
import sys
from confluent_kafka import Consumer, KafkaError, Producer, KafkaException

try:
    KAFKA_BROKERS = os.environ['KAFKA_BROKERS']
except KeyError:
    print("The KAFKA_BROKERS environment variable needs to be set.")
    exit

# See https://github.com/edenhill/librdkafka/blob/master/CONFIGURATION.md
orderConsumer = Consumer({
    #'bootstrap.servers': KAFKA_BROKERS,
    'bootstrap.servers': 'kafka1:9092',
    'group.id': 'python-containermetrics-consumer',
    'auto.offset.reset': 'earliest',
    'enable.auto.commit': True
})
orderConsumer.subscribe(['ContainerMetrics'])

def traceResponse(msg):
    containerMetrics = msg.value().decode('utf-8')
    print('@@@ pollNextMetric {} partition: [{}] at offset {} with key {}:\n\tvalue: {}'
                .format(msg.topic(), msg.partition(), msg.offset(), str(msg.key()), containerMetrics ))
    return containerMetrics

# def pollNextMetric():
#     gotIt = False
#     containerMetricsEvent = {}
#     while True:
#         msg = orderConsumer.poll(timeout=10.0)
#         if msg is None:
#             continue
#         if msg.error():
#             print("Consumer error: {}".format(msg.error()))
#             continue
#         containerMetrics = traceResponse(msg)
#         containerMetricsEvent = json.loads(containerMetricsEvent)
#     return containerMetricsEvent

try:
    while True:
        msg = orderConsumer.poll(timeout=1.0)
        if msg is None:
            continue
        if msg.error():
            raise KafkaException(msg.error())
        else:
            # Proper message
            sys.stderr.write('%% %s [%d] at offset %d with key %s:\n' %
                                (msg.topic(), msg.partition(), msg.offset(),
                                str(msg.key())))
            print(msg.value())

except KeyboardInterrupt:
    sys.stderr.write('%% Aborted by user\n')

finally:
    # Close down consumer to commit final offsets.
    orderConsumer.close()
