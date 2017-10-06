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
import org.apache.commons.io.FileUtils;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.utils.ZipUtils;

import java.io.File;
import java.io.IOException;

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
            json = gson.toJson(request);
        } else {
            json = new Gson().toJson(request);
        }

        // write to file
        File requestFile = new File(workDir, "update-request.txt");
        FileUtils.writeStringToFile(requestFile, json, UTF_8);
        return requestFile;
    }
}
