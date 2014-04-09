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
/** Generated Model for XX_VLO_ClientDelivery
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_ClientDelivery extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_ClientDelivery_ID id
    @param trx transaction
    */
    public X_XX_VLO_ClientDelivery (Ctx ctx, int XX_VLO_ClientDelivery_ID, Trx trx)
    {
        super (ctx, XX_VLO_ClientDelivery_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_ClientDelivery_ID == 0)
        {
            setValue (null);
            setXX_VLO_ClientDelivery_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_ClientDelivery (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27624243782789L;
    /** Last Updated Timestamp 2012-07-12 14:11:06.0 */
    public static final long updatedMS = 1342118466000L;
    /** AD_Table_ID=1000395 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_ClientDelivery");
        
    }
    ;
    
    /** TableName=XX_VLO_ClientDelivery */
    public static final String Table_Name="XX_VLO_ClientDelivery";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set City.
    @param C_City_ID City */
    public void setC_City_ID (int C_City_ID)
    {
        if (C_City_ID <= 0) set_Value ("C_City_ID", null);
        else
        set_Value ("C_City_ID", Integer.valueOf(C_City_ID));
        
    }
    
    /** Get City.
    @return City */
    public int getC_City_ID() 
    {
        return get_ValueAsInt("C_City_ID");
        
    }
    
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
        else
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
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
    
    /** Set Cedula/RIF.
    @param XX_CI_RIF Cedula o RIF del Proveedor o Cliente */
    public void setXX_CI_RIF (String XX_CI_RIF)
    {
        set_Value ("XX_CI_RIF", XX_CI_RIF);
        
    }
    
    /** Get Cedula/RIF.
    @return Cedula o RIF del Proveedor o Cliente */
    public String getXX_CI_RIF() 
    {
        return (String)get_Value("XX_CI_RIF");
        
    }
    
    /** Set XX_CLIENT.
    @param XX_CLIENT XX_CLIENT */
    public void setXX_CLIENT (String XX_CLIENT)
    {
        set_Value ("XX_CLIENT", XX_CLIENT);
        
    }
    
    /** Get XX_CLIENT.
    @return XX_CLIENT */
    public String getXX_CLIENT() 
    {
        return (String)get_Value("XX_CLIENT");
        
    }
    
    /** Set Date.
    @param XX_DATE Date */
    public void setXX_DATE (Timestamp XX_DATE)
    {
        set_Value ("XX_DATE", XX_DATE);
        
    }
    
    /** Get Date.
    @return Date */
    public Timestamp getXX_DATE() 
    {
        return (Timestamp)get_Value("XX_DATE");
        
    }
    
    /** Set XX_ServiceAppliesSector.
    @param XX_ServiceAppliesSector XX_ServiceAppliesSector */
    public void setXX_ServiceAppliesSector (String XX_ServiceAppliesSector)
    {
        throw new IllegalArgumentException ("XX_ServiceAppliesSector is virtual column");
        
    }
    
    /** Get XX_ServiceAppliesSector.
    @return XX_ServiceAppliesSector */
    public String getXX_ServiceAppliesSector() 
    {
        return (String)get_Value("XX_ServiceAppliesSector");
        
    }
    
    /** Set XX_ServiceAppliesUrban.
    @param XX_ServiceAppliesUrban XX_ServiceAppliesUrban */
    public void setXX_ServiceAppliesUrban (String XX_ServiceAppliesUrban)
    {
        throw new IllegalArgumentException ("XX_ServiceAppliesUrban is virtual column");
        
    }
    
    /** Get XX_ServiceAppliesUrban.
    @return XX_ServiceAppliesUrban */
    public String getXX_ServiceAppliesUrban() 
    {
        return (String)get_Value("XX_ServiceAppliesUrban");
        
    }
    
    /** Set Address.
    @param XX_VLO_Address Address */
    public void setXX_VLO_Address (String XX_VLO_Address)
    {
        set_Value ("XX_VLO_Address", XX_VLO_Address);
        
    }
    
    /** Get Address.
    @return Address */
    public String getXX_VLO_Address() 
    {
        return (String)get_Value("XX_VLO_Address");
        
    }
    
    /** Set Approve.
    @param XX_VLO_Approve Este proceso solo se debe ejectar cuando este seguro que los datos de la órden de entrega han sido completados. */
    public void setXX_VLO_Approve (String XX_VLO_Approve)
    {
        set_Value ("XX_VLO_Approve", XX_VLO_Approve);
        
    }
    
    /** Get Approve.
    @return Este proceso solo se debe ejectar cuando este seguro que los datos de la órden de entrega han sido completados. */
    public String getXX_VLO_Approve() 
    {
        return (String)get_Value("XX_VLO_Approve");
        
    }
    
    /** Set CI_RIF_Receiver.
    @param XX_VLO_CI_RIF_Receiver CI_RIF_Receiver */
    public void setXX_VLO_CI_RIF_Receiver (String XX_VLO_CI_RIF_Receiver)
    {
        set_Value ("XX_VLO_CI_RIF_Receiver", XX_VLO_CI_RIF_Receiver);
        
    }
    
    /** Get CI_RIF_Receiver.
    @return CI_RIF_Receiver */
    public String getXX_VLO_CI_RIF_Receiver() 
    {
        return (String)get_Value("XX_VLO_CI_RIF_Receiver");
        
    }
    
    /** Set XX_VLO_ClientDelivery_ID.
    @param XX_VLO_ClientDelivery_ID XX_VLO_ClientDelivery_ID */
    public void setXX_VLO_ClientDelivery_ID (int XX_VLO_ClientDelivery_ID)
    {
        if (XX_VLO_ClientDelivery_ID < 1) throw new IllegalArgumentException ("XX_VLO_ClientDelivery_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_ClientDelivery_ID", Integer.valueOf(XX_VLO_ClientDelivery_ID));
        
    }
    
    /** Get XX_VLO_ClientDelivery_ID.
    @return XX_VLO_ClientDelivery_ID */
    public int getXX_VLO_ClientDelivery_ID() 
    {
        return get_ValueAsInt("XX_VLO_ClientDelivery_ID");
        
    }
    
    /** Set ClientDeliveryReport.
    @param XX_VLO_ClientDeliveryReport ClientDeliveryReport */
    public void setXX_VLO_ClientDeliveryReport (String XX_VLO_ClientDeliveryReport)
    {
        set_Value ("XX_VLO_ClientDeliveryReport", XX_VLO_ClientDeliveryReport);
        
    }
    
    /** Get ClientDeliveryReport.
    @return ClientDeliveryReport */
    public String getXX_VLO_ClientDeliveryReport() 
    {
        return (String)get_Value("XX_VLO_ClientDeliveryReport");
        
    }
    
    /** Set DateReception.
    @param XX_VLO_DateReception DateReception */
    public void setXX_VLO_DateReception (Timestamp XX_VLO_DateReception)
    {
        set_Value ("XX_VLO_DateReception", XX_VLO_DateReception);
        
    }
    
    /** Get DateReception.
    @return DateReception */
    public Timestamp getXX_VLO_DateReception() 
    {
        return (Timestamp)get_Value("XX_VLO_DateReception");
        
    }
    
    /** Set Delivered.
    @param XX_VLO_Delivered Permite agregar los datos de la entrega. */
    public void setXX_VLO_Delivered (String XX_VLO_Delivered)
    {
        set_Value ("XX_VLO_Delivered", XX_VLO_Delivered);
        
    }
    
    /** Get Delivered.
    @return Permite agregar los datos de la entrega. */
    public String getXX_VLO_Delivered() 
    {
        return (String)get_Value("XX_VLO_Delivered");
        
    }
    
    /** Cliente Ausente = CA */
    public static final String XX_VLO_MOTIVE_ClienteAusente = X_Ref_XX_UndeliveredMotive.CLIENTE_AUSENTE.getValue();
    /** Dirección Errada = DE */
    public static final String XX_VLO_MOTIVE_DirecciónErrada = X_Ref_XX_UndeliveredMotive.DIRECCIÓN_ERRADA.getValue();
    /** Set Motive.
    @param XX_VLO_Motive Motive */
    public void setXX_VLO_Motive (String XX_VLO_Motive)
    {
        if (!X_Ref_XX_UndeliveredMotive.isValid(XX_VLO_Motive))
        throw new IllegalArgumentException ("XX_VLO_Motive Invalid value - " + XX_VLO_Motive + " - Reference_ID=1000302 - CA - DE");
        set_Value ("XX_VLO_Motive", XX_VLO_Motive);
        
    }
    
    /** Get Motive.
    @return Motive */
    public String getXX_VLO_Motive() 
    {
        return (String)get_Value("XX_VLO_Motive");
        
    }
    
    /** Set Municipality.
    @param XX_VLO_Municipality_ID Municipality */
    public void setXX_VLO_Municipality_ID (int XX_VLO_Municipality_ID)
    {
        if (XX_VLO_Municipality_ID <= 0) set_Value ("XX_VLO_Municipality_ID", null);
        else
        set_Value ("XX_VLO_Municipality_ID", Integer.valueOf(XX_VLO_Municipality_ID));
        
    }
    
    /** Get Municipality.
    @return Municipality */
    public int getXX_VLO_Municipality_ID() 
    {
        return get_ValueAsInt("XX_VLO_Municipality_ID");
        
    }
    
    /** Set ReceivedBy.
    @param XX_VLO_ReceivedBy ReceivedBy */
    public void setXX_VLO_ReceivedBy (String XX_VLO_ReceivedBy)
    {
        set_Value ("XX_VLO_ReceivedBy", XX_VLO_ReceivedBy);
        
    }
    
    /** Get ReceivedBy.
    @return ReceivedBy */
    public String getXX_VLO_ReceivedBy() 
    {
        return (String)get_Value("XX_VLO_ReceivedBy");
        
    }
    
    /** Set Sector.
    @param XX_VLO_Sector_ID Sector */
    public void setXX_VLO_Sector_ID (int XX_VLO_Sector_ID)
    {
        if (XX_VLO_Sector_ID <= 0) set_Value ("XX_VLO_Sector_ID", null);
        else
        set_Value ("XX_VLO_Sector_ID", Integer.valueOf(XX_VLO_Sector_ID));
        
    }
    
    /** Get Sector.
    @return Sector */
    public int getXX_VLO_Sector_ID() 
    {
        return get_ValueAsInt("XX_VLO_Sector_ID");
        
    }
    
    /** Entregado = EN */
    public static final String XX_VLO_STATUS_Entregado = X_Ref_XX_ClientDeliveryStatus.ENTREGADO.getValue();
    /** No Entregado = NE */
    public static final String XX_VLO_STATUS_NoEntregado = X_Ref_XX_ClientDeliveryStatus.NO_ENTREGADO.getValue();
    /** Pendiente Aprobación = PA */
    public static final String XX_VLO_STATUS_PendienteAprobación = X_Ref_XX_ClientDeliveryStatus.PENDIENTE_APROBACIÓN.getValue();
    /** Pendiente por Entregar = PE */
    public static final String XX_VLO_STATUS_PendientePorEntregar = X_Ref_XX_ClientDeliveryStatus.PENDIENTE_POR_ENTREGAR.getValue();
    /** Set Status.
    @param XX_VLO_Status Status */
    public void setXX_VLO_Status (String XX_VLO_Status)
    {
        if (!X_Ref_XX_ClientDeliveryStatus.isValid(XX_VLO_Status))
        throw new IllegalArgumentException ("XX_VLO_Status Invalid value - " + XX_VLO_Status + " - Reference_ID=1000549 - EN - NE - PA - PE");
        set_Value ("XX_VLO_Status", XX_VLO_Status);
        
    }
    
    /** Get Status.
    @return Status */
    public String getXX_VLO_Status() 
    {
        return (String)get_Value("XX_VLO_Status");
        
    }
    
    /** Set Street or Avenue.
    @param XX_VLO_Street_ID Street or Avenue */
    public void setXX_VLO_Street_ID (int XX_VLO_Street_ID)
    {
        if (XX_VLO_Street_ID <= 0) set_Value ("XX_VLO_Street_ID", null);
        else
        set_Value ("XX_VLO_Street_ID", Integer.valueOf(XX_VLO_Street_ID));
        
    }
    
    /** Get Street or Avenue.
    @return Street or Avenue */
    public int getXX_VLO_Street_ID() 
    {
        return get_ValueAsInt("XX_VLO_Street_ID");
        
    }
    
    /** Set TimeReception.
    @param XX_VLO_TimeReception TimeReception */
    public void setXX_VLO_TimeReception (Timestamp XX_VLO_TimeReception)
    {
        set_Value ("XX_VLO_TimeReception", XX_VLO_TimeReception);
        
    }
    
    /** Get TimeReception.
    @return TimeReception */
    public Timestamp getXX_VLO_TimeReception() 
    {
        return (Timestamp)get_Value("XX_VLO_TimeReception");
        
    }
    
    /** Set Undelivered.
    @param XX_VLO_Undelivered Permite agregar el motivo del por qué no se realizó la entrega */
    public void setXX_VLO_Undelivered (String XX_VLO_Undelivered)
    {
        set_Value ("XX_VLO_Undelivered", XX_VLO_Undelivered);
        
    }
    
    /** Get Undelivered.
    @return Permite agregar el motivo del por qué no se realizó la entrega */
    public String getXX_VLO_Undelivered() 
    {
        return (String)get_Value("XX_VLO_Undelivered");
        
    }
    
    /** Set Urbanization.
    @param XX_VLO_Urbanization_ID Urbanization */
    public void setXX_VLO_Urbanization_ID (int XX_VLO_Urbanization_ID)
    {
        if (XX_VLO_Urbanization_ID <= 0) set_Value ("XX_VLO_Urbanization_ID", null);
        else
        set_Value ("XX_VLO_Urbanization_ID", Integer.valueOf(XX_VLO_Urbanization_ID));
        
    }
    
    /** Get Urbanization.
    @return Urbanization */
    public int getXX_VLO_Urbanization_ID() 
    {
        return get_ValueAsInt("XX_VLO_Urbanization_ID");
        
    }
    
    
}
