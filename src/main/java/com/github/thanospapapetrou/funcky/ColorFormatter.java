package com.github.thanospapapetrou.funcky;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ColorFormatter extends Formatter {
	private static final Map<Level, String> COLORS = new HashMap<>();
	private static final String FORMAT = "%1$s%2$s%3$s\n";
	private static final String RESET = "\u001B[0m";

	static {
		COLORS.put(Level.INFO, "\u001B[32m");
		COLORS.put(Level.WARNING, "\u001B[31m");
	}

	@Override
	public String format(final LogRecord record) {
		return String.format(FORMAT, COLORS.containsKey(record.getLevel()) ? COLORS.get(record.getLevel()) : RESET, record.getMessage(), RESET);
	}

}
