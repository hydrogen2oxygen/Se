package net.hydrogen2oxygen.se.snippets;

import net.hydrogen2oxygen.se.IAutomation;
import net.hydrogen2oxygen.se.Se;
import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import net.hydrogen2oxygen.se.selenium.HyperWebDriver;

public class OpenGithubProject implements IAutomation {

    private Se se;
    private HyperWebDriver wd;

    public OpenGithubProject(Se se) {
        this.se = se;
        wd = se.getWebDriver();
    }

    @Override
    public void checkPreconditions() throws PreconditionsException {

    }

    @Override
    public void run() throws Exception {

        wd.openPage("https://github.com/hydrogen2oxygen/Se")
                .waitMillis(1000)
                .textByName("q", "Selenium")
                .sendReturnForElementByName("q")
                .screenshot();
    }

    @Override
    public void cleanUp() throws Exception {

    }
}
