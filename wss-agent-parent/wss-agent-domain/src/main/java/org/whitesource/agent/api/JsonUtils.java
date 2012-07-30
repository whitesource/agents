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
package org.whitesource.agent.api;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


/**
 * Utility class for JSON conversion.
 * 
 * @author tom.shapira
 */
public final class JsonUtils {

	/* --- Static methods --- */

	/**
	 * The method convert the given object to a JSON string.
	 * 
	 * @return JSON string representation of the object.
	 * 
	 * @throws IOException In case of errors during the conversion process.
	 */
	public static String toJson(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return  mapper.writeValueAsString(object);
	}
	
	/**
	 * Convert the given JSON string to java object
	 * 
	 * @param json JSON string to parse.
	 * @param clazz Result object class.
	 * 
	 * @return Java object value of the JSON string.
	 * 
	 * @throws IOException In case of errors during the conversion process.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, clazz);
	}
	
	/**
	 * Convert the given JSON string to java object
	 * 
	 * @param json JSON string to parse.
	 * @param typeRef Result object type reference.
	 * 
	 * @return Java object value of the JSON string.
	 * 
	 * @throws IOException In case of errors during the conversion process.
	 */
	@SuppressWarnings("unchecked") 
	public static <T> T fromJson(String json, TypeReference<T> typeRef) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, typeRef);
	}
	
	/* --- Constructors --- */
	
	/**
	 * Private default constructor
	 */
	private JsonUtils() {
		// avoid instantiation
	}
}
