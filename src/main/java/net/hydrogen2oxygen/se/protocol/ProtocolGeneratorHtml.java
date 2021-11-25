package net.hydrogen2oxygen.se.protocol;

import static j2html.TagCreator.*;

public class ProtocolGeneratorHtml {

    public String generateHtml(Protocol protocol) {

        String html = html(
                head(
                        title("Title"),
                        link().withRel("stylesheet").withHref("/css/main.css")
                ),
                body(
                        main(attrs("#main.content"),
                                h1("Heading!")
                        )
                )
        ).renderFormatted();
        return html;
    }
}
