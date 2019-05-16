package org.cts.oneframework.cucumber.testrunners;

import org.cts.oneframework.cucumber.AbstractTestNGCucumberTest;

import cucumber.api.CucumberOptions;

//@CucumberOptions(features = "src/test/resources/features/CucumberTest.feature", glue = { "org.cts.oneframework.cucumber.stepdefinition" }, plugin = { "pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:" })
public class TestRunner extends AbstractTestNGCucumberTest {
}