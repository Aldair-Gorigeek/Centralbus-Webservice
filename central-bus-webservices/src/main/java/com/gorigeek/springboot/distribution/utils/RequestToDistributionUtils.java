package com.gorigeek.springboot.distribution.utils;

import org.apache.logging.log4j.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.gorigeek.springboot.conections.DistributionConnection;
import com.gorigeek.springboot.distribution.entity.Response;

@Component
public class RequestToDistributionUtils {
    @Value("${baseUrlDistribution}")
    private String baseUrlDistribution;
    
    @Value("${portDistribution}")
    private int portDistribution;
    
    private static final Logger logger = LogManager.getLogger(RequestToDistributionUtils.class);
    private final DistributionConnection distributionConnection = new DistributionConnection();

    public Response<JsonNode> request(String endpoint, String method, Object body) {
        final String baseUrl = baseUrlDistribution + ":" + portDistribution;
        String endpointUrl = baseUrl + endpoint;
        logger.log(Level.INFO, "Endpoint: \n" + endpointUrl);
        try {
            HttpURLConnection connection = distributionConnection.apiConnection(endpointUrl, method);
            if (isRequestBodyRequired(method))
                connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            if (isRequestBodyRequired(method))
                setJsonRequestProperties(connection, body);

            JsonNode content = ProcessResponse.readResponse(connection);

            return buildResponse(content);
        } catch (IOException e) {
            logger.log(Level.FATAL,
                    "Error en la request. \n" + "Endpoint: " + endpointUrl + "\n" + "Mensage: " + e.getMessage());
            return new Response<>();
        }
    }

    private boolean isRequestBodyRequired(String method) {
        return method.equals("POST") || method.equals("PUT");
    }

    private void setJsonRequestProperties(HttpURLConnection connection, Object body) throws IOException {
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            String jsonBody = new Gson().toJson(body);
            logger.log(Level.INFO, "Json Enviado: \n" + jsonBody);
            byte[] postDataBytes = jsonBody.getBytes("UTF-8");
            outputStream.write(postDataBytes);
            outputStream.flush();
        }
    }

    private Response<JsonNode> buildResponse(JsonNode content) {        
        try {
            int responseCode = content.get("code").asInt();
            Response<JsonNode> response = new Response<>();
            response.setCode(responseCode);

            if (responseCode >= 200 && responseCode < 300) {
                response.setBody(content.get("body"));
            } else {
                response.setBody(content.get("body"));
                logger.log(Level.FATAL, String.format("La solicitud de API falló con el código de estado: %s y cuerpo: %s",
                        response.getCode(), response.getBody()));
            }
            return response;
        }catch(Exception e) {
            logger.log(Level.ERROR, "Error al crear la respuesta");
            return new Response<>();
        }
        
    }
}
