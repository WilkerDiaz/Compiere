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
/** Generated Model for I_XX_VMR_VendorProdRef
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_I_XX_VMR_VendorProdRef extends PO
{
    /** Standard Constructor
    @param ctx context
    @param I_XX_VMR_VendorProdRef_ID id
    @param trx transaction
    */
    public X_I_XX_VMR_VendorProdRef (Ctx ctx, int I_XX_VMR_VendorProdRef_ID, Trx trx)
    {
        super (ctx, I_XX_VMR_VendorProdRef_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (I_XX_VMR_VendorProdRef_ID == 0)
        {
            setI_XX_VMR_VENDORPRODREF_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_I_XX_VMR_VendorProdRef (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27568084720789L;
    /** Last Updated Timestamp 2010-10-01 14:26:44.0 */
    public static final long updatedMS = 1285959404000L;
    /** AD_Table_ID=1000194 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("I_XX_VMR_VendorProdRef");
        
    }
    ;
    
    /** TableName=I_XX_VMR_VendorProdRef */
    public static final String Table_Name="I_XX_VMR_VendorProdRef";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set Long Characteristic.
    @param CARACT FULL FEATURE DESCRIPTION */
    public void setCARACT (String CARACT)
    {
        set_Value ("CARACT", CARACT);
        
    }
    
    /** Get Long Characteristic.
    @return FULL FEATURE DESCRIPTION */
    public String getCARACT() 
    {
        return (String)get_Value("CARACT");
        
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
    
    /** Set COEMPE.
    @param COEMPE COEMPE */
    public void setCOEMPE (String COEMPE)
    {
        set_Value ("COEMPE", COEMPE);
        
    }
    
    /** Get COEMPE.
    @return COEMPE */
    public String getCOEMPE() 
    {
        return (String)get_Value("COEMPE");
        
    }
    
    /** Set CON.
    @param CON CON */
    public void setCON (String CON)
    {
        set_Value ("CON", CON);
        
    }
    
    /** Get CON.
    @return CON */
    public String getCON() 
    {
        return (String)get_Value("CON");
        
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
    public void setI_IsImported (boolean I_IsImported)
    {
        set_Value ("I_IsImported", Boolean.valueOf(I_IsImported));
        
    }
    
    /** Get Imported.
    @return Has this import been processed? */
    public boolean isI_IsImported() 
    {
        return get_ValueAsBoolean("I_IsImported");
        
    }
    
    /** Set I_XX_VMR_VENDORPRODREF_ID.
    @param I_XX_VMR_VENDORPRODREF_ID I_XX_VMR_VENDORPRODREF_ID */
    public void setI_XX_VMR_VENDORPRODREF_ID (int I_XX_VMR_VENDORPRODREF_ID)
    {
        if (I_XX_VMR_VENDORPRODREF_ID < 1) throw new IllegalArgumentException ("I_XX_VMR_VENDORPRODREF_ID is mandatory.");
        set_ValueNoCheck ("I_XX_VMR_VENDORPRODREF_ID", Integer.valueOf(I_XX_VMR_VENDORPRODREF_ID));
        
    }
    
    /** Get I_XX_VMR_VENDORPRODREF_ID.
    @return I_XX_VMR_VENDORPRODREF_ID */
    public int getI_XX_VMR_VENDORPRODREF_ID() 
    {
        return get_ValueAsInt("I_XX_VMR_VENDORPRODREF_ID");
        
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
    
    /** Set Package Multiple.
    @param MULEMP Package Multiple */
    public void setMULEMP (String MULEMP)
    {
        set_Value ("MULEMP", MULEMP);
        
    }
    
    /** Get Package Multiple.
    @return Package Multiple */
    public String getMULEMP() 
    {
        return (String)get_Value("MULEMP");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
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
    
    /** Set Name 2.
    @param Name2 Additional Name */
    public void setName2 (String Name2)
    {
        set_Value ("Name2", Name2);
        
    }
    
    /** Get Name 2.
    @return Additional Name */
    public String getName2() 
    {
        return (String)get_Value("Name2");
        
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
    
    /** Set Vendor Reference.
    @param REFPRO Alphanumeric identifier of the entity */
    public void setREFPRO (String REFPRO)
    {
        set_Value ("REFPRO", REFPRO);
        
    }
    
    /** Get Vendor Reference.
    @return Alphanumeric identifier of the entity */
    public String getREFPRO() 
    {
        return (String)get_Value("REFPRO");
        
    }
    
    /** Set Tax Type.
    @param TIPIMP Tax Type */
    public void setTIPIMP (String TIPIMP)
    {
        set_Value ("TIPIMP", TIPIMP);
        
    }
    
    /** Get Tax Type.
    @return Tax Type */
    public String getTIPIMP() 
    {
        return (String)get_Value("TIPIMP");
        
    }
    
    /** Set Unit Purchase.
    @param UNICOM PURCHASE UNIT OF MEASURE */
    public void setUNICOM (String UNICOM)
    {
        set_Value ("UNICOM", UNICOM);
        
    }
    
    /** Get Unit Purchase.
    @return PURCHASE UNIT OF MEASURE */
    public String getUNICOM() 
    {
        return (String)get_Value("UNICOM");
        
    }
    
    /** Set Unit of Measure.
    @param UNIVEN SELLING UNIT OF MEASURE */
    public void setUNIVEN (String UNIVEN)
    {
        set_Value ("UNIVEN", UNIVEN);
        
    }
    
    /** Get Unit of Measure.
    @return SELLING UNIT OF MEASURE */
    public String getUNIVEN() 
    {
        return (String)get_Value("UNIVEN");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set XX_DEPARTMENT_VALUE.
    @param XX_DEPARTMENT_VALUE XX_DEPARTMENT_VALUE */
    public void setXX_DEPARTMENT_VALUE (int XX_DEPARTMENT_VALUE)
    {
        set_Value ("XX_DEPARTMENT_VALUE", Integer.valueOf(XX_DEPARTMENT_VALUE));
        
    }
    
    /** Get XX_DEPARTMENT_VALUE.
    @return XX_DEPARTMENT_VALUE */
    public int getXX_DEPARTMENT_VALUE() 
    {
        return get_ValueAsInt("XX_DEPARTMENT_VALUE");
        
    }
    
    /** Set XX_LINE_VALUE.
    @param XX_LINE_VALUE XX_LINE_VALUE */
    public void setXX_LINE_VALUE (int XX_LINE_VALUE)
    {
        set_Value ("XX_LINE_VALUE", Integer.valueOf(XX_LINE_VALUE));
        
    }
    
    /** Get XX_LINE_VALUE.
    @return XX_LINE_VALUE */
    public int getXX_LINE_VALUE() 
    {
        return get_ValueAsInt("XX_LINE_VALUE");
        
    }
    
    /** Set XX_MARCA_VALUE.
    @param XX_MARCA_VALUE XX_MARCA_VALUE */
    public void setXX_MARCA_VALUE (String XX_MARCA_VALUE)
    {
        set_Value ("XX_MARCA_VALUE", XX_MARCA_VALUE);
        
    }
    
    /** Get XX_MARCA_VALUE.
    @return XX_MARCA_VALUE */
    public String getXX_MARCA_VALUE() 
    {
        return (String)get_Value("XX_MARCA_VALUE");
        
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
    
    /** Set XX_SECTION_VALUE.
    @param XX_SECTION_VALUE XX_SECTION_VALUE */
    public void setXX_SECTION_VALUE (int XX_SECTION_VALUE)
    {
        set_Value ("XX_SECTION_VALUE", Integer.valueOf(XX_SECTION_VALUE));
        
    }
    
    /** Get XX_SECTION_VALUE.
    @return XX_SECTION_VALUE */
    public int getXX_SECTION_VALUE() 
    {
        return get_ValueAsInt("XX_SECTION_VALUE");
        
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
        if (XX_VMR_VendorProdRef_ID <= 0) set_Value ("XX_VMR_VendorProdRef_ID", null);
        else
        set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));
        
    }
    
    /** Get Vendor Product Reference.
    @return Vendor Product Reference */
    public int getXX_VMR_VendorProdRef_ID() 
    {
        return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
        
    }
    
    
}
