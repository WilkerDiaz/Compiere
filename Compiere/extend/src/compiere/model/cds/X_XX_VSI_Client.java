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
/** Generated Model for XX_VSI_Client
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VSI_Client extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VSI_Client_ID id
    @param trx transaction
    */
    public X_XX_VSI_Client (Ctx ctx, int XX_VSI_Client_ID, Trx trx)
    {
        super (ctx, XX_VSI_Client_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VSI_Client_ID == 0)
        {
            setName (null);
            setXX_VSI_CLIENT_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VSI_Client (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27582261045789L;
    /** Last Updated Timestamp 2011-03-14 16:18:49.0 */
    public static final long updatedMS = 1300135729000L;
    /** AD_Table_ID=1000399 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VSI_Client");
        
    }
    ;
    
    /** TableName=XX_VSI_Client */
    public static final String Table_Name="XX_VSI_Client";
    
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
    
    /** Set Cedula/RIF.
    @param XX_CI_RIF Cedula o RIF del Proveedor o Cliente */
    public void setXX_CI_RIF (String XX_CI_RIF)
    {
        set_Value ("XX_CI_RIF", XX_CI_RIF);
        
    }
    
    /** Get Cedula/RIF.
    @return Cedula o RIF del Proveedor o Cliente */
    public String getXX_CI_RIF() 
    {
        return (String)get_Value("XX_CI_RIF");
        
    }
    
    /** Set Company ID.
    @param XX_Company Company ID */
    public void setXX_Company (int XX_Company)
    {
        set_Value ("XX_Company", Integer.valueOf(XX_Company));
        
    }
    
    /** Get Company ID.
    @return Company ID */
    public int getXX_Company() 
    {
        return get_ValueAsInt("XX_Company");
        
    }
    
    /** Set Fax.
    @param XX_Fax Facsimile number */
    public void setXX_Fax (String XX_Fax)
    {
        set_Value ("XX_Fax", XX_Fax);
        
    }
    
    /** Get Fax.
    @return Facsimile number */
    public String getXX_Fax() 
    {
        return (String)get_Value("XX_Fax");
        
    }
    
    /** Set Logo.
    @param XX_Logo_ID Logo */
    public void setXX_Logo_ID (int XX_Logo_ID)
    {
        if (XX_Logo_ID <= 0) set_Value ("XX_Logo_ID", null);
        else
        set_Value ("XX_Logo_ID", Integer.valueOf(XX_Logo_ID));
        
    }
    
    /** Get Logo.
    @return Logo */
    public int getXX_Logo_ID() 
    {
        return get_ValueAsInt("XX_Logo_ID");
        
    }
    
    /** Set NIT.
    @param XX_NIT NIT del Proveedor */
    public void setXX_NIT (String XX_NIT)
    {
        set_Value ("XX_NIT", XX_NIT);
        
    }
    
    /** Get NIT.
    @return NIT del Proveedor */
    public String getXX_NIT() 
    {
        return (String)get_Value("XX_NIT");
        
    }
    
    /** Set Phone.
    @param XX_Phone Identifies a telephone number */
    public void setXX_Phone (String XX_Phone)
    {
        set_Value ("XX_Phone", XX_Phone);
        
    }
    
    /** Get Phone.
    @return Identifies a telephone number */
    public String getXX_Phone() 
    {
        return (String)get_Value("XX_Phone");
        
    }
    
    /** Set XX_VSI_CLIENT_ID.
    @param XX_VSI_CLIENT_ID XX_VSI_CLIENT_ID */
    public void setXX_VSI_CLIENT_ID (int XX_VSI_CLIENT_ID)
    {
        if (XX_VSI_CLIENT_ID < 1) throw new IllegalArgumentException ("XX_VSI_CLIENT_ID is mandatory.");
        set_ValueNoCheck ("XX_VSI_CLIENT_ID", Integer.valueOf(XX_VSI_CLIENT_ID));
        
    }
    
    /** Get XX_VSI_CLIENT_ID.
    @return XX_VSI_CLIENT_ID */
    public int getXX_VSI_CLIENT_ID() 
    {
        return get_ValueAsInt("XX_VSI_CLIENT_ID");
        
    }
    
    
}
