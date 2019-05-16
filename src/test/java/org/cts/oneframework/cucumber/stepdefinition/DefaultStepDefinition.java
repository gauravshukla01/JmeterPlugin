package org.cts.oneframework.cucumber.stepdefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.cts.oneframework.excelreader.ReadExcelJmeter;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import ucar.nc2.ft2.Structure.Iterator;

public class DefaultStepDefinition {
	
	public static HashMap<String,String> currentIterationMap;
	private HashMap<String,HashMap<String,String>> excelData = new HashMap<String,HashMap<String,String>>();
	private static String rowName;

	@Before
	public void readScenarioName(Scenario scenario) {
		rowName = scenario.getName();
	}

	@Given("^A workbook named \"([^\"]*)\" and sheet named \"([^\"]*)\" is read$")
	public synchronized void a_workbook_named_and_sheet_named_is_read(String excelName, String sheetName) {
		List<HashMap<String,String>> data = ReadExcelJmeter.readData(excelName,sheetName);
		for(HashMap<String,String> map:data) {
			excelData.put(map.get("TestDataID").trim(), map);
		}
		currentIterationMap = excelData.get(rowName);
		
		/*System.out.println("Excel Data:");
		System.out.println("***************************************");	
		for(String key:currentIterationMap.keySet())
			System.out.println(key+" : "+currentIterationMap.get(key));
		System.out.println("***************************************");*/
	}
}
