package com.app.backend.protocolController;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;


import javax.xml.transform.TransformerException;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.java.control.gui.JavaTestSamplerGui;
import org.apache.jmeter.protocol.java.sampler.JavaSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class HTTPController {

	public static List<String> stats = new ArrayList<String>(); 
	
	public static String springServerResponse;
	
	@SuppressWarnings({ "unchecked" })
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/api/http")
	public List<JSONObject> httpRequest(@RequestParam(required = false, value = "users") String users,
			@RequestParam(required = false, value = "ramp") String ramp,
			@RequestParam(required = false, value = "loops") String loops,
			@RequestParam(required = false, value = "server") String server,
			@RequestParam(required = false, value = "method") String method,
			@RequestParam(required = false, value = "port") String port,
			@RequestParam(required = false, value = "path") String path,
			@RequestParam(required = false, value = "protocol") String protocol,
			@RequestParam(required = false, value = "httpparam") String httpparam)
			throws FileNotFoundException, IOException, ParserConfigurationException, SAXException, TransformerException {
		// JMeter Engine
		StandardJMeterEngine jmeter = new StandardJMeterEngine();

		// JMeter initialization (properties, log levels, locale, etc)
		JMeterUtils.loadJMeterProperties(
				"D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\jars\\apache-jmeter-5.4\\bin\\jmeter.properties");
		JMeterUtils.setJMeterHome("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\jars\\apache-jmeter-5.4");
		JMeterUtils.initLocale();

		// JMeter Test Plan
		HashTree testPlanTree = new HashTree();

		// Java Request
		JavaSampler javaSampler = new JavaSampler();
		javaSampler.setName("Java Request");
		javaSampler.setClassname("org.apache.jmeter.protocol.java.test.SleepTest");
		Arguments arguments = new Arguments();
		arguments.addArgument("SleepTime", "1000");
		arguments.addArgument("SleepMask", "0x33F");
		javaSampler.setArguments(arguments);
		javaSampler.setProperty(TestElement.TEST_CLASS, JavaSampler.class.getName());
		javaSampler.setProperty(TestElement.GUI_CLASS, JavaTestSamplerGui.class.getName());

		// HTTP Sampler
		HTTPSampler httpSampler = new HTTPSampler();
		httpSampler.setDomain(server);
		if(port != null && port != "")  httpSampler.setPort(Integer.parseInt(port));
		httpSampler.setPath(path);
		httpSampler.setMethod(method);
		httpSampler.addTestElement(javaSampler);

		// Loop Controller
		TestElement loopController = new LoopController();
		((LoopController) loopController).setLoops(Integer.parseInt(loops));
		loopController.addTestElement(javaSampler);
		((LoopController) loopController).setFirst(true);
		loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
		loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
		((LoopController) loopController).initialize();

		// Thread Group
		ThreadGroup threadGroup = new ThreadGroup();
		threadGroup.setNumThreads(Integer.parseInt(users));
		threadGroup.setRampUp(Integer.parseInt(ramp));
		threadGroup.setName("Thread Group");
		threadGroup.setSamplerController(((LoopController) loopController));
		threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
		threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());

		// Test Plan
		TestPlan testPlan = new TestPlan("Create JMeter Script From Java Code");
		testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
		testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
		testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

		// Construct Test Plan from previously initialized elements
		testPlanTree.add(testPlan);
		HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
		threadGroupHashTree.add(javaSampler);

		String fileName = String.valueOf(System.currentTimeMillis());

		// Save generated test plan to JMeter's .jmx file format
		SaveService.saveTree(testPlanTree, new FileOutputStream(
				"D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\testingFiles\\ApacheJMeter_Test"
						+ fileName + ".jmx"));
		    
		// Add Summarizer output to get test progress
		Summariser summer = null;
		String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
		if (summariserName.length() > 0) {
			summer = new Summariser(summariserName);
		}

		// Store execution results into a .csv file
		String logFile = "D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\logFiles\\ApacheJMeter_Test"
				+ fileName + ".csv";
		ResultCollector logger = new ResultCollector(summer);
		logger.setFilename(logFile);
		testPlanTree.add(testPlanTree.getArray()[0], logger);

		jmeter.configure(testPlanTree);
		jmeter.run();
		
		
		PrintWriter writer = new PrintWriter(logFile);
		writer.print("");
		writer.close();
		
	
		JMXFileGenerator jmxGenerator = new JMXFileGenerator();
		jmxGenerator.generateFile(loops, users, ramp, server, method, path, port, protocol, httpparam, fileName);
		
		List<JSONObject> finalarray = new ArrayList<JSONObject>();
		try (BufferedReader br = Files.newBufferedReader(Paths.get("src\\main\\java\\com\\app\\backend\\logFiles\\ApacheJMeter_Test"+fileName+".csv"))) {

		    String DELIMITER = ",";
		    String line1;
		    while ((line1 = br.readLine()) != null) {
		        String[] columns = line1.split(DELIMITER);
		        JSONObject obj = new JSONObject();
		        obj.put("responseCode", columns[3]);
		        obj.put("responseMessage", columns[4]);
		        obj.put("sentBytes", columns[9]);
		        obj.put("receivedBytes", columns[10]);
		        obj.put("thread", columns[12]);
		        obj.put("url", columns[13]);
		        obj.put("latency", columns[14]);
		        finalarray.add(obj);
		    }

		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        HttpResponseModel[] response = gson.fromJson(finalarray.toString(), HttpResponseModel[].class);

        HTTPController.springServerResponse = gson.toJson(response);
		return finalarray;		
		
	}
	
}
