package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.HyperWebDriverException;
import net.hydrogen2oxygen.se.snippets.OpenGithubProject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class SeTest {

    private static Logger logger = LogManager.getLogger(SeTest.class);

    @Test
    public void testGithubSearch() {

        try {
            Se se = Se.getInstance();
            // let's use a snippet
            se.run(new OpenGithubProject(se));

            // now do exactly the same, but add it to a group
            Group group = new Group(se, "GitHub");
            group.add(new OpenGithubProject(se));
            se.run(group);
        } catch (HyperWebDriverException e) {
            logger.error("Check your driver configuration please!", e);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
