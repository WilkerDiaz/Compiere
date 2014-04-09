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
/** Generated Model for XX_VLO_NewsReport
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_NewsReport extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_NewsReport_ID id
    @param trx transaction
    */
    public X_XX_VLO_NewsReport (Ctx ctx, int XX_VLO_NewsReport_ID, Trx trx)
    {
        super (ctx, XX_VLO_NewsReport_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_NewsReport_ID == 0)
        {
            setXX_NoSencamer (false);
            setXX_VLO_NewsReport_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_NewsReport (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27616466802789L;
    /** Last Updated Timestamp 2012-04-13 13:54:46.0 */
    public static final long updatedMS = 1334341486000L;
    /** AD_Table_ID=1000441 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_NewsReport");
        
    }
    ;
    
    /** TableName=XX_VLO_NewsReport */
    public static final String Table_Name="XX_VLO_NewsReport";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_Value ("C_Order_ID", null);
        else
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
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
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
    }
    
    /** Set Annul.
    @param XX_Annul Annul */
    public void setXX_Annul (String XX_Annul)
    {
        set_Value ("XX_Annul", XX_Annul);
        
    }
    
    /** Get Annul.
    @return Annul */
    public String getXX_Annul() 
    {
        return (String)get_Value("XX_Annul");
        
    }
    
    /** Set Annul Motive.
    @param XX_AnnulMotive Annul Motive */
    public void setXX_AnnulMotive (String XX_AnnulMotive)
    {
        set_Value ("XX_AnnulMotive", XX_AnnulMotive);
        
    }
    
    /** Get Annul Motive.
    @return Annul Motive */
    public String getXX_AnnulMotive() 
    {
        return (String)get_Value("XX_AnnulMotive");
        
    }
    
    /** Set Aux Product.
    @param XX_AuxProduct_ID Aux Product */
    public void setXX_AuxProduct_ID (int XX_AuxProduct_ID)
    {
        if (XX_AuxProduct_ID <= 0) set_Value ("XX_AuxProduct_ID", null);
        else
        set_Value ("XX_AuxProduct_ID", Integer.valueOf(XX_AuxProduct_ID));
        
    }
    
    /** Get Aux Product.
    @return Aux Product */
    public int getXX_AuxProduct_ID() 
    {
        return get_ValueAsInt("XX_AuxProduct_ID");
        
    }
    
    /** Set Complete News Report.
    @param XX_CompleteNR Complete News Report */
    public void setXX_CompleteNR (String XX_CompleteNR)
    {
        set_Value ("XX_CompleteNR", XX_CompleteNR);
        
    }
    
    /** Get Complete News Report.
    @return Complete News Report */
    public String getXX_CompleteNR() 
    {
        return (String)get_Value("XX_CompleteNR");
        
    }
    
    /** Set Convert Product.
    @param XX_ConvertProduct Convert Product */
    public void setXX_ConvertProduct (String XX_ConvertProduct)
    {
        set_Value ("XX_ConvertProduct", XX_ConvertProduct);
        
    }
    
    /** Get Convert Product.
    @return Convert Product */
    public String getXX_ConvertProduct() 
    {
        return (String)get_Value("XX_ConvertProduct");
        
    }
    
    /** Set New Order.
    @param XX_NewOrder_ID New Order */
    public void setXX_NewOrder_ID (int XX_NewOrder_ID)
    {
        if (XX_NewOrder_ID <= 0) set_Value ("XX_NewOrder_ID", null);
        else
        set_Value ("XX_NewOrder_ID", Integer.valueOf(XX_NewOrder_ID));
        
    }
    
    /** Get New Order.
    @return New Order */
    public int getXX_NewOrder_ID() 
    {
        return get_ValueAsInt("XX_NewOrder_ID");
        
    }
    
    /** DAÑADO = 1 */
    public static final String XX_NEWSREPORTMOTIVE_DAÑADO = X_Ref_XX_NewsReportMotive.DAÑADO.getValue();
    /** NO APTO PARA LA VENTA = 2 */
    public static final String XX_NEWSREPORTMOTIVE_NOAPTOPARALAVENTA = X_Ref_XX_NewsReportMotive.NOAPTOPARALAVENTA.getValue();
    /** NO CORRESPONDE CON LO SOLICITADO = 3 */
    public static final String XX_NEWSREPORTMOTIVE_NOCORRESPONDECONLOSOLICITADO = X_Ref_XX_NewsReportMotive.NOCORRESPONDECONLOSOLICITADO.getValue();
    /** NO PEDIDO = 4 */
    public static final String XX_NEWSREPORTMOTIVE_NOPEDIDO = X_Ref_XX_NewsReportMotive.NOPEDIDO.getValue();
    /** EXCEDENTE = 5 */
    public static final String XX_NEWSREPORTMOTIVE_EXCEDENTE = X_Ref_XX_NewsReportMotive.EXCEDENTE.getValue();
    /** Set Motive.
    @param XX_NewsReportMotive Motive */
    public void setXX_NewsReportMotive (String XX_NewsReportMotive)
    {
        if (!X_Ref_XX_NewsReportMotive.isValid(XX_NewsReportMotive))
        throw new IllegalArgumentException ("XX_NewsReportMotive Invalid value - " + XX_NewsReportMotive + " - Reference_ID=1000349 - 1 - 2 - 3 - 4 - 5");
        set_Value ("XX_NewsReportMotive", XX_NewsReportMotive);
        
    }
    
    /** Get Motive.
    @return Motive */
    public String getXX_NewsReportMotive() 
    {
        return (String)get_Value("XX_NewsReportMotive");
        
    }
    
    /** Set No Sencamer.
    @param XX_NoSencamer No Sencamer */
    public void setXX_NoSencamer (boolean XX_NoSencamer)
    {
        set_Value ("XX_NoSencamer", Boolean.valueOf(XX_NoSencamer));
        
    }
    
    /** Get No Sencamer.
    @return No Sencamer */
    public boolean isXX_NoSencamer() 
    {
        return get_ValueAsBoolean("XX_NoSencamer");
        
    }
    
    /** Set Product Department.
    @param XX_ProductDepartment_ID Product Department */
    public void setXX_ProductDepartment_ID (int XX_ProductDepartment_ID)
    {
        throw new IllegalArgumentException ("XX_ProductDepartment_ID is virtual column");
        
    }
    
    /** Get Product Department.
    @return Product Department */
    public int getXX_ProductDepartment_ID() 
    {
        return get_ValueAsInt("XX_ProductDepartment_ID");
        
    }
    
    /** Set Quantity.
    @param XX_Quantity Quantity */
    public void setXX_Quantity (int XX_Quantity)
    {
        set_Value ("XX_Quantity", Integer.valueOf(XX_Quantity));
        
    }
    
    /** Get Quantity.
    @return Quantity */
    public int getXX_Quantity() 
    {
        return get_ValueAsInt("XX_Quantity");
        
    }
    
    /** Set Sale Price.
    @param XX_SalePrice Sale Price */
    public void setXX_SalePrice (java.math.BigDecimal XX_SalePrice)
    {
        set_Value ("XX_SalePrice", XX_SalePrice);
        
    }
    
    /** Get Sale Price.
    @return Sale Price */
    public java.math.BigDecimal getXX_SalePrice() 
    {
        return get_ValueAsBigDecimal("XX_SalePrice");
        
    }
    
    /** ANULADO = AN */
    public static final String XX_STATUS_ANULADO = X_Ref_News_Report_Status.ANULADO.getValue();
    /** POR ASIGNAR ORDEN = AO */
    public static final String XX_STATUS_PORASIGNARORDEN = X_Ref_News_Report_Status.PORASIGNARORDEN.getValue();
    /** COMPLETADO = CO */
    public static final String XX_STATUS_COMPLETADO = X_Ref_News_Report_Status.COMPLETADO.getValue();
    /** PRE-GUARDADO = PR */
    public static final String XX_STATUS_PRE_GUARDADO = X_Ref_News_Report_Status.PR_E__GUARDADO.getValue();
    /** Set Status.
    @param XX_Status Status */
    public void setXX_Status (String XX_Status)
    {
        if (!X_Ref_News_Report_Status.isValid(XX_Status))
        throw new IllegalArgumentException ("XX_Status Invalid value - " + XX_Status + " - Reference_ID=1000345 - AN - AO - CO - PR");
        set_Value ("XX_Status", XX_Status);
        
    }
    
    /** Get Status.
    @return Status */
    public String getXX_Status() 
    {
        return (String)get_Value("XX_Status");
        
    }
    
    /** Set User Buyer.
    @param XX_UserBuyer_ID Usuario Comprador */
    public void setXX_UserBuyer_ID (int XX_UserBuyer_ID)
    {
        throw new IllegalArgumentException ("XX_UserBuyer_ID is virtual column");
        
    }
    
    /** Get User Buyer.
    @return Usuario Comprador */
    public int getXX_UserBuyer_ID() 
    {
        return get_ValueAsInt("XX_UserBuyer_ID");
        
    }
    
    /** Set Vendor Reference.
    @param XX_VendorReference Vendor Reference */
    public void setXX_VendorReference (String XX_VendorReference)
    {
        set_Value ("XX_VendorReference", XX_VendorReference);
        
    }
    
    /** Get Vendor Reference.
    @return Vendor Reference */
    public String getXX_VendorReference() 
    {
        return (String)get_Value("XX_VendorReference");
        
    }
    
    /** Set News Report.
    @param XX_VLO_NewsReport_ID News Report */
    public void setXX_VLO_NewsReport_ID (int XX_VLO_NewsReport_ID)
    {
        if (XX_VLO_NewsReport_ID < 1) throw new IllegalArgumentException ("XX_VLO_NewsReport_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_NewsReport_ID", Integer.valueOf(XX_VLO_NewsReport_ID));
        
    }
    
    /** Get News Report.
    @return News Report */
    public int getXX_VLO_NewsReport_ID() 
    {
        return get_ValueAsInt("XX_VLO_NewsReport_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        throw new IllegalArgumentException ("XX_VMR_Department_ID is virtual column");
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    
}
