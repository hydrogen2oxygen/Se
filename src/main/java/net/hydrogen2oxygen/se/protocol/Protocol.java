package net.hydrogen2oxygen.se.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Every automation generates a protocol
 */
public class Protocol {

    private final List<ProtocolEntry> protocolEntryList = new ArrayList<>();

    private String title;

    public enum ProtocolType {
        H1,H2,H3,H4,PARAGRAPH,INFO,WARNING,ERROR,PRECONDITION_FAIL,SCREENSHOT,
        SCREENSHOT_WITH_DESCRIPTION,ASSERT_SUCCESS,ASSERT_FAIL,UNEXPECTED_TECHNICAL_ERROR,
        SKIP
    }

    public enum ProtocolResult {
        SUCCESS, FAIL, PRECONDITION_FAIL, TECHNICAL_ERROR, SKIPPED, UNKNOWN
    }

    public void h1(String title) {
        add(ProtocolType.H1, title);
    }

    public void h2(String title) {
        add(ProtocolType.H2, title);
    }

    public void h3(String title) {
        add(ProtocolType.H3, title);
    }

    public void h4(String title) {
        add(ProtocolType.H4, title);
    }

    public void paragraph(String text) {
        add(ProtocolType.PARAGRAPH, text);
    }

    public void info(String message) {
        add(ProtocolType.INFO, message);
    }

    public void warn(String message) {
        add(ProtocolType.WARNING, message);
    }

    public void error(String message) {
        add(ProtocolType.ERROR, message);
    }

    public void skip(String message) {
        add(ProtocolType.SKIP, message);
    }

    public void preconditionFail(String message) {
        add(ProtocolType.PRECONDITION_FAIL, message);
    }

    public void screenshot(String imageId) {
        add(ProtocolType.SCREENSHOT, imageId);
    }

    public void screenshot(String imageId, String description) {
        add(ProtocolType.SCREENSHOT_WITH_DESCRIPTION, imageId + "|" + description);
    }

    public void assertSuccess(String message) {
        add(ProtocolType.ASSERT_SUCCESS, message);
    }

    public void assertFail(String message) {
        add(ProtocolType.ASSERT_FAIL, message);
    }

    public void unexpectedTechnicalError(String message) {
        add(ProtocolType.UNEXPECTED_TECHNICAL_ERROR, message);
    }

    private void add(ProtocolType protocolType, String data) {
        protocolEntryList.add(new ProtocolEntry(protocolType, data));
    }

    private class ProtocolEntry {

        private ProtocolType protocolType;
        private String data;

        public ProtocolEntry(ProtocolType protocolType, String data) {
            this.protocolType = protocolType;
            this.data = data;
        }

        public ProtocolType getProtocolType() {
            return protocolType;
        }

        public String getData() {
            return data;
        }
    }

    public ProtocolResult getProtocolResult() {

        boolean assertSuccess = false;

        for (ProtocolEntry entry : protocolEntryList) {

            if (entry.protocolType.equals(ProtocolType.UNEXPECTED_TECHNICAL_ERROR)) {
                return ProtocolResult.TECHNICAL_ERROR;
            }

            if (entry.protocolType.equals(ProtocolType.ASSERT_FAIL)) {
                return ProtocolResult.FAIL;
            }

            if (entry.protocolType.equals(ProtocolType.PRECONDITION_FAIL)) {
                return ProtocolResult.PRECONDITION_FAIL;
            }

            if (entry.protocolType.equals(ProtocolType.SKIP)) {
                return ProtocolResult.SKIPPED;
            }

            if (entry.protocolType.equals(ProtocolType.ASSERT_SUCCESS)) {
                assertSuccess = true;
            }
        }

        if (assertSuccess) return ProtocolResult.SUCCESS;

        return ProtocolResult.UNKNOWN;
    }
}
