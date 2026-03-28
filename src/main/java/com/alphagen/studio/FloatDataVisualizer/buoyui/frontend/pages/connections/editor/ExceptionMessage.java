package com.alphagen.studio.FloatDataVisualizer.buoyui.frontend.pages.connections.editor;

public class ExceptionMessage {

	// Connection Data (7)
	public static final String CONNECTION_NAME_EMPTY = "Name cannot be empty";
	public static final String CONNECTION_NAME_INVALID_CHARACTERS = "Name cannot have special characters other than \" \", \"-\", \"_\"";
	public static final String CONNECTION_NAME_EXISTS = "Name already exists. Change name or delete duplicate connection";
	public static final String CONNECTION_BAUD_RATE_EMPTY = "Baud Rate cannot be empty";
	public static final String CONNECTION_BAUD_RATE_INVALID_CHARACTERS = "Only numbers (0-9) are allowed";
	public static final String CONNECTION_BAUD_RATE_INVALID_RANGE = "Baud Rate cannot be less than 0";
	public static final String CONNECTION_PORT_EMPTY = "Port cannot be is empty";

	// Float Data (7)
	public static final String FLOAT_DATA_FORMAT_EMPTY_ARGUMENTS = "Data Format cannot be empty";
	public static final String FLOAT_DATA_FORMAT_MISSING_ARGUMENTS = "There should be at least 1 recorded value. Ex: Depth(m), Pressure(Pa)";
	public static final String FLOAT_DATA_FORMAT_TEAM_DATA_ARGUMENTS_NOT_FOUND = "Team Info not found as first argument";
	public static final String FLOAT_DATA_FORMAT_TEAM_DATA_ARGUMENTS_MISSING = "Team Name or Number does not exist";
	public static final String FLOAT_DATA_FORMAT_TEAM_DATA_NUM_INVALID_LENGTH = "Team Number needs to be 4 characters long";
	public static final String FLOAT_DATA_FORMAT_TEAM_DATA_NAME_INVALID = "Team Name cannot be empty";
	public static final String FLOAT_DATA_FORMAT_PACKET_ID_NOT_FOUND = "Cannot find \"pkt-\" as second argument";

	// Measurement Data (4)
	public static final String MEASUREMENT_DATA_FORMAT_TIME_NOT_FOUND = "Time needs to be third argument";
	public static final String MEASUREMENT_DATA_FORMAT_MEASUREMENT_UNIT_NOT_FOUND = "Unit for the measurement not found: ";
	public static final String MEASUREMENT_DATA_FORMAT_TIME_INVALID = "Format for recording values in app: name (unit)";
	public static final String MEASUREMENT_DATA_FORMAT_MEASUREMENT_NAME_NOT_FOUND = "Name of the measurement is empty";

	// Processor Data (3)
	public static final String PROCESSOR_DATA_START_DATA_TRANSFER_FLAG_EMPTY = "Start Data Transfer Flag is empty";
	public static final String PROCESSOR_DATA_END_DATA_TRANSFER_FLAG_EMPTY = "End Data Transfer Flag empty";
	public static final String PROCESSOR_DATA_TRANSFER_FLAG_SAME = "Start and End Data Transfer Flag cannot be the same";

}