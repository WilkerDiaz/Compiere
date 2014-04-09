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
package compiere.model.payments;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_DetailAccoutingEntry
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_DetailAccoutingEntry extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_DetailAccoutingEntry_ID id
    @param trx transaction
    */
    public X_XX_VCN_DetailAccoutingEntry (Ctx ctx, int XX_VCN_DetailAccoutingEntry_ID, Trx trx)
    {
        super (ctx, XX_VCN_DetailAccoutingEntry_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_DetailAccoutingEntry_ID == 0)
        {
            setXX_VCN_DetailAccoutingEntry_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_DetailAccoutingEntry (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27641872578789L;
    /** Last Updated Timestamp 2013-02-01 15:04:22.0 */
    public static final long updatedMS = 1359747262000L;
    /** AD_Table_ID=1002657 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_DetailAccoutingEntry");
        
    }
    ;
    
    /** TableName=XX_VCN_DetailAccoutingEntry */
    public static final String Table_Name="XX_VCN_DetailAccoutingEntry";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Account Element.
    @param C_ElementValue_ID Account Element */
    public void setC_ElementValue_ID (int C_ElementValue_ID)
    {
        if (C_ElementValue_ID <= 0) set_Value ("C_ElementValue_ID", null);
        else
        set_Value ("C_ElementValue_ID", Integer.valueOf(C_ElementValue_ID));
        
    }
    
    /** Get Account Element.
    @return Account Element */
    public int getC_ElementValue_ID() 
    {
        return get_ValueAsInt("C_ElementValue_ID");
        
    }
    
    /** Set Invoice.
    @param C_Invoice_ID Invoice Identifier */
    public void setC_Invoice_ID (int C_Invoice_ID)
    {
        if (C_Invoice_ID <= 0) set_Value ("C_Invoice_ID", null);
        else
        set_Value ("C_Invoice_ID", Integer.valueOf(C_Invoice_ID));
        
    }
    
    /** Get Invoice.
    @return Invoice Identifier */
    public int getC_Invoice_ID() 
    {
        return get_ValueAsInt("C_Invoice_ID");
        
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
    
    /** Set AUX.
    @param XX_Aux AUX */
    public void setXX_Aux (String XX_Aux)
    {
        set_Value ("XX_Aux", XX_Aux);
        
    }
    
    /** Get AUX.
    @return AUX */
    public String getXX_Aux() 
    {
        return (String)get_Value("XX_Aux");
        
    }
    
    /** Set Departament.
    @param XX_Departament Departament */
    public void setXX_Departament (String XX_Departament)
    {
        set_Value ("XX_Departament", XX_Departament);
        
    }
    
    /** Get Departament.
    @return Departament */
    public String getXX_Departament() 
    {
        return (String)get_Value("XX_Departament");
        
    }
    
    /** Set Division.
    @param XX_Division Division */
    public void setXX_Division (String XX_Division)
    {
        set_Value ("XX_Division", XX_Division);
        
    }
    
    /** Get Division.
    @return Division */
    public String getXX_Division() 
    {
        return (String)get_Value("XX_Division");
        
    }
    
    /** Set Document Date.
    @param XX_DocumentDate Document Date */
    public void setXX_DocumentDate (Timestamp XX_DocumentDate)
    {
        set_Value ("XX_DocumentDate", XX_DocumentDate);
        
    }
    
    /** Get Document Date.
    @return Document Date */
    public Timestamp getXX_DocumentDate() 
    {
        return (Timestamp)get_Value("XX_DocumentDate");
        
    }
    
    /** Set Document Type.
    @param XX_DocumentType Document Type */
    public void setXX_DocumentType (String XX_DocumentType)
    {
        set_Value ("XX_DocumentType", XX_DocumentType);
        
    }
    
    /** Get Document Type.
    @return Document Type */
    public String getXX_DocumentType() 
    {
        return (String)get_Value("XX_DocumentType");
        
    }
    
    /** Set DueDate.
    @param XX_DueDate DueDate */
    public void setXX_DueDate (Timestamp XX_DueDate)
    {
        set_Value ("XX_DueDate", XX_DueDate);
        
    }
    
    /** Get DueDate.
    @return DueDate */
    public Timestamp getXX_DueDate() 
    {
        return (Timestamp)get_Value("XX_DueDate");
        
    }
    
    /** Set Have.
    @param XX_Have Have */
    public void setXX_Have (java.math.BigDecimal XX_Have)
    {
        set_Value ("XX_Have", XX_Have);
        
    }
    
    /** Get Have.
    @return Have */
    public java.math.BigDecimal getXX_Have() 
    {
        return get_ValueAsBigDecimal("XX_Have");
        
    }
    
    /** Set Line Code.
    @param XX_Line Código de línea */
    public void setXX_Line (int XX_Line)
    {
        set_Value ("XX_Line", Integer.valueOf(XX_Line));
        
    }
    
    /** Get Line Code.
    @return Código de línea */
    public int getXX_Line() 
    {
        return get_ValueAsInt("XX_Line");
        
    }
    
    /** Set Office.
    @param XX_Office Office */
    public void setXX_Office (String XX_Office)
    {
        set_Value ("XX_Office", XX_Office);
        
    }
    
    /** Get Office.
    @return Office */
    public String getXX_Office() 
    {
        return (String)get_Value("XX_Office");
        
    }
    
    /** Set Section Code.
    @param XX_SectionCode Section Code */
    public void setXX_SectionCode (String XX_SectionCode)
    {
        set_Value ("XX_SectionCode", XX_SectionCode);
        
    }
    
    /** Get Section Code.
    @return Section Code */
    public String getXX_SectionCode() 
    {
        return (String)get_Value("XX_SectionCode");
        
    }
    
    /** Set Should.
    @param XX_Should Should */
    public void setXX_Should (java.math.BigDecimal XX_Should)
    {
        set_Value ("XX_Should", XX_Should);
        
    }
    
    /** Get Should.
    @return Should */
    public java.math.BigDecimal getXX_Should() 
    {
        return get_ValueAsBigDecimal("XX_Should");
        
    }
    
    /** Set XX_VCN_AccoutingEntry_ID.
    @param XX_VCN_AccoutingEntry_ID XX_VCN_AccoutingEntry_ID */
    public void setXX_VCN_AccoutingEntry_ID (int XX_VCN_AccoutingEntry_ID)
    {
        if (XX_VCN_AccoutingEntry_ID <= 0) set_Value ("XX_VCN_AccoutingEntry_ID", null);
        else
        set_Value ("XX_VCN_AccoutingEntry_ID", Integer.valueOf(XX_VCN_AccoutingEntry_ID));
        
    }
    
    /** Get XX_VCN_AccoutingEntry_ID.
    @return XX_VCN_AccoutingEntry_ID */
    public int getXX_VCN_AccoutingEntry_ID() 
    {
        return get_ValueAsInt("XX_VCN_AccoutingEntry_ID");
        
    }
    
    /** Set XX_VCN_DetailAccoutingEntry_ID.
    @param XX_VCN_DetailAccoutingEntry_ID XX_VCN_DetailAccoutingEntry_ID */
    public void setXX_VCN_DetailAccoutingEntry_ID (int XX_VCN_DetailAccoutingEntry_ID)
    {
        if (XX_VCN_DetailAccoutingEntry_ID < 1) throw new IllegalArgumentException ("XX_VCN_DetailAccoutingEntry_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_DetailAccoutingEntry_ID", Integer.valueOf(XX_VCN_DetailAccoutingEntry_ID));
        
    }
    
    /** Get XX_VCN_DetailAccoutingEntry_ID.
    @return XX_VCN_DetailAccoutingEntry_ID */
    public int getXX_VCN_DetailAccoutingEntry_ID() 
    {
        return get_ValueAsInt("XX_VCN_DetailAccoutingEntry_ID");
        
    }
    
    /** Set XX_VCN_EstimatedAPayable_ID.
    @param XX_VCN_EstimatedAPayable_ID XX_VCN_EstimatedAPayable_ID */
    public void setXX_VCN_EstimatedAPayable_ID (int XX_VCN_EstimatedAPayable_ID)
    {
        if (XX_VCN_EstimatedAPayable_ID <= 0) set_Value ("XX_VCN_EstimatedAPayable_ID", null);
        else
        set_Value ("XX_VCN_EstimatedAPayable_ID", Integer.valueOf(XX_VCN_EstimatedAPayable_ID));
        
    }
    
    /** Get XX_VCN_EstimatedAPayable_ID.
    @return XX_VCN_EstimatedAPayable_ID */
    public int getXX_VCN_EstimatedAPayable_ID() 
    {
        return get_ValueAsInt("XX_VCN_EstimatedAPayable_ID");
        
    }
    
    /** Set XX_VCN_SALEPURCHASE_ID.
    @param XX_VCN_SALEPURCHASE_ID XX_VCN_SALEPURCHASE_ID */
    public void setXX_VCN_SALEPURCHASE_ID (int XX_VCN_SALEPURCHASE_ID)
    {
        if (XX_VCN_SALEPURCHASE_ID <= 0) set_Value ("XX_VCN_SALEPURCHASE_ID", null);
        else
        set_Value ("XX_VCN_SALEPURCHASE_ID", Integer.valueOf(XX_VCN_SALEPURCHASE_ID));
        
    }
    
    /** Get XX_VCN_SALEPURCHASE_ID.
    @return XX_VCN_SALEPURCHASE_ID */
    public int getXX_VCN_SALEPURCHASE_ID() 
    {
        return get_ValueAsInt("XX_VCN_SALEPURCHASE_ID");
        
    }
    
    
}
