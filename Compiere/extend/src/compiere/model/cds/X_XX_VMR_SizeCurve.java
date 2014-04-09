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
/** Generated Model for XX_VMR_SizeCurve
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_SizeCurve extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_SizeCurve_ID id
    @param trx transaction
    */
    public X_XX_VMR_SizeCurve (Ctx ctx, int XX_VMR_SizeCurve_ID, Trx trx)
    {
        super (ctx, XX_VMR_SizeCurve_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_SizeCurve_ID == 0)
        {
            setXX_ReferencesDescription (null);
            setXX_SizesDescription (null);
            setXX_VMR_DistributionHeader_ID (0);
            setXX_VMR_SIZECURVE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_SizeCurve (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27578030112789L;
    /** Last Updated Timestamp 2011-01-24 17:03:16.0 */
    public static final long updatedMS = 1295904796000L;
    /** AD_Table_ID=1000182 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_SizeCurve");
        
    }
    ;
    
    /** TableName=XX_VMR_SizeCurve */
    public static final String Table_Name="XX_VMR_SizeCurve";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set CalculateByReferences.
    @param XX_CalculateByReferences CalculateByReferences */
    public void setXX_CalculateByReferences (String XX_CalculateByReferences)
    {
        set_Value ("XX_CalculateByReferences", XX_CalculateByReferences);
        
    }
    
    /** Get CalculateByReferences.
    @return CalculateByReferences */
    public String getXX_CalculateByReferences() 
    {
        return (String)get_Value("XX_CalculateByReferences");
        
    }
    
    /** Set Calculated Size Curve by References.
    @param XX_CalculatedSizeCurveRef Calculated Size Curve by References */
    public void setXX_CalculatedSizeCurveRef (boolean XX_CalculatedSizeCurveRef)
    {
        set_Value ("XX_CalculatedSizeCurveRef", Boolean.valueOf(XX_CalculatedSizeCurveRef));
        
    }
    
    /** Get Calculated Size Curve by References.
    @return Calculated Size Curve by References */
    public boolean isXX_CalculatedSizeCurveRef() 
    {
        return get_ValueAsBoolean("XX_CalculatedSizeCurveRef");
        
    }
    
    /** Set References Description.
    @param XX_ReferencesDescription References Description */
    public void setXX_ReferencesDescription (String XX_ReferencesDescription)
    {
        if (XX_ReferencesDescription == null) throw new IllegalArgumentException ("XX_ReferencesDescription is mandatory.");
        set_Value ("XX_ReferencesDescription", XX_ReferencesDescription);
        
    }
    
    /** Get References Description.
    @return References Description */
    public String getXX_ReferencesDescription() 
    {
        return (String)get_Value("XX_ReferencesDescription");
        
    }
    
    /** Set Sizes Description.
    @param XX_SizesDescription Sizes Description */
    public void setXX_SizesDescription (String XX_SizesDescription)
    {
        if (XX_SizesDescription == null) throw new IllegalArgumentException ("XX_SizesDescription is mandatory.");
        set_Value ("XX_SizesDescription", XX_SizesDescription);
        
    }
    
    /** Get Sizes Description.
    @return Sizes Description */
    public String getXX_SizesDescription() 
    {
        return (String)get_Value("XX_SizesDescription");
        
    }
    
    /** Set DistributionHeader.
    @param XX_VMR_DistributionHeader_ID DistributionHeader */
    public void setXX_VMR_DistributionHeader_ID (int XX_VMR_DistributionHeader_ID)
    {
        if (XX_VMR_DistributionHeader_ID < 1) throw new IllegalArgumentException ("XX_VMR_DistributionHeader_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DistributionHeader_ID", Integer.valueOf(XX_VMR_DistributionHeader_ID));
        
    }
    
    /** Get DistributionHeader.
    @return DistributionHeader */
    public int getXX_VMR_DistributionHeader_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionHeader_ID");
        
    }
    
    /** Set XX_VMR_SIZECURVE_ID.
    @param XX_VMR_SIZECURVE_ID XX_VMR_SIZECURVE_ID */
    public void setXX_VMR_SIZECURVE_ID (int XX_VMR_SIZECURVE_ID)
    {
        if (XX_VMR_SIZECURVE_ID < 1) throw new IllegalArgumentException ("XX_VMR_SIZECURVE_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_SIZECURVE_ID", Integer.valueOf(XX_VMR_SIZECURVE_ID));
        
    }
    
    /** Get XX_VMR_SIZECURVE_ID.
    @return XX_VMR_SIZECURVE_ID */
    public int getXX_VMR_SIZECURVE_ID() 
    {
        return get_ValueAsInt("XX_VMR_SIZECURVE_ID");
        
    }
    
    
}
