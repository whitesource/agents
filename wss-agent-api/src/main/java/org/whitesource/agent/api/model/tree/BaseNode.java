package org.whitesource.agent.api.model.tree;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Base node entity represents the hierarchy tree of dependencies
 *
 * @author chen.luigi
 */
public abstract class BaseNode implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -7313089660886237684L;

    /* --- Private Members --- */

    private NodeType nodeType;

    private String groupId;
    private String artifactId;
    private String version;
    private String type;
    private String classifier;

    private Collection<BaseNode> children;

    /* --- Constructors --- */

    public BaseNode() {
        children = new LinkedList<>();
    }

    public BaseNode(String groupId, String artifactId, String version, NodeType nodeType) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.nodeType = nodeType;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseNode)) return false;
        BaseNode that = (BaseNode) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(artifactId, that.artifactId) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version);
    }

    @Override
    public String toString() {
        return "BaseNode{" +
                "nodeType=" + nodeType +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", type='" + type + '\'' +
                ", classifier='" + classifier + '\'' +
                '}';
    }

    public abstract NodeType getNodeType();

    /* --- Getters / Setters --- */

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

    public Collection<BaseNode> getChildren() {
        return children;
    }

    public void setChildren(Collection<BaseNode> children) {
        this.children = children;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
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
}
