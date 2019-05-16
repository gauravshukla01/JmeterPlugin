package org.cts.oneframework.cucumber.testrunners;

import org.cts.oneframework.cucumber.AbstractTestNGCucumberTest;

import cucumber.api.CucumberOptions;

@CucumberOptions(tags = {
		"@GooglePerformance" }, features = "src/test/resources/features_JMeter/JMeterTest.feature", glue = {
				"org.cts.oneframework.cucumber.stepdefinition",
				"org.cts.oneframework.cucumber.stepdefinition_JMeter" }, plugin = { "pretty" })
public class JMeterSampleRunner extends AbstractTestNGCucumberTest {
}