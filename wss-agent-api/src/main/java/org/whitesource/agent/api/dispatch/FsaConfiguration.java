package org.whitesource.agent.api.dispatch;

import java.io.Serializable;

/**
 * Created by anna.rozin
 */
public class FsaConfiguration implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -4407661669260351123L;

    /* --- Members --- */

    private boolean npmIgnoreJavaScriptFiles;
    private boolean npmResolveDependencies;
    private boolean npmIncludeDevDependencies;
    private boolean npmRunPreStep;
    private boolean npmIgnoreNpmLsErrors;

    private boolean bowerResolveDependencies;
    private boolean bowerRunPreStep;

    private boolean pythonResolveDependencies;

    private String mavenIgnoredScopes;
    private boolean mavenResolveDependencies;
    private boolean mavenAggregateModules;

    private boolean gradleResolveDependencies;
    private boolean gradleRunAssembleCommand;

    // Docker settings 
    private  String dockerIncludes;
    private  String dockerExcludes;
    private  boolean scanDockerImages;

    // SCM settings - Mainly for FSA
    private String scmType;
    private String scmUser;
    private String scmPass;
    private String scmPPK;
    private String scmUrl;
    private String scmBranch;

    /* --- Constructors --- */

    public FsaConfiguration(){
    }

    /* --- Getters / Setters --- */

    public String getDockerIncludes() {
        return dockerIncludes;
    }

    public void setDockerIncludes(String dockerIncludes) {
        this.dockerIncludes = dockerIncludes;
    }

    public String getDockerExcludes() {
        return dockerExcludes;
    }

    public void setDockerExcludes(String dockerExcludes) {
        this.dockerExcludes = dockerExcludes;
    }

    public boolean isScanDockerImages() {
        return scanDockerImages;
    }

    public void setScanDockerImages(boolean scanDockerImages) {
        this.scanDockerImages = scanDockerImages;
    }

    public boolean isNpmIgnoreJavaScriptFiles() {
        return npmIgnoreJavaScriptFiles;
    }

    public void setNpmIgnoreJavaScriptFiles(boolean npmIgnoreJavaScriptFiles) {
        this.npmIgnoreJavaScriptFiles = npmIgnoreJavaScriptFiles;
    }

    public boolean isNpmResolveDependencies() {
        return npmResolveDependencies;
    }

    public void setNpmResolveDependencies(boolean npmResolveDependencies) {
        this.npmResolveDependencies = npmResolveDependencies;
    }

    public boolean isNpmIncludeDevDependencies() {
        return npmIncludeDevDependencies;
    }

    public void setNpmIncludeDevDependencies(boolean npmIncludeDevDependencies) {
        this.npmIncludeDevDependencies = npmIncludeDevDependencies;
    }

    public boolean isNpmRunPreStep() {
        return npmRunPreStep;
    }

    public void setNpmRunPreStep(boolean npmRunPreStep) {
        this.npmRunPreStep = npmRunPreStep;
    }

    public boolean isNpmIgnoreNpmLsErrors() {
        return npmIgnoreNpmLsErrors;
    }

    public void setNpmIgnoreNpmLsErrors(boolean npmIgnoreNpmLsErrors) {
        this.npmIgnoreNpmLsErrors = npmIgnoreNpmLsErrors;
    }

    public boolean isBowerResolveDependencies() {
        return bowerResolveDependencies;
    }

    public void setBowerResolveDependencies(boolean bowerResolveDependencies) {
        this.bowerResolveDependencies = bowerResolveDependencies;
    }

    public boolean isBowerRunPreStep() {
        return bowerRunPreStep;
    }

    public void setBowerRunPreStep(boolean bowerRunPreStep) {
        this.bowerRunPreStep = bowerRunPreStep;
    }

    public boolean isPythonResolveDependencies() {
        return pythonResolveDependencies;
    }

    public void setPythonResolveDependencies(boolean pythonResolveDependencies) {
        this.pythonResolveDependencies = pythonResolveDependencies;
    }

    public String getMavenIgnoredScopes() {
        return mavenIgnoredScopes;
    }

    public void setMavenIgnoredScopes(String mavenIgnoredScopes) {
        this.mavenIgnoredScopes = mavenIgnoredScopes;
    }

    public boolean isMavenResolveDependencies() {
        return mavenResolveDependencies;
    }

    public void setMavenResolveDependencies(boolean mavenResolveDependencies) {
        this.mavenResolveDependencies = mavenResolveDependencies;
    }

    public boolean isMavenAggregateModules() {
        return mavenAggregateModules;
    }

    public void setMavenAggregateModules(boolean mavenAggregateModules) {
        this.mavenAggregateModules = mavenAggregateModules;
    }

    public boolean isGradleResolveDependencies() {
        return gradleResolveDependencies;
    }

    public void setGradleResolveDependencies(boolean gradleResolveDependencies) {
        this.gradleResolveDependencies = gradleResolveDependencies;
    }

    public boolean isGradleRunAssembleCommand() {
        return gradleRunAssembleCommand;
    }

    public void setGradleRunAssembleCommand(boolean gradleRunAssembleCommand) {
        this.gradleRunAssembleCommand = gradleRunAssembleCommand;
    }

    public String getScmType() {
        return scmType;
    }

    public void setScmType(String scmType) {
        this.scmType = scmType;
    }

    public String getScmUser() {
        return scmUser;
    }

    public void setScmUser(String scmUser) {
        this.scmUser = scmUser;
    }

    public String getScmPass() {
        return scmPass;
    }

    public void setScmPass(String scmPass) {
        this.scmPass = scmPass;
    }

    public String getScmPPK() {
        return scmPPK;
    }

    public void setScmPPK(String scmPPK) {
        this.scmPPK = scmPPK;
    }

    public String getScmUrl() {
        return scmUrl;
    }

    public void setScmUrl(String scmUrl) {
        this.scmUrl = scmUrl;
    }

    public String getScmBranch() {
        return scmBranch;
    }

    public void setScmBranch(String scmBranch) {
        this.scmBranch = scmBranch;
    }
}
