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

import java.sql.*;
import java.util.logging.*;

import org.compiere.common.CompiereStateException;

/**
 *	Transaction Management.
 *	- Create new Transaction by Trx.get(name);
 *	- ..transactions..
 *	- commit();
 *	----	start();
 *	----	commit();
 *	- close();
 *	
 *  @author Jorg Janke
 *  @version $Id: Trx.java 8760 2010-05-13 16:54:16Z nnayak $
 */
public class Trx
{

	
	/**
	 * Records the stack trace where this Trx was closed.
	 */
	private Throwable m_closeStackTrace = null;
	
	
	/**
	 * Create new transaction. You MUST call the close() method on the returned
	 * Trx object when you are finished with the transaction.
	 * 
	 * @param trxName
	 *            Transaction Name prefix. Use for debugging purposes. Can be
	 *            anything except null or empty.
	 * @return Transaction or null
	 */
	public static Trx get(String trxName) {
		return new Trx(trxName, false);
	} // get
	

	public static Trx getAutoCommitTrx() {
		return new Trx("autoCommit", true);		
	}

	
	

	/**************************************************************************
	 * 	Transaction Constructor
	 * 	@param trxName unique name
	 */
	private Trx(String trxName, boolean autoCommit) {
		if (trxName == null || trxName.length() == 0)
			throw new IllegalArgumentException("No Transaction Name");
		m_trxName = trxName;
		try {
			m_conn = DB.getCachedConnection();
			m_conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new CompiereStateException("Could not get connection", e);
		}

	} // Trx

	/** Logger */
	private static final CLogger 		 log = CLogger.getCLogger(Trx.class);
	
	private	Connection m_conn;
	private	final String m_trxName;
	

	/**
	 * 	Get Connection
	 *	@return connection
	 */
	public Connection getConnection()
	{
		if( m_closeStackTrace != null ) {
			log.log(Level.SEVERE, "Trx " + m_trxName + " was already closed at:", m_closeStackTrace);
		}
		return m_conn;
	}	//	getConnection

	
	/**
	 * 	Rollback
	 *	@return true if success
	 */
	public boolean rollback()
	{
		try
		{
			if (m_conn != null)
			{
				m_conn.rollback();
				log.fine ("**** " + m_trxName);
				return true;
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, m_trxName, e);
		}		
		return false;
	}	//	rollback

	/**
	 * 	Commit
	 **/
	public boolean commit()
	{
		try
		{
			if (m_conn != null)
			{
				m_conn.commit();
				log.fine ("**** " + m_trxName);
				return true;
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, m_trxName, e);
		}
		return false;
	}	//	commit

	/**
	 * End Transaction and Close Connection. NOTE, close() will NOT commit or
	 * rollback the transaction. You MUST manually commit or rollback before
	 * calling this method.
	 * 
	 * @return true if success
	 */
	public synchronized boolean close()
	{
		if( m_closeStackTrace != null ) {
			log.log(Level.SEVERE, "Trx " + m_trxName + " was already closed at:", m_closeStackTrace);
		}
		m_closeStackTrace = new Throwable();
		
		//
		if (m_conn == null)
			return true;
			
		//	Close Connection
		try
		{
			if( !m_conn.isClosed() ) {
				m_conn.close();
				DB.returnCachedConnection(m_conn);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, m_trxName, e);
		}
		m_conn = null;
		log.config(m_trxName);
		return true;
	}	//	close
	
    /**
     * Closes A Trx object, but only if not null.
     */
    public static void closeTrx( final Trx trx ) {
        if (trx != null) 
        	trx.close();
    }

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString() {
		return "Trx[" + m_trxName + ", " + m_conn + "]";
	} // toString


	public String getTrxName() {
		return m_trxName;
	}

	@Override
	protected void finalize() throws Throwable {
		// finalize() is exceedingly inefficient, but it's better than having
		// connection or statement leaks.
		if( m_conn != null && !m_conn.isClosed() ){
			log.log(Level.INFO, "Connection not properly closed; closing now; was borrowed at "
					+ DB.getBorrower(m_conn));
			close();
		}
		super.finalize();
	}



	
	
}	//	Trx
