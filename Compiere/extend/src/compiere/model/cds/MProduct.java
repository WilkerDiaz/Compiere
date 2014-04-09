package compiere.model.cds;
 
import java.awt.Window;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.ProcessCtl;
import org.compiere.apps.form.FormFrame;
import org.compiere.model.MBPartnerProduct;
import org.compiere.model.MMovement;
import org.compiere.model.MMovementLine;
import org.compiere.model.MPInstance;
import org.compiere.model.MProductPO;
import org.compiere.model.MProductPrice;
import org.compiere.model.X_Ref_M_Product_ProductType;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.forms.XX_AssociateReferenceWith_Form;
import compiere.model.cds.forms.XX_AssociateReference_Form;
import compiere.model.cds.forms.XX_UnsolicitedProductIncorrect_Form;


public class MProduct extends org.compiere.model.MProduct {

	static X_XX_VMR_PO_LineRefProv LineRefProv;
	static MVLOUnsolicitedProduct unsolProd;
	static Ctx ctx_aux;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeUse.class);	

	public MProduct(Ctx ctx, int M_Product_ID, Trx trx) {
		super(ctx, M_Product_ID, trx);
	}
	
	public MProduct(Ctx ctx, MProduct oldProduct, Trx trx) {
		super(ctx, 0, trx);
		set_ValueNoCheck("XX_VMR_Department_ID", oldProduct.getXX_VMR_Department_ID());
		set_ValueNoCheck("XX_VMR_Line_ID", oldProduct.getXX_VMR_Line_ID());
		set_ValueNoCheck("XX_VMR_Section_ID", oldProduct.getXX_VMR_Section_ID());
		set_ValueNoCheck("XX_VMR_VendorProdRef_ID", oldProduct.getXX_VMR_VendorProdRef_ID());
		
		set_ValueNoCheck("C_TaxCategory_ID", oldProduct.getC_TaxCategory_ID());
		set_ValueNoCheck("XX_VME_ConceptValue_ID", oldProduct.getXX_VME_ConceptValue_ID());
		set_ValueNoCheck("XX_VMR_ProductClass_ID", oldProduct.getXX_VMR_ProductClass_ID());
		set_ValueNoCheck("XX_VMR_TypeInventory_ID",oldProduct.getXX_VMR_TypeInventory_ID());
	}
	
	public MProduct(Ctx ctx, int depart, int line, int section, int vendorProdRef, int taxCategory, int concept, int typeInventory, Trx trx) {
		super(ctx, 0, trx);
		set_ValueNoCheck("XX_VMR_Department_ID", depart);
		set_ValueNoCheck("XX_VMR_Line_ID", line);
		set_ValueNoCheck("XX_VMR_Section_ID", section);
		set_ValueNoCheck("XX_VMR_VendorProdRef_ID", vendorProdRef);
		
		set_ValueNoCheck("C_TaxCategory_ID",taxCategory);
		set_ValueNoCheck("XX_VME_ConceptValue_ID", concept);
		set_ValueNoCheck("XX_VMR_TypeInventory_ID",typeInventory);
	}
	
	/**
	 * 	Load constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MProduct (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MProduct

	
	/**
	 * 	Before Save
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		String SQLProductName = "";
		PreparedStatement pstmProductName = null;
		ResultSet rsProductName = null;
		
			boolean save=super.beforeSave(newRecord);
			
			if(Env.getCtx().getContext("#XX_L_COMESFROMPLACEDORDER").equalsIgnoreCase("Y")){			
				return save;
			}
			
			//logica
			if(save){
				
				/**** Maria vintimilla 
				 * Se valida que el nombre de producto no esté asociado a un producto
				 * ya existente ****/
				if(!getProductType().equals(X_Ref_M_Product_ProductType.ITEM.getValue())){
					if(newRecord) {
						SQLProductName = " SELECT * " +
								" FROM M_PRODUCT " +
								" WHERE XX_PRODUCTNAME_ID = "+getXX_ProductName_ID() +
								" AND AD_Client_ID = " + 
								Env.getCtx().getAD_Client_ID();
						//System.out.println(SQLProductName);
						try{
							pstmProductName = DB.prepareStatement(SQLProductName, null);
							rsProductName = pstmProductName.executeQuery();
							if (rsProductName.next()){
								log.saveError("Warning", "Nombre de producto debe ser único");
								return false;
							}
						} 
						catch(SQLException e){ e.getMessage(); 	}
						finally {
							DB.closeResultSet(rsProductName);
							DB.closeStatement(pstmProductName);
						}
					}	
				} // tipo de producto
				
				/****Jessica Mendoza****/
				/**
				 * 	Se valida que dependiendo del tipo de producto y/o tipo de bien,
				 * se modifica el tipo de categoria del producto
				 */
				if (getProductType().equals("S")){
					setM_Product_Category_ID(Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSERVICES_ID"));
					
					if(getXX_ProductName_ID()==0){
						log.saveError("Error", "Debe ingresar un nombre de producto");
						return false;
					}
				}
				if (getProductType().equals("I")){
					setM_Product_Category_ID(Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID"));
				}
				if (getProductType().equals("A")){
					if (getXX_AssetsType().equals("A")){
						setM_Product_Category_ID(Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTFASSET_ID"));
					}else{
						if (getXX_AssetsType().equals("S")){
							setM_Product_Category_ID(Env.getCtx().getContextAsInt("#XX_L_CATEPRODUCTSM_ID"));
						}
					}
				}
				/****Fin de código - Jessica Mendoza****/
				
				if(getM_AttributeSet_ID() == 0)
				{
					String sql = "select * from M_Product where " +
							     "XX_VMR_VENDORPRODREF_ID = " + getXX_VMR_VendorProdRef_ID()+
				                 " and M_ATTRIBUTESET_ID = "+Env.getCtx().getContextAsInt("XX_L_P_ATTRIBUTESETST_ID");
				           
							if(!newRecord)
						       sql+=" AND ISACTIVE='Y' and M_Product_ID <> "+getM_Product_ID();
							
					PreparedStatement priceRulePstmt = null;
					ResultSet rs = null;
					try{
						
						priceRulePstmt = DB.prepareStatement(sql, null);
						rs = priceRulePstmt.executeQuery();
						if (rs.next())
						{
						log.saveError("Warning", "XX_CreatedReference");
						return false;
						}
					}catch (Exception e) {
						e.printStackTrace();
						return false;
					}finally{
						
						try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
						try {priceRulePstmt.close();} catch (SQLException e) {e.printStackTrace();}
					}
					setM_AttributeSet_ID(Env.getCtx().getContextAsInt("XX_L_P_ATTRIBUTESETST_ID"));
				}
				else
					if(getM_AttributeSetInstance_ID()!=0){ //si hay una instancia de atributos
					
					Vector <Integer> chValues = new Vector<Integer>();
					String SQL = "Select M_ATTRIBUTEVALUE_ID " +
							"from M_ATTRIBUTEINSTANCE where " +
							"M_ATTRIBUTESETINSTANCE_ID ="+getM_AttributeSetInstance_ID();
					
					try {
						
						PreparedStatement pstmt = DB.prepareStatement(SQL, null);
						ResultSet rs;
						rs = pstmt.executeQuery();
						
						while(rs.next())
						{	
							chValues.add(rs.getInt("M_ATTRIBUTEVALUE_ID"));
						}
						
						rs.close();
						pstmt.close();
					}catch (SQLException e) {
						e.printStackTrace();
						return false;
					}
					
					//una vez que tengo los valores de los atributos los comparo con los de la BD
					 
					SQL="Select M_Product_ID from M_Product "+
					    "where ";
					    
					    for(int i=0;i<chValues.size();i++){
				        	
				        	if(i>0){
				        		SQL+=" and ";
				        	}
				        	
				        	SQL+="M_ATTRIBUTESETINSTANCE_ID IN "+
				        		 "(Select M_ATTRIBUTESETINSTANCE_ID "+
				        		 "from M_ATTRIBUTEINSTANCE where " +
				        		 "M_ATTRIBUTESETINSTANCE_ID IN "+
				                 "(Select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTEINSTANCE where M_ATTRIBUTEVALUE_ID = "+chValues.get(i)+") " +
				                		"group by  M_ATTRIBUTESETINSTANCE_ID having count(*)="+chValues.size()+")";
				        }
					
						if (chValues.size()>0)
					    	SQL+=" AND ISACTIVE='Y' and C_TaxCategory_ID = "+getC_TaxCategory_ID()+" and XX_VMR_VendorProdRef_ID = "+getXX_VMR_VendorProdRef_ID();
	    
					    
						PreparedStatement pstmt = null;
						ResultSet rs = null;
					try {
						pstmt = DB.prepareStatement(SQL, null);
						rs = pstmt.executeQuery();
						
						while(rs.next())
						{	
							if(rs.getInt("M_Product_ID")!=getM_Product_ID()){
								save=false; //si hubo resultados
							}
						}
						
					}catch (SQLException e) {
						e.printStackTrace();
						return false;
					} finally {
						DB.closeResultSet(rs);
						DB.closeStatement(pstmt);
					}
					
					if(save==false){
						log.saveError("Error",Msg.translate(Env.getCtx(), "CombinationAlreadyExists"));
						return save;
					}
				}
			}
			return save;
	}
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	
		boolean save = super.afterSave(newRecord, success);
		PreparedStatement pstmProductName = null; 
		ResultSet rsProductName = null;
		
		if(getCtx().getContext("#XX_L_COMESFROMPLACEDORDER").equalsIgnoreCase("Y")){
			Env.getCtx().remove("#XX_L_COMESFROMPLACEDORDER");
			return save;
		}
		
		if(save){
			if(!newRecord && is_ValueChanged("XX_ProductName_ID()")){
				
				/**** Maria vintimilla 
				 * Se valida que el nombre de producto no esté asociado a un producto
				 * ya existente ****/
				if(!getProductType().equals(X_Ref_M_Product_ProductType.ITEM.getValue())){
					if(newRecord) {
						String SQLProductName = " SELECT * " +
								" FROM M_PRODUCT " +
								" WHERE XX_PRODUCTNAME_ID = "+getXX_ProductName_ID()+
								" AND AD_Client_ID = " + 
								Env.getCtx().getAD_Client_ID();
						//System.out.println(SQLProductName);
						try{
							pstmProductName = DB.prepareStatement(SQLProductName, null);
							rsProductName = pstmProductName.executeQuery();
							if (rsProductName.next()){
								log.saveError("Warning", "Nombre de producto debe ser único");
								return false;
							}
						} 
						catch(SQLException e){ e.getMessage(); 	}
						finally {
							DB.closeResultSet(rsProductName);
							DB.closeStatement(pstmProductName);
						}
					}
					
						
				} // tipo de producto
			}
			
			
			if(newRecord){
				
				//Combinatoria de productos segun caracteristicas dinamicas  
				//*** Realizado por JTrias
			    
				if(getXX_FromProcess()=='Y'){ //Si es sin caracteristicas
					//Asocio producto
					X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(ctx_aux,0,null);
					
					matrix.setXX_VALUE1(0);
					matrix.setXX_VALUE2(0);
					matrix.setXX_COLUMN(0);
					matrix.setXX_ROW(0);
					matrix.setXX_QUANTITYC(LineRefProv.getQty());
					matrix.setXX_QUANTITYV(LineRefProv.getSaleQty());
					matrix.setXX_QUANTITYO(LineRefProv.getXX_GiftsQty());
					matrix.setXX_VMR_PO_LineRefProv_ID((Integer)LineRefProv.get_Value("XX_VMR_PO_LineRefProv_ID"));
					matrix.setM_Product(get_ID());
					
					matrix.save();
					
				    Integer Referencia = getXX_VMR_VendorProdRef_ID();
				    X_XX_VMR_VendorProdRef proveedor = new X_XX_VMR_VendorProdRef(getCtx(), Referencia, null);
				    
				    String moneda_SQL = "Select C_Currency_ID " +
				    		"from C_ORDER " +
				    		"where C_Order_ID="+LineRefProv.getC_Order_ID();
				    
				    Integer moneda=0;
				    try{
						PreparedStatement pstmt = DB.prepareStatement(moneda_SQL, null);
						ResultSet rs = pstmt.executeQuery();
						
						if(rs.next()){
							moneda= rs.getInt("C_Currency_ID");
						}
						
						rs.close();
						pstmt.close();
					}
					catch (SQLException e){
						log.log(Level.SEVERE, moneda_SQL + e.getMessage());
					}
				    
				    MProductPO purchasing = new MProductPO(getCtx(),0,get_Trx());
//				    purchasing = new MProductPO(getCtx(), 0, null);
				    purchasing.setC_BPartner_ID(proveedor.getC_BPartner_ID());
				    purchasing.setM_Product_ID(getM_Product_ID());
				    purchasing.setC_Currency_ID(moneda);
				    purchasing.setIsActive(true);
				    purchasing.setIsCurrentVendor(true);
				    purchasing.setVendorProductNo(Referencia.toString());
				    purchasing.setPricePO(LineRefProv.getXX_UnitPurchasePrice());
				    purchasing.setPriceLastPO(LineRefProv.getXX_UnitPurchasePrice());
				    purchasing.setPriceList(LineRefProv.getXX_UnitPurchasePrice());
					purchasing.save();
					
					proveedor.setXX_IsAssociated(true);
					proveedor.save();
					
					Window[] List = AWindow.getWindows();
					
					for (int i = 0; i < List.length; i++) {
						
						if(List[i].getName().equals("Associate")){
							List[i].dispose();
						}	
						
						if(List[i].isFocused()){
							List[i].setVisible(false);
						}					
					}
	
				}// final del fromprocess
				
				
				if(getXX_FromProcess()=='W'){ //Si es con caracteristica
					
					getCtx().setContext("#LineRefProvID_Aux",LineRefProv.get_ID());
					
					//Asocio producto
					X_XX_VMR_ReferenceMatrix matrix = new X_XX_VMR_ReferenceMatrix(ctx_aux,XX_AssociateReferenceWith_Form.selectedReference_ID,null);
						
					matrix.setM_Product(get_ID());
					matrix.save();
				    
				    Integer Referencia = getXX_VMR_VendorProdRef_ID();
				    X_XX_VMR_VendorProdRef proveedor = new X_XX_VMR_VendorProdRef(getCtx(), Referencia, null);
				    
				    String moneda_SQL = "Select C_Currency_ID " +
		    		"from C_ORDER " +
		    		"where C_Order_ID="+LineRefProv.getC_Order_ID();
		    
				    Integer moneda=0;
				    try{
						PreparedStatement pstmt = DB.prepareStatement(moneda_SQL, null);
						ResultSet rs = pstmt.executeQuery();
						
						if(rs.next()){
							moneda= rs.getInt("C_Currency_ID");
						}
						
						rs.close();
						pstmt.close();
					}
					catch (SQLException e){
						log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
					}
				    
				    MProductPO purchasing = new MProductPO(getCtx(),0,get_Trx());
//				    purchasing = new MProductPO(getCtx(), 0, null);
				    purchasing.setC_BPartner_ID(proveedor.getC_BPartner_ID());
				    purchasing.setM_Product_ID(getM_Product_ID());
				    purchasing.setC_Currency_ID(moneda);
				    purchasing.setIsActive(true);
				    purchasing.setIsCurrentVendor(true);
				    purchasing.setVendorProductNo(Referencia.toString());
				    purchasing.setPricePO(LineRefProv.getXX_UnitPurchasePrice());
				    purchasing.setPriceLastPO(LineRefProv.getXX_UnitPurchasePrice());
				    purchasing.setPriceList(LineRefProv.getXX_UnitPurchasePrice());
					purchasing.save();
					
					proveedor.setXX_IsAssociated(true);
					proveedor.save();
						
					Window[] List = AWindow.getWindows();
					
					for (int i = 0; i < List.length; i++) {
						
						if(List[i].getName().equals("Associate With")){
							List[i].dispose();
						}	
						
						if(List[i].isFocused()){
							List[i].setVisible(false);
						}	
											
					}

				}// final del fromprocess
				
				// Realizado por Rosmaira Arvelo				
				if(getXX_FromProcess()=='U') // Si vengo de Unsolicited Product Incorrect
				{	
					//Cargo el producto que se acaba de crear	
					MProduct productReference = new MProduct(getCtx(), getM_Product_ID(), null);				   
				    
				    Window[] List = AWindow.getWindows();
					
					for (int i = 0; i < List.length; i++) {
						
						if(List[i].getName().equals("UnsolicitedProductIncorrect")){
							List[i].dispose();
						}	
						
						if(List[i].isFocused()){
							List[i].setVisible(false);
						}					
					}
					
				    XX_UnsolicitedProductIncorrect_Form.loadMProduct(productReference, ctx_aux);
				    XX_UnsolicitedProductIncorrect_Form.loadUnsolicitedProduct(unsolProd, ctx_aux);
				    getCtx().setContext("#XX_FROMPRODUCT",1);
				
				    FormFrame form = new FormFrame();
					form.openForm(getCtx().getContextAsInt("XX_L_FORMUNSOLPRODINCOR_ID"));
					AEnv.showCenterScreen(form);
					
				} // fin del fromprocess				
				
				//return true;
				//return true;
				//**Realizado por WDíaz
				
				
				try{
					/**Asigna Lista de Precio Standar al PRODUCTO Cuando es Creado WDíaz*/
					MProductPrice listaPrecio = new MProductPrice(getCtx(), 0, get_Trx());
					listaPrecio.setM_Product_ID(get_ID());
					listaPrecio.setM_PriceList_Version_ID(getCtx().getContextAsInt("#XX_L_P_PRICELISTST_ID"));
					listaPrecio.setPriceList(new BigDecimal(0));
					listaPrecio.setPriceStd(new BigDecimal(0));
					listaPrecio.setPriceLimit(new BigDecimal(0));
					listaPrecio.setIsActive(true);
					listaPrecio.save();
				}catch (Exception e) {
					log.saveError("Error", Msg.getMsg(getCtx(), " Lista de Precio no Creada"+e.getMessage()));
					return false;
				}
				
				/****Jessica Mendoza****/
				//Se agrega al producto la cuenta contable correspondiente a 
				//mercancia, con tipo de cuenta activo
				if (getProductType().equals("I")){
					String updateProductAcct = "update M_Product_Acct " +
							"set XX_PInventory_ID = " + cuentaContable() + " " +
							"where M_Product_ID = " + getM_Product_ID();
					DB.executeUpdate(get_Trx(), updateProductAcct);
				}
				/****Fin código - Jessica Mendoza****/			
			}
			
			// Realizado por Rosmaira Arvelo
			if(Env.getCtx().getContext("#FromProcess_Aux").equals("V")){// Si vengo de Validate Product
				
				MInOutLine inOutLine = new MInOutLine(Env.getCtx(),unsolProd.getM_InOutLine_ID(),null);
				BigDecimal quantity = inOutLine.getMovementQty();
				
				// Hago el movimiento de inventario del locator "Por Validar"
				// al Locator "Chequeado" 				    
				MMovement movement = new MMovement(Env.getCtx(),0,null);
				movement.setC_DocType_ID(1000124);
				movement.save();
				    
				MMovementLine movementLine = new MMovementLine(Env.getCtx(),0,null);
				movementLine.setM_AttributeSetInstance_ID(inOutLine.getM_AttributeSetInstance_ID());
				movementLine.setM_Movement_ID(movement.get_ID());
				movementLine.setM_Locator_ID(inOutLine.getM_Locator_ID());
				movementLine.setM_LocatorTo_ID(getLocatorCheck(inOutLine.getM_Warehouse_ID()));
				movementLine.setM_Product_ID(inOutLine.getM_Product_ID());
				movementLine.setMovementQty(quantity);
				movementLine.save();
				    
				movement.setDocAction(MMovement.DOCACTION_Complete);
				DocumentEngine.processIt(movement, MMovement.DOCACTION_Complete);
				movement.save();
					
				X_XX_VMR_VendorProdRef reference = new X_XX_VMR_VendorProdRef(getCtx(),unsolProd.getXX_VMR_VendorProdRef_ID(),null);
				reference.setXX_VMR_Line_ID(getXX_VMR_Line_ID());
				reference.setXX_VMR_Section_ID(getXX_VMR_Section_ID());
				reference.save();
					
				unsolProd.setXX_ValidateProduct(true);
				unsolProd.setXX_Record_ID(unsolProd.getM_Product_ID());
				unsolProd.save();				
				
			} // fin del fromprocess
			
			/** Envia Mensaje Cuando Se guarda el Producto y si no tiene codigo arancelario WDíaz*/
			//Se comento porque no se esta utilizando actualmente código arancerio
			/*if(!Env.getCtx().getContext("#FromProcess_Aux").equals("R")){//Si no viene de Unsolicited Product RArvelo
				try {
					String sql="";
					if ((getC_Country_ID() != 339) && ((getXX_CodeTariff() == null) || (getXX_CodeTariff().equals(""))) && !getProductType().equalsIgnoreCase("S"))
					{						
						//sql = "SELECT B.C_BPARTNER_ID AS JEFE " + "FROM C_BPARTNER B " +"WHERE ISACTIVE = 'Y' " +
						//		     "AND IsEmployee = 'Y' " + "AND B.C_JOB_ID = (SELECT C_JOB_ID FROM C_JOB WHERE NAME = upper('Jefe de Importaciones'))";
						sql = "select AD_USER_ID AS JEFE from AD_USER_ROLES where AD_ROLE_ID = (select AD_ROLE_ID from AD_ROLE where upper(name) = upper('BECO Analista de Importaciones')) and IsActive = 'Y'";
						PreparedStatement priceRulePstmt = DB.prepareStatement(sql, null);
						ResultSet rs = priceRulePstmt.executeQuery();
						while (rs.next())
						{
							//System.out.println("AD_USER_IDAD_USER_ID:"+rs.getInt("AD_USER_ID"));
							String Mensaje = ":Código del Producto-"+getValue()+" Name-"+getName();
							Utilities f = new Utilities(getCtx(), null, getCtx().getContextAsInt("#XX_L_MT_CODETARIFF_ID"), Mensaje, -1, getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, rs.getInt("JEFE"),null);
							f.ejecutarMail(); 
						}
						rs.close();
						priceRulePstmt.close();
						
						//Realizado por Rosmaira Arvelo
						try
						{
							Env.getCtx().setContext("#XX_TypeAlertAC","AC");
							Env.getCtx().setContext("#XX_ProductCT",get_ID());
							
							//LLama al proceso de generar alerta Asignación de código arancelario
							MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID"), get_ID()); 
							mpi.save();
							
							ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
							pi.setRecord_ID(mpi.getRecord_ID());
							pi.setAD_PInstance_ID(mpi.get_ID());
							pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
							pi.setClassName(""); 
							pi.setTitle(""); 
							
							ProcessCtl pc = new ProcessCtl(null ,pi,null); 
							pc.start();
						}
						catch(Exception e)
						{
							log.log(Level.SEVERE,e.getMessage());
						}
						//Fin Rosmaira Arvelo
					}
				} catch (Exception e) {
					log.log(Level.SEVERE, " Mensaje no Enviado"+e.getMessage());
					return false;
				}						
			}
			*/
			/**Asigna el Provedor al purchase por referencia en producto WDíaz*/
			String sql = "";
			PreparedStatement priceRulePstmt = null;
			ResultSet rs = null;
			try{
				 
				Integer Referencia = getXX_VMR_VendorProdRef_ID();
				if (Referencia > 0)
				{
					   X_XX_VMR_VendorProdRef ReferenciaProveedor = new X_XX_VMR_VendorProdRef(getCtx(), Referencia, get_Trx());
							
						    sql = "select * from C_BPARTNER_PRODUCT  where M_PRODUCT_ID = "+getM_Product_ID()+"";
						    priceRulePstmt = DB.prepareStatement(sql, get_Trx());
							rs = priceRulePstmt.executeQuery();
							if (rs.next())
							{
								sql = "delete from C_BPARTNER_PRODUCT  where M_PRODUCT_ID = "+getM_Product_ID()+"";
								DB.executeUpdate(get_Trx(),sql);
							}
							MBPartnerProduct BP = new MBPartnerProduct(getCtx(), 0, get_Trx());
							BP.setC_BPartner_ID(ReferenciaProveedor.getC_BPartner_ID());
							BP.setM_Product_ID(getM_Product_ID());
							BP.setIsActive(true);
							BP.setShelfLifeMinDays(0);
							BP.setShelfLifeMinPct(0);
							BP.save();
			    }
			}catch (Exception e) {
				log.saveError("Error", Msg.getMsg(getCtx(), sql+" Proveedor no Asignado"+e.getMessage()));
				return false;
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(priceRulePstmt);
			}
//Fin Realizado por WDíaz
			
			//Realizado por Rosmaira Arvelo
			if(!newRecord){								
						
				MVMRCriticalTaskForClose task = new MVMRCriticalTaskForClose(Env.getCtx(),getCriticalTaskForClose(get_ID()),null);
				
				//Llama al proceso cerrar alerta de Asignación de código arancelario
				if(!(getXX_CodeTariff().equals(""))&&!(getXX_CodeTariff().equals(null))&&(task.get_ID()!=0)&&(task.isActive()==true)&&(task.getXX_TypeTask().equals("AC")))
				{	
					try
					{
						MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID"), task.get_ID()); 
						mpi.save();
						
						ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
						pi.setRecord_ID(mpi.getRecord_ID());
						pi.setAD_PInstance_ID(mpi.get_ID());
						pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
						pi.setClassName(""); 
						pi.setTitle(""); 
						
						ProcessCtl pc = new ProcessCtl(null ,pi,null); 
						pc.start();
					}
					catch(Exception e)
					{
						log.log(Level.SEVERE,e.getMessage());
					}
				}
				
				if (isXX_Status_Sinc())   //actualiza el status para indicar que hay q sincronizar el registro
				{
					setXX_Status_Sinc(false);
					save();
				}		
				
				
			} //Fin RArvelo
			
			
			if(getXX_FromProcess()=='Y' && newRecord){ //Si es sin caracteristicas
			
				XX_AssociateReference_Form.loadLineRefProv(LineRefProv,ctx_aux);
				
				getCtx().setContext("#Dialog_Associate_Aux","1");
				
				get_Trx().commit();
				FormFrame form = new FormFrame();
				form.setName("Associate");
				form.openForm(1000004);
				AEnv.showCenterScreen(form);
				
			}else if(getXX_FromProcess()=='W' && newRecord){ //Si es con caracteristica
			
				XX_AssociateReferenceWith_Form.loadLineRefProv(LineRefProv,ctx_aux);
				
				getCtx().setContext("#Dialog_Associate_Aux","1");
				
				get_Trx().commit();
				FormFrame form = new FormFrame();
				form.setName("Associate With");
				form.openForm(1000005);
				AEnv.showCenterScreen(form);
			}
			
		
		}
		return save;
	}
	
	@Override
	protected boolean beforeDelete()
	{
		boolean delete = super.beforeDelete();
		
		if(delete){
			
			String SQL="Select * from XX_VMR_ReferenceMatrix where " +
					"M_Product="+get_ID();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try 
			{	
				pstmt = DB.prepareStatement(SQL, null);
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					log.saveError("Can Not Delete Record", Msg.getMsg(getCtx(), "ProductAssociated"));
					return false;
				}
							
			}
			catch(Exception e)
			{	
				e.getMessage();
			}finally{
				
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
		
		return delete;
		
	}	//	beforeDelete	
	
	/**
	 * Se encarga de buscar la cuenta contable de mercancia
	 * @author Jessica Mendoza
	 * @return identificador de la cuenta contable
	 */
	public Integer cuentaContable(){
		int cuentaId = 0;
		String sql = "select C_ElementValue_ID " +
					"from C_ElementValue " +
					"where M_Product_Category_ID = " + Env.getCtx().getContextAsInt("#XX_L_CATEGORYPRODUCTITEM_ID") + " " + 
					"and AccountType = 'A' ";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
		    if(rs.next()){
		    	cuentaId = rs.getInt(1);
		    }
		}catch(Exception e){
			log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return cuentaId;
	}

    /** Get XX_FromProduct.
    @return XX_FromProduct*/
    public int getXX_FromProduct_ID() 
    {
    	return get_ValueAsInt("XX_FromProduct_ID");
        
    }
    
    // Get y Set de Departamento
    public int getXX_VMR_Department_ID(){
    	return get_ValueAsInt("XX_VMR_Department_ID");
    }
    
    public void setXX_VMR_Department_ID(int XX_VMR_Department_ID)
    {
    	if (XX_VMR_Department_ID <= 0) set_Value ("XX_VMR_Department_ID", null);
        else
        	set_Value ("XX_VMR_Department_ID", Integer.valueOf(XX_VMR_Department_ID));   	
    }
    
    
    // Get y Set de Section
    public int getXX_VMR_Section_ID(){
    	return get_ValueAsInt("XX_VMR_Section_ID");
    }
    
    public void setXX_VMR_Section_ID(int XX_VMR_Section_ID)
    {
    	if (XX_VMR_Section_ID <= 0) set_Value ("XX_VMR_Section_ID", null);
        else
        	set_Value ("XX_VMR_Section_ID", Integer.valueOf(XX_VMR_Section_ID));   	
    }
    
    
    // Get y Set de Linea
    public int getXX_VMR_Line_ID(){
    	return get_ValueAsInt("XX_VMR_Line_ID");
    }
    public void setXX_VMR_Line_ID(int XX_VMR_Line_ID)
    {
    	if (XX_VMR_Line_ID <= 0) set_Value ("XX_VMR_Line_ID", null);
        else
        	set_Value ("XX_VMR_Line_ID", Integer.valueOf(XX_VMR_Line_ID));   	
    }
   
    /** Get y Set Brand
     * */
    public int getXX_VMR_Brand_ID() {
    	return get_ValueAsInt("XX_VMR_Brand_ID");
    }
    public void setXX_VMR_Brand_ID(int XX_VMR_Brand_ID)
    {
    	if (XX_VMR_Brand_ID <= 0) set_Value ("XX_VMR_Brand_ID", null);
        else
        	set_Value ("XX_VMR_Brand_ID", Integer.valueOf(XX_VMR_Brand_ID));    	
    }
    
    /** Get y Set XX_VMR_VendorProdRef_ID
     * */
    public int getXX_VMR_VendorProdRef_ID() {
    	
    	return get_ValueAsInt("XX_VMR_VendorProdRef_ID");
    }
    public void setXX_VMR_VendorProdRef_ID(int XX_VMR_VendorProdRef_ID)
    {
    	if (XX_VMR_VendorProdRef_ID<= 0) set_Value ("XX_VMR_VendorProdRef_ID", null);
        else
        	set_Value ("XX_VMR_VendorProdRef_ID", Integer.valueOf(XX_VMR_VendorProdRef_ID));   	
    }
    
    /** Get y Set XX_VMR_TypeInventory_ID
     * */
    public int getXX_VMR_TypeInventory_ID() {
    	return get_ValueAsInt("XX_VMR_TypeInventory_ID");
    }
    public void setXX_VMR_TypeInventory_ID(int XX_VMR_TypeInventory_ID)
    {
    	if (XX_VMR_TypeInventory_ID <= 0) set_Value ("XX_VMR_TypeInventory_ID", null);
        else
        	set_Value ("XX_VMR_TypeInventory_ID", Integer.valueOf(XX_VMR_TypeInventory_ID));   	
    }
    
    /** Get y Set C_Country_ID
     * */
    public int getC_Country_ID() {
    	return get_ValueAsInt("C_Country_ID");
    }
    public void setC_Country_ID(int C_Country_ID)
    {
    	if (C_Country_ID <= 0) set_Value ("C_Country_ID", null);
        else
        	set_Value ("C_Country_ID", Integer.valueOf(C_Country_ID));  	
    }
    
    /** Get y Set XX_VMR_LongCharacteristic_ID
     * */
    public int getXX_VMR_LongCharacteristic_ID() {
    	return get_ValueAsInt("XX_VMR_LongCharacteristic_ID");
    }
    public void setXX_VMR_LongCharacteristic_ID(int id)
    {
    	if (id <= 0) set_Value ("XX_VMR_LongCharacteristic_ID", null);
        else
        set_Value ("XX_VMR_LongCharacteristic_ID", Integer.valueOf(id));    	
    }
    
    /** Get y Set XX_VMR_UnitConversion_ID
     * */
    public int getXX_VMR_UnitConversion_ID() {
    	return get_ValueAsInt("XX_VMR_UnitConversion_ID");
    }
    public void setXX_VMR_UnitConversion_ID(int XX_VMR_UnitConversion_ID)
    {
    	if (XX_VMR_UnitConversion_ID <= 0) set_Value ("XX_VMR_UnitConversion_ID", null);
        else
        	set_Value ("XX_VMR_UnitConversion_ID", Integer.valueOf(XX_VMR_UnitConversion_ID));    	
    }
    
    /** Get y Set XX_PiecesBySale_ID
     * */
    public int getXX_PiecesBySale_ID() {
    	return get_ValueAsInt("XX_PiecesBySale_ID");
    }
    public void setXX_PiecesBySale_ID(int XX_PiecesBySale_ID)
    {
    	if (XX_PiecesBySale_ID <= 0) set_Value ("XX_PiecesBySale_ID", null);
        else
        	set_Value ("XX_PiecesBySale_ID", Integer.valueOf(XX_PiecesBySale_ID));  	
    }
    
    /** Get y Set XX_VMR_ConceptValue_ID
     * */
    public int getXX_VME_ConceptValue_ID() {
    	return get_ValueAsInt("XX_VME_ConceptValue_ID");
    }
    public void setXX_VME_ConceptValue_ID(int XX_VME_ConceptValue_ID)
    {
    	if (XX_VME_ConceptValue_ID <= 0) set_Value ("XX_VME_ConceptValue_ID", null);
        else
        	set_Value ("XX_VME_ConceptValue_ID", Integer.valueOf(XX_VME_ConceptValue_ID)); 	
    }
    
    /** Get y Set XX_VMR_Category_ID
     * */
    public int getXX_VMR_Category_ID() {
    	return get_ValueAsInt("XX_VMR_Category_ID");
    }
    public void setXX_VMR_Category_ID(int XX_VMR_Category_ID)
    {
    	if (XX_VMR_Category_ID <= 0) set_Value ("XX_VMR_Category_ID", null);
        else
        	set_Value ("XX_VMR_Category_ID", Integer.valueOf(XX_VMR_Category_ID));  	
    }
    
    /** Get y Set XX_VMR_ProductClass_ID
     * */
    public int getXX_VMR_ProductClass_ID() {
    	return get_ValueAsInt("XX_VMR_ProductClass_ID");
    }
    public void setXX_VMR_ProductClass_ID(int XX_VMR_ProductClass_ID)
    {
    	if (XX_VMR_ProductClass_ID <= 0) set_Value ("XX_VMR_ProductClass_ID", null);
        else
        	set_Value ("XX_VMR_ProductClass_ID", Integer.valueOf(XX_VMR_ProductClass_ID));    	
    }
    
    /** Get y Set XX_VMR_TypeExhibition_ID
     * */
    public int getXX_VMR_TypeExhibition_ID() {
    	return get_ValueAsInt("XX_VMR_TypeExhibition_ID");
    }
    public void setXX_VMR_TypeExhibition_ID(int XX_VMR_TypeExhibition_ID)
    {
    	if (XX_VMR_TypeExhibition_ID <= 0) set_Value ("XX_VMR_TypeExhibition_ID", null);
        else
        	set_Value ("XX_VMR_TypeExhibition_ID", Integer.valueOf(XX_VMR_TypeExhibition_ID));  	
    }
    
    /** Get y Set XX_VMR_TypeExhibition_ID
     * */
    public int getXX_VMR_TypeLabel_ID() {
    	return get_ValueAsInt("XX_VMR_TypeLabel_ID");
    }
    public void setXX_VMR_TypeLabel_ID(int XX_VMR_TypeLabel_ID)
    {
    	if (XX_VMR_TypeLabel_ID <= 0) set_Value ("XX_VMR_TypeLabel_ID", null);
        else
        	set_Value ("XX_VMR_TypeLabel_ID", Integer.valueOf(XX_VMR_TypeLabel_ID));
    }
    
    /** Get y Set XX_SaleUnit_ID
     * */
    public int getXX_SaleUnit_ID() {
    	return get_ValueAsInt("XX_SaleUnit_ID");
    }
    public void setXX_SaleUnit_ID(int XX_SaleUnit_ID)
    {
    	if (XX_SaleUnit_ID <= 0) set_Value ("XX_SaleUnit_ID", null);
        else
        	set_Value ("XX_SaleUnit_ID", Integer.valueOf(XX_SaleUnit_ID));
    }
    
    /** Get y Set XX_VMR_UnitPurchase_ID
     * */
    public int getXX_VMR_UnitPurchase_ID() {
    	return get_ValueAsInt("XX_VMR_UnitPurchase_ID");
    }
    public void setXX_VMR_UnitPurchase_ID(int XX_VMR_UnitPurchase_ID)
    {
    	if (XX_VMR_UnitPurchase_ID <= 0) set_Value ("XX_VMR_UnitPurchase_ID", null);
        else
        	set_Value ("XX_VMR_UnitPurchase_ID", Integer.valueOf(XX_VMR_UnitPurchase_ID));
    }
   
    /** Get y Set XX_CodeTariff
     * */
    public void setXX_CodeTariff (String name)
    {
        set_Value ("XX_CodeTariff", name);
    }
    
    public String getXX_CodeTariff()
    {
    	return get_ValueAsString("XX_CodeTariff");
    	
    }
    
    
    /** Get y Set XX_PercentageTariff
     * */
    public java.math.BigDecimal getXX_PercentageTariff()
    {
    	return get_ValueAsBigDecimal("XX_PercentageTariff");
    }
    public void setXX_PercentageTariff(java.math.BigDecimal XX_PercentageTariff)
    {
    	set_Value ("XX_PercentageTariff", XX_PercentageTariff);
    }
   
    
    /** Get XX_FromProcess.
    @return XX_FromProcess*/
    public char getXX_FromProcess() 
    {
    	return get_ValueAsString("XX_FromProcess").charAt(0);   
    }
    
    
	/**
	 *  cargar la linea a la cual está relacionada la referencia
	 */
	public static void loadLineRefProv(X_XX_VMR_PO_LineRefProv LineRefProv_aux, Ctx ctx)
	{
		LineRefProv = LineRefProv_aux;
		ctx_aux = ctx;
		
	}	//	LineRefProv
		
	/**
	 *  cargar el producto no solicitado al cual está relacionado la referencia
	 */
	public static void loadUnsolicitedProduct(MVLOUnsolicitedProduct unsolProd_aux, Ctx ctx)
	{
		unsolProd=unsolProd_aux;
		ctx_aux = ctx;
		
	}	//	Unsolicited Product 
	
	//Get y Set de C_BPartner
	public int getC_BPartner_ID() {
    	return get_ValueAsInt("C_BPartner_ID");
    }
    
	public void setC_BPartner_ID(int C_BPartner_ID)
    {
       set_Value ("C_BPartner_ID", C_BPartner_ID);
    }
    
	
	 /*
	 * Status Sincronizacion al AS de "Producto"
	 */
	public void setXX_Status_Sinc (boolean XX_Status_Sinc)
	{
		set_Value ("XX_Status_Sinc", XX_Status_Sinc);
	        
	} 
	  
	public boolean isXX_Status_Sinc() 
	{
		return get_ValueAsBoolean("XX_Status_Sinc");    
	}
	

	
	/*
	 *	Obtengo el ID del Locator "Chequeado"
	 */
	private Integer getLocatorCheck(Integer M_Warehouse_ID){
		
		Integer locator=0;
		String SQL = "SELECT l.M_Locator_ID FROM M_Locator l, M_Warehouse w "
				   + "WHERE l.M_Warehouse_ID=w.M_Warehouse_ID AND IsDefault='Y' "
				   + "AND w.M_Warehouse_ID="+M_Warehouse_ID+" AND l.IsActive='Y'";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				locator = rs.getInt("M_Locator_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return locator;
	}
    /** Set Alert.
    @param XX_Alert Alert */
    public void setXX_Alert (boolean XX_Alert)
    {
        set_Value ("XX_Alert", Boolean.valueOf(XX_Alert));
        
    }
    
    /** Get Alert.
    @return Alert */
    public boolean isXX_Alert() 
    {
        return get_ValueAsBoolean("XX_Alert");
        
    }
    
  //Realizado por Rosmaira Arvelo
    /*
	 *	Obtengo el ID de la tarea critica segun el producto
	 */
	private Integer getCriticalTaskForClose(Integer prod){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTaskForClose_ID FROM XX_VMR_CriticalTaskForClose "
				   + "WHERE XX_ActualRecord_ID="+prod;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
		    ResultSet rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{				
				criticalTask = rs.getInt("XX_VMR_CriticalTaskForClose_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return criticalTask;
	}//fin getCriticalTask RArvelo
	
	/** Purchase of Supplies and Services
	 * Maria Vintimilla Funcion 004**/
	
	/** Set Acquisition Date.
	@param XX_AcquisitionDate Acquisition Date on M_Product */
    public void setXX_AcquisitionDate (Timestamp XX_AcquisitionDate){
        set_Value ("XX_AcquisitionDate", XX_AcquisitionDate); 
    }
	    
	/** Get Acquisition Date.
	@return XX_AcquisitionDate Acquisition Date on M_Product */
    public Timestamp getXX_AcquisitionDate(){
        return (Timestamp)get_Value("XX_AcquisitionDate");
    }
    
	/** Set Deactivation Date.
	@param XX_DeactivationDate Deactivation Date on M_Product */
    public void setXX_DeactivationDate (Timestamp XX_DeactivationDate){
        set_Value ("XX_DeactivationDate", XX_DeactivationDate); 
    }
	    
	/** Get Deactivation Date.
	@return XX_DeactivationDate Deactivation Date on M_Product */
    public Timestamp getXX_DeactivationDate(){
        return (Timestamp)get_Value("XX_Deactivation");
    }
    
    /** Set XX_AveragePrice.
    @param XX_AveragePrice Average Price on M_Product */
    public void setXX_AveragePrice (Integer XX_AveragePrice)
    {
        set_Value ("XX_AveragePrice", XX_AveragePrice);
        
    }
    
    /** Get XX_AveragePrice.
    @return XX_AveragePrice Average Price on M_Product */
    public Integer getXX_AveragePrice() 
    {
        return (Integer)get_Value("XX_AveragePrice");
    }
    
    /** Set XX_LastPrice.
    @param XX_LastPrice Last Price on M_Product */
    public void setXX_LastPrice (Integer XX_LastPrice)
    {
        set_Value ("XX_LastPrice", XX_LastPrice);
        
    }
    
    /** Get XX_LastPrice.
    @return XX_LastPrice Last Price on M_Product */
    public Integer getXX_LastPrice() 
    {
        return (Integer)get_Value("XX_LastPrice");
    }
    
    /** Set XX_AssetsType.
    @param XX_AssetsType Assets Type on M_Product */
    public void setXX_AssetsType (String XX_AssetsType)
    {
        set_Value ("XX_AssetsType", XX_AssetsType);
        
    }
    
    /** Get XX_AssetsType.
    @return XX_AssetsType Assets Type on M_Product */
    public String getXX_AssetsType() 
    {
        return (String)get_Value("XX_AssetsType");
        
    }
    
    /** Set Model.
    @param XX_Model Model on M_Product */
    public void setXX_Model (Integer XX_Model){
        set_Value ("XX_Model", XX_Model);  
    }
    
    /** Get Model.
    @return XX_Model Model on M_Product */
    public String getXX_Model(){
        return (String)get_Value("XX_Model");
    }
    
    /** Set Product Name.
    @param XX_ProductName_ID Product Name */
    public void setXX_ProductName_ID(int XX_ProductName_ID)
    {
        if (XX_ProductName_ID < 1) throw new IllegalArgumentException ("XX_ProductName_ID is mandatory.");
        set_Value ("XX_ProductName_ID", Integer.valueOf(XX_ProductName_ID));
        
    }
    
    /** Get XX_ProductName_ID.
    @return XX_ProductName_ID Product Name */
    public int getXX_ProductName_ID() 
    {
        return get_ValueAsInt("XX_ProductName_ID");
        
    }
}
