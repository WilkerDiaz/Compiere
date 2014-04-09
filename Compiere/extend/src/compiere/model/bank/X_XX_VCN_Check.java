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
/** Generated Model for XX_VCN_Check
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VCN_Check extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VCN_Check_ID id
    @param trx transaction
    */
    public X_XX_VCN_Check (Ctx ctx, int XX_VCN_Check_ID, Trx trx)
    {
        super (ctx, XX_VCN_Check_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VCN_Check_ID == 0)
        {
            setC_BankAccountDoc_ID (0);
            setXX_VCN_Check_ID (0);
            setXX_VCN_CheckNumber (0);
            setXX_VCN_LocationofChecks (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VCN_Check (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27665893641789L;
    /** Last Updated Timestamp 2013-11-06 15:35:25.0 */
    public static final long updatedMS = 1383768325000L;
    /** AD_Table_ID=1004753 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VCN_Check");
        
    }
    ;
    
    /** TableName=XX_VCN_Check */
    public static final String Table_Name="XX_VCN_Check";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Bank Account Document.
    @param C_BankAccountDoc_ID Checks, Transfers, etc. */
    public void setC_BankAccountDoc_ID (int C_BankAccountDoc_ID)
    {
        if (C_BankAccountDoc_ID < 1) throw new IllegalArgumentException ("C_BankAccountDoc_ID is mandatory.");
        set_Value ("C_BankAccountDoc_ID", Integer.valueOf(C_BankAccountDoc_ID));
        
    }
    
    /** Get Bank Account Document.
    @return Checks, Transfers, etc. */
    public int getC_BankAccountDoc_ID() 
    {
        return get_ValueAsInt("C_BankAccountDoc_ID");
        
    }
    
    /** Set Payment.
    @param C_Payment_ID Payment identifier */
    public void setC_Payment_ID (int C_Payment_ID)
    {
        if (C_Payment_ID <= 0) set_Value ("C_Payment_ID", null);
        else
        set_Value ("C_Payment_ID", Integer.valueOf(C_Payment_ID));
        
    }
    
    /** Get Payment.
    @return Payment identifier */
    public int getC_Payment_ID() 
    {
        return get_ValueAsInt("C_Payment_ID");
        
    }
    
    /** Set XX_VCN_BankName.
    @param XX_VCN_BankName XX_VCN_BankName */
    public void setXX_VCN_BankName (String XX_VCN_BankName)
    {
        throw new IllegalArgumentException ("XX_VCN_BankName is virtual column");
        
    }
    
    /** Get XX_VCN_BankName.
    @return XX_VCN_BankName */
    public String getXX_VCN_BankName() 
    {
        return (String)get_Value("XX_VCN_BankName");
        
    }
    
    /** Set VCN_CancellationDate.
    @param XX_VCN_CancellationDate VCN_CancellationDate */
    public void setXX_VCN_CancellationDate (Timestamp XX_VCN_CancellationDate)
    {
        set_Value ("XX_VCN_CancellationDate", XX_VCN_CancellationDate);
        
    }
    
    /** Get VCN_CancellationDate.
    @return VCN_CancellationDate */
    public Timestamp getXX_VCN_CancellationDate() 
    {
        return (Timestamp)get_Value("XX_VCN_CancellationDate");
        
    }
    
    /** Error en firma = 1 */
    public static final String XX_VCN_CAUSESOFCANCELLATION_ErrorEnFirma = X_Ref_XX_VCN_CausesCancellationofChecks.ERROR_EN_FIRMA.getValue();
    /** Se dañó el cheque = 2 */
    public static final String XX_VCN_CAUSESOFCANCELLATION_SeDañóElCheque = X_Ref_XX_VCN_CausesCancellationofChecks.SE_DAÑÓ_EL_CHEQUE.getValue();
    /** Error en monto = 3 */
    public static final String XX_VCN_CAUSESOFCANCELLATION_ErrorEnMonto = X_Ref_XX_VCN_CausesCancellationofChecks.ERROR_EN_MONTO.getValue();
    /** Se desactivo Cuenta/Chequera = 4 */
    public static final String XX_VCN_CAUSESOFCANCELLATION_SeDesactivoCuentaChequera = X_Ref_XX_VCN_CausesCancellationofChecks.SE_DESACTIVO_CUENTA_CHEQUERA.getValue();
    /** Set VCN_CausesofCancellation.
    @param XX_VCN_CausesofCancellation VCN_CausesofCancellation */
    public void setXX_VCN_CausesofCancellation (String XX_VCN_CausesofCancellation)
    {
        if (!X_Ref_XX_VCN_CausesCancellationofChecks.isValid(XX_VCN_CausesofCancellation))
        throw new IllegalArgumentException ("XX_VCN_CausesofCancellation Invalid value - " + XX_VCN_CausesofCancellation + " - Reference_ID=1003750 - 1 - 2 - 3 - 4");
        set_Value ("XX_VCN_CausesofCancellation", XX_VCN_CausesofCancellation);
        
    }
    
    /** Get VCN_CausesofCancellation.
    @return VCN_CausesofCancellation */
    public String getXX_VCN_CausesofCancellation() 
    {
        return (String)get_Value("XX_VCN_CausesofCancellation");
        
    }
    
    /** Set XX_VCN_Check_ID.
    @param XX_VCN_Check_ID XX_VCN_Check_ID */
    public void setXX_VCN_Check_ID (int XX_VCN_Check_ID)
    {
        if (XX_VCN_Check_ID < 1) throw new IllegalArgumentException ("XX_VCN_Check_ID is mandatory.");
        set_ValueNoCheck ("XX_VCN_Check_ID", Integer.valueOf(XX_VCN_Check_ID));
        
    }
    
    /** Get XX_VCN_Check_ID.
    @return XX_VCN_Check_ID */
    public int getXX_VCN_Check_ID() 
    {
        return get_ValueAsInt("XX_VCN_Check_ID");
        
    }
    
    /** Set VCN_CheckNumber.
    @param XX_VCN_CheckNumber VCN_CheckNumber */
    public void setXX_VCN_CheckNumber (int XX_VCN_CheckNumber)
    {
        set_Value ("XX_VCN_CheckNumber", Integer.valueOf(XX_VCN_CheckNumber));
        
    }
    
    /** Get VCN_CheckNumber.
    @return VCN_CheckNumber */
    public int getXX_VCN_CheckNumber() 
    {
        return get_ValueAsInt("XX_VCN_CheckNumber");
        
    }
    
    /** Set VCN_DateofIssuance.
    @param XX_VCN_DateofIssuance VCN_DateofIssuance */
    public void setXX_VCN_DateofIssuance (Timestamp XX_VCN_DateofIssuance)
    {
        set_Value ("XX_VCN_DateofIssuance", XX_VCN_DateofIssuance);
        
    }
    
    /** Get VCN_DateofIssuance.
    @return VCN_DateofIssuance */
    public Timestamp getXX_VCN_DateofIssuance() 
    {
        return (Timestamp)get_Value("XX_VCN_DateofIssuance");
        
    }
    
    /** En oficina = 1 */
    public static final String XX_VCN_LOCATIONOFCHECKS_EnOficina = X_Ref_XX_VCN_LocationofChecks.EN_OFICINA.getValue();
    /** Cobrado = 2 */
    public static final String XX_VCN_LOCATIONOFCHECKS_Cobrado = X_Ref_XX_VCN_LocationofChecks.COBRADO.getValue();
    /** Entregado = 3 */
    public static final String XX_VCN_LOCATIONOFCHECKS_Entregado = X_Ref_XX_VCN_LocationofChecks.ENTREGADO.getValue();
    /** Perdido = 4 */
    public static final String XX_VCN_LOCATIONOFCHECKS_Perdido = X_Ref_XX_VCN_LocationofChecks.PERDIDO.getValue();
    /** Elaborado = 5 */
    public static final String XX_VCN_LOCATIONOFCHECKS_Elaborado = X_Ref_XX_VCN_LocationofChecks.ELABORADO.getValue();
    /** Anulado = 6 */
    public static final String XX_VCN_LOCATIONOFCHECKS_Anulado = X_Ref_XX_VCN_LocationofChecks.ANULADO.getValue();
    /** Set LocationofChecks.
    @param XX_VCN_LocationofChecks LocationofChecks */
    public void setXX_VCN_LocationofChecks (String XX_VCN_LocationofChecks)
    {
        if (XX_VCN_LocationofChecks == null) throw new IllegalArgumentException ("XX_VCN_LocationofChecks is mandatory");
        if (!X_Ref_XX_VCN_LocationofChecks.isValid(XX_VCN_LocationofChecks))
        throw new IllegalArgumentException ("XX_VCN_LocationofChecks Invalid value - " + XX_VCN_LocationofChecks + " - Reference_ID=1003749 - 1 - 2 - 3 - 4 - 5 - 6");
        set_Value ("XX_VCN_LocationofChecks", XX_VCN_LocationofChecks);
        
    }
    
    /** Get LocationofChecks.
    @return LocationofChecks */
    public String getXX_VCN_LocationofChecks() 
    {
        return (String)get_Value("XX_VCN_LocationofChecks");
        
    }
    
    /** Set VoidCheck.
    @param XX_VCN_VoidCheck VoidCheck */
    public void setXX_VCN_VoidCheck (String XX_VCN_VoidCheck)
    {
        set_Value ("XX_VCN_VoidCheck", XX_VCN_VoidCheck);
        
    }
    
    /** Get VoidCheck.
    @return VoidCheck */
    public String getXX_VCN_VoidCheck() 
    {
        return (String)get_Value("XX_VCN_VoidCheck");
        
    }
    
    
}
