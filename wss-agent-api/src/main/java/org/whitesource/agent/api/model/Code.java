package org.whitesource.agent.api.model;

import java.util.List;

public class Code {
    private List<Line> lines;
    public List<Line> getLines() {
        return lines;
    }
    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

}
