/**
 * Copyright (C) 2012 White Source Ltd.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.agent.api.model;

import org.apache.commons.lang.StringUtils;
import org.whitesource.agent.api.APIConstants;
import org.whitesource.agent.via.api.VulnerabilityAnalysisResult;

import java.io.Serializable;
import java.util.*;

/**
 * WhiteSource Model for a project's dependency
 *
 * @author tom.shapira
 */
public class DependencyInfo implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -6212622409560068635L;

    /* --- Members --- */

    private String groupId;
    private String artifactId;
    private String version;
    private String type;
    private String classifier;
    private String scope;
    private String sha1;
    private String headerSha1;
    private String footerSha1;
    private String utf8Sha1;
    private String fullHash;
    private String mostSigBitsHash;
    private String leastSigBitsHash;
    private String commentlessSha1;
    private String noNewLinesSha1;
    private String otherPlatformSha1;
    private String systemPath;
    private boolean optional;
    private Collection<DependencyInfo> children;
    private Date lastModified;
    private String filename;
    private DependencyType dependencyType;
    private DependencyHintsInfo hints;
    private Map<ChecksumType, String> checksums;
    private VulnerabilityAnalysisResult vulnerabilityAnalysisResult;
    private String commit;
    private String dependencyFile;
    private String additionalSha1;
    private String architecture;
    private String languageVersion;
    private boolean deduped;
    private OSInfo osInfo;
    private AnalysisInputs analysisInputs;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public DependencyInfo() {
    }

    /**
     * Constructor
     *
     * @param groupId    - the dependency groupId
     * @param artifactId - the dependency artifactId
     * @param version    - the dependency version
     */
    public DependencyInfo(String groupId, String artifactId, String version) {
        this();
        this.groupId = groupId;
        this.version = version;
        this.artifactId = artifactId;
    }

    /**
     * Constructor
     *
     * @param sha1 - the dependency sha1
     */
    public DependencyInfo(String sha1) {
        this();
        setSha1(sha1);
    }

    /**
     * Constructor
     *
     * @param sha1     - the dependency sha1
     * @param fullHash - the dependency fullHash
     */
    public DependencyInfo(String sha1, String fullHash) {
        this(sha1);
        setFullHash(fullHash);
    }

    /**
     * Constructor
     *
     * @param sha1             - the dependency sha1
     * @param fullHash         - the dependency fullHash
     * @param mostSigBitsHash  - the dependency mostSigBitsHash
     * @param leastSigBitsHash - the dependency leastSigBitsHash
     */
    public DependencyInfo(String sha1, String fullHash, String mostSigBitsHash, String leastSigBitsHash) {
        this(sha1, fullHash);
        setMostSigBitsHash(mostSigBitsHash);
        setLeastSigBitsHash(leastSigBitsHash);
    }

    /* --- Overridden methods --- */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("DependencyInfo@").append(Integer.toHexString(hashCode()))
                .append("[")
                .append("groupId= ").append(groupId).append(",")
                .append("artifactId= ").append(artifactId).append(",")
                .append("version= ").append(version).append(",")
                .append("filename= ").append(filename).append(",")
                .append("dependencyType= ").append(dependencyType)
                .append(" ]");

        return sb.toString();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DependencyInfo)) return false;

        DependencyInfo that = (DependencyInfo) o;

        if (StringUtils.isNotEmpty(sha1)) {
            return sha1.equals(that.sha1);
        } else if (StringUtils.isNotEmpty(that.sha1)) {
            return false;
        }
        if (optional != that.optional) return false;
        if (artifactId != null ? !artifactId.equals(that.artifactId) : that.artifactId != null) return false;
        if (classifier != null ? !classifier.equals(that.classifier) : that.classifier != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (scope != null ? !scope.equals(that.scope) : that.scope != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (filename != null ? !filename.equals(that.filename) : that.filename != null) return false;
        if (commit != null ? !commit.equals(that.commit) : that.commit != null) return false;
        if (dependencyType != null ? !dependencyType.equals(that.dependencyType) : that.dependencyType != null)
            return false;
        if (systemPath != null ? !systemPath.equals(that.systemPath) : that.systemPath != null) return false;
        if (dependencyFile != null ? !dependencyFile.equals(that.dependencyFile) : that.dependencyFile != null)
            return false;
        if (vulnerabilityAnalysisResult != null ?
                !vulnerabilityAnalysisResult.equals(that.vulnerabilityAnalysisResult) :
                that.vulnerabilityAnalysisResult != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = APIConstants.HASH_CODE_SEED;
        result = APIConstants.HASH_CODE_FACTOR * result + (groupId != null ? groupId.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (artifactId != null ? artifactId.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (version != null ? version.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (type != null ? type.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (classifier != null ? classifier.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (scope != null ? scope.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (sha1 != null ? sha1.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (optional ? 1 : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (filename != null ? filename.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (dependencyType != null ? dependencyType.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (systemPath != null ? systemPath.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (dependencyFile != null ? dependencyFile.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (vulnerabilityAnalysisResult != null ? vulnerabilityAnalysisResult.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (commit != null ? commit.hashCode() : 0);
        result = APIConstants.HASH_CODE_FACTOR * result + (additionalSha1 != null ? additionalSha1.hashCode() : 0);
        return result;
    }

    /* --- Public methods --- */


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
        addChecksum(ChecksumType.SHA1, sha1);
    }

    public String getSystemPath() {
        return systemPath;
    }

    public void setSystemPath(String systemPath) {
        this.systemPath = systemPath;
    }

    public boolean getOptional() {
        return optional;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public Collection<DependencyInfo> getChildren() {
        if (children == null) {
            children = new LinkedList<>();
        }
        return children;
    }

    public void setChildren(Collection<DependencyInfo> children) {
        this.children = children;
    }

    public void addChild(DependencyInfo child) {
        if (children == null) {
            children = new LinkedList<>();
        }
        children.add(child);
    }

    public boolean hasChildren() {
        return children != null && children.size() != 0;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getHeaderSha1() {
        return headerSha1;
    }

    public void setHeaderSha1(String headerSha1) {
        this.headerSha1 = headerSha1;
    }

    public String getFooterSha1() {
        return footerSha1;
    }

    public void setFooterSha1(String footerSha1) {
        this.footerSha1 = footerSha1;
    }

    public String getUtf8Sha1() {
        return utf8Sha1;
    }

    public void setUtf8Sha1(String utf8Sha1) {
        this.utf8Sha1 = utf8Sha1;
        addChecksum(ChecksumType.SHA1_UTF8, utf8Sha1);
    }

    public String getCommentlessSha1() {
        return commentlessSha1;
    }

    public void setCommentlessSha1(String commentlessSha1) {
        this.commentlessSha1 = commentlessSha1;
        addChecksum(ChecksumType.SHA1_NO_COMMENTS, commentlessSha1);
    }

    public String getNoNewLinesSha1() {
        return noNewLinesSha1;
    }

    public void setNoNewLinesSha1(String noNewLinesSha1) {
        this.noNewLinesSha1 = noNewLinesSha1;
    }

    public String getOtherPlatformSha1() {
        return otherPlatformSha1;
    }

    public void setOtherPlatformSha1(String otherPlatformSha1) {
        this.otherPlatformSha1 = otherPlatformSha1;
        addChecksum(ChecksumType.SHA1_OTHER_PLATFORM, otherPlatformSha1);
    }

    public String getFullHash() {
        return fullHash;
    }

    public void setFullHash(String fullHash) {
        this.fullHash = fullHash;
        addChecksum(ChecksumType.SHA1_SUPER_HASH, fullHash);
    }

    public String getMostSigBitsHash() {
        return mostSigBitsHash;
    }

    public void setMostSigBitsHash(String mostSigBitsHash) {
        this.mostSigBitsHash = mostSigBitsHash;
        addChecksum(ChecksumType.SHA1_SUPER_HASH_MSB, mostSigBitsHash);
    }

    public String getLeastSigBitsHash() {
        return leastSigBitsHash;
    }

    public void setLeastSigBitsHash(String leastSigBitsHash) {
        this.leastSigBitsHash = leastSigBitsHash;
        addChecksum(ChecksumType.SHA1_SUPER_HASH_LSB, leastSigBitsHash);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public DependencyType getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    public DependencyHintsInfo getHints() {
        return hints;
    }

    public void setHints(DependencyHintsInfo hints) {
        this.hints = hints;
    }

    public Map<ChecksumType, String> getChecksums() {
        if (checksums == null) {
            checksums = new TreeMap<>();
        }
        return checksums;
    }

    public void setChecksums(Map<ChecksumType, String> checksums) {
        this.checksums = checksums;
    }

    public void addChecksum(ChecksumType checksumType, String checksum) {
        if (StringUtils.isNotBlank(checksum)) {
            if (checksums == null) {
                checksums = new TreeMap<>();
            }
            checksums.put(checksumType, checksum);
        }
    }

    public boolean hasChecksum() {
        return checksums != null && checksums.size() != 0;
    }

    public VulnerabilityAnalysisResult getVulnerabilityAnalysisResult() {
        return vulnerabilityAnalysisResult;
    }

    public void setVulnerabilityAnalysisResult(VulnerabilityAnalysisResult vulnerabilityAnalysisResult) {
        this.vulnerabilityAnalysisResult = vulnerabilityAnalysisResult;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getDependencyFile() {
        return dependencyFile;
    }

    public void setDependencyFile(String dependencyFile) {
        this.dependencyFile = dependencyFile;
    }

    public String getAdditionalSha1() {
        return additionalSha1;
    }

    public void setAdditionalSha1(String additionalSha1) {
        this.additionalSha1 = additionalSha1;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getLanguageVersion() {
        return languageVersion;
    }

    public void setLanguageVersion(String languageVersion) {
        this.languageVersion = languageVersion;
    }

    public boolean isDeduped() {
        return deduped;
    }

    public void setDeduped(boolean deduped) {
        this.deduped = deduped;
    }

    public OSInfo getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(OSInfo osInfo) {
        this.osInfo = osInfo;
    }

    public AnalysisInputs getAnalysisInputs() { return analysisInputs; }

    public void setAnalysisInputs(AnalysisInputs analysisInputs) {
        this.analysisInputs = analysisInputs;
    }

    /* --- Utility methods --- */

    /**
     * This method initializes the analysis inputs only if it's null.
     * Otherwise, nothing will happen.
     *
     * The artifactId is copied to the analysisInputs object.
     */
    public void initAnalysisInputs() {
        if (analysisInputs == null) {
            analysisInputs = new AnalysisInputs(artifactId);
        }
    }

    /**
     * This method initializes the analysisInputs only if it's null.
     * Otherwise, nothing will happen.
     *
     * @param euaArtifactId to be set inside the analysisInputs object
     */
    public void initAnalysisInputs(String euaArtifactId) {
        if (analysisInputs == null) {
            analysisInputs = new AnalysisInputs(euaArtifactId);
        }
    }

    /**
     * This method was created in WSE-5514 task which will help decrease the size of the update-request file,
     * by removing all the empty initialized objects.
     * This method runs recursively on a dependencyInfo object and its children and reset all the initialized object
     * which contains no elements (empty collections)
     * In addition the analysisInputs object is set to null, as it is not needed in the update-request.
     */
    public void resetUnusedFields() {
        analysisInputs = null;

        if (checksums != null && checksums.size() == 0) {
            checksums = null;
        }

        if (children != null && children.size() == 0) {
            children = null;
        }

        // recursive call
        if (children != null) {
            for (DependencyInfo child : children) {
                child.resetUnusedFields();
            }
        }
    }
}
