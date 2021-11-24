package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.selenium.HyperWebDriver;

public abstract class AbstractBaseAutomation implements IAutomation {

    protected Se se;
    protected HyperWebDriver wd;
    protected Environment env;

    @Override
    public void setSe(Se se) {
        this.se = se;
        this.env = se.getEnvironment();
        this.wd = se.getWebDriver();
    }
}
