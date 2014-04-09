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
/** Generated Model for XX_VLO_PCProcess
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_PCProcess extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_PCProcess_ID id
    @param trx transaction
    */
    public X_XX_VLO_PCProcess (Ctx ctx, int XX_VLO_PCProcess_ID, Trx trx)
    {
        super (ctx, XX_VLO_PCProcess_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_PCProcess_ID == 0)
        {
            setXX_DATED (0);
            setXX_DATEH (0);
            setXX_VLO_PCPROCESS_ID (0);
            setXX_YEARD (0);
            setXX_YEARH (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_PCProcess (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27533590508789L;
    /** Last Updated Timestamp 2009-08-28 08:43:12.0 */
    public static final long updatedMS = 1251465192000L;
    /** AD_Table_ID=1000135 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_PCProcess");
        
    }
    ;
    
    /** TableName=XX_VLO_PCProcess */
    public static final String Table_Name="XX_VLO_PCProcess";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Month from.
    @param XX_DATED Month from */
    public void setXX_DATED (int XX_DATED)
    {
        set_Value ("XX_DATED", Integer.valueOf(XX_DATED));
        
    }
    
    /** Get Month from.
    @return Month from */
    public int getXX_DATED() 
    {
        return get_ValueAsInt("XX_DATED");
        
    }
    
    /** Set Date From.
    @param XX_DATEH Date From */
    public void setXX_DATEH (int XX_DATEH)
    {
        set_Value ("XX_DATEH", Integer.valueOf(XX_DATEH));
        
    }
    
    /** Get Date From.
    @return Date From */
    public int getXX_DATEH() 
    {
        return get_ValueAsInt("XX_DATEH");
        
    }
    
    /** Set XX_VLO_PCPROCESS_ID.
    @param XX_VLO_PCPROCESS_ID XX_VLO_PCPROCESS_ID */
    public void setXX_VLO_PCPROCESS_ID (int XX_VLO_PCPROCESS_ID)
    {
        if (XX_VLO_PCPROCESS_ID < 1) throw new IllegalArgumentException ("XX_VLO_PCPROCESS_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_PCPROCESS_ID", Integer.valueOf(XX_VLO_PCPROCESS_ID));
        
    }
    
    /** Get XX_VLO_PCPROCESS_ID.
    @return XX_VLO_PCPROCESS_ID */
    public int getXX_VLO_PCPROCESS_ID() 
    {
        return get_ValueAsInt("XX_VLO_PCPROCESS_ID");
        
    }
    
    /** Set Year From.
    @param XX_YEARD Year From */
    public void setXX_YEARD (int XX_YEARD)
    {
        set_Value ("XX_YEARD", Integer.valueOf(XX_YEARD));
        
    }
    
    /** Get Year From.
    @return Year From */
    public int getXX_YEARD() 
    {
        return get_ValueAsInt("XX_YEARD");
        
    }
    
    /** Set Date Until.
    @param XX_YEARH Date Until */
    public void setXX_YEARH (int XX_YEARH)
    {
        set_Value ("XX_YEARH", Integer.valueOf(XX_YEARH));
        
    }
    
    /** Get Date Until.
    @return Date Until */
    public int getXX_YEARH() 
    {
        return get_ValueAsInt("XX_YEARH");
        
    }
    
    
}
