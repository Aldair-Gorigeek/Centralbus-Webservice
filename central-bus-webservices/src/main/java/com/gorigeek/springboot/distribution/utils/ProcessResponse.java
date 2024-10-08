package com.gorigeek.springboot.distribution.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ProcessResponse {
	public static JsonNode readResponse(HttpURLConnection connection) throws IOException {
		// StringBuilder para almacenar la respuesta del servidor
		StringBuilder response = new StringBuilder();
		int responseCode = connection.getResponseCode();
		// Utiliza getInputStream() para códigos de estado 2xx (éxito)
		if (responseCode >= 200 && responseCode < 300) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {				    
					response.append(line);
				}
			}
		} else {
			// Utiliza getErrorStream() para códigos de estado diferentes de 2xx (errores)
			try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
				String errorLine;
				while ((errorLine = errorReader.readLine()) != null) {
					response.append(errorLine);
				}
			}
		}		
		 ObjectMapper objectMapper = new ObjectMapper();
		 JsonNode jsonNode = objectMapper.readTree(response.toString());	    	  
		return jsonNode;
	}
}
