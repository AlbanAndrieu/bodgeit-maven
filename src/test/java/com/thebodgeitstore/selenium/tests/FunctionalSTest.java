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
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
public class FunctionalSTest
{

    private static final transient Logger LOGGER               = LoggerFactory.getLogger(FunctionalSTest.class);

    public static final String            DEFAULT_CHROMEDRIVER = "/var/lib/chromedriver";                       // "C:\\chromedriver\\chromedriver.exe"
    public static final String            DEFAULT_FIREFOXBIN   = "/usr/lib/firefox/firefox";                    // "C:\\Program Files\\Mozilla Firefox\\firefox.exe"

    public static final String            DEFAULT_HOST         = "localhost";
    public static final String            DEFAULT_PORT         = "9090";
    public static final String            DEFAULT_URL          = "http://" + DEFAULT_HOST + ":" + DEFAULT_PORT;
    public static String                  DEFAULT_SITE         = DEFAULT_URL + "/bodgeit/";
    public static final String            PAGE_TO_LOAD_TIMEOUT = "30000";
    private static WebDriver              DRIVER;
    private static String                 BASE_URL             = DEFAULT_URL;
    private static String                 CHROME_DRIVER        = DEFAULT_CHROMEDRIVER;
    private static String                 FIREFOX_BIN          = DEFAULT_FIREFOXBIN;

    private static DefaultSelenium        SELENIUM;
    private static DesiredCapabilities    CAPABILITIES;

    @BeforeClass
    public static void setUp() throws Exception
    {

        BASE_URL = System.getProperty("webdriver.base.url");

        if (null == BASE_URL)
        {
            System.out.println("Use default webdriver.base.url");
            BASE_URL = DEFAULT_URL;
            System.setProperty("webdriver.base.url", BASE_URL);
        }
        System.out.println("webdriver.base.url is : " + BASE_URL + "\n");

        CHROME_DRIVER = System.getProperty("webdriver.chrome.driver");
        if (null == CHROME_DRIVER)
        {
            System.out.println("Use default webdriver.base.url");
            CHROME_DRIVER = DEFAULT_CHROMEDRIVER;
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER);
        }
        System.out.println("webdriver.chrome.driver is : " + CHROME_DRIVER + "\n");

        FIREFOX_BIN = System.getProperty("webdriver.firefox.bin");
        if (null == FIREFOX_BIN)
        {
            System.out.println("Use default webdriver.firefox.bin");
            FIREFOX_BIN = DEFAULT_FIREFOXBIN;
            System.setProperty("webdriver.firefox.bin", FIREFOX_BIN);
        }
        System.out.println("webdriver.firefox.bin is : " + FIREFOX_BIN + "\n");

        Properties properties = new Properties();
        properties.load(new FileReader("local.properties"));
        String target = properties.getProperty("zap.targetApp");
        if (target != null && target.length() > 0)
        {
            FunctionalSTest.LOGGER.info("Testing URL : {}", target);
            // Theres an override
            setSite(target);
        }

        Proxy proxy = new Proxy();
        String proxyIP = properties.getProperty("zap.proxy");
        FunctionalSTest.LOGGER.info("Proxy IP : {}", proxyIP);
        proxy.setHttpProxy(proxyIP).setFtpProxy(proxyIP).setSslProxy(proxyIP).setNoProxy("");

        // FirefoxProfile profile = new ProfilesIni().getProfile("selenium");

        // We use firefox as an example here.
        // capabilities = DesiredCapabilities.firefox();
        // capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        // FirefoxBinary ffb = new FirefoxBinary(new File(firefoxBin));
        // capabilities.setCapability(FirefoxDriver.BINARY, ffb);

        CAPABILITIES = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("no-sandbox");

        CAPABILITIES.setCapability(ChromeOptions.CAPABILITY, options);
        CAPABILITIES.setJavascriptEnabled(true);

        CAPABILITIES.setCapability(CapabilityType.PROXY, proxy);

        // You could use any webdriver implementation here
        // driver = new FirefoxDriver(capabilities);
        DRIVER = new ChromeDriver(CAPABILITIES);

        DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        // driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        DRIVER.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
        // driver.manage().window().setSize(new Dimension(1920, 1080));
        setDriver(DRIVER);

        SELENIUM = new WebDriverBackedSelenium(DRIVER, BASE_URL);

        Thread.sleep(10000); // 10 s
    }

    private void sleep()
    {
        try
        {
            Thread.sleep(300);
        } catch (InterruptedException e)
        {
            // Ignore
        }

    }

    public void checkMenu(String linkText, String page)
    {
        sleep();
        WebElement link = DRIVER.findElement(By.linkText(linkText));
        link.click();
        sleep();

        assertEquals(DEFAULT_SITE + page, DRIVER.getCurrentUrl());
    }

    public void checkMenuLinks(String page)
    {

        DRIVER.get(DEFAULT_SITE + page);
        // selenium.waitForPageToLoad(PAGE_TO_LOAD_TIMEOUT);
        checkMenu("Home", "home.jsp");

        DRIVER.get(DEFAULT_SITE + page);
        checkMenu("About Us", "about.jsp");

        DRIVER.get(DEFAULT_SITE + page);
        checkMenu("Contact Us", "contact.jsp");

        DRIVER.get(DEFAULT_SITE + page);
        checkMenu("Login", "login.jsp");

        DRIVER.get(DEFAULT_SITE + page);
        checkMenu("Your Basket", "basket.jsp");

        DRIVER.get(DEFAULT_SITE + page);
        checkMenu("Search", "search.jsp");

    }

    @Test
    @Ignore
    public void tstMenuLinks()
    {
        checkMenuLinks("home.jsp");
        checkMenuLinks("about.jsp");
        checkMenuLinks("contact.jsp");
        checkMenuLinks("login.jsp");
        checkMenuLinks("basket.jsp");
        checkMenuLinks("search.jsp");
    }

    public void registerUser(String user, String password)
    {
        DRIVER.get(DEFAULT_SITE + "login.jsp");
        checkMenu("Register", "register.jsp");

        WebElement link = DRIVER.findElement(By.name("username"));
        link.sendKeys(user);

        link = DRIVER.findElement(By.name("password1"));
        link.sendKeys(password);

        link = DRIVER.findElement(By.name("password2"));
        link.sendKeys(password);

        link = DRIVER.findElement(By.id("submit"));
        link.click();
        sleep();

    }

    public void loginUser(String user, String password)
    {
        DRIVER.get(DEFAULT_SITE + "login.jsp");

        WebElement link = DRIVER.findElement(By.name("username"));
        link.sendKeys(user);

        link = DRIVER.findElement(By.name("password"));
        link.sendKeys(password);

        link = DRIVER.findElement(By.id("submit"));
        link.click();
        sleep();
    }

    @Test
    @Ignore
    public void tstRegisterUser()
    {
        // Create random username so we can rerun test
        String randomUser = RandomStringUtils.randomAlphabetic(10) + "@test.com";
        this.registerUser(randomUser, "password");
        assertTrue(DRIVER.getPageSource().indexOf("You have successfully registered with The BodgeIt Store.") > 0);
    }

    @Test
    @Ignore
    public void tstRegisterAndLoginUser()
    {
        // Create random username so we can rerun test
        String randomUser = RandomStringUtils.randomAlphabetic(10) + "@test.com";
        this.registerUser(randomUser, "password");
        assertTrue(DRIVER.getPageSource().indexOf("You have successfully registered with The BodgeIt Store.") > 0);
        checkMenu("Logout", "logout.jsp");

        this.loginUser(randomUser, "password");
        assertTrue(DRIVER.getPageSource().indexOf("You have logged in successfully:") > 0);
    }

    @Test
    @Ignore
    public void tstAddProductsToBasket()
    {
        DRIVER.get(DEFAULT_SITE + "product.jsp?typeid=1");
        sleep();
        DRIVER.findElement(By.linkText("Basic Widget")).click();
        sleep();
        DRIVER.findElement(By.id("submit")).click();
        sleep();

        DRIVER.get(DEFAULT_SITE + "product.jsp?typeid=2");
        sleep();
        DRIVER.findElement(By.linkText("Thingie 2")).click();
        sleep();
        DRIVER.findElement(By.id("submit")).click();
        sleep();

        DRIVER.get(DEFAULT_SITE + "product.jsp?typeid=3");
        sleep();
        DRIVER.findElement(By.linkText("TGJ CCC")).click();
        sleep();
        DRIVER.findElement(By.id("submit")).click();
        sleep();
    }

    @Test
    @Ignore
    public void tstSearch()
    {
        DRIVER.get(DEFAULT_SITE + "search.jsp?q=doo");
        sleep();

        // TODO check the results!
        // driver.findElement(By.name("q")).sendKeys("doo");

    }

    @Test
    public void testAll()
    {
        tstMenuLinks();
        tstRegisterUser();
        tstRegisterAndLoginUser();
        tstAddProductsToBasket();
        tstSearch();

    }

    @AfterClass
    public static void tearDown() throws Exception
    {
        DRIVER.close();
        DRIVER.quit();
    }

    protected static WebDriver getDriver()
    {
        return DRIVER;
    }

    protected static void setDriver(WebDriver aDriver)
    {
        DRIVER = aDriver;
    }

    protected String getSite()
    {
        return DEFAULT_SITE;
    }

    protected static void setSite(String site)
    {
        DEFAULT_SITE = site;
    }
}
