package compiere.model.cds;

import java.awt.Window;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.apps.AEnv;
import org.compiere.apps.AWindow;
import org.compiere.apps.form.FormFrame;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.cds.forms.XX_UnsolicitedProduct_Form;

public class MVMRVendorProdRef extends X_XX_VMR_VendorProdRef {
	
	static Integer inOutLineID;
	static Integer invoiceLineID;
	static Ctx ctx_aux;

	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MVMRVendorProdRef.class);
	
	public MVMRVendorProdRef (Ctx ctx, int VendorProdRef_ID, Trx trx)
	{
		super (ctx, VendorProdRef_ID, trx);
		if (VendorProdRef_ID == 0)
		{
		//	setM_Requisition_ID (0);
//			setLine (0);	// @SQL=SELECT COALESCE(MAX(Line),0)+10 AS DefaultValue FROM M_RequisitionLine WHERE M_Requisition_ID=@M_Requisition_ID@
//			setLineNetAmt (Env.ZERO);
//			setPriceActual (Env.ZERO);
//			setQty (Env.ONE);	// 1
//			setMargin (Env.ONE);			/////////////////////////////////////////////////////////////////
//			setSalePrice (Env.ONE);			/////////////////////////////////////////////////////////////////
//			setLinePVPAmount (Env.ZERO);	/////////////////////////////////////////////////////////////////
//			setLinePlusTaxAmount (Env.ZERO);	////////////////////////////////////////////////////////////
//			setPackageMultiple (Env.ONE);				///////////////////////////////////////////

		}
		
	}	
	
	public MVMRVendorProdRef (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	
	
	@Override
	protected boolean beforeSave (boolean newRecord)
	{

		if (getValue()==null)
		{
			log.saveError("Error", Msg.getMsg(getCtx(), "XX_InvalidReferenceCode"));
			return false;
		}
		
		
		//Si ya existe una referencia con el mismo proveedor, codigo, departamento, linea y seccion se envia un mensaje de alerta
		 String SQL = "SELECT XX_VMR_VENDORPRODREF_ID " +
 					  "FROM XX_VMR_VENDORPRODREF " +
 					  "WHERE XX_VMR_DEPARTMENT_ID="+getXX_VMR_Department_ID()+
 					  " AND XX_VMR_LINE_ID="+getXX_VMR_Line_ID()+
		 			  " AND XX_VMR_SECTION_ID="+getXX_VMR_Section_ID()+
		              " AND C_BPARTNER_ID="+getC_BPartner_ID()+
		              " AND VALUE='"+getValue()+"'";
		 
	    	PreparedStatement pstmt = null;
			ResultSet rs = null;
		 try{
			
	    	pstmt = DB.prepareStatement(SQL, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				if(rs.getInt("XX_VMR_VENDORPRODREF_ID")!=get_ID()){
					log.saveWarning("Warning", Msg.getMsg(getCtx(), "DuplicatedReference"));
				}
			}
		 }
		 catch (SQLException e){
			 log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		 } finally {
			 DB.closeResultSet(rs);
			 DB.closeStatement(pstmt);
		 }
		 
//		 String typeSQL = "SELECT XX_VMR_TYPEBASIC_ID " +
//		  "FROM XX_VMR_SECTION " +
//		  "WHERE XX_VMR_SECTION_ID="+getXX_VMR_Section_ID()+"'";
//
//		 PreparedStatement pstmt0 = null;
//		 ResultSet rs0 = null;
//		 try{
//
//			 pstmt0 = DB.prepareStatement(typeSQL, null);
//			 rs0 = pstmt.executeQuery();
//			 if(rs0.next()){
//				 setXX_VMR_TypeBasic_ID(rs0.getInt(1));
//			 }
//		 }
//		 catch (SQLException e){
//			 log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
//		 } finally {
//			 DB.closeResultSet(rs0);
//			 DB.closeStatement(pstmt0);
//		 }
		
		 int sect = getXX_VMR_Section_ID();
		 //X_XX_VMR_TypeInventory type = new X_XX_VMR_TypeInventory(Env.getCtx(), line.getXX_VMR_TypeInventory_ID(), null);
		 String sql = "SELECT XX_VMR_TypeBasic_ID FROM XX_VMR_Section WHERE XX_VMR_Section_ID = "+sect;
		 
		 if(getXX_VMR_TypeBasic_ID()==0){
				PreparedStatement pstmtS = null;
				ResultSet rsS = null;
				try{
					pstmtS = DB.prepareStatement(sql, null);
					rsS =  pstmtS.executeQuery();
					if(rsS.next()){
						setXX_VMR_TypeBasic_ID(rsS.getInt(1));
					}
				}catch (SQLException e) {
					log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
				}finally{
					try {
						pstmtS.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {
						rsS.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		 //Ya no aplica porque las referencias ahora se bloquean al crearse
		//Verifico que los campos Package Multiple, Purchase Unit, Sale Unit, 
		//Pieces By Purchase y Pieces By Sale hayan sido modificados
		/*
		if(!newRecord){
			X_XX_VMR_VendorProdRef reference = new X_XX_VMR_VendorProdRef(getCtx(), getXX_VMR_VendorProdRef_ID(), null);
			
			if(is_ValueChanged("XX_VMR_UnitConversion_ID") || is_ValueChanged("XX_PiecesBySale_ID"))
			{
				 SQL = "Select M_Product_ID " +
		    		"from M_Product " +
		    		"where XX_VMR_VENDORPRODREF_ID="+get_ID();
				 
				 try{
					
			    	PreparedStatement pstmt = DB.prepareStatement(SQL, null);
					ResultSet rs = pstmt.executeQuery();
					
					while(rs.next()){
						MProduct product= new MProduct(getCtx(),rs.getInt("M_Product_ID"),null);
						product.setXX_VMR_UnitConversion_ID(reference.getXX_VMR_UnitConversion_ID());
						product.setXX_PiecesBySale_ID(reference.getXX_PiecesBySale_ID());
						product.save();
					}
					
					rs.close();
					pstmt.close();
				 }
				 catch (SQLException e){
					 log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
				 }
			}
			
		}*/
		
		 String SQL1 = "select value from xx_vmr_line where xx_vmr_line_id=" + getXX_VMR_Line_ID();
		 String SQL2 = "select value from xx_vmr_section where xx_vmr_section_id=" + getXX_VMR_Section_ID();
		 String SQL3 = "select name from XX_VMR_LONGCHARACTERISTIC where XX_VMR_LONGCHARACTERISTIC_ID=" + getXX_VMR_LongCharacteristic_ID();
		 String line = "";
		 String section = "";
		 String caracteristic = "";
	    	PreparedStatement pstmt1 = null;
	    	PreparedStatement pstmt2 = null;
	    	PreparedStatement pstmt3 = null;
			ResultSet rs1 = null;
			ResultSet rs2 = null;
			ResultSet rs3 = null;
		try{
			
	    	pstmt1 = DB.prepareStatement(SQL1, null);
	    	pstmt2 = DB.prepareStatement(SQL2, null);
	    	pstmt3 = DB.prepareStatement(SQL3, null);
			rs1 = pstmt1.executeQuery();
			rs2 = pstmt2.executeQuery();
			rs3 = pstmt3.executeQuery();
			
			if(rs1.next()){
				line = rs1.getString("value");
			}
			if(rs2.next()){
				section = rs2.getString("value");
			}
			if(rs3.next()){
				caracteristic = rs3.getString("name");
			}
			
		 }
		 catch (SQLException e){
			 log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		 } finally {
			 DB.closeResultSet(rs1);
			 DB.closeResultSet(rs2);
			 DB.closeResultSet(rs3);
			 DB.closeStatement(pstmt1);
			 DB.closeStatement(pstmt2);
			 DB.closeStatement(pstmt3);
		 }
		 
		 
		 // Como la inscripcion en ingles no puede estar vacia porque es lo que se imprime,
		 // si esta vacia le seteo lo que tenga el name
		 if (get_ValueAsString("XX_EnglishDescription").equals(""))
			 setXX_EnglishDescription(get_ValueAsString("Name").toUpperCase());
		
		setValue(get_ValueAsString("Value").toUpperCase());	
		setName(get_ValueAsString("Name").toUpperCase());
		if (caracteristic.equals(""))
			setDescription(getValue() + "_" + getName() + "_Line:" + line + "_Sec:" + section);
		else
		{
			setDescription(getValue() + "_" + getName() + "_Line:" + line + "_Sec:" + section + "_Caract:" + caracteristic);
		}
				
		//Se bloquea la referencia
		setXX_IsAssociated(true);
		
		//Si no tiene concepto valor no lo dejo guardar
		if(!conceptValue()){
			log.saveError("Error", Msg.getMsg(getCtx(), "NoConceptValue"));
			return false;
		}
		
		return true;
	}
	
	private boolean conceptValue(){
		
		String sql = "SELECT XX_VME_CONCEPTVALUE_ID"											//////////////////////
			+ " FROM XX_VMR_CONCEPTVALDPTBRAND"
			+ " WHERE XX_VMR_DEPARTMENT_ID=" + getXX_VMR_Department_ID() + " AND XX_VMR_BRAND_ID=" + getXX_VMR_Brand_ID() 
			+ " AND ISACTIVE = 'Y'";
	
		PreparedStatement prst = DB.prepareStatement(sql,null);
		ResultSet rs = null;
		try {
			
			rs = prst.executeQuery();
			
			while (rs.next()){
				return true;
			}
		
		} catch (SQLException e){
			return false;
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(prst);	
		}
	
		return false;
	} 
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return true if can be saved
	 *	
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{	 
		boolean save = super.afterSave(newRecord, success);
		
		if(save){
			if(newRecord){				
			    //Realizado por Rosmaira Arvelo
				if(getXX_FromProcess().equals("Y")){ //Si vengo de Unsolicited Product
					
					//Capturo el ID de la referencia de producto que se acaba de crear					
				    Integer vendorProdRefID = getXX_VMR_VendorProdRef_ID();
				    X_XX_VMR_VendorProdRef vendorProdRef = new X_XX_VMR_VendorProdRef(getCtx(), vendorProdRefID, get_Trx());
				    
				    X_XX_VMR_Department depart = new X_XX_VMR_Department(getCtx(),vendorProdRef.getXX_VMR_Department_ID(),get_Trx());				    
				    int category = depart.getXX_VMR_Category_ID();
				    
				    X_XX_VMR_Line line = new X_XX_VMR_Line(getCtx(), vendorProdRef.getXX_VMR_Line_ID(), get_Trx());
				    int typeInventory = line.getXX_VMR_TypeInventory_ID();
				    
				    MOrder order = new MOrder(Env.getCtx(),0,null);
				    
				    if(inOutLineID != 0)
				    {
					    MInOutLine inOutLine = new MInOutLine(Env.getCtx(),inOutLineID,null);
					    MInOut inOut = new MInOut(Env.getCtx(),inOutLine.getM_InOut_ID(),null);
						order = new MOrder(Env.getCtx(), inOut.getC_Order_ID(),null);
				    }
				    
				    if(invoiceLineID != 0)
				    {
				    	MInvoiceLine invoiceLine = new MInvoiceLine(Env.getCtx(),invoiceLineID,null);
						MInvoice invoice = new MInvoice(Env.getCtx(),invoiceLine.getC_Invoice_ID(),null);
						order = new MOrder(Env.getCtx(), invoice.getC_Order_ID(),null);
				    }
				    
				    Env.getCtx().setContext("#FromProcess_Aux", "R");
				    
				    MProduct newProduct = new MProduct(getCtx(), vendorProdRef.getXX_VMR_Department_ID(), 
				    		vendorProdRef.getXX_VMR_Line_ID(), vendorProdRef.getXX_VMR_Section_ID(),vendorProdRefID,
				    		vendorProdRef.getC_TaxCategory_ID(),vendorProdRef.getXX_VME_ConceptValue_ID(),typeInventory, get_Trx());
				    newProduct.setName(vendorProdRef.getName());
				    newProduct.setXX_VMR_Category_ID(category);
				    newProduct.setXX_VMR_LongCharacteristic_ID(vendorProdRef.getXX_VMR_LongCharacteristic_ID());
				    newProduct.setXX_VMR_Brand_ID(vendorProdRef.getXX_VMR_Brand_ID());				    
				    newProduct.setXX_VMR_UnitConversion_ID(vendorProdRef.getXX_VMR_UnitConversion_ID());
				    newProduct.setXX_PiecesBySale_ID(vendorProdRef.getXX_PiecesBySale_ID());
				    newProduct.set_Value("XX_VMR_UnitPurchase_ID", vendorProdRef.getXX_VMR_UnitPurchase_ID());
				    newProduct.set_Value("XX_SaleUnit_ID", vendorProdRef.getXX_SaleUnit_ID());
				    newProduct.setC_Country_ID(order.getC_Country_ID());
				    newProduct.setIsActive(true);				    
				    newProduct.save();
				    
					Window[] List = AWindow.getWindows();
					
					for (int i = 0; i < List.length; i++) {
						
						if(List[i].getName().equals("UnsolicitedProduct")){
							List[i].dispose();
						}	
						
						if(List[i].isFocused()){
							List[i].setVisible(false);
						}					
					}
					
					get_Trx().commit();
					
					XX_UnsolicitedProduct_Form.loadMProduct(newProduct, ctx_aux);
					Env.getCtx().setContext("#XX_CheckupInOutLine",inOutLineID);
					Env.getCtx().setContext("#XX_UnsolicitePrd_InvoiceLine",invoiceLineID);
					Env.getCtx().setContext("#XX_FROMVENDORPRODREF",1);
					
					FormFrame form = new FormFrame();
					form.openForm(Env.getCtx().getContextAsInt("XX_L_FORMUNSOLPROD_ID"));
					AEnv.showCenterScreen(form);
					
					Env.getCtx().remove("#FromProcess_Aux");
					
				}// final del fromprocess	Rosmaira Arvelo			
			}
		}
		return save;
	}
	
	/**
	 *  cargar la linea del producto a chequear RArvelo
	 */
	public static void loadMInOutLine(Integer inOutLine_aux, Ctx ctx)
	{
		inOutLineID=inOutLine_aux;
		ctx_aux = ctx;		
	}  // InOutLine
	
	/**
	 *  cargar la linea del producto a facturar RArvelo
	 */
	public static void loadMInvoiceLine(Integer invoiceLine_aux, Ctx ctx)
	{
		invoiceLineID=invoiceLine_aux;
		ctx_aux = ctx;		
	}  // InvoiceLine
}
