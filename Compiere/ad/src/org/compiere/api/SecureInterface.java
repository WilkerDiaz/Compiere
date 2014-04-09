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
package org.compiere.api;

import java.math.*;
import java.sql.*;

/**
 * 	Compiere Security Interface.
 * 	To enable your own class, you need to set the property COMPIERE_SECURE 
 * 	when starting the client or server.
 *  The setting for the default class would be:
 *  -DCOMPIERE_SECURE=org.compiere.util.Secure
 *	
 *  @author Jorg Janke
 *  @version $Id: SecureInterface.java 8244 2009-12-04 23:25:29Z freyes $
 */
public interface SecureInterface
{
	/** Class Name implementing SecureInterface	*/
	public static final String	COMPIERE_SECURE = "COMPIERE_SECURE";
	/** Default Class Name implementing SecureInterface	*/
	public static final String	COMPIERE_SECURE_DEFAULT = "com.compiere.client.Secure";

	/** Encryption Key Value		*/
	public static final String	KEY_VALUE = "EKeyValue";
	/** Encryption Key Algorithm	*/
	public static final String	KEY_ALGORITHM = "EKeyAlgorithm";
	
	/** Clear Text Indicator xyz	*/
	public static final String		CLEARVALUE_START = "xyz";
	/** Clear Text Indicator		*/
	public static final String		CLEARVALUE_END = "";
	/** Encrypted Text Indicator ~	*/
	public static final String		ENCRYPTEDVALUE_START = "~";
	/** Encrypted Text Indicator ~	*/
	public static final String		ENCRYPTEDVALUE_END = "~";

	
	/**
	 *	Is Encrypted
	 *  @param value value
	 *  @return true if value is an encrypted String
	 */
	public boolean isEncrypted (String value);

	/**
	 *	Encryption.
	 *  @param value clear value
	 *  @return encrypted String
	 */
	public String encrypt (String value);

	/**
	 *	Decryption.
	 *  @param value encrypted value
	 *  @return decrypted String
	 */
	public String decrypt (String value);

	/**
	 *	Encryption.
	 * 	The methods must recognize clear text values
	 *  @param value clear value
	 *  @return encrypted String
	 */
	public Integer encrypt (Integer value);

	/**
	 *	Decryption.
	 * 	The methods must recognize clear text values
	 *  @param value encrypted value
	 *  @return decrypted String
	 */
	public Integer decrypt (Integer value);
	
	/**
	 *	Encryption.
	 * 	The methods must recognize clear text values
	 *  @param value clear value
	 *  @return encrypted String
	 */
	public BigDecimal encrypt (BigDecimal value);

	/**
	 *	Decryption.
	 * 	The methods must recognize clear text values
	 *  @param value encrypted value
	 *  @return decrypted String
	 */
	public BigDecimal decrypt (BigDecimal value);

	/**
	 *	Encryption.
	 * 	The methods must recognize clear text values
	 *  @param value clear value
	 *  @return encrypted String
	 */
	public Timestamp encrypt (Timestamp value);

	/**
	 *	Decryption.
	 * 	The methods must recognize clear text values
	 *  @param value encrypted value
	 *  @return decrypted String
	 */
	public Timestamp decrypt (Timestamp value);
	
	
	/**
	 *  Convert String to Digest.
	 *  JavaScript version see - http://pajhome.org.uk/crypt/md5/index.html
	 *
	 *  @param value message
	 *  @return HexString of message (length = 32 characters)
	 */
	public String getDigest (String value);

	/**
	 * 	Checks, if value is a valid digest
	 *  @param value digest string
	 *  @return true if valid digest
	 */
	public boolean isDigest (String value);
	
}	//	SecureInterface
