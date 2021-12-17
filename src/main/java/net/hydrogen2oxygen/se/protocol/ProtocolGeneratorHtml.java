package net.hydrogen2oxygen.se.protocol;

import j2html.tags.ContainerTag;
import net.hydrogen2oxygen.se.Group;
import net.hydrogen2oxygen.se.IAutomation;
import net.hydrogen2oxygen.se.Parallel;
import org.apache.commons.io.FileUtils;

import java.io.File;

import static j2html.TagCreator.*;

public class ProtocolGeneratorHtml {

    private static final String BOOTSTRAP_CSS_LINK = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css";

    private String cssFile = "css/se.css";

    // TODO render errors overview with inner links on top of the page
    // TODO link single automations to the Groups and Groups to the Parallel
    // TODO create a overview table in Groups (failed, warnings, success etc)
    public String generateHtml(IAutomation automation) throws Exception {

        Protocol protocol = automation.getProtocol();
        String css = FileUtils.fileRead(cssFile);

        if (automation instanceof Parallel) {
            for (IAutomation a : ((Parallel) automation).getAutomationList()) {
                generateHtml(a);
            }
        } else if (automation instanceof Group) {
            for (IAutomation a : ((Group) automation).getAutomationList()) {
                generateHtml(a);
            }
        }

        String html = html(
                head(
                        title(protocol.getTitle()),
                        link().withRel("stylesheet").withHref(BOOTSTRAP_CSS_LINK),
                        style().withText(css)
                ),
                body(
                        div(attrs("#protocol"),
                                protocol.getProtocolEntryList().stream()
                                        .map(Protocol.ProtocolEntry::getDomContent)
                                        .toArray(j2html.tags.DomContent[]::new)
                        )
                )
        ).renderFormatted();

        File protocolsFolder = new File(protocol.getProtocolsPath());
        if (!protocolsFolder.exists()) {
            protocolsFolder.mkdirs();
        }
        FileUtils.fileWrite(protocol.getProtocolsPath() + protocol.getTitle() + ".html", html);

        return html;
    }
}
