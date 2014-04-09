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
/** Generated Model for XX_DefinitiveDocumentView1
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_DefinitiveDocumentView1 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_DefinitiveDocumentView1_ID id
    @param trx transaction
    */
    public X_XX_DefinitiveDocumentView1 (Ctx ctx, int XX_DefinitiveDocumentView1_ID, Trx trx)
    {
        super (ctx, XX_DefinitiveDocumentView1_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_DefinitiveDocumentView1_ID == 0)
        {
            setC_BPartner_ID (0);
            setC_Order_ID (0);
            setC_PaymentTerm_ID (0);
            setM_InOut_ID (0);
            setXX_DEFINITIVEDOCUMENTVIEW_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_DefinitiveDocumentView1 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27572748827789L;
    /** Last Updated Timestamp 2010-11-24 14:01:51.0 */
    public static final long updatedMS = 1290623511000L;
    /** AD_Table_ID=1000383 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_DefinitiveDocumentView1");
        
    }
    ;
    
    /** TableName=XX_DefinitiveDocumentView1 */
    public static final String Table_Name="XX_DefinitiveDocumentView1";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set AJUSTES.
    @param AJUSTES AJUSTES */
    public void setAJUSTES (java.math.BigDecimal AJUSTES)
    {
        set_Value ("AJUSTES", AJUSTES);
        
    }
    
    /** Get AJUSTES.
    @return AJUSTES */
    public java.math.BigDecimal getAJUSTES() 
    {
        return get_ValueAsBigDecimal("AJUSTES");
        
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
    
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID < 1) throw new IllegalArgumentException ("C_Order_ID is mandatory.");
        set_Value ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Payment Term.
    @param C_PaymentTerm_ID The terms of Payment (timing, discount) */
    public void setC_PaymentTerm_ID (int C_PaymentTerm_ID)
    {
        if (C_PaymentTerm_ID < 1) throw new IllegalArgumentException ("C_PaymentTerm_ID is mandatory.");
        set_Value ("C_PaymentTerm_ID", Integer.valueOf(C_PaymentTerm_ID));
        
    }
    
    /** Get Payment Term.
    @return The terms of Payment (timing, discount) */
    public int getC_PaymentTerm_ID() 
    {
        return get_ValueAsInt("C_PaymentTerm_ID");
        
    }
    
    /** Set DESCUENTOS.
    @param DESCUENTOS DESCUENTOS */
    public void setDESCUENTOS (java.math.BigDecimal DESCUENTOS)
    {
        set_Value ("DESCUENTOS", DESCUENTOS);
        
    }
    
    /** Get DESCUENTOS.
    @return DESCUENTOS */
    public java.math.BigDecimal getDESCUENTOS() 
    {
        return get_ValueAsBigDecimal("DESCUENTOS");
        
    }
    
    /** Set DISCOUNTS.
    @param DISCOUNTS DISCOUNTS */
    public void setDISCOUNTS (String DISCOUNTS)
    {
        set_Value ("DISCOUNTS", DISCOUNTS);
        
    }
    
    /** Get DISCOUNTS.
    @return DISCOUNTS */
    public String getDISCOUNTS() 
    {
        return (String)get_Value("DISCOUNTS");
        
    }
    
    /** Set INVOICELIST.
    @param INVOICELIST INVOICELIST */
    public void setINVOICELIST (String INVOICELIST)
    {
        set_Value ("INVOICELIST", INVOICELIST);
        
    }
    
    /** Get INVOICELIST.
    @return INVOICELIST */
    public String getINVOICELIST() 
    {
        return (String)get_Value("INVOICELIST");
        
    }
    
    /** Set Shipment/Receipt.
    @param M_InOut_ID Material Shipment Document */
    public void setM_InOut_ID (int M_InOut_ID)
    {
        if (M_InOut_ID < 1) throw new IllegalArgumentException ("M_InOut_ID is mandatory.");
        set_Value ("M_InOut_ID", Integer.valueOf(M_InOut_ID));
        
    }
    
    /** Get Shipment/Receipt.
    @return Material Shipment Document */
    public int getM_InOut_ID() 
    {
        return get_ValueAsInt("M_InOut_ID");
        
    }
    
    /** Set PLACEDORDERS.
    @param PLACEDORDERS PLACEDORDERS */
    public void setPLACEDORDERS (String PLACEDORDERS)
    {
        set_Value ("PLACEDORDERS", PLACEDORDERS);
        
    }
    
    /** Get PLACEDORDERS.
    @return PLACEDORDERS */
    public String getPLACEDORDERS() 
    {
        return (String)get_Value("PLACEDORDERS");
        
    }
    
    /** Set SUBTOTALAJUSTADO.
    @param SUBTOTALAJUSTADO SUBTOTALAJUSTADO */
    public void setSUBTOTALAJUSTADO (java.math.BigDecimal SUBTOTALAJUSTADO)
    {
        set_Value ("SUBTOTALAJUSTADO", SUBTOTALAJUSTADO);
        
    }
    
    /** Get SUBTOTALAJUSTADO.
    @return SUBTOTALAJUSTADO */
    public java.math.BigDecimal getSUBTOTALAJUSTADO() 
    {
        return get_ValueAsBigDecimal("SUBTOTALAJUSTADO");
        
    }
    
    /** Set SUBTOTALCOSTOBRUTO.
    @param SUBTOTALCOSTOBRUTO SUBTOTALCOSTOBRUTO */
    public void setSUBTOTALCOSTOBRUTO (java.math.BigDecimal SUBTOTALCOSTOBRUTO)
    {
        set_Value ("SUBTOTALCOSTOBRUTO", SUBTOTALCOSTOBRUTO);
        
    }
    
    /** Get SUBTOTALCOSTOBRUTO.
    @return SUBTOTALCOSTOBRUTO */
    public java.math.BigDecimal getSUBTOTALCOSTOBRUTO() 
    {
        return get_ValueAsBigDecimal("SUBTOTALCOSTOBRUTO");
        
    }
    
    /** Set XX_DEFINITIVEDOCUMENTVIEW_ID.
    @param XX_DEFINITIVEDOCUMENTVIEW_ID XX_DEFINITIVEDOCUMENTVIEW_ID */
    public void setXX_DEFINITIVEDOCUMENTVIEW_ID (int XX_DEFINITIVEDOCUMENTVIEW_ID)
    {
        if (XX_DEFINITIVEDOCUMENTVIEW_ID < 1) throw new IllegalArgumentException ("XX_DEFINITIVEDOCUMENTVIEW_ID is mandatory.");
        set_Value ("XX_DEFINITIVEDOCUMENTVIEW_ID", Integer.valueOf(XX_DEFINITIVEDOCUMENTVIEW_ID));
        
    }
    
    /** Get XX_DEFINITIVEDOCUMENTVIEW_ID.
    @return XX_DEFINITIVEDOCUMENTVIEW_ID */
    public int getXX_DEFINITIVEDOCUMENTVIEW_ID() 
    {
        return get_ValueAsInt("XX_DEFINITIVEDOCUMENTVIEW_ID");
        
    }
    
    /** Set XX_EXPIRATIONDATE.
    @param XX_EXPIRATIONDATE XX_EXPIRATIONDATE */
    public void setXX_EXPIRATIONDATE (Timestamp XX_EXPIRATIONDATE)
    {
        set_ValueNoCheck ("XX_EXPIRATIONDATE", XX_EXPIRATIONDATE);
        
    }
    
    /** Get XX_EXPIRATIONDATE.
    @return XX_EXPIRATIONDATE */
    public Timestamp getXX_EXPIRATIONDATE() 
    {
        return (Timestamp)get_Value("XX_EXPIRATIONDATE");
        
    }
    
    /** Set Payment Rule.
    @param XX_VCN_PaymentRule Id de la Tabla Forma de Pago  */
    public void setXX_VCN_PaymentRule (String XX_VCN_PaymentRule)
    {
        set_Value ("XX_VCN_PaymentRule", XX_VCN_PaymentRule);
        
    }
    
    /** Get Payment Rule.
    @return Id de la Tabla Forma de Pago  */
    public String getXX_VCN_PaymentRule() 
    {
        return (String)get_Value("XX_VCN_PaymentRule");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    
}
