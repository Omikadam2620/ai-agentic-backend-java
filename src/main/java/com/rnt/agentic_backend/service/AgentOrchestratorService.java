package com.rnt.agentic_backend.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rnt.agentic_backend.model.SignalDefinition;

@Service
public class AgentOrchestratorService {

	private final ObjectMapper mapper = new ObjectMapper();

	private String postJson(String endpoint, Object data) throws Exception {
		URL url = new URL(endpoint);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);
		try (OutputStream os = conn.getOutputStream()) {
			mapper.writeValue(os, data);
		}
		if (conn.getResponseCode() >= 400) {
			throw new IOException("Error from " + endpoint + ": " + conn.getResponseMessage());
		}
		try (InputStream is = conn.getInputStream()) {
			return new String(is.readAllBytes());
		}
	}

	private String downloadMatlabFile(String endpoint, Object data, String outputPath) throws Exception {
		URL url = new URL(endpoint);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);

		// Write request body
		try (OutputStream os = conn.getOutputStream()) {
			mapper.writeValue(os, data);
		}
		if (conn.getResponseCode() >= 400) {
			throw new IOException("Error from " + endpoint + ": " + conn.getResponseMessage());
		}

		try (InputStream is = conn.getInputStream(); FileOutputStream fos = new FileOutputStream(outputPath)) {
			byte[] buffer = new byte[8192];
			int n;
			while ((n = is.read(buffer)) > 0) {
				fos.write(buffer, 0, n);
			}
		}
		return outputPath;
	}

	public Map<String, Object> fullOrchestration(MultipartFile file) throws Exception {
		// 1. Parse Excel
		RequirementImporterService importer = new RequirementImporterService();
		List<SignalDefinition> signals = importer.parseExcel(file);

		// 2. Call RIA Agent
		String riaResp = postJson("http://127.0.0.1:5001/ria", signals);
		Map<String, Object> riaOutput = mapper.readValue(riaResp, Map.class);

		// 3. Call LLRGA Agent (input: RIA output)
		String llrgaResp = postJson("http://127.0.0.1:5002/llrga", riaOutput);
		Map<String, Object> llrgaOutput = mapper.readValue(llrgaResp, Map.class);

		String flaResp = postJson("http://127.0.0.1:5003/fla", llrgaOutput);
		Map<String, Object> flaOutput = mapper.readValue(flaResp, Map.class);

		String matlabFilePath = "output/gen_model.m";
		downloadMatlabFile("http://127.0.0.1:5006/matlab-export", flaOutput, matlabFilePath);

		// 5. Call PRA Agent (input: FLA output)
		String praResp = postJson("http://127.0.0.1:5004/pra", flaOutput);
		Map<String, Object> praOutput = mapper.readValue(praResp, Map.class);

		Map<String, Object> result = new LinkedHashMap<>();
		result.put("ria", riaOutput);
		result.put("llrga", llrgaOutput);
		result.put("fla", flaOutput);
		result.put("matlab_download", "/api/download/gen_model.m");
		result.put("pra", praOutput);
		return result;
	}

}
