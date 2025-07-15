package com.rnt.agentic_backend.util;

import com.rnt.agentic_backend.model.SignalDefinition;
import java.util.Map;

public class GapFiller {
    // Example hardcoded reference maps (extend as needed)
    private static final Map<String, String> SIGNAL_TYPES = Map.of(
        "LvEisExctCdnVld", "Boolean",
        "LvEisMesEnd", "Boolean",
        "LvChaDcvCpLinkEisRdy", "Boolean",
        "SetFrqCntctCtlEis", "Action",
        "SetLvCntctEisClsCmd", "Action",
        "SetDucyCntctCtlEis", "Action"
    );
    private static final Map<String, String> SIGNAL_MODES = Map.of(
        "LvEisExctCdnVld", "Input",
        "LvEisMesEnd", "Input",
        "SetFrqCntctCtlEis", "Action",
        "SetLvCntctEisClsCmd", "Action",
        "SetDucyCntctCtlEis", "Action"
    );
    private static final Map<String, String> CODED_LIMITS = Map.of(
        "LvEisExctCdnVld", "0H FALSE, 1H TRUE"
    );

    public static void fillGaps(SignalDefinition signal) {
        // Data type
        if (signal.getDataType() == null || signal.getDataType().isBlank()) {
            signal.setDataType(SIGNAL_TYPES.getOrDefault(signal.getSignalName(), ""));
        }
        // Mode
        if (signal.getMode() == null || signal.getMode().isBlank()) {
            signal.setMode(SIGNAL_MODES.getOrDefault(signal.getSignalName(), ""));
        }
        // Coded limits
        if (signal.getCodedLimits() == null || signal.getCodedLimits().isBlank()) {
            signal.setCodedLimits(CODED_LIMITS.getOrDefault(signal.getSignalName(), ""));
        }
    }
}
