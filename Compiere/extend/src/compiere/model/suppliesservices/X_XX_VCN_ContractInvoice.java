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
package compiere.model.suppliesservices;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_ContractInvoice
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_ContractInvoice extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_ContractInvoice_ID id
    @param trx transaction
    */
    public X_XX_VCN_ContractInvoice (Ctx ctx, int XX_VCN_ContractInvoice_ID, Trx trx)
    {
        super (ctx, XX_VCN_ContractInvoice_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_ContractInvoice_ID == 0)
        {
            setXX_VCN_ContractInvoice_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_ContractInvoice (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27640645603789L;
    /** Last Updated Timestamp 2013-01-18 10:14:47.0 */
    public static final long updatedMS = 1358520287000L;
    /** AD_Table_ID=1003553 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_ContractInvoice");
        
    }
    ;
    
    /** TableName=XX_VCN_ContractInvoice */
    public static final String Table_Name="XX_VCN_ContractInvoice";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
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
    
    /** Set Contract ID.
    @param XX_Contract_ID Contract ID */
    public void setXX_Contract_ID (int XX_Contract_ID)
    {
        if (XX_Contract_ID <= 0) set_Value ("XX_Contract_ID", null);
        else
        set_Value ("XX_Contract_ID", Integer.valueOf(XX_Contract_ID));
        
    }
    
    /** Get Contract ID.
    @return Contract ID */
    public int getXX_Contract_ID() 
    {
        return get_ValueAsInt("XX_Contract_ID");
        
    }
    
    /** Set Expenses Product.
    @param XX_PExpenses_ID Expenses Product */
    public void setXX_PExpenses_ID (int XX_PExpenses_ID)
    {
        if (XX_PExpenses_ID <= 0) set_Value ("XX_PExpenses_ID", null);
        else
        set_Value ("XX_PExpenses_ID", Integer.valueOf(XX_PExpenses_ID));
        
    }
    
    /** Get Expenses Product.
    @return Expenses Product */
    public int getXX_PExpenses_ID() 
    {
        return get_ValueAsInt("XX_PExpenses_ID");
        
    }
    
    /** Set Contract Invoice.
    @param XX_VCN_ContractInvoice_ID Contract Invoice */
    public void setXX_VCN_ContractInvoice_ID (int XX_VCN_ContractInvoice_ID)
    {
        if (XX_VCN_ContractInvoice_ID < 1) throw new IllegalArgumentException ("XX_VCN_ContractInvoice_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_ContractInvoice_ID", Integer.valueOf(XX_VCN_ContractInvoice_ID));
        
    }
    
    /** Get Contract Invoice.
    @return Contract Invoice */
    public int getXX_VCN_ContractInvoice_ID() 
    {
        return get_ValueAsInt("XX_VCN_ContractInvoice_ID");
        
    }
    
    
}
