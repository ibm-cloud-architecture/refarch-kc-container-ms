package ibm.labs.kc.containermgr.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KCKafkaConfiguration {

    public static Map<String, Object> getConsumerProperties(String groupID) {
    	Map<String, Object>  props = buildCommonProperties();
    	props.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
 	    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
 	    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
 	    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
 	    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
 	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    	return props;
    }


    public static Map<String, Object> getPublisherProperties(String clientID) {
	    Map<String, Object> props = buildCommonProperties();
	    props.put(ProducerConfig.RETRIES_CONFIG, 0);
	    props.put(ProducerConfig.CLIENT_ID_CONFIG,clientID);
	    /*
	    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
	    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_DOC,true);
	    props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, clientID);
	   */
	    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
	    props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

	    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    return props;
	}

    private static Map<String, Object> buildCommonProperties() {
    	Map<String, Object> properties = new HashMap<String,Object>();
      Map<String, String> env = System.getenv();

      if (env.get("KAFKA_BROKERS") == null) {
          throw new IllegalStateException("Missing environment variable KAFKA_BROKERS");
      }
      properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, env.get("KAFKA_BROKERS"));

      if (env.get("KAFKA_USER") != null && !env.get("KAFKA_USER").isEmpty() && env.get("KAFKA_PASSWORD") != null && !env.get("KAFKA_PASSWORD").isEmpty()) {
        properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        properties.put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLSv1.2");
        properties.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, "TLSv1.2");
        properties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "HTTPS");
        // If we are connecting to ES on IBM Cloud, the SASL mechanism is plain
        if ("token".equals(env.get("KAFKA_USER"))) {
          properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
          properties.put(SaslConfigs.SASL_JAAS_CONFIG,"org.apache.kafka.common.security.plain.PlainLoginModule required username=\"" + env.get("KAFKA_USER") + "\" password=\"" + env.get("KAFKA_PASSWORD") + "\";");
        }
        // If we are connecting to ES on OCP, the SASL mechanism is scram-sha-512
        else {
          properties.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-512");
          properties.put(SaslConfigs.SASL_JAAS_CONFIG,"org.apache.kafka.common.security.scram.ScramLoginModule required username=\"" + env.get("KAFKA_USER") + "\" password=\"" + env.get("KAFKA_PASSWORD") + "\";");
        }

        if ("true".equals(env.get("TRUSTSTORE_ENABLED"))){
          properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, env.get("TRUSTSTORE_PATH"));
          properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, env.get("TRUSTSTORE_PWD"));
        }
      }

      return properties;
    }

}
