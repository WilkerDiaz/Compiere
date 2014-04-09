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
import java.util.logging.*;


/**
 *	Compiere Logger
 *
 *  @author Jorg Janke
 *  @version $Id: CLogger.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CLogger
	extends Logger implements CompiereLogger, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Logger
	 *	@param className class name
	 *	@return Logger
	 */
    public static synchronized CLogger getCLogger (String className)
    {
   	//	CLogMgt.initialize();
    	LogManager manager = LogManager.getLogManager();
    	if (className == null)
    		className = "";
    	Logger result = manager.getLogger(className);
    	if ((result != null) && (result instanceof CLogger))
    		return (CLogger)result;
    	//
   	    CLogger newLogger = new CLogger(className, null);
   	    newLogger.setLevel(CLogMgt.getLevel());
   	    manager.addLogger(newLogger);
    	return newLogger;
    }	//	getLogger

	/**
	 * 	Get Logger
	 *	@param clazz class name
	 *	@return Logger
	 */
    public static CLogger getCLogger (Class<?> clazz)
    {
    	if (clazz == null)
    		return get();
    	return getCLogger (clazz.getName());
    }	//	getLogger

    /**
     * 	Get default Compiere Logger.
     * 	Need to be used in serialized objects
     *	@return logger
     */
    public static CLogger get()
    {
    	if (s_logger == null)
    		s_logger = getCLogger("org.compiere.default");
    	return s_logger;
    }	//	get

    /**	Default Logger			*/
    private static CLogger	s_logger = null;


	/**************************************************************************
	 * 	Standard constructor
	 *	@param name logger name
	 *	@param resourceBundleName optional resource bundle (ignored)
	 */
    private CLogger (String name, String resourceBundleName)
	{
		super (name, resourceBundleName);
	//	setLevel(Level.ALL);
	}	//	CLogger


	/*************************************************************************/

	/** Last Error Message   */
	private static  ThreadLocal<ValueNamePair>   s_lastError = new ThreadLocal<ValueNamePair>() {
		@Override
		protected ValueNamePair initialValue() {
			return null;
		}
	};
	/**	Last Exception		*/
	private static ThreadLocal<Exception>		s_lastException = new ThreadLocal<Exception>() {
		@Override
		protected Exception initialValue() {
			return null;
		}
	};
	/** Last Warning Message   */
	private static  ThreadLocal<ValueNamePair>   s_lastWarning = new ThreadLocal<ValueNamePair>() {
		@Override
		protected ValueNamePair initialValue() {
			return null;
		}
	};
	/** Last Info Message   */
	private static  ThreadLocal<ValueNamePair>   s_lastInfo = new ThreadLocal<ValueNamePair>() {
		@Override
		protected ValueNamePair initialValue() {
			return null;
		}
	};

	/**
	 *  Set and issue Error and save as ValueNamePair
	 *  @param AD_Message message key
	 *  @param message clear text message
	 *  @return true (to avoid removal of method)
	 */
	public boolean saveError (String AD_Message, String message)
	{
		return saveError (AD_Message, message, true);
	}   //  saveError

	/**
	 *  Set and issue Error and save as ValueNamePair
	 *  @param AD_Message message key
	 *  @param ex exception
	 *  @return true (to avoid removal of method)
	 */
	public boolean saveError (String AD_Message, Exception ex)
	{
		s_lastException.set(ex);
		return saveError (AD_Message, ex.getLocalizedMessage(), true);
	}   //  saveError

	/**
	 *  Save Warning as ValueNamePair.
	 *  @param AD_Message message key
	 *  @param ex exception
	 *  @return true
	 */
	public boolean saveWarning (String AD_Message, Exception ex)
	{
		s_lastException.set(ex);
		return saveWarning(AD_Message, ex.getLocalizedMessage());
	}   //  saveWarning

	/**
	 *  Set Error and save as ValueNamePair
	 *  @param AD_Message message key
	 *  @param message clear text message
	 *  @param issueError print error message (default true)
	 *  @return true
	 */
	public boolean saveError (String AD_Message, String message, boolean issueError)
	{
		s_lastError.set(new ValueNamePair (AD_Message, message));
		//  print it
		if (issueError)
			warning(AD_Message + " - " + message);
		return true;
	}   //  saveError


	/**
	 * @return Whether an error had been saved onto the stack.
	 */
	public static boolean hasError()
	{
		return s_lastError.get() != null;
	}


	/**
	 *  Get Error from Stack
	 *  @return AD_Message as Value and Message as String
	 */
	public static ValueNamePair retrieveError()
	{
		ValueNamePair vp = s_lastError.get();
		s_lastError.set(null);
		return vp;
	}   //  retrieveError

	/**
	 *  Get Error from Stack
	 *  @return last exception
	 */
	public static Exception retrieveException()
	{
		Exception ex = s_lastException.get();
		s_lastException.set(null);
		return ex;
	}   //  retrieveError

	/**
	 *  Save Warning as ValueNamePair.
	 *  @param AD_Message message key
	 *  @param message clear text message
	 *  @return true
	 */
	public boolean saveWarning (String AD_Message, String message)
	{
		s_lastWarning.set(new ValueNamePair (AD_Message, message));
		//  print it
		if (true) //	issueError
			warning(AD_Message + " - " + message);
		return true;
	}   //  saveWarning

	/**
	 *  Get Warning from Stack
	 *  @return AD_Message as Value and Message as String
	 */
	public static ValueNamePair retrieveWarning()
	{
		ValueNamePair vp = s_lastWarning.get();
		s_lastWarning.set(null);
		return vp;
	}   //  retrieveWarning

	/**
	 *  Save Info as ValueNamePair
	 *  @param AD_Message message key
	 *  @param message clear text message
	 *  @return true
	 */
	public boolean saveInfo (String AD_Message, String message)
	{
		s_lastInfo.set(new ValueNamePair (AD_Message, message));
		return true;
	}   //  saveInfo

	/**
	 *  Get Info from Stack
	 *  @return AD_Message as Value and Message as String
	 */
	public static ValueNamePair retrieveInfo()
	{
		ValueNamePair vp = s_lastInfo.get();
		s_lastInfo.set(null);
		return vp;
	}   //  retrieveInfo

	/**
	 * 	Reset Saved Messages/Errors/Info
	 */
	public static void resetLast()
	{
		s_lastError.set(null);
		s_lastException.set(null);
		s_lastWarning.set(null);
		s_lastInfo.set(null);
	}	//	resetLast

	/**
	 * 	Is Finest Level
	 *	@return true if finest or more
	 */
	public boolean isLevelFinest()
	{
		return CLogMgt.isLevelFinest();
	}	//	isLevelFinest


	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("CLogger[");
		sb.append (getName())
			.append (",Level=").append (getLevel()).append ("]");
		return sb.toString ();
	}	 //	toString

	/**
	 * 	Write Object - Serialization
	 *	@param out out
	 *	@throws IOException
	 *
	private void writeObject (ObjectOutputStream out) throws IOException
	{
		out.writeObject(getName());
		System.out.println("====writeObject:" + getName());
	}	//	writeObject

	private String m_className = null;

	private void readObject (ObjectInputStream in) throws IOException
	{
		try
		{
			m_className = (String)in.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("====readObject:" + m_className);
	}

	protected Object readResolve() throws ObjectStreamException
	{
		System.out.println("====readResolve:" + m_className);
		return getLogger(m_className);
	}
	/** **/
}	//	CLogger
