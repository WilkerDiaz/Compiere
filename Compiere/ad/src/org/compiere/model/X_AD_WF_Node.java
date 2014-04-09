/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for AD_WF_Node
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_WF_Node.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_WF_Node extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_WF_Node_ID id
    @param trx transaction
    */
    public X_AD_WF_Node (Ctx ctx, int AD_WF_Node_ID, Trx trx)
    {
        super (ctx, AD_WF_Node_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_WF_Node_ID == 0)
        {
            setAD_WF_Node_ID (0);
            setAD_Workflow_ID (0);
            setAction (null);	// N
            setDuration (0);
            setDurationLimit (0);
            setEntityType (null);	// U
            setIsCentrallyMaintained (true);	// Y
            setJoinElement (null);	// X
            setName (null);
            setSplitElement (null);	// X
            setValue (null);
            setWaitingTime (0);
            setXPosition (0);
            setYPosition (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_WF_Node (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=129 */
    public static final int Table_ID=129;
    
    /** TableName=AD_WF_Node */
    public static final String Table_Name="AD_WF_Node";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID <= 0) set_Value ("AD_Column_ID", null);
        else
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set Special Form.
    @param AD_Form_ID Special Form */
    public void setAD_Form_ID (int AD_Form_ID)
    {
        if (AD_Form_ID <= 0) set_Value ("AD_Form_ID", null);
        else
        set_Value ("AD_Form_ID", Integer.valueOf(AD_Form_ID));
        
    }
    
    /** Get Special Form.
    @return Special Form */
    public int getAD_Form_ID() 
    {
        return get_ValueAsInt("AD_Form_ID");
        
    }
    
    /** Set Image.
    @param AD_Image_ID Image or Icon */
    public void setAD_Image_ID (int AD_Image_ID)
    {
        if (AD_Image_ID <= 0) set_Value ("AD_Image_ID", null);
        else
        set_Value ("AD_Image_ID", Integer.valueOf(AD_Image_ID));
        
    }
    
    /** Get Image.
    @return Image or Icon */
    public int getAD_Image_ID() 
    {
        return get_ValueAsInt("AD_Image_ID");
        
    }
    
    /** Set Process.
    @param AD_Process_ID Process or Report */
    public void setAD_Process_ID (int AD_Process_ID)
    {
        if (AD_Process_ID <= 0) set_Value ("AD_Process_ID", null);
        else
        set_Value ("AD_Process_ID", Integer.valueOf(AD_Process_ID));
        
    }
    
    /** Get Process.
    @return Process or Report */
    public int getAD_Process_ID() 
    {
        return get_ValueAsInt("AD_Process_ID");
        
    }
    
    /** Set OS Task.
    @param AD_Task_ID Operation System Task */
    public void setAD_Task_ID (int AD_Task_ID)
    {
        if (AD_Task_ID <= 0) set_Value ("AD_Task_ID", null);
        else
        set_Value ("AD_Task_ID", Integer.valueOf(AD_Task_ID));
        
    }
    
    /** Get OS Task.
    @return Operation System Task */
    public int getAD_Task_ID() 
    {
        return get_ValueAsInt("AD_Task_ID");
        
    }
    
    /** Set Workflow Block.
    @param AD_WF_Block_ID Workflow Transaction Execution Block */
    public void setAD_WF_Block_ID (int AD_WF_Block_ID)
    {
        if (AD_WF_Block_ID <= 0) set_Value ("AD_WF_Block_ID", null);
        else
        set_Value ("AD_WF_Block_ID", Integer.valueOf(AD_WF_Block_ID));
        
    }
    
    /** Get Workflow Block.
    @return Workflow Transaction Execution Block */
    public int getAD_WF_Block_ID() 
    {
        return get_ValueAsInt("AD_WF_Block_ID");
        
    }
    
    /** Set Node.
    @param AD_WF_Node_ID Workflow Node (activity), step or process */
    public void setAD_WF_Node_ID (int AD_WF_Node_ID)
    {
        if (AD_WF_Node_ID < 1) throw new IllegalArgumentException ("AD_WF_Node_ID is mandatory.");
        set_ValueNoCheck ("AD_WF_Node_ID", Integer.valueOf(AD_WF_Node_ID));
        
    }
    
    /** Get Node.
    @return Workflow Node (activity), step or process */
    public int getAD_WF_Node_ID() 
    {
        return get_ValueAsInt("AD_WF_Node_ID");
        
    }
    
    /** Set Workflow Owner.
    @param AD_WF_Responsible_ID Responsible for Workflow Execution */
    public void setAD_WF_Responsible_ID (int AD_WF_Responsible_ID)
    {
        if (AD_WF_Responsible_ID <= 0) set_Value ("AD_WF_Responsible_ID", null);
        else
        set_Value ("AD_WF_Responsible_ID", Integer.valueOf(AD_WF_Responsible_ID));
        
    }
    
    /** Get Workflow Owner.
    @return Responsible for Workflow Execution */
    public int getAD_WF_Responsible_ID() 
    {
        return get_ValueAsInt("AD_WF_Responsible_ID");
        
    }
    
    /** Set Window.
    @param AD_Window_ID Data entry or display window */
    public void setAD_Window_ID (int AD_Window_ID)
    {
        if (AD_Window_ID <= 0) set_Value ("AD_Window_ID", null);
        else
        set_Value ("AD_Window_ID", Integer.valueOf(AD_Window_ID));
        
    }
    
    /** Get Window.
    @return Data entry or display window */
    public int getAD_Window_ID() 
    {
        return get_ValueAsInt("AD_Window_ID");
        
    }
    
    /** Set Workflow.
    @param AD_Workflow_ID Workflow or combination of tasks */
    public void setAD_Workflow_ID (int AD_Workflow_ID)
    {
        if (AD_Workflow_ID < 1) throw new IllegalArgumentException ("AD_Workflow_ID is mandatory.");
        set_ValueNoCheck ("AD_Workflow_ID", Integer.valueOf(AD_Workflow_ID));
        
    }
    
    /** Get Workflow.
    @return Workflow or combination of tasks */
    public int getAD_Workflow_ID() 
    {
        return get_ValueAsInt("AD_Workflow_ID");
        
    }
    
    /** User Workbench = B */
    public static final String ACTION_UserWorkbench = X_Ref_WF_Action.USER_WORKBENCH.getValue();
    /** User Choice = C */
    public static final String ACTION_UserChoice = X_Ref_WF_Action.USER_CHOICE.getValue();
    /** Document Action = D */
    public static final String ACTION_DocumentAction = X_Ref_WF_Action.DOCUMENT_ACTION.getValue();
    /** Sub Workflow = F */
    public static final String ACTION_SubWorkflow = X_Ref_WF_Action.SUB_WORKFLOW.getValue();
    /** EMail = M */
    public static final String ACTION_EMail = X_Ref_WF_Action.E_MAIL.getValue();
    /** Apps Process = P */
    public static final String ACTION_AppsProcess = X_Ref_WF_Action.APPS_PROCESS.getValue();
    /** Apps Report = R */
    public static final String ACTION_AppsReport = X_Ref_WF_Action.APPS_REPORT.getValue();
    /** Apps Task = T */
    public static final String ACTION_AppsTask = X_Ref_WF_Action.APPS_TASK.getValue();
    /** Set Variable = V */
    public static final String ACTION_SetVariable = X_Ref_WF_Action.SET_VARIABLE.getValue();
    /** User Window = W */
    public static final String ACTION_UserWindow = X_Ref_WF_Action.USER_WINDOW.getValue();
    /** User Form = X */
    public static final String ACTION_UserForm = X_Ref_WF_Action.USER_FORM.getValue();
    /** Wait (Sleep) = Z */
    public static final String ACTION_WaitSleep = X_Ref_WF_Action.WAIT_SLEEP.getValue();
    /** Set Action.
    @param Action Indicates the Action to be performed */
    public void setAction (String Action)
    {
        if (Action == null) throw new IllegalArgumentException ("Action is mandatory");
        if (!X_Ref_WF_Action.isValid(Action))
        throw new IllegalArgumentException ("Action Invalid value - " + Action + " - Reference_ID=302 - B - C - D - F - M - P - R - T - V - W - X - Z");
        set_Value ("Action", Action);
        
    }
    
    /** Get Action.
    @return Indicates the Action to be performed */
    public String getAction() 
    {
        return (String)get_Value("Action");
        
    }
    
    /** Set Attribute Name.
    @param AttributeName Name of the Attribute */
    public void setAttributeName (String AttributeName)
    {
        set_Value ("AttributeName", AttributeName);
        
    }
    
    /** Get Attribute Name.
    @return Name of the Attribute */
    public String getAttributeName() 
    {
        return (String)get_Value("AttributeName");
        
    }
    
    /** Set Attribute Value.
    @param AttributeValue Value of the Attribute */
    public void setAttributeValue (String AttributeValue)
    {
        set_Value ("AttributeValue", AttributeValue);
        
    }
    
    /** Get Attribute Value.
    @return Value of the Attribute */
    public String getAttributeValue() 
    {
        return (String)get_Value("AttributeValue");
        
    }
    
    /** Set Cost.
    @param Cost Cost information */
    public void setCost (java.math.BigDecimal Cost)
    {
        set_Value ("Cost", Cost);
        
    }
    
    /** Get Cost.
    @return Cost information */
    public java.math.BigDecimal getCost() 
    {
        return get_ValueAsBigDecimal("Cost");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** <None> = -- */
    public static final String DOCACTION_None = X_Ref__Document_Action.NONE.getValue();
    /** Approve = AP */
    public static final String DOCACTION_Approve = X_Ref__Document_Action.APPROVE.getValue();
    /** Close = CL */
    public static final String DOCACTION_Close = X_Ref__Document_Action.CLOSE.getValue();
    /** Complete = CO */
    public static final String DOCACTION_Complete = X_Ref__Document_Action.COMPLETE.getValue();
    /** Invalidate = IN */
    public static final String DOCACTION_Invalidate = X_Ref__Document_Action.INVALIDATE.getValue();
    /** Post = PO */
    public static final String DOCACTION_Post = X_Ref__Document_Action.POST.getValue();
    /** Prepare = PR */
    public static final String DOCACTION_Prepare = X_Ref__Document_Action.PREPARE.getValue();
    /** Reverse - Accrual = RA */
    public static final String DOCACTION_Reverse_Accrual = X_Ref__Document_Action.REVERSE__ACCRUAL.getValue();
    /** Reverse - Correct = RC */
    public static final String DOCACTION_Reverse_Correct = X_Ref__Document_Action.REVERSE__CORRECT.getValue();
    /** Re-activate = RE */
    public static final String DOCACTION_Re_Activate = X_Ref__Document_Action.RE__ACTIVATE.getValue();
    /** Reject = RJ */
    public static final String DOCACTION_Reject = X_Ref__Document_Action.REJECT.getValue();
    /** Void = VO */
    public static final String DOCACTION_Void = X_Ref__Document_Action.VOID.getValue();
    /** Wait Complete = WC */
    public static final String DOCACTION_WaitComplete = X_Ref__Document_Action.WAIT_COMPLETE.getValue();
    /** Unlock = XL */
    public static final String DOCACTION_Unlock = X_Ref__Document_Action.UNLOCK.getValue();
    /** Set Document Action.
    @param DocAction The targeted status of the document */
    public void setDocAction (String DocAction)
    {
        if (!X_Ref__Document_Action.isValid(DocAction))
        throw new IllegalArgumentException ("DocAction Invalid value - " + DocAction + " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");
        set_Value ("DocAction", DocAction);
        
    }
    
    /** Get Document Action.
    @return The targeted status of the document */
    public String getDocAction() 
    {
        return (String)get_Value("DocAction");
        
    }
    
    /** Set Duration.
    @param Duration Normal Duration in Duration Unit */
    public void setDuration (int Duration)
    {
        set_Value ("Duration", Integer.valueOf(Duration));
        
    }
    
    /** Get Duration.
    @return Normal Duration in Duration Unit */
    public int getDuration() 
    {
        return get_ValueAsInt("Duration");
        
    }
    
    /** Set Duration Limit.
    @param DurationLimit Maximum Duration in Duration Unit */
    public void setDurationLimit (int DurationLimit)
    {
        set_Value ("DurationLimit", Integer.valueOf(DurationLimit));
        
    }
    
    /** Get Duration Limit.
    @return Maximum Duration in Duration Unit */
    public int getDurationLimit() 
    {
        return get_ValueAsInt("DurationLimit");
        
    }
    
    /** Set Dynamic Priority Change.
    @param DynPriorityChange Change of priority when Activity is suspended waiting for user */
    public void setDynPriorityChange (java.math.BigDecimal DynPriorityChange)
    {
        set_Value ("DynPriorityChange", DynPriorityChange);
        
    }
    
    /** Get Dynamic Priority Change.
    @return Change of priority when Activity is suspended waiting for user */
    public java.math.BigDecimal getDynPriorityChange() 
    {
        return get_ValueAsBigDecimal("DynPriorityChange");
        
    }
    
    /** Day = D */
    public static final String DYNPRIORITYUNIT_Day = X_Ref__Frequency_Type.DAY.getValue();
    /** Hour = H */
    public static final String DYNPRIORITYUNIT_Hour = X_Ref__Frequency_Type.HOUR.getValue();
    /** Minute = M */
    public static final String DYNPRIORITYUNIT_Minute = X_Ref__Frequency_Type.MINUTE.getValue();
    /** Set Dynamic Priority Unit.
    @param DynPriorityUnit Change of priority when Activity is suspended waiting for user */
    public void setDynPriorityUnit (String DynPriorityUnit)
    {
        if (!X_Ref__Frequency_Type.isValid(DynPriorityUnit))
        throw new IllegalArgumentException ("DynPriorityUnit Invalid value - " + DynPriorityUnit + " - Reference_ID=221 - D - H - M");
        set_Value ("DynPriorityUnit", DynPriorityUnit);
        
    }
    
    /** Get Dynamic Priority Unit.
    @return Change of priority when Activity is suspended waiting for user */
    public String getDynPriorityUnit() 
    {
        return (String)get_Value("DynPriorityUnit");
        
    }
    
    /** Set EMail Address.
    @param EMail Electronic Mail Address */
    public void setEMail (String EMail)
    {
        set_Value ("EMail", EMail);
        
    }
    
    /** Get EMail Address.
    @return Electronic Mail Address */
    public String getEMail() 
    {
        return (String)get_Value("EMail");
        
    }
    
    /** Document Business Partner = B */
    public static final String EMAILRECIPIENT_DocumentBusinessPartner = X_Ref_AD_WF_Node_EMailRecipient.DOCUMENT_BUSINESS_PARTNER.getValue();
    /** Document Owner = D */
    public static final String EMAILRECIPIENT_DocumentOwner = X_Ref_AD_WF_Node_EMailRecipient.DOCUMENT_OWNER.getValue();
    /** WF Owner = R */
    public static final String EMAILRECIPIENT_WFOwner = X_Ref_AD_WF_Node_EMailRecipient.WF_OWNER.getValue();
    /** Set EMail Recipient.
    @param EMailRecipient Recipient of the EMail */
    public void setEMailRecipient (String EMailRecipient)
    {
        if (!X_Ref_AD_WF_Node_EMailRecipient.isValid(EMailRecipient))
        throw new IllegalArgumentException ("EMailRecipient Invalid value - " + EMailRecipient + " - Reference_ID=363 - B - D - R");
        set_Value ("EMailRecipient", EMailRecipient);
        
    }
    
    /** Get EMail Recipient.
    @return Recipient of the EMail */
    public String getEMailRecipient() 
    {
        return (String)get_Value("EMailRecipient");
        
    }
    
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        set_Value ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
    }
    
    /** Automatic = A */
    public static final String FINISHMODE_Automatic = X_Ref_WF_Start_Finish_Mode.AUTOMATIC.getValue();
    /** Manual = M */
    public static final String FINISHMODE_Manual = X_Ref_WF_Start_Finish_Mode.MANUAL.getValue();
    /** Set Finish Mode.
    @param FinishMode Workflow Activity Finish Mode */
    public void setFinishMode (String FinishMode)
    {
        if (!X_Ref_WF_Start_Finish_Mode.isValid(FinishMode))
        throw new IllegalArgumentException ("FinishMode Invalid value - " + FinishMode + " - Reference_ID=303 - A - M");
        set_Value ("FinishMode", FinishMode);
        
    }
    
    /** Get Finish Mode.
    @return Workflow Activity Finish Mode */
    public String getFinishMode() 
    {
        return (String)get_Value("FinishMode");
        
    }
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Centrally maintained.
    @param IsCentrallyMaintained Information maintained in System Element table */
    public void setIsCentrallyMaintained (boolean IsCentrallyMaintained)
    {
        set_Value ("IsCentrallyMaintained", Boolean.valueOf(IsCentrallyMaintained));
        
    }
    
    /** Get Centrally maintained.
    @return Information maintained in System Element table */
    public boolean isCentrallyMaintained() 
    {
        return get_ValueAsBoolean("IsCentrallyMaintained");
        
    }
    
    /** AND = A */
    public static final String JOINELEMENT_AND = X_Ref_WF_Join_Split.AND.getValue();
    /** XOR = X */
    public static final String JOINELEMENT_XOR = X_Ref_WF_Join_Split.XOR.getValue();
    /** Set Join Element.
    @param JoinElement Semantics for multiple incoming Transitions */
    public void setJoinElement (String JoinElement)
    {
        if (JoinElement == null) throw new IllegalArgumentException ("JoinElement is mandatory");
        if (!X_Ref_WF_Join_Split.isValid(JoinElement))
        throw new IllegalArgumentException ("JoinElement Invalid value - " + JoinElement + " - Reference_ID=301 - A - X");
        set_Value ("JoinElement", JoinElement);
        
    }
    
    /** Get Join Element.
    @return Semantics for multiple incoming Transitions */
    public String getJoinElement() 
    {
        return (String)get_Value("JoinElement");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Priority.
    @param Priority Indicates if this request is of a high, medium or low priority. */
    public void setPriority (int Priority)
    {
        set_Value ("Priority", Integer.valueOf(Priority));
        
    }
    
    /** Get Priority.
    @return Indicates if this request is of a high, medium or low priority. */
    public int getPriority() 
    {
        return get_ValueAsInt("Priority");
        
    }
    
    /** Set Mail Template.
    @param R_MailText_ID Text templates for mailings */
    public void setR_MailText_ID (int R_MailText_ID)
    {
        if (R_MailText_ID <= 0) set_Value ("R_MailText_ID", null);
        else
        set_Value ("R_MailText_ID", Integer.valueOf(R_MailText_ID));
        
    }
    
    /** Get Mail Template.
    @return Text templates for mailings */
    public int getR_MailText_ID() 
    {
        return get_ValueAsInt("R_MailText_ID");
        
    }
    
    /** AND = A */
    public static final String SPLITELEMENT_AND = X_Ref_WF_Join_Split.AND.getValue();
    /** XOR = X */
    public static final String SPLITELEMENT_XOR = X_Ref_WF_Join_Split.XOR.getValue();
    /** Set Split Element.
    @param SplitElement Semantics for multiple outgoing Transitions */
    public void setSplitElement (String SplitElement)
    {
        if (SplitElement == null) throw new IllegalArgumentException ("SplitElement is mandatory");
        if (!X_Ref_WF_Join_Split.isValid(SplitElement))
        throw new IllegalArgumentException ("SplitElement Invalid value - " + SplitElement + " - Reference_ID=301 - A - X");
        set_Value ("SplitElement", SplitElement);
        
    }
    
    /** Get Split Element.
    @return Semantics for multiple outgoing Transitions */
    public String getSplitElement() 
    {
        return (String)get_Value("SplitElement");
        
    }
    
    /** Automatic = A */
    public static final String STARTMODE_Automatic = X_Ref_WF_Start_Finish_Mode.AUTOMATIC.getValue();
    /** Manual = M */
    public static final String STARTMODE_Manual = X_Ref_WF_Start_Finish_Mode.MANUAL.getValue();
    /** Set Start Mode.
    @param StartMode Workflow Activity Start Mode */
    public void setStartMode (String StartMode)
    {
        if (!X_Ref_WF_Start_Finish_Mode.isValid(StartMode))
        throw new IllegalArgumentException ("StartMode Invalid value - " + StartMode + " - Reference_ID=303 - A - M");
        set_Value ("StartMode", StartMode);
        
    }
    
    /** Get Start Mode.
    @return Workflow Activity Start Mode */
    public String getStartMode() 
    {
        return (String)get_Value("StartMode");
        
    }
    
    /** Asynchronously = A */
    public static final String SUBFLOWEXECUTION_Asynchronously = X_Ref_WF_SubFlow_Execution.ASYNCHRONOUSLY.getValue();
    /** Synchronously = S */
    public static final String SUBFLOWEXECUTION_Synchronously = X_Ref_WF_SubFlow_Execution.SYNCHRONOUSLY.getValue();
    /** Set Subflow Execution.
    @param SubflowExecution Mode how the sub-workflow is executed */
    public void setSubflowExecution (String SubflowExecution)
    {
        if (!X_Ref_WF_SubFlow_Execution.isValid(SubflowExecution))
        throw new IllegalArgumentException ("SubflowExecution Invalid value - " + SubflowExecution + " - Reference_ID=307 - A - S");
        set_Value ("SubflowExecution", SubflowExecution);
        
    }
    
    /** Get Subflow Execution.
    @return Mode how the sub-workflow is executed */
    public String getSubflowExecution() 
    {
        return (String)get_Value("SubflowExecution");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Wait Time.
    @param WaitTime Time in minutes to wait (sleep) */
    public void setWaitTime (int WaitTime)
    {
        set_Value ("WaitTime", Integer.valueOf(WaitTime));
        
    }
    
    /** Get Wait Time.
    @return Time in minutes to wait (sleep) */
    public int getWaitTime() 
    {
        return get_ValueAsInt("WaitTime");
        
    }
    
    /** Set Waiting Time.
    @param WaitingTime Workflow Simulation Waiting time */
    public void setWaitingTime (int WaitingTime)
    {
        set_Value ("WaitingTime", Integer.valueOf(WaitingTime));
        
    }
    
    /** Get Waiting Time.
    @return Workflow Simulation Waiting time */
    public int getWaitingTime() 
    {
        return get_ValueAsInt("WaitingTime");
        
    }
    
    /** Set Workflow.
    @param Workflow_ID Workflow or tasks */
    public void setWorkflow_ID (int Workflow_ID)
    {
        if (Workflow_ID <= 0) set_Value ("Workflow_ID", null);
        else
        set_Value ("Workflow_ID", Integer.valueOf(Workflow_ID));
        
    }
    
    /** Get Workflow.
    @return Workflow or tasks */
    public int getWorkflow_ID() 
    {
        return get_ValueAsInt("Workflow_ID");
        
    }
    
    /** Set Working Time.
    @param WorkingTime Workflow Simulation Execution Time */
    public void setWorkingTime (int WorkingTime)
    {
        set_Value ("WorkingTime", Integer.valueOf(WorkingTime));
        
    }
    
    /** Get Working Time.
    @return Workflow Simulation Execution Time */
    public int getWorkingTime() 
    {
        return get_ValueAsInt("WorkingTime");
        
    }
    
    /** Set X Position.
    @param XPosition Absolute X (horizontal) position in 1/72 of an inch */
    public void setXPosition (int XPosition)
    {
        set_Value ("XPosition", Integer.valueOf(XPosition));
        
    }
    
    /** Get X Position.
    @return Absolute X (horizontal) position in 1/72 of an inch */
    public int getXPosition() 
    {
        return get_ValueAsInt("XPosition");
        
    }
    
    /** Set Y Position.
    @param YPosition Absolute Y (vertical) position in 1/72 of an inch */
    public void setYPosition (int YPosition)
    {
        set_Value ("YPosition", Integer.valueOf(YPosition));
        
    }
    
    /** Get Y Position.
    @return Absolute Y (vertical) position in 1/72 of an inch */
    public int getYPosition() 
    {
        return get_ValueAsInt("YPosition");
        
    }
    
    
}
