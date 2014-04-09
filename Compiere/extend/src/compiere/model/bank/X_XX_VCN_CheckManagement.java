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
package compiere.model.bank;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_VCN_CheckManagement
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_CheckManagement extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_CheckManagement_ID id
    @param trx transaction
    */
    public X_XX_VCN_CheckManagement (Ctx ctx, int XX_VCN_CheckManagement_ID, Trx trx)
    {
        super (ctx, XX_VCN_CheckManagement_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_CheckManagement_ID == 0)
        {
            setC_BankAccount_ID (0);
            setXX_VCN_Beneficiary (null);
            setXX_VCN_CheckManagement_ID (0);
            setXX_VCN_IdentifierNro (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_CheckManagement (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27663625828789L;
    /** Last Updated Timestamp 2013-10-11 09:38:32.0 */
    public static final long updatedMS = 1381500512000L;
    /** AD_Table_ID=1005153 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_CheckManagement");
        
    }
    ;
    
    /** TableName=XX_VCN_CheckManagement */
    public static final String Table_Name="XX_VCN_CheckManagement";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Bank Account.
    @param C_BankAccount_ID Account at the Bank */
    public void setC_BankAccount_ID (int C_BankAccount_ID)
    {
        if (C_BankAccount_ID < 1) throw new IllegalArgumentException ("C_BankAccount_ID is mandatory.");
        set_ValueNoCheck ("C_BankAccount_ID", Integer.valueOf(C_BankAccount_ID));
        
    }
    
    /** Get Bank Account.
    @return Account at the Bank */
    public int getC_BankAccount_ID() 
    {
        return get_ValueAsInt("C_BankAccount_ID");
        
    }
    
    /** Set XX_VCN_Beneficiary.
    @param XX_VCN_Beneficiary XX_VCN_Beneficiary */
    public void setXX_VCN_Beneficiary (String XX_VCN_Beneficiary)
    {
        if (XX_VCN_Beneficiary == null) throw new IllegalArgumentException ("XX_VCN_Beneficiary is mandatory.");
        set_Value ("XX_VCN_Beneficiary", XX_VCN_Beneficiary);
        
    }
    
    /** Get XX_VCN_Beneficiary.
    @return XX_VCN_Beneficiary */
    public String getXX_VCN_Beneficiary() 
    {
        return (String)get_Value("XX_VCN_Beneficiary");
        
    }
    
    /** Set XX_VCN_CheckManagement_ID.
    @param XX_VCN_CheckManagement_ID XX_VCN_CheckManagement_ID */
    public void setXX_VCN_CheckManagement_ID (int XX_VCN_CheckManagement_ID)
    {
        if (XX_VCN_CheckManagement_ID < 1) throw new IllegalArgumentException ("XX_VCN_CheckManagement_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_CheckManagement_ID", Integer.valueOf(XX_VCN_CheckManagement_ID));
        
    }
    
    /** Get XX_VCN_CheckManagement_ID.
    @return XX_VCN_CheckManagement_ID */
    public int getXX_VCN_CheckManagement_ID() 
    {
        return get_ValueAsInt("XX_VCN_CheckManagement_ID");
        
    }
    
    /** Set XX_VCN_IdentifierNro.
    @param XX_VCN_IdentifierNro XX_VCN_IdentifierNro */
    public void setXX_VCN_IdentifierNro (String XX_VCN_IdentifierNro)
    {
        if (XX_VCN_IdentifierNro == null) throw new IllegalArgumentException ("XX_VCN_IdentifierNro is mandatory.");
        set_Value ("XX_VCN_IdentifierNro", XX_VCN_IdentifierNro);
        
    }
    
    /** Get XX_VCN_IdentifierNro.
    @return XX_VCN_IdentifierNro */
    public String getXX_VCN_IdentifierNro() 
    {
        return (String)get_Value("XX_VCN_IdentifierNro");
        
    }
    
    /** Set XX_VCN_PrintLetter.
    @param XX_VCN_PrintLetter XX_VCN_PrintLetter */
    public void setXX_VCN_PrintLetter (String XX_VCN_PrintLetter)
    {
        set_Value ("XX_VCN_PrintLetter", XX_VCN_PrintLetter);
        
    }
    
    /** Get XX_VCN_PrintLetter.
    @return XX_VCN_PrintLetter */
    public String getXX_VCN_PrintLetter() 
    {
        return (String)get_Value("XX_VCN_PrintLetter");
        
    }
    
    
}
