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
/** Generated Model for XX_VMR_ConceptValDptBrand
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_ConceptValDptBrand extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_ConceptValDptBrand_ID id
    @param trx transaction
    */
    public X_XX_VMR_ConceptValDptBrand (Ctx ctx, int XX_VMR_ConceptValDptBrand_ID, Trx trx)
    {
        super (ctx, XX_VMR_ConceptValDptBrand_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_ConceptValDptBrand_ID == 0)
        {
            setXX_VMR_ConceptValDptBrand_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_ConceptValDptBrand (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567482127789L;
    /** Last Updated Timestamp 2010-09-24 15:03:31.0 */
    public static final long updatedMS = 1285356811000L;
    /** AD_Table_ID=1000124 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_ConceptValDptBrand");
        
    }
    ;
    
    /** TableName=XX_VMR_ConceptValDptBrand */
    public static final String Table_Name="XX_VMR_ConceptValDptBrand";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Concept Value.
    @param XX_VME_ConceptValue_ID Concept Value */
    public void setXX_VME_ConceptValue_ID (int XX_VME_ConceptValue_ID)
    {
        if (XX_VME_ConceptValue_ID <= 0) set_Value ("XX_VME_ConceptValue_ID", null);
        else
        set_Value ("XX_VME_ConceptValue_ID", Integer.valueOf(XX_VME_ConceptValue_ID));
        
    }
    
    /** Get Concept Value.
    @return Concept Value */
    public int getXX_VME_ConceptValue_ID() 
    {
        return get_ValueAsInt("XX_VME_ConceptValue_ID");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_ValueNoCheck ("XX_VMR_Brand_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
    }
    
    /** Set Concept Value Dpt Brand.
    @param XX_VMR_ConceptValDptBrand_ID Concept Value Dpt Brand */
    public void setXX_VMR_ConceptValDptBrand_ID (int XX_VMR_ConceptValDptBrand_ID)
    {
        if (XX_VMR_ConceptValDptBrand_ID < 1) throw new IllegalArgumentException ("XX_VMR_ConceptValDptBrand_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_ConceptValDptBrand_ID", Integer.valueOf(XX_VMR_ConceptValDptBrand_ID));
        
    }
    
    /** Get Concept Value Dpt Brand.
    @return Concept Value Dpt Brand */
    public int getXX_VMR_ConceptValDptBrand_ID() 
    {
        return get_ValueAsInt("XX_VMR_ConceptValDptBrand_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_ValueNoCheck ("XX_VMR_Department_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    
}
