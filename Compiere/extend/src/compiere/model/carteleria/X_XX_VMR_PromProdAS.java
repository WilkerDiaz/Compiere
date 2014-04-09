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
package compiere.model.carteleria;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_PromProdAS
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_PromProdAS extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_PromProdAS_ID id
    @param trx transaction
    */
    public X_XX_VMR_PromProdAS (Ctx ctx, int XX_VMR_PromProdAS_ID, Trx trx)
    {
        super (ctx, XX_VMR_PromProdAS_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_PromProdAS_ID == 0)
        {
            setXX_VMR_PROMPRODAS_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_PromProdAS (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27601241868789L;
    /** Last Updated Timestamp 2011-10-20 08:45:52.0 */
    public static final long updatedMS = 1319116552000L;
    /** AD_Table_ID=1001053 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_PromProdAS");
        
    }
    ;
    
    /** TableName=XX_VMR_PromProdAS */
    public static final String Table_Name="XX_VMR_PromProdAS";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set codProd.
    @param codProd codProd */
    public void setcodProd (int codProd)
    {
        set_Value ("codProd", Integer.valueOf(codProd));
        
    }
    
    /** Get codProd.
    @return codProd */
    public int getcodProd() 
    {
        return get_ValueAsInt("codProd");
        
    }
    
    /** Set codProm.
    @param codProm codProm */
    public void setcodProm (int codProm)
    {
        set_Value ("codProm", Integer.valueOf(codProm));
        
    }
    
    /** Get codProm.
    @return codProm */
    public int getcodProm() 
    {
        return get_ValueAsInt("codProm");
        
    }
    
    /** Set descriptionPrm.
    @param descriptionPrm descriptionPrm */
    public void setdescriptionPrm (String descriptionPrm)
    {
        set_Value ("descriptionPrm", descriptionPrm);
        
    }
    
    /** Get descriptionPrm.
    @return descriptionPrm */
    public String getdescriptionPrm() 
    {
        return (String)get_Value("descriptionPrm");
        
    }
    
    /** Set discProm.
    @param discProm discProm */
    public void setdiscProm (java.math.BigDecimal discProm)
    {
        set_Value ("discProm", discProm);
        
    }
    
    /** Get discProm.
    @return discProm */
    public java.math.BigDecimal getdiscProm() 
    {
        return get_ValueAsBigDecimal("discProm");
        
    }
    
    /** Set finalDateP.
    @param finalDateP finalDateP */
    public void setfinalDateP (Timestamp finalDateP)
    {
        set_Value ("finalDateP", finalDateP);
        
    }
    
    /** Get finalDateP.
    @return finalDateP */
    public Timestamp getfinalDateP() 
    {
        return (Timestamp)get_Value("finalDateP");
        
    }
    
    /** Set initialDate.
    @param initialDate initialDate */
    public void setinitialDate (Timestamp initialDate)
    {
        set_Value ("initialDate", initialDate);
        
    }
    
    /** Get initialDate.
    @return initialDate */
    public Timestamp getinitialDate() 
    {
        return (Timestamp)get_Value("initialDate");
        
    }
    
    /** Set priceProm.
    @param priceProm priceProm */
    public void setpriceProm (java.math.BigDecimal priceProm)
    {
        set_Value ("priceProm", priceProm);
        
    }
    
    /** Get priceProm.
    @return priceProm */
    public java.math.BigDecimal getpriceProm() 
    {
        return get_ValueAsBigDecimal("priceProm");
        
    }
    
    /** Set statusProm.
    @param statusProm statusProm */
    public void setstatusProm (int statusProm)
    {
        set_Value ("statusProm", Integer.valueOf(statusProm));
        
    }
    
    /** Get statusProm.
    @return statusProm */
    public int getstatusProm() 
    {
        return get_ValueAsInt("statusProm");
        
    }
    
    /** Set XX_VMR_PROMPRODAS_ID.
    @param XX_VMR_PROMPRODAS_ID XX_VMR_PROMPRODAS_ID */
    public void setXX_VMR_PROMPRODAS_ID (int XX_VMR_PROMPRODAS_ID)
    {
        if (XX_VMR_PROMPRODAS_ID < 1) throw new IllegalArgumentException ("XX_VMR_PROMPRODAS_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_PROMPRODAS_ID", Integer.valueOf(XX_VMR_PROMPRODAS_ID));
        
    }
    
    /** Get XX_VMR_PROMPRODAS_ID.
    @return XX_VMR_PROMPRODAS_ID */
    public int getXX_VMR_PROMPRODAS_ID() 
    {
        return get_ValueAsInt("XX_VMR_PROMPRODAS_ID");
        
    }
    
    
}
