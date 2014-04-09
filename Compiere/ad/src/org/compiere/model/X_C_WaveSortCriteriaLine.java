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
/** Generated Model for C_WaveSortCriteriaLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_WaveSortCriteriaLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_WaveSortCriteriaLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_WaveSortCriteriaLine_ID id
    @param trx transaction
    */
    public X_C_WaveSortCriteriaLine (Ctx ctx, int C_WaveSortCriteriaLine_ID, Trx trx)
    {
        super (ctx, C_WaveSortCriteriaLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_WaveSortCriteriaLine_ID == 0)
        {
            setAD_InfoColumn_ID (0);
            setC_WaveSortCriteriaLine_ID (0);
            setC_WaveSortCriteria_ID (0);
            setOrderByType (null);	// A
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM C_WaveSortCriteriaLine WHERE C_WaveSortCriteria_ID=@C_WaveSortCriteria_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_WaveSortCriteriaLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27502607088789L;
    /** Last Updated Timestamp 2008-09-03 15:42:52.0 */
    public static final long updatedMS = 1220481772000L;
    /** AD_Table_ID=1045 */
    public static final int Table_ID=1045;
    
    /** TableName=C_WaveSortCriteriaLine */
    public static final String Table_Name="C_WaveSortCriteriaLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Info Column.
    @param AD_InfoColumn_ID Info Window Column */
    public void setAD_InfoColumn_ID (int AD_InfoColumn_ID)
    {
        if (AD_InfoColumn_ID < 1) throw new IllegalArgumentException ("AD_InfoColumn_ID is mandatory.");
        set_Value ("AD_InfoColumn_ID", Integer.valueOf(AD_InfoColumn_ID));
        
    }
    
    /** Get Info Column.
    @return Info Window Column */
    public int getAD_InfoColumn_ID() 
    {
        return get_ValueAsInt("AD_InfoColumn_ID");
        
    }
    
    /** Set Wave Sort Criteria Line.
    @param C_WaveSortCriteriaLine_ID Lines (Order By columns) in the Wave Sort Criteria */
    public void setC_WaveSortCriteriaLine_ID (int C_WaveSortCriteriaLine_ID)
    {
        if (C_WaveSortCriteriaLine_ID < 1) throw new IllegalArgumentException ("C_WaveSortCriteriaLine_ID is mandatory.");
        set_ValueNoCheck ("C_WaveSortCriteriaLine_ID", Integer.valueOf(C_WaveSortCriteriaLine_ID));
        
    }
    
    /** Get Wave Sort Criteria Line.
    @return Lines (Order By columns) in the Wave Sort Criteria */
    public int getC_WaveSortCriteriaLine_ID() 
    {
        return get_ValueAsInt("C_WaveSortCriteriaLine_ID");
        
    }
    
    /** Set Wave Sort Criteria.
    @param C_WaveSortCriteria_ID Sort criteria to be applied on orders selected during wave planning */
    public void setC_WaveSortCriteria_ID (int C_WaveSortCriteria_ID)
    {
        if (C_WaveSortCriteria_ID < 1) throw new IllegalArgumentException ("C_WaveSortCriteria_ID is mandatory.");
        set_ValueNoCheck ("C_WaveSortCriteria_ID", Integer.valueOf(C_WaveSortCriteria_ID));
        
    }
    
    /** Get Wave Sort Criteria.
    @return Sort criteria to be applied on orders selected during wave planning */
    public int getC_WaveSortCriteria_ID() 
    {
        return get_ValueAsInt("C_WaveSortCriteria_ID");
        
    }
    
    /** Ascending = A */
    public static final String ORDERBYTYPE_Ascending = X_Ref_Order_By_Type.ASCENDING.getValue();
    /** Descending = D */
    public static final String ORDERBYTYPE_Descending = X_Ref_Order_By_Type.DESCENDING.getValue();
    /** Set Order By Type.
    @param OrderByType Type of Order By - Ascending or Descending */
    public void setOrderByType (String OrderByType)
    {
        if (OrderByType == null) throw new IllegalArgumentException ("OrderByType is mandatory");
        if (!X_Ref_Order_By_Type.isValid(OrderByType))
        throw new IllegalArgumentException ("OrderByType Invalid value - " + OrderByType + " - Reference_ID=473 - A - D");
        set_Value ("OrderByType", OrderByType);
        
    }
    
    /** Get Order By Type.
    @return Type of Order By - Ascending or Descending */
    public String getOrderByType() 
    {
        return (String)get_Value("OrderByType");
        
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
