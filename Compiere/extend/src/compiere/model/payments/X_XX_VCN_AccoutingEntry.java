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
package compiere.model.payments;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_AccoutingEntry
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_AccoutingEntry extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_AccoutingEntry_ID id
    @param trx transaction
    */
    public X_XX_VCN_AccoutingEntry (Ctx ctx, int XX_VCN_AccoutingEntry_ID, Trx trx)
    {
        super (ctx, XX_VCN_AccoutingEntry_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_AccoutingEntry_ID == 0)
        {
            setXX_VCN_AccoutingEntry_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_AccoutingEntry (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27666406120789L;
    /** Last Updated Timestamp 2013-11-12 13:56:44.0 */
    public static final long updatedMS = 1384280804000L;
    /** AD_Table_ID=1002654 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_AccoutingEntry");
        
    }
    ;
    
    /** TableName=XX_VCN_AccoutingEntry */
    public static final String Table_Name="XX_VCN_AccoutingEntry";
    
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
    
    /** Set Date From.
    @param DateFrom Starting date for a range */
    public void setDateFrom (Timestamp DateFrom)
    {
        set_Value ("DateFrom", DateFrom);
        
    }
    
    /** Get Date From.
    @return Starting date for a range */
    public Timestamp getDateFrom() 
    {
        return (Timestamp)get_Value("DateFrom");
        
    }
    
    /** Set Date To.
    @param DateTo End date of a date range */
    public void setDateTo (Timestamp DateTo)
    {
        set_Value ("DateTo", DateTo);
        
    }
    
    /** Get Date To.
    @return End date of a date range */
    public Timestamp getDateTo() 
    {
        return (Timestamp)get_Value("DateTo");
        
    }
    
    /** Set Transaction Date.
    @param DateTrx Transaction Date */
    public void setDateTrx (Timestamp DateTrx)
    {
        set_Value ("DateTrx", DateTrx);
        
    }
    
    /** Get Transaction Date.
    @return Transaction Date */
    public Timestamp getDateTrx() 
    {
        return (Timestamp)get_Value("DateTrx");
        
    }
    
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Transferred.
    @param IsTransferred Transferred to General Ledger (i.e. accounted) */
    public void setIsTransferred (boolean IsTransferred)
    {
        set_Value ("IsTransferred", Boolean.valueOf(IsTransferred));
        
    }
    
    /** Get Transferred.
    @return Transferred to General Ledger (i.e. accounted) */
    public boolean isTransferred() 
    {
        return get_ValueAsBoolean("IsTransferred");
        
    }
    
    /** Set Warehouse.
    @param M_Warehouse_ID Storage Warehouse and Service Point */
    public void setM_Warehouse_ID (int M_Warehouse_ID)
    {
        if (M_Warehouse_ID <= 0) set_Value ("M_Warehouse_ID", null);
        else
        set_Value ("M_Warehouse_ID", Integer.valueOf(M_Warehouse_ID));
        
    }
    
    /** Get Warehouse.
    @return Storage Warehouse and Service Point */
    public int getM_Warehouse_ID() 
    {
        return get_ValueAsInt("M_Warehouse_ID");
        
    }
    
    /** Set Control Number.
    @param XX_ControlNumber Número de Control de la Factura */
    public void setXX_ControlNumber (String XX_ControlNumber)
    {
        set_Value ("XX_ControlNumber", XX_ControlNumber);
        
    }
    
    /** Get Control Number.
    @return Número de Control de la Factura */
    public String getXX_ControlNumber() 
    {
        return (String)get_Value("XX_ControlNumber");
        
    }
    
    /** Bank = BA */
    public static final String XX_LISTCX017_Bank = X_Ref_XX_ListTransferCX017.BANK.getValue();
    /** Accounts Payable Approved  = CA */
    public static final String XX_LISTCX017_AccountsPayableApproved = X_Ref_XX_ListTransferCX017.ACCOUNTS_PAYABLE_APPROVED.getValue();
    /** Accounts Payable Estimated = CE */
    public static final String XX_LISTCX017_AccountsPayableEstimated = X_Ref_XX_ListTransferCX017.ACCOUNTS_PAYABLE_ESTIMATED.getValue();
    /** Purchase/Sale = CV */
    public static final String XX_LISTCX017_PurchaseSale = X_Ref_XX_ListTransferCX017.PURCHASE_SALE.getValue();
    /** Set List Transfer CX017.
    @param XX_ListCX017 List Transfer CX017 */
    public void setXX_ListCX017 (String XX_ListCX017)
    {
        if (!X_Ref_XX_ListTransferCX017.isValid(XX_ListCX017))
        throw new IllegalArgumentException ("XX_ListCX017 Invalid value - " + XX_ListCX017 + " - Reference_ID=1001855 - BA - CA - CE - CV");
        set_Value ("XX_ListCX017", XX_ListCX017);
        
    }
    
    /** Get List Transfer CX017.
    @return List Transfer CX017 */
    public String getXX_ListCX017() 
    {
        return (String)get_Value("XX_ListCX017");
        
    }
    
    /** Set Office.
    @param XX_Office Office */
    public void setXX_Office (String XX_Office)
    {
        set_Value ("XX_Office", XX_Office);
        
    }
    
    /** Get Office.
    @return Office */
    public String getXX_Office() 
    {
        return (String)get_Value("XX_Office");
        
    }
    
    /** Assets/Services = A */
    public static final String XX_PROCESSTYPE_AssetsServices = X_Ref_XX_ProcessType.ASSETS_SERVICES.getValue();
    /** Item = I */
    public static final String XX_PROCESSTYPE_Item = X_Ref_XX_ProcessType.ITEM.getValue();
    /** Set Process Type.
    @param XX_ProcessType Process Type */
    public void setXX_ProcessType (String XX_ProcessType)
    {
        if (!X_Ref_XX_ProcessType.isValid(XX_ProcessType))
        throw new IllegalArgumentException ("XX_ProcessType Invalid value - " + XX_ProcessType + " - Reference_ID=1001858 - A - I");
        set_Value ("XX_ProcessType", XX_ProcessType);
        
    }
    
    /** Get Process Type.
    @return Process Type */
    public String getXX_ProcessType() 
    {
        return (String)get_Value("XX_ProcessType");
        
    }
    
    /** Set Total Have.
    @param XX_TotalHave Total Have */
    public void setXX_TotalHave (java.math.BigDecimal XX_TotalHave)
    {
        set_Value ("XX_TotalHave", XX_TotalHave);
        
    }
    
    /** Get Total Have.
    @return Total Have */
    public java.math.BigDecimal getXX_TotalHave() 
    {
        return get_ValueAsBigDecimal("XX_TotalHave");
        
    }
    
    /** Set Total Should.
    @param XX_TotalShould Total Should */
    public void setXX_TotalShould (java.math.BigDecimal XX_TotalShould)
    {
        set_Value ("XX_TotalShould", XX_TotalShould);
        
    }
    
    /** Get Total Should.
    @return Total Should */
    public java.math.BigDecimal getXX_TotalShould() 
    {
        return get_ValueAsBigDecimal("XX_TotalShould");
        
    }
    
    /** Discount = D */
    public static final String XX_TYPETRANSFERSP_Discount = X_Ref_XX_TypeTransferSPList.DISCOUNT.getValue();
    /** Inventory Change = I */
    public static final String XX_TYPETRANSFERSP_InventoryChange = X_Ref_XX_TypeTransferSPList.INVENTORY_CHANGE.getValue();
    /** Purchases = P */
    public static final String XX_TYPETRANSFERSP_Purchases = X_Ref_XX_TypeTransferSPList.PURCHASES.getValue();
    /** Sales = S */
    public static final String XX_TYPETRANSFERSP_Sales = X_Ref_XX_TypeTransferSPList.SALES.getValue();
    /** Asientos Anuales de Inventario = Y */
    public static final String XX_TYPETRANSFERSP_AsientosAnualesDeInventario = X_Ref_XX_TypeTransferSPList.YEARLY_INVENTORY.getValue();
    /** Set Type Transfer Sale/Purchase.
    @param XX_TypeTransferSP Type Transfer Sale/Purchase */
    public void setXX_TypeTransferSP (String XX_TypeTransferSP)
    {
        if (!X_Ref_XX_TypeTransferSPList.isValid(XX_TypeTransferSP))
        throw new IllegalArgumentException ("XX_TypeTransferSP Invalid value - " + XX_TypeTransferSP + " - Reference_ID=1002249 - D - I - P - S - Y");
        set_Value ("XX_TypeTransferSP", XX_TypeTransferSP);
        
    }
    
    /** Get Type Transfer Sale/Purchase.
    @return Type Transfer Sale/Purchase */
    public String getXX_TypeTransferSP() 
    {
        return (String)get_Value("XX_TypeTransferSP");
        
    }
    
    /** Set XX_VCN_AccoutingEntry_ID.
    @param XX_VCN_AccoutingEntry_ID XX_VCN_AccoutingEntry_ID */
    public void setXX_VCN_AccoutingEntry_ID (int XX_VCN_AccoutingEntry_ID)
    {
        if (XX_VCN_AccoutingEntry_ID < 1) throw new IllegalArgumentException ("XX_VCN_AccoutingEntry_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_AccoutingEntry_ID", Integer.valueOf(XX_VCN_AccoutingEntry_ID));
        
    }
    
    /** Get XX_VCN_AccoutingEntry_ID.
    @return XX_VCN_AccoutingEntry_ID */
    public int getXX_VCN_AccoutingEntry_ID() 
    {
        return get_ValueAsInt("XX_VCN_AccoutingEntry_ID");
        
    }
    
    
}
