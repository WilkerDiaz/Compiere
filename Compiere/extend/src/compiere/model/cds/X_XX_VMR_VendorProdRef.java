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
/** Generated Model for XX_VMR_VendorProdRef
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_VendorProdRef extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_VendorProdRef_ID id
    @param trx transaction
    */
    public X_XX_VMR_VendorProdRef (Ctx ctx, int XX_VMR_VendorProdRef_ID, Trx trx)
    {
        super (ctx, XX_VMR_VendorProdRef_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_VendorProdRef_ID == 0)
        {
            setName (null);
            setValue (null);
            setXX_VMR_VendorProdRef_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_VendorProdRef (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27655677050789L;
    /** Last Updated Timestamp 2013-07-11 09:38:54.0 */
    public static final long updatedMS = 1373551734000L;
    /** AD_Table_ID=1000090 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_VendorProdRef");
        
    }
    ;
    
    /** TableName=XX_VMR_VendorProdRef */
    public static final String Table_Name="XX_VMR_VendorProdRef";
    
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
        if (C_BPartner_ID <= 0) set_Value ("C_BPartner_ID", null);
        else
        set_Value ("C_BPartner_ID", Integer.valueOf(C_BPartner_ID));
        
    }
    
    /** Get Business Partner.
    @return Identifies a Business Partner */
    public int getC_BPartner_ID() 
    {
        return get_ValueAsInt("C_BPartner_ID");
        
    }
    
    /** Set Tax Category.
    @param C_TaxCategory_ID Tax Category */
    public void setC_TaxCategory_ID (int C_TaxCategory_ID)
    {
        if (C_TaxCategory_ID <= 0) set_Value ("C_TaxCategory_ID", null);
        else
        set_Value ("C_TaxCategory_ID", Integer.valueOf(C_TaxCategory_ID));
        
    }
    
    /** Get Tax Category.
    @return Tax Category */
    public int getC_TaxCategory_ID() 
    {
        return get_ValueAsInt("C_TaxCategory_ID");
        
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
    
    /** Set Is Imported.
    @param IsImported Define si es importado */
    public void setIsImported (boolean IsImported)
    {
        set_Value ("IsImported", Boolean.valueOf(IsImported));
        
    }
    
    /** Get Is Imported.
    @return Define si es importado */
    public boolean isImported() 
    {
        return get_ValueAsBoolean("IsImported");
        
    }
    
    /** Set Attribute Set.
    @param M_AttributeSet_ID Product Attribute Set */
    public void setM_AttributeSet_ID (int M_AttributeSet_ID)
    {
        if (M_AttributeSet_ID <= 0) set_Value ("M_AttributeSet_ID", null);
        else
        set_Value ("M_AttributeSet_ID", Integer.valueOf(M_AttributeSet_ID));
        
    }
    
    /** Get Attribute Set.
    @return Product Attribute Set */
    public int getM_AttributeSet_ID() 
    {
        return get_ValueAsInt("M_AttributeSet_ID");
        
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
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        if (Value == null) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set XX_ComesFromExcel.
    @param XX_ComesFromExcel XX_ComesFromExcel */
    public void setXX_ComesFromExcel (boolean XX_ComesFromExcel)
    {
        set_Value ("XX_ComesFromExcel", Boolean.valueOf(XX_ComesFromExcel));
        
    }
    
    /** Get XX_ComesFromExcel.
    @return XX_ComesFromExcel */
    public boolean isXX_ComesFromExcel() 
    {
        return get_ValueAsBoolean("XX_ComesFromExcel");
        
    }
    
    /** Set Description for Vendor.
    @param XX_EnglishDescription Description for Vendor */
    public void setXX_EnglishDescription (String XX_EnglishDescription)
    {
        set_Value ("XX_EnglishDescription", XX_EnglishDescription);
        
    }
    
    /** Get Description for Vendor.
    @return Description for Vendor */
    public String getXX_EnglishDescription() 
    {
        return (String)get_Value("XX_EnglishDescription");
        
    }
    
    /** Set From Process.
    @param XX_FromProcess From Process */
    public void setXX_FromProcess (String XX_FromProcess)
    {
        set_Value ("XX_FromProcess", XX_FromProcess);
        
    }
    
    /** Get From Process.
    @return From Process */
    public String getXX_FromProcess() 
    {
        return (String)get_Value("XX_FromProcess");
        
    }
    
    /** Set Associated.
    @param XX_IsAssociated Associated */
    public void setXX_IsAssociated (boolean XX_IsAssociated)
    {
        set_Value ("XX_IsAssociated", Boolean.valueOf(XX_IsAssociated));
        
    }
    
    /** Get Associated.
    @return Associated */
    public boolean isXX_IsAssociated() 
    {
        return get_ValueAsBoolean("XX_IsAssociated");
        
    }
    
    /** Set Package Multiple.
    @param XX_PackageMultiple Package Multiple */
    public void setXX_PackageMultiple (int XX_PackageMultiple)
    {
        set_Value ("XX_PackageMultiple", Integer.valueOf(XX_PackageMultiple));
        
    }
    
    /** Get Package Multiple.
    @return Package Multiple */
    public int getXX_PackageMultiple() 
    {
        return get_ValueAsInt("XX_PackageMultiple");
        
    }
    
    /** Set Pieces By Sale.
    @param XX_PiecesBySale_ID Pieces By Sale */
    public void setXX_PiecesBySale_ID (int XX_PiecesBySale_ID)
    {
        if (XX_PiecesBySale_ID <= 0) set_Value ("XX_PiecesBySale_ID", null);
        else
        set_Value ("XX_PiecesBySale_ID", Integer.valueOf(XX_PiecesBySale_ID));
        
    }
    
    /** Get Pieces By Sale.
    @return Pieces By Sale */
    public int getXX_PiecesBySale_ID() 
    {
        return get_ValueAsInt("XX_PiecesBySale_ID");
        
    }
    
    /** Set Sale Unit.
    @param XX_SaleUnit_ID Sale Unit */
    public void setXX_SaleUnit_ID (int XX_SaleUnit_ID)
    {
        if (XX_SaleUnit_ID <= 0) set_Value ("XX_SaleUnit_ID", null);
        else
        set_Value ("XX_SaleUnit_ID", Integer.valueOf(XX_SaleUnit_ID));
        
    }
    
    /** Get Sale Unit.
    @return Sale Unit */
    public int getXX_SaleUnit_ID() 
    {
        return get_ValueAsInt("XX_SaleUnit_ID");
        
    }
    
    /** Set Concept Value.
    @param XX_VME_ConceptValue_ID Concept Value */
    public void setXX_VME_ConceptValue_ID (int XX_VME_ConceptValue_ID)
    {
        throw new IllegalArgumentException ("XX_VME_ConceptValue_ID is virtual column");
        
    }
    
    /** Get Concept Value.
    @return Concept Value */
    public int getXX_VME_ConceptValue_ID() 
    {
        return get_ValueAsInt("XX_VME_ConceptValue_ID");
        
    }
    
    /** Set Brand.
    @param XX_VMR_Brand_ID Id de la Tabla XX_VMR_BRAND(Marca) */
    public void setXX_VMR_Brand_ID (int XX_VMR_Brand_ID)
    {
        if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));
        
    }
    
    /** Get Brand.
    @return Id de la Tabla XX_VMR_BRAND(Marca) */
    public int getXX_VMR_Brand_ID() 
    {
        return get_ValueAsInt("XX_VMR_Brand_ID");
        
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
    
    /** Set Line.
    @param XX_VMR_Line_ID Line */
    public void setXX_VMR_Line_ID (int XX_VMR_Line_ID)
    {
        if (XX_VMR_Line_ID <= 0) set_Value ("XX_VMR_Line_ID", null);
        else
        set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));
        
    }
    
    /** Get Line.
    @return Line */
    public int getXX_VMR_Line_ID() 
    {
        return get_ValueAsInt("XX_VMR_Line_ID");
        
    }
    
    /** Set Main Characteristic.
    @param XX_VMR_LongCharacteristic_ID Main Characteristic */
    public void setXX_VMR_LongCharacteristic_ID (int XX_VMR_LongCharacteristic_ID)
    {
        if (XX_VMR_LongCharacteristic_ID <= 0) set_Value ("XX_VMR_LongCharacteristic_ID", null);
        else
        set_Value ("XX_VMR_LongCharacteristic_ID", Integer.valueOf(XX_VMR_LongCharacteristic_ID));
        
    }
    
    /** Get Main Characteristic.
    @return Main Characteristic */
    public int getXX_VMR_LongCharacteristic_ID() 
    {
        return get_ValueAsInt("XX_VMR_LongCharacteristic_ID");
        
    }
    
    /** Set Product Classification.
    @param XX_VMR_ProductClass_ID Clase de Productos que vende el Proveedor */
    public void setXX_VMR_ProductClass_ID (int XX_VMR_ProductClass_ID)
    {
        if (XX_VMR_ProductClass_ID <= 0) set_Value ("XX_VMR_ProductClass_ID", null);
        else
        set_Value ("XX_VMR_ProductClass_ID", Integer.valueOf(XX_VMR_ProductClass_ID));
        
    }
    
    /** Get Product Classification.
    @return Clase de Productos que vende el Proveedor */
    public int getXX_VMR_ProductClass_ID() 
    {
        return get_ValueAsInt("XX_VMR_ProductClass_ID");
        
    }
    
    /** Set Section.
    @param XX_VMR_Section_ID Section */
    public void setXX_VMR_Section_ID (int XX_VMR_Section_ID)
    {
        if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));
        
    }
    
    /** Get Section.
    @return Section */
    public int getXX_VMR_Section_ID() 
    {
        return get_ValueAsInt("XX_VMR_Section_ID");
        
    }
    
    /** Set XX_VMR_TypeBasic_ID.
    @param XX_VMR_TypeBasic_ID XX_VMR_TypeBasic_ID */
    public void setXX_VMR_TypeBasic_ID (int XX_VMR_TypeBasic_ID)
    {
        if (XX_VMR_TypeBasic_ID <= 0) set_Value ("XX_VMR_TypeBasic_ID", null);
        else
        set_Value ("XX_VMR_TypeBasic_ID", Integer.valueOf(XX_VMR_TypeBasic_ID));
        
    }
    
    /** Get XX_VMR_TypeBasic_ID.
    @return XX_VMR_TypeBasic_ID */
    public int getXX_VMR_TypeBasic_ID() 
    {
        return get_ValueAsInt("XX_VMR_TypeBasic_ID");
        
    }
    
    /** Set Label Type.
    @param XX_VMR_TypeLabel_ID Label Type */
    public void setXX_VMR_TypeLabel_ID (int XX_VMR_TypeLabel_ID)
    {
        if (XX_VMR_TypeLabel_ID <= 0) set_Value ("XX_VMR_TypeLabel_ID", null);
        else
        set_Value ("XX_VMR_TypeLabel_ID", Integer.valueOf(XX_VMR_TypeLabel_ID));
        
    }
    
    /** Get Label Type.
    @return Label Type */
    public int getXX_VMR_TypeLabel_ID() 
    {
        return get_ValueAsInt("XX_VMR_TypeLabel_ID");
        
    }
    
    /** Set Unit Conversion.
    @param XX_VMR_UnitConversion_ID Unit Conversion */
    public void setXX_VMR_UnitConversion_ID (int XX_VMR_UnitConversion_ID)
    {
        if (XX_VMR_UnitConversion_ID <= 0) set_Value ("XX_VMR_UnitConversion_ID", null);
        else
        set_Value ("XX_VMR_UnitConversion_ID", Integer.valueOf(XX_VMR_UnitConversion_ID));
        
    }
    
    /** Get Unit Conversion.
    @return Unit Conversion */
    public int getXX_VMR_UnitConversion_ID() 
    {
        return get_ValueAsInt("XX_VMR_UnitConversion_ID");
        
    }
    
    /** Set Purchase Unit.
    @param XX_VMR_UnitPurchase_ID Purchase Unit */
    public void setXX_VMR_UnitPurchase_ID (int XX_VMR_UnitPurchase_ID)
    {
        if (XX_VMR_UnitPurchase_ID <= 0) set_Value ("XX_VMR_UnitPurchase_ID", null);
        else
        set_Value ("XX_VMR_UnitPurchase_ID", Integer.valueOf(XX_VMR_UnitPurchase_ID));
        
    }
    
    /** Get Purchase Unit.
    @return Purchase Unit */
    public int getXX_VMR_UnitPurchase_ID() 
    {
        return get_ValueAsInt("XX_VMR_UnitPurchase_ID");
        
    }
    
    /** Set Vendor Product Reference.
    @param XX_VMR_VendorProdRef_ID Vendor Product Reference */
    public void setXX_VMR_VendorProdRef_ID (int XX_VMR_VendorProdRef_ID)
    {
        if (XX_VMR_VendorProdRef_ID < 1) throw new IllegalArgumentException ("XX_VMR_VendorProdRef_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
