package org.whitesource.maven;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.whitesource.agent.api.ChecksumUtils;
import org.whitesource.agent.api.dispatch.UpdateInventoryResult;
import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.Coordinates;
import org.whitesource.agent.api.model.DependencyInfo;
import org.whitesource.agent.api.model.ExclusionInfo;
import org.whitesource.api.client.WssServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Send updates of open source software usage information to White Source.
 *
 * @author Edo.Shor
 */
@Mojo(name = "update",
        defaultPhase = LifecyclePhase.PACKAGE,
        requiresDependencyResolution = ResolutionScope.TEST,
        aggregator = true
)
public class UpdateMojo extends WhitesourceMojo {

    /**
     * Unique identifier of the organization to update.
     */
    @Parameter(alias = "orgToken",
            property = Constants.ORG_TOKEN,
            required = true)
    private String orgToken;

    /**
     * Optional. Unique identifier of the mavenProject to update.
     * If omitted, default naming convention will apply to match White Source mavenProject.
     */
    @Parameter(alias = "projectToken",
            property = Constants.PROJECT_TOKEN,
            required = false)
    private String projectToken;

    /**
     * Optional. Map of module artifactId to White Source project token.
     */
    @Parameter(alias = "moduleTokens",
            property = Constants.MODULE_TOKENS,
            required = false)
    private Map<String, String> moduleTokens = new HashMap<String, String>();

    /**
     * Optional. Set to true to ignore this maven mavenProject. Overrides any include patterns.
     */
    @Parameter(alias = "ignore",
            property = Constants.IGNORE,
            required = false,
            defaultValue = "false")
    private boolean ignore;

    /**
     * Optional. Only modules with an artifactId matching any of these patterns will be processed by the plugin.
     */
    @Parameter(alias = "includes",
            property = Constants.INCLUDES,
            required = false,
            defaultValue = "")
    private String[] includes;

    /**
     * Optional. Modules with an artifactId matching any of these patterns will not be processed by the plugin.
     */
    @Parameter(alias = "excludes",
            property = Constants.EXCLUDES,
            required = false,
            defaultValue = "")
    private String[] excludes;

    /**
     * Optional. Set to true to ignore this maven mavenProject.
     */
    @Parameter(alias = "ignorePomModules",
            property = Constants.IGNORE_POM_MODULES,
            required = false,
            defaultValue = "true")
    private boolean ignorePomModules;

    @Parameter(defaultValue = "${reactorProjects}",
            required = true,
            readonly = true)
    private Collection<MavenProject> reactorProjects;

    /* --- Concrete implementation methods --- */

    @Override
    public void doExecute() throws MojoExecutionException, MojoFailureException {
        // Collect OSS usage information
        Collection<AgentProjectInfo> projectInfos = new ArrayList<AgentProjectInfo>();
        for (MavenProject project : reactorProjects) {
            if (shouldProcess(project)) {
                projectInfos.add(processProject(project));
            } else {
                info("skipping " + project.getId());
            }
        }
        debugProjectInfos(projectInfos);

        // send to white source
        try {
            final UpdateInventoryResult result = service.update(orgToken, projectInfos);
            logResult(result);
        } catch (WssServiceException e) {
            throw new MojoExecutionException(Constants.ERROR_SERVICE_CONNECTION + e.getMessage(), e);
        }
    }

    private boolean shouldProcess(MavenProject project) {
        boolean process = true;

        if (ignorePomModules && project.getPackaging().equals("pom")) {
            process = false;
        } else if (project.equals(mavenProject)) {
            process = !ignore;
        } else if (excludes.length > 0 && matchAny(project.getArtifactId(), excludes)) {
            process = false;
        } else if (includes.length > 0 && matchAny(project.getArtifactId(), includes)) {
            process = true;
        }

        return process;
    }

    private boolean matchAny(String value, String[] patterns) {
        boolean match = false;

        for (String pattern : patterns) {
            String regex = pattern.replace(".", "\\.").replace("*", ".*");
            if (value.matches(regex)) {
                break;
            }
        }

        return match;
    }

    private AgentProjectInfo processProject(MavenProject project) {
        long startTime = System.currentTimeMillis();

        info("processing " + project.getId());

        AgentProjectInfo projectInfo = new AgentProjectInfo();

        // project token
        if (StringUtils.isBlank(projectToken)) {
            projectInfo.setProjectToken(moduleTokens.get(project.getArtifactId()));
        } else {
            projectInfo.setProjectToken(projectToken);
        }

        // project coordinates
        projectInfo.setCoordinates(extractCoordinates(project));

        // parent coordinates
        if (project.hasParent()) {
            projectInfo.setParentCoordinates(extractCoordinates(project.getParent()));
        }

        // dependencies
        Map<Dependency, Artifact> lut = createLookupTable(project);
        for (Dependency dependency : project.getDependencies()) {
            DependencyInfo dependencyInfo = getDependencyInfo(dependency);

            Artifact artifact = lut.get(dependency);
            if (artifact != null) {
                if (artifact.getFile().exists()) {
                    try {
                        dependencyInfo.setSha1(ChecksumUtils.calculateSHA1(artifact.getFile()));
                    } catch (IOException e) {
                        debug(Constants.ERROR_SHA1 + " for " + artifact.getId());
                    }
                }
            }

            projectInfo.getDependencies().add(dependencyInfo);
        }

        info("Total processing time is " + (System.currentTimeMillis() - startTime) + " [msec]");

        return projectInfo;
    }

    private Coordinates extractCoordinates(MavenProject mavenProject) {
        return new Coordinates(mavenProject.getGroupId(),
                mavenProject.getArtifactId(),
                mavenProject.getVersion());
    }

    private Map<Dependency, Artifact> createLookupTable(MavenProject project) {
        Map<Dependency, Artifact> lut = new HashMap<Dependency, Artifact>();

        for (Dependency dependency : project.getDependencies()) {
            for (Artifact dependencyArtifact : project.getDependencyArtifacts()) {
                if (match(dependency, dependencyArtifact)) {
                    lut.put(dependency, dependencyArtifact);
                }
            }
        }

        return lut;
    }

    private boolean match(Dependency dependency, Artifact artifact) {
        boolean match = dependency.getGroupId().equals(artifact.getGroupId()) &&
                dependency.getArtifactId().equals(artifact.getArtifactId()) &&
                dependency.getVersion().equals(artifact.getVersion());

        if (match) {
            if (dependency.getClassifier() == null) {
                match = artifact.getClassifier() == null;
            } else {
                match = dependency.getClassifier().equals(artifact.getClassifier());
            }
        }

        if (match) {
            String type = artifact.getType();
            if (dependency.getType() == null) {
                match = type == null || type.equals("jar");
            } else {
                match = dependency.getType().equals(type);
            }
        }

        return match;
    }

    private DependencyInfo getDependencyInfo(Dependency dependency) {
        DependencyInfo info = new DependencyInfo();

        // dependency data
        info.setGroupId(dependency.getGroupId());
        info.setArtifactId(dependency.getArtifactId());
        info.setVersion(dependency.getVersion());
        info.setScope(dependency.getScope());
        info.setClassifier(dependency.getClassifier());
        info.setOptional(dependency.isOptional());
        info.setType(dependency.getType());
        info.setSystemPath(dependency.getSystemPath());

        // exclusions
        Collection<ExclusionInfo> exclusions = info.getExclusions();
        for (Exclusion exclusion : dependency.getExclusions()) {
            exclusions.add(new ExclusionInfo(exclusion.getGroupId(), exclusion.getArtifactId()));
        }

        return info;
    }

    private void logResult(UpdateInventoryResult result) {
        info("Inventory update results for " + result.getOrganization());

        // newly created projects
        Collection<String> createdProjects = result.getCreatedProjects();
        if (createdProjects.isEmpty()) {
            info("No new projects found.");
        } else {
            info("Newly created projects:");
            for (String projectName : createdProjects) {
                info(projectName);
            }
        }

        // updated projects
        Collection<String> updatedProjects = result.getUpdatedProjects();
        if (updatedProjects.isEmpty()) {
            info("No projects were updated.");
        } else {
            info("Updated projects:");
            for (String projectName : updatedProjects) {
                info(projectName);
            }
        }
    }

    public void debugProjectInfos(Collection<AgentProjectInfo> projectInfos) {
        debug("----------------- dumping projectInfos -----------------");
        debug("Total number of projects : " + projectInfos.size());

        for (AgentProjectInfo projectInfo : projectInfos) {
            debug("Project coordiantes: " + projectInfo.getCoordinates().toString());
            debug("Project parent coordiantes: " + projectInfo.getParentCoordinates().toString());
            debug("Project token: " + projectInfo.getProjectToken());
            debug("total # of dependencies: " + projectInfo.getDependencies().size());
            for (DependencyInfo info : projectInfo.getDependencies()) {
                debug(info.toString() + " SHA-1: " + info.getSha1());
            }
        }

        debug("----------------- dump finished -----------------");
    }

}
