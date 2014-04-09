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
/** Generated Model for XX_VLO_LeadTimes
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_LeadTimes extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_LeadTimes_ID id
    @param trx transaction
    */
    public X_XX_VLO_LeadTimes (Ctx ctx, int XX_VLO_LeadTimes_ID, Trx trx)
    {
        super (ctx, XX_VLO_LeadTimes_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_LeadTimes_ID == 0)
        {
            setC_Country_ID (0);
            setXX_VLO_LEADTIMES_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_LeadTimes (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27593402193789L;
    /** Last Updated Timestamp 2011-07-21 15:04:37.0 */
    public static final long updatedMS = 1311276877000L;
    /** AD_Table_ID=1000121 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_LeadTimes");
        
    }
    ;
    
    /** TableName=XX_VLO_LeadTimes */
    public static final String Table_Name="XX_VLO_LeadTimes";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Business Partner.
    @param C_BPartner_ID Identifies a Business Partner */
    public void setC_BPartner_ID (int C_BPartner_ID)
    {
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Country.
    @param C_Country_ID Country */
    public void setC_Country_ID (int C_Country_ID)
    {
        if (C_Country_ID < 1) throw new IllegalArgumentException ("C_Country_ID is mandatory.");
        set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));
        
    }
    
    /** Get Country.
    @return Country */
    public int getC_Country_ID() 
    {
        return get_ValueAsInt("C_Country_ID");
        
    }
    
    /** Set Date From.
    @param XX_DATEH Date From */
    public void setXX_DATEH (String XX_DATEH)
    {
        set_Value ("XX_DATEH", XX_DATEH);
        
    }
    
    /** Get Date From.
    @return Date From */
    public String getXX_DATEH() 
    {
        return (String)get_Value("XX_DATEH");
        
    }
    
    /** Set TLI.
    @param XX_IMPORTSLOGISTICSTIMETLI Tiempo de Logística de Importaciones  */
    public void setXX_IMPORTSLOGISTICSTIMETLI (java.math.BigDecimal XX_IMPORTSLOGISTICSTIMETLI)
    {
        set_Value ("XX_IMPORTSLOGISTICSTIMETLI", XX_IMPORTSLOGISTICSTIMETLI);
        
    }
    
    /** Get TLI.
    @return Tiempo de Logística de Importaciones  */
    public java.math.BigDecimal getXX_IMPORTSLOGISTICSTIMETLI() 
    {
        return get_ValueAsBigDecimal("XX_IMPORTSLOGISTICSTIMETLI");
        
    }
    
    /** Set TEI.
    @param XX_INTERNACARRIVALTIMETEI Tiempo de Entrega Internacional  */
    public void setXX_INTERNACARRIVALTIMETEI (java.math.BigDecimal XX_INTERNACARRIVALTIMETEI)
    {
        set_Value ("XX_INTERNACARRIVALTIMETEI", XX_INTERNACARRIVALTIMETEI);
        
    }
    
    /** Get TEI.
    @return Tiempo de Entrega Internacional  */
    public java.math.BigDecimal getXX_INTERNACARRIVALTIMETEI() 
    {
        return get_ValueAsBigDecimal("XX_INTERNACARRIVALTIMETEI");
        
    }
    
    /** Set TEN.
    @param XX_NACARRIVALTIMETEN Tiempo de Entrega Nacional  */
    public void setXX_NACARRIVALTIMETEN (java.math.BigDecimal XX_NACARRIVALTIMETEN)
    {
        set_Value ("XX_NACARRIVALTIMETEN", XX_NACARRIVALTIMETEN);
        
    }
    
    /** Get TEN.
    @return Tiempo de Entrega Nacional  */
    public java.math.BigDecimal getXX_NACARRIVALTIMETEN() 
    {
        return get_ValueAsBigDecimal("XX_NACARRIVALTIMETEN");
        
    }
    
    /** Set TNAC.
    @param XX_NATIONALIZATIONTIMETNAC Tiempo de Nacionalización */
    public void setXX_NATIONALIZATIONTIMETNAC (java.math.BigDecimal XX_NATIONALIZATIONTIMETNAC)
    {
        set_Value ("XX_NATIONALIZATIONTIMETNAC", XX_NATIONALIZATIONTIMETNAC);
        
    }
    
    /** Get TNAC.
    @return Tiempo de Nacionalización */
    public java.math.BigDecimal getXX_NATIONALIZATIONTIMETNAC() 
    {
        return get_ValueAsBigDecimal("XX_NATIONALIZATIONTIMETNAC");
        
    }
    
    /** Set TRC.
    @param XX_TIMEREGISTCELLATIONTRC TRC */
    public void setXX_TIMEREGISTCELLATIONTRC (java.math.BigDecimal XX_TIMEREGISTCELLATIONTRC)
    {
        set_Value ("XX_TIMEREGISTCELLATIONTRC", XX_TIMEREGISTCELLATIONTRC);
        
    }
    
    /** Get TRC.
    @return TRC */
    public java.math.BigDecimal getXX_TIMEREGISTCELLATIONTRC() 
    {
        return get_ValueAsBigDecimal("XX_TIMEREGISTCELLATIONTRC");
        
    }
    
    /** Set TT.
    @param XX_TRANSITTIMETT Tiempo de Tránsito  */
    public void setXX_TRANSITTIMETT (java.math.BigDecimal XX_TRANSITTIMETT)
    {
        set_Value ("XX_TRANSITTIMETT", XX_TRANSITTIMETT);
        
    }
    
    /** Get TT.
    @return Tiempo de Tránsito  */
    public java.math.BigDecimal getXX_TRANSITTIMETT() 
    {
        return get_ValueAsBigDecimal("XX_TRANSITTIMETT");
        
    }
    
    /** Set Arrival Port.
    @param XX_VLO_ArrivalPort_ID Arrival Port */
    public void setXX_VLO_ArrivalPort_ID (int XX_VLO_ArrivalPort_ID)
    {
        if (XX_VLO_ArrivalPort_ID <= 0) set_Value ("XX_VLO_ArrivalPort_ID", null);
        else
        set_Value ("XX_VLO_ArrivalPort_ID", Integer.valueOf(XX_VLO_ArrivalPort_ID));
        
    }
    
    /** Get Arrival Port.
    @return Arrival Port */
    public int getXX_VLO_ArrivalPort_ID() 
    {
        return get_ValueAsInt("XX_VLO_ArrivalPort_ID");
        
    }
    
    /** Set XX_VLO_LEADTIMES_ID.
    @param XX_VLO_LEADTIMES_ID XX_VLO_LEADTIMES_ID */
    public void setXX_VLO_LEADTIMES_ID (int XX_VLO_LEADTIMES_ID)
    {
        if (XX_VLO_LEADTIMES_ID < 1) throw new IllegalArgumentException ("XX_VLO_LEADTIMES_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_LEADTIMES_ID", Integer.valueOf(XX_VLO_LEADTIMES_ID));
        
    }
    
    /** Get XX_VLO_LEADTIMES_ID.
    @return XX_VLO_LEADTIMES_ID */
    public int getXX_VLO_LEADTIMES_ID() 
    {
        return get_ValueAsInt("XX_VLO_LEADTIMES_ID");
        
    }
    
    /** Set Date Until.
    @param XX_YEARH Date Until */
    public void setXX_YEARH (String XX_YEARH)
    {
        set_Value ("XX_YEARH", XX_YEARH);
        
    }
    
    /** Get Date Until.
    @return Date Until */
    public String getXX_YEARH() 
    {
        return (String)get_Value("XX_YEARH");
        
    }
    
    
}
