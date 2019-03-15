# KStream implementation of the container inventory management

The [Apache Kafka Streams API](https://kafka.apache.org/documentation/streams/) is a client library for building applications and microservices, where the input and output data are stored in Kafka clusters. It simplifies the implementation of the stateless or stateful event processing to transform and enrich data. It supports time windowing processing.

We encourage to do [this KStream tutorial](https://kafka.apache.org/21/documentation/streams/tutorial). 

The features we want to illustrate using KStreams are:

* Listen to ContainerAddedToInventory event from the `containers` topic and maintains a stateful table of containers. 
* Listen to OrderCreated event from `orders` and assign a container from the inventory based on the pickup location and the container location and its characteristics.
* Implemented as JAXRS application deployed on Liberty and packaged with dockerfile.
* Deploy to kubernetes or run with docker-compose

## Start with maven

Kafka stream delivers a Maven archetype to create a squeleton project. The following command can be used to create the base code.
```sh
mvn archetype:generate -DarchetypeGroupId=org.apache.kafka -DarchetypeArtifactId=streams-quickstart-java     -DarchetypeVersion=2.1.0     -DgroupId=kc-container     -DartifactId=kc-container-streams    -Dversion=0.1     -Dpackage=containerManager
```

We added a `.project` file to develop the code in Eclipse, imported the code into Eclipse and modify the `.classpath` with the following lines:
```xml
    <classpathentry kind="con" path="org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER">
		<attributes>
			<attribute name="maven.pomderived" value="true"/>
		</attributes>
	</classpathentry>
```

To access to serializer and testing framework we added the following dependencies in the pom.xml:

```xml
      <dependency>
    		<groupId>org.apache.kafka</groupId>
    		<artifactId>kafka-clients</artifactId>
    		<version>${kafka.version}</version>
		</dependency>
		<dependency>
		    <groupId>org.apache.kafka</groupId>
		    <artifactId>kafka-streams-test-utils</artifactId>
		    <version>${kafka.version}</version>
		    <scope>test</scope>
		</dependency>
```

## Some Kafka streams APIs

The stream configuration looks similar to consumer and producer configuration using the Kafka APIs. The new class is the KStream to manage a stream of structured events. It represents unbounded collection of immutable data elements or events.

```java
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "container-streams");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");    
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
```
The StreamsConfig is a specific configuration for Streams app.

Two classes are supporting the order and container processing:

* [ContainerInventoryView](https://github.com/ibm-cloud-architecture/refarch-kc-container-ms/blob/master/kstreams/src/main/java/ibm/labs/kc/streams/containerManager/ContainerInventoryView.java)
* [ContainerOrderAssignment](https://github.com/ibm-cloud-architecture/refarch-kc-container-ms/blob/master/kstreams/src/main/java/ibm/labs/kc/streams/containerManager/ContainerOrderAssissgnment.java)

We are using the Streams DSL API to do the processing. Here is an example of terminal stream to print what is coming to the `orders` topic:
```java
   final StreamsBuilder builder = new StreamsBuilder();
    builder.stream("orders")
        .foreach((key,value) -> {
        			Order order = parser.fromJson((String)value, OrderEvent.class).getPayload();
        			// TODO do something to the order
        			System.out.println("received order " + key + " " + value);
        		});

    final Topology topology = builder.build();
    final KafkaStreams streams = new KafkaStreams(topology, props);
    streams.start();
```
Before going to far let start by the tests.

See also the Streams DSL API [here](https://kafka.apache.org/21/documentation/streams/developer-guide/dsl-api.html). 

## Test Driven Development

We want to document two major test suites. One for building the internal view of the container inventory, the other for container to order assignments.

### 

To test a stream application without Kafka backbone there is a test utility available [here](https://kafka.apache.org/documentation/streams/developer-guide/testing.html#unit-testing-processors).

The test is illustrating how to use the [TopologyTestDriver](https://kafka.apache.org/21/javadoc/org/apache/kafka/streams/TopologyTestDriver.html).

The business logic we want to implement is to get an order with the source pickup city, the type of product, the quantity and the expected pickup date, manage the internal list of containers and search for a container located close to the pickup city from the order.
The test is under kstreams/src/test/java/ut. 

### Run test

Recall with maven we can run all the unit tests, one test and skip integration tests.

```shell
# Test a unique test
$  mvn -Dtest=TestContainerInventory test
# Skip all tests
mvn install -DskipTests
# Keep integration test
mvn install -DskipITs
```

To start the liberty server use the script: `./script/startLocalLiberty` or `mvn liberty:run-server`

## Resiliency

## Scaling