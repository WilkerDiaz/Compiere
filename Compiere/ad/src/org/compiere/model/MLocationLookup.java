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

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Address Loaction Lookup Model.
 *
 *  @author 	Jorg Janke
 *  @version 	$Id: MLocationLookup.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public final class MLocationLookup extends Lookup
	implements Serializable
{
    /** Logger for class MLocationLookup */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLocationLookup.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param WindowNo window no (to derive AD_Client/Org for new records)
	 */
	public MLocationLookup(Ctx ctx, int WindowNo)
	{
		super (ctx, WindowNo, DisplayTypeConstants.TableDir);
	}	//	MLocation

	/**
	 *	Get Display for Value (not cached)
	 *  @param value Location_ID
	 *  @return String Value
	 */
	@Override
	public String getDisplay (Object value)
	{
		if (value == null)
			return null;
		MLocation loc = getLocation (value, null);
		if (loc == null)
			return "<" + value.toString() + ">";
		return loc.toString();
	}	//	getDisplay

	/**
	 *	Get Object of Key Value
	 *  @param value value
	 *  @return Object or null
	 */
	@Override
	public NamePair get (Object value)
	{
		if (value == null)
			return null;
		MLocation loc = getLocation (value, null);
		if (loc == null)
			return null;
		return new KeyNamePair (loc.getC_Location_ID(), loc.toString());
	}	//	get

	/**
	 *  The Lookup contains the key 
	 *  @param key Location_ID
	 *  @return true if key known
	 */
	@Override
	public boolean containsKey (Object key)
	{
		return getLocation(key, null) == null;
	}   //  containsKey

	
	/**************************************************************************
	 * 	Get Location
	 * 	@param key ID as string or integer
	 *	@param trx transaction
	 * 	@return Location
	 */
	public MLocation getLocation (Object key, Trx trx)
	{
		if (key == null)
			return null;
		int C_Location_ID = 0;
		if (key instanceof Integer)
			C_Location_ID = ((Integer)key).intValue();
		else if (key != null)
			C_Location_ID = Integer.parseInt(key.toString());
		//
		return getLocation(C_Location_ID, trx);
	}	//	getLocation
	
	/**
	 * 	Get Location
	 * 	@param C_Location_ID id
	 *	@param trx transaction
	 * 	@return Location
	 */
	public MLocation getLocation (int C_Location_ID, Trx trx)
	{
		return MLocation.get(getCtx(), C_Location_ID, trx);
	}	//	getC_Location_ID

	/**
	 *	Get underlying fully qualified Table.Column Name.
	 *	Used for VLookup.actionButton (Zoom)
	 *  @return column name
	 */
	@Override
	public String getColumnName()
	{
		return "C_Location_ID";
	}   //  getColumnName

	/**
	 *	Return data as sorted Array - not implemented
	 *  @param mandatory mandatory
	 *  @param onlyValidated only validated
	 *  @param onlyActive only active
	 * 	@param temporary force load for temporary display
	 *  @return null
	 */
	@Override
	public ArrayList< NamePair > getData( boolean mandatory, boolean onlyValidated, boolean onlyActive, boolean temporary )
	{

		ArrayList< NamePair > list = new ArrayList< NamePair >();
		if( !mandatory )
			list.add( new KeyNamePair( -1, "" ) );
		//
		int count = 0;
		StringBuffer sql = new StringBuffer(
				"SELECT C_Location_ID from C_Location WHERE AD_Client_ID = ? AND (AD_Org_ID = 0 OR ? = 0)" );
		if( onlyActive )
			sql.append( " AND IsActive='Y'" );
		sql.append( " ORDER BY 1" );
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement( sql.toString(), (Trx) null);
			pstmt.setInt( 1, getCtx().getAD_Client_ID( m_WindowNo ) );
			pstmt.setInt( 2, getCtx().getAD_Org_ID( m_WindowNo ) );
			rs = pstmt.executeQuery();
			while( rs.next() )
			{
				list.add( get( rs.getInt( 1 ) ) );
				if(++count > MLookup.MAX_ROWS) {
					log.warning("Too many location data, only retrieve "+MLookup.MAX_ROWS);
					break;
				}
			}
		}
		catch( SQLException e ) {
			log.log( Level.SEVERE, sql.toString(), e );
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		// Sort & return
		return list;
	}   //  getArray

}	//	MLocation
