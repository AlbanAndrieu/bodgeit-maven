/*
 * The BodgeIt Store and its related class files.
 *
 * The BodgeIt Store is a vulnerable web application suitable for pen testing
 *
 * Copyright 2011 psiinon@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thebodgeitstore.selenium.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

/*
 * Note that this is an example of how to use ZAP with Selenium tests,
 * not a good example of how to write good Selenium tests!
 */
public class FunctionalSTest {

	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(FunctionalSTest.class);

	public static final String DEFAULT_CHROMEDRIVER = "/var/lib/chromedriver"; // "C:\\chromedriver\\chromedriver.exe"
	public static final String DEFAULT_FIREFOXBIN = "/usr/lib/firefox/firefox"; // "C:\\Program Files\\Mozilla Firefox\\firefox.exe"

	public static final String DEFAULT_HOST = "localhost";
	public static final String DEFAULT_PORT = "9090";
	public static final String DEFAULT_URL = "http://" + DEFAULT_HOST + ":"
			+ DEFAULT_PORT;	
	public static String DEFAULT_SITE = DEFAULT_URL + "/bodgeit/";
    public static final String PAGE_TO_LOAD_TIMEOUT = "30000";
	private WebDriver driver;
	private String baseUrl = DEFAULT_URL;
	private String chromeDriver = DEFAULT_CHROMEDRIVER;
	private String firefoxBin = DEFAULT_FIREFOXBIN;

	private DefaultSelenium selenium;
	private static DesiredCapabilities capabilities;

	@Before
	public void setUp() throws Exception {

		baseUrl = System.getProperty("webdriver.base.url");

		if (null == baseUrl) {
			System.out.println("Use default webdriver.base.url");
			baseUrl = DEFAULT_URL;
			System.setProperty("webdriver.base.url", baseUrl);
		}
		System.out.println("webdriver.base.url is : " + baseUrl + "\n");

		chromeDriver = System.getProperty("webdriver.chrome.driver");
		if (null == chromeDriver) {
			System.out.println("Use default webdriver.base.url");
			chromeDriver = DEFAULT_CHROMEDRIVER;
			System.setProperty("webdriver.chrome.driver", chromeDriver);
		}
		System.out.println("webdriver.chrome.driver is : " + chromeDriver
				+ "\n");

		firefoxBin = System.getProperty("webdriver.firefox.bin");
		if (null == firefoxBin) {
			System.out.println("Use default webdriver.firefox.bin");
			firefoxBin = DEFAULT_FIREFOXBIN;
			System.setProperty("webdriver.firefox.bin", firefoxBin);
		}
		System.out.println("webdriver.firefox.bin is : " + firefoxBin + "\n");

		Properties properties = new Properties();
		properties.load(new FileReader("local.properties"));
		String target = properties.getProperty("zap.targetApp");
		if (target != null && target.length() > 0) {
			FunctionalSTest.LOGGER.info("Testing URL : {}", target);
			// Theres an override
			setSite(target);
		}

		Proxy proxy = new Proxy();
		String proxyIP = properties.getProperty("zap.proxy");
		FunctionalSTest.LOGGER.info("Proxy IP : {}", proxyIP);
		proxy.setHttpProxy(proxyIP).setFtpProxy(proxyIP).setSslProxy(proxyIP)
				.setNoProxy("");

		
		//FirefoxProfile profile = new ProfilesIni().getProfile("selenium");
		 
		// We use firefox as an example here.
		//capabilities = DesiredCapabilities.firefox();
		//capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        //FirefoxBinary ffb = new FirefoxBinary(new File(firefoxBin));
        //capabilities.setCapability(FirefoxDriver.BINARY, ffb);
		
        capabilities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("no-sandbox");
                
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setJavascriptEnabled(true);
        
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		
		// You could use any webdriver implementation here
		//driver = new FirefoxDriver(capabilities);
		driver = new ChromeDriver(capabilities);
		
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        // driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        // driver.manage().window().setSize(new Dimension(1920, 1080));
		this.setDriver(driver);
		
        selenium = new WebDriverBackedSelenium(driver, baseUrl);

        Thread.sleep(10000); // 10 s
	}

	private void sleep() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// Ignore
		}

	}

	public void checkMenu(String linkText, String page) {
		sleep();
		WebElement link = driver.findElement(By.linkText(linkText));
		link.click();
		sleep();

		assertEquals(DEFAULT_SITE + page, driver.getCurrentUrl());
	}

	public void checkMenuLinks(String page) {

		driver.get(DEFAULT_SITE + page);
		//selenium.waitForPageToLoad(PAGE_TO_LOAD_TIMEOUT);
		checkMenu("Home", "home.jsp");

		driver.get(DEFAULT_SITE + page);
		checkMenu("About Us", "about.jsp");

		driver.get(DEFAULT_SITE + page);
		checkMenu("Contact Us", "contact.jsp");

		driver.get(DEFAULT_SITE + page);
		checkMenu("Login", "login.jsp");

		driver.get(DEFAULT_SITE + page);
		checkMenu("Your Basket", "basket.jsp");

		driver.get(DEFAULT_SITE + page);
		checkMenu("Search", "search.jsp");

	}

	@Test
	//@Ignore
	public void tstMenuLinks() {
		checkMenuLinks("home.jsp");
		checkMenuLinks("about.jsp");
		checkMenuLinks("contact.jsp");
		checkMenuLinks("login.jsp");
		checkMenuLinks("basket.jsp");
		checkMenuLinks("search.jsp");
	}

	public void registerUser(String user, String password) {
		driver.get(DEFAULT_SITE + "login.jsp");
		checkMenu("Register", "register.jsp");

		WebElement link = driver.findElement(By.name("username"));
		link.sendKeys(user);

		link = driver.findElement(By.name("password1"));
		link.sendKeys(password);

		link = driver.findElement(By.name("password2"));
		link.sendKeys(password);

		link = driver.findElement(By.id("submit"));
		link.click();
		sleep();

	}

	public void loginUser(String user, String password) {
		driver.get(DEFAULT_SITE + "login.jsp");

		WebElement link = driver.findElement(By.name("username"));
		link.sendKeys(user);

		link = driver.findElement(By.name("password"));
		link.sendKeys(password);

		link = driver.findElement(By.id("submit"));
		link.click();
		sleep();
	}

	@Test
	public void tstRegisterUser() {
		// Create random username so we can rerun test
		String randomUser = RandomStringUtils.randomAlphabetic(10)
				+ "@test.com";
		this.registerUser(randomUser, "password");
		assertTrue(driver.getPageSource().indexOf(
				"You have successfully registered with The BodgeIt Store.") > 0);
	}

	@Test
	public void tstRegisterAndLoginUser() {
		// Create random username so we can rerun test
		String randomUser = RandomStringUtils.randomAlphabetic(10)
				+ "@test.com";
		this.registerUser(randomUser, "password");
		assertTrue(driver.getPageSource().indexOf(
				"You have successfully registered with The BodgeIt Store.") > 0);
		checkMenu("Logout", "logout.jsp");

		this.loginUser(randomUser, "password");
		assertTrue(driver.getPageSource().indexOf(
				"You have logged in successfully:") > 0);
	}

	@Test
	public void tstAddProductsToBasket() {
		driver.get(DEFAULT_SITE + "product.jsp?typeid=1");
		sleep();
		driver.findElement(By.linkText("Basic Widget")).click();
		sleep();
		driver.findElement(By.id("submit")).click();
		sleep();

		driver.get(DEFAULT_SITE + "product.jsp?typeid=2");
		sleep();
		driver.findElement(By.linkText("Thingie 2")).click();
		sleep();
		driver.findElement(By.id("submit")).click();
		sleep();

		driver.get(DEFAULT_SITE + "product.jsp?typeid=3");
		sleep();
		driver.findElement(By.linkText("TGJ CCC")).click();
		sleep();
		driver.findElement(By.id("submit")).click();
		sleep();
	}

	@Test
	public void tstSearch() {
		driver.get(DEFAULT_SITE + "search.jsp?q=doo");
		sleep();

		// TODO check the results!
		// driver.findElement(By.name("q")).sendKeys("doo");

	}

	//@Test
	@Ignore
	public void testAll() {
		tstMenuLinks();
		tstRegisterUser();
		tstRegisterAndLoginUser();
		tstAddProductsToBasket();
		tstSearch();

	}

	@After
	public void tearDown() throws Exception {
		driver.close();
		driver.quit();
	}

	protected WebDriver getDriver() {
		return driver;
	}

	protected void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	protected String getSite() {
		return DEFAULT_SITE;
	}

	protected void setSite(String site) {
		this.DEFAULT_SITE = site;
	}
}
