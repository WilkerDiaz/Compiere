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
/** Generated Model for I_XX_VCN_BookPurchases
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VCN_BookPurchases extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VCN_BookPurchases_ID id
    @param trx transaction
    */
    public X_I_XX_VCN_BookPurchases (Ctx ctx, int I_XX_VCN_BookPurchases_ID, Trx trx)
    {
        super (ctx, I_XX_VCN_BookPurchases_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VCN_BookPurchases_ID == 0)
        {
            setI_XX_VCN_BOOKPURCHASES_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VCN_BookPurchases (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27572928610789L;
    /** Last Updated Timestamp 2010-11-26 15:58:14.0 */
    public static final long updatedMS = 1290803294000L;
    /** AD_Table_ID=1000256 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VCN_BookPurchases");
        
    }
    ;
    
    /** TableName=I_XX_VCN_BookPurchases */
    public static final String Table_Name="I_XX_VCN_BookPurchases";
    
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
    
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID <= 0) set_Value ("C_Invoice_ID", null);
        else
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
    }
    
    /** Set Tax.
    @param C_Tax_ID Tax identifier */
    public void setC_Tax_ID (int C_Tax_ID)
    {
        if (C_Tax_ID <= 0) set_Value ("C_Tax_ID", null);
        else
        set_Value ("C_Tax_ID", Integer.valueOf(C_Tax_ID));
        
    }
    
    /** Get Tax.
    @return Tax identifier */
    public int getC_Tax_ID() 
    {
        return get_ValueAsInt("C_Tax_ID");
        
    }
    
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (String I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", I_ErrorMsg);
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public String getI_ErrorMsg() 
    {
        return (String)get_Value("I_ErrorMsg");
        
    }
    
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (boolean I_IsImported)
    {
        set_Value ("I_IsImported", Boolean.valueOf(I_IsImported));
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public boolean isI_IsImported() 
    {
        return get_ValueAsBoolean("I_IsImported");
        
    }
    
    /** Set I_XX_VCN_BOOKPURCHASES_ID.
    @param I_XX_VCN_BOOKPURCHASES_ID I_XX_VCN_BOOKPURCHASES_ID */
    public void setI_XX_VCN_BOOKPURCHASES_ID (int I_XX_VCN_BOOKPURCHASES_ID)
    {
        if (I_XX_VCN_BOOKPURCHASES_ID < 1) throw new IllegalArgumentException ("I_XX_VCN_BOOKPURCHASES_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VCN_BOOKPURCHASES_ID", Integer.valueOf(I_XX_VCN_BOOKPURCHASES_ID));
        
    }
    
    /** Get I_XX_VCN_BOOKPURCHASES_ID.
    @return I_XX_VCN_BOOKPURCHASES_ID */
    public int getI_XX_VCN_BOOKPURCHASES_ID() 
    {
        return get_ValueAsInt("I_XX_VCN_BOOKPURCHASES_ID");
        
    }
    
    /** Set Match PO.
    @param M_MatchPO_ID Match Purchase Order to Shipment/Receipt and Invoice */
    public void setM_MatchPO_ID (int M_MatchPO_ID)
    {
        if (M_MatchPO_ID <= 0) set_Value ("M_MatchPO_ID", null);
        else
        set_Value ("M_MatchPO_ID", Integer.valueOf(M_MatchPO_ID));
        
    }
    
    /** Get Match PO.
    @return Match Purchase Order to Shipment/Receipt and Invoice */
    public int getM_MatchPO_ID() 
    {
        return get_ValueAsInt("M_MatchPO_ID");
        
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
    
    /** Set XX_COBASE.
    @param XX_COBASE XX_COBASE */
    public void setXX_COBASE (String XX_COBASE)
    {
        set_Value ("XX_COBASE", XX_COBASE);
        
    }
    
    /** Get XX_COBASE.
    @return XX_COBASE */
    public String getXX_COBASE() 
    {
        return (String)get_Value("XX_COBASE");
        
    }
    
    /** Set XX_COBASENS.
    @param XX_COBASENS XX_COBASENS */
    public void setXX_COBASENS (String XX_COBASENS)
    {
        set_Value ("XX_COBASENS", XX_COBASENS);
        
    }
    
    /** Get XX_COBASENS.
    @return XX_COBASENS */
    public String getXX_COBASENS() 
    {
        return (String)get_Value("XX_COBASENS");
        
    }
    
    /** Set XX_COBASEX.
    @param XX_COBASEX XX_COBASEX */
    public void setXX_COBASEX (String XX_COBASEX)
    {
        set_Value ("XX_COBASEX", XX_COBASEX);
        
    }
    
    /** Get XX_COBASEX.
    @return XX_COBASEX */
    public String getXX_COBASEX() 
    {
        return (String)get_Value("XX_COBASEX");
        
    }
    
    /** Set XX_COCOST.
    @param XX_COCOST XX_COCOST */
    public void setXX_COCOST (String XX_COCOST)
    {
        set_Value ("XX_COCOST", XX_COCOST);
        
    }
    
    /** Get XX_COCOST.
    @return XX_COCOST */
    public String getXX_COCOST() 
    {
        return (String)get_Value("XX_COCOST");
        
    }
    
    /** Set XX_COFACT.
    @param XX_COFACT XX_COFACT */
    public void setXX_COFACT (String XX_COFACT)
    {
        set_Value ("XX_COFACT", XX_COFACT);
        
    }
    
    /** Get XX_COFACT.
    @return XX_COFACT */
    public String getXX_COFACT() 
    {
        return (String)get_Value("XX_COFACT");
        
    }
    
    /** Set XX_COIMPU.
    @param XX_COIMPU XX_COIMPU */
    public void setXX_COIMPU (String XX_COIMPU)
    {
        set_Value ("XX_COIMPU", XX_COIMPU);
        
    }
    
    /** Get XX_COIMPU.
    @return XX_COIMPU */
    public String getXX_COIMPU() 
    {
        return (String)get_Value("XX_COIMPU");
        
    }
    
    /** Set XX_CONOTA.
    @param XX_CONOTA XX_CONOTA */
    public void setXX_CONOTA (String XX_CONOTA)
    {
        set_Value ("XX_CONOTA", XX_CONOTA);
        
    }
    
    /** Get XX_CONOTA.
    @return XX_CONOTA */
    public String getXX_CONOTA() 
    {
        return (String)get_Value("XX_CONOTA");
        
    }
    
    /** Set XX_COORIG.
    @param XX_COORIG XX_COORIG */
    public void setXX_COORIG (String XX_COORIG)
    {
        set_Value ("XX_COORIG", XX_COORIG);
        
    }
    
    /** Get XX_COORIG.
    @return XX_COORIG */
    public String getXX_COORIG() 
    {
        return (String)get_Value("XX_COORIG");
        
    }
    
    /** Set XX_COPROV.
    @param XX_COPROV XX_COPROV */
    public void setXX_COPROV (String XX_COPROV)
    {
        set_Value ("XX_COPROV", XX_COPROV);
        
    }
    
    /** Get XX_COPROV.
    @return XX_COPROV */
    public String getXX_COPROV() 
    {
        return (String)get_Value("XX_COPROV");
        
    }
    
    /** Set XX_COTIEN.
    @param XX_COTIEN XX_COTIEN */
    public void setXX_COTIEN (String XX_COTIEN)
    {
        set_Value ("XX_COTIEN", XX_COTIEN);
        
    }
    
    /** Get XX_COTIEN.
    @return XX_COTIEN */
    public String getXX_COTIEN() 
    {
        return (String)get_Value("XX_COTIEN");
        
    }
    
    /** Set Date.
    @param XX_DATE Date */
    public void setXX_DATE (Timestamp XX_DATE)
    {
        set_Value ("XX_DATE", XX_DATE);
        
    }
    
    /** Get Date.
    @return Date */
    public Timestamp getXX_DATE() 
    {
        return (Timestamp)get_Value("XX_DATE");
        
    }
    
    /** Set XX_DAY.
    @param XX_DAY XX_DAY */
    public void setXX_DAY (String XX_DAY)
    {
        set_Value ("XX_DAY", XX_DAY);
        
    }
    
    /** Get XX_DAY.
    @return XX_DAY */
    public String getXX_DAY() 
    {
        return (String)get_Value("XX_DAY");
        
    }
    
    /** Set XX_DEBCRE.
    @param XX_DEBCRE XX_DEBCRE */
    public void setXX_DEBCRE (String XX_DEBCRE)
    {
        set_Value ("XX_DEBCRE", XX_DEBCRE);
        
    }
    
    /** Get XX_DEBCRE.
    @return XX_DEBCRE */
    public String getXX_DEBCRE() 
    {
        return (String)get_Value("XX_DEBCRE");
        
    }
    
    /** Set XX_DOCDATE.
    @param XX_DOCDATE XX_DOCDATE */
    public void setXX_DOCDATE (Timestamp XX_DOCDATE)
    {
        set_Value ("XX_DOCDATE", XX_DOCDATE);
        
    }
    
    /** Get XX_DOCDATE.
    @return XX_DOCDATE */
    public Timestamp getXX_DOCDATE() 
    {
        return (Timestamp)get_Value("XX_DOCDATE");
        
    }
    
    /** Set Document Number.
    @param XX_DocumentNo_ID Document Number */
    public void setXX_DocumentNo_ID (int XX_DocumentNo_ID)
    {
        if (XX_DocumentNo_ID <= 0) set_Value ("XX_DocumentNo_ID", null);
        else
        set_Value ("XX_DocumentNo_ID", Integer.valueOf(XX_DocumentNo_ID));
        
    }
    
    /** Get Document Number.
    @return Document Number */
    public int getXX_DocumentNo_ID() 
    {
        return get_ValueAsInt("XX_DocumentNo_ID");
        
    }
    
    /** Set XX_FECDOC.
    @param XX_FECDOC XX_FECDOC */
    public void setXX_FECDOC (String XX_FECDOC)
    {
        set_Value ("XX_FECDOC", XX_FECDOC);
        
    }
    
    /** Get XX_FECDOC.
    @return XX_FECDOC */
    public String getXX_FECDOC() 
    {
        return (String)get_Value("XX_FECDOC");
        
    }
    
    /** Set XX_MONRET.
    @param XX_MONRET XX_MONRET */
    public void setXX_MONRET (String XX_MONRET)
    {
        set_Value ("XX_MONRET", XX_MONRET);
        
    }
    
    /** Get XX_MONRET.
    @return XX_MONRET */
    public String getXX_MONRET() 
    {
        return (String)get_Value("XX_MONRET");
        
    }
    
    /** Set Month.
    @param XX_Month Month */
    public void setXX_Month (String XX_Month)
    {
        set_Value ("XX_Month", XX_Month);
        
    }
    
    /** Get Month.
    @return Month */
    public String getXX_Month() 
    {
        return (String)get_Value("XX_Month");
        
    }
    
    /** Set XX_NROCOM.
    @param XX_NROCOM XX_NROCOM */
    public void setXX_NROCOM (String XX_NROCOM)
    {
        set_Value ("XX_NROCOM", XX_NROCOM);
        
    }
    
    /** Get XX_NROCOM.
    @return XX_NROCOM */
    public String getXX_NROCOM() 
    {
        return (String)get_Value("XX_NROCOM");
        
    }
    
    /** Set XX_NUMCTL.
    @param XX_NUMCTL XX_NUMCTL */
    public void setXX_NUMCTL (String XX_NUMCTL)
    {
        set_Value ("XX_NUMCTL", XX_NUMCTL);
        
    }
    
    /** Get XX_NUMCTL.
    @return XX_NUMCTL */
    public String getXX_NUMCTL() 
    {
        return (String)get_Value("XX_NUMCTL");
        
    }
    
    /** Set XX_NUMEXP.
    @param XX_NUMEXP XX_NUMEXP */
    public void setXX_NUMEXP (String XX_NUMEXP)
    {
        set_Value ("XX_NUMEXP", XX_NUMEXP);
        
    }
    
    /** Get XX_NUMEXP.
    @return XX_NUMEXP */
    public String getXX_NUMEXP() 
    {
        return (String)get_Value("XX_NUMEXP");
        
    }
    
    /** Set XX_NUMPLA.
    @param XX_NUMPLA XX_NUMPLA */
    public void setXX_NUMPLA (String XX_NUMPLA)
    {
        set_Value ("XX_NUMPLA", XX_NUMPLA);
        
    }
    
    /** Get XX_NUMPLA.
    @return XX_NUMPLA */
    public String getXX_NUMPLA() 
    {
        return (String)get_Value("XX_NUMPLA");
        
    }
    
    /** Set XX_STAELI.
    @param XX_STAELI XX_STAELI */
    public void setXX_STAELI (String XX_STAELI)
    {
        set_Value ("XX_STAELI", XX_STAELI);
        
    }
    
    /** Get XX_STAELI.
    @return XX_STAELI */
    public String getXX_STAELI() 
    {
        return (String)get_Value("XX_STAELI");
        
    }
    
    /** Set XX_TASAS.
    @param XX_TASAS XX_TASAS */
    public void setXX_TASAS (String XX_TASAS)
    {
        set_Value ("XX_TASAS", XX_TASAS);
        
    }
    
    /** Get XX_TASAS.
    @return XX_TASAS */
    public String getXX_TASAS() 
    {
        return (String)get_Value("XX_TASAS");
        
    }
    
    /** Set XX_USRELI.
    @param XX_USRELI XX_USRELI */
    public void setXX_USRELI (String XX_USRELI)
    {
        set_Value ("XX_USRELI", XX_USRELI);
        
    }
    
    /** Get XX_USRELI.
    @return XX_USRELI */
    public String getXX_USRELI() 
    {
        return (String)get_Value("XX_USRELI");
        
    }
    
    /** Set Purchases' Book ID.
    @param XX_VCN_PurchasesBook_ID Purchases' Book ID */
    public void setXX_VCN_PurchasesBook_ID (int XX_VCN_PurchasesBook_ID)
    {
        if (XX_VCN_PurchasesBook_ID <= 0) set_Value ("XX_VCN_PurchasesBook_ID", null);
        else
        set_Value ("XX_VCN_PurchasesBook_ID", Integer.valueOf(XX_VCN_PurchasesBook_ID));
        
    }
    
    /** Get Purchases' Book ID.
    @return Purchases' Book ID */
    public int getXX_VCN_PurchasesBook_ID() 
    {
        return get_ValueAsInt("XX_VCN_PurchasesBook_ID");
        
    }
    
    /** Set Year.
    @param XX_Year Year */
    public void setXX_Year (String XX_Year)
    {
        set_Value ("XX_Year", XX_Year);
        
    }
    
    /** Get Year.
    @return Year */
    public String getXX_Year() 
    {
        return (String)get_Value("XX_Year");
        
    }
    
    
}
