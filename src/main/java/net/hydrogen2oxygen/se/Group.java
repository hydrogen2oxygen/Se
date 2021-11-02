package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Group implements IAutomation {

    private static Logger logger = LogManager.getLogger(Group.class);
    private Se se;
    private String groupName;
    private List<IAutomation> automationList = new ArrayList<>();

    public Group(Se se, String groupName) {
        this.se = se;
        this.groupName = groupName;
    }

    public void add(IAutomation automation) {
        automationList.add(automation);
    }

    @Override
    public void checkPreconditions() throws PreconditionsException {

        logger.info("Running group automation " + groupName);

        for (IAutomation automation : automationList) {
            try {
                automation.checkPreconditions();
            } catch (PreconditionsException e) {
                logger.error(e);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    @Override
    public void run() throws Exception {
        for (IAutomation automation : automationList) {
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
        se.getWebDriver().close();
    }
}
