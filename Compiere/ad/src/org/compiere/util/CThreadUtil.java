package org.compiere.util;

public final class CThreadUtil {
	static ThreadLocal<Boolean> isCalloutActive = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
		
	};
	public static boolean isCalloutActive() {
		return isCalloutActive.get();
	}
	public static void setCalloutActive(boolean a) {
		isCalloutActive.set(a);
	}
	
	
	static ThreadLocal<CContext> ctx = new ThreadLocal<CContext>() {
		@Override
		protected CContext initialValue() {
			return null;
		}
	};
	public static CContext getCtx() {
		return ctx.get();
	}
	public static void setCtx(CContext a) {
		ctx.set(a);
	}


}
