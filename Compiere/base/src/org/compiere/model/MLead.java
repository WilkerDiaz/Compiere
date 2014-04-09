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

import java.sql.*;
import java.util.*;

import org.compiere.*;
import org.compiere.util.*;

/**
 * 	Lead Model
 *	
 *  @author Jorg Janke
 *  @version $Id$
 */
public class MLead extends X_C_Lead
{
    /** Logger for class MLead */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MLead.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_Lead_ID id
	 *	@param trx p_trx
	 */
	public MLead(Ctx ctx, int C_Lead_ID, Trx trx)
	{
		super(ctx, C_Lead_ID, trx);
		if (C_Lead_ID == 0)
		{
			setProcessed(false);
		}
	}	//	MLead

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MLead (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MLead

	/**
	 * 	Lead map constructor
	 *	@param ctx context
	 *	@param map map
	 *	@param trx p_trx
	 */
	public MLead (Ctx ctx, Map<String,String> map, Trx trx)
	{
		this (ctx, 0, trx);
		load (map);
		//	Overwrite
	//	set_ValueNoCheck ("C_Lead_ID", null);
		setIsActive(true);
		setProcessed(false);
	}	//	MLead
	
	
	/** BPartner			*/
	private MBPartner	m_bp = null;
	/** User				*/
	private MUser		m_user = null;
	/** Request				*/
	private MRequest	m_request = null;
	/** Project				*/
	private MProject	m_project = null;
	/** Request Status		*/
	private MStatus		m_Status = null;

	/**
	 * 	Set AD_User_ID from email
	 */
	public void setAD_User_ID()
	{
		if (getAD_User_ID() != 0)
			return;
		String email = getEMail();
		if (email != null && email.length() > 0)
		{
			m_user = MUser.get(getCtx(), email, get_Trx());
			if (m_user != null)
			{
			    super.setAD_User_ID(m_user.getAD_User_ID());
				if (getC_BPartner_ID() == 0)
					setC_BPartner_ID(m_user.getC_BPartner_ID());
				else if (m_user.getC_BPartner_ID() != getC_BPartner_ID())
				{
					log.warning("@C_BPartner_ID@ (ID=" + getC_BPartner_ID() 
						+ ") <> @AD_User_ID@ @C_BPartner_ID@ (ID=" + m_user.getC_BPartner_ID() + ")");
				}
			}
		}
	}	//	setAD_User_ID
	
	/**
	 * 	Set AD_User_ID
	 *	@param AD_User_ID user
	 */
	@Override
	public void setAD_User_ID(int AD_User_ID)
	{
	    super.setAD_User_ID(AD_User_ID);
	    getUser();
	}	//	setAD_User_ID
	
	/**
	 * 	Get User
	 *	@return user
	 */
	public MUser getUser()
    {
		if (getAD_User_ID() == 0)
			m_user = null;
		else if (m_user == null 
			|| m_user.getAD_User_ID() != getAD_User_ID())
			m_user = new MUser(getCtx(), getAD_User_ID(), get_Trx());
	    return m_user;
    }	//	getUser
	
	/**
	 * 	Set C_BPartner_ID
	 *	@param C_BPartner_ID bp
	 */
	@Override
	public void setC_BPartner_ID(int C_BPartner_ID)
	{
	    super.setC_BPartner_ID(C_BPartner_ID);
	    getBPartner();
	}	//	setC_BPartner_ID
	
	/**
	 * 	Get BPartner
	 *	@return bp or null
	 */
	public MBPartner getBPartner()
	{
		if (getC_BPartner_ID() == 0)
			m_bp = null;
		else if (m_bp == null 
			|| m_bp.getC_BPartner_ID() != getC_BPartner_ID())
			m_bp = new MBPartner (getCtx(), getC_BPartner_ID(), get_Trx());
		return m_bp;
	}	//	getBPartner
	
	/**
	 * 	Set R_Request_ID
	 *	@param R_Request_ID
	 */
	@Override
	public void setR_Request_ID(int R_Request_ID)
	{
	    super.setR_Request_ID(R_Request_ID);
	    getRequest();
	}	//	setR_Request_ID
	
	/**
	 * 	Get Request
	 *	@return request
	 */
	public MRequest getRequest()
	{
		if (getR_Request_ID() == 0)
			m_request = null;
		else if (m_request == null 
			|| m_request.getR_Request_ID() != getR_Request_ID())
			m_request = new MRequest(getCtx(), getR_Request_ID(), get_Trx());
		return m_request;
	}	//	getRequest
	
	/**
	 * 	Set R_Status_ID
	 *	@see org.compiere.model.X_C_Lead#setR_Status_ID(int)
	 *	@param R_Status_ID
	 */
	@Override
	public void setR_Status_ID(int R_Status_ID)
	{
		if (isR_Status_IDValid(R_Status_ID))
			super.setR_Status_ID (R_Status_ID);
		else
			super.setR_Status_ID (0);
		getStatus();
	}	//	setR_Status_ID

	/**
	 * 	Is R_Status_ID Valid
	 *	@param R_Status_ID id
	 *	@return true if valid
	 */
	public boolean isR_Status_IDValid (int R_Status_ID)
	{
		if (R_Status_ID == 0)
			return true;
		
		m_Status = MStatus.get (getCtx(), R_Status_ID);
		int R_StatusCategory_ID = m_Status.getR_StatusCategory_ID();
		//
		int R_RequestType_ID = getR_RequestType_ID();
		if (R_RequestType_ID == 0)
		{
			log.warning ("No Client Request Type");
			return false;
		}
		MRequestType rt = MRequestType.get (getCtx(), R_RequestType_ID);
		if (rt.getR_StatusCategory_ID() != R_StatusCategory_ID)
		{
			log.warning ("Status Category different - Status(" 
				+ R_StatusCategory_ID + ") <> RequestType(" 
				+ rt.getR_StatusCategory_ID() + ")");
			return false;
		}
		return true;
	}	//	isR_Status_IDValid
	
	/**
	 * 	Get R_RequestType_ID
	 *	@return Request Type
	 */
	private int getR_RequestType_ID()
	{
		MClientInfo ci = MClientInfo.get (getCtx(), getAD_Client_ID());
		int R_RequestType_ID = ci.getR_RequestType_ID();
		if (R_RequestType_ID != 0)
			return R_RequestType_ID;
		log.warning("Set Request Type in Window Client Info");
		
		//	Default
		MRequestType rt = MRequestType.getDefault (getCtx());
		if (rt != null)
		{
			R_RequestType_ID = rt.getR_RequestType_ID();
		//	ci.setR_RequestType_ID(R_RequestType_ID);
		//	ci.save();
			return R_RequestType_ID;
		}
		//
		return 0;
	}	//	getR_RequestType_ID

	/**
	 * 	Get Status
	 *	@return status or null
	 */
	public MStatus getStatus()
	{
		if (getR_Status_ID() == 0)
			m_Status = null;
		else if (m_Status == null 
			|| m_Status.getR_Status_ID() != getR_Status_ID())
			m_Status = MStatus.get (getCtx(), getR_Status_ID()); 
		return m_Status;
	}	//	getStatus

	/**
	 * 	Set C_Project_ID
	 *	@param C_Project_ID project
	 */
	@Override
	public void setC_Project_ID(int C_Project_ID)
	{
	    super.setC_Project_ID(C_Project_ID);
	    getProject();
	}	//	setC_Project_ID
	
	/**
	 * 	Get Project
	 *	@return project or null
	 */
	public MProject getProject()
	{
		if (getC_Project_ID() == 0)
			m_project = null;
		else if (m_project == null 
			|| m_project.getC_Project_ID() != getC_Project_ID())
			m_project = new MProject(getCtx(), getC_Project_ID(), get_Trx());
		return m_project;
	}	//	getProject
	
	/**
	 * 	Get Name
	 *	@return not null value
	 */
	@Override
	public String getName()
	{
    	String name = super.getName();			//	Subject
    	if (name == null)
    	{
    		name = getBPName();					//	BPartner
    		if (name == null)
    		{
    			name = getContactName();		//	Contact
    			if (name == null)
    			{
    				name = getDocumentNo();		//	DocumentNo
    				if (name == null)
    					name = "Lead";
    			}
    		}
    	}
    	return name;
	}	//	getName
	
	/**
     * 	String Representation
     *	@return info
     */
    @Override
	public String toString()
    {
	    StringBuffer sb = new StringBuffer("MLead[");
	    sb.append(get_ID())
	    	.append("-").append(getName()).append("]");
	    return sb.toString();
    }	//	toString
	
    /**
     * 	Create BP, Contact, Location from Lead
     *	@return error message
     */
    public String createBP()
    {
    	if (getC_BPartner_ID() != 0 && getAD_User_ID() != 0 && getC_BPartner_Location_ID() == 0)
    		return "@AlreadyExists@: @C_BPartner_ID@ (ID=" + getC_BPartner_ID() + ")";
    	
    	//	BPartner
    	if (getC_BPartner_ID() == 0 
    		&& (getBPName() != null && getBPName().length() > 0))
    	{
    		//	Existing User
    		m_user = getUser();
    		if (m_user != null)
    		{
    			if (m_user.getC_BPartner_ID() != 0)
    			{
    				setC_BPartner_ID(m_user.getC_BPartner_ID());
    				log.info("Set to BPartner of User - " + m_user);
    				return createBPLocation();
    			}
    		}
    		//
    		m_bp = new MBPartner(getCtx(), get_Trx());	//	Template
    		m_bp.setAD_Org_ID(getAD_Org_ID());
    		m_bp.setValue(getBPName());
    		m_bp.setName(getBPName());
    		m_bp.setIsCustomer(true);
    		//
    		if (getC_BP_Group_ID() != 0)
    			m_bp.setC_BP_Group_ID(getC_BP_Group_ID());
    		if (getC_BP_Size_ID() != 0)
    			m_bp.setC_BP_Size_ID(getC_BP_Size_ID());
    		if (getC_BP_Status_ID() != 0)
    			m_bp.setC_BP_Status_ID(getC_BP_Status_ID());
    		if (getC_IndustryCode_ID() != 0)
    			m_bp.setC_IndustryCode_ID(getC_IndustryCode_ID());
    		if (getNAICS() != null)
    			m_bp.setNAICS(getNAICS());
    		if (getDUNS() != null)
    			m_bp.setDUNS(getDUNS());
    		if (getNumberEmployees() != 0)
    			m_bp.setNumberEmployees(getNumberEmployees());
    		if (getSalesVolume() != 0)
    			m_bp.setSalesVolume(getSalesVolume());
    		if (getSalesRep_ID() != 0)
    			m_bp.setSalesRep_ID(getSalesRep_ID());
    		if (!m_bp.save())
    			return "@SaveError@";
    		//	Update User
    		if (m_user != null && m_user.getC_BPartner_ID() == 0)
    		{
    			m_user.setC_BPartner_ID (m_bp.getC_BPartner_ID());
    			m_user.save();
    		}
    		//	Save BP
    		setC_BPartner_ID(m_bp.getC_BPartner_ID());
    	}	//	BPartner

    	String error = createBPContact();
    	if (error != null && error.length() > 0)
    		return error;
    	return createBPLocation();
    }	//	createBP
    
    /**
     * 	Create BP Contact from Lead
     *	@return error
     */
    private String createBPContact()
    {
    	//	Contact exists
    	if (getAD_User_ID() != 0)
    		return null;
    	
    	//	Something to save
    	if ((getContactName() != null && getContactName().length() > 0)
    		)
    		;
    	else
    	{
    		log.fine("No BP Contact Info to save");
    		return null;
    	}
    		
   		if (m_user == null)
   		{
   			if (m_bp == null)
   				m_user = new MUser(getCtx(), 0, get_Trx());
   			else
   				m_user = new MUser(m_bp);
   		}
   		m_user.setName(getContactName());
   		//
   		if (getC_Job_ID() != 0)
   			m_user.setC_Job_ID(getC_Job_ID());
   		if (getEMail() != null)
   			m_user.setEMail(getEMail());
   		if (getC_Greeting_ID() != 0)
  			m_user.setC_Greeting_ID(getC_Greeting_ID());
   		if (getPhone() != null)
   			m_user.setPhone(getPhone());
   		if (getPhone2() != null)
   			m_user.setPhone2(getPhone2());
   		if (getFax() != null)
   			m_user.setFax(getFax());
   		//
   		if (!m_user.save())
   			log.warning("Contact not saved");
   		else
   			setAD_User_ID(m_user.getAD_User_ID());
   		return null;
    }	//	createBPContact
        
    /**
     * 	Create BP Location from Lead
     *	@return error message
     */
    private String createBPLocation()
    {
    	if (getC_BPartner_Location_ID() != 0
    		|| getC_Country_ID() == 0)	//	mandatory
    		return null;
    	
    	//	Something to save
    	if ((getAddress1() != null && getAddress1().length() > 0)
    		|| (getPostal() != null  && getPostal().length() > 0)
    		|| (getCity() != null  && getCity().length() > 0)
    		|| (getRegionName() != null  && getRegionName().length() > 0)
    	)
    		;
    	else
    	{
    		log.fine("No BP Location Info to save");
    		return null;
    	}
    	
    	//	Address
		MLocation location = new MLocation(getCtx(), getC_Country_ID(), 
			getC_Region_ID(), getCity(), get_Trx());
		location.setAddress1(getAddress1());
		location.setAddress2(getAddress2());
		location.setPostal(getPostal());
		location.setPostal_Add(getPostal_Add());
		location.setRegionName(getRegionName());
		if (location.save())
		{
			MBPartnerLocation bpl = new MBPartnerLocation (m_bp);
			bpl.setC_Location_ID(location.getC_Location_ID());
			bpl.setPhone(getPhone());
			bpl.setPhone2(getPhone2());
			bpl.setFax(getFax());
			if (bpl.save())
				setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
		}
    	return null;
    }	//	createBPLocation
    
    /**
     * 	Create Project from Lead
     *	@return error message
     */
    public String createProject(int C_ProjectType_ID)
    {
    	if (getC_Project_ID() != 0)
    		return "@AlreadyExists@: @C_Project_ID@ (ID=" + getC_Project_ID() + ")";
    	if (getC_BPartner_ID() == 0)
    	{
    		String retValue = createBP();
    		if (retValue != null)
    			return retValue;
    	}
    	m_project = new MProject(getCtx(), 0, get_Trx());
    	m_project.setAD_Org_ID(getAD_Org_ID());
    	m_project.setProjectLineLevel(X_C_Project.PROJECTLINELEVEL_Project);
    	m_project.setName(getName());
    	m_project.setDescription(getDescription());
    	m_project.setNote(getHelp());
    	//
    	m_project.setC_BPartner_ID(getC_BPartner_ID());
    	m_project.setC_BPartner_Location_ID(getC_BPartner_Location_ID());
		m_project.setAD_User_ID(getAD_User_ID());
		m_project.setC_BPartnerSR_ID(getC_BPartnerSR_ID());
		m_project.setC_Campaign_ID(getC_Campaign_ID());

		m_project.setC_ProjectType_ID(C_ProjectType_ID);
		m_project.setSalesRep_ID(getSalesRep_ID());
		m_project.setC_SalesRegion_ID (getC_SalesRegion_ID());
		if (!m_project.save())
			return "@SaveError@";
		//
		if (getRequest() != null)
		{
			m_request.setC_Project_ID(m_project.getC_Project_ID());
			m_request.save();
		}
		//
    	setC_Project_ID(m_project.getC_Project_ID());
    	return null;
    }	//	createProject
    
    /**
     * 	Create Request from Lead
     * 	@param R_RequestType_ID request type
     *	@return error message
     */
    public String createRequest()
    {
    	int R_RequestType_ID = getR_RequestType_ID();
    	if (R_RequestType_ID == 0)
    		return "@NotFound@: @R_RequestType_ID@ (@AD_Client_ID@)";
    	return createRequest (R_RequestType_ID);
    }	//	createRequest
    
    /**
     * 	Create Request from Lead
     * 	@param R_RequestType_ID request type
     *	@return error message
     */
    private String createRequest(int R_RequestType_ID)
    {
    	if (getR_Request_ID() != 0)
    		return "@AlreadyExists@: @R_Request_ID@ (ID=" + getR_Request_ID() + ")";
    	if (getC_BPartner_ID() == 0)
    	{
    		String retValue = createBP();
    		if (retValue != null)
    			return retValue;
    	}
    	m_request = new MRequest(getCtx(), 0, get_Trx());
    	m_request.setAD_Org_ID(getAD_Org_ID());
    	String summary = getName();
    	if (summary == null)
    		summary = getHelp();
    	if (summary == null)
    		summary = getSummary();
    	if (summary == null)
    		summary = getDescription();
    	m_request.setSummary(summary);
    	//
    	m_request.setR_RequestType_ID(R_RequestType_ID);
    	if (isR_Status_IDValid (getR_Status_ID()))
    		m_request.setR_Status_ID(getR_Status_ID());
    	else
    		m_request.setR_Status_ID();
    	//
    	m_request.setC_Lead_ID (getC_Lead_ID());
    	//
    	m_request.setC_BPartner_ID(getC_BPartner_ID());
		m_request.setAD_User_ID(getAD_User_ID());
		m_request.setC_Project_ID(getC_Project_ID());
		m_request.setC_Campaign_ID(getC_Campaign_ID());
		m_request.setR_Source_ID (getR_Source_ID());
		m_request.setC_BPartnerSR_ID (getC_BPartnerSR_ID());
		m_request.setC_SalesRegion_ID (getC_SalesRegion_ID());

		m_request.setSalesRep_ID(getSalesRep_ID());
		if (!m_request.save())
			return "@SaveError@";
		//
    	setR_Request_ID(m_request.getR_Request_ID());    	
    	return null;
    }	//	createRequest
    
    
    /**
     * 	Before Save
     *	@param newRecord new
     *	@return true
     */
    @Override
	protected boolean beforeSave(boolean newRecord)
    {
    	//	EMail Address specified
    	if (getEMail() != null && getAD_User_ID() == 0)
    		setAD_User_ID();
    	
    	if (newRecord || is_ValueChanged("R_Status_ID"))
    	{
    		if (!isR_Status_IDValid (getR_Status_ID()))
    			setR_Status_ID (0);
    		else if (m_Status != null)
    			setProcessed(m_Status.isClosed());
    	}
    	
        return true;
    }	//	beforeSave
	
    
    /**
     * 	After Save
     *	@param newRecord new
     *	@param success success
     *	@returnsuccess
     */
    @Override
	protected boolean afterSave(boolean newRecord, boolean success)
    {
    	if (!success)
    		return success;
    	
    	//	Create Contact Interest
    	if (getAD_User_ID() != 0 && getR_InterestArea_ID() != 0
    		&& (is_ValueChanged("AD_User_ID") || is_ValueChanged("R_InterestArea_ID")))
    	{
    		MContactInterest ci = MContactInterest.get (getCtx(),
    			getR_InterestArea_ID(), getAD_User_ID(), 
    			true, get_Trx());
			ci.save();		//	don't subscribe or re-activate
    	}
    	return true;
    }	//	afterSave
    
    /**
     * 	Test
     *	@param args
     */
    public static void main(String[] args)
    {
    	Compiere.startup(true);
    	
		HashMap<String,String> parameter = new HashMap<String,String>();
		parameter.put("EMail","test1"); 
		parameter.put("SalesRep_ID","0"); 
		parameter.put("AD_Org_ID","0"); 
		parameter.put("Submit","Submit"); 
		parameter.put("ForwardTo","http://www.compiere.com"); 
		parameter.put("ContactName","Test 1"); 
		parameter.put("Name","Test 1"); 
		parameter.put("AD_Client_ID","11"); 
		parameter.put("R_Source_ID","100");
   
    	MLead lead = new MLead (Env.getCtx(), parameter, null);
		lead.save();
		System.out.println(lead);
    }	//	main
    
    
}	//	MLead
