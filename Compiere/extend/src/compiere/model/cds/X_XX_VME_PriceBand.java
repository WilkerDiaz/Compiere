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
/** Generated Model for XX_VME_PriceBand
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VME_PriceBand extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VME_PriceBand_ID id
    @param trx transaction
    */
    public X_XX_VME_PriceBand (Ctx ctx, int XX_VME_PriceBand_ID, Trx trx)
    {
        super (ctx, XX_VME_PriceBand_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VME_PriceBand_ID == 0)
        {
            setValue (null);
            setXX_COMPARISONVALUE_ID (0);
            setXX_ConceptValue_ID (0);
            setXX_HIGHRANK (Env.ZERO);
            setXX_LOWRANK (Env.ZERO);
            setXX_OPERATING (null);
            setXX_PERCENTAGEVALUE (Env.ZERO);
            setXX_VME_PRICEBAND_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VME_PriceBand (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27566276481789L;
    /** Last Updated Timestamp 2010-09-10 16:09:25.0 */
    public static final long updatedMS = 1284151165000L;
    /** AD_Table_ID=1000085 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VME_PriceBand");
        
    }
    ;
    
    /** TableName=XX_VME_PriceBand */
    public static final String Table_Name="XX_VME_PriceBand";
    
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
    
    /** Set Comparison Value.
    @param XX_COMPARISONVALUE_ID Valor de Comparacion */
    public void setXX_COMPARISONVALUE_ID (int XX_COMPARISONVALUE_ID)
    {
        if (XX_COMPARISONVALUE_ID < 1) throw new IllegalArgumentException ("XX_COMPARISONVALUE_ID is mandatory.");
        set_Value ("XX_COMPARISONVALUE_ID", Integer.valueOf(XX_COMPARISONVALUE_ID));
        
    }
    
    /** Get Comparison Value.
    @return Valor de Comparacion */
    public int getXX_COMPARISONVALUE_ID() 
    {
        return get_ValueAsInt("XX_COMPARISONVALUE_ID");
        
    }
    
    /** Set Concept Value.
    @param XX_ConceptValue_ID Valor de Concepto */
    public void setXX_ConceptValue_ID (int XX_ConceptValue_ID)
    {
        if (XX_ConceptValue_ID < 1) throw new IllegalArgumentException ("XX_ConceptValue_ID is mandatory.");
        set_Value ("XX_ConceptValue_ID", Integer.valueOf(XX_ConceptValue_ID));
        
    }
    
    /** Get Concept Value.
    @return Valor de Concepto */
    public int getXX_ConceptValue_ID() 
    {
        return get_ValueAsInt("XX_ConceptValue_ID");
        
    }
    
    /** Set High Rank.
    @param XX_HIGHRANK Rango de Precio Superior */
    public void setXX_HIGHRANK (java.math.BigDecimal XX_HIGHRANK)
    {
        if (XX_HIGHRANK == null) throw new IllegalArgumentException ("XX_HIGHRANK is mandatory.");
        set_Value ("XX_HIGHRANK", XX_HIGHRANK);
        
    }
    
    /** Get High Rank.
    @return Rango de Precio Superior */
    public java.math.BigDecimal getXX_HIGHRANK() 
    {
        return get_ValueAsBigDecimal("XX_HIGHRANK");
        
    }
    
    /** Set Low Rank.
    @param XX_LOWRANK Rango de Precio Inferior */
    public void setXX_LOWRANK (java.math.BigDecimal XX_LOWRANK)
    {
        if (XX_LOWRANK == null) throw new IllegalArgumentException ("XX_LOWRANK is mandatory.");
        set_Value ("XX_LOWRANK", XX_LOWRANK);
        
    }
    
    /** Get Low Rank.
    @return Rango de Precio Inferior */
    public java.math.BigDecimal getXX_LOWRANK() 
    {
        return get_ValueAsBigDecimal("XX_LOWRANK");
        
    }
    
    /** < (Menor que) = Minor */
    public static final String XX_OPERATING_LeMenorQue = X_Ref_Operators.LE_MENOR_QUE.getValue();
    /** >(Mayor que) = higher */
    public static final String XX_OPERATING_GtMayorQue = X_Ref_Operators.GT_MAYOR_QUE.getValue();
    /** Set Operating.
    @param XX_OPERATING Operando */
    public void setXX_OPERATING (String XX_OPERATING)
    {
        if (XX_OPERATING == null) throw new IllegalArgumentException ("XX_OPERATING is mandatory");
        if (!X_Ref_Operators.isValid(XX_OPERATING))
        throw new IllegalArgumentException ("XX_OPERATING Invalid value - " + XX_OPERATING + " - Reference_ID=1000085 - Minor - higher");
        set_Value ("XX_OPERATING", XX_OPERATING);
        
    }
    
    /** Get Operating.
    @return Operando */
    public String getXX_OPERATING() 
    {
        return (String)get_Value("XX_OPERATING");
        
    }
    
    /** Set Percentage Value.
    @param XX_PERCENTAGEVALUE Valor Porcentual */
    public void setXX_PERCENTAGEVALUE (java.math.BigDecimal XX_PERCENTAGEVALUE)
    {
        if (XX_PERCENTAGEVALUE == null) throw new IllegalArgumentException ("XX_PERCENTAGEVALUE is mandatory.");
        set_Value ("XX_PERCENTAGEVALUE", XX_PERCENTAGEVALUE);
        
    }
    
    /** Get Percentage Value.
    @return Valor Porcentual */
    public java.math.BigDecimal getXX_PERCENTAGEVALUE() 
    {
        return get_ValueAsBigDecimal("XX_PERCENTAGEVALUE");
        
    }
    
    /** Set XX_VME_PRICEBAND_ID.
    @param XX_VME_PRICEBAND_ID XX_VME_PRICEBAND_ID */
    public void setXX_VME_PRICEBAND_ID (int XX_VME_PRICEBAND_ID)
    {
        if (XX_VME_PRICEBAND_ID < 1) throw new IllegalArgumentException ("XX_VME_PRICEBAND_ID is mandatory.");
        set_ValueNoCheck ("XX_VME_PRICEBAND_ID", Integer.valueOf(XX_VME_PRICEBAND_ID));
        
    }
    
    /** Get XX_VME_PRICEBAND_ID.
    @return XX_VME_PRICEBAND_ID */
    public int getXX_VME_PRICEBAND_ID() 
    {
        return get_ValueAsInt("XX_VME_PRICEBAND_ID");
        
    }
    
    
}
