package org.whitesource.agent.api.model;

import org.apache.commons.lang.StringUtils;
import org.whitesource.agent.api.RegexUtils;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Represents a rule by which in-house libraries will be matched.
 *
 * @author tom.shapira
 * @since 1.3.0
 */
public class InHouseRule implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -2240187933063850973L;

    /* --- Members --- */

    private String groupIdRegex;

    private String artifactIdRegex;

    private String nameRegex;

    private transient Pattern groupIdPattern;
    private transient Pattern artifactIdPattern;
    private transient Pattern namePattern;

    /* --- Constructors --- */

    /**
     * Default constructor
     */
    public InHouseRule() {

    }

    /**
     * Constructor
     */
    public InHouseRule(String groupIdRegex, String artifactIdRegex) {
        this();
        this.groupIdRegex = groupIdRegex;
        this.artifactIdRegex = artifactIdRegex;
    }

    /**
     * Constructor
     */
    public InHouseRule(String nameRegex) {
        this.nameRegex = nameRegex;
    }

    /* --- Overridden methods --- */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(nameRegex)) {
            sb.append("name: ");
            sb.append(nameRegex);
        } else {
            if (StringUtils.isNotBlank(groupIdRegex)) {
                sb.append("g: ");
                sb.append(groupIdRegex);
                sb.append(" ");
            }

            if (StringUtils.isNotBlank(artifactIdRegex)) {
                sb.append("a: ");
                sb.append(artifactIdRegex);
            }
        }
        return sb.toString();
    }

    /* --- Public methods --- */

    public boolean match(String groupId, String artifactId) {
        recompile();
        if (groupIdPattern == null && artifactIdPattern == null) {
            return false;
        }

        boolean matches = true;
        if (groupIdPattern != null) {
            matches = matchPattern(groupIdPattern, groupId);
        }
        if (artifactIdPattern != null) {
            matches = matches && matchPattern(artifactIdPattern, artifactId);
        }

        return matches;
    }

    public boolean match (String name) {
        recompile();
        return namePattern != null && matchPattern(namePattern, name);
    }

    public void recompile() {
        if (StringUtils.isNotBlank(groupIdRegex)) {
            groupIdPattern = Pattern.compile(RegexUtils.toJava(groupIdRegex));
        }
        if (StringUtils.isNotBlank(artifactIdRegex)) {
            artifactIdPattern = Pattern.compile(RegexUtils.toJava(artifactIdRegex));
        }
        if (StringUtils.isNotBlank(nameRegex)) {
            namePattern = Pattern.compile(RegexUtils.toJava(nameRegex));
        }
    }

	/* --- Private methods --- */

    private boolean matchPattern(Pattern pattern, CharSequence value) {
        return value != null && pattern.matcher(value).matches();
    }

	/* --- Getters / Setters --- */

    public String getGroupIdRegex() {
        return groupIdRegex;
    }

    public void setGroupIdRegex(String regex) {
        groupIdRegex = regex;
        if (StringUtils.isBlank(regex)) {
            groupIdPattern = null;
        } else {
            groupIdPattern = Pattern.compile(RegexUtils.toJava(regex));
        }
    }

    public String getArtifactIdRegex() {
        return artifactIdRegex;
    }

    public void setArtifactIdRegex(String regex) {
        artifactIdRegex = regex;
        if (StringUtils.isBlank(regex)) {
            artifactIdPattern = null;
        } else {
            artifactIdPattern = Pattern.compile(RegexUtils.toJava(regex));
        }
    }

    public String getNameRegex() {
        return nameRegex;
    }

    public void setNameRegex(String regex) {
        this.nameRegex = regex;
        if (StringUtils.isBlank(regex)) {
            namePattern = null;
        } else {
            namePattern = Pattern.compile(RegexUtils.toJava(regex));
        }
    }

}
