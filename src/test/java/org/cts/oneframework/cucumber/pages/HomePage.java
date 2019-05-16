package org.cts.oneframework.cucumber.pages;

import org.cts.oneframework.utilities.AssertionLibrary;
import org.cts.oneframework.utilities.BasePageObject;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePageObject {

	public HomePage(WebDriver driver) {
		super(driver);
	}

	private String TITLE = "//a[text()='redbus']";
	private String SRC = "//*[@id='src']";
	private String DEST = "//*[@id='dest']";
	private String ONWARD_DATE = "//label[@for='onward_cal']";
	private String RETURN_DATE = "//label[@for='return_cal']";
	private String SET_DAY = "//div[@class='rb-calendar']//td[text()='%s']";
	private String GET_MONTH_YEAR = "//div[@class='rb-calendar']//td[@class='monthTitle']";
	private String SEARCH_BUSES = "//button[@id='search_btn']";

	public Search search() {
		return new Search();
	}

	public void assertPageIsDisplayed() {
		AssertionLibrary.assertEquals(getText(TITLE), "redbus", "Verify user is on RedBus home page.");
	}

	public class Search {

		public Search setSource(String src) {
			setInputValue(SRC, src);
			shiftFocusAway(SRC);
			return this;
		}

		public Search setDestination(String dest) {
			setInputValue(DEST, dest);
			shiftFocusAway(DEST);
			return this;
		}

		private String getMonthYearFromCalendar() {
			return getText(GET_MONTH_YEAR);
		}

		private void selectDay(String day) {
			clickElement(String.format(SET_DAY, day), "Day");
		}

		public Search selectOnwardDate(String date) {
			clickElement(ONWARD_DATE, "Onward date");
			String[] dateArray = date.split("-");
			if (getMonthYearFromCalendar().equals(dateArray[1])) {
				selectDay(dateArray[0]);
			}
			return this;
		}

		public Search selectReturnDate(String date) {
			clickElement(RETURN_DATE, "Return date");
			String[] dateArray = date.split("-");
			if (getMonthYearFromCalendar().equals(dateArray[1])) {
				selectDay(dateArray[0]);
			}
			return this;
		}

		public void searchBuses() {
			clickElement(SEARCH_BUSES, "Search button");
		}
	}
}
