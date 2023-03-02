//import java.io.BufferedReader;
//import java.io.InputStreamReader;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

public class executeJMETER {
	 public static void main(String[] argv) throws Exception {

//		 Process process = Runtime.getRuntime().exec("ping www.stackabuse.com");
//		 BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//		    String line = "";
//		    while ((line = reader.readLine()) != null) {
//		        System.out.println(line);
//		    }
	    
	 
	        //JMeter Engine
	        StandardJMeterEngine jmeter = new StandardJMeterEngine();

	        //JMeter initialization (properties, log levels, locale, etc)
	        JMeterUtils.loadJMeterProperties("C:\\Users\\popae\\Downloads\\apache-jmeter-5.4\\apache-jmeter-5.4\\bin\\jmeter.properties");
	      //  JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
	        JMeterUtils.initLocale();

	        // JMeter Test Plan, basic all u JOrphan HashTree
	        HashTree testPlanTree = new HashTree();

	        // HTTP Sampler
	        HTTPSampler httpSampler = new HTTPSampler();
	        httpSampler.setDomain("www.google.com");
	        httpSampler.setPort(80);
	        httpSampler.setPath("/");
	        httpSampler.setMethod("GET");

	        // Loop Controller
	        LoopController loopController = new LoopController();
	        loopController.setLoops(10);
	        loopController.addTestElement(httpSampler);
	        loopController.setFirst(true);
	        loopController.initialize();

	        // Thread Group
	        ThreadGroup threadGroup = new ThreadGroup();
	        threadGroup.setNumThreads(4);
	        threadGroup.setRampUp(1);
	        threadGroup.setSamplerController(loopController);

	        // Test Plan
	        TestPlan testPlan = new TestPlan("Create JMeter Script From Java Code");

	        // Construct Test Plan from previously initialized elements
	        testPlanTree.add("testPlan", testPlan);
	        testPlanTree.add("loopController", loopController);
	        testPlanTree.add("threadGroup", threadGroup);
	        testPlanTree.add("httpSampler", httpSampler);

	        
	        Summariser summer = null;
	        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");//$NON-NLS-1$
	        if (summariserName.length() > 0) {
	            summer = new Summariser(summariserName);
	        }

	        String logFile = "D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\file.csv";
	        ResultCollector logger = new ResultCollector(summer);
	        logger.setFilename(logFile);
	        testPlanTree.add(testPlanTree.getArray()[0], logger);
//	        logger.setErrorLogging(true);
	        
	        //System.out.println(httpSampler.getPort());
	        jmeter.configure(testPlanTree);
	        jmeter.run();
	        
	    }
}
