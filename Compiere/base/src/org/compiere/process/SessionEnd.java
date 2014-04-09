/**
 *
 */
package org.compiere.process;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 * 	End the current session
 *	@author Jorg Janke
 */
public class SessionEnd extends SvrProcess
{
	/**	Session ID			*/
	private int		m_AD_Session_ID = 0;

	/**
	 * 	Get Parameter
	 */
	@Override
	protected void prepare()
	{
		m_AD_Session_ID = getRecord_ID();
	}

	/**
	 * 	Process
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_Session_ID=" + m_AD_Session_ID);
		if (m_AD_Session_ID == 0)
			throw new CompiereUserException("@NotFound@ @AD_Session_ID@");
		MSession session = new MSession(getCtx(), m_AD_Session_ID, get_TrxName());
		if (session.get_ID() != m_AD_Session_ID)
			throw new CompiereUserException("@NotFound@ @AD_Session_ID@ ID=" + m_AD_Session_ID);

		if (session.isProcessed())
			return "@Processed@";

		MUser user = MUser.get(getCtx(), getCtx().getAD_User_ID());

		session.setProcessed(true);
		session.addDescription("Ended by " + user.getName());
		session.logout();

		return "OK";
	}	//	doIt

}	//	SessionEnd
