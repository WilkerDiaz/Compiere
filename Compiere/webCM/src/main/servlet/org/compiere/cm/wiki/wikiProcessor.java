package org.compiere.cm.wiki;

import org.compiere.cm.cache.*;
import org.compiere.model.X_CM_WikiToken;

/**
 * @author YS
 * @version $Id$
 */
public class wikiProcessor
{
	public static StringBuffer run (String source, int CM_WebProject_ID, String mediaPath, WikiToken wikiCache, boolean includeStyleTokens) throws Exception
	{
		return run (new StringBuffer(source), CM_WebProject_ID, mediaPath, wikiCache, includeStyleTokens);
	}
	
	/**
	 * 	Run
	 *	@param source
	 *  @param wikiCache 
	 *	@return xml
	 *	@throws Exception
	 */
	public static StringBuffer run (StringBuffer source, int CM_WebProject_ID, String mediaPath, WikiToken wikiCache, boolean includeStyleTokens) throws Exception
	{
		if (wikiCache.getSize ()>0) 
		{
			String [] wikiKeys = wikiCache.getSortedKeys ();
			for (int i = 0; i<wikiKeys.length; i++)
				if (includeStyleTokens || !wikiCache.get (wikiKeys[i], true).getTokenType ().equals (X_CM_WikiToken.TOKENTYPE_Style)) 
					source =  wikiCache.get (wikiKeys[i], true).processToken (source, CM_WebProject_ID, mediaPath);
		}
		return source;
	}
}	//	wikiProcessor
