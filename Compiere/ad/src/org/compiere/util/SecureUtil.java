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
import java.util.*;
import java.util.logging.*;

import javax.crypto.*;

import org.compiere.api.*;

/**
 * 	Security Utility
 *	
 *  @author Jorg Janke
 *  @version $Id: SecureUtil.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class SecureUtil
{
	/**************************************************************************
	 *	Hash checksum number
	 *  @param key key
	 *  @return checksum number
	 */
	public static int hash (String key)
	{
		long tableSize = 2147483647;	// one less than max int
		long hashValue = 0;

		for (int i = 0; i < key.length(); i++)
			hashValue = (37 * hashValue) + (key.charAt(i) -31);

		hashValue %= tableSize;
		if (hashValue < 0)
			hashValue += tableSize;

		int retValue = (int)hashValue;
		return retValue;
	}	//	hash

	
	/**************************************************************************
	 *  Convert Byte Array to Hex String
	 *  @param bytes bytes
	 *  @return HexString
	 */
	public static String convertToHexString (byte[] bytes)
	{
		//	see also Util.toHex
		int size = bytes.length;
		StringBuffer buffer = new StringBuffer(size*2);
		for(int i=0; i<size; i++)
		{
			// convert byte to an int
			int x = bytes[i];
			// account for int being a signed type and byte being unsigned
			if (x < 0)
				x += 256;
			String tmp = Integer.toHexString(x);
			// pad out "1" to "01" etc.
			if (tmp.length() == 1)
				buffer.append("0");
			buffer.append(tmp);
		}
		return buffer.toString();
	}   //  convertToHexString


	/**
	 *  Convert Hex String to Byte Array
	 *  @param hexString hex string
	 *  @return byte array
	 */
	public static byte[] convertHexString (String hexString)
	{
		if (hexString == null || hexString.length() == 0)
			return null;
		int size = hexString.length()/2;
		byte[] retValue = new byte[size];
		String inString = hexString.toLowerCase();

		try
		{
			for (int i = 0; i < size; i++)
			{
				int index = i*2;
				int ii = Integer.parseInt(inString.substring(index, index+2), 16);
				retValue[i] = (byte)ii;
			}
			return retValue;
		}
		catch (Exception e)
		{
			log.finest(hexString + " - " + e.getLocalizedMessage());
		}
		return null;
	}   //  convertToHexString

	
	/**
	 * 	Create New Key and Install.
	 * 	Should not be called as it always will create a new key
	 *	@param compiereHome compiere home directory
	 *	@return true if key installed
	 */
	public static boolean createNewKey (String compiereHome)
	{
		SecureUtil util = new SecureUtil();
		//	create key
		if (!util.createKey())
			return false;

		Properties prop = new Properties();
		prop.setProperty(SecureInterface.KEY_ALGORITHM, "DES");
		prop.setProperty(SecureInterface.KEY_VALUE, "100,25,28,-122,-26,94,-3,-26");
		//	Save
		File file = getKeyFile(compiereHome);
		try
		{
			FileOutputStream out = new FileOutputStream(file);
			prop.store(out, "Compiere Encryption Key");
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			System.err.println("Could not write " + file.toString());
			System.exit(1);
		}
		//
		return installKey(compiereHome);
	}	//	installNewKey
	
	/**
	 * 	Install Key
	 *	@param compiereHome compiere home directory
	 *	@return true if key installed
	 */
	public static boolean installKey (String compiereHome)
	{
		File keyFile = getKeyFile(compiereHome);
		if (!keyFile.exists())
		{
			log.info("File not found");
			return createNewKey(compiereHome);
		}
		
		//	Update Secure
		SecureEngine.reset();
		return true;
	}	//	installKey
	
	/**
	 * 	Get Key Name
	 *	@param compiereHome compiere home directory
	 *	@return file name
	 */
	public static File getKeyFile (String compiereHome)
	{
		return new File(compiereHome + "/lib/" + SecureFileName);
	}	//	getFileName
	
	/**************************************************************************
	 * 	SecureUtil
	 */
	private SecureUtil()
	{
	}	//	SecureUtil
	
	/**	Logger	*/
    private static CLogger log = CLogger.getCLogger(SecureUtil.class);
	/** Secure File name 		*/
	public static final String	SecureFileName = "CompiereSecure.properties";
	/**	The Key					*/
	private SecretKey 		m_key = null;

	/**
	 * 	Create Key
	 *	@return true if success
	 */
	private boolean createKey()
	{
		try
		{
			KeyGenerator keygen = KeyGenerator.getInstance("DES");
			m_key = keygen.generateKey();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			m_key = null;
			return false;
		}
		byte[] keys = m_key.getEncoded();
		StringBuffer sb = new StringBuffer ("Key ")
			.append(m_key.getAlgorithm())
			.append("(").append(keys.length).append(")= ");
		for (byte element : keys)
			sb.append(element).append(",");
		log.info(sb.toString());
		return true;
	}	//	createKey

	/**************************************************************************
	 * 	Test
	 *	@param args
	 */
	public static void main(String[] args)
	{
		CLogMgt.initialize(true);
		CLogMgt.setLevel(Level.FINEST);

		installKey("C:\\Compiere2");
		
	}	//	main
	
}	//	SecureUtil
