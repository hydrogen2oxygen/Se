package net.hydrogen2oxygen.se.snippets;

import net.hydrogen2oxygen.se.AbstractBaseAutomation;
import net.hydrogen2oxygen.se.Snippet;
import net.hydrogen2oxygen.se.exceptions.PreconditionsException;

@Snippet
public class SearchGithub extends AbstractBaseAutomation {

    private String queryString;

    public SearchGithub(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public void checkPreconditions() throws PreconditionsException {

    }

    @Override
    public void run() throws Exception {
        wd.textByName("q", queryString)
                .sendReturnForElementByName("q")
                .screenshot();
    }

    @Override
    public void cleanUp() throws Exception {

    }
}
