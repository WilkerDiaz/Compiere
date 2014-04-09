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
/** Generated Model for T_Report
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_T_Report.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_T_Report extends PO
{
    /** Standard Constructor
    @param ctx context
    @param T_Report_ID id
    @param trx transaction
    */
    public X_T_Report (Ctx ctx, int T_Report_ID, Trx trx)
    {
        super (ctx, T_Report_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (T_Report_ID == 0)
        {
            setAD_PInstance_ID (0);
            setFact_Acct_ID (0);
            setPA_ReportLine_ID (0);
            setRecord_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_T_Report (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27511671701789L;
    /** Last Updated Timestamp 2008-12-17 12:39:45.0 */
    public static final long updatedMS = 1229546385000L;
    /** AD_Table_ID=544 */
    public static final int Table_ID=544;
    
    /** TableName=T_Report */
    public static final String Table_Name="T_Report";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Process Instance.
    @param AD_PInstance_ID Instance of the process */
    public void setAD_PInstance_ID (int AD_PInstance_ID)
    {
        if (AD_PInstance_ID < 1) throw new IllegalArgumentException ("AD_PInstance_ID is mandatory.");
        set_ValueNoCheck ("AD_PInstance_ID", Integer.valueOf(AD_PInstance_ID));
        
    }
    
    /** Get Process Instance.
    @return Instance of the process */
    public int getAD_PInstance_ID() 
    {
        return get_ValueAsInt("AD_PInstance_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_PInstance_ID()));
        
    }
    
    /** Set Col_0.
    @param Col_0 Col_0 */
    public void setCol_0 (java.math.BigDecimal Col_0)
    {
        set_ValueNoCheck ("Col_0", Col_0);
        
    }
    
    /** Get Col_0.
    @return Col_0 */
    public java.math.BigDecimal getCol_0() 
    {
        return get_ValueAsBigDecimal("Col_0");
        
    }
    
    /** Set Col_1.
    @param Col_1 Col_1 */
    public void setCol_1 (java.math.BigDecimal Col_1)
    {
        set_ValueNoCheck ("Col_1", Col_1);
        
    }
    
    /** Get Col_1.
    @return Col_1 */
    public java.math.BigDecimal getCol_1() 
    {
        return get_ValueAsBigDecimal("Col_1");
        
    }
    
    /** Set Col_10.
    @param Col_10 Col_10 */
    public void setCol_10 (java.math.BigDecimal Col_10)
    {
        set_ValueNoCheck ("Col_10", Col_10);
        
    }
    
    /** Get Col_10.
    @return Col_10 */
    public java.math.BigDecimal getCol_10() 
    {
        return get_ValueAsBigDecimal("Col_10");
        
    }
    
    /** Set Col_11.
    @param Col_11 Col_11 */
    public void setCol_11 (java.math.BigDecimal Col_11)
    {
        set_ValueNoCheck ("Col_11", Col_11);
        
    }
    
    /** Get Col_11.
    @return Col_11 */
    public java.math.BigDecimal getCol_11() 
    {
        return get_ValueAsBigDecimal("Col_11");
        
    }
    
    /** Set Col_12.
    @param Col_12 Col_12 */
    public void setCol_12 (java.math.BigDecimal Col_12)
    {
        set_ValueNoCheck ("Col_12", Col_12);
        
    }
    
    /** Get Col_12.
    @return Col_12 */
    public java.math.BigDecimal getCol_12() 
    {
        return get_ValueAsBigDecimal("Col_12");
        
    }
    
    /** Set Col_13.
    @param Col_13 Col_13 */
    public void setCol_13 (java.math.BigDecimal Col_13)
    {
        set_ValueNoCheck ("Col_13", Col_13);
        
    }
    
    /** Get Col_13.
    @return Col_13 */
    public java.math.BigDecimal getCol_13() 
    {
        return get_ValueAsBigDecimal("Col_13");
        
    }
    
    /** Set Col_14.
    @param Col_14 Col_14 */
    public void setCol_14 (java.math.BigDecimal Col_14)
    {
        set_ValueNoCheck ("Col_14", Col_14);
        
    }
    
    /** Get Col_14.
    @return Col_14 */
    public java.math.BigDecimal getCol_14() 
    {
        return get_ValueAsBigDecimal("Col_14");
        
    }
    
    /** Set Col_15.
    @param Col_15 Col_15 */
    public void setCol_15 (java.math.BigDecimal Col_15)
    {
        set_ValueNoCheck ("Col_15", Col_15);
        
    }
    
    /** Get Col_15.
    @return Col_15 */
    public java.math.BigDecimal getCol_15() 
    {
        return get_ValueAsBigDecimal("Col_15");
        
    }
    
    /** Set Col_16.
    @param Col_16 Col_16 */
    public void setCol_16 (java.math.BigDecimal Col_16)
    {
        set_ValueNoCheck ("Col_16", Col_16);
        
    }
    
    /** Get Col_16.
    @return Col_16 */
    public java.math.BigDecimal getCol_16() 
    {
        return get_ValueAsBigDecimal("Col_16");
        
    }
    
    /** Set Col_17.
    @param Col_17 Col_17 */
    public void setCol_17 (java.math.BigDecimal Col_17)
    {
        set_ValueNoCheck ("Col_17", Col_17);
        
    }
    
    /** Get Col_17.
    @return Col_17 */
    public java.math.BigDecimal getCol_17() 
    {
        return get_ValueAsBigDecimal("Col_17");
        
    }
    
    /** Set Col_18.
    @param Col_18 Col_18 */
    public void setCol_18 (java.math.BigDecimal Col_18)
    {
        set_ValueNoCheck ("Col_18", Col_18);
        
    }
    
    /** Get Col_18.
    @return Col_18 */
    public java.math.BigDecimal getCol_18() 
    {
        return get_ValueAsBigDecimal("Col_18");
        
    }
    
    /** Set Col_19.
    @param Col_19 Col_19 */
    public void setCol_19 (java.math.BigDecimal Col_19)
    {
        set_ValueNoCheck ("Col_19", Col_19);
        
    }
    
    /** Get Col_19.
    @return Col_19 */
    public java.math.BigDecimal getCol_19() 
    {
        return get_ValueAsBigDecimal("Col_19");
        
    }
    
    /** Set Col_2.
    @param Col_2 Col_2 */
    public void setCol_2 (java.math.BigDecimal Col_2)
    {
        set_ValueNoCheck ("Col_2", Col_2);
        
    }
    
    /** Get Col_2.
    @return Col_2 */
    public java.math.BigDecimal getCol_2() 
    {
        return get_ValueAsBigDecimal("Col_2");
        
    }
    
    /** Set Col_20.
    @param Col_20 Col_20 */
    public void setCol_20 (java.math.BigDecimal Col_20)
    {
        set_ValueNoCheck ("Col_20", Col_20);
        
    }
    
    /** Get Col_20.
    @return Col_20 */
    public java.math.BigDecimal getCol_20() 
    {
        return get_ValueAsBigDecimal("Col_20");
        
    }
    
    /** Set Col_3.
    @param Col_3 Col_3 */
    public void setCol_3 (java.math.BigDecimal Col_3)
    {
        set_ValueNoCheck ("Col_3", Col_3);
        
    }
    
    /** Get Col_3.
    @return Col_3 */
    public java.math.BigDecimal getCol_3() 
    {
        return get_ValueAsBigDecimal("Col_3");
        
    }
    
    /** Set Col_4.
    @param Col_4 Col_4 */
    public void setCol_4 (java.math.BigDecimal Col_4)
    {
        set_ValueNoCheck ("Col_4", Col_4);
        
    }
    
    /** Get Col_4.
    @return Col_4 */
    public java.math.BigDecimal getCol_4() 
    {
        return get_ValueAsBigDecimal("Col_4");
        
    }
    
    /** Set Col_5.
    @param Col_5 Col_5 */
    public void setCol_5 (java.math.BigDecimal Col_5)
    {
        set_ValueNoCheck ("Col_5", Col_5);
        
    }
    
    /** Get Col_5.
    @return Col_5 */
    public java.math.BigDecimal getCol_5() 
    {
        return get_ValueAsBigDecimal("Col_5");
        
    }
    
    /** Set Col_6.
    @param Col_6 Col_6 */
    public void setCol_6 (java.math.BigDecimal Col_6)
    {
        set_ValueNoCheck ("Col_6", Col_6);
        
    }
    
    /** Get Col_6.
    @return Col_6 */
    public java.math.BigDecimal getCol_6() 
    {
        return get_ValueAsBigDecimal("Col_6");
        
    }
    
    /** Set Col_7.
    @param Col_7 Col_7 */
    public void setCol_7 (java.math.BigDecimal Col_7)
    {
        set_ValueNoCheck ("Col_7", Col_7);
        
    }
    
    /** Get Col_7.
    @return Col_7 */
    public java.math.BigDecimal getCol_7() 
    {
        return get_ValueAsBigDecimal("Col_7");
        
    }
    
    /** Set Col_8.
    @param Col_8 Col_8 */
    public void setCol_8 (java.math.BigDecimal Col_8)
    {
        set_ValueNoCheck ("Col_8", Col_8);
        
    }
    
    /** Get Col_8.
    @return Col_8 */
    public java.math.BigDecimal getCol_8() 
    {
        return get_ValueAsBigDecimal("Col_8");
        
    }
    
    /** Set Col_9.
    @param Col_9 Col_9 */
    public void setCol_9 (java.math.BigDecimal Col_9)
    {
        set_ValueNoCheck ("Col_9", Col_9);
        
    }
    
    /** Get Col_9.
    @return Col_9 */
    public java.math.BigDecimal getCol_9() 
    {
        return get_ValueAsBigDecimal("Col_9");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_ValueNoCheck ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Accounting Fact.
    @param Fact_Acct_ID Accounting Fact */
    public void setFact_Acct_ID (int Fact_Acct_ID)
    {
        if (Fact_Acct_ID < 1) throw new IllegalArgumentException ("Fact_Acct_ID is mandatory.");
        set_ValueNoCheck ("Fact_Acct_ID", Integer.valueOf(Fact_Acct_ID));
        
    }
    
    /** Get Accounting Fact.
    @return Accounting Fact */
    public int getFact_Acct_ID() 
    {
        return get_ValueAsInt("Fact_Acct_ID");
        
    }
    
    /** Set Level no.
    @param LevelNo Level Number */
    public void setLevelNo (int LevelNo)
    {
        set_ValueNoCheck ("LevelNo", Integer.valueOf(LevelNo));
        
    }
    
    /** Get Level no.
    @return Level Number */
    public int getLevelNo() 
    {
        return get_ValueAsInt("LevelNo");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_ValueNoCheck ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Report Line.
    @param PA_ReportLine_ID Report Line */
    public void setPA_ReportLine_ID (int PA_ReportLine_ID)
    {
        if (PA_ReportLine_ID < 1) throw new IllegalArgumentException ("PA_ReportLine_ID is mandatory.");
        set_ValueNoCheck ("PA_ReportLine_ID", Integer.valueOf(PA_ReportLine_ID));
        
    }
    
    /** Get Report Line.
    @return Report Line */
    public int getPA_ReportLine_ID() 
    {
        return get_ValueAsInt("PA_ReportLine_ID");
        
    }
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID < 0) throw new IllegalArgumentException ("Record_ID is mandatory.");
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set Sequence.
    @param SeqNo Method of ordering elements;
     lowest number comes first */
    public void setSeqNo (int SeqNo)
    {
        set_ValueNoCheck ("SeqNo", Integer.valueOf(SeqNo));
        
    }
    
    /** Get Sequence.
    @return Method of ordering elements;
     lowest number comes first */
    public int getSeqNo() 
    {
        return get_ValueAsInt("SeqNo");
        
    }
    
    
}
