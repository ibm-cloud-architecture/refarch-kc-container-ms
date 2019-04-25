package ibm.labs.kc.containermgr.rest.containers;

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

import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.model.ContainerEntity;

@RestController
public class ContainerController {
	
	@Autowired
	protected ContainerDAO containerDao;
	
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
    public ContainerEntity createContainer(@Valid @RequestBody ContainerEntity container) {
        return containerDao.save(container);
    }
    

    @PutMapping("/containers/{containerId}")
    public ContainerEntity updateContainer(@PathVariable String containerId, 
    		@Valid @RequestBody ContainerEntity containerRequest) {
        return containerDao.update(containerId,containerRequest);
    
    }
    
}
