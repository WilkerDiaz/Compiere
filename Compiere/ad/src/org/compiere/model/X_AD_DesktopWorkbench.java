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
/** Generated Model for AD_DesktopWorkbench
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_DesktopWorkbench.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_DesktopWorkbench extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_DesktopWorkbench_ID id
    @param trx transaction
    */
    public X_AD_DesktopWorkbench (Ctx ctx, int AD_DesktopWorkbench_ID, Trx trx)
    {
        super (ctx, AD_DesktopWorkbench_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_DesktopWorkbench_ID == 0)
        {
            setAD_DesktopWorkbench_ID (0);
            setAD_Desktop_ID (0);
            setAD_Workbench_ID (0);
            setSeqNo (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_DesktopWorkbench (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=459 */
    public static final int Table_ID=459;
    
    /** TableName=AD_DesktopWorkbench */
    public static final String Table_Name="AD_DesktopWorkbench";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Desktop Workbench.
    @param AD_DesktopWorkbench_ID Desktop Workbench */
    public void setAD_DesktopWorkbench_ID (int AD_DesktopWorkbench_ID)
    {
        if (AD_DesktopWorkbench_ID < 1) throw new IllegalArgumentException ("AD_DesktopWorkbench_ID is mandatory.");
        set_ValueNoCheck ("AD_DesktopWorkbench_ID", Integer.valueOf(AD_DesktopWorkbench_ID));
        
    }
    
    /** Get Desktop Workbench.
    @return Desktop Workbench */
    public int getAD_DesktopWorkbench_ID() 
    {
        return get_ValueAsInt("AD_DesktopWorkbench_ID");
        
    }
    
    /** Set Desktop.
    @param AD_Desktop_ID Collection of Workbenches */
    public void setAD_Desktop_ID (int AD_Desktop_ID)
    {
        if (AD_Desktop_ID < 1) throw new IllegalArgumentException ("AD_Desktop_ID is mandatory.");
        set_ValueNoCheck ("AD_Desktop_ID", Integer.valueOf(AD_Desktop_ID));
        
    }
    
    /** Get Desktop.
    @return Collection of Workbenches */
    public int getAD_Desktop_ID() 
    {
        return get_ValueAsInt("AD_Desktop_ID");
        
    }
    
    /** Set Workbench.
    @param AD_Workbench_ID Collection of windows, reports */
    public void setAD_Workbench_ID (int AD_Workbench_ID)
    {
        if (AD_Workbench_ID < 1) throw new IllegalArgumentException ("AD_Workbench_ID is mandatory.");
        set_Value ("AD_Workbench_ID", Integer.valueOf(AD_Workbench_ID));
        
    }
    
    /** Get Workbench.
    @return Collection of windows, reports */
    public int getAD_Workbench_ID() 
    {
        return get_ValueAsInt("AD_Workbench_ID");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getAD_Workbench_ID()));
        
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
