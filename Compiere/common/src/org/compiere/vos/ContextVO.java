package org.compiere.vos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ContextVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6013471900017545075L;
	
	public Map<String, String> stringMap = new HashMap<String, String>();
	
	public String getStringValue(String key) {
		return stringMap.get(key);
	}
}
