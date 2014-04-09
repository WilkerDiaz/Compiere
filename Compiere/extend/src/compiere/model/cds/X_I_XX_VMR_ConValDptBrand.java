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
/** Generated Model for I_XX_VMR_ConValDptBrand
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VMR_ConValDptBrand extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_ConValDptBrand_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_ConValDptBrand (Ctx ctx, int I_XX_VMR_ConValDptBrand_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_ConValDptBrand_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_ConValDptBrand_ID == 0)
        {
            setI_XX_VMR_CONVALDPTBRAND_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_ConValDptBrand (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567884204789L;
    /** Last Updated Timestamp 2010-09-29 06:44:48.0 */
    public static final long updatedMS = 1285758888000L;
    /** AD_Table_ID=1000377 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_ConValDptBrand");
        
    }
    ;
    
    /** TableName=I_XX_VMR_ConValDptBrand */
    public static final String Table_Name="I_XX_VMR_ConValDptBrand";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (boolean I_IsImported)
    {
        set_Value ("I_IsImported", Boolean.valueOf(I_IsImported));
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public boolean isI_IsImported() 
    {
        return get_ValueAsBoolean("I_IsImported");
        
    }
    
    /** Set I_XX_VMR_CONVALDPTBRAND_ID.
    @param I_XX_VMR_CONVALDPTBRAND_ID I_XX_VMR_CONVALDPTBRAND_ID */
    public void setI_XX_VMR_CONVALDPTBRAND_ID (int I_XX_VMR_CONVALDPTBRAND_ID)
    {
        if (I_XX_VMR_CONVALDPTBRAND_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_CONVALDPTBRAND_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_CONVALDPTBRAND_ID", Integer.valueOf(I_XX_VMR_CONVALDPTBRAND_ID));
        
    }
    
    /** Get I_XX_VMR_CONVALDPTBRAND_ID.
    @return I_XX_VMR_CONVALDPTBRAND_ID */
    public int getI_XX_VMR_CONVALDPTBRAND_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_CONVALDPTBRAND_ID");
        
    }
    
    /** Set MARCAPRODUCTO.
    @param MARCAPRODUCTO MARCAPRODUCTO */
    public void setMARCAPRODUCTO (String MARCAPRODUCTO)
    {
        set_Value ("MARCAPRODUCTO", MARCAPRODUCTO);
        
    }
    
    /** Get MARCAPRODUCTO.
    @return MARCAPRODUCTO */
    public String getMARCAPRODUCTO() 
    {
        return (String)get_Value("MARCAPRODUCTO");
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_Value ("Processed", Boolean.valueOf(Processed));
        
    }
    
    /** Get Processed.
    @return The document has been processed */
    public boolean isProcessed() 
    {
        return get_ValueAsBoolean("Processed");
        
    }
    
    /** Set Process Now.
    @param Processing Process Now */
    public void setProcessing (boolean Processing)
    {
        set_Value ("Processing", Boolean.valueOf(Processing));
        
    }
    
    /** Get Process Now.
    @return Process Now */
    public boolean isProcessing() 
    {
        return get_ValueAsBoolean("Processing");
        
    }
    
    /** Set Department Code	.
    @param XX_CodDep Codigo del Departameto */
    public void setXX_CodDep (String XX_CodDep)
    {
        set_Value ("XX_CodDep", XX_CodDep);
        
    }
    
    /** Get Department Code	.
    @return Codigo del Departameto */
    public String getXX_CodDep() 
    {
        return (String)get_Value("XX_CodDep");
        
    }
    
    /** Set XX_ValueConcept.
    @param XX_ValueConcept XX_ValueConcept */
    public void setXX_ValueConcept (String XX_ValueConcept)
    {
        set_Value ("XX_ValueConcept", XX_ValueConcept);
        
    }
    
    /** Get XX_ValueConcept.
    @return XX_ValueConcept */
    public String getXX_ValueConcept() 
    {
        return (String)get_Value("XX_ValueConcept");
        
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
    
    /** Set Concept Value Dpt Brand.
    @param XX_VMR_ConceptValDptBrand_ID Concept Value Dpt Brand */
    public void setXX_VMR_ConceptValDptBrand_ID (int XX_VMR_ConceptValDptBrand_ID)
    {
        if (XX_VMR_ConceptValDptBrand_ID <= 0) set_Value ("XX_VMR_ConceptValDptBrand_ID", null);
        else
        set_Value ("XX_VMR_ConceptValDptBrand_ID", Integer.valueOf(XX_VMR_ConceptValDptBrand_ID));
        
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
    
    
}
