package compiere.model.cds.processes;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import org.compiere.model.X_M_Movement;
import org.compiere.process.DocumentEngine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import compiere.model.cds.MBPartner;
import compiere.model.cds.MInvoice;
import compiere.model.cds.MMovement;
import compiere.model.cds.MMovementLine;
import compiere.model.cds.MProduct;
import compiere.model.cds.Utilities;
import compiere.model.cds.X_Ref_XX_StatusReturn;
import compiere.model.cds.X_Ref_XX_TransferStatus;
import compiere.model.cds.X_XX_VLO_ReturnDetail;
import compiere.model.cds.X_XX_VLO_ReturnOfProduct;

//Programa Modificado por WDiaz. Aprobar Devoluciones de Tiendas

public class XX_ApproveReturn extends SvrProcess {

	int producto = 0;
	@Override
	protected String doIt() throws Exception {
		
		Utilities u =  null;
		
		int total = 0;
		int aux = 0;
		MMovement movimiento_viejo = new MMovement(getCtx(), getRecord_ID(), null);						
		MMovement movimiento = new MMovement(getCtx(), 0, get_TrxName());
		MMovement movimientoDevTienda = new MMovement(getCtx(), 0, get_TrxName());
		X_XX_VLO_ReturnDetail detail = null;
		boolean crearCabeceraDev = true, crearCabeceraMov = true;
		System.out.println("	Dev# "+movimiento_viejo.getDocumentNo()+" ");
		X_XX_VLO_ReturnOfProduct preturn = null;
		MProduct product = null;				
		//Agregado por Javier - Crea los detalles de devolucion -No aplica a traspasos
    	String create_returns = "SELECT * FROM M_MOVEMENTLINE WHERE M_MOVEMENT_ID = " + getRecord_ID(); 
    	PreparedStatement ps_returns = DB.prepareStatement(create_returns, get_TrxName());
    	ResultSet rs_returns = ps_returns.executeQuery();
    	int return_of_product = 0;
    	while (rs_returns.next()) {
    		/*if (producto == 0)
    			producto = rs_returns.getInt("M_Product_ID");
    		else
    			continue;*/
    		if(crearCabeceraMov && rs_returns.getInt("XX_QuantityReceived") > 0)
    		{
    			copyMovement(movimiento_viejo, movimiento, true);
    			crearCabeceraMov = false;
    		}
    		product = new MProduct(getCtx(), rs_returns.getInt("M_PRODUCT_ID"), get_TrxName());	
    		
    		if (return_of_product == 0 && rs_returns.getInt("XX_QuantityReceived") > 0) {
    			
    			preturn = new X_XX_VLO_ReturnOfProduct(getCtx(), 0 ,get_TrxName());					    		
	    		preturn.setXX_Status(X_Ref_XX_StatusReturn.PENDIENTE_POR_RETIRAR.getValue());
	    		preturn.setC_BPartner_ID(product.getC_BPartner_ID());						    		
	    		preturn.setXX_ReturnedFrom("ST");
	    		preturn.setM_Warehouse_ID(movimiento_viejo.getM_WarehouseFrom_ID());
	    		preturn.setM_Movement_ID(getRecord_ID());
	    		preturn.setXX_NotificationDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
	    		preturn.setXX_DocumentNo(movimiento_viejo.getDocumentNo());
	    		preturn.save();
	    		preturn.load(get_TrxName());
	    		return_of_product = preturn.get_ID();
    		}
	        if (rs_returns.getInt("XX_QuantityReceived") > 0)
	    	    {
	    		//Se llena el detalle de devolucion				    		
	    		detail = new X_XX_VLO_ReturnDetail(getCtx(), 0, get_TrxName());
	    		detail.setXX_VLO_ReturnOfProduct_ID(return_of_product);
	    		detail.setM_Product_ID(product.get_ID());
	    		detail.setXX_VMR_CancellationMotive_ID(rs_returns.getInt("XX_RETURNMOTIVE_ID"));
	    		//detail.setXX_TotalPieces(rs_returns.getInt("MOVEMENTQTY"));
	    		detail.setXX_TotalPieces(rs_returns.getInt("XX_QuantityReceived"));
	    		detail.setC_TaxCategory_ID(rs_returns.getInt("C_TaxCategory_ID"));
	    		detail.setPriceActual(rs_returns.getBigDecimal("PRICEACTUAL"));
	    		detail.setTaxAmt(rs_returns.getBigDecimal("TAXAMT").multiply(new BigDecimal(rs_returns.getInt("XX_QuantityReceived"))));
	    		detail.setM_AttributeSetInstance_ID(rs_returns.getInt("M_ATTRIBUTESETINSTANCE_ID"));
	    		if (!detail.save())
	    			System.out.println("no guardo");
	    		//Insertamos la Linea del Movimiento
	    			copyMovementLine(rs_returns, movimiento, rs_returns.getBigDecimal("XX_QuantityReceived"));
	    	    }
    	    //
    		aux = rs_returns.getInt("MOVEMENTQTY") - rs_returns.getInt("XX_QuantityReceived");
    		
    		total = total + rs_returns.getInt("XX_QuantityReceived");
    		
    		if (aux > 0)
    		{
    			//Hacer el Movimiento a la tienda que envio la merca
    			if(crearCabeceraDev){
    				
    				copyMovement(movimiento_viejo, movimientoDevTienda, false);
    				crearCabeceraDev = false;
    			}
    			copyMovementLine(rs_returns, movimientoDevTienda, new BigDecimal(aux));
    		}
    	}
    	rs_returns.close();
    	ps_returns.close();
    
    	if (!crearCabeceraMov)
    	{
	    	//Ahora se completa el movimiento				    	
			movimiento.setXX_DispatchDate(movimiento.getUpdated());
			movimiento.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENCD.getValue());
			movimiento.save();
			//Aprueba el movimento para que que pase al locator en transito de la tienda
			movimiento.setDocAction(X_M_Movement.DOCACTION_Complete);
			DocumentEngine.processIt(movimiento, X_M_Movement.DOCACTION_Complete);
		    
		    SendEmailVendor(return_of_product);
    	}
    	
    	if (!crearCabeceraDev)
    	{
	    	//Ahora se completa el movimiento				    	
    		movimientoDevTienda.setXX_DispatchDate(movimiento.getUpdated());
    		movimientoDevTienda.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENCD.getValue());
    		movimientoDevTienda.save();
			//Aprueba el movimento para que que pase al locator en transito de la tienda
    		movimientoDevTienda.setDocAction(X_M_Movement.DOCACTION_Complete);
    		DocumentEngine.processIt(movimientoDevTienda, X_M_Movement.DOCACTION_Complete);				    
    	}
	    
	    if (movimiento.getDocStatus().equals(MMovement.DOCSTATUS_Completed) || movimientoDevTienda.getDocStatus().equals(MMovement.DOCSTATUS_Completed)) {
	    	//Modificar el viejo
		    movimiento_viejo.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENCD.getValue());
		    movimiento_viejo.save();
		    if (movimiento.getDocStatus().equals(MMovement.DOCSTATUS_Completed)){
					//Generar Factura y Purchase's Book
				    int invoiceAux = new Utilities().generateInvoice(get_TrxName(),movimiento_viejo, producto, preturn);
				    new Utilities().generatePurchaseBook( get_TrxName(), movimiento_viejo, producto, invoiceAux);
				    
				    u = new Utilities();
				    MInvoice nota = new MInvoice(getCtx(), invoiceAux, null);
				    Vector<File> files = new Vector<File>(); 
				    files.add(u.reportDebCred(nota, false));
				    try{
				    	u.sendEmail(null , new MBPartner(getCtx(),  nota.getC_BPartner_ID(), null), files, 2);//(new MInvoice(getCtx(), invoiceAux, null));
				    }catch (Exception e) {
						log.log(log.getLevel(), "error enviando correo. Correo Invalido"); 
					}
				    
				    //actualiza el return of products
				    String sql = ("UPDATE XX_VLO_ReturnOfProduct SET C_INVOICE_ID = "+invoiceAux+" WHERE XX_VLO_ReturnOfProduct_ID = " + preturn.get_ID());			
					DB.executeUpdate(null, sql); 
		    }
	   } else {					    	
	    	log.saveError("Error", Msg.translate(getCtx(), "XX_ReceptionFail"));
	   }
	    
	    if(total==0){
	    	movimiento_viejo.setXX_Status(X_Ref_XX_TransferStatus.ANULADO.getValue());
	    	movimiento_viejo.save();
	    }
	    
	return "Movimiento Aprobado";
	}

	@Override
	protected void prepare() {

	}
	
	private void copyMovement(MMovement movimiento_viejo, MMovement movimiento, boolean locatorDevolucion)
	{
		int locatorDesde = 0, locatorHacia = 0;
		//Se deben copiar los movimientos
		movimiento.setC_DocType_ID(getCtx().getContextAsInt("#XX_L_MOVEMENTCD_ID"));
		movimiento.setDescription(movimiento_viejo.getDescription());
		movimiento.setMovementDate(movimiento_viejo.getMovementDate());
		
		//Hallar el locator desde
		 locatorDesde = Utilities.obtenerLocatorEnTransito(movimiento_viejo.getM_WarehouseTo_ID()).getM_Locator_ID();						
		 movimiento.setM_Locator_ID(locatorDesde);
		 
		 if(locatorDevolucion)
		 { 
			 locatorHacia = getCtx().getContextAsInt("#XX_L_LOCATORCDDEVOLUCION_ID");
			 movimiento.setM_WarehouseFrom_ID(movimiento_viejo.getM_WarehouseFrom_ID());
			 movimiento.setM_WarehouseTo_ID(movimiento_viejo.getM_WarehouseTo_ID());
		 }
		 else
		 {
			 locatorHacia =Utilities.obtenerLocatorEnTienda(movimiento_viejo.getM_WarehouseFrom_ID()).getM_Locator_ID();
			 movimiento.setM_WarehouseFrom_ID(movimiento_viejo.getM_WarehouseTo_ID());
			 movimiento.setM_WarehouseTo_ID(movimiento_viejo.getM_WarehouseFrom_ID());
		 }
		 movimiento.setM_LocatorTo_ID(locatorHacia);
		//El locator hasta dado que es retorno es el locator devolucion
		//movimiento.setM_LocatorTo_ID(getCtx().getContextAsInt("#XX_L_LOCATORCDDEVOLUCION_ID"));
		
		//El resto de los campos
		movimiento.setXX_VMR_Department_ID(movimiento_viejo.getXX_VMR_Department_ID());
		movimiento.setXX_DispatchDate(movimiento_viejo.getXX_DispatchDate());
		movimiento.setXX_PackageQuantity(movimiento_viejo.getXX_PackageQuantity());
		movimiento.setXX_RequestDate(movimiento_viejo.getXX_RequestDate());
		movimiento.save(get_TrxName());
		commit();
		movimiento.load(get_TrxName());
	}
	
	private void copyMovementLine(ResultSet rs_returns, MMovement movimiento, BigDecimal cantProd)
	{
		try{
				MMovementLine linea = new MMovementLine(getCtx(), 0, get_TrxName());
				linea.setDescription(rs_returns.getString("DESCRIPTION"));
				linea.setConfirmedQty(rs_returns.getBigDecimal("CONFIRMEDQTY"));
				linea.setM_AttributeSetInstanceTo_ID(rs_returns.getInt("M_ATTRIBUTESETINSTANCETO_ID"));
				linea.setM_AttributeSetInstance_ID(rs_returns.getInt("M_ATTRIBUTESETINSTANCETO_ID"));
				linea.setM_LocatorTo_ID(movimiento.getM_LocatorTo_ID());				    		
				linea.setM_Locator_ID(movimiento.getM_Locator_ID());
				linea.setM_Movement_ID(movimiento.get_ID());
				linea.setM_Product_ID(rs_returns.getInt("M_PRODUCT_ID"));
				if (this.producto == 0)
				    this.producto = rs_returns.getInt("M_PRODUCT_ID");
				//linea.setMovementQty(rs_returns.getBigDecimal("MOVEMENTQTY"));
				linea.setMovementQty(cantProd);
				
				if (rs_returns.getInt("XX_VMR_BRAND_ID") != 0)
					linea.setXX_VMR_Brand_ID(rs_returns.getInt("XX_VMR_BRAND_ID"));
				linea.setXX_VMR_Line_ID(rs_returns.getInt("XX_VMR_Line_ID"));
				linea.setXX_VMR_Section_ID(rs_returns.getInt("XX_VMR_Section_ID"));
				linea.setXX_SalePrice(rs_returns.getBigDecimal("XX_SalePrice"));
				linea.setXX_ReturnMotive_ID(rs_returns.getInt("XX_ReturnMotive_ID"));
				linea.setQtyRequired(rs_returns.getBigDecimal("QtyRequired"));
				linea.setC_TaxCategory_ID(rs_returns.getInt("C_TaxCategory_ID"));
				linea.setPriceActual(rs_returns.getBigDecimal("PRICEACTUAL"));
				linea.setTaxAmt(rs_returns.getBigDecimal("TAXAMT"));
				linea.save();
				commit();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void SendEmailVendor(int devolucion)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Utilities f = null;
		String sql = "SELECT a.Value, C_BPartner_ID, SUM(XX_TOTALPIECES) as pieces " +
		 "FROM XX_VLO_RETURNOFPRODUCT a, XX_VLO_RETURNDETAIL b " +
		 "WHERE a.XX_VLO_RETURNOFPRODUCT_ID=b.XX_VLO_RETURNOFPRODUCT_ID " +
		 "AND a.XX_VLO_RETURNOFPRODUCT_ID =" + devolucion +
		 " GROUP BY a.VALUE, C_BPartner_ID ";
		
		try
		{
			pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();			
		    
			while (rs.next())
			{		
				int numDev = rs.getInt("Value");
				int pieces = rs.getInt("pieces");
				int vendor = rs.getInt("C_BPartner_ID");
				
				int contacto = buscarContacto(vendor); 
				
				MBPartner proveedor = new MBPartner(getCtx(), vendor, null);
				
				String Mensaje= "\n " + proveedor.getName() + " \n \n Estimados Señores \n \n"
				  +" Por medio de la presente comunicación, le informamos que tiene un total de "+ pieces
				  + " Piezas Devueltas " +"bajo la Devolucion No- "+numDev + ". El plazo para el retiro de la mercancía es de quince (15) días hábiles contados a partir de la fecha de hoy. " 
				  + "\n \n La entrega se realizará en las instalaciones de CENTROBECO, de acuerdo a lo estipulado en las condiciones impresas en la Orden de Compra correspondiente."
				  + "\n \n La persona que retire la mercancía deberá presentar Carta de Autorización que contenga la información que a continuación se indica: Membrete, firma y sello de " 
				  +	"la Compañía, nombre y cédula de identidad del autorizado y de quien autoriza, fecha idéntica al día del retiro."
				  + "\n \n Vencido el plazo para el retiro de la mercancía, CENTROBECO, C.A. descontará un cinco por ciento (5%) del valor total de la factura de los productos devueltos " 
				  +	"por concepto de gastos de almacenaje. "
				  + "\n \n Nota: Éste es un mensaje automático, por favor no responder.";
				
				//Envio correo al contacto del proveedor 
				if (contacto != 0)
				     f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_VENDORRETURN_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), -1, contacto, null);
				else
					 f = new Utilities(Env.getCtx(), null, Env.getCtx().getContextAsInt("#XX_L_MT_VENDORRETURN_ID"), Mensaje, -1, Env.getCtx().getContextAsInt("#XX_L_USERFROMMAIL_ID"), vendor, -1, null);
				
				try {
					f.ejecutarMail();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	private int buscarContacto(int vendor) {
		
		String sql = "select AD_User_ID from AD_User where C_BPartner_ID = ? and XX_ContactType = ? ";
		PreparedStatement pstmt = DB.prepareStatement(sql, null); 
		ResultSet rs = null;
	    try {
	    	pstmt.setInt(1, vendor);
	    	pstmt.setInt(2, Env.getCtx().getContextAsInt("#XX_L_CONTACTTYPESALES"));
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		return 0;
	}
}
