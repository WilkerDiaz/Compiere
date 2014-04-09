package org.compiere.vos;

import java.io.*;


public class SortVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SortVO(){}
	public int key = 0;
	public int seq = 0;
	public String name = "";
	public boolean isYes = false;
}
