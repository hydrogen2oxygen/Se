package net.hydrogen2oxygen.se;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.hydrogen2oxygen.se.exceptions.EnvironmentException;
import net.hydrogen2oxygen.se.exceptions.HyperWebDriverException;
import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import net.hydrogen2oxygen.se.selenium.HyperWebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * 1) Download the proper Browser Driver (chromedriver.exe for example) or set up a docker module for selenium
 * 2) set the environment with java or system properties (example -Denvironment=exampleEnvironment.json)
 * 3) start your test with se.run(MyTest.java);
 */
public class Se {

    public static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
    public static final String SCREENSHOTS_PATH = "screenshots.path";
    public static final String PROTOCOLS_PATH = "protocols.path";
    public static final String HEADLESS = "headless";
    public static final String ENVIRONMENT = "environment";
    private static Logger logger = LogManager.getLogger(Se.class);
    private static  Se se;
    private Environment environment;
    private HyperWebDriver webDriver;

    private Se(Environment env, HyperWebDriver.DriverTypes webDriverType) throws HyperWebDriverException, EnvironmentException, IOException {

        if (env == null) {
            environment = loadEnvironment(System.getProperty(ENVIRONMENT));
        } else {
            environment = env;
        }

        if (webDriverType == null) {
            webDriverType = HyperWebDriver.DriverTypes.LOCAL_CHROME;
        }

        if (environment.get(WEBDRIVER_CHROME_DRIVER) != null) {
            System.setProperty(WEBDRIVER_CHROME_DRIVER, environment.get(WEBDRIVER_CHROME_DRIVER));
        }

        try {
            webDriver = new HyperWebDriver(webDriverType, null, null, environment.getBoolean(Se.HEADLESS));
        } catch (IllegalStateException e) {
            throw new HyperWebDriverException("Check your driver configuration please!", e);
        }
    }

    public static Environment loadEnvironment() throws EnvironmentException, IOException {
        return loadEnvironment(System.getProperty("environment"));
    }

    public static Environment loadEnvironment(String environmentFileString) throws EnvironmentException, IOException {

        if (environmentFileString == null || environmentFileString.trim().length() == 0) {
            throw new EnvironmentException("No environment provided. You cannot switch an environment without proper configuration. But you can still work with the standard automation.");
        }

        logger.info("Loading environment " + environmentFileString);
        File environmentFile = new File(environmentFileString);

        if (!environmentFile.exists()) {
            throw new EnvironmentException("Unable to load environment " + environmentFileString);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(environmentFile, Environment.class);
    }

    public static Se getInstance() throws HyperWebDriverException, EnvironmentException, IOException {

        if (se == null) {
            se = new Se(null, null);
        }

        return se;
    }

    public static Se getNewInstance() throws HyperWebDriverException, EnvironmentException, IOException {

        return new Se(null, null);
    }

    public static Se getNewInstance(Environment env, HyperWebDriver.DriverTypes webDriverType) throws HyperWebDriverException, EnvironmentException, IOException {

        return new Se(env, webDriverType);
    }

    /**
     * True if the class contains a snippet annotation
     * @param object
     * @return boolean
     */
    public static boolean isSnippet(Object object) {

        if (object == null) {
            return false;
        }

        return object.getClass().isAnnotationPresent(Snippet.class);
    }

    public void setEnvironment(String path) throws IOException {
        setEnvironment(new File(path));
    }

    public void setEnvironment(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.environment = objectMapper.readValue(file, Environment.class);
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void run(IAutomation automation) {
        try {
            logger.info("Running automation " + automation.getClass().getSimpleName());
            automation.checkPreconditions();
            automation.run();
            automation.cleanUp();
        } catch (PreconditionsException pe) {
            logger.error(pe);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public HyperWebDriver getWebDriver() {
        return webDriver;
    }
}
