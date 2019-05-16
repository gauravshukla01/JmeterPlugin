package org.cts.oneframework.cucumber.stepdefinition_JMeter.steps;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;
import org.cts.oneframework.configprovider.ConfigProvider;
import org.cts.oneframework.cucumber.pages.BaseJMeterOperations;
import org.cts.oneframework.cucumber.stepdefinition.AbstractSteps;

public class JMeterUtils {

	BaseJMeterOperations base = new BaseJMeterOperations();

	public static HTTPSamplerProxy HTTPrequest = null;
	public static LoopController loops = null;
	public static ThreadGroup threads = null;
	public static HashTree testPlan = null;

	public void initJMeter() {
		BaseJMeterOperations.init(ConfigProvider.getAsString("jmeter_location"),
				ConfigProvider.getAsString("report_path"));
	}

	public void setHTTPSampler(String url, String port, String protocol, String path, String method, String samplerName,
			String body) {
		HTTPrequest = BaseJMeterOperations.getHTTPSampler(url, Integer.parseInt(port), protocol, path, method,
				samplerName, body);
	}

	public void setLoopController(String loopCount, String setFirst) {
		loops = BaseJMeterOperations.getLoopController(Integer.parseInt(loopCount), Boolean.parseBoolean(setFirst));
	}

	public void setThreadGroup(String tCount, String tName, String rampUp) {
		threads = BaseJMeterOperations.getThreadGroup(tName, Integer.parseInt(tCount), Integer.parseInt(rampUp), loops);
	}

	public void createTestPlan(String tPlanName) throws Exception {
		BaseJMeterOperations.getTestPlan(tPlanName, HTTPrequest, threads);
	}
//
//	public void executeTestPlan() {
//		BaseJMeterOperations.runJMeter(testPlan);
//	}
//
//	public void createExcelReport() throws Exception {
//		BaseJMeterOperations.reportToExcel();
//	}
}
