package com.wss.agent.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
	
	private String projectToken;
	
	/* --- Constructors --- */
	
	/**
	 * Default constructor
	 * 
	 */
	public AgentProjectInfo() {
		dependencies = new ArrayList<DependencyInfo>();
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
}
