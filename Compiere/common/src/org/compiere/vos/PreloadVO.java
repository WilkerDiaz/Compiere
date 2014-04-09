package org.compiere.vos;

import java.io.*;
import java.util.*;

import org.compiere.util.*;

public class PreloadVO extends ResponseVO implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public HashMap<String,String> messages;
	public HashMap<String, ArrayList<ValueNamePair>> lookups;	
	public HashMap<String,String> licenses;
	
	public FormatVO formatVO;
	public int windowSerial;
	
	// DO NOT COMMENT OUT THE LICENSE CHECK!  Instead, use RUN_Support to get a valid license!
	public String licenseCheck = "LICENSE CHECK FAILED.  Use RUN_Support to get a valid license.";

}
