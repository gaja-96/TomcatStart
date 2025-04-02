package com.starttomcat.controller;

import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.starttomcat.configuration.PropertiesConfiguration;
import com.starttomcat.scheduler.SchedulerService;
import com.starttomcat.service.ServerService;

@RestController
public class HighAvailabilityController {
	
    private static final Logger logger =  (Logger) LoggerFactory.getLogger(HighAvailabilityController.class);


	//private static Logger logger = Logger.getLogger(HighAvailabilityController.class);

	@Autowired
	ServerService serverService;
 
	@Autowired
	PropertiesConfiguration propsConfig;

	@Autowired  
	SchedulerService schedulerService;
	
	@Autowired
	RestTemplate restTemplate;
	
//	@Value("${scheduledtime}")
//    String scheduledTime=propsConfig.getInterval();

	@PostMapping(value = "/status")
	public String serverStatus(@RequestBody Map<String, String> requestBody) {
		logger.info("Checking status for :: " + "ip ::" + requestBody.get("ip") + "port ::" + requestBody.get("port")
				+ "url ::" + requestBody.get("url"));

		JSONObject json = new JSONObject(
				serverService.Server(requestBody.get("ip"), requestBody.get("port"), requestBody.get("url")));
		json.put("status code", 200);
		return json.toString();
	}

	//@PostMapping(value = "/check")
	 @Scheduled(fixedRateString = "#{@propertiesConfiguration.interval}")
    public String highAvailability() {
        logger.info("Operation started");
        String ret = schedulerService.runScheduler();
        logger.info(ret);
        
        return ret;
    }
	
	 
    @GetMapping("/checkTraces")
    public String checkTraces() {
    	
    	ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:8081/helloTraces", String.class);
    	
    	logger.info("Getting Traces Succesfull");
    	System.out.println("Getting Traces Succesfull==");
    	
    	logger.info("ENTITY++++++++"+forEntity);
    	
    return "Getting Traces Succesfull ";
    }	

    
    @GetMapping("/checkTracess")
    public String checkTracess() {
    return "Getting Traces Succesfull ";
    }
    
    
    
    
    
    
    
    
    
}
