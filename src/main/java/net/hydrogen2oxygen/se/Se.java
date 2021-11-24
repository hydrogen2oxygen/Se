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

public class Se {

    private static Logger logger = LogManager.getLogger(Se.class);
    private static  Se se;
    private Environment environment;
    private HyperWebDriver webDriver;
    private ObjectMapper objectMapper;

    private Se() throws HyperWebDriverException, EnvironmentException {
        objectMapper = new ObjectMapper();
        loadEnvironment(System.getProperty("environment"));
        // TODO implement a nice autoConfigurator ... inside the getInstance() method!
        try {
            webDriver = new HyperWebDriver(HyperWebDriver.DriverTypes.LOCAL_CHROME.name(), null, null);
        } catch (IllegalStateException e) {
            throw new HyperWebDriverException("Check your driver configuration please!", e);
        }
    }

    private void loadEnvironment(String environment) throws EnvironmentException {

        if (environment == null || environment.trim().length() == 0) {
            logger.warn("No environment provided. You cannot switch an environment without proper configuration. But you can still work with the standard automation.");
            return;
        }

        logger.info("Loading environment " + environment);
        File environmentFile = new File(environment);

        if (!environmentFile.exists()) {
            throw new EnvironmentException("Unable to load environment " + environment);
        }
    }

    public static Se getInstance() throws HyperWebDriverException, EnvironmentException {

        if (se == null) {
            se = new Se();
        }

        return se;
    }

    public void setEnvironment(String path) throws IOException {
        setEnvironment(new File(path));
    }

    public void setEnvironment(File file) throws IOException {
        this.environment = objectMapper.readValue(file, Environment.class);
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
