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
package compiere.model.cds;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_BenefitsMatrix
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_BenefitsMatrix extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_BenefitsMatrix_ID id
    @param trx transaction
    */
    public X_XX_VCN_BenefitsMatrix (Ctx ctx, int XX_VCN_BenefitsMatrix_ID, Trx trx)
    {
        super (ctx, XX_VCN_BenefitsMatrix_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_BenefitsMatrix_ID == 0)
        {
            setXX_VCN_BENEFITSMATRIX_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_BenefitsMatrix (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27565921212789L;
    /** Last Updated Timestamp 2010-09-06 13:28:16.0 */
    public static final long updatedMS = 1283795896000L;
    /** AD_Table_ID=1000123 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_BenefitsMatrix");
        
    }
    ;
    
    /** TableName=XX_VCN_BenefitsMatrix */
    public static final String Table_Name="XX_VCN_BenefitsMatrix";
    
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
    
    /** Set Value.
    @param ValueNumber Numeric Value */
    public void setValueNumber (int ValueNumber)
    {
        set_Value ("ValueNumber", Integer.valueOf(ValueNumber));
        
    }
    
    /** Get Value.
    @return Numeric Value */
    public int getValueNumber() 
    {
        return get_ValueAsInt("ValueNumber");
        
    }
    
    /** Set Show Detail Vendor Rating.
    @param XX_DetailVendorRating Show Detail Vendor Rating */
    public void setXX_DetailVendorRating (String XX_DetailVendorRating)
    {
        set_Value ("XX_DetailVendorRating", XX_DetailVendorRating);
        
    }
    
    /** Get Show Detail Vendor Rating.
    @return Show Detail Vendor Rating */
    public String getXX_DetailVendorRating() 
    {
        return (String)get_Value("XX_DetailVendorRating");
        
    }
    
    /** Set Higher Limit.
    @param XX_HigherLimit Limite mayor del Rango del Beneficio */
    public void setXX_HigherLimit (java.math.BigDecimal XX_HigherLimit)
    {
        set_Value ("XX_HigherLimit", XX_HigherLimit);
        
    }
    
    /** Get Higher Limit.
    @return Limite mayor del Rango del Beneficio */
    public java.math.BigDecimal getXX_HigherLimit() 
    {
        return get_ValueAsBigDecimal("XX_HigherLimit");
        
    }
    
    /** Set Is All Vendor.
    @param XX_IsAllVendor Is All Vendor */
    public void setXX_IsAllVendor (boolean XX_IsAllVendor)
    {
        set_Value ("XX_IsAllVendor", Boolean.valueOf(XX_IsAllVendor));
        
    }
    
    /** Get Is All Vendor.
    @return Is All Vendor */
    public boolean isXX_IsAllVendor() 
    {
        return get_ValueAsBoolean("XX_IsAllVendor");
        
    }
    
    /** Set Lower Limit.
    @param XX_LowerLimit Limite menor del Rango del Beneficio */
    public void setXX_LowerLimit (java.math.BigDecimal XX_LowerLimit)
    {
        set_Value ("XX_LowerLimit", XX_LowerLimit);
        
    }
    
    /** Get Lower Limit.
    @return Limite menor del Rango del Beneficio */
    public java.math.BigDecimal getXX_LowerLimit() 
    {
        return get_ValueAsBigDecimal("XX_LowerLimit");
        
    }
    
    /** Set Benefit.
    @param XX_VCN_Benefits_ID Id de la Tabla de Beneficios */
    public void setXX_VCN_Benefits_ID (int XX_VCN_Benefits_ID)
    {
        if (XX_VCN_Benefits_ID <= 0) set_Value ("XX_VCN_Benefits_ID", null);
        else
        set_Value ("XX_VCN_Benefits_ID", Integer.valueOf(XX_VCN_Benefits_ID));
        
    }
    
    /** Get Benefit.
    @return Id de la Tabla de Beneficios */
    public int getXX_VCN_Benefits_ID() 
    {
        return get_ValueAsInt("XX_VCN_Benefits_ID");
        
    }
    
    /** Set Benefits Matrix ID.
    @param XX_VCN_BENEFITSMATRIX_ID Id de la tabla XX_VCN_BenefitsMatrix */
    public void setXX_VCN_BENEFITSMATRIX_ID (int XX_VCN_BENEFITSMATRIX_ID)
    {
        if (XX_VCN_BENEFITSMATRIX_ID < 1) throw new IllegalArgumentException ("XX_VCN_BENEFITSMATRIX_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_BENEFITSMATRIX_ID", Integer.valueOf(XX_VCN_BENEFITSMATRIX_ID));
        
    }
    
    /** Get Benefits Matrix ID.
    @return Id de la tabla XX_VCN_BenefitsMatrix */
    public int getXX_VCN_BENEFITSMATRIX_ID() 
    {
        return get_ValueAsInt("XX_VCN_BENEFITSMATRIX_ID");
        
    }
    
    /** Set Show Vendor Rating.
    @param XX_VendorRating Ver puntuación del Proveedor */
    public void setXX_VendorRating (String XX_VendorRating)
    {
        set_Value ("XX_VendorRating", XX_VendorRating);
        
    }
    
    /** Get Show Vendor Rating.
    @return Ver puntuación del Proveedor */
    public String getXX_VendorRating() 
    {
        return (String)get_Value("XX_VendorRating");
        
    }
    
    /** Set Vendor Type.
    @param XX_VendorType_ID Describe el Tipo de Proveedor */
    public void setXX_VendorType_ID (int XX_VendorType_ID)
    {
        if (XX_VendorType_ID <= 0) set_Value ("XX_VendorType_ID", null);
        else
        set_Value ("XX_VendorType_ID", Integer.valueOf(XX_VendorType_ID));
        
    }
    
    /** Get Vendor Type.
    @return Describe el Tipo de Proveedor */
    public int getXX_VendorType_ID() 
    {
        return get_ValueAsInt("XX_VendorType_ID");
        
    }
    
    
}
