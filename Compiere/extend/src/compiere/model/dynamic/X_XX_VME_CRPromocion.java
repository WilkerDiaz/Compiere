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
package compiere.model.dynamic;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VME_CRPromocion
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_CRPromocion extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_CRPromocion_ID id
    @param trx transaction
    */
    public X_XX_VME_CRPromocion (Ctx ctx, int XX_VME_CRPromocion_ID, Trx trx)
    {
        super (ctx, XX_VME_CRPromocion_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_CRPromocion_ID == 0)
        {
            setValue (null);
            setXX_VME_CRPromocion_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_CRPromocion (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27536277627789L;
    /** Last Updated Timestamp 2009-09-28 11:08:31.0 */
    public static final long updatedMS = 1254152311000L;
    /** AD_Table_ID=1000429 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_CRPromocion");
        
    }
    ;
    
    /** TableName=XX_VME_CRPromocion */
    public static final String Table_Name="XX_VME_CRPromocion";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set End Date.
    @param EndDate Last effective date (inclusive) */
    public void setEndDate (Timestamp EndDate)
    {
        set_Value ("EndDate", EndDate);
        
    }
    
    /** Get End Date.
    @return Last effective date (inclusive) */
    public Timestamp getEndDate() 
    {
        return (Timestamp)get_Value("EndDate");
        
    }
    
    /** Set Start Date.
    @param StartDate First effective day (inclusive) */
    public void setStartDate (Timestamp StartDate)
    {
        set_Value ("StartDate", StartDate);
        
    }
    
    /** Get Start Date.
    @return First effective day (inclusive) */
    public Timestamp getStartDate() 
    {
        return (Timestamp)get_Value("StartDate");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Promotion.
    @param XX_VME_CRPromocion_ID Promotion identifier. */
    public void setXX_VME_CRPromocion_ID (int XX_VME_CRPromocion_ID)
    {
        if (XX_VME_CRPromocion_ID < 1) throw new IllegalArgumentException ("XX_VME_CRPromocion_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_CRPromocion_ID", Integer.valueOf(XX_VME_CRPromocion_ID));
        
    }
    
    /** Get Promotion.
    @return Promotion identifier. */
    public int getXX_VME_CRPromocion_ID() 
    {
        return get_ValueAsInt("XX_VME_CRPromocion_ID");
        
    }
    
    /** Set End Time.
    @param XX_VME_HoraFinaliza Time when the promotion ends. */
    public void setXX_VME_HoraFinaliza (Timestamp XX_VME_HoraFinaliza)
    {
        set_ValueNoCheck ("XX_VME_HoraFinaliza", XX_VME_HoraFinaliza);
        
    }
    
    /** Get End Time.
    @return Time when the promotion ends. */
    public Timestamp getXX_VME_HoraFinaliza() 
    {
        return (Timestamp)get_Value("XX_VME_HoraFinaliza");
        
    }
    
    /** Set Start Time.
    @param XX_VME_HoraInicio Time when the promotions starts. */
    public void setXX_VME_HoraInicio (Timestamp XX_VME_HoraInicio)
    {
        set_ValueNoCheck ("XX_VME_HoraInicio", XX_VME_HoraInicio);
        
    }
    
    /** Get Start Time.
    @return Time when the promotions starts. */
    public Timestamp getXX_VME_HoraInicio() 
    {
        return (Timestamp)get_Value("XX_VME_HoraInicio");
        
    }
    
    /** Set Priority.
    @param XX_VME_Prioridad Priority of the promotion. */
    public void setXX_VME_Prioridad (String XX_VME_Prioridad)
    {
        set_Value ("XX_VME_Prioridad", XX_VME_Prioridad);
        
    }
    
    /** Get Priority.
    @return Priority of the promotion. */
    public String getXX_VME_Prioridad() 
    {
        return (String)get_Value("XX_VME_Prioridad");
        
    }
    
    /** Set Type of Promotion.
    @param XX_VME_TipoPromocion Is the Type of a Promotion */
    public void setXX_VME_TipoPromocion (String XX_VME_TipoPromocion)
    {
        set_Value ("XX_VME_TipoPromocion", XX_VME_TipoPromocion);
        
    }
    
    /** Get Type of Promotion.
    @return Is the Type of a Promotion */
    public String getXX_VME_TipoPromocion() 
    {
        return (String)get_Value("XX_VME_TipoPromocion");
        
    }
    
    
}
