package com.starttomcat.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api")
public class PropertiesConfiguration {

	private String ip;
	private String port;
	private String url;
	private String ip1;
	private String port1;
	private String url1;
	private String interval;
	private String tomcatURL;
	private String binPath;
	private String javaHome;
	private String jre;
	
	

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getTomcatURL() {
		return tomcatURL;
	}

	public void setTomcatURL(String tomcatURL) {
		this.tomcatURL = tomcatURL;
	}

	public String getBinPath() {
		return binPath;
	}

	public void setBinPath(String binPath) {
		this.binPath = binPath;
	}

	public String getJavaHome() {
		return javaHome;
	}

	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}

	public String getJre() {
		return jre;
	}

	public void setJre(String jre) {
		this.jre = jre;
	}

	public String getIp1() {
		return ip1;
	}

	public void setIp1(String ip1) {
		this.ip1 = ip1;
	}

	public String getPort1() {
		return port1;
	}

	public void setPort1(String port1) {
		this.port1 = port1;
	}

	public String getUrl1() {
		return url1;
	}

	public void setUrl1(String url1) {
		this.url1 = url1;
	}

}
