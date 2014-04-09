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
/** Generated Model for I_XX_VCN_ProductName
 *  @author Jorg Janke (generated) 
 *  @version Release 3.2.1 - $Id$ */
public class X_I_XX_VCN_ProductName extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VCN_ProductName_ID id
    @param trxName transaction
    */
    public X_I_XX_VCN_ProductName (Ctx ctx, int I_XX_VCN_ProductName_ID, Trx trxName)
    {
        super (ctx, I_XX_VCN_ProductName_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VCN_ProductName_ID == 0)
        {
            setI_XX_VCN_PRODUCTNAME_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_I_XX_VCN_ProductName (Ctx ctx, ResultSet rs, Trx trxName)
    {
        super (ctx, rs, trxName);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27581984763789L;
    /** Last Updated Timestamp 2011-03-11 11:34:07.0 */
    public static final long updatedMS = 1299859447000L;
    /** AD_Table_ID=1000391 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VCN_ProductName");
        
    }
    ;
    
    /** TableName=I_XX_VCN_ProductName */
    public static final String Table_Name="I_XX_VCN_ProductName";
    
    protected static KeyNamePair Model = new KeyNamePair(Table_ID,"I_XX_VCN_ProductName");

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
    @param string Has this import been processed? */
    public void setI_IsImported (String string)
    {
        set_Value ("I_IsImported", Boolean.valueOf(string));
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public boolean isI_IsImported() 
    {
        return get_ValueAsBoolean("I_IsImported");
        
    }
    
    /** Set I_XX_VCN_PRODUCTNAME_ID.
    @param I_XX_VCN_PRODUCTNAME_ID I_XX_VCN_PRODUCTNAME_ID */
    public void setI_XX_VCN_PRODUCTNAME_ID (int I_XX_VCN_PRODUCTNAME_ID)
    {
        if (I_XX_VCN_PRODUCTNAME_ID < 1) throw new IllegalArgumentException ("I_XX_VCN_PRODUCTNAME_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VCN_PRODUCTNAME_ID", Integer.valueOf(I_XX_VCN_PRODUCTNAME_ID));
        
    }
    
    /** Get I_XX_VCN_PRODUCTNAME_ID.
    @return I_XX_VCN_PRODUCTNAME_ID */
    public int getI_XX_VCN_PRODUCTNAME_ID() 
    {
        return get_ValueAsInt("I_XX_VCN_PRODUCTNAME_ID");
        
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
    
    /** Set Product Name.
    @param XX_ProductName Product Name */
    public void setXX_ProductName (String XX_ProductName)
    {
        set_Value ("XX_ProductName", XX_ProductName);
        
    }
    
    /** Get Product Name.
    @return Product Name */
    public String getXX_ProductName() 
    {
        return (String)get_Value("XX_ProductName");
        
    }
    
    
}
