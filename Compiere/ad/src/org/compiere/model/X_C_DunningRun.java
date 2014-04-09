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
/** Generated Model for C_DunningRun
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_C_DunningRun.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_C_DunningRun extends PO
{
    /** Standard Constructor
    @param ctx context
    @param C_DunningRun_ID id
    @param trx transaction
    */
    public X_C_DunningRun (Ctx ctx, int C_DunningRun_ID, Trx trx)
    {
        super (ctx, C_DunningRun_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (C_DunningRun_ID == 0)
        {
            setC_DunningLevel_ID (0);
            setC_DunningRun_ID (0);
            setDunningDate (new Timestamp(System.currentTimeMillis()));	// @#Date@
            setProcessed (false);	// N
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_C_DunningRun (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=526 */
    public static final int Table_ID=526;
    
    /** TableName=C_DunningRun */
    public static final String Table_Name="C_DunningRun";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Dunning Level.
    @param C_DunningLevel_ID Dunning Level */
    public void setC_DunningLevel_ID (int C_DunningLevel_ID)
    {
        if (C_DunningLevel_ID < 1) throw new IllegalArgumentException ("C_DunningLevel_ID is mandatory.");
        set_ValueNoCheck ("C_DunningLevel_ID", Integer.valueOf(C_DunningLevel_ID));
        
    }
    
    /** Get Dunning Level.
    @return Dunning Level */
    public int getC_DunningLevel_ID() 
    {
        return get_ValueAsInt("C_DunningLevel_ID");
        
    }
    
    /** Set Dunning Run.
    @param C_DunningRun_ID Dunning Run */
    public void setC_DunningRun_ID (int C_DunningRun_ID)
    {
        if (C_DunningRun_ID < 1) throw new IllegalArgumentException ("C_DunningRun_ID is mandatory.");
        set_ValueNoCheck ("C_DunningRun_ID", Integer.valueOf(C_DunningRun_ID));
        
    }
    
    /** Get Dunning Run.
    @return Dunning Run */
    public int getC_DunningRun_ID() 
    {
        return get_ValueAsInt("C_DunningRun_ID");
        
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
    
    /** Set Dunning Date.
    @param DunningDate Date of Dunning */
    public void setDunningDate (Timestamp DunningDate)
    {
        if (DunningDate == null) throw new IllegalArgumentException ("DunningDate is mandatory.");
        set_Value ("DunningDate", DunningDate);
        
    }
    
    /** Get Dunning Date.
    @return Date of Dunning */
    public Timestamp getDunningDate() 
    {
        return (Timestamp)get_Value("DunningDate");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getDunningDate()));
        
    }
    
    /** Set Processed.
    @param Processed The document has been processed */
    public void setProcessed (boolean Processed)
    {
        set_ValueNoCheck ("Processed", Boolean.valueOf(Processed));
        
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
    
    /** Set Send.
    @param SendIt Send */
    public void setSendIt (String SendIt)
    {
        set_Value ("SendIt", SendIt);
        
    }
    
    /** Get Send.
    @return Send */
    public String getSendIt() 
    {
        return (String)get_Value("SendIt");
        
    }
    
    
}
