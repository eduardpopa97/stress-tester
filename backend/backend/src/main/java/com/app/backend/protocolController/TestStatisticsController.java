package com.app.backend.protocolController;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestStatisticsController {

	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/api/getStats")
	public List<JSONObject> httpGetStats()  {
		
		List<JSONObject> finalarray = new ArrayList<JSONObject>();
		JSONObject obj = new JSONObject();
		obj.put("tests", HTTPController.stats.get(2));
		obj.put("throughput", HTTPController.stats.get(6));
		obj.put("average", HTTPController.stats.get(8)+" ms");
		obj.put("minimum", HTTPController.stats.get(10)+" ms");
		obj.put("maximum", HTTPController.stats.get(12)+" ms");
        finalarray.add(obj);
		return finalarray;
	}
}
