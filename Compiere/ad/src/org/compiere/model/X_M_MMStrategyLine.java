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
/** Generated Model for M_MMStrategyLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_MMStrategyLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_MMStrategyLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_MMStrategyLine_ID id
    @param trx transaction
    */
    public X_M_MMStrategyLine (Ctx ctx, int M_MMStrategyLine_ID, Trx trx)
    {
        super (ctx, M_MMStrategyLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_MMStrategyLine_ID == 0)
        {
            setM_MMRule_ID (0);
            setM_MMStrategyLine_ID (0);
            setM_MMStrategy_ID (0);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM M_MMStrategyLine WHERE M_MMStrategy_ID=@M_MMStrategy_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_MMStrategyLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27503840378789L;
    /** Last Updated Timestamp 2008-09-17 22:17:42.0 */
    public static final long updatedMS = 1221715062000L;
    /** AD_Table_ID=1041 */
    public static final int Table_ID=1041;
    
    /** TableName=M_MMStrategyLine */
    public static final String Table_Name="M_MMStrategyLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Warehouse Management Rule.
    @param M_MMRule_ID Rule to determine the putaway or pick location for goods stocked in the warehouse */
    public void setM_MMRule_ID (int M_MMRule_ID)
    {
        if (M_MMRule_ID < 1) throw new IllegalArgumentException ("M_MMRule_ID is mandatory.");
        set_Value ("M_MMRule_ID", Integer.valueOf(M_MMRule_ID));
        
    }
    
    /** Get Warehouse Management Rule.
    @return Rule to determine the putaway or pick location for goods stocked in the warehouse */
    public int getM_MMRule_ID() 
    {
        return get_ValueAsInt("M_MMRule_ID");
        
    }
    
    /** Set Material Management Strategy Line.
    @param M_MMStrategyLine_ID Rules included in the strategy */
    public void setM_MMStrategyLine_ID (int M_MMStrategyLine_ID)
    {
        if (M_MMStrategyLine_ID < 1) throw new IllegalArgumentException ("M_MMStrategyLine_ID is mandatory.");
        set_ValueNoCheck ("M_MMStrategyLine_ID", Integer.valueOf(M_MMStrategyLine_ID));
        
    }
    
    /** Get Material Management Strategy Line.
    @return Rules included in the strategy */
    public int getM_MMStrategyLine_ID() 
    {
        return get_ValueAsInt("M_MMStrategyLine_ID");
        
    }
    
    /** Set Warehouse Management Strategy.
    @param M_MMStrategy_ID Sequential group of rules used for picking or putaway */
    public void setM_MMStrategy_ID (int M_MMStrategy_ID)
    {
        if (M_MMStrategy_ID < 1) throw new IllegalArgumentException ("M_MMStrategy_ID is mandatory.");
        set_ValueNoCheck ("M_MMStrategy_ID", Integer.valueOf(M_MMStrategy_ID));
        
    }
    
    /** Get Warehouse Management Strategy.
    @return Sequential group of rules used for picking or putaway */
    public int getM_MMStrategy_ID() 
    {
        return get_ValueAsInt("M_MMStrategy_ID");
        
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
