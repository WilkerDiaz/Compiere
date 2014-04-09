package org.compiere.common.constants;

public final class Build {
	private static final int OFFICIAL = 0;
	private static final int DEBUG = 1;
	private static final int VERBOSE = 2;
	//make PERF the same as OFFICIAL now so that the interim build will have the time instrumentation
	private static final int PERF = 3;
	private static final int mode = OFFICIAL;
	public static boolean isVerbose() {
		return (mode >= VERBOSE);
	}
	public static boolean isDebug() {
		return (mode >= DEBUG);
	}
	public static boolean isOfficial() {
		return (mode == OFFICIAL);
	}
	public static boolean isPerf() {
		return mode == PERF;
	}
}
