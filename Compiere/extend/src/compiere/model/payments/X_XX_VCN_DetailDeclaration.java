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
package compiere.model.payments;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_DetailDeclaration
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_DetailDeclaration extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_DetailDeclaration_ID id
    @param trx transaction
    */
    public X_XX_VCN_DetailDeclaration (Ctx ctx, int XX_VCN_DetailDeclaration_ID, Trx trx)
    {
        super (ctx, XX_VCN_DetailDeclaration_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_DetailDeclaration_ID == 0)
        {
            setXX_VCN_DetailDeclaration_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_DetailDeclaration (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27627084823789L;
    /** Last Updated Timestamp 2012-08-14 11:21:47.0 */
    public static final long updatedMS = 1344959507000L;
    /** AD_Table_ID=1002658 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_DetailDeclaration");
        
    }
    ;
    
    /** TableName=XX_VCN_DetailDeclaration */
    public static final String Table_Name="XX_VCN_DetailDeclaration";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Close Completed.
    @param XX_CheckCompleted Completed */
    public void setXX_CheckCompleted (boolean XX_CheckCompleted)
    {
        set_Value ("XX_CheckCompleted", Boolean.valueOf(XX_CheckCompleted));
        
    }
    
    /** Get Close Completed.
    @return Completed */
    public boolean isXX_CheckCompleted() 
    {
        return get_ValueAsBoolean("XX_CheckCompleted");
        
    }
    
    /** Set End of Period.
    @param XX_CheckEndPeriod Check End of Period */
    public void setXX_CheckEndPeriod (boolean XX_CheckEndPeriod)
    {
        set_Value ("XX_CheckEndPeriod", Boolean.valueOf(XX_CheckEndPeriod));
        
    }
    
    /** Get End of Period.
    @return Check End of Period */
    public boolean isXX_CheckEndPeriod() 
    {
        return get_ValueAsBoolean("XX_CheckEndPeriod");
        
    }
    
    /** Set Close Date.
    @param XX_DateClose Close Date */
    public void setXX_DateClose (Timestamp XX_DateClose)
    {
        set_Value ("XX_DateClose", XX_DateClose);
        
    }
    
    /** Get Close Date.
    @return Close Date */
    public Timestamp getXX_DateClose() 
    {
        return (Timestamp)get_Value("XX_DateClose");
        
    }
    
    /** Julio = 01 */
    public static final String XX_MONTH_Julio = X_Ref_XX_RefMonthDeclaration.JULIO.getValue();
    /** Agosto = 02 */
    public static final String XX_MONTH_Agosto = X_Ref_XX_RefMonthDeclaration.AGOSTO.getValue();
    /** Septiembre = 03 */
    public static final String XX_MONTH_Septiembre = X_Ref_XX_RefMonthDeclaration.SEPTIEMBRE.getValue();
    /** Octubre = 04 */
    public static final String XX_MONTH_Octubre = X_Ref_XX_RefMonthDeclaration.OCTUBRE.getValue();
    /** Noviembre = 05 */
    public static final String XX_MONTH_Noviembre = X_Ref_XX_RefMonthDeclaration.NOVIEMBRE.getValue();
    /** Diciembre = 06 */
    public static final String XX_MONTH_Diciembre = X_Ref_XX_RefMonthDeclaration.DICIEMBRE.getValue();
    /** Enero = 07 */
    public static final String XX_MONTH_Enero = X_Ref_XX_RefMonthDeclaration.ENERO.getValue();
    /** Febrero = 08 */
    public static final String XX_MONTH_Febrero = X_Ref_XX_RefMonthDeclaration.FEBRERO.getValue();
    /** Marzo = 09 */
    public static final String XX_MONTH_Marzo = X_Ref_XX_RefMonthDeclaration.MARZO.getValue();
    /** Abril = 10 */
    public static final String XX_MONTH_Abril = X_Ref_XX_RefMonthDeclaration.ABRIL.getValue();
    /** Mayo = 11 */
    public static final String XX_MONTH_Mayo = X_Ref_XX_RefMonthDeclaration.MAYO.getValue();
    /** Junio = 12 */
    public static final String XX_MONTH_Junio = X_Ref_XX_RefMonthDeclaration.JUNIO.getValue();
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (String XX_Month)
    {
        if (!X_Ref_XX_RefMonthDeclaration.isValid(XX_Month))
        throw new IllegalArgumentException ("XX_Month Invalid value - " + XX_Month + " - Reference_ID=1001865 - 01 - 02 - 03 - 04 - 05 - 06 - 07 - 08 - 09 - 10 - 11 - 12");
        set_Value ("XX_Month", XX_Month);
        
    }
    
    /** Get Month.
    @return Month */
    public String getXX_Month() 
    {
        return (String)get_Value("XX_Month");
        
    }
    
    /** Set Number Declaration ID.
    @param XX_NumberDeclaration_ID Number Declaration ID */
    public void setXX_NumberDeclaration_ID (int XX_NumberDeclaration_ID)
    {
        if (XX_NumberDeclaration_ID <= 0) set_Value ("XX_NumberDeclaration_ID", null);
        else
        set_Value ("XX_NumberDeclaration_ID", Integer.valueOf(XX_NumberDeclaration_ID));
        
    }
    
    /** Get Number Declaration ID.
    @return Number Declaration ID */
    public int getXX_NumberDeclaration_ID() 
    {
        return get_ValueAsInt("XX_NumberDeclaration_ID");
        
    }
    
    /** Set Period Year.
    @param XX_PeriodYear_ID Period Year */
    public void setXX_PeriodYear_ID (int XX_PeriodYear_ID)
    {
        if (XX_PeriodYear_ID <= 0) set_Value ("XX_PeriodYear_ID", null);
        else
        set_Value ("XX_PeriodYear_ID", Integer.valueOf(XX_PeriodYear_ID));
        
    }
    
    /** Get Period Year.
    @return Period Year */
    public int getXX_PeriodYear_ID() 
    {
        return get_ValueAsInt("XX_PeriodYear_ID");
        
    }
    
    /** Set Retention Agent.
    @param XX_RetentionAgent RetentionAgent */
    public void setXX_RetentionAgent (boolean XX_RetentionAgent)
    {
        throw new IllegalArgumentException ("XX_RetentionAgent is virtual column");
        
    }
    
    /** Get Retention Agent.
    @return RetentionAgent */
    public boolean isXX_RetentionAgent() 
    {
        return get_ValueAsBoolean("XX_RetentionAgent");
        
    }
    
    /** Set XX_VCN_DetailDeclaration_ID.
    @param XX_VCN_DetailDeclaration_ID XX_VCN_DetailDeclaration_ID */
    public void setXX_VCN_DetailDeclaration_ID (int XX_VCN_DetailDeclaration_ID)
    {
        if (XX_VCN_DetailDeclaration_ID < 1) throw new IllegalArgumentException ("XX_VCN_DetailDeclaration_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_DetailDeclaration_ID", Integer.valueOf(XX_VCN_DetailDeclaration_ID));
        
    }
    
    /** Get XX_VCN_DetailDeclaration_ID.
    @return XX_VCN_DetailDeclaration_ID */
    public int getXX_VCN_DetailDeclaration_ID() 
    {
        return get_ValueAsInt("XX_VCN_DetailDeclaration_ID");
        
    }
    
    /** Set Withholding Process.
    @param XX_WithholdingProcess Process that is responsible for generating the txt file withholding */
    public void setXX_WithholdingProcess (String XX_WithholdingProcess)
    {
        set_Value ("XX_WithholdingProcess", XX_WithholdingProcess);
        
    }
    
    /** Get Withholding Process.
    @return Process that is responsible for generating the txt file withholding */
    public String getXX_WithholdingProcess() 
    {
        return (String)get_Value("XX_WithholdingProcess");
        
    }
    
    /** Set Year/Month.
    @param XX_YearMonth Year/Month */
    public void setXX_YearMonth (String XX_YearMonth)
    {
        set_Value ("XX_YearMonth", XX_YearMonth);
        
    }
    
    /** Get Year/Month.
    @return Year/Month */
    public String getXX_YearMonth() 
    {
        return (String)get_Value("XX_YearMonth");
        
    }
    
    
}
