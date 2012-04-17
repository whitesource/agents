package org.whitesource.agent.api.dispatch;

import java.util.Collection;

import org.whitesource.agent.api.model.DependencyInfo;

/**
 * Request for WhiteSource anonymous report.
 * 
 * @author tom.shapira
 */
public class ReportRequest extends BaseRequest<ReportResult> {

	/* --- Static members --- */
	
	private static final long serialVersionUID = 909591223876500674L;
	
	/* --- Members --- */
	
	protected Collection<DependencyInfo> dependencies;
	
	/* --- Constructors --- */
	
	/**
	 * Constructor
	 * 
	 * @param dependencies Dependencies for analysis.
	 */
	public ReportRequest(Collection<DependencyInfo> dependencies) {
		super(RequestType.REPORT);
		
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