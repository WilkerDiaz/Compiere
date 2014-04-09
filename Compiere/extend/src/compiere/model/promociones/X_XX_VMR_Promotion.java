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
package compiere.model.promociones;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_Promotion
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Promotion extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Promotion_ID id
    @param trx transaction
    */
    public X_XX_VMR_Promotion (Ctx ctx, int XX_VMR_Promotion_ID, Trx trx)
    {
        super (ctx, XX_VMR_Promotion_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Promotion_ID == 0)
        {
            setXX_VMR_Promotion_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Promotion (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27622437994789L;
    /** Last Updated Timestamp 2012-06-21 16:34:38.0 */
    public static final long updatedMS = 1340312678000L;
    /** AD_Table_ID=1000406 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Promotion");
        
    }
    ;
    
    /** TableName=XX_VMR_Promotion */
    public static final String Table_Name="XX_VMR_Promotion";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Finish Date.
    @param DateFinish Indicates the (planned) completion date */
    public void setDateFinish (Timestamp DateFinish)
    {
        set_Value ("DateFinish", DateFinish);
        
    }
    
    /** Get Finish Date.
    @return Indicates the (planned) completion date */
    public Timestamp getDateFinish() 
    {
        return (Timestamp)get_Value("DateFinish");
        
    }
    
    /** Set Date From.
    @param DateFrom Starting date for a range */
    public void setDateFrom (Timestamp DateFrom)
    {
        set_Value ("DateFrom", DateFrom);
        
    }
    
    /** Get Date From.
    @return Starting date for a range */
    public Timestamp getDateFrom() 
    {
        return (Timestamp)get_Value("DateFrom");
        
    }
    
    /** Set Descripción.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Descripción.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Info EMail.
    @param EMail_Info_To EMail address to send informational messages and copies */
    public void setEMail_Info_To (String EMail_Info_To)
    {
        set_Value ("EMail_Info_To", EMail_Info_To);
        
    }
    
    /** Get Info EMail.
    @return EMail address to send informational messages and copies */
    public String getEMail_Info_To() 
    {
        return (String)get_Value("EMail_Info_To");
        
    }
    
    /** Set Nombre.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Nombre.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Set Priority.
    @param Priority Indicates if this request is of a high, medium or low priority. */
    public void setPriority (int Priority)
    {
        set_Value ("Priority", Integer.valueOf(Priority));
        
    }
    
    /** Get Priority.
    @return Indicates if this request is of a high, medium or low priority. */
    public int getPriority() 
    {
        return get_ValueAsInt("Priority");
        
    }
    
    /** Set Slot End.
    @param TimeSlotEnd Time when timeslot ends */
    public void setTimeSlotEnd (Timestamp TimeSlotEnd)
    {
        set_Value ("TimeSlotEnd", TimeSlotEnd);
        
    }
    
    /** Get Slot End.
    @return Time when timeslot ends */
    public Timestamp getTimeSlotEnd() 
    {
        return (Timestamp)get_Value("TimeSlotEnd");
        
    }
    
    /** Set Slot Start.
    @param TimeSlotStart Time when timeslot starts */
    public void setTimeSlotStart (Timestamp TimeSlotStart)
    {
        set_Value ("TimeSlotStart", TimeSlotStart);
        
    }
    
    /** Get Slot Start.
    @return Time when timeslot starts */
    public Timestamp getTimeSlotStart() 
    {
        return (Timestamp)get_Value("TimeSlotStart");
        
    }
    
    /** Set Alert.
    @param XX_Alert Alert */
    public void setXX_Alert (boolean XX_Alert)
    {
        set_Value ("XX_Alert", Boolean.valueOf(XX_Alert));
        
    }
    
    /** Get Alert.
    @return Alert */
    public boolean isXX_Alert() 
    {
        return get_ValueAsBoolean("XX_Alert");
        
    }
    
    /** Set Approved Marketing.
    @param XX_ApproveMar Approved Marketing */
    public void setXX_ApproveMar (boolean XX_ApproveMar)
    {
        set_Value ("XX_ApproveMar", Boolean.valueOf(XX_ApproveMar));
        
    }
    
    /** Get Approved Marketing.
    @return Approved Marketing */
    public boolean isXX_ApproveMar() 
    {
        return get_ValueAsBoolean("XX_ApproveMar");
        
    }
    
    /** Set Approve Marketing.
    @param XX_ApproveMarketing Approve Marketing */
    public void setXX_ApproveMarketing (String XX_ApproveMarketing)
    {
        set_Value ("XX_ApproveMarketing", XX_ApproveMarketing);
        
    }
    
    /** Get Approve Marketing.
    @return Approve Marketing */
    public String getXX_ApproveMarketing() 
    {
        return (String)get_Value("XX_ApproveMarketing");
        
    }
    
    /** Set Approved Merchandising.
    @param XX_ApproveMer Approved by Merchandising Manager or Category Manager */
    public void setXX_ApproveMer (boolean XX_ApproveMer)
    {
        set_Value ("XX_ApproveMer", Boolean.valueOf(XX_ApproveMer));
        
    }
    
    /** Get Approved Merchandising.
    @return Approved by Merchandising Manager or Category Manager */
    public boolean isXX_ApproveMer() 
    {
        return get_ValueAsBoolean("XX_ApproveMer");
        
    }
    
    /** Set Approve Merchandising.
    @param XX_ApproveMerchandising Approve Merchandising */
    public void setXX_ApproveMerchandising (String XX_ApproveMerchandising)
    {
        set_Value ("XX_ApproveMerchandising", XX_ApproveMerchandising);
        
    }
    
    /** Get Approve Merchandising.
    @return Approve Merchandising */
    public String getXX_ApproveMerchandising() 
    {
        return (String)get_Value("XX_ApproveMerchandising");
        
    }
    
    /** Promociones de Temporadas Comerciales = 1000000 */
    public static final String XX_COMMERCIALOBJETIVE_PromocionesDeTemporadasComerciales = X_Ref_XX_OBJETICOCOMERCIAL.PROMOCIONES_DE_TEMPORADAS_COMERCIALES.getValue();
    /** Promociones Creadoras de Valor = 2000000 */
    public static final String XX_COMMERCIALOBJETIVE_PromocionesCreadorasDeValor = X_Ref_XX_OBJETICOCOMERCIAL.PROMOCIONES_CREADORAS_DE_VALOR.getValue();
    /** Promociones Orientadas a Impulsar Surtido = 3000000 */
    public static final String XX_COMMERCIALOBJETIVE_PromocionesOrientadasAImpulsarSurtido = X_Ref_XX_OBJETICOCOMERCIAL.PROMOCIONES_ORIENTADAS_A_IMPULSAR_SURTIDO.getValue();
    /** Set Commercial Objetive.
    @param XX_CommercialObjetive Commercial Objetive */
    public void setXX_CommercialObjetive (String XX_CommercialObjetive)
    {
        if (!X_Ref_XX_OBJETICOCOMERCIAL.isValid(XX_CommercialObjetive))
        throw new IllegalArgumentException ("XX_CommercialObjetive Invalid value - " + XX_CommercialObjetive + " - Reference_ID=1000012 - 1000000 - 2000000 - 3000000");
        set_Value ("XX_CommercialObjetive", XX_CommercialObjetive);
        
    }
    
    /** Get Commercial Objetive.
    @return Commercial Objetive */
    public String getXX_CommercialObjetive() 
    {
        return (String)get_Value("XX_CommercialObjetive");
        
    }
    
    /** Set Immediate Transfer.
    @param XX_ImmediateTransfer Immediate Transfer */
    public void setXX_ImmediateTransfer (String XX_ImmediateTransfer)
    {
        set_Value ("XX_ImmediateTransfer", XX_ImmediateTransfer);
        
    }
    
    /** Get Immediate Transfer.
    @return Immediate Transfer */
    public String getXX_ImmediateTransfer() 
    {
        return (String)get_Value("XX_ImmediateTransfer");
        
    }
    
    /** Set XX_LoadPromotionProducts.
    @param XX_LoadPromotionProducts XX_LoadPromotionProducts */
    public void setXX_LoadPromotionProducts (String XX_LoadPromotionProducts)
    {
        set_Value ("XX_LoadPromotionProducts", XX_LoadPromotionProducts);
        
    }
    
    /** Get XX_LoadPromotionProducts.
    @return XX_LoadPromotionProducts */
    public String getXX_LoadPromotionProducts() 
    {
        return (String)get_Value("XX_LoadPromotionProducts");
        
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
    
    /** Aumento del Ticket Promedio = 1000000 */
    public static final String XX_TACTICALOBJECTIVE_AumentoDelTicketPromedio = X_Ref_XX_OBJETIVOTACTICO.AUMENTO_DEL_TICKET_PROMEDIO.getValue();
    /** Aumento del número de Transacciones = 20000000 */
    public static final String XX_TACTICALOBJECTIVE_AumentoDelNúmeroDeTransacciones = X_Ref_XX_OBJETIVOTACTICO.AUMENTO_DEL_NÚMERO_DE_TRANSACCIONES.getValue();
    /** Aumento del Tráfico = 3000000 */
    public static final String XX_TACTICALOBJECTIVE_AumentoDelTráfico = X_Ref_XX_OBJETIVOTACTICO.AUMENTO_DEL_TRÁFICO.getValue();
    /** Set Tactical Objective.
    @param XX_TacticalObjective Tactical Objective */
    public void setXX_TacticalObjective (String XX_TacticalObjective)
    {
        if (!X_Ref_XX_OBJETIVOTACTICO.isValid(XX_TacticalObjective))
        throw new IllegalArgumentException ("XX_TacticalObjective Invalid value - " + XX_TacticalObjective + " - Reference_ID=1000011 - 1000000 - 20000000 - 3000000");
        set_Value ("XX_TacticalObjective", XX_TacticalObjective);
        
    }
    
    /** Get Tactical Objective.
    @return Tactical Objective */
    public String getXX_TacticalObjective() 
    {
        return (String)get_Value("XX_TacticalObjective");
        
    }
    
    /** A1- Ahorro en compra = 1000100 */
    public static final String XX_TYPEPROMOTION_A1_AhorroEnCompra = X_Ref_XX_TypePromotion.A1__AHORRO_EN_COMPRA.getValue();
    /** A2- Producto gratis o en descuento = 1000200 */
    public static final String XX_TYPEPROMOTION_A2_ProductoGratisOEnDescuento = X_Ref_XX_TypePromotion.A2__PRODUCTO_GRATIS_O_EN_DESCUENTO.getValue();
    /** A3- Descuento en productos publicados = 1000300 */
    public static final String XX_TYPEPROMOTION_A3_DescuentoEnProductosPublicados = X_Ref_XX_TypePromotion.A3__DESCUENTO_EN_PRODUCTOS_PUBLICADOS.getValue();
    /** B1- Descuento en el producto X + 1 = 1000400 */
    public static final String XX_TYPEPROMOTION_B1_DescuentoEnElProductoXPlus1 = X_Ref_XX_TypePromotion.B1__DESCUENTO_EN_EL_PRODUCTO_X_PLUS1.getValue();
    /** C2 - Coorporativas = 1000500 */
    public static final String XX_TYPEPROMOTION_C2_Coorporativas = X_Ref_XX_TypePromotion.C2__COORPORATIVAS.getValue();
    /** D1- Más se compra más se gana = 1000600 */
    public static final String XX_TYPEPROMOTION_D1_MásSeCompraMásSeGana = X_Ref_XX_TypePromotion.D1__MÁS_SE_COMPRA_MÁS_SE_GANA.getValue();
    /** D2- Transacción premiada = 1000700 */
    public static final String XX_TYPEPROMOTION_D2_TransacciónPremiada = X_Ref_XX_TypePromotion.D2__TRANSACCIÓN_PREMIADA.getValue();
    /** D3- Premio de Bono Regalo = 1000800 */
    public static final String XX_TYPEPROMOTION_D3_PremioDeBonoRegalo = X_Ref_XX_TypePromotion.D3__PREMIO_DE_BONO_REGALO.getValue();
    /** D4- Cupón de descuento = 1000900 */
    public static final String XX_TYPEPROMOTION_D4_CupónDeDescuento = X_Ref_XX_TypePromotion.D4__CUPÓN_DE_DESCUENTO.getValue();
    /** E1- Regalo por compra = 1001000 */
    public static final String XX_TYPEPROMOTION_E1_RegaloPorCompra = X_Ref_XX_TypePromotion.E1__REGALO_POR_COMPRA.getValue();
    /** E3 - Promociones Clásicas = 1001100 */
    public static final String XX_TYPEPROMOTION_E3_PromocionesClásicas = X_Ref_XX_TypePromotion.E3__PROMOCIONES_CLÁSICAS.getValue();
    /** F1- Premio Ilusión = 1001200 */
    public static final String XX_TYPEPROMOTION_F1_PremioIlusión = X_Ref_XX_TypePromotion.F1__PREMIO_ILUSIÓN.getValue();
    /** Set Type of Promotion.
    @param XX_TypePromotion Type of Promotion */
    public void setXX_TypePromotion (String XX_TypePromotion)
    {
        if (!X_Ref_XX_TypePromotion.isValid(XX_TypePromotion))
        throw new IllegalArgumentException ("XX_TypePromotion Invalid value - " + XX_TypePromotion + " - Reference_ID=1000013 - 1000100 - 1000200 - 1000300 - 1000400 - 1000500 - 1000600 - 1000700 - 1000800 - 1000900 - 1001000 - 1001100 - 1001200");
        set_Value ("XX_TypePromotion", XX_TypePromotion);
        
    }
    
    /** Get Type of Promotion.
    @return Type of Promotion */
    public String getXX_TypePromotion() 
    {
        return (String)get_Value("XX_TypePromotion");
        
    }
    
    /** Set Promotion ID.
    @param XX_VMR_Promotion_ID Promotion ID */
    public void setXX_VMR_Promotion_ID (int XX_VMR_Promotion_ID)
    {
        if (XX_VMR_Promotion_ID < 1) throw new IllegalArgumentException ("XX_VMR_Promotion_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Promotion_ID", Integer.valueOf(XX_VMR_Promotion_ID));
        
    }
    
    /** Get Promotion ID.
    @return Promotion ID */
    public int getXX_VMR_Promotion_ID() 
    {
        return get_ValueAsInt("XX_VMR_Promotion_ID");
        
    }
    
    
}
