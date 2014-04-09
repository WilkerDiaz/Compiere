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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for AD_OrgInfo
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_OrgInfo.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_OrgInfo extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_OrgInfo_ID id
    @param trx transaction
    */
    public X_AD_OrgInfo (Ctx ctx, int AD_OrgInfo_ID, Trx trx)
    {
        super (ctx, AD_OrgInfo_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_OrgInfo_ID == 0)
        {
            setDUNS (null);	// ?
            setTaxID (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_OrgInfo (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27507962592789L;
    /** Last Updated Timestamp 2008-11-04 14:21:16.0 */
    public static final long updatedMS = 1225837276000L;
    /** AD_Table_ID=228 */
    public static final int Table_ID=228;
    
    /** TableName=AD_OrgInfo */
    public static final String Table_Name="AD_OrgInfo";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Organization Type.
    @param AD_OrgType_ID Organization Type allows you to categorize your organizations */
    public void setAD_OrgType_ID (int AD_OrgType_ID)
    {
        if (AD_OrgType_ID <= 0) set_Value ("AD_OrgType_ID", null);
        else
        set_Value ("AD_OrgType_ID", Integer.valueOf(AD_OrgType_ID));
        
    }
    
    /** Get Organization Type.
    @return Organization Type allows you to categorize your organizations */
    public int getAD_OrgType_ID() 
    {
        return get_ValueAsInt("AD_OrgType_ID");
        
    }
    
    /** Set Calendar.
    @param C_Calendar_ID Accounting Calendar Name */
    public void setC_Calendar_ID (int C_Calendar_ID)
    {
        if (C_Calendar_ID <= 0) set_Value ("C_Calendar_ID", null);
        else
        set_Value ("C_Calendar_ID", Integer.valueOf(C_Calendar_ID));
        
    }
    
    /** Get Calendar.
    @return Accounting Calendar Name */
    public int getC_Calendar_ID() 
    {
        return get_ValueAsInt("C_Calendar_ID");
        
    }
    
    /** Set Address.
    @param C_Location_ID Location or Address */
    public void setC_Location_ID (int C_Location_ID)
    {
        if (C_Location_ID <= 0) set_Value ("C_Location_ID", null);
        else
        set_Value ("C_Location_ID", Integer.valueOf(C_Location_ID));
        
    }
    
    /** Get Address.
    @return Location or Address */
    public int getC_Location_ID() 
    {
        return get_ValueAsInt("C_Location_ID");
        
    }
    
    /** Set D-U-N-S.
    @param DUNS Creditor Check (Dun & Bradstreet) Number */
    public void setDUNS (String DUNS)
    {
        if (DUNS == null) throw new IllegalArgumentException ("DUNS is mandatory.");
        set_Value ("DUNS", DUNS);
        
    }
    
    /** Get D-U-N-S.
    @return Creditor Check (Dun & Bradstreet) Number */
    public String getDUNS() 
    {
        return (String)get_Value("DUNS");
        
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
    
    /** Set Parent Organization.
    @param Parent_Org_ID Parent (superior) Organization */
    public void setParent_Org_ID (int Parent_Org_ID)
    {
        if (Parent_Org_ID <= 0) set_Value ("Parent_Org_ID", null);
        else
        set_Value ("Parent_Org_ID", Integer.valueOf(Parent_Org_ID));
        
    }
    
    /** Get Parent Organization.
    @return Parent (superior) Organization */
    public int getParent_Org_ID() 
    {
        return get_ValueAsInt("Parent_Org_ID");
        
    }
    
    /** Set Supervisor.
    @param Supervisor_ID Supervisor for this user/organization - used for escalation and approval */
    public void setSupervisor_ID (int Supervisor_ID)
    {
        if (Supervisor_ID <= 0) set_Value ("Supervisor_ID", null);
        else
        set_Value ("Supervisor_ID", Integer.valueOf(Supervisor_ID));
        
    }
    
    /** Get Supervisor.
    @return Supervisor for this user/organization - used for escalation and approval */
    public int getSupervisor_ID() 
    {
        return get_ValueAsInt("Supervisor_ID");
        
    }
    
    /** Set Tax ID.
    @param TaxID Tax Identification */
    public void setTaxID (String TaxID)
    {
        if (TaxID == null) throw new IllegalArgumentException ("TaxID is mandatory.");
        set_Value ("TaxID", TaxID);
        
    }
    
    /** Get Tax ID.
    @return Tax Identification */
    public String getTaxID() 
    {
        return (String)get_Value("TaxID");
        
    }
    
    
}
