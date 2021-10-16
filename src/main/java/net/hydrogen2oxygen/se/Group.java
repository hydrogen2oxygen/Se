package net.hydrogen2oxygen.se;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private List<IAutomation> automationList = new ArrayList<>();

    public void add(IAutomation automation) {
        automationList.add(automation);
    }


}
