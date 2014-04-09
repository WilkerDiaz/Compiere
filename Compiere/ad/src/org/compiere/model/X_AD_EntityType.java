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
/** Generated Model for AD_EntityType
 *  @author Jorg Janke (generated) 
 *  @version Release 3.7.0 - $Id: X_AD_EntityType.java 9228 2010-09-22 19:07:22Z kvora $ */
public class X_AD_EntityType extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_EntityType_ID id
    @param trx transaction
    */
    public X_AD_EntityType (Ctx ctx, int AD_EntityType_ID, Trx trx)
    {
        super (ctx, AD_EntityType_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_EntityType_ID == 0)
        {
            setAD_EntityType_ID (0);	// @SQL=SELECT NVL(MAX(AD_EntityType_ID),999999)+1 FROM AD_EntityType WHERE AD_EntityType_ID > 1000
            setEntityType (null);	// U
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_EntityType (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567306587789L;
    /** Last Updated Timestamp 2010-09-22 11:47:51.0 */
    public static final long updatedMS = 1285181271000L;
    /** AD_Table_ID=882 */
    public static final int Table_ID=882;
    
    /** TableName=AD_EntityType */
    public static final String Table_Name="AD_EntityType";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set System Entity Type.
    @param AD_EntityType_ID System Entity Type */
    public void setAD_EntityType_ID (int AD_EntityType_ID)
    {
        if (AD_EntityType_ID < 1) throw new IllegalArgumentException ("AD_EntityType_ID is mandatory.");
        set_ValueNoCheck ("AD_EntityType_ID", Integer.valueOf(AD_EntityType_ID));
        
    }
    
    /** Get System Entity Type.
    @return System Entity Type */
    public int getAD_EntityType_ID() 
    {
        return get_ValueAsInt("AD_EntityType_ID");
        
    }
    
    /** Set BinaryData.
    @param BinaryData Binary Data */
    public void setBinaryData (byte[] BinaryData)
    {
        set_Value ("BinaryData", BinaryData);
        
    }
    
    /** Get BinaryData.
    @return Binary Data */
    public byte[] getBinaryData() 
    {
        return (byte[])get_Value("BinaryData");
        
    }
    
    /** Set Classpath.
    @param Classpath Extension Classpath */
    public void setClasspath (String Classpath)
    {
        set_Value ("Classpath", Classpath);
        
    }
    
    /** Get Classpath.
    @return Extension Classpath */
    public String getClasspath() 
    {
        return (String)get_Value("Classpath");
        
    }
    
    /** Set Create Component.
    @param CreateComponent Create Component */
    public void setCreateComponent (String CreateComponent)
    {
        set_Value ("CreateComponent", CreateComponent);
        
    }
    
    /** Get Create Component.
    @return Create Component */
    public String getCreateComponent() 
    {
        return (String)get_Value("CreateComponent");
        
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
    
    /** Business Partner (User) = B */
    public static final String DISTRIBUTIONTYPE_BusinessPartnerUser = X_Ref_AD_Component_Distribution.BUSINESS_PARTNER_USER.getValue();
    /** Free - Unlimited = F */
    public static final String DISTRIBUTIONTYPE_Free_Unlimited = X_Ref_AD_Component_Distribution.FREE__UNLIMITED.getValue();
    /** License = L */
    public static final String DISTRIBUTIONTYPE_License = X_Ref_AD_Component_Distribution.LICENSE.getValue();
    /** Business Partner Relation = R */
    public static final String DISTRIBUTIONTYPE_BusinessPartnerRelation = X_Ref_AD_Component_Distribution.BUSINESS_PARTNER_RELATION.getValue();
    /** Set Distribution Type.
    @param DistributionType Type of Distribution */
    public void setDistributionType (String DistributionType)
    {
        if (!X_Ref_AD_Component_Distribution.isValid(DistributionType))
        throw new IllegalArgumentException ("DistributionType Invalid value - " + DistributionType + " - Reference_ID=438 - B - F - L - R");
        set_ValueNoCheck ("DistributionType", DistributionType);
        
    }
    
    /** Get Distribution Type.
    @return Type of Distribution */
    public String getDistributionType() 
    {
        return (String)get_Value("DistributionType");
        
    }
    
    /** Set Documentation Text.
    @param DocumentationText Documentation */
    public void setDocumentationText (String DocumentationText)
    {
        set_Value ("DocumentationText", DocumentationText);
        
    }
    
    /** Get Documentation Text.
    @return Documentation */
    public String getDocumentationText() 
    {
        return (String)get_Value("DocumentationText");
        
    }
    
    /** Set Entity Type.
    @param EntityType Dictionary Entity Type;
     Determines ownership and synchronization */
    public void setEntityType (String EntityType)
    {
        if (EntityType == null) throw new IllegalArgumentException ("EntityType is mandatory.");
        set_ValueNoCheck ("EntityType", EntityType);
        
    }
    
    /** Get Entity Type.
    @return Dictionary Entity Type;
     Determines ownership and synchronization */
    public String getEntityType() 
    {
        return (String)get_Value("EntityType");
        
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
    
    /** Set Registered.
    @param IsRegistered The application is registered. */
    public void setIsRegistered (boolean IsRegistered)
    {
        set_ValueNoCheck ("IsRegistered", Boolean.valueOf(IsRegistered));
        
    }
    
    /** Get Registered.
    @return The application is registered. */
    public boolean isRegistered() 
    {
        return get_ValueAsBoolean("IsRegistered");
        
    }
    
    /** Set License Text.
    @param LicenseText Text of the License of the Component */
    public void setLicenseText (String LicenseText)
    {
        set_Value ("LicenseText", LicenseText);
        
    }
    
    /** Get License Text.
    @return Text of the License of the Component */
    public String getLicenseText() 
    {
        return (String)get_Value("LicenseText");
        
    }
    
    /** Set ModelPackage.
    @param ModelPackage Java Package of the model classes */
    public void setModelPackage (String ModelPackage)
    {
        set_Value ("ModelPackage", ModelPackage);
        
    }
    
    /** Get ModelPackage.
    @return Java Package of the model classes */
    public String getModelPackage() 
    {
        return (String)get_Value("ModelPackage");
        
    }
    
    /** Set Model Validation Classes.
    @param ModelValidationClasses List of data model validation classes separated by ;
     */
    public void setModelValidationClasses (String ModelValidationClasses)
    {
        set_Value ("ModelValidationClasses", ModelValidationClasses);
        
    }
    
    /** Get Model Validation Classes.
    @return List of data model validation classes separated by ;
     */
    public String getModelValidationClasses() 
    {
        return (String)get_Value("ModelValidationClasses");
        
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
    
    /** Set Record ID.
    @param Record_ID Direct internal record ID */
    public void setRecord_ID (int Record_ID)
    {
        if (Record_ID <= 0) set_ValueNoCheck ("Record_ID", null);
        else
        set_ValueNoCheck ("Record_ID", Integer.valueOf(Record_ID));
        
    }
    
    /** Get Record ID.
    @return Direct internal record ID */
    public int getRecord_ID() 
    {
        return get_ValueAsInt("Record_ID");
        
    }
    
    /** Set Load Sequence.
    @param RelativeLoadNo Relative Load Sequence */
    public void setRelativeLoadNo (int RelativeLoadNo)
    {
        set_Value ("RelativeLoadNo", Integer.valueOf(RelativeLoadNo));
        
    }
    
    /** Get Load Sequence.
    @return Relative Load Sequence */
    public int getRelativeLoadNo() 
    {
        return get_ValueAsInt("RelativeLoadNo");
        
    }
    
    /** Set Prerequisite Version.
    @param RequireCompiereVersion Prerequisite Compiere Base Version */
    public void setRequireCompiereVersion (String RequireCompiereVersion)
    {
        set_Value ("RequireCompiereVersion", RequireCompiereVersion);
        
    }
    
    /** Get Prerequisite Version.
    @return Prerequisite Compiere Base Version */
    public String getRequireCompiereVersion() 
    {
        return (String)get_Value("RequireCompiereVersion");
        
    }
    
    /** Set Prerequisite Applications.
    @param RequireComponentVersion Prerequisite Applications with optional Version */
    public void setRequireComponentVersion (String RequireComponentVersion)
    {
        set_Value ("RequireComponentVersion", RequireComponentVersion);
        
    }
    
    /** Get Prerequisite Applications.
    @return Prerequisite Applications with optional Version */
    public String getRequireComponentVersion() 
    {
        return (String)get_Value("RequireComponentVersion");
        
    }
    
    /** Set Summary.
    @param Summary Textual summary of this request */
    public void setSummary (String Summary)
    {
        set_Value ("Summary", Summary);
        
    }
    
    /** Get Summary.
    @return Textual summary of this request */
    public String getSummary() 
    {
        return (String)get_Value("Summary");
        
    }
    
    /** Set Version.
    @param Version Version of the table definition */
    public void setVersion (String Version)
    {
        set_Value ("Version", Version);
        
    }
    
    /** Get Version.
    @return Version of the table definition */
    public String getVersion() 
    {
        return (String)get_Value("Version");
        
    }
    
    
}
