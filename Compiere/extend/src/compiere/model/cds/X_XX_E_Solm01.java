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
/** Generated Model for XX_E_Solm01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_E_Solm01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_E_Solm01_ID id
    @param trx transaction
    */
    public X_XX_E_Solm01 (Ctx ctx, int XX_E_Solm01_ID, Trx trx)
    {
        super (ctx, XX_E_Solm01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_E_Solm01_ID == 0)
        {
            setValue (null);
            setXX_E_Solm01_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_E_Solm01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27577523609789L;
    /** Last Updated Timestamp 2011-01-18 20:21:33.0 */
    public static final long updatedMS = 1295398293000L;
    /** AD_Table_ID=1000379 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_E_Solm01");
        
    }
    ;
    
    /** TableName=XX_E_Solm01 */
    public static final String Table_Name="XX_E_Solm01";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
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
    
    /** Set XX_E_Solm01_ID.
    @param XX_E_Solm01_ID XX_E_Solm01_ID */
    public void setXX_E_Solm01_ID (int XX_E_Solm01_ID)
    {
        if (XX_E_Solm01_ID < 1) throw new IllegalArgumentException ("XX_E_Solm01_ID is mandatory.");
        set_ValueNoCheck ("XX_E_Solm01_ID", Integer.valueOf(XX_E_Solm01_ID));
        
    }
    
    /** Get XX_E_Solm01_ID.
    @return XX_E_Solm01_ID */
    public int getXX_E_Solm01_ID() 
    {
        return get_ValueAsInt("XX_E_Solm01_ID");
        
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
    
    /** Set Synchronized.
    @param XX_Synchronized Indica si el registro ya fue exportado */
    public void setXX_Synchronized (boolean XX_Synchronized)
    {
        set_Value ("XX_Synchronized", Boolean.valueOf(XX_Synchronized));
        
    }
    
    /** Get Synchronized.
    @return Indica si el registro ya fue exportado */
    public boolean isXX_Synchronized() 
    {
        return get_ValueAsBoolean("XX_Synchronized");
        
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
    
    
}
