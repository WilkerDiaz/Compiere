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
/** Generated Model for I_XX_VMR_Solm01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VMR_Solm01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_Solm01_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_Solm01 (Ctx ctx, int I_XX_VMR_Solm01_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_Solm01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_Solm01_ID == 0)
        {
            setI_XX_VMR_SOLM01_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_Solm01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27566100594789L;
    /** Last Updated Timestamp 2010-09-08 15:17:58.0 */
    public static final long updatedMS = 1283975278000L;
    /** AD_Table_ID=1000053 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_Solm01");
        
    }
    ;
    
    /** TableName=I_XX_VMR_Solm01 */
    public static final String Table_Name="I_XX_VMR_Solm01";
    
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
    
    /** Set I_XX_VMR_SOLM01_ID.
    @param I_XX_VMR_SOLM01_ID I_XX_VMR_SOLM01_ID */
    public void setI_XX_VMR_SOLM01_ID (int I_XX_VMR_SOLM01_ID)
    {
        if (I_XX_VMR_SOLM01_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_SOLM01_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_SOLM01_ID", Integer.valueOf(I_XX_VMR_SOLM01_ID));
        
    }
    
    /** Get I_XX_VMR_SOLM01_ID.
    @return I_XX_VMR_SOLM01_ID */
    public int getI_XX_VMR_SOLM01_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_SOLM01_ID");
        
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
    
    /** Set Year Registration.
    @param XX_AñoReg Año de registro */
    public void setXX_AñoReg (java.math.BigDecimal XX_AñoReg)
    {
        set_Value ("XX_AñoReg", XX_AñoReg);
        
    }
    
    /** Get Year Registration.
    @return Año de registro */
    public java.math.BigDecimal getXX_AñoReg() 
    {
        return get_ValueAsBigDecimal("XX_AñoReg");
        
    }
    
    /** Set Application Year.
    @param XX_AÑOSOL Año de Solicitud */
    public void setXX_AÑOSOL (java.math.BigDecimal XX_AÑOSOL)
    {
        set_Value ("XX_AÑOSOL", XX_AÑOSOL);
        
    }
    
    /** Get Application Year.
    @return Año de Solicitud */
    public java.math.BigDecimal getXX_AÑOSOL() 
    {
        return get_ValueAsBigDecimal("XX_AÑOSOL");
        
    }
    
    /** Set YearStatus|.
    @param XX_AÑOSTA Año de status */
    public void setXX_AÑOSTA (java.math.BigDecimal XX_AÑOSTA)
    {
        set_Value ("XX_AÑOSTA", XX_AÑOSTA);
        
    }
    
    /** Get YearStatus|.
    @return Año de status */
    public java.math.BigDecimal getXX_AÑOSTA() 
    {
        return get_ValueAsBigDecimal("XX_AÑOSTA");
        
    }
    
    /** Set Year Of Release Status.
    @param XX_ASTDES Año de status despacho */
    public void setXX_ASTDES (java.math.BigDecimal XX_ASTDES)
    {
        set_Value ("XX_ASTDES", XX_ASTDES);
        
    }
    
    /** Get Year Of Release Status.
    @return Año de status despacho */
    public java.math.BigDecimal getXX_ASTDES() 
    {
        return get_ValueAsBigDecimal("XX_ASTDES");
        
    }
    
    /** Set Number Of Packages.
    @param XX_CANBUL Cantidad de Bultos */
    public void setXX_CANBUL (java.math.BigDecimal XX_CANBUL)
    {
        set_Value ("XX_CANBUL", XX_CANBUL);
        
    }
    
    /** Get Number Of Packages.
    @return Cantidad de Bultos */
    public java.math.BigDecimal getXX_CANBUL() 
    {
        return get_ValueAsBigDecimal("XX_CANBUL");
        
    }
    
    /** Set Consecutive Price.
    @param XX_CANORI Consecutivo de precio */
    public void setXX_CANORI (java.math.BigDecimal XX_CANORI)
    {
        set_Value ("XX_CANORI", XX_CANORI);
        
    }
    
    /** Get Consecutive Price.
    @return Consecutivo de precio */
    public java.math.BigDecimal getXX_CANORI() 
    {
        return get_ValueAsBigDecimal("XX_CANORI");
        
    }
    
    /** Set Cancellation Code.
    @param XX_CODASO  Codigo de anulacion de solicitud */
    public void setXX_CODASO (String XX_CODASO)
    {
        set_Value ("XX_CODASO", XX_CODASO);
        
    }
    
    /** Get Cancellation Code.
    @return  Codigo de anulacion de solicitud */
    public String getXX_CODASO() 
    {
        return (String)get_Value("XX_CODASO");
        
    }
    
    /** Set Department Code	.
    @param XX_CodDep Codigo del Departameto */
    public void setXX_CodDep (String XX_CodDep)
    {
        set_Value ("XX_CodDep", XX_CodDep);
        
    }
    
    /** Get Department Code	.
    @return Codigo del Departameto */
    public String getXX_CodDep() 
    {
        return (String)get_Value("XX_CodDep");
        
    }
    
    /** Set Quantity Of Product Storage.
    @param XX_CONORI Cantidad de producto deposito */
    public void setXX_CONORI (java.math.BigDecimal XX_CONORI)
    {
        set_Value ("XX_CONORI", XX_CONORI);
        
    }
    
    /** Get Quantity Of Product Storage.
    @return Cantidad de producto deposito */
    public java.math.BigDecimal getXX_CONORI() 
    {
        return get_ValueAsBigDecimal("XX_CONORI");
        
    }
    
    /** Set Application Description.
    @param XX_DESSOL Descripcion de solicitud */
    public void setXX_DESSOL (String XX_DESSOL)
    {
        set_Value ("XX_DESSOL", XX_DESSOL);
        
    }
    
    /** Get Application Description.
    @return Descripcion de solicitud */
    public String getXX_DESSOL() 
    {
        return (String)get_Value("XX_DESSOL");
        
    }
    
    /** Set Registration day.
    @param XX_DiaReg Dia de registro */
    public void setXX_DiaReg (java.math.BigDecimal XX_DiaReg)
    {
        set_Value ("XX_DiaReg", XX_DiaReg);
        
    }
    
    /** Get Registration day.
    @return Dia de registro */
    public java.math.BigDecimal getXX_DiaReg() 
    {
        return get_ValueAsBigDecimal("XX_DiaReg");
        
    }
    
    /** Set RequestDay.
    @param XX_DIASOL Dia de solicitud */
    public void setXX_DIASOL (java.math.BigDecimal XX_DIASOL)
    {
        set_Value ("XX_DIASOL", XX_DIASOL);
        
    }
    
    /** Get RequestDay.
    @return Dia de solicitud */
    public java.math.BigDecimal getXX_DIASOL() 
    {
        return get_ValueAsBigDecimal("XX_DIASOL");
        
    }
    
    /** Set Day Status.
    @param XX_DIASTA Dia de status */
    public void setXX_DIASTA (java.math.BigDecimal XX_DIASTA)
    {
        set_Value ("XX_DIASTA", XX_DIASTA);
        
    }
    
    /** Get Day Status.
    @return Dia de status */
    public java.math.BigDecimal getXX_DIASTA() 
    {
        return get_ValueAsBigDecimal("XX_DIASTA");
        
    }
    
    /** Set Day Release Status.
    @param XX_DSTDES Dia  de status despacho */
    public void setXX_DSTDES (java.math.BigDecimal XX_DSTDES)
    {
        set_Value ("XX_DSTDES", XX_DSTDES);
        
    }
    
    /** Get Day Release Status.
    @return Dia  de status despacho */
    public java.math.BigDecimal getXX_DSTDES() 
    {
        return get_ValueAsBigDecimal("XX_DSTDES");
        
    }
    
    /** Set ReleaseGuide.
    @param XX_GUIDES Guia de despacho */
    public void setXX_GUIDES (String XX_GUIDES)
    {
        set_Value ("XX_GUIDES", XX_GUIDES);
        
    }
    
    /** Get ReleaseGuide.
    @return Guia de despacho */
    public String getXX_GUIDES() 
    {
        return (String)get_Value("XX_GUIDES");
        
    }
    
    /** Set Register Month.
    @param XX_MesReg Mes de registro */
    public void setXX_MesReg (java.math.BigDecimal XX_MesReg)
    {
        set_Value ("XX_MesReg", XX_MesReg);
        
    }
    
    /** Get Register Month.
    @return Mes de registro */
    public java.math.BigDecimal getXX_MesReg() 
    {
        return get_ValueAsBigDecimal("XX_MesReg");
        
    }
    
    /** Set Month Application.
    @param XX_MESSOL Mes de solicitud */
    public void setXX_MESSOL (java.math.BigDecimal XX_MESSOL)
    {
        set_Value ("XX_MESSOL", XX_MESSOL);
        
    }
    
    /** Get Month Application.
    @return Mes de solicitud */
    public java.math.BigDecimal getXX_MESSOL() 
    {
        return get_ValueAsBigDecimal("XX_MESSOL");
        
    }
    
    /** Set Month Status.
    @param XX_MESSTA Mes de status */
    public void setXX_MESSTA (java.math.BigDecimal XX_MESSTA)
    {
        set_Value ("XX_MESSTA", XX_MESSTA);
        
    }
    
    /** Get Month Status.
    @return Mes de status */
    public java.math.BigDecimal getXX_MESSTA() 
    {
        return get_ValueAsBigDecimal("XX_MESSTA");
        
    }
    
    /** Set Month Release Status.
    @param XX_MSTDES Mes de status despacho */
    public void setXX_MSTDES (java.math.BigDecimal XX_MSTDES)
    {
        set_Value ("XX_MSTDES", XX_MSTDES);
        
    }
    
    /** Get Month Release Status.
    @return Mes de status despacho */
    public java.math.BigDecimal getXX_MSTDES() 
    {
        return get_ValueAsBigDecimal("XX_MSTDES");
        
    }
    
    /** Set Application Number.
    @param XX_NUMSOL Numero de Solicitud */
    public void setXX_NUMSOL (java.math.BigDecimal XX_NUMSOL)
    {
        set_Value ("XX_NUMSOL", XX_NUMSOL);
        
    }
    
    /** Get Application Number.
    @return Numero de Solicitud */
    public java.math.BigDecimal getXX_NUMSOL() 
    {
        return get_ValueAsBigDecimal("XX_NUMSOL");
        
    }
    
    /** Set Product Code.
    @param XX_PRDORI Codigo de producto */
    public void setXX_PRDORI (java.math.BigDecimal XX_PRDORI)
    {
        set_Value ("XX_PRDORI", XX_PRDORI);
        
    }
    
    /** Get Product Code.
    @return Codigo de producto */
    public java.math.BigDecimal getXX_PRDORI() 
    {
        return get_ValueAsBigDecimal("XX_PRDORI");
        
    }
    
    /** Set Application Status.
    @param XX_STASOL Status de solicitud */
    public void setXX_STASOL (String XX_STASOL)
    {
        set_Value ("XX_STASOL", XX_STASOL);
        
    }
    
    /** Get Application Status.
    @return Status de solicitud */
    public String getXX_STASOL() 
    {
        return (String)get_Value("XX_STASOL");
        
    }
    
    /** Set Shop by Room Status.
    @param XX_STDESP Status de despacho por tienda */
    public void setXX_STDESP (String XX_STDESP)
    {
        set_Value ("XX_STDESP", XX_STDESP);
        
    }
    
    /** Get Shop by Room Status.
    @return Status de despacho por tienda */
    public String getXX_STDESP() 
    {
        return (String)get_Value("XX_STDESP");
        
    }
    
    /** Set Code Store2.
    @param XX_TADESP Codigo de tienda */
    public void setXX_TADESP (java.math.BigDecimal XX_TADESP)
    {
        set_Value ("XX_TADESP", XX_TADESP);
        
    }
    
    /** Get Code Store2.
    @return Codigo de tienda */
    public java.math.BigDecimal getXX_TADESP() 
    {
        return get_ValueAsBigDecimal("XX_TADESP");
        
    }
    
    /** Set Store.
    @param XX_TIENDA Tienda */
    public void setXX_TIENDA (java.math.BigDecimal XX_TIENDA)
    {
        set_Value ("XX_TIENDA", XX_TIENDA);
        
    }
    
    /** Get Store.
    @return Tienda */
    public java.math.BigDecimal getXX_TIENDA() 
    {
        return get_ValueAsBigDecimal("XX_TIENDA");
        
    }
    
    /** Set Type Of Application.
    @param XX_TIPSOL Tipo de solicitud */
    public void setXX_TIPSOL (String XX_TIPSOL)
    {
        set_Value ("XX_TIPSOL", XX_TIPSOL);
        
    }
    
    /** Get Type Of Application.
    @return Tipo de solicitud */
    public String getXX_TIPSOL() 
    {
        return (String)get_Value("XX_TIPSOL");
        
    }
    
    /** Set User Update.
    @param XX_USRACT Usuario de actualizacion  */
    public void setXX_USRACT (String XX_USRACT)
    {
        set_Value ("XX_USRACT", XX_USRACT);
        
    }
    
    /** Get User Update.
    @return Usuario de actualizacion  */
    public String getXX_USRACT() 
    {
        return (String)get_Value("XX_USRACT");
        
    }
    
    /** Set User Creation.
    @param XX_USRCRE Usuario de creacion */
    public void setXX_USRCRE (String XX_USRCRE)
    {
        set_Value ("XX_USRCRE", XX_USRCRE);
        
    }
    
    /** Get User Creation.
    @return Usuario de creacion */
    public String getXX_USRCRE() 
    {
        return (String)get_Value("XX_USRCRE");
        
    }
    
    /** Set Dispatch Guide.
    @param XX_VLO_DispatchGuide_ID Dispatch Guide */
    public void setXX_VLO_DispatchGuide_ID (int XX_VLO_DispatchGuide_ID)
    {
        throw new IllegalArgumentException ("XX_VLO_DispatchGuide_ID is virtual column");
        
    }
    
    /** Get Dispatch Guide.
    @return Dispatch Guide */
    public int getXX_VLO_DispatchGuide_ID() 
    {
        return get_ValueAsInt("XX_VLO_DispatchGuide_ID");
        
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
    
    /** Set PriceConsecutive.
    @param XX_VMR_PriceConsecutive_ID PriceConsecutive */
    public void setXX_VMR_PriceConsecutive_ID (int XX_VMR_PriceConsecutive_ID)
    {
        if (XX_VMR_PriceConsecutive_ID <= 0) set_Value ("XX_VMR_PriceConsecutive_ID", null);
        else
        set_Value ("XX_VMR_PriceConsecutive_ID", Integer.valueOf(XX_VMR_PriceConsecutive_ID));
        
    }
    
    /** Get PriceConsecutive.
    @return PriceConsecutive */
    public int getXX_VMR_PriceConsecutive_ID() 
    {
        return get_ValueAsInt("XX_VMR_PriceConsecutive_ID");
        
    }
    
    /** Set XX_VMR_Solm01_ID.
    @param XX_VMR_Solm01_ID XX_VMR_Solm01_ID */
    public void setXX_VMR_Solm01_ID (int XX_VMR_Solm01_ID)
    {
        if (XX_VMR_Solm01_ID <= 0) set_Value ("XX_VMR_Solm01_ID", null);
        else
        set_Value ("XX_VMR_Solm01_ID", Integer.valueOf(XX_VMR_Solm01_ID));
        
    }
    
    /** Get XX_VMR_Solm01_ID.
    @return XX_VMR_Solm01_ID */
    public int getXX_VMR_Solm01_ID() 
    {
        return get_ValueAsInt("XX_VMR_Solm01_ID");
        
    }
    
    
}
