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
package compiere.model.bank;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_BankTransfer
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_BankTransfer extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_BankTransfer_ID id
    @param trx transaction
    */
    public X_XX_VCN_BankTransfer (Ctx ctx, int XX_VCN_BankTransfer_ID, Trx trx)
    {
        super (ctx, XX_VCN_BankTransfer_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_BankTransfer_ID == 0)
        {
            setC_BankAccount_ID (0);
            setC_Bank_ID (0);
            setXX_VCN_BankTransfer_ID (0);
            setXX_VCN_TransferState (null);	// 1
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_BankTransfer (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27665402384789L;
    /** Last Updated Timestamp 2013-10-31 23:07:48.0 */
    public static final long updatedMS = 1383277068000L;
    /** AD_Table_ID=1005453 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_BankTransfer");
        
    }
    ;
    
    /** TableName=XX_VCN_BankTransfer */
    public static final String Table_Name="XX_VCN_BankTransfer";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Bank Account.
    @param C_BankAccount_ID Account at the Bank */
    public void setC_BankAccount_ID (int C_BankAccount_ID)
    {
        if (C_BankAccount_ID < 1) throw new IllegalArgumentException ("C_BankAccount_ID is mandatory.");
        set_ValueNoCheck ("C_BankAccount_ID", Integer.valueOf(C_BankAccount_ID));
        
    }
    
    /** Get Bank Account.
    @return Account at the Bank */
    public int getC_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BankAccount_ID");
        
    }
    
    /** Set Bank.
    @param C_Bank_ID Bank */
    public void setC_Bank_ID (int C_Bank_ID)
    {
        if (C_Bank_ID < 1) throw new IllegalArgumentException ("C_Bank_ID is mandatory.");
        set_ValueNoCheck ("C_Bank_ID", Integer.valueOf(C_Bank_ID));
        
    }
    
    /** Get Bank.
    @return Bank */
    public int getC_Bank_ID() 
    {
        return get_ValueAsInt("C_Bank_ID");
        
    }
    
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Nombre.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Nombre.
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
    
    /** Set XX_VCN_Amount.
    @param XX_VCN_Amount XX_VCN_Amount */
    public void setXX_VCN_Amount (java.math.BigDecimal XX_VCN_Amount)
    {
        set_Value ("XX_VCN_Amount", XX_VCN_Amount);
        
    }
    
    /** Get XX_VCN_Amount.
    @return XX_VCN_Amount */
    public java.math.BigDecimal getXX_VCN_Amount() 
    {
        return get_ValueAsBigDecimal("XX_VCN_Amount");
        
    }
    
    /** Set XX_VCN_BankTransfer_ID.
    @param XX_VCN_BankTransfer_ID XX_VCN_BankTransfer_ID */
    public void setXX_VCN_BankTransfer_ID (int XX_VCN_BankTransfer_ID)
    {
        if (XX_VCN_BankTransfer_ID < 1) throw new IllegalArgumentException ("XX_VCN_BankTransfer_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_BankTransfer_ID", Integer.valueOf(XX_VCN_BankTransfer_ID));
        
    }
    
    /** Get XX_VCN_BankTransfer_ID.
    @return XX_VCN_BankTransfer_ID */
    public int getXX_VCN_BankTransfer_ID() 
    {
        return get_ValueAsInt("XX_VCN_BankTransfer_ID");
        
    }
    
    /** Set VCN_CancellationDate.
    @param XX_VCN_CancellationDate VCN_CancellationDate */
    public void setXX_VCN_CancellationDate (Timestamp XX_VCN_CancellationDate)
    {
        set_Value ("XX_VCN_CancellationDate", XX_VCN_CancellationDate);
        
    }
    
    /** Get VCN_CancellationDate.
    @return VCN_CancellationDate */
    public Timestamp getXX_VCN_CancellationDate() 
    {
        return (Timestamp)get_Value("XX_VCN_CancellationDate");
        
    }
    
    /** Set XX_VCN_ExecuteTransfer.
    @param XX_VCN_ExecuteTransfer XX_VCN_ExecuteTransfer */
    public void setXX_VCN_ExecuteTransfer (String XX_VCN_ExecuteTransfer)
    {
        set_Value ("XX_VCN_ExecuteTransfer", XX_VCN_ExecuteTransfer);
        
    }
    
    /** Get XX_VCN_ExecuteTransfer.
    @return XX_VCN_ExecuteTransfer */
    public String getXX_VCN_ExecuteTransfer() 
    {
        return (String)get_Value("XX_VCN_ExecuteTransfer");
        
    }
    
    /** Set XX_VCN_ExecutionDate.
    @param XX_VCN_ExecutionDate XX_VCN_ExecutionDate */
    public void setXX_VCN_ExecutionDate (Timestamp XX_VCN_ExecutionDate)
    {
        set_Value ("XX_VCN_ExecutionDate", XX_VCN_ExecutionDate);
        
    }
    
    /** Get XX_VCN_ExecutionDate.
    @return XX_VCN_ExecutionDate */
    public Timestamp getXX_VCN_ExecutionDate() 
    {
        return (Timestamp)get_Value("XX_VCN_ExecutionDate");
        
    }
    
    /** Set XX_VCN_GenerateTxt.
    @param XX_VCN_GenerateTxt XX_VCN_GenerateTxt */
    public void setXX_VCN_GenerateTxt (String XX_VCN_GenerateTxt)
    {
        set_Value ("XX_VCN_GenerateTxt", XX_VCN_GenerateTxt);
        
    }
    
    /** Get XX_VCN_GenerateTxt.
    @return XX_VCN_GenerateTxt */
    public String getXX_VCN_GenerateTxt() 
    {
        return (String)get_Value("XX_VCN_GenerateTxt");
        
    }
    
    /** Set XX_VCN_ReverseTransfer.
    @param XX_VCN_ReverseTransfer XX_VCN_ReverseTransfer */
    public void setXX_VCN_ReverseTransfer (String XX_VCN_ReverseTransfer)
    {
        set_Value ("XX_VCN_ReverseTransfer", XX_VCN_ReverseTransfer);
        
    }
    
    /** Get XX_VCN_ReverseTransfer.
    @return XX_VCN_ReverseTransfer */
    public String getXX_VCN_ReverseTransfer() 
    {
        return (String)get_Value("XX_VCN_ReverseTransfer");
        
    }
    
    /** Pendiente = 1 */
    public static final String XX_VCN_TRANSFERSTATE_Pendiente = X_Ref_XX_VCN_TransferStatus.PENDIENTE.getValue();
    /** Generada = 2 */
    public static final String XX_VCN_TRANSFERSTATE_Generada = X_Ref_XX_VCN_TransferStatus.GENERADA.getValue();
    /** Anulada = 3 */
    public static final String XX_VCN_TRANSFERSTATE_Anulada = X_Ref_XX_VCN_TransferStatus.ANULADA.getValue();
    /** Set XX_VCN_TransferState.
    @param XX_VCN_TransferState XX_VCN_TransferState */
    public void setXX_VCN_TransferState (String XX_VCN_TransferState)
    {
        if (XX_VCN_TransferState == null) throw new IllegalArgumentException ("XX_VCN_TransferState is mandatory");
        if (!X_Ref_XX_VCN_TransferStatus.isValid(XX_VCN_TransferState))
        throw new IllegalArgumentException ("XX_VCN_TransferState Invalid value - " + XX_VCN_TransferState + " - Reference_ID=1004449 - 1 - 2 - 3");
        set_Value ("XX_VCN_TransferState", XX_VCN_TransferState);
        
    }
    
    /** Get XX_VCN_TransferState.
    @return XX_VCN_TransferState */
    public String getXX_VCN_TransferState() 
    {
        return (String)get_Value("XX_VCN_TransferState");
        
    }
    
    /** Set XX_VCN_UserAnnulation.
    @param XX_VCN_UserAnnulation XX_VCN_UserAnnulation */
    public void setXX_VCN_UserAnnulation (int XX_VCN_UserAnnulation)
    {
        set_Value ("XX_VCN_UserAnnulation", Integer.valueOf(XX_VCN_UserAnnulation));
        
    }
    
    /** Get XX_VCN_UserAnnulation.
    @return XX_VCN_UserAnnulation */
    public int getXX_VCN_UserAnnulation() 
    {
        return get_ValueAsInt("XX_VCN_UserAnnulation");
        
    }
    
    /** Set XX_VCN_VoidTransfer.
    @param XX_VCN_VoidTransfer XX_VCN_VoidTransfer */
    public void setXX_VCN_VoidTransfer (String XX_VCN_VoidTransfer)
    {
        set_Value ("XX_VCN_VoidTransfer", XX_VCN_VoidTransfer);
        
    }
    
    /** Get XX_VCN_VoidTransfer.
    @return XX_VCN_VoidTransfer */
    public String getXX_VCN_VoidTransfer() 
    {
        return (String)get_Value("XX_VCN_VoidTransfer");
        
    }
    
    
}
