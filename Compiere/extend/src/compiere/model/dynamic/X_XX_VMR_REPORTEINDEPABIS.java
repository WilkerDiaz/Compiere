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
package compiere.model.dynamic;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VMR_REPORTEINDEPABIS
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.1 - $Id: GenerateModel.java 8757 2010-05-12 21:32:32Z nnayak $ */
public class X_XX_VMR_REPORTEINDEPABIS extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_REPORTEINDEPABIS_ID id
    @param trx transaction
    */
    public X_XX_VMR_REPORTEINDEPABIS (Ctx ctx, int XX_VMR_REPORTEINDEPABIS_ID, Trx trx)
    {
        super (ctx, XX_VMR_REPORTEINDEPABIS_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_REPORTEINDEPABIS_ID == 0)
        {
            setXX_VMR_REPORTEINDEPABIS_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_REPORTEINDEPABIS (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27536727172789L;
    /** Last Updated Timestamp 2009-10-03 16:00:56.0 */
    public static final long updatedMS = 1254601856000L;
    /** AD_Table_ID=1000031 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_REPORTEINDEPABIS");
        
    }
    ;
    
    /** TableName=XX_VMR_REPORTEINDEPABIS */
    public static final String Table_Name="XX_VMR_REPORTEINDEPABIS";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Cantidad en inventario.
    @param XX_VMR_CANTINVENTARIO Cantidad en inventario */
    public void setXX_VMR_CANTINVENTARIO (int XX_VMR_CANTINVENTARIO)
    {
        set_Value ("XX_VMR_CANTINVENTARIO", Integer.valueOf(XX_VMR_CANTINVENTARIO));
        
    }
    
    /** Get Cantidad en inventario.
    @return Cantidad en inventario */
    public int getXX_VMR_CANTINVENTARIO() 
    {
        return get_ValueAsInt("XX_VMR_CANTINVENTARIO");
        
    }
    
    /** Set Código.
    @param XX_VMR_CODIGOPRODUCTO Código */
    public void setXX_VMR_CODIGOPRODUCTO (String XX_VMR_CODIGOPRODUCTO)
    {
        set_Value ("XX_VMR_CODIGOPRODUCTO", XX_VMR_CODIGOPRODUCTO);
        
    }
    
    /** Get Código.
    @return Código */
    public String getXX_VMR_CODIGOPRODUCTO() 
    {
        return (String)get_Value("XX_VMR_CODIGOPRODUCTO");
        
    }
    
    /** Set Hasta.
    @param XX_VMR_FECHAFIN Hasta */
    public void setXX_VMR_FECHAFIN (String XX_VMR_FECHAFIN)
    {
        set_Value ("XX_VMR_FECHAFIN", XX_VMR_FECHAFIN);
        
    }
    
    /** Get Hasta.
    @return Hasta */
    public String getXX_VMR_FECHAFIN() 
    {
        return (String)get_Value("XX_VMR_FECHAFIN");
        
    }
    
    /** Set Desde.
    @param XX_VMR_FECHAINICIO Desde */
    public void setXX_VMR_FECHAINICIO (String XX_VMR_FECHAINICIO)
    {
        set_Value ("XX_VMR_FECHAINICIO", XX_VMR_FECHAINICIO);
        
    }
    
    /** Get Desde.
    @return Desde */
    public String getXX_VMR_FECHAINICIO() 
    {
        return (String)get_Value("XX_VMR_FECHAINICIO");
        
    }
    
    /** Set Descripción del artículo.
    @param XX_VMR_NOMBREDESCRIPCIONMARCAR Descripción del artículo */
    public void setXX_VMR_NOMBREDESCRIPCIONMARCAR (String XX_VMR_NOMBREDESCRIPCIONMARCAR)
    {
        set_Value ("XX_VMR_NOMBREDESCRIPCIONMARCAR", XX_VMR_NOMBREDESCRIPCIONMARCAR);
        
    }
    
    /** Get Descripción del artículo.
    @return Descripción del artículo */
    public String getXX_VMR_NOMBREDESCRIPCIONMARCAR() 
    {
        return (String)get_Value("XX_VMR_NOMBREDESCRIPCIONMARCAR");
        
    }
    
    /** Set Nombre de la Promoción.
    @param XX_VMR_NOMBREPROMO Nombre de la Promoción */
    public void setXX_VMR_NOMBREPROMO (String XX_VMR_NOMBREPROMO)
    {
        set_Value ("XX_VMR_NOMBREPROMO", XX_VMR_NOMBREPROMO);
        
    }
    
    /** Get Nombre de la Promoción.
    @return Nombre de la Promoción */
    public String getXX_VMR_NOMBREPROMO() 
    {
        return (String)get_Value("XX_VMR_NOMBREPROMO");
        
    }
    
    /** Set % descuento.
    @param XX_VMR_PORCDESCUENTO % descuento */
    public void setXX_VMR_PORCDESCUENTO (String XX_VMR_PORCDESCUENTO)
    {
        set_Value ("XX_VMR_PORCDESCUENTO", XX_VMR_PORCDESCUENTO);
        
    }
    
    /** Get % descuento.
    @return % descuento */
    public String getXX_VMR_PORCDESCUENTO() 
    {
        return (String)get_Value("XX_VMR_PORCDESCUENTO");
        
    }
    
    /** Set PVP promocion c/IVA.
    @param XX_VMR_PVPPROMOIVA PVP promocion c/IVA */
    public void setXX_VMR_PVPPROMOIVA (String XX_VMR_PVPPROMOIVA)
    {
        set_Value ("XX_VMR_PVPPROMOIVA", XX_VMR_PVPPROMOIVA);
        
    }
    
    /** Get PVP promocion c/IVA.
    @return PVP promocion c/IVA */
    public String getXX_VMR_PVPPROMOIVA() 
    {
        return (String)get_Value("XX_VMR_PVPPROMOIVA");
        
    }
    
    /** Set PVP regular c/IVA.
    @param XX_VMR_PVPREGULARCONIVA PVP regular c/IVA */
    public void setXX_VMR_PVPREGULARCONIVA (String XX_VMR_PVPREGULARCONIVA)
    {
        set_Value ("XX_VMR_PVPREGULARCONIVA", XX_VMR_PVPREGULARCONIVA);
        
    }
    
    /** Get PVP regular c/IVA.
    @return PVP regular c/IVA */
    public String getXX_VMR_PVPREGULARCONIVA() 
    {
        return (String)get_Value("XX_VMR_PVPREGULARCONIVA");
        
    }
    
    /** Set XX_VMR_REPORTEINDEPABIS_ID.
    @param XX_VMR_REPORTEINDEPABIS_ID XX_VMR_REPORTEINDEPABIS_ID */
    public void setXX_VMR_REPORTEINDEPABIS_ID (int XX_VMR_REPORTEINDEPABIS_ID)
    {
        if (XX_VMR_REPORTEINDEPABIS_ID < 1) throw new IllegalArgumentException ("XX_VMR_REPORTEINDEPABIS_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_REPORTEINDEPABIS_ID", Integer.valueOf(XX_VMR_REPORTEINDEPABIS_ID));
        
    }
    
    /** Get XX_VMR_REPORTEINDEPABIS_ID.
    @return XX_VMR_REPORTEINDEPABIS_ID */
    public int getXX_VMR_REPORTEINDEPABIS_ID() 
    {
        return get_ValueAsInt("XX_VMR_REPORTEINDEPABIS_ID");
        
    }
	
    
}
