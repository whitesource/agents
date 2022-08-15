package org.whitesource.agent.api.model;

import java.io.Serializable;

public class CvssInfo implements Serializable {
    private String V2Vector;
    private String V3Vector;
    private String V2Score;
    private String V3Score;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public CvssInfo() {}

    public String getV2Vector() {
        return V2Vector;
    }

    public void setV2Vector(String v2Vector) {
        V2Vector = v2Vector;
    }

    public String getV3Vector() {
        return V3Vector;
    }

    public void setV3Vector(String v3Vector) {
        V3Vector = v3Vector;
    }

    public String getV2Score() {
        return V2Score;
    }

    public void setV2Score(String v2Score) {
        V2Score = v2Score;
    }

    public String getV3Score() {
        return V3Score;
    }

    public void setV3Score(String v3Score) {
        V3Score = v3Score;
    }
}
