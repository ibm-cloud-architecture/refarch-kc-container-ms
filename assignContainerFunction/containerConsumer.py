import sys, time, json, os
from confluent_kafka import KafkaError, Consumer, KafkaException 

#try:
#    KAFKA_BROKERS = os.environ['KAFKA_BROKERS']
#except KeyError:
 #   print("The KAFKA_BROKERS environment variable needs to be set.")
 #   exit

containerConsumer = Consumer({
    'bootstrap.servers': 'kafka1:9092',
    'group.id':'test-group',
    'default.topic.config': {'auto.offset.reset': 'earliest'} 
})

topics = ['containers']

containerConsumer.subscribe(topics)

try:
    while True:
        msg = containerConsumer.poll(timeout=1.0)
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
    containerConsumer.close()
