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
package org.compiere.session;

import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.compiere.Compiere;
import org.compiere.db.CConnection;
import org.compiere.util.CLogger;


/**
 * 	Compiere Status Bean
 *
 *  @ejb.bean name="compiere/Status"
 *		display-name="Compiere Status Session Bean"
 *		type="Stateless"
 *		view-type="both"
 *		transaction-type="Bean"
 *		jndi-name="compiere/Status"
 *      local-jndi-name="compiere/StatusLocal"
 *
 *  @ejb.ejb-ref ejb-name="compiere/Status"
 *  	view-type="both"
 *		ref-name="compiere/Status"
 *  @ejb.ejb-ref ejb-name="compiere/Status"
 *  	view-type="local"
 *		ref-name="compiere/StatusLocal"
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: StatusBean.java 7514 2009-04-20 21:57:40Z freyes $
 */
public class StatusBean implements SessionBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**	Logging				*/
	private static CLogger	log = CLogger.getCLogger(StatusBean.class);

	private static int		s_no = 0;
	private int				m_no = 0;
	//
	private int				m_versionCount = 0;
	private int				m_databaseCount = 0;


	/**
	 * 	Get Version (Date)
	 *  @ejb.interface-method view-type="both"
	 *  @return version e.g. 2002-09-02
	 */
	public String getDateVersion()
	{
		m_versionCount++;
		log.info ("getDateVersion " + m_versionCount);
		return Compiere.DATE_VERSION;
	}	//	getDateVersion

	/**
	 * 	Get Main Version
	 *  @ejb.interface-method view-type="both"
	 *  @return main version - e.g. Version 2.4.3b
	 */
	public String getMainVersion()
	{
		return Compiere.MAIN_VERSION;
	}	//	getMainVersion

	/**
	 *  Get Database Type
	 *  @ejb.interface-method view-type="both"
	 *  @return Database Type
	 */
	public String getDbType()
	{
		return CConnection.get().getDBType();
	}   //  getDbType

	/**
	 *  Get Database Host
	 *  @ejb.interface-method view-type="both"
	 *  @return Database Host Name
	 */
	public String getDbHost()
	{
		m_databaseCount++;
		log.info ("getDbHost " + m_databaseCount);
		return CConnection.get().getDbHost();
	}   //  getDbHost

	/**
	 *  Get Database Port
	 *  @ejb.interface-method view-type="both"
	 *  @return Database Port
	 */
	public int getDbPort()
	{
		return CConnection.get().getDbPort();
	}   //  getDbPort

	/**
	 *  Get Database SID
	 *  @ejb.interface-method view-type="both"
	 *  @return Database SID
	 */
	public String getDbName()
	{
		return CConnection.get().getDbName();
	}   //  getDbSID

	/**
	 *  Get Database URL
	 *  @ejb.interface-method view-type="both"
	 *  @return Database URL
	 */
	public String getConnectionURL()
	{
		return CConnection.get().getConnectionURL();
	}   //  getConnectionURL
	
	/**
	 *  Get Database UID
	 *  @ejb.interface-method view-type="both"
	 *  @return Database User Name
	 */
	public String getDbUid()
	{
		return CConnection.get().getDbUid();
	}   //  getDbUID

	/**
	 *  Get Database PWD
	 *  @ejb.interface-method view-type="both"
	 *  @return Database User Password
	 */
	public String getDbPwd()
	{
		return CConnection.get().getDbPwd();
	}   //  getDbPWD

	/**
	 *  Get Connection Manager Host
	 *  @ejb.interface-method view-type="both"
	 *  @return Connection Manager Host
	 */
	public String getFwHost()
	{
		return CConnection.get().getFwHost();
	}   //  getCMHost

	/**
	 *  Get Connection Manager Port
	 *  @ejb.interface-method view-type="both"
	 *  @return Connection Manager Port
	 */
	public int getFwPort()
	{
		return CConnection.get().getFwPort();
	}   //  getCMPort

	
	/**************************************************************************
	 * 	Get Version Count
	 *  @ejb.interface-method view-type="both"
	 * 	@return number of version inquiries
	 */
	public int getVersionCount()
	{
		return m_versionCount;
	}	//	getVersionCount

	/**
	 * 	Get Database Count
	 *  @ejb.interface-method view-type="both"
	 * 	@return number of database inquiries
	 */
	public int getDatabaseCount()
	{
		return m_databaseCount;
	}	//	getVersionCount

	/**
	 * 	Describes the instance and its content for debugging purpose
	 *  @ejb.interface-method view-type="both"
	 * 	@return Debugging information about the instance and its content
	 */
	public String getStatus()
	{
		StringBuffer sb = new StringBuffer("StatusBean[No=");
		sb.append(m_no)
			.append(",VersionCount=").append(m_versionCount)
			.append(",DatabaseCount=").append(m_versionCount)
			.append("]");
		return sb.toString();
	}	//	getStatus


	/**
	 * 	String Representation
	 * 	@return info
	 */
	public String toString()
	{
		return getStatus();
	}	//	toString

	
	/**************************************************************************
	 * 	Create the Session Bean
	 * 	@throws EJBException
	 * 	@throws CreateException
	 *  @ejb.create-method view-type="both"
	 */
	public void ejbCreate() throws EJBException, CreateException
	{
		m_no = ++s_no;
		try
		{
			if (!Compiere.startup(false, "StatusBean"))
				throw new CreateException("Compiere could not start");
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "", ex);
		//	throw new CreateException ();
		}
		log.info("#" + m_no + " - " + getStatus());
	}	//	ejbCreate


	// -------------------------------------------------------------------------
	// Framework Callbacks
	// -------------------------------------------------------------------------

	/**
	 * 	Set Session Context
	 * 	@param aContext context
	 * 	@throws EJBException
	 */
	public void setSessionContext (SessionContext aContext) throws EJBException
	{
	}

	/**
	 * 	Ejb Activate
	 *	@throws EJBException
	 */
	public void ejbActivate() throws EJBException
	{
		if (log == null)
			log = CLogger.getCLogger(getClass());
		log.fine("ejbActivate");
	}

	/**
	 * 	Ejb Passivate
	 *	@throws EJBException
	 */
	public void ejbPassivate() throws EJBException
	{
		log.fine("ejbPassivate");
	}

	/**
	 * 	Ejb Remove
	 *	@throws EJBException
	 */
	public void ejbRemove() throws EJBException
	{
		log.fine("ejbRemove");
	}

}	//	StatusBean
