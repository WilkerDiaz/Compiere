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
/** Generated Model for XX_VMA_PageDepartment
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMA_PageDepartment extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMA_PageDepartment_ID id
    @param trx transaction
    */
    public X_XX_VMA_PageDepartment (Ctx ctx, int XX_VMA_PageDepartment_ID, Trx trx)
    {
        super (ctx, XX_VMA_PageDepartment_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMA_PageDepartment_ID == 0)
        {
            setXX_VMA_BrochurePage_ID (0);
            setXX_VMA_PageDepartment_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMA_PageDepartment (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27584142046789L;
    /** Last Updated Timestamp 2011-04-05 10:48:50.0 */
    public static final long updatedMS = 1302016730000L;
    /** AD_Table_ID=1000418 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMA_PageDepartment");
        
    }
    ;
    
    /** TableName=XX_VMA_PageDepartment */
    public static final String Table_Name="XX_VMA_PageDepartment";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Brochure Page.
    @param XX_VMA_BrochurePage_ID Identifier of the Brochure Page. */
    public void setXX_VMA_BrochurePage_ID (int XX_VMA_BrochurePage_ID)
    {
        if (XX_VMA_BrochurePage_ID < 1) throw new IllegalArgumentException ("XX_VMA_BrochurePage_ID is mandatory.");
        set_Value ("XX_VMA_BrochurePage_ID", Integer.valueOf(XX_VMA_BrochurePage_ID));
        
    }
    
    /** Get Brochure Page.
    @return Identifier of the Brochure Page. */
    public int getXX_VMA_BrochurePage_ID() 
    {
        return get_ValueAsInt("XX_VMA_BrochurePage_ID");
        
    }
    
    /** Set Page Department relation.
    @param XX_VMA_PageDepartment_ID The relation between a page with a department in a brochure */
    public void setXX_VMA_PageDepartment_ID (int XX_VMA_PageDepartment_ID)
    {
        if (XX_VMA_PageDepartment_ID < 1) throw new IllegalArgumentException ("XX_VMA_PageDepartment_ID is mandatory.");
        set_ValueNoCheck ("XX_VMA_PageDepartment_ID", Integer.valueOf(XX_VMA_PageDepartment_ID));
        
    }
    
    /** Get Page Department relation.
    @return The relation between a page with a department in a brochure */
    public int getXX_VMA_PageDepartment_ID() 
    {
        return get_ValueAsInt("XX_VMA_PageDepartment_ID");
        
    }
    
    /** Set Department.
    @param XX_VMR_Department_ID Department */
    public void setXX_VMR_Department_ID (int XX_VMR_Department_ID)
    {
        if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));
        
    }
    
    /** Get Department.
    @return Department */
    public int getXX_VMR_Department_ID() 
    {
        return get_ValueAsInt("XX_VMR_Department_ID");
        
    }
    
    
}
