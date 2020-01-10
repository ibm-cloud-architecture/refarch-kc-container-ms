package ibm.labs.kc.containermgr.rest.containers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ibm.labs.kc.containermgr.ContainerService;
import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.model.container.Container;

@RestController
public class ContainerController {
	
	@Autowired
    protected ContainerDAO containerDao;
    
    @Autowired
	private ContainerService containerService;
	
	ContainerController(ContainerDAO dao) {
		this.containerDao = dao;
	}
	
    @GetMapping("/containers")
    public Page<ContainerEntity> getAllContainers(Pageable pageable) {
    	return containerDao.getAllContainers(pageable);
    }
    
    @GetMapping("/containers/{containerId}")
    public ContainerEntity getContainerById(@PathVariable String containerId) {
    	return containerDao.getById(containerId);
    }
   
    @PostMapping("/containers")
    public ContainerEntity createContainer(@Valid @RequestBody Container container) {
        return containerService.createContainer(container);
    }
    

    @PutMapping("/containers")
    public ContainerEntity updateContainer(@Valid @RequestBody Container container) {
        return containerService.updateContainer(container);
    
    }
    
    @PostMapping("/containers/toMaintenance")
    // Request Container so that if new latitude, longitude or capacity changes due to the maintenance, the container gets updated
    public ResponseEntity<String> containerToMaintenance(@Valid @RequestBody Container container) {
        if (containerService.setContainerToMaintenance(container)){
            return ResponseEntity.status(HttpStatus.OK).body("Container " + container.getContainerID() + " set to maintenance.");
        }
        return ResponseEntity.badRequest().body("[ERROR] - A problem ocurred setting container " + container.getContainerID() + " to maintenance.");
    }
    
    @PostMapping("/containers/offMaintenance")
    // Request Container so that if new latitude, longitude or capacity changes due to the maintenance, the container gets updated
    public ResponseEntity<String> containerOffMaintenance(@Valid @RequestBody Container container) {
        if (containerService.setContainerOffMaintenance(container)){
            return ResponseEntity.status(HttpStatus.OK).body("Container " + container.getContainerID() + " out from maintenance.");
        }
        return ResponseEntity.badRequest().body("[ERROR] - A problem ocurred getting container " + container.getContainerID() + " out from maintenance.");
    }

    @DeleteMapping("/containers/all")
    //Delete all existing containers from the backend
    public ResponseEntity<String> deleteAllContainers(){
        if(containerService.deleteAllContainers()){
            return ResponseEntity.status(HttpStatus.OK).body("All containers deleted successfully.");
        }
        return ResponseEntity.badRequest().body("[ERROR] - A problem occurred deleting all containers.");
    }

    @DeleteMapping("/containers/{containerId}")
    //Delete all existing containers from the backend
    public ResponseEntity<String> deleteContainerById(@PathVariable String containerId){
        ContainerEntity ce = containerDao.getById(containerId);
        if(containerService.deleteContainer(ce)){
            return ResponseEntity.status(HttpStatus.OK).body("Container "+ containerId + " deleted successfully.");
        }
        return ResponseEntity.badRequest().body("[ERROR] - A problem occurred deleting container "+containerId + ".");
    }
    
    @GetMapping("/bpm/enable")
    public ResponseEntity<String> enableBPM() {
        containerService.enableBPM();
        return ResponseEntity.status(HttpStatus.OK).body("BPM service enabled = " + containerService.statusBPM());
    }
    
    @GetMapping("/bpm/disable")
    public ResponseEntity<String> disableBPM() {
        containerService.disableBPM();
        return ResponseEntity.status(HttpStatus.OK).body("BPM service enabled = " + containerService.statusBPM());
    }
    
    @GetMapping("/bpm/status")
    public ResponseEntity<String> statusBPM() {
    	return ResponseEntity.status(HttpStatus.OK).body("BPM service enabled = " + containerService.statusBPM());
    }
}
