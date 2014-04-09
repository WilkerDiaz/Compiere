/**
 *
 */
package org.compiere.wstore;

import javax.servlet.http.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 * 	Web Store Session Management
 *	@author Jorg Janke
 */
public class SessionManager implements HttpSessionListener
{

	/**	Logger	*/
    private static CLogger	log = CLogger.getCLogger(SessionManager.class);
    /** Session Counter		*/
    private static int		m_counter = 0;

	/**
	 *	@see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent e)
	{
		if (m_counter == 0)
		{
			//	Assumes only one AppsServer
		//	MSession.reset(MSession.SESSIONTYPE_WebStore);
		}
		m_counter++;
	}	//	sessionCreated

	/**
	 * 	Session Destroyed - logout session
	 *	@see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent e)
	{
		m_counter--;
		HttpSession session = e.getSession();
		Object oo = session.getAttribute("AD_Session_ID");
		if (oo == null)
			return;
		log.info("AD_Session_ID=" + oo);
		Integer ii = (Integer)oo;
		int AD_Session_ID = ii.intValue();
		MSession ss = new MSession(Env.getCtx(), AD_Session_ID, null);
		if (ss.getAD_Session_ID() == 0
			|| ss.getAD_Session_ID() != AD_Session_ID
			|| ss.isProcessed())
			return;
		//
		ss.logout("LogoutX");
	}	//	sessionDestroyed

	/**
	 * 	Get Session Count
	 *	@return count
	 */
	public static int getSessionCount()
	{
		return m_counter;
	}	//	getSessionCount

}	//	SessionManager
