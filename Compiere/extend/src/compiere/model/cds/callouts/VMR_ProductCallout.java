package compiere.model.cds.callouts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.X_Ref_M_Product_ProductType;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MLocation;
import compiere.model.cds.X_XX_VMR_Section;
import compiere.model.cds.X_XX_VMR_VendorProdRef;

public class VMR_ProductCallout extends CalloutEngine {
	
	public String ShowName (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		Integer section = (Integer) mField.getValue();
		GridField tipo= mTab.getField("ProductType");
		if(section != null){ 
			try 
			{
				// Realizado por Wdiaz
				// Modificado por Maria Vintimilla Funcion 004
				if (tipo.getValue().equals(X_Ref_M_Product_ProductType.ITEM.getValue())){
					X_XX_VMR_Section aux = new X_XX_VMR_Section(ctx, section, null);
					mTab.setValue("name", aux.getName());
			    }//Fin maria
			}//try
			catch(Exception e)
			{	
				return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
			}//catch
		}//if
		
		return "";
	}//section code id
	
	/**
	 * Inicio Victor Lo Monaco
	 * Setea la categoria al escoger el departamento
	 */
	public String onSelectDepartment (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{

		Integer department = (Integer)mTab.getValue("XX_VMR_Department_ID");		
		Integer category = 0;

		String SQL = "SELECT XX_VMR_CATEGORY_ID FROM XX_VMR_Department WHERE XX_VMR_Department_ID=" + department;

		try {
			PreparedStatement pstmt = DB.prepareStatement(SQL, null);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				category = rs.getInt("XX_VMR_CATEGORY_ID");
			} 
			rs.close();
			pstmt.close();
		}catch (Exception e) {
				System.out.println(e + " " + SQL);
		}

		mTab.setValue("XX_VMR_Category_ID", category);
		
		return "";
	}//section code id
	/**
	 * Fin Victor Lo Monaco
	 */

	
	public String ShowTypeInventory (Ctx ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		try{ 
			//Integer Linea = (Integer)mTab.getValue("XX_LineCode_ID");
			Integer Linea = (Integer)mField.getValue();

			if (Linea != null)
			{
				String sql = "select XX_VMR_TypeInventory_ID as id from XX_VMR_LINE where XX_VMR_LINE_ID = "+Linea+" and ISACTIVE = 'Y'";
				PreparedStatement priceRulePstmt = DB.prepareStatement(sql, null);
				ResultSet rs = priceRulePstmt.executeQuery();
				if (rs.next())
				{
					int Clave = rs.getInt("id");
					mTab.setValue("XX_VMR_TypeInventory_ID", Clave);
					
				}else
					{
					    mTab.setValue("XX_VMR_TypeInventory_ID", null);
					    return  Msg.getMsg(ctx, "XX_InventoryTypeLine");
					}
				rs.close();
				priceRulePstmt.close();
			}
		
		}catch (Exception e) {
			return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
		}
	   return "";
	}
	
	/*public String ShowVendor(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
			try{
					Integer Referencia_ID =  Integer.parseInt(ctx.getContext(WindowNo,"XX_VendorProdRef_ID"));
					System.out.println("El Valor:"+mField.getValue());
					if ((Referencia_ID!=null))
					{
						X_XX_VMR_VendorProdRef aux = new X_XX_VMR_VendorProdRef(ctx, Referencia_ID, null);
						System.out.println("VENDEDOR ID:"+ aux.getXX_VENDOR_ID());
						//MBPartner Vendedor = new MBPartner(ctx, aux.getXX_VENDOR_ID(), null);
						//System.out.println("VENDEDOR ID:"+ Vendedor.getC_BPartner_ID());
						//mField.setValue("1000087", true);
						
						//mTab.setValue("C_BPartner_ID", aux.getXX_VENDOR_ID());
						MProductPO Producto = new MProductPO(ctx, 0, null);
						//System.out.println("A ver:"+Producto.getC_BPartner_ID());
						Producto.setC_BPartner_ID(aux.getXX_VENDOR_ID());
						
						//ctx.setContext(WindowNo, "C_BPARTNER_ID", Vendedor.getC_BPartner_ID());
					
					}
				
				
			}catch (Exception e) {
				return "Error LLamar a Soporte";
			}
		return "";
	}*/
	// Funcion que obtiene los datos necesarios de referencia de proveedor para llenar producto dependiendo
	// referencia que seleccione el usuario. wdiaz
	public String ShowRefProduct(Ctx ctx, int WindowNo,GridTab mTab, GridField mField, Object value, Object oldValue)
	{
		try{
			   PreparedStatement Pstmt = null;
			   ResultSet rs = null;
			   //Env.getCtx().remove("#XX_ProductClass");
			   //Env.getCtx().remove("#XX_VMR_TypeLabel");
			   //Env.getCtx().remove("##XX_VMR_TypeExhibition");
				//String ProductoReferencia = null;
				String sql = null;
			    Integer Referencia = (Integer)mField.getValue();
				// Modificado por Maria Vintimilla Funcion 004
				GridField tipo= mTab.getField("ProductType");
			    
				if ((Referencia!=null)&&(mTab.getAD_Tab_ID() == 180))
				{
					X_XX_VMR_VendorProdRef aux = new X_XX_VMR_VendorProdRef(ctx, Referencia, null);

					// Se buscará si por la referencia para producto ya existe
					sql = "select * from M_Product where XX_VMR_DEPARTMENT_ID = "+aux.getXX_VMR_Department_ID()+" and " +
						  "XX_VMR_LINE_ID = "+aux.getXX_VMR_Line_ID()+" and XX_VMR_SECTION_ID = "+aux.getXX_VMR_Section_ID()+" and " +
						  "XX_VMR_VendorProdRef_id = "+aux.getXX_VMR_VendorProdRef_ID()+" order by M_Product_ID desc";
					Pstmt = DB.prepareStatement(sql, null);
				    rs = Pstmt.executeQuery();
				    
				    if (rs.next())
					{
				    	mTab.setValue("XX_VMR_ProductClass_ID", rs.getInt("XX_VMR_ProductClass_ID"));
				    	mTab.setValue("XX_VMR_TypeLabel_ID", rs.getInt("XX_VMR_TypeLabel_ID"));
				    	mTab.setValue("XX_VMR_TypeExhibition_ID", rs.getInt("XX_VMR_TypeExhibition_ID"));
				    	
				    	//mTab.setValue("XX_Void", true);
				    }
				     else{
				    	 mTab.setValue("XX_VMR_ProductClass_ID", aux.getXX_VMR_ProductClass_ID());
					     mTab.setValue("XX_VMR_TypeLabel_ID",  aux.getXX_VMR_TypeLabel_ID());
					     mTab.setValue("XX_VMR_TypeExhibition_ID", null);
					    // mTab.setValue("XX_Void", false);
				     }
				        // mTab.setValue("XX_VMR_TypeExhibition_ID", null);
						// Env.getCtx().remove("#XX_FigureVendor");
				    	
				    	if(aux.getXX_VMR_LongCharacteristic_ID()==0){
				    		mTab.setValue("XX_VMR_LongCharacteristic_ID", null);		
				    	}else{
				    		mTab.setValue("XX_VMR_LongCharacteristic_ID", aux.getXX_VMR_LongCharacteristic_ID());	
				    	}
				    	
						if(aux.getM_AttributeSet_ID() > 0)
						   mTab.setValue("M_AttributeSet_ID", aux.getM_AttributeSet_ID());
						else
						   mTab.setValue("M_AttributeSet_ID", null);
						mTab.setValue("C_TaxCategory_ID", aux.getC_TaxCategory_ID());
						mTab.setValue("XX_SaleUnit_ID", aux.getXX_SaleUnit_ID());
						mTab.setValue("XX_PiecesBySale_ID", aux.getXX_PiecesBySale_ID());
						
						// Modificado por Maria Vintimilla Funcion 004
						if (tipo.getValue().equals(X_Ref_M_Product_ProductType.ITEM.getValue())){
							mTab.setValue("XX_VMR_Brand_ID", aux.getXX_VMR_Brand_ID());
							mTab.setValue("XX_VMR_UnitPurchase_ID", aux.getXX_VMR_UnitPurchase_ID());
							mTab.setValue("XX_VMR_UnitConversion_ID", aux.getXX_VMR_UnitConversion_ID());
					    }//Fin maria
						
						mTab.setValue("XX_VME_ConceptValue_ID", aux.getXX_VME_ConceptValue_ID());
						mTab.setValue("C_BPartner_ID", aux.getC_BPartner_ID());
						//Colocar el pais del Business Partner en el City
							Pstmt.close();
							rs.close();
							MBPartner proveedor = new MBPartner(ctx, aux.getC_BPartner_ID(), null);
						    sql = "select c_location_id from c_bpartner_location where c_bpartner_id = "+proveedor.getC_BPartner_ID()+"";
						    Pstmt = DB.prepareStatement(sql, null);
						    rs = Pstmt.executeQuery();
							if (rs.next())
							{
								int Clave = rs.getInt("c_location_id");
								MLocation direccion = new MLocation(ctx, Clave, null);
								mTab.setValue("C_Country_ID", direccion.getC_Country_ID());
								//Env.getCtx().setContext( "#XX_FigureVendor", proveedor.getXX_FigureVendor());
							}
					//	 }
				    Pstmt.close();
					rs.close();
				}
			}catch (Exception e) {
				return Msg.getMsg(ctx, "XX_Support") + e.getMessage();
			}
		return "";
	}
}
