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
/** Generated Model for XX_VLO_NewsReportCompany
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VLO_NewsReportCompany extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VLO_NewsReportCompany_ID id
    @param trx transaction
    */
    public X_XX_VLO_NewsReportCompany (Ctx ctx, int XX_VLO_NewsReportCompany_ID, Trx trx)
    {
        super (ctx, XX_VLO_NewsReportCompany_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VLO_NewsReportCompany_ID == 0)
        {
            setC_BPartner_ID (0);
            setXX_VLO_NewsReportCompany_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VLO_NewsReportCompany (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27605569963789L;
    /** Last Updated Timestamp 2011-12-09 11:00:47.0 */
    public static final long updatedMS = 1323444647000L;
    /** AD_Table_ID=1000853 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VLO_NewsReportCompany");
        
    }
    ;
    
    /** TableName=XX_VLO_NewsReportCompany */
    public static final String Table_Name="XX_VLO_NewsReportCompany";
    
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
        if (C_BPartner_ID < 1) throw new IllegalArgumentException ("C_BPartner_ID is mandatory.");
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
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
    
    /** Set News Report Company ID.
    @param XX_VLO_NewsReportCompany_ID News Report Company ID */
    public void setXX_VLO_NewsReportCompany_ID (int XX_VLO_NewsReportCompany_ID)
    {
        if (XX_VLO_NewsReportCompany_ID < 1) throw new IllegalArgumentException ("XX_VLO_NewsReportCompany_ID is mandatory.");
        set_ValueNoCheck ("XX_VLO_NewsReportCompany_ID", Integer.valueOf(XX_VLO_NewsReportCompany_ID));
        
    }
    
    /** Get News Report Company ID.
    @return News Report Company ID */
    public int getXX_VLO_NewsReportCompany_ID() 
    {
        return get_ValueAsInt("XX_VLO_NewsReportCompany_ID");
        
    }
    
    
}
