package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.EnvironmentException;
import net.hydrogen2oxygen.se.exceptions.HyperWebDriverException;
import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Group extends AbstractBaseAutomation {

    private static Logger logger = LogManager.getLogger(Group.class);
    private String groupName;
    private List<IAutomation> automationList = new ArrayList<>();

    /**
     * Don't use this constructor for parallel automation, only for simple grouping.
     * @param se
     * @param groupName
     */
    public Group(Se se, String groupName) {
        this.setSe(se);
        this.groupName = groupName;
    }

    /**
     * Use this constructor for parallel automation, because it will create a separate instance of selenium webDriver
     * @param environment
     * @param groupName
     * @throws HyperWebDriverException
     * @throws IOException
     * @throws EnvironmentException
     */
    public Group(Environment environment, String groupName) throws HyperWebDriverException, IOException, EnvironmentException {
        this.se = Se.getNewInstance();
        this.se.setEnvironment("./exampleEnvironment.json");
        this.groupName = groupName;
    }

    public void add(IAutomation automation) {
        automation.setSe(se);
        automationList.add(automation);
    }

    @Override
    public void checkPreconditions() throws PreconditionsException {

        logger.info("Running group automation checking preconditions for " + groupName);

        for (IAutomation automation : automationList) {
            try {
                automation.checkPreconditions();
            } catch (PreconditionsException e) {
                logger.error("PreconditionsException for automation: " + automation.getClass().getSimpleName(), e);
            } catch (Exception e) {
                logger.error("Unexpected exception for automation: " + automation.getClass().getSimpleName(), e);
            }
        }
    }

    @Override
    public void run() throws Exception {

        logger.info("Running group automation for " + groupName);

        for (IAutomation automation : automationList) {

            logger.info("Running automation " + automation.getClass().getSimpleName());

            try {
                automation.run();
                automation.cleanUp();
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    @Override
    public void cleanUp() throws Exception {
        // finally, a group always close the driver, ALWAYS
        logger.info("Closing driver for group " + groupName);
        se.getWebDriver().close();
    }

    public String getGroupName() {
        return groupName;
    }
}
