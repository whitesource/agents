package org.whitesource.agent.api.model.tree;

import java.io.Serializable;
import java.util.Objects;

/**
 * A library is an entity that represents dependency and contains other children dependencies and sha1
 *
 * @author chen.luigi
 */
public class LibraryNode extends BaseNode implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 7766430180883318538L;

    /* --- Private Members --- */

    private String sha1;

    /* --- Constructors --- */

    public LibraryNode() {
        super();
    }

    public LibraryNode(String groupId, String artifactId, String version, NodeType nodeType, String sha1) {
        super(groupId, artifactId, version, nodeType);
        this.sha1 = sha1;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LibraryNode)) return false;
        LibraryNode that = (LibraryNode) o;
        return Objects.equals(sha1, that.sha1);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sha1);
    }

    @Override
    public String toString() {
        return "LibraryNode{" +
                super.toString() +
                "sha1='" + sha1 + '\'' +
                '}';
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.DEPENDENCY;
    }

    /* --- Getters / Setters --- */

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }
}
