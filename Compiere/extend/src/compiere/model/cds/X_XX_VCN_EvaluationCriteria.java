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
/** Generated Model for XX_VCN_EvaluationCriteria
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_EvaluationCriteria extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_EvaluationCriteria_ID id
    @param trx transaction
    */
    public X_XX_VCN_EvaluationCriteria (Ctx ctx, int XX_VCN_EvaluationCriteria_ID, Trx trx)
    {
        super (ctx, XX_VCN_EvaluationCriteria_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_EvaluationCriteria_ID == 0)
        {
            setName (null);
            setXX_LEVELIMPORTANCE (Env.ZERO);
            setXX_POINT (Env.ZERO);
            setXX_VCN_EvaluationCriteria_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_EvaluationCriteria (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27615607665789L;
    /** Last Updated Timestamp 2012-04-03 15:15:49.0 */
    public static final long updatedMS = 1333482349000L;
    /** AD_Table_ID=1000126 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_EvaluationCriteria");
        
    }
    ;
    
    /** TableName=XX_VCN_EvaluationCriteria */
    public static final String Table_Name="XX_VCN_EvaluationCriteria";
    
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
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
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
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Compares the Value.
    @param XX_CompareValue Compara el Valor con */
    public void setXX_CompareValue (String XX_CompareValue)
    {
        set_Value ("XX_CompareValue", XX_CompareValue);
        
    }
    
    /** Get Compares the Value.
    @return Compara el Valor con */
    public String getXX_CompareValue() 
    {
        return (String)get_Value("XX_CompareValue");
        
    }
    
    /** Set Item Sequence.
    @param XX_ItemSequence Item Sequence */
    public void setXX_ItemSequence (java.math.BigDecimal XX_ItemSequence)
    {
        set_Value ("XX_ItemSequence", XX_ItemSequence);
        
    }
    
    /** Get Item Sequence.
    @return Item Sequence */
    public java.math.BigDecimal getXX_ItemSequence() 
    {
        return get_ValueAsBigDecimal("XX_ItemSequence");
        
    }
    
    /** Set Level Importance.
    @param XX_LEVELIMPORTANCE Nivel de Importancia del Criterio para el calculo del Peso */
    public void setXX_LEVELIMPORTANCE (java.math.BigDecimal XX_LEVELIMPORTANCE)
    {
        if (XX_LEVELIMPORTANCE == null) throw new IllegalArgumentException ("XX_LEVELIMPORTANCE is mandatory.");
        set_Value ("XX_LEVELIMPORTANCE", XX_LEVELIMPORTANCE);
        
    }
    
    /** Get Level Importance.
    @return Nivel de Importancia del Criterio para el calculo del Peso */
    public java.math.BigDecimal getXX_LEVELIMPORTANCE() 
    {
        return get_ValueAsBigDecimal("XX_LEVELIMPORTANCE");
        
    }
    
    /** Set Operation Type.
    @param XX_OperationTypeMatrix Tipo de Operacion que se va a utilizar para el Calculo del Peso */
    public void setXX_OperationTypeMatrix (String XX_OperationTypeMatrix)
    {
        set_Value ("XX_OperationTypeMatrix", XX_OperationTypeMatrix);
        
    }
    
    /** Get Operation Type.
    @return Tipo de Operacion que se va a utilizar para el Calculo del Peso */
    public String getXX_OperationTypeMatrix() 
    {
        return (String)get_Value("XX_OperationTypeMatrix");
        
    }
    
    /** Set Point.
    @param XX_POINT Puntos. Peso Máximo de del Criterio */
    public void setXX_POINT (java.math.BigDecimal XX_POINT)
    {
        if (XX_POINT == null) throw new IllegalArgumentException ("XX_POINT is mandatory.");
        set_Value ("XX_POINT", XX_POINT);
        
    }
    
    /** Get Point.
    @return Puntos. Peso Máximo de del Criterio */
    public java.math.BigDecimal getXX_POINT() 
    {
        return get_ValueAsBigDecimal("XX_POINT");
        
    }
    
    /** Set Takes the Value.
    @param XX_TakeValue Toma el Valor de que Tabla.Campo */
    public void setXX_TakeValue (String XX_TakeValue)
    {
        set_Value ("XX_TakeValue", XX_TakeValue);
        
    }
    
    /** Get Takes the Value.
    @return Toma el Valor de que Tabla.Campo */
    public String getXX_TakeValue() 
    {
        return (String)get_Value("XX_TakeValue");
        
    }
    
    /** Set Evaluation Criteria ID.
    @param XX_VCN_EvaluationCriteria_ID ID de la Tabla Criterio de Evaluación */
    public void setXX_VCN_EvaluationCriteria_ID (int XX_VCN_EvaluationCriteria_ID)
    {
        if (XX_VCN_EvaluationCriteria_ID < 1) throw new IllegalArgumentException ("XX_VCN_EvaluationCriteria_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_EvaluationCriteria_ID", Integer.valueOf(XX_VCN_EvaluationCriteria_ID));
        
    }
    
    /** Get Evaluation Criteria ID.
    @return ID de la Tabla Criterio de Evaluación */
    public int getXX_VCN_EvaluationCriteria_ID() 
    {
        return get_ValueAsInt("XX_VCN_EvaluationCriteria_ID");
        
    }
    
    
}
