package com.wss.agent.request;

import java.text.SimpleDateFormat;
import java.util.Collection;

import com.wss.agent.model.AgentProjectInfo;
import com.wss.agent.model.Coordinates;
import com.wss.agent.model.DependencyInfo;
import com.wss.agent.model.ExclusionInfo;

/**
 * Represents the request sent to the server from the agent after converted to JSON 
 * 
 * @author tom.shapira
 *
 */
public class UpdateInventoryRequest {

	/* --- Static members --- */
	
	private static final String LINE_SEPARATOR = "******************************************\n";

	private static final String NEW_LINE = "\n";
	
	/* --- Members --- */

	private long timeStamp;

	private String token;

	private Collection<AgentProjectInfo> projects;

	/* --- Constructors --- */

	/**
	 * Default constructor
	 * 
	 */
	public UpdateInventoryRequest(String token, Collection<AgentProjectInfo> projects) {
		this.token = token;
		this.projects = projects;
		
		timeStamp = System.currentTimeMillis();
	}

	/* --- Overridden methods --- */

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(LINE_SEPARATOR);
		sb.append("************* Agent Request **************" + NEW_LINE);
		sb.append(LINE_SEPARATOR);
		
		sb.append("***************** Token ******************" + NEW_LINE);
		sb.append(token + NEW_LINE);
		
		sb.append("*************** Time stamp ***************" + NEW_LINE);
		SimpleDateFormat sdf = new SimpleDateFormat();
		sb.append(sdf.format(timeStamp) + NEW_LINE);
		
		sb.append("**************** Projects ****************" + NEW_LINE);
		for (AgentProjectInfo project : projects) {
			sb.append("Project coordinates" + NEW_LINE);
			Coordinates coordinates = project.getCoordinates();
			sb.append(coordinates.toString() + NEW_LINE + NEW_LINE);

			Coordinates parentCoordinates = project.getParentCoordinates();
			if (parentCoordinates == null) {
				sb.append("Has no parent" + NEW_LINE);
			} else {
				sb.append("Parent coordinates" + NEW_LINE);
				sb.append(parentCoordinates.toString() + NEW_LINE + NEW_LINE);
			}
			
			int length = 0;
			Collection<DependencyInfo> dependencies = project.getDependencies();
			if (dependencies.isEmpty()) {
				sb.append("No dependencies" + NEW_LINE);
			} else {
				sb.append("Dependencies" + NEW_LINE);
				sb.append("{");
				for (DependencyInfo dependencyInfo : dependencies) {
					sb.append(dependencyInfo.toString());
					
					Collection<ExclusionInfo> exclusions = dependencyInfo.getExclusions();
					if (!exclusions.isEmpty()) {
						sb.append(" (exclusions {");
						
						for (ExclusionInfo exclusionInfo : exclusions) {
							sb.append(exclusionInfo.toString() + ", ");
						}
						length = sb.length();
						sb.replace(length - 2, length, "})");
					}
					sb.append(", ");
				}
				length = sb.length();
				sb.replace(length - 2, length, "}");
				sb.append(NEW_LINE);
			}
			sb.append(LINE_SEPARATOR);
		}
		return sb.toString();
	}

	/* --- Getters / Setters --- */

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Collection<AgentProjectInfo> getProjects() {
		return projects;
	}

	public void setProjects(Collection<AgentProjectInfo> projects) {
		this.projects = projects;
	}

}
