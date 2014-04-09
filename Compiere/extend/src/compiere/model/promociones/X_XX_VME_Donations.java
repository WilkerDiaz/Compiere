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
package compiere.model.promociones;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VME_Donations
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_Donations extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_Donations_ID id
    @param trx transaction
    */
    public X_XX_VME_Donations (Ctx ctx, int XX_VME_Donations_ID, Trx trx)
    {
        super (ctx, XX_VME_Donations_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_Donations_ID == 0)
        {
            setXX_VME_Donations_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_Donations (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27612590786789L;
    /** Last Updated Timestamp 2012-02-28 17:14:30.0 */
    public static final long updatedMS = 1330465470000L;
    /** AD_Table_ID=1000403 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_Donations");
        
    }
    ;
    
    /** TableName=XX_VME_Donations */
    public static final String Table_Name="XX_VME_Donations";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Amount.
    @param Amount Amount in a defined currency */
    public void setAmount (java.math.BigDecimal Amount)
    {
        set_Value ("Amount", Amount);
        
    }
    
    /** Get Amount.
    @return Amount in a defined currency */
    public java.math.BigDecimal getAmount() 
    {
        return get_ValueAsBigDecimal("Amount");
        
    }
    
    /** Set Finish Date.
    @param DateFinish Indicates the (planned) completion date */
    public void setDateFinish (Timestamp DateFinish)
    {
        set_ValueNoCheck ("DateFinish", DateFinish);
        
    }
    
    /** Get Finish Date.
    @return Indicates the (planned) completion date */
    public Timestamp getDateFinish() 
    {
        return (Timestamp)get_Value("DateFinish");
        
    }
    
    /** Set Date From.
    @param DateFrom Starting date for a range */
    public void setDateFrom (Timestamp DateFrom)
    {
        set_Value ("DateFrom", DateFrom);
        
    }
    
    /** Get Date From.
    @return Starting date for a range */
    public Timestamp getDateFrom() 
    {
        return (Timestamp)get_Value("DateFrom");
        
    }
    
    /** Free = f */
    public static final String ELEMENTTYPE_Free = X_Ref_XX_DonationsTypes.FREE.getValue();
    /** Predefined  = p */
    public static final String ELEMENTTYPE_Predefined = X_Ref_XX_DonationsTypes.PREDEFINED.getValue();
    /** Set Type.
    @param ElementType Element Type (account or user defined) */
    public void setElementType (String ElementType)
    {
        if (!X_Ref_XX_DonationsTypes.isValid(ElementType))
        throw new IllegalArgumentException ("ElementType Invalid value - " + ElementType + " - Reference_ID=1000308 - f - p");
        set_Value ("ElementType", ElementType);
        
    }
    
    /** Get Type.
    @return Element Type (account or user defined) */
    public String getElementType() 
    {
        return (String)get_Value("ElementType");
        
    }
    
    /** Set Nombre.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Nombre.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set TOTAL.
    @param TOTAL TOTAL */
    public void setTOTAL (boolean TOTAL)
    {
        set_Value ("TOTAL", Boolean.valueOf(TOTAL));
        
    }
    
    /** Get TOTAL.
    @return TOTAL */
    public boolean isTOTAL() 
    {
        return get_ValueAsBoolean("TOTAL");
        
    }
    
    /** Set Approved Marketing.
    @param XX_ApproveMar Approved Marketing */
    public void setXX_ApproveMar (boolean XX_ApproveMar)
    {
        set_Value ("XX_ApproveMar", Boolean.valueOf(XX_ApproveMar));
        
    }
    
    /** Get Approved Marketing.
    @return Approved Marketing */
    public boolean isXX_ApproveMar() 
    {
        return get_ValueAsBoolean("XX_ApproveMar");
        
    }
    
    /** Set Synchronized.
    @param XX_Synchronized Indica si el registro ya fue exportado */
    public void setXX_Synchronized (boolean XX_Synchronized)
    {
        set_Value ("XX_Synchronized", Boolean.valueOf(XX_Synchronized));
        
    }
    
    /** Get Synchronized.
    @return Indica si el registro ya fue exportado */
    public boolean isXX_Synchronized() 
    {
        return get_ValueAsBoolean("XX_Synchronized");
        
    }
    
    /** Set XX_VME_Donations_ID.
    @param XX_VME_Donations_ID XX_VME_Donations_ID */
    public void setXX_VME_Donations_ID (int XX_VME_Donations_ID)
    {
        if (XX_VME_Donations_ID < 1) throw new IllegalArgumentException ("XX_VME_Donations_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_Donations_ID", Integer.valueOf(XX_VME_Donations_ID));
        
    }
    
    /** Get XX_VME_Donations_ID.
    @return XX_VME_Donations_ID */
    public int getXX_VME_Donations_ID() 
    {
        return get_ValueAsInt("XX_VME_Donations_ID");
        
    }
    
    
}
