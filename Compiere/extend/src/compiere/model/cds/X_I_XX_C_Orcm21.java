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
/** Generated Model for I_XX_C_Orcm21
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_C_Orcm21 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_C_Orcm21_ID id
    @param trx transaction
    */
    public X_I_XX_C_Orcm21 (Ctx ctx, int I_XX_C_Orcm21_ID, Trx trx)
    {
        super (ctx, I_XX_C_Orcm21_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_C_Orcm21_ID == 0)
        {
            setI_XX_C_ORCM21_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_C_Orcm21 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27565921212789L;
    /** Last Updated Timestamp 2010-09-06 13:28:16.0 */
    public static final long updatedMS = 1283795896000L;
    /** AD_Table_ID=1000157 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_C_Orcm21");
        
    }
    ;
    
    /** TableName=I_XX_C_Orcm21 */
    public static final String Table_Name="I_XX_C_Orcm21";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Tax Category.
    @param C_TaxCategory_ID Tax Category */
    public void setC_TaxCategory_ID (int C_TaxCategory_ID)
    {
        if (C_TaxCategory_ID <= 0) set_Value ("C_TaxCategory_ID", null);
        else
        set_Value ("C_TaxCategory_ID", Integer.valueOf(C_TaxCategory_ID));
        
    }
    
    /** Get Tax Category.
    @return Tax Category */
    public int getC_TaxCategory_ID() 
    {
        return get_ValueAsInt("C_TaxCategory_ID");
        
    }
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (int DocumentNo)
    {
        set_Value ("DocumentNo", Integer.valueOf(DocumentNo));
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public int getDocumentNo() 
    {
        return get_ValueAsInt("DocumentNo");
        
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
    public void setI_IsImported (String I_IsImported)
    {
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set I_XX_C_ORCM21_ID.
    @param I_XX_C_ORCM21_ID I_XX_C_ORCM21_ID */
    public void setI_XX_C_ORCM21_ID (int I_XX_C_ORCM21_ID)
    {
        if (I_XX_C_ORCM21_ID < 1) throw new IllegalArgumentException ("I_XX_C_ORCM21_ID is mandatory.");
        set_ValueNoCheck ("I_XX_C_ORCM21_ID", Integer.valueOf(I_XX_C_ORCM21_ID));
        
    }
    
    /** Get I_XX_C_ORCM21_ID.
    @return I_XX_C_ORCM21_ID */
    public int getI_XX_C_ORCM21_ID() 
    {
        return get_ValueAsInt("I_XX_C_ORCM21_ID");
        
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
    
    /** Set XX_CANEMC.
    @param XX_CANEMC XX_CANEMC */
    public void setXX_CANEMC (int XX_CANEMC)
    {
        set_Value ("XX_CANEMC", Integer.valueOf(XX_CANEMC));
        
    }
    
    /** Get XX_CANEMC.
    @return XX_CANEMC */
    public int getXX_CANEMC() 
    {
        return get_ValueAsInt("XX_CANEMC");
        
    }
    
    /** Set XX_CANEMV.
    @param XX_CANEMV XX_CANEMV */
    public void setXX_CANEMV (int XX_CANEMV)
    {
        set_Value ("XX_CANEMV", Integer.valueOf(XX_CANEMV));
        
    }
    
    /** Get XX_CANEMV.
    @return XX_CANEMV */
    public int getXX_CANEMV() 
    {
        return get_ValueAsInt("XX_CANEMV");
        
    }
    
    /** Set Characteristic 1.
    @param XX_Characteristic1_ID Characteristic 1 */
    public void setXX_Characteristic1_ID (int XX_Characteristic1_ID)
    {
        if (XX_Characteristic1_ID <= 0) set_Value ("XX_Characteristic1_ID", null);
        else
        set_Value ("XX_Characteristic1_ID", Integer.valueOf(XX_Characteristic1_ID));
        
    }
    
    /** Get Characteristic 1.
    @return Characteristic 1 */
    public int getXX_Characteristic1_ID() 
    {
        return get_ValueAsInt("XX_Characteristic1_ID");
        
    }
    
    /** Set Characteristic 2.
    @param XX_Characteristic2_ID Characteristic 2 */
    public void setXX_Characteristic2_ID (int XX_Characteristic2_ID)
    {
        if (XX_Characteristic2_ID <= 0) set_Value ("XX_Characteristic2_ID", null);
        else
        set_Value ("XX_Characteristic2_ID", Integer.valueOf(XX_Characteristic2_ID));
        
    }
    
    /** Get Characteristic 2.
    @return Characteristic 2 */
    public int getXX_Characteristic2_ID() 
    {
        return get_ValueAsInt("XX_Characteristic2_ID");
        
    }
    
    /** Set Code Line.
    @param XX_CodLin Codigo de Linea */
    public void setXX_CodLin (int XX_CodLin)
    {
        set_Value ("XX_CodLin", Integer.valueOf(XX_CodLin));
        
    }
    
    /** Get Code Line.
    @return Codigo de Linea */
    public int getXX_CodLin() 
    {
        return get_ValueAsInt("XX_CodLin");
        
    }
    
    /** Set Code Section.
    @param XX_Codsec Codigo de Seccion */
    public void setXX_Codsec (int XX_Codsec)
    {
        set_Value ("XX_Codsec", Integer.valueOf(XX_Codsec));
        
    }
    
    /** Get Code Section.
    @return Codigo de Seccion */
    public int getXX_Codsec() 
    {
        return get_ValueAsInt("XX_Codsec");
        
    }
    
    /** Set Descuento 1.
    @param XX_DESCU1 Discount 1 - Descuento 1 */
    public void setXX_DESCU1 (int XX_DESCU1)
    {
        set_Value ("XX_DESCU1", Integer.valueOf(XX_DESCU1));
        
    }
    
    /** Get Descuento 1.
    @return Discount 1 - Descuento 1 */
    public int getXX_DESCU1() 
    {
        return get_ValueAsInt("XX_DESCU1");
        
    }
    
    /** Set XX_DESCU2.
    @param XX_DESCU2 Discount 2 - Descuento 2 */
    public void setXX_DESCU2 (int XX_DESCU2)
    {
        set_Value ("XX_DESCU2", Integer.valueOf(XX_DESCU2));
        
    }
    
    /** Get XX_DESCU2.
    @return Discount 2 - Descuento 2 */
    public int getXX_DESCU2() 
    {
        return get_ValueAsInt("XX_DESCU2");
        
    }
    
    /** Set XX_DESCU3.
    @param XX_DESCU3 Discount 3 - Descuento 3 */
    public void setXX_DESCU3 (int XX_DESCU3)
    {
        set_Value ("XX_DESCU3", Integer.valueOf(XX_DESCU3));
        
    }
    
    /** Get XX_DESCU3.
    @return Discount 3 - Descuento 3 */
    public int getXX_DESCU3() 
    {
        return get_ValueAsInt("XX_DESCU3");
        
    }
    
    /** Set XX_DESCU4.
    @param XX_DESCU4 Discount 4 - Descuento 4 */
    public void setXX_DESCU4 (int XX_DESCU4)
    {
        set_Value ("XX_DESCU4", Integer.valueOf(XX_DESCU4));
        
    }
    
    /** Get XX_DESCU4.
    @return Discount 4 - Descuento 4 */
    public int getXX_DESCU4() 
    {
        return get_ValueAsInt("XX_DESCU4");
        
    }
    
    /** Set XX_IMPVEN.
    @param XX_IMPVEN Tax Amount - Impuesto de Venta */
    public void setXX_IMPVEN (int XX_IMPVEN)
    {
        set_Value ("XX_IMPVEN", Integer.valueOf(XX_IMPVEN));
        
    }
    
    /** Get XX_IMPVEN.
    @return Tax Amount - Impuesto de Venta */
    public int getXX_IMPVEN() 
    {
        return get_ValueAsInt("XX_IMPVEN");
        
    }
    
    /** Set XX_MARGEN.
    @param XX_MARGEN Margin - Margen */
    public void setXX_MARGEN (int XX_MARGEN)
    {
        set_Value ("XX_MARGEN", Integer.valueOf(XX_MARGEN));
        
    }
    
    /** Get XX_MARGEN.
    @return Margin - Margen */
    public int getXX_MARGEN() 
    {
        return get_ValueAsInt("XX_MARGEN");
        
    }
    
    /** Set XX_MCOSTO.
    @param XX_MCOSTO Unit Purchase Price - Costo de Compra Unitario */
    public void setXX_MCOSTO (int XX_MCOSTO)
    {
        set_Value ("XX_MCOSTO", Integer.valueOf(XX_MCOSTO));
        
    }
    
    /** Get XX_MCOSTO.
    @return Unit Purchase Price - Costo de Compra Unitario */
    public int getXX_MCOSTO() 
    {
        return get_ValueAsInt("XX_MCOSTO");
        
    }
    
    /** Set XX_MCOSVE.
    @param XX_MCOSVE Sale Cost - Costo Venta */
    public void setXX_MCOSVE (int XX_MCOSVE)
    {
        set_Value ("XX_MCOSVE", Integer.valueOf(XX_MCOSVE));
        
    }
    
    /** Get XX_MCOSVE.
    @return Sale Cost - Costo Venta */
    public int getXX_MCOSVE() 
    {
        return get_ValueAsInt("XX_MCOSVE");
        
    }
    
    /** Set XX_MULEMP.
    @param XX_MULEMP Package Multiple - Multiplo Empaque */
    public void setXX_MULEMP (int XX_MULEMP)
    {
        set_Value ("XX_MULEMP", Integer.valueOf(XX_MULEMP));
        
    }
    
    /** Get XX_MULEMP.
    @return Package Multiple - Multiplo Empaque */
    public int getXX_MULEMP() 
    {
        return get_ValueAsInt("XX_MULEMP");
        
    }
    
    /** Set XX_MVENTA.
    @param XX_MVENTA Unit Sale Price - Costo de Venta Unitario */
    public void setXX_MVENTA (int XX_MVENTA)
    {
        set_Value ("XX_MVENTA", Integer.valueOf(XX_MVENTA));
        
    }
    
    /** Get XX_MVENTA.
    @return Unit Sale Price - Costo de Venta Unitario */
    public int getXX_MVENTA() 
    {
        return get_ValueAsInt("XX_MVENTA");
        
    }
    
    /** Set XX_REFPRO.
    @param XX_REFPRO Prov. Reference - Referencia Proveedor */
    public void setXX_REFPRO (String XX_REFPRO)
    {
        set_Value ("XX_REFPRO", XX_REFPRO);
        
    }
    
    /** Get XX_REFPRO.
    @return Prov. Reference - Referencia Proveedor */
    public String getXX_REFPRO() 
    {
        return (String)get_Value("XX_REFPRO");
        
    }
    
    /** Set Sale Unit.
    @param XX_SaleUnit_ID Sale Unit */
    public void setXX_SaleUnit_ID (int XX_SaleUnit_ID)
    {
        if (XX_SaleUnit_ID <= 0) set_Value ("XX_SaleUnit_ID", null);
        else
        set_Value ("XX_SaleUnit_ID", Integer.valueOf(XX_SaleUnit_ID));
        
    }
    
    /** Get Sale Unit.
    @return Sale Unit */
    public int getXX_SaleUnit_ID() 
    {
        return get_ValueAsInt("XX_SaleUnit_ID");
        
    }
    
    /** Set XX_TIPCAR1.
    @param XX_TIPCAR1 Characteristic Type 1 - Tipo Caracteristica 1 */
    public void setXX_TIPCAR1 (int XX_TIPCAR1)
    {
        set_Value ("XX_TIPCAR1", Integer.valueOf(XX_TIPCAR1));
        
    }
    
    /** Get XX_TIPCAR1.
    @return Characteristic Type 1 - Tipo Caracteristica 1 */
    public int getXX_TIPCAR1() 
    {
        return get_ValueAsInt("XX_TIPCAR1");
        
    }
    
    /** Set XX_TIPCAR2.
    @param XX_TIPCAR2 Characteristic Type 2 - Tipo Caracteristica 2 */
    public void setXX_TIPCAR2 (int XX_TIPCAR2)
    {
        set_Value ("XX_TIPCAR2", Integer.valueOf(XX_TIPCAR2));
        
    }
    
    /** Get XX_TIPCAR2.
    @return Characteristic Type 2 - Tipo Caracteristica 2 */
    public int getXX_TIPCAR2() 
    {
        return get_ValueAsInt("XX_TIPCAR2");
        
    }
    
    /** Set XX_TIPIMP.
    @param XX_TIPIMP Tax ID - Codigo Impuesto */
    public void setXX_TIPIMP (int XX_TIPIMP)
    {
        set_Value ("XX_TIPIMP", Integer.valueOf(XX_TIPIMP));
        
    }
    
    /** Get XX_TIPIMP.
    @return Tax ID - Codigo Impuesto */
    public int getXX_TIPIMP() 
    {
        return get_ValueAsInt("XX_TIPIMP");
        
    }
    
    /** Set XX_UNICOM.
    @param XX_UNICOM Purchase Unit - Unidad de Compra */
    public void setXX_UNICOM (String XX_UNICOM)
    {
        set_Value ("XX_UNICOM", XX_UNICOM);
        
    }
    
    /** Get XX_UNICOM.
    @return Purchase Unit - Unidad de Compra */
    public String getXX_UNICOM() 
    {
        return (String)get_Value("XX_UNICOM");
        
    }
    
    /** Set XX_UNIVEN.
    @param XX_UNIVEN Sale Unit - Unidad de Venta */
    public void setXX_UNIVEN (String XX_UNIVEN)
    {
        set_Value ("XX_UNIVEN", XX_UNIVEN);
        
    }
    
    /** Get XX_UNIVEN.
    @return Sale Unit - Unidad de Venta */
    public String getXX_UNIVEN() 
    {
        return (String)get_Value("XX_UNIVEN");
        
    }
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID <= 0) set_Value ("XX_VMR_Line_ID", null);
        else
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    /** Set Purchase Unit.
    @param XX_VMR_UnitPurchase_ID Purchase Unit */
    public void setXX_VMR_UnitPurchase_ID (int XX_VMR_UnitPurchase_ID)
    {
        if (XX_VMR_UnitPurchase_ID <= 0) set_Value ("XX_VMR_UnitPurchase_ID", null);
        else
        set_Value ("XX_VMR_UnitPurchase_ID", Integer.valueOf(XX_VMR_UnitPurchase_ID));
        
    }
    
    /** Get Purchase Unit.
    @return Purchase Unit */
    public int getXX_VMR_UnitPurchase_ID() 
    {
        return get_ValueAsInt("XX_VMR_UnitPurchase_ID");
        
    }
    
    
}
