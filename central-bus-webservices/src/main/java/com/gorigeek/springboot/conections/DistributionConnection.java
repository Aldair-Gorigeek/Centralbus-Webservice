package com.gorigeek.springboot.conections;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DistributionConnection {
    public HttpURLConnection apiConnection(String endpoint, String methodType) throws IOException {
        URL apiURL = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
        connection.setRequestMethod(methodType);
        connection.setAllowUserInteraction(true);        
        return connection;
    }
}
