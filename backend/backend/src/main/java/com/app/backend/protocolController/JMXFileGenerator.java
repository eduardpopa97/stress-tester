package com.app.backend.protocolController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JMXFileGenerator {

	public void generateFile(String loops, String users, String ramp, String server,
			String method, String path, String port, String protocol, String httpparam, String fileName) throws IOException, ParserConfigurationException, SAXException, TransformerException
	{
		File myObj = new File(
				"D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\templateFiles\\created.jmx");
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(myObj.toString());
		Document param = db.parse(new File(
				"D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\templateFiles\\parametersDOM.jmx")
						.toString());

		// numarul de bucle
		NodeList nList = dom.getElementsByTagName("elementProp");
		Element emp = (Element) nList.item(1);
		Node name = emp.getElementsByTagName("stringProp").item(0).getFirstChild();
		name.setNodeValue(loops);

		// numarul de fire de executie
		NodeList nList1 = dom.getElementsByTagName("ThreadGroup");
		Element emp1 = (Element) nList1.item(0);
		Node name1 = emp1.getElementsByTagName("stringProp").item(2).getFirstChild();
		name1.setNodeValue(users);

		// perioada de timp intre firele de executie
		NodeList nList2 = dom.getElementsByTagName("ThreadGroup");
		Element emp2 = (Element) nList2.item(0);
		Node name2 = emp2.getElementsByTagName("stringProp").item(3).getFirstChild();
		name2.setNodeValue(ramp);

		// domeniul web
		NodeList nList3 = dom.getElementsByTagName("HTTPSamplerProxy");
		Element emp3 = (Element) nList3.item(0);
		Node name3 = emp3.getElementsByTagName("stringProp").item(0).getFirstChild();
		name3.setNodeValue(server);

		// numarui portului
		NodeList nList4 = dom.getElementsByTagName("HTTPSamplerProxy");
		Element emp4 = (Element) nList4.item(0);
		Node name4 = emp4.getElementsByTagName("stringProp").item(1).getFirstChild();
		if(port != null && port != "") name4.setNodeValue(port);
		
		// protocolul
		NodeList nListProtocol = dom.getElementsByTagName("HTTPSamplerProxy");
		Element empProtocol = (Element) nListProtocol.item(0);
		Node nameProtocol = empProtocol.getElementsByTagName("stringProp").item(2).getFirstChild();
		nameProtocol.setNodeValue(protocol);

		// calea catre resursa
		NodeList nList5 = dom.getElementsByTagName("HTTPSamplerProxy");
		Element emp5 = (Element) nList5.item(0);
		Node name5 = emp5.getElementsByTagName("stringProp").item(4).getFirstChild();
		name5.setNodeValue(path);

		// metoda HTTP
		NodeList nList6 = dom.getElementsByTagName("HTTPSamplerProxy");
		Element emp6 = (Element) nList6.item(0);
		Node name6 = emp6.getElementsByTagName("stringProp").item(5).getFirstChild();
		name6.setNodeValue(method);

		// adaugare parametri in cererea HTTP
		String[] valuesHttp = StringUtils.substringsBetween(httpparam , "\"", "\"");
		NodeList nList7 = param.getElementsByTagName("elementProp");
		Element emp7 = (Element) nList7.item(0);
		
		if(valuesHttp != null) {
			
			for(int i=0; i<valuesHttp.length/2; i++) {
				emp7.setAttribute("name", valuesHttp[2*i]);
				Node name7 = emp7.getElementsByTagName("stringProp").item(0).getFirstChild();
				name7.setNodeValue(valuesHttp[2*i+1]);
			
				Node name8 = emp7.getElementsByTagName("stringProp").item(2).getFirstChild();
				name8.setNodeValue(valuesHttp[2*i]);
			
				param.getDocumentElement().normalize();
				TransformerFactory transformerFactory1 = TransformerFactory.newInstance();
				Transformer transformer1 = transformerFactory1.newTransformer();
				DOMSource source1 = new DOMSource(param);
				StreamResult result1 = new StreamResult(new File("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\templateFiles\\createdPARAM.jmx"));
				transformer1.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer1.transform(source1, result1);
			 
				Document param1 = db.parse(new File("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\templateFiles\\createdPARAM.jmx").toString());
	        
				NodeList list = param1.getElementsByTagName("elementProp");
				Element element = (Element) list.item(0);
				Node dup = dom.importNode(element, true);
				dom.getElementsByTagName("collectionProp").item(1).appendChild(dup);
			
				// update JMX
			 
				dom.getDocumentElement().normalize();
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
	         
				DOMSource source = new DOMSource(dom);
				StreamResult result = new StreamResult(new File("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\testingFiles\\ApacheJMeter_Test"
						+ fileName + ".jmx"));
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.transform(source, result);
				//  System.out.println("XML file updated successfully");
			}
		}
		else {
			dom.getDocumentElement().normalize();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
	         
	        DOMSource source = new DOMSource(dom);
	        StreamResult result = new StreamResult(new File("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\testingFiles\\ApacheJMeter_Test"
					+ fileName + ".jmx"));
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.transform(source, result);
		}
		
		//lansare comanda
		String command = "D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\jars\\apache-jmeter-5.4\\bin\\jmeter.bat -n -t src\\main\\java\\com\\app\\backend\\testingFiles\\ApacheJMeter_Test"+fileName+".jmx"+" -l"+" src\\main\\java\\com\\app\\backend\\logFiles\\ApacheJMeter_Test"+fileName+".csv";
		System.out.println(command);
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		List<String> myList = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			myList.add(line);
			System.out.println(line);
		 }
		 System.out.println(myList.get(myList.size()-3));
		 String data = myList.get(myList.size()-3);
		 
		 HTTPController.stats.clear();
		 String ch = "";
		 for(int i=0; i<data.length(); i++) {
			 if (!String.valueOf(data.charAt(i)).equals(" ")) {
				 ch = ch + String.valueOf(data.charAt(i));
			 }
			 else {
				 if(!ch.equals("")) HTTPController.stats.add(ch);
				 ch = "";
			 }
		 }
	}
	
}

