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
/** Generated Model for XX_VMR_WeightAssigned
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_WeightAssigned extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_WeightAssigned_ID id
    @param trx transaction
    */
    public X_XX_VMR_WeightAssigned (Ctx ctx, int XX_VMR_WeightAssigned_ID, Trx trx)
    {
        super (ctx, XX_VMR_WeightAssigned_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_WeightAssigned_ID == 0)
        {
            setValue (null);
            setXX_VMR_WeightAssigned_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_WeightAssigned (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27564714402789L;
    /** Last Updated Timestamp 2010-08-23 14:14:46.0 */
    public static final long updatedMS = 1282589086000L;
    /** AD_Table_ID=1000310 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_WeightAssigned");
        
    }
    ;
    
    /** TableName=XX_VMR_WeightAssigned */
    public static final String Table_Name="XX_VMR_WeightAssigned";
    
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
    
    /** Set Weight.
    @param Weight Weight of a product */
    public void setWeight (java.math.BigDecimal Weight)
    {
        set_Value ("Weight", Weight);
        
    }
    
    /** Get Weight.
    @return Weight of a product */
    public java.math.BigDecimal getWeight() 
    {
        return get_ValueAsBigDecimal("Weight");
        
    }
    
    /** Enero = 01 */
    public static final String XX_MONTH_Enero = X_Ref_XX_Months.ENERO.getValue();
    /** Febrero = 02 */
    public static final String XX_MONTH_Febrero = X_Ref_XX_Months.FEBRERO.getValue();
    /** Marzo = 03 */
    public static final String XX_MONTH_Marzo = X_Ref_XX_Months.MARZO.getValue();
    /** Abril = 04 */
    public static final String XX_MONTH_Abril = X_Ref_XX_Months.ABRIL.getValue();
    /** Mayo = 05 */
    public static final String XX_MONTH_Mayo = X_Ref_XX_Months.MAYO.getValue();
    /** Junio = 06 */
    public static final String XX_MONTH_Junio = X_Ref_XX_Months.JUNIO.getValue();
    /** Julio = 07 */
    public static final String XX_MONTH_Julio = X_Ref_XX_Months.JULIO.getValue();
    /** Agosto = 08 */
    public static final String XX_MONTH_Agosto = X_Ref_XX_Months.AGOSTO.getValue();
    /** Septiembre = 09 */
    public static final String XX_MONTH_Septiembre = X_Ref_XX_Months.SEPTIEMBRE.getValue();
    /** Octubre = 10 */
    public static final String XX_MONTH_Octubre = X_Ref_XX_Months.OCTUBRE.getValue();
    /** Noviembre = 11 */
    public static final String XX_MONTH_Noviembre = X_Ref_XX_Months.NOVIEMBRE.getValue();
    /** Diciembre = 12 */
    public static final String XX_MONTH_Diciembre = X_Ref_XX_Months.DICIEMBRE.getValue();
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (String XX_Month)
    {
        if (!X_Ref_XX_Months.isValid(XX_Month))
        throw new IllegalArgumentException ("XX_Month Invalid value - " + XX_Month + " - Reference_ID=1000289 - 01 - 02 - 03 - 04 - 05 - 06 - 07 - 08 - 09 - 10 - 11 - 12");
        set_Value ("XX_Month", XX_Month);
        
    }
    
    /** Get Month.
    @return Month */
    public String getXX_Month() 
    {
        return (String)get_Value("XX_Month");
        
    }
    
    /** Set Store.
    @param XX_Store_ID Codigo de la Tienda */
    public void setXX_Store_ID (int XX_Store_ID)
    {
        if (XX_Store_ID <= 0) set_Value ("XX_Store_ID", null);
        else
        set_Value ("XX_Store_ID", Integer.valueOf(XX_Store_ID));
        
    }
    
    /** Get Store.
    @return Codigo de la Tienda */
    public int getXX_Store_ID() 
    {
        return get_ValueAsInt("XX_Store_ID");
        
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
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
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
        if (XX_VMR_Line_ID <= 0) set_Value ("XX_VMR_Line_ID", null);
        else
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    /** Set Weight By Department - Brand.
    @param XX_VMR_WByDepBrandView Weight By Department - Brand */
    public void setXX_VMR_WByDepBrandView (String XX_VMR_WByDepBrandView)
    {
        set_Value ("XX_VMR_WByDepBrandView", XX_VMR_WByDepBrandView);
        
    }
    
    /** Get Weight By Department - Brand.
    @return Weight By Department - Brand */
    public String getXX_VMR_WByDepBrandView() 
    {
        return (String)get_Value("XX_VMR_WByDepBrandView");
        
    }
    
    /** Set Weight Assigned.
    @param XX_VMR_WeightAssigned_ID Asignación del peso al Presupuesto Comercial */
    public void setXX_VMR_WeightAssigned_ID (int XX_VMR_WeightAssigned_ID)
    {
        if (XX_VMR_WeightAssigned_ID < 1) throw new IllegalArgumentException ("XX_VMR_WeightAssigned_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_WeightAssigned_ID", Integer.valueOf(XX_VMR_WeightAssigned_ID));
        
    }
    
    /** Get Weight Assigned.
    @return Asignación del peso al Presupuesto Comercial */
    public int getXX_VMR_WeightAssigned_ID() 
    {
        return get_ValueAsInt("XX_VMR_WeightAssigned_ID");
        
    }
    
    /** Set Weight By Line - Value Concept.
    @param XX_WeightByLineValConView Weight By Line - Value Concept */
    public void setXX_WeightByLineValConView (String XX_WeightByLineValConView)
    {
        set_Value ("XX_WeightByLineValConView", XX_WeightByLineValConView);
        
    }
    
    /** Get Weight By Line - Value Concept.
    @return Weight By Line - Value Concept */
    public String getXX_WeightByLineValConView() 
    {
        return (String)get_Value("XX_WeightByLineValConView");
        
    }
    
    /** Set Weight By Line.
    @param XX_WeightByLineView Weight By Line */
    public void setXX_WeightByLineView (String XX_WeightByLineView)
    {
        set_Value ("XX_WeightByLineView", XX_WeightByLineView);
        
    }
    
    /** Get Weight By Line.
    @return Weight By Line */
    public String getXX_WeightByLineView() 
    {
        return (String)get_Value("XX_WeightByLineView");
        
    }
    
    /** Set Weight By Section.
    @param XX_WeightBySectionView Weight By Section */
    public void setXX_WeightBySectionView (String XX_WeightBySectionView)
    {
        set_Value ("XX_WeightBySectionView", XX_WeightBySectionView);
        
    }
    
    /** Get Weight By Section.
    @return Weight By Section */
    public String getXX_WeightBySectionView() 
    {
        return (String)get_Value("XX_WeightBySectionView");
        
    }
    
    /** Set Weight By Store Month.
    @param XX_WeightByStoreMonthView Weight By Store Month */
    public void setXX_WeightByStoreMonthView (String XX_WeightByStoreMonthView)
    {
        set_Value ("XX_WeightByStoreMonthView", XX_WeightByStoreMonthView);
        
    }
    
    /** Get Weight By Store Month.
    @return Weight By Store Month */
    public String getXX_WeightByStoreMonthView() 
    {
        return (String)get_Value("XX_WeightByStoreMonthView");
        
    }
    
    /** Set Weight Type.
    @param XX_WeightType Weight Type */
    public void setXX_WeightType (int XX_WeightType)
    {
        set_Value ("XX_WeightType", Integer.valueOf(XX_WeightType));
        
    }
    
    /** Get Weight Type.
    @return Weight Type */
    public int getXX_WeightType() 
    {
        return get_ValueAsInt("XX_WeightType");
        
    }
    
    
}
