package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MRole;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import compiere.model.cds.MOrder;
import compiere.model.cds.MVMRVendorProdRef;
import compiere.model.cds.X_XX_VLO_BoardingGuide;
import compiere.model.cds.X_XX_VLO_DateLeadTime;
import compiere.model.cds.X_XX_VLO_LTProcess;
import compiere.model.cds.X_XX_VLO_LeadTimes;


public class VLO_ProcessLeadTime extends SvrProcess {
	
	private Timestamp FRE = null;
	private Timestamp FREAC = null;
	private Timestamp FRLV = null;
	private Timestamp FRRCDA = null;
	private Timestamp FRDA = null;
	private Timestamp FRLCD = null;

	
	private String fechaFrom = new String();
	private String fechaUntil = new String();
	
	private Integer C_BPartner_ID = null;
	private Integer Country_ID = null;
	private Integer ArrivalPort_ID = null;
	
	private Integer BoardingGuide_ID = null;
	private Integer C_Order_ID = null;
	
	private Double TEI = new Double(0);
	private Double TT = new Double(0);
	private Double TRC = new Double(0);
	private Double TNAC = new Double(0);
	private Double TEN = new Double(0);
	private Double TLI = new Double(0);
	
	private X_XX_VLO_BoardingGuide BoardingGuide = null;
	private MOrder PO = null;
	
	private Double restarTimestamp(Timestamp a, Timestamp b) {	
		String sql = "select to_date('"+a.toString().substring(0, 19)+"', 'YYYY-MM-DD HH24:MI:SS') - to_date('"+b.toString().substring(0, 19)+"', 'YYYY-MM-DD HH24:MI:SS') aux from dual";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();

			if (rs.next()){
				return rs.getDouble("aux");
			}
			
		}catch (Exception e) {
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}	
		return null;
	}
	
	private Double calcularTiempos(Double tiempoAux){
		Double montoAux = new Double(0);
		
		String sql1 = "select sum(TotalLines)" +
			     "FROM C_ORDER " +
			     "WHERE XX_OrderType = 'Importada' " + 
			     "AND upper(XX_OrderStatus) = 'CH' " +
			     "AND XX_ARRIVALDATE BETWEEN TO_DATE("+fechaFrom+", 'YYYYMM') AND TO_DATE("+fechaUntil+", 'YYYYMM')";
		try {
			PreparedStatement pstmt1 = DB.prepareStatement(sql1, null);
			ResultSet rs1 = pstmt1.executeQuery();
			if (rs1.next()){
				montoAux = rs1.getDouble("sum(TotalLines)");
			}
			rs1.close();
			pstmt1.close();
		}catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		tiempoAux = tiempoAux/ montoAux;
		
		sql1 =   "select sum(TotalLines)" +
			     "FROM C_ORDER " +
			     "WHERE XX_OrderType = 'Importada' " + 
			     "AND upper(XX_OrderStatus) = 'RE' " +
			     "AND XX_ARRIVALDATE BETWEEN TO_DATE("+fechaFrom+", 'YYYYMM') AND TO_DATE("+fechaUntil+", 'YYYYMM') " +
			     "AND C_BPARTNER_ID = "+ C_BPartner_ID +" " +
			     "AND C_Country_ID = "+Country_ID+" " +
			     "AND XX_VLO_ArrivalPort_ID = "+ArrivalPort_ID+" ";
		try {
			PreparedStatement pstmt1 = DB.prepareStatement(sql1, null);
			ResultSet rs1 = pstmt1.executeQuery();
			if (rs1.next()){
				montoAux = rs1.getDouble("sum(TotalLines)");
			}
			rs1.close();
			pstmt1.close();
		} catch (Exception e) {
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		
		tiempoAux = tiempoAux / montoAux;
		return tiempoAux;
	}
	
	@Override
	protected String doIt() throws Exception {
		
		// Realizado por VLOMONACO. Reemplaza el codigo de PELLEGRINO que esta mas abajo comentado
		X_XX_VLO_LTProcess aux = new X_XX_VLO_LTProcess(getCtx(),getRecord_ID(),null);
		
		// Primero creamos la nueva cabecera con las fechas dadas
		X_XX_VLO_DateLeadTime cabeceraNueva = new X_XX_VLO_DateLeadTime(getCtx(),0, null);
		cabeceraNueva.setXX_DateFrom(aux.getXX_YearFrom()+"-"+aux.getXX_MonthFrom());
		cabeceraNueva.setXX_DateUntil(aux.getXX_YearUntil()+"-"+aux.getXX_MonthUntil());
		cabeceraNueva.save();
		
		// Luego copiamos el detalle (los lead time) del año justo anterior
	    // que son los lead times activos ya que los demas estaran inactivos
				
		String sql = "select XX_VLO_LeadTimes_ID " +
	     "FROM XX_VLO_LeadTimes " +
	     "WHERE isactive='Y'";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				X_XX_VLO_LeadTimes detalleNuevo = new X_XX_VLO_LeadTimes(getCtx(),0, null);
				X_XX_VLO_LeadTimes detalleViejo = new X_XX_VLO_LeadTimes(getCtx(),rs.getInt("XX_VLO_LeadTimes_ID"), null);
				detalleNuevo.setC_Country_ID(detalleViejo.getC_Country_ID());
				detalleNuevo.setC_BPartner_ID(detalleViejo.getC_BPartner_ID());
				detalleNuevo.setXX_VLO_ArrivalPort_ID(detalleViejo.getXX_VLO_ArrivalPort_ID());
				detalleNuevo.setXX_DATEH(cabeceraNueva.getXX_DateFrom());
				detalleNuevo.setXX_YEARH(cabeceraNueva.getXX_DateUntil());
				detalleNuevo.setXX_INTERNACARRIVALTIMETEI(detalleViejo.getXX_INTERNACARRIVALTIMETEI());
				detalleNuevo.setXX_TRANSITTIMETT(detalleViejo.getXX_TRANSITTIMETT());
				detalleNuevo.setXX_TIMEREGISTCELLATIONTRC(detalleViejo.getXX_TIMEREGISTCELLATIONTRC());
				detalleNuevo.setXX_NATIONALIZATIONTIMETNAC(detalleViejo.getXX_NATIONALIZATIONTIMETNAC());
				detalleNuevo.setXX_NACARRIVALTIMETEN(detalleViejo.getXX_NACARRIVALTIMETEN());
				detalleNuevo.setXX_IMPORTSLOGISTICSTIMETLI(detalleViejo.getXX_IMPORTSLOGISTICSTIMETLI());
				detalleViejo.setIsActive(false);
				detalleViejo.save();
				detalleNuevo.save();
			}
		} catch (SQLException e){
			System.out.println("Error al buscar los lead times");
		} finally {
			rs.close();
			pstmt.close();
		}
			
		// ponemos todas las demas cabeceras como inactivas
		sql = "UPDATE  XX_VLO_DateLeadTime SET isactive = 'N' " 
		+ " WHERE XX_VLO_DateLeadTime_ID <> " + cabeceraNueva.getXX_VLO_DateLeadTime_ID();

		PreparedStatement pstmt1 = DB.prepareStatement(sql, null);
	
		try {
			pstmt1.executeUpdate(sql);	
		} catch (SQLException e) {
			System.out.println("Mensaje de ERROR UPDATE " + e.getMessage());
			log.log(Level.SEVERE, sql, e);
			// e.printStackTrace();
		} finally {
			pstmt1.close();
		}

		// fin VLOMONACO
		
/**		String sql = "select distinct C_BPARTNER_ID, C_Country_ID, XX_VLO_ArrivalPort_ID " +
				     "FROM C_ORDER " +
				     "WHERE XX_OrderType = 'Importada' " + 
				     "AND upper(XX_OrderStatus) = 'CH' " +
				     "AND XX_ARRIVALDATE BETWEEN TO_DATE("+fechaFrom+", 'YYYYMM') AND TO_DATE("+fechaUntil+", 'YYYYMM')";
		
		sql = MRole.getDefault().addAccessSQL(
				sql, "", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RO);
		
		//System.out.println(sql);
		
		X_XX_VLO_DateLeadTime dateLeadTime = new X_XX_VLO_DateLeadTime(getCtx(),0,null);
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				C_BPartner_ID = rs.getInt("C_BPARTNER_ID");
				Country_ID = rs.getInt("C_Country_ID");
				ArrivalPort_ID = rs.getInt("XX_VLO_ArrivalPort_ID");
				
				String sql1 = "select C_ORDER_ID, XX_VLO_BOARDINGGUIDE_ID " +
						     "FROM C_ORDER " +
						     "WHERE XX_OrderType = 'Importada' " + 
						     "AND upper(XX_OrderStatus) = 'CH' " +
						     "AND XX_ARRIVALDATE BETWEEN TO_DATE("+fechaFrom+", 'YYYYMM') AND TO_DATE("+fechaUntil+", 'YYYYMM') " +
						     "AND C_BPARTNER_ID = " + C_BPartner_ID + " "+
						     "AND C_Country_ID = " + Country_ID + " "+
						     "AND XX_VLO_ArrivalPort_ID = " + ArrivalPort_ID + " ";
				
				//System.out.println(sql1);
				PreparedStatement pstmt1 = DB.prepareStatement(sql1, null);
				ResultSet rs1 = pstmt1.executeQuery();
				while(rs1.next()){
					
					C_Order_ID = rs1.getInt("C_ORDER_ID");
					BoardingGuide_ID = rs1.getInt("XX_VLO_BOARDINGGUIDE_ID");
					
					if (BoardingGuide_ID > 0){
						BoardingGuide = new X_XX_VLO_BoardingGuide(getCtx(), BoardingGuide_ID, null);
						PO = new MOrder(getCtx(), C_Order_ID, null);						
						
						//Set Fecha Real o Estimada de Embarque -- FRE
						if(BoardingGuide.getXX_SHIPPINGREALDATE() == null){
							FRE = BoardingGuide.getXX_SHIPPINGREALDATE();
						}else{
							FRE = PO.getXX_SHIPPREALESTEEMEDDATE();
						}
						
						//Set Fecha Real o Estimada Entrega Agente de Carga -- FREAC
						FREAC = PO.getXX_CARAGENTDELIVREALDATE();
						
						//Set Fecha Real o Estimada de Llegada a Vzla -- FRLV
						if(BoardingGuide.getXX_VZLAARRIVALREALDATE() == null){
							FRLV = BoardingGuide.getXX_VZLAARRIVALREALDATE();
						}else{
							FRLV = PO.getXX_EstArrivalDateToVzla();
						}
						
						//Set Fecha Real o Estimada de Registro o Cancelacion de Derechos de Aduana -- FRRCDA
						if(BoardingGuide.getXX_CUSTOMRIGHTSCANCELDATE() == null){
							FRRCDA = BoardingGuide.getXX_CUSTOMRIGHTSCANCELDATE();
						}else{
							FRRCDA = PO.getXX_CustDutEstPayDatePO();
						}
						
						//Set Fecha Real o Estimada de Despacho de Aduana -- FRDA
						if(BoardingGuide.getXX_REALDISPATCHDATE() == null){
							FRDA = BoardingGuide.getXX_REALDISPATCHDATE();							
						}else{
							FRDA = PO.getXX_CustDutEstShipDatePO();
							FRDA = PO.getXX_CustDutEstShipDate();
						}
						
						//Set Fecha Real o Estimada de Llegada a Centro de Distribucion - FRLCD
						if(BoardingGuide.getXX_CDARRIVALREALDATE() == null){
							FRLCD = BoardingGuide.getXX_CDARRIVALREALDATE();							
						}else{
							FRLCD = PO.getXX_CDArrivalEstimatedDatePO();
							//FRLCD = PO.getXX_CDArrivalEs();
						}
					}else{
						PO = new MOrder(getCtx(), C_Order_ID, null);
						
						//Set Fecha Real o Estimada de Embarque -- FRE
						FRE = PO.getXX_SHIPPREALESTEEMEDDATE();
						
						//Set Fecha Real o Estimada Entrega Agente de Carga -- FREAC
						FREAC = PO.getXX_CARAGENTDELIVREALDATE();
						
						//Set Fecha Real o Estimada de Llegada a Vzla -- FRLV
						FRLV = PO.getXX_EstArrivalDateToVzla();
						
						//Set Fecha Real o Estimada de Registro o Cancelacion de Derechos de Aduana -- FRRCDA
						FRRCDA = PO.getXX_CustDutEstPayDatePO();
						
						//Set Fecha Real o Estimada de Despacho de Aduana -- FRDA
						FRDA = PO.getXX_CustDutEstShipDatePO();
						
						//Set Fecha Real o Estimada de Llegada a Centro de Distribucion -- FRLCD
						FRLCD = PO.getXX_CDArrivalEstimatedDatePO();
					}
					
					/**
					 * CALCULO DE LOS TIEMPOS 
					 */
					/*System.out.println(FRE);
					System.out.println(FREAC);
					System.out.println(FRLV);
					System.out.println(FRRCDA);
					System.out.println(FRDA);
					System.out.println(FRLCD);*/
					
/**					TEI = TEI + restarTimestamp(FRE, FREAC);
					TT = TT + restarTimestamp(FRLV, FRE);
					TRC = TRC + restarTimestamp(FRRCDA, FRLV);
					TNAC = TNAC + restarTimestamp(FRDA, FRRCDA);
					TEN = TEN + restarTimestamp(FRLCD, FRDA);
				}
				rs1.close();
				pstmt1.close();
				
				TEI = calcularTiempos(TEI);
				TT = calcularTiempos(TT);
				TRC = calcularTiempos(TRC);
				TNAC = calcularTiempos(TNAC);
				TEN = calcularTiempos(TEN);
				
				TLI = TEI + TT + TRC + TNAC + TEN;
				
				X_XX_VLO_LeadTimes newLeadTime = new X_XX_VLO_LeadTimes(getCtx(),0,null);
				
				newLeadTime.setC_BPartner_ID(C_BPartner_ID);
				newLeadTime.setC_Country_ID(Country_ID);
				newLeadTime.setXX_VLO_ArrivalPort_ID(ArrivalPort_ID);
				newLeadTime.setXX_YEARH(fechaUntil.substring(0, 4)+"-"+fechaUntil.substring(4));
				newLeadTime.setXX_DATEH(fechaFrom.substring(0, 4)+"-"+fechaFrom.substring(4));
				
				newLeadTime.setXX_INTERNACARRIVALTIMETEI(new BigDecimal(TEI).setScale(0, BigDecimal.ROUND_HALF_UP));
				newLeadTime.setXX_TRANSITTIMETT(new BigDecimal(TT).setScale(0, BigDecimal.ROUND_HALF_UP));
				newLeadTime.setXX_TIMEREGISTCELLATIONTRC(new BigDecimal(TRC).setScale(0, BigDecimal.ROUND_HALF_UP));
				newLeadTime.setXX_NATIONALIZATIONTIMETNAC(new BigDecimal(TNAC).setScale(0, BigDecimal.ROUND_HALF_UP));
				newLeadTime.setXX_NACARRIVALTIMETEN(new BigDecimal(TEN).setScale(0, BigDecimal.ROUND_HALF_UP));
				
				newLeadTime.setXX_IMPORTSLOGISTICSTIMETLI(new BigDecimal(TLI).setScale(0, BigDecimal.ROUND_HALF_UP));

				newLeadTime.save();
			}
			rs.close();
			pstmt.close();
			
			dateLeadTime.setXX_DateFrom(fechaFrom.substring(0, 4)+"-"+fechaFrom.substring(4));
			dateLeadTime.setXX_DateUntil(fechaUntil.substring(0, 4)+"-"+fechaUntil.substring(4));
			dateLeadTime.save();
			
			
			return "";
		}
		catch (SQLException e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			return "";
		}
		*/
		
		return "";
		
	}

	@Override
	protected void prepare() {
		X_XX_VLO_LTProcess aux = new X_XX_VLO_LTProcess(getCtx(), getRecord_ID(), null);
		
		fechaFrom =  new Integer(aux.getXX_YearFrom()).toString()+ new Integer(aux.getXX_MonthFrom()).toString();
		fechaUntil = new Integer(aux.getXX_YearUntil()).toString() + new Integer(aux.getXX_MonthUntil()).toString() ;
	}

}
