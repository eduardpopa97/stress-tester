import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class createJMXFile {

	public static void main(String[] argv) throws Exception {
		
		 File myObj = new File("D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\created.jmx");
//		 if (myObj.createNewFile()) {          // nu mai e nevoide de partea asta
//			 System.out.println("File created: " + myObj.getName());
//		 } else {
//		     System.out.println("File already exists.");
//		 }
//		  
//		 FileUtils.copyFile(new File("C:\\Users\\popae\\Downloads\\apache-jmeter-5.4\\apache-jmeter-5.4\\bin\\viewresults.jmx"), myObj);
		 
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 DocumentBuilder db = dbf.newDocumentBuilder();
		 Document dom = db.parse(myObj.toString());
		 Document param = db.parse(new File("D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\parametersDOM.jmx").toString());
		 
//		 NodeList nList = dom.getElementsByTagName("elementProp");
//		 Node nNode = nList.item(1);
//		// nNode.setNodeValue("12");
//		 Element eElement = (Element) nNode;
//		 System.out.println(eElement.getElementsByTagName("stringProp").item(0).getTextContent());
//		 
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
         StreamResult result = new StreamResult(new File("D:\\facultate\\Anul 5\\cercetare\\cercetare - semestrul 2\\schita\\JMeterFromScratch\\src\\createdCOPY.jmx"));
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.transform(source, result);
         System.out.println("XML file updated successfully");
         
         
         //lansare comanda
         Process process = Runtime.getRuntime().exec("C:\\Users\\popae\\Downloads\\apache-jmeter-5.4\\apache-jmeter-5.4\\bin\\jmeter.bat -n -t src\\createdCOPY.jmx -l src\\file2.csv");
         BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		 String line = "";
		 while ((line = reader.readLine()) != null) {
			 System.out.println(line);
		 }
		
		 
   }
}


