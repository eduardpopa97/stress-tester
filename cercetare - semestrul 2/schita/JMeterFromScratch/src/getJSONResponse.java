import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;

public class getJSONResponse {

	public static void main (String[] args) {

		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();
		
		try {
			URL url = new URL("http://google.com");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int status = connection.getResponseCode();
			if(status > 299) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			}
			else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			}
			System.out.println(responseContent.toString());
			String html = responseContent.toString();
			Document doc = Jsoup.parseBodyFragment(html);
			System.out.println(doc.body().html());
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
//			JsonElement jsonElement =  new JsonParser().parse(responseContent.toString());
//			System.out.println(gson.toJson(jsonElement));
			
		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
