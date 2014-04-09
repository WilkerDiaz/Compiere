package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;

import org.compiere.model.X_C_BPartner;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.X_XX_VLO_NewsReport;

public class XX_ConvertProductNR extends SvrProcess {

	@Override
	protected void prepare() {

	}

	@Override
	protected String doIt() {
		
		X_XX_VLO_NewsReport newsReport = new X_XX_VLO_NewsReport( Env.getCtx(), getRecord_ID(), null);
		
		Integer oldproductID = newsReport.get_ValueAsInt("XX_AuxProduct_ID");
		
		if(oldproductID!=null)
		{
			MProduct oldProduct = new MProduct( Env.getCtx(), oldproductID, null);
			
			//Producto Nuevo
			MProduct newProduct = new MProduct( Env.getCtx(), 0, null);

			newProduct.setXX_VMR_Category_ID(oldProduct.getXX_VMR_Category_ID());
			newProduct.set_ValueNoCheck("XX_VMR_Department_ID", oldProduct.getXX_VMR_Department_ID());
			newProduct.set_ValueNoCheck("XX_VMR_Line_ID", oldProduct.getXX_VMR_Line_ID());
			newProduct.set_ValueNoCheck("XX_VMR_Section_ID", oldProduct.getXX_VMR_Section_ID());
			newProduct.setXX_VMR_ProductClass_ID(oldProduct.getXX_VMR_ProductClass_ID());
			newProduct.set_ValueNoCheck("XX_VMR_TypeInventory_ID", oldProduct.getXX_VMR_TypeInventory_ID());
			newProduct.setAD_Org_ID( oldProduct.getAD_Org_ID());
			newProduct.setM_Locator_ID(oldProduct.getM_Locator_ID());
			newProduct.set_ValueNoCheck("XX_VME_ConceptValue_ID", oldProduct.getXX_VME_ConceptValue_ID());
			newProduct.setM_AttributeSet_ID(oldProduct.getM_AttributeSet_ID());
			newProduct.setM_AttributeSetInstance_ID(oldProduct.getM_AttributeSetInstance_ID());
			newProduct.setXX_VMR_LongCharacteristic_ID(oldProduct.getXX_VMR_LongCharacteristic_ID());
			newProduct.setXX_VMR_UnitConversion_ID(oldProduct.getXX_VMR_UnitConversion_ID());
			newProduct.setXX_PiecesBySale_ID(oldProduct.getXX_PiecesBySale_ID());
			newProduct.setName(oldProduct.getName());
			newProduct.setDescription(oldProduct.getDescription());
			newProduct.setXX_VMR_Brand_ID(oldProduct.getXX_VMR_Brand_ID());
			newProduct.setXX_CodeTariff(oldProduct.getXX_CodeTariff());
			
			//Nuevo Proveedor
			int vendorID = Env.getCtx().getContextAsInt("#XX_L_NEWSREPORTVENDOR_ID");
			int reference = 0;
			
			newProduct.setC_BPartner_ID(vendorID); 
			
			//Referencia de Proveedor
			MVMRVendorProdRef oldReference = new MVMRVendorProdRef( Env.getCtx(), oldProduct.getXX_VMR_VendorProdRef_ID(), null);
			MVMRVendorProdRef newReference = new MVMRVendorProdRef( Env.getCtx(), 0, null);
			newReference.setC_BPartner_ID(vendorID);
			newReference.setXX_EnglishDescription( oldReference.getXX_EnglishDescription());
			newReference.setValue(oldReference.getValue());
			newReference.setDescription(oldReference.getDescription());
			newReference.setName(oldReference.getName());
			newReference.setXX_VMR_Department_ID(oldReference.getXX_VMR_Department_ID());
			newReference.setXX_VMR_Line_ID(oldReference.getXX_VMR_Line_ID());
			newReference.setXX_VMR_Section_ID(oldReference.getXX_VMR_Section_ID());
			newReference.setXX_VMR_LongCharacteristic_ID(oldReference.getXX_VMR_LongCharacteristic_ID());
			newReference.setXX_VMR_Brand_ID(oldReference.getXX_VMR_Brand_ID());
			newReference.setC_TaxCategory_ID(oldReference.getC_TaxCategory_ID());
			newReference.setXX_VMR_UnitConversion_ID(oldReference.getXX_VMR_UnitConversion_ID());
			newReference.setXX_VMR_UnitPurchase_ID(oldReference.getXX_VMR_UnitPurchase_ID());
			newReference.setXX_PackageMultiple(oldReference.getXX_PackageMultiple());
			newReference.setXX_PiecesBySale_ID(oldReference.getXX_PiecesBySale_ID());
			newReference.setXX_SaleUnit_ID(oldReference.getXX_SaleUnit_ID());
			if(oldProduct.getM_AttributeSetInstance_ID()>0)
				newReference.setM_AttributeSet_ID(oldProduct.getM_AttributeSetInstance_ID());
			
			boolean aux = false;
			try{
				aux = newReference.save();
			}
			catch (Exception e) {
				
				//Ya existe una referencia igual, busco cual es;
				String sqlRef = "select XX_VMR_VendorProdRef_ID " +
								"from XX_VMR_VendorProdRef where " +
								"C_BPartner_ID = " + vendorID +
								" AND XX_VMR_Department_ID = " + oldReference.getXX_VMR_Department_ID() +
								" AND XX_VMR_Line_ID = " + oldReference.getXX_VMR_Line_ID() +
								" AND XX_VMR_Section_ID = " + oldReference.getXX_VMR_Section_ID() +
								" AND VALUE = '" + oldReference.getValue() + "'";
				
				ResultSet rs = null;
				PreparedStatement prst = null;
				
				try {
				
					prst = DB.prepareStatement(sqlRef, null);
					rs = prst.executeQuery();
					
					int i = 0;
					while(rs.next()){
						
						reference = rs.getInt("XX_VMR_VendorProdRef_ID");
					}
					
					if(i>1)
						return "Hay mas de una referencia para este producto con el proveedor SAN PANTALEON";
					
				}catch (Exception ee) {
					log.log(Level.SEVERE, ee.getMessage());
					return "BD error";
				}
				finally
				{
					DB.closeResultSet(rs);
					DB.closeStatement(prst);
				}
				
				newReference = new MVMRVendorProdRef( Env.getCtx(), reference, null);
			}
			
			if(aux || reference!=0){
				
				newProduct.set_ValueNoCheck("XX_VMR_VendorProdRef_ID", newReference.get_ID());
			
				newProduct.setXX_SaleUnit_ID(oldProduct.getXX_SaleUnit_ID());
				newProduct.setXX_VMR_UnitPurchase_ID(oldProduct.getXX_VMR_UnitPurchase_ID());
				newProduct.setXX_VMR_TypeExhibition_ID(oldProduct.getXX_VMR_TypeExhibition_ID());
				newProduct.setXX_VMR_TypeLabel_ID(oldProduct.getXX_VMR_TypeLabel_ID());
				newProduct.setXX_PercentageTariff(oldProduct.getXX_PercentageTariff());
				newProduct.setVolume(oldProduct.getVolume());
				newProduct.setWeight(oldProduct.getWeight());
				newProduct.setC_Country_ID(oldProduct.getC_Country_ID());
				newProduct.setIsActive(oldProduct.isActive());
				newProduct.setHelp(oldProduct.getHelp());
				newProduct.setC_UOM_ID(oldProduct.getC_UOM_ID());
				newProduct.setM_Product_Category_ID(oldProduct.getM_Product_Category_ID());
	
				if(newProduct.save()){
					newsReport.setM_Product_ID(newProduct.get_ID());
					newsReport.set_Value("XX_ConvertProduct", 'Y');
					
					if(newsReport.save())
						return "Producto Convertido";
				}else
				{
					String sqlProduct = "select value from m_product " +
										"where XX_VMR_VendorProdRef_ID = " + newReference.get_ID() +
										" AND M_AttributeSetInstance_ID = " + oldProduct.getM_AttributeSetInstance_ID();
					
					String productValue = "";
					
					ResultSet rs = null;
					PreparedStatement prst = null;
					
					try {
					
						prst = DB.prepareStatement(sqlProduct, null);
						rs = prst.executeQuery();
					
						while(rs.next()){
							
							productValue = rs.getString("value");
						}
						
					}catch (Exception ee) {
						log.log(Level.SEVERE, ee.getMessage());
						return "BD error";
					}
					finally
					{
						DB.closeResultSet(rs);
						DB.closeStatement(prst);
					}
					
					return "Ya existe un producto con esas caracteristicas: " + productValue;
				}
			}
		}
	
		return "";
	}
}
