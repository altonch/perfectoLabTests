// Created by Christopher Alton
// Version 3.0
// Updated 11-24-2024
package androidTests;

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.Select;

//Perfecto Dependencies
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.android.AndroidDriver;

//My Custom Imports for Utilities
import myUtilities.logins;

@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
public class androidWebTest {

private static AndroidDriver driver;

// ****** This section controls the network virtualization function. ******
// ****** Declare true or false on the networkVirtualization string ****
// ****** Set this to true to capture packets using Network Virtualization ******
// ****** Set this to false to disable Network Virtualization ******
// ****** Network Virtualization does not work on hybrid HSS ******
// ****** For Hybrid clouds, this value must be set to false ******
	private static String networkVirtualization = "false";

	public static void main(String[] args) throws MalformedURLException, IOException, UnknownHostException {
		System.out.println("Run started");

// ****** This section handles your host cloud URL, security token and device ID ******
// ****** We call our host URL and security token from the logins feature file ******
// ****** located under the myUtilities Class ******
// ****** Declare your host URL in the (host) string ******
// ****** Declare your security token in the (myToken) string ******
// ****** Declare your device ID in the (myDUT) string******

		logins login = new logins();

		String host = login.trial;
		String myToken = login.trialst;
		String myDUT = "";

// ****** This section handles the variables that configure the test capabilities ******
// ****** The myWUT string declares the Website Under Test (myWUT) ******
// ****** The scriptname string declares the name of the test (scriptname) ******
// ****** The projectName string declares the project for the test (projectName) ******
// ****** The projectVersion string declares the version of the project (projectVersion) ******
// ****** The browserName string declares the browser we are using (browserName) ******

		String myWUT = "https://the-internet.herokuapp.com/login";
		String bundleid = "";
		String scriptname = "support-androidWebTest";
		String projectName = "support-androidWebTest";
		String projectVersion = "3";
		String browserName = "chrome";
		
		Map<String, Object> perfectoOptions = new HashMap<>();

		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);

		capabilities.setCapability("securityToken", myToken);
		capabilities.setCapability("deviceName", myDUT);
		capabilities.setCapability("automationName", "Appium");
		capabilities.setCapability("appiumVersion", "1.22.3");
		capabilities.setCapability("screenshotOnError", true);
		capabilities.setCapability("takesScreenshot", true);
		capabilities.setCapability("scriptName", scriptname);
		capabilities.setCapability("openDeviceTimeout", 2);

// Build the Constructor based upon Selected Capabilities

		driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub"), capabilities);
		System.out.println("Retrieving Browser Type and Session ID");
		System.out.println(driver);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

// Reporting client
		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
				.withProject(new Project(projectName, projectVersion))
				.withJob(new Job(projectName, 3)
				.withBranch("Troubleshooting"))
				.withContextTags("testCode")
				.withWebDriver(driver)
				.build();
		ReportiumClient reportiumClient = new ReportiumClientFactory()
				.createPerfectoReportiumClient(perfectoExecutionContext);
		try {

			Map<String, Object> params = new HashMap<>();

			reportiumClient.testStart(scriptname, new TestContext("testCode", "support", "appium1"));

			System.out.println("**************** TEST STARTED ****************");

// ****** This is the Network Virtualization Code Block ******
// ****** This handles the request to either run Network Virtualization based ******
// ****** upon the choice of True or False in the networkVirtualization option ******			

			try {
				if (networkVirtualization.equals("true")) {
					reportiumClient.stepStart("Start Network Virtualization");
					System.out.println("Start Network Virtualization");
					params.clear();
					params.put("generateHarFile", "false");
					driver.executeScript("mobile:vnetwork:start", params);
				} else {
				}

			} catch (Exception e) {
				reportiumClient.reportiumAssert("Network Virtualization Failed to Start", false);
			}

// ****** This is the Logs and Vitals Code Block ******
// ****** This handles the requests to gather logs and vitals during your ******
// ****** automated test run. These logs will be attached to your execution report ******

			try {
				reportiumClient.stepStart("Start Vitals and Log Capture");
				System.out.println("Start Vitals and Log Capture");
				params.clear();
				params.put("sources", "Device");
				params.put("interval", 5);
				driver.executeScript("mobile:monitor:start", params);

				params.clear();
				driver.executeScript("mobile:logs:start", params);

			} catch (Exception e) {
				reportiumClient.reportiumAssert("Logging failed to start", false);
			}

				// ****** This is the Main Body of the test ******
			
// ****** This is the Website Get (go to) Code Block ******
// ****** This block handles opening a website using driver.get and ******
// ****** handles any Google Chrome pop ups when the browser is started ******

			reportiumClient.stepStart("Open Website Under Test");
			System.out.println("Open Website Under Test");
			driver.get(myWUT);
			
			reportiumClient.stepStart("Clear Location Disclaimer - If Needed");
			System.out.println("Clear Location Disclaimer - If Needed");
			params.clear();
			params.put("content", "value your privacy");
			params.put("threshold", "80");
			params.put("timeout", "10");
			String disclaimer1 = (String) driver.executeScript("mobile:text:find", params);

			if (disclaimer1.equalsIgnoreCase("true")) {
				// successful checkpoint code
				try {
					reportiumClient.reportiumAssert("Text Found", true);
					params.clear();
					params.put("label", "agree");
					params.put("timeout", "15");
					params.put("threshold", "80");
					driver.executeScript("mobile:button-text:click", params);
				} catch (Exception e) {
				}

			} else {
				// failed checkpoint code
				reportiumClient.reportiumAssert("Text Not Found - No Issue", false);
			}

// ****** This verifies the Log in Page ******
// ****** This block ensures that the web page we requested, actually opened ******
			
			reportiumClient.stepStart("Verify The Login Page");
			System.out.println("Verify The Login Page");
			params.clear();
			params.put("content", "Login Page");
			params.put("threshold", "80");
			params.put("timeout", "30");
			String res1 = (String) driver.executeScript("mobile:text:find", params);

			if (res1.equalsIgnoreCase("true")) {
				// successful checkpoint code
				reportiumClient.reportiumAssert("Text Found", true);
			} else {
				// failed checkpoint code
				reportiumClient.reportiumAssert("Text Not Found - Alternate Search", false);

				reportiumClient.stepStart("Alternate Verify");
				System.out.println("Alternate Verify");
				params.clear();
				params.put("content", "Enter tomsmith");
				params.put("threshold", "80");
				params.put("timeout", "30");
				String res2 = (String) driver.executeScript("mobile:text:find", params);
				if (res2.equalsIgnoreCase("true")) {
					// successful checkpoint code
					reportiumClient.reportiumAssert("Text Found", true);
				} else {
					// failed checkpoint code
					reportiumClient.reportiumAssert("Text Not Found", false);
				}
			}

// ****** This verifies the Context we are in ******
// ****** This is a Web Test using Google Chrome. We need to ensure we are in ******
// ****** either WEBVIEW or CHROMIUM context. We need to be in either of these ******
// ****** contexts in order to see and work with the Web Elements of Chrome ******
			
			reportiumClient.stepStart("Verify WebView Context");
			System.out.println("Verify WebView Context");
			String myContext = (String) driver.getContext();
			System.out.println(myContext);

// ****** These are the variables used for the following activities:  ******
// ****** Variables for xPath locators ******
// ****** Variables for the username and password data
// ****** We use this for Clean Code Writing in your findElement commands ******
			
			String userPath = "//*[@id=\"username\"]";
			String passPath = "//*[@id=\"password\"]";
			String loginButton = "//*[@class=\"radius\"]";
			String logoutButton = "//*[@class=\"button secondary radius\"]";
			
			String userName = "tomsmith";
			String passWord = "SuperSecretPassword!";
			
			String google = "https://www.google.com";
			String clearPopup = "//*[@resource-id=\"com.android.chrome:id/positive_button\"]";

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
			
// ****** This is a code block for a Sanity Check ******
// ****** Some android devices have Google Password Manager. ******
// ****** Password manager may warn us that the password we are using ******
// ****** for the site under test is easy to guess. We need to clear the pop up by ******
// ****** switching to the Native context to clear it. Android OS pop ups ******
// ****** exist outside the web browser, are system level pop ups and are Native OS pop ups. ******
// ****** If there is no pop up, the test logic handles both the pop up and no pop up scenario ******
			
			reportiumClient.stepStart("Check for Password Warning");
			System.out.println("Check for Password Warning");
			params.clear();
			params.put("content", "Change your password");
			params.put("threshold", "80");
			params.put("timeout", "30");
			String popup1 = (String) driver.executeScript("mobile:text:find", params);

			if (popup1.equalsIgnoreCase("true")) {
				// successful checkpoint code
				reportiumClient.reportiumAssert("Password Pop Up Found", true);
				System.out.println("Password Pop Up Found");
				
				try {
				System.out.println("Get Contexts and Switch to Native");
				
				getContextHandles(driver);
				driver.context("NATIVE_APP");    
				
		         reportiumClient.stepStart("Clear the Pop Up");
		         System.out.println("Clear the Pop Up");
		         driver.findElement(By.xpath(clearPopup)).click();
			
				} catch (Exception e) {}				
				
			} else {
				// No Pop Up Code
				reportiumClient.reportiumAssert("No Pop Up - No Issue", true);
			}

			
// ****** This is a code block to verify the log in was successful ******
// ****** The logic verifies that the log in was successful. If it was not successful ******
// ****** the try/catch block inside will action a second step of validation instructions ******
			
			reportiumClient.stepStart("Verify Successful Login");
			System.out.println("Verify Successful Login");
			params.clear();
			params.put("content", "Secure Area");
			params.put("threshold", "80");
			params.put("timeout", "30");
			String auth1 = (String) driver.executeScript("mobile:text:find", params);

			if (auth1.equalsIgnoreCase("true")) {
				// successful checkpoint code
				reportiumClient.reportiumAssert("Text Found", true);
			} else {
				// failed checkpoint code
				reportiumClient.reportiumAssert("Text Not Found - Alternate Verify", false);

				reportiumClient.stepStart("Alternate Verify");
				System.out.println("Alternate Verify");
				params.clear();
				params.put("content", "You logged into");
				params.put("threshold", "80");
				params.put("timeout", "30");
				String auth2 = (String) driver.executeScript("mobile:text:find", params);
				if (auth2.equalsIgnoreCase("true")) {
					// successful checkpoint code
					reportiumClient.reportiumAssert("Text Found", true);
				} else {
					// failed checkpoint code
					reportiumClient.reportiumAssert("Text Not Found", false);
				}
			}

// ****** This is a code block for an End Scenario ******
// ****** We are done with the test. We will now log out of the test page ******
// ****** and go to a neutral website before we close and release the device ******			

			reportiumClient.stepStart("Log Out of TestPage");
			System.out.println("Log Out of TestPage");
			
			try {
			System.out.println("Get Contexts and Switch to Webview");
            driver.context("WEBVIEW");
			driver.findElement(By.xpath(logoutButton)).click();
			
			reportiumClient.stepStart("Move Browswer to a Clean Page");
			System.out.println("Move Browser to a Clean Page");
			driver.get(google);
			} catch (Exception e) {}
			
			
			Thread.sleep(10000);

// ****** This is the code block for the Tear Down Scenario ******
// ****** This will turn off Network Virtualization and stop our log capture ******
// ****** followed by closing the session and releasing the mobile device ******
			
			try {
				if (networkVirtualization.equals("true")) {
					reportiumClient.stepStart("Stop the NV Service");
					System.out.println("Stop the NV Service");
					params.clear();
					driver.executeScript("mobile:vnetwork:stop", params);
				} else {
				}
			} catch (Exception e) {
				reportiumClient.reportiumAssert("NV Failed to Stop", false);
			}

			reportiumClient.stepStart("Stop Log Capture");
			System.out.println("Stop Vitals and Log Capture");
			params.clear();
			driver.executeScript("mobile:logs:stop", params);

			Thread.sleep(10000);

			reportiumClient.reportiumAssert("Successful Test Run", true);
			reportiumClient.testStop(TestResultFactory.createSuccess());
			System.out.println("**************** TEST ENDED ****************");
			System.out.println("Successful Test Run\n");

		} catch (Exception e) {
			Map<String, Object> params = new HashMap<>();
			if (networkVirtualization.equals("true")) {
				reportiumClient.stepStart("Stop the NV service");
				params.clear();
				driver.executeScript("mobile:vnetwork:stop", params);
			} else {
			}

			reportiumClient.stepStart("Stop Log Capture");
			params.clear();
			driver.executeScript("mobile:monitor:stop", params);

			params.clear();
			driver.executeScript("mobile:logs:stop", params);

			reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
			System.out.println("**************** TEST ENDED ****************");
			System.out.println("Unsuccessful Test Run\n");

			e.printStackTrace();

		} finally {
			try {

				driver.quit();

				System.out.println("Run ended - Calling Execution Report Link");

				String reportUrl = reportiumClient.getReportUrl();
				System.out.println(reportUrl);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Completed KnownGood Device Test\n");
	}
	
// ****** This is a method that handles the ability to get the context handles ******
// ****** In this test, we use the method to check the context handle of the browser ******
	
private static List<String> getContextHandles(RemoteWebDriver driver) {
	RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
	List<String> contexts = (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
	return contexts;
	}
}