package ibm.labs.kc.containermgr.kafka;

import java.util.logging.Logger;
import java.util.Collections;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import ibm.labs.kc.model.events.ContainerAnomalyEvent;
import ibm.labs.kc.model.events.ContainerAnomalyEventDead;
import ibm.labs.kc.model.events.ContainerAnomalyEventRetry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class BpmAgent {
	private static final Logger LOG = Logger.getLogger(BpmAgent.class.toString());

	@Value("${kcsolution.bpm_anomaly_service_login}")
	private String bpm_anomaly_service_login;
	@Value("${kcsolution.bpm_anomaly_service}")
	private String bpm_anomaly_service;
	@Value("${kcsolution.bpm_anomaly_service_user}")
	private String bpm_anomaly_service_user;
	@Value("${kcsolution.bpm_anomaly_service_password}")
	private String bpm_anomaly_service_password;
	@Value("${kcsolution.bpm_anomaly_service_expiration:60}")
	private String bpm_anomaly_service_expiration;

	private String bpm_token = "";

	@Autowired
	private ContainerAnomalyRetryProducerImpl containerAnomalyRetryProducer;

	@Autowired
	private ContainerAnomalyDeadProducerImpl containerAnomalyDeadProducer;

	private String getBPMToken() throws Exception {
		LOG.info("Get BPM token");

		int max_retries = 3; //This config could be externalized for more flexibility
		int retries = 0;

		while (retries < max_retries) {
			LOG.info("Attempt " + retries);

			// create headers
			HttpHeaders headers_login = new HttpHeaders();
			// Set the basicAuth header parameter
			headers_login.setBasicAuth(bpm_anomaly_service_user, bpm_anomaly_service_password);
			// set `content-type` header
			headers_login.setContentType(MediaType.APPLICATION_JSON);
			// set `accept` header
			headers_login.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			// create an instance of RestTemplate
			RestTemplate restTemplate_login = new RestTemplate();

			// Create the JSON object for post parameters
			JsonObject dataJson = new JsonObject();
			dataJson.addProperty("refresh_groups", true);
			dataJson.addProperty("requested_lifetime", bpm_anomaly_service_expiration);

			// build the request
			HttpEntity<String> entity_login = new HttpEntity<String>(dataJson.toString(), headers_login);

			try {
				// send POST request
				ResponseEntity<String> response_login = restTemplate_login.postForEntity(bpm_anomaly_service_login,
						entity_login, String.class);
				// check response status code
				if (response_login.getStatusCode() == HttpStatus.CREATED) {
					LOG.info("BPM authentication login successful.");
					JsonObject jsonObject = new JsonParser().parse(response_login.getBody()).getAsJsonObject();
					return jsonObject.get("csrf_token").getAsString();
				} else {
					LOG.severe("[ERROR] - An error occurred getting authenticated with BPM. Please, make sure your credentials are valid and BPM is reachable.");
					LOG.severe("[ERROR] - Returned status: " + response_login.getStatusCode().toString());
					retries++;
					continue;
				}
			} catch (Exception ex) {
				// Exception is handled in the calling method
				LOG.severe("[ERROR] - An exception occurred during BPM authentication: " + ex.getMessage());
				throw new Exception("BPM authentication exception");
			}
		}
		// Finally, if we can not get authenticated to BPM, we throw an exception.
		LOG.severe("[ERROR] - The BPM authentication call did eventually not succeed after " + max_retries + " retries.");
		throw new Exception("No BPM authentication attempts left");
	}

	public void callBPM(ContainerAnomalyEvent cae, boolean expired) {

		boolean goodBPMToken = true;

		if (!expired) LOG.info("Call to BPM");

		// Check if we need to call BPM or it has been disabled (for integration tests for instance)
		if (System.getProperty("bpm_anomaly_service_enabled") == "false") {
			LOG.info("BPM service disabled. Bypassing it.");
		}
		else {
			// Manage BPM token
			if (bpm_token == "" || expired) {
				// Logging
				if (bpm_token == "") LOG.info("No BPM token - Calling the BPM authentication service");
				if (expired) LOG.info("BPM token expired - Calling the BPM authentication service");
				// Get the BPM token
				try {
					bpm_token = getBPMToken();
				} catch (Exception ex) {
					/*
					* DEAD
					* We Consider not being able to authenticate with BPM a severe problem as it does not
					* make sense to retry calling a service which we can't authenticate against.
					*/
					toDeadTopic(cae, ex.getMessage());
					goodBPMToken = false;
				}
			}
			// We have a good BPM token so we proceed to make the call
			if (goodBPMToken) {
				LOG.info("Call the BPM process");

				// create headers
				HttpHeaders headers_maintenance = new HttpHeaders();
				// Set the basicAuth header parameter
				headers_maintenance.setBasicAuth(bpm_anomaly_service_user, bpm_anomaly_service_password);
				// set the BPMCSRFToken header
				headers_maintenance.set("BPMCSRFToken", bpm_token);
				// set `content-type` header
				headers_maintenance.setContentType(MediaType.APPLICATION_JSON);
				// set `accept` header
				headers_maintenance.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

				// create an instance of RestTemplate
				RestTemplate restTemplate_maintenance = new RestTemplate();

				// Create the map for post parameters
				Map<String, List> data = new HashMap<>();
				List<Map> input_list = new ArrayList<Map>();
				Map<String, Object> input_map = new HashMap<>();
				input_map.put("name", "incomingWarning");
				input_map.put("data", cae.getBPMMessage());
				input_list.add(input_map);
				data.put("input", input_list);

				// build the request
				HttpEntity<Map<String, List>> entity_maintenance = new HttpEntity<>(data, headers_maintenance);

				try {
					// send POST request
					ResponseEntity<String> response_maintenance = restTemplate_maintenance.postForEntity(bpm_anomaly_service, entity_maintenance, String.class);
					if (response_maintenance.getStatusCode() == HttpStatus.CREATED) {
						LOG.info("BPM service call successful.");
					} 
					else{
						LOG.severe("[ERROR] - An error occurred calling the BPM service container anomaly process.");
						LOG.severe("[ERROR] - Response code: " + response_maintenance.getStatusCodeValue());
						LOG.severe("[ERROR] - Response to string: " + response_maintenance.toString());
						// Retry the call to BPM
						toRetryTopic(cae);
					}
				}
				// We can use the response codes to determine where the anomaly event has to go
				// We can send it to the containerAnomaly retry or to the dead letter queue
				catch (HttpClientErrorException.Forbidden ex) {
					// BPM token expired
					callBPM(cae, true);
				}

				catch (HttpClientErrorException ex) {
					LOG.severe("[ERROR] - An error occurred calling the BPM service");
					LOG.severe("[ERROR] - Status code: " + ex.getStatusCode().toString());
					LOG.severe("[ERROR] - Exception to String: " + ex.toString());
					// Retry the call to BPM
					toRetryTopic(cae);
				}
			}
		}
	}

	private void toRetryTopic(ContainerAnomalyEvent cae){
		ContainerAnomalyEventRetry caer;

		// Add one retry to ContainerAnomalyEventRetry or create a new one
		if (cae instanceof ContainerAnomalyEventRetry){
			caer = (ContainerAnomalyEventRetry)cae;
			caer.setRetries(caer.getRetries()+1);
		}
		else caer = new ContainerAnomalyEventRetry(cae,1);

		if (caer.getRetries() > 3){
			// send the event to the container anomaly dead queue
			toDeadTopic(cae,"No more BPM process retries left");
		}
		else {
			// Send the event to the container anomaly retry queue
			LOG.info("Sending ContainerAnomalyEventRetry event for containerID: " + cae.getContainerID() + " to the container anomaly retry topic");
			containerAnomalyRetryProducer.emit(caer);
		}
	}

	private void toDeadTopic(ContainerAnomalyEvent cae, String reason){

		LOG.info("Sending ContainerAnomalyEventDead event for containerID: " + cae.getContainerID() + " to the container anomaly dead topic");
		LOG.info("Reason: " + reason);

		ContainerAnomalyEventDead caed = new ContainerAnomalyEventDead(cae, reason);

		// Send the event to the container anomaly dead queue
		containerAnomalyDeadProducer.emit(caed);
	}

}