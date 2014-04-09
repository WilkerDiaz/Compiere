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
/** Generated Model for XX_VMR_Prld01
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Prld01 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Prld01_ID id
    @param trx transaction
    */
    public X_XX_VMR_Prld01 (Ctx ctx, int XX_VMR_Prld01_ID, Trx trx)
    {
        super (ctx, XX_VMR_Prld01_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Prld01_ID == 0)
        {
            setXX_VMR_PRLD01_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Prld01 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27616280502789L;
    /** Last Updated Timestamp 2012-04-11 10:09:46.0 */
    public static final long updatedMS = 1334155186000L;
    /** AD_Table_ID=1000070 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Prld01");
        
    }
    ;
    
    /** TableName=XX_VMR_Prld01 */
    public static final String Table_Name="XX_VMR_Prld01";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
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
    
    /** Set XX_ACTAMOUNTSALEFR.
    @param XX_ACTAMOUNTSALEFR XX_ACTAMOUNTSALEFR */
    public void setXX_ACTAMOUNTSALEFR (java.math.BigDecimal XX_ACTAMOUNTSALEFR)
    {
        set_Value ("XX_ACTAMOUNTSALEFR", XX_ACTAMOUNTSALEFR);
        
    }
    
    /** Get XX_ACTAMOUNTSALEFR.
    @return XX_ACTAMOUNTSALEFR */
    public java.math.BigDecimal getXX_ACTAMOUNTSALEFR() 
    {
        return get_ValueAsBigDecimal("XX_ACTAMOUNTSALEFR");
        
    }
    
    /** Set XX_ACTAMOUNTSALEPROM.
    @param XX_ACTAMOUNTSALEPROM XX_ACTAMOUNTSALEPROM */
    public void setXX_ACTAMOUNTSALEPROM (java.math.BigDecimal XX_ACTAMOUNTSALEPROM)
    {
        set_Value ("XX_ACTAMOUNTSALEPROM", XX_ACTAMOUNTSALEPROM);
        
    }
    
    /** Get XX_ACTAMOUNTSALEPROM.
    @return XX_ACTAMOUNTSALEPROM */
    public java.math.BigDecimal getXX_ACTAMOUNTSALEPROM() 
    {
        return get_ValueAsBigDecimal("XX_ACTAMOUNTSALEPROM");
        
    }
    
    /** Set XX_AMOUNTACTUALSALE.
    @param XX_AMOUNTACTUALSALE XX_AMOUNTACTUALSALE */
    public void setXX_AMOUNTACTUALSALE (java.math.BigDecimal XX_AMOUNTACTUALSALE)
    {
        set_Value ("XX_AMOUNTACTUALSALE", XX_AMOUNTACTUALSALE);
        
    }
    
    /** Get XX_AMOUNTACTUALSALE.
    @return XX_AMOUNTACTUALSALE */
    public java.math.BigDecimal getXX_AMOUNTACTUALSALE() 
    {
        return get_ValueAsBigDecimal("XX_AMOUNTACTUALSALE");
        
    }
    
    /** Set XX_AMOUNTINIINVEREAL.
    @param XX_AMOUNTINIINVEREAL XX_AMOUNTINIINVEREAL */
    public void setXX_AMOUNTINIINVEREAL (java.math.BigDecimal XX_AMOUNTINIINVEREAL)
    {
        set_Value ("XX_AMOUNTINIINVEREAL", XX_AMOUNTINIINVEREAL);
        
    }
    
    /** Get XX_AMOUNTINIINVEREAL.
    @return XX_AMOUNTINIINVEREAL */
    public java.math.BigDecimal getXX_AMOUNTINIINVEREAL() 
    {
        return get_ValueAsBigDecimal("XX_AMOUNTINIINVEREAL");
        
    }
    
    /** Set XX_AMOUNTPLACEDNACCAMPRA.
    @param XX_AMOUNTPLACEDNACCAMPRA XX_AMOUNTPLACEDNACCAMPRA */
    public void setXX_AMOUNTPLACEDNACCAMPRA (java.math.BigDecimal XX_AMOUNTPLACEDNACCAMPRA)
    {
        set_Value ("XX_AMOUNTPLACEDNACCAMPRA", XX_AMOUNTPLACEDNACCAMPRA);
        
    }
    
    /** Get XX_AMOUNTPLACEDNACCAMPRA.
    @return XX_AMOUNTPLACEDNACCAMPRA */
    public java.math.BigDecimal getXX_AMOUNTPLACEDNACCAMPRA() 
    {
        return get_ValueAsBigDecimal("XX_AMOUNTPLACEDNACCAMPRA");
        
    }
    
    /** Set XX_AMOUNTPLACEDNACPURCHCOST.
    @param XX_AMOUNTPLACEDNACPURCHCOST XX_AMOUNTPLACEDNACPURCHCOST */
    public void setXX_AMOUNTPLACEDNACPURCHCOST (java.math.BigDecimal XX_AMOUNTPLACEDNACPURCHCOST)
    {
        set_Value ("XX_AMOUNTPLACEDNACPURCHCOST", XX_AMOUNTPLACEDNACPURCHCOST);
        
    }
    
    /** Get XX_AMOUNTPLACEDNACPURCHCOST.
    @return XX_AMOUNTPLACEDNACPURCHCOST */
    public java.math.BigDecimal getXX_AMOUNTPLACEDNACPURCHCOST() 
    {
        return get_ValueAsBigDecimal("XX_AMOUNTPLACEDNACPURCHCOST");
        
    }
    
    /** Set XX_AMOUNTPURCHASELIMIT.
    @param XX_AMOUNTPURCHASELIMIT XX_AMOUNTPURCHASELIMIT */
    public void setXX_AMOUNTPURCHASELIMIT (java.math.BigDecimal XX_AMOUNTPURCHASELIMIT)
    {
        set_Value ("XX_AMOUNTPURCHASELIMIT", XX_AMOUNTPURCHASELIMIT);
        
    }
    
    /** Get XX_AMOUNTPURCHASELIMIT.
    @return XX_AMOUNTPURCHASELIMIT */
    public java.math.BigDecimal getXX_AMOUNTPURCHASELIMIT() 
    {
        return get_ValueAsBigDecimal("XX_AMOUNTPURCHASELIMIT");
        
    }
    
    /** Set XX_AMOUNTSALECOST.
    @param XX_AMOUNTSALECOST XX_AMOUNTSALECOST */
    public void setXX_AMOUNTSALECOST (java.math.BigDecimal XX_AMOUNTSALECOST)
    {
        set_Value ("XX_AMOUNTSALECOST", XX_AMOUNTSALECOST);
        
    }
    
    /** Get XX_AMOUNTSALECOST.
    @return XX_AMOUNTSALECOST */
    public java.math.BigDecimal getXX_AMOUNTSALECOST() 
    {
        return get_ValueAsBigDecimal("XX_AMOUNTSALECOST");
        
    }
    
    /** Set Amount of Sale F.R Budgeted.
    @param XX_AMOUNTSALEFRBUD Monto de Rebajas F.R Presupuestadas */
    public void setXX_AMOUNTSALEFRBUD (java.math.BigDecimal XX_AMOUNTSALEFRBUD)
    {
        set_Value ("XX_AMOUNTSALEFRBUD", XX_AMOUNTSALEFRBUD);
        
    }
    
    /** Get Amount of Sale F.R Budgeted.
    @return Monto de Rebajas F.R Presupuestadas */
    public java.math.BigDecimal getXX_AMOUNTSALEFRBUD() 
    {
        return get_ValueAsBigDecimal("XX_AMOUNTSALEFRBUD");
        
    }
    
    /** Set XX_AMOUNTSALEFRINTERESTS.
    @param XX_AMOUNTSALEFRINTERESTS XX_AMOUNTSALEFRINTERESTS */
    public void setXX_AMOUNTSALEFRINTERESTS (java.math.BigDecimal XX_AMOUNTSALEFRINTERESTS)
    {
        set_Value ("XX_AMOUNTSALEFRINTERESTS", XX_AMOUNTSALEFRINTERESTS);
        
    }
    
    /** Get XX_AMOUNTSALEFRINTERESTS.
    @return XX_AMOUNTSALEFRINTERESTS */
    public java.math.BigDecimal getXX_AMOUNTSALEFRINTERESTS() 
    {
        return get_ValueAsBigDecimal("XX_AMOUNTSALEFRINTERESTS");
        
    }
    
    /** Set XX_AMOUNTSALEPROMINTERESTS.
    @param XX_AMOUNTSALEPROMINTERESTS XX_AMOUNTSALEPROMINTERESTS */
    public void setXX_AMOUNTSALEPROMINTERESTS (java.math.BigDecimal XX_AMOUNTSALEPROMINTERESTS)
    {
        set_Value ("XX_AMOUNTSALEPROMINTERESTS", XX_AMOUNTSALEPROMINTERESTS);
        
    }
    
    /** Get XX_AMOUNTSALEPROMINTERESTS.
    @return XX_AMOUNTSALEPROMINTERESTS */
    public java.math.BigDecimal getXX_AMOUNTSALEPROMINTERESTS() 
    {
        return get_ValueAsBigDecimal("XX_AMOUNTSALEPROMINTERESTS");
        
    }
    
    /** Set Budgeted amount of F.R Sale.
    @param XX_BUDAMOUNTFRSALE Cantidad de Rebajas F.R Presupuestadas */
    public void setXX_BUDAMOUNTFRSALE (java.math.BigDecimal XX_BUDAMOUNTFRSALE)
    {
        set_Value ("XX_BUDAMOUNTFRSALE", XX_BUDAMOUNTFRSALE);
        
    }
    
    /** Get Budgeted amount of F.R Sale.
    @return Cantidad de Rebajas F.R Presupuestadas */
    public java.math.BigDecimal getXX_BUDAMOUNTFRSALE() 
    {
        return get_ValueAsBigDecimal("XX_BUDAMOUNTFRSALE");
        
    }
    
    /** Set Budgeted decline.
    @param XX_BUDDDECLINE Merma Presupuestada */
    public void setXX_BUDDDECLINE (java.math.BigDecimal XX_BUDDDECLINE)
    {
        set_Value ("XX_BUDDDECLINE", XX_BUDDDECLINE);
        
    }
    
    /** Get Budgeted decline.
    @return Merma Presupuestada */
    public java.math.BigDecimal getXX_BUDDDECLINE() 
    {
        return get_ValueAsBigDecimal("XX_BUDDDECLINE");
        
    }
    
    /** Set XX_BUDGETYEARMONTH.
    @param XX_BUDGETYEARMONTH XX_BUDGETYEARMONTH */
    public void setXX_BUDGETYEARMONTH (int XX_BUDGETYEARMONTH)
    {
        set_Value ("XX_BUDGETYEARMONTH", Integer.valueOf(XX_BUDGETYEARMONTH));
        
    }
    
    /** Get XX_BUDGETYEARMONTH.
    @return XX_BUDGETYEARMONTH */
    public int getXX_BUDGETYEARMONTH() 
    {
        return get_ValueAsInt("XX_BUDGETYEARMONTH");
        
    }
    
    /** Set By Winning Margin Percentage Budgeted.
    @param XX_BYWINMARGPERTBUD Porcentaje de Margen Por Ganar Presupuestado */
    public void setXX_BYWINMARGPERTBUD (java.math.BigDecimal XX_BYWINMARGPERTBUD)
    {
        set_Value ("XX_BYWINMARGPERTBUD", XX_BYWINMARGPERTBUD);
        
    }
    
    /** Get By Winning Margin Percentage Budgeted.
    @return Porcentaje de Margen Por Ganar Presupuestado */
    public java.math.BigDecimal getXX_BYWINMARGPERTBUD() 
    {
        return get_ValueAsBigDecimal("XX_BYWINMARGPERTBUD");
        
    }
    
    /** Set XX_FINALACTAMOUNTSALE.
    @param XX_FINALACTAMOUNTSALE XX_FINALACTAMOUNTSALE */
    public void setXX_FINALACTAMOUNTSALE (java.math.BigDecimal XX_FINALACTAMOUNTSALE)
    {
        set_Value ("XX_FINALACTAMOUNTSALE", XX_FINALACTAMOUNTSALE);
        
    }
    
    /** Get XX_FINALACTAMOUNTSALE.
    @return XX_FINALACTAMOUNTSALE */
    public java.math.BigDecimal getXX_FINALACTAMOUNTSALE() 
    {
        return get_ValueAsBigDecimal("XX_FINALACTAMOUNTSALE");
        
    }
    
    /** Set XX_FINALBUDAMOUNTSALE.
    @param XX_FINALBUDAMOUNTSALE XX_FINALBUDAMOUNTSALE */
    public void setXX_FINALBUDAMOUNTSALE (java.math.BigDecimal XX_FINALBUDAMOUNTSALE)
    {
        set_Value ("XX_FINALBUDAMOUNTSALE", XX_FINALBUDAMOUNTSALE);
        
    }
    
    /** Get XX_FINALBUDAMOUNTSALE.
    @return XX_FINALBUDAMOUNTSALE */
    public java.math.BigDecimal getXX_FINALBUDAMOUNTSALE() 
    {
        return get_ValueAsBigDecimal("XX_FINALBUDAMOUNTSALE");
        
    }
    
    /** Set Final Inventory Quantity Budgeted.
    @param XX_FINALINVAMOUNTBUD Cantidad de Inventario Final Presupuestado */
    public void setXX_FINALINVAMOUNTBUD (java.math.BigDecimal XX_FINALINVAMOUNTBUD)
    {
        set_Value ("XX_FINALINVAMOUNTBUD", XX_FINALINVAMOUNTBUD);
        
    }
    
    /** Get Final Inventory Quantity Budgeted.
    @return Cantidad de Inventario Final Presupuestado */
    public java.math.BigDecimal getXX_FINALINVAMOUNTBUD() 
    {
        return get_ValueAsBigDecimal("XX_FINALINVAMOUNTBUD");
        
    }
    
    /** Set Final Inventory Amount Budgeted.
    @param XX_FINALINVAMOUNTBUD2 Monto de Inventario Final Presupuestado */
    public void setXX_FINALINVAMOUNTBUD2 (java.math.BigDecimal XX_FINALINVAMOUNTBUD2)
    {
        set_Value ("XX_FINALINVAMOUNTBUD2", XX_FINALINVAMOUNTBUD2);
        
    }
    
    /** Get Final Inventory Amount Budgeted.
    @return Monto de Inventario Final Presupuestado */
    public java.math.BigDecimal getXX_FINALINVAMOUNTBUD2() 
    {
        return get_ValueAsBigDecimal("XX_FINALINVAMOUNTBUD2");
        
    }
    
    /** Set XX_FINALINVAMOUNTPROJD.
    @param XX_FINALINVAMOUNTPROJD XX_FINALINVAMOUNTPROJD */
    public void setXX_FINALINVAMOUNTPROJD (java.math.BigDecimal XX_FINALINVAMOUNTPROJD)
    {
        set_Value ("XX_FINALINVAMOUNTPROJD", XX_FINALINVAMOUNTPROJD);
        
    }
    
    /** Get XX_FINALINVAMOUNTPROJD.
    @return XX_FINALINVAMOUNTPROJD */
    public java.math.BigDecimal getXX_FINALINVAMOUNTPROJD() 
    {
        return get_ValueAsBigDecimal("XX_FINALINVAMOUNTPROJD");
        
    }
    
    /** Set XX_FINALSALEAMOUNTBUD.
    @param XX_FINALSALEAMOUNTBUD XX_FINALSALEAMOUNTBUD */
    public void setXX_FINALSALEAMOUNTBUD (java.math.BigDecimal XX_FINALSALEAMOUNTBUD)
    {
        set_Value ("XX_FINALSALEAMOUNTBUD", XX_FINALSALEAMOUNTBUD);
        
    }
    
    /** Get XX_FINALSALEAMOUNTBUD.
    @return XX_FINALSALEAMOUNTBUD */
    public java.math.BigDecimal getXX_FINALSALEAMOUNTBUD() 
    {
        return get_ValueAsBigDecimal("XX_FINALSALEAMOUNTBUD");
        
    }
    
    /** Set XX_FINALSALEAMOUNTINTERESTS.
    @param XX_FINALSALEAMOUNTINTERESTS XX_FINALSALEAMOUNTINTERESTS */
    public void setXX_FINALSALEAMOUNTINTERESTS (java.math.BigDecimal XX_FINALSALEAMOUNTINTERESTS)
    {
        set_Value ("XX_FINALSALEAMOUNTINTERESTS", XX_FINALSALEAMOUNTINTERESTS);
        
    }
    
    /** Get XX_FINALSALEAMOUNTINTERESTS.
    @return XX_FINALSALEAMOUNTINTERESTS */
    public java.math.BigDecimal getXX_FINALSALEAMOUNTINTERESTS() 
    {
        return get_ValueAsBigDecimal("XX_FINALSALEAMOUNTINTERESTS");
        
    }
    
    /** Set XX_INIAMOUNTINVECOST.
    @param XX_INIAMOUNTINVECOST XX_INIAMOUNTINVECOST */
    public void setXX_INIAMOUNTINVECOST (java.math.BigDecimal XX_INIAMOUNTINVECOST)
    {
        set_Value ("XX_INIAMOUNTINVECOST", XX_INIAMOUNTINVECOST);
        
    }
    
    /** Get XX_INIAMOUNTINVECOST.
    @return XX_INIAMOUNTINVECOST */
    public java.math.BigDecimal getXX_INIAMOUNTINVECOST() 
    {
        return get_ValueAsBigDecimal("XX_INIAMOUNTINVECOST");
        
    }
    
    /** Set XX_INVAMOUNTFINALREAL.
    @param XX_INVAMOUNTFINALREAL XX_INVAMOUNTFINALREAL */
    public void setXX_INVAMOUNTFINALREAL (java.math.BigDecimal XX_INVAMOUNTFINALREAL)
    {
        set_Value ("XX_INVAMOUNTFINALREAL", XX_INVAMOUNTFINALREAL);
        
    }
    
    /** Get XX_INVAMOUNTFINALREAL.
    @return XX_INVAMOUNTFINALREAL */
    public java.math.BigDecimal getXX_INVAMOUNTFINALREAL() 
    {
        return get_ValueAsBigDecimal("XX_INVAMOUNTFINALREAL");
        
    }
    
    /** Set Inventory amount originally budgeted.
    @param XX_INVAMOUNTORIGBUDGETED Cantidad de Inventario Inicial Presupuestado  */
    public void setXX_INVAMOUNTORIGBUDGETED (java.math.BigDecimal XX_INVAMOUNTORIGBUDGETED)
    {
        set_Value ("XX_INVAMOUNTORIGBUDGETED", XX_INVAMOUNTORIGBUDGETED);
        
    }
    
    /** Get Inventory amount originally budgeted.
    @return Cantidad de Inventario Inicial Presupuestado  */
    public java.math.BigDecimal getXX_INVAMOUNTORIGBUDGETED() 
    {
        return get_ValueAsBigDecimal("XX_INVAMOUNTORIGBUDGETED");
        
    }
    
    /** Set Inventory Efetue Budgeted Amount.
    @param XX_INVEFECBUDGETEDAMOUNT Monto de Inventario Inicial Presupuestado */
    public void setXX_INVEFECBUDGETEDAMOUNT (java.math.BigDecimal XX_INVEFECBUDGETEDAMOUNT)
    {
        set_Value ("XX_INVEFECBUDGETEDAMOUNT", XX_INVEFECBUDGETEDAMOUNT);
        
    }
    
    /** Get Inventory Efetue Budgeted Amount.
    @return Monto de Inventario Inicial Presupuestado */
    public java.math.BigDecimal getXX_INVEFECBUDGETEDAMOUNT() 
    {
        return get_ValueAsBigDecimal("XX_INVEFECBUDGETEDAMOUNT");
        
    }
    
    /** Set Line Code.
    @param XX_LineCode Código de línea */
    public void setXX_LineCode (java.math.BigDecimal XX_LineCode)
    {
        set_Value ("XX_LineCode", XX_LineCode);
        
    }
    
    /** Get Line Code.
    @return Código de línea */
    public java.math.BigDecimal getXX_LineCode() 
    {
        return get_ValueAsBigDecimal("XX_LineCode");
        
    }
    
    /** Set Livestock Gross Margin Percent Presupuestado.
    @param XX_LISCKGROSSMARGPERCTBUD Porcentaje de Margen Bruto Ganado Presupuestado */
    public void setXX_LISCKGROSSMARGPERCTBUD (java.math.BigDecimal XX_LISCKGROSSMARGPERCTBUD)
    {
        set_Value ("XX_LISCKGROSSMARGPERCTBUD", XX_LISCKGROSSMARGPERCTBUD);
        
    }
    
    /** Get Livestock Gross Margin Percent Presupuestado.
    @return Porcentaje de Margen Bruto Ganado Presupuestado */
    public java.math.BigDecimal getXX_LISCKGROSSMARGPERCTBUD() 
    {
        return get_ValueAsBigDecimal("XX_LISCKGROSSMARGPERCTBUD");
        
    }
    
    /** Set XX_LISCKGROSSMARGPERTREAL.
    @param XX_LISCKGROSSMARGPERTREAL XX_LISCKGROSSMARGPERTREAL */
    public void setXX_LISCKGROSSMARGPERTREAL (java.math.BigDecimal XX_LISCKGROSSMARGPERTREAL)
    {
        set_Value ("XX_LISCKGROSSMARGPERTREAL", XX_LISCKGROSSMARGPERTREAL);
        
    }
    
    /** Get XX_LISCKGROSSMARGPERTREAL.
    @return XX_LISCKGROSSMARGPERTREAL */
    public java.math.BigDecimal getXX_LISCKGROSSMARGPERTREAL() 
    {
        return get_ValueAsBigDecimal("XX_LISCKGROSSMARGPERTREAL");
        
    }
    
    /** Set Margin (%) according Budgeted Purchase.
    @param XX_MARGACCORDINGBUDPURCH Margen(%) Segun Compra Presupuestado */
    public void setXX_MARGACCORDINGBUDPURCH (java.math.BigDecimal XX_MARGACCORDINGBUDPURCH)
    {
        set_Value ("XX_MARGACCORDINGBUDPURCH", XX_MARGACCORDINGBUDPURCH);
        
    }
    
    /** Get Margin (%) according Budgeted Purchase.
    @return Margen(%) Segun Compra Presupuestado */
    public java.math.BigDecimal getXX_MARGACCORDINGBUDPURCH() 
    {
        return get_ValueAsBigDecimal("XX_MARGACCORDINGBUDPURCH");
        
    }
    
    /** Set XX_MARGACCORDINGBUYREAL.
    @param XX_MARGACCORDINGBUYREAL XX_MARGACCORDINGBUYREAL */
    public void setXX_MARGACCORDINGBUYREAL (java.math.BigDecimal XX_MARGACCORDINGBUYREAL)
    {
        set_Value ("XX_MARGACCORDINGBUYREAL", XX_MARGACCORDINGBUYREAL);
        
    }
    
    /** Get XX_MARGACCORDINGBUYREAL.
    @return XX_MARGACCORDINGBUYREAL */
    public java.math.BigDecimal getXX_MARGACCORDINGBUYREAL() 
    {
        return get_ValueAsBigDecimal("XX_MARGACCORDINGBUYREAL");
        
    }
    
    /** Set XX_NACPURCHAMOUNTRECEIVED.
    @param XX_NACPURCHAMOUNTRECEIVED XX_NACPURCHAMOUNTRECEIVED */
    public void setXX_NACPURCHAMOUNTRECEIVED (java.math.BigDecimal XX_NACPURCHAMOUNTRECEIVED)
    {
        set_Value ("XX_NACPURCHAMOUNTRECEIVED", XX_NACPURCHAMOUNTRECEIVED);
        
    }
    
    /** Get XX_NACPURCHAMOUNTRECEIVED.
    @return XX_NACPURCHAMOUNTRECEIVED */
    public java.math.BigDecimal getXX_NACPURCHAMOUNTRECEIVED() 
    {
        return get_ValueAsBigDecimal("XX_NACPURCHAMOUNTRECEIVED");
        
    }
    
    /** Set Net Margin Percentage Cattle Budgeted.
    @param XX_NETMARGPERTCATTLEBUD Porcentaje de Margen Neto Ganado Presupuestado */
    public void setXX_NETMARGPERTCATTLEBUD (java.math.BigDecimal XX_NETMARGPERTCATTLEBUD)
    {
        set_Value ("XX_NETMARGPERTCATTLEBUD", XX_NETMARGPERTCATTLEBUD);
        
    }
    
    /** Get Net Margin Percentage Cattle Budgeted.
    @return Porcentaje de Margen Neto Ganado Presupuestado */
    public java.math.BigDecimal getXX_NETMARGPERTCATTLEBUD() 
    {
        return get_ValueAsBigDecimal("XX_NETMARGPERTCATTLEBUD");
        
    }
    
    /** Set XX_NETMARGPERTROYALLIVESTOCK.
    @param XX_NETMARGPERTROYALLIVESTOCK XX_NETMARGPERTROYALLIVESTOCK */
    public void setXX_NETMARGPERTROYALLIVESTOCK (java.math.BigDecimal XX_NETMARGPERTROYALLIVESTOCK)
    {
        set_Value ("XX_NETMARGPERTROYALLIVESTOCK", XX_NETMARGPERTROYALLIVESTOCK);
        
    }
    
    /** Get XX_NETMARGPERTROYALLIVESTOCK.
    @return XX_NETMARGPERTROYALLIVESTOCK */
    public java.math.BigDecimal getXX_NETMARGPERTROYALLIVESTOCK() 
    {
        return get_ValueAsBigDecimal("XX_NETMARGPERTROYALLIVESTOCK");
        
    }
    
    /** Set XX_NUMBTRANSFREV.
    @param XX_NUMBTRANSFREV XX_NUMBTRANSFREV */
    public void setXX_NUMBTRANSFREV (java.math.BigDecimal XX_NUMBTRANSFREV)
    {
        set_Value ("XX_NUMBTRANSFREV", XX_NUMBTRANSFREV);
        
    }
    
    /** Get XX_NUMBTRANSFREV.
    @return XX_NUMBTRANSFREV */
    public java.math.BigDecimal getXX_NUMBTRANSFREV() 
    {
        return get_ValueAsBigDecimal("XX_NUMBTRANSFREV");
        
    }
    
    /** Set XX_NUMINIINVEREAL.
    @param XX_NUMINIINVEREAL XX_NUMINIINVEREAL */
    public void setXX_NUMINIINVEREAL (java.math.BigDecimal XX_NUMINIINVEREAL)
    {
        set_Value ("XX_NUMINIINVEREAL", XX_NUMINIINVEREAL);
        
    }
    
    /** Get XX_NUMINIINVEREAL.
    @return XX_NUMINIINVEREAL */
    public java.math.BigDecimal getXX_NUMINIINVEREAL() 
    {
        return get_ValueAsBigDecimal("XX_NUMINIINVEREAL");
        
    }
    
    /** Set XX_NUMMONTHSREDSHOP.
    @param XX_NUMMONTHSREDSHOP XX_NUMMONTHSREDSHOP */
    public void setXX_NUMMONTHSREDSHOP (java.math.BigDecimal XX_NUMMONTHSREDSHOP)
    {
        set_Value ("XX_NUMMONTHSREDSHOP", XX_NUMMONTHSREDSHOP);
        
    }
    
    /** Get XX_NUMMONTHSREDSHOP.
    @return XX_NUMMONTHSREDSHOP */
    public java.math.BigDecimal getXX_NUMMONTHSREDSHOP() 
    {
        return get_ValueAsBigDecimal("XX_NUMMONTHSREDSHOP");
        
    }
    
    /** Set XX_NUMNACSHOPPINGPLACED.
    @param XX_NUMNACSHOPPINGPLACED XX_NUMNACSHOPPINGPLACED */
    public void setXX_NUMNACSHOPPINGPLACED (java.math.BigDecimal XX_NUMNACSHOPPINGPLACED)
    {
        set_Value ("XX_NUMNACSHOPPINGPLACED", XX_NUMNACSHOPPINGPLACED);
        
    }
    
    /** Get XX_NUMNACSHOPPINGPLACED.
    @return XX_NUMNACSHOPPINGPLACED */
    public java.math.BigDecimal getXX_NUMNACSHOPPINGPLACED() 
    {
        return get_ValueAsBigDecimal("XX_NUMNACSHOPPINGPLACED");
        
    }
    
    /** Set XX_NUMPROJDFINALINV.
    @param XX_NUMPROJDFINALINV XX_NUMPROJDFINALINV */
    public void setXX_NUMPROJDFINALINV (java.math.BigDecimal XX_NUMPROJDFINALINV)
    {
        set_Value ("XX_NUMPROJDFINALINV", XX_NUMPROJDFINALINV);
        
    }
    
    /** Get XX_NUMPROJDFINALINV.
    @return XX_NUMPROJDFINALINV */
    public java.math.BigDecimal getXX_NUMPROJDFINALINV() 
    {
        return get_ValueAsBigDecimal("XX_NUMPROJDFINALINV");
        
    }
    
    /** Set XX_NUMREALFINALINV.
    @param XX_NUMREALFINALINV XX_NUMREALFINALINV */
    public void setXX_NUMREALFINALINV (java.math.BigDecimal XX_NUMREALFINALINV)
    {
        set_Value ("XX_NUMREALFINALINV", XX_NUMREALFINALINV);
        
    }
    
    /** Get XX_NUMREALFINALINV.
    @return XX_NUMREALFINALINV */
    public java.math.BigDecimal getXX_NUMREALFINALINV() 
    {
        return get_ValueAsBigDecimal("XX_NUMREALFINALINV");
        
    }
    
    /** Set XX_NUMTRANSFSENT.
    @param XX_NUMTRANSFSENT XX_NUMTRANSFSENT */
    public void setXX_NUMTRANSFSENT (java.math.BigDecimal XX_NUMTRANSFSENT)
    {
        set_Value ("XX_NUMTRANSFSENT", XX_NUMTRANSFSENT);
        
    }
    
    /** Get XX_NUMTRANSFSENT.
    @return XX_NUMTRANSFSENT */
    public java.math.BigDecimal getXX_NUMTRANSFSENT() 
    {
        return get_ValueAsBigDecimal("XX_NUMTRANSFSENT");
        
    }
    
    /** Set Percentage of Sale Promotional Budgeted.
    @param XX_PECTSALEPROMBUD Porcentaje de Rebajas Promocional Presupuestadas */
    public void setXX_PECTSALEPROMBUD (java.math.BigDecimal XX_PECTSALEPROMBUD)
    {
        set_Value ("XX_PECTSALEPROMBUD", XX_PECTSALEPROMBUD);
        
    }
    
    /** Get Percentage of Sale Promotional Budgeted.
    @return Porcentaje de Rebajas Promocional Presupuestadas */
    public java.math.BigDecimal getXX_PECTSALEPROMBUD() 
    {
        return get_ValueAsBigDecimal("XX_PECTSALEPROMBUD");
        
    }
    
    /** Set XX_PECTSALEPROMINTERESTS.
    @param XX_PECTSALEPROMINTERESTS XX_PECTSALEPROMINTERESTS */
    public void setXX_PECTSALEPROMINTERESTS (java.math.BigDecimal XX_PECTSALEPROMINTERESTS)
    {
        set_Value ("XX_PECTSALEPROMINTERESTS", XX_PECTSALEPROMINTERESTS);
        
    }
    
    /** Get XX_PECTSALEPROMINTERESTS.
    @return XX_PECTSALEPROMINTERESTS */
    public java.math.BigDecimal getXX_PECTSALEPROMINTERESTS() 
    {
        return get_ValueAsBigDecimal("XX_PECTSALEPROMINTERESTS");
        
    }
    
    /** Set XX_PERCENTACTFINALSALE.
    @param XX_PERCENTACTFINALSALE XX_PERCENTACTFINALSALE */
    public void setXX_PERCENTACTFINALSALE (java.math.BigDecimal XX_PERCENTACTFINALSALE)
    {
        set_Value ("XX_PERCENTACTFINALSALE", XX_PERCENTACTFINALSALE);
        
    }
    
    /** Get XX_PERCENTACTFINALSALE.
    @return XX_PERCENTACTFINALSALE */
    public java.math.BigDecimal getXX_PERCENTACTFINALSALE() 
    {
        return get_ValueAsBigDecimal("XX_PERCENTACTFINALSALE");
        
    }
    
    /** Set XX_PERCENTSQALEFINALBUD.
    @param XX_PERCENTSQALEFINALBUD XX_PERCENTSQALEFINALBUD */
    public void setXX_PERCENTSQALEFINALBUD (java.math.BigDecimal XX_PERCENTSQALEFINALBUD)
    {
        set_Value ("XX_PERCENTSQALEFINALBUD", XX_PERCENTSQALEFINALBUD);
        
    }
    
    /** Get XX_PERCENTSQALEFINALBUD.
    @return XX_PERCENTSQALEFINALBUD */
    public java.math.BigDecimal getXX_PERCENTSQALEFINALBUD() 
    {
        return get_ValueAsBigDecimal("XX_PERCENTSQALEFINALBUD");
        
    }
    
    /** Set Percentage of budgeted Coverage.
    @param XX_PERCNBUDCOVERAGE Porcentaje de Cobertura Presupuestada */
    public void setXX_PERCNBUDCOVERAGE (java.math.BigDecimal XX_PERCNBUDCOVERAGE)
    {
        set_Value ("XX_PERCNBUDCOVERAGE", XX_PERCNBUDCOVERAGE);
        
    }
    
    /** Get Percentage of budgeted Coverage.
    @return Porcentaje de Cobertura Presupuestada */
    public java.math.BigDecimal getXX_PERCNBUDCOVERAGE() 
    {
        return get_ValueAsBigDecimal("XX_PERCNBUDCOVERAGE");
        
    }
    
    /** Set XX_PERTSALEFRINTERESTS.
    @param XX_PERTSALEFRINTERESTS XX_PERTSALEFRINTERESTS */
    public void setXX_PERTSALEFRINTERESTS (java.math.BigDecimal XX_PERTSALEFRINTERESTS)
    {
        set_Value ("XX_PERTSALEFRINTERESTS", XX_PERTSALEFRINTERESTS);
        
    }
    
    /** Get XX_PERTSALEFRINTERESTS.
    @return XX_PERTSALEFRINTERESTS */
    public java.math.BigDecimal getXX_PERTSALEFRINTERESTS() 
    {
        return get_ValueAsBigDecimal("XX_PERTSALEFRINTERESTS");
        
    }
    
    /** Set XX_PERTWINGMARGREAL.
    @param XX_PERTWINGMARGREAL XX_PERTWINGMARGREAL */
    public void setXX_PERTWINGMARGREAL (java.math.BigDecimal XX_PERTWINGMARGREAL)
    {
        set_Value ("XX_PERTWINGMARGREAL", XX_PERTWINGMARGREAL);
        
    }
    
    /** Get XX_PERTWINGMARGREAL.
    @return XX_PERTWINGMARGREAL */
    public java.math.BigDecimal getXX_PERTWINGMARGREAL() 
    {
        return get_ValueAsBigDecimal("XX_PERTWINGMARGREAL");
        
    }
    
    /** Set Placed Order PVP Adjustment.
    @param XX_PlacedOrderPVPAdjustment Placed Order PVP Adjustment */
    public void setXX_PlacedOrderPVPAdjustment (java.math.BigDecimal XX_PlacedOrderPVPAdjustment)
    {
        set_Value ("XX_PlacedOrderPVPAdjustment", XX_PlacedOrderPVPAdjustment);
        
    }
    
    /** Get Placed Order PVP Adjustment.
    @return Placed Order PVP Adjustment */
    public java.math.BigDecimal getXX_PlacedOrderPVPAdjustment() 
    {
        return get_ValueAsBigDecimal("XX_PlacedOrderPVPAdjustment");
        
    }
    
    /** Set XX_PORTSALEFRBUD.
    @param XX_PORTSALEFRBUD XX_PORTSALEFRBUD */
    public void setXX_PORTSALEFRBUD (java.math.BigDecimal XX_PORTSALEFRBUD)
    {
        set_Value ("XX_PORTSALEFRBUD", XX_PORTSALEFRBUD);
        
    }
    
    /** Get XX_PORTSALEFRBUD.
    @return XX_PORTSALEFRBUD */
    public java.math.BigDecimal getXX_PORTSALEFRBUD() 
    {
        return get_ValueAsBigDecimal("XX_PORTSALEFRBUD");
        
    }
    
    /** Set Promotional Sale Amount Budgeted.
    @param XX_PROMSALEAMOUNTBUD Monto de Rebajas Promocional Presupuestadas */
    public void setXX_PROMSALEAMOUNTBUD (java.math.BigDecimal XX_PROMSALEAMOUNTBUD)
    {
        set_Value ("XX_PROMSALEAMOUNTBUD", XX_PROMSALEAMOUNTBUD);
        
    }
    
    /** Get Promotional Sale Amount Budgeted.
    @return Monto de Rebajas Promocional Presupuestadas */
    public java.math.BigDecimal getXX_PROMSALEAMOUNTBUD() 
    {
        return get_ValueAsBigDecimal("XX_PROMSALEAMOUNTBUD");
        
    }
    
    /** Set Promotional Sale Number of Budgeted.
    @param XX_PROMSALENUMBUD Cantidad de Rebajas Promocional Presupuestadas */
    public void setXX_PROMSALENUMBUD (java.math.BigDecimal XX_PROMSALENUMBUD)
    {
        set_Value ("XX_PROMSALENUMBUD", XX_PROMSALENUMBUD);
        
    }
    
    /** Get Promotional Sale Number of Budgeted.
    @return Cantidad de Rebajas Promocional Presupuestadas */
    public java.math.BigDecimal getXX_PROMSALENUMBUD() 
    {
        return get_ValueAsBigDecimal("XX_PROMSALENUMBUD");
        
    }
    
    /** Set Purchase Amount Budgeted.
    @param XX_PURCHAMOUNTBUDGETED Monto de Compras Presupuestadas */
    public void setXX_PURCHAMOUNTBUDGETED (java.math.BigDecimal XX_PURCHAMOUNTBUDGETED)
    {
        set_Value ("XX_PURCHAMOUNTBUDGETED", XX_PURCHAMOUNTBUDGETED);
        
    }
    
    /** Get Purchase Amount Budgeted.
    @return Monto de Compras Presupuestadas */
    public java.math.BigDecimal getXX_PURCHAMOUNTBUDGETED() 
    {
        return get_ValueAsBigDecimal("XX_PURCHAMOUNTBUDGETED");
        
    }
    
    /** Set XX_PURCHAMOUNTPLACEDCOSTIMP.
    @param XX_PURCHAMOUNTPLACEDCOSTIMP XX_PURCHAMOUNTPLACEDCOSTIMP */
    public void setXX_PURCHAMOUNTPLACEDCOSTIMP (java.math.BigDecimal XX_PURCHAMOUNTPLACEDCOSTIMP)
    {
        set_Value ("XX_PURCHAMOUNTPLACEDCOSTIMP", XX_PURCHAMOUNTPLACEDCOSTIMP);
        
    }
    
    /** Get XX_PURCHAMOUNTPLACEDCOSTIMP.
    @return XX_PURCHAMOUNTPLACEDCOSTIMP */
    public java.math.BigDecimal getXX_PURCHAMOUNTPLACEDCOSTIMP() 
    {
        return get_ValueAsBigDecimal("XX_PURCHAMOUNTPLACEDCOSTIMP");
        
    }
    
    /** Set XX_PURCHAMOUNTPLACEDIMPD.
    @param XX_PURCHAMOUNTPLACEDIMPD XX_PURCHAMOUNTPLACEDIMPD */
    public void setXX_PURCHAMOUNTPLACEDIMPD (java.math.BigDecimal XX_PURCHAMOUNTPLACEDIMPD)
    {
        set_Value ("XX_PURCHAMOUNTPLACEDIMPD", XX_PURCHAMOUNTPLACEDIMPD);
        
    }
    
    /** Get XX_PURCHAMOUNTPLACEDIMPD.
    @return XX_PURCHAMOUNTPLACEDIMPD */
    public java.math.BigDecimal getXX_PURCHAMOUNTPLACEDIMPD() 
    {
        return get_ValueAsBigDecimal("XX_PURCHAMOUNTPLACEDIMPD");
        
    }
    
    /** Set XX_PURCHAMOUNTPLADPASTMONTHS.
    @param XX_PURCHAMOUNTPLADPASTMONTHS XX_PURCHAMOUNTPLADPASTMONTHS */
    public void setXX_PURCHAMOUNTPLADPASTMONTHS (java.math.BigDecimal XX_PURCHAMOUNTPLADPASTMONTHS)
    {
        set_Value ("XX_PURCHAMOUNTPLADPASTMONTHS", XX_PURCHAMOUNTPLADPASTMONTHS);
        
    }
    
    /** Get XX_PURCHAMOUNTPLADPASTMONTHS.
    @return XX_PURCHAMOUNTPLADPASTMONTHS */
    public java.math.BigDecimal getXX_PURCHAMOUNTPLADPASTMONTHS() 
    {
        return get_ValueAsBigDecimal("XX_PURCHAMOUNTPLADPASTMONTHS");
        
    }
    
    /** Set XX_PURCHAMOUNTREDPASTMONTHS.
    @param XX_PURCHAMOUNTREDPASTMONTHS XX_PURCHAMOUNTREDPASTMONTHS */
    public void setXX_PURCHAMOUNTREDPASTMONTHS (java.math.BigDecimal XX_PURCHAMOUNTREDPASTMONTHS)
    {
        set_Value ("XX_PURCHAMOUNTREDPASTMONTHS", XX_PURCHAMOUNTREDPASTMONTHS);
        
    }
    
    /** Get XX_PURCHAMOUNTREDPASTMONTHS.
    @return XX_PURCHAMOUNTREDPASTMONTHS */
    public java.math.BigDecimal getXX_PURCHAMOUNTREDPASTMONTHS() 
    {
        return get_ValueAsBigDecimal("XX_PURCHAMOUNTREDPASTMONTHS");
        
    }
    
    /** Set XX_PURCHAMOUNTREVIMPD.
    @param XX_PURCHAMOUNTREVIMPD XX_PURCHAMOUNTREVIMPD */
    public void setXX_PURCHAMOUNTREVIMPD (java.math.BigDecimal XX_PURCHAMOUNTREVIMPD)
    {
        set_Value ("XX_PURCHAMOUNTREVIMPD", XX_PURCHAMOUNTREVIMPD);
        
    }
    
    /** Get XX_PURCHAMOUNTREVIMPD.
    @return XX_PURCHAMOUNTREVIMPD */
    public java.math.BigDecimal getXX_PURCHAMOUNTREVIMPD() 
    {
        return get_ValueAsBigDecimal("XX_PURCHAMOUNTREVIMPD");
        
    }
    
    /** Set XX_PURCHQUANTIMPDPLACED.
    @param XX_PURCHQUANTIMPDPLACED XX_PURCHQUANTIMPDPLACED */
    public void setXX_PURCHQUANTIMPDPLACED (java.math.BigDecimal XX_PURCHQUANTIMPDPLACED)
    {
        set_Value ("XX_PURCHQUANTIMPDPLACED", XX_PURCHQUANTIMPDPLACED);
        
    }
    
    /** Get XX_PURCHQUANTIMPDPLACED.
    @return XX_PURCHQUANTIMPDPLACED */
    public java.math.BigDecimal getXX_PURCHQUANTIMPDPLACED() 
    {
        return get_ValueAsBigDecimal("XX_PURCHQUANTIMPDPLACED");
        
    }
    
    /** Set XX_PURCHQUANTPLACEDMONTHS.
    @param XX_PURCHQUANTPLACEDMONTHS XX_PURCHQUANTPLACEDMONTHS */
    public void setXX_PURCHQUANTPLACEDMONTHS (java.math.BigDecimal XX_PURCHQUANTPLACEDMONTHS)
    {
        set_Value ("XX_PURCHQUANTPLACEDMONTHS", XX_PURCHQUANTPLACEDMONTHS);
        
    }
    
    /** Get XX_PURCHQUANTPLACEDMONTHS.
    @return XX_PURCHQUANTPLACEDMONTHS */
    public java.math.BigDecimal getXX_PURCHQUANTPLACEDMONTHS() 
    {
        return get_ValueAsBigDecimal("XX_PURCHQUANTPLACEDMONTHS");
        
    }
    
    /** Set XX_QUANTACTUALSALE.
    @param XX_QUANTACTUALSALE XX_QUANTACTUALSALE */
    public void setXX_QUANTACTUALSALE (java.math.BigDecimal XX_QUANTACTUALSALE)
    {
        set_Value ("XX_QUANTACTUALSALE", XX_QUANTACTUALSALE);
        
    }
    
    /** Get XX_QUANTACTUALSALE.
    @return XX_QUANTACTUALSALE */
    public java.math.BigDecimal getXX_QUANTACTUALSALE() 
    {
        return get_ValueAsBigDecimal("XX_QUANTACTUALSALE");
        
    }
    
    /** Set Quantity Budgeted Shopping.
    @param XX_QUANTBUDGETEDSHOPPING Cantidad de Compras Presupuestadas */
    public void setXX_QUANTBUDGETEDSHOPPING (java.math.BigDecimal XX_QUANTBUDGETEDSHOPPING)
    {
        set_Value ("XX_QUANTBUDGETEDSHOPPING", XX_QUANTBUDGETEDSHOPPING);
        
    }
    
    /** Get Quantity Budgeted Shopping.
    @return Cantidad de Compras Presupuestadas */
    public java.math.BigDecimal getXX_QUANTBUDGETEDSHOPPING() 
    {
        return get_ValueAsBigDecimal("XX_QUANTBUDGETEDSHOPPING");
        
    }
    
    /** Set XX_QUANTITYPURCHLIMIT.
    @param XX_QUANTITYPURCHLIMIT XX_QUANTITYPURCHLIMIT */
    public void setXX_QUANTITYPURCHLIMIT (java.math.BigDecimal XX_QUANTITYPURCHLIMIT)
    {
        set_Value ("XX_QUANTITYPURCHLIMIT", XX_QUANTITYPURCHLIMIT);
        
    }
    
    /** Get XX_QUANTITYPURCHLIMIT.
    @return XX_QUANTITYPURCHLIMIT */
    public java.math.BigDecimal getXX_QUANTITYPURCHLIMIT() 
    {
        return get_ValueAsBigDecimal("XX_QUANTITYPURCHLIMIT");
        
    }
    
    /** Set XX_QUANTPURCHAMOUNTSREV.
    @param XX_QUANTPURCHAMOUNTSREV XX_QUANTPURCHAMOUNTSREV */
    public void setXX_QUANTPURCHAMOUNTSREV (java.math.BigDecimal XX_QUANTPURCHAMOUNTSREV)
    {
        set_Value ("XX_QUANTPURCHAMOUNTSREV", XX_QUANTPURCHAMOUNTSREV);
        
    }
    
    /** Get XX_QUANTPURCHAMOUNTSREV.
    @return XX_QUANTPURCHAMOUNTSREV */
    public java.math.BigDecimal getXX_QUANTPURCHAMOUNTSREV() 
    {
        return get_ValueAsBigDecimal("XX_QUANTPURCHAMOUNTSREV");
        
    }
    
    /** Set XX_QUANTPURCHNAC.
    @param XX_QUANTPURCHNAC XX_QUANTPURCHNAC */
    public void setXX_QUANTPURCHNAC (java.math.BigDecimal XX_QUANTPURCHNAC)
    {
        set_Value ("XX_QUANTPURCHNAC", XX_QUANTPURCHNAC);
        
    }
    
    /** Get XX_QUANTPURCHNAC.
    @return XX_QUANTPURCHNAC */
    public java.math.BigDecimal getXX_QUANTPURCHNAC() 
    {
        return get_ValueAsBigDecimal("XX_QUANTPURCHNAC");
        
    }
    
    /** Set XX_REALDECLINE.
    @param XX_REALDECLINE XX_REALDECLINE */
    public void setXX_REALDECLINE (java.math.BigDecimal XX_REALDECLINE)
    {
        set_Value ("XX_REALDECLINE", XX_REALDECLINE);
        
    }
    
    /** Get XX_REALDECLINE.
    @return XX_REALDECLINE */
    public java.math.BigDecimal getXX_REALDECLINE() 
    {
        return get_ValueAsBigDecimal("XX_REALDECLINE");
        
    }
    
    /** Set XX_REALPERCCOVERAGE.
    @param XX_REALPERCCOVERAGE XX_REALPERCCOVERAGE */
    public void setXX_REALPERCCOVERAGE (java.math.BigDecimal XX_REALPERCCOVERAGE)
    {
        set_Value ("XX_REALPERCCOVERAGE", XX_REALPERCCOVERAGE);
        
    }
    
    /** Get XX_REALPERCCOVERAGE.
    @return XX_REALPERCCOVERAGE */
    public java.math.BigDecimal getXX_REALPERCCOVERAGE() 
    {
        return get_ValueAsBigDecimal("XX_REALPERCCOVERAGE");
        
    }
    
    /** Set Receipts PVP.
    @param XX_ReceiptPVP Receipts PVP */
    public void setXX_ReceiptPVP (java.math.BigDecimal XX_ReceiptPVP)
    {
        set_Value ("XX_ReceiptPVP", XX_ReceiptPVP);
        
    }
    
    /** Get Receipts PVP.
    @return Receipts PVP */
    public java.math.BigDecimal getXX_ReceiptPVP() 
    {
        return get_ValueAsBigDecimal("XX_ReceiptPVP");
        
    }
    
    /** Set Receipt Qty.
    @param XX_ReceiptQty Receipt Qty */
    public void setXX_ReceiptQty (int XX_ReceiptQty)
    {
        set_Value ("XX_ReceiptQty", Integer.valueOf(XX_ReceiptQty));
        
    }
    
    /** Get Receipt Qty.
    @return Receipt Qty */
    public int getXX_ReceiptQty() 
    {
        return get_ValueAsInt("XX_ReceiptQty");
        
    }
    
    /** Set Returns PVP.
    @param XX_ReturnsPVP Returns PVP */
    public void setXX_ReturnsPVP (java.math.BigDecimal XX_ReturnsPVP)
    {
        set_Value ("XX_ReturnsPVP", XX_ReturnsPVP);
        
    }
    
    /** Get Returns PVP.
    @return Returns PVP */
    public java.math.BigDecimal getXX_ReturnsPVP() 
    {
        return get_ValueAsBigDecimal("XX_ReturnsPVP");
        
    }
    
    /** Set Returns Qty.
    @param XX_ReturnsQty Returns Qty */
    public void setXX_ReturnsQty (int XX_ReturnsQty)
    {
        set_Value ("XX_ReturnsQty", Integer.valueOf(XX_ReturnsQty));
        
    }
    
    /** Get Returns Qty.
    @return Returns Qty */
    public int getXX_ReturnsQty() 
    {
        return get_ValueAsInt("XX_ReturnsQty");
        
    }
    
    /** Set Rotation (%) Budgeted.
    @param XX_ROTATIONBUD Rotacion(%) Presupuestado */
    public void setXX_ROTATIONBUD (java.math.BigDecimal XX_ROTATIONBUD)
    {
        set_Value ("XX_ROTATIONBUD", XX_ROTATIONBUD);
        
    }
    
    /** Get Rotation (%) Budgeted.
    @return Rotacion(%) Presupuestado */
    public java.math.BigDecimal getXX_ROTATIONBUD() 
    {
        return get_ValueAsBigDecimal("XX_ROTATIONBUD");
        
    }
    
    /** Set XX_ROTATIONREAL.
    @param XX_ROTATIONREAL XX_ROTATIONREAL */
    public void setXX_ROTATIONREAL (java.math.BigDecimal XX_ROTATIONREAL)
    {
        set_Value ("XX_ROTATIONREAL", XX_ROTATIONREAL);
        
    }
    
    /** Get XX_ROTATIONREAL.
    @return XX_ROTATIONREAL */
    public java.math.BigDecimal getXX_ROTATIONREAL() 
    {
        return get_ValueAsBigDecimal("XX_ROTATIONREAL");
        
    }
    
    /** Set Sales Quantity Budgeted.
    @param XX_SALESAMOUNTBUD Cantidad de Ventas Presupuestadas */
    public void setXX_SALESAMOUNTBUD (java.math.BigDecimal XX_SALESAMOUNTBUD)
    {
        set_Value ("XX_SALESAMOUNTBUD", XX_SALESAMOUNTBUD);
        
    }
    
    /** Get Sales Quantity Budgeted.
    @return Cantidad de Ventas Presupuestadas */
    public java.math.BigDecimal getXX_SALESAMOUNTBUD() 
    {
        return get_ValueAsBigDecimal("XX_SALESAMOUNTBUD");
        
    }
    
    /** Set Sales Amount Budgeted.
    @param XX_SALESAMOUNTBUD2 Monto de Ventas Presupuestadas */
    public void setXX_SALESAMOUNTBUD2 (java.math.BigDecimal XX_SALESAMOUNTBUD2)
    {
        set_Value ("XX_SALESAMOUNTBUD2", XX_SALESAMOUNTBUD2);
        
    }
    
    /** Get Sales Amount Budgeted.
    @return Monto de Ventas Presupuestadas */
    public java.math.BigDecimal getXX_SALESAMOUNTBUD2() 
    {
        return get_ValueAsBigDecimal("XX_SALESAMOUNTBUD2");
        
    }
    
    /** Set XX_TRANSFAMOUNTRECEIVED.
    @param XX_TRANSFAMOUNTRECEIVED XX_TRANSFAMOUNTRECEIVED */
    public void setXX_TRANSFAMOUNTRECEIVED (java.math.BigDecimal XX_TRANSFAMOUNTRECEIVED)
    {
        set_Value ("XX_TRANSFAMOUNTRECEIVED", XX_TRANSFAMOUNTRECEIVED);
        
    }
    
    /** Get XX_TRANSFAMOUNTRECEIVED.
    @return XX_TRANSFAMOUNTRECEIVED */
    public java.math.BigDecimal getXX_TRANSFAMOUNTRECEIVED() 
    {
        return get_ValueAsBigDecimal("XX_TRANSFAMOUNTRECEIVED");
        
    }
    
    /** Set XX_TRANSFAMOUNTSENT.
    @param XX_TRANSFAMOUNTSENT XX_TRANSFAMOUNTSENT */
    public void setXX_TRANSFAMOUNTSENT (java.math.BigDecimal XX_TRANSFAMOUNTSENT)
    {
        set_Value ("XX_TRANSFAMOUNTSENT", XX_TRANSFAMOUNTSENT);
        
    }
    
    /** Get XX_TRANSFAMOUNTSENT.
    @return XX_TRANSFAMOUNTSENT */
    public java.math.BigDecimal getXX_TRANSFAMOUNTSENT() 
    {
        return get_ValueAsBigDecimal("XX_TRANSFAMOUNTSENT");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
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
        if (XX_VMR_PRLD01_ID < 1) throw new IllegalArgumentException ("XX_VMR_PRLD01_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PRLD01_ID", Integer.valueOf(XX_VMR_PRLD01_ID));
        
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
