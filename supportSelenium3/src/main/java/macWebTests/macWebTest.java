// Created by Christopher Alton
// Version 4.0
// Updated 07-11-2024
package macWebTests;

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

@SuppressWarnings({ "unused" })
public class macWebTest {

	private static RemoteWebDriver driver;

	// ***** DEVICE SELECTION *****
	private static String macOS = "sonoma";
	private static String browserType = "chrome";
	private static String browserVersion = "122";
	private static String location = "boston";

	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
		System.out.println("Run started");

		DesiredCapabilities capabilities = new DesiredCapabilities();

		logins login = new logins();
		
//		**** Enter your host name and security token below ****
		String host = login.trial;
		String myToken = login.trialst;
		
// ***** Do Not Change Anything Below This Line *****	
		String myWUT = "https://www.time.is";
		String projectName = "support-desktopWeb-test";
		String projectVersion = "4";
		String scriptname = "";
		String modeset = "";

		switch(macOS){
		case "ventura":
			System.out.println("Creating Session Using Ventura on MAC");
			capabilities.setCapability("platformName", "Mac");
			capabilities.setCapability("platformVersion", "macOS Ventura");
			capabilities.setCapability("resolution", "1440x900");
			capabilities.setCapability("scriptName", "support-ventura-macTest");
			scriptname = "mac-ventura-webTest";
			break;
		case "monterey":
			System.out.println("Creating Session Using Monterey on MAC");
			capabilities.setCapability("platformName", "Mac");
			capabilities.setCapability("platformVersion", "macOS Monterey");
			capabilities.setCapability("resolution", "1440x900");
			capabilities.setCapability("scriptName", "support-monterey-macTest");
			scriptname = "mac-monterey-webTest";
			break;
		case "sonoma":
			System.out.println("Creating Session Using Sonoma on MAC");
			capabilities.setCapability("platformName", "Mac");
			capabilities.setCapability("platformVersion", "macOS Sonoma");
			capabilities.setCapability("resolution", "1440x900");
			capabilities.setCapability("scriptName", "support-sonoma-macTest");
			scriptname = "mac-sonoma-webTest";
			break;
		case "bigsur":
			System.out.println("Creating Session Using Big Sur");
			capabilities.setCapability("platformName", "Mac");
			capabilities.setCapability("platformVersion", "macOS Big Sur");
			capabilities.setCapability("resolution", "1440x900");
			capabilities.setCapability("scriptName", "support-bigsur-macTest");
			scriptname = "mac-bigsur-webtest";
			break;
		}

		switch(browserType){
		case "safari":
			capabilities.setCapability("browserName", "Safari");
			break;
		case "chrome":
			capabilities.setCapability("browserName", "Chrome");
			break;
		case "firefox":
			capabilities.setCapability("browserName", "Firefox");
			break;
		}

		switch(browserVersion){
		case"latest":
			capabilities.setCapability("browserVersion", "latest");
			break;
		case"beta":
			capabilities.setCapability("browserVersion", "beta");	
			break;
		case"126":
			capabilities.setCapability("browserVersion", "126");
			break;
		case"125":
			capabilities.setCapability("browserVersion", "125");
			break;
		case"124":
			capabilities.setCapability("browserVersion", "124");
			break;
		case"123":
			capabilities.setCapability("broswserVersion", "123");
			break;
		case"16":
			capabilities.setCapability("browserVersion", "16");
			break;
		case"15":
			capabilities.setCapability("browserVersion", "15");
			break;
		case"14":
			capabilities.setCapability("browserVersion", "14");
			break;
		}

		switch(location){
		case "bos":
			capabilities.setCapability("location", "NA-US-BOS");
			break;
		case "phx":
			capabilities.setCapability("location", "NA-US-PHX");
			break;
		case "london":
			capabilities.setCapability("location", "EU-UK-LON");
			break;
		case "germany":
			capabilities.setCapability("location", "EU-DE-FRA");
			break;
		case "canada":
			capabilities.setCapability("location", "NA-CA-YYZ");
			break;
		case "aus":
			capabilities.setCapability("location", "APAC-AUS-SYD");
			break;
		}

		// Additional capabilities
		capabilities.setCapability("takesScreenshot", true);
		capabilities.setCapability("securityToken", myToken);
		capabilities.setCapability("waitForAvailableLicense", true);

		System.out.println(capabilities);
		System.out.println("Creating MAC Desktop Web per specified capabilities");
		driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub/"),capabilities);
		System.out.println("Retrieving Browser Type and Session ID");
		System.out.println(driver);


		// Define execution timeouts and Desktop VM Window Size
		driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Reporting client. For more details, see https://github.com/perfectocode/samples/wiki/reporting
		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
				.withProject(new Project(projectName, projectVersion))
				.withJob(new Job("support-mac-webTest", 4)
				.withBranch("troubleshooting"))
				.withContextTags("supportCode")
				.withWebDriver(driver)
				.build();
		ReportiumClient reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

		try {			

			Map<String, Object> params = new HashMap<>();

			reportiumClient.testStart(scriptname, new TestContext("webTest", "supportTest", "troubleshooting"));

			reportiumClient.stepStart("Navigate to the Website");
			System.out.println("Navigate to the Website");
			driver.get(myWUT);

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
				System.out.println("Alternate Verify - Text Not Found");
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

			System.out.println("Completed My Desktop MAC Web Test");

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