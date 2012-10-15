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

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.whitesource.agent.api.dispatch.CheckPoliciesResult;
import org.whitesource.agent.api.model.ResourceInfo;
import org.whitesource.agent.report.model.LicenseHistogramDataPoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
    private static final String CSS_FILE = "wss.css";

    private static final float MAX_BAR_HEIGHT = 50;
    private static final int LICENSE_LIMIT = 6;
    private static final String OTHER_LICENSE = "Other types";


    /* --- Members --- */

    private CheckPoliciesResult result;

    private String buildName;

    private String buildNumber;

    /* --- Constructors --- */

    /**
     * Constructor
     *
     * @param result
     */
    public PolicyCheckReport(CheckPoliciesResult result) {
        this.result = result;
    }

    /**
     * Constructor
     *
     * @param result
     * @param buildName
     * @param buildNumber
     */
    public PolicyCheckReport(CheckPoliciesResult result, String buildName, String buildNumber) {
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
     */
    public File generate(File outputDir, boolean pack) throws IOException {
        if (result == null) {
            throw new IllegalStateException("Check policies result is null");
        }

        // make sure we have a working directory
        File workDir = new File(outputDir, "whitesource");
        File reportFile = workDir;
        if (!workDir.exists()) {
            boolean mkdir = workDir.mkdir();
            if (!mkdir) {
                throw new IOException("Unable to make output directory");
            }
        }

        // create actual report
        createReport(workDir);

        // copy report resources
        copyReportResources(workDir);

        // package report into a zip archive
        if (pack) {
            reportFile = new File(outputDir, "whitesource.zip");
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(reportFile));
            FileUtils.packDirectory(workDir, zos);
            FileUtils.deleteRecursive(workDir);
        }

        return reportFile;
    }

    /* --- Private methods --- */

    private void createReport(File outputDir) throws IOException {
        // initialize velocity engine
        Velocity.setProperty(Velocity.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();

        // create template context
        VelocityContext context = new VelocityContext();
        context.put("result", result);
        context.put("hasRejections", result.hasRejections());
        context.put("licenses", createLicenseHistogram(result));
        context.put("creationTime", SimpleDateFormat.getInstance().format(new Date()));

        if (StringUtils.isNotBlank(buildName)) {
            context.put("buildName", buildName);
        }
        if (StringUtils.isNotBlank(buildNumber)) {
            context.put("buildNumber", buildNumber);
        }

        // merge template and close file
        FileWriter fw = new FileWriter(new File(outputDir, "index.html"));
        Velocity.mergeTemplate(TEMPLATE_FOLDER + TEMPLATE_FILE, "UTF-8", context, fw);
        fw.flush();
        fw.close();
    }

    private Collection<LicenseHistogramDataPoint> createLicenseHistogram(CheckPoliciesResult result) {
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
        Collections.sort(licenses, new Comparator<Map.Entry<String, Integer>>() {

            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });


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

    private void copyReportResources(File outputDir) throws IOException {
        // copy css file
        FileUtils.copyResource(TEMPLATE_FOLDER + CSS_FILE, new File(outputDir, CSS_FILE));
    }

    /* --- Getters / Setters --- */

    public CheckPoliciesResult getResult() {
        return result;
    }

    public void setResult(CheckPoliciesResult result) {
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
