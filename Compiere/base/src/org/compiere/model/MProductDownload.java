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
import java.net.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;


/**
 * 	Product Download Model
 *  @author Jorg Janke
 *  @version $Id: MProductDownload.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MProductDownload extends X_M_ProductDownload
{
    /** Logger for class MProductDownload */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MProductDownload.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Migrate Download URLs (2.5.2c)
	 *	@param ctx context
	 */
	public static void migrateDownloads (Ctx ctx)
	{
		String sql = "SELECT COUNT(*) FROM M_ProductDownload";
		int no = QueryUtil.getSQLValue(null, sql);
		if (no > 0)
			return;
		//
		int count = 0;
		sql = "SELECT AD_Client_ID, AD_Org_ID, M_Product_ID, Name, DownloadURL "
			+ "FROM M_Product "
			+ "WHERE DownloadURL IS NOT NULL";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				int AD_Client_ID = rs.getInt(1);
				int AD_Org_ID = rs.getInt(2);
				int M_Product_ID = rs.getInt(3);
				String Name = rs.getString(4);
				String DownloadURL = rs.getString(5);
				//
				MProductDownload pdl = new MProductDownload(ctx, 0, null);
				pdl.setClientOrg(AD_Client_ID, AD_Org_ID);
				pdl.setM_Product_ID(M_Product_ID);
				pdl.setName(Name);
				pdl.setDownloadURL(DownloadURL);
				if (pdl.save())
				{
					count++;
					String sqlUpdate = "UPDATE M_Product SET DownloadURL = NULL WHERE M_Product_ID=?";
					int updated = DB.executeUpdate((Trx) null, sqlUpdate,M_Product_ID);
					if (updated != 1)
						s_log.warning("Product not updated");
				}
				else
					s_log.warning("Product Download not created M_Product_ID=" + M_Product_ID);
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		s_log.info("#" + count);
	}	//	migrateDownloads
	
	/**
	 * 	Get Product Download from Cache
	 *	@param ctx context
	 *	@param M_ProductDownload_ID id
	 *	@return MProductDownload
	 */
	public static MProductDownload get(Ctx ctx, int M_ProductDownload_ID)
	{
		Integer key = Integer.valueOf (M_ProductDownload_ID);
		MProductDownload retValue = s_cache.get (ctx, key);
		if (retValue != null)
			return retValue;
		retValue = new MProductDownload (ctx, M_ProductDownload_ID, null);
		if (retValue.get_ID () != 0)
			s_cache.put (key, retValue);
		return retValue;
	}	//	get

	/**	Cache						*/
	private static final CCache<Integer, MProductDownload> s_cache 
		= new CCache<Integer, MProductDownload> ("M_ProductDownload", 20);
	
	/**	Logger	*/
	private static final CLogger	s_log	= CLogger.getCLogger (MProductDownload.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_ProductDownload_ID id
	 *	@param trx p_trx
	 */
	public MProductDownload (Ctx ctx, int M_ProductDownload_ID,
		Trx trx)
	{
		super (ctx, M_ProductDownload_ID, trx);
		if (M_ProductDownload_ID == 0)
		{
		//	setM_Product_ID (0);
		//	setName (null);
		//	setDownloadURL (null);
		}
	}	//	MProductDownload

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MProductDownload (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MProductDownload
	
	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MProductDownload[")
			.append(get_ID())
			.append(",M_Product_ID=").append(getM_Product_ID())
			.append(",").append(getDownloadURL())
			.append ("]");
		return sb.toString ();
	}	//	toString
	
	
	/**
	 * 	Get Download Name
	 *	@return download name (last part of name)
	 */
	public String getDownloadName()
	{
		String url = getDownloadURL();
		if (url == null || !isActive())
			return null;
		int pos = Math.max(url.lastIndexOf('/'), url.lastIndexOf('\\'));
		if (pos != -1)
			return url.substring(pos+1);
		return url;
	}	//	getDownloadName


	/**
	 * 	Get Download URL
	 * 	@param directory optional directory
	 *	@return url
	 */
	public URL getDownloadURL (String directory)
	{
		String dl_url = getDownloadURL();
		if (dl_url == null || !isActive())
			return null;

		URL url = null;
		try
		{
			if (dl_url.indexOf ("://") != -1)
				url = new URL (dl_url);
			else
			{
				File f = getDownloadFile (directory);
				if (f != null)
					url = f.toURI().toURL ();
			}
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, dl_url, ex);
			return null;
		}
		return url;
	}	//	getDownloadURL

	
	/**
	 * 	Find download url
	 *	@param directory optional directory
	 *	@return file or null
	 */
	public File getDownloadFile (String directory)
	{
		File file = new File (getDownloadURL());	//	absolute file
		if (file.exists())
			return file;
		if (directory == null || directory.length() == 0)
		{
			log.log(Level.WARNING, "Not found " + getDownloadURL());
			return null;
		}
		String downloadURL2 = directory;
		if (!downloadURL2.endsWith(File.separator))
			downloadURL2 += File.separator;
		downloadURL2 += getDownloadURL();
		file = new File (downloadURL2);
		if (file.exists())
			return file;

		log.log(Level.WARNING, "Not found " + getDownloadURL() + " / " + downloadURL2);
		return null;
	}	//	getDownloadFile

	/**
	 * 	Get Download Stream
	 * 	@param directory optional directory
	 *	@return input stream
	 */
	public InputStream getDownloadStream (String directory)
	{
		String dl_url = getDownloadURL();
		if (dl_url == null || !isActive())
			return null;

		InputStream in = null;
		try
		{
			if (dl_url.indexOf ("://") != -1)
			{
				URL url = new URL (dl_url);
				in = url.openStream();
			}
			else //	file
			{
				File file = getDownloadFile(directory);
				if (file == null)
					return null;
				in = new FileInputStream (file);
			}
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, dl_url, ex);
			return null;
		}
		return in;
	}	//	getDownloadStream
	
	/**
	 * 	Save Download in Attachment
	 *	@param entry attachment entry
	 *	@return true if saved
	 */
	public boolean saveDownload (MAttachmentEntry entry)
	{
		String dl_url = getDownloadURL();
		if (dl_url == null || !isActive())
		{
			log.info("No Attachment");
			return false;
		}

		File outFile = null;
		FileOutputStream out = null;
		try
		{
			if (dl_url.indexOf ("://") != -1)
			{
				URL url = new URL (dl_url);
				outFile = new File(url.toURI());
			}
			else //	file
			{
				MClient client = MClient.get(getCtx());
				String directory = client.getDocumentDir();
				if (directory != null && directory.length() > 0)
				{
					File dir = new File(directory);
					if (!dir.exists() && !dir.mkdir())
					{
						log.warning("Cannot create Directory: " + directory);
						return false;
					}
					outFile = new File(directory, dl_url);
				}
				else
					outFile = new File(dl_url);
			}
			if (outFile.exists())
			{
				boolean success = outFile.delete();
				if( !success )
					log.warning("Failed to delete file: " + outFile.toString());
			}
			out = new FileOutputStream(outFile);
			byte[] data = entry.getData(); 
			out.write(data);
			out.flush();
			out.close();
			log.info(outFile.toString() + " -> " + (data.length/1024) + "kB");
		}
		catch (Exception ex)
		{
			log.log(Level.WARNING, dl_url, ex);
			return false;
		}
		return true;
	}	//	saveDownload
	
}	//	MProductDownload
