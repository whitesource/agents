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

import org.whitesource.agent.api.model.tree.BaseNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to hold all information about a project to update. 
 *
 * @author tom.shapira
 */
public class AgentProjectInfo implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -7847114716505720514L;

    /* --- Members --- */

    private Coordinates coordinates;

    private Coordinates parentCoordinates;

    private Collection<DependencyInfo> dependencies;

    /**
     * In case the hierarchy of the dependencies collection is flattened,
     * this collection will hold the original dependency tree structure.
     */
    private Collection<BaseNode> dependencyTree;

    private String projectToken;

    private ProjectSetupStatus projectSetupStatus;

    private String projectSetupDescription;

    /* --- Constructors --- */

    /**
     * Default constructor
     *
     */
    public AgentProjectInfo() {
        dependencies = new ArrayList<>();
        dependencyTree = new LinkedList<>();
    }

    /* --- Overridden methods --- */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("AgentProjectInfo@").append(Integer.toHexString(hashCode()))
                .append("[")
                .append("coordinates= ").append(coordinates).append(",")
                .append("parentCoordinates= ").append(parentCoordinates).append(",")
                .append("projectToken= ").append(projectToken)
                .append(" ]");

        return sb.toString();
    }

    /* --- Getters / Setters --- */

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getParentCoordinates() {
        return parentCoordinates;
    }

    public void setParentCoordinates(Coordinates parentCoordinates) {
        this.parentCoordinates = parentCoordinates;
    }

    public Collection<DependencyInfo> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<DependencyInfo> dependencies) {
        this.dependencies = dependencies;
    }

    public String getProjectToken() {
        return projectToken;
    }

    public void setProjectToken(String projectToken) {
        this.projectToken = projectToken;
    }

    public String getProjectSetupDescription() {
        return projectSetupDescription;
    }

    public void setProjectSetupDescription(String projectSetupDescription) {
        this.projectSetupDescription = projectSetupDescription;
    }

    public ProjectSetupStatus getProjectSetupStatus() {
        return projectSetupStatus;
    }

    public void setProjectSetupStatus(ProjectSetupStatus projectSetupStatus) {
        this.projectSetupStatus = projectSetupStatus;
    }

    public Collection<BaseNode> getDependencyTree() {
        return dependencyTree;
    }

    public void setDependencyTree(Collection<BaseNode> dependencyTree) {
        this.dependencyTree = dependencyTree;
    }
}
