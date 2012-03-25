package org.whitesource.agent.api.dispatch;

import java.util.ArrayList;
import java.util.Collection;

import org.whitesource.agent.api.model.DependencyInfo;


/**
 * Request for license analysis.
 * 
 * @author tom.shapira
 */
public class ReportRequest extends BaseRequest<ReportResult> {

	/* --- Static members --- */
	
	private static final long serialVersionUID = 909591223876500674L;
	
	/* --- Members --- */
	
	private Collection<DependencyInfo> dependencies;
	
	/* --- Constructors --- */
	
	/**
	 * Default constructor
	 */
	public ReportRequest() {
		this(new ArrayList<DependencyInfo>());
	}
	
	/**
	 * Constructor
	 */
	public ReportRequest(Collection<DependencyInfo> dependencies) {
		super(RequestType.REPORT, null);
		
		this.dependencies = dependencies;
	}
	
	/* --- Getters / Setters --- */
	
	public Collection<DependencyInfo> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Collection<DependencyInfo> dependencies) {
		this.dependencies = dependencies;
	}
	
}