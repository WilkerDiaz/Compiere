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
/** Generated Model for XX_VMR_PO_ApprovalRole
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PO_ApprovalRole extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PO_ApprovalRole_ID id
    @param trx transaction
    */
    public X_XX_VMR_PO_ApprovalRole (Ctx ctx, int XX_VMR_PO_ApprovalRole_ID, Trx trx)
    {
        super (ctx, XX_VMR_PO_ApprovalRole_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PO_ApprovalRole_ID == 0)
        {
            setAD_Role_ID (0);
            setXX_NeedsOtherRole (false);
            setXX_RankHigh (Env.ZERO);
            setXX_RankLow (Env.ZERO);
            setXX_VMR_PO_ApprovalRole_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PO_ApprovalRole (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27622030202789L;
    /** Last Updated Timestamp 2012-06-16 23:18:06.0 */
    public static final long updatedMS = 1339904886000L;
    /** AD_Table_ID=1000401 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PO_ApprovalRole");
        
    }
    ;
    
    /** TableName=XX_VMR_PO_ApprovalRole */
    public static final String Table_Name="XX_VMR_PO_ApprovalRole";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID < 0) throw new IllegalArgumentException ("AD_Role_ID is mandatory.");
        set_Value ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set Needs Other Role.
    @param XX_NeedsOtherRole Needs Other Role */
    public void setXX_NeedsOtherRole (boolean XX_NeedsOtherRole)
    {
        set_Value ("XX_NeedsOtherRole", Boolean.valueOf(XX_NeedsOtherRole));
        
    }
    
    /** Get Needs Other Role.
    @return Needs Other Role */
    public boolean isXX_NeedsOtherRole() 
    {
        return get_ValueAsBoolean("XX_NeedsOtherRole");
        
    }
    
    /** Set Notifying Role.
    @param XX_NotifyingRole_ID Notifying Role */
    public void setXX_NotifyingRole_ID (int XX_NotifyingRole_ID)
    {
        if (XX_NotifyingRole_ID <= 0) set_Value ("XX_NotifyingRole_ID", null);
        else
        set_Value ("XX_NotifyingRole_ID", Integer.valueOf(XX_NotifyingRole_ID));
        
    }
    
    /** Get Notifying Role.
    @return Notifying Role */
    public int getXX_NotifyingRole_ID() 
    {
        return get_ValueAsInt("XX_NotifyingRole_ID");
        
    }
    
    /** Set Second Notifying Role.
    @param XX_NotifyingRole2_ID Second Notifying Role */
    public void setXX_NotifyingRole2_ID (int XX_NotifyingRole2_ID)
    {
        if (XX_NotifyingRole2_ID <= 0) set_Value ("XX_NotifyingRole2_ID", null);
        else
        set_Value ("XX_NotifyingRole2_ID", Integer.valueOf(XX_NotifyingRole2_ID));
        
    }
    
    /** Get Second Notifying Role.
    @return Second Notifying Role */
    public int getXX_NotifyingRole2_ID() 
    {
        return get_ValueAsInt("XX_NotifyingRole2_ID");
        
    }
    
    /** Set Rank High.
    @param XX_RankHigh Rank High */
    public void setXX_RankHigh (java.math.BigDecimal XX_RankHigh)
    {
        if (XX_RankHigh == null) throw new IllegalArgumentException ("XX_RankHigh is mandatory.");
        set_Value ("XX_RankHigh", XX_RankHigh);
        
    }
    
    /** Get Rank High.
    @return Rank High */
    public java.math.BigDecimal getXX_RankHigh() 
    {
        return get_ValueAsBigDecimal("XX_RankHigh");
        
    }
    
    /** Set Rank Low.
    @param XX_RankLow Rank Low */
    public void setXX_RankLow (java.math.BigDecimal XX_RankLow)
    {
        if (XX_RankLow == null) throw new IllegalArgumentException ("XX_RankLow is mandatory.");
        set_Value ("XX_RankLow", XX_RankLow);
        
    }
    
    /** Get Rank Low.
    @return Rank Low */
    public java.math.BigDecimal getXX_RankLow() 
    {
        return get_ValueAsBigDecimal("XX_RankLow");
        
    }
    
    /** Set Tax Unit.
    @param XX_UT Tax Unit */
    public void setXX_UT (java.math.BigDecimal XX_UT)
    {
        set_Value ("XX_UT", XX_UT);
        
    }
    
    /** Get Tax Unit.
    @return Tax Unit */
    public java.math.BigDecimal getXX_UT() 
    {
        return get_ValueAsBigDecimal("XX_UT");
        
    }
    
    /** Set Approval Role.
    @param XX_VMR_PO_ApprovalRole_ID Approval Role */
    public void setXX_VMR_PO_ApprovalRole_ID (int XX_VMR_PO_ApprovalRole_ID)
    {
        if (XX_VMR_PO_ApprovalRole_ID < 1) throw new IllegalArgumentException ("XX_VMR_PO_ApprovalRole_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PO_ApprovalRole_ID", Integer.valueOf(XX_VMR_PO_ApprovalRole_ID));
        
    }
    
    /** Get Approval Role.
    @return Approval Role */
    public int getXX_VMR_PO_ApprovalRole_ID() 
    {
        return get_ValueAsInt("XX_VMR_PO_ApprovalRole_ID");
        
    }
    
    
}
