/**
 *
 */
package org.compiere.process;

import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 * 	Make User a System Admin
 *	@author Jorg Janke
 */
public class RoleMakeSysAdmin extends SvrProcess
{

	/** The User		*/
	private int		p_AD_User_ID = 0;

	/**
	 * 	Get Parameters
	 *	@see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para)
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_User_ID"))
				p_AD_User_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_User_ID=" + p_AD_User_ID);
		if (p_AD_User_ID == 0)
			throw new CompiereUserException("@NotFound@ @AD_User_ID@ =0");
		MUser user = new MUser(getCtx(), p_AD_User_ID, get_TrxName());
		if (user.get_ID() != p_AD_User_ID)
			throw new CompiereUserException("@NotFound@ @AD_User_ID@ =" + p_AD_User_ID);
		if (user.getAD_Client_ID() != getCtx().getAD_Client_ID())
			throw new CompiereUserException("@AD_Client_ID@ "
				+ user.getAD_Client_ID() + " <> "+ getCtx().getAD_Client_ID());

		//	Move to System Level
		user.setClientOrg(0, 0);
		if (!user.save())
		{
			String errorMsg = "";
			ValueNamePair ep = CLogger.retrieveError();
			if (ep != null)
				errorMsg = ep.getName();
			throw new CompiereSystemException("@AD_User_ID@: " + errorMsg);
		}

		//	Create new User Role
		//	(user should not have SysAdmin role yet - parameter check)
		MUserRoles ur = new MUserRoles(getCtx(), user.getAD_User_ID(), 0, get_TrxName());
		if (!ur.save())
		{
			String errorMsg = "";
			ValueNamePair ep = CLogger.retrieveError();
			if (ep != null)
				errorMsg = ep.getName();
			throw new CompiereSystemException("@AD_Role_ID@: " + errorMsg);
		}

		return "OK";
	}	//	doIt

}	//	RoleMakeSysAdmin
