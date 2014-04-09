package org.compiere.esb;

import java.util.Map;
import java.util.Set;

import org.compiere.util.*;
/**empty class for now. write operations will be disabled in the future
 * 
 * @author dzhao
 *
 */
public class GWTServerContext extends CContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*
	@Override
	public void setContext(int windowNo, Map<String, String> addContext) {
		throw new UnsupportedOperationException("no set context for window:"+windowNo);
	}

	@Override
	public void setContext(int windowNo, String context, String value) {
		throw new UnsupportedOperationException("no set context value for window:"+windowNo);
	}

	@Override
	public void addWindow(int windowNo, Map<String, String> map) {
		throw new UnsupportedOperationException("no add window for window:"+windowNo);
	}
*
*/
	@Override
	protected Map<String, String> getMap() {
		return m_map;
	}
	
	@Override
	public Set<Entry<String, String>> entrySet() {
		return getMap().entrySet();
	}
	
	
}
