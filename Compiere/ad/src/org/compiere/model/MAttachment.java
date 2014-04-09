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
import java.util.*;
import java.util.logging.*;
import java.util.zip.*;

import org.compiere.framework.*;
import org.compiere.util.*;

/**
 *	Attachment Model.
 *	One Attachment can have multiple entries
 *
 *  @author Jorg Janke
 *  @version $Id: MAttachment.java 8753 2010-05-12 17:34:49Z nnayak $
 */
public class MAttachment extends X_AD_Attachment
{
    /** Logger for class MAttachment */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttachment.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Attachment
	 *	@param ctx context
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@return attachment or null
	 */
	public static MAttachment get (Ctx ctx, int AD_Table_ID, int Record_ID)
	{
		MAttachment retValue = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM AD_Attachment WHERE AD_Table_ID=? AND Record_ID=?";
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt (1, AD_Table_ID);
			pstmt.setInt (2, Record_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MAttachment (ctx, rs, null);
		}
		catch (Exception e) {
			s_log.log(Level.SEVERE, sql, e);
		}
        finally {
        	DB.closeResultSet(rs);
        	DB.closeStatement(pstmt);
        }

        return retValue;
	}	//	get


	/**
	 * 	Add Attachment Entry Reference for PO
	 *	@param po persistent object
	 *	@param uri url or file reference
	 *	@return true if reference added
	 */
	public static boolean addEntry (PO po, URI uri)
	{
		if (po == null || uri == null)
			return false;
		MAttachment attachment = get(po.getCtx(), po.get_Table_ID(), po.get_ID());
		if (attachment == null)
		{
			attachment = new MAttachment(po.getCtx(), po.get_Table_ID(), po.get_ID(), po.get_Trx());
			if (!attachment.save())
				return false;
		}
		boolean success = attachment.addEntry(uri);
		if (success)
			return attachment.save();
		return false;
	}	//	addEntry

	/**
	 * 	Add Attachment Entry for PO
	 *	@param po persistent object
	 *	@param file file to be loaded
	 *	@return true if file loaded and added
	 */
	public static boolean addEntry (PO po, File file)
	{
		if (po == null || file == null)
			return false;
		MAttachment attachment = get(po.getCtx(), po.get_Table_ID(), po.get_ID());
		if (attachment == null)
		{
			attachment = new MAttachment(po.getCtx(), po.get_Table_ID(), po.get_ID(), po.get_Trx());
			if (!attachment.save())
				return false;
		}
		boolean success = attachment.addEntry(file);
		if (success)
			return attachment.save();
		return false;
	}	//	addEntry

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MAttachment.class);


	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param AD_Attachment_ID id
	 *	@param trx transaction
	 */
	public MAttachment(Ctx ctx, int AD_Attachment_ID, Trx trx)
	{
		super (ctx, AD_Attachment_ID, trx);
	}	//	MAttachment

	/**
	 * 	New Constructor
	 *	@param ctx context
	 *	@param AD_Table_ID table
	 *	@param Record_ID record
	 *	@param trx transaction
	 */
	public MAttachment(Ctx ctx, int AD_Table_ID, int Record_ID, Trx trx)
	{
		this (ctx, 0, trx);
		setAD_Table_ID (AD_Table_ID);
		setRecord_ID (Record_ID);
	}	//	MAttachment

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MAttachment(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAttachment

	/** Indicator for no data   */
	public static final String 	NONE = ".";
	/** Indicator for zip data  */
	public static final String 	ZIP = "zip";

	/**	List of Entry Data		*/
	private ArrayList<MAttachmentEntry> m_items = null;

	/**
	 * 	Set Client Org
	 *	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 */
	@Override
	public void setClientOrg(int AD_Client_ID, int AD_Org_ID)
	{
		super.setClientOrg(AD_Client_ID, AD_Org_ID);
	}	//	setClientOrg

	/**
	 * 	Add to Text Msg
	 *	@param added text
	 */
	public void addTextMsg (String added)
	{
		String oldTextMsg = getTextMsg();
		if (oldTextMsg == null)
			setTextMsg (added);
		else if (added != null)
			setTextMsg (oldTextMsg + added);
	}	//	addTextMsg

	/**
	 * 	Get Text Msg
	 *	@return trimmed message
	 */
	@Override
	public String getTextMsg ()
	{
		String msg = super.getTextMsg ();
		if (msg == null)
			return null;
		return msg.trim();
	}	//	setTextMsg

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MAttachment[");
		sb.append(getAD_Attachment_ID()).append(",Title=").append(getTitle())
			.append(",Entries=").append(getEntryCount());
		for (int i = 0; i < getEntryCount(); i++)
		{
			if (i == 0)
				sb.append(":");
			else
				sb.append(",");
			sb.append(getEntryName(i));
		}
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Add new Data Entry
	 *	@param file file
	 *	@return true if added
	 */
	public boolean addEntry (File file)
	{
		if (file == null)
		{
			log.warning("No File");
			return false;
		}
		if (!file.exists() || file.isDirectory())
		{
			log.warning("Not added - " + file
				+ ", Exists=" + file.exists() + ", Directory=" + file.isDirectory());
			return false;
		}
		//
		byte[] data = null;
		try
		{
			FileInputStream fis = new FileInputStream (file);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024*8];   //  8kB
			int length = -1;
			while ((length = fis.read(buffer)) != -1)
				os.write(buffer, 0, length);
			fis.close();
			data = os.toByteArray();
			os.close();
		}
		catch (IOException ioe)
		{
			log.log(Level.SEVERE, "(file)", ioe);
		}
		//
		String name = file.getName();
		int index = name.lastIndexOf("/");
		if (index > 0)
			name = name.substring(index+1);
		index = name.lastIndexOf("\\");
		if (index > 0)
			name = name.substring(index+1);
		return addEntry (name, data);
	}	//	addEntry

	/**
	 * 	Add URI Entry
	 *	@param uri uri
	 *	@return true if added
	 */
	public boolean addEntry(URI uri)
	{
		if (uri == null)
			return false;
		/* Test *
		String schema = uri.getScheme();
		if (!("file".equals(schema) || schema.startsWith("http")))
		{
			log.warning("Not supported: " + schema);
			return false;
		}	*/
		//	check if exists
		if (MAttachmentEntry.getData(uri) == null)
			return false;

		String name = uri.toString();
		int index = name.lastIndexOf("/");
		if (index > 0)
			name = name.substring(index+1);
		index = name.lastIndexOf("\\");
		if (index > 0)
			name = name.substring(index+1);
		return addEntry(name, uri.toString().getBytes());
	}	//	addEntry

	/**
	 * 	Add new Data Entry
	 *	@param name name
	 *	@param data data
	 *	@return true if added
	 */
	public boolean addEntry (String name, byte[] data)
	{
		if (name == null || data == null)
			return false;
		return addEntry (new MAttachmentEntry (name, data));	//	random index
	}	//	addEntry

	/**
	 * 	Add Entry
	 * 	@param item attachment entry
	 * 	@return true if added
	 */
	public boolean addEntry (MAttachmentEntry item)
	{
		if (item == null)
			return false;
		if (m_items == null)
			loadLOBData();
		boolean retValue = m_items.add(item);
		log.fine(item.toStringX());
		addTextMsg(" ");	//	otherwise not saved
		return retValue;
	}	//	addEntry

	/**
	 * 	Get Attachment Entry
	 * 	@param index index of the item
	 * 	@return Entry or null
	 */
	public MAttachmentEntry getEntry (int index)
	{
		if (m_items == null)
			loadLOBData();
		if (index < 0 || index >= m_items.size())
			return null;
		return m_items.get(index);
	}	//	getEntry

	/**
	 * 	Get Attachment Entries as array
	 * 	@return array or null
	 */
	public MAttachmentEntry[] getEntries ()
	{
		if (m_items == null)
			loadLOBData();
		MAttachmentEntry[] retValue = new MAttachmentEntry[m_items.size()];
		m_items.toArray (retValue);
		return retValue;
	}	//	getEntries

	/**
	 * 	Delete Entry
	 *	@param index index
	 *	@return true if deleted
	 */
	public boolean deleteEntry (int index)
	{
		if (m_items == null)
			loadLOBData();
		if (index >= 0 && index < m_items.size())
		{
			m_items.remove(index);
			addTextMsg(" ");	//	otherwise not saved
			log.config("Index=" + index + " - NewSize=" + m_items.size());
			return true;
		}
		log.warning("Not deleted Index=" + index + " - Size=" + m_items.size());
		return false;
	}	//	deleteEntry

	/**
	 * 	Delete All Entries
	 *	@return true if deleted
	 */
	public boolean deleteAllEntries ()
	{
		if (m_items == null)
			loadLOBData();
		m_items.clear();
		addTextMsg(" ");	//	otherwise not saved
		log.config("NewSize=" + m_items.size());
		return true;
	}	//	deleteEntry

	/**
	 * 	Get Entry Count
	 *	@return number of entries
	 */
	public int getEntryCount()
	{
		if (m_items == null)
			loadLOBData();
		return m_items.size();
	}	//	getEntryCount


	/**
	 * 	Get Entry Name
	 * 	@param index index
	 * 	@return name or null
	 */
	public String getEntryName (int index)
	{
		MAttachmentEntry item = getEntry(index);
		if (item != null)
			return item.getName();
		return null;
	}	//	getEntryName

	/**
	 * 	Dump Entry Names
	 */
	public void dumpEntryNames()
	{
		if (m_items == null)
			loadLOBData();
		if (m_items == null || m_items.size() == 0)
		{
			System.out.println("- no entries -");
			return;
		}
		System.out.println("- entries: " + m_items.size());
		for (int i = 0; i < m_items.size(); i++)
			System.out.println("  - " + getEntryName(i));
	}	//	dumpEntryNames

	/**
	 * 	Get Entry Data
	 * 	@param index index
	 * 	@return data or null
	 */
	public byte[] getEntryData (int index)
	{
		MAttachmentEntry item = getEntry(index);
		if (item != null)
			return item.getData();	// de-reference
		return null;
	}	//	getEntryData

	/**
	 * 	Get Entry File with name
	 * 	@param index index
	 *	@param fileName optional file name
	 *	@return file
	 */
	public File getEntryFile (int index, String fileName)
	{
		MAttachmentEntry item = getEntry(index);
		if (item != null)
			return item.getFile(fileName);
		return null;
	}	//	getEntryFile

	/**
	 * 	Save Entry in File with name
	 * 	@param index index
	 *	@param file file
	 *	@return file
	 */
	public File getEntryFile (int index, File file)
	{
		MAttachmentEntry item = getEntry(index);
		if (item != null)
			return item.getFile(file);
		return null;
	}	//	getEntryFile

	/**
	 * 	Save Entry Data in Zip File format
	 *	@return true if saved
	 */
	private boolean saveLOBData()
	{
		if (m_items == null || m_items.size() == 0)
		{
			setBinaryData(null);
			return true;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(out);
		zip.setMethod(ZipOutputStream.DEFLATED);
		zip.setLevel(Deflater.BEST_COMPRESSION);
		zip.setComment("compiere");
		//
		checkNames();	//	make names unique
		try
		{
			for (int i = 0; i < m_items.size(); i++)
			{
				MAttachmentEntry item = getEntry(i);
				ZipEntry entry = new ZipEntry(item.getName());
				entry.setTime(System.currentTimeMillis());
				entry.setMethod(ZipEntry.DEFLATED);
				zip.putNextEntry(entry);
				byte[] data = item.getData(true);	//	raw
				zip.write (data, 0, data.length);
				zip.closeEntry();
				log.fine(entry.getName() + " - "
					+ entry.getCompressedSize() + " (" + entry.getSize() + ") "
					+ entry.getCompressedSize()*100/entry.getSize()+ "%");
			}
		//	zip.finish();
			zip.close();
			byte[] zipData = out.toByteArray();
			log.fine("Length=" +  zipData.length);
			setBinaryData(zipData);
			return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "saveLOBData", e);
		}
		setBinaryData(null);
		return false;
	}	//	saveLOBData

	/**
	 * 	Making sure that names for ZipEntry name is unique
	 */
	private void checkNames ()
	{
		ArrayList<String> uniqueNames = new ArrayList<String>();
		for (int i = 0; i < m_items.size(); i++)
		{
			MAttachmentEntry item = getEntry(i);
			String name = item.getName();
			while (uniqueNames.contains(name))
			{
				name += "_" + i;
				item.setName(name);
			}
			uniqueNames.add(name);
		}
	}	//	checkNames

	/**
	 * 	Load Data into local m_data
	 *	@return true if success
	 */
	private boolean loadLOBData ()
	{
		//	Reset
		m_items = new ArrayList<MAttachmentEntry>();
		//
		byte[] data = getBinaryData();
		if (data == null)
			return true;
		log.fine("ZipSize=" + data.length);
		if (data.length == 0)
			return true;

		//	Old Format - single file
		if (!ZIP.equals(getTitle()))
		{
			m_items.add (new MAttachmentEntry(getTitle(), data, 1));
			return true;
		}

		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			ZipInputStream zip = new ZipInputStream (in);
			ZipEntry entry = zip.getNextEntry();
			while (entry != null)
			{
				String name = entry.getName();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[2048];
				int length = zip.read(buffer);
				while (length != -1)
				{
					out.write(buffer, 0, length);
					length = zip.read(buffer);
				}
				//
				byte[] dataEntry = out.toByteArray();
				log.fine(name
					+ " - size=" + dataEntry.length + " - zip="
					+ entry.getCompressedSize() + "(" + entry.getSize() + ") "
					+ entry.getCompressedSize()*100/entry.getSize()+ "%");
				//
				m_items.add (new MAttachmentEntry (name, dataEntry, m_items.size()+1));
				entry = zip.getNextEntry();
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "loadLOBData", e);
			m_items = null;
			return false;
		}
		return true;
	}	//	loadLOBData


	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (getTitle() == null || !getTitle().equals(ZIP))
			setTitle (ZIP);
		return saveLOBData();		//	save in BinaryData
	}	//	beforeSave


	/**************************************************************************
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
	//	System.setProperty("javax.activation.debug", "true");

		System.out.println(MimeType.getMimeType("data.xls"));
		System.out.println(MimeType.getMimeType("data.cvs"));
		System.out.println(MimeType.getMimeType("data.txt"));
		System.out.println(MimeType.getMimeType("data.log"));
		System.out.println(MimeType.getMimeType("data.html"));
		System.out.println(MimeType.getMimeType("data.htm"));
		System.out.println(MimeType.getMimeType("data.png"));
		System.out.println(MimeType.getMimeType("data.gif"));
		System.out.println(MimeType.getMimeType("data.jpg"));
		System.out.println(MimeType.getMimeType("data.xml"));
		System.out.println(MimeType.getMimeType("data.rtf"));

	//	System.exit(0);

		org.compiere.Compiere.startup(true);
		MAttachment att = new MAttachment(Env.getCtx(), 100, 0, null);
	//	att.addEntry(new File ("C:\\Compiere\\Dev.properties"));
	//	att.addEntry(new File ("C:\\Compiere2\\index.html"));

		URI uri = new File("C:\\Compiere2\\index.html").toURI();
		att.addEntry(uri);

		try
		{
			URI uri2 = new URI("http://www.compiere.com/downloads/toc.pdf");
			att.addEntry(uri2);
		}
		catch (Exception e)
		{
		}

		att.save();
		System.out.println (att);
		att.dumpEntryNames();
		int AD_Attachment_ID = att.getAD_Attachment_ID();
		//
		System.out.println ("===========================================");
		att = new MAttachment (Env.getCtx(), AD_Attachment_ID, null);
		System.out.println (att);
		att.dumpEntryNames();
		System.out.println ("===========================================");
		MAttachmentEntry[] entries = att.getEntries();
		for (MAttachmentEntry entry : entries) {
			entry.dump();
			byte[] finalData = entry.getData();
			System.out.println("  DeReferenced length=" + finalData.length);
		}
		System.out.println ("===========================================");
		//
		att.delete(true);
	}	//	main

}	//	MAttachment
