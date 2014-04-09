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
package org.compiere.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;

/**
 *	Zip/Jar File Utilities
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ZipUtil.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class ZipUtil
{
	/**
	 *	Empty Constructor, need to open explicitly.
	 */
	public ZipUtil()
	{
	}	//	ZipUtil

	/**
	 *	Open zip file.
	 *  @param fileName zip file name
	 */
	public ZipUtil(String fileName)
	{
		open (fileName);
	}	//	ZipUtil

	/**
	 *	Open zip file.
	 *  @param file zip file
	 */
	public ZipUtil(File file)
	{
		open(file);
	}	//	ZipUtil

	/**
	 *	Open zip file.
	 *  @param fileName zip file name
	 */
	public ZipUtil(URI uri)
	{
		open (uri);
	}	//	ZipUtil

	/** The Zip File		*/
	private ZipFile		m_zipFile;

	/**
	 * 	Open the Zip File for reading
	 * 	@param fileName zip file
	 * 	@return true if opened
	 */
	public boolean open (URI uri)
	{
		if (uri == null)
			return false;
		try
		{
			return open (new File(uri));
		}
		catch (Exception ex)
		{
			System.err.println("ZipUtil.open - " + ex);
			ex.printStackTrace();
		}
		return false;
	}	//	open

	/**
	 * 	Open the Zip File for reading
	 * 	@param fileName zip file
	 * 	@return true if opened
	 */
	public boolean open (String fileName)
	{
		if (fileName == null)
			return false;
		try
		{
			return open (new File(fileName));
		}
		catch (Exception ex)
		{
			System.err.println("ZipUtil.open - " + ex);
		}
		return false;
	}	//	open

	/**
	 * 	Open the Zip File for reading
	 * 	@param file zip file
	 * 	@return true if opened
	 */
	public boolean open (File file)
	{
		if (file == null)
			return false;
		try
		{
			if (file.getName().endsWith("jar"))
				m_zipFile = new JarFile (file, false, ZipFile.OPEN_READ);
			else
				m_zipFile = new ZipFile (file, ZipFile.OPEN_READ);
		}
		catch (IOException ex)
		{
			System.err.println("ZipUtil.open - " + ex);
			m_zipFile = null;
			return false;
		}
		return true;
	}	//	open

	/**
	 * 	Close Zip File
	 */
	public void close()
	{
		try
		{
			if (m_zipFile != null)
				m_zipFile.close();
		}
		catch (IOException ex)
		{
			System.err.println("ZipUtil.close - " + ex);
		}
		m_zipFile = null;
	}	//	close
	
	
	/**
	 * 	Is the Zip File Open
	 * 	@return true if yes
	 */
	public boolean isOpen()
	{
		return m_zipFile != null;
	}	//	isOpen

	/**
	 * 	Is it a Jar
	 * 	@return true if yes
	 */
	public boolean isJar()
	{
		return (m_zipFile != null && m_zipFile instanceof JarFile);
	}	//	isJar

	/**
	 * 	Get it as Jar if it is a Jar
	 * 	@return jar or null if not a jar
	 */
	public JarFile getJar()
	{
		if (m_zipFile != null && m_zipFile instanceof JarFile)
			return (JarFile)m_zipFile;
		return null;
	}	//	getJar

	/**
	 * 	String Representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		if (m_zipFile != null)
			return m_zipFile.toString();
		return "ZipUtil";
	}	//	toString


	/**************************************************************************
	 * 	Get Content as sorted String Array
	 * 	@return content
	 */
	public String[] getContent()
	{
		if (!isOpen())
			return null;
		Enumeration<? extends ZipEntry> e = m_zipFile.entries();
		ArrayList<ZipEntry> list = new ArrayList<ZipEntry>();
		while (e.hasMoreElements())
			list.add(e.nextElement());
		//	return sorted array
		String[] retValue = new String[list.size()];
		for (int i = 0; i < retValue.length; i++)
			retValue[i] = (list.get(i)).getName();
		Arrays.sort(retValue);
		return retValue;
	}	//	getContent

	/**
	 * 	Get ZipEntries as Enumeration
	 * 	@return entries
	 */
	public Enumeration<? extends ZipEntry> entries()
	{
		if (!isOpen())
			return null;
		return m_zipFile.entries();
	}	//	entries
	
	/**
	 *  Get the input stream for reading the content of the zip file entry
	 *  @return InputStream
	 */
	public InputStream getInputStream(ZipEntry zipEntry)
		throws IOException
	{
		if (!isOpen())
			return null;
		
		return m_zipFile.getInputStream(zipEntry);
	}  // getInputStream
	
	
	/**
	 *	Write Entry to File
	 *	@param zipEntry entry
	 *	@param fileName absolute path of the file
	 *	@return true if success
	 */
	public boolean writeToFile(ZipEntry zipEntry, String fileName)
	{
		File file = null;
		FileOutputStream fOut = null;
		BufferedOutputStream bOut = null;
		BufferedInputStream bIn = null;
		boolean success = false;
		try
		{
			InputStream in = getInputStream(zipEntry);
			if (in == null)
				return false;
			file = new File(fileName);
			if (file.exists())
			{
				System.err.println("Overwriting: " + fileName);
				file.delete();
			}
			fOut = new FileOutputStream(file);
			bOut = new BufferedOutputStream(fOut);
			
			bIn = new BufferedInputStream(in);
			while (bIn.available() > 0) 
				bOut.write(bIn.read());
			
			bOut.flush();
			bOut.close();
			bOut = null;
			success = true;
		}
		catch (IOException ex)
		{
			System.err.println("Unable to write to  " + fileName);
		}
		finally
		{
			try
			{
				if (bOut != null)
					bOut.close();
				if (bIn != null)
					bIn.close();
			}
			catch (IOException ex) 
			{}
		}
		return success;
	}	// writeToFile

	/**
	 *	Write Entry to File
	 *	@param zipEntry entry
	 *	@return temporary File or null
	 */
	public File writeToTemp(ZipEntry zipEntry)
	{
		File file = null;
		FileOutputStream fOut = null;
		BufferedOutputStream bOut = null;
		BufferedInputStream bIn = null;
		try
		{
			file = File.createTempFile("Zip", ".tmp");
			InputStream in = getInputStream(zipEntry);
			if (in == null)
				return null;
			fOut = new FileOutputStream(file);
			bOut = new BufferedOutputStream(fOut);
			
			bIn = new BufferedInputStream(in);
			while (bIn.available() > 0) 
				bOut.write(bIn.read());
			
			bOut.flush();
			bOut.close();
			bOut = null;
			fOut.close();
			fOut = null;
		}
		catch (IOException ex)
		{
			System.err.println("Unable to write to  " + file);
			file = null;
		}
		finally
		{
			try
			{
				if (bOut != null)
					bOut.close();
				if (fOut != null)
					fOut.close();
				if (bIn != null)
					bIn.close();
			}
			catch (IOException ex) 
			{}
		}
		return file;
	}	// writeToTemp

	/**
	 * 	Get Zip Entry
	 * 	@param name entry name
	 * 	@return ZipEntry or null if not found
	 */
	public ZipEntry getEntry (String name)
	{
		if (!isOpen())
			return null;
		return m_zipFile.getEntry(name);
	}	//	getEntry

	/**
	 * 	Get File Info
	 * 	@param name name
	 * 	@return time and size
	 */
	public String getEntryInfo (String name)
	{
		StringBuffer sb = new StringBuffer(name);
		ZipEntry e = getEntry(name);
		if (e == null)
			sb.append(": -");
		else
		{
			Timestamp ts = new Timestamp(e.getTime());
			sb.append(": ").append(ts).append(" - ").append(e.getSize());
		}
		return sb.toString();
	}	//	getEntryInfo

	/**
	 * 	Get Manifest if a Jar
	 * 	@return Manifest if exists or null
	 */
	public Manifest getManifest()
	{
		try
		{
			JarFile jar = getJar();
			if (jar != null)
				return jar.getManifest();
		}
		catch (IOException ex)
		{
			System.err.println("ZipUtil.getManifest - " + ex);
		}
		return null;
	}	//	getManifest


	/**************************************************************************
	 * 	Get Zip Entry
	 * 	@param fileName zip/jar file
	 * 	@param entryName entry
	 * 	@return ZipEntry
	 */
	static public ZipEntry getEntry (String fileName, String entryName)
	{
		if (fileName == null || entryName == null)
			return null;
		//	File
		File file = new File(fileName);
		if (!file.exists())
		{
			String fn = findInPath(fileName);
			if (fn == null)
				return null;	//	file not found
			file = new File(fn);
		}
		ZipUtil zu = new ZipUtil (file);
		if (!zu.isOpen())
			return null;
		//	Entry
		ZipEntry retValue = zu.getEntry(entryName);
		if (retValue == null)
		{
			Enumeration<? extends ZipEntry> e = zu.entries();
			while (e.hasMoreElements())
			{
				ZipEntry entry = e.nextElement();
				if (entry.getName().indexOf(entryName) != -1)
				{
					retValue = entry;
					break;
				}
			}
		}
		zu.close();
		return retValue;
	}	//	getEntry

	/**
	 * 	Get Jar File
	 * 	@param fileName zip/jar file
	 * 	@return Jar
	 */
	static public JarFile getJar (String fileName)
	{
		if (fileName == null)
			return null;
		//	File
		File file = new File(fileName);
		if (!file.exists())
		{
			String fn = findInPath(fileName);
			if (fn == null)
				return null;	//	file not found
			file = new File(fn);
		}
		ZipUtil zu = new ZipUtil (file);
		return zu.getJar();
	}	//	getJar

	/**
	 * 	Get Manifest
	 * 	@param fileName zip/jar file
	 * 	@return Manifest or null
	 */
	static public Manifest getManifest (String fileName)
	{
		if (fileName == null)
			return null;
		JarFile jar = getJar (fileName);
		if (jar == null)
			return null;
		try
		{
			return jar.getManifest();
		}
		catch (IOException ex)
		{
			System.err.println("ZipUtil.getManifest - " + ex);
		}
		return null;
	}	//	getManifest

	/**
	 * 	Get Manifest
	 * 	@param fileName jar file
	 *  @param jarEntry jar entry
	 * 	@return Manifest
	 */
	static public JarEntry getJarEntry (String fileName, String jarEntry)
	{
		if (fileName == null)
			return null;
		JarFile jar = getJar (fileName);
		if (jar == null)
			return null;
		return jar.getJarEntry(jarEntry);
	}	//	getManifest

	/**
	 * 	Dump Manifest to
	 * 	@param fileName zip/jar file
	 * 	 */
	static public void dumpManifest (String fileName)
	{
		Manifest mf = getManifest (fileName);
		if (mf == null)
		{
			System.out.println("No Jar file: " + fileName);
			return;
		}
		//
		System.out.println(mf.getEntries());
	}	//	dumpManifest

	/**
	 * 	Get Zip Entry time
	 * 	@param fileName zip file
	 * 	@param entryName entry
	 * 	@return Time as String or null
	 */
	static public String getEntryTime (String fileName, String entryName)
	{
		ZipEntry entry = getEntry(fileName, entryName);
		if (entry == null)
			return null;
		Timestamp ts = new Timestamp (entry.getTime());
		return ts.toString();
	}	//	getEntryTime

	/**
	 * 	Get Fill name of jarfile in path
	 * 	@param jarFile name
	 * 	@return full name or null if not found
	 */
	static public String findInPath (String jarFile)
	{
		String path = System.getProperty("java.class.path");
		String[] pathEntries = path.split(System.getProperty("path.separator"));
		for (String element : pathEntries) {
		//	System.out.println(pathEntries[i]);
			if (element.indexOf(jarFile) != -1)
				return element;
		}
		path = System.getProperty("sun.boot.class.path");
		pathEntries = path.split(System.getProperty("path.separator"));
		for (String element : pathEntries) {
		//	System.out.println(pathEntries[i]);
			if (element.indexOf(jarFile) != -1)
				return element;
		}
		return null;
	}	//	findInPath
	
	/**
	 * 	Test
	 *	@param args ignored
	 */
	public static void main (String[] args)
	{
		try
		{
			//	Get Jar File
			JarFile jar = ZipUtil.getJar("CClient.jar");
			if (jar == null)
				jar = ZipUtil.getJar("CTools.jar");
			if (jar == null)
				return;

		//	JarEntry je = jar.getJarEntry(JarFile.MANIFEST_NAME);
		//	if (je != null)
		//		System.out.println("Time " + new Date(je.getTime()));
			Manifest mf = jar.getManifest();
			if (mf != null)
			{
				Attributes atts = mf.getMainAttributes();
				atts.getValue("Implementation-Vendor");
				atts.getValue("Implementation-Version");
				//
			}
		}
		catch (IOException ex)
		{
			System.err.println(ex);
		}
	}

}	//	ZipUtil
