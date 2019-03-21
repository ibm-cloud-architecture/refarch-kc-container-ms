import sys, time, json, os
from confluent_kafka import KafkaError, Consumer, KafkaException 

#try:
#    KAFKA_BROKERS = os.environ['KAFKA_BROKERS']
#except KeyError:
 #   print("The KAFKA_BROKERS environment variable needs to be set.")
 #   exit

containerConsumer = Consumer({
    'bootstrap.servers': 'localhost:9092',
    'group.id':'test-group',
    'default.topic.config': {'auto.offset.reset': 'latest'} 
})

topics = ['orders','containers', 'ContainerMetrics']

def print_assignment(consumer, partitions):
        print('Assignment:', partitions)

containerConsumer.subscribe(topics)

def pollMessages():
    running = True
    while running:
        msg = containerConsumer.poll()
        if not msg.error():
            print("Good Message:")
            print("    Topic: " + str(msg.topic()))
            print("    Partition: " + str(msg.partition()))
            print("    Offset: " + str(msg.offset()))
            pass
        else:
            if msg.error().code() == KafkaError._PARTITION_EOF:
                print("Error Message:")
                print("    Topic: " + str(msg.topic()))
                print("    Partition: " + str(msg.partition()))
                print("    Offset: " + str(msg.offset()))
                print("    Error: " + str(msg.error()))
                running = True
            else:
                print(msg.error())
                running = True

print("\nPolling...")
pollMessages()
print("\nWaiting for 3 seconds for broker to repopulate...")
time.sleep(3)
print("\nSecond Poll...")
pollMessages()
