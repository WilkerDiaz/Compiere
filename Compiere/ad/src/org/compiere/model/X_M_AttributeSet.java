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
package org.compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for M_AttributeSet
 *  @author Jorg Janke (generated) 
 *  @version Release 3.5.1 Dev - $Id: X_M_AttributeSet.java 8247 2009-12-08 15:26:09Z gwu $ */
public class X_M_AttributeSet extends PO
{
    /** Standard Constructor
    @param ctx context
    @param M_AttributeSet_ID id
    @param trx transaction
    */
    public X_M_AttributeSet (Ctx ctx, int M_AttributeSet_ID, Trx trx)
    {
        super (ctx, M_AttributeSet_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (M_AttributeSet_ID == 0)
        {
            setIsGuaranteeDate (false);
            setIsGuaranteeDateMandatory (false);
            setIsInstanceAttribute (false);
            setIsLot (false);
            setIsLotMandatory (false);
            setIsSerNo (false);
            setIsSerNoMandatory (false);
            setM_AttributeSet_ID (0);
            setMandatoryType (null);
            setName (null);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_M_AttributeSet (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27495261242789L;
    /** Last Updated Timestamp 2008-06-10 15:12:06.0 */
    public static final long updatedMS = 1213135926000L;
    /** AD_Table_ID=560 */
    public static final int Table_ID=560;
    
    /** TableName=M_AttributeSet */
    public static final String Table_Name="M_AttributeSet";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Shelf Life.
    @param GuaranteeDays Number of days after which the product will expire */
    public void setGuaranteeDays (int GuaranteeDays)
    {
        set_Value ("GuaranteeDays", Integer.valueOf(GuaranteeDays));
        
    }
    
    /** Get Shelf Life.
    @return Number of days after which the product will expire */
    public int getGuaranteeDays() 
    {
        return get_ValueAsInt("GuaranteeDays");
        
    }
    
    /** Set Expiration Date.
    @param IsGuaranteeDate Product has Expiry Date */
    public void setIsGuaranteeDate (boolean IsGuaranteeDate)
    {
        set_Value ("IsGuaranteeDate", Boolean.valueOf(IsGuaranteeDate));
        
    }
    
    /** Get Expiration Date.
    @return Product has Expiry Date */
    public boolean isGuaranteeDate() 
    {
        return get_ValueAsBoolean("IsGuaranteeDate");
        
    }
    
    /** Set Mandatory Expiration Date.
    @param IsGuaranteeDateMandatory The entry of an expiration Date is mandatory when creating a Product Instance */
    public void setIsGuaranteeDateMandatory (boolean IsGuaranteeDateMandatory)
    {
        set_Value ("IsGuaranteeDateMandatory", Boolean.valueOf(IsGuaranteeDateMandatory));
        
    }
    
    /** Get Mandatory Expiration Date.
    @return The entry of an expiration Date is mandatory when creating a Product Instance */
    public boolean isGuaranteeDateMandatory() 
    {
        return get_ValueAsBoolean("IsGuaranteeDateMandatory");
        
    }
    
    /** Set Instance Attribute.
    @param IsInstanceAttribute The product attribute is specific to the instance (like Serial No, Lot or Guarantee Date) */
    public void setIsInstanceAttribute (boolean IsInstanceAttribute)
    {
        set_Value ("IsInstanceAttribute", Boolean.valueOf(IsInstanceAttribute));
        
    }
    
    /** Get Instance Attribute.
    @return The product attribute is specific to the instance (like Serial No, Lot or Guarantee Date) */
    public boolean isInstanceAttribute() 
    {
        return get_ValueAsBoolean("IsInstanceAttribute");
        
    }
    
    /** Set Lot.
    @param IsLot The product instances have a Lot Number */
    public void setIsLot (boolean IsLot)
    {
        set_Value ("IsLot", Boolean.valueOf(IsLot));
        
    }
    
    /** Get Lot.
    @return The product instances have a Lot Number */
    public boolean isLot() 
    {
        return get_ValueAsBoolean("IsLot");
        
    }
    
    /** Set Mandatory Lot.
    @param IsLotMandatory The entry of Lot info is mandatory when creating a Product Instance */
    public void setIsLotMandatory (boolean IsLotMandatory)
    {
        set_Value ("IsLotMandatory", Boolean.valueOf(IsLotMandatory));
        
    }
    
    /** Get Mandatory Lot.
    @return The entry of Lot info is mandatory when creating a Product Instance */
    public boolean isLotMandatory() 
    {
        return get_ValueAsBoolean("IsLotMandatory");
        
    }
    
    /** Set Serial No.
    @param IsSerNo The product instances have Serial Numbers */
    public void setIsSerNo (boolean IsSerNo)
    {
        set_Value ("IsSerNo", Boolean.valueOf(IsSerNo));
        
    }
    
    /** Get Serial No.
    @return The product instances have Serial Numbers */
    public boolean isSerNo() 
    {
        return get_ValueAsBoolean("IsSerNo");
        
    }
    
    /** Set Mandatory Serial No.
    @param IsSerNoMandatory The entry of a Serial No is mandatory when creating a Product Instance */
    public void setIsSerNoMandatory (boolean IsSerNoMandatory)
    {
        set_Value ("IsSerNoMandatory", Boolean.valueOf(IsSerNoMandatory));
        
    }
    
    /** Get Mandatory Serial No.
    @return The entry of a Serial No is mandatory when creating a Product Instance */
    public boolean isSerNoMandatory() 
    {
        return get_ValueAsBoolean("IsSerNoMandatory");
        
    }
    
    /** Set Lot Char End Overwrite.
    @param LotCharEOverwrite Lot/Batch End Indicator overwrite - default » */
    public void setLotCharEOverwrite (String LotCharEOverwrite)
    {
        set_Value ("LotCharEOverwrite", LotCharEOverwrite);
        
    }
    
    /** Get Lot Char End Overwrite.
    @return Lot/Batch End Indicator overwrite - default » */
    public String getLotCharEOverwrite() 
    {
        return (String)get_Value("LotCharEOverwrite");
        
    }
    
    /** Set Lot Char Start Overwrite.
    @param LotCharSOverwrite Lot/Batch Start Indicator overwrite - default « */
    public void setLotCharSOverwrite (String LotCharSOverwrite)
    {
        set_Value ("LotCharSOverwrite", LotCharSOverwrite);
        
    }
    
    /** Get Lot Char Start Overwrite.
    @return Lot/Batch Start Indicator overwrite - default « */
    public String getLotCharSOverwrite() 
    {
        return (String)get_Value("LotCharSOverwrite");
        
    }
    
    /** Set Attribute Set.
    @param M_AttributeSet_ID Product Attribute Set */
    public void setM_AttributeSet_ID (int M_AttributeSet_ID)
    {
        if (M_AttributeSet_ID < 0) throw new IllegalArgumentException ("M_AttributeSet_ID is mandatory.");
        set_ValueNoCheck ("M_AttributeSet_ID", Integer.valueOf(M_AttributeSet_ID));
        
    }
    
    /** Get Attribute Set.
    @return Product Attribute Set */
    public int getM_AttributeSet_ID() 
    {
        return get_ValueAsInt("M_AttributeSet_ID");
        
    }
    
    /** Set Lot Control.
    @param M_LotCtl_ID Product Lot Control */
    public void setM_LotCtl_ID (int M_LotCtl_ID)
    {
        if (M_LotCtl_ID <= 0) set_Value ("M_LotCtl_ID", null);
        else
        set_Value ("M_LotCtl_ID", Integer.valueOf(M_LotCtl_ID));
        
    }
    
    /** Get Lot Control.
    @return Product Lot Control */
    public int getM_LotCtl_ID() 
    {
        return get_ValueAsInt("M_LotCtl_ID");
        
    }
    
    /** Set Serial No Control.
    @param M_SerNoCtl_ID Product Serial Number Control */
    public void setM_SerNoCtl_ID (int M_SerNoCtl_ID)
    {
        if (M_SerNoCtl_ID <= 0) set_Value ("M_SerNoCtl_ID", null);
        else
        set_Value ("M_SerNoCtl_ID", Integer.valueOf(M_SerNoCtl_ID));
        
    }
    
    /** Get Serial No Control.
    @return Product Serial Number Control */
    public int getM_SerNoCtl_ID() 
    {
        return get_ValueAsInt("M_SerNoCtl_ID");
        
    }
    
    /** Not Mandatory = N */
    public static final String MANDATORYTYPE_NotMandatory = X_Ref_M_AttributeSet_MandatoryType.NOT_MANDATORY.getValue();
    /** When Shipping = S */
    public static final String MANDATORYTYPE_WhenShipping = X_Ref_M_AttributeSet_MandatoryType.WHEN_SHIPPING.getValue();
    /** Always Mandatory = Y */
    public static final String MANDATORYTYPE_AlwaysMandatory = X_Ref_M_AttributeSet_MandatoryType.ALWAYS_MANDATORY.getValue();
    /** Set Mandatory Type.
    @param MandatoryType The specification of an Attribute is mandatory */
    public void setMandatoryType (String MandatoryType)
    {
        if (MandatoryType == null) throw new IllegalArgumentException ("MandatoryType is mandatory");
        if (!X_Ref_M_AttributeSet_MandatoryType.isValid(MandatoryType))
        throw new IllegalArgumentException ("MandatoryType Invalid value - " + MandatoryType + " - Reference_ID=324 - N - S - Y");
        set_Value ("MandatoryType", MandatoryType);
        
    }
    
    /** Get Mandatory Type.
    @return The specification of an Attribute is mandatory */
    public String getMandatoryType() 
    {
        return (String)get_Value("MandatoryType");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        if (Name == null) throw new IllegalArgumentException ("Name is mandatory.");
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set SerNo Char End Overwrite.
    @param SerNoCharEOverwrite Serial Number End Indicator overwrite - default empty */
    public void setSerNoCharEOverwrite (String SerNoCharEOverwrite)
    {
        set_Value ("SerNoCharEOverwrite", SerNoCharEOverwrite);
        
    }
    
    /** Get SerNo Char End Overwrite.
    @return Serial Number End Indicator overwrite - default empty */
    public String getSerNoCharEOverwrite() 
    {
        return (String)get_Value("SerNoCharEOverwrite");
        
    }
    
    /** Set SerNo Char Start Overwrite.
    @param SerNoCharSOverwrite Serial Number Start Indicator overwrite - default # */
    public void setSerNoCharSOverwrite (String SerNoCharSOverwrite)
    {
        set_Value ("SerNoCharSOverwrite", SerNoCharSOverwrite);
        
    }
    
    /** Get SerNo Char Start Overwrite.
    @return Serial Number Start Indicator overwrite - default # */
    public String getSerNoCharSOverwrite() 
    {
        return (String)get_Value("SerNoCharSOverwrite");
        
    }
    
    
}
