package net.hydrogen2oxygen.se;

import net.hydrogen2oxygen.se.exceptions.PreconditionsException;

/**
 * Every selenium test, automation or snippet implements this interface
 */
public interface IAutomation {

    /**
     * This works also as a prepare method, because it runs before everything else
     * @throws PreconditionsException thrown if the preconditions don't exist
     */
    void checkPreconditions () throws PreconditionsException;

    /**
     * Main run method of the automation
     * @throws Exception thrown usually when unexpected errors occurs
     */
    void run() throws Exception;

    /**
     * Clean up the mess afterwards
     * @throws Exception thrown usually when unexpected errors occurs
     */
    void cleanUp() throws Exception;
}
