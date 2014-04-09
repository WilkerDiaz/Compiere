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
/** Generated Model for I_XX_VMR_VendorDepart
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VMR_VendorDepart extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_VendorDepart_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_VendorDepart (Ctx ctx, int I_XX_VMR_VendorDepart_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_VendorDepart_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_VendorDepart_ID == 0)
        {
            setI_XX_VMR_VENDORDEPART_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_VendorDepart (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567740401789L;
    /** Last Updated Timestamp 2010-09-27 14:48:05.0 */
    public static final long updatedMS = 1285615085000L;
    /** AD_Table_ID=1000378 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_VendorDepart");
        
    }
    ;
    
    /** TableName=I_XX_VMR_VendorDepart */
    public static final String Table_Name="I_XX_VMR_VendorDepart";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Department Code.
    @param CODDEP Department Code */
    public void setCODDEP (String CODDEP)
    {
        set_Value ("CODDEP", CODDEP);
        
    }
    
    /** Get Department Code.
    @return Department Code */
    public String getCODDEP() 
    {
        return (String)get_Value("CODDEP");
        
    }
    
    /** Set COEMPE.
    @param COEMPE COEMPE */
    public void setCOEMPE (String COEMPE)
    {
        set_Value ("COEMPE", COEMPE);
        
    }
    
    /** Get COEMPE.
    @return COEMPE */
    public String getCOEMPE() 
    {
        return (String)get_Value("COEMPE");
        
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
    
    /** Set I_XX_VMR_VENDORDEPART_ID.
    @param I_XX_VMR_VENDORDEPART_ID I_XX_VMR_VENDORDEPART_ID */
    public void setI_XX_VMR_VENDORDEPART_ID (int I_XX_VMR_VENDORDEPART_ID)
    {
        if (I_XX_VMR_VENDORDEPART_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_VENDORDEPART_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_VENDORDEPART_ID", Integer.valueOf(I_XX_VMR_VENDORDEPART_ID));
        
    }
    
    /** Get I_XX_VMR_VENDORDEPART_ID.
    @return I_XX_VMR_VENDORDEPART_ID */
    public int getI_XX_VMR_VENDORDEPART_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_VENDORDEPART_ID");
        
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
    
    /** Set XX_VMR_VENDORDEPARTASSOCI_ID.
    @param XX_VMR_VENDORDEPARTASSOCI_ID XX_VMR_VENDORDEPARTASSOCI_ID */
    public void setXX_VMR_VENDORDEPARTASSOCI_ID (int XX_VMR_VENDORDEPARTASSOCI_ID)
    {
        if (XX_VMR_VENDORDEPARTASSOCI_ID <= 0) set_Value ("XX_VMR_VENDORDEPARTASSOCI_ID", null);
        else
        set_Value ("XX_VMR_VENDORDEPARTASSOCI_ID", Integer.valueOf(XX_VMR_VENDORDEPARTASSOCI_ID));
        
    }
    
    /** Get XX_VMR_VENDORDEPARTASSOCI_ID.
    @return XX_VMR_VENDORDEPARTASSOCI_ID */
    public int getXX_VMR_VENDORDEPARTASSOCI_ID() 
    {
        return get_ValueAsInt("XX_VMR_VENDORDEPARTASSOCI_ID");
        
    }
    
    
}
