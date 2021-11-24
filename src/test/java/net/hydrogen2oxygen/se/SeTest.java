package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.HyperWebDriverException;
import net.hydrogen2oxygen.se.snippets.OpenGithubSearchElectron;
import net.hydrogen2oxygen.se.snippets.OpenGithubSearchHydrogen2oxygen;
import net.hydrogen2oxygen.se.snippets.OpenGithubSearchSelenium;
import net.hydrogen2oxygen.se.snippets.OpenGithubSearchSpringBoot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class SeTest {

    private static Logger logger = LogManager.getLogger(SeTest.class);

    @Test
    public void testSingleSnippet() {

        try {
            Se se = Se.getInstance();
            se.setEnvironment("./exampleEnvironment.json");
            // let's use a snippet
            OpenGithubSearchSelenium openGithubSearchSelenium = new OpenGithubSearchSelenium();
            openGithubSearchSelenium.setSe(se);
            se.run(openGithubSearchSelenium);

        } catch (HyperWebDriverException e) {
            logger.error("Check your driver configuration please!", e);
        } catch (Exception e) {
            logger.error("Unexpected error occured", e);
        }
    }

    @Test
    public void testParallel() {

        try {
            // load the environment
            Environment environment = Se.loadEnvironment();
            // add a group
            Group group1 = new Group(environment, "GitHub1");
            group1.add(new OpenGithubSearchSelenium());
            group1.add(new OpenGithubSearchHydrogen2oxygen());

            // and a second group
            Group group2 = new Group(environment, "GitHub2");
            group2.add(new OpenGithubSearchElectron());
            group2.add(new OpenGithubSearchSpringBoot());

            // run group 1 and 2 in parallel
            Parallel parallel = new Parallel("Parallel Selenium Run, prove of concept", environment);
            parallel.add(group1);
            parallel.add(group2);
            parallel.run();

        } catch (HyperWebDriverException e) {
            logger.error("Check your driver configuration please!", e);
        } catch (Exception e) {
            logger.error("Unexpected error occured", e);
        }
    }
}
