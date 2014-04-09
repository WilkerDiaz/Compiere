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
/** Generated Model for XX_VMR_ConditionPromotion
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_ConditionPromotion extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_ConditionPromotion_ID id
    @param trx transaction
    */
    public X_XX_VMR_ConditionPromotion (Ctx ctx, int XX_VMR_ConditionPromotion_ID, Trx trx)
    {
        super (ctx, XX_VMR_ConditionPromotion_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_ConditionPromotion_ID == 0)
        {
            setXX_VMR_ConditionPromotion_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_ConditionPromotion (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27616454389789L;
    /** Last Updated Timestamp 2012-04-13 10:27:53.0 */
    public static final long updatedMS = 1334329073000L;
    /** AD_Table_ID=1001953 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_ConditionPromotion");
        
    }
    ;
    
    /** TableName=XX_VMR_ConditionPromotion */
    public static final String Table_Name="XX_VMR_ConditionPromotion";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Column.
    @param AD_Column_ID Column in the table */
    public void setAD_Column_ID (int AD_Column_ID)
    {
        if (AD_Column_ID <= 0) set_Value ("AD_Column_ID", null);
        else
        set_Value ("AD_Column_ID", Integer.valueOf(AD_Column_ID));
        
    }
    
    /** Get Column.
    @return Column in the table */
    public int getAD_Column_ID() 
    {
        return get_ValueAsInt("AD_Column_ID");
        
    }
    
    /** Set Table.
    @param AD_Table_ID Database Table information */
    public void setAD_Table_ID (int AD_Table_ID)
    {
        if (AD_Table_ID <= 0) set_Value ("AD_Table_ID", null);
        else
        set_Value ("AD_Table_ID", Integer.valueOf(AD_Table_ID));
        
    }
    
    /** Get Table.
    @return Database Table information */
    public int getAD_Table_ID() 
    {
        return get_ValueAsInt("AD_Table_ID");
        
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
    
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (String DocumentNo)
    {
        set_Value ("DocumentNo", DocumentNo);
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public String getDocumentNo() 
    {
        return (String)get_Value("DocumentNo");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
        
    }
    
    /** Set Position.
    @param XX_Position Posición de prioridad  */
    public void setXX_Position (int XX_Position)
    {
        set_Value ("XX_Position", Integer.valueOf(XX_Position));
        
    }
    
    /** Get Position.
    @return Posición de prioridad  */
    public int getXX_Position() 
    {
        return get_ValueAsInt("XX_Position");
        
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
    
    /** Data Base = 10000056 */
    public static final String XX_TYPESELECTION_DataBase = X_Ref_XX_TypeSelection.DATA_BASE.getValue();
    /** Parameter = 10000057 */
    public static final String XX_TYPESELECTION_Parameter = X_Ref_XX_TypeSelection.PARAMETER.getValue();
    /** Set Type Selection.
    @param XX_TypeSelection Tipo de seleccion, si es de BD o por parametro */
    public void setXX_TypeSelection (String XX_TypeSelection)
    {
        if (!X_Ref_XX_TypeSelection.isValid(XX_TypeSelection))
        throw new IllegalArgumentException ("XX_TypeSelection Invalid value - " + XX_TypeSelection + " - Reference_ID=1002049 - 10000056 - 10000057");
        set_Value ("XX_TypeSelection", XX_TypeSelection);
        
    }
    
    /** Get Type Selection.
    @return Tipo de seleccion, si es de BD o por parametro */
    public String getXX_TypeSelection() 
    {
        return (String)get_Value("XX_TypeSelection");
        
    }
    
    /** Set Condition Promotion.
    @param XX_VMR_ConditionPromotion_ID Condition Promotion */
    public void setXX_VMR_ConditionPromotion_ID (int XX_VMR_ConditionPromotion_ID)
    {
        if (XX_VMR_ConditionPromotion_ID < 1) throw new IllegalArgumentException ("XX_VMR_ConditionPromotion_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_ConditionPromotion_ID", Integer.valueOf(XX_VMR_ConditionPromotion_ID));
        
    }
    
    /** Get Condition Promotion.
    @return Condition Promotion */
    public int getXX_VMR_ConditionPromotion_ID() 
    {
        return get_ValueAsInt("XX_VMR_ConditionPromotion_ID");
        
    }
    
    
}
