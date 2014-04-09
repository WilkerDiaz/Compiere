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
/** Generated Model for XX_VMR_VendorDepartAssoci
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_VendorDepartAssoci extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_VendorDepartAssoci_ID id
    @param trx transaction
    */
    public X_XX_VMR_VendorDepartAssoci (Ctx ctx, int XX_VMR_VendorDepartAssoci_ID, Trx trx)
    {
        super (ctx, XX_VMR_VendorDepartAssoci_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_VendorDepartAssoci_ID == 0)
        {
            setXX_VMR_Department_ID (0);
            setXX_VMR_VENDORDEPARTASSOCI_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_VendorDepartAssoci (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27568764649789L;
    /** Last Updated Timestamp 2010-10-09 11:18:53.0 */
    public static final long updatedMS = 1286639333000L;
    /** AD_Table_ID=1000094 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_VendorDepartAssoci");
        
    }
    ;
    
    /** TableName=XX_VMR_VendorDepartAssoci */
    public static final String Table_Name="XX_VMR_VendorDepartAssoci";
    
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
        if (C_BPartner_ID <= 0) set_ValueNoCheck ("C_BPartner_ID", null);
        else
        set_ValueNoCheck ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID < 1) throw new IllegalArgumentException ("XX_VMR_Department_ID is mandatory.");
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
        if (XX_VMR_VENDORDEPARTASSOCI_ID < 1) throw new IllegalArgumentException ("XX_VMR_VENDORDEPARTASSOCI_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_VENDORDEPARTASSOCI_ID", Integer.valueOf(XX_VMR_VENDORDEPARTASSOCI_ID));
        
    }
    
    /** Get XX_VMR_VENDORDEPARTASSOCI_ID.
    @return XX_VMR_VENDORDEPARTASSOCI_ID */
    public int getXX_VMR_VENDORDEPARTASSOCI_ID() 
    {
        return get_ValueAsInt("XX_VMR_VENDORDEPARTASSOCI_ID");
        
    }
    
    
}
