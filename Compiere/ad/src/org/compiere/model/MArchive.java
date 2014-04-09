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
import java.util.zip.*;
import org.compiere.framework.*;
import org.compiere.util.*;


/**
 *	Archive Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MArchive.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class MArchive extends X_AD_Archive
{
    /** Logger for class MArchive */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MArchive.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Archives
	 *	@param ctx context
	 *	@param whereClause optional where clause (starting with AND)
	 *	@return archives
	 */
	public static MArchive[] get (Ctx ctx, String whereClause)
	{
		ArrayList<MArchive> list = new ArrayList<MArchive>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM AD_Archive WHERE AD_Client_ID=?";
		if (whereClause != null && whereClause.length() > 0)
			sql += whereClause;
		sql += " ORDER BY Created";
		
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, ctx.getAD_Client_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add (new MArchive(ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		if (list.size() == 0)
			s_log.fine(sql);
		else
			s_log.finer(sql);
		//
		MArchive[] retValue = new MArchive[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	get

	/**	Logger			*/
	private static CLogger s_log = CLogger.getCLogger(MArchive.class);
	
	private Integer		m_inflated = null;
	private Integer		m_deflated = null;
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Archive_ID id
	 *	@param trx transaction
	 */
	public MArchive (Ctx ctx, int AD_Archive_ID, Trx trx)
	{
		super (ctx, AD_Archive_ID, trx);
	}	//	MArchive

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MArchive (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MArchive

	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param info print info
	 *	@param trx transaction
	 */
	public MArchive (Ctx ctx, PrintInfo info, Trx trx)
	{
		this (ctx, 0, trx);
		setName(info.getName());
		setIsReport(info.isReport());
		//
		setAD_Process_ID(info.getAD_Process_ID());
		setAD_Table_ID(info.getAD_Table_ID());
		setRecord_ID(info.getRecord_ID());
		setC_BPartner_ID(info.getC_BPartner_ID());
	}	//	MArchive

	
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MArchive[");
		sb.append(get_ID()).append(",Name=").append(getName());
		if (m_inflated != null)
			sb.append(",Inflated=" + m_inflated);
		if (m_deflated != null)
			sb.append(",Deflated=" + m_deflated);
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Get Binary Data.
	 * 	(inflate)
	 *	@return inflated data
	 */
	@Override
	public byte[] getBinaryData()
	{
		byte[] deflatedData = super.getBinaryData();
		m_deflated = null;
		m_inflated = null;
		if (deflatedData == null)
			return null;
		//
		log.fine("ZipSize=" + deflatedData.length);
		m_deflated = Integer.valueOf(deflatedData.length);
		if (deflatedData.length == 0)
			return null;

		byte[] inflatedData = null;
		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(deflatedData);
			ZipInputStream zip = new ZipInputStream (in);
			ZipEntry entry = zip.getNextEntry();
			if (entry != null)	//	just one entry
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[2048];
				int length = zip.read(buffer);
				while (length != -1)
				{
					out.write(buffer, 0, length);
					length = zip.read(buffer);
				}
				//
				inflatedData = out.toByteArray();
				log.fine("Size=" + inflatedData.length + " - zip="
					+ entry.getCompressedSize() + "(" + entry.getSize() + ") "
					+ (entry.getCompressedSize()*100/entry.getSize())+ "%");
				m_inflated = Integer.valueOf(inflatedData.length);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			inflatedData = null;
		}
		return inflatedData;
	}	//	getBinaryData
	
	/**
	 * 	Get Data as Input Stream
	 *	@return input stream or null
	 */
	public InputStream getInputStream()
	{
		byte[] inflatedData = getBinaryData();
		if (inflatedData == null)
			return null;
		return new ByteArrayInputStream(inflatedData);
	}	//	getInputStream
	
	/**
	 * 	Save Binary Data.
	 * 	(deflate)
	 *	@param inflatedData inflated data 
	 */
	@Override
	public void setBinaryData (byte[] inflatedData)
	{
		if (inflatedData == null || inflatedData.length == 0)
			throw new IllegalArgumentException("InflatedData is NULL");
		m_inflated = Integer.valueOf(inflatedData.length);
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		ZipOutputStream zip = new ZipOutputStream(out);
		zip.setMethod(ZipOutputStream.DEFLATED);
		zip.setLevel(Deflater.BEST_COMPRESSION);
		zip.setComment("compiere");
		//
		byte[] deflatedData = null;
		try
		{
			ZipEntry entry = new ZipEntry("CompiereArchive");
			entry.setTime(System.currentTimeMillis());
			entry.setMethod(ZipEntry.DEFLATED);
			zip.putNextEntry(entry);
			zip.write (inflatedData, 0, inflatedData.length);
			zip.closeEntry();
			log.fine(entry.getCompressedSize() + " (" + entry.getSize() + ") "
					+ (entry.getCompressedSize()*100/entry.getSize())+ "%");
			//
		//	zip.finish();
			zip.close();
			deflatedData = out.toByteArray();
			log.fine("Length=" +  inflatedData.length);
			m_deflated = Integer.valueOf(deflatedData.length);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "saveLOBData", e);
			deflatedData = null;
			m_deflated = null;
		}
		super.setBinaryData (deflatedData);
	}	//	setBinaryData
	
	/**
	 * 	Get Created By (User) Name
	 *	@return name
	 */
	public String getCreatedByName()
	{
		String name = "?";
		String sql = "SELECT Name FROM AD_User WHERE AD_User_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, getCreatedBy());
			rs = pstmt.executeQuery ();
			if (rs.next ())
				name = rs.getString(1);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return name;
	}	//	getCreatedByName
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		//	Binary Data is Mandatory
		byte[] data = super.getBinaryData();
		if (data == null || data.length == 0)
			return false;
		//
		log.fine(toString());
		return true;
	}	//	beforeSave
	
}	//	MArchive
