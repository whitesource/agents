package org.whitesource.agent.via.api;

import java.io.Serializable;
import java.util.Objects;

/**
 * Responsible for sorted order of the code invocations, linked to it's corresponding method names
 *
 * @author artiom.petrov
 */
public class CodeInvocation implements Serializable {

    /* --- Static members --- */

    private static final long serialVersionUID = -5248494825933425058L;

    /* --- Members --- */

    private InvocationType invocationType;
    private String methodName;
    private int order;

    /* --- Constructor --- */

    public CodeInvocation() {
    }

    public CodeInvocation(InvocationType invocationType, String methodName, int order) {
        this.invocationType = invocationType;
        this.methodName = methodName;
        this.order = order;
    }

    /* --- Overridden methods --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeInvocation)) return false;
        CodeInvocation that = (CodeInvocation) o;
        return order == that.order &&
                invocationType == that.invocationType &&
                Objects.equals(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invocationType, methodName, order);
    }

    /* --- Getters / Setters --- */

    public InvocationType getInvocationType() {
        return invocationType;
    }

    public void setInvocationType(InvocationType invocationType) {
        this.invocationType = invocationType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
