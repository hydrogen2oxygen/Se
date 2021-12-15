package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.SnippetException;
import net.hydrogen2oxygen.se.protocol.Protocol;
import net.hydrogen2oxygen.se.selenium.HyperWebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Abstract Base Automation providing basics for all automation classes
 */
public abstract class AbstractBaseAutomation implements IAutomation {

    public static final String PING_TIMEOUT = "ping.timeout.milliseconds";
    public static final int TIME_OUT = 5000;
    private static Logger logger = LogManager.getLogger(AbstractBaseAutomation.class);

    protected Se se;
    protected HyperWebDriver wd;
    protected Environment env;
    protected Protocol protocol = new Protocol();

    @Override
    public void setSe(Se se) {
        this.se = se;
        this.env = se.getEnvironment();
        this.wd = se.getWebDriver();

        if (protocol == null) {
            // for test purposes, set new only if it is still empty
            protocol = new Protocol();
        }
        protocol.setTitle(this.getClass().getSimpleName());
        protocol.setProtocolsPath(env.getData().get(Se.PROTOCOLS_PATH));
        protocol.setScreenshotPath(env.getData().get(Se.SCREENSHOTS_PATH));

        this.wd.setProtocol(protocol);
    }

    /**
     * Runs a snippet
     * @param automation
     */
    public void snippet(IAutomation automation) throws Exception {
        if (!Se.isSnippet(automation)) {
            throw new SnippetException("The class " + automation.getClass().getName() + " is not a Snippet! You need to annotate snippets!");
        }
        automation.setSe(se);
        automation.checkPreconditions();
        automation.run();
        automation.cleanUp();
        getProtocol().getProtocolEntryList().addAll(automation.getProtocol().getProtocolEntryList());
    }

    /**
     * Performs a ping to a host
     * @param host
     * @return true if success
     */
    public boolean ping(String host) {

        Integer timeOut = getTimeOut();

        try {
            InetAddress addr = InetAddress.getByName(host);
            return addr.isReachable(timeOut);
        } catch (UnknownHostException e) {
            protocol.warn("PING - Host " + host + " is unknown!");
            return false;
        } catch (IOException e) {
            logger.warn("PING - Host " + host + " unreachable, TIMEOUT after " + timeOut + " seconds !");
            return false;
        }
    }

    @Override
    public Protocol getProtocol() {
        return protocol;
    }

    public void initProtocol(String title) {
        protocol.setTitle(title);
        protocol.setProtocolsPath(env.getData().get(Se.PROTOCOLS_PATH));
        protocol.setScreenshotPath(env.getData().get(Se.SCREENSHOTS_PATH));
    }

    private Integer getTimeOut() {

        Integer timeOut = env.getInt(PING_TIMEOUT);

        if (timeOut == null) {
            logger.debug(PING_TIMEOUT + " value not set, using default value of " + TIME_OUT + " milliseconds!");
            timeOut = TIME_OUT;
        }

        return timeOut;
    }
}
