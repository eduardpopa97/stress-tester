package com.app.backend;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.loadtest4j.*;
import org.loadtest4j.driver.DriverRequest;

import com.app.backend.auxiliaryClasses.NopDriver;
import com.app.backend.auxiliaryClasses.SpyDriver;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JMeterDriverTest {

	@SuppressWarnings("deprecation")
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
    public void shouldPreserveRequestOrdering() {
     
        final SpyDriver driver = new SpyDriver(new NopDriver());
        final LoadTester loadTester = new DriverAdapter(driver);

        loadTester.run(list(Request.get("/api/resource"), Request.get("/api/public")));

        assertThat(driver.getActualRequests())
                .extracting(DriverRequest::getPath)
                .containsSequence("/api/resource", "/api/public");
    }
	
	 @Test
	 public void shouldRun() {
	        
	    final SpyDriver driver = new SpyDriver(new NopDriver());
	    final LoadTester loadTester = new DriverAdapter(driver);

	    loadTester.run(list(Request.get("/")));

	    assertThat(driver.getActualRequests())
	    	.extracting(DriverRequest::getPath)
	        .containsSequence("/");
	    }
	 
	 private static List<Request> list(Request... elements) {
	     return Arrays.asList(elements);
	 }
	
}
