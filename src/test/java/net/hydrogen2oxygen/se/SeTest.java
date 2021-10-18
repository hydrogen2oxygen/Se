package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.HyperWebDriverException;
import net.hydrogen2oxygen.se.selenium.HyperWebDriver;
import net.hydrogen2oxygen.se.snippets.OpenGithubProject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SeTest {

    @Test
    public void testGithubSearch() {

        try {
            Se se = Se.getInstance();
            HyperWebDriver wd = se.getWebDriver();

            // let's use a snippet
            se.run(new OpenGithubProject(se));

            wd.close();
        } catch (HyperWebDriverException e) {
            System.err.println("Check your driver configuration please!");
        }


    }
}
