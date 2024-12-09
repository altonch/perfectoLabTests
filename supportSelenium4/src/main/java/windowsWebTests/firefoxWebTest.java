// Created by Christopher Alton
// Version 3.0
// Updated 11-26-2024
package windowsWebTests;

//****** These are the JAVA dependencies required to run this test ******
//****** Without these, the test will be unable to run ******
//****** as these tell the test how to handle the assorted timeouts and ******
//****** the protocols to access the cloud for automation ******
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//****** These are the dependencies required to run this test ******
//****** Without these, the test will be unable to run ******
//****** RemoteWebDriver commands or findElement commands ******
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

//****** These are the dependencies for the Perfecto Reportium Client ******
import com.perfecto.reportium.client.DigitalZoomClient;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

//****** This is the feature file that contains ******
//****** of my cloud URL's and security tokens ******
import myUtilities.logins;

@SuppressWarnings({ "unused" })
public class firefoxWebTest {

	// ****** These are the strings that control the browser details ******
	// ****** We need to set the browserVersion to one we currently support ******
	// ****** We need to set the browserLocation to one of the data centers ******
	// ****** Valid Locations: (US East), (EU Frankfurt), (AP Sydney) ******
	// ****** These are North America, Germany and Australia respectively ******
	private static String browserVersion = "130";
	private static String browserLocation = "US East";

	public static RemoteWebDriver driver;

	public static void main(String[] args) throws MalformedURLException, IOException {

		logins login = new logins();

		String host = login.trial;
		String myToken = login.trialst;

		String myWUT = "https://the-internet.herokuapp.com/login";
		String projectName = "support-Windows-webTest";
		String projectVersion = "3";
		String scriptname = null;

		Map<String, Object> perfectoOptions = new HashMap<>();

		FirefoxOptions browserOptions = new FirefoxOptions();
		browserOptions.setPlatformName("Windows");
		browserOptions.setBrowserVersion(browserVersion);
		perfectoOptions.put("platformVersion", "11");
		perfectoOptions.put("browserName", "Firefox");
		perfectoOptions.put("location", browserLocation);
		perfectoOptions.put("resolution", "1920x1080");
		perfectoOptions.put("securityToken", myToken);
		perfectoOptions.put("scriptName", "support-windows-Firefox");

		browserOptions.setCapability("perfecto:options", perfectoOptions);

		driver = new RemoteWebDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub/"),
				browserOptions);
		System.out.println("Retrieving Browser Type and Session ID");
		System.out.println(driver);
		System.out.println(browserOptions);

// Define execution timeouts and Desktop VM Window Size
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		// Reporting client. For more details, see
		// https://github.com/perfectocode/samples/wiki/reporting
		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
				.withProject(new Project(projectName, projectVersion))
				.withJob(new Job("support-desktopWeb-test", 1)
				.withBranch("troubleshooting"))
				.withContextTags("supportCode")
				.withWebDriver(driver)
				.build();
		ReportiumClient reportiumClient = new ReportiumClientFactory()
				.createPerfectoReportiumClient(perfectoExecutionContext);

		try {

			System.out.println("**************** TEST STARTED ****************");

			Map<String, Object> params = new HashMap<>();

			reportiumClient.testStart(scriptname, new TestContext("desktopWeb", "support", "firefox"));

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
