package com.starttomcat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.starttomcat.scheduler.SchedulerService;

  
//@ComponentScan
@SpringBootApplication
@EnableScheduling
public class TomcatStartApplication  {

    @Autowired
    SchedulerService schedulerService;

    public static void main(String[] args) {
        SpringApplication.run(TomcatStartApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        schedulerService.runScheduler();
//    }
    
    @Bean
	public RestTemplate test() {
		return new RestTemplate();
	}
}