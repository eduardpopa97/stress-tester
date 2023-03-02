package com.app.backend.protocolController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchK6PlanController {

	@SuppressWarnings("unchecked")
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/api/getK6Plans")
	public List<JSONObject> getK6Plans() {
		
		List<JSONObject> finalarray = new ArrayList<JSONObject>();
		File folder = new File("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\k6Files");
		File[] listOfFiles = folder.listFiles();
		
		for (int i=0; i<listOfFiles.length; i++) {
			JSONObject obj = new JSONObject();
			if(listOfFiles[i].isFile()) {
				obj.put("planK6Files", listOfFiles[i].getName());
				finalarray.add(obj);
			}
		}
		return finalarray;
	}
}
