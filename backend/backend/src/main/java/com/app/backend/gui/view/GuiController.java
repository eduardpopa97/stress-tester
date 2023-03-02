package com.app.backend.gui.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.app.backend.protocolController.HTTPController;
import com.app.backend.protocolController.HttpResponseModel;
import com.app.backend.protocolController.TestGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GuiController {

	@FXML
	private Tab Tab1;

	@FXML
	private TextField users;

	@FXML
	private TextField ramp;

	@FXML
	private TextField loops;

	@FXML
	private TextField server;

	@FXML
	private ChoiceBox<String> method;

	@FXML
	private TextField port;

	@FXML
	private TextField path;

	@FXML
	private Button addHttpParameterButton;

	@FXML
	private Button removeHttpParameterButton;

	@FXML
	private TableView<HttpParamClass> httpParamTable;

	@FXML
	private Button runHttpTestButton;

	@FXML
	private TextField paramName;

	@FXML
	private TextField paramValue;

	@FXML
	private Tab Tab2;

	@FXML
	private TextArea responseContent;

	@FXML
	private Tab Tab3;

	@FXML
	private TextArea jmxContentArea;

	@FXML
	private TableView<LogFilesClass> jmxTable;

	@FXML
	private Button displayJMXButton;
	
	@FXML
	private TextArea testedServerResponseContent;

	@FXML
	private Label statisticsResults;

	public void initialize() throws IOException, ParseException {

		method.getItems().add("GET");
		method.getItems().add("POST");
		method.getItems().add("PUT");
		method.getItems().add("DELETE");

		TableColumn<LogFilesClass, String> column = new TableColumn<>("JMX File Name");
		column.setCellValueFactory(new PropertyValueFactory<>("fileName"));
		jmxTable.getColumns().add(column);
		column.prefWidthProperty().bind(jmxTable.widthProperty().multiply(1));

		HttpGet request = new HttpGet("http://localhost:8080/api/getPlans");
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity);
		
		String[] logFiles = result.split("\"");

		for (int i = 0; i < logFiles.length; i++) {
			if (i >= 3 && (i - 3) % 4 == 0)
				jmxTable.getItems().add(new LogFilesClass(logFiles[i]));
		}

		TableColumn<HttpParamClass, String> columnHttpParamName = new TableColumn<>("HTTP Parameter Name");
		columnHttpParamName.setCellValueFactory(new PropertyValueFactory<>("HttpParamName"));
		TableColumn<HttpParamClass, String> columnHttpParamValue = new TableColumn<>("HTTP Parameter Value");
		columnHttpParamValue.setCellValueFactory(new PropertyValueFactory<>("HttpParamValue"));
		httpParamTable.getColumns().add(columnHttpParamName);
		httpParamTable.getColumns().add(columnHttpParamValue);
		columnHttpParamName.prefWidthProperty().bind(httpParamTable.widthProperty().multiply(0.5));
		columnHttpParamValue.prefWidthProperty().bind(httpParamTable.widthProperty().multiply(0.5));
		columnHttpParamName.setResizable(false);
		columnHttpParamValue.setResizable(false);

	}

	@FXML
	void addParam(ActionEvent event) {
		if (paramName.getText().equals("") || paramValue.getText().equals("")) {
			Alert alert = new Alert(AlertType.WARNING, "Parameters cannot have empty options");
			alert.showAndWait();
		} else {
			httpParamTable.getItems().add(new HttpParamClass(paramName.getText(), paramValue.getText()));
			paramName.setText("");
			paramValue.setText("");
		}

	}

	@FXML
	void fetchJMX(ActionEvent event) throws IOException, TransformerFactoryConfigurationError, TransformerException {

		LogFilesClass selectedItems = (LogFilesClass) jmxTable.getSelectionModel().getSelectedItems().get(0);

		if (selectedItems == null) {
			Alert alert = new Alert(AlertType.WARNING, "You haven't chosen a JMX file to display!");
			alert.showAndWait();
		}

		Map<String, String> params = new LinkedHashMap<>();
		params.put("test", selectedItems.getFileName());

		StringBuilder postData = new StringBuilder();
		for (Entry<String, String> param : params.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}

		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		URL url = new URL("http://localhost:8080/api/getPlansDetails");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);

		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		StringBuilder sb = new StringBuilder();
		for (int c; (c = in.read()) >= 0;)
			sb.append((char) c);
		String response = sb.toString();
		org.jsoup.nodes.Document doc = Jsoup
				.parseBodyFragment(response.substring(2).substring(0, response.length() - 4));
		jmxContentArea.setText(doc.body().html());
		jmxContentArea.setEditable(false);
	}

	@FXML
	void removeParam(ActionEvent event) {
		int selectedRow = httpParamTable.getSelectionModel().getSelectedIndex();
		if (selectedRow != -1) {
			int newSelectedRow = (selectedRow == httpParamTable.getItems().size() - 1) ? selectedRow - 1 : selectedRow;
			httpParamTable.getItems().remove(selectedRow);
			httpParamTable.getSelectionModel().select(newSelectedRow);
		} else {
			Alert alert = new Alert(AlertType.WARNING, "You haven't selected an item to remove!");
			alert.showAndWait();
		}
	}

	@SuppressWarnings({ "unchecked" })
	@FXML
    void runTest(ActionEvent event) throws IOException, ParserConfigurationException, SAXException, TransformerException, ParseException {
    	if(users.getText().equals("")     || 
    	   ramp.getText().equals("")      ||
    	   loops.getText().equals("")     ||
    	   server.getText().equals("")    ||
    	   method.getValue().equals("")   ||
    	   port.getText().equals("")) 
    	{
    		Alert alert = new Alert(AlertType.WARNING, "You haven't specified all data for test plan!");
        	alert.showAndWait();
    	}
    	else
    	{
            
            List<String> httpparams = new ArrayList<String>();
            for(int i=0; i<httpParamTable.getItems().size(); i++)
            {
            	httpparams.add(httpParamTable.getItems().get(i).getHttpParamName());
            	httpparams.add(httpParamTable.getItems().get(i).getHttpParamValue());
            
            }
         
 
		String fileName = String.valueOf(System.currentTimeMillis());
		
		JMeterUtils.loadJMeterProperties(
				"C:\\Users\\popae\\Downloads\\apache-jmeter-5.4\\apache-jmeter-5.4\\bin\\jmeter.properties");
		JMeterUtils.setJMeterHome("C:\\Users\\popae\\Downloads\\apache-jmeter-5.4");
		JMeterUtils.setProperty("saveservice_properties", "/saveservice.properties");
		
	    HashTree projectTree = TestGenerator.create(method.getValue(), server.getText(),
				 port.getText(), path.getText(), loops.getText(), users.getText(), ramp.getText());
		SaveService.saveTree(projectTree, new FileOutputStream("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\testingFiles\\HTTP_Test"+fileName+".jmx"));

		PrintWriter writer = new PrintWriter("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\testingFiles\\HTTP_Test"
				+ fileName + ".jmx");
		writer.print("");
		writer.close();
		
		String logFile = "D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\logFiles\\HTTP_Test"
				+ fileName + ".csv";
		
		PrintWriter writer1 = new PrintWriter(logFile);
		writer1.print("");
		writer1.close();
		
		FileInputStream in = new FileInputStream("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\templateFiles\\created.jmx");
	    FileOutputStream out = new FileOutputStream("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\testingFiles\\HTTP_Test"
					+ fileName + ".jmx");
	  
	    try {
	  
	            int n;
	  
	            // read() function to read the
	            // byte of data
	            while ((n = in.read()) != -1) {
	                // write() function to write
	                // the byte of data
	                out.write(n);
	            }
	        }
	    
	    finally {
	            if (in != null) {
	  
	                // close() function to close the
	                // stream
	                in.close();
	            }
	            // close() function to close
	            // the stream
	            if (out != null) {
	                out.close();
	            }
	        }
		
	    File myObj = new File(
				"D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\testingFiles\\HTTP_Test"+fileName+".jmx");
			
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
		name.setNodeValue(loops.getText());
			
		// numarul de fire de executie
		NodeList nList1 = dom.getElementsByTagName("ThreadGroup");
		Element emp1 = (Element) nList1.item(0);
		Node name1 = emp1.getElementsByTagName("stringProp").item(2).getFirstChild();
		name1.setNodeValue(users.getText());

		// perioada de timp intre firele de executie
		NodeList nList2 = dom.getElementsByTagName("ThreadGroup");
		Element emp2 = (Element) nList2.item(0);
		Node name2 = emp2.getElementsByTagName("stringProp").item(3).getFirstChild();
		name2.setNodeValue(ramp.getText());

		// domeniul web
		NodeList nList3 = dom.getElementsByTagName("HTTPSamplerProxy");
		Element emp3 = (Element) nList3.item(0);
		Node name3 = emp3.getElementsByTagName("stringProp").item(0).getFirstChild();
		name3.setNodeValue(server.getText());

		// numarui portului
		NodeList nList4 = dom.getElementsByTagName("HTTPSamplerProxy");
		Element emp4 = (Element) nList4.item(0);
		Node name4 = emp4.getElementsByTagName("stringProp").item(1).getFirstChild();
		if(port != null && port.getText() != "") name4.setNodeValue(port.getText());

		// calea catre resursa
		NodeList nList5 = dom.getElementsByTagName("HTTPSamplerProxy");
		Element emp5 = (Element) nList5.item(0);
	    Node name5 = emp5.getElementsByTagName("stringProp").item(4).getFirstChild();
		name5.setNodeValue(path.getText());

		// metoda HTTP
		NodeList nList6 = dom.getElementsByTagName("HTTPSamplerProxy");
		Element emp6 = (Element) nList6.item(0);
		Node name6 = emp6.getElementsByTagName("stringProp").item(5).getFirstChild();
		name6.setNodeValue(method.getValue());
		

		// adaugare parametri in cererea HTTP
		NodeList nList7 = param.getElementsByTagName("elementProp");
		Element emp7 = (Element) nList7.item(0);

					
		for(int i=0; i<httpparams.size()/2; i++) {
			emp7.setAttribute("name", httpparams.get(2*i));
			Node name7 = emp7.getElementsByTagName("stringProp").item(0).getFirstChild();
			name7.setNodeValue(httpparams.get(2*i+1));
				
			Node name8 = emp7.getElementsByTagName("stringProp").item(2).getFirstChild();
			name8.setNodeValue(httpparams.get(2*i));
				
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
		    StreamResult result = new StreamResult(new File("D:\\facultate\\Anul 5\\cercetare\\backend\\backend\\src\\main\\java\\com\\app\\backend\\testingFiles\\HTTP_Test"
					+ fileName + ".jmx"));
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.transform(source, result);
	
		}
			
		String command = "C:\\Users\\popae\\Downloads\\apache-jmeter-5.4\\apache-jmeter-5.4\\bin\\jmeter.bat -n -t src\\main\\java\\com\\app\\backend\\testingFiles\\HTTP_Test"+fileName+".jmx"+" -l"+" src\\main\\java\\com\\app\\backend\\logFiles\\HTTP_Test"+fileName+".csv";
		System.out.println(command);
	    Process process = Runtime.getRuntime().exec(command);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		List<String> myList = new ArrayList<String>();
			
		while ((line = reader.readLine()) != null) {
			myList.add(line);
			System.out.println(line);
		}
			
//		System.out.println(myList.get(myList.size()-3));
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
			
			
		List<JSONObject> finalarray = new ArrayList<JSONObject>();
		try (BufferedReader br = Files.newBufferedReader(Paths.get("src\\main\\java\\com\\app\\backend\\logFiles\\HTTP_Test"+fileName+".csv"))) {

		    String DELIMITER = ",";
		    String line1;
		    while ((line1 = br.readLine()) != null) {
		        String[] columns = line1.split(DELIMITER);
		        JSONObject obj = new JSONObject();
		        obj.put("responseCode", columns[3]);
		        obj.put("responseMessage", columns[4]);
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

        String serverResponse = gson.toJson(response);
        System.out.println(serverResponse);
		
        StringBuilder statistics = new StringBuilder();
		for(int i=0; i<HTTPController.stats.size(); i++)
		{
			statistics.append(HTTPController.stats.get(i) + " ");
		}
		
		responseContent.setEditable(false);
		responseContent.setText(serverResponse);
		
		statisticsResults.setText(statistics.toString());
		
		
		Map<String, String> params = new LinkedHashMap<>();
		params.put("server", server.getText());

		StringBuilder postData1 = new StringBuilder();
		for (Entry<String, String> s : params.entrySet()) {
			if (postData1.length() != 0)
				postData1.append('&');
			postData1.append(URLEncoder.encode(s.getKey(), "UTF-8"));
			postData1.append('=');
			postData1.append(URLEncoder.encode(String.valueOf(s.getValue()), "UTF-8"));
		}

		byte[] postDataBytes1 = postData1.toString().getBytes("UTF-8");

		URL url1 = new URL("http://localhost:8080/api/getResponse");

		HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
		conn1.setRequestMethod("POST");
		conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn1.setRequestProperty("Content-Length", String.valueOf(postDataBytes1.length));
		conn1.setDoOutput(true);
		conn1.getOutputStream().write(postDataBytes1);

		Reader in1 = new BufferedReader(new InputStreamReader(conn1.getInputStream(), "UTF-8"));

		StringBuilder sb1 = new StringBuilder();
		for (int c; (c = in1.read()) >= 0;)
			sb1.append((char) c);
		String response1 = sb1.toString();
		org.jsoup.nodes.Document doc1 = Jsoup.parseBodyFragment(response1);
		testedServerResponseContent.setText(doc1.body().html());
		testedServerResponseContent.setEditable(false);
		
     }
    	
   }
}
