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
/** Generated Model for I_XX_VMR_Prld01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VMR_Prld01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_Prld01_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_Prld01 (Ctx ctx, int I_XX_VMR_Prld01_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_Prld01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_Prld01_ID == 0)
        {
            setI_XX_VMR_Prld01_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_Prld01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27567907828789L;
    /** Last Updated Timestamp 2010-09-29 13:18:32.0 */
    public static final long updatedMS = 1285782512000L;
    /** AD_Table_ID=1000093 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_Prld01");
        
    }
    ;
    
    /** TableName=I_XX_VMR_Prld01 */
    public static final String Table_Name="I_XX_VMR_Prld01";
    
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
    
    /** Set I_XX_VMR_Prld01_ID.
    @param I_XX_VMR_Prld01_ID I_XX_VMR_Prld01_ID */
    public void setI_XX_VMR_Prld01_ID (int I_XX_VMR_Prld01_ID)
    {
        if (I_XX_VMR_Prld01_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_Prld01_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_Prld01_ID", Integer.valueOf(I_XX_VMR_Prld01_ID));
        
    }
    
    /** Get I_XX_VMR_Prld01_ID.
    @return I_XX_VMR_Prld01_ID */
    public int getI_XX_VMR_Prld01_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_Prld01_ID");
        
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
    
    /** Set Budget Year and Month.
    @param XX_AÑOMESPRE Año y Mes de Presupuesto  */
    public void setXX_AÑOMESPRE (int XX_AÑOMESPRE)
    {
        set_Value ("XX_AÑOMESPRE", Integer.valueOf(XX_AÑOMESPRE));
        
    }
    
    /** Get Budget Year and Month.
    @return Año y Mes de Presupuesto  */
    public int getXX_AÑOMESPRE() 
    {
        return get_ValueAsInt("XX_AÑOMESPRE");
        
    }
    
    /** Set Purchase Quantity Placed months .
    @param XX_CACOMCOMA Cantidad de Compras Colocadas Meses Anteriores  */
    public void setXX_CACOMCOMA (java.math.BigDecimal XX_CACOMCOMA)
    {
        set_Value ("XX_CACOMCOMA", XX_CACOMCOMA);
        
    }
    
    /** Get Purchase Quantity Placed months .
    @return Cantidad de Compras Colocadas Meses Anteriores  */
    public java.math.BigDecimal getXX_CACOMCOMA() 
    {
        return get_ValueAsBigDecimal("XX_CACOMCOMA");
        
    }
    
    /** Set Purchase Quantity Imported Placed .
    @param XX_CACOMICOL Cantidad de Compras Importadas Colocadas  */
    public void setXX_CACOMICOL (java.math.BigDecimal XX_CACOMICOL)
    {
        set_Value ("XX_CACOMICOL", XX_CACOMICOL);
        
    }
    
    /** Get Purchase Quantity Imported Placed .
    @return Cantidad de Compras Importadas Colocadas  */
    public java.math.BigDecimal getXX_CACOMICOL() 
    {
        return get_ValueAsBigDecimal("XX_CACOMICOL");
        
    }
    
    /** Set Quantity Purchase amounts received .
    @param XX_CACOMIREC Cantidad de Compras Importadas Recibidas  */
    public void setXX_CACOMIREC (java.math.BigDecimal XX_CACOMIREC)
    {
        set_Value ("XX_CACOMIREC", XX_CACOMIREC);
        
    }
    
    /** Get Quantity Purchase amounts received .
    @return Cantidad de Compras Importadas Recibidas  */
    public java.math.BigDecimal getXX_CACOMIREC() 
    {
        return get_ValueAsBigDecimal("XX_CACOMIREC");
        
    }
    
    /** Set Number of National Shopping Placed .
    @param XX_CACOMNCOL Cantidad de Compras Nacionales Colocadas  */
    public void setXX_CACOMNCOL (java.math.BigDecimal XX_CACOMNCOL)
    {
        set_Value ("XX_CACOMNCOL", XX_CACOMNCOL);
        
    }
    
    /** Get Number of National Shopping Placed .
    @return Cantidad de Compras Nacionales Colocadas  */
    public java.math.BigDecimal getXX_CACOMNCOL() 
    {
        return get_ValueAsBigDecimal("XX_CACOMNCOL");
        
    }
    
    /** Set Quantity Purchase nationality .
    @param XX_CACOMNREC Cantidad de Compras Nacionales Recibidas  */
    public void setXX_CACOMNREC (java.math.BigDecimal XX_CACOMNREC)
    {
        set_Value ("XX_CACOMNREC", XX_CACOMNREC);
        
    }
    
    /** Get Quantity Purchase nationality .
    @return Cantidad de Compras Nacionales Recibidas  */
    public java.math.BigDecimal getXX_CACOMNREC() 
    {
        return get_ValueAsBigDecimal("XX_CACOMNREC");
        
    }
    
    /** Set Number of months received Shopping .
    @param XX_CACOMREMA Cantidad de Compras Recibidas Meses Anteriores  */
    public void setXX_CACOMREMA (java.math.BigDecimal XX_CACOMREMA)
    {
        set_Value ("XX_CACOMREMA", XX_CACOMREMA);
        
    }
    
    /** Get Number of months received Shopping .
    @return Cantidad de Compras Recibidas Meses Anteriores  */
    public java.math.BigDecimal getXX_CACOMREMA() 
    {
        return get_ValueAsBigDecimal("XX_CACOMREMA");
        
    }
    
    /** Set Final Inventory Amount Budgeted .
    @param XX_CAINVFIPR Cantidad de Inventario Final Presupuestado   */
    public void setXX_CAINVFIPR (java.math.BigDecimal XX_CAINVFIPR)
    {
        set_Value ("XX_CAINVFIPR", XX_CAINVFIPR);
        
    }
    
    /** Get Final Inventory Amount Budgeted .
    @return Cantidad de Inventario Final Presupuestado   */
    public java.math.BigDecimal getXX_CAINVFIPR() 
    {
        return get_ValueAsBigDecimal("XX_CAINVFIPR");
        
    }
    
    /** Set Number of Projected Final Inventory .
    @param XX_CAINVFIPY Cantidad de Inventario Final Proyectado  */
    public void setXX_CAINVFIPY (java.math.BigDecimal XX_CAINVFIPY)
    {
        set_Value ("XX_CAINVFIPY", XX_CAINVFIPY);
        
    }
    
    /** Get Number of Projected Final Inventory .
    @return Cantidad de Inventario Final Proyectado  */
    public java.math.BigDecimal getXX_CAINVFIPY() 
    {
        return get_ValueAsBigDecimal("XX_CAINVFIPY");
        
    }
    
    /** Set Number of Real Final Inventory .
    @param XX_CAINVFIRE Cantidad de Inventario Final Real  */
    public void setXX_CAINVFIRE (java.math.BigDecimal XX_CAINVFIRE)
    {
        set_Value ("XX_CAINVFIRE", XX_CAINVFIRE);
        
    }
    
    /** Get Number of Real Final Inventory .
    @return Cantidad de Inventario Final Real  */
    public java.math.BigDecimal getXX_CAINVFIRE() 
    {
        return get_ValueAsBigDecimal("XX_CAINVFIRE");
        
    }
    
    /** Set Inventory amount originally budgeted .
    @param XX_CAINVINPR Cantidad de Inventario Inicial Presupuestado  */
    public void setXX_CAINVINPR (java.math.BigDecimal XX_CAINVINPR)
    {
        set_Value ("XX_CAINVINPR", XX_CAINVINPR);
        
    }
    
    /** Get Inventory amount originally budgeted .
    @return Cantidad de Inventario Inicial Presupuestado  */
    public java.math.BigDecimal getXX_CAINVINPR() 
    {
        return get_ValueAsBigDecimal("XX_CAINVINPR");
        
    }
    
    /** Set Number of Initial Inventory Real .
    @param XX_CAINVINRE Cantidad de Inventario Inicial Real  */
    public void setXX_CAINVINRE (java.math.BigDecimal XX_CAINVINRE)
    {
        set_Value ("XX_CAINVINRE", XX_CAINVINRE);
        
    }
    
    /** Get Number of Initial Inventory Real .
    @return Cantidad de Inventario Inicial Real  */
    public java.math.BigDecimal getXX_CAINVINRE() 
    {
        return get_ValueAsBigDecimal("XX_CAINVINRE");
        
    }
    
    /** Set Quantity Budgeted Shopping  .
    @param XX_CANCOMPRE Cantidad de Compras Presupuestadas  */
    public void setXX_CANCOMPRE (java.math.BigDecimal XX_CANCOMPRE)
    {
        set_Value ("XX_CANCOMPRE", XX_CANCOMPRE);
        
    }
    
    /** Get Quantity Budgeted Shopping  .
    @return Cantidad de Compras Presupuestadas  */
    public java.math.BigDecimal getXX_CANCOMPRE() 
    {
        return get_ValueAsBigDecimal("XX_CANCOMPRE");
        
    }
    
    /** Set Quantity of Purchase Limit  .
    @param XX_CANLIMCOM Cantidad de Limite de Compra  */
    public void setXX_CANLIMCOM (java.math.BigDecimal XX_CANLIMCOM)
    {
        set_Value ("XX_CANLIMCOM", XX_CANLIMCOM);
        
    }
    
    /** Get Quantity of Purchase Limit  .
    @return Cantidad de Limite de Compra  */
    public java.math.BigDecimal getXX_CANLIMCOM() 
    {
        return get_ValueAsBigDecimal("XX_CANLIMCOM");
        
    }
    
    /** Set Number of Transfers Sent .
    @param XX_CANTRAENV Cantidad de Traspasos Enviados  */
    public void setXX_CANTRAENV (java.math.BigDecimal XX_CANTRAENV)
    {
        set_Value ("XX_CANTRAENV", XX_CANTRAENV);
        
    }
    
    /** Get Number of Transfers Sent .
    @return Cantidad de Traspasos Enviados  */
    public java.math.BigDecimal getXX_CANTRAENV() 
    {
        return get_ValueAsBigDecimal("XX_CANTRAENV");
        
    }
    
    /** Set Number of Transfers Received .
    @param XX_CANTRAREC Cantidad de Traspasos Recibidos  */
    public void setXX_CANTRAREC (java.math.BigDecimal XX_CANTRAREC)
    {
        set_Value ("XX_CANTRAREC", XX_CANTRAREC);
        
    }
    
    /** Get Number of Transfers Received .
    @return Cantidad de Traspasos Recibidos  */
    public java.math.BigDecimal getXX_CANTRAREC() 
    {
        return get_ValueAsBigDecimal("XX_CANTRAREC");
        
    }
    
    /** Set Sales Amount Budgeted .
    @param XX_CANVENPRE Cantidad de Ventas Presupuestadas  */
    public void setXX_CANVENPRE (java.math.BigDecimal XX_CANVENPRE)
    {
        set_Value ("XX_CANVENPRE", XX_CANVENPRE);
        
    }
    
    /** Get Sales Amount Budgeted .
    @return Cantidad de Ventas Presupuestadas  */
    public java.math.BigDecimal getXX_CANVENPRE() 
    {
        return get_ValueAsBigDecimal("XX_CANVENPRE");
        
    }
    
    /** Set Quantity of actual sales .
    @param XX_CANVENREA Cantidad de Ventas Reales  */
    public void setXX_CANVENREA (java.math.BigDecimal XX_CANVENREA)
    {
        set_Value ("XX_CANVENREA", XX_CANVENREA);
        
    }
    
    /** Get Quantity of actual sales .
    @return Cantidad de Ventas Reales  */
    public java.math.BigDecimal getXX_CANVENREA() 
    {
        return get_ValueAsBigDecimal("XX_CANVENREA");
        
    }
    
    /** Set Final Budgeted Amount of Sale .
    @param XX_CAREBDFPR Cantidad de Rebajas Definitivas Presupuestadas  */
    public void setXX_CAREBDFPR (java.math.BigDecimal XX_CAREBDFPR)
    {
        set_Value ("XX_CAREBDFPR", XX_CAREBDFPR);
        
    }
    
    /** Get Final Budgeted Amount of Sale .
    @return Cantidad de Rebajas Definitivas Presupuestadas  */
    public java.math.BigDecimal getXX_CAREBDFPR() 
    {
        return get_ValueAsBigDecimal("XX_CAREBDFPR");
        
    }
    
    /** Set Final Sale Amount Interests .
    @param XX_CAREBDFRE Cantidad de Rebajas Definitivas Reales  */
    public void setXX_CAREBDFRE (java.math.BigDecimal XX_CAREBDFRE)
    {
        set_Value ("XX_CAREBDFRE", XX_CAREBDFRE);
        
    }
    
    /** Get Final Sale Amount Interests .
    @return Cantidad de Rebajas Definitivas Reales  */
    public java.math.BigDecimal getXX_CAREBDFRE() 
    {
        return get_ValueAsBigDecimal("XX_CAREBDFRE");
        
    }
    
    /** Set Budgeted amount of F.R Sale .
    @param XX_CAREBFRPR Cantidad de Rebajas F.R Presupuestadas  */
    public void setXX_CAREBFRPR (java.math.BigDecimal XX_CAREBFRPR)
    {
        set_Value ("XX_CAREBFRPR", XX_CAREBFRPR);
        
    }
    
    /** Get Budgeted amount of F.R Sale .
    @return Cantidad de Rebajas F.R Presupuestadas  */
    public java.math.BigDecimal getXX_CAREBFRPR() 
    {
        return get_ValueAsBigDecimal("XX_CAREBFRPR");
        
    }
    
    /** Set Amount of Sale F.R Interests .
    @param XX_CAREBFRRE Cantidad de Rebajas F.R Reales  */
    public void setXX_CAREBFRRE (java.math.BigDecimal XX_CAREBFRRE)
    {
        set_Value ("XX_CAREBFRRE", XX_CAREBFRRE);
        
    }
    
    /** Get Amount of Sale F.R Interests .
    @return Cantidad de Rebajas F.R Reales  */
    public java.math.BigDecimal getXX_CAREBFRRE() 
    {
        return get_ValueAsBigDecimal("XX_CAREBFRRE");
        
    }
    
    /** Set Promotional Sale Number of Budgeted .
    @param XX_CAREBPRPR Cantidad de Rebajas Promocional Presupuestadas  */
    public void setXX_CAREBPRPR (java.math.BigDecimal XX_CAREBPRPR)
    {
        set_Value ("XX_CAREBPRPR", XX_CAREBPRPR);
        
    }
    
    /** Get Promotional Sale Number of Budgeted .
    @return Cantidad de Rebajas Promocional Presupuestadas  */
    public java.math.BigDecimal getXX_CAREBPRPR() 
    {
        return get_ValueAsBigDecimal("XX_CAREBPRPR");
        
    }
    
    /** Set Amount of Sale Promotional Interests .
    @param XX_CAREBPRRE Cantidad de Rebajas Promocional Reales  */
    public void setXX_CAREBPRRE (java.math.BigDecimal XX_CAREBPRRE)
    {
        set_Value ("XX_CAREBPRRE", XX_CAREBPRRE);
        
    }
    
    /** Get Amount of Sale Promotional Interests .
    @return Cantidad de Rebajas Promocional Reales  */
    public java.math.BigDecimal getXX_CAREBPRRE() 
    {
        return get_ValueAsBigDecimal("XX_CAREBPRRE");
        
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
    
    /** Set Code Store .
    @param XX_CODTIE Codigo de Tienda  */
    public void setXX_CODTIE (int XX_CODTIE)
    {
        set_Value ("XX_CODTIE", Integer.valueOf(XX_CODTIE));
        
    }
    
    /** Get Code Store .
    @return Codigo de Tienda  */
    public int getXX_CODTIE() 
    {
        return get_ValueAsInt("XX_CODTIE");
        
    }
    
    /** Set Purchase Amount Placed Past Months .
    @param XX_MOCOMCOMA Monto de Compras Colocadas Meses Anteriores  */
    public void setXX_MOCOMCOMA (java.math.BigDecimal XX_MOCOMCOMA)
    {
        set_Value ("XX_MOCOMCOMA", XX_MOCOMCOMA);
        
    }
    
    /** Get Purchase Amount Placed Past Months .
    @return Monto de Compras Colocadas Meses Anteriores  */
    public java.math.BigDecimal getXX_MOCOMCOMA() 
    {
        return get_ValueAsBigDecimal("XX_MOCOMCOMA");
        
    }
    
    /** Set Purchase Amount Placed at Cost Imported .
    @param XX_MOCOMICCO Monto de Compras Colocadas Importadas al Costo  */
    public void setXX_MOCOMICCO (java.math.BigDecimal XX_MOCOMICCO)
    {
        set_Value ("XX_MOCOMICCO", XX_MOCOMICCO);
        
    }
    
    /** Get Purchase Amount Placed at Cost Imported .
    @return Monto de Compras Colocadas Importadas al Costo  */
    public java.math.BigDecimal getXX_MOCOMICCO() 
    {
        return get_ValueAsBigDecimal("XX_MOCOMICCO");
        
    }
    
    /** Set Purchase Amount Placed Imported .
    @param XX_MOCOMICOL Monto de Compras Importadas Colocadas  */
    public void setXX_MOCOMICOL (java.math.BigDecimal XX_MOCOMICOL)
    {
        set_Value ("XX_MOCOMICOL", XX_MOCOMICOL);
        
    }
    
    /** Get Purchase Amount Placed Imported .
    @return Monto de Compras Importadas Colocadas  */
    public java.math.BigDecimal getXX_MOCOMICOL() 
    {
        return get_ValueAsBigDecimal("XX_MOCOMICOL");
        
    }
    
    /** Set Purchase Amount Received Imported .
    @param XX_MOCOMIREC Monto de Compras Importadas Recibidas  */
    public void setXX_MOCOMIREC (java.math.BigDecimal XX_MOCOMIREC)
    {
        set_Value ("XX_MOCOMIREC", XX_MOCOMIREC);
        
    }
    
    /** Get Purchase Amount Received Imported .
    @return Monto de Compras Importadas Recibidas  */
    public java.math.BigDecimal getXX_MOCOMIREC() 
    {
        return get_ValueAsBigDecimal("XX_MOCOMIREC");
        
    }
    
    /** Set Amount Placed at National Purchasing Cost .
    @param XX_MOCOMNCCO Monto de Compras Nacionales Colocadas al Costo  */
    public void setXX_MOCOMNCCO (java.math.BigDecimal XX_MOCOMNCCO)
    {
        set_Value ("XX_MOCOMNCCO", XX_MOCOMNCCO);
        
    }
    
    /** Get Amount Placed at National Purchasing Cost .
    @return Monto de Compras Nacionales Colocadas al Costo  */
    public java.math.BigDecimal getXX_MOCOMNCCO() 
    {
        return get_ValueAsBigDecimal("XX_MOCOMNCCO");
        
    }
    
    /** Set Amount Placed National Campra .
    @param XX_MOCOMNCOL Monto de Campras Nacionales Colocadas  */
    public void setXX_MOCOMNCOL (java.math.BigDecimal XX_MOCOMNCOL)
    {
        set_Value ("XX_MOCOMNCOL", XX_MOCOMNCOL);
        
    }
    
    /** Get Amount Placed National Campra .
    @return Monto de Campras Nacionales Colocadas  */
    public java.math.BigDecimal getXX_MOCOMNCOL() 
    {
        return get_ValueAsBigDecimal("XX_MOCOMNCOL");
        
    }
    
    /** Set National Purchasing Amount received .
    @param XX_MOCOMNREC Monto de Compras Nacionales recibidas  */
    public void setXX_MOCOMNREC (java.math.BigDecimal XX_MOCOMNREC)
    {
        set_Value ("XX_MOCOMNREC", XX_MOCOMNREC);
        
    }
    
    /** Get National Purchasing Amount received .
    @return Monto de Compras Nacionales recibidas  */
    public java.math.BigDecimal getXX_MOCOMNREC() 
    {
        return get_ValueAsBigDecimal("XX_MOCOMNREC");
        
    }
    
    /** Set Purchase Amount Received Past Months .
    @param XX_MOCOMREMA Monto de Compras Recibidas Meses Anteriores  */
    public void setXX_MOCOMREMA (java.math.BigDecimal XX_MOCOMREMA)
    {
        set_Value ("XX_MOCOMREMA", XX_MOCOMREMA);
        
    }
    
    /** Get Purchase Amount Received Past Months .
    @return Monto de Compras Recibidas Meses Anteriores  */
    public java.math.BigDecimal getXX_MOCOMREMA() 
    {
        return get_ValueAsBigDecimal("XX_MOCOMREMA");
        
    }
    
    /** Set Final Inventory Amount Budgeted .
    @param XX_MOINVFIPR Monto de Inventario Final Presupuestado  */
    public void setXX_MOINVFIPR (java.math.BigDecimal XX_MOINVFIPR)
    {
        set_Value ("XX_MOINVFIPR", XX_MOINVFIPR);
        
    }
    
    /** Get Final Inventory Amount Budgeted .
    @return Monto de Inventario Final Presupuestado  */
    public java.math.BigDecimal getXX_MOINVFIPR() 
    {
        return get_ValueAsBigDecimal("XX_MOINVFIPR");
        
    }
    
    /** Set Final Inventory Amount Projected .
    @param XX_MOINVFIPY Monto de Inventario Final Proyectado  */
    public void setXX_MOINVFIPY (java.math.BigDecimal XX_MOINVFIPY)
    {
        set_Value ("XX_MOINVFIPY", XX_MOINVFIPY);
        
    }
    
    /** Get Final Inventory Amount Projected .
    @return Monto de Inventario Final Proyectado  */
    public java.math.BigDecimal getXX_MOINVFIPY() 
    {
        return get_ValueAsBigDecimal("XX_MOINVFIPY");
        
    }
    
    /** Set Inventory Amount Final Real .
    @param XX_MOINVFIRE Monto de Inventario Final Real  */
    public void setXX_MOINVFIRE (java.math.BigDecimal XX_MOINVFIRE)
    {
        set_Value ("XX_MOINVFIRE", XX_MOINVFIRE);
        
    }
    
    /** Get Inventory Amount Final Real .
    @return Monto de Inventario Final Real  */
    public java.math.BigDecimal getXX_MOINVFIRE() 
    {
        return get_ValueAsBigDecimal("XX_MOINVFIRE");
        
    }
    
    /** Set Initial Amount of the Inventory Cost .
    @param XX_MOINVINCO Monto de Inventario Inicial al Costo  */
    public void setXX_MOINVINCO (java.math.BigDecimal XX_MOINVINCO)
    {
        set_Value ("XX_MOINVINCO", XX_MOINVINCO);
        
    }
    
    /** Get Initial Amount of the Inventory Cost .
    @return Monto de Inventario Inicial al Costo  */
    public java.math.BigDecimal getXX_MOINVINCO() 
    {
        return get_ValueAsBigDecimal("XX_MOINVINCO");
        
    }
    
    /** Set Inventory Efetue Budgeted Amount .
    @param XX_MOINVINPR Monto de Inventario Inicia Presupuestado  */
    public void setXX_MOINVINPR (java.math.BigDecimal XX_MOINVINPR)
    {
        set_Value ("XX_MOINVINPR", XX_MOINVINPR);
        
    }
    
    /** Get Inventory Efetue Budgeted Amount .
    @return Monto de Inventario Inicia Presupuestado  */
    public java.math.BigDecimal getXX_MOINVINPR() 
    {
        return get_ValueAsBigDecimal("XX_MOINVINPR");
        
    }
    
    /** Set Amount of Initial Inventory Real .
    @param XX_MOINVINRE Monto de Inventario Inicial Real  */
    public void setXX_MOINVINRE (java.math.BigDecimal XX_MOINVINRE)
    {
        set_Value ("XX_MOINVINRE", XX_MOINVINRE);
        
    }
    
    /** Get Amount of Initial Inventory Real .
    @return Monto de Inventario Inicial Real  */
    public java.math.BigDecimal getXX_MOINVINRE() 
    {
        return get_ValueAsBigDecimal("XX_MOINVINRE");
        
    }
    
    /** Set Budgeted decline .
    @param XX_MOMERMPRE Merma Presupuestada  */
    public void setXX_MOMERMPRE (java.math.BigDecimal XX_MOMERMPRE)
    {
        set_Value ("XX_MOMERMPRE", XX_MOMERMPRE);
        
    }
    
    /** Get Budgeted decline .
    @return Merma Presupuestada  */
    public java.math.BigDecimal getXX_MOMERMPRE() 
    {
        return get_ValueAsBigDecimal("XX_MOMERMPRE");
        
    }
    
    /** Set Real decline .
    @param XX_MOMERMREA Merma Real  */
    public void setXX_MOMERMREA (java.math.BigDecimal XX_MOMERMREA)
    {
        set_Value ("XX_MOMERMREA", XX_MOMERMREA);
        
    }
    
    /** Get Real decline .
    @return Merma Real  */
    public java.math.BigDecimal getXX_MOMERMREA() 
    {
        return get_ValueAsBigDecimal("XX_MOMERMREA");
        
    }
    
    /** Set Purchase Amount Budgeted .
    @param XX_MONCOMPRE Monto de Compras Presupuestadas  */
    public void setXX_MONCOMPRE (java.math.BigDecimal XX_MONCOMPRE)
    {
        set_Value ("XX_MONCOMPRE", XX_MONCOMPRE);
        
    }
    
    /** Get Purchase Amount Budgeted .
    @return Monto de Compras Presupuestadas  */
    public java.math.BigDecimal getXX_MONCOMPRE() 
    {
        return get_ValueAsBigDecimal("XX_MONCOMPRE");
        
    }
    
    /** Set Amount of Purchase Limit .
    @param XX_MONLIMCOM Monto de Limite de Compra  */
    public void setXX_MONLIMCOM (java.math.BigDecimal XX_MONLIMCOM)
    {
        set_Value ("XX_MONLIMCOM", XX_MONLIMCOM);
        
    }
    
    /** Get Amount of Purchase Limit .
    @return Monto de Limite de Compra  */
    public java.math.BigDecimal getXX_MONLIMCOM() 
    {
        return get_ValueAsBigDecimal("XX_MONLIMCOM");
        
    }
    
    /** Set Transfer Amount Sent .
    @param XX_MONTRAENV Monto de Traspasos Enviados  */
    public void setXX_MONTRAENV (java.math.BigDecimal XX_MONTRAENV)
    {
        set_Value ("XX_MONTRAENV", XX_MONTRAENV);
        
    }
    
    /** Get Transfer Amount Sent .
    @return Monto de Traspasos Enviados  */
    public java.math.BigDecimal getXX_MONTRAENV() 
    {
        return get_ValueAsBigDecimal("XX_MONTRAENV");
        
    }
    
    /** Set Transfer Amount Received .
    @param XX_MONTRAREC Monto de Traspasos Recibidos  */
    public void setXX_MONTRAREC (java.math.BigDecimal XX_MONTRAREC)
    {
        set_Value ("XX_MONTRAREC", XX_MONTRAREC);
        
    }
    
    /** Get Transfer Amount Received .
    @return Monto de Traspasos Recibidos  */
    public java.math.BigDecimal getXX_MONTRAREC() 
    {
        return get_ValueAsBigDecimal("XX_MONTRAREC");
        
    }
    
    /** Set Amount of Sales Cost .
    @param XX_MONVENCOS Monto de Ventas al Costo  */
    public void setXX_MONVENCOS (java.math.BigDecimal XX_MONVENCOS)
    {
        set_Value ("XX_MONVENCOS", XX_MONVENCOS);
        
    }
    
    /** Get Amount of Sales Cost .
    @return Monto de Ventas al Costo  */
    public java.math.BigDecimal getXX_MONVENCOS() 
    {
        return get_ValueAsBigDecimal("XX_MONVENCOS");
        
    }
    
    /** Set Sales Amount Budgeted.
    @param XX_MONVENPRE Monto de Ventas Presupuestadas  */
    public void setXX_MONVENPRE (java.math.BigDecimal XX_MONVENPRE)
    {
        set_Value ("XX_MONVENPRE", XX_MONVENPRE);
        
    }
    
    /** Get Sales Amount Budgeted.
    @return Monto de Ventas Presupuestadas  */
    public java.math.BigDecimal getXX_MONVENPRE() 
    {
        return get_ValueAsBigDecimal("XX_MONVENPRE");
        
    }
    
    /** Set Amount of actual sales .
    @param XX_MONVENREA Monto de Ventas Reales   */
    public void setXX_MONVENREA (java.math.BigDecimal XX_MONVENREA)
    {
        set_Value ("XX_MONVENREA", XX_MONVENREA);
        
    }
    
    /** Get Amount of actual sales .
    @return Monto de Ventas Reales   */
    public java.math.BigDecimal getXX_MONVENREA() 
    {
        return get_ValueAsBigDecimal("XX_MONVENREA");
        
    }
    
    /** Set Final Sale Amount Budgeted .
    @param XX_MOREBDFPR Monto de Rebajas Definitivas Presupuestadas  */
    public void setXX_MOREBDFPR (java.math.BigDecimal XX_MOREBDFPR)
    {
        set_Value ("XX_MOREBDFPR", XX_MOREBDFPR);
        
    }
    
    /** Get Final Sale Amount Budgeted .
    @return Monto de Rebajas Definitivas Presupuestadas  */
    public java.math.BigDecimal getXX_MOREBDFPR() 
    {
        return get_ValueAsBigDecimal("XX_MOREBDFPR");
        
    }
    
    /** Set Final Actual Amount of Sale .
    @param XX_MOREBDFRE Monto de Rebajas Definitivas Reales  */
    public void setXX_MOREBDFRE (java.math.BigDecimal XX_MOREBDFRE)
    {
        set_Value ("XX_MOREBDFRE", XX_MOREBDFRE);
        
    }
    
    /** Get Final Actual Amount of Sale .
    @return Monto de Rebajas Definitivas Reales  */
    public java.math.BigDecimal getXX_MOREBDFRE() 
    {
        return get_ValueAsBigDecimal("XX_MOREBDFRE");
        
    }
    
    /** Set Amount of Sale F.R Budgeted .
    @param XX_MOREBFRPR Monto de Rebajas F.R Presupuestadas   */
    public void setXX_MOREBFRPR (java.math.BigDecimal XX_MOREBFRPR)
    {
        set_Value ("XX_MOREBFRPR", XX_MOREBFRPR);
        
    }
    
    /** Get Amount of Sale F.R Budgeted .
    @return Monto de Rebajas F.R Presupuestadas   */
    public java.math.BigDecimal getXX_MOREBFRPR() 
    {
        return get_ValueAsBigDecimal("XX_MOREBFRPR");
        
    }
    
    /** Set Actual Amount of Sale F.R .
    @param XX_MOREBFRRE Monto de Rebajas F.R Reales  */
    public void setXX_MOREBFRRE (java.math.BigDecimal XX_MOREBFRRE)
    {
        set_Value ("XX_MOREBFRRE", XX_MOREBFRRE);
        
    }
    
    /** Get Actual Amount of Sale F.R .
    @return Monto de Rebajas F.R Reales  */
    public java.math.BigDecimal getXX_MOREBFRRE() 
    {
        return get_ValueAsBigDecimal("XX_MOREBFRRE");
        
    }
    
    /** Set Promotional Sale Amount Budgeted .
    @param XX_MOREBPRPR Monto de Rebajas Promocional Presupuestadas  */
    public void setXX_MOREBPRPR (java.math.BigDecimal XX_MOREBPRPR)
    {
        set_Value ("XX_MOREBPRPR", XX_MOREBPRPR);
        
    }
    
    /** Get Promotional Sale Amount Budgeted .
    @return Monto de Rebajas Promocional Presupuestadas  */
    public java.math.BigDecimal getXX_MOREBPRPR() 
    {
        return get_ValueAsBigDecimal("XX_MOREBPRPR");
        
    }
    
    /** Set Actual Amount of Sale Promotional .
    @param XX_MOREBPRRE Monto de Rebajas Promocional Reales  */
    public void setXX_MOREBPRRE (java.math.BigDecimal XX_MOREBPRRE)
    {
        set_Value ("XX_MOREBPRRE", XX_MOREBPRRE);
        
    }
    
    /** Get Actual Amount of Sale Promotional .
    @return Monto de Rebajas Promocional Reales  */
    public java.math.BigDecimal getXX_MOREBPRRE() 
    {
        return get_ValueAsBigDecimal("XX_MOREBPRRE");
        
    }
    
    /** Set Livestock Gross Margin Percent Real.
    @param XX_POMARBGPR Porcentaje de Margen Bruto Ganado Real  */
    public void setXX_POMARBGPR (java.math.BigDecimal XX_POMARBGPR)
    {
        set_Value ("XX_POMARBGPR", XX_POMARBGPR);
        
    }
    
    /** Get Livestock Gross Margin Percent Real.
    @return Porcentaje de Margen Bruto Ganado Real  */
    public java.math.BigDecimal getXX_POMARBGPR() 
    {
        return get_ValueAsBigDecimal("XX_POMARBGPR");
        
    }
    
    /** Set Livestock Gross Margin Percent Real .
    @param XX_POMARBGRE Porcentaje de Margen Bruto Ganado Real  */
    public void setXX_POMARBGRE (java.math.BigDecimal XX_POMARBGRE)
    {
        set_Value ("XX_POMARBGRE", XX_POMARBGRE);
        
    }
    
    /** Get Livestock Gross Margin Percent Real .
    @return Porcentaje de Margen Bruto Ganado Real  */
    public java.math.BigDecimal getXX_POMARBGRE() 
    {
        return get_ValueAsBigDecimal("XX_POMARBGRE");
        
    }
    
    /** Set Net Margin Percentage Cattle Budgeted .
    @param XX_POMARNGPR Porcentaje de Margen Neto Ganado Presupuestado  */
    public void setXX_POMARNGPR (java.math.BigDecimal XX_POMARNGPR)
    {
        set_Value ("XX_POMARNGPR", XX_POMARNGPR);
        
    }
    
    /** Get Net Margin Percentage Cattle Budgeted .
    @return Porcentaje de Margen Neto Ganado Presupuestado  */
    public java.math.BigDecimal getXX_POMARNGPR() 
    {
        return get_ValueAsBigDecimal("XX_POMARNGPR");
        
    }
    
    /** Set Net Margin Percentage Royal Livestock.
    @param XX_POMARNGRE Porcentaje de Margen Neto Ganado Real  */
    public void setXX_POMARNGRE (java.math.BigDecimal XX_POMARNGRE)
    {
        set_Value ("XX_POMARNGRE", XX_POMARNGRE);
        
    }
    
    /** Get Net Margin Percentage Royal Livestock.
    @return Porcentaje de Margen Neto Ganado Real  */
    public java.math.BigDecimal getXX_POMARNGRE() 
    {
        return get_ValueAsBigDecimal("XX_POMARNGRE");
        
    }
    
    /** Set By Winning Margin Percentage Budgeted .
    @param XX_POMARPGPR Porcentaje de Margen Por Ganar Presupuestado  */
    public void setXX_POMARPGPR (java.math.BigDecimal XX_POMARPGPR)
    {
        set_Value ("XX_POMARPGPR", XX_POMARPGPR);
        
    }
    
    /** Get By Winning Margin Percentage Budgeted .
    @return Porcentaje de Margen Por Ganar Presupuestado  */
    public java.math.BigDecimal getXX_POMARPGPR() 
    {
        return get_ValueAsBigDecimal("XX_POMARPGPR");
        
    }
    
    /** Set Percentage Of Winning Margin Real.
    @param XX_POMARPGRE Porcentaje de Margen Por Ganar Real  */
    public void setXX_POMARPGRE (java.math.BigDecimal XX_POMARPGRE)
    {
        set_Value ("XX_POMARPGRE", XX_POMARPGRE);
        
    }
    
    /** Get Percentage Of Winning Margin Real.
    @return Porcentaje de Margen Por Ganar Real  */
    public java.math.BigDecimal getXX_POMARPGRE() 
    {
        return get_ValueAsBigDecimal("XX_POMARPGRE");
        
    }
    
    /** Set Margin (%) according Budgeted Purchase .
    @param XX_POMARSCPR Margen(%) Segun Compra Presupuestado  */
    public void setXX_POMARSCPR (java.math.BigDecimal XX_POMARSCPR)
    {
        set_Value ("XX_POMARSCPR", XX_POMARSCPR);
        
    }
    
    /** Get Margin (%) according Budgeted Purchase .
    @return Margen(%) Segun Compra Presupuestado  */
    public java.math.BigDecimal getXX_POMARSCPR() 
    {
        return get_ValueAsBigDecimal("XX_POMARSCPR");
        
    }
    
    /** Set Margin (%) according Buy Real .
    @param XX_POMARSCRE Margen(%) Segun Compra Real  */
    public void setXX_POMARSCRE (java.math.BigDecimal XX_POMARSCRE)
    {
        set_Value ("XX_POMARSCRE", XX_POMARSCRE);
        
    }
    
    /** Get Margin (%) according Buy Real .
    @return Margen(%) Segun Compra Real  */
    public java.math.BigDecimal getXX_POMARSCRE() 
    {
        return get_ValueAsBigDecimal("XX_POMARSCRE");
        
    }
    
    /** Set Percentage of budgeted Coverage .
    @param XX_PORCOBPRE Porcentaje de Cobertura Presupuestada  */
    public void setXX_PORCOBPRE (java.math.BigDecimal XX_PORCOBPRE)
    {
        set_Value ("XX_PORCOBPRE", XX_PORCOBPRE);
        
    }
    
    /** Get Percentage of budgeted Coverage .
    @return Porcentaje de Cobertura Presupuestada  */
    public java.math.BigDecimal getXX_PORCOBPRE() 
    {
        return get_ValueAsBigDecimal("XX_PORCOBPRE");
        
    }
    
    /** Set Real Percent Coverage .
    @param XX_PORCOBREA Porcentaje de Cobertura Real  */
    public void setXX_PORCOBREA (java.math.BigDecimal XX_PORCOBREA)
    {
        set_Value ("XX_PORCOBREA", XX_PORCOBREA);
        
    }
    
    /** Get Real Percent Coverage .
    @return Porcentaje de Cobertura Real  */
    public java.math.BigDecimal getXX_PORCOBREA() 
    {
        return get_ValueAsBigDecimal("XX_PORCOBREA");
        
    }
    
    /** Set Percentage of Sale Final Budgeted .
    @param XX_POREBDFPR Porcentaje de Rebajas Definitivas Presupuestadas  */
    public void setXX_POREBDFPR (java.math.BigDecimal XX_POREBDFPR)
    {
        set_Value ("XX_POREBDFPR", XX_POREBDFPR);
        
    }
    
    /** Get Percentage of Sale Final Budgeted .
    @return Porcentaje de Rebajas Definitivas Presupuestadas  */
    public java.math.BigDecimal getXX_POREBDFPR() 
    {
        return get_ValueAsBigDecimal("XX_POREBDFPR");
        
    }
    
    /** Set Percentage of Actual Final Sale .
    @param XX_POREBDFRE Porcentaje de Rebajas Definitivas Reales  */
    public void setXX_POREBDFRE (java.math.BigDecimal XX_POREBDFRE)
    {
        set_Value ("XX_POREBDFRE", XX_POREBDFRE);
        
    }
    
    /** Get Percentage of Actual Final Sale .
    @return Porcentaje de Rebajas Definitivas Reales  */
    public java.math.BigDecimal getXX_POREBDFRE() 
    {
        return get_ValueAsBigDecimal("XX_POREBDFRE");
        
    }
    
    /** Set Poncentaje of Sale F.R Budgeted .
    @param XX_POREBFRPR Poncentaje de Rebajas F.R Presupuestadas  */
    public void setXX_POREBFRPR (java.math.BigDecimal XX_POREBFRPR)
    {
        set_Value ("XX_POREBFRPR", XX_POREBFRPR);
        
    }
    
    /** Get Poncentaje of Sale F.R Budgeted .
    @return Poncentaje de Rebajas F.R Presupuestadas  */
    public java.math.BigDecimal getXX_POREBFRPR() 
    {
        return get_ValueAsBigDecimal("XX_POREBFRPR");
        
    }
    
    /** Set Percentage of Sale F.R Interests .
    @param XX_POREBFRRE Porcentaje de Rebajas F.R Reales  */
    public void setXX_POREBFRRE (java.math.BigDecimal XX_POREBFRRE)
    {
        set_Value ("XX_POREBFRRE", XX_POREBFRRE);
        
    }
    
    /** Get Percentage of Sale F.R Interests .
    @return Porcentaje de Rebajas F.R Reales  */
    public java.math.BigDecimal getXX_POREBFRRE() 
    {
        return get_ValueAsBigDecimal("XX_POREBFRRE");
        
    }
    
    /** Set Percentage of Sale Promotional Budgeted  .
    @param XX_POREBPRPR Porcentaje de Rebajas Promocional Presupuestadas  */
    public void setXX_POREBPRPR (java.math.BigDecimal XX_POREBPRPR)
    {
        set_Value ("XX_POREBPRPR", XX_POREBPRPR);
        
    }
    
    /** Get Percentage of Sale Promotional Budgeted  .
    @return Porcentaje de Rebajas Promocional Presupuestadas  */
    public java.math.BigDecimal getXX_POREBPRPR() 
    {
        return get_ValueAsBigDecimal("XX_POREBPRPR");
        
    }
    
    /** Set Percentage of Sale Promotional Interests .
    @param XX_POREBPRRE Porcentaje de Rebajas Promocional Reales  */
    public void setXX_POREBPRRE (java.math.BigDecimal XX_POREBPRRE)
    {
        set_Value ("XX_POREBPRRE", XX_POREBPRRE);
        
    }
    
    /** Get Percentage of Sale Promotional Interests .
    @return Porcentaje de Rebajas Promocional Reales  */
    public java.math.BigDecimal getXX_POREBPRRE() 
    {
        return get_ValueAsBigDecimal("XX_POREBPRRE");
        
    }
    
    /** Set Rotation (%) Budgeted .
    @param XX_PORROTPRE Rotacion(%) Presupuestado  */
    public void setXX_PORROTPRE (java.math.BigDecimal XX_PORROTPRE)
    {
        set_Value ("XX_PORROTPRE", XX_PORROTPRE);
        
    }
    
    /** Get Rotation (%) Budgeted .
    @return Rotacion(%) Presupuestado  */
    public java.math.BigDecimal getXX_PORROTPRE() 
    {
        return get_ValueAsBigDecimal("XX_PORROTPRE");
        
    }
    
    /** Set Rotation (%) Real .
    @param XX_PORROTREA Rotacion(%) Real  */
    public void setXX_PORROTREA (java.math.BigDecimal XX_PORROTREA)
    {
        set_Value ("XX_PORROTREA", XX_PORROTREA);
        
    }
    
    /** Get Rotation (%) Real .
    @return Rotacion(%) Real  */
    public java.math.BigDecimal getXX_PORROTREA() 
    {
        return get_ValueAsBigDecimal("XX_PORROTREA");
        
    }
    
    /** Set Store.
    @param XX_TIENDA Tienda */
    public void setXX_TIENDA (String XX_TIENDA)
    {
        set_Value ("XX_TIENDA", XX_TIENDA);
        
    }
    
    /** Get Store.
    @return Tienda */
    public String getXX_TIENDA() 
    {
        return (String)get_Value("XX_TIENDA");
        
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
    
    /** Set XX_VMR_PRLD01_ID.
    @param XX_VMR_PRLD01_ID XX_VMR_PRLD01_ID */
    public void setXX_VMR_PRLD01_ID (int XX_VMR_PRLD01_ID)
    {
        if (XX_VMR_PRLD01_ID <= 0) set_Value ("XX_VMR_PRLD01_ID", null);
        else
        set_Value ("XX_VMR_PRLD01_ID", Integer.valueOf(XX_VMR_PRLD01_ID));
        
    }
    
    /** Get XX_VMR_PRLD01_ID.
    @return XX_VMR_PRLD01_ID */
    public int getXX_VMR_PRLD01_ID() 
    {
        return get_ValueAsInt("XX_VMR_PRLD01_ID");
        
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
