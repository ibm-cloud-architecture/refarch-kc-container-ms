import time
import sys
import pprint
import uuid
import wiotp.sdk.device


def myCommandCallback(cmd):
    print("Command received: %s" % cmd.data)

# Configure


myConfig = wiotp.sdk.device.parseConfigFile("device.yaml")
client = wiotp.sdk.device.Client(config=myConfig, logHandlers=None)
client.setKeepAliveInterval(60)
client.commandCallback = myCommandCallback

# Connect


print("Connecting using keepalive interval of %s seconds" % (client.getKeepAliveInterval()))
client.connect()

# Send Data

myData = {'name': 'foo', 'cpu': 60, 'mem': 50}
client.publishEvent(event="status", msgFormat="json", data=myData, qos=0, on_publish=None)

# Disconnect
client.disconnect()
