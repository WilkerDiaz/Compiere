package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VMR_PriceConsecutive;
import compiere.model.cds.X_XX_VMR_PriceConsecutive;

public class XX_ImportPriceConsecutive extends SvrProcess {

	/** Client to be imported to */
	private int s_AD_Client_ID = 0;

	/** Delete old Imported */
	private boolean s_deleteOldImported = false;
	
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Calendar date = Calendar.getInstance(); ;
	Timestamp fecha = null;
	PreparedStatement Pstmt = null;
	
	protected String doIt() throws Exception {
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;
		String ts = null;
		// **** Prepare **** //
		try{
		// Delete Old Imported
		/*if (s_deleteOldImported) {
			sql = new StringBuffer("DELETE FROM I_XX_VMR_PriceConsecutive"
					+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(sql.toString(), get_TrxName());
			log.fine("Delete Old Imported =" + no);
		}*/
		sql = new StringBuffer("UPDATE I_XX_VMR_PriceConsecutive i "
				+ "SET M_Product_ID=(SELECT M_Product_ID FROM M_Product p "
				+ "WHERE p.Value = i.XX_PRODUCT_VALUE) "
				+ "WHERE i.M_Product_ID IS NULL " 
				+ "AND I_IsImported='N' AND i.XX_PRODUCT_VALUE IS NOT NULL");
				//.append(clientCheck);
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("SET M_Product_ID=" + no);
		
		sql = new StringBuffer("UPDATE I_XX_VMR_PRICECONSECUTIVE i "
		+ "  Set  M_AttributeSetInstance_ID = (select M_AttributeSetInstance_ID from XX_VMR_ProductBatch pb where pb.Lot = i.Lot and pb.ProductValue = i.XX_PRODUCT_VALUE)"
		+ "WHERE i.Lot is not null and i.Lot > 0 and i.M_AttributeSetInstance_ID is null " 
		+ "AND i.XX_PRODUCT_VALUE IS NOT NULL");
		no = DB.executeUpdate(get_TrxName(), sql.toString());
		log.fine("SET M_Product_ID=" + no);
		
		/*sql = new StringBuffer("UPDATE I_XX_VMR_PriceConsecutive i "
				+ "SET C_Order_ID=(SELECT C_Order_ID FROM C_Order "
				+ "WHERE i.XX_OrderNum = DOCUMENTNO AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.C_Order_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N' AND XX_OrderNum IS NOT NULL")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("SET C_Order_ID=" + no);*/
		
		/*sql = new StringBuffer("UPDATE I_XX_VMR_PriceConsecutive i "
				+ "SET XX_VMR_PriceConsecutive_ID=(SELECT XX_VMR_PriceConsecutive_ID FROM XX_VMR_PriceConsecutive "
				+ "WHERE i.Value=XX_PriceConsecutive"+""+" AND AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.XX_VMR_Category_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND I_IsImported='N'")
				.append(clientCheck);
		no = DB.executeUpdate(sql.toString(), get_TrxName());
		log.fine("Set Category ID=" + no);*/

		 ts = DB.isPostgreSQL() ? "COALESCE(I_ErrorMsg,'')"
				: "I_ErrorMsg"; // java bug, it could not be used directly
		
		commit();
		}catch (Exception e) {
			log.log(Level.SEVERE, "", e);
			e.printStackTrace();
			return "error importando";
		}
		int noInsert = 0;

		// Go through Records
		//log.fine("start inserting...");
		//sql = new StringBuffer("SELECT * FROM I_XX_Tabm11 "
		//		+ " WHERE I_IsImported='N'").append(clientCheck);
	   
		
		sql =new StringBuffer("select * from I_XX_VMR_PriceConsecutive where I_ISIMPORTED = 'N'");//.append(clientCheck);
		
		try {
			
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			X_I_XX_VMR_PriceConsecutive IPriceConse = null;
			X_XX_VMR_PriceConsecutive PriceConse = null;
			while (rs.next()) {
				IPriceConse = new X_I_XX_VMR_PriceConsecutive(getCtx(), rs, null);
				log.fine("I_XX_VMR_PriceConsecutive_ID=" + IPriceConse.getI_XX_VMR_PriceConsecutive_ID());

				// *** Create/Update Solm01 **** //

				// Pregunto por ID de la tabla compiere que es igual al de la
				// tabla I //
				if (IPriceConse.getXX_VMR_PriceConsecutive_ID() == 0) {
					
					PriceConse = new X_XX_VMR_PriceConsecutive(getCtx(), 0, null);
					
				} else {
					
					PriceConse = new X_XX_VMR_PriceConsecutive(getCtx(), IPriceConse.getXX_VMR_PriceConsecutive_ID(), get_TrxName());
					
				}

				// get and set
				if (IPriceConse.getM_Product_ID() > 0)
				     PriceConse.setM_Product_ID(IPriceConse.getM_Product_ID());
				
				if(IPriceConse.getValue()!=null && !(IPriceConse.getValue().equals("")))
					PriceConse.setXX_PriceConsecutive(Integer.parseInt(IPriceConse.getValue())); // Consecutivo de precio para un producto
				
				if (IPriceConse.getC_Order_ID() > 0)
					PriceConse.setC_Order_ID(IPriceConse.getC_Order_ID());
				
				PriceConse.setXX_SalePrice(IPriceConse.getXX_SalePrice());
				
				PriceConse.setXX_UnitPurchasePrice(IPriceConse.getXX_UnitPurchasePrice());
				
				PriceConse.setIsActive(true);
				
				PriceConse.setXX_ConsecutiveOrigin(IPriceConse.getXX_ConsecutiveOrigin());
				
				PriceConse.setM_AttributeSetInstance_ID(IPriceConse.getM_AttributeSetInstance_ID());
				
				
				//
				if (PriceConse.save()) {
					log.finest("Insert/Update XX_VMR_PriceConsecutive - "
							+ PriceConse.getXX_VMR_PriceConsecutive_ID());
					noInsert++;
					IPriceConse.setXX_VMR_PriceConsecutive_ID(PriceConse.getXX_VMR_PriceConsecutive_ID());
					IPriceConse.setI_IsImported(true);
					IPriceConse.setProcessed(true);
					IPriceConse.setProcessing(false);
					IPriceConse.save();
					System.out.println("Año:"+ IPriceConse.getXX_YearCreate()+"Mes:"+ IPriceConse.getXX_MonthCreate()+"Dia:"+IPriceConse.getXX_DayCreate());
			    	date.set(IPriceConse.getXX_YearCreate(), IPriceConse.getXX_MonthCreate() + 1, IPriceConse.getXX_DayCreate());
			    	fecha = new Timestamp(date.getTimeInMillis());
		          	String fecha1 = fecha.toString().substring(0, 10);
		          	System.out.println("fechaParaAlmacenar:"+fecha1);
		          
					String sql1 = "Update XX_VMR_PriceConsecutive set Created = TO_DATE('"+fecha1+"','yyyy-MM-dd') where XX_VMR_PriceConsecutive_ID = "+PriceConse.getXX_VMR_PriceConsecutive_ID();
					DB.executeUpdate(null,sql1);
				} else {
					rollback();
					noInsert--;
					sql = new StringBuffer("UPDATE I_XX_VMR_PriceConsecutive i "
							+ "SET I_IsImported='E', I_ErrorMsg=" + ts + "|| '")
							.append("Cannot Insert Tabm11...").append(
									"' WHERE I_XX_VMR_PriceConsecutive_ID=").append(
											IPriceConse.getI_XX_VMR_PriceConsecutive_ID());
					DB.executeUpdate( get_TrxName(), sql.toString());
					// continue;
				}
				//commit();
				
			} // end while

			rs.close();
			pstmt.close();

		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
			e.printStackTrace();
			rollback();
			return "error importando";
		}

		return "importado correctamente";
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null);

			else if (name.equals("AD_Client_ID"))
				s_AD_Client_ID = ((BigDecimal) element.getParameter())
						.intValue();
			else if (name.equals("DeleteOldImported"))
				s_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}

}
