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
package compiere.model.promociones;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VSI_WarehouseNet
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VSI_WarehouseNet extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VSI_WarehouseNet_ID id
    @param trx transaction
    */
    public X_XX_VSI_WarehouseNet (Ctx ctx, int XX_VSI_WarehouseNet_ID, Trx trx)
    {
        super (ctx, XX_VSI_WarehouseNet_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VSI_WarehouseNet_ID == 0)
        {
            setM_Warehouse_ID (0);
            setXX_VSI_WarehouseNet_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VSI_WarehouseNet (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27612590786789L;
    /** Last Updated Timestamp 2012-02-28 17:14:30.0 */
    public static final long updatedMS = 1330465470000L;
    /** AD_Table_ID=1000410 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VSI_WarehouseNet");
        
    }
    ;
    
    /** TableName=XX_VSI_WarehouseNet */
    public static final String Table_Name="XX_VSI_WarehouseNet";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set IP Address.
    @param IP_Address Defines the IP address to transfer data to */
    public void setIP_Address (String IP_Address)
    {
        set_Value ("IP_Address", IP_Address);
        
    }
    
    /** Get IP Address.
    @return Defines the IP address to transfer data to */
    public String getIP_Address() 
    {
        return (String)get_Value("IP_Address");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_ValueNoCheck ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Password.
    @param Password Password of any length (case sensitive) */
    public void setPassword (String Password)
    {
        set_Value ("Password", Password);
        
    }
    
    /** Get Password.
    @return Password of any length (case sensitive) */
    public String getPassword() 
    {
        return (String)get_Value("Password");
        
    }
    
    /** Set Registered EMail.
    @param UserName Email of the responsible for the System */
    public void setUserName (String UserName)
    {
        set_Value ("UserName", UserName);
        
    }
    
    /** Get Registered EMail.
    @return Email of the responsible for the System */
    public String getUserName() 
    {
        return (String)get_Value("UserName");
        
    }
    
    /** Set XX_PutFTP.
    @param XX_PutFTP XX_PutFTP */
    public void setXX_PutFTP (String XX_PutFTP)
    {
        set_Value ("XX_PutFTP", XX_PutFTP);
        
    }
    
    /** Get XX_PutFTP.
    @return XX_PutFTP */
    public String getXX_PutFTP() 
    {
        return (String)get_Value("XX_PutFTP");
        
    }
    
    /** Set XX_RootFTP.
    @param XX_RootFTP XX_RootFTP */
    public void setXX_RootFTP (String XX_RootFTP)
    {
        set_Value ("XX_RootFTP", XX_RootFTP);
        
    }
    
    /** Get XX_RootFTP.
    @return XX_RootFTP */
    public String getXX_RootFTP() 
    {
        return (String)get_Value("XX_RootFTP");
        
    }
    
    /** Set XX_VSI_WarehouseNet_ID.
    @param XX_VSI_WarehouseNet_ID XX_VSI_WarehouseNet_ID */
    public void setXX_VSI_WarehouseNet_ID (int XX_VSI_WarehouseNet_ID)
    {
        if (XX_VSI_WarehouseNet_ID < 1) throw new IllegalArgumentException ("XX_VSI_WarehouseNet_ID is mandatory.");
        set_ValueNoCheck ("XX_VSI_WarehouseNet_ID", Integer.valueOf(XX_VSI_WarehouseNet_ID));
        
    }
    
    /** Get XX_VSI_WarehouseNet_ID.
    @return XX_VSI_WarehouseNet_ID */
    public int getXX_VSI_WarehouseNet_ID() 
    {
        return get_ValueAsInt("XX_VSI_WarehouseNet_ID");
        
    }
    
    
}
