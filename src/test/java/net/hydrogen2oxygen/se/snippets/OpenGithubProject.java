package net.hydrogen2oxygen.se.snippets;

import net.hydrogen2oxygen.se.IAutomation;
import net.hydrogen2oxygen.se.Se;
import net.hydrogen2oxygen.se.exceptions.PreconditionsException;
import net.hydrogen2oxygen.se.selenium.HyperWebDriver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ListIterator;

public class OpenGithubProject implements IAutomation {

    private HyperWebDriver wd;

    public OpenGithubProject(Se se) {
        wd = se.getWebDriver();
    }

    @Override
    public void checkPreconditions() throws PreconditionsException {

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
            System.out.println(element.text());
        }
    }

    @Override
    public void cleanUp() throws Exception {
        //wd.close();
    }
}
