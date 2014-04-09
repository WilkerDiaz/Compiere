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
/** Generated Model for XX_VMR_HeaderAssociation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_HeaderAssociation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_HeaderAssociation_ID id
    @param trx transaction
    */
    public X_XX_VMR_HeaderAssociation (Ctx ctx, int XX_VMR_HeaderAssociation_ID, Trx trx)
    {
        super (ctx, XX_VMR_HeaderAssociation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_HeaderAssociation_ID == 0)
        {
            setXX_VMR_HeaderAssociation_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_HeaderAssociation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27555314821789L;
    /** Last Updated Timestamp 2010-05-06 19:15:05.0 */
    public static final long updatedMS = 1273189505000L;
    /** AD_Table_ID=1000302 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_HeaderAssociation");
        
    }
    ;
    
    /** TableName=XX_VMR_HeaderAssociation */
    public static final String Table_Name="XX_VMR_HeaderAssociation";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Approved.
    @param IsApproved Indicates if this document requires approval */
    public void setIsApproved (boolean IsApproved)
    {
        set_Value ("IsApproved", Boolean.valueOf(IsApproved));
        
    }
    
    /** Get Approved.
    @return Indicates if this document requires approval */
    public boolean isApproved() 
    {
        return get_ValueAsBoolean("IsApproved");
        
    }
    
    /** Set Association Number.
    @param XX_AssociationNumber Association Number */
    public void setXX_AssociationNumber (int XX_AssociationNumber)
    {
        set_Value ("XX_AssociationNumber", Integer.valueOf(XX_AssociationNumber));
        
    }
    
    /** Get Association Number.
    @return Association Number */
    public int getXX_AssociationNumber() 
    {
        return get_ValueAsInt("XX_AssociationNumber");
        
    }
    
    /** Set DistributionHeader.
    @param XX_VMR_DistributionHeader_ID DistributionHeader */
    public void setXX_VMR_DistributionHeader_ID (int XX_VMR_DistributionHeader_ID)
    {
        if (XX_VMR_DistributionHeader_ID <= 0) set_Value ("XX_VMR_DistributionHeader_ID", null);
        else
        set_Value ("XX_VMR_DistributionHeader_ID", Integer.valueOf(XX_VMR_DistributionHeader_ID));
        
    }
    
    /** Get DistributionHeader.
    @return DistributionHeader */
    public int getXX_VMR_DistributionHeader_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionHeader_ID");
        
    }
    
    /** Set Header Association.
    @param XX_VMR_HeaderAssociation_ID Header Association */
    public void setXX_VMR_HeaderAssociation_ID (int XX_VMR_HeaderAssociation_ID)
    {
        if (XX_VMR_HeaderAssociation_ID < 1) throw new IllegalArgumentException ("XX_VMR_HeaderAssociation_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_HeaderAssociation_ID", Integer.valueOf(XX_VMR_HeaderAssociation_ID));
        
    }
    
    /** Get Header Association.
    @return Header Association */
    public int getXX_VMR_HeaderAssociation_ID() 
    {
        return get_ValueAsInt("XX_VMR_HeaderAssociation_ID");
        
    }
    
    
}
