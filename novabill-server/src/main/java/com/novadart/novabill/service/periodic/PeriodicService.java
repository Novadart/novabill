package com.novadart.novabill.service.periodic;

public interface PeriodicService {
	
	public static final long MILLIS_IN_HOUR = 3_600_000l;
	
	public static final long MILLIS_IN_DAY = 86_400_000;
	
	public void runTasks();

}
