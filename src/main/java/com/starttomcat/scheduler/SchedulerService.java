package com.starttomcat.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.starttomcat.configuration.PropertiesConfiguration;
import com.starttomcat.service.CheckTomcat;
import com.starttomcat.service.ServerService;


@Service
public class SchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private ServerService serverService;

    @Autowired
    private PropertiesConfiguration propsConfig;

    @Autowired
    private CheckTomcat checkTomcat;
    
    

    public String runScheduler() {
        logger.info("Scheduling application");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String response = "";

       // int scheduleTime = Integer.parseInt(propsConfig.getInterval());

        try {
            // Perform server checks
            logger.debug("Server checking time ::" + sdf.format(new Date()));
            JSONObject jsonResponse1 = new JSONObject(
                serverService.Server(propsConfig.getIp(), propsConfig.getPort(), propsConfig.getUrl()));
            JSONObject jsonResponse2 = new JSONObject(
                serverService.Server(propsConfig.getIp1(), propsConfig.getPort1(), propsConfig.getUrl1()));
            String retcode1 = jsonResponse1.getString("Retcode");
            String retcode2 = jsonResponse2.getString("Retcode");
            logger.info("Server 1 Retcode: " + retcode1);
            logger.info("Server 2 Retcode: " + retcode2);

            // Start or stop Tomcat based on server responses
            if ("00".equals(retcode1) && "00".equals(retcode2)) {
                checkTomcat.startTomcat();
                response = "BackUpServer Started";
                logger.info(response);
            } else if("01".equals(retcode1) && "01".equals(retcode2)) {
                // Stop unwanted processes using port 8089
            	checkTomcat.stopUnwantedProcessesUsingPort(Integer.parseInt(propsConfig.getPort1()));
            	response = "BackUpServer Stopped";
                logger.info(response);
            }
//            else {
//            	checkTomcat.stopUnwantedProcessesUsingPort(Integer.parseInt(propsConfig.getPort1()));
//                logger.info("BackUpServer Stopped1");
//            }

            return response;
        } catch (Exception e) {
            logger.error("Error during scheduler execution: " + e.getMessage(), e);
            return "Error starting scheduler: " + e.getMessage();
        }
    }

}
