package com.rnt.agentic_backend.model;

public class SignalDefinition {
	private String signalName;
	private String description;
	private String dataType;
	private String range;
	private String unit;
	private String initialValue;
	private String comment;
	// Gap-filling fields:
	private String mode; // Input/Output/Action/Calibration
	private String codedLimits; // e.g., "0H FALSE, 1H TRUE"
	private String displayLimits; // e.g., "0..100"
	private String resolution; // e.g., "0.1"

	private String calibration;

    public String getCalibration() { return calibration; }
    public void setCalibration(String calibration) { this.calibration = calibration; }
    // ...add more as needed
	public String getSignalName() {
		return signalName;
	}
	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getInitialValue() {
		return initialValue;
	}
	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getCodedLimits() {
		return codedLimits;
	}
	public void setCodedLimits(String codedLimits) {
		this.codedLimits = codedLimits;
	}
	public String getDisplayLimits() {
		return displayLimits;
	}
	public void setDisplayLimits(String displayLimits) {
		this.displayLimits = displayLimits;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	
    
}
	