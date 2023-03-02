package com.app.backend;

import org.junit.Test;
import org.loadtest4j.Request;


import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class RequestMethodTest {
	
	 @Test
	 public void shouldHaveMethod() {
	    assertSoftly(s -> {
	        s.assertThat(Request.get("/").getMethod()).as("GET").isEqualTo("GET");
	        s.assertThat(Request.post("/").getMethod()).as("POST").isEqualTo("POST");
	        s.assertThat(Request.put("/").getMethod()).as("PUT").isEqualTo("PUT");
	        s.assertThat(Request.delete("/").getMethod()).as("DELETE").isEqualTo("DELETE");
	        s.assertThat(Request.options("/").getMethod()).as("OPTIONS").isEqualTo("OPTIONS");
	        s.assertThat(Request.head("/").getMethod()).as("HEAD").isEqualTo("HEAD");
	        s.assertThat(Request.trace("/").getMethod()).as("TRACE").isEqualTo("TRACE");
	        s.assertThat(Request.patch("/").getMethod()).as("PATCH").isEqualTo("PATCH");
	        s.assertThat(Request.link("/").getMethod()).as("LINK").isEqualTo("LINK");
	        s.assertThat(Request.unlink("/").getMethod()).as("UNLINK").isEqualTo("UNLINK");
	      });
	   }

}
