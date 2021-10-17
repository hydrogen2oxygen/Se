package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.HyperWebDriverException;
import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import net.hydrogen2oxygen.se.selenium.HyperWebDriver;

public class Se {

    private static  Se se;
    private HyperWebDriver webDriver;

    private Se() throws HyperWebDriverException {
        // TODO implement a nice autoConfigurator ... inside the getInstance() method!
        webDriver = new HyperWebDriver(HyperWebDriver.DriverTypes.LOCAL_CHROME.name(),null, null);
    }

    public Se getInstance() throws HyperWebDriverException {

        if (se == null) {
            // TODO get the Config for Selenium, Drivers, Global Environments etc here (use a convention)
            se = new Se();
        }

        return se;
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
}
