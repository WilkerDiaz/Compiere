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
/** Generated Model for XX_VMR_Priority
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_Priority extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_Priority_ID id
    @param trx transaction
    */
    public X_XX_VMR_Priority (Ctx ctx, int XX_VMR_Priority_ID, Trx trx)
    {
        super (ctx, XX_VMR_Priority_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_Priority_ID == 0)
        {
            setXX_Motive (null);
            setXX_Position (0);
            setXX_VMR_Priority_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_Priority (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27596938764789L;
    /** Last Updated Timestamp 2011-08-31 13:27:28.0 */
    public static final long updatedMS = 1314813448000L;
    /** AD_Table_ID=1000553 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_Priority");
        
    }
    ;
    
    /** TableName=XX_VMR_Priority */
    public static final String Table_Name="XX_VMR_Priority";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Order.
    @param C_Order_ID Order */
    public void setC_Order_ID (int C_Order_ID)
    {
        if (C_Order_ID <= 0) set_ValueNoCheck ("C_Order_ID", null);
        else
        set_ValueNoCheck ("C_Order_ID", Integer.valueOf(C_Order_ID));
        
    }
    
    /** Get Order.
    @return Order */
    public int getC_Order_ID() 
    {
        return get_ValueAsInt("C_Order_ID");
        
    }
    
    /** Set Sales Transaction.
    @param IsSOTrx This is a Sales Transaction */
    public void setIsSOTrx (boolean IsSOTrx)
    {
        set_Value ("IsSOTrx", Boolean.valueOf(IsSOTrx));
        
    }
    
    /** Get Sales Transaction.
    @return This is a Sales Transaction */
    public boolean isSOTrx() 
    {
        return get_ValueAsBoolean("IsSOTrx");
        
    }
    
    /** Evento = EV */
    public static final String XX_MOTIVE_Evento = X_Ref_XX_Ref_PriorityMotive.EVENTO.getValue();
    /** Falla de Inventario = FI */
    public static final String XX_MOTIVE_FallaDeInventario = X_Ref_XX_Ref_PriorityMotive.FALLA_DE_INVENTARIO.getValue();
    /** Folleto = FO */
    public static final String XX_MOTIVE_Folleto = X_Ref_XX_Ref_PriorityMotive.FOLLETO.getValue();
    /** Pentacorporativa = PE */
    public static final String XX_MOTIVE_Pentacorporativa = X_Ref_XX_Ref_PriorityMotive.PENTACORPORATIVA.getValue();
    /** Set Motive.
    @param XX_Motive Motive */
    public void setXX_Motive (String XX_Motive)
    {
        if (XX_Motive == null) throw new IllegalArgumentException ("XX_Motive is mandatory");
        if (!X_Ref_XX_Ref_PriorityMotive.isValid(XX_Motive))
        throw new IllegalArgumentException ("XX_Motive Invalid value - " + XX_Motive + " - Reference_ID=1000450 - EV - FI - FO - PE");
        set_Value ("XX_Motive", XX_Motive);
        
    }
    
    /** Get Motive.
    @return Motive */
    public String getXX_Motive() 
    {
        return (String)get_Value("XX_Motive");
        
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
    
    /** Set DistributionHeader.
    @param XX_VMR_DistributionHeader_ID DistributionHeader */
    public void setXX_VMR_DistributionHeader_ID (int XX_VMR_DistributionHeader_ID)
    {
        if (XX_VMR_DistributionHeader_ID <= 0) set_ValueNoCheck ("XX_VMR_DistributionHeader_ID", null);
        else
        set_ValueNoCheck ("XX_VMR_DistributionHeader_ID", Integer.valueOf(XX_VMR_DistributionHeader_ID));
        
    }
    
    /** Get DistributionHeader.
    @return DistributionHeader */
    public int getXX_VMR_DistributionHeader_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionHeader_ID");
        
    }
    
    /** Set XX_VMR_Priority_ID.
    @param XX_VMR_Priority_ID XX_VMR_Priority_ID */
    public void setXX_VMR_Priority_ID (int XX_VMR_Priority_ID)
    {
        if (XX_VMR_Priority_ID < 1) throw new IllegalArgumentException ("XX_VMR_Priority_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_Priority_ID", Integer.valueOf(XX_VMR_Priority_ID));
        
    }
    
    /** Get XX_VMR_Priority_ID.
    @return XX_VMR_Priority_ID */
    public int getXX_VMR_Priority_ID() 
    {
        return get_ValueAsInt("XX_VMR_Priority_ID");
        
    }
    
    
}
