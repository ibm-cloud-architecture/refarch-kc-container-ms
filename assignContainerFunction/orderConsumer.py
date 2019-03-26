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

topics = ['orders']

containerConsumer.subscribe(topics)

def pollMessages():
    running = True
    while running:
        msg = containerConsumer.poll()
        if not msg.error():
            print("    Topic: " + (msg.topic()))
            print("    Message: " + (msg.value()))
            if msg.topic() == 'orders':
                print('MESSAGE TOPIC = ORDERS')
                print('Message Data: ', msg.value())
        else:
            if msg.error().code() == KafkaError._PARTITION_EOF:
                print("\n\nError Message: End of Messages")
                running = False
            else:
                print(msg.error())
                running = False

print("\nPolling...")
pollMessages()
print("\nWaiting for 10 seconds for broker to repopulate...")
time.sleep(10)
print("\nSecond Poll...")
pollMessages()
