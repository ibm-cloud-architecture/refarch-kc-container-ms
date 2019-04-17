package ibm.labs.kc.containermgr.rest.containers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ibm.labs.kc.containermgr.dao.ContainerRepository;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.containermgr.rest.ResourceNotFoundException;

@RestController
public class ContainerController {
	
	@Autowired
	private ContainerRepository containerRepository;
	
	ContainerController() {
	}
	
    @GetMapping("/containers")
    public Page<ContainerEntity> getAllContainers(Pageable pageable) {
    	return containerRepository.findAll(pageable);
    }
    
    @GetMapping("/containers/{containerId}")
    public ContainerEntity getContainerById(@PathVariable String containerId) {
    	return containerRepository.findById(containerId)
    			.orElseThrow(() -> 
    	 new ResourceNotFoundException("Container not found with id " + containerId));
    }
   
    @PostMapping("/containers")
    public ContainerEntity createContainer(@Valid @RequestBody ContainerEntity container) {
        return containerRepository.save(container);
    }
    

    @PutMapping("/containers/{containerId}")
    public ContainerEntity updateContainer(@PathVariable String containerId, 
    		@Valid @RequestBody ContainerEntity containerRequest) {
        return containerRepository.findById(containerId)
        		.map(container -> {
        			container.setBrand(containerRequest.getBrand());
        			container.setType(containerRequest.getType());
        			container.setCapacity(containerRequest.getCapacity());
        			container.setCurrentCity(containerRequest.getCurrentCity());
        			container.setStatus(containerRequest.getStatus());
        			container.setUpdatedAt(new Date());
        			return containerRepository.save(container);
        		})
               .orElseThrow(() -> new ResourceNotFoundException("Container not found with id " + containerId));
    
    }
    
}
