package net.hydrogen2oxygen.se.protocol;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProtocolGeneratorHtmlTest {

    @Test
    public void test() throws Exception {
        Protocol protocol = new Protocol();
        protocol.h1("Simple Protocol Test");
        protocol.paragraph("This will include hopefully every available element.");
        protocol.info("This is a info.");
        protocol.warn("And this is a warning!");
        protocol.error("Beware, this one is a error!!!");
        protocol.h2("Asserts");
        protocol.assertSuccess("Some will succeed.");
        protocol.assertFail("Other will maybe fail!");
        protocol.h3("And there is more");
        protocol.preconditionFail("... like for example if a precondition fails ...");
        protocol.unexpectedTechnicalError("or even an unexpected technical error occurs");
        protocol.h4("Screenshots");
        protocol.screenshot("screenshot8723894723948.jpg");
        protocol.screenshot("screenshot8723894723948.jpg", "The same screenshot, but this time with a nice description");

        ProtocolGeneratorHtml generatorHtml = new ProtocolGeneratorHtml();
        String html = generatorHtml.generateHtml(protocol);
        FileUtils.fileWrite("target/test.html", html);
        assertTrue(html.contains("Screenshots"), "Should contain the text Screenshots");
    }

}
