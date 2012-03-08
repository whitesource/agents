package com.wss.agent.utils;

import java.io.IOException;
import java.util.Collection;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.wss.agent.exception.JsonParsingException;
import com.wss.agent.model.AgentProjectInfo;
import com.wss.agent.request.PropertiesResult;
import com.wss.agent.request.ResultEnvelope;
import com.wss.agent.request.UpdateInventoryRequest;
import com.wss.agent.request.UpdateInventoryResult;

/**
 * Utility class for JSON conversion.
 * 
 * @author tom.shapira
 *
 */
public class JsonUtils {

	/* --- Static methods --- */

	/**
	 * Converts the object to a JSON string.
	 * 
	 * @return JSON string representation of the object.
	 * @throws JsonParsingException
	 */
	public static String toJson(Object object) throws JsonParsingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			throw new JsonParsingException(e);
		} catch (JsonMappingException e) {
			throw new JsonParsingException(e);
		} catch (IOException e) {
			throw new JsonParsingException(e);
		}
		return jsonString;
	}

	/**
	 * Parses the JSON string back to a {@link UpdateInventoryRequest}.
	 * 
	 * @param json The JSON string to parse.
	 * @return {@link UpdateInventoryRequest} request
	 * 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws IOException
	 */
	public static Collection<AgentProjectInfo> parseProjectInfosJson(String json) throws JsonParsingException  {
		ObjectMapper mapper = new ObjectMapper();
		Collection<AgentProjectInfo> agentRequest = null;
		try {
			agentRequest = mapper.readValue(json, new TypeReference<Collection<AgentProjectInfo>>() { });
		} catch (JsonGenerationException e) {
			throw new JsonParsingException(e);
		} catch (JsonMappingException e) {
			throw new JsonParsingException(e);
		} catch (IOException e) {
			throw new JsonParsingException(e);
		}
		return agentRequest;
	}
	
	/**
	 * Parses the JSON string back to a {@link PropertiesResult}.
	 * 
	 * @param json The JSON string to parse.
	 * @return {@link PropertiesResult} properties.
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static PropertiesResult parsePropertiesJson(String json) throws JsonParsingException {
		ObjectMapper mapper = new ObjectMapper();
		PropertiesResult properties = null;
		try {
			properties = mapper.readValue(json, PropertiesResult.class);
		} catch (JsonGenerationException e) {
			throw new JsonParsingException(e);
		} catch (JsonMappingException e) {
			throw new JsonParsingException(e);
		} catch (IOException e) {
			throw new JsonParsingException(e);
		}
		return properties;
	}

	/**
	 * Parses the JSON string back to a {@link ResultEnvelope}.
	 * 
	 * @param json The JSON string to parse.
	 * @return {@link ResultEnvelope} result envelope.
	 * @throws JsonParsingException 
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static ResultEnvelope parseResultEnvelopeJson(String json) throws JsonParsingException {
		ObjectMapper mapper = new ObjectMapper();
		ResultEnvelope envelope = null;
		try {
			envelope = mapper.readValue(json, ResultEnvelope.class);
		} catch (JsonGenerationException e) {
			throw new JsonParsingException(e);
		} catch (JsonMappingException e) {
			throw new JsonParsingException(e);
		} catch (IOException e) {
			throw new JsonParsingException(e);
		}
		return envelope;
	}
	
	/**
	 * Parses the JSON string back to a {@link UpdateInventoryResult}.
	 * 
	 * @param json The JSON string to parse.
	 * @return {@link UpdateInventoryResult} update inventory result.
	 * @throws JsonParsingException 
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static UpdateInventoryResult parseUpdateInventoryResultJson(String json) throws JsonParsingException {
		ObjectMapper mapper = new ObjectMapper();
		UpdateInventoryResult result = null;
		try {
			result = mapper.readValue(json, UpdateInventoryResult.class);
		} catch (JsonGenerationException e) {
			throw new JsonParsingException(e);
		} catch (JsonMappingException e) {
			throw new JsonParsingException(e);
		} catch (IOException e) {
			throw new JsonParsingException(e);
		}
		return result;
	}
}
