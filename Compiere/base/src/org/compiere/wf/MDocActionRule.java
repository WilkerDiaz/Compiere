package org.compiere.wf;

import org.compiere.model.*;
import org.compiere.util.*;


/**
 * 	Document Action Transition Rule
 *	@author Jorg Janke
 */
public class MDocActionRule
{
	/**
	 *	General Rule Constructor
	 *	@param docBaseType document base type
	 *	@param docStatus start document status (if null, applies to all)
	 *	@param docAction transition document action
	 *	@param enable if true enable transition if false delete it
	 */
	public MDocActionRule(String docBaseType,
		String docStatus, String docAction, boolean enable)
	{
		this (docBaseType, docStatus, docAction, enable, 0);
	}	//	MDocActionRule

	/**
	 *	Specific Rule Constructor
	 *	@param docBaseType document base type
	 *	@param docStatus start document status (if null, applies to all)
	 *	@param docAction transition document action
	 *	@param enable if true enable transition if false delete it
     *	@param AD_Client_ID if 0 for all otherwise for specific tenant
	 */
	public MDocActionRule(String docBaseType,
		String docStatus, String docAction, boolean enable, int AD_Client_ID)
	{
		m_docBaseType = docBaseType;
		m_docStatus = docStatus;
		m_docAction = docAction;
		m_enable = enable;
		m_AD_Client_ID = AD_Client_ID;
	}	//	MDocActionRule

	/**
	 *	General Rule Constructor
	 *	@param AD_Table_ID document table
	 *	@param docStatus start document status (if null, applies to all)
	 *	@param docAction transition document action
	 *	@param enable if true enable transition if false delete it
	 */
	public MDocActionRule(int AD_Table_ID,
		String docStatus, String docAction, boolean enable)
	{
		this (null, docStatus, docAction, enable, 0);
		m_AD_Table_ID = AD_Table_ID;
	}	//	MDocActionRule

	/**
	 *	Specific Rule Constructor
	 *	@param AD_Table_ID document table
	 *	@param docStatus start document status (if null, applies to all)
	 *	@param docAction transition document action
	 *	@param enable if true enable transition if false delete it
     *	@param AD_Client_ID if 0 for all otherwise for specific tenant
	 */
	public MDocActionRule(int AD_Table_ID,
		String docStatus, String docAction, boolean enable, int AD_Client_ID)
	{
		this (null, docStatus, docAction, enable, 0);
		m_AD_Table_ID = AD_Table_ID;
	}	//	MDocActionRule

	/**	Document Base Type		*/
	private String		 	m_docBaseType = null;
	/* Document Table			*/
	private int				m_AD_Table_ID = 0;

	private final String	m_docStatus;
	private final String	m_docAction;
	private final boolean	m_enable;
	private final int		m_AD_Client_ID;

    /**
     * @return the docBaseType
     */
    public String getDocBaseType()
    {
    	return m_docBaseType;
    }

    /**
     * 	Does the rule apply for the document base type
     *	@param docBaseType document base type
     *	@return true if rule applies
     */
    public boolean isDocBaseType (String docBaseType)
    {
    	if (Util.isEmpty(docBaseType))
    		return false;
    	if (docBaseType.equals(m_docBaseType))
    		return true;
    	if ((m_docBaseType != null) || (m_AD_Table_ID == 0))
    		return false;
    	//
    	MDocBaseType[] baseTypes = MDocBaseType.getAll(Env.getCtx());
    	for (MDocBaseType type : baseTypes)
        {
        	if (type.getAD_Table_ID() != m_AD_Table_ID)
        		continue;
	        if ((type.getAD_Client_ID() == 0)
	        	|| (type.getAD_Client_ID() == m_AD_Client_ID))
	        {
	        	if (type.getDocBaseType().equals(docBaseType))
	        		return true;
	        }
        }
    	return false;
    }	//	isDocBaseType

    /**
     * @return the docStatus
     */
    public String getDocStatus()
    {
    	return m_docStatus;
    }

    /**
     * 	Does the rule apply for the Document Status
     *	@param docStatus status
     *	@return true if applies
     */
    public boolean isDocStatus(String docStatus)
    {
    	if (m_docStatus == null)
    		return true;
    	if (Util.isEmpty(docStatus))
    		return false;
    	return m_docStatus.equals(docStatus);
    }	//	isDocStatus

    /**
     * @return the docAction
     */
    public String getDocAction()
    {
    	return m_docAction;
    }

    /**
     * @return the enable
     */
    public boolean isEnabled()
    {
    	return m_enable;
    }

    /**
     * @return the AD_Client_ID
     */
    public int getAD_Client_ID()
    {
    	return m_AD_Client_ID;
    }

    /**
     * 	Does Rule apply for AD_Client_ID
     *	@param AD_Client_ID tenant
     *	@return true if general of tenant specific rule
     */
    public boolean isAD_Client_ID (int AD_Client_ID)
    {
    	if (m_AD_Client_ID == 0)
    		return true;
    	return AD_Client_ID == m_AD_Client_ID;
    }	//	isAD_Client_ID

    /**
     * @return the AD_Table_ID
     */
    public int getAD_Table_ID()
    {
    	return m_AD_Table_ID;
    }

}	//	MDocActionRule
