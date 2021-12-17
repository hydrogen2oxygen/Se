package net.hydrogen2oxygen.se.protocol;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.specialized.BodyTag;
import j2html.tags.specialized.HeadTag;
import net.hydrogen2oxygen.se.Group;
import net.hydrogen2oxygen.se.IAutomation;
import net.hydrogen2oxygen.se.Parallel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        List<DomContent> content = new ArrayList<>();

        HeadTag head = head(
                title(protocol.getTitle()),
                link().withRel("stylesheet").withHref(BOOTSTRAP_CSS_LINK),
                style().withText(css)
        );

        if (automation instanceof Parallel) {

            Parallel parallel = (Parallel) automation;
            List<IAutomation> automationList = parallel.getAutomationList();

            for (IAutomation a : automationList) {
                generateHtml(a);
            }

            generateOverviewTable(content, automationList);

        } else if (automation instanceof Group) {

            Group group = (Group) automation;
            List<IAutomation> automationList = group.getAutomationList();

            for (IAutomation a : automationList) {
                generateHtml(a);
            }

            generateOverviewTable(content, automationList);
        }

        content.add(div(attrs("#protocol"),
                protocol.getProtocolEntryList().stream().map(p ->
                        p.getDomContent()
                ).toArray(ContainerTag[]::new)
        ));

        BodyTag body = body(
                content.toArray(new DomContent[0])
        );

        String html = html(head, body).renderFormatted();

        File protocolsFolder = new File(protocol.getProtocolsPath());
        if (!protocolsFolder.exists()) {
            protocolsFolder.mkdirs();
        }
        FileUtils.fileWrite(protocol.getProtocolsPath() + protocol.getTitle() + ".html", html);

        return html;
    }

    private void generateOverviewTable(List<DomContent> content, List<IAutomation> automationList) {
        content.add(div(attrs("#protocolOverview"),
                h1("OVERVIEW"),
                table(thead(tr(th("NAME"), th("RESULT")),
                        tbody(automationList.stream().map(a ->
                                        tr(
                                                td(a(a.getProtocol().getTitle()).withHref(a.getProtocol().getTitle() + ".html")),
                                                td("-"))
                                ).toArray(ContainerTag[]::new)
                        ))).withClasses("table","table-striped")));
    }
}
