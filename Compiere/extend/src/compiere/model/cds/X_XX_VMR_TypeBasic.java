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
/** Generated Model for XX_VMR_TypeBasic
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_TypeBasic extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_TypeBasic_ID id
    @param trx transaction
    */
    public X_XX_VMR_TypeBasic (Ctx ctx, int XX_VMR_TypeBasic_ID, Trx trx)
    {
        super (ctx, XX_VMR_TypeBasic_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_TypeBasic_ID == 0)
        {
            setXX_VMR_TypeBasic_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_TypeBasic (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27610158689789L;
    /** Last Updated Timestamp 2012-01-31 13:39:33.0 */
    public static final long updatedMS = 1328033373000L;
    /** AD_Table_ID=1001153 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_TypeBasic");
        
    }
    ;
    
    /** TableName=XX_VMR_TypeBasic */
    public static final String Table_Name="XX_VMR_TypeBasic";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Description Type Basic.
    @param DescriptionTypeBasic Description Type Basic */
    public void setDescriptionTypeBasic (String DescriptionTypeBasic)
    {
        set_Value ("DescriptionTypeBasic", DescriptionTypeBasic);
        
    }
    
    /** Get Description Type Basic.
    @return Description Type Basic */
    public String getDescriptionTypeBasic() 
    {
        return (String)get_Value("DescriptionTypeBasic");
        
    }
    
    /** Set Goal.
    @param Goal Goal */
    public void setGoal (int Goal)
    {
        set_Value ("Goal", Integer.valueOf(Goal));
        
    }
    
    /** Get Goal.
    @return Goal */
    public int getGoal() 
    {
        return get_ValueAsInt("Goal");
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set XX_VMR_ClassificationLevel.
    @param XX_VMR_ClassificationLevel XX_VMR_ClassificationLevel */
    public void setXX_VMR_ClassificationLevel (String XX_VMR_ClassificationLevel)
    {
        set_Value ("XX_VMR_ClassificationLevel", XX_VMR_ClassificationLevel);
        
    }
    
    /** Get XX_VMR_ClassificationLevel.
    @return XX_VMR_ClassificationLevel */
    public String getXX_VMR_ClassificationLevel() 
    {
        return (String)get_Value("XX_VMR_ClassificationLevel");
        
    }
    
    /** Set XX_VMR_TypeBasic_ID.
    @param XX_VMR_TypeBasic_ID XX_VMR_TypeBasic_ID */
    public void setXX_VMR_TypeBasic_ID (int XX_VMR_TypeBasic_ID)
    {
        if (XX_VMR_TypeBasic_ID < 1) throw new IllegalArgumentException ("XX_VMR_TypeBasic_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_TypeBasic_ID", Integer.valueOf(XX_VMR_TypeBasic_ID));
        
    }
    
    /** Get XX_VMR_TypeBasic_ID.
    @return XX_VMR_TypeBasic_ID */
    public int getXX_VMR_TypeBasic_ID() 
    {
        return get_ValueAsInt("XX_VMR_TypeBasic_ID");
        
    }
    
    
}
