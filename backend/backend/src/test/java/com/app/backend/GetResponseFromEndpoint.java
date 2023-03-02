package com.app.backend;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GetResponseFromEndpoint  {

	@Test
	public void testLocalEndpoint() throws IOException, ParseException {
	
		HttpGet request = new HttpGet("http://localhost:8080/api/getPlans");
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity);

		assertThat(result).isNotEmpty();
	}
}
