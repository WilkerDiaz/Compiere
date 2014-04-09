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
package compiere.model.payments;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_TaxPeriod
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_TaxPeriod extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_TaxPeriod_ID id
    @param trx transaction
    */
    public X_XX_VCN_TaxPeriod (Ctx ctx, int XX_VCN_TaxPeriod_ID, Trx trx)
    {
        super (ctx, XX_VCN_TaxPeriod_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_TaxPeriod_ID == 0)
        {
            setXX_VCN_TaxPeriod_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_TaxPeriod (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27607033992789L;
    /** Last Updated Timestamp 2011-12-26 09:41:16.0 */
    public static final long updatedMS = 1324908676000L;
    /** AD_Table_ID=1002665 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_TaxPeriod");
        
    }
    ;
    
    /** TableName=XX_VCN_TaxPeriod */
    public static final String Table_Name="XX_VCN_TaxPeriod";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Date From of the Declaration.
    @param XX_DateFromDeclaration Date From of the Declaration */
    public void setXX_DateFromDeclaration (java.math.BigDecimal XX_DateFromDeclaration)
    {
        set_Value ("XX_DateFromDeclaration", XX_DateFromDeclaration);
        
    }
    
    /** Get Date From of the Declaration.
    @return Date From of the Declaration */
    public java.math.BigDecimal getXX_DateFromDeclaration() 
    {
        return get_ValueAsBigDecimal("XX_DateFromDeclaration");
        
    }
    
    /** Set Date To of the Declaration.
    @param XX_DateToDeclaration Date To of the Declaration */
    public void setXX_DateToDeclaration (java.math.BigDecimal XX_DateToDeclaration)
    {
        set_Value ("XX_DateToDeclaration", XX_DateToDeclaration);
        
    }
    
    /** Get Date To of the Declaration.
    @return Date To of the Declaration */
    public java.math.BigDecimal getXX_DateToDeclaration() 
    {
        return get_ValueAsBigDecimal("XX_DateToDeclaration");
        
    }
    
    /** Set Number of the Declaration.
    @param XX_NumberDeclaration Number of the Declaration */
    public void setXX_NumberDeclaration (String XX_NumberDeclaration)
    {
        set_Value ("XX_NumberDeclaration", XX_NumberDeclaration);
        
    }
    
    /** Get Number of the Declaration.
    @return Number of the Declaration */
    public String getXX_NumberDeclaration() 
    {
        return (String)get_Value("XX_NumberDeclaration");
        
    }
    
    /** Set XX_VCN_TaxPeriod_ID.
    @param XX_VCN_TaxPeriod_ID XX_VCN_TaxPeriod_ID */
    public void setXX_VCN_TaxPeriod_ID (int XX_VCN_TaxPeriod_ID)
    {
        if (XX_VCN_TaxPeriod_ID < 1) throw new IllegalArgumentException ("XX_VCN_TaxPeriod_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_TaxPeriod_ID", Integer.valueOf(XX_VCN_TaxPeriod_ID));
        
    }
    
    /** Get XX_VCN_TaxPeriod_ID.
    @return XX_VCN_TaxPeriod_ID */
    public int getXX_VCN_TaxPeriod_ID() 
    {
        return get_ValueAsInt("XX_VCN_TaxPeriod_ID");
        
    }
    
    
}
