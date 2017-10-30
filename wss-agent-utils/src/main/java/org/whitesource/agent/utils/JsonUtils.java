package org.whitesource.agent.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whitesource.agent.api.dispatch.UpdateInventoryRequest;
import org.whitesource.agent.api.model.AgentProjectInfo;
import org.whitesource.agent.api.model.DependencyInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for various json operations.
 *
 * @author eugen.horovitz
 */
public class JsonUtils {

    /* --- Static members --- */

    public static final String UTF_8 = "UTF-8";

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /* --- Static methods --- */

    public static List<DependencyInfo> readDependencies(InputStream in) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(in, UTF_8);
             BufferedReader br = new BufferedReader(inputStreamReader);
             JsonReader reader = new JsonReader(br)) {
            return getDependencies(reader);
        } catch (IOException ex) {
            logger.error("Failed to read dependencies: {}", ex.getMessage());
        }
        return null;
    }

    public static List<AgentProjectInfo> readProjects(InputStream in) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(in, UTF_8);
             BufferedReader br = new BufferedReader(inputStreamReader);
             JsonReader reader = new JsonReader(br)) {
            return getProjectsFromReader(reader);
        } catch (IOException ex) {
            logger.error("Failed to read projects: {}", ex.getMessage());
        }
        return null;
    }

    public static UpdateInventoryRequest readUpdateInventoryRequest(InputStream in) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(in, UTF_8);
             BufferedReader br = new BufferedReader(inputStreamReader);
             JsonReader reader = new JsonReader(br)) {
            Gson gson = new Gson();
            UpdateInventoryRequest message = gson.fromJson(reader, UpdateInventoryRequest.class);
            return message;
        }
    }

    public static void writeUpdateInventoryRequest(OutputStream out, UpdateInventoryRequest message) throws IOException {
        Gson gson = new Gson();
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, UTF_8);
             BufferedWriter bw = new BufferedWriter(outputStreamWriter);
             JsonWriter writer = new JsonWriter(bw);) {
            writer.setIndent("  ");
            writer.beginArray();

            gson.toJson(message, UpdateInventoryRequest.class, writer);
            writer.endArray();
        } catch (IOException ex) {
            logger.error("Failed to write data :", ex);
        }
    }

    public static void writeProjects(OutputStream out, List<AgentProjectInfo> projects) throws IOException {
        Gson gson = new Gson();
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, UTF_8);
             BufferedWriter bw = new BufferedWriter(outputStreamWriter);
             JsonWriter writer = new JsonWriter(bw)) {
            writer.setIndent("  ");
            writer.beginArray();
            for (AgentProjectInfo project : projects) {
                gson.toJson(project, AgentProjectInfo.class, writer);
            }
            writer.endArray();
        } catch (IOException ex) {
            logger.error("Failed to write data: {}", ex.getMessage());
        }
    }

    public static<T> String save(T object, boolean pretty){
        Gson gson;
        if(pretty) {
            gson = (new GsonBuilder()).setPrettyPrinting().create();
        }
        else{
            gson = (new GsonBuilder()).create();
        }
        return gson.toJson(object);
    }

    public static <T> T load(final InputStream inputStream, final Class<T> clazz) throws IOException {
        if (inputStream != null) {
            final Gson gson = new Gson();
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, UTF_8);
                 BufferedReader br = new BufferedReader(inputStreamReader);
                 JsonReader reader = new JsonReader(br)) {
                return gson.fromJson(reader, clazz);
            } catch (IOException ex) {
                logger.error("Failed to load data: {}", ex.getMessage());
            }
            return null;
        }
        return null;
    }

    public static List<AgentProjectInfo> readProjectsChunks(InputStream in) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(in, UTF_8);
             BufferedReader br = new BufferedReader(inputStreamReader);
             JsonReader reader = new JsonReader(br)) {
            return getProjectsFromReader(reader);
        } catch (IOException ex) {
            logger.error("Failed to read data: {}", ex.getMessage());
        }
        return null;
    }

    /* --- Private methods --- */

    private static List<DependencyInfo> getDependencies(JsonReader reader) throws IOException {
        Gson gson = new Gson();
        List<DependencyInfo> dependencies = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            DependencyInfo dependencyInfo = gson.fromJson(reader, DependencyInfo.class);
            dependencies.add(dependencyInfo);
        }
        reader.endArray();
        return dependencies;
    }

    private static List<AgentProjectInfo> getProjectsFromReader(JsonReader reader) throws IOException {
        Gson gson = new Gson();
        List<AgentProjectInfo> projects = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            AgentProjectInfo message = gson.fromJson(reader, AgentProjectInfo.class);
            projects.add(message);
        }
        reader.endArray();
        return projects;
    }
}