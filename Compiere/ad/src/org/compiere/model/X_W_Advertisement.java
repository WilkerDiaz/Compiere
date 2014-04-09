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
/** Generated Model for W_Advertisement
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_W_Advertisement.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_W_Advertisement extends PO
{
    /** Standard Constructor
    @param ctx context
    @param W_Advertisement_ID id
    @param trx transaction
    */
    public X_W_Advertisement (Ctx ctx, int W_Advertisement_ID, Trx trx)
    {
        super (ctx, W_Advertisement_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (W_Advertisement_ID == 0)
        {
            setC_BPartner_ID (0);
            setIsSelfService (true);	// Y
            setName (null);
            setPublishStatus (null);	// U
            setW_Advertisement_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_W_Advertisement (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=579 */
    public static final int Table_ID=579;
    
    /** TableName=W_Advertisement */
    public static final String Table_Name="W_Advertisement";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set User/Contact.
    @param AD_User_ID User within the system - Internal or Business Partner Contact */
    public void setAD_User_ID (int AD_User_ID)
    {
        if (AD_User_ID <= 0) set_Value ("AD_User_ID", null);
        else
        set_Value ("AD_User_ID", Integer.valueOf(AD_User_ID));
        
    }
    
    /** Get User/Contact.
    @return User within the system - Internal or Business Partner Contact */
    public int getAD_User_ID() 
    {
        return get_ValueAsInt("AD_User_ID");
        
    }
    
    /** Set Advertisement Text.
    @param AdText Text of the Advertisement */
    public void setAdText (String AdText)
    {
        set_Value ("AdText", AdText);
        
    }
    
    /** Get Advertisement Text.
    @return Text of the Advertisement */
    public String getAdText() 
    {
        return (String)get_Value("AdText");
        
    }
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
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
    
    /** Set Image URL.
    @param ImageURL URL of image */
    public void setImageURL (String ImageURL)
    {
        set_Value ("ImageURL", ImageURL);
        
    }
    
    /** Get Image URL.
    @return URL of image */
    public String getImageURL() 
    {
        return (String)get_Value("ImageURL");
        
    }
    
    /** Set Self-Service.
    @param IsSelfService This is a Self-Service entry or this entry can be changed via Self-Service */
    public void setIsSelfService (boolean IsSelfService)
    {
        set_Value ("IsSelfService", Boolean.valueOf(IsSelfService));
        
    }
    
    /** Get Self-Service.
    @return This is a Self-Service entry or this entry can be changed via Self-Service */
    public boolean isSelfService() 
    {
        return get_ValueAsBoolean("IsSelfService");
        
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
    
    /** Released = R */
    public static final String PUBLISHSTATUS_Released = X_Ref__PublishStatus.RELEASED.getValue();
    /** Test = T */
    public static final String PUBLISHSTATUS_Test = X_Ref__PublishStatus.TEST.getValue();
    /** Under Revision = U */
    public static final String PUBLISHSTATUS_UnderRevision = X_Ref__PublishStatus.UNDER_REVISION.getValue();
    /** Void = V */
    public static final String PUBLISHSTATUS_Void = X_Ref__PublishStatus.VOID.getValue();
    /** Set Publication Status.
    @param PublishStatus Status of Publication */
    public void setPublishStatus (String PublishStatus)
    {
        if (PublishStatus == null) throw new IllegalArgumentException ("PublishStatus is mandatory");
        if (!X_Ref__PublishStatus.isValid(PublishStatus))
        throw new IllegalArgumentException ("PublishStatus Invalid value - " + PublishStatus + " - Reference_ID=310 - R - T - U - V");
        set_Value ("PublishStatus", PublishStatus);
        
    }
    
    /** Get Publication Status.
    @return Status of Publication */
    public String getPublishStatus() 
    {
        return (String)get_Value("PublishStatus");
        
    }
    
    /** Set Valid from.
    @param ValidFrom Valid from including this date (first day) */
    public void setValidFrom (Timestamp ValidFrom)
    {
        set_Value ("ValidFrom", ValidFrom);
        
    }
    
    /** Get Valid from.
    @return Valid from including this date (first day) */
    public Timestamp getValidFrom() 
    {
        return (Timestamp)get_Value("ValidFrom");
        
    }
    
    /** Set Valid to.
    @param ValidTo Valid to including this date (last day) */
    public void setValidTo (Timestamp ValidTo)
    {
        set_Value ("ValidTo", ValidTo);
        
    }
    
    /** Get Valid to.
    @return Valid to including this date (last day) */
    public Timestamp getValidTo() 
    {
        return (Timestamp)get_Value("ValidTo");
        
    }
    
    /** Set Version.
    @param Version Version of the table definition */
    public void setVersion (int Version)
    {
        set_Value ("Version", Integer.valueOf(Version));
        
    }
    
    /** Get Version.
    @return Version of the table definition */
    public int getVersion() 
    {
        return get_ValueAsInt("Version");
        
    }
    
    /** Set Advertisement.
    @param W_Advertisement_ID Web Advertisement */
    public void setW_Advertisement_ID (int W_Advertisement_ID)
    {
        if (W_Advertisement_ID < 1) throw new IllegalArgumentException ("W_Advertisement_ID is mandatory.");
        set_ValueNoCheck ("W_Advertisement_ID", Integer.valueOf(W_Advertisement_ID));
        
    }
    
    /** Get Advertisement.
    @return Web Advertisement */
    public int getW_Advertisement_ID() 
    {
        return get_ValueAsInt("W_Advertisement_ID");
        
    }
    
    /** Set Click Count.
    @param W_ClickCount_ID Web Click Management */
    public void setW_ClickCount_ID (int W_ClickCount_ID)
    {
        if (W_ClickCount_ID <= 0) set_Value ("W_ClickCount_ID", null);
        else
        set_Value ("W_ClickCount_ID", Integer.valueOf(W_ClickCount_ID));
        
    }
    
    /** Get Click Count.
    @return Web Click Management */
    public int getW_ClickCount_ID() 
    {
        return get_ValueAsInt("W_ClickCount_ID");
        
    }
    
    /** Set Counter Count.
    @param W_CounterCount_ID Web Counter Count Management */
    public void setW_CounterCount_ID (int W_CounterCount_ID)
    {
        if (W_CounterCount_ID <= 0) set_Value ("W_CounterCount_ID", null);
        else
        set_Value ("W_CounterCount_ID", Integer.valueOf(W_CounterCount_ID));
        
    }
    
    /** Get Counter Count.
    @return Web Counter Count Management */
    public int getW_CounterCount_ID() 
    {
        return get_ValueAsInt("W_CounterCount_ID");
        
    }
    
    /** Set Web Parameter 1.
    @param WebParam1 Web Site Parameter 1 (default: header image) */
    public void setWebParam1 (String WebParam1)
    {
        set_Value ("WebParam1", WebParam1);
        
    }
    
    /** Get Web Parameter 1.
    @return Web Site Parameter 1 (default: header image) */
    public String getWebParam1() 
    {
        return (String)get_Value("WebParam1");
        
    }
    
    /** Set Web Parameter 2.
    @param WebParam2 Web Site Parameter 2 (default index page) */
    public void setWebParam2 (String WebParam2)
    {
        set_Value ("WebParam2", WebParam2);
        
    }
    
    /** Get Web Parameter 2.
    @return Web Site Parameter 2 (default index page) */
    public String getWebParam2() 
    {
        return (String)get_Value("WebParam2");
        
    }
    
    /** Set Web Parameter 3.
    @param WebParam3 Web Site Parameter 3 (default left - menu) */
    public void setWebParam3 (String WebParam3)
    {
        set_Value ("WebParam3", WebParam3);
        
    }
    
    /** Get Web Parameter 3.
    @return Web Site Parameter 3 (default left - menu) */
    public String getWebParam3() 
    {
        return (String)get_Value("WebParam3");
        
    }
    
    /** Set Web Parameter 4.
    @param WebParam4 Web Site Parameter 4 (default footer left) */
    public void setWebParam4 (String WebParam4)
    {
        set_Value ("WebParam4", WebParam4);
        
    }
    
    /** Get Web Parameter 4.
    @return Web Site Parameter 4 (default footer left) */
    public String getWebParam4() 
    {
        return (String)get_Value("WebParam4");
        
    }
    
    
}
