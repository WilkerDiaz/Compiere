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
/** Generated Model for XX_VMR_DistributionDetail
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_VMR_DistributionDetail extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_VMR_DistributionDetail_ID id
    @param trx transaction
    */
    public X_XX_VMR_DistributionDetail (Ctx ctx, int XX_VMR_DistributionDetail_ID, Trx trx)
    {
        super (ctx, XX_VMR_DistributionDetail_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_VMR_DistributionDetail_ID == 0)
        {
            setXX_DistributionApplied (false);
            setXX_VMR_DistributionDetail_ID (0);
            setXX_VMR_DistributionHeader_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_VMR_DistributionDetail (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27614401268789L;
    /** Last Updated Timestamp 2012-03-20 16:09:12.0 */
    public static final long updatedMS = 1332275952000L;
    /** AD_Table_ID=1000206 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_VMR_DistributionDetail");
        
    }
    ;
    
    /** TableName=XX_VMR_DistributionDetail */
    public static final String Table_Name="XX_VMR_DistributionDetail";
    
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
    
    /** Set Campaign.
    @param C_Campaign_ID Marketing Campaign */
    public void setC_Campaign_ID (int C_Campaign_ID)
    {
        if (C_Campaign_ID <= 0) set_Value ("C_Campaign_ID", null);
        else
        set_Value ("C_Campaign_ID", Integer.valueOf(C_Campaign_ID));
        
    }
    
    /** Get Campaign.
    @return Marketing Campaign */
    public int getC_Campaign_ID() 
    {
        return get_ValueAsInt("C_Campaign_ID");
        
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
    
    /** Set Product.
    @param M_Product_ID Product, Service, Item */
    public void setM_Product_ID (int M_Product_ID)
    {
        if (M_Product_ID <= 0) set_Value ("M_Product_ID", null);
        else
        set_Value ("M_Product_ID", Integer.valueOf(M_Product_ID));
        
    }
    
    /** Get Product.
    @return Product, Service, Item */
    public int getM_Product_ID() 
    {
        return get_ValueAsInt("M_Product_ID");
        
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
    
    /** Set XX_BudgetDistrib.
    @param XX_BudgetDistrib XX_BudgetDistrib */
    public void setXX_BudgetDistrib (String XX_BudgetDistrib)
    {
        set_Value ("XX_BudgetDistrib", XX_BudgetDistrib);
        
    }
    
    /** Get XX_BudgetDistrib.
    @return XX_BudgetDistrib */
    public String getXX_BudgetDistrib() 
    {
        return (String)get_Value("XX_BudgetDistrib");
        
    }
    
    /** Set XX_BudgetSalesDistrib.
    @param XX_BudgetSalesDistrib XX_BudgetSalesDistrib */
    public void setXX_BudgetSalesDistrib (String XX_BudgetSalesDistrib)
    {
        set_Value ("XX_BudgetSalesDistrib", XX_BudgetSalesDistrib);
        
    }
    
    /** Get XX_BudgetSalesDistrib.
    @return XX_BudgetSalesDistrib */
    public String getXX_BudgetSalesDistrib() 
    {
        return (String)get_Value("XX_BudgetSalesDistrib");
        
    }
    
    /** Set XX_ByPiecesDistrib.
    @param XX_ByPiecesDistrib XX_ByPiecesDistrib */
    public void setXX_ByPiecesDistrib (String XX_ByPiecesDistrib)
    {
        set_Value ("XX_ByPiecesDistrib", XX_ByPiecesDistrib);
        
    }
    
    /** Get XX_ByPiecesDistrib.
    @return XX_ByPiecesDistrib */
    public String getXX_ByPiecesDistrib() 
    {
        return (String)get_Value("XX_ByPiecesDistrib");
        
    }
    
    /** Set XX_CalculatedPER.
    @param XX_CalculatedPER XX_CalculatedPER */
    public void setXX_CalculatedPER (boolean XX_CalculatedPER)
    {
        set_Value ("XX_CalculatedPER", Boolean.valueOf(XX_CalculatedPER));
        
    }
    
    /** Get XX_CalculatedPER.
    @return XX_CalculatedPER */
    public boolean isXX_CalculatedPER() 
    {
        return get_ValueAsBoolean("XX_CalculatedPER");
        
    }
    
    /** Set XX_CalculatedQTY.
    @param XX_CalculatedQTY XX_CalculatedQTY */
    public void setXX_CalculatedQTY (boolean XX_CalculatedQTY)
    {
        set_Value ("XX_CalculatedQTY", Boolean.valueOf(XX_CalculatedQTY));
        
    }
    
    /** Get XX_CalculatedQTY.
    @return XX_CalculatedQTY */
    public boolean isXX_CalculatedQTY() 
    {
        return get_ValueAsBoolean("XX_CalculatedQTY");
        
    }
    
    /** Set XX_DesiredQuantity.
    @param XX_DesiredQuantity XX_DesiredQuantity */
    public void setXX_DesiredQuantity (int XX_DesiredQuantity)
    {
        set_Value ("XX_DesiredQuantity", Integer.valueOf(XX_DesiredQuantity));
        
    }
    
    /** Get XX_DesiredQuantity.
    @return XX_DesiredQuantity */
    public int getXX_DesiredQuantity() 
    {
        return get_ValueAsInt("XX_DesiredQuantity");
        
    }
    
    /** Set Distribute Detail.
    @param XX_DistributeDetail Distribuye Detalle según el tipo de distribución seleccionada */
    public void setXX_DistributeDetail (String XX_DistributeDetail)
    {
        set_Value ("XX_DistributeDetail", XX_DistributeDetail);
        
    }
    
    /** Get Distribute Detail.
    @return Distribuye Detalle según el tipo de distribución seleccionada */
    public String getXX_DistributeDetail() 
    {
        return (String)get_Value("XX_DistributeDetail");
        
    }
    
    /** Set Distributed Quantity (Gift Included).
    @param XX_DistributedQTY Distributed Quantity (Gift Included) */
    public void setXX_DistributedQTY (int XX_DistributedQTY)
    {
        throw new IllegalArgumentException ("XX_DistributedQTY is virtual column");
        
    }
    
    /** Get Distributed Quantity (Gift Included).
    @return Distributed Quantity (Gift Included) */
    public int getXX_DistributedQTY() 
    {
        return get_ValueAsInt("XX_DistributedQTY");
        
    }
    
    /** Set XX_DistributionApplied.
    @param XX_DistributionApplied XX_DistributionApplied */
    public void setXX_DistributionApplied (boolean XX_DistributionApplied)
    {
        set_Value ("XX_DistributionApplied", Boolean.valueOf(XX_DistributionApplied));
        
    }
    
    /** Get XX_DistributionApplied.
    @return XX_DistributionApplied */
    public boolean isXX_DistributionApplied() 
    {
        return get_ValueAsBoolean("XX_DistributionApplied");
        
    }
    
    /** Set XX_EndDate.
    @param XX_EndDate XX_EndDate */
    public void setXX_EndDate (int XX_EndDate)
    {
        set_Value ("XX_EndDate", Integer.valueOf(XX_EndDate));
        
    }
    
    /** Get XX_EndDate.
    @return XX_EndDate */
    public int getXX_EndDate() 
    {
        return get_ValueAsInt("XX_EndDate");
        
    }
    
    /** Set XX_InitDate.
    @param XX_InitDate XX_InitDate */
    public void setXX_InitDate (int XX_InitDate)
    {
        set_Value ("XX_InitDate", Integer.valueOf(XX_InitDate));
        
    }
    
    /** Get XX_InitDate.
    @return XX_InitDate */
    public int getXX_InitDate() 
    {
        return get_ValueAsInt("XX_InitDate");
        
    }
    
    /** Set XX_ManualDistrib.
    @param XX_ManualDistrib XX_ManualDistrib */
    public void setXX_ManualDistrib (String XX_ManualDistrib)
    {
        set_Value ("XX_ManualDistrib", XX_ManualDistrib);
        
    }
    
    /** Get XX_ManualDistrib.
    @return XX_ManualDistrib */
    public String getXX_ManualDistrib() 
    {
        return (String)get_Value("XX_ManualDistrib");
        
    }
    
    /** Set XX_PercentDistrib.
    @param XX_PercentDistrib XX_PercentDistrib */
    public void setXX_PercentDistrib (String XX_PercentDistrib)
    {
        set_Value ("XX_PercentDistrib", XX_PercentDistrib);
        
    }
    
    /** Get XX_PercentDistrib.
    @return XX_PercentDistrib */
    public String getXX_PercentDistrib() 
    {
        return (String)get_Value("XX_PercentDistrib");
        
    }
    
    /** Set XX_SalesDistrib.
    @param XX_SalesDistrib XX_SalesDistrib */
    public void setXX_SalesDistrib (String XX_SalesDistrib)
    {
        set_Value ("XX_SalesDistrib", XX_SalesDistrib);
        
    }
    
    /** Get XX_SalesDistrib.
    @return XX_SalesDistrib */
    public String getXX_SalesDistrib() 
    {
        return (String)get_Value("XX_SalesDistrib");
        
    }
    
    /** Set Show Percentages .
    @param XX_ShowPercentages Show Percentages  */
    public void setXX_ShowPercentages (String XX_ShowPercentages)
    {
        set_Value ("XX_ShowPercentages", XX_ShowPercentages);
        
    }
    
    /** Get Show Percentages .
    @return Show Percentages  */
    public String getXX_ShowPercentages() 
    {
        return (String)get_Value("XX_ShowPercentages");
        
    }
    
    /** Set XX_StockQuantity.
    @param XX_StockQuantity XX_StockQuantity */
    public void setXX_StockQuantity (int XX_StockQuantity)
    {
        set_Value ("XX_StockQuantity", Integer.valueOf(XX_StockQuantity));
        
    }
    
    /** Get XX_StockQuantity.
    @return XX_StockQuantity */
    public int getXX_StockQuantity() 
    {
        return get_ValueAsInt("XX_StockQuantity");
        
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
    
    /** Set Collection.
    @param XX_VMR_Collection_ID ID de Colección */
    public void setXX_VMR_Collection_ID (int XX_VMR_Collection_ID)
    {
        if (XX_VMR_Collection_ID <= 0) set_Value ("XX_VMR_Collection_ID", null);
        else
        set_Value ("XX_VMR_Collection_ID", Integer.valueOf(XX_VMR_Collection_ID));
        
    }
    
    /** Get Collection.
    @return ID de Colección */
    public int getXX_VMR_Collection_ID() 
    {
        return get_ValueAsInt("XX_VMR_Collection_ID");
        
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
    
    /** Set XX_VMR_DistributionDetail_ID.
    @param XX_VMR_DistributionDetail_ID Distribution Detail */
    public void setXX_VMR_DistributionDetail_ID (int XX_VMR_DistributionDetail_ID)
    {
        if (XX_VMR_DistributionDetail_ID < 1) throw new IllegalArgumentException ("XX_VMR_DistributionDetail_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DistributionDetail_ID", Integer.valueOf(XX_VMR_DistributionDetail_ID));
        
    }
    
    /** Get XX_VMR_DistributionDetail_ID.
    @return Distribution Detail */
    public int getXX_VMR_DistributionDetail_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionDetail_ID");
        
    }
    
    /** Set DistributionHeader.
    @param XX_VMR_DistributionHeader_ID DistributionHeader */
    public void setXX_VMR_DistributionHeader_ID (int XX_VMR_DistributionHeader_ID)
    {
        if (XX_VMR_DistributionHeader_ID < 1) throw new IllegalArgumentException ("XX_VMR_DistributionHeader_ID is mandatory.");
        set_ValueNoCheck ("XX_VMR_DistributionHeader_ID", Integer.valueOf(XX_VMR_DistributionHeader_ID));
        
    }
    
    /** Get DistributionHeader.
    @return DistributionHeader */
    public int getXX_VMR_DistributionHeader_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionHeader_ID");
        
    }
    
    /** Set Distribution Type.
    @param XX_VMR_DistributionType_ID Distribution Type */
    public void setXX_VMR_DistributionType_ID (int XX_VMR_DistributionType_ID)
    {
        if (XX_VMR_DistributionType_ID <= 0) set_Value ("XX_VMR_DistributionType_ID", null);
        else
        set_Value ("XX_VMR_DistributionType_ID", Integer.valueOf(XX_VMR_DistributionType_ID));
        
    }
    
    /** Get Distribution Type.
    @return Distribution Type */
    public int getXX_VMR_DistributionType_ID() 
    {
        return get_ValueAsInt("XX_VMR_DistributionType_ID");
        
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
    
    /** Set Package.
    @param XX_VMR_Package_ID Package */
    public void setXX_VMR_Package_ID (int XX_VMR_Package_ID)
    {
        if (XX_VMR_Package_ID <= 0) set_Value ("XX_VMR_Package_ID", null);
        else
        set_Value ("XX_VMR_Package_ID", Integer.valueOf(XX_VMR_Package_ID));
        
    }
    
    /** Get Package.
    @return Package */
    public int getXX_VMR_Package_ID() 
    {
        return get_ValueAsInt("XX_VMR_Package_ID");
        
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
