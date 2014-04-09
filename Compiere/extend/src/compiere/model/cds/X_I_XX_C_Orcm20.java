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
/** Generated Model for I_XX_C_Orcm20
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_C_Orcm20 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_C_Orcm20_ID id
    @param trx transaction
    */
    public X_I_XX_C_Orcm20 (Ctx ctx, int I_XX_C_Orcm20_ID, Trx trx)
    {
        super (ctx, I_XX_C_Orcm20_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_C_Orcm20_ID == 0)
        {
            setI_XX_C_ORCM20_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_C_Orcm20 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27565921212789L;
    /** Last Updated Timestamp 2010-09-06 13:28:16.0 */
    public static final long updatedMS = 1283795896000L;
    /** AD_Table_ID=1000154 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_C_Orcm20");
        
    }
    ;
    
    /** TableName=I_XX_C_Orcm20 */
    public static final String Table_Name="I_XX_C_Orcm20";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Document No.
    @param DocumentNo Document sequence number of the document */
    public void setDocumentNo (int DocumentNo)
    {
        set_Value ("DocumentNo", Integer.valueOf(DocumentNo));
        
    }
    
    /** Get Document No.
    @return Document sequence number of the document */
    public int getDocumentNo() 
    {
        return get_ValueAsInt("DocumentNo");
        
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
    public void setI_IsImported (String I_IsImported)
    {
        set_Value ("I_IsImported", I_IsImported);
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public String getI_IsImported() 
    {
        return (String)get_Value("I_IsImported");
        
    }
    
    /** Set I_XX_C_ORCM20_ID.
    @param I_XX_C_ORCM20_ID I_XX_C_ORCM20_ID */
    public void setI_XX_C_ORCM20_ID (int I_XX_C_ORCM20_ID)
    {
        if (I_XX_C_ORCM20_ID < 1) throw new IllegalArgumentException ("I_XX_C_ORCM20_ID is mandatory.");
        set_ValueNoCheck ("I_XX_C_ORCM20_ID", Integer.valueOf(I_XX_C_ORCM20_ID));
        
    }
    
    /** Get I_XX_C_ORCM20_ID.
    @return I_XX_C_ORCM20_ID */
    public int getI_XX_C_ORCM20_ID() 
    {
        return get_ValueAsInt("I_XX_C_ORCM20_ID");
        
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
    
    /** Set XX_CODCEM.
    @param XX_CODCEM Shiping Condition ID - Codigo Condicion de Embarque */
    public void setXX_CODCEM (int XX_CODCEM)
    {
        set_Value ("XX_CODCEM", Integer.valueOf(XX_CODCEM));
        
    }
    
    /** Get XX_CODCEM.
    @return Shiping Condition ID - Codigo Condicion de Embarque */
    public int getXX_CODCEM() 
    {
        return get_ValueAsInt("XX_CODCEM");
        
    }
    
    /** Set Department Code	.
    @param XX_CodDep Codigo del Departameto */
    public void setXX_CodDep (int XX_CodDep)
    {
        set_Value ("XX_CodDep", Integer.valueOf(XX_CodDep));
        
    }
    
    /** Get Department Code	.
    @return Codigo del Departameto */
    public int getXX_CodDep() 
    {
        return get_ValueAsInt("XX_CodDep");
        
    }
    
    /** Set XX_CODFOLL.
    @param XX_CODFOLL Brochure ID - Codigo de Folleto */
    public void setXX_CODFOLL (int XX_CODFOLL)
    {
        set_Value ("XX_CODFOLL", Integer.valueOf(XX_CODFOLL));
        
    }
    
    /** Get XX_CODFOLL.
    @return Brochure ID - Codigo de Folleto */
    public int getXX_CODFOLL() 
    {
        return get_ValueAsInt("XX_CODFOLL");
        
    }
    
    /** Set Currency Code.
    @param XX_CODMON Código de moneda  */
    public void setXX_CODMON (int XX_CODMON)
    {
        set_Value ("XX_CODMON", Integer.valueOf(XX_CODMON));
        
    }
    
    /** Get Currency Code.
    @return Código de moneda  */
    public int getXX_CODMON() 
    {
        return get_ValueAsInt("XX_CODMON");
        
    }
    
    /** Set XX_CODTEMP.
    @param XX_CODTEMP Season ID - Codigo Temporada */
    public void setXX_CODTEMP (int XX_CODTEMP)
    {
        set_Value ("XX_CODTEMP", Integer.valueOf(XX_CODTEMP));
        
    }
    
    /** Get XX_CODTEMP.
    @return Season ID - Codigo Temporada */
    public int getXX_CODTEMP() 
    {
        return get_ValueAsInt("XX_CODTEMP");
        
    }
    
    /** Set XX_CODUBE.
    @param XX_CODUBE Delivery Location ID - Codigo Locacion de Embarque */
    public void setXX_CODUBE (int XX_CODUBE)
    {
        set_Value ("XX_CODUBE", Integer.valueOf(XX_CODUBE));
        
    }
    
    /** Get XX_CODUBE.
    @return Delivery Location ID - Codigo Locacion de Embarque */
    public int getXX_CODUBE() 
    {
        return get_ValueAsInt("XX_CODUBE");
        
    }
    
    /** Set XX_CODVIA.
    @param XX_CODVIA Dispatch Route ID - Codigo Via de Despacho */
    public void setXX_CODVIA (int XX_CODVIA)
    {
        set_Value ("XX_CODVIA", Integer.valueOf(XX_CODVIA));
        
    }
    
    /** Get XX_CODVIA.
    @return Dispatch Route ID - Codigo Via de Despacho */
    public int getXX_CODVIA() 
    {
        return get_ValueAsInt("XX_CODVIA");
        
    }
    
    /** Set Alternate Code PER/EMP.
    @param XX_COEMPE Codigo alterno PER/EMP */
    public void setXX_COEMPE (int XX_COEMPE)
    {
        set_Value ("XX_COEMPE", Integer.valueOf(XX_COEMPE));
        
    }
    
    /** Get Alternate Code PER/EMP.
    @return Codigo alterno PER/EMP */
    public int getXX_COEMPE() 
    {
        return get_ValueAsInt("XX_COEMPE");
        
    }
    
    /** Set Descuento 1.
    @param XX_DESCU1 Discount 1 - Descuento 1 */
    public void setXX_DESCU1 (int XX_DESCU1)
    {
        set_Value ("XX_DESCU1", Integer.valueOf(XX_DESCU1));
        
    }
    
    /** Get Descuento 1.
    @return Discount 1 - Descuento 1 */
    public int getXX_DESCU1() 
    {
        return get_ValueAsInt("XX_DESCU1");
        
    }
    
    /** Set XX_DESCU2.
    @param XX_DESCU2 Discount 2 - Descuento 2 */
    public void setXX_DESCU2 (int XX_DESCU2)
    {
        set_Value ("XX_DESCU2", Integer.valueOf(XX_DESCU2));
        
    }
    
    /** Get XX_DESCU2.
    @return Discount 2 - Descuento 2 */
    public int getXX_DESCU2() 
    {
        return get_ValueAsInt("XX_DESCU2");
        
    }
    
    /** Set XX_DESCU3.
    @param XX_DESCU3 Discount 3 - Descuento 3 */
    public void setXX_DESCU3 (int XX_DESCU3)
    {
        set_Value ("XX_DESCU3", Integer.valueOf(XX_DESCU3));
        
    }
    
    /** Get XX_DESCU3.
    @return Discount 3 - Descuento 3 */
    public int getXX_DESCU3() 
    {
        return get_ValueAsInt("XX_DESCU3");
        
    }
    
    /** Set XX_DESCU4.
    @param XX_DESCU4 Discount 4 - Descuento 4 */
    public void setXX_DESCU4 (int XX_DESCU4)
    {
        set_Value ("XX_DESCU4", Integer.valueOf(XX_DESCU4));
        
    }
    
    /** Get XX_DESCU4.
    @return Discount 4 - Descuento 4 */
    public int getXX_DESCU4() 
    {
        return get_ValueAsInt("XX_DESCU4");
        
    }
    
    /** Set XX_FACCAM.
    @param XX_FACCAM Conversion Rate ID - Codigo Factor de Cambio */
    public void setXX_FACCAM (int XX_FACCAM)
    {
        set_Value ("XX_FACCAM", Integer.valueOf(XX_FACCAM));
        
    }
    
    /** Get XX_FACCAM.
    @return Conversion Rate ID - Codigo Factor de Cambio */
    public int getXX_FACCAM() 
    {
        return get_ValueAsInt("XX_FACCAM");
        
    }
    
    /** Set XX_FACCOE.
    @param XX_FACCOE Estimate Factor ID - Codigo Factor Estimado */
    public void setXX_FACCOE (int XX_FACCOE)
    {
        set_Value ("XX_FACCOE", Integer.valueOf(XX_FACCOE));
        
    }
    
    /** Get XX_FACCOE.
    @return Estimate Factor ID - Codigo Factor Estimado */
    public int getXX_FACCOE() 
    {
        return get_ValueAsInt("XX_FACCOE");
        
    }
    
    /** Set XX_FACCOP.
    @param XX_FACCOP Definitive Factor ID - Codigo Factor Definitivo */
    public void setXX_FACCOP (int XX_FACCOP)
    {
        set_Value ("XX_FACCOP", Integer.valueOf(XX_FACCOP));
        
    }
    
    /** Get XX_FACCOP.
    @return Definitive Factor ID - Codigo Factor Definitivo */
    public int getXX_FACCOP() 
    {
        return get_ValueAsInt("XX_FACCOP");
        
    }
    
    /** Set XX_FECCON.
    @param XX_FECCON Consigment Date - Fecha Consignacion */
    public void setXX_FECCON (Timestamp XX_FECCON)
    {
        set_ValueNoCheck ("XX_FECCON", XX_FECCON);
        
    }
    
    /** Get XX_FECCON.
    @return Consigment Date - Fecha Consignacion */
    public Timestamp getXX_FECCON() 
    {
        return (Timestamp)get_Value("XX_FECCON");
        
    }
    
    /** Set XX_FECDESP.
    @param XX_FECDESP Dispatch Date - Fecha de Despacho */
    public void setXX_FECDESP (Timestamp XX_FECDESP)
    {
        set_ValueNoCheck ("XX_FECDESP", XX_FECDESP);
        
    }
    
    /** Get XX_FECDESP.
    @return Dispatch Date - Fecha de Despacho */
    public Timestamp getXX_FECDESP() 
    {
        return (Timestamp)get_Value("XX_FECDESP");
        
    }
    
    /** Set XX_FECENT.
    @param XX_FECENT Arrival Date - Fecha de Llegada */
    public void setXX_FECENT (Timestamp XX_FECENT)
    {
        set_ValueNoCheck ("XX_FECENT", XX_FECENT);
        
    }
    
    /** Get XX_FECENT.
    @return Arrival Date - Fecha de Llegada */
    public Timestamp getXX_FECENT() 
    {
        return (Timestamp)get_Value("XX_FECENT");
        
    }
    
    /** Set XX_FECENTEST.
    @param XX_FECENTEST Estimate Date - Fecha Estimada de Llegada */
    public void setXX_FECENTEST (Timestamp XX_FECENTEST)
    {
        set_ValueNoCheck ("XX_FECENTEST", XX_FECENTEST);
        
    }
    
    /** Get XX_FECENTEST.
    @return Estimate Date - Fecha Estimada de Llegada */
    public Timestamp getXX_FECENTEST() 
    {
        return (Timestamp)get_Value("XX_FECENTEST");
        
    }
    
    /** Set XX_FORPAG.
    @param XX_FORPAG Payment Rule ID - Codigo Forma de Pago */
    public void setXX_FORPAG (int XX_FORPAG)
    {
        set_Value ("XX_FORPAG", Integer.valueOf(XX_FORPAG));
        
    }
    
    /** Get XX_FORPAG.
    @return Payment Rule ID - Codigo Forma de Pago */
    public int getXX_FORPAG() 
    {
        return get_ValueAsInt("XX_FORPAG");
        
    }
    
    /** Set XX_GUIAEM.
    @param XX_GUIAEM Guide Number - Numero Guia de Embarque */
    public void setXX_GUIAEM (int XX_GUIAEM)
    {
        set_Value ("XX_GUIAEM", Integer.valueOf(XX_GUIAEM));
        
    }
    
    /** Get XX_GUIAEM.
    @return Guide Number - Numero Guia de Embarque */
    public int getXX_GUIAEM() 
    {
        return get_ValueAsInt("XX_GUIAEM");
        
    }
    
    /** Set XX_MARGEN.
    @param XX_MARGEN Margin - Margen */
    public void setXX_MARGEN (int XX_MARGEN)
    {
        set_Value ("XX_MARGEN", Integer.valueOf(XX_MARGEN));
        
    }
    
    /** Get XX_MARGEN.
    @return Margin - Margen */
    public int getXX_MARGEN() 
    {
        return get_ValueAsInt("XX_MARGEN");
        
    }
    
    /** Set XX_PAIS.
    @param XX_PAIS Country Name - Nombre Pais */
    public void setXX_PAIS (String XX_PAIS)
    {
        set_Value ("XX_PAIS", XX_PAIS);
        
    }
    
    /** Get XX_PAIS.
    @return Country Name - Nombre Pais */
    public String getXX_PAIS() 
    {
        return (String)get_Value("XX_PAIS");
        
    }
    
    /** Set XX_PAIS_ID.
    @param XX_PAIS_ID Country ID - Codigo Pais */
    public void setXX_PAIS_ID (int XX_PAIS_ID)
    {
        if (XX_PAIS_ID <= 0) set_Value ("XX_PAIS_ID", null);
        else
        set_Value ("XX_PAIS_ID", Integer.valueOf(XX_PAIS_ID));
        
    }
    
    /** Get XX_PAIS_ID.
    @return Country ID - Codigo Pais */
    public int getXX_PAIS_ID() 
    {
        return get_ValueAsInt("XX_PAIS_ID");
        
    }
    
    /** Set XX_PTOLLE.
    @param XX_PTOLLE Arrival Port - Puerto de Llegada */
    public void setXX_PTOLLE (int XX_PTOLLE)
    {
        set_Value ("XX_PTOLLE", Integer.valueOf(XX_PTOLLE));
        
    }
    
    /** Get XX_PTOLLE.
    @return Arrival Port - Puerto de Llegada */
    public int getXX_PTOLLE() 
    {
        return get_ValueAsInt("XX_PTOLLE");
        
    }
    
    /** Set XX_STAORD.
    @param XX_STAORD Order Status - Estado de la Orden */
    public void setXX_STAORD (String XX_STAORD)
    {
        set_Value ("XX_STAORD", XX_STAORD);
        
    }
    
    /** Get XX_STAORD.
    @return Order Status - Estado de la Orden */
    public String getXX_STAORD() 
    {
        return (String)get_Value("XX_STAORD");
        
    }
    
    /** Set XX_TOTCOS.
    @param XX_TOTCOS Total Cost - Costo Total */
    public void setXX_TOTCOS (int XX_TOTCOS)
    {
        set_Value ("XX_TOTCOS", Integer.valueOf(XX_TOTCOS));
        
    }
    
    /** Get XX_TOTCOS.
    @return Total Cost - Costo Total */
    public int getXX_TOTCOS() 
    {
        return get_ValueAsInt("XX_TOTCOS");
        
    }
    
    /** Set XX_TOTIVA.
    @param XX_TOTIVA Total Cost Plus Tax - Costo Total mas Impuesto */
    public void setXX_TOTIVA (int XX_TOTIVA)
    {
        set_Value ("XX_TOTIVA", Integer.valueOf(XX_TOTIVA));
        
    }
    
    /** Get XX_TOTIVA.
    @return Total Cost Plus Tax - Costo Total mas Impuesto */
    public int getXX_TOTIVA() 
    {
        return get_ValueAsInt("XX_TOTIVA");
        
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
    
    /** Set XX_TOTVEN.
    @param XX_TOTVEN Total PVP */
    public void setXX_TOTVEN (int XX_TOTVEN)
    {
        set_Value ("XX_TOTVEN", Integer.valueOf(XX_TOTVEN));
        
    }
    
    /** Get XX_TOTVEN.
    @return Total PVP */
    public int getXX_TOTVEN() 
    {
        return get_ValueAsInt("XX_TOTVEN");
        
    }
    
    
}
