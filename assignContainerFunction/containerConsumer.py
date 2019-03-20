import sys, time, json, os
from confluent_kafka import KafkaError, Consumer 

try:
    KAFKA_BROKERS = os.environ['KAFKA_BROKERS']
except KeyError:
    print("The KAFKA_BROKERS environment variable needs to be set.")
    exit
