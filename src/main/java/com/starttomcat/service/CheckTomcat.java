package com.starttomcat.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.starttomcat.configuration.PropertiesConfiguration;

@Service
public class CheckTomcat {

    private static final Logger logger = LoggerFactory.getLogger(CheckTomcat.class);

    @Autowired
    PropertiesConfiguration propsConfig;

    public void startTomcat() {
        if (!startTomcatProcess("catalina.bat", "start")) {
            startTomcatProcess("startup.bat", "start");
        }
    }

    public boolean startTomcatProcess(String scriptName, String command) {
        return executeTomcatCommand(scriptName, command);
    }


    private boolean executeTomcatCommand(String scriptName, String command) {
        boolean status = false;
        try {
            System.out.println("Executing Tomcat command: " + scriptName + " " + command);
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", scriptName, command);
            processBuilder.directory(new File(propsConfig.getBinPath()));

            // Check and set environment variables if they are not already set
            Map<String, String> environment = processBuilder.environment();
            boolean javaHomeSet = setEnvironmentVariableIfAbsent(environment, "JAVA_HOME", propsConfig.getJavaHome());
            boolean jreHomeSet = setEnvironmentVariableIfAbsent(environment, "JRE_HOME", propsConfig.getJre());

            if (!javaHomeSet && !jreHomeSet) {
                logger.error("Neither JAVA_HOME nor JRE_HOME is set in the environment. At least one of these environment variables is needed to run this program.");
                return false;
            }

            Process process = processBuilder.start();

            // Capture and log the output of the process
            logProcessOutput(process);

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Tomcat command executed successfully: " + scriptName + " " + command);
                status = true;
            } else {
                logger.error("Tomcat command failed: " + scriptName + " " + command + ". Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Failed to execute Tomcat command: " + scriptName + " " + command + ". " + e.getMessage(), e);
        }

        return status;
    }

    private boolean setEnvironmentVariableIfAbsent(Map<String, String> environment, String variableName, String defaultValue) {
        boolean isSet = environment.get(variableName) != null;
        if (!isSet) {
            String value = System.getenv(variableName);
            if (value != null) {
                environment.put(variableName, value);
                logger.info(variableName + " set to " + value);
                isSet = true;
            } else {
                logger.info(variableName + " not found in environment, setting default value.");
                environment.put(variableName, defaultValue);
                isSet = true;
            }
        }
        return isSet;
    }

    private void logProcessOutput(Process process) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);

                if (line.contains("CATALINA_OPTS")) {
                    // Break out once a specific line is detected (if needed)
                    return;
                }
            }
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.error(line);

                if (line.contains("CATALINA_OPTS")) {
                    // Break out once a specific line is detected (if needed)
                    return;
                }
            }
        }
    }

    public void setPropsConfig(PropertiesConfiguration propsConfig) {
        this.propsConfig = propsConfig;
    }
    
    
    
    public void stopUnwantedProcessesUsingPort(int port) {
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

            logger.info("Processes using port " + port + ": " + pids);

            // Get current process ID (Spring Boot application process ID)
            String currentProcessId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

            // Stop unwanted processes, skipping the current process ID
            for (String pid : pids) {
                if (!pid.equals(currentProcessId)) {
                    logger.info("Stopping process with PID: " + pid);
                    stopProcessById(pid);
                } else {
                    logger.info("Skipping stop for current process with PID: " + pid);
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error stopping unwanted processes: " + e.getMessage(), e);
        }
    }

    private static void stopProcessById(String pid) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", "Stop-Process -Id " + pid + " -Force");
        Process process = processBuilder.start();
        process.waitFor();
    }
    
    
    
	public  void stopProcessUsingPort(int port) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "netstat -ano | findstr :" + port);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                if (tokens.length > 4) {
                    String pid = tokens[4];
                    System.out.println("Stopping process with PID: " + pid);
                    stopProcess(pid);
                }
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String stopProcess(String pid) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe", "-Command", "Stop-Process -Id " + pid + " -Force");
        Process process = processBuilder.start();
        process.waitFor();
        System.out.println("Process " + pid + " stopped.");
       return "Process " + pid + " stopped.";
    }
    
    
    
}
