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
/** Generated Model for XX_VLO_LTProcess
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_LTProcess extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_LTProcess_ID id
    @param trx transaction
    */
    public X_XX_VLO_LTProcess (Ctx ctx, int XX_VLO_LTProcess_ID, Trx trx)
    {
        super (ctx, XX_VLO_LTProcess_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_LTProcess_ID == 0)
        {
            setXX_VLO_LTPROCESS_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_LTProcess (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27543209154789L;
    /** Last Updated Timestamp 2009-12-17 16:33:58.0 */
    public static final long updatedMS = 1261083838000L;
    /** AD_Table_ID=1000130 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_LTProcess");
        
    }
    ;
    
    /** TableName=XX_VLO_LTProcess */
    public static final String Table_Name="XX_VLO_LTProcess";
    
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
    
    /** Set Month From.
    @param XX_MonthFrom Month From */
    public void setXX_MonthFrom (int XX_MonthFrom)
    {
        set_Value ("XX_MonthFrom", Integer.valueOf(XX_MonthFrom));
        
    }
    
    /** Get Month From.
    @return Month From */
    public int getXX_MonthFrom() 
    {
        return get_ValueAsInt("XX_MonthFrom");
        
    }
    
    /** Set Month Until.
    @param XX_MonthUntil Month Until */
    public void setXX_MonthUntil (int XX_MonthUntil)
    {
        set_Value ("XX_MonthUntil", Integer.valueOf(XX_MonthUntil));
        
    }
    
    /** Get Month Until.
    @return Month Until */
    public int getXX_MonthUntil() 
    {
        return get_ValueAsInt("XX_MonthUntil");
        
    }
    
    /** Set XX_VLO_LTPROCESS_ID.
    @param XX_VLO_LTPROCESS_ID XX_VLO_LTPROCESS_ID */
    public void setXX_VLO_LTPROCESS_ID (int XX_VLO_LTPROCESS_ID)
    {
        if (XX_VLO_LTPROCESS_ID < 1) throw new IllegalArgumentException ("XX_VLO_LTPROCESS_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_LTPROCESS_ID", Integer.valueOf(XX_VLO_LTPROCESS_ID));
        
    }
    
    /** Get XX_VLO_LTPROCESS_ID.
    @return XX_VLO_LTPROCESS_ID */
    public int getXX_VLO_LTPROCESS_ID() 
    {
        return get_ValueAsInt("XX_VLO_LTPROCESS_ID");
        
    }
    
    /** Set Year From.
    @param XX_YearFrom Year From */
    public void setXX_YearFrom (int XX_YearFrom)
    {
        set_Value ("XX_YearFrom", Integer.valueOf(XX_YearFrom));
        
    }
    
    /** Get Year From.
    @return Year From */
    public int getXX_YearFrom() 
    {
        return get_ValueAsInt("XX_YearFrom");
        
    }
    
    /** Set Year Until.
    @param XX_YearUntil Year Until */
    public void setXX_YearUntil (int XX_YearUntil)
    {
        set_Value ("XX_YearUntil", Integer.valueOf(XX_YearUntil));
        
    }
    
    /** Get Year Until.
    @return Year Until */
    public int getXX_YearUntil() 
    {
        return get_ValueAsInt("XX_YearUntil");
        
    }
    
    
}
