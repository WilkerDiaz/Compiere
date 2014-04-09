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
/** Generated Model for W_Store
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_W_Store.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_W_Store extends PO
{
    /** Standard Constructor
    @param ctx context
    @param W_Store_ID id
    @param trx transaction
    */
    public X_W_Store (Ctx ctx, int W_Store_ID, Trx trx)
    {
        super (ctx, W_Store_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (W_Store_ID == 0)
        {
            setIsDefault (false);
            setIsMenuAssets (true);	// Y
            setIsMenuContact (true);	// Y
            setIsMenuInterests (true);	// Y
            setIsMenuInvoices (true);	// Y
            setIsMenuOrders (true);	// Y
            setIsMenuPayments (true);	// Y
            setIsMenuRegistrations (true);	// Y
            setIsMenuRequests (true);	// Y
            setIsMenuRfQs (true);	// Y
            setIsMenuShipments (true);	// Y
            setM_PriceList_ID (0);
            setM_Warehouse_ID (0);
            setName (null);
            setSalesRep_ID (0);
            setURL (null);
            setW_Store_ID (0);
            setWebContext (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_W_Store (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=778 */
    public static final int Table_ID=778;
    
    /** TableName=W_Store */
    public static final String Table_Name="W_Store";
    
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
        if (AD_Role_ID <= 0) set_Value ("AD_Role_ID", null);
        else
        set_Value ("AD_Role_ID", Integer.valueOf(AD_Role_ID));
        
    }
    
    /** Get Role.
    @return Responsibility Role */
    public int getAD_Role_ID() 
    {
        return get_ValueAsInt("AD_Role_ID");
        
    }
    
    /** Set Payment Term.
    @param C_PaymentTerm_ID The terms of Payment (timing, discount) */
    public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
    {
        if (C_PaymentTerm_ID <= 0) set_Value ("C_PaymentTerm_ID", null);
        else
        set_Value ("C_PaymentTerm_ID", Integer.valueOf(C_PaymentTerm_ID));
        
    }
    
    /** Get Payment Term.
    @return The terms of Payment (timing, discount) */
    public int getC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("C_PaymentTerm_ID");
        
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
    
    /** Set EMail Footer.
    @param EMailFooter Footer added to EMails */
    public void setEMailFooter (String EMailFooter)
    {
        set_Value ("EMailFooter", EMailFooter);
        
    }
    
    /** Get EMail Footer.
    @return Footer added to EMails */
    public String getEMailFooter() 
    {
        return (String)get_Value("EMailFooter");
        
    }
    
    /** Set EMail Header.
    @param EMailHeader Header added to EMails */
    public void setEMailHeader (String EMailHeader)
    {
        set_Value ("EMailHeader", EMailHeader);
        
    }
    
    /** Get EMail Header.
    @return Header added to EMails */
    public String getEMailHeader() 
    {
        return (String)get_Value("EMailHeader");
        
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
    
    /** Set Default.
    @param IsDefault Default value */
    public void setIsDefault (boolean IsDefault)
    {
        set_Value ("IsDefault", Boolean.valueOf(IsDefault));
        
    }
    
    /** Get Default.
    @return Default value */
    public boolean isDefault() 
    {
        return get_ValueAsBoolean("IsDefault");
        
    }
    
    /** Set Menu Assets.
    @param IsMenuAssets Show Menu Assets */
    public void setIsMenuAssets (boolean IsMenuAssets)
    {
        set_Value ("IsMenuAssets", Boolean.valueOf(IsMenuAssets));
        
    }
    
    /** Get Menu Assets.
    @return Show Menu Assets */
    public boolean isMenuAssets() 
    {
        return get_ValueAsBoolean("IsMenuAssets");
        
    }
    
    /** Set Menu Contact.
    @param IsMenuContact Show Menu Contact */
    public void setIsMenuContact (boolean IsMenuContact)
    {
        set_Value ("IsMenuContact", Boolean.valueOf(IsMenuContact));
        
    }
    
    /** Get Menu Contact.
    @return Show Menu Contact */
    public boolean isMenuContact() 
    {
        return get_ValueAsBoolean("IsMenuContact");
        
    }
    
    /** Set Menu Interests.
    @param IsMenuInterests Show Menu Interests */
    public void setIsMenuInterests (boolean IsMenuInterests)
    {
        set_Value ("IsMenuInterests", Boolean.valueOf(IsMenuInterests));
        
    }
    
    /** Get Menu Interests.
    @return Show Menu Interests */
    public boolean isMenuInterests() 
    {
        return get_ValueAsBoolean("IsMenuInterests");
        
    }
    
    /** Set Menu Invoices.
    @param IsMenuInvoices Show Menu Invoices */
    public void setIsMenuInvoices (boolean IsMenuInvoices)
    {
        set_Value ("IsMenuInvoices", Boolean.valueOf(IsMenuInvoices));
        
    }
    
    /** Get Menu Invoices.
    @return Show Menu Invoices */
    public boolean isMenuInvoices() 
    {
        return get_ValueAsBoolean("IsMenuInvoices");
        
    }
    
    /** Set Menu Orders.
    @param IsMenuOrders Show Menu Orders */
    public void setIsMenuOrders (boolean IsMenuOrders)
    {
        set_Value ("IsMenuOrders", Boolean.valueOf(IsMenuOrders));
        
    }
    
    /** Get Menu Orders.
    @return Show Menu Orders */
    public boolean isMenuOrders() 
    {
        return get_ValueAsBoolean("IsMenuOrders");
        
    }
    
    /** Set Menu Payments.
    @param IsMenuPayments Show Menu Payments */
    public void setIsMenuPayments (boolean IsMenuPayments)
    {
        set_Value ("IsMenuPayments", Boolean.valueOf(IsMenuPayments));
        
    }
    
    /** Get Menu Payments.
    @return Show Menu Payments */
    public boolean isMenuPayments() 
    {
        return get_ValueAsBoolean("IsMenuPayments");
        
    }
    
    /** Set Menu Registrations.
    @param IsMenuRegistrations Show Menu Registrations */
    public void setIsMenuRegistrations (boolean IsMenuRegistrations)
    {
        set_Value ("IsMenuRegistrations", Boolean.valueOf(IsMenuRegistrations));
        
    }
    
    /** Get Menu Registrations.
    @return Show Menu Registrations */
    public boolean isMenuRegistrations() 
    {
        return get_ValueAsBoolean("IsMenuRegistrations");
        
    }
    
    /** Set Menu Requests.
    @param IsMenuRequests Show Menu Requests */
    public void setIsMenuRequests (boolean IsMenuRequests)
    {
        set_Value ("IsMenuRequests", Boolean.valueOf(IsMenuRequests));
        
    }
    
    /** Get Menu Requests.
    @return Show Menu Requests */
    public boolean isMenuRequests() 
    {
        return get_ValueAsBoolean("IsMenuRequests");
        
    }
    
    /** Set Menu RfQs.
    @param IsMenuRfQs Show Menu RfQs */
    public void setIsMenuRfQs (boolean IsMenuRfQs)
    {
        set_Value ("IsMenuRfQs", Boolean.valueOf(IsMenuRfQs));
        
    }
    
    /** Get Menu RfQs.
    @return Show Menu RfQs */
    public boolean isMenuRfQs() 
    {
        return get_ValueAsBoolean("IsMenuRfQs");
        
    }
    
    /** Set Menu Shipments.
    @param IsMenuShipments Show Menu Shipments */
    public void setIsMenuShipments (boolean IsMenuShipments)
    {
        set_Value ("IsMenuShipments", Boolean.valueOf(IsMenuShipments));
        
    }
    
    /** Get Menu Shipments.
    @return Show Menu Shipments */
    public boolean isMenuShipments() 
    {
        return get_ValueAsBoolean("IsMenuShipments");
        
    }
    
    /** Set Price List.
    @param M_PriceList_ID Unique identifier of a Price List */
    public void setM_PriceList_ID (int M_PriceList_ID)
    {
        if (M_PriceList_ID < 1) throw new IllegalArgumentException ("M_PriceList_ID is mandatory.");
        set_Value ("M_PriceList_ID", Integer.valueOf(M_PriceList_ID));
        
    }
    
    /** Get Price List.
    @return Unique identifier of a Price List */
    public int getM_PriceList_ID() 
    {
        return get_ValueAsInt("M_PriceList_ID");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID < 1) throw new IllegalArgumentException ("M_Warehouse_ID is mandatory.");
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
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
    
    /** Set Representative.
    @param SalesRep_ID Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public void setSalesRep_ID (int SalesRep_ID)
    {
        if (SalesRep_ID < 1) throw new IllegalArgumentException ("SalesRep_ID is mandatory.");
        set_Value ("SalesRep_ID", Integer.valueOf(SalesRep_ID));
        
    }
    
    /** Get Representative.
    @return Company Agent like Sales Representative, Purchase Agent, and Customer Service Representative... */
    public int getSalesRep_ID() 
    {
        return get_ValueAsInt("SalesRep_ID");
        
    }
    
    /** Set Stylesheet.
    @param Stylesheet CSS (Stylesheet) used */
    public void setStylesheet (String Stylesheet)
    {
        set_Value ("Stylesheet", Stylesheet);
        
    }
    
    /** Get Stylesheet.
    @return CSS (Stylesheet) used */
    public String getStylesheet() 
    {
        return (String)get_Value("Stylesheet");
        
    }
    
    /** Set URL.
    @param URL Full URL address - e.g. http://www.compiere.org */
    public void setURL (String URL)
    {
        if (URL == null) throw new IllegalArgumentException ("URL is mandatory.");
        set_Value ("URL", URL);
        
    }
    
    /** Get URL.
    @return Full URL address - e.g. http://www.compiere.org */
    public String getURL() 
    {
        return (String)get_Value("URL");
        
    }
    
    /** Set Web Store EMail.
    @param WStoreEMail EMail address used as the sender (From) */
    public void setWStoreEMail (String WStoreEMail)
    {
        set_Value ("WStoreEMail", WStoreEMail);
        
    }
    
    /** Get Web Store EMail.
    @return EMail address used as the sender (From) */
    public String getWStoreEMail() 
    {
        return (String)get_Value("WStoreEMail");
        
    }
    
    /** Set Web Store User.
    @param WStoreUser User ID of the Web Store EMail address */
    public void setWStoreUser (String WStoreUser)
    {
        set_Value ("WStoreUser", WStoreUser);
        
    }
    
    /** Get Web Store User.
    @return User ID of the Web Store EMail address */
    public String getWStoreUser() 
    {
        return (String)get_Value("WStoreUser");
        
    }
    
    /** Set Web Store Password.
    @param WStoreUserPW Password of the Web Store EMail address */
    public void setWStoreUserPW (String WStoreUserPW)
    {
        set_Value ("WStoreUserPW", WStoreUserPW);
        
    }
    
    /** Get Web Store Password.
    @return Password of the Web Store EMail address */
    public String getWStoreUserPW() 
    {
        return (String)get_Value("WStoreUserPW");
        
    }
    
    /** Set Web Store.
    @param W_Store_ID A Web Store of the Client */
    public void setW_Store_ID (int W_Store_ID)
    {
        if (W_Store_ID < 1) throw new IllegalArgumentException ("W_Store_ID is mandatory.");
        set_ValueNoCheck ("W_Store_ID", Integer.valueOf(W_Store_ID));
        
    }
    
    /** Get Web Store.
    @return A Web Store of the Client */
    public int getW_Store_ID() 
    {
        return get_ValueAsInt("W_Store_ID");
        
    }
    
    /** Set Web Context.
    @param WebContext Web Server Context - e.g. /wstore */
    public void setWebContext (String WebContext)
    {
        if (WebContext == null) throw new IllegalArgumentException ("WebContext is mandatory.");
        set_Value ("WebContext", WebContext);
        
    }
    
    /** Get Web Context.
    @return Web Server Context - e.g. /wstore */
    public String getWebContext() 
    {
        return (String)get_Value("WebContext");
        
    }
    
    /** Set Web Store Info.
    @param WebInfo Web Store Header Information */
    public void setWebInfo (String WebInfo)
    {
        set_Value ("WebInfo", WebInfo);
        
    }
    
    /** Get Web Store Info.
    @return Web Store Header Information */
    public String getWebInfo() 
    {
        return (String)get_Value("WebInfo");
        
    }
    
    /** Set Web Order EMail.
    @param WebOrderEMail EMail address to receive notifications when web orders were processed */
    public void setWebOrderEMail (String WebOrderEMail)
    {
        set_Value ("WebOrderEMail", WebOrderEMail);
        
    }
    
    /** Get Web Order EMail.
    @return EMail address to receive notifications when web orders were processed */
    public String getWebOrderEMail() 
    {
        return (String)get_Value("WebOrderEMail");
        
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
    
    /** Set Web Parameter 5.
    @param WebParam5 Web Site Parameter 5 (default footer center) */
    public void setWebParam5 (String WebParam5)
    {
        set_Value ("WebParam5", WebParam5);
        
    }
    
    /** Get Web Parameter 5.
    @return Web Site Parameter 5 (default footer center) */
    public String getWebParam5() 
    {
        return (String)get_Value("WebParam5");
        
    }
    
    /** Set Web Parameter 6.
    @param WebParam6 Web Site Parameter 6 (default footer right) */
    public void setWebParam6 (String WebParam6)
    {
        set_Value ("WebParam6", WebParam6);
        
    }
    
    /** Get Web Parameter 6.
    @return Web Site Parameter 6 (default footer right) */
    public String getWebParam6() 
    {
        return (String)get_Value("WebParam6");
        
    }
    
    
}
