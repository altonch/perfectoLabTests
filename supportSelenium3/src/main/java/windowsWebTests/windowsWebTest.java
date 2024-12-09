// Created by Christopher Alton
// Version 4.0
// Updated 07-11-2024
package windowsWebTests;

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
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
	private static String browserVersion = "130";
	private static String location = "boston";
	private static String harCapture = "true";

	public static void main(String[] args) throws MalformedURLException, IOException {
		System.out.println("Run started");
		
		logins login = new logins();

		String host = login.trial;
		String myToken = login.trialst;
			
			String myWUT = "https://www.time.is";
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
			case "126":
				capabilities.setCapability("browserVersion", "126");
				break;
			case "125":
				capabilities.setCapability("browserVersion", "125");
				break;
			case "124":
				capabilities.setCapability("browserVersion", "124");
				break;
			case "123":
				capabilities.setCapability("browserVersion", "123");
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

			reportiumClient.testStart(scriptname, new TestContext("webTest", "supportTest"));
			
				reportiumClient.stepStart("Navigate to the Website");
				System.out.println("Navigate to the Website");
				driver.get(myWUT);

				reportiumClient.stepStart("Clear Disclaimer - If needed");
				System.out.println("Clear Disclaimer - If needed");
	            params.clear();
	            params.put("content", "value your privacy");
	            params.put("threshold", "80");
	            params.put("timeout", "15");
	            String disclaimer1 = (String)driver.executeScript("mobile:text:find", params);
	            
	            if (disclaimer1.equalsIgnoreCase("true")) {
	                //successful checkpoint code
	            	try {
	            	reportiumClient.reportiumAssert("Text Found", true);
					System.out.println("Text Found");
	            	params.clear();
	            	params.put("label", "agree");
	            	params.put("timeout", "15");
	            	params.put("threshold", "80");
	            	driver.executeScript("mobile:button-text:click", params);
	            	} catch (Exception e) {}          	
	            	
	            } else {
	                // failed checkpoint code
	            	reportiumClient.reportiumAssert("Text Not Found - No Issue", false);
					System.out.println("Text Not Found - No Issue");
	            }
				
				reportiumClient.stepStart("Verify The Time");
				System.out.println("Verify The Time");
				params.clear();
				params.put("content", "Your Time is");
				params.put("timeout", "30");
				String res1 = (String)driver.executeScript("mobile:text:find", params);

				if (res1.equalsIgnoreCase("true")) {
					//successful checkpoint code
					reportiumClient.reportiumAssert("Text Found", true);
				} else {
					// failed checkpoint code
					reportiumClient.reportiumAssert("Text Not Found - Alternate Search", false);

					reportiumClient.stepStart("Alternate Verify");
					params.clear();
					params.put("content",  "Your clock is");
					params.put("timeout", "30");
					String res2 = (String)driver.executeScript("mobile:text:find", params);
					if (res2.equalsIgnoreCase("true")) {
						//successful checkpoint code
						reportiumClient.reportiumAssert("Text Found", true);
					} else {
						// failed checkpoint code
						reportiumClient.reportiumAssert("Text Not Found", false);
					}  
				}

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

	@SuppressWarnings("deprecation")
	private static void getSource(RemoteWebDriver driver) throws IOException {
		String source = driver.getPageSource();
		System.out.println(source);
		FileUtils.write(new File("C:\\Users\\christophera\\Desktop\\source.xml"), source); 
	}
}
