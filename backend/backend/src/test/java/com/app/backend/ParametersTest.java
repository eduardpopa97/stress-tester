package com.app.backend;

import org.junit.Test;
import org.loadtest4j.drivers.jmeter.util.QueryString;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ParametersTest {

	@Test
    public void testQueryString() {
        
		final String queryString = QueryString.fromMap(Collections.singletonMap("mail", "abc@yahoo.com"));
		assertThat(queryString).isEqualTo("?mail=abc@yahoo.com");
    }
	
	@SuppressWarnings("serial")
	@Test
	 public void testQueryStringWithMultipleParams() {
	       
		 final Map<String, String> queryParams = new LinkedHashMap<String, String>() {{
	            put("courseName", "Sisteme Cloud");
	            put("professorID", "208313");
	    }};

	     final String queryString = QueryString.fromMap(queryParams);
	     assertThat(queryString).isEqualTo("?courseName=Sisteme Cloud&professorID=208313");
	    }

}
