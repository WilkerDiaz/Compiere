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
/** Generated Model for AD_Role
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.1 - $Id: X_AD_Role.java 8593 2010-03-29 21:38:25Z nnayak $ */
public class X_AD_Role extends PO
{
    /** Standard Constructor
    @param ctx context
    @param AD_Role_ID id
    @param trx transaction
    */
    public X_AD_Role (Ctx ctx, int AD_Role_ID, Trx trx)
    {
        super (ctx, AD_Role_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (AD_Role_ID == 0)
        {
            setAD_Role_ID (0);
            setConfirmQueryRecords (0);	// 0
            setDisplayColumns (0);	// 2
            setIsAccessAllOrgs (false);	// N
            setIsAdministrator (false);	// N
            setIsCanApproveOwnDoc (false);
            setIsCanExport (true);	// Y
            setIsCanReport (true);	// Y
            setIsChangeLog (false);	// N
            setIsEnableCreateUpdateBP (true);	// Y
            setIsManual (false);
            setIsPersonalAccess (false);	// N
            setIsPersonalLock (false);	// N
            setIsShowAcct (false);	// N
            setIsUseBPRestrictions (false);	// N
            setIsUseUserOrgAccess (false);	// N
            setMaxQueryRecords (0);	// 0
            setName (null);
            setOverrideReturnPolicy (false);	// N
            setOverwritePriceLimit (false);	// N
            setPreferenceType (null);	// O
            setUserLevel (null);	// '  O'
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_AD_Role (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27552025014789L;
    /** Last Updated Timestamp 2010-03-29 14:54:58.0 */
    public static final long updatedMS = 1269899698000L;
    /** AD_Table_ID=156 */
    public static final int Table_ID=156;
    
    /** TableName=AD_Role */
    public static final String Table_Name="AD_Role";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Role.
    @param AD_Role_ID Responsibility Role */
    public void setAD_Role_ID (int AD_Role_ID)
    {
        if (AD_Role_ID < 0) throw new IllegalArgumentException ("AD_Role_ID is mandatory.");
        set_ValueNoCheck ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set Menu Tree.
    @param AD_Tree_Menu_ID Tree of the menu */
    public void setAD_Tree_Menu_ID (int AD_Tree_Menu_ID)
    {
        if (AD_Tree_Menu_ID <= 0) set_Value ("AD_Tree_Menu_ID", null);
        else
        set_Value ("AD_Tree_Menu_ID", Integer.valueOf(AD_Tree_Menu_ID));
        
    }
    
    /** Get Menu Tree.
    @return Tree of the menu */
    public int getAD_Tree_Menu_ID() 
    {
        return get_ValueAsInt("AD_Tree_Menu_ID");
        
    }
    
    /** Set Organization Tree.
    @param AD_Tree_Org_ID Tree to determine organizational hierarchy */
    public void setAD_Tree_Org_ID (int AD_Tree_Org_ID)
    {
        if (AD_Tree_Org_ID <= 0) set_Value ("AD_Tree_Org_ID", null);
        else
        set_Value ("AD_Tree_Org_ID", Integer.valueOf(AD_Tree_Org_ID));
        
    }
    
    /** Get Organization Tree.
    @return Tree to determine organizational hierarchy */
    public int getAD_Tree_Org_ID() 
    {
        return get_ValueAsInt("AD_Tree_Org_ID");
        
    }
    
    /** Set Approval Amount.
    @param AmtApproval The approval amount limit for this role */
    public void setAmtApproval (java.math.BigDecimal AmtApproval)
    {
        set_Value ("AmtApproval", AmtApproval);
        
    }
    
    /** Get Approval Amount.
    @return The approval amount limit for this role */
    public java.math.BigDecimal getAmtApproval() 
    {
        return get_ValueAsBigDecimal("AmtApproval");
        
    }
    
    /** Set Currency.
    @param C_Currency_ID The Currency for this record */
    public void setC_Currency_ID (int C_Currency_ID)
    {
        if (C_Currency_ID <= 0) set_Value ("C_Currency_ID", null);
        else
        set_Value ("C_Currency_ID", Integer.valueOf(C_Currency_ID));
        
    }
    
    /** Get Currency.
    @return The Currency for this record */
    public int getC_Currency_ID() 
    {
        return get_ValueAsInt("C_Currency_ID");
        
    }
    
    /** Set Confirm Query Records.
    @param ConfirmQueryRecords Require Confirmation if more records will be returned by the query (If not defined 500) */
    public void setConfirmQueryRecords (int ConfirmQueryRecords)
    {
        set_Value ("ConfirmQueryRecords", Integer.valueOf(ConfirmQueryRecords));
        
    }
    
    /** Get Confirm Query Records.
    @return Require Confirmation if more records will be returned by the query (If not defined 500) */
    public int getConfirmQueryRecords() 
    {
        return get_ValueAsInt("ConfirmQueryRecords");
        
    }
    
    /** LAN = L */
    public static final String CONNECTIONPROFILE_LAN = X_Ref_AD_User_ConnectionProfile.LAN.getValue();
    /** Terminal Server = T */
    public static final String CONNECTIONPROFILE_TerminalServer = X_Ref_AD_User_ConnectionProfile.TERMINAL_SERVER.getValue();
    /** VPN = V */
    public static final String CONNECTIONPROFILE_VPN = X_Ref_AD_User_ConnectionProfile.VPN.getValue();
    /** WAN = W */
    public static final String CONNECTIONPROFILE_WAN = X_Ref_AD_User_ConnectionProfile.WAN.getValue();
    /** Set Connection Profile.
    @param ConnectionProfile How a Java Client connects to the server(s) */
    public void setConnectionProfile (String ConnectionProfile)
    {
        if (!X_Ref_AD_User_ConnectionProfile.isValid(ConnectionProfile))
        throw new IllegalArgumentException ("ConnectionProfile Invalid value - " + ConnectionProfile + " - Reference_ID=364 - L - T - V - W");
        set_Value ("ConnectionProfile", ConnectionProfile);
        
    }
    
    /** Get Connection Profile.
    @return How a Java Client connects to the server(s) */
    public String getConnectionProfile() 
    {
        return (String)get_Value("ConnectionProfile");
        
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
    
    /** Always Tenant, Organization = A */
    public static final String DISPLAYCLIENTORG_AlwaysTenantOrganization = X_Ref_AD_Role_DisplayClientOrg.ALWAYS_TENANT_ORGANIZATION.getValue();
    /** Neither Tenant nor Org = N */
    public static final String DISPLAYCLIENTORG_NeitherTenantNorOrg = X_Ref_AD_Role_DisplayClientOrg.NEITHER_TENANT_NOR_ORG.getValue();
    /** Only Organization = O */
    public static final String DISPLAYCLIENTORG_OnlyOrganization = X_Ref_AD_Role_DisplayClientOrg.ONLY_ORGANIZATION.getValue();
    /** Set Display.
    @param DisplayClientOrg Display Policy for Tenant and Organization */
    public void setDisplayClientOrg (String DisplayClientOrg)
    {
        if (!X_Ref_AD_Role_DisplayClientOrg.isValid(DisplayClientOrg))
        throw new IllegalArgumentException ("DisplayClientOrg Invalid value - " + DisplayClientOrg + " - Reference_ID=427 - A - N - O");
        set_Value ("DisplayClientOrg", DisplayClientOrg);
        
    }
    
    /** Get Display.
    @return Display Policy for Tenant and Organization */
    public String getDisplayClientOrg() 
    {
        return (String)get_Value("DisplayClientOrg");
        
    }
    
    /** Set Display Columns.
    @param DisplayColumns No of columns for displaying dashboards on the home page */
    public void setDisplayColumns (int DisplayColumns)
    {
        set_Value ("DisplayColumns", Integer.valueOf(DisplayColumns));
        
    }
    
    /** Get Display Columns.
    @return No of columns for displaying dashboards on the home page */
    public int getDisplayColumns() 
    {
        return get_ValueAsInt("DisplayColumns");
        
    }
    
    /** Set Access all Orgs.
    @param IsAccessAllOrgs Access all Organizations (no org access control) of the client */
    public void setIsAccessAllOrgs (boolean IsAccessAllOrgs)
    {
        set_Value ("IsAccessAllOrgs", Boolean.valueOf(IsAccessAllOrgs));
        
    }
    
    /** Get Access all Orgs.
    @return Access all Organizations (no org access control) of the client */
    public boolean isAccessAllOrgs() 
    {
        return get_ValueAsBoolean("IsAccessAllOrgs");
        
    }
    
    /** Set Administrator.
    @param IsAdministrator This is an administrator role */
    public void setIsAdministrator (boolean IsAdministrator)
    {
        set_Value ("IsAdministrator", Boolean.valueOf(IsAdministrator));
        
    }
    
    /** Get Administrator.
    @return This is an administrator role */
    public boolean isAdministrator() 
    {
        return get_ValueAsBoolean("IsAdministrator");
        
    }
    
    /** Set Approve own Documents.
    @param IsCanApproveOwnDoc Users with this role can approve their own documents */
    public void setIsCanApproveOwnDoc (boolean IsCanApproveOwnDoc)
    {
        set_Value ("IsCanApproveOwnDoc", Boolean.valueOf(IsCanApproveOwnDoc));
        
    }
    
    /** Get Approve own Documents.
    @return Users with this role can approve their own documents */
    public boolean isCanApproveOwnDoc() 
    {
        return get_ValueAsBoolean("IsCanApproveOwnDoc");
        
    }
    
    /** Set Can Export.
    @param IsCanExport Users with this role can export data */
    public void setIsCanExport (boolean IsCanExport)
    {
        set_Value ("IsCanExport", Boolean.valueOf(IsCanExport));
        
    }
    
    /** Get Can Export.
    @return Users with this role can export data */
    public boolean isCanExport() 
    {
        return get_ValueAsBoolean("IsCanExport");
        
    }
    
    /** Set Can Report.
    @param IsCanReport Users with this role can create reports */
    public void setIsCanReport (boolean IsCanReport)
    {
        set_Value ("IsCanReport", Boolean.valueOf(IsCanReport));
        
    }
    
    /** Get Can Report.
    @return Users with this role can create reports */
    public boolean isCanReport() 
    {
        return get_ValueAsBoolean("IsCanReport");
        
    }
    
    /** Set Maintain Change Log.
    @param IsChangeLog Maintain a log of changes */
    public void setIsChangeLog (boolean IsChangeLog)
    {
        set_Value ("IsChangeLog", Boolean.valueOf(IsChangeLog));
        
    }
    
    /** Get Maintain Change Log.
    @return Maintain a log of changes */
    public boolean isChangeLog() 
    {
        return get_ValueAsBoolean("IsChangeLog");
        
    }
    
    /** Set Enable BP Quick Entry Menus.
    @param IsEnableCreateUpdateBP Enable Create New/Update Business Partner quick entry menus on lookups */
    public void setIsEnableCreateUpdateBP (boolean IsEnableCreateUpdateBP)
    {
        set_Value ("IsEnableCreateUpdateBP", Boolean.valueOf(IsEnableCreateUpdateBP));
        
    }
    
    /** Get Enable BP Quick Entry Menus.
    @return Enable Create New/Update Business Partner quick entry menus on lookups */
    public boolean isEnableCreateUpdateBP() 
    {
        return get_ValueAsBoolean("IsEnableCreateUpdateBP");
        
    }
    
    /** Set Manual.
    @param IsManual This is a manual process or entry */
    public void setIsManual (boolean IsManual)
    {
        set_Value ("IsManual", Boolean.valueOf(IsManual));
        
    }
    
    /** Get Manual.
    @return This is a manual process or entry */
    public boolean isManual() 
    {
        return get_ValueAsBoolean("IsManual");
        
    }
    
    /** Set Personal Access.
    @param IsPersonalAccess Allow access to all personal records */
    public void setIsPersonalAccess (boolean IsPersonalAccess)
    {
        set_Value ("IsPersonalAccess", Boolean.valueOf(IsPersonalAccess));
        
    }
    
    /** Get Personal Access.
    @return Allow access to all personal records */
    public boolean isPersonalAccess() 
    {
        return get_ValueAsBoolean("IsPersonalAccess");
        
    }
    
    /** Set Personal Lock.
    @param IsPersonalLock Allow users with role to lock access to personal records */
    public void setIsPersonalLock (boolean IsPersonalLock)
    {
        set_Value ("IsPersonalLock", Boolean.valueOf(IsPersonalLock));
        
    }
    
    /** Get Personal Lock.
    @return Allow users with role to lock access to personal records */
    public boolean isPersonalLock() 
    {
        return get_ValueAsBoolean("IsPersonalLock");
        
    }
    
    /** Set Show Accounting.
    @param IsShowAcct Users with this role can see accounting information */
    public void setIsShowAcct (boolean IsShowAcct)
    {
        set_Value ("IsShowAcct", Boolean.valueOf(IsShowAcct));
        
    }
    
    /** Get Show Accounting.
    @return Users with this role can see accounting information */
    public boolean isShowAcct() 
    {
        return get_ValueAsBoolean("IsShowAcct");
        
    }
    
    /** Set Use BP Restrictions.
    @param IsUseBPRestrictions Restrict users to only Business Partners they are associated with */
    public void setIsUseBPRestrictions (boolean IsUseBPRestrictions)
    {
        set_Value ("IsUseBPRestrictions", Boolean.valueOf(IsUseBPRestrictions));
        
    }
    
    /** Get Use BP Restrictions.
    @return Restrict users to only Business Partners they are associated with */
    public boolean isUseBPRestrictions() 
    {
        return get_ValueAsBoolean("IsUseBPRestrictions");
        
    }
    
    /** Set Use User Org Access.
    @param IsUseUserOrgAccess Use Org Access defined by user instead of Role Org Access */
    public void setIsUseUserOrgAccess (boolean IsUseUserOrgAccess)
    {
        set_Value ("IsUseUserOrgAccess", Boolean.valueOf(IsUseUserOrgAccess));
        
    }
    
    /** Get Use User Org Access.
    @return Use Org Access defined by user instead of Role Org Access */
    public boolean isUseUserOrgAccess() 
    {
        return get_ValueAsBoolean("IsUseUserOrgAccess");
        
    }
    
    /** Set Max Query Records.
    @param MaxQueryRecords If defined, you cannot query more records as defined - the query criteria needs to be changed to query less records */
    public void setMaxQueryRecords (int MaxQueryRecords)
    {
        set_Value ("MaxQueryRecords", Integer.valueOf(MaxQueryRecords));
        
    }
    
    /** Get Max Query Records.
    @return If defined, you cannot query more records as defined - the query criteria needs to be changed to query less records */
    public int getMaxQueryRecords() 
    {
        return get_ValueAsInt("MaxQueryRecords");
        
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
    
    /** Set Override Return Policy.
    @param OverrideReturnPolicy Override Return Policy if the policy timeframe is exceeded */
    public void setOverrideReturnPolicy (boolean OverrideReturnPolicy)
    {
        set_Value ("OverrideReturnPolicy", Boolean.valueOf(OverrideReturnPolicy));
        
    }
    
    /** Get Override Return Policy.
    @return Override Return Policy if the policy timeframe is exceeded */
    public boolean isOverrideReturnPolicy() 
    {
        return get_ValueAsBoolean("OverrideReturnPolicy");
        
    }
    
    /** Set Overwrite Price Limit.
    @param OverwritePriceLimit Overwrite Price Limit if the Price List enforces the Price Limit */
    public void setOverwritePriceLimit (boolean OverwritePriceLimit)
    {
        set_Value ("OverwritePriceLimit", Boolean.valueOf(OverwritePriceLimit));
        
    }
    
    /** Get Overwrite Price Limit.
    @return Overwrite Price Limit if the Price List enforces the Price Limit */
    public boolean isOverwritePriceLimit() 
    {
        return get_ValueAsBoolean("OverwritePriceLimit");
        
    }
    
    /** Tenant = C */
    public static final String PREFERENCETYPE_Tenant = X_Ref_AD_Role_PreferenceType.TENANT.getValue();
    /** None = N */
    public static final String PREFERENCETYPE_None = X_Ref_AD_Role_PreferenceType.NONE.getValue();
    /** Organization = O */
    public static final String PREFERENCETYPE_Organization = X_Ref_AD_Role_PreferenceType.ORGANIZATION.getValue();
    /** User = U */
    public static final String PREFERENCETYPE_User = X_Ref_AD_Role_PreferenceType.USER.getValue();
    /** Set Preference Level.
    @param PreferenceType Determines what preferences the user can set */
    public void setPreferenceType (String PreferenceType)
    {
        if (PreferenceType == null) throw new IllegalArgumentException ("PreferenceType is mandatory");
        if (!X_Ref_AD_Role_PreferenceType.isValid(PreferenceType))
        throw new IllegalArgumentException ("PreferenceType Invalid value - " + PreferenceType + " - Reference_ID=330 - C - N - O - U");
        set_Value ("PreferenceType", PreferenceType);
        
    }
    
    /** Get Preference Level.
    @return Determines what preferences the user can set */
    public String getPreferenceType() 
    {
        return (String)get_Value("PreferenceType");
        
    }
    
    /** Set Supervisor.
    @param Supervisor_ID Supervisor for this user/organization - used for escalation and approval */
    public void setSupervisor_ID (int Supervisor_ID)
    {
        if (Supervisor_ID <= 0) set_Value ("Supervisor_ID", null);
        else
        set_Value ("Supervisor_ID", Integer.valueOf(Supervisor_ID));
        
    }
    
    /** Get Supervisor.
    @return Supervisor for this user/organization - used for escalation and approval */
    public int getSupervisor_ID() 
    {
        return get_ValueAsInt("Supervisor_ID");
        
    }
    
    /** Organization =   O */
    public static final String USERLEVEL_Organization = X_Ref_AD_Role_User_Level.ORGANIZATION.getValue();
    /** Tenant =  C  */
    public static final String USERLEVEL_Tenant = X_Ref_AD_Role_User_Level.TENANT.getValue();
    /** Tenant+Organization =  CO */
    public static final String USERLEVEL_TenantPlusOrganization = X_Ref_AD_Role_User_Level.TENANT_PLUS_ORGANIZATION.getValue();
    /** System = S   */
    public static final String USERLEVEL_System = X_Ref_AD_Role_User_Level.SYSTEM.getValue();
    /** Set User Level.
    @param UserLevel System Client Organization */
    public void setUserLevel (String UserLevel)
    {
        if (UserLevel == null) throw new IllegalArgumentException ("UserLevel is mandatory");
        if (!X_Ref_AD_Role_User_Level.isValid(UserLevel))
        throw new IllegalArgumentException ("UserLevel Invalid value - " + UserLevel + " - Reference_ID=226 -   O -  C  -  CO - S  ");
        set_Value ("UserLevel", UserLevel);
        
    }
    
    /** Get User Level.
    @return System Client Organization */
    public String getUserLevel() 
    {
        return (String)get_Value("UserLevel");
        
    }
    
    /** Tenant (or Role or User) = C */
    public static final String WINUSERDEFLEVEL_TenantOrRoleOrUser = X_Ref_AD_Role_WinUserDefLevel.TENANT_OR_ROLE_OR_USER.getValue();
    /** None = N */
    public static final String WINUSERDEFLEVEL_None = X_Ref_AD_Role_WinUserDefLevel.NONE.getValue();
    /** Role (or User) = R */
    public static final String WINUSERDEFLEVEL_RoleOrUser = X_Ref_AD_Role_WinUserDefLevel.ROLE_OR_USER.getValue();
    /** User only = U */
    public static final String WINUSERDEFLEVEL_UserOnly = X_Ref_AD_Role_WinUserDefLevel.USER_ONLY.getValue();
    /** Set Customization Level.
    @param WinUserDefLevel Level for Window Layout Customization */
    public void setWinUserDefLevel (String WinUserDefLevel)
    {
        if (!X_Ref_AD_Role_WinUserDefLevel.isValid(WinUserDefLevel))
        throw new IllegalArgumentException ("WinUserDefLevel Invalid value - " + WinUserDefLevel + " - Reference_ID=428 - C - N - R - U");
        set_Value ("WinUserDefLevel", WinUserDefLevel);
        
    }
    
    /** Get Customization Level.
    @return Level for Window Layout Customization */
    public String getWinUserDefLevel() 
    {
        return (String)get_Value("WinUserDefLevel");
        
    }
    
    
}
