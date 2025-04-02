package com.starttomcat.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ServerStatus {

	private static Logger logger = Logger.getLogger(ServerStatus.class);

	public boolean isRunning(String host, String port) {
		Socket socket;
		try {
			logger.info("Host ::" + host);
			logger.info("Port ::" + port);
			socket = new Socket(host, Integer.parseInt(port));
			logger.info("server running status ::" + socket.isConnected());
			if (socket.isConnected()) {
				socket.close();
				return true;
			}
			socket.close();
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error(e.getMessage());
			logger.error("The Server is Stopped : Connection refused: connect");
		}
		
		return false;
	}

	public boolean isPingable(String host) {

		try {
			InetAddress inetAddress = InetAddress.getByName(host);
			boolean status = inetAddress.isReachable(5000);
			logger.info("is server pinged ::" + status);
			if (status) {
				return true;
			}

		} catch (Exception e) {
			//e.printStackTrace();
			logger.error(e.getMessage());
		}

		return false;
	}

	public String isApplicationRunning(String RequestURL) {
		JSONObject result = new JSONObject();
//		String[] arrURL = RequestURL.split(",");
		logger.info("URLs::"+ RequestURL);
//		for (int i = 0; i < arrURL.length; i++) {
			JSONObject json = new JSONObject();
			JSONArray jsonArry = new JSONArray();
			logger.info("connecting URL::"+RequestURL);
			try {
				URL url = new URL(RequestURL);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				int responceCode = connection.getResponseCode();
				json.put("Responce Code", responceCode);
				if (responceCode == 200) {
					json.put("Status", "Reachable");
					json.put("Whitelabel", "No");
					json.put("Retcode", "01");
				} else if (responceCode == 404) {
					BufferedReader bfreReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
					String line;
					boolean isWhiteLabel = false;
					while ((line = bfreReader.readLine()) != null) {
						if (line.contains("Whitelabel Error Page")) {
							isWhiteLabel = true;
							break;
						}
						if((line = bfreReader.readLine()) == null){
							json.put("Status", "Not Reachable");
							json.put("Whitelabel", "No");
							json.put("Retcode", "00");
						}
					}
					bfreReader.close();

					if (isWhiteLabel) {
						json.put("Status", "Reachable");
						json.put("Whitelabel", "Yes");
						json.put("Retcode", "01");
					}
				} else {
					json.put("Status", "Not Reachable");
					json.put("Whitelabel", "No");
					json.put("Retcode", "00");
				}

//				jsonArry.put(json);
//				result.put(url.toString(), jsonArry);
			} catch (Exception e) {
				//e.printStackTrace();
				logger.error(e.getMessage());
			}

//		}

		return json.toString();
	}
}
