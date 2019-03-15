/**
 * Copyright (C) 2012 White Source Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.whitesource.agent.report;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.whitesource.agent.api.dispatch.BaseCheckPoliciesResult;
import org.whitesource.agent.api.model.PolicyCheckResourceNode;
import org.whitesource.agent.api.model.RequestPolicyInfo;
import org.whitesource.agent.api.model.ResourceInfo;
import org.whitesource.agent.report.summary.PolicyRejectionReport;
import org.whitesource.agent.report.summary.RejectingPolicy;
import org.whitesource.agent.report.summary.RejectedLibrary;
import org.whitesource.agent.report.model.LicenseHistogramDataPoint;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * A report generator for policy check results.
 *
 * @author Edo.Shor
 */
public class PolicyCheckReport {

    /* --- Static members --- */

    private static final String TEMPLATE_FOLDER = "templates/";
    private static final String TEMPLATE_FILE = "policy-check.vm";

    private static final float MAX_BAR_HEIGHT = 50;
    private static final int LICENSE_LIMIT = 6;
    private static final String OTHER_LICENSE = "Other types";

    public static final String REJECT = "Reject";
    public static final String CHECK_POLICIES_JSON_FILE = "checkPolicies-json.txt";
    public static final String POLICY_REJECTION_SUMMARY_FILE = "policyRejectionSummary.json";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /* --- Members --- */

    private BaseCheckPoliciesResult result;
    private String buildName;
    private String buildNumber;

    /* --- Constructors --- */

    /**
     * Constructor
     *
     * @param result
     */
    public PolicyCheckReport(BaseCheckPoliciesResult result) {
        this.result = result;
    }

    /**
     * Constructor
     *
     * @param result report
     * @param buildName of the report
     * @param buildNumber of the report
     */
    public PolicyCheckReport(BaseCheckPoliciesResult result, String buildName, String buildNumber) {
        this(result);
        this.buildName = buildName;
        this.buildNumber = buildNumber;
    }

    /* --- Public methods --- */

    /**
     * The method generate the policy check report
     *
     * @param outputDir Directory where report files will be created.
     * @param pack      <code>True</code> to create a zip file from the resulting directory.
     * @return  File reference to the resulting report.
     * @throws IOException In case of errors during report generation process.
     */
    public File generate(File outputDir, boolean pack) throws IOException {
        return generate(outputDir, pack, null);
    }

    /**
     * The method generate the policy check report
     *
     * @param outputDir Directory where report files will be created.
     * @param pack      <code>True</code> to create a zip file from the resulting directory.
     * @param properties Properties for template engine.
     *
     * @return  File reference to the resulting report.
     *
     * @throws IOException In case of errors during report generation process.
     */
    public File generate(File outputDir, boolean pack, Properties properties) throws IOException {
        if (result == null) {
            throw new IllegalStateException("Check policies result is null");
        }

        // prepare working directory
        File workDir = new File(outputDir, "whitesource");
        File reportFile = workDir;
        if (!workDir.exists() && !workDir.mkdir()) {
            throw new IOException("Unable to make output directory: " + workDir);
        }

        // create actual report
        VelocityEngine engine = createTemplateEngine(properties);
        VelocityContext context = createTemplateContext();
        FileWriter fw = new FileWriter(new File(workDir, "index.html"));
        try {
            engine.mergeTemplate(TEMPLATE_FOLDER + TEMPLATE_FILE, "UTF-8", context, fw);
            fw.flush();
        } finally {
            FileUtils.close(fw);
        }

        // package report into a zip archive
        if (pack) {
            reportFile = new File(outputDir, "whitesource.zip");
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(reportFile));
            FileUtils.packDirectory(workDir, zos);
            FileUtils.deleteRecursive(workDir);
        }

        return reportFile;
    }

    public File generateJson(File outputDir) throws IOException {
        if (result == null) {
            throw new IllegalStateException("Check policies result is null");
        }

        // prepare working directory
        File workDir = new File(outputDir, "whitesource");
        if (!workDir.exists() && !workDir.mkdir()) {
            throw new IOException("Unable to make output directory: " + workDir);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writeToFile(new File(workDir, CHECK_POLICIES_JSON_FILE), gson.toJson(result));

        // summarized policy rejection report
        Map<RequestPolicyInfo, RejectingPolicy> policyToSummaryMap = new HashMap<RequestPolicyInfo, RejectingPolicy>();
        for (Map.Entry<String, PolicyCheckResourceNode> entry : result.getExistingProjects().entrySet()) {
            updatePolicyRejectionSummary(entry.getValue(), entry.getKey(), policyToSummaryMap);
        }
        for (Map.Entry<String, PolicyCheckResourceNode> entry : result.getNewProjects().entrySet()) {
            updatePolicyRejectionSummary(entry.getValue(), entry.getKey(), policyToSummaryMap);
        }

        PolicyRejectionReport report = new PolicyRejectionReport();
        for (RejectingPolicy rejectingPolicy : policyToSummaryMap.values()) {
            report.getRejectingPolicies().add(rejectingPolicy);
            report.getSummary().setTotalRejectedLibraries(
                    report.getSummary().getTotalRejectedLibraries() + rejectingPolicy.getRejectedLibraries().size());
        }
        writeToFile(new File(workDir, POLICY_REJECTION_SUMMARY_FILE), gson.toJson(report));

        return workDir;
    }

    /* --- Protected methods --- */

    /**
     * Create and initialize a template engine to use for generating reports.
     *
     * @param properties properties to use for engine initialization. May be null.
     *
     * @return A new instance of th template engine to use.
     */
    protected VelocityEngine createTemplateEngine(Properties properties) {
        Properties actualProperties = properties;
        if (actualProperties == null) {
            actualProperties = new Properties();
        }

        String resourceLoader = actualProperties.getProperty(VelocityEngine.RESOURCE_LOADER);
        if (StringUtils.isBlank(resourceLoader)) {
            actualProperties.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
            actualProperties.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        }

        String loggerClass = actualProperties.getProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS);
        if (StringUtils.isBlank(loggerClass)) {
            actualProperties.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS,
                    "org.apache.velocity.runtime.log.NullLogChute");
        }

        VelocityEngine velocity = new VelocityEngine(actualProperties);
        velocity.init();

        return velocity;
    }

    /**
     * Create the context holding all the information of the report.
     * @return the velocity context
     */
    protected VelocityContext createTemplateContext() {
        VelocityContext context = new VelocityContext();
        context.put("result", result);
        context.put("hasRejections", result.hasRejections());
        context.put("licenses", createLicenseHistogram(result));
        context.put("creationTime", new SimpleDateFormat(DATE_FORMAT).format(new Date()));

        if (StringUtils.isNotBlank(buildName)) {
            context.put("buildName", buildName);
        }
        if (StringUtils.isNotBlank(buildNumber)) {
            context.put("buildNumber", buildNumber);
        }
        return context;
    }

    protected void writeToFile(File file, String json) throws IOException {
        FileWriter fw = new FileWriter(file);
        try {
            fw.write(StringEscapeUtils.unescapeJava(json));
            fw.flush();
        } finally {
            FileUtils.close(fw);
        }
    }

    /* --- Private methods --- */

    private Collection<LicenseHistogramDataPoint> createLicenseHistogram(BaseCheckPoliciesResult result) {
        Collection<LicenseHistogramDataPoint> dataPoints = new ArrayList<LicenseHistogramDataPoint>();

        // create distribution histogram
        Map<String, Integer> licenseHistogram = new HashMap<String, Integer>();
        for (Map.Entry<String, Collection<ResourceInfo>> entry : result.getProjectNewResources().entrySet()) {
            for (ResourceInfo resource : entry.getValue()) {
                for (String license : resource.getLicenses()) {
                    licenseHistogram.put(license, MapUtils.getInteger(licenseHistogram, license, 0) + 1);
                }
            }
        }

        // sort by count descending
        List<Map.Entry<String, Integer>> licenses = new ArrayList<Map.Entry<String, Integer>>(licenseHistogram.entrySet());
        Collections.sort(licenses, new ValueComparator());

        // create data points
        if (!licenses.isEmpty()) {
            // first licenses
            for (Map.Entry<String, Integer> entry : licenses.subList(0, Math.min(LICENSE_LIMIT, licenses.size()))) {
                dataPoints.add(new LicenseHistogramDataPoint(entry.getKey(), entry.getValue()));
            }

            // aggregation of histogram tail
            int tailSize = licenses.size() - LICENSE_LIMIT;
            int tailSum = 0;
            if (tailSize > 0) {
                for (Map.Entry<String, Integer> entry : licenses.subList(LICENSE_LIMIT, licenses.size())) {
                    tailSum += entry.getValue();
                }
                dataPoints.add(new LicenseHistogramDataPoint(OTHER_LICENSE + " (" + tailSize + ")", tailSum));
            }

            // normalize bar height
            float factor = MAX_BAR_HEIGHT / (float) Math.max(tailSum, licenses.get(0).getValue());
            for (LicenseHistogramDataPoint dataPoint : dataPoints) {
                dataPoint.setHeight((int) (factor * dataPoint.getOccurrences()));
            }
        }

        return dataPoints;
    }

    private void updatePolicyRejectionSummary(PolicyCheckResourceNode node, String projectName,
                                              Map<RequestPolicyInfo, RejectingPolicy> policyResourceMap) {
        RequestPolicyInfo policy = node.getPolicy();
        if (policy != null && policy.getActionType().equals(REJECT)) {
            RejectingPolicy rejectingPolicy = policyResourceMap.get(policy);
            if (rejectingPolicy == null) {
                rejectingPolicy = new RejectingPolicy(policy.getDisplayName(), policy.getFilterType(), policy.isProjectLevel(), policy.isInclusive());
                policyResourceMap.put(policy, rejectingPolicy);
            }

            Set<RejectedLibrary> rejectedResources = rejectingPolicy.getRejectedLibraries();
            ResourceInfo resource = node.getResource();
            RejectedLibrary rejectedLibrary = new RejectedLibrary(resource.getDisplayName(), resource.getSha1(), resource.getLink());
            if (rejectedResources.contains(rejectedLibrary)) {
                for (RejectedLibrary summary : rejectedResources) {
                    if (rejectedLibrary.equals(summary)) {
                        // add project to existing resource
                        summary.getProjects().add(projectName);
                        break;
                    }
                }
            } else {
                rejectedLibrary.getProjects().add(projectName);
                rejectedResources.add(rejectedLibrary);
            }
        }

        for (PolicyCheckResourceNode childNode : node.getChildren()) {
            updatePolicyRejectionSummary(childNode, projectName, policyResourceMap);
        }
    }

    /* --- Nested classes --- */

    static class ValueComparator implements Comparator<Map.Entry<String, Integer>>, Serializable {

        private static final long serialVersionUID = 2134689708073092860L;

        @Override
        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    }

    /* --- Getters / Setters --- */

    public BaseCheckPoliciesResult getResult() {
        return result;
    }

    public void setResult(BaseCheckPoliciesResult result) {
        this.result = result;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }
}
