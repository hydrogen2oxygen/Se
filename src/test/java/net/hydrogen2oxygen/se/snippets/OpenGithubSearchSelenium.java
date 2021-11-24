package net.hydrogen2oxygen.se.snippets;

import net.hydrogen2oxygen.se.AbstractBaseAutomation;
import net.hydrogen2oxygen.se.IAutomation;
import net.hydrogen2oxygen.se.Se;
import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import net.hydrogen2oxygen.se.selenium.HyperWebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OpenGithubSearchSelenium extends AbstractBaseAutomation {

    private static Logger logger = LogManager.getLogger(OpenGithubSearchSelenium.class);

    @Override
    public void checkPreconditions() throws PreconditionsException {
        assertNotNull(wd);
    }

    @Override
    public void run() throws Exception {

        wd.openPage("https://github.com/hydrogen2oxygen/Se")
                .waitMillis(1000)
                .textByName("q", "Selenium")
                .sendReturnForElementByName("q")
                .screenshot();

        String html = wd.getHtml();
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("a");
        ListIterator<Element> iter = elements.listIterator();

        while (iter.hasNext()) {
            Element element = iter.next();
            logger.debug(element.text());
        }
    }

    @Override
    public void cleanUp() throws Exception {
        //wd.close(); ... don't do this inside a snippet or inside a automation intended to run inside a group
    }
}