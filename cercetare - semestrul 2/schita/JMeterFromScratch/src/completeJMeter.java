import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class completeJMeter {

	public static void main(String[] argv) throws Exception {
	
		//JMeter Engine
        StandardJMeterEngine jmeter = new StandardJMeterEngine();

        //JMeter initialization (properties, log levels, locale, etc)
        JMeterUtils.loadJMeterProperties("C:\\Users\\popae\\Downloads\\apache-jmeter-5.4\\apache-jmeter-5.4\\bin\\jmeter.properties");
        JMeterUtils.setJMeterHome("C:\\Users\\popae\\Downloads\\apache-jmeter-5.4");
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
        httpSampler.setDomain("www.google.com");
        httpSampler.setPort(80);
        httpSampler.setPath("/");
        httpSampler.setMethod("GET");
        httpSampler.addTestElement(javaSampler);
        
        // Loop Controller
        TestElement loopController = new LoopController();
        ((LoopController) loopController).setLoops(1);
        loopController.addTestElement(javaSampler);
        ((LoopController) loopController).setFirst(true);
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
        ((LoopController) loopController).initialize();

        // Thread Group
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(10);
        threadGroup.setRampUp(1);
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
        
        // save generated test plan to JMeter's .jmx file format
        SaveService.saveTree(testPlanTree, new FileOutputStream("D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\11.jmx"));
        
        //add Summarizer output to get test progress in stdout like:
        // summary =      2 in   1.3s =    1.5/s Avg:   631 Min:   290 Max:   973 Err:     0 (0.00%)
        Summariser summer = null;
        String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
        if (summariserName.length() > 0) {
            summer = new Summariser(summariserName);
        }


        // Store execution results into a .jtl file
        String logFile = "D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\22.csv";
        ResultCollector logger = new ResultCollector(summer);
        logger.setFilename(logFile);
        testPlanTree.add(testPlanTree.getArray()[0], logger);
        
        jmeter.configure(testPlanTree);
        jmeter.run();
        
        PrintWriter writer = new PrintWriter(logFile);
        writer.print("");
        writer.close();

        File myObj = new File("D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\created.jmx");
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(myObj.toString());
		Document param = db.parse(new File("D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\parametersDOM.jmx").toString());
        
		 //nr de bucle
		 NodeList nList = dom.getElementsByTagName("elementProp");
		 Element emp = (Element) nList.item(1);
		 Node name = emp.getElementsByTagName("stringProp").item(0).getFirstChild();
		 name.setNodeValue("20");
		 
		 //nr de fire de executie
		 NodeList nList1 = dom.getElementsByTagName("ThreadGroup");
		 Element emp1 = (Element) nList1.item(0);
		 Node name1 = emp1.getElementsByTagName("stringProp").item(2).getFirstChild();
		 name1.setNodeValue("5");
		 
		 //perioada de timp intre firele de executie
		 NodeList nList2 = dom.getElementsByTagName("ThreadGroup");
		 Element emp2 = (Element) nList2.item(0);
		 Node name2 = emp2.getElementsByTagName("stringProp").item(3).getFirstChild();
		 name2.setNodeValue("1");
		 
		 //domeniul
		 NodeList nList3 = dom.getElementsByTagName("HTTPSamplerProxy");
		 Element emp3 = (Element) nList3.item(0);
		 Node name3 = emp3.getElementsByTagName("stringProp").item(0).getFirstChild();
		 name3.setNodeValue("www.google.com");
		 
		 //nr port
		 NodeList nList4 = dom.getElementsByTagName("HTTPSamplerProxy");
		 Element emp4 = (Element) nList4.item(0);
		 Node name4 = emp4.getElementsByTagName("stringProp").item(1).getFirstChild();
		 name4.setNodeValue("80");
		 
		 //calea
		 NodeList nList5 = dom.getElementsByTagName("HTTPSamplerProxy");
		 Element emp5 = (Element) nList5.item(0);
		 Node name5 = emp5.getElementsByTagName("stringProp").item(4).getFirstChild();
		 name5.setNodeValue("/");
		 
		 //metoda HTTP
		 NodeList nList6 = dom.getElementsByTagName("HTTPSamplerProxy");
		 Element emp6 = (Element) nList6.item(0);
		 Node name6 = emp6.getElementsByTagName("stringProp").item(5).getFirstChild();
		 name6.setNodeValue("GET");
		 
		 //adaugare parametri in cererea HTTP
		 NodeList nList7 = param.getElementsByTagName("elementProp");
		 Element emp7 = (Element) nList7.item(0);
		 emp7.setAttribute("name", "Email");
		 Node name7 = emp7.getElementsByTagName("stringProp").item(0).getFirstChild();
		 name7.setNodeValue("abcd@yahoo.com");
		 
		 Node name8 = emp7.getElementsByTagName("stringProp").item(2).getFirstChild();
		 name8.setNodeValue("Email");
		 
		 param.getDocumentElement().normalize();
		 TransformerFactory transformerFactory1 = TransformerFactory.newInstance();
		 Transformer transformer1 = transformerFactory1.newTransformer();
		 DOMSource source1 = new DOMSource(param);
		 StreamResult result1 = new StreamResult(new File("D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\createdPARAM.jmx"));
		 transformer1.setOutputProperty(OutputKeys.INDENT, "yes");
		 transformer1.transform(source1, result1);
		 
		 Document param1 = db.parse(new File("D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\createdPARAM.jmx").toString());
        
		 NodeList list = param1.getElementsByTagName("elementProp");
		 Element element = (Element) list.item(0);
		 Node dup = dom.importNode(element, true);
		 dom.getElementsByTagName("collectionProp").item(1).appendChild(dup);
		
		 
		 
		 // update JMX
		 
		 dom.getDocumentElement().normalize();
		 TransformerFactory transformerFactory = TransformerFactory.newInstance();
		 Transformer transformer = transformerFactory.newTransformer();
         
         DOMSource source = new DOMSource(dom);
         StreamResult result = new StreamResult(new File("D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\11.jmx"));
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.transform(source, result);
         System.out.println("XML file updated successfully");
        
        
        //lansare comanda
         Process process = Runtime.getRuntime().exec("C:\\Users\\popae\\Downloads\\apache-jmeter-5.4\\apache-jmeter-5.4\\bin\\jmeter.bat -n -t src\\11.jmx -l src\\23.csv");
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		 String line = "";
		 List<String> myList = new ArrayList<String>();
		 while ((line = reader.readLine()) != null) {
			 myList.add(line);
			 System.out.println(line);
		 }
		 System.out.println(myList.get(myList.size()-3));
		 String data = myList.get(myList.size()-3);
		 String[] split = data.split(" ");
		 List<String> values = new ArrayList<String>();
		 for (String ss : split) {
			 values.add(ss);
		 }
		 List<String> par = new ArrayList<String>();
		 for (int i=0; i<values.size();i++) {
			 if(!values.get(i).equals(" ")) par.add(values.get(i));
		 }
//		 System.out.println(values.get(5));
//		 System.out.println(values.get(11));
//		 System.out.println(values.get(15));
//		 System.out.println(values.get(20));
//		 System.out.println(values.get(24));
//		 System.out.println(values.get(30));
		 System.out.println(par);
		 System.out.println(par.get(5));
		 System.out.println(par.get(11));
		 System.out.println(par.get(15));
		 System.out.println(par.get(20));
		 System.out.println(par.get(24));
		 System.out.println(par.get(30));
	}
	
}
