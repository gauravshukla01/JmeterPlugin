package org.cts.oneframework.cucumber.stepdefinition_JMeter;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.cts.oneframework.cucumber.stepdefinition.DefaultStepDefinition;
import org.cts.oneframework.cucumber.stepdefinition_JMeter.steps.JMeterUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class JMeterStepDefinition extends JMeterUtils {

	public JMeterStepDefinition() {
		super();
	}

	@Given("^I provide the following data for HTTPSampler and LoopController:(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*),(.*)$")
	public void SamplerAndLoops(String url, String port, String protocol, String path, String method,
			String samplerName, String body, String loops, String setFirst, String threadName, String threadCount,
			String rampup) {
		// JMeter must be always initialized before execution of tests
		initJMeter();

		setHTTPSampler(DefaultStepDefinition.currentIterationMap.get(url),
				DefaultStepDefinition.currentIterationMap.get(port),
				DefaultStepDefinition.currentIterationMap.get(protocol),
				DefaultStepDefinition.currentIterationMap.get(path),
				DefaultStepDefinition.currentIterationMap.get(method),
				DefaultStepDefinition.currentIterationMap.get(samplerName),
				DefaultStepDefinition.currentIterationMap.get(body));

		setLoopController(DefaultStepDefinition.currentIterationMap.get(loops),
				DefaultStepDefinition.currentIterationMap.get(setFirst));

		setThreadGroup(DefaultStepDefinition.currentIterationMap.get(threadCount),
				DefaultStepDefinition.currentIterationMap.get(threadName),
				DefaultStepDefinition.currentIterationMap.get(rampup));
	}

	@When("I create the TestPlan(.*) with input data and Run the Test$")
	public void createJMTestPlan(String testPlanName) throws Exception {
		createTestPlan(DefaultStepDefinition.currentIterationMap.get(testPlanName));
		//executeTestPlan();
	}

	@Then("^Report should be created$")
	public void reportCreation() throws Exception {
		//createExcelReport();
	}
}
