package net.hydrogen2oxygen.se;

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

    public static final String PING_TIMEOOUT = "ping.timeoout.milliseconds";
    private static Logger logger = LogManager.getLogger(AbstractBaseAutomation.class);

    protected Se se;
    protected HyperWebDriver wd;
    protected Environment env;

    @Override
    public void setSe(Se se) {
        this.se = se;
        this.env = se.getEnvironment();
        this.wd = se.getWebDriver();
    }

    /**
     * Performs a ping to a host
     * @param host
     * @return true if success
     */
    public boolean ping(String host) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            return addr.isReachable(env.getInt(PING_TIMEOOUT));
        } catch (UnknownHostException e) {
            logger.warn("Host " + host + " is unknown!");
            return false;
        } catch (IOException e) {
            logger.warn("Host " + host + " unreachable, TIMEOUT after " + env.getInt("ping.timeoout") + " seconds !");
            return false;
        }
    }
}
