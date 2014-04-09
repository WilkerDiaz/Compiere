package compiere.model.cds.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.compiere.model.X_M_AttributeInstance;
import org.compiere.model.X_M_AttributeSetInstance;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import compiere.model.cds.MProduct;
import compiere.model.cds.MVMRPOLineRefProv;
import compiere.model.cds.forms.XX_AssociateReferenceWith_Form;


public class XX_DoAllCombinations extends SvrProcess {

	Vector<Integer[]> zeroCombination = new Vector<Integer[]>();
	
	@Override
	protected String doIt() throws Exception {
		
		int LineRefProvID = Env.getCtx().getContextAsInt("#LineRefProvID_Aux");
		Env.getCtx().remove("#LineRefProvID_Aux");
		MVMRPOLineRefProv Line = new MVMRPOLineRefProv(Env.getCtx(), LineRefProvID, get_Trx());
		
		XX_AssociateReferenceWith_Form.changeStatusBarPerc(0);
		
		//El registro actual
		MProduct oldProduct = new MProduct(Env.getCtx(), getRecord_ID(), get_Trx());
		
	    Vector<Integer> ch1IDs = new Vector<Integer>();
	    Vector<Integer> ch2IDs = new Vector<Integer>();
	    
	    //Characteristic 1
	    if(Line.getXX_Characteristic1Value1_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value1_ID());
	    }
	    if(Line.getXX_Characteristic1Value2_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value2_ID());
	    }
	    if(Line.getXX_Characteristic1Value3_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value3_ID());
	    }
	    if(Line.getXX_Characteristic1Value4_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value4_ID());
	    }
	    if(Line.getXX_Characteristic1Value5_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value5_ID());
	    }
	    if(Line.getXX_Characteristic1Value6_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value6_ID());
	    }
	    if(Line.getXX_Characteristic1Value7_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value7_ID());
	    }
	    if(Line.getXX_Characteristic1Value8_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value8_ID());
	    }
	    if(Line.getXX_Characteristic1Value9_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value9_ID());
	    }
	    if(Line.getXX_Characteristic1Value10_ID()!=0){
	    	ch1IDs.add(Line.getXX_Characteristic1Value10_ID());
	    }
	    
	    //Characteristic 2
	    if(Line.getXX_Characteristic2_ID()!=0){
		    if(Line.getXX_Characteristic2Value1_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value1_ID());
		    }
		    if(Line.getXX_Characteristic2Value2_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value2_ID());
		    }
		    if(Line.getXX_Characteristic2Value3_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value3_ID());
		    }
		    if(Line.getXX_Characteristic2Value4_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value4_ID());
		    }
		    if(Line.getXX_Characteristic2Value5_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value5_ID());
		    }
		    if(Line.getXX_Characteristic2Value6_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value6_ID());
		    }
		    if(Line.getXX_Characteristic2Value7_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value7_ID());
		    }
		    if(Line.getXX_Characteristic2Value8_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value8_ID());
		    }
		    if(Line.getXX_Characteristic2Value9_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value9_ID());
		    }
		    if(Line.getXX_Characteristic2Value10_ID()!=0){
		    	ch2IDs.add(Line.getXX_Characteristic2Value10_ID());
		    }
	    }
	    
	    String value1Name = "";
	    String value2Name = "";
	    String SQL = "";
	    
	    int sizeAux = 1;
	    
	    if(ch2IDs.size()>0){
	    	sizeAux = ch2IDs.size();
	    }
	    
	    zeroCombination = getZeroCombinations(LineRefProvID);
	    int aux = 0;
	    
		for(int i=0; i<ch1IDs.size(); i++){
			
			for(int j=0; j<sizeAux; j++){
					
				if(ch2IDs.size()>0){
					if(compareZeros(ch1IDs.get(i),ch2IDs.get(j))){
						continue;
					}
				}
				else{
					if(compareZeros(ch1IDs.get(i),0)){
						continue;
					}
				}
				
				//Creo el M_AttributeSetInstance
				X_M_AttributeSetInstance newAttributeSetInstance = new X_M_AttributeSetInstance(Env.getCtx(),0,get_Trx());
				newAttributeSetInstance.setM_AttributeSet_ID(oldProduct.getM_AttributeSet_ID());
				
				SQL = "Select Name " +
							 "from M_AttributeValue " +
							 "where M_AttributeValue_ID="+ch1IDs.get(i);
				
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					
					pstmt = DB.prepareStatement(SQL, null);
					rs = pstmt.executeQuery();
					
					while(rs.next())
					{	
						value1Name = rs.getString("Name");
					}
				}catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.closeResultSet(rs);
					DB.closeStatement(pstmt);
				}
				
				if(Line.getXX_Characteristic2_ID()!=0){
					
					 SQL = "Select Name " +
					 "from M_AttributeValue " +
					 "where M_AttributeValue_ID="+ch2IDs.get(j);
		
					try {
						
						pstmt = DB.prepareStatement(SQL, null);
						rs = pstmt.executeQuery();
						
						while(rs.next())
						{	
							value2Name = rs.getString("Name");
						}
					}catch (SQLException e) {
						e.printStackTrace();
					} finally {
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}	
				}
				
				if(Line.getXX_Characteristic2_ID()!=0){
					newAttributeSetInstance.setDescription(value1Name+"_"+value2Name);
				}else{
					newAttributeSetInstance.setDescription(value1Name);
				}
				newAttributeSetInstance.save();
					
				X_M_AttributeInstance newAttributeInstance = new X_M_AttributeInstance(Env.getCtx(), 0, get_Trx());
				newAttributeInstance.setM_AttributeSetInstance_ID(newAttributeSetInstance.getM_AttributeSetInstance_ID());
				newAttributeInstance.setM_AttributeValue_ID(ch1IDs.get(i));
				newAttributeInstance.setValue(value1Name);
				newAttributeInstance.setM_Attribute_ID(Line.getXX_Characteristic1_ID());
				newAttributeInstance.save();
					
				if(Line.getXX_Characteristic2_ID()!=0){
					X_M_AttributeInstance newAttributeInstance2 = new X_M_AttributeInstance(Env.getCtx(), 0, get_Trx());
					newAttributeInstance2.setM_AttributeSetInstance_ID(newAttributeSetInstance.getM_AttributeSetInstance_ID());
					newAttributeInstance2.setM_AttributeValue_ID(ch2IDs.get(j));
					newAttributeInstance2.setValue(value2Name);
					newAttributeInstance2.setM_Attribute_ID(Line.getXX_Characteristic2_ID());
					newAttributeInstance2.save();
				}
				get_Trx().commit();
				
				//Los registros nuevos
				MProduct newProduct = new MProduct(Env.getCtx(), oldProduct, get_Trx());
				
				newProduct.setXX_VMR_Category_ID(oldProduct.getXX_VMR_Category_ID());
				//newProduct.setC_TaxCategory_ID(oldProduct.getC_TaxCategory_ID());
				newProduct.setM_AttributeSet_ID(oldProduct.getM_AttributeSet_ID());
				newProduct.setM_AttributeSetInstance_ID(newAttributeSetInstance.get_ID());
				newProduct.setXX_VMR_LongCharacteristic_ID(oldProduct.getXX_VMR_LongCharacteristic_ID());
				newProduct.setXX_VMR_UnitConversion_ID(oldProduct.getXX_VMR_UnitConversion_ID());
				newProduct.setXX_PiecesBySale_ID(oldProduct.getXX_PiecesBySale_ID());
				newProduct.setName(oldProduct.getName());
				newProduct.setDescription(oldProduct.getDescription());
				newProduct.setXX_VMR_Brand_ID(oldProduct.getXX_VMR_Brand_ID());
				newProduct.setXX_CodeTariff(oldProduct.getXX_CodeTariff());
				
				newProduct.setC_BPartner_ID(oldProduct.getC_BPartner_ID());
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

				newProduct.save();
				get_Trx().commit();
				
			}
			
			aux = ((i*100)/ch1IDs.size());
			XX_AssociateReferenceWith_Form.changeStatusBarPerc(aux);	
		}
		
		XX_AssociateReferenceWith_Form.changeStatusBar();
		
		return "";
	}
	
	private Vector<Integer[]> getZeroCombinations(int LineRefProvID){
		
		Vector<Integer[]> aux = new Vector<Integer[]>();
		
		
		String SQL = 
			"SELECT XX_VALUE1,XX_VALUE2 " +
			"FROM XX_VMR_ReferenceMATRIX " +
			"WHERE XX_VMR_PO_LINEREFPROV_ID="+LineRefProvID+" AND XX_QUANTITYV = 0";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while(rs.next())
			{	
				Integer[] auxArray = new Integer[2];
				auxArray[0] = rs.getInt("XX_VALUE1");
				auxArray[1] = rs.getInt("XX_VALUE2");
				aux.add(auxArray);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return aux;
	}
	
	private boolean compareZeros(int value1, int value2){
		
		for(int i=0; i < zeroCombination.size(); i++){
			
			if(zeroCombination.get(i)[0]==value1 && zeroCombination.get(i)[1]==value2){
				return true;
			}
		}
		
		return false;
	}
	

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

}
