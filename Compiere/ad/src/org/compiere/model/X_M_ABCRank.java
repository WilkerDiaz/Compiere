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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for M_ABCRank
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.2 Dev - $Id: GenerateModel.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ABCRank extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ABCRank_ID id
    @param trx transaction
    */
    public X_M_ABCRank (Ctx ctx, int M_ABCRank_ID, Trx trx)
    {
        super (ctx, M_ABCRank_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ABCRank_ID == 0)
        {
            setABCRank (null);
            setDuration (Env.ZERO);
            setFrequency (null);
            setIsBasedOnPriceList (false);
            setM_ABCAnalysisGroup_ID (0);
            setM_ABCRankSort_ID (0);
            setM_ABCRank_ID (0);
            setPercentage (Env.ZERO);
            setSeqNo (0);	// @SQL=SELECT COALESCE(MAX(SeqNo),0)+10 AS DefaultValue FROM M_ABCRank WHERE M_ABCAnalysisGroup_ID=@M_ABCAnalysisGroup_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ABCRank (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27548385582789L;
    /** Last Updated Timestamp 2010-02-16 00:27:46.0 */
    public static final long updatedMS = 1266260266000L;
    /** AD_Table_ID=2157 */
    public static final int Table_ID=2157;
    
    /** TableName=M_ABCRank */
    public static final String Table_Name="M_ABCRank";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Rank.
    @param ABCRank Rank */
    public void setABCRank (String ABCRank)
    {
        if (ABCRank == null) throw new IllegalArgumentException ("ABCRank is mandatory.");
        set_Value ("ABCRank", ABCRank);
        
    }
    
    /** Get Rank.
    @return Rank */
    public String getABCRank() 
    {
        return (String)get_Value("ABCRank");
        
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
    
    /** Set Duration.
    @param Duration Normal Duration in Duration Unit */
    public void setDuration (java.math.BigDecimal Duration)
    {
        if (Duration == null) throw new IllegalArgumentException ("Duration is mandatory.");
        set_Value ("Duration", Duration);
        
    }
    
    /** Get Duration.
    @return Normal Duration in Duration Unit */
    public java.math.BigDecimal getDuration() 
    {
        return get_ValueAsBigDecimal("Duration");
        
    }
    
    /** Daily = D */
    public static final String FREQUENCY_Daily = X_Ref_M_ABCFrequency.DAILY.getValue();
    /** Monthly = M */
    public static final String FREQUENCY_Monthly = X_Ref_M_ABCFrequency.MONTHLY.getValue();
    /** Quarterly = Q */
    public static final String FREQUENCY_Quarterly = X_Ref_M_ABCFrequency.QUARTERLY.getValue();
    /** Weekly = W */
    public static final String FREQUENCY_Weekly = X_Ref_M_ABCFrequency.WEEKLY.getValue();
    /** Yearly = Y */
    public static final String FREQUENCY_Yearly = X_Ref_M_ABCFrequency.YEARLY.getValue();
    /** Set Frequency.
    @param Frequency Frequency of events */
    public void setFrequency (String Frequency)
    {
        if (Frequency == null) throw new IllegalArgumentException ("Frequency is mandatory");
        if (!X_Ref_M_ABCFrequency.isValid(Frequency))
        throw new IllegalArgumentException ("Frequency Invalid value - " + Frequency + " - Reference_ID=531 - D - M - Q - W - Y");
        set_Value ("Frequency", Frequency);
        
    }
    
    /** Get Frequency.
    @return Frequency of events */
    public String getFrequency() 
    {
        return (String)get_Value("Frequency");
        
    }
    
    /** Set Based on Price List.
    @param IsBasedOnPriceList Is the criteria based on price list */
    public void setIsBasedOnPriceList (boolean IsBasedOnPriceList)
    {
        set_Value ("IsBasedOnPriceList", Boolean.valueOf(IsBasedOnPriceList));
        
    }
    
    /** Get Based on Price List.
    @return Is the criteria based on price list */
    public boolean isBasedOnPriceList() 
    {
        return get_ValueAsBoolean("IsBasedOnPriceList");
        
    }
    
    /** Set M_ABCAnalysisGroup_ID.
    @param M_ABCAnalysisGroup_ID M_ABCAnalysisGroup_ID */
    public void setM_ABCAnalysisGroup_ID (int M_ABCAnalysisGroup_ID)
    {
        if (M_ABCAnalysisGroup_ID < 1) throw new IllegalArgumentException ("M_ABCAnalysisGroup_ID is mandatory.");
        set_Value ("M_ABCAnalysisGroup_ID", Integer.valueOf(M_ABCAnalysisGroup_ID));
        
    }
    
    /** Get M_ABCAnalysisGroup_ID.
    @return M_ABCAnalysisGroup_ID */
    public int getM_ABCAnalysisGroup_ID() 
    {
        return get_ValueAsInt("M_ABCAnalysisGroup_ID");
        
    }
    
    /** Set Rank Sort Criteria.
    @param M_ABCRankSort_ID Rank Sort Criteria */
    public void setM_ABCRankSort_ID (int M_ABCRankSort_ID)
    {
        if (M_ABCRankSort_ID < 1) throw new IllegalArgumentException ("M_ABCRankSort_ID is mandatory.");
        set_Value ("M_ABCRankSort_ID", Integer.valueOf(M_ABCRankSort_ID));
        
    }
    
    /** Get Rank Sort Criteria.
    @return Rank Sort Criteria */
    public int getM_ABCRankSort_ID() 
    {
        return get_ValueAsInt("M_ABCRankSort_ID");
        
    }
    
    /** Set Rank.
    @param M_ABCRank_ID Rank */
    public void setM_ABCRank_ID (int M_ABCRank_ID)
    {
        if (M_ABCRank_ID < 1) throw new IllegalArgumentException ("M_ABCRank_ID is mandatory.");
        set_ValueNoCheck ("M_ABCRank_ID", Integer.valueOf(M_ABCRank_ID));
        
    }
    
    /** Get Rank.
    @return Rank */
    public int getM_ABCRank_ID() 
    {
        return get_ValueAsInt("M_ABCRank_ID");
        
    }
    
    /** Set Price List Version.
    @param M_PriceList_Version_ID Identifies a unique instance of a Price List */
    public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
    {
        if (M_PriceList_Version_ID <= 0) set_Value ("M_PriceList_Version_ID", null);
        else
        set_Value ("M_PriceList_Version_ID", Integer.valueOf(M_PriceList_Version_ID));
        
    }
    
    /** Get Price List Version.
    @return Identifies a unique instance of a Price List */
    public int getM_PriceList_Version_ID() 
    {
        return get_ValueAsInt("M_PriceList_Version_ID");
        
    }
    
    /** Set Percentage.
    @param Percentage Percent of the entire amount */
    public void setPercentage (java.math.BigDecimal Percentage)
    {
        if (Percentage == null) throw new IllegalArgumentException ("Percentage is mandatory.");
        set_Value ("Percentage", Percentage);
        
    }
    
    /** Get Percentage.
    @return Percent of the entire amount */
    public java.math.BigDecimal getPercentage() 
    {
        return get_ValueAsBigDecimal("Percentage");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_Value ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    
}
