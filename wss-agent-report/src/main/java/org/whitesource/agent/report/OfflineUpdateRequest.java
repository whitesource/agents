/**
 * Copyright (C) 2014 WhiteSource Ltd.
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
import com.google.gson.stream.JsonWriter;
import com.sun.org.apache.xpath.internal.axes.UnionPathIterator;
import org.apache.commons.io.FileUtils;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.dispatch.UpdateType;
import org.whitesource.agent.api.model.*;
import org.whitesource.agent.utils.ZipUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Map;

/**
 * A generator for offline update requests.
 *
 * @author tom.shapira
 */
public class OfflineUpdateRequest {

    /* --- Static members --- */

    public static final String UTF_8 = "UTF-8";

    /* --- Members --- */

    private UpdateInventoryRequest request;

    /* --- Constructors --- */

    /**
     * Constructor
     *
     * @param request update inventory
     */
    public OfflineUpdateRequest(UpdateInventoryRequest request) {
        this.request = request;
    }

    /* --- Public methods --- */

    /**
     * The method generates the update request file.
     *
     * @param outputDir Directory where request file will be created.
     * @param zip Whether or not to zip the request.
     * @param prettyJson Whether or not to parse the json before writing to file (only if zip is false).
     *
     * @return File reference to the resulting request.
     * @throws java.io.IOException In case of errors during file generation process.
     */
    public File generate(File outputDir, boolean zip, boolean prettyJson) throws IOException {
        if (request == null) {
            throw new IllegalStateException("Update inventory request is null");
        }

        // prepare working directory
        File workDir = new File(outputDir, "whitesource");
        if (!workDir.exists() && !workDir.mkdir()) {
            throw new IOException("Unable to make output directory: " + workDir);
        }

        String json;
        if (zip) {
            json = new Gson().toJson(request);
            json = ZipUtils.compressString(json);
        } else if (prettyJson) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            json = turnRequestToJson(gson);//gson.toJson(request);
        } else {
            json = new Gson().toJson(request);
        }

        // write to file
        File requestFile = new File(workDir, "update-request.txt");
        FileUtils.writeStringToFile(requestFile, json, UTF_8);
        return requestFile;
    }

    private String turnRequestToJson(Gson gson) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writer.beginArray();
        UpdateType updateType = this.request.getUpdateType();
        gson.toJson(updateType, UpdateType.class, writer);
        Map<String, String> extraProperties = this.request.getExtraProperties();
        gson.toJson(extraProperties, Map.class, writer);
        ScanSummaryInfo scanSummaryInfo = this.request.getScanSummaryInfo();
        gson.toJson(scanSummaryInfo, ScanSummaryInfo.class, writer);
        for (AgentProjectInfo agentProjectInfo : this.request.getProjects()){
            Coordinates coordinates = agentProjectInfo.getCoordinates();
            gson.toJson(coordinates, Coordinates.class, writer);
            Coordinates parentCoordinates = agentProjectInfo.getParentCoordinates();
            gson.toJson(parentCoordinates, Coordinates.class, writer);
            String projectSetupDescription = agentProjectInfo.getProjectSetupDescription();
            gson.toJson(projectSetupDescription, String.class, writer);
            ProjectSetupStatus projectSetupStatus = agentProjectInfo.getProjectSetupStatus();
            gson.toJson(projectSetupStatus, ProjectSetupStatus.class, writer);
            Collection<ProjectTag> projectTags = agentProjectInfo.getProjectTags();
            gson.toJson(projectTags, Collection.class, writer);
            String projectToken = agentProjectInfo.getProjectToken();
            gson.toJson(projectToken, String.class, writer);
            for (DependencyInfo dependencyInfo : agentProjectInfo.getDependencies()){
                gson.toJson(dependencyInfo, DependencyInfo.class, writer);
            }
        }
        writer.endArray();
        writer.close();
        return out.toString("UTF-8");
    }
}
