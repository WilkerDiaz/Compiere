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
/** Generated Model for I_XX_VendorCountryDis
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VendorCountryDis extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VendorCountryDis_ID id
    @param trx transaction
    */
    public X_I_XX_VendorCountryDis (Ctx ctx, int I_XX_VendorCountryDis_ID, Trx trx)
    {
        super (ctx, I_XX_VendorCountryDis_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VendorCountryDis_ID == 0)
        {
            setI_XX_VENDORCOUNTRYDIS_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VendorCountryDis (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567477439789L;
    /** Last Updated Timestamp 2010-09-24 13:45:23.0 */
    public static final long updatedMS = 1285352123000L;
    /** AD_Table_ID=1000376 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VendorCountryDis");
        
    }
    ;
    
    /** TableName=I_XX_VendorCountryDis */
    public static final String Table_Name="I_XX_VendorCountryDis";
    
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
    
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
        else
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (boolean I_IsImported)
    {
        set_Value ("I_IsImported", Boolean.valueOf(I_IsImported));
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public boolean isI_IsImported() 
    {
        return get_ValueAsBoolean("I_IsImported");
        
    }
    
    /** Set I_XX_VENDORCOUNTRYDIS_ID.
    @param I_XX_VENDORCOUNTRYDIS_ID I_XX_VENDORCOUNTRYDIS_ID */
    public void setI_XX_VENDORCOUNTRYDIS_ID (int I_XX_VENDORCOUNTRYDIS_ID)
    {
        if (I_XX_VENDORCOUNTRYDIS_ID < 1) throw new IllegalArgumentException ("I_XX_VENDORCOUNTRYDIS_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VENDORCOUNTRYDIS_ID", Integer.valueOf(I_XX_VENDORCOUNTRYDIS_ID));
        
    }
    
    /** Get I_XX_VENDORCOUNTRYDIS_ID.
    @return I_XX_VENDORCOUNTRYDIS_ID */
    public int getI_XX_VENDORCOUNTRYDIS_ID() 
    {
        return get_ValueAsInt("I_XX_VENDORCOUNTRYDIS_ID");
        
    }
    
    /** Set PAIS.
    @param PAIS PAIS */
    public void setPAIS (String PAIS)
    {
        set_Value ("PAIS", PAIS);
        
    }
    
    /** Get PAIS.
    @return PAIS */
    public String getPAIS() 
    {
        return (String)get_Value("PAIS");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_Value ("Processed", Boolean.valueOf(Processed));
        
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
    
    /** Set Code Product.
    @param XX_CODPRO Codigo del Producto */
    public void setXX_CODPRO (String XX_CODPRO)
    {
        set_Value ("XX_CODPRO", XX_CODPRO);
        
    }
    
    /** Get Code Product.
    @return Codigo del Producto */
    public String getXX_CODPRO() 
    {
        return (String)get_Value("XX_CODPRO");
        
    }
    
    /** Set Vendor Country Distribution ID.
    @param XX_VCN_VENDORCOUNTRYDISTRI_ID Id de la Tabla de Paises de Distribucion del Proveedor */
    public void setXX_VCN_VENDORCOUNTRYDISTRI_ID (int XX_VCN_VENDORCOUNTRYDISTRI_ID)
    {
        if (XX_VCN_VENDORCOUNTRYDISTRI_ID <= 0) set_Value ("XX_VCN_VENDORCOUNTRYDISTRI_ID", null);
        else
        set_Value ("XX_VCN_VENDORCOUNTRYDISTRI_ID", Integer.valueOf(XX_VCN_VENDORCOUNTRYDISTRI_ID));
        
    }
    
    /** Get Vendor Country Distribution ID.
    @return Id de la Tabla de Paises de Distribucion del Proveedor */
    public int getXX_VCN_VENDORCOUNTRYDISTRI_ID() 
    {
        return get_ValueAsInt("XX_VCN_VENDORCOUNTRYDISTRI_ID");
        
    }
    
    
}
