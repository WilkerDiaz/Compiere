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
/** Generated Model for XX_VCN_WeightCalculation
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_WeightCalculation extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_WeightCalculation_ID id
    @param trx transaction
    */
    public X_XX_VCN_WeightCalculation (Ctx ctx, int XX_VCN_WeightCalculation_ID, Trx trx)
    {
        super (ctx, XX_VCN_WeightCalculation_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_WeightCalculation_ID == 0)
        {
            setXX_VCN_WeightCalculation_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_WeightCalculation (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27617321100789L;
    /** Last Updated Timestamp 2012-04-23 11:13:04.0 */
    public static final long updatedMS = 1335195784000L;
    /** AD_Table_ID=1000131 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_WeightCalculation");
        
    }
    ;
    
    /** TableName=XX_VCN_WeightCalculation */
    public static final String Table_Name="XX_VCN_WeightCalculation";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set PO Payment Term.
    @param PO_PaymentTerm_ID Payment rules for a purchase order */
    public void setPO_PaymentTerm_ID (int PO_PaymentTerm_ID)
    {
        if (PO_PaymentTerm_ID <= 0) set_Value ("PO_PaymentTerm_ID", null);
        else
        set_Value ("PO_PaymentTerm_ID", Integer.valueOf(PO_PaymentTerm_ID));
        
    }
    
    /** Get PO Payment Term.
    @return Payment rules for a purchase order */
    public int getPO_PaymentTerm_ID() 
    {
        return get_ValueAsInt("PO_PaymentTerm_ID");
        
    }
    
    /** Set Atributte Value.
    @param XX_AtributteValue Valor del Atributo, solo cuando el XX_OperationTypeMatrix es Value */
    public void setXX_AtributteValue (String XX_AtributteValue)
    {
        set_Value ("XX_AtributteValue", XX_AtributteValue);
        
    }
    
    /** Get Atributte Value.
    @return Valor del Atributo, solo cuando el XX_OperationTypeMatrix es Value */
    public String getXX_AtributteValue() 
    {
        return (String)get_Value("XX_AtributteValue");
        
    }
    
    /** Set Evaluation Criteria.
    @param XX_EvaluationCriteria_ID Criterios de Evaluación */
    public void setXX_EvaluationCriteria_ID (int XX_EvaluationCriteria_ID)
    {
        if (XX_EvaluationCriteria_ID <= 0) set_Value ("XX_EvaluationCriteria_ID", null);
        else
        set_Value ("XX_EvaluationCriteria_ID", Integer.valueOf(XX_EvaluationCriteria_ID));
        
    }
    
    /** Get Evaluation Criteria.
    @return Criterios de Evaluación */
    public int getXX_EvaluationCriteria_ID() 
    {
        return get_ValueAsInt("XX_EvaluationCriteria_ID");
        
    }
    
    /** Set Formula.
    @param XX_Formula Formula */
    public void setXX_Formula (String XX_Formula)
    {
        set_Value ("XX_Formula", XX_Formula);
        
    }
    
    /** Get Formula.
    @return Formula */
    public String getXX_Formula() 
    {
        return (String)get_Value("XX_Formula");
        
    }
    
    /** Set Max Value Range.
    @param XX_MaxValueRange Valor del Rango, solo cuando el XX_OperationTypeMatrix es Range */
    public void setXX_MaxValueRange (String XX_MaxValueRange)
    {
        set_Value ("XX_MaxValueRange", XX_MaxValueRange);
        
    }
    
    /** Get Max Value Range.
    @return Valor del Rango, solo cuando el XX_OperationTypeMatrix es Range */
    public String getXX_MaxValueRange() 
    {
        return (String)get_Value("XX_MaxValueRange");
        
    }
    
    /** Set Min Value Range.
    @param XX_MinValueRange Valor del Rango, solo cuando el XX_OperationTypeMatrix es Range */
    public void setXX_MinValueRange (String XX_MinValueRange)
    {
        set_Value ("XX_MinValueRange", XX_MinValueRange);
        
    }
    
    /** Get Min Value Range.
    @return Valor del Rango, solo cuando el XX_OperationTypeMatrix es Range */
    public String getXX_MinValueRange() 
    {
        return (String)get_Value("XX_MinValueRange");
        
    }
    
    /** Add = ADD */
    public static final String XX_OPERATIONTYPEMATRIX_Add = X_Ref_XX_Ref_OperationTypeMatrix.ADD.getValue();
    /** Formula = FOR */
    public static final String XX_OPERATIONTYPEMATRIX_Formula = X_Ref_XX_Ref_OperationTypeMatrix.FORMULA.getValue();
    /** Range = RAN */
    public static final String XX_OPERATIONTYPEMATRIX_Range = X_Ref_XX_Ref_OperationTypeMatrix.RANGE.getValue();
    /** Value = VAL */
    public static final String XX_OPERATIONTYPEMATRIX_Value = X_Ref_XX_Ref_OperationTypeMatrix.VALUE.getValue();
    /** Set Operation Type.
    @param XX_OperationTypeMatrix Tipo de Operacion que se va a utilizar para el Calculo del Peso */
    public void setXX_OperationTypeMatrix (String XX_OperationTypeMatrix)
    {
        if (!X_Ref_XX_Ref_OperationTypeMatrix.isValid(XX_OperationTypeMatrix))
        throw new IllegalArgumentException ("XX_OperationTypeMatrix Invalid value - " + XX_OperationTypeMatrix + " - Reference_ID=1000119 - ADD - FOR - RAN - VAL");
        set_Value ("XX_OperationTypeMatrix", XX_OperationTypeMatrix);
        
    }
    
    /** Get Operation Type.
    @return Tipo de Operacion que se va a utilizar para el Calculo del Peso */
    public String getXX_OperationTypeMatrix() 
    {
        return (String)get_Value("XX_OperationTypeMatrix");
        
    }
    
    /** Set Percentage.
    @param XX_Percentage Porcentaje del Valor para el calculo de Peso */
    public void setXX_Percentage (java.math.BigDecimal XX_Percentage)
    {
        set_Value ("XX_Percentage", XX_Percentage);
        
    }
    
    /** Get Percentage.
    @return Porcentaje del Valor para el calculo de Peso */
    public java.math.BigDecimal getXX_Percentage() 
    {
        return get_ValueAsBigDecimal("XX_Percentage");
        
    }
    
    /** Set Weight Calculation ID.
    @param XX_VCN_WeightCalculation_ID ID de la tabla Weight Calculation */
    public void setXX_VCN_WeightCalculation_ID (int XX_VCN_WeightCalculation_ID)
    {
        if (XX_VCN_WeightCalculation_ID < 1) throw new IllegalArgumentException ("XX_VCN_WeightCalculation_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_WeightCalculation_ID", Integer.valueOf(XX_VCN_WeightCalculation_ID));
        
    }
    
    /** Get Weight Calculation ID.
    @return ID de la tabla Weight Calculation */
    public int getXX_VCN_WeightCalculation_ID() 
    {
        return get_ValueAsInt("XX_VCN_WeightCalculation_ID");
        
    }
    
    
}
