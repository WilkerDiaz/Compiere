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
/** Generated Model for C_DocTypeGroupLine
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_DocTypeGroupLine.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_DocTypeGroupLine extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_DocTypeGroupLine_ID id
    @param trx transaction
    */
    public X_C_DocTypeGroupLine (Ctx ctx, int C_DocTypeGroupLine_ID, Trx trx)
    {
        super (ctx, C_DocTypeGroupLine_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_DocTypeGroupLine_ID == 0)
        {
            setC_DocTypeGroupLine_ID (0);
            setC_DocTypeGroup_ID (0);
            setC_DocType_ID (0);
            setSeqNo (0);	// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM C_DocTypeGroupLine WHERE C_DocTypeGroup_ID=@C_DocTypeGroup_ID@
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_DocTypeGroupLine (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495497657789L;
    /** Last Updated Timestamp 2008-06-13 08:52:21.0 */
    public static final long updatedMS = 1213372341000L;
    /** AD_Table_ID=1017 */
    public static final int Table_ID=1017;
    
    /** TableName=C_DocTypeGroupLine */
    public static final String Table_Name="C_DocTypeGroupLine";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order Type Group Line.
    @param C_DocTypeGroupLine_ID Standard Order Types belonging to the group */
    public void setC_DocTypeGroupLine_ID (int C_DocTypeGroupLine_ID)
    {
        if (C_DocTypeGroupLine_ID < 1) throw new IllegalArgumentException ("C_DocTypeGroupLine_ID is mandatory.");
        set_ValueNoCheck ("C_DocTypeGroupLine_ID", Integer.valueOf(C_DocTypeGroupLine_ID));
        
    }
    
    /** Get Order Type Group Line.
    @return Standard Order Types belonging to the group */
    public int getC_DocTypeGroupLine_ID() 
    {
        return get_ValueAsInt("C_DocTypeGroupLine_ID");
        
    }
    
    /** Set Order Type Group.
    @param C_DocTypeGroup_ID Order Type Groups allows grouping of different standard Sales Order types */
    public void setC_DocTypeGroup_ID (int C_DocTypeGroup_ID)
    {
        if (C_DocTypeGroup_ID < 1) throw new IllegalArgumentException ("C_DocTypeGroup_ID is mandatory.");
        set_ValueNoCheck ("C_DocTypeGroup_ID", Integer.valueOf(C_DocTypeGroup_ID));
        
    }
    
    /** Get Order Type Group.
    @return Order Type Groups allows grouping of different standard Sales Order types */
    public int getC_DocTypeGroup_ID() 
    {
        return get_ValueAsInt("C_DocTypeGroup_ID");
        
    }
    
    /** Set Document Type.
    @param C_DocType_ID Document type or rules */
    public void setC_DocType_ID (int C_DocType_ID)
    {
        if (C_DocType_ID < 0) throw new IllegalArgumentException ("C_DocType_ID is mandatory.");
        set_Value ("C_DocType_ID", Integer.valueOf(C_DocType_ID));
        
    }
    
    /** Get Document Type.
    @return Document type or rules */
    public int getC_DocType_ID() 
    {
        return get_ValueAsInt("C_DocType_ID");
        
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
