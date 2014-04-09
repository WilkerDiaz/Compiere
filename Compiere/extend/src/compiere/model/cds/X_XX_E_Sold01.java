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
/** Generated Model for XX_E_Sold01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_E_Sold01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_E_Sold01_ID id
    @param trx transaction
    */
    public X_XX_E_Sold01 (Ctx ctx, int XX_E_Sold01_ID, Trx trx)
    {
        super (ctx, XX_E_Sold01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_E_Sold01_ID == 0)
        {
            setValue (null);
            setXX_E_Sold01_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_E_Sold01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27633749030789L;
    /** Last Updated Timestamp 2012-10-30 14:31:54.0 */
    public static final long updatedMS = 1351623714000L;
    /** AD_Table_ID=1000380 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_E_Sold01");
        
    }
    ;
    
    /** TableName=XX_E_Sold01 */
    public static final String Table_Name="XX_E_Sold01";
    
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
    
    /** Set Receipt Number.
    @param NUMREC receipt number */
    public void setNUMREC (String NUMREC)
    {
        set_Value ("NUMREC", NUMREC);
        
    }
    
    /** Get Receipt Number.
    @return receipt number */
    public String getNUMREC() 
    {
        return (String)get_Value("NUMREC");
        
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
    
    /** Set YearMovementStatus.
    @param XX_ASTMOV Año de status movimiento */
    public void setXX_ASTMOV (java.math.BigDecimal XX_ASTMOV)
    {
        set_Value ("XX_ASTMOV", XX_ASTMOV);
        
    }
    
    /** Get YearMovementStatus.
    @return Año de status movimiento */
    public java.math.BigDecimal getXX_ASTMOV() 
    {
        return get_ValueAsBigDecimal("XX_ASTMOV");
        
    }
    
    /** Set QuantityProductStorage.
    @param XX_CANDEP Cantidad de producto deposito */
    public void setXX_CANDEP (java.math.BigDecimal XX_CANDEP)
    {
        set_Value ("XX_CANDEP", XX_CANDEP);
        
    }
    
    /** Get QuantityProductStorage.
    @return Cantidad de producto deposito */
    public java.math.BigDecimal getXX_CANDEP() 
    {
        return get_ValueAsBigDecimal("XX_CANDEP");
        
    }
    
    /** Set QuantityPhysicalStore.
    @param XX_CANFIS Cantidad fisica tienda */
    public void setXX_CANFIS (java.math.BigDecimal XX_CANFIS)
    {
        set_Value ("XX_CANFIS", XX_CANFIS);
        
    }
    
    /** Get QuantityPhysicalStore.
    @return Cantidad fisica tienda */
    public java.math.BigDecimal getXX_CANFIS() 
    {
        return get_ValueAsBigDecimal("XX_CANFIS");
        
    }
    
    /** Set QuantityProductStore.
    @param XX_CANPRO Cantidad de producto tienda */
    public void setXX_CANPRO (java.math.BigDecimal XX_CANPRO)
    {
        set_Value ("XX_CANPRO", XX_CANPRO);
        
    }
    
    /** Get QuantityProductStore.
    @return Cantidad de producto tienda */
    public java.math.BigDecimal getXX_CANPRO() 
    {
        return get_ValueAsBigDecimal("XX_CANPRO");
        
    }
    
    /** Set QuantityProductBuyer .
    @param XX_CAPRAP Cantidad de producto comprador  */
    public void setXX_CAPRAP (java.math.BigDecimal XX_CAPRAP)
    {
        set_Value ("XX_CAPRAP", XX_CAPRAP);
        
    }
    
    /** Get QuantityProductBuyer .
    @return Cantidad de producto comprador  */
    public java.math.BigDecimal getXX_CAPRAP() 
    {
        return get_ValueAsBigDecimal("XX_CAPRAP");
        
    }
    
    /** Set AnnulmentProductCode.
    @param XX_CODAPR Codigo de anulacion producto */
    public void setXX_CODAPR (String XX_CODAPR)
    {
        set_Value ("XX_CODAPR", XX_CODAPR);
        
    }
    
    /** Get AnnulmentProductCode.
    @return Codigo de anulacion producto */
    public String getXX_CODAPR() 
    {
        return (String)get_Value("XX_CODAPR");
        
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
    
    /** Set Code Line.
    @param XX_CodLin Codigo de Linea */
    public void setXX_CodLin (String XX_CodLin)
    {
        set_Value ("XX_CodLin", XX_CodLin);
        
    }
    
    /** Get Code Line.
    @return Codigo de Linea */
    public String getXX_CodLin() 
    {
        return (String)get_Value("XX_CodLin");
        
    }
    
    /** Set Code Product.
    @param XX_CODPRO Codigo del Producto */
    public void setXX_CODPRO (java.math.BigDecimal XX_CODPRO)
    {
        set_Value ("XX_CODPRO", XX_CODPRO);
        
    }
    
    /** Get Code Product.
    @return Codigo del Producto */
    public java.math.BigDecimal getXX_CODPRO() 
    {
        return get_ValueAsBigDecimal("XX_CODPRO");
        
    }
    
    /** Set Alternate Code PER/EMP.
    @param XX_COEMPE Codigo alterno PER/EMP */
    public void setXX_COEMPE (java.math.BigDecimal XX_COEMPE)
    {
        set_ValueNoCheck ("XX_COEMPE", XX_COEMPE);
        
    }
    
    /** Get Alternate Code PER/EMP.
    @return Codigo alterno PER/EMP */
    public java.math.BigDecimal getXX_COEMPE() 
    {
        return get_ValueAsBigDecimal("XX_COEMPE");
        
    }
    
    /** Set Code Movement Request.
    @param XX_COMOSO Codigo movimiento solicitud */
    public void setXX_COMOSO (String XX_COMOSO)
    {
        set_Value ("XX_COMOSO", XX_COMOSO);
        
    }
    
    /** Get Code Movement Request.
    @return Codigo movimiento solicitud */
    public String getXX_COMOSO() 
    {
        return (String)get_Value("XX_COMOSO");
        
    }
    
    /** Set Consecutive Price.
    @param XX_CONPRE Consecutivo de Precio */
    public void setXX_CONPRE (java.math.BigDecimal XX_CONPRE)
    {
        set_Value ("XX_CONPRE", XX_CONPRE);
        
    }
    
    /** Get Consecutive Price.
    @return Consecutivo de Precio */
    public java.math.BigDecimal getXX_CONPRE() 
    {
        return get_ValueAsBigDecimal("XX_CONPRE");
        
    }
    
    /** Set Consecutive Prices.
    @param XX_COPRVI Consecutivo de precio */
    public void setXX_COPRVI (java.math.BigDecimal XX_COPRVI)
    {
        set_Value ("XX_COPRVI", XX_COPRVI);
        
    }
    
    /** Get Consecutive Prices.
    @return Consecutivo de precio */
    public java.math.BigDecimal getXX_COPRVI() 
    {
        return get_ValueAsBigDecimal("XX_COPRVI");
        
    }
    
    /** Set Amount Cost.
    @param XX_COSANT Monto de costo */
    public void setXX_COSANT (java.math.BigDecimal XX_COSANT)
    {
        set_Value ("XX_COSANT", XX_COSANT);
        
    }
    
    /** Get Amount Cost.
    @return Monto de costo */
    public java.math.BigDecimal getXX_COSANT() 
    {
        return get_ValueAsBigDecimal("XX_COSANT");
        
    }
    
    /** Set Product Code.
    @param XX_CPROVI Codigo de Producto */
    public void setXX_CPROVI (java.math.BigDecimal XX_CPROVI)
    {
        set_Value ("XX_CPROVI", XX_CPROVI);
        
    }
    
    /** Get Product Code.
    @return Codigo de Producto */
    public java.math.BigDecimal getXX_CPROVI() 
    {
        return get_ValueAsBigDecimal("XX_CPROVI");
        
    }
    
    /** Set Discount Promotion.
    @param XX_DSCPRM % descuento promocion */
    public void setXX_DSCPRM (java.math.BigDecimal XX_DSCPRM)
    {
        set_Value ("XX_DSCPRM", XX_DSCPRM);
        
    }
    
    /** Get Discount Promotion.
    @return % descuento promocion */
    public java.math.BigDecimal getXX_DSCPRM() 
    {
        return get_ValueAsBigDecimal("XX_DSCPRM");
        
    }
    
    /** Set Movement Day Status.
    @param XX_DSTMOV Dia de status movimiento */
    public void setXX_DSTMOV (java.math.BigDecimal XX_DSTMOV)
    {
        set_Value ("XX_DSTMOV", XX_DSTMOV);
        
    }
    
    /** Get Movement Day Status.
    @return Dia de status movimiento */
    public java.math.BigDecimal getXX_DSTMOV() 
    {
        return get_ValueAsBigDecimal("XX_DSTMOV");
        
    }
    
    /** Set XX_E_Sold01_ID.
    @param XX_E_Sold01_ID XX_E_Sold01_ID */
    public void setXX_E_Sold01_ID (int XX_E_Sold01_ID)
    {
        if (XX_E_Sold01_ID < 1) throw new IllegalArgumentException ("XX_E_Sold01_ID is mandatory.");
        set_ValueNoCheck ("XX_E_Sold01_ID", Integer.valueOf(XX_E_Sold01_ID));
        
    }
    
    /** Get XX_E_Sold01_ID.
    @return XX_E_Sold01_ID */
    public int getXX_E_Sold01_ID() 
    {
        return get_ValueAsInt("XX_E_Sold01_ID");
        
    }
    
    /** Set Month Status Movement.
    @param XX_MSTMOV Mes de status movimiento */
    public void setXX_MSTMOV (java.math.BigDecimal XX_MSTMOV)
    {
        set_Value ("XX_MSTMOV", XX_MSTMOV);
        
    }
    
    /** Get Month Status Movement.
    @return Mes de status movimiento */
    public java.math.BigDecimal getXX_MSTMOV() 
    {
        return get_ValueAsBigDecimal("XX_MSTMOV");
        
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
    
    /** Set Price Promotion.
    @param XX_PREPRM Precio de promocion */
    public void setXX_PREPRM (java.math.BigDecimal XX_PREPRM)
    {
        set_Value ("XX_PREPRM", XX_PREPRM);
        
    }
    
    /** Get Price Promotion.
    @return Precio de promocion */
    public java.math.BigDecimal getXX_PREPRM() 
    {
        return get_ValueAsBigDecimal("XX_PREPRM");
        
    }
    
    /** Set Section.
    @param XX_Seccio Seccion */
    public void setXX_Seccio (String XX_Seccio)
    {
        set_Value ("XX_Seccio", XX_Seccio);
        
    }
    
    /** Get Section.
    @return Seccion */
    public String getXX_Seccio() 
    {
        return (String)get_Value("XX_Seccio");
        
    }
    
    /** Set Movement Request Status.
    @param XX_STMOSO Status movimiento solicitud */
    public void setXX_STMOSO (String XX_STMOSO)
    {
        set_Value ("XX_STMOSO", XX_STMOSO);
        
    }
    
    /** Get Movement Request Status.
    @return Status movimiento solicitud */
    public String getXX_STMOSO() 
    {
        return (String)get_Value("XX_STMOSO");
        
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
    
    /** Set Movement Type Application.
    @param XX_TIMOSO Tipo de movimiento de solicitud */
    public void setXX_TIMOSO (String XX_TIMOSO)
    {
        set_Value ("XX_TIMOSO", XX_TIMOSO);
        
    }
    
    /** Get Movement Type Application.
    @return Tipo de movimiento de solicitud */
    public String getXX_TIMOSO() 
    {
        return (String)get_Value("XX_TIMOSO");
        
    }
    
    /** Set Amount Of Sale.
    @param XX_VENANT Monto de Venta  */
    public void setXX_VENANT (java.math.BigDecimal XX_VENANT)
    {
        set_Value ("XX_VENANT", XX_VENANT);
        
    }
    
    /** Get Amount Of Sale.
    @return Monto de Venta  */
    public java.math.BigDecimal getXX_VENANT() 
    {
        return get_ValueAsBigDecimal("XX_VENANT");
        
    }
    
    
}
