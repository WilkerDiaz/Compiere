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
/** Generated Model for I_XX_VCN_INVM14
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VCN_INVM14 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VCN_INVM14_ID id
    @param trx transaction
    */
    public X_I_XX_VCN_INVM14 (Ctx ctx, int I_XX_VCN_INVM14_ID, Trx trx)
    {
        super (ctx, I_XX_VCN_INVM14_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VCN_INVM14_ID == 0)
        {
            setI_XX_VCN_INVM14_ID (0);
            setProcessed (false);	// N
            setProcessing (false);	// N
            setXX_ANOINV (Env.ZERO);
            setXX_CANTAJUANT (Env.ZERO);
            setXX_CANTAJUSTE (Env.ZERO);
            setXX_CANTCOMPRA (Env.ZERO);
            setXX_CANTINVINI (Env.ZERO);
            setXX_CANTMOVIMI (Env.ZERO);
            setXX_CANTVENTAS (Env.ZERO);
            setXX_CodCat (0);
            setXX_CodDep (0);
            setXX_CodLin (0);
            setXX_CODPRO (0);
            setXX_Codsec (0);
            setXX_CONPRE (Env.ZERO);
            setXX_MESINV (Env.ZERO);
            setXX_MONTAJUANT (Env.ZERO);
            setXX_MONTAJUSTE (Env.ZERO);
            setXX_MONTCOMPRA (Env.ZERO);
            setXX_MONTINVINI (Env.ZERO);
            setXX_MONTMOVIMI (Env.ZERO);
            setXX_MONTVENTAS (Env.ZERO);
            setXX_TIENDA (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VCN_INVM14 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27569389488789L;
    /** Last Updated Timestamp 2010-10-16 16:52:52.0 */
    public static final long updatedMS = 1287264172000L;
    /** AD_Table_ID=1000033 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VCN_INVM14");
        
    }
    ;
    
    /** TableName=I_XX_VCN_INVM14 */
    public static final String Table_Name="I_XX_VCN_INVM14";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Import Error Message.
    @param I_ErrorMsg Messages generated from import process */
    public void setI_ErrorMsg (boolean I_ErrorMsg)
    {
        set_Value ("I_ErrorMsg", Boolean.valueOf(I_ErrorMsg));
        
    }
    
    /** Get Import Error Message.
    @return Messages generated from import process */
    public boolean isI_ErrorMsg() 
    {
        return get_ValueAsBoolean("I_ErrorMsg");
        
    }
    
    /** Error = E */
    public static final String I_ISIMPORTED_Error = X_Ref__IsImported.ERROR.getValue();
    /** No = N */
    public static final String I_ISIMPORTED_No = X_Ref__IsImported.NO.getValue();
    /** Yes = Y */
    public static final String I_ISIMPORTED_Yes = X_Ref__IsImported.YES.getValue();
    /** Set Imported.
    @param I_IsImported Has this import been processed? */
    public void setI_IsImported (String I_IsImported)
    {
        if (!X_Ref__IsImported.isValid(I_IsImported))
        throw new IllegalArgumentException ("I_IsImported Invalid value - " + I_IsImported + " - Reference_ID=420 - E - N - Y");
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set I_XX_VCN_INVM14_ID.
    @param I_XX_VCN_INVM14_ID I_XX_VCN_INVM14_ID */
    public void setI_XX_VCN_INVM14_ID (int I_XX_VCN_INVM14_ID)
    {
        if (I_XX_VCN_INVM14_ID < 1) throw new IllegalArgumentException ("I_XX_VCN_INVM14_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VCN_INVM14_ID", Integer.valueOf(I_XX_VCN_INVM14_ID));
        
    }
    
    /** Get I_XX_VCN_INVM14_ID.
    @return I_XX_VCN_INVM14_ID */
    public int getI_XX_VCN_INVM14_ID() 
    {
        return get_ValueAsInt("I_XX_VCN_INVM14_ID");
        
    }
    
    /** Set Lot No.
    @param Lot Lot number (alphanumeric) */
    public void setLot (String Lot)
    {
        set_Value ("Lot", Lot);
        
    }
    
    /** Get Lot No.
    @return Lot number (alphanumeric) */
    public String getLot() 
    {
        return (String)get_Value("Lot");
        
    }
    
    /** Set Attribute Set Instance.
    @param M_AttributeSetInstance_ID Product Attribute Set Instance */
    public void setM_AttributeSetInstance_ID (int M_AttributeSetInstance_ID)
    {
        if (M_AttributeSetInstance_ID <= 0) set_Value ("M_AttributeSetInstance_ID", null);
        else
        set_Value ("M_AttributeSetInstance_ID", Integer.valueOf(M_AttributeSetInstance_ID));
        
    }
    
    /** Get Attribute Set Instance.
    @return Product Attribute Set Instance */
    public int getM_AttributeSetInstance_ID() 
    {
        return get_ValueAsInt("M_AttributeSetInstance_ID");
        
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
    
    /** Set Inventory Year.
    @param XX_ANOINV Ano de Inventario */
    public void setXX_ANOINV (java.math.BigDecimal XX_ANOINV)
    {
        if (XX_ANOINV == null) throw new IllegalArgumentException ("XX_ANOINV is mandatory.");
        set_Value ("XX_ANOINV", XX_ANOINV);
        
    }
    
    /** Get Inventory Year.
    @return Ano de Inventario */
    public java.math.BigDecimal getXX_ANOINV() 
    {
        return get_ValueAsBigDecimal("XX_ANOINV");
        
    }
    
    /** Set Previous Adjustments Quantity.
    @param XX_CANTAJUANT Cantidad de Ajuste Anterior */
    public void setXX_CANTAJUANT (java.math.BigDecimal XX_CANTAJUANT)
    {
        if (XX_CANTAJUANT == null) throw new IllegalArgumentException ("XX_CANTAJUANT is mandatory.");
        set_Value ("XX_CANTAJUANT", XX_CANTAJUANT);
        
    }
    
    /** Get Previous Adjustments Quantity.
    @return Cantidad de Ajuste Anterior */
    public java.math.BigDecimal getXX_CANTAJUANT() 
    {
        return get_ValueAsBigDecimal("XX_CANTAJUANT");
        
    }
    
    /** Set AdjustmentsQuantity.
    @param XX_CANTAJUSTE Cantidad de Ajustes */
    public void setXX_CANTAJUSTE (java.math.BigDecimal XX_CANTAJUSTE)
    {
        if (XX_CANTAJUSTE == null) throw new IllegalArgumentException ("XX_CANTAJUSTE is mandatory.");
        set_Value ("XX_CANTAJUSTE", XX_CANTAJUSTE);
        
    }
    
    /** Get AdjustmentsQuantity.
    @return Cantidad de Ajustes */
    public java.math.BigDecimal getXX_CANTAJUSTE() 
    {
        return get_ValueAsBigDecimal("XX_CANTAJUSTE");
        
    }
    
    /** Set Shopping Quantity.
    @param XX_CANTCOMPRA Cantidad de Compras */
    public void setXX_CANTCOMPRA (java.math.BigDecimal XX_CANTCOMPRA)
    {
        if (XX_CANTCOMPRA == null) throw new IllegalArgumentException ("XX_CANTCOMPRA is mandatory.");
        set_Value ("XX_CANTCOMPRA", XX_CANTCOMPRA);
        
    }
    
    /** Get Shopping Quantity.
    @return Cantidad de Compras */
    public java.math.BigDecimal getXX_CANTCOMPRA() 
    {
        return get_ValueAsBigDecimal("XX_CANTCOMPRA");
        
    }
    
    /** Set Initial Inventory Quantity.
    @param XX_CANTINVINI Cantidad de Inventario Inicial */
    public void setXX_CANTINVINI (java.math.BigDecimal XX_CANTINVINI)
    {
        if (XX_CANTINVINI == null) throw new IllegalArgumentException ("XX_CANTINVINI is mandatory.");
        set_Value ("XX_CANTINVINI", XX_CANTINVINI);
        
    }
    
    /** Get Initial Inventory Quantity.
    @return Cantidad de Inventario Inicial */
    public java.math.BigDecimal getXX_CANTINVINI() 
    {
        return get_ValueAsBigDecimal("XX_CANTINVINI");
        
    }
    
    /** Set Movement Quantity.
    @param XX_CANTMOVIMI Cantidad de Movimientos */
    public void setXX_CANTMOVIMI (java.math.BigDecimal XX_CANTMOVIMI)
    {
        if (XX_CANTMOVIMI == null) throw new IllegalArgumentException ("XX_CANTMOVIMI is mandatory.");
        set_Value ("XX_CANTMOVIMI", XX_CANTMOVIMI);
        
    }
    
    /** Get Movement Quantity.
    @return Cantidad de Movimientos */
    public java.math.BigDecimal getXX_CANTMOVIMI() 
    {
        return get_ValueAsBigDecimal("XX_CANTMOVIMI");
        
    }
    
    /** Set Sales Quantity.
    @param XX_CANTVENTAS Cantidad de Ventas */
    public void setXX_CANTVENTAS (java.math.BigDecimal XX_CANTVENTAS)
    {
        if (XX_CANTVENTAS == null) throw new IllegalArgumentException ("XX_CANTVENTAS is mandatory.");
        set_Value ("XX_CANTVENTAS", XX_CANTVENTAS);
        
    }
    
    /** Get Sales Quantity.
    @return Cantidad de Ventas */
    public java.math.BigDecimal getXX_CANTVENTAS() 
    {
        return get_ValueAsBigDecimal("XX_CANTVENTAS");
        
    }
    
    /** Set Category Code.
    @param XX_CodCat Codigo de Categoria */
    public void setXX_CodCat (int XX_CodCat)
    {
        set_Value ("XX_CodCat", Integer.valueOf(XX_CodCat));
        
    }
    
    /** Get Category Code.
    @return Codigo de Categoria */
    public int getXX_CodCat() 
    {
        return get_ValueAsInt("XX_CodCat");
        
    }
    
    /** Set Department Code	.
    @param XX_CodDep Codigo del Departameto */
    public void setXX_CodDep (int XX_CodDep)
    {
        set_Value ("XX_CodDep", Integer.valueOf(XX_CodDep));
        
    }
    
    /** Get Department Code	.
    @return Codigo del Departameto */
    public int getXX_CodDep() 
    {
        return get_ValueAsInt("XX_CodDep");
        
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
    
    /** Set Code Product.
    @param XX_CODPRO Codigo del Producto */
    public void setXX_CODPRO (int XX_CODPRO)
    {
        set_Value ("XX_CODPRO", Integer.valueOf(XX_CODPRO));
        
    }
    
    /** Get Code Product.
    @return Codigo del Producto */
    public int getXX_CODPRO() 
    {
        return get_ValueAsInt("XX_CODPRO");
        
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
    
    /** Set Consecutive Price.
    @param XX_CONPRE Consecutivo de Precio */
    public void setXX_CONPRE (java.math.BigDecimal XX_CONPRE)
    {
        if (XX_CONPRE == null) throw new IllegalArgumentException ("XX_CONPRE is mandatory.");
        set_Value ("XX_CONPRE", XX_CONPRE);
        
    }
    
    /** Get Consecutive Price.
    @return Consecutivo de Precio */
    public java.math.BigDecimal getXX_CONPRE() 
    {
        return get_ValueAsBigDecimal("XX_CONPRE");
        
    }
    
    /** Set Inventory Month.
    @param XX_MESINV Mes de Inventario */
    public void setXX_MESINV (java.math.BigDecimal XX_MESINV)
    {
        if (XX_MESINV == null) throw new IllegalArgumentException ("XX_MESINV is mandatory.");
        set_Value ("XX_MESINV", XX_MESINV);
        
    }
    
    /** Get Inventory Month.
    @return Mes de Inventario */
    public java.math.BigDecimal getXX_MESINV() 
    {
        return get_ValueAsBigDecimal("XX_MESINV");
        
    }
    
    /** Set Previous Adjustments Amount.
    @param XX_MONTAJUANT Monto de Ajuste Anterior */
    public void setXX_MONTAJUANT (java.math.BigDecimal XX_MONTAJUANT)
    {
        if (XX_MONTAJUANT == null) throw new IllegalArgumentException ("XX_MONTAJUANT is mandatory.");
        set_Value ("XX_MONTAJUANT", XX_MONTAJUANT);
        
    }
    
    /** Get Previous Adjustments Amount.
    @return Monto de Ajuste Anterior */
    public java.math.BigDecimal getXX_MONTAJUANT() 
    {
        return get_ValueAsBigDecimal("XX_MONTAJUANT");
        
    }
    
    /** Set Adjustments Amount.
    @param XX_MONTAJUSTE Monto de Ajuste */
    public void setXX_MONTAJUSTE (java.math.BigDecimal XX_MONTAJUSTE)
    {
        if (XX_MONTAJUSTE == null) throw new IllegalArgumentException ("XX_MONTAJUSTE is mandatory.");
        set_Value ("XX_MONTAJUSTE", XX_MONTAJUSTE);
        
    }
    
    /** Get Adjustments Amount.
    @return Monto de Ajuste */
    public java.math.BigDecimal getXX_MONTAJUSTE() 
    {
        return get_ValueAsBigDecimal("XX_MONTAJUSTE");
        
    }
    
    /** Set Shopping Amount.
    @param XX_MONTCOMPRA Monto de Compras */
    public void setXX_MONTCOMPRA (java.math.BigDecimal XX_MONTCOMPRA)
    {
        if (XX_MONTCOMPRA == null) throw new IllegalArgumentException ("XX_MONTCOMPRA is mandatory.");
        set_Value ("XX_MONTCOMPRA", XX_MONTCOMPRA);
        
    }
    
    /** Get Shopping Amount.
    @return Monto de Compras */
    public java.math.BigDecimal getXX_MONTCOMPRA() 
    {
        return get_ValueAsBigDecimal("XX_MONTCOMPRA");
        
    }
    
    /** Set Initial Inventory Amount.
    @param XX_MONTINVINI Monto de Inventario Inicial */
    public void setXX_MONTINVINI (java.math.BigDecimal XX_MONTINVINI)
    {
        if (XX_MONTINVINI == null) throw new IllegalArgumentException ("XX_MONTINVINI is mandatory.");
        set_Value ("XX_MONTINVINI", XX_MONTINVINI);
        
    }
    
    /** Get Initial Inventory Amount.
    @return Monto de Inventario Inicial */
    public java.math.BigDecimal getXX_MONTINVINI() 
    {
        return get_ValueAsBigDecimal("XX_MONTINVINI");
        
    }
    
    /** Set Movement Amount.
    @param XX_MONTMOVIMI Monto de Movimiento */
    public void setXX_MONTMOVIMI (java.math.BigDecimal XX_MONTMOVIMI)
    {
        if (XX_MONTMOVIMI == null) throw new IllegalArgumentException ("XX_MONTMOVIMI is mandatory.");
        set_Value ("XX_MONTMOVIMI", XX_MONTMOVIMI);
        
    }
    
    /** Get Movement Amount.
    @return Monto de Movimiento */
    public java.math.BigDecimal getXX_MONTMOVIMI() 
    {
        return get_ValueAsBigDecimal("XX_MONTMOVIMI");
        
    }
    
    /** Set Sales Amount.
    @param XX_MONTVENTAS Monto de Ventas */
    public void setXX_MONTVENTAS (java.math.BigDecimal XX_MONTVENTAS)
    {
        if (XX_MONTVENTAS == null) throw new IllegalArgumentException ("XX_MONTVENTAS is mandatory.");
        set_Value ("XX_MONTVENTAS", XX_MONTVENTAS);
        
    }
    
    /** Get Sales Amount.
    @return Monto de Ventas */
    public java.math.BigDecimal getXX_MONTVENTAS() 
    {
        return get_ValueAsBigDecimal("XX_MONTVENTAS");
        
    }
    
    /** Set Store.
    @param XX_TIENDA Tienda */
    public void setXX_TIENDA (int XX_TIENDA)
    {
        set_Value ("XX_TIENDA", Integer.valueOf(XX_TIENDA));
        
    }
    
    /** Get Store.
    @return Tienda */
    public int getXX_TIENDA() 
    {
        return get_ValueAsInt("XX_TIENDA");
        
    }
    
    /** Set XX_VCN_INVENTORY_ID.
    @param XX_VCN_INVENTORY_ID Id de la Tabla de Inventario */
    public void setXX_VCN_INVENTORY_ID (int XX_VCN_INVENTORY_ID)
    {
        if (XX_VCN_INVENTORY_ID <= 0) set_Value ("XX_VCN_INVENTORY_ID", null);
        else
        set_Value ("XX_VCN_INVENTORY_ID", Integer.valueOf(XX_VCN_INVENTORY_ID));
        
    }
    
    /** Get XX_VCN_INVENTORY_ID.
    @return Id de la Tabla de Inventario */
    public int getXX_VCN_INVENTORY_ID() 
    {
        return get_ValueAsInt("XX_VCN_INVENTORY_ID");
        
    }
    
    /** Set Category.
    @param XX_VMR_Category_ID Category */
    public void setXX_VMR_Category_ID (int XX_VMR_Category_ID)
    {
        if (XX_VMR_Category_ID <= 0) set_Value ("XX_VMR_Category_ID", null);
        else
        set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));
        
    }
    
    /** Get Category.
    @return Category */
    public int getXX_VMR_Category_ID() 
    {
        return get_ValueAsInt("XX_VMR_Category_ID");
        
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
    
    
}
