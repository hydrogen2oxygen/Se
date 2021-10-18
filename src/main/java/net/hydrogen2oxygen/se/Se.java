package net.hydrogen2oxygen.se;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.hydrogen2oxygen.se.exceptions.HyperWebDriverException;
import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import net.hydrogen2oxygen.se.selenium.HyperWebDriver;

import java.io.File;
import java.io.IOException;

public class Se {

    private static  Se se;
    private Environment environment;
    private HyperWebDriver webDriver;
    private ObjectMapper objectMapper;

    private Se() throws HyperWebDriverException {
        objectMapper = new ObjectMapper();
        // TODO implement a nice autoConfigurator ... inside the getInstance() method!
        try {
            webDriver = new HyperWebDriver(HyperWebDriver.DriverTypes.LOCAL_CHROME.name(), null, null);
        } catch (IllegalStateException e) {
            throw new HyperWebDriverException("Check your driver configuration please!", e);
        }
    }

    public static Se getInstance() throws HyperWebDriverException {

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
            automation.checkPreconditions();
            automation.run();
            automation.cleanUp();
        } catch (PreconditionsException pe) {
            // TODO logs
        } catch (Exception e) {
            // TODO logs
        }
    }

    public HyperWebDriver getWebDriver() {
        return webDriver;
    }
}
