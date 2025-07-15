package com.rnt.agentic_backend.model;

import java.util.List;

public class ModelSpec {
    private String moduleName;                 // e.g., "RBEISE"
    private String eventSpec;                  // e.g., "Every 100ms"
    private List<SignalDefinition> inputs;
    private List<SignalDefinition> outputs;
    private List<SignalDefinition> actions;
    private List<SignalDefinition> calibrations;

    // Constructors
    public ModelSpec() {}

    public ModelSpec(String moduleName, String eventSpec,
                     List<SignalDefinition> inputs,
                     List<SignalDefinition> outputs,
                     List<SignalDefinition> actions,
                     List<SignalDefinition> calibrations) {
        this.moduleName = moduleName;
        this.eventSpec = eventSpec;
        this.inputs = inputs;
        this.outputs = outputs;
        this.actions = actions;
        this.calibrations = calibrations;
    }

    // Getters and Setters
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }

    public String getEventSpec() { return eventSpec; }
    public void setEventSpec(String eventSpec) { this.eventSpec = eventSpec; }

    public List<SignalDefinition> getInputs() { return inputs; }
    public void setInputs(List<SignalDefinition> inputs) { this.inputs = inputs; }

    public List<SignalDefinition> getOutputs() { return outputs; }
    public void setOutputs(List<SignalDefinition> outputs) { this.outputs = outputs; }

    public List<SignalDefinition> getActions() { return actions; }
    public void setActions(List<SignalDefinition> actions) { this.actions = actions; }

    public List<SignalDefinition> getCalibrations() { return calibrations; }
    public void setCalibrations(List<SignalDefinition> calibrations) { this.calibrations = calibrations; }
}
