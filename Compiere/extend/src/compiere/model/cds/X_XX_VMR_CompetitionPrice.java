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
/** Generated Model for XX_VMR_CompetitionPrice
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_CompetitionPrice extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_CompetitionPrice_ID id
    @param trx transaction
    */
    public X_XX_VMR_CompetitionPrice (Ctx ctx, int XX_VMR_CompetitionPrice_ID, Trx trx)
    {
        super (ctx, XX_VMR_CompetitionPrice_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_CompetitionPrice_ID == 0)
        {
            setXX_MaxPrice (Env.ZERO);
            setXX_MinPrice (Env.ZERO);
            setXX_VME_ConceptValue_ID (0);
            setXX_VMR_Category_ID (0);
            setXX_VMR_Competition_ID (0);
            setXX_VMR_CompetitionPrice_ID (0);
            setXX_VMR_Department_ID (0);
            setXX_VMR_Line_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_CompetitionPrice (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27656024255789L;
    /** Last Updated Timestamp 2013-07-15 10:05:39.0 */
    public static final long updatedMS = 1373898939000L;
    /** AD_Table_ID=1004254 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_CompetitionPrice");
        
    }
    ;
    
    /** TableName=XX_VMR_CompetitionPrice */
    public static final String Table_Name="XX_VMR_CompetitionPrice";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Maximum Price.
    @param XX_MaxPrice Maximum Price */
    public void setXX_MaxPrice (java.math.BigDecimal XX_MaxPrice)
    {
        if (XX_MaxPrice == null) throw new IllegalArgumentException ("XX_MaxPrice is mandatory.");
        set_Value ("XX_MaxPrice", XX_MaxPrice);
        
    }
    
    /** Get Maximum Price.
    @return Maximum Price */
    public java.math.BigDecimal getXX_MaxPrice() 
    {
        return get_ValueAsBigDecimal("XX_MaxPrice");
        
    }
    
    /** Set Minimum Price.
    @param XX_MinPrice Minimum Price */
    public void setXX_MinPrice (java.math.BigDecimal XX_MinPrice)
    {
        if (XX_MinPrice == null) throw new IllegalArgumentException ("XX_MinPrice is mandatory.");
        set_Value ("XX_MinPrice", XX_MinPrice);
        
    }
    
    /** Get Minimum Price.
    @return Minimum Price */
    public java.math.BigDecimal getXX_MinPrice() 
    {
        return get_ValueAsBigDecimal("XX_MinPrice");
        
    }
    
    /** Set Concept Value.
    @param XX_VME_ConceptValue_ID Concept Value */
    public void setXX_VME_ConceptValue_ID (int XX_VME_ConceptValue_ID)
    {
        if (XX_VME_ConceptValue_ID < 1) throw new IllegalArgumentException ("XX_VME_ConceptValue_ID is mandatory.");
        set_Value ("XX_VME_ConceptValue_ID", Integer.valueOf(XX_VME_ConceptValue_ID));
        
    }
    
    /** Get Concept Value.
    @return Concept Value */
    public int getXX_VME_ConceptValue_ID() 
    {
        return get_ValueAsInt("XX_VME_ConceptValue_ID");
        
    }
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID < 1) throw new IllegalArgumentException ("XX_VMR_Category_ID is mandatory.");
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
    }
    
    /** Get Category.
    @return Category */
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");
        
    }
    
    /** Set XX_VMR_Competition_ID.
    @param XX_VMR_Competition_ID XX_VMR_Competition_ID */
    public void setXX_VMR_Competition_ID (int XX_VMR_Competition_ID)
    {
        if (XX_VMR_Competition_ID < 1) throw new IllegalArgumentException ("XX_VMR_Competition_ID is mandatory.");
        set_Value ("XX_VMR_Competition_ID", Integer.valueOf(XX_VMR_Competition_ID));
        
    }
    
    /** Get XX_VMR_Competition_ID.
    @return XX_VMR_Competition_ID */
    public int getXX_VMR_Competition_ID() 
    {
        return get_ValueAsInt("XX_VMR_Competition_ID");
        
    }
    
    /** Set XX_VMR_CompetitionPrice_ID.
    @param XX_VMR_CompetitionPrice_ID XX_VMR_CompetitionPrice_ID */
    public void setXX_VMR_CompetitionPrice_ID (int XX_VMR_CompetitionPrice_ID)
    {
        if (XX_VMR_CompetitionPrice_ID < 1) throw new IllegalArgumentException ("XX_VMR_CompetitionPrice_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_CompetitionPrice_ID", Integer.valueOf(XX_VMR_CompetitionPrice_ID));
        
    }
    
    /** Get XX_VMR_CompetitionPrice_ID.
    @return XX_VMR_CompetitionPrice_ID */
    public int getXX_VMR_CompetitionPrice_ID() 
    {
        return get_ValueAsInt("XX_VMR_CompetitionPrice_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID < 1) throw new IllegalArgumentException ("XX_VMR_Department_ID is mandatory.");
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID < 1) throw new IllegalArgumentException ("XX_VMR_Line_ID is mandatory.");
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    
}
