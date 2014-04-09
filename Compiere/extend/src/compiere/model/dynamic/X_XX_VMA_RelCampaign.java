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
package compiere.model.dynamic;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMA_RelCampaign
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMA_RelCampaign extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMA_RelCampaign_ID id
    @param trx transaction
    */
    public X_XX_VMA_RelCampaign (Ctx ctx, int XX_VMA_RelCampaign_ID, Trx trx)
    {
        super (ctx, XX_VMA_RelCampaign_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMA_RelCampaign_ID == 0)
        {
            setC_Campaign_ID (0);
            setValue (null);
            setXX_VMA_RELCAMPAIGN_ID (0);
            setXX_VMR_Collection_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMA_RelCampaign (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27590534410789L;
    /** Last Updated Timestamp 2011-06-18 10:28:14.0 */
    public static final long updatedMS = 1308409094000L;
    /** AD_Table_ID=1000442 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMA_RelCampaign");
        
    }
    ;
    
    /** TableName=XX_VMA_RelCampaign */
    public static final String Table_Name="XX_VMA_RelCampaign";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID < 1) throw new IllegalArgumentException ("C_Campaign_ID is mandatory.");
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set XX_VMA_RELCAMPAIGN_ID.
    @param XX_VMA_RELCAMPAIGN_ID XX_VMA_RELCAMPAIGN_ID */
    public void setXX_VMA_RELCAMPAIGN_ID (int XX_VMA_RELCAMPAIGN_ID)
    {
        if (XX_VMA_RELCAMPAIGN_ID < 1) throw new IllegalArgumentException ("XX_VMA_RELCAMPAIGN_ID is mandatory.");
        set_ValueNoCheck ("XX_VMA_RELCAMPAIGN_ID", Integer.valueOf(XX_VMA_RELCAMPAIGN_ID));
        
    }
    
    /** Get XX_VMA_RELCAMPAIGN_ID.
    @return XX_VMA_RELCAMPAIGN_ID */
    public int getXX_VMA_RELCAMPAIGN_ID() 
    {
        return get_ValueAsInt("XX_VMA_RELCAMPAIGN_ID");
        
    }
    
    /** Set Collection.
    @param XX_VMR_Collection_ID ID de Colección */
    public void setXX_VMR_Collection_ID (int XX_VMR_Collection_ID)
    {
        if (XX_VMR_Collection_ID < 1) throw new IllegalArgumentException ("XX_VMR_Collection_ID is mandatory.");
        set_Value ("XX_VMR_Collection_ID", Integer.valueOf(XX_VMR_Collection_ID));
        
    }
    
    /** Get Collection.
    @return ID de Colección */
    public int getXX_VMR_Collection_ID() 
    {
        return get_ValueAsInt("XX_VMR_Collection_ID");
        
    }
    
    
}
