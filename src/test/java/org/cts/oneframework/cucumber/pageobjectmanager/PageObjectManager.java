package org.cts.oneframework.cucumber.pageobjectmanager;

import org.cts.oneframework.cucumber.pages.BaseJMeterOperations;
import org.cts.oneframework.cucumber.pages.HomePage;
import org.openqa.selenium.WebDriver;

public class PageObjectManager{

	private WebDriver driver;
	private HomePage homePage;
	//private BaseJMeterOperations baseJM;

	public PageObjectManager(WebDriver driver) {
		this.driver = driver;
	}

	public HomePage getHomePage() {
		return (homePage == null) ? homePage = new HomePage(driver) : homePage;
	}
	
	/*public BaseJMeterOperations getJMeterBase() {
		return (baseJM == null) ? baseJM = new BaseJMeterOperations(driver) : baseJM;
	} */

}