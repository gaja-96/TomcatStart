package com.starttomcat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TomcatController {

//	 public static void main(String[] args) {
//		 stopUnwantedProcessesUsingPort(8089);
//	    }

	 public static void stopUnwantedProcessesUsingPort(int port) {
		    try {
		        // Find processes using the specified port
		        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "netstat -ano | findstr :" + port);
		        Process process = processBuilder.start();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		        // Extract PIDs of processes using the port
		        List<String> pids = new ArrayList<>();
		        String line;
		        while ((line = reader.readLine()) != null) {
		            String[] tokens = line.trim().split("\\s+");
		            if (tokens.length > 4) {
		                pids.add(tokens[4]);
		            }
		        }

		        // Stop unwanted processes
		        for (String pid : pids) {
		            stopProcessById(pid);
		        }
		    } catch (IOException | InterruptedException e) {
		        //logger.error("Error stopping unwanted processes: " + e.getMessage(), e);
		    }
		}

		private static void stopProcessById(String pid) throws IOException, InterruptedException {
		    ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", "Stop-Process -Id " + pid + " -Force");
		    Process process = processBuilder.start();
		    process.waitFor();
		}

	}