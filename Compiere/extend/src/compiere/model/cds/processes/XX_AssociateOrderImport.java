package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.MOrder;
import compiere.model.cds.MVLODetailGuide;
import compiere.model.cds.X_XX_VLO_BoardingGuide;
import compiere.model.importcost.MImportCostDetail;

public class XX_AssociateOrderImport extends SvrProcess {

	Integer documentno_asoc;
	
	@Override
	protected String doIt() throws Exception {
		
		Integer guiaNro = 0, socioNro = 0;
		
//		and XX_IMPORTINGCOMPANY_ID in (select XX_IMPORTINGCOMPANY_ID from c_order where xx_vlo_boardingguide_id=)
		
		MOrder orden = new MOrder(getCtx(),documentno_asoc,get_TrxName());
		
		String sql2 = "select XX_ImportingCompany_ID from c_order where issotrx = 'N' and xx_vlo_boardingguide_id=" + getRecord_ID();
		PreparedStatement pstmt2 = DB.prepareStatement(sql2, null);
		ResultSet rs2 = pstmt2.executeQuery();
		try{
			while (rs2.next()) {
					if (rs2.getInt("XX_ImportingCompany_ID")!=orden.getXX_ImportingCompany_ID())
					{
						ADialog.info(1, new Container(), "No se puede asociar debido a que la compañía importadora es distinta al resto de la guía de embarque");
						return "";
					}
				}
		}
		catch (SQLException e)
		{
			System.out.println("Error " + e);
		} finally
		{
			DB.closeResultSet(rs2);
			DB.closeStatement(pstmt2);
		}

		X_XX_VLO_BoardingGuide aux = new X_XX_VLO_BoardingGuide(getCtx(), getRecord_ID(), null);
	
		guiaNro = aux.getXX_VLO_BoardingGuide_ID();
		socioNro = orden.getC_BPartner_ID();
		
		MVLODetailGuide detail = new MVLODetailGuide(getCtx(), 0, guiaNro, get_Trx());	
		
		orden.setXX_VLO_BOARDINGGUIDE_ID(guiaNro);

		// INSERTO LAS O/C QUE ESTAS ASOCIADAS A UNA G.E
		detail.setC_Order_ID(documentno_asoc);
	

		String sql = "select count(*) as x from XX_VLO_IMPORTCOSTDETAIL " +
				"where XX_VLO_BoardingGuide_id="+guiaNro+" and C_BPartner_id ="+socioNro;
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if(orden.save()){
				detail.save();
				if (rs.next()) {
					int x = rs.getInt("x");
					if (x==0){
						MImportCostDetail importCost = new MImportCostDetail(getCtx(), 0, null, guiaNro, socioNro);
						importCost.save();
					}
				}
			}
			rs.close();
			pstmt.close();
		}catch (SQLException e){e.printStackTrace();}			
		return "Proceso Realizado";
		
	}

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for(int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;	
			else if (name.equals("C_Order_ID"))
				documentno_asoc = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		
		}
	}

}
