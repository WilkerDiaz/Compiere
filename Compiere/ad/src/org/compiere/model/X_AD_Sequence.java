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
/** Generated Model for AD_Sequence
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_AD_Sequence.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_AD_Sequence extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Sequence_ID id
    @param trx transaction
    */
    public X_AD_Sequence (Ctx ctx, int AD_Sequence_ID, Trx trx)
    {
        super (ctx, AD_Sequence_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Sequence_ID == 0)
        {
            setAD_Sequence_ID (0);
            setCurrentNext (0);	// 1000000
            setCurrentNextSys (0);	// 100
            setIncrementNo (0);	// 1
            setIsAutoSequence (false);
            setIsGapless (false);
            setName (null);
            setStartNo (0);	// 1000000
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Sequence (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27534734092789L;
    /** Last Updated Timestamp 2009-09-10 11:52:56.0 */
    public static final long updatedMS = 1252608776000L;
    /** AD_Table_ID=115 */
    public static final int Table_ID=115;
    
    /** TableName=AD_Sequence */
    public static final String Table_Name="AD_Sequence";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Sequence.
    @param AD_Sequence_ID Document Sequence */
    public void setAD_Sequence_ID (int AD_Sequence_ID)
    {
        if (AD_Sequence_ID < 1) throw new IllegalArgumentException ("AD_Sequence_ID is mandatory.");
        set_ValueNoCheck ("AD_Sequence_ID", Integer.valueOf(AD_Sequence_ID));
        
    }
    
    /** Get Sequence.
    @return Document Sequence */
    public int getAD_Sequence_ID() 
    {
        return get_ValueAsInt("AD_Sequence_ID");
        
    }
    
    /** Set Current Next.
    @param CurrentNext The next number to be used */
    public void setCurrentNext (int CurrentNext)
    {
        set_Value ("CurrentNext", Integer.valueOf(CurrentNext));
        
    }
    
    /** Get Current Next.
    @return The next number to be used */
    public int getCurrentNext() 
    {
        return get_ValueAsInt("CurrentNext");
        
    }
    
    /** Set Current Next (System).
    @param CurrentNextSys Next sequence for system use */
    public void setCurrentNextSys (int CurrentNextSys)
    {
        set_Value ("CurrentNextSys", Integer.valueOf(CurrentNextSys));
        
    }
    
    /** Get Current Next (System).
    @return Next sequence for system use */
    public int getCurrentNextSys() 
    {
        return get_ValueAsInt("CurrentNextSys");
        
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
    
    /** Set Increment.
    @param IncrementNo The number to increment the last document number by */
    public void setIncrementNo (int IncrementNo)
    {
        set_Value ("IncrementNo", Integer.valueOf(IncrementNo));
        
    }
    
    /** Get Increment.
    @return The number to increment the last document number by */
    public int getIncrementNo() 
    {
        return get_ValueAsInt("IncrementNo");
        
    }
    
    /** Set Activate Audit.
    @param IsAudited Activate Audit Trail of what numbers are generated */
    public void setIsAudited (boolean IsAudited)
    {
        set_Value ("IsAudited", Boolean.valueOf(IsAudited));
        
    }
    
    /** Get Activate Audit.
    @return Activate Audit Trail of what numbers are generated */
    public boolean isAudited() 
    {
        return get_ValueAsBoolean("IsAudited");
        
    }
    
    /** Set Auto numbering.
    @param IsAutoSequence Automatically assign the next number */
    public void setIsAutoSequence (boolean IsAutoSequence)
    {
        set_Value ("IsAutoSequence", Boolean.valueOf(IsAutoSequence));
        
    }
    
    /** Get Auto numbering.
    @return Automatically assign the next number */
    public boolean isAutoSequence() 
    {
        return get_ValueAsBoolean("IsAutoSequence");
        
    }
    
    /** Set Gapless.
    @param IsGapless Whether the sequence is gapless */
    public void setIsGapless (boolean IsGapless)
    {
        set_Value ("IsGapless", Boolean.valueOf(IsGapless));
        
    }
    
    /** Get Gapless.
    @return Whether the sequence is gapless */
    public boolean isGapless() 
    {
        return get_ValueAsBoolean("IsGapless");
        
    }
    
    /** Set Used for Record ID.
    @param IsTableID The document number will be used as the record key */
    public void setIsTableID (boolean IsTableID)
    {
        set_Value ("IsTableID", Boolean.valueOf(IsTableID));
        
    }
    
    /** Get Used for Record ID.
    @return The document number will be used as the record key */
    public boolean isTableID() 
    {
        return get_ValueAsBoolean("IsTableID");
        
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
    
    /** Set Restart sequence every Year.
    @param StartNewYear Restart the sequence with Start on every 1/1 */
    public void setStartNewYear (boolean StartNewYear)
    {
        set_Value ("StartNewYear", Boolean.valueOf(StartNewYear));
        
    }
    
    /** Get Restart sequence every Year.
    @return Restart the sequence with Start on every 1/1 */
    public boolean isStartNewYear() 
    {
        return get_ValueAsBoolean("StartNewYear");
        
    }
    
    /** Set Start No.
    @param StartNo Starting number/position */
    public void setStartNo (int StartNo)
    {
        set_Value ("StartNo", Integer.valueOf(StartNo));
        
    }
    
    /** Get Start No.
    @return Starting number/position */
    public int getStartNo() 
    {
        return get_ValueAsInt("StartNo");
        
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
    
    /** Set Value Format.
    @param VFormat Format of the value;
     Can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public void setVFormat (String VFormat)
    {
        set_Value ("VFormat", VFormat);
        
    }
    
    /** Get Value Format.
    @return Format of the value;
     Can contain fixed format elements, Variables: "_lLoOaAcCa09" */
    public String getVFormat() 
    {
        return (String)get_Value("VFormat");
        
    }
    
    
}
