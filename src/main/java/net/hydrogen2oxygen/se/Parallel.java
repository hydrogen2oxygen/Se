package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.EnvironmentException;
import net.hydrogen2oxygen.se.exceptions.HyperWebDriverException;
import net.hydrogen2oxygen.se.exceptions.ParallelExecutionException;
import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Parallel extends AbstractBaseAutomation {

    private static Logger logger = LogManager.getLogger(Parallel.class);
    private List<IAutomation> automationList = new ArrayList<>();

    public Parallel(String parallelName, Environment env) {
        this.env = env;
        initProtocol(parallelName);
    }

    @Override
    public void checkPreconditions() throws PreconditionsException {

    }

    @Override
    public void run() throws Exception {

        ExecutorService es = Executors.newFixedThreadPool(env.getInt("nThreads"));

        for (IAutomation automation : automationList) {
            es.execute(new Thread() {
                public void run() {
                    if (automation instanceof Group) {
                        Group group = (Group) automation;
                        try {
                            group.checkPreconditions();
                            group.run();
                            group.cleanUp();
                        } catch (Exception e) {
                            logger.error("Error during parallel run of group: " + group.getGroupName(), e);
                        }
                    } else {

                            try {
                                Se se = Se.getNewInstance();
                                se.run(automation);
                                se.getWebDriver().close();
                            } catch (HyperWebDriverException e) {
                                logger.error("HyperWebDriverException during parallel run of automation: " + automation.getClass().getSimpleName(), e);
                            } catch (IOException e) {
                                logger.error("IOException during parallel run of automation: " + automation.getClass().getSimpleName(), e);
                            } catch (EnvironmentException e) {
                                logger.error("EnvironmentException during parallel run of automation: " + automation.getClass().getSimpleName(), e);
                            }
                        }
                    }
            });
        }

        es.shutdown();
        boolean finished = es.awaitTermination(env.getInt("parallel.timeout.minutes"), TimeUnit.MINUTES);

        if (!finished) {
            throw new ParallelExecutionException("Parallel execution not finished after " + env.getInt("parallel.timeout.minutes") + " minutes yet!");
        }
    }

    @Override
    public void cleanUp() throws Exception {
        wd.close();
    }

    /**
     * Add a group or single automation which shall run in a parallel thread
     * @param automation
     */
    public void add(IAutomation automation) {
        automationList.add(automation);
    }

    public List<IAutomation> getAutomationList() {
        return automationList;
    }
}
