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
/** Generated Model for XX_VCN_VendorCountryDistri
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_VendorCountryDistri extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_VendorCountryDistri_ID id
    @param trx transaction
    */
    public X_XX_VCN_VendorCountryDistri (Ctx ctx, int XX_VCN_VendorCountryDistri_ID, Trx trx)
    {
        super (ctx, XX_VCN_VendorCountryDistri_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_VendorCountryDistri_ID == 0)
        {
            setC_BPartner_ID (0);
            setXX_Country_ID (0);
            setXX_VCN_VENDORCOUNTRYDISTRI_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_VendorCountryDistri (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27546900740789L;
    /** Last Updated Timestamp 2010-01-29 10:00:24.0 */
    public static final long updatedMS = 1264775424000L;
    /** AD_Table_ID=1000092 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_VendorCountryDistri");
        
    }
    ;
    
    /** TableName=XX_VCN_VendorCountryDistri */
    public static final String Table_Name="XX_VCN_VendorCountryDistri";
    
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
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Country Distribution.
    @param XX_Country_ID Pais de Distribucion del Proveedor */
    public void setXX_Country_ID (int XX_Country_ID)
    {
        if (XX_Country_ID < 1) throw new IllegalArgumentException ("XX_Country_ID is mandatory.");
        set_Value ("XX_Country_ID", Integer.valueOf(XX_Country_ID));
        
    }
    
    /** Get Country Distribution.
    @return Pais de Distribucion del Proveedor */
    public int getXX_Country_ID() 
    {
        return get_ValueAsInt("XX_Country_ID");
        
    }
    
    /** Set Vendor Country Distribution ID.
    @param XX_VCN_VENDORCOUNTRYDISTRI_ID Id de la Tabla de Paises de Distribucion del Proveedor */
    public void setXX_VCN_VENDORCOUNTRYDISTRI_ID (int XX_VCN_VENDORCOUNTRYDISTRI_ID)
    {
        if (XX_VCN_VENDORCOUNTRYDISTRI_ID < 1) throw new IllegalArgumentException ("XX_VCN_VENDORCOUNTRYDISTRI_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_VENDORCOUNTRYDISTRI_ID", Integer.valueOf(XX_VCN_VENDORCOUNTRYDISTRI_ID));
        
    }
    
    /** Get Vendor Country Distribution ID.
    @return Id de la Tabla de Paises de Distribucion del Proveedor */
    public int getXX_VCN_VENDORCOUNTRYDISTRI_ID() 
    {
        return get_ValueAsInt("XX_VCN_VENDORCOUNTRYDISTRI_ID");
        
    }
    
    
}
