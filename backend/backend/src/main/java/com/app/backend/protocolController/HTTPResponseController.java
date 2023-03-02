package com.app.backend.protocolController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HTTPResponseController {

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/api/getResponse")
	public List<Object> httpGetResponse(@RequestParam(required = false, value = "server") String server) throws IOException {
		
		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();
		
		try {
			String path = "https://"+server;
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				responseContent.append(line);
			}
			reader.close();
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Object> finalarray = new ArrayList<Object>();
		String html = responseContent.toString();
		org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(html);
		finalarray.add(doc.body().html());
		return finalarray;
	
	}
}
