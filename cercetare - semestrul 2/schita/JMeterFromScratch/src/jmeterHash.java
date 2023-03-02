import java.io.FileOutputStream;
import java.util.ArrayList;

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


public class jmeterHash {
	
	public static void main(String[] args) throws Exception {
	
		 StandardJMeterEngine jmeter = new StandardJMeterEngine();

	        //JMeter initialization (properties, log levels, locale, etc)
	        JMeterUtils.loadJMeterProperties("C:\\Users\\popae\\Downloads\\apache-jmeter-5.4\\apache-jmeter-5.4\\bin\\jmeter.properties");
	        JMeterUtils.setJMeterHome("C:\\Users\\popae\\Downloads\\apache-jmeter-5.4");
	        //JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
	        JMeterUtils.initLocale();

	     // TestPlan
	        TestPlan testPlan = new TestPlan();
	        testPlan.setName("Test Plan");
	        testPlan.setEnabled(true);
	        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
	        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());

	        // ThreadGroup controller
	        LoopController loopController = new LoopController();
	        loopController.setEnabled(true);
	        loopController.setLoops(5);
	        loopController.setProperty(TestElement.TEST_CLASS,
	                LoopController.class.getName());
	        loopController.setProperty(TestElement.GUI_CLASS,
	                LoopControlPanel.class.getName());

	        // ThreadGroup
	        ThreadGroup threadGroup = new ThreadGroup();
	        threadGroup.setName("Thread Group");
	        threadGroup.setEnabled(true);
	        threadGroup.setSamplerController(loopController);
	        threadGroup.setNumThreads(5);
	        threadGroup.setRampUp(10);
	        threadGroup.setProperty(TestElement.TEST_CLASS,
	                ThreadGroup.class.getName());
	        threadGroup.setProperty(TestElement.GUI_CLASS,
	                ThreadGroupGui.class.getName());

	        // JavaSampler
	        JavaSampler javaSampler = new JavaSampler();
	        javaSampler.setClassname("my.example.sampler");
	        javaSampler.setEnabled(true);
	        javaSampler.setProperty(TestElement.TEST_CLASS,
	                JavaSampler.class.getName());
	        javaSampler.setProperty(TestElement.GUI_CLASS,
	                JavaTestSamplerGui.class.getName());

	        // Create TestPlan hash tree
	        HashTree testPlanHashTree = new HashTree();
	        testPlanHashTree.add(testPlan);

	        // Add ThreadGroup to TestPlan hash tree
	        HashTree threadGroupHashTree = new HashTree();
	        threadGroupHashTree = testPlanHashTree.add(testPlan, threadGroup);

	        // Add Java Sampler to ThreadGroup hash tree
	        HashTree javaSamplerHashTree = new HashTree();
	        javaSamplerHashTree = threadGroupHashTree.add(javaSampler);

	        // Save to jmx file
	        SaveService.saveTree(testPlanHashTree, new FileOutputStream(
	                "src\\testHASH.jmx"));
	    
	}
}
