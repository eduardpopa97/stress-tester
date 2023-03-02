package com.app.backend.protocolController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchPlanController {

	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/api/getPlans")
	public List<JSONObject> getPlans() {
		
		List<JSONObject> finalarray = new ArrayList<JSONObject>();
		File folder = new File("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\testingFiles");
		File[] listOfFiles = folder.listFiles();
		
		for (int i=0; i<listOfFiles.length; i++) {
			JSONObject obj = new JSONObject();
			if(listOfFiles[i].isFile()) {
				obj.put("planFiles", listOfFiles[i].getName());
				finalarray.add(obj);
			}
		}
		return finalarray;
		
	}
}
