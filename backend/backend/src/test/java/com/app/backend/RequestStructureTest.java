package com.app.backend;

import org.junit.Test;
import org.loadtest4j.Body;
import org.loadtest4j.driver.DriverRequest;

import com.app.backend.auxiliaryClasses.MockBodyMatcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

public class RequestStructureTest {

	 private final DriverRequest request = new DriverRequest(Body.string("{}"), 
			 Collections.singletonMap("Accept", "application/json"), 
			 "GET", 
			 "/", 
			 Collections.singletonMap("q", "what is the meaning of life"));
	
	 @Test
	 public void shouldHaveBody() {
	    assertThat(request.getBody().match(new MockBodyMatcher())).containsExactly("{}");
	 }
	 
	 @Test
	 public void shouldHaveHeaders() {
		assertThat(request.getHeaders()).containsEntry("Accept", "application/json");
	 }
	 
	 @Test
	 public void shouldHaveMethod() {
	    assertThat(request.getMethod()).isEqualTo("GET");
	 }

	 @Test
	 public void shouldHavePath() {
	    assertThat(request.getPath()).isEqualTo("/");
	 }

	 @Test
	 public void shouldHaveQueryParams() {
	     assertThat(request.getQueryParams()).containsEntry("q", "what is the meaning of life");
	 }
}



