package com.rnt.agentic_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.rnt.agentic_backend.model.ModelSpec;
import com.rnt.agentic_backend.model.SignalDefinition;
import com.rnt.agentic_backend.service.AgentOrchestratorService;
import com.rnt.agentic_backend.service.RequirementImporterService;
import com.rnt.agentic_backend.util.ModelSpecBuilder;
import com.rnt.agentic_backend.util.PlantUmlDiagramGenerator;
import com.rnt.agentic_backend.util.PlantUmlImageRenderer;
import com.rnt.agentic_backend.util.PythonAgentClient;

@RestController
@RequestMapping("/api/requirements")
public class RequirementImporterController {

	@Autowired
	private RequirementImporterService importerService;

	@Autowired
	private AgentOrchestratorService orchestratorService;

	@PostMapping("/run-orchestration")
	public ResponseEntity<Map<String, Object>> orchestrate(@RequestParam("file") MultipartFile file) {
		try {
			Map<String, Object> result = orchestratorService.fullOrchestration(file);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
		}
	}

	@PostMapping("/upload")
	public ResponseEntity<List<SignalDefinition>> uploadRequirementsExcel(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		List<SignalDefinition> signals = importerService.parseExcel(file);
		try {
			signals = PythonAgentClient.enrichSignals(signals);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
		return ResponseEntity.ok(signals);
	}

	@PostMapping("/model-spec")
	public ResponseEntity<ModelSpec> buildModelSpec(@RequestParam("file") MultipartFile file) {
		List<SignalDefinition> parsedSignals = importerService.parseExcel(file);
		try {
			parsedSignals = PythonAgentClient.enrichSignals(parsedSignals); // <-- Enrich with Python agent!
		} catch (Exception e) {
			e.printStackTrace();
			// Decide: continue with un-enriched, or fail
			return ResponseEntity.internalServerError().build();
		}
		ModelSpec modelSpec = ModelSpecBuilder.build("RBEISE", "Every 100ms", parsedSignals);
		return ResponseEntity.ok(modelSpec);
	}

	
	@PostMapping(value = "/matlab-script", consumes = "text/plain")
	public ResponseEntity<String> uploadMatlabScript(@RequestBody String matlabScript) {
	    try {
	        // For demonstration, just save to a file. Customize as needed!
	        java.nio.file.Files.write(
	            java.nio.file.Paths.get("auto_model.m"),
	            matlabScript.getBytes(java.nio.charset.StandardCharsets.UTF_8)
	        );
	        return ResponseEntity.ok("MATLAB script received and saved.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().body("Failed to save script: " + e.getMessage());
	    }
	}

	
	

}
