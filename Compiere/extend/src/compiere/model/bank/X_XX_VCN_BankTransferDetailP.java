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
/** Generated Model for XX_VCN_BankTransferDetailP
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_BankTransferDetailP extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_BankTransferDetailP_ID id
    @param trx transaction
    */
    public X_XX_VCN_BankTransferDetailP (Ctx ctx, int XX_VCN_BankTransferDetailP_ID, Trx trx)
    {
        super (ctx, XX_VCN_BankTransferDetailP_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_BankTransferDetailP_ID == 0)
        {
            setC_Payment_ID (0);
            setXX_VCN_BankTransferDetailP_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_BankTransferDetailP (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27665404778789L;
    /** Last Updated Timestamp 2013-10-31 23:47:42.0 */
    public static final long updatedMS = 1383279462000L;
    /** AD_Table_ID=1005553 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_BankTransferDetailP");
        
    }
    ;
    
    /** TableName=XX_VCN_BankTransferDetailP */
    public static final String Table_Name="XX_VCN_BankTransferDetailP";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID < 1) throw new IllegalArgumentException ("C_Payment_ID is mandatory.");
        set_ValueNoCheck ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
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
        throw new IllegalArgumentException ("XX_VCN_Amount is virtual column");
        
    }
    
    /** Get XX_VCN_Amount.
    @return XX_VCN_Amount */
    public java.math.BigDecimal getXX_VCN_Amount() 
    {
        return get_ValueAsBigDecimal("XX_VCN_Amount");
        
    }
    
    /** Set XX_VCN_BankRejection.
    @param XX_VCN_BankRejection XX_VCN_BankRejection */
    public void setXX_VCN_BankRejection (String XX_VCN_BankRejection)
    {
        set_Value ("XX_VCN_BankRejection", XX_VCN_BankRejection);
        
    }
    
    /** Get XX_VCN_BankRejection.
    @return XX_VCN_BankRejection */
    public String getXX_VCN_BankRejection() 
    {
        return (String)get_Value("XX_VCN_BankRejection");
        
    }
    
    /** Set XX_VCN_BankTransferDetailP_ID.
    @param XX_VCN_BankTransferDetailP_ID XX_VCN_BankTransferDetailP_ID */
    public void setXX_VCN_BankTransferDetailP_ID (int XX_VCN_BankTransferDetailP_ID)
    {
        if (XX_VCN_BankTransferDetailP_ID < 1) throw new IllegalArgumentException ("XX_VCN_BankTransferDetailP_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_BankTransferDetailP_ID", Integer.valueOf(XX_VCN_BankTransferDetailP_ID));
        
    }
    
    /** Get XX_VCN_BankTransferDetailP_ID.
    @return XX_VCN_BankTransferDetailP_ID */
    public int getXX_VCN_BankTransferDetailP_ID() 
    {
        return get_ValueAsInt("XX_VCN_BankTransferDetailP_ID");
        
    }
    
    /** Set XX_VCN_BankTransfer_ID.
    @param XX_VCN_BankTransfer_ID XX_VCN_BankTransfer_ID */
    public void setXX_VCN_BankTransfer_ID (int XX_VCN_BankTransfer_ID)
    {
        if (XX_VCN_BankTransfer_ID <= 0) set_Value ("XX_VCN_BankTransfer_ID", null);
        else
        set_Value ("XX_VCN_BankTransfer_ID", Integer.valueOf(XX_VCN_BankTransfer_ID));
        
    }
    
    /** Get XX_VCN_BankTransfer_ID.
    @return XX_VCN_BankTransfer_ID */
    public int getXX_VCN_BankTransfer_ID() 
    {
        return get_ValueAsInt("XX_VCN_BankTransfer_ID");
        
    }
    
    /** Set XX_VCN_Beneficiary.
    @param XX_VCN_Beneficiary XX_VCN_Beneficiary */
    public void setXX_VCN_Beneficiary (String XX_VCN_Beneficiary)
    {
        throw new IllegalArgumentException ("XX_VCN_Beneficiary is virtual column");
        
    }
    
    /** Get XX_VCN_Beneficiary.
    @return XX_VCN_Beneficiary */
    public String getXX_VCN_Beneficiary() 
    {
        return (String)get_Value("XX_VCN_Beneficiary");
        
    }
    
    /** Set XX_VCN_CreateDate.
    @param XX_VCN_CreateDate XX_VCN_CreateDate */
    public void setXX_VCN_CreateDate (Timestamp XX_VCN_CreateDate)
    {
        throw new IllegalArgumentException ("XX_VCN_CreateDate is virtual column");
        
    }
    
    /** Get XX_VCN_CreateDate.
    @return XX_VCN_CreateDate */
    public Timestamp getXX_VCN_CreateDate() 
    {
        return (Timestamp)get_Value("XX_VCN_CreateDate");
        
    }
    
    /** Set XX_VCN_DeleteDetail.
    @param XX_VCN_DeleteDetail XX_VCN_DeleteDetail */
    public void setXX_VCN_DeleteDetail (String XX_VCN_DeleteDetail)
    {
        set_Value ("XX_VCN_DeleteDetail", XX_VCN_DeleteDetail);
        
    }
    
    /** Get XX_VCN_DeleteDetail.
    @return XX_VCN_DeleteDetail */
    public String getXX_VCN_DeleteDetail() 
    {
        return (String)get_Value("XX_VCN_DeleteDetail");
        
    }
    
    /** Transferencia = 1 */
    public static final String XX_VCN_STATETRANSFERDETAIL_Transferencia = X_Ref_XX_VCN_StateTransferDetail.TRANSFERENCIA.getValue();
    /** Anulada = 2 */
    public static final String XX_VCN_STATETRANSFERDETAIL_Anulada = X_Ref_XX_VCN_StateTransferDetail.ANULADA.getValue();
    /** Pendiente = 3 */
    public static final String XX_VCN_STATETRANSFERDETAIL_Pendiente = X_Ref_XX_VCN_StateTransferDetail.PENDIENTE.getValue();
    /** Set XX_VCN_StateTransferDetail.
    @param XX_VCN_StateTransferDetail XX_VCN_StateTransferDetail */
    public void setXX_VCN_StateTransferDetail (String XX_VCN_StateTransferDetail)
    {
        if (!X_Ref_XX_VCN_StateTransferDetail.isValid(XX_VCN_StateTransferDetail))
        throw new IllegalArgumentException ("XX_VCN_StateTransferDetail Invalid value - " + XX_VCN_StateTransferDetail + " - Reference_ID=1004450 - 1 - 2 - 3");
        set_Value ("XX_VCN_StateTransferDetail", XX_VCN_StateTransferDetail);
        
    }
    
    /** Get XX_VCN_StateTransferDetail.
    @return XX_VCN_StateTransferDetail */
    public String getXX_VCN_StateTransferDetail() 
    {
        return (String)get_Value("XX_VCN_StateTransferDetail");
        
    }
    
    
}
