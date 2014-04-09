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
/** Generated Model for M_ZoneRelationship
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_ZoneRelationship.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_ZoneRelationship extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_ZoneRelationship_ID id
    @param trx transaction
    */
    public X_M_ZoneRelationship (Ctx ctx, int M_ZoneRelationship_ID, Trx trx)
    {
        super (ctx, M_ZoneRelationship_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_ZoneRelationship_ID == 0)
        {
            setM_SourceZone_ID (0);
            setM_ZoneRelationship_ID (0);
            setM_Zone_ID (0);
            setReplenishmentSeqNo (0);	// 0
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_ZoneRelationship (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=1013 */
    public static final int Table_ID=1013;
    
    /** TableName=M_ZoneRelationship */
    public static final String Table_Name="M_ZoneRelationship";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Source Zone.
    @param M_SourceZone_ID Source Warehouse zone */
    public void setM_SourceZone_ID (int M_SourceZone_ID)
    {
        if (M_SourceZone_ID < 1) throw new IllegalArgumentException ("M_SourceZone_ID is mandatory.");
        set_Value ("M_SourceZone_ID", Integer.valueOf(M_SourceZone_ID));
        
    }
    
    /** Get Source Zone.
    @return Source Warehouse zone */
    public int getM_SourceZone_ID() 
    {
        return get_ValueAsInt("M_SourceZone_ID");
        
    }
    
    /** Set Zone Relationship.
    @param M_ZoneRelationship_ID Zone Relationships are used define replenishment sources */
    public void setM_ZoneRelationship_ID (int M_ZoneRelationship_ID)
    {
        if (M_ZoneRelationship_ID < 1) throw new IllegalArgumentException ("M_ZoneRelationship_ID is mandatory.");
        set_ValueNoCheck ("M_ZoneRelationship_ID", Integer.valueOf(M_ZoneRelationship_ID));
        
    }
    
    /** Get Zone Relationship.
    @return Zone Relationships are used define replenishment sources */
    public int getM_ZoneRelationship_ID() 
    {
        return get_ValueAsInt("M_ZoneRelationship_ID");
        
    }
    
    /** Set Zone.
    @param M_Zone_ID Warehouse zone */
    public void setM_Zone_ID (int M_Zone_ID)
    {
        if (M_Zone_ID < 1) throw new IllegalArgumentException ("M_Zone_ID is mandatory.");
        set_ValueNoCheck ("M_Zone_ID", Integer.valueOf(M_Zone_ID));
        
    }
    
    /** Get Zone.
    @return Warehouse zone */
    public int getM_Zone_ID() 
    {
        return get_ValueAsInt("M_Zone_ID");
        
    }
    
    /** Set Replenishment Sequence No.
    @param ReplenishmentSeqNo Replenishment Sequence No of zone */
    public void setReplenishmentSeqNo (int ReplenishmentSeqNo)
    {
        set_Value ("ReplenishmentSeqNo", Integer.valueOf(ReplenishmentSeqNo));
        
    }
    
    /** Get Replenishment Sequence No.
    @return Replenishment Sequence No of zone */
    public int getReplenishmentSeqNo() 
    {
        return get_ValueAsInt("ReplenishmentSeqNo");
        
    }
    
    
}
