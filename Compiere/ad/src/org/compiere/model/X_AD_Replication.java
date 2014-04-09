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
/** Generated Model for AD_Replication
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Replication.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Replication extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Replication_ID id
    @param trx transaction
    */
    public X_AD_Replication (Ctx ctx, int AD_Replication_ID, Trx trx)
    {
        super (ctx, AD_Replication_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Replication_ID == 0)
        {
            setAD_ReplicationStrategy_ID (0);
            setAD_Replication_ID (0);
            setHostAddress (null);
            setHostPort (0);	// 80
            setIsRMIoverHTTP (true);	// Y
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Replication (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=605 */
    public static final int Table_ID=605;
    
    /** TableName=AD_Replication */
    public static final String Table_Name="AD_Replication";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Replication Strategy.
    @param AD_ReplicationStrategy_ID Data Replication Strategy */
    public void setAD_ReplicationStrategy_ID (int AD_ReplicationStrategy_ID)
    {
        if (AD_ReplicationStrategy_ID < 1) throw new IllegalArgumentException ("AD_ReplicationStrategy_ID is mandatory.");
        set_Value ("AD_ReplicationStrategy_ID", Integer.valueOf(AD_ReplicationStrategy_ID));
        
    }
    
    /** Get Replication Strategy.
    @return Data Replication Strategy */
    public int getAD_ReplicationStrategy_ID() 
    {
        return get_ValueAsInt("AD_ReplicationStrategy_ID");
        
    }
    
    /** Set Replication.
    @param AD_Replication_ID Data Replication Target */
    public void setAD_Replication_ID (int AD_Replication_ID)
    {
        if (AD_Replication_ID < 1) throw new IllegalArgumentException ("AD_Replication_ID is mandatory.");
        set_ValueNoCheck ("AD_Replication_ID", Integer.valueOf(AD_Replication_ID));
        
    }
    
    /** Get Replication.
    @return Data Replication Target */
    public int getAD_Replication_ID() 
    {
        return get_ValueAsInt("AD_Replication_ID");
        
    }
    
    /** Set Date Last Run.
    @param DateLastRun Date the process was last run. */
    public void setDateLastRun (Timestamp DateLastRun)
    {
        set_ValueNoCheck ("DateLastRun", DateLastRun);
        
    }
    
    /** Get Date Last Run.
    @return Date the process was last run. */
    public Timestamp getDateLastRun() 
    {
        return (Timestamp)get_Value("DateLastRun");
        
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
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Host Address.
    @param HostAddress Host Address URL or DNS */
    public void setHostAddress (String HostAddress)
    {
        if (HostAddress == null) throw new IllegalArgumentException ("HostAddress is mandatory.");
        set_Value ("HostAddress", HostAddress);
        
    }
    
    /** Get Host Address.
    @return Host Address URL or DNS */
    public String getHostAddress() 
    {
        return (String)get_Value("HostAddress");
        
    }
    
    /** Set Host port.
    @param HostPort Host Communication Port */
    public void setHostPort (int HostPort)
    {
        set_Value ("HostPort", Integer.valueOf(HostPort));
        
    }
    
    /** Get Host port.
    @return Host Communication Port */
    public int getHostPort() 
    {
        return get_ValueAsInt("HostPort");
        
    }
    
    /** Set ID Range End.
    @param IDRangeEnd End of the ID Range used */
    public void setIDRangeEnd (java.math.BigDecimal IDRangeEnd)
    {
        set_Value ("IDRangeEnd", IDRangeEnd);
        
    }
    
    /** Get ID Range End.
    @return End of the ID Range used */
    public java.math.BigDecimal getIDRangeEnd() 
    {
        return get_ValueAsBigDecimal("IDRangeEnd");
        
    }
    
    /** Set ID Range Start.
    @param IDRangeStart Start of the ID Range used */
    public void setIDRangeStart (java.math.BigDecimal IDRangeStart)
    {
        set_Value ("IDRangeStart", IDRangeStart);
        
    }
    
    /** Get ID Range Start.
    @return Start of the ID Range used */
    public java.math.BigDecimal getIDRangeStart() 
    {
        return get_ValueAsBigDecimal("IDRangeStart");
        
    }
    
    /** Set Tunnel via HTTP.
    @param IsRMIoverHTTP Connect to Server via HTTP Tunnel */
    public void setIsRMIoverHTTP (boolean IsRMIoverHTTP)
    {
        set_Value ("IsRMIoverHTTP", Boolean.valueOf(IsRMIoverHTTP));
        
    }
    
    /** Get Tunnel via HTTP.
    @return Connect to Server via HTTP Tunnel */
    public boolean isRMIoverHTTP() 
    {
        return get_ValueAsBoolean("IsRMIoverHTTP");
        
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
    
    /** Set Prefix.
    @param Prefix Prefix before the sequence number */
    public void setPrefix (String Prefix)
    {
        set_Value ("Prefix", Prefix);
        
    }
    
    /** Get Prefix.
    @return Prefix before the sequence number */
    public String getPrefix() 
    {
        return (String)get_Value("Prefix");
        
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
    
    /** Set Remote Tenant.
    @param Remote_Client_ID Identifies the Remote Tenant to be used to replicate / synchronize data with. */
    public void setRemote_Client_ID (int Remote_Client_ID)
    {
        if (Remote_Client_ID <= 0) set_ValueNoCheck ("Remote_Client_ID", null);
        else
        set_ValueNoCheck ("Remote_Client_ID", Integer.valueOf(Remote_Client_ID));
        
    }
    
    /** Get Remote Tenant.
    @return Identifies the Remote Tenant to be used to replicate / synchronize data with. */
    public int getRemote_Client_ID() 
    {
        return get_ValueAsInt("Remote_Client_ID");
        
    }
    
    /** Set Remote Organization.
    @param Remote_Org_ID Identifies the Remote Organization to be used to replicate / synchronize data with. */
    public void setRemote_Org_ID (int Remote_Org_ID)
    {
        if (Remote_Org_ID <= 0) set_ValueNoCheck ("Remote_Org_ID", null);
        else
        set_ValueNoCheck ("Remote_Org_ID", Integer.valueOf(Remote_Org_ID));
        
    }
    
    /** Get Remote Organization.
    @return Identifies the Remote Organization to be used to replicate / synchronize data with. */
    public int getRemote_Org_ID() 
    {
        return get_ValueAsInt("Remote_Org_ID");
        
    }
    
    /** Set Suffix.
    @param Suffix Suffix after the number */
    public void setSuffix (String Suffix)
    {
        set_Value ("Suffix", Suffix);
        
    }
    
    /** Get Suffix.
    @return Suffix after the number */
    public String getSuffix() 
    {
        return (String)get_Value("Suffix");
        
    }
    
    
}
