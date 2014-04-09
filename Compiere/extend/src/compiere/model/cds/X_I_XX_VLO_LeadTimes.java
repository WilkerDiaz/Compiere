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
/** Generated Model for I_XX_VLO_LeadTimes
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VLO_LeadTimes extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VLO_LeadTimes_ID id
    @param trx transaction
    */
    public X_I_XX_VLO_LeadTimes (Ctx ctx, int I_XX_VLO_LeadTimes_ID, Trx trx)
    {
        super (ctx, I_XX_VLO_LeadTimes_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VLO_LeadTimes_ID == 0)
        {
            setI_XX_VLO_LEADTIMES_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VLO_LeadTimes (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27580071519789L;
    /** Last Updated Timestamp 2011-02-17 08:06:43.0 */
    public static final long updatedMS = 1297946203000L;
    /** AD_Table_ID=1000318 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VLO_LeadTimes");
        
    }
    ;
    
    /** TableName=I_XX_VLO_LeadTimes */
    public static final String Table_Name="I_XX_VLO_LeadTimes";
    
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
    
    /** Set I_XX_VLO_LEADTIMES_ID.
    @param I_XX_VLO_LEADTIMES_ID I_XX_VLO_LEADTIMES_ID */
    public void setI_XX_VLO_LEADTIMES_ID (int I_XX_VLO_LEADTIMES_ID)
    {
        if (I_XX_VLO_LEADTIMES_ID < 1) throw new IllegalArgumentException ("I_XX_VLO_LEADTIMES_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VLO_LEADTIMES_ID", Integer.valueOf(I_XX_VLO_LEADTIMES_ID));
        
    }
    
    /** Get I_XX_VLO_LEADTIMES_ID.
    @return I_XX_VLO_LEADTIMES_ID */
    public int getI_XX_VLO_LEADTIMES_ID() 
    {
        return get_ValueAsInt("I_XX_VLO_LEADTIMES_ID");
        
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
    
    /** Set Vendor.
    @param XX_BPartnerName Vendor */
    public void setXX_BPartnerName (String XX_BPartnerName)
    {
        set_Value ("XX_BPartnerName", XX_BPartnerName);
        
    }
    
    /** Get Vendor.
    @return Vendor */
    public String getXX_BPartnerName() 
    {
        return (String)get_Value("XX_BPartnerName");
        
    }
    
    /** Set XX_COUNTRYNAME.
    @param XX_COUNTRYNAME XX_COUNTRYNAME */
    public void setXX_COUNTRYNAME (String XX_COUNTRYNAME)
    {
        set_Value ("XX_COUNTRYNAME", XX_COUNTRYNAME);
        
    }
    
    /** Get XX_COUNTRYNAME.
    @return XX_COUNTRYNAME */
    public String getXX_COUNTRYNAME() 
    {
        return (String)get_Value("XX_COUNTRYNAME");
        
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
    
    /** Set XX_PORTNAME.
    @param XX_PORTNAME XX_PORTNAME */
    public void setXX_PORTNAME (String XX_PORTNAME)
    {
        set_Value ("XX_PORTNAME", XX_PORTNAME);
        
    }
    
    /** Get XX_PORTNAME.
    @return XX_PORTNAME */
    public String getXX_PORTNAME() 
    {
        return (String)get_Value("XX_PORTNAME");
        
    }
    
    /** Set XX_TEI.
    @param XX_TEI XX_TEI */
    public void setXX_TEI (java.math.BigDecimal XX_TEI)
    {
        set_Value ("XX_TEI", XX_TEI);
        
    }
    
    /** Get XX_TEI.
    @return XX_TEI */
    public java.math.BigDecimal getXX_TEI() 
    {
        return get_ValueAsBigDecimal("XX_TEI");
        
    }
    
    /** Set XX_TEN.
    @param XX_TEN XX_TEN */
    public void setXX_TEN (java.math.BigDecimal XX_TEN)
    {
        set_Value ("XX_TEN", XX_TEN);
        
    }
    
    /** Get XX_TEN.
    @return XX_TEN */
    public java.math.BigDecimal getXX_TEN() 
    {
        return get_ValueAsBigDecimal("XX_TEN");
        
    }
    
    /** Set XX_TLI.
    @param XX_TLI XX_TLI */
    public void setXX_TLI (java.math.BigDecimal XX_TLI)
    {
        set_Value ("XX_TLI", XX_TLI);
        
    }
    
    /** Get XX_TLI.
    @return XX_TLI */
    public java.math.BigDecimal getXX_TLI() 
    {
        return get_ValueAsBigDecimal("XX_TLI");
        
    }
    
    /** Set TNAC.
    @param XX_Tnac TNAC */
    public void setXX_Tnac (java.math.BigDecimal XX_Tnac)
    {
        set_Value ("XX_Tnac", XX_Tnac);
        
    }
    
    /** Get TNAC.
    @return TNAC */
    public java.math.BigDecimal getXX_Tnac() 
    {
        return get_ValueAsBigDecimal("XX_Tnac");
        
    }
    
    /** Set XX_TRC.
    @param XX_TRC XX_TRC */
    public void setXX_TRC (java.math.BigDecimal XX_TRC)
    {
        set_Value ("XX_TRC", XX_TRC);
        
    }
    
    /** Get XX_TRC.
    @return XX_TRC */
    public java.math.BigDecimal getXX_TRC() 
    {
        return get_ValueAsBigDecimal("XX_TRC");
        
    }
    
    /** Set XX_TT.
    @param XX_TT XX_TT */
    public void setXX_TT (java.math.BigDecimal XX_TT)
    {
        set_Value ("XX_TT", XX_TT);
        
    }
    
    /** Get XX_TT.
    @return XX_TT */
    public java.math.BigDecimal getXX_TT() 
    {
        return get_ValueAsBigDecimal("XX_TT");
        
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
    
    /** Set XX_VLO_LEADTIMES_ID.
    @param XX_VLO_LEADTIMES_ID XX_VLO_LEADTIMES_ID */
    public void setXX_VLO_LEADTIMES_ID (int XX_VLO_LEADTIMES_ID)
    {
        if (XX_VLO_LEADTIMES_ID <= 0) set_Value ("XX_VLO_LEADTIMES_ID", null);
        else
        set_Value ("XX_VLO_LEADTIMES_ID", Integer.valueOf(XX_VLO_LEADTIMES_ID));
        
    }
    
    /** Get XX_VLO_LEADTIMES_ID.
    @return XX_VLO_LEADTIMES_ID */
    public int getXX_VLO_LEADTIMES_ID() 
    {
        return get_ValueAsInt("XX_VLO_LEADTIMES_ID");
        
    }
    
    
}
