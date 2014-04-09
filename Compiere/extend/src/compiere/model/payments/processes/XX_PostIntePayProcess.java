package compiere.model.payments.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.cds.MBPartner;
import compiere.model.cds.MOrder;
import compiere.model.cds.Utilities;

/**
 * Proceso que se encarga de realizar algunas actualizaciones de la información en el servidor destino
 * NOTA: Solo se va a ejecutar cuando se haga el pase a producción del proyecto de pagos.
 * Nombre del Proceso en Compiere: 
 * @author Jessica Mendoza
 *  compiere.model.payments.processes.XX_PostIntegrationPayProcess
 */
public class XX_PostIntePayProcess extends SvrProcess{
	
	Utilities util = new Utilities(); 
	int diasBeneficio = 0;
	int diasTotalesI = 0;
	int diasTotalesII = 0;
	int diasTotalesIII = 0;
	Vector<Integer> condicionPago = new Vector<Integer>(6);
	Calendar calendarEstimadaI = Calendar.getInstance();
	Calendar calendarEstimadaII = Calendar.getInstance();
	Calendar calendarEstimadaIII = Calendar.getInstance();
	Timestamp fechaEstimada = new Timestamp(0);
	Timestamp fechaEstimadaS = new Timestamp(0);
	Timestamp fechaEstimadaT = new Timestamp(0);
	Vector<Timestamp> vector = new Vector<Timestamp>(3);

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
				
		//Calculo de la fecha de vencimiento para todas las facturas de mercancía
		//calDateDueInvoice();

		//Creación en las cuentas por pagar estimadas, de las O/C aprobadas y que su facturación no esté aprobada
		createdAccountPayEstimated();
		
		//Modificar el valor de la columna XX_Dispensable = 'Y', en las cuentas por pagar estimadas, 
		//de aquellas O/C que no tienen una factura asociada
		/*String update = "update XX_VCN_EstimatedAPayable " +
						"set XX_Dispensable = 'Y' " +
						"where C_Order_ID in (select ord.C_Order_ID " +
							"from C_Order ord " +
							"where not exists (select C_Order_ID " +
								"from C_Invoice " +
								"where C_Order_ID = ord.C_Order_ID and isSoTrx = 'N') " +
							"and ord.isSoTrx = 'N' " +
							"and ord.DocStatus = 'CO' " +
							"and ord.XX_OrderStatus = 'AP' )";
		DB.executeUpdate(null, update);*/
		
		return null;
	}
	
	/**
	 * Se encarga de calcular la fecha de vencimiento de las facturas de mercancía, 
	 * a través de la condición de pago
	 */
	public void calDateDueInvoice(){
		int i = 1;		
		String sqlFactura = "select inv.C_Invoice_ID, inv.C_BPartner_ID, inv.C_PaymentTerm_ID, " +
							"ord.C_Order_ID, ord.XX_OrderType " +
							"from C_Invoice inv " +
							"left outer join C_Order ord on (inv.C_Order_ID = ord.C_Order_ID " +
								"and ord.isSoTrx  = 'N') " +
							"where inv.isSoTrx = 'N' " +
							"and inv.C_Order_ID is not null " +
							"and inv.XX_DueDate is null " +
							"and inv.XX_DatePaid is null " +
							"and ord.XX_OrderStatus <> 'AN' ";
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sqlFactura, null); 
			rs = pstmt.executeQuery();
			while(rs.next()){		
				String updateFactura = "update C_Invoice set ";
				diasBeneficio = util.benefitVendorDayAdvance(rs.getInt(2));
				condicionPago = util.infoCondicionPago(rs.getInt(3));					
				
				if (rs.getString("XX_OrderType").equals("Nacional")){
					diasTotalesI = condicionPago.get(1) - diasBeneficio;
					diasTotalesII = condicionPago.get(4) - diasBeneficio;
					diasTotalesIII = condicionPago.get(7) - diasBeneficio;
				}else{
					diasTotalesI = condicionPago.get(1);
					diasTotalesII = condicionPago.get(4);
					diasTotalesIII = condicionPago.get(7);
				}
				
				vector = util.calcularFecha(rs.getInt(3),rs.getInt(4),"factura");
				if (vector.get(0) != null || vector.get(1) != null || vector.get(2) != null){
					if (condicionPago.get(0) == 100){
						fechaEstimada = vector.get(0);
						calendarEstimadaI.setTimeInMillis(fechaEstimada.getTime());
						calendarEstimadaI.add(Calendar.DATE, diasTotalesI);
						fechaEstimada = new Timestamp(calendarEstimadaI.getTimeInMillis());
						updateFactura = updateFactura + "XX_DueDate = (timestamp '" + fechaEstimada + "') , " +
										"XX_DatePaid = (timestamp '" + fechaEstimada + "') ";
					}else if ((condicionPago.get(0)+condicionPago.get(3)) == 100){
					fechaEstimadaS = vector.get(1);
						calendarEstimadaII.setTimeInMillis(fechaEstimadaS.getTime());
						calendarEstimadaII.add(Calendar.DATE, diasTotalesII);
						fechaEstimadaS = new Timestamp(calendarEstimadaII.getTimeInMillis());
						updateFactura = updateFactura + "XX_DueDate = (timestamp '" + fechaEstimadaS + "') , " +
										"XX_DatePaid = (timestamp '" + fechaEstimadaS + "') ";
					}else{
						fechaEstimadaT = vector.get(2);
						calendarEstimadaIII.setTimeInMillis(fechaEstimadaT.getTime());
						calendarEstimadaIII.add(Calendar.DATE, diasTotalesIII);
						fechaEstimadaT = new Timestamp(calendarEstimadaIII.getTimeInMillis());
						updateFactura = updateFactura + "XX_DueDate = (timestamp '" + fechaEstimadaT + "') , " +
										"XX_DatePaid = (timestamp '" + fechaEstimadaT + "') ";
					}
				}else{
					MOrder order = new MOrder(Env.getCtx(),rs.getInt(4),null);
					fechaEstimada = order.getXX_EntranceDate();
					updateFactura = updateFactura + "XX_DueDate = (timestamp '" + fechaEstimada + "') , " +
						"XX_DatePaid = (timestamp '" + fechaEstimada + "') ";
				}
				updateFactura = updateFactura + "where C_Invoice_ID = " + rs.getInt(1);
				DB.executeUpdate(null, updateFactura);
				System.out.println(i);
				i++;
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sqlFactura);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Se encarga de crear en las cuentas por pagar estimadas, aquellas O/C aprobadas y 
	 * que el estado de su facturación es distinto a aprobado
	 */
	public void createdAccountPayEstimated(){
		MOrder order;
		int i = 1;
		String sql = "select C_Order_ID " +
					 "from C_Order ord " +
					 "where ord.DocStatus = 'CO' " +
					 "and ord.XX_OrderStatus = 'AP' " +
					 "and ord.XX_InvoicingStatus  <> 'AP' " +
					 "and ord.IsSoTrx = 'N' " +
					 "and not exists (select C_Order_ID from XX_VCN_EstimatedAPayable where C_Order_ID = ord.C_Order_ID) ";
		
		//String sql = "select C_Order_ID from C_Order where documentNo in ('86064','86065','86066','86067','86068') ";

		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			while(rs.next()){	
				order = new MOrder(Env.getCtx(),rs.getInt(1),null);
				BigDecimal subTotal1 = new BigDecimal(0);
				BigDecimal subTotal2 = new BigDecimal(0);
				diasBeneficio = util.benefitVendorDayAdvance(order.getC_BPartner_ID());
				condicionPago = util.infoCondicionPago(order.getC_PaymentTerm_ID());					

				if (order.getXX_OrderType().equals("Nacional")){
					diasTotalesI = condicionPago.get(1) - diasBeneficio;
					diasTotalesII = condicionPago.get(4) - diasBeneficio;
					diasTotalesIII = condicionPago.get(7) - diasBeneficio;
				}else{
					diasTotalesI = condicionPago.get(1);
					diasTotalesII = condicionPago.get(4);
					diasTotalesIII = condicionPago.get(7);
				}
				
				vector = util.calcularFecha(order.getC_PaymentTerm_ID(),order.getC_Order_ID(),"estimada");						
			    if (vector.get(0) != null){
					fechaEstimada = vector.get(0);
					calendarEstimadaI.setTimeInMillis(fechaEstimada.getTime());
				    calendarEstimadaI.add(Calendar.DATE, diasTotalesI);
				    fechaEstimada = new Timestamp(calendarEstimadaI.getTimeInMillis());	
				}
				if (vector.get(1) != null){
					fechaEstimadaS = vector.get(1);
					calendarEstimadaII.setTimeInMillis(fechaEstimadaS.getTime());
				    calendarEstimadaII.add(Calendar.DATE, diasTotalesII);
				    fechaEstimadaS = new Timestamp(calendarEstimadaII.getTimeInMillis());
				}
				if (vector.get(2) != null){
					fechaEstimadaT = vector.get(3);
					calendarEstimadaIII.setTimeInMillis(fechaEstimadaT.getTime());
				    calendarEstimadaIII.add(Calendar.DATE, diasTotalesIII);
				    fechaEstimadaT = new Timestamp(calendarEstimadaIII.getTimeInMillis());
				}
				
				if (vector.get(0) == null){
					System.out.println("La O/C " + order.getDocumentNo() + " no tiene fecha estimada ");
				}else{
					Vector<Integer> vecI = new Vector<Integer>(9);
					Vector<String> vecS = new Vector<String>(2);
					vecI.add(order.getC_Order_ID());
					vecI.add(order.getC_BPartner_ID());
					vecI.add(order.getC_PaymentTerm_ID());
					vecI.add(order.getC_Currency_ID());
					MBPartner vendor = new MBPartner(Env.getCtx(), order.getC_BPartner_ID(), null);
					if (vendor.getXX_VendorType_ID() != null)
						vecI.add(vendor.getXX_VendorType_ID());
					else
						vecI.add(0);
					vecS.add(order.getXX_POType());
					vecI.add(order.getXX_VMR_DEPARTMENT_ID());
					vecS.add(order.getXX_OrderType());
					vecI.add(order.getC_Country_ID());
					vecI.add(order.getXX_Category_ID()); 
					vecI.add(order.getXX_ImportingCompany_ID());
							
					String type = "";
					String tipoCompra = order.getXX_PurchaseType();
					if (order.getXX_POType().equals("POM")){
						type = "% - Mercancia";
					}else{
						if (tipoCompra.equals("SU"))
							type = "Suministros y Materiales";
						else if (tipoCompra.equals("SE"))
							type = "Servicios";
						else 
							type = "Activos Fijos";
					}

					if (condicionPago.get(0) == 100){ 
						if (order.getXX_POType().equals("POM"))
							util.crearEstimacion(vecI, vecS, order.getGrandTotal(),fechaEstimada,"Orden",condicionPago.get(0)+type, 0);
					}else{
						BigDecimal total = new BigDecimal(0);
						if (order.getXX_POType().equals("POM")){
							total = order.getTotalLines();
						}else{
							total = order.getGrandTotal();
						}
						if (condicionPago.get(6) != 0){
							subTotal1 = order.getTotalLines().multiply(new BigDecimal(condicionPago.get(0))).divide(new BigDecimal(100));
							subTotal2 = order.getTotalLines().multiply(new BigDecimal(condicionPago.get(3))).divide(new BigDecimal(100));
							if (order.getXX_POType().equals("POM")){
								util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",condicionPago.get(0)+type, 0);
								util.crearEstimacion(vecI, vecS, subTotal2,fechaEstimadaS,"Orden",condicionPago.get(3)+type, 0);
								util.crearEstimacion(vecI, vecS, total.subtract(subTotal1.add(subTotal2)),fechaEstimadaT,"Orden",condicionPago.get(6)+type, 0);
							}else{
								util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",tipoCompra, 0);
								util.crearEstimacion(vecI, vecS, subTotal2,fechaEstimadaS,"Orden",tipoCompra, 0);
								util.crearEstimacion(vecI, vecS, total.subtract(subTotal1.add(subTotal2)),fechaEstimadaT,"Orden",tipoCompra, 0);
							}
						}else{
							subTotal1 = order.getTotalLines().multiply(new BigDecimal(condicionPago.get(0))).divide(new BigDecimal(100));
							if (order.getXX_POType().equals("POM")){
								util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",condicionPago.get(0)+type, 0);
								util.crearEstimacion(vecI, vecS, total.subtract(subTotal1),fechaEstimadaS,"Orden",condicionPago.get(3)+type, 0);
							}else{
								util.crearEstimacion(vecI, vecS, subTotal1,fechaEstimada,"Orden",tipoCompra, 0);
								util.crearEstimacion(vecI, vecS, total.subtract(subTotal1),fechaEstimadaS,"Orden",tipoCompra, 0);
							}
						}
					}
					i++;
					System.out.println(i);
				}				
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}			
	}
}
