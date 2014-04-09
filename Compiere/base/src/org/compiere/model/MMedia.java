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

import org.compiere.util.*;

/**
 * 	Web Media Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MMedia.java,v 1.7 2006/07/30 00:51:02 jjanke Exp $
 */
public class MMedia extends X_CM_Media
{
    /** Logger for class MMedia */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMedia.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Get Media
	 *	@param project
	 *	@return server list
	 */
	public static MMedia[] getMedia (MWebProject project)
	{
		ArrayList<MMedia> list = new ArrayList<MMedia>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CM_Media WHERE CM_WebProject_ID=? ORDER BY CM_Media_ID";
		try
		{
			pstmt = DB.prepareStatement (sql, project.get_Trx());
			pstmt.setInt (1, project.getCM_WebProject_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MMedia (project.getCtx(), rs, project.get_Trx()));
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		MMedia[] retValue = new MMedia[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getMedia
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MMedia.class);
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param CM_Media_ID id
	 *	@param trx transaction
	 */
	public MMedia (Ctx ctx, int CM_Media_ID, Trx trx)
	{
		super (ctx, CM_Media_ID, trx);
	}	//	MMedia

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MMedia (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MMedia
	
	/** Web Project			*/
	private MWebProject 	m_project = null;
	
	/**
	 * 	Get Media to deploy
	 *  @param ctx Corresponding Context
	 *  @param CM_Media_Server_ID Identify Server
	 *  @param trx Transaction ID
	 *	@return server list of media Items to deploy
	 */
	public static MMedia[] getMediaToDeploy (Ctx ctx, int CM_Media_Server_ID, Trx trx)
	{
		ArrayList<MMedia> list = new ArrayList<MMedia>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT CMM.* FROM CM_Media CMM, CM_MediaDeploy CMMD WHERE " +
				"CMM.CM_Media_ID = CMMD.CM_Media_ID AND " +
				"CMMD.CM_Media_Server_ID = ? AND " +
				"CMMD.IsDeployed='N' " +
				"ORDER BY CMM.CM_Media_ID";
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, CM_Media_Server_ID);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MMedia (ctx, rs, trx));
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		MMedia[] retValue = new MMedia[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getMedia
	

	
	/**
	 * 	get Container by Name
	 *	@param ctx
	 *  @param Name 
	 *	@param CM_WebProject_Id
	 *	@param trx
	 *	@return Container or null if not found
	 */
	public static MMedia getByName(Ctx ctx, String Name, int CM_WebProject_Id, Trx trx) {
		MMedia thisMedia = null;
		String sql = "SELECT * FROM CM_Media WHERE (LOWER(Name) LIKE ?) AND CM_WebProject_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			pstmt.setString (1,Name.toLowerCase ());
			pstmt.setInt(2, CM_WebProject_Id);
			rs = pstmt.executeQuery();
			if (rs.next())
				thisMedia = (new MMedia(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return thisMedia;
	}
	
	/**
	 * 	Get Web Project
	 *	@return web project
	 */
	public MWebProject getWebProject()
	{
		if (m_project == null)
			m_project = MWebProject.get(getCtx(), getCM_WebProject_ID());
		return m_project;
	}	//	getWebProject
	
	/**
	 * 	Get AD_Tree_ID
	 *	@return tree
	 */
	public int getAD_Tree_ID()
	{
		return getWebProject().getAD_TreeCMM_ID();
	}	//	getAD_Tree_ID;
	
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (isSummary())
		{
			setMediaType(null);
			setAD_Image_ID(0);
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save.
	 * 	Insert
	 * 	- create tree
	 *	@param newRecord insert
	 *	@param success save success
	 *	@return true if saved
	 */
	@Override
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		if (!success)
			return success;
		if (newRecord)
		{
			StringBuffer sb = new StringBuffer ("INSERT INTO AD_TreeNodeCMM "
				+ "(AD_Client_ID,AD_Org_ID, IsActive,Created,CreatedBy,Updated,UpdatedBy, "
				+ "AD_Tree_ID, Node_ID, Parent_ID, SeqNo) "
				+ "VALUES (?,0, 'Y', SysDate, 0, SysDate, 0,?,?, 0, 999)");
			Object[] params = new Object[]{getAD_Client_ID(),getAD_Tree_ID(),get_ID()};
			int no = DB.executeUpdate(get_Trx(), sb.toString(),params);
			if (no > 0)
				log.fine("#" + no + " - TreeType=CMM");
			else
				log.warning("#" + no + " - TreeType=CMM");
			return no > 0;
		}
		// Construct / Update Deployment Procedure
		MMediaServer[] theseServers = MMediaServer.getMediaServer (m_project);
		if (theseServers!=null && theseServers.length>0)
			for (MMediaServer element : theseServers) {
				MMediaDeploy thisDeploy = MMediaDeploy.getByMedia (getCtx(), get_ID(), element.get_ID(), true, get_Trx());
				if (thisDeploy.isDeployed ())
				{
					thisDeploy.setIsDeployed (false);
					thisDeploy.save ();
				}
			}
		return success;
	}	//	afterSave
	
	/**
	 * 	Return String representation
	 *	@see org.compiere.model.X_CM_Media#toString()
	 *	@return Info
	 */
	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer ("MMedia[ID=")
		.append(get_ID())
		.append(",FileName=").append(get_ID() + getExtension())
		.append ("]");
		return sb.toString ();
	}
	
	@Override
	protected boolean beforeDelete()
	{
		// Delete from Deployment Server
		MMediaDeploy [] theseDeployers = MMediaDeploy.getByMediaAndProject (getCtx(), get_IDOld(), m_project, false, get_Trx());
		if (theseDeployers!=null && theseDeployers.length>0)
			for (int i=0;i<theseDeployers.length;i++)
				if (!theseDeployers[i].getServer().deleteMediaItem (this)) 
					log.warning ("Could not delete file + " + this.toString () + " from Server: " + theseDeployers[i].getServer ());
		// Delete From MMediaDeploy
		StringBuffer sb = new StringBuffer ("DELETE FROM CM_MediaDeploy ")
			.append(" WHERE CM_Media_ID=? ");
		int no = DB.executeUpdate(get_Trx(), sb.toString(),get_IDOld());
		if (no > 0)
			log.fine("#" + no + " - CM_MediaDeploy");
		else
			log.warning("#" + no + " - CM_MediaDeploy");

		return true;
	}
	
	/**
	 * 	After Delete
	 *	@param success
	 *	@return deleted
	 */
	@Override
	protected boolean afterDelete (boolean success)
	{
		if (!success)
			return success;
		// Delete from tree
		StringBuffer sb = new StringBuffer ("DELETE FROM AD_TreeNodeCMM ")
			.append(" WHERE Node_ID= ? ")
			.append(" AND AD_Tree_ID= ? ");
		Object[] params = new Object[]{get_IDOld(),getAD_Tree_ID()};
		int no = DB.executeUpdate(get_Trx(), sb.toString(),params);
		if (no > 0)
			log.fine("#" + no + " - TreeType=CMM");
		else
			log.warning("#" + no + " - TreeType=CMM");
		return no > 0;
	}	//	afterDelete

	/**
	 * 	Get File Name
	 *	@return file name return ID
	 */
	public String getFileName()
	{
		return get_ID() + getExtension();
	}	//	getFileName
	
	/**
	 * 	Get Extension with .
	 *	@return extension
	 */
	public String getExtension()
	{
		String mt = getMediaType();
		if (MEDIATYPE_ApplicationPdf.equals(mt))
			return ".pdf";
		if (MEDIATYPE_ImageGif.equals(mt))
			return ".gif";
		if (MEDIATYPE_ImageJpeg.equals(mt))
			return ".jpg";
		if (MEDIATYPE_ImagePng.equals(mt))
			return ".png";
		if (MEDIATYPE_TextCss.equals(mt))
			return ".css";
		if (MEDIATYPE_TextJs.equals (mt))
			return ".js";
		//	Unknown
		return ".dat";
	}	//	getExtension

	/**
	 * 	Get Image
	 *	@return image or null
	 */
	public MImage getImage()
	{
		if (getAD_Image_ID() != 0)
			return MImage.get(getCtx(), getAD_Image_ID());
		return null;
	}	//	getImage
	
	/**
	 * 	Get Data as byte array
	 *	@return data or null
	 */
	public byte[] getData()
	{
		MImage image = getImage();
		if (image != null)
		{
			byte[] data = image.getData();
			if (data == null || data.length == 0)
				log.config("No Image Data");
		}
		
		//	Attachment
		MAttachment att = getAttachment();
		if (att == null || att.getEntryCount() == 0)
		{
			log.config("No Attachment");
			return null;
		}
		if (att.getEntryCount() > 1)
			log.warning(getName() + " - more then one attachment - " + att.getEntryCount());
		//
		MAttachmentEntry entry = att.getEntry(0);
		if (entry == null)
		{
			log.config("No Attachment Entry");
			return null;
		}
		byte[] buffer = entry.getData();
		if (buffer == null || buffer.length == 0)
		{
			log.config("No Attachment Entry Data");
			return null;
		}
		return buffer;
	}	//	getData

	/**
	 * 	Get Input Stream
	 *	@return imput stream or null
	 */
	public InputStream getInputStream()
	{
		byte[] buffer = getData();
		ByteArrayInputStream is = new ByteArrayInputStream(buffer);
		return is;
	}	//	getInputStream

	/**
	 * 	Get Updated timestamp of Attachment
	 *	@return updated or null if no attchment
	 */
	public Timestamp getAttachmentUpdated()
	{
		MAttachment att = getAttachment();
		if (att == null)
			return null;
		return att.getUpdated();
	}	//	getAttachmentUpdated
	
}	//	MMedia
