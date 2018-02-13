package org.whitesource.agent.via.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Holds all vulnerability traces which address specific element
 *
 * @author artiom.petrov
 */
public class VulnerableElement implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -4764927498260473239L;

    /* --- Members --- */

    // class or method
    private String element;
    private Collection<VulnerabilityTrace> vulnerabilityTraces;

    /* --- Constructor --- */

    public VulnerableElement() {
        vulnerabilityTraces = new LinkedList<>();
    }

    public VulnerableElement(String element, Collection<VulnerabilityTrace> vulnerabilityTraces) {
        this.element = element;
        this.vulnerabilityTraces = vulnerabilityTraces;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VulnerableElement)) return false;
        VulnerableElement that = (VulnerableElement) o;
        return Objects.equals(element, that.element) &&
                Objects.equals(vulnerabilityTraces, that.vulnerabilityTraces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, vulnerabilityTraces);
    }

    /* --- Getters / Setters --- */

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Collection<VulnerabilityTrace> getVulnerabilityTraces() {
        return vulnerabilityTraces;
    }

    public void setVulnerabilityTraces(Collection<VulnerabilityTrace> vulnerabilityTraces) {
        this.vulnerabilityTraces = vulnerabilityTraces;
    }
}
