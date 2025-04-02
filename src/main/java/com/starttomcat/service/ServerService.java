package com.starttomcat.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.starttomcat.util.ServerStatus;

@Service
public class ServerService {

	@Autowired
	ServerStatus serverStatus;
	
	public String Server(String host,String port,String url) {
		
		if(serverStatus.isRunning(host, port) && serverStatus.isPingable(host)) {
			return serverStatus.isApplicationRunning(url);
		}else {
			JSONObject json=new JSONObject();
			json.put("Status", "server not reachable");
			json.put("Retcode", "00");
			return json.toString();
		}
	}
	
	
}
