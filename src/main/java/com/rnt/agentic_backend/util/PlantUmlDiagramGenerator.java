package com.rnt.agentic_backend.util;

import com.rnt.agentic_backend.model.ModelSpec;
import com.rnt.agentic_backend.model.SignalDefinition;

public class PlantUmlDiagramGenerator {

    public static String generate(ModelSpec spec) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");
        sb.append("title Module: ").append(spec.getModuleName()).append("\\nEvent: ").append(spec.getEventSpec()).append("\n\n");

        // Inputs
        for (SignalDefinition input : spec.getInputs()) {
            sb.append("interface ").append(input.getSignalName()).append("\n");
        }

        // Outputs
        for (SignalDefinition output : spec.getOutputs()) {
            sb.append("interface ").append(output.getSignalName()).append("\n");
        }

        // Actions (as rectangles)
        for (SignalDefinition action : spec.getActions()) {
            sb.append("class ").append(action.getSignalName()).append("\n");
        }

        // Show connections (simple: connect all inputs and actions to all outputs)
        for (SignalDefinition input : spec.getInputs()) {
            for (SignalDefinition output : spec.getOutputs()) {
                sb.append(input.getSignalName()).append(" --> ").append(output.getSignalName()).append("\n");
            }
        }
        for (SignalDefinition action : spec.getActions()) {
            for (SignalDefinition output : spec.getOutputs()) {
                sb.append(action.getSignalName()).append(" --> ").append(output.getSignalName()).append("\n");
            }
        }

        sb.append("@enduml");
        return sb.toString();
    }
}
