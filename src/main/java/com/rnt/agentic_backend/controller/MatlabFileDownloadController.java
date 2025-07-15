package com.rnt.agentic_backend.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class MatlabFileDownloadController {

    @GetMapping("/api/download/gen_model.m")
    public ResponseEntity<FileSystemResource> downloadMatlabFile() {
        FileSystemResource file = new FileSystemResource("output/gen_model.m");
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=gen_model.m")
                .contentType(MediaType.parseMediaType("text/x-matlab"))
                .body(file);
    }
}
