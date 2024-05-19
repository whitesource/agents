package org.whitesource.agent.api.dispatch;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anna.rozin
 */
public class ConfigurationResult extends BaseResult {

    /* --- Static members --- */

    private static final long serialVersionUID = 4057058239018411900L;

    /* --- Members --- */

    private String id;

    private boolean checkPolicies;
    private boolean forceCheckAllDependencies;
    private boolean forceUpdate;

    private boolean projectPerFolder;
    private UpdateType updateType;
    private String requesterEmail;

    private boolean caseSensitiveGlob;
    private boolean followSymbolicLinks;

    private boolean scanPackageManager;

    // FSA|Build Servers|DockerAgent
    private String includes;
    private String excludes;

    private FsaConfiguration fsaConfiguration;

    private Map<AppFlags, Object> flags;
    /* --- Constructors --- */

    public ConfigurationResult() {
        fsaConfiguration = new FsaConfiguration();
        flags = new HashMap<>();
    }

    public ConfigurationResult(boolean checkPolicies, boolean forceCheckAllDependencies, boolean forceUpdate, String includes,
                               String excludes) {
        this();
        this.checkPolicies = checkPolicies;
        this.forceCheckAllDependencies = forceCheckAllDependencies;
        this.forceUpdate = forceUpdate;
        this.includes = includes;
        this.excludes = excludes;
    }

    public ConfigurationResult(boolean checkPolicies, boolean forceCheckAllDependencies, boolean forceUpdate, String includes,
                               String excludes, Map<AppFlags, Object> flags) {
        this();
        this.checkPolicies = checkPolicies;
        this.forceCheckAllDependencies = forceCheckAllDependencies;
        this.forceUpdate = forceUpdate;
        this.includes = includes;
        this.excludes = excludes;
        this.flags = flags;
    }


    /* --- Getters / Setters --- */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCheckPolicies() {
        return checkPolicies;
    }

    public void setCheckPolicies(boolean checkPolicies) {
        this.checkPolicies = checkPolicies;
    }

    public boolean isForceCheckAllDependencies() {
        return forceCheckAllDependencies;
    }

    public void setForceCheckAllDependencies(boolean forceCheckAllDependencies) {
        this.forceCheckAllDependencies = forceCheckAllDependencies;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isProjectPerFolder() {
        return projectPerFolder;
    }

    public void setProjectPerFolder(boolean projectPerFolder) {
        this.projectPerFolder = projectPerFolder;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getIncludes() {
        return includes;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public boolean isCaseSensitiveGlob() {
        return caseSensitiveGlob;
    }

    public void setCaseSensitiveGlob(boolean caseSensitiveGlob) {
        this.caseSensitiveGlob = caseSensitiveGlob;
    }

    public boolean isFollowSymbolicLinks() {
        return followSymbolicLinks;
    }

    public void setFollowSymbolicLinks(boolean followSymbolicLinks) {
        this.followSymbolicLinks = followSymbolicLinks;
    }

    public boolean isScanPackageManager() {
        return scanPackageManager;
    }

    public void setScanPackageManager(boolean scanPackageManager) {
        this.scanPackageManager = scanPackageManager;
    }

    public FsaConfiguration getFsaConfiguration() {
        return fsaConfiguration;
    }

    public void setFsaConfiguration(FsaConfiguration fsaConfiguration) {
        this.fsaConfiguration = fsaConfiguration;
    }

    public void setFlags(Map<AppFlags, Object> flags) {
        this.flags = flags;
    }
    public Map<AppFlags, Object> getFlags() {
        return this.flags;
    }

}
