package ibm.labs.kc.containermgr.rest.bpm.mockup;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BpmMockup {
		
	BpmMockup() {}
	
    @PostMapping("/bpm_mockup/login")
    public ResponseEntity<String> loginBPM() {
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"expiration\":0000,\"csrf_token\":\"this_is_a_mockup_token\"}");
    }
    
    @PostMapping("/bpm_mockup/bpm_process_404")
    public ResponseEntity<String> process404() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404 Not Found");
    }
}
