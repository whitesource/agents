package org.whitesource.agent.api.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds all information necessary for the Effective Usage Analysis to run successfully.
 *
 * @author noam.dolovich
 */
public class AnalysisInputs {
    private Map<String, String> modulesToPaths;
    private String euaArtifactId;
    private boolean emptyDependency;

    public AnalysisInputs() {
    }

    public AnalysisInputs(String euaArtifactId) {
        this.euaArtifactId = euaArtifactId;
    }

    public Map<String, String> getModulesToPaths() {
        if (modulesToPaths == null) {
            modulesToPaths = new HashMap<>();
        }
        return modulesToPaths;
    }

    public void setModulesToPaths(Map<String, String> modulesToPaths) {
        this.modulesToPaths = modulesToPaths;
    }

    public void addDependencyModulesToPaths(String key, String value) {
        getModulesToPaths().put(key, value);
    }

    public String getEuaArtifactId() {
        return euaArtifactId;
    }

    public void setEuaArtifactId(String euaArtifactId) {
        this.euaArtifactId = euaArtifactId;
    }

    public boolean isEmptyDependency() {
        return emptyDependency;
    }

    public void setEmptyDependency(boolean emptyDependency) {
        this.emptyDependency = emptyDependency;
    }
}
