package com.rnt.agentic_backend.util;

import com.rnt.agentic_backend.model.ModelSpec;
import com.rnt.agentic_backend.model.SignalDefinition;

import java.util.List;
import java.util.stream.Collectors;

public class ModelSpecBuilder {
    public static ModelSpec build(String moduleName, String eventSpec, List<SignalDefinition> allSignals) {
        List<SignalDefinition> inputs = allSignals.stream()
                .filter(s -> "Input".equalsIgnoreCase(s.getMode()))
                .collect(Collectors.toList());

        List<SignalDefinition> outputs = allSignals.stream()
                .filter(s -> "Output".equalsIgnoreCase(s.getMode()))
                .collect(Collectors.toList());

        List<SignalDefinition> actions = allSignals.stream()
                .filter(s -> "Action".equalsIgnoreCase(s.getMode()))
                .collect(Collectors.toList());

        List<SignalDefinition> calibrations = allSignals.stream()
                .filter(s -> "Calibration".equalsIgnoreCase(s.getMode()))
                .collect(Collectors.toList());

        return new ModelSpec(moduleName, eventSpec, inputs, outputs, actions, calibrations);
    }
}
