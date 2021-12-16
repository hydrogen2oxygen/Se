package net.hydrogen2oxygen.se.snippets;

import net.hydrogen2oxygen.se.AbstractBaseAutomation;
import net.hydrogen2oxygen.se.Snippet;

@Snippet
public class SearchGithub extends AbstractBaseAutomation {

    private String queryString;

    public SearchGithub(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public void run() throws Exception {
        wd.textByName("q", queryString)
                .sendReturnForElementByName("q")
                .screenshot();
    }
}
