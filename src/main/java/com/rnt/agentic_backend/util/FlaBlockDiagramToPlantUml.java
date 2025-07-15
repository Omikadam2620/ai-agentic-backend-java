package com.rnt.agentic_backend.util;

import java.util.List;
import java.util.Map;

public class FlaBlockDiagramToPlantUml {
    @SuppressWarnings("unchecked")
    public static String generate(Map<String, Object> flaOutput) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        sb.append("title AI-Generated Block Diagram\n\n");

        List<Map<String, Object>> blocks = null;
        List<Map<String, Object>> connections = null;
        try {
            blocks = (List<Map<String, Object>>) flaOutput.get("blocks");
            connections = (List<Map<String, Object>>) flaOutput.get("connections");
        } catch (Exception e) {
            sb.append("note as ERROR\nError reading blocks/connections from FLA output\nend note\n@enduml\n");
            return sb.toString();
        }

        if (blocks == null || blocks.isEmpty()) {
            sb.append("note as ERROR\nNo blocks found in FLA output!\nend note\n@enduml\n");
            return sb.toString();
        }
        if (connections == null) {
            sb.append("note as ERROR\nNo connections found in FLA output!\nend note\n@enduml\n");
            return sb.toString();
        }

        // Draw rectangles for each block, colored by type
        for (Map<String, Object> block : blocks) {
            String id = (String) block.get("id");
            String label = (String) block.get("label");
            String type = (String) block.get("type");
            String color = "#white";
            if ("Input".equalsIgnoreCase(type)) color = "#aaffaa";
            if ("Output".equalsIgnoreCase(type)) color = "#aaaaff";
            if ("Action".equalsIgnoreCase(type)) color = "#ffffaa";
            if ("Runnable".equalsIgnoreCase(type)) color = "#ffddaa";
            sb.append(String.format("rectangle \"%s\\n(%s)\" as B%s %s\n", label, type, id, color));
        }
        sb.append("\n");
        for (Map<String, Object> conn : connections) {
            String fromBlock = (String) conn.get("fromBlock");
            String fromPort = (String) conn.get("fromPort");
            String toBlock = (String) conn.get("toBlock");
            String toPort = (String) conn.get("toPort");
            sb.append(String.format("B%s --> B%s : %s ➜ %s\n", fromBlock, toBlock, fromPort, toPort));
        }
        sb.append("@enduml\n");
        return sb.toString();
    }
}
