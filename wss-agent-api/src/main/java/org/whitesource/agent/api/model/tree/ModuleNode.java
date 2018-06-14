package org.whitesource.agent.api.model.tree;

import java.io.Serializable;

/**
 * A module is an entity that represents a sub project (like in maven) and contains dependencies (libraries)
 * in a tree format.
 *
 * @author chen.luigi
 */
public class ModuleNode extends BaseNode implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = 974730010611686947L;

    /* --- Constructors --- */

    public ModuleNode() {
        super();
    }

    public ModuleNode(String groupId, String artifactId, String version, NodeType nodeType) {
        super(groupId, artifactId, version, nodeType);
    }

    /* --- Overridden methods --- */

    @Override
    public NodeType getNodeType() {
        return NodeType.MODULE;
    }
}