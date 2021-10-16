package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import net.hydrogen2oxygen.se.selenium.HyperWebDriver;

public class Se extends HyperWebDriver {

    private static  Se se;

    private Se() {
        // TODO implement a nice autoConfigurator ... inside the getInstance() method!
        super();
    }

    public Se getInstance() {

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
