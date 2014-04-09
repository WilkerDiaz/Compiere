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
/** Generated Model for I_XX_VLO_CostsPercent
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VLO_CostsPercent extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VLO_CostsPercent_ID id
    @param trx transaction
    */
    public X_I_XX_VLO_CostsPercent (Ctx ctx, int I_XX_VLO_CostsPercent_ID, Trx trx)
    {
        super (ctx, I_XX_VLO_CostsPercent_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VLO_CostsPercent_ID == 0)
        {
            setI_XX_VLO_CostsPercent_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VLO_CostsPercent (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27564291540789L;
    /** Last Updated Timestamp 2010-08-18 16:47:04.0 */
    public static final long updatedMS = 1282166224000L;
    /** AD_Table_ID=1000352 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VLO_CostsPercent");
        
    }
    ;
    
    /** TableName=I_XX_VLO_CostsPercent */
    public static final String Table_Name="I_XX_VLO_CostsPercent";
    
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
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
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
    
    /** Set Costs Percent ID.
    @param I_XX_VLO_CostsPercent_ID Costs Percent ID */
    public void setI_XX_VLO_CostsPercent_ID (int I_XX_VLO_CostsPercent_ID)
    {
        if (I_XX_VLO_CostsPercent_ID < 1) throw new IllegalArgumentException ("I_XX_VLO_CostsPercent_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VLO_CostsPercent_ID", Integer.valueOf(I_XX_VLO_CostsPercent_ID));
        
    }
    
    /** Get Costs Percent ID.
    @return Costs Percent ID */
    public int getI_XX_VLO_CostsPercent_ID() 
    {
        return get_ValueAsInt("I_XX_VLO_CostsPercent_ID");
        
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
    
    /** Set Country Code.
    @param XX_CountryCode Country Code */
    public void setXX_CountryCode (String XX_CountryCode)
    {
        set_Value ("XX_CountryCode", XX_CountryCode);
        
    }
    
    /** Get Country Code.
    @return Country Code */
    public String getXX_CountryCode() 
    {
        return (String)get_Value("XX_CountryCode");
        
    }
    
    /** Set Custom Agent.
    @param XX_CustomAgent Custom Agent */
    public void setXX_CustomAgent (java.math.BigDecimal XX_CustomAgent)
    {
        set_Value ("XX_CustomAgent", XX_CustomAgent);
        
    }
    
    /** Get Custom Agent.
    @return Custom Agent */
    public java.math.BigDecimal getXX_CustomAgent() 
    {
        return get_ValueAsBigDecimal("XX_CustomAgent");
        
    }
    
    /** Set Date From.
    @param XX_DateFrom Date From */
    public void setXX_DateFrom (String XX_DateFrom)
    {
        set_Value ("XX_DateFrom", XX_DateFrom);
        
    }
    
    /** Get Date From.
    @return Date From */
    public String getXX_DateFrom() 
    {
        return (String)get_Value("XX_DateFrom");
        
    }
    
    /** Set Date Until.
    @param XX_DateUntil Date Until */
    public void setXX_DateUntil (String XX_DateUntil)
    {
        set_Value ("XX_DateUntil", XX_DateUntil);
        
    }
    
    /** Get Date Until.
    @return Date Until */
    public String getXX_DateUntil() 
    {
        return (String)get_Value("XX_DateUntil");
        
    }
    
    /** Set International Freight.
    @param XX_InternationalFreight International Freight */
    public void setXX_InternationalFreight (java.math.BigDecimal XX_InternationalFreight)
    {
        set_Value ("XX_InternationalFreight", XX_InternationalFreight);
        
    }
    
    /** Get International Freight.
    @return International Freight */
    public java.math.BigDecimal getXX_InternationalFreight() 
    {
        return get_ValueAsBigDecimal("XX_InternationalFreight");
        
    }
    
    /** Set National Freight.
    @param XX_NationalFreight National Freight */
    public void setXX_NationalFreight (java.math.BigDecimal XX_NationalFreight)
    {
        set_Value ("XX_NationalFreight", XX_NationalFreight);
        
    }
    
    /** Get National Freight.
    @return National Freight */
    public java.math.BigDecimal getXX_NationalFreight() 
    {
        return get_ValueAsBigDecimal("XX_NationalFreight");
        
    }
    
    /** Set Port Code.
    @param XX_PortCode Port Code */
    public void setXX_PortCode (String XX_PortCode)
    {
        set_Value ("XX_PortCode", XX_PortCode);
        
    }
    
    /** Get Port Code.
    @return Port Code */
    public String getXX_PortCode() 
    {
        return (String)get_Value("XX_PortCode");
        
    }
    
    /** Set Vendor Code.
    @param XX_VendorCode Vendor Code */
    public void setXX_VendorCode (String XX_VendorCode)
    {
        set_Value ("XX_VendorCode", XX_VendorCode);
        
    }
    
    /** Get Vendor Code.
    @return Vendor Code */
    public String getXX_VendorCode() 
    {
        return (String)get_Value("XX_VendorCode");
        
    }
    
    /** Set Arrival Port.
    @param XX_VLO_ArrivalPort_ID Arrival Port */
    public void setXX_VLO_ArrivalPort_ID (int XX_VLO_ArrivalPort_ID)
    {
        if (XX_VLO_ArrivalPort_ID <= 0) set_Value ("XX_VLO_ArrivalPort_ID", null);
        else
        set_Value ("XX_VLO_ArrivalPort_ID", Integer.valueOf(XX_VLO_ArrivalPort_ID));
        
    }
    
    /** Get Arrival Port.
    @return Arrival Port */
    public int getXX_VLO_ArrivalPort_ID() 
    {
        return get_ValueAsInt("XX_VLO_ArrivalPort_ID");
        
    }
    
    /** Set XX_VLO_CostsPercent_ID.
    @param XX_VLO_CostsPercent_ID XX_VLO_CostsPercent_ID */
    public void setXX_VLO_CostsPercent_ID (int XX_VLO_CostsPercent_ID)
    {
        if (XX_VLO_CostsPercent_ID <= 0) set_Value ("XX_VLO_CostsPercent_ID", null);
        else
        set_Value ("XX_VLO_CostsPercent_ID", Integer.valueOf(XX_VLO_CostsPercent_ID));
        
    }
    
    /** Get XX_VLO_CostsPercent_ID.
    @return XX_VLO_CostsPercent_ID */
    public int getXX_VLO_CostsPercent_ID() 
    {
        return get_ValueAsInt("XX_VLO_CostsPercent_ID");
        
    }
    
    
}
