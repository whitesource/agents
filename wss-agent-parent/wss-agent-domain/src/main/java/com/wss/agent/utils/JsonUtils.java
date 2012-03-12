package com.wss.agent.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.wss.agent.exception.JsonParsingException;

/**
 * Utility class for JSON conversion.
 * 
 * @author tom.shapira
 *
 */
public final class JsonUtils {

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
	 * Convert the given json string to java object
	 * 
	 * @param json JSON string to parse.
	 * @param clazz Result object class.
	 * 
	 * @return Java object value of the json string.
	 * 
	 * @throws JsonParsingException In case of any errors.
	 */
	public static <T> T fromJson(String json, Class<T> clazz) throws JsonParsingException{
		ObjectMapper mapper = new ObjectMapper();
		T result = null;
		try {
			result = mapper.readValue(json, clazz);
		} catch (JsonGenerationException e) {
			throw new JsonParsingException(e);
		} catch (JsonMappingException e) {
			throw new JsonParsingException(e);
		} catch (IOException e) {
			throw new JsonParsingException(e);
		}
		return result;
	}
	
	/**
	 * Convert the given json string to java object
	 * 
	 * @param json JSON string to parse.
	 * @param typeRef Result object type reference.
	 * 
	 * @return Java object value of the json string.
	 * 
	 * @throws JsonParsingException In case of any errors.
	 */
	public static <T> T fromJson(String json, TypeReference<T> typeRef) throws JsonParsingException{
		ObjectMapper mapper = new ObjectMapper();
		T result = null;
		try {
			result = mapper.readValue(json, typeRef);
		} catch (JsonGenerationException e) {
			throw new JsonParsingException(e);
		} catch (JsonMappingException e) {
			throw new JsonParsingException(e);
		} catch (IOException e) {
			throw new JsonParsingException(e);
		}
		return result;
	}
	
	/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private JsonUtils() {
		// avoid instantiation
	}
}
