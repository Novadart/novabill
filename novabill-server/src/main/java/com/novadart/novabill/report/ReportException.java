package com.novadart.novabill.report;

public class ReportException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public ReportException() {
	}

	public ReportException(String message) {
		super(message);
	}

	public ReportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportException(Throwable cause) {
		super(cause);
	}

	public ReportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
