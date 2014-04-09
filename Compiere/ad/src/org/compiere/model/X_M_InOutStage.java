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
/** Generated Model for M_InOutStage
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_InOutStage.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_InOutStage extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_InOutStage_ID id
    @param trx transaction
    */
    public X_M_InOutStage (Ctx ctx, int M_InOutStage_ID, Trx trx)
    {
        super (ctx, M_InOutStage_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_InOutStage_ID == 0)
        {
            setIsCreateOnSave (false);	// N
            setIsInOutCreated (null);	// N
            setM_InOutStage_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_InOutStage (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27529723012789L;
    /** Last Updated Timestamp 2009-07-14 11:54:56.0 */
    public static final long updatedMS = 1247597696000L;
    /** AD_Table_ID=1015 */
    public static final int Table_ID=1015;
    
    /** TableName=M_InOutStage */
    public static final String Table_Name="M_InOutStage";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID <= 0) set_Value ("C_DocType_ID", null);
        else
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
    }
    
    /** Set Order Line.
    @param C_OrderLine_ID Order Line */
    public void setC_OrderLine_ID (int C_OrderLine_ID)
    {
        if (C_OrderLine_ID <= 0) set_Value ("C_OrderLine_ID", null);
        else
        set_Value ("C_OrderLine_ID", Integer.valueOf(C_OrderLine_ID));
        
    }
    
    /** Get Order Line.
    @return Order Line */
    public int getC_OrderLine_ID() 
    {
        return get_ValueAsInt("C_OrderLine_ID");
        
    }
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set UOM.
    @param C_UOM_ID Unit of Measure */
    public void setC_UOM_ID (int C_UOM_ID)
    {
        if (C_UOM_ID <= 0) set_Value ("C_UOM_ID", null);
        else
        set_Value ("C_UOM_ID", Integer.valueOf(C_UOM_ID));
        
    }
    
    /** Get UOM.
    @return Unit of Measure */
    public int getC_UOM_ID() 
    {
        return get_ValueAsInt("C_UOM_ID");
        
    }
    
    /** <None> = -- */
    public static final String CREATEINOUTDOCACTION_None = X_Ref__Document_Action.NONE.getValue();
    /** Approve = AP */
    public static final String CREATEINOUTDOCACTION_Approve = X_Ref__Document_Action.APPROVE.getValue();
    /** Close = CL */
    public static final String CREATEINOUTDOCACTION_Close = X_Ref__Document_Action.CLOSE.getValue();
    /** Complete = CO */
    public static final String CREATEINOUTDOCACTION_Complete = X_Ref__Document_Action.COMPLETE.getValue();
    /** Invalidate = IN */
    public static final String CREATEINOUTDOCACTION_Invalidate = X_Ref__Document_Action.INVALIDATE.getValue();
    /** Post = PO */
    public static final String CREATEINOUTDOCACTION_Post = X_Ref__Document_Action.POST.getValue();
    /** Prepare = PR */
    public static final String CREATEINOUTDOCACTION_Prepare = X_Ref__Document_Action.PREPARE.getValue();
    /** Reverse - Accrual = RA */
    public static final String CREATEINOUTDOCACTION_Reverse_Accrual = X_Ref__Document_Action.REVERSE__ACCRUAL.getValue();
    /** Reverse - Correct = RC */
    public static final String CREATEINOUTDOCACTION_Reverse_Correct = X_Ref__Document_Action.REVERSE__CORRECT.getValue();
    /** Re-activate = RE */
    public static final String CREATEINOUTDOCACTION_Re_Activate = X_Ref__Document_Action.RE__ACTIVATE.getValue();
    /** Reject = RJ */
    public static final String CREATEINOUTDOCACTION_Reject = X_Ref__Document_Action.REJECT.getValue();
    /** Void = VO */
    public static final String CREATEINOUTDOCACTION_Void = X_Ref__Document_Action.VOID.getValue();
    /** Wait Complete = WC */
    public static final String CREATEINOUTDOCACTION_WaitComplete = X_Ref__Document_Action.WAIT_COMPLETE.getValue();
    /** Unlock = XL */
    public static final String CREATEINOUTDOCACTION_Unlock = X_Ref__Document_Action.UNLOCK.getValue();
    /** Set Document Action.
    @param CreateInOutDocAction Document Action for the created receipt */
    public void setCreateInOutDocAction (String CreateInOutDocAction)
    {
        if (!X_Ref__Document_Action.isValid(CreateInOutDocAction))
        throw new IllegalArgumentException ("CreateInOutDocAction Invalid value - " + CreateInOutDocAction + " - Reference_ID=135 - -- - AP - CL - CO - IN - PO - PR - RA - RC - RE - RJ - VO - WC - XL");
        set_Value ("CreateInOutDocAction", CreateInOutDocAction);
        
    }
    
    /** Get Document Action.
    @return Document Action for the created receipt */
    public String getCreateInOutDocAction() 
    {
        return (String)get_Value("CreateInOutDocAction");
        
    }
    
    /** Set Error Message.
    @param ErrorMsg Error Message */
    public void setErrorMsg (String ErrorMsg)
    {
        set_Value ("ErrorMsg", ErrorMsg);
        
    }
    
    /** Get Error Message.
    @return Error Message */
    public String getErrorMsg() 
    {
        return (String)get_Value("ErrorMsg");
        
    }
    
    /** Set Create Receipt during Save.
    @param IsCreateOnSave Create a Material Receipt when interface record is saved */
    public void setIsCreateOnSave (boolean IsCreateOnSave)
    {
        set_Value ("IsCreateOnSave", Boolean.valueOf(IsCreateOnSave));
        
    }
    
    /** Get Create Receipt during Save.
    @return Create a Material Receipt when interface record is saved */
    public boolean isCreateOnSave() 
    {
        return get_ValueAsBoolean("IsCreateOnSave");
        
    }
    
    /** No = N */
    public static final String ISINOUTCREATED_No = X_Ref__YesNo.NO.getValue();
    /** Yes = Y */
    public static final String ISINOUTCREATED_Yes = X_Ref__YesNo.YES.getValue();
    /** Set Receipt Created.
    @param IsInOutCreated Indicates if a receipt been created for this record */
    public void setIsInOutCreated (String IsInOutCreated)
    {
        if (IsInOutCreated == null) throw new IllegalArgumentException ("IsInOutCreated is mandatory");
        if (!X_Ref__YesNo.isValid(IsInOutCreated))
        throw new IllegalArgumentException ("IsInOutCreated Invalid value - " + IsInOutCreated + " - Reference_ID=319 - N - Y");
        set_Value ("IsInOutCreated", IsInOutCreated);
        
    }
    
    /** Get Receipt Created.
    @return Indicates if a receipt been created for this record */
    public String getIsInOutCreated() 
    {
        return (String)get_Value("IsInOutCreated");
        
    }
    
    /** Set Return Transaction.
    @param IsReturnTrx This is a return transaction */
    public void setIsReturnTrx (boolean IsReturnTrx)
    {
        set_Value ("IsReturnTrx", Boolean.valueOf(IsReturnTrx));
        
    }
    
    /** Get Return Transaction.
    @return This is a return transaction */
    public boolean isReturnTrx() 
    {
        return get_ValueAsBoolean("IsReturnTrx");
        
    }
    
    /** Set Sales Transaction.
    @param IsSOTrx This is a Sales Transaction */
    public void setIsSOTrx (boolean IsSOTrx)
    {
        set_Value ("IsSOTrx", Boolean.valueOf(IsSOTrx));
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
    }
    
    /** Set Receipt Interface.
    @param M_InOutStage_ID Receipt Interface */
    public void setM_InOutStage_ID (int M_InOutStage_ID)
    {
        if (M_InOutStage_ID < 1) throw new IllegalArgumentException ("M_InOutStage_ID is mandatory.");
        set_ValueNoCheck ("M_InOutStage_ID", Integer.valueOf(M_InOutStage_ID));
        
    }
    
    /** Get Receipt Interface.
    @return Receipt Interface */
    public int getM_InOutStage_ID() 
    {
        return get_ValueAsInt("M_InOutStage_ID");
        
    }
    
    /** Set Shipment/Receipt.
    @param M_InOut_ID Material Shipment Document */
    public void setM_InOut_ID (int M_InOut_ID)
    {
        if (M_InOut_ID <= 0) set_Value ("M_InOut_ID", null);
        else
        set_Value ("M_InOut_ID", Integer.valueOf(M_InOut_ID));
        
    }
    
    /** Get Shipment/Receipt.
    @return Material Shipment Document */
    public int getM_InOut_ID() 
    {
        return get_ValueAsInt("M_InOut_ID");
        
    }
    
    /** Set Locator.
    @param M_Locator_ID Warehouse Locator */
    public void setM_Locator_ID (int M_Locator_ID)
    {
        if (M_Locator_ID <= 0) set_Value ("M_Locator_ID", null);
        else
        set_Value ("M_Locator_ID", Integer.valueOf(M_Locator_ID));
        
    }
    
    /** Get Locator.
    @return Warehouse Locator */
    public int getM_Locator_ID() 
    {
        return get_ValueAsInt("M_Locator_ID");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Movement Date.
    @param MovementDate Date a product was moved in or out of inventory */
    public void setMovementDate (Timestamp MovementDate)
    {
        set_ValueNoCheck ("MovementDate", MovementDate);
        
    }
    
    /** Get Movement Date.
    @return Date a product was moved in or out of inventory */
    public Timestamp getMovementDate() 
    {
        return (Timestamp)get_Value("MovementDate");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Quantity.
    @param QtyEntered The Quantity Entered is based on the selected UoM */
    public void setQtyEntered (java.math.BigDecimal QtyEntered)
    {
        set_Value ("QtyEntered", QtyEntered);
        
    }
    
    /** Get Quantity.
    @return The Quantity Entered is based on the selected UoM */
    public java.math.BigDecimal getQtyEntered() 
    {
        return get_ValueAsBigDecimal("QtyEntered");
        
    }
    
    
}
