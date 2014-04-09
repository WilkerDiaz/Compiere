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
/** Generated Model for I_XX_VCN_COPD07
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VCN_COPD07 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VCN_COPD07_ID id
    @param trx transaction
    */
    public X_I_XX_VCN_COPD07 (Ctx ctx, int I_XX_VCN_COPD07_ID, Trx trx)
    {
        super (ctx, I_XX_VCN_COPD07_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VCN_COPD07_ID == 0)
        {
            setI_XX_VCN_COPD07_ID (0);
            setProcessed (false);	// N
            setProcessing (false);
            setXX_APLICADES (false);
            setXX_CODACU (Env.ZERO);
            setXX_CodCat (Env.ZERO);
            setXX_CODMON (Env.ZERO);
            setXX_COEMPE (Env.ZERO);
            setXX_DESCENEM (Env.ZERO);
            setXX_DESGOSM (Env.ZERO);
            setXX_DESGOSP (Env.ZERO);
            setXX_DESINAT (Env.ZERO);
            setXX_DESPARFM (Env.ZERO);
            setXX_DESPOSVM (Env.ZERO);
            setXX_DESRECMM (Env.ZERO);
            setXX_DESVOLFM (Env.ZERO);
            setXX_DESVOLFP (Env.ZERO);
            setXX_ESTREG (Env.ZERO);
            setXX_FECEXCD (new Timestamp(System.currentTimeMillis()));
            setXX_FECEXCH (new Timestamp(System.currentTimeMillis()));
            setXX_FECREG (new Timestamp(System.currentTimeMillis()));
            setXX_FECVIGD (new Timestamp(System.currentTimeMillis()));
            setXX_FECVIGH (new Timestamp(System.currentTimeMillis()));
            setXX_FORPAGA (Env.ZERO);
            setXX_MARCA (null);
            setXX_OBSERV (null);
            setXX_PDESCENE (Env.ZERO);
            setXX_PDESPARF (Env.ZERO);
            setXX_PDESPOSV (Env.ZERO);
            setXX_PDESRECM (Env.ZERO);
            setXX_PDESVOLVD (Env.ZERO);
            setXX_PDESVOLVH (Env.ZERO);
            setXX_PDESVOLVP (Env.ZERO);
            setXX_PORBECO (null);
            setXX_PORBECOCED (null);
            setXX_PORBECONAC (null);
            setXX_PORPROV (null);
            setXX_PORPROVCED (null);
            setXX_PORPROVNAC (null);
            setXX_PROACU (Env.ZERO);
            setXX_PROVEEDOR (null);
            setXX_SDESVOLVD (Env.ZERO);
            setXX_SDESVOLVH (Env.ZERO);
            setXX_SDESVOLVP (Env.ZERO);
            setXX_TDESINAT (Env.ZERO);
            setXX_TDESVOLVD (Env.ZERO);
            setXX_TDESVOLVH (Env.ZERO);
            setXX_TDESVOLVP (Env.ZERO);
            setXX_USRREG (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VCN_COPD07 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27533960401789L;
    /** Last Updated Timestamp 2009-09-01 15:28:05.0 */
    public static final long updatedMS = 1251835085000L;
    /** AD_Table_ID=1000105 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VCN_COPD07");
        
    }
    ;
    
    /** TableName=I_XX_VCN_COPD07 */
    public static final String Table_Name="I_XX_VCN_COPD07";
    
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
    
    /** Set COPD07 ID.
    @param I_XX_VCN_COPD07_ID Codigo de la Tabla COPD07 */
    public void setI_XX_VCN_COPD07_ID (int I_XX_VCN_COPD07_ID)
    {
        if (I_XX_VCN_COPD07_ID < 1) throw new IllegalArgumentException ("I_XX_VCN_COPD07_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VCN_COPD07_ID", Integer.valueOf(I_XX_VCN_COPD07_ID));
        
    }
    
    /** Get COPD07 ID.
    @return Codigo de la Tabla COPD07 */
    public int getI_XX_VCN_COPD07_ID() 
    {
        return get_ValueAsInt("I_XX_VCN_COPD07_ID");
        
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
    
    /** Set Apply Fixed Discount.
    @param XX_APLICADES Descuento Fijo aplica siempre (S/N) */
    public void setXX_APLICADES (boolean XX_APLICADES)
    {
        set_Value ("XX_APLICADES", Boolean.valueOf(XX_APLICADES));
        
    }
    
    /** Get Apply Fixed Discount.
    @return Descuento Fijo aplica siempre (S/N) */
    public boolean isXX_APLICADES() 
    {
        return get_ValueAsBoolean("XX_APLICADES");
        
    }
    
    /** Set Agreement Code.
    @param XX_CODACU Código Acuerdo */
    public void setXX_CODACU (java.math.BigDecimal XX_CODACU)
    {
        if (XX_CODACU == null) throw new IllegalArgumentException ("XX_CODACU is mandatory.");
        set_Value ("XX_CODACU", XX_CODACU);
        
    }
    
    /** Get Agreement Code.
    @return Código Acuerdo */
    public java.math.BigDecimal getXX_CODACU() 
    {
        return get_ValueAsBigDecimal("XX_CODACU");
        
    }
    
    /** Set Category Code.
    @param XX_CodCat Codigo de Categoria */
    public void setXX_CodCat (java.math.BigDecimal XX_CodCat)
    {
        if (XX_CodCat == null) throw new IllegalArgumentException ("XX_CodCat is mandatory.");
        set_Value ("XX_CodCat", XX_CodCat);
        
    }
    
    /** Get Category Code.
    @return Codigo de Categoria */
    public java.math.BigDecimal getXX_CodCat() 
    {
        return get_ValueAsBigDecimal("XX_CodCat");
        
    }
    
    /** Set Currency Code.
    @param XX_CODMON Código de moneda  */
    public void setXX_CODMON (java.math.BigDecimal XX_CODMON)
    {
        if (XX_CODMON == null) throw new IllegalArgumentException ("XX_CODMON is mandatory.");
        set_Value ("XX_CODMON", XX_CODMON);
        
    }
    
    /** Get Currency Code.
    @return Código de moneda  */
    public java.math.BigDecimal getXX_CODMON() 
    {
        return get_ValueAsBigDecimal("XX_CODMON");
        
    }
    
    /** Set Alternate Code PER/EMP.
    @param XX_COEMPE Codigo alterno PER/EMP */
    public void setXX_COEMPE (java.math.BigDecimal XX_COEMPE)
    {
        if (XX_COEMPE == null) throw new IllegalArgumentException ("XX_COEMPE is mandatory.");
        set_Value ("XX_COEMPE", XX_COEMPE);
        
    }
    
    /** Get Alternate Code PER/EMP.
    @return Codigo alterno PER/EMP */
    public java.math.BigDecimal getXX_COEMPE() 
    {
        return get_ValueAsBigDecimal("XX_COEMPE");
        
    }
    
    /** Set Centralization Discount Delivery Amount.
    @param XX_DESCENEM Descuento por Centralización de entregas Monto */
    public void setXX_DESCENEM (java.math.BigDecimal XX_DESCENEM)
    {
        if (XX_DESCENEM == null) throw new IllegalArgumentException ("XX_DESCENEM is mandatory.");
        set_Value ("XX_DESCENEM", XX_DESCENEM);
        
    }
    
    /** Get Centralization Discount Delivery Amount.
    @return Descuento por Centralización de entregas Monto */
    public java.math.BigDecimal getXX_DESCENEM() 
    {
        return get_ValueAsBigDecimal("XX_DESCENEM");
        
    }
    
    /** Set Discount Gos Amount.
    @param XX_DESGOSM Descuento por GOS Monto */
    public void setXX_DESGOSM (java.math.BigDecimal XX_DESGOSM)
    {
        if (XX_DESGOSM == null) throw new IllegalArgumentException ("XX_DESGOSM is mandatory.");
        set_Value ("XX_DESGOSM", XX_DESGOSM);
        
    }
    
    /** Get Discount Gos Amount.
    @return Descuento por GOS Monto */
    public java.math.BigDecimal getXX_DESGOSM() 
    {
        return get_ValueAsBigDecimal("XX_DESGOSM");
        
    }
    
    /** Set Discount Gos Percentaje.
    @param XX_DESGOSP Descuento por GOS % */
    public void setXX_DESGOSP (java.math.BigDecimal XX_DESGOSP)
    {
        if (XX_DESGOSP == null) throw new IllegalArgumentException ("XX_DESGOSP is mandatory.");
        set_Value ("XX_DESGOSP", XX_DESGOSP);
        
    }
    
    /** Get Discount Gos Percentaje.
    @return Descuento por GOS % */
    public java.math.BigDecimal getXX_DESGOSP() 
    {
        return get_ValueAsBigDecimal("XX_DESGOSP");
        
    }
    
    /** Set Credits to Reopening.
    @param XX_DESINAT Crédito por Inauguración de Tiendas */
    public void setXX_DESINAT (java.math.BigDecimal XX_DESINAT)
    {
        if (XX_DESINAT == null) throw new IllegalArgumentException ("XX_DESINAT is mandatory.");
        set_Value ("XX_DESINAT", XX_DESINAT);
        
    }
    
    /** Get Credits to Reopening.
    @return Crédito por Inauguración de Tiendas */
    public java.math.BigDecimal getXX_DESINAT() 
    {
        return get_ValueAsBigDecimal("XX_DESINAT");
        
    }
    
    /** Set Participation in Beco Brochure Amount.
    @param XX_DESPARFM Participación en folleto Beco Monto */
    public void setXX_DESPARFM (java.math.BigDecimal XX_DESPARFM)
    {
        if (XX_DESPARFM == null) throw new IllegalArgumentException ("XX_DESPARFM is mandatory.");
        set_Value ("XX_DESPARFM", XX_DESPARFM);
        
    }
    
    /** Get Participation in Beco Brochure Amount.
    @return Participación en folleto Beco Monto */
    public java.math.BigDecimal getXX_DESPARFM() 
    {
        return get_ValueAsBigDecimal("XX_DESPARFM");
        
    }
    
    /** Set Discount After Sale Amount.
    @param XX_DESPOSVM Descuento por Servicio Post Venta Monto */
    public void setXX_DESPOSVM (java.math.BigDecimal XX_DESPOSVM)
    {
        if (XX_DESPOSVM == null) throw new IllegalArgumentException ("XX_DESPOSVM is mandatory.");
        set_Value ("XX_DESPOSVM", XX_DESPOSVM);
        
    }
    
    /** Get Discount After Sale Amount.
    @return Descuento por Servicio Post Venta Monto */
    public java.math.BigDecimal getXX_DESPOSVM() 
    {
        return get_ValueAsBigDecimal("XX_DESPOSVM");
        
    }
    
    /** Set Discount Amount Recognition of Decline.
    @param XX_DESRECMM Descuento por Reconocimiento de merma Monto */
    public void setXX_DESRECMM (java.math.BigDecimal XX_DESRECMM)
    {
        if (XX_DESRECMM == null) throw new IllegalArgumentException ("XX_DESRECMM is mandatory.");
        set_Value ("XX_DESRECMM", XX_DESRECMM);
        
    }
    
    /** Get Discount Amount Recognition of Decline.
    @return Descuento por Reconocimiento de merma Monto */
    public java.math.BigDecimal getXX_DESRECMM() 
    {
        return get_ValueAsBigDecimal("XX_DESRECMM");
        
    }
    
    /** Set Fixed volume discount Amount.
    @param XX_DESVOLFM Descuento por volumen Fijo Monto */
    public void setXX_DESVOLFM (java.math.BigDecimal XX_DESVOLFM)
    {
        if (XX_DESVOLFM == null) throw new IllegalArgumentException ("XX_DESVOLFM is mandatory.");
        set_Value ("XX_DESVOLFM", XX_DESVOLFM);
        
    }
    
    /** Get Fixed volume discount Amount.
    @return Descuento por volumen Fijo Monto */
    public java.math.BigDecimal getXX_DESVOLFM() 
    {
        return get_ValueAsBigDecimal("XX_DESVOLFM");
        
    }
    
    /** Set Fixed volume discount Percentaje.
    @param XX_DESVOLFP Descuento por volumen Fijo % */
    public void setXX_DESVOLFP (java.math.BigDecimal XX_DESVOLFP)
    {
        if (XX_DESVOLFP == null) throw new IllegalArgumentException ("XX_DESVOLFP is mandatory.");
        set_Value ("XX_DESVOLFP", XX_DESVOLFP);
        
    }
    
    /** Get Fixed volume discount Percentaje.
    @return Descuento por volumen Fijo % */
    public java.math.BigDecimal getXX_DESVOLFP() 
    {
        return get_ValueAsBigDecimal("XX_DESVOLFP");
        
    }
    
    /** Set State Register.
    @param XX_ESTREG Estado Registro */
    public void setXX_ESTREG (java.math.BigDecimal XX_ESTREG)
    {
        if (XX_ESTREG == null) throw new IllegalArgumentException ("XX_ESTREG is mandatory.");
        set_Value ("XX_ESTREG", XX_ESTREG);
        
    }
    
    /** Get State Register.
    @return Estado Registro */
    public java.math.BigDecimal getXX_ESTREG() 
    {
        return get_ValueAsBigDecimal("XX_ESTREG");
        
    }
    
    /** Set Date Valid Exclusive From.
    @param XX_FECEXCD Fecha vigencia exclusividad desde */
    public void setXX_FECEXCD (Timestamp XX_FECEXCD)
    {
        if (XX_FECEXCD == null) throw new IllegalArgumentException ("XX_FECEXCD is mandatory.");
        set_ValueNoCheck ("XX_FECEXCD", XX_FECEXCD);
        
    }
    
    /** Get Date Valid Exclusive From.
    @return Fecha vigencia exclusividad desde */
    public Timestamp getXX_FECEXCD() 
    {
        return (Timestamp)get_Value("XX_FECEXCD");
        
    }
    
    /** Set Date Valid Exclusive To.
    @param XX_FECEXCH Fecha vigencia exclusividad hasta */
    public void setXX_FECEXCH (Timestamp XX_FECEXCH)
    {
        if (XX_FECEXCH == null) throw new IllegalArgumentException ("XX_FECEXCH is mandatory.");
        set_ValueNoCheck ("XX_FECEXCH", XX_FECEXCH);
        
    }
    
    /** Get Date Valid Exclusive To.
    @return Fecha vigencia exclusividad hasta */
    public Timestamp getXX_FECEXCH() 
    {
        return (Timestamp)get_Value("XX_FECEXCH");
        
    }
    
    /** Set Registration Date.
    @param XX_FECREG Fecha Registro */
    public void setXX_FECREG (Timestamp XX_FECREG)
    {
        if (XX_FECREG == null) throw new IllegalArgumentException ("XX_FECREG is mandatory.");
        set_ValueNoCheck ("XX_FECREG", XX_FECREG);
        
    }
    
    /** Get Registration Date.
    @return Fecha Registro */
    public Timestamp getXX_FECREG() 
    {
        return (Timestamp)get_Value("XX_FECREG");
        
    }
    
    /** Set Date Effective From.
    @param XX_FECVIGD Fecha Vigencia Desde */
    public void setXX_FECVIGD (Timestamp XX_FECVIGD)
    {
        if (XX_FECVIGD == null) throw new IllegalArgumentException ("XX_FECVIGD is mandatory.");
        set_ValueNoCheck ("XX_FECVIGD", XX_FECVIGD);
        
    }
    
    /** Get Date Effective From.
    @return Fecha Vigencia Desde */
    public Timestamp getXX_FECVIGD() 
    {
        return (Timestamp)get_Value("XX_FECVIGD");
        
    }
    
    /** Set Date Effective To.
    @param XX_FECVIGH Fecha Vigencia Hasta */
    public void setXX_FECVIGH (Timestamp XX_FECVIGH)
    {
        if (XX_FECVIGH == null) throw new IllegalArgumentException ("XX_FECVIGH is mandatory.");
        set_ValueNoCheck ("XX_FECVIGH", XX_FECVIGH);
        
    }
    
    /** Get Date Effective To.
    @return Fecha Vigencia Hasta */
    public Timestamp getXX_FECVIGH() 
    {
        return (Timestamp)get_Value("XX_FECVIGH");
        
    }
    
    /** Set Payment Rule.
    @param XX_FORPAGA Forma de pago de Acuerdo Comerciales */
    public void setXX_FORPAGA (java.math.BigDecimal XX_FORPAGA)
    {
        if (XX_FORPAGA == null) throw new IllegalArgumentException ("XX_FORPAGA is mandatory.");
        set_Value ("XX_FORPAGA", XX_FORPAGA);
        
    }
    
    /** Get Payment Rule.
    @return Forma de pago de Acuerdo Comerciales */
    public java.math.BigDecimal getXX_FORPAGA() 
    {
        return get_ValueAsBigDecimal("XX_FORPAGA");
        
    }
    
    /** Set Marca.
    @param XX_MARCA Marca */
    public void setXX_MARCA (String XX_MARCA)
    {
        if (XX_MARCA == null) throw new IllegalArgumentException ("XX_MARCA is mandatory.");
        set_Value ("XX_MARCA", XX_MARCA);
        
    }
    
    /** Get Marca.
    @return Marca */
    public String getXX_MARCA() 
    {
        return (String)get_Value("XX_MARCA");
        
    }
    
    /** Set Observations.
    @param XX_OBSERV Observaciones */
    public void setXX_OBSERV (String XX_OBSERV)
    {
        if (XX_OBSERV == null) throw new IllegalArgumentException ("XX_OBSERV is mandatory.");
        set_Value ("XX_OBSERV", XX_OBSERV);
        
    }
    
    /** Get Observations.
    @return Observaciones */
    public String getXX_OBSERV() 
    {
        return (String)get_Value("XX_OBSERV");
        
    }
    
    /** Set Centralization Discount delivery Percentaje.
    @param XX_PDESCENE Descuento por Centralización de entregas % */
    public void setXX_PDESCENE (java.math.BigDecimal XX_PDESCENE)
    {
        if (XX_PDESCENE == null) throw new IllegalArgumentException ("XX_PDESCENE is mandatory.");
        set_Value ("XX_PDESCENE", XX_PDESCENE);
        
    }
    
    /** Get Centralization Discount delivery Percentaje.
    @return Descuento por Centralización de entregas % */
    public java.math.BigDecimal getXX_PDESCENE() 
    {
        return get_ValueAsBigDecimal("XX_PDESCENE");
        
    }
    
    /** Set Participation in Beco Brochure Percentaje.
    @param XX_PDESPARF Participación en folleto Beco % */
    public void setXX_PDESPARF (java.math.BigDecimal XX_PDESPARF)
    {
        if (XX_PDESPARF == null) throw new IllegalArgumentException ("XX_PDESPARF is mandatory.");
        set_Value ("XX_PDESPARF", XX_PDESPARF);
        
    }
    
    /** Get Participation in Beco Brochure Percentaje.
    @return Participación en folleto Beco % */
    public java.math.BigDecimal getXX_PDESPARF() 
    {
        return get_ValueAsBigDecimal("XX_PDESPARF");
        
    }
    
    /** Set Discount After Sale Percentaje.
    @param XX_PDESPOSV Descuento por Servicio Post Venta % */
    public void setXX_PDESPOSV (java.math.BigDecimal XX_PDESPOSV)
    {
        if (XX_PDESPOSV == null) throw new IllegalArgumentException ("XX_PDESPOSV is mandatory.");
        set_Value ("XX_PDESPOSV", XX_PDESPOSV);
        
    }
    
    /** Get Discount After Sale Percentaje.
    @return Descuento por Servicio Post Venta % */
    public java.math.BigDecimal getXX_PDESPOSV() 
    {
        return get_ValueAsBigDecimal("XX_PDESPOSV");
        
    }
    
    /** Set Discount Recognition decline Percentaje.
    @param XX_PDESRECM Descuento por Reconocimiento de merma % */
    public void setXX_PDESRECM (java.math.BigDecimal XX_PDESRECM)
    {
        if (XX_PDESRECM == null) throw new IllegalArgumentException ("XX_PDESRECM is mandatory.");
        set_Value ("XX_PDESRECM", XX_PDESRECM);
        
    }
    
    /** Get Discount Recognition decline Percentaje.
    @return Descuento por Reconocimiento de merma % */
    public java.math.BigDecimal getXX_PDESRECM() 
    {
        return get_ValueAsBigDecimal("XX_PDESRECM");
        
    }
    
    /** Set First variable volume discount From.
    @param XX_PDESVOLVD Primer Descuento por volumen variable Desde */
    public void setXX_PDESVOLVD (java.math.BigDecimal XX_PDESVOLVD)
    {
        if (XX_PDESVOLVD == null) throw new IllegalArgumentException ("XX_PDESVOLVD is mandatory.");
        set_Value ("XX_PDESVOLVD", XX_PDESVOLVD);
        
    }
    
    /** Get First variable volume discount From.
    @return Primer Descuento por volumen variable Desde */
    public java.math.BigDecimal getXX_PDESVOLVD() 
    {
        return get_ValueAsBigDecimal("XX_PDESVOLVD");
        
    }
    
    /** Set First variable volume discount To.
    @param XX_PDESVOLVH Primer Descuento por volumen variable Hasta */
    public void setXX_PDESVOLVH (java.math.BigDecimal XX_PDESVOLVH)
    {
        if (XX_PDESVOLVH == null) throw new IllegalArgumentException ("XX_PDESVOLVH is mandatory.");
        set_Value ("XX_PDESVOLVH", XX_PDESVOLVH);
        
    }
    
    /** Get First variable volume discount To.
    @return Primer Descuento por volumen variable Hasta */
    public java.math.BigDecimal getXX_PDESVOLVH() 
    {
        return get_ValueAsBigDecimal("XX_PDESVOLVH");
        
    }
    
    /** Set First variable volume discount Percentaje.
    @param XX_PDESVOLVP Primer Descuento por volumen variable % */
    public void setXX_PDESVOLVP (java.math.BigDecimal XX_PDESVOLVP)
    {
        if (XX_PDESVOLVP == null) throw new IllegalArgumentException ("XX_PDESVOLVP is mandatory.");
        set_Value ("XX_PDESVOLVP", XX_PDESVOLVP);
        
    }
    
    /** Get First variable volume discount Percentaje.
    @return Primer Descuento por volumen variable % */
    public java.math.BigDecimal getXX_PDESVOLVP() 
    {
        return get_ValueAsBigDecimal("XX_PDESVOLVP");
        
    }
    
    /** Set Beco Representative.
    @param XX_PORBECO Negociado por Beco */
    public void setXX_PORBECO (String XX_PORBECO)
    {
        if (XX_PORBECO == null) throw new IllegalArgumentException ("XX_PORBECO is mandatory.");
        set_Value ("XX_PORBECO", XX_PORBECO);
        
    }
    
    /** Get Beco Representative.
    @return Negociado por Beco */
    public String getXX_PORBECO() 
    {
        return (String)get_Value("XX_PORBECO");
        
    }
    
    /** Set Cedula Beco Representative.
    @param XX_PORBECOCED Cédula negociador */
    public void setXX_PORBECOCED (String XX_PORBECOCED)
    {
        if (XX_PORBECOCED == null) throw new IllegalArgumentException ("XX_PORBECOCED is mandatory.");
        set_Value ("XX_PORBECOCED", XX_PORBECOCED);
        
    }
    
    /** Get Cedula Beco Representative.
    @return Cédula negociador */
    public String getXX_PORBECOCED() 
    {
        return (String)get_Value("XX_PORBECOCED");
        
    }
    
    /** Set Nationality Beco Representative.
    @param XX_PORBECONAC Nacionalidad negociador */
    public void setXX_PORBECONAC (String XX_PORBECONAC)
    {
        if (XX_PORBECONAC == null) throw new IllegalArgumentException ("XX_PORBECONAC is mandatory.");
        set_Value ("XX_PORBECONAC", XX_PORBECONAC);
        
    }
    
    /** Get Nationality Beco Representative.
    @return Nacionalidad negociador */
    public String getXX_PORBECONAC() 
    {
        return (String)get_Value("XX_PORBECONAC");
        
    }
    
    /** Set Vendor Representative.
    @param XX_PORPROV Negociado por Proveedor */
    public void setXX_PORPROV (String XX_PORPROV)
    {
        if (XX_PORPROV == null) throw new IllegalArgumentException ("XX_PORPROV is mandatory.");
        set_Value ("XX_PORPROV", XX_PORPROV);
        
    }
    
    /** Get Vendor Representative.
    @return Negociado por Proveedor */
    public String getXX_PORPROV() 
    {
        return (String)get_Value("XX_PORPROV");
        
    }
    
    /** Set Cedula Vendor Representative.
    @param XX_PORPROVCED Cedula proveedor */
    public void setXX_PORPROVCED (String XX_PORPROVCED)
    {
        if (XX_PORPROVCED == null) throw new IllegalArgumentException ("XX_PORPROVCED is mandatory.");
        set_Value ("XX_PORPROVCED", XX_PORPROVCED);
        
    }
    
    /** Get Cedula Vendor Representative.
    @return Cedula proveedor */
    public String getXX_PORPROVCED() 
    {
        return (String)get_Value("XX_PORPROVCED");
        
    }
    
    /** Set Nationality Vendor Representative.
    @param XX_PORPROVNAC Nacionalidad proveedor */
    public void setXX_PORPROVNAC (String XX_PORPROVNAC)
    {
        if (XX_PORPROVNAC == null) throw new IllegalArgumentException ("XX_PORPROVNAC is mandatory.");
        set_Value ("XX_PORPROVNAC", XX_PORPROVNAC);
        
    }
    
    /** Get Nationality Vendor Representative.
    @return Nacionalidad proveedor */
    public String getXX_PORPROVNAC() 
    {
        return (String)get_Value("XX_PORPROVNAC");
        
    }
    
    /** Set Product Trade Agreement.
    @param XX_PROACU Productos Acuerdo Comercial */
    public void setXX_PROACU (java.math.BigDecimal XX_PROACU)
    {
        if (XX_PROACU == null) throw new IllegalArgumentException ("XX_PROACU is mandatory.");
        set_Value ("XX_PROACU", XX_PROACU);
        
    }
    
    /** Get Product Trade Agreement.
    @return Productos Acuerdo Comercial */
    public java.math.BigDecimal getXX_PROACU() 
    {
        return get_ValueAsBigDecimal("XX_PROACU");
        
    }
    
    /** Set Vendor Name.
    @param XX_PROVEEDOR Denominación del Proveedor */
    public void setXX_PROVEEDOR (String XX_PROVEEDOR)
    {
        if (XX_PROVEEDOR == null) throw new IllegalArgumentException ("XX_PROVEEDOR is mandatory.");
        set_Value ("XX_PROVEEDOR", XX_PROVEEDOR);
        
    }
    
    /** Get Vendor Name.
    @return Denominación del Proveedor */
    public String getXX_PROVEEDOR() 
    {
        return (String)get_Value("XX_PROVEEDOR");
        
    }
    
    /** Set Second variable volume discount From.
    @param XX_SDESVOLVD Segundo Descuento por volumen variable Desde */
    public void setXX_SDESVOLVD (java.math.BigDecimal XX_SDESVOLVD)
    {
        if (XX_SDESVOLVD == null) throw new IllegalArgumentException ("XX_SDESVOLVD is mandatory.");
        set_Value ("XX_SDESVOLVD", XX_SDESVOLVD);
        
    }
    
    /** Get Second variable volume discount From.
    @return Segundo Descuento por volumen variable Desde */
    public java.math.BigDecimal getXX_SDESVOLVD() 
    {
        return get_ValueAsBigDecimal("XX_SDESVOLVD");
        
    }
    
    /** Set Second variable volume discount To.
    @param XX_SDESVOLVH Segundo Descuento por volumen variable Hasta */
    public void setXX_SDESVOLVH (java.math.BigDecimal XX_SDESVOLVH)
    {
        if (XX_SDESVOLVH == null) throw new IllegalArgumentException ("XX_SDESVOLVH is mandatory.");
        set_Value ("XX_SDESVOLVH", XX_SDESVOLVH);
        
    }
    
    /** Get Second variable volume discount To.
    @return Segundo Descuento por volumen variable Hasta */
    public java.math.BigDecimal getXX_SDESVOLVH() 
    {
        return get_ValueAsBigDecimal("XX_SDESVOLVH");
        
    }
    
    /** Set Second variable volume discount Percentaje.
    @param XX_SDESVOLVP Segundo Descuento por volumen variable % */
    public void setXX_SDESVOLVP (java.math.BigDecimal XX_SDESVOLVP)
    {
        if (XX_SDESVOLVP == null) throw new IllegalArgumentException ("XX_SDESVOLVP is mandatory.");
        set_Value ("XX_SDESVOLVP", XX_SDESVOLVP);
        
    }
    
    /** Get Second variable volume discount Percentaje.
    @return Segundo Descuento por volumen variable % */
    public java.math.BigDecimal getXX_SDESVOLVP() 
    {
        return get_ValueAsBigDecimal("XX_SDESVOLVP");
        
    }
    
    /** Set Type of Credit Store Opening.
    @param XX_TDESINAT Tipo de Crédito por Inauguración de Tiendas */
    public void setXX_TDESINAT (java.math.BigDecimal XX_TDESINAT)
    {
        if (XX_TDESINAT == null) throw new IllegalArgumentException ("XX_TDESINAT is mandatory.");
        set_Value ("XX_TDESINAT", XX_TDESINAT);
        
    }
    
    /** Get Type of Credit Store Opening.
    @return Tipo de Crédito por Inauguración de Tiendas */
    public java.math.BigDecimal getXX_TDESINAT() 
    {
        return get_ValueAsBigDecimal("XX_TDESINAT");
        
    }
    
    /** Set Third variable volume discount From.
    @param XX_TDESVOLVD Tercer Descuento por volumen variable Desde */
    public void setXX_TDESVOLVD (java.math.BigDecimal XX_TDESVOLVD)
    {
        if (XX_TDESVOLVD == null) throw new IllegalArgumentException ("XX_TDESVOLVD is mandatory.");
        set_Value ("XX_TDESVOLVD", XX_TDESVOLVD);
        
    }
    
    /** Get Third variable volume discount From.
    @return Tercer Descuento por volumen variable Desde */
    public java.math.BigDecimal getXX_TDESVOLVD() 
    {
        return get_ValueAsBigDecimal("XX_TDESVOLVD");
        
    }
    
    /** Set Third variable volume discount To.
    @param XX_TDESVOLVH Tercer Descuento por volumen variable Hasta */
    public void setXX_TDESVOLVH (java.math.BigDecimal XX_TDESVOLVH)
    {
        if (XX_TDESVOLVH == null) throw new IllegalArgumentException ("XX_TDESVOLVH is mandatory.");
        set_Value ("XX_TDESVOLVH", XX_TDESVOLVH);
        
    }
    
    /** Get Third variable volume discount To.
    @return Tercer Descuento por volumen variable Hasta */
    public java.math.BigDecimal getXX_TDESVOLVH() 
    {
        return get_ValueAsBigDecimal("XX_TDESVOLVH");
        
    }
    
    /** Set Third variable volume discount Percentaje.
    @param XX_TDESVOLVP Tercer Descuento por volumen variable % */
    public void setXX_TDESVOLVP (java.math.BigDecimal XX_TDESVOLVP)
    {
        if (XX_TDESVOLVP == null) throw new IllegalArgumentException ("XX_TDESVOLVP is mandatory.");
        set_Value ("XX_TDESVOLVP", XX_TDESVOLVP);
        
    }
    
    /** Get Third variable volume discount Percentaje.
    @return Tercer Descuento por volumen variable % */
    public java.math.BigDecimal getXX_TDESVOLVP() 
    {
        return get_ValueAsBigDecimal("XX_TDESVOLVP");
        
    }
    
    /** Set CreatedBy.
    @param XX_USRREG Usuario Registro */
    public void setXX_USRREG (String XX_USRREG)
    {
        if (XX_USRREG == null) throw new IllegalArgumentException ("XX_USRREG is mandatory.");
        set_Value ("XX_USRREG", XX_USRREG);
        
    }
    
    /** Get CreatedBy.
    @return Usuario Registro */
    public String getXX_USRREG() 
    {
        return (String)get_Value("XX_USRREG");
        
    }
    
    /** Set Trade Agreement.
    @param XX_VCN_TradeAgreements_ID Id de la tabla Acuerdos Comerciales */
    public void setXX_VCN_TradeAgreements_ID (java.math.BigDecimal XX_VCN_TradeAgreements_ID)
    {
        set_ValueNoCheck ("XX_VCN_TradeAgreements_ID", XX_VCN_TradeAgreements_ID);
        
    }
    
    /** Get Trade Agreement.
    @return Id de la tabla Acuerdos Comerciales */
    public java.math.BigDecimal getXX_VCN_TradeAgreements_ID() 
    {
        return get_ValueAsBigDecimal("XX_VCN_TradeAgreements_ID");
        
    }
    
    
}
