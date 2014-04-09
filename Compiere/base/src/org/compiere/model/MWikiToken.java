/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.util.regex.*;

import org.compiere.util.*;

/**
 * 	Container Stage Model
 *	
 *  @author Yves Sandfort
 *  @version $Id$
 */
public class MWikiToken extends X_CM_WikiToken
{
    /** Logger for class MWikiToken */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWikiToken.class);
	/**	serialVersionUID	*/
	private static final long serialVersionUID = 4980395873969221189L;

	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param CM_WikiToken_ID id
	 *	@param trx tansaction
	 */
	public MWikiToken (Ctx ctx, int CM_WikiToken_ID, Trx trx)
	{
		super (ctx, CM_WikiToken_ID, trx);
	}	//	MWikiToken

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MWikiToken (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MWikiToken
	
	/** Logger								*/
	private static CLogger		s_log = CLogger.getCLogger (MWikiToken.class);
	/** Pattern								*/
	private Pattern pattern = null;
	
	/**
	 * 	getPattern
	 *	@return returns preCompiled RegEx Pattern
	 */
	public Pattern getPattern()
	{
		if (pattern!=null)
			return pattern;
		pattern = Pattern.compile (getName());
		return pattern;
	}
	
	/**
	 *  (re)Load record with m_ID[*]
	 *  @param trx transaction
	 *  @return true if loaded
	 */
	@Override
	public boolean load (Trx trx)
	{
		pattern = null;
		return super.load (trx);
	}

	
	/**
	 * 	get all Wiki Tokens on system level (i.e. to preload cache)
	 *  @param ctx 
	 *  @param trx 
	 *	@return Array of previous DunningLevels
	 */
	public static MWikiToken[] getAllForPreload(Ctx ctx, Trx trx) 
	{
		ArrayList<MWikiToken> list = new ArrayList<MWikiToken>();
		String sql = "SELECT * FROM CM_WikiToken WHERE Ad_Client_ID=0 AND IsActive='Y' ORDER By SeqNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MWikiToken(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		MWikiToken[] retValue = new MWikiToken[list.size()];
		list.toArray(retValue);
		return retValue;
	}
	
	/**
	 * 	process any token to a StringBuffer, the different types need to handle the results different.
	 *	@param source
	 *	@param CM_WebProject_ID
	 *	@return converted StringBuffer
	 */
	public StringBuffer processToken(StringBuffer source, int CM_WebProject_ID, String MediaPath)
	{
		if (getTokenType ().equals (X_CM_WikiToken.TOKENTYPE_Style)) {
			Matcher matcher = getPattern ().matcher (source);
			source = new StringBuffer(matcher.replaceAll (getMacro ()));
		} else if (getTokenType ().equals (X_CM_WikiToken.TOKENTYPE_SQLCommand)) {
			
		} else if (getTokenType ().equals (X_CM_WikiToken.TOKENTYPE_ExternalLink)) {
			Matcher matcher = getPattern ().matcher (source);
			source = new StringBuffer(matcher.replaceAll (getMacro ()));
		} else if (getTokenType ().equals (X_CM_WikiToken.TOKENTYPE_InternalLink)) {
			Matcher matcher = getPattern ().matcher (source);
			while(matcher.find ()) {
				if (matcher.group(1).equals ("Media:")) 
				{
					String Name = matcher.group (2);
					MMedia thisMedia = MMedia.getByName (getCtx(), Name, CM_WebProject_ID, null);
					String replaceString = "";
					if (thisMedia != null)
					{
						if (matcher.groupCount ()>2)
							replaceString = "<img src=\"" + MediaPath + thisMedia.getFileName () + "\" alt=\"" + matcher.group (3) + "\"/>";
					}
					source = new StringBuffer(matcher.replaceFirst (replaceString));
					matcher = getPattern ().matcher (source);
				} else {
					String link = matcher.group (1);
					MContainer thisContainer = MContainer.getByName (getCtx(), link, CM_WebProject_ID, null);
					if (thisContainer==null) 
						thisContainer = MContainer.getByTitle(getCtx(), link, CM_WebProject_ID, null);
					String replaceURL = "/index.html";
					if (thisContainer != null)
					{
						if (matcher.groupCount ()>1)
							replaceURL = "<a href=\"" + thisContainer.getRelativeURL () + "\">" + matcher.group (2) + "</a>";
						else
							replaceURL = "<a href=\"" + thisContainer.getRelativeURL () + "\">" + matcher.group (1) + "</a>";
					}
					source = new StringBuffer(matcher.replaceFirst (replaceURL));
					matcher = getPattern ().matcher (source);
				}
			}
			
		} 
		return source;
	}
}	//	MWikiToken
