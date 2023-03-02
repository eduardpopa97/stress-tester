package com.app.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.loadtest4j.driver.Driver;
import org.loadtest4j.driver.DriverFactory;
import org.loadtest4j.drivers.jmeter.JMeterFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class JMeterBuilderTest {

	 @Test
	 public void testGetMandatoryProperties() {
	     
		 final DriverFactory df = new JMeterFactory();
		 final Set<String> mandatoryProperties = df.getMandatoryProperties();

	     assertThat(mandatoryProperties).containsExactly("domain", "numThreads", 
	    		 "port", "protocol", "rampUp");
	    }
	 
	 @Test
	 public void testCreate() {
	        
		 final DriverFactory df = new JMeterFactory();

	     final Map<String, String> properties = new HashMap<>();
	     properties.put("domain", "example.com");
	     properties.put("numThreads", "2");
	     properties.put("port", "443");
	     properties.put("protocol", "https");
	     properties.put("rampUp", "10");

	     final Driver testplan = df.create(properties);

	     assertThat(testplan).isInstanceOf(Driver.class);
	    }
}
