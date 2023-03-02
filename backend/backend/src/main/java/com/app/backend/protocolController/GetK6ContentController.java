package com.app.backend.protocolController;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetK6ContentController {

	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/api/getK6PlansDetails")
	public List<String> getPlansDetails(@RequestParam(required = false, value = "test") String test) {
		
		List<String> finalarray = new ArrayList<String>();
		StringBuffer planContent = new StringBuffer();
		try (BufferedReader br = Files.newBufferedReader(Paths.get("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\k6Files\\"+test))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		      planContent.append(line);
		      planContent.append("\n");
		    }
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		finalarray.add(planContent.toString());
		return finalarray;
	}
}
