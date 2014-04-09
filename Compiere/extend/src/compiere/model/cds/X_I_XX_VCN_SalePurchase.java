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
/** Generated Model for I_XX_VCN_SalePurchase
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VCN_SalePurchase extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VCN_SalePurchase_ID id
    @param trx transaction
    */
    public X_I_XX_VCN_SalePurchase (Ctx ctx, int I_XX_VCN_SalePurchase_ID, Trx trx)
    {
        super (ctx, I_XX_VCN_SalePurchase_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VCN_SalePurchase_ID == 0)
        {
            setI_XX_VCN_SALEPURCHASE_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VCN_SalePurchase (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27549854686789L;
    /** Last Updated Timestamp 2010-03-04 14:32:50.0 */
    public static final long updatedMS = 1267729370000L;
    /** AD_Table_ID=1000254 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VCN_SalePurchase");
        
    }
    ;
    
    /** TableName=I_XX_VCN_SalePurchase */
    public static final String Table_Name="I_XX_VCN_SalePurchase";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
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
    
    /** Set I_XX_VCN_SALEPURCHASE_ID.
    @param I_XX_VCN_SALEPURCHASE_ID I_XX_VCN_SALEPURCHASE_ID */
    public void setI_XX_VCN_SALEPURCHASE_ID (int I_XX_VCN_SALEPURCHASE_ID)
    {
        if (I_XX_VCN_SALEPURCHASE_ID < 1) throw new IllegalArgumentException ("I_XX_VCN_SALEPURCHASE_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VCN_SALEPURCHASE_ID", Integer.valueOf(I_XX_VCN_SALEPURCHASE_ID));
        
    }
    
    /** Get I_XX_VCN_SALEPURCHASE_ID.
    @return I_XX_VCN_SALEPURCHASE_ID */
    public int getI_XX_VCN_SALEPURCHASE_ID() 
    {
        return get_ValueAsInt("I_XX_VCN_SALEPURCHASE_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
    
    /** Set Accumulated Amount.
    @param XX_AMOUNTACU Accumulated Amount */
    public void setXX_AMOUNTACU (String XX_AMOUNTACU)
    {
        set_Value ("XX_AMOUNTACU", XX_AMOUNTACU);
        
    }
    
    /** Get Accumulated Amount.
    @return Accumulated Amount */
    public String getXX_AMOUNTACU() 
    {
        return (String)get_Value("XX_AMOUNTACU");
        
    }
    
    /** Set Month Amount.
    @param XX_AMOUNTMONTH Month Amount */
    public void setXX_AMOUNTMONTH (String XX_AMOUNTMONTH)
    {
        set_Value ("XX_AMOUNTMONTH", XX_AMOUNTMONTH);
        
    }
    
    /** Get Month Amount.
    @return Month Amount */
    public String getXX_AMOUNTMONTH() 
    {
        return (String)get_Value("XX_AMOUNTMONTH");
        
    }
    
    /** Set XX_CVDPT.
    @param XX_CVDPT XX_CVDPT */
    public void setXX_CVDPT (String XX_CVDPT)
    {
        set_Value ("XX_CVDPT", XX_CVDPT);
        
    }
    
    /** Get XX_CVDPT.
    @return XX_CVDPT */
    public String getXX_CVDPT() 
    {
        return (String)get_Value("XX_CVDPT");
        
    }
    
    /** Set XX_CVTIE.
    @param XX_CVTIE XX_CVTIE */
    public void setXX_CVTIE (String XX_CVTIE)
    {
        set_Value ("XX_CVTIE", XX_CVTIE);
        
    }
    
    /** Get XX_CVTIE.
    @return XX_CVTIE */
    public String getXX_CVTIE() 
    {
        return (String)get_Value("XX_CVTIE");
        
    }
    
    /** Set XX_CVTRE.
    @param XX_CVTRE XX_CVTRE */
    public void setXX_CVTRE (String XX_CVTRE)
    {
        set_Value ("XX_CVTRE", XX_CVTRE);
        
    }
    
    /** Get XX_CVTRE.
    @return XX_CVTRE */
    public String getXX_CVTRE() 
    {
        return (String)get_Value("XX_CVTRE");
        
    }
    
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (String XX_Month)
    {
        set_Value ("XX_Month", XX_Month);
        
    }
    
    /** Get Month.
    @return Month */
    public String getXX_Month() 
    {
        return (String)get_Value("XX_Month");
        
    }
    
    /** Set Registration Type.
    @param XX_TYPEREG Registration Type */
    public void setXX_TYPEREG (String XX_TYPEREG)
    {
        set_Value ("XX_TYPEREG", XX_TYPEREG);
        
    }
    
    /** Get Registration Type.
    @return Registration Type */
    public String getXX_TYPEREG() 
    {
        return (String)get_Value("XX_TYPEREG");
        
    }
    
    /** Set XX_VCN_SALEPURCHASE_ID.
    @param XX_VCN_SALEPURCHASE_ID XX_VCN_SALEPURCHASE_ID */
    public void setXX_VCN_SALEPURCHASE_ID (int XX_VCN_SALEPURCHASE_ID)
    {
        if (XX_VCN_SALEPURCHASE_ID <= 0) set_Value ("XX_VCN_SALEPURCHASE_ID", null);
        else
        set_Value ("XX_VCN_SALEPURCHASE_ID", Integer.valueOf(XX_VCN_SALEPURCHASE_ID));
        
    }
    
    /** Get XX_VCN_SALEPURCHASE_ID.
    @return XX_VCN_SALEPURCHASE_ID */
    public int getXX_VCN_SALEPURCHASE_ID() 
    {
        return get_ValueAsInt("XX_VCN_SALEPURCHASE_ID");
        
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
    
    /** Set Year.
    @param XX_Year Year */
    public void setXX_Year (String XX_Year)
    {
        set_Value ("XX_Year", XX_Year);
        
    }
    
    /** Get Year.
    @return Year */
    public String getXX_Year() 
    {
        return (String)get_Value("XX_Year");
        
    }
    
    
}
