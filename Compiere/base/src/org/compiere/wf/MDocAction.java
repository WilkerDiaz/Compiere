package org.compiere.wf;

import java.util.*;

import org.compiere.model.*;
import org.compiere.util.*;
import org.compiere.vos.*;

/**
 * 	Document Action Model.
 *	@author Jorg Janke
 */
public class MDocAction
{
	/**
	 * 	Get general Doc Action Model.
	 *	@return Doc Action Model
	 */
	public static MDocAction get()
	{
		return get(0, true);
	}	//	get

	/**
	 * 	Get general Doc Action Model or for specific tenant (if rules exist)
	 *	@param AD_Client_ID tenant
	 *	@return Doc Action Model
	 */
	public static MDocAction get (int AD_Client_ID)
	{
		return get(AD_Client_ID, false);
	}	//	get

	/**
	 * 	Get Doc Action Model for specific tenant
	 *	@param AD_Client_ID tenant
	 *	@param createIfNotExists if false, the general model is returned
	 *	@return Doc Action Model
	 */
	public static MDocAction get (int AD_Client_ID, boolean createIfNotExists)
	{
	    MDocAction retValue = s_cache.get(AD_Client_ID);
	    if (retValue != null)
		    return retValue;
	    //	None Found
	    if (!createIfNotExists)
	    {
	    	AD_Client_ID = 0;
	    	retValue = s_cache.get(AD_Client_ID);
	    }
	    if (retValue == null)
	    {
	    	retValue = new MDocAction(AD_Client_ID);
	    	s_cache.put(AD_Client_ID, retValue);
	    }
	    return retValue;
	}	//	get

	/**
	 * 	Clear Doc action Cache
	 */
	public static void resetCache()
	{
		s_cache.clear();
	}	//	resetCache

    /**	DocAction Cache					*/
    private static HashMap<Integer, MDocAction> s_cache
    	= new HashMap<Integer, MDocAction>(10);

    /**
     * 	Add a transition Rule
     *	@param rule rule
     *	@return true if added
     */
    public static boolean addRule(MDocActionRule rule)
    {
    	if (s_rules.size() == 0)
    		addDefaultRules();

    	if ((rule != null)
    		&& Util.isEmpty(rule.getDocAction()))
    	{
    		return s_rules.add(rule);
    	}
   		return false;
    }	//	addRule

    /**
     * 	Get the Rules
     *	@return additional rules
     */
    public static ArrayList<MDocActionRule> getRules()
    {
    	if (s_rules.size() == 0)
    		addDefaultRules();
    	return s_rules;
    }

    /**
     * 	Add the Default Rules
     */
    static void addDefaultRules()
    {
    }	//	addDefaultRules

    /** The Rules						*/
    private static ArrayList<MDocActionRule>	s_rules
    	= new ArrayList<MDocActionRule>(40);


    /**	Logger	*/
    private static CLogger log = CLogger.getCLogger(MDocAction.class);

	/**************************************************************************
	 * 	Private Constructor
	 *	@param AD_Client_ID tenant
	 */
	private MDocAction(int AD_Client_ID)
	{
		m_AD_Client_ID = AD_Client_ID;
		//	fill Doc Statuss

		//	fill Doc Actions
	}	//	MDocAction

	/** The Tenant					*/
	private int m_AD_Client_ID = 0;

	private final ArrayList<String>	m_docStatuss = new ArrayList<String>();

	private final ArrayList<String>	m_docActions = new ArrayList<String>();

	/**
	 * 	Return list of Doc Actions for Doc Base Type and Doc Status
	 * 	@param docBaseType document base type
	 *	@param docStatus document status
	 *	@return list of doc actions
	 */
	public ArrayList<String> getDocActions (String docBaseType, String docStatus)
	{
		ArrayList<String> actions = new ArrayList<String>();

		//	Locked
		String Processing = null;
		if ("Y".equals(Processing))
			actions.add(DocActionConstants.ACTION_Unlock);

		//	No Status
		if (Util.isEmpty(docStatus))
			return actions;

		//	Approval required           ..  NA
		if (docStatus.equals(DocActionConstants.STATUS_NotApproved))
		{
			actions.add(DocActionConstants.ACTION_Prepare);
			actions.add(DocActionConstants.ACTION_Void);
		}
		//	Draft/Invalid				..  DR/IN
		else if (docStatus.equals(DocActionConstants.STATUS_Drafted)
			|| docStatus.equals(DocActionConstants.STATUS_Invalid))
		{
			actions.add(DocActionConstants.ACTION_Complete);
		//	actions.add(DocumentEngine.ACTION_Prepare);
			actions.add(DocActionConstants.ACTION_Void);
		}
		//	In Process                  ..  IP
		else if (docStatus.equals(DocActionConstants.STATUS_InProgress)
			|| docStatus.equals(DocActionConstants.STATUS_Approved))
		{
			actions.add(DocActionConstants.ACTION_Complete);
			actions.add(DocActionConstants.ACTION_Void);
		}
		//	Complete                    ..  CO
		else if (docStatus.equals(DocActionConstants.STATUS_Completed))
		{
			actions.add(DocActionConstants.ACTION_Close);
		}
		//	Waiting Payment
		else if (docStatus.equals(DocActionConstants.STATUS_WaitingPayment)
			|| docStatus.equals(DocActionConstants.STATUS_WaitingConfirmation))
		{
			actions.add(DocActionConstants.ACTION_Void);
			actions.add(DocActionConstants.ACTION_Prepare);
		}
		//	Closed, Voided, REversed    ..  CL/VO/RE
		else if (docStatus.equals(DocActionConstants.STATUS_Closed)
			|| docStatus.equals(DocActionConstants.STATUS_Voided)
			|| docStatus.equals(DocActionConstants.STATUS_Reversed))
			;

		//	No DocBaseType
		if (Util.isEmpty(docBaseType))
			return actions;

		addStandardActions(docBaseType, actions);

		//	Apply Rules
		ArrayList<MDocActionRule> rules = getRules();
		for (MDocActionRule rule : rules)
        {
			//	Check Client, DocBaseType, Status
			if (!rule.isAD_Client_ID(m_AD_Client_ID))
				continue;
			if (!rule.isDocBaseType(docBaseType))
				continue;
			if (!rule.isDocStatus(docStatus))
				continue;

			//	Check Action
			String da = rule.getDocAction();
			if (Util.isEmpty(da))
				continue;

			//	Apply Rule
			if (rule.isEnabled())
			{
				if (!actions.contains(da))		//	add if not exists
					actions.add(da);
			}
			else
				actions.remove(da);				// remove
        }
		log.config("#" + actions.size());
		return actions;
	}	//	getDocActions


	private void addStandardActions(String docBaseType, ArrayList<String> actions)
	{
		int AD_Table_ID = 0;
    	MDocBaseType[] baseTypes = MDocBaseType.getAll(Env.getCtx());
    	for (MDocBaseType type : baseTypes)
        {
    		if (type.getDocBaseType().equals(docBaseType))
    		{
    			AD_Table_ID = type.getAD_Table_ID();
    			break;
    		}
        }
    	if (AD_Table_ID == 0)
    		return;


		/********************
		 *  Order
		 *
		if (m_AD_Table_ID == X_C_Order.Table_ID)
		{
			//	Draft                       ..  DR/IP/IN
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Drafted)
				|| DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_InProgress)
				|| DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Invalid))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Prepare;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Close;
				//	Draft Sales Order Quote/Proposal - Process
				if (isSOTrx
					&& ("OB".equals(OrderType) || "ON".equals(OrderType)))
					DocAction = org.compiere.vos.DocActionConstants.ACTION_Prepare;
			}
			//	Complete                    ..  CO
			else if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_ReActivate;
			}
			else if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_WaitingPayment))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_ReActivate;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Close;
			}
		}
		/********************
		 *  Shipment
		 *
		else if (m_AD_Table_ID == X_M_InOut.Table_ID)
		{
			//	Complete                    ..  CO
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Reverse_Correct;
			}
		}
		/********************
		 *  Invoice
		 *
		else if (m_AD_Table_ID == X_C_Invoice.Table_ID)
		{
			//	Complete                    ..  CO
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Reverse_Correct;
			}
		}

		/********************
		 *  Cash Journal
		 *
		else if (m_AD_Table_ID == X_C_Cash.Table_ID)
		{
			//	Draft                    ..  DR
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Drafted))
			{
				//Void is not a valid option for Cash Journal in Drafted status
				int t1,t2;
				for(t1=0,t2=0;t1<index;t1++,t2++)
				{
					if(	options[t1] == org.compiere.vos.DocActionConstants.ACTION_Void)
						t2++;
					options[t1]=options[t2];
				}
				index--;
			}
		}


		/********************
		 *  Payment
		 *
		else if (m_AD_Table_ID == X_C_Payment.Table_ID)
		{
			//	Complete                    ..  CO
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Reverse_Correct;
			}
		}
		/********************
		 *  GL Journal
		 *
		else if ((m_AD_Table_ID == X_GL_Journal.Table_ID) || (m_AD_Table_ID == X_GL_JournalBatch.Table_ID))
		{
			//	Complete                    ..  CO
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Reverse_Correct;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Reverse_Accrual;
			}
		}
		/********************
		 *  Allocation
		 *
		else if (m_AD_Table_ID == X_C_AllocationHdr.Table_ID)
		{
			//	Complete                    ..  CO
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Reverse_Correct;
			}
		}
		/********************
		 *  Bank Statement
		 *
		else if (m_AD_Table_ID == X_C_BankStatement.Table_ID)
		{
			//	Complete                    ..  CO
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
			}
		}
		/********************
		 *  Inventory Movement, Physical Inventory
		 *
		else if ((m_AD_Table_ID == X_M_Movement.Table_ID)
			|| (m_AD_Table_ID == X_M_Inventory.Table_ID))
		{
			//	Complete                    ..  CO
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Reverse_Correct;
			}
		}
		/********************
		 *  Warehouse Task
		 *
		else if (m_AD_Table_ID == X_M_WarehouseTask.Table_ID)
		{
			//	Draft                       ..  DR/IP/IN
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Drafted)
				|| DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_InProgress)
				|| DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Invalid))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Prepare;
			}
			//	Complete                    ..  CO
			else if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Reverse_Correct;
			}
		}
		/********************
		 *  Work Order
		 *
		else if (m_AD_Table_ID == X_M_WorkOrder.Table_ID)
		{
			//	Draft                       ..  DR/IP/IN
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Drafted)
				|| DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_InProgress)
				|| DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Invalid))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Prepare;
			}
			//	Complete                    ..  CO
			else if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
			}
		}
		/********************
		 *  Work Order Transaction
		 *
		else if (m_AD_Table_ID == X_M_WorkOrderTransaction.Table_ID)
		{
			//	Draft                       ..  DR/IP/IN
			if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Drafted)
				|| DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_InProgress)
				|| DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Invalid))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Prepare;
			}
			//	Complete                    ..  CO
			else if (DocStatus.equals(org.compiere.vos.DocActionConstants.STATUS_Completed))
			{
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Void;
				options[index++] = org.compiere.vos.DocActionConstants.ACTION_Reverse_Correct;
			}
		}
		/**/
	}	//

}	//	MDocAction
