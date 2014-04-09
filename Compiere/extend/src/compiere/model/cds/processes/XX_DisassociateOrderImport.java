package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MOrder;

public class XX_DisassociateOrderImport extends SvrProcess {

	Integer documentno_asoc;
	
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		//System.out.println(documentno_asoc);
		
		MOrder orden = new MOrder(getCtx(),documentno_asoc,get_TrxName());
		int guia = orden.getXX_VLO_BOARDINGGUIDE_ID();
		orden.setXX_VLO_BOARDINGGUIDE_ID(0);
		
		// seteo los monto prorateados en cero
	
		//@XX_ProcessedImport@='Y' | @XX_DefinitiveShippingDate@='Y'
		
		orden.setXX_FreightAgentInvoiceAmount(new BigDecimal(0));
		orden.setXX_AntiAmAgentPro(new BigDecimal(0));
		orden.setXX_NatTreasRealAmPro(new BigDecimal(0));
		orden.setXX_SenRealEstAmPro(new BigDecimal(0));
		orden.setXX_CustomAgentAmountPro(new BigDecimal(0));
		orden.setXX_NacInvoiceAmountPro(new BigDecimal(0));
		orden.setXX_InterFretRealAmountPro(new BigDecimal(0));
		orden.setXX_RealInsuranceAmPro(new BigDecimal(0));
		
		// pongo los montos estimado iguales a de los historicos

		orden.setXX_CARAGENTDELIVESTEMEDDATE(null);
		orden.setXX_BlockAgeDelEstDate(false);
		orden.setXX_CustDisEstiDatePO(orden.getXX_SHIPPREALESTEEMEDDATE());
		orden.setXX_EstArrivalDateToVzla(orden.getXX_VZLAARRIVALESTEMEDDATE());
		orden.setXX_CustDutEstPayDatePO(orden.getXX_CustDutEstPayDate());
		orden.setXX_CustDutEstShipDatePO(orden.getXX_CustDutEstShipDate());
		orden.setXX_CDArrivalEstimatedDatePO(orden.getXX_CDARRIVALESTEEMEDDATE());
		orden.save();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
		String SQL = ("DELETE FROM XX_VLO_DETAILGUIDE WHERE XX_VLO_BOARDINGGUIDE_ID = '"+getRecord_ID()+"' AND C_ORDER_ID = '"+documentno_asoc+"' ");
		pstmt = DB.prepareStatement(SQL, null); 
		pstmt.execute();
		pstmt.close();
		SQL="select count(*) as x FROM " +
				"XX_VLO_DETAILGUIDE a, c_order b " +
				"WHERE a.XX_VLO_BOARDINGGUIDE_ID=b.XX_VLO_BOARDINGGUIDE_ID and a.c_order_id=b.c_order_id " +
				"and a.XX_VLO_BOARDINGGUIDE_ID="+guia+" and b.C_BPARTNER_ID="+orden.getC_BPartner_ID();
		pstmt = DB.prepareStatement(SQL, null); 
		rs = pstmt.executeQuery(SQL);
		if (rs.next()){
			int x =rs.getInt("x");
			if (x==0)
				SQL="delete from XX_VLO_IMPORTCOSTDETAIL where XX_VLO_BOARDINGGUIDE_ID="+guia+" and C_BPARTNER_ID="+orden.getC_BPartner_ID();
			pstmt = DB.prepareStatement(SQL, null);
		}
		pstmt.execute();
		pstmt.close();
		}
		catch (Exception e) {
			System.out.println("Error al desasociar la orden");
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		
		
	return "Proceso Realizado";
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;	
			else if (name.equals("C_Order_ID"))
				documentno_asoc = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}// end for
		
	}// end prepare

}
