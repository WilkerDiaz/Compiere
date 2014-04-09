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

import org.apache.commons.net.ftp.*;
import org.compiere.util.*;

/**
 * 	Media Server Model
 *	
 *  @author Yves Sandfort
 */
public class MMediaServer extends X_CM_Media_Server
{
    /** Logger for class MMediaServer */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MMediaServer.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Media Server
	 *	@param project
	 *	@return server list
	 */
	public static MMediaServer[] getMediaServer (MWebProject project)
	{
		ArrayList<MMediaServer> list = new ArrayList<MMediaServer>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM CM_Media_Server WHERE CM_WebProject_ID=? ORDER BY CM_Media_Server_ID";
		try
		{
			pstmt = DB.prepareStatement (sql, project.get_Trx());
			pstmt.setInt (1, project.getCM_WebProject_ID());
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				list.add (new MMediaServer (project.getCtx(), rs, project.get_Trx()));
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
		MMediaServer[] retValue = new MMediaServer[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getMediaServer
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MMediaServer.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param CM_Media_Server_ID id
	 *	@param trx transaction
	 */
	public MMediaServer (Ctx ctx, int CM_Media_Server_ID, Trx trx)
	{
		super (ctx, CM_Media_Server_ID, trx);
	}	//	MMediaServer

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs request 
	 *	@param trx transaction
	 */
	public MMediaServer (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MMediaServer
	
	/**  MWebProject WebProject (parent) */
	public MWebProject m_project = null;
	
	/**
	 * 	getWebProject
	 *	@return Webproject (Parent)
	 */
	public MWebProject getWebProject()
	{
		if (m_project==null) 
			m_project = new MWebProject(getCtx(), getCM_WebProject_ID (), get_Trx ());
		return m_project;
	}
	
	/**
	 * 	reDeployAll set all media items to redeploy
	 */
	public void reDeployAll () 
	{
		MMedia[] media = MMedia.getMedia (getWebProject());
		if (media!=null && media.length>0)
		{
			for (MMedia element : media) {
				MMediaDeploy thisDeploy = MMediaDeploy.getByMedia (getCtx(), element.get_ID (), get_ID(), true, null);
				if (thisDeploy.isDeployed ())
				{
					log.log (Level.FINE, "Reset Deployed Flag on MediaItem" + element.get_ID());
					thisDeploy.setIsDeployed (false);
					thisDeploy.save ();
				}
			}
		}
	}
	
	/**
	 * 	(Re-)Deploy all media
	 * 	@return true if deployed
	 */
	public boolean deploy ()
	{
		MMedia[] media = MMedia.getMediaToDeploy(getCtx(), this.get_ID (), get_Trx());

		// Check whether the host is our example localhost, we will not deploy locally, but this is no error
		if (this.getIP_Address().equals("127.0.0.1") || this.getName().equals("localhost")) {
			log.warning("You have not defined your own server, we will not really deploy to localhost!");
			return true;
		}

		FTPClient ftp = new FTPClient();
		try
		{
			ftp.connect (getIP_Address());
			if (ftp.login (getUserName(), getPassword()))
				log.info("Connected to " + getIP_Address() + " as " + getUserName());
			else
			{
				log.warning("Could NOT connect to " + getIP_Address() + " as " + getUserName());
				return false;
			}
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Could NOT connect to " + getIP_Address() 
				+ " as " + getUserName(), e);
			return false;
		}

		boolean success = true;
		String cmd = null;
		//	List the files in the directory
		try
		{
			cmd = "cwd";
			ftp.changeWorkingDirectory (getFolder());
			//
			cmd = "list";
			String[] fileNames = ftp.listNames();
			log.log(Level.FINE, "Number of files in " + getFolder() + ": " + fileNames.length);
			
			/*
			FTPFile[] files = ftp.listFiles();
			log.config("Number of files in " + getFolder() + ": " + files.length);
			for (int i = 0; i < files.length; i++)
				log.fine(files[i].getTimestamp() + " \t" + files[i].getName());*/
			//
			cmd = "bin";
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			//
			for (int i = 0; i < media.length; i++)
			{
				MMediaDeploy thisDeployment = MMediaDeploy.getByMedia (getCtx(), media[i].get_ID (), this.get_ID (), false, get_Trx());
				if (!media[i].isSummary() && media[i].getMediaType ()!=null) {
					log.log(Level.INFO, " Deploying Media Item: " + media[i].toString());
					MImage thisImage = media[i].getImage();
		
					// Open the file and output streams
					byte[] buffer = thisImage.getData();
					ByteArrayInputStream is = new ByteArrayInputStream(buffer);

					String fileName = media[i].get_ID() + media[i].getExtension();
					cmd = "put " + fileName;
					ftp.storeFile(fileName, is);
					is.close();
					thisDeployment.setIsDeployed (true);
					thisDeployment.save ();
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, cmd, e);
			success = false;
		}
		//	Logout from the FTP Server and disconnect
		try
		{
			cmd = "logout";
			ftp.logout();
			log.log(Level.FINE, " FTP logged out");
			cmd = "disconnect";
			ftp.disconnect();
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, cmd, e);
		}
		ftp = null;
		return success;
	}	//	deploy
	
	/**
	 * 	(Re-)Deploy all media
	 * 	@return true if deployed
	 */
	public boolean deleteMediaItem (MMedia t_media)
	{
		// Check whether the host is our example localhost, we will not deploy locally, but this is no error
		if (this.getIP_Address().equals("127.0.0.1") || this.getName().equals("localhost")) {
			log.warning("You have not defined your own server, we will not really deploy to localhost!");
			return true;
		}

		FTPClient ftp = new FTPClient();
		try
		{
			ftp.connect (getIP_Address());
			if (ftp.login (getUserName(), getPassword()))
				log.info("Connected to " + getIP_Address() + " as " + getUserName());
			else
			{
				log.warning("Could NOT connect to " + getIP_Address() + " as " + getUserName());
				return false;
			}
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Could NOT connect to " + getIP_Address() 
				+ " as " + getUserName(), e);
			return false;
		}

		boolean success = true;
		String cmd = null;
		//	List the files in the directory
		try
		{
			cmd = "cwd";
			ftp.changeWorkingDirectory (getFolder());
			//
			if (!t_media.isSummary()) {
				log.log(Level.INFO, " Deleting Media Item:" + t_media.get_ID() + t_media.getExtension());
				ftp.dele (t_media.get_ID() + t_media.getExtension());
			}
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, cmd, e);
			success = false;
		}
		//	Logout from the FTP Server and disconnect
		try
		{
			cmd = "logout";
			ftp.logout();
			cmd = "disconnect";
			ftp.disconnect();
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, cmd, e);
		}
		ftp = null;
		return success;
	}	//	deploy
	
	@Override
	public String toString() 
	{
		StringBuffer sb = new StringBuffer ("MMediaServer[")
		.append (get_ID()).append ("-").append (getName()).append ("]");
		return sb.toString ();
	}
}	//	MMediaServer
