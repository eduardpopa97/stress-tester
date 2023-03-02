package com.app.backend.protocolController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class K6Controller {
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/api/k6")
	public List<String> k6(@RequestParam(required = false, value = "url") String url,
			@RequestParam(required = false, value = "load") String load) throws IOException, InterruptedException
	{

		String testingFile = String.valueOf(System.currentTimeMillis());
		File myObj = new File("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\k6Files\\K6_Test" + testingFile + ".js");
		FileOutputStream fos = new FileOutputStream(myObj);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		String[] trend = StringUtils.substringsBetween(load , "\"", "\"");
		
		bw.write("import http from 'k6/http'");
		bw.newLine();
		bw.write("import { sleep } from 'k6'");
		bw.newLine();
		bw.newLine();
		bw.write("export const options = {");
		bw.newLine();
		bw.write("	stages: [");
		bw.newLine();
		for(int i=0; i<trend.length/2; i++)
		{
			bw.write("		{ duration: '" + trend[2*i] + "s', target: " + trend[2*i+1] + "},");
			bw.newLine();	
		}
		bw.write(" 	],");
		bw.newLine();
		bw.write("};");
		bw.newLine();
		bw.newLine();
		bw.write("export default function () {");
		bw.newLine();
		bw.write("	const BASE_URL = '" + url + "';");
		bw.newLine();
		bw.newLine();
		bw.write("	const responses = http.batch([");
		bw.newLine();
		bw.write("		['GET', `${BASE_URL}`, null, {tags: { name: 'localtest' } }]");
		bw.newLine();
		bw.write("	]);");
		bw.newLine();
		bw.newLine();
		bw.write("	sleep(1);");
		bw.newLine();
		bw.newLine();
		bw.write("}");
 
		bw.close();
		
		ProcessBuilder p = new ProcessBuilder("C:\\Program Files\\k6\\k6.exe", "run", "D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\k6Files\\K6_Test" + testingFile + ".js");
		p.redirectOutput(new File("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\k6Console\\K6_Result" + testingFile + ".txt"));
		p.start();
		
		Thread.sleep(10000);
		
		StringBuffer planContent = new StringBuffer();
		try (BufferedReader br = Files.newBufferedReader(Paths.get("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\k6Console\\K6_Result" + testingFile + ".txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		      planContent.append(line);
		      planContent.append("\n");
		    }
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		String k6 = planContent.toString();
		List<String> finalarray = new ArrayList<String>();
		finalarray .add(k6);
		
		return finalarray;
	
	}
}

