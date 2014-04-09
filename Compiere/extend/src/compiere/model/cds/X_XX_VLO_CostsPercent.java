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
/** Generated Model for XX_VLO_CostsPercent
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_CostsPercent extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_CostsPercent_ID id
    @param trx transaction
    */
    public X_XX_VLO_CostsPercent (Ctx ctx, int XX_VLO_CostsPercent_ID, Trx trx)
    {
        super (ctx, XX_VLO_CostsPercent_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_CostsPercent_ID == 0)
        {
            setC_Country_ID (0);
            setXX_DATEH (null);
            setXX_VLO_ArrivalPort_ID (0);
            setXX_VLO_CostsPercent_ID (0);
            setXX_YEARH (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_CostsPercent (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27546908319789L;
    /** Last Updated Timestamp 2010-01-29 12:06:43.0 */
    public static final long updatedMS = 1264783003000L;
    /** AD_Table_ID=1000134 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_CostsPercent");
        
    }
    ;
    
    /** TableName=XX_VLO_CostsPercent */
    public static final String Table_Name="XX_VLO_CostsPercent";
    
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
        if (C_Country_ID < 1) throw new IllegalArgumentException ("C_Country_ID is mandatory.");
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Date From.
    @param XX_DATEH Date From */
    public void setXX_DATEH (String XX_DATEH)
    {
        if (XX_DATEH == null) throw new IllegalArgumentException ("XX_DATEH is mandatory.");
        set_Value ("XX_DATEH", XX_DATEH);
        
    }
    
    /** Get Date From.
    @return Date From */
    public String getXX_DATEH() 
    {
        return (String)get_Value("XX_DATEH");
        
    }
    
    /** Set Estimated Percentage Insurance.
    @param XX_ESTIMATEDPERTINSUR Estimated Percentage Insurance */
    public void setXX_ESTIMATEDPERTINSUR (java.math.BigDecimal XX_ESTIMATEDPERTINSUR)
    {
        set_Value ("XX_ESTIMATEDPERTINSUR", XX_ESTIMATEDPERTINSUR);
        
    }
    
    /** Get Estimated Percentage Insurance.
    @return Estimated Percentage Insurance */
    public java.math.BigDecimal getXX_ESTIMATEDPERTINSUR() 
    {
        return get_ValueAsBigDecimal("XX_ESTIMATEDPERTINSUR");
        
    }
    
    /** Set Estimated Percentage Customs Agent.
    @param XX_ESTIMATEDPERTUSAGENT Estimated Percentage Customs Agent */
    public void setXX_ESTIMATEDPERTUSAGENT (java.math.BigDecimal XX_ESTIMATEDPERTUSAGENT)
    {
        set_Value ("XX_ESTIMATEDPERTUSAGENT", XX_ESTIMATEDPERTUSAGENT);
        
    }
    
    /** Get Estimated Percentage Customs Agent.
    @return Estimated Percentage Customs Agent */
    public java.math.BigDecimal getXX_ESTIMATEDPERTUSAGENT() 
    {
        return get_ValueAsBigDecimal("XX_ESTIMATEDPERTUSAGENT");
        
    }
    
    /** Set International Freight Estimate Percentage.
    @param XX_INTERFREESTIMATEPERT International Freight Estimate Percentage */
    public void setXX_INTERFREESTIMATEPERT (java.math.BigDecimal XX_INTERFREESTIMATEPERT)
    {
        set_Value ("XX_INTERFREESTIMATEPERT", XX_INTERFREESTIMATEPERT);
        
    }
    
    /** Get International Freight Estimate Percentage.
    @return International Freight Estimate Percentage */
    public java.math.BigDecimal getXX_INTERFREESTIMATEPERT() 
    {
        return get_ValueAsBigDecimal("XX_INTERFREESTIMATEPERT");
        
    }
    
    /** Set National Freight Estimate Percentage.
    @param XX_NACFREESTIMATEPERT National Freight Estimate Percentage */
    public void setXX_NACFREESTIMATEPERT (java.math.BigDecimal XX_NACFREESTIMATEPERT)
    {
        set_Value ("XX_NACFREESTIMATEPERT", XX_NACFREESTIMATEPERT);
        
    }
    
    /** Get National Freight Estimate Percentage.
    @return National Freight Estimate Percentage */
    public java.math.BigDecimal getXX_NACFREESTIMATEPERT() 
    {
        return get_ValueAsBigDecimal("XX_NACFREESTIMATEPERT");
        
    }
    
    /** Set Arrival Port.
    @param XX_VLO_ArrivalPort_ID Arrival Port */
    public void setXX_VLO_ArrivalPort_ID (int XX_VLO_ArrivalPort_ID)
    {
        if (XX_VLO_ArrivalPort_ID < 1) throw new IllegalArgumentException ("XX_VLO_ArrivalPort_ID is mandatory.");
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
        if (XX_VLO_CostsPercent_ID < 1) throw new IllegalArgumentException ("XX_VLO_CostsPercent_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_CostsPercent_ID", Integer.valueOf(XX_VLO_CostsPercent_ID));
        
    }
    
    /** Get XX_VLO_CostsPercent_ID.
    @return XX_VLO_CostsPercent_ID */
    public int getXX_VLO_CostsPercent_ID() 
    {
        return get_ValueAsInt("XX_VLO_CostsPercent_ID");
        
    }
    
    /** Set Date Until.
    @param XX_YEARH Date Until */
    public void setXX_YEARH (String XX_YEARH)
    {
        if (XX_YEARH == null) throw new IllegalArgumentException ("XX_YEARH is mandatory.");
        set_Value ("XX_YEARH", XX_YEARH);
        
    }
    
    /** Get Date Until.
    @return Date Until */
    public String getXX_YEARH() 
    {
        return (String)get_Value("XX_YEARH");
        
    }
    
    
}
