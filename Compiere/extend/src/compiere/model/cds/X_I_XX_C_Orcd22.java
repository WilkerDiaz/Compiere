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
/** Generated Model for I_XX_C_Orcd22
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_C_Orcd22 extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_C_Orcd22_ID id
    @param trx transaction
    */
    public X_I_XX_C_Orcd22 (Ctx ctx, int I_XX_C_Orcd22_ID, Trx trx)
    {
        super (ctx, I_XX_C_Orcd22_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_C_Orcd22_ID == 0)
        {
            setI_XX_C_ORCD22_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_C_Orcd22 (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27565921212789L;
    /** Last Updated Timestamp 2010-09-06 13:28:16.0 */
    public static final long updatedMS = 1283795896000L;
    /** AD_Table_ID=1000158 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_C_Orcd22");
        
    }
    ;
    
    /** TableName=I_XX_C_Orcd22 */
    public static final String Table_Name="I_XX_C_Orcd22";
    
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
    
    /** Set I_XX_C_ORCD22_ID.
    @param I_XX_C_ORCD22_ID I_XX_C_ORCD22_ID */
    public void setI_XX_C_ORCD22_ID (int I_XX_C_ORCD22_ID)
    {
        if (I_XX_C_ORCD22_ID < 1) throw new IllegalArgumentException ("I_XX_C_ORCD22_ID is mandatory.");
        set_ValueNoCheck ("I_XX_C_ORCD22_ID", Integer.valueOf(I_XX_C_ORCD22_ID));
        
    }
    
    /** Get I_XX_C_ORCD22_ID.
    @return I_XX_C_ORCD22_ID */
    public int getI_XX_C_ORCD22_ID() 
    {
        return get_ValueAsInt("I_XX_C_ORCD22_ID");
        
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
    
    /** Set XX_CANCOM.
    @param XX_CANCOM XX_CANCOM */
    public void setXX_CANCOM (int XX_CANCOM)
    {
        set_Value ("XX_CANCOM", Integer.valueOf(XX_CANCOM));
        
    }
    
    /** Get XX_CANCOM.
    @return XX_CANCOM */
    public int getXX_CANCOM() 
    {
        return get_ValueAsInt("XX_CANCOM");
        
    }
    
    /** Set XX_CANOBS.
    @param XX_CANOBS XX_CANOBS */
    public void setXX_CANOBS (int XX_CANOBS)
    {
        set_Value ("XX_CANOBS", Integer.valueOf(XX_CANOBS));
        
    }
    
    /** Get XX_CANOBS.
    @return XX_CANOBS */
    public int getXX_CANOBS() 
    {
        return get_ValueAsInt("XX_CANOBS");
        
    }
    
    /** Set XX_CANVEN.
    @param XX_CANVEN XX_CANVEN */
    public void setXX_CANVEN (int XX_CANVEN)
    {
        set_Value ("XX_CANVEN", Integer.valueOf(XX_CANVEN));
        
    }
    
    /** Get XX_CANVEN.
    @return XX_CANVEN */
    public int getXX_CANVEN() 
    {
        return get_ValueAsInt("XX_CANVEN");
        
    }
    
    /** Set XX_CARAC1.
    @param XX_CARAC1 XX_CARAC1 */
    public void setXX_CARAC1 (String XX_CARAC1)
    {
        set_Value ("XX_CARAC1", XX_CARAC1);
        
    }
    
    /** Get XX_CARAC1.
    @return XX_CARAC1 */
    public String getXX_CARAC1() 
    {
        return (String)get_Value("XX_CARAC1");
        
    }
    
    /** Set XX_CARAC2.
    @param XX_CARAC2 XX_CARAC2 */
    public void setXX_CARAC2 (String XX_CARAC2)
    {
        set_Value ("XX_CARAC2", XX_CARAC2);
        
    }
    
    /** Get XX_CARAC2.
    @return XX_CARAC2 */
    public String getXX_CARAC2() 
    {
        return (String)get_Value("XX_CARAC2");
        
    }
    
    /** Set Code Product.
    @param XX_CODPRO Codigo del Producto */
    public void setXX_CODPRO (int XX_CODPRO)
    {
        set_Value ("XX_CODPRO", Integer.valueOf(XX_CODPRO));
        
    }
    
    /** Get Code Product.
    @return Codigo del Producto */
    public int getXX_CODPRO() 
    {
        return get_ValueAsInt("XX_CODPRO");
        
    }
    
    
}
