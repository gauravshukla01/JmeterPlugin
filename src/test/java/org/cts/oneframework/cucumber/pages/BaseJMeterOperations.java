package org.cts.oneframework.cucumber.pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.assertions.gui.AssertionGui;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.LoopControlPanel;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.threads.gui.ThreadGroupGui;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cts.oneframework.excelreader.ExcelDataProviderJmeter;
import org.cts.oneframework.utilities.BasePageObject;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.w3c.dom.Node;

public class BaseJMeterOperations {

	public static StandardJMeterEngine jmeter = new StandardJMeterEngine();

	public static File jmeterHome = null;
	public static File jmeterProperties = null;
	public static HashTree testPlanTree = null;
	public static HTTPSamplerProxy examplecomSampler = null;
	public static LoopController loopController = null;
	public static ThreadGroup threadGroup = null;
	public static TestPlan testPlan = null;

	public static HeaderManager manager = null;
	public static String time = null;
	public static String reportPath = null;

	public static void init(String jMHome, String reportFolder) {

		jmeterHome = new File(jMHome);
		reportPath = reportFolder;
		String slash = System.getProperty("file.separator");
		// path = ConfigProvider.getAsString("TestDataPath");

		if (jmeterHome.exists()) {
			jmeterProperties = new File(jmeterHome.getPath() + slash + "bin" + slash + "jmeter.properties");
			if (jmeterProperties.exists()) {
				// JMeter Engine
				jmeter = new StandardJMeterEngine();

				// JMeter initialization (properties, log levels, locale, etc)
				JMeterUtils.setJMeterHome(jmeterHome.getPath());
				JMeterUtils.loadJMeterProperties(jmeterProperties.getPath());
				// JMeterUtils.initLogging(); // you can comment this line out to see extra log
				// messages of i.e. DEBUG level
				JMeterUtils.initLocale();

				// JMeter Test Plan
				testPlanTree = new HashTree();
			}
		}
	}

	@DataProvider(name = "data")
	public Object[][] readExcelData(Method method) {
		ExcelDataProviderJmeter excelDataProvider = new ExcelDataProviderJmeter(getClass());
		excelDataProvider.getExcelDetails();
		return excelDataProvider.data(method);
	}

	public static HeaderManager getHTTPHeader() {
		manager = new HeaderManager();
		manager.add(new Header("Content-Type", "application/json"));
		manager.setName(JMeterUtils.getResString("header_manager_title")); // $NON-NLS-1$
		manager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
		manager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
		return manager;
	}

	public static HTTPSamplerProxy getHTTPSampler(String url, int port, String protocol, String path, String method,
			String name, String body) {
		examplecomSampler = new HTTPSamplerProxy();
		examplecomSampler.setHeaderManager(getHTTPHeader());
		examplecomSampler.setDomain(url);
		examplecomSampler.setPort(port);
		examplecomSampler.setProtocol(protocol);
		examplecomSampler.setPath(path);
		if (method.equals("GET") || method.equals("get")) {
			examplecomSampler.setMethod(method);
			if (body.equals("{}")) {
				examplecomSampler.addNonEncodedArgument("", body, "");
				examplecomSampler.setPostBodyRaw(true);
			}
		} else if (method.equals("POST") || method.equals("post")) {
			examplecomSampler.setMethod(method);
			examplecomSampler.addNonEncodedArgument("", body, "");
			examplecomSampler.setPostBodyRaw(true);
		} else if (method == "PUT" || method == "put") {
			examplecomSampler.setMethod(method);
			examplecomSampler.addNonEncodedArgument("", body, "");
			examplecomSampler.setPostBodyRaw(true);
		} else if (method == "DELETE" || method == "delete") {
			examplecomSampler.setMethod(method);
		}
		examplecomSampler.setName(name);
		/*
		 * examplecomSampler.setFollowRedirects(true);
		 * examplecomSampler.setAutoRedirects(true);
		 * examplecomSampler.setUseKeepAlive(true);
		 * examplecomSampler.setDoMultipartPost(true);
		 * examplecomSampler.setDoBrowserCompatibleMultipart(true);
		 */
		examplecomSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
		examplecomSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
		return examplecomSampler; 
	}

	public static LoopController getLoopController(int count, boolean val) {
		loopController = new LoopController();
		loopController.setLoops(count);
		loopController.setFirst(val);
		loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
		loopController.setProperty(TestElement.GUI_CLASS, LoopControlPanel.class.getName());
		loopController.initialize();
		return loopController;
	}

	public static ThreadGroup getThreadGroup(String name, int num, int rampup, LoopController loopController) {
		threadGroup = new ThreadGroup();
		threadGroup.setName(name);
		threadGroup.setNumThreads(num);
		threadGroup.setRampUp(rampup);
		threadGroup.setSamplerController(loopController);
		threadGroup.setProperty(TestElement.TEST_CLASS, ThreadGroup.class.getName());
		threadGroup.setProperty(TestElement.GUI_CLASS, ThreadGroupGui.class.getName());
		return threadGroup;
	}

	public static ResponseAssertion addAssertion(String resField, String responsePatternType) {
		ResponseAssertion assertion = new ResponseAssertion();
		assertion.setProperty(TestElement.GUI_CLASS, AssertionGui.class.getName());
		assertion.setProperty(TestElement.TEST_CLASS, ResponseAssertion.class.getName());
		assertion.setName("Assertion");
		assertion.setEnabled(true);

		switch (resField) {
		case "TEXT":
			assertion.setTestFieldResponseData();
			break;
		case "DOCUMENT":
			assertion.setTestFieldResponseDataAsDocument();
			break;
		case "URL":
			assertion.setTestFieldURL();
			break;
		case "RESPONSE_CODE":
			assertion.setTestFieldResponseCode();
			break;
		case "RESPONSE_MESSAGE":
			assertion.setTestFieldResponseMessage();
			break;
		case "RESPONSE_HEADERS":
			assertion.setTestFieldResponseHeaders();
			break;
		}

		switch (responsePatternType) {
		case "CONTAINS":
			assertion.setToContainsType();
			break;
		case "MATCHES":
			assertion.setToMatchType();
			break;
		case "EQUALS":
			assertion.setToEqualsType();
			break;
		case "SUBSTRING":
			assertion.setToSubstringType();
			break;
		}

		assertion.setCustomFailureMessage("JMeter test failed...Check the input provided");
		assertion.addTestString("301");
		assertion.addTestString("200");
		assertion.addTestString("302");

		boolean or = true;
		boolean not = false;

		if (or)
			assertion.setToOrType();
		else if (not)
			assertion.setToNotType();
		return assertion;
	}

	public static HashTree getTestPlanWithAssertion(String name, HTTPSamplerProxy examplecomSampler,
			ThreadGroup threadGroup, ResponseAssertion assertion) throws FileNotFoundException, IOException {
		testPlan = new TestPlan(name);

		testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
		testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
		testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

		// Construct Test Plan from previously initialized elements
		testPlanTree.add(testPlan);
		HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
		threadGroupHashTree.add(assertion);
		threadGroupHashTree.add(examplecomSampler);

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM_dd_hh_mm");
		time = "Report_" + dateFormat.format(now);
		File dir = new File(reportPath + time);
		dir.mkdir();

		// JMeter's .jmx file format
		SaveService.saveTree(testPlanTree, new FileOutputStream(reportPath + time + "\\jmeter_api_sample.jmx"));

		return testPlanTree;
	}

	public static void getTestPlan(String name, HTTPSamplerProxy examplecomSampler, ThreadGroup threadGroup)
			throws Exception {
		testPlan = new TestPlan(name);

		testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
		testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());
		testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());

		// Construct Test Plan from previously initialized elements
		testPlanTree.add(testPlan);
		HashTree threadGroupHashTree = testPlanTree.add(testPlan, threadGroup);
		threadGroupHashTree.add(examplecomSampler);

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM_dd_hh_mm");
		time = "Report_" + dateFormat.format(now);
		File dir = new File(reportPath + time);
		dir.mkdir();

		// JMeter's .jmx file format
		SaveService.saveTree(testPlanTree, new FileOutputStream(reportPath + time + "\\jmeter_api_sample.jmx"));

		reportToExcel();
	}

	public static String runJMeter(HashTree testPlanTree) {
		System.out.println("running...");
		jmeter.configure(testPlanTree);
		jmeter.run();
		return "Done";
	}
	//new
	static String xmlFile;
	static XSSFWorkbook workBook;
	
	public static void reportToExcel() throws IOException, Exception {
		String reportFile = reportPath + time + "\\" + time + "report.jtl";
		xmlFile = reportPath + time + "\\" + time + "_report.xml";

		Summariser summer = null;
		String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
		if (summariserName.length() > 0) {
			summer = new Summariser(summariserName);
		}

		ResultCollector logger = new ResultCollector(summer);
		logger. setFilename(reportFile);
		ResultCollector xmllogger = new ResultCollector(summer);
		xmllogger. setFilename(xmlFile);
		testPlanTree.add(testPlanTree.getArray()[0], logger);
		testPlanTree.add(testPlanTree.getArray()[0], xmllogger);
		String exe = runJMeter(testPlanTree);
		System.out.println("JMeter process is: "+ exe);

		String sourcePath = System.getProperty("user.dir") + "\\src\\test\\resources\\data\\Report_Template.xlsx";
		String xlsxFileAddress = reportPath + time +"\\"+ time + ".xlsx"; 

		FileInputStream excelClone = new FileInputStream(new File(sourcePath));
		workBook = new XSSFWorkbook(excelClone);
		XSSFSheet sheet = workBook.getSheet("Performance Report");

		File file = new File(xmlFile);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(file);
		document.getDocumentElement().normalize();

		NodeList nList = document.getElementsByTagName("httpSample");

		int RowNum = 0;
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				int j = 0;
				RowNum++;
				XSSFRow currentRow = sheet.createRow(RowNum);
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("tn"));
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("ts"));
				
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("t"));
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("lb"));
				
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("rc"));
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("rm"));
				
				currentRow.createCell(j++)
						.setCellValue(eElement.getElementsByTagName("responseData").item(0).getTextContent());
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("dt"));
				
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("s"));
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("by"));
				
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("sby"));
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("ng"));
				
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("na"));
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("lt"));
				
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("it"));
				currentRow.createCell(j++).setCellValue(eElement.getAttribute("ct"));
			}
		}

		FileOutputStream fileOutputStream = new FileOutputStream(xlsxFileAddress);
		workBook .write(fileOutputStream);
		fileOutputStream.close();
		System.out.println("Done");
	}

	public static Map parseJSONResponse(String key) throws Exception {
		File file = new File(xmlFile);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(file);
		document.getDocumentElement().normalize();

		Map result = new HashMap<String, String>();
		NodeList nList = document.getElementsByTagName("httpSample");

		for (int i = 0; i < nList.getLength(); i++) {
		Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String response = eElement.getElementsByTagName("responseData").item(0).getTextContent();
				String thread = eElement.getAttribute("tn");
				JSONObject res = new JSONObject(response);
				result.put(thread, res.get(key));
			}
		}
		return result;
	}
}
