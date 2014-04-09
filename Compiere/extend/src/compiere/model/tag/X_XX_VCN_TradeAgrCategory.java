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
package compiere.model.tag;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_TradeAgrCategory
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_TradeAgrCategory extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_TradeAgrCategory_ID id
    @param trx transaction
    */
    public X_XX_VCN_TradeAgrCategory (Ctx ctx, int XX_VCN_TradeAgrCategory_ID, Trx trx)
    {
        super (ctx, XX_VCN_TradeAgrCategory_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_TradeAgrCategory_ID == 0)
        {
            setValue (null);
            setXX_VCN_TradeAgrCategory_ID (0);
            setXX_VCN_TradeAgreements_ID (0);	// @XX_VCN_TradeAgreements_ID@
            setXX_VMR_Category_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_TradeAgrCategory (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27588285229789L;
    /** Last Updated Timestamp 2011-05-23 09:41:53.0 */
    public static final long updatedMS = 1306159913000L;
    /** AD_Table_ID=1002561 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_TradeAgrCategory");
        
    }
    ;
    
    /** TableName=XX_VCN_TradeAgrCategory */
    public static final String Table_Name="XX_VCN_TradeAgrCategory";
    
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
        throw new IllegalArgumentException ("C_BPartner_ID is virtual column");
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
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
    
    /** Set XX_VCN_TradeAgrCategory_ID.
    @param XX_VCN_TradeAgrCategory_ID XX_VCN_TradeAgrCategory_ID */
    public void setXX_VCN_TradeAgrCategory_ID (int XX_VCN_TradeAgrCategory_ID)
    {
        if (XX_VCN_TradeAgrCategory_ID < 1) throw new IllegalArgumentException ("XX_VCN_TradeAgrCategory_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_TradeAgrCategory_ID", Integer.valueOf(XX_VCN_TradeAgrCategory_ID));
        
    }
    
    /** Get XX_VCN_TradeAgrCategory_ID.
    @return XX_VCN_TradeAgrCategory_ID */
    public int getXX_VCN_TradeAgrCategory_ID() 
    {
        return get_ValueAsInt("XX_VCN_TradeAgrCategory_ID");
        
    }
    
    /** Set TRADEAGREEMENTS ID.
    @param XX_VCN_TradeAgreements_ID Id de la tabla Acuerdos Comerciales */
    public void setXX_VCN_TradeAgreements_ID (int XX_VCN_TradeAgreements_ID)
    {
        if (XX_VCN_TradeAgreements_ID < 1) throw new IllegalArgumentException ("XX_VCN_TradeAgreements_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_TradeAgreements_ID", Integer.valueOf(XX_VCN_TradeAgreements_ID));
        
    }
    
    /** Get TRADEAGREEMENTS ID.
    @return Id de la tabla Acuerdos Comerciales */
    public int getXX_VCN_TradeAgreements_ID() 
    {
        return get_ValueAsInt("XX_VCN_TradeAgreements_ID");
        
    }
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID < 1) throw new IllegalArgumentException ("XX_VMR_Category_ID is mandatory.");
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
    }
    
    /** Get Category.
    @return Category */
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");
        
    }
    
    
}
