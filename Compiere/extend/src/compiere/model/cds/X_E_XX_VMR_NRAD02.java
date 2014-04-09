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
/** Generated Model for E_XX_VMR_NRAD02
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_E_XX_VMR_NRAD02 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param E_XX_VMR_NRAD02_ID id
    @param trx transaction
    */
    public X_E_XX_VMR_NRAD02 (Ctx ctx, int E_XX_VMR_NRAD02_ID, Trx trx)
    {
        super (ctx, E_XX_VMR_NRAD02_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (E_XX_VMR_NRAD02_ID == 0)
        {
            setE_XX_VMR_NRAD02_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_E_XX_VMR_NRAD02 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27634250513789L;
    /** Last Updated Timestamp 2012-11-05 09:49:57.0 */
    public static final long updatedMS = 1352125197000L;
    /** AD_Table_ID=1003253 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("E_XX_VMR_NRAD02");
        
    }
    ;
    
    /** TableName=E_XX_VMR_NRAD02 */
    public static final String Table_Name="E_XX_VMR_NRAD02";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set AREA DE CHEQUEO.
    @param AREACH AREA DE CHEQUEO */
    public void setAREACH (String AREACH)
    {
        set_Value ("AREACH", AREACH);
        
    }
    
    /** Get AREA DE CHEQUEO.
    @return AREA DE CHEQUEO */
    public String getAREACH() 
    {
        return (String)get_Value("AREACH");
        
    }
    
    /** Set BULTO BECO.
    @param BULBEC BULTO BECO */
    public void setBULBEC (int BULBEC)
    {
        set_Value ("BULBEC", Integer.valueOf(BULBEC));
        
    }
    
    /** Get BULTO BECO.
    @return BULTO BECO */
    public int getBULBEC() 
    {
        return get_ValueAsInt("BULBEC");
        
    }
    
    /** Set BULTO TIENDA.
    @param BULTIE BULTO TIENDA */
    public void setBULTIE (int BULTIE)
    {
        set_Value ("BULTIE", Integer.valueOf(BULTIE));
        
    }
    
    /** Get BULTO TIENDA.
    @return BULTO TIENDA */
    public int getBULTIE() 
    {
        return get_ValueAsInt("BULTIE");
        
    }
    
    /** Set DIASTA.
    @param DIASTA Status Day at CB98 */
    public void setDIASTA (int DIASTA)
    {
        set_Value ("DIASTA", Integer.valueOf(DIASTA));
        
    }
    
    /** Get DIASTA.
    @return Status Day at CB98 */
    public int getDIASTA() 
    {
        return get_ValueAsInt("DIASTA");
        
    }
    
    /** Set E_XX_VMR_NRAD02_ID.
    @param E_XX_VMR_NRAD02_ID E_XX_VMR_NRAD02_ID */
    public void setE_XX_VMR_NRAD02_ID (int E_XX_VMR_NRAD02_ID)
    {
        if (E_XX_VMR_NRAD02_ID < 1) throw new IllegalArgumentException ("E_XX_VMR_NRAD02_ID is mandatory.");
        set_ValueNoCheck ("E_XX_VMR_NRAD02_ID", Integer.valueOf(E_XX_VMR_NRAD02_ID));
        
    }
    
    /** Get E_XX_VMR_NRAD02_ID.
    @return E_XX_VMR_NRAD02_ID */
    public int getE_XX_VMR_NRAD02_ID() 
    {
        return get_ValueAsInt("E_XX_VMR_NRAD02_ID");
        
    }
    
    /** Set Status Month.
    @param MESSTA Status Month at CB98 */
    public void setMESSTA (int MESSTA)
    {
        set_Value ("MESSTA", Integer.valueOf(MESSTA));
        
    }
    
    /** Get Status Month.
    @return Status Month at CB98 */
    public int getMESSTA() 
    {
        return get_ValueAsInt("MESSTA");
        
    }
    
    /** Set MTO RECIBIDO.
    @param MONREC MTO RECIBIDO */
    public void setMONREC (java.math.BigDecimal MONREC)
    {
        set_Value ("MONREC", MONREC);
        
    }
    
    /** Get MTO RECIBIDO.
    @return MTO RECIBIDO */
    public java.math.BigDecimal getMONREC() 
    {
        return get_ValueAsBigDecimal("MONREC");
        
    }
    
    /** Set Receipt Number.
    @param NUMREC receipt number */
    public void setNUMREC (int NUMREC)
    {
        set_Value ("NUMREC", Integer.valueOf(NUMREC));
        
    }
    
    /** Get Receipt Number.
    @return receipt number */
    public int getNUMREC() 
    {
        return get_ValueAsInt("NUMREC");
        
    }
    
    /** Set Status.
    @param Status Status of the currently running check */
    public void setStatus (String Status)
    {
        set_Value ("Status", Status);
        
    }
    
    /** Get Status.
    @return Status of the currently running check */
    public String getStatus() 
    {
        return (String)get_Value("Status");
        
    }
    
    /** Set Tienda.
    @param Tienda Wharehouse */
    public void setTienda (int Tienda)
    {
        set_Value ("Tienda", Integer.valueOf(Tienda));
        
    }
    
    /** Get Tienda.
    @return Wharehouse */
    public int getTienda() 
    {
        return get_ValueAsInt("Tienda");
        
    }
    
    /** Set Status Year(Reception).
    @param XX_ANOSTA Status Year(Reception) at CB98 */
    public void setXX_ANOSTA (int XX_ANOSTA)
    {
        set_Value ("XX_ANOSTA", Integer.valueOf(XX_ANOSTA));
        
    }
    
    /** Get Status Year(Reception).
    @return Status Year(Reception) at CB98 */
    public int getXX_ANOSTA() 
    {
        return get_ValueAsInt("XX_ANOSTA");
        
    }
    
    /** Set Year Of Release Status.
    @param XX_ASTDES Año de status despacho */
    public void setXX_ASTDES (int XX_ASTDES)
    {
        set_Value ("XX_ASTDES", Integer.valueOf(XX_ASTDES));
        
    }
    
    /** Get Year Of Release Status.
    @return Año de status despacho */
    public int getXX_ASTDES() 
    {
        return get_ValueAsInt("XX_ASTDES");
        
    }
    
    /** Set Day Release Status.
    @param XX_DSTDES Dia  de status despacho */
    public void setXX_DSTDES (int XX_DSTDES)
    {
        set_Value ("XX_DSTDES", Integer.valueOf(XX_DSTDES));
        
    }
    
    /** Get Day Release Status.
    @return Dia  de status despacho */
    public int getXX_DSTDES() 
    {
        return get_ValueAsInt("XX_DSTDES");
        
    }
    
    /** Set ReleaseGuide.
    @param XX_GUIDES Guia de despacho */
    public void setXX_GUIDES (String XX_GUIDES)
    {
        set_Value ("XX_GUIDES", XX_GUIDES);
        
    }
    
    /** Get ReleaseGuide.
    @return Guia de despacho */
    public String getXX_GUIDES() 
    {
        return (String)get_Value("XX_GUIDES");
        
    }
    
    /** Set Month Release Status.
    @param XX_MSTDES Mes de status despacho */
    public void setXX_MSTDES (int XX_MSTDES)
    {
        set_Value ("XX_MSTDES", Integer.valueOf(XX_MSTDES));
        
    }
    
    /** Get Month Release Status.
    @return Mes de status despacho */
    public int getXX_MSTDES() 
    {
        return get_ValueAsInt("XX_MSTDES");
        
    }
    
    /** Set XX_STAAUT.
    @param XX_STAAUT XX_STAAUT */
    public void setXX_STAAUT (String XX_STAAUT)
    {
        set_Value ("XX_STAAUT", XX_STAAUT);
        
    }
    
    /** Get XX_STAAUT.
    @return XX_STAAUT */
    public String getXX_STAAUT() 
    {
        return (String)get_Value("XX_STAAUT");
        
    }
    
    /** Set STAIMP.
    @param XX_STAIMP STAIMP */
    public void setXX_STAIMP (String XX_STAIMP)
    {
        set_Value ("XX_STAIMP", XX_STAIMP);
        
    }
    
    /** Get STAIMP.
    @return STAIMP */
    public String getXX_STAIMP() 
    {
        return (String)get_Value("XX_STAIMP");
        
    }
    
    /** Set Shop by Room Status.
    @param XX_STDESP Status de despacho por tienda */
    public void setXX_STDESP (String XX_STDESP)
    {
        set_Value ("XX_STDESP", XX_STDESP);
        
    }
    
    /** Get Shop by Room Status.
    @return Status de despacho por tienda */
    public String getXX_STDESP() 
    {
        return (String)get_Value("XX_STDESP");
        
    }
    
    /** Set XX_TOTPRO.
    @param XX_TOTPRO Product Quantity - Cantidad Producto */
    public void setXX_TOTPRO (int XX_TOTPRO)
    {
        set_Value ("XX_TOTPRO", Integer.valueOf(XX_TOTPRO));
        
    }
    
    /** Get XX_TOTPRO.
    @return Product Quantity - Cantidad Producto */
    public int getXX_TOTPRO() 
    {
        return get_ValueAsInt("XX_TOTPRO");
        
    }
    
    /** Set AUTORIZATION USER.
    @param XX_USRAUT AUTORIZATION USER */
    public void setXX_USRAUT (String XX_USRAUT)
    {
        set_Value ("XX_USRAUT", XX_USRAUT);
        
    }
    
    /** Get AUTORIZATION USER.
    @return AUTORIZATION USER */
    public String getXX_USRAUT() 
    {
        return (String)get_Value("XX_USRAUT");
        
    }
    
    
}
