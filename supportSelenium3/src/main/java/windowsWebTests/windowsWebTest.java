// Created by Christopher Alton
// Version 5.0
// Updated 01-06-2025
package windowsWebTests;

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.Select;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import myUtilities.logins;

@SuppressWarnings({"unused"})
public class windowsWebTest {
	
	private static RemoteWebDriver driver;
	private static String windowsVersion = "11";
	private static String browserType = "chrome";
	private static String browserVersion = "131";
	private static String location = "boston";
	private static String harCapture = "false";

	public static void main(String[] args) throws MalformedURLException, IOException {
		System.out.println("Run started");
		
		logins login = new logins();

		String host = login.trial;
		String myToken = login.trialst;
			
			String myWUT = "https://the-internet.herokuapp.com/login";
			String projectName = "support-windows-webTest";
			String projectVersion = "1";
			String scriptname = null;
				
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("waitForAvailableLicense", true);
			
			switch(windowsVersion){
			case "11":
				capabilities.setCapability("platformVersion", "11");
				break;
			case "10":
				capabilities.setCapability("platformVersion", "10");
				break;
			}
			
			switch(browserType){
			case "chrome":
				System.out.println("Creating VM Using Chrome Capabilities");
				capabilities.setCapability("platformName", "Windows");
				capabilities.setCapability("browserName", "Chrome");
				capabilities.setCapability("resolution", "1600x1200");
				capabilities.setCapability("scriptName", "windows-chrome-webtest");
				scriptname = "Windows-Chrome-Webtest";
				break;
				
			case "ie":
				System.out.println("Creating VM Using Internet Explorer Capabilities");				
				capabilities.setCapability("platformName", "Windows");
				capabilities.setCapability("browserName", "Internet Explorer");
				capabilities.setCapability("resolution", "1600x1200");
				capabilities.setCapability("scriptName", "windows-IE-webtest");
				scriptname = "Windows-InternetExplorer-Webtest";
				break;
				
			case "firefox":
				System.out.println("Creating VM Using Firefox Capabilities");
				capabilities.setCapability("platformName", "Windows");
				capabilities.setCapability("browserName", "Firefox");
				capabilities.setCapability("resolution", "1600x1200");
				capabilities.setCapability("scriptName", "Windows-Firefox-Webtest");
				scriptname = "Windows-Firefox-Webtest";
				break;
			}
			
			switch(browserVersion){
			case "beta":
				capabilities.setCapability("browserVersion", "beta");
				System.out.println("If beta does not work - Try 126 or latest");
				break;
			case "latest":
				capabilities.setCapability("browserVersion", "latest");
				System.out.println("If browser version latest does not work - Try Version 111 or earlier");
				break;
			case "latest-1":
				capabilities.setCapability("browserVersion", "latest-1");
				System.out.println("If browser version latest-1 does not work - Try Version 111 or earlier");
				break;
			case "131":
				capabilities.setCapability("browserVersion", "131");
				break;
			case "130":
				capabilities.setCapability("browserVersion", "130");
				break;
			case "129":
				capabilities.setCapability("browserVersion", "129");
				break;
			case "128":
				capabilities.setCapability("browserVersion", "128");
				break;
			}
			
			switch(location){
			case "boston":
				capabilities.setCapability("location", "US East");
				break;
			case "sydney":
				capabilities.setCapability("location", "AP Sydney");
				break;
			case "germany":
				capabilities.setCapability("location", "EU Frankfurt");
				break;
			}
			
			switch(harCapture){
			case "true":
				capabilities.setCapability("captureHAR", true);
				System.out.println("HAR Capture Enabled");
				break;
			case "false":
				System.out.println("No HAR Capture");
				break;
			}
			  
			// Additional capabilities
			capabilities.setCapability("takesScreenshot", true);
			capabilities.setCapability("securityToken", myToken);
			
				System.out.println(capabilities);
				System.out.println("Creating Windows Desktop Web VM per specified capabilities");
				driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub/"),capabilities);
				System.out.println("Retrieving Browser Type and Session ID");
				System.out.println(driver);

		// Define execution timeouts and Desktop VM Window Size
		driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
        // Reporting client. For more details, see https://github.com/perfectocode/samples/wiki/reporting
		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
				.withProject(new Project(projectName, projectVersion))
				.withJob(new Job("support-windows-webTest", 4)
				.withBranch("troubleshooting"))
				.withContextTags("supportCode")
				.withWebDriver(driver)
				.build();
		ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

		try {			
			
			System.out.println("**************** TEST STARTED ****************");

			Map<String, Object> params = new HashMap<>();

			reportiumClient.testStart(scriptname, new TestContext("desktopWeb", "support", "chrome"));

			reportiumClient.stepStart("Navigate to the Website");
			System.out.println("Navigate to the Website");
			driver.get(myWUT);

			String userPath = "//*[@id=\"username\"]";
			String passPath = "//*[@id=\"password\"]";
			String loginButton = "//*[@class=\"radius\"]";
			String logoutButton = "//*[@class=\"button secondary radius\"]";

			String userName = "tomsmith";
			String passWord = "SuperSecretPassword!";

			String secureArea = "//*[text()=\" Secure Area\"]";

			String google = "https://www.google.com";

// ****** This is the code block for Logging Into the Website under test ******
// ****** We will action findElement commands to send text (sendKeys) and ******
// ****** to action click commands on the page under test ******

			reportiumClient.stepStart("Type in userName");
			System.out.println("Type in userName");
			driver.findElement(By.xpath(userPath)).sendKeys(userName);

			reportiumClient.stepStart("Type in passWord");
			System.out.println("Type in passWord");
			driver.findElement(By.xpath(passPath)).sendKeys(passWord);

			reportiumClient.stepStart("Click Login Button");
			System.out.println("Click Login Button");
			driver.findElement(By.xpath(loginButton)).click();

// ****** This is a code block for a Validation Scenario ******
// ****** We will action a findElement command to verify ******
// ****** if the selected element (secureArea) is visible ****** 	

			try {
				reportiumClient.stepStart("Verify Login Page");
				System.out.println("Verify Login Page");
				WebElement element = driver.findElement(By.xpath(secureArea));
				if (element.isDisplayed()) {
					reportiumClient.reportiumAssert("Login Page is Visible", true);
					System.out.println("Login Page is Visible");
				} else {
					reportiumClient.reportiumAssert("Login Page is Not Visible", false);
					System.out.println("Login Page is Not Visible");
				}
			} catch (Exception e) {
				System.out.println("Check to see why the element was not found");
			}

// ****** This is a code block for an End Scenario ******
// ****** We are done with the test. We will now log out of the test page ******
// ****** and go to a neutral website before we close and release the device ******			

			reportiumClient.stepStart("Log Out of Test Page");
			System.out.println("Log Out of TestPage");
			driver.findElement(By.xpath(logoutButton)).click();

			reportiumClient.stepStart("Move Browser to a Clean Page");
			System.out.println("Move Browser to a Clean Page");
			driver.get(google);

			Thread.sleep(2000);

// ****** This is the code block for the Tear Down Scenario ******				

			reportiumClient.reportiumAssert("Successful Test Run", true);
			System.out.println("Successful Test Run");
			reportiumClient.testStop(TestResultFactory.createSuccess());

			System.out.println("**************** TEST ENDED ****************");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());

			reportiumClient.reportiumAssert("Test Did Not Pass", false);

			reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
		} finally {
			try {
				driver.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			driver.quit();

			System.out.println("Completed My Desktop Windows Web VM Test");

			String reportUrl = reportiumClient.getReportUrl();
			System.out.println(reportUrl);
		}

		System.out.println("Report Link Above");

	}
}
