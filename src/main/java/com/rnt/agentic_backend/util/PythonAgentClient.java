package com.rnt.agentic_backend.util;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rnt.agentic_backend.model.SignalDefinition;

public class PythonAgentClient {

    private static final String AGENT_URL = "http://127.0.0.1:5001/llrga";
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<SignalDefinition> enrichSignals(List<SignalDefinition> signals) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = mapper.writeValueAsString(signals);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(AGENT_URL, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return mapper.readValue(response.getBody(), new TypeReference<List<SignalDefinition>>() {});
        } else {
            throw new RuntimeException("Failed to call Python agent: " + response.getStatusCode());
        }
    }
}
