package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_C_Orcm21;
import compiere.model.cds.X_XX_VMR_PO_LineRefProv;

/**
 * Import PO_LineRefProv from ORCM21
 * 
 * @author Patricia Ayuso
 */
public class ImportCOrderOrcm21 extends SvrProcess {

	/** Data to be imported to */
	private int s_AD_Client_ID = 0;
	
	/** Delete old Imported */
	private boolean s_deleteOldImported = false;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null);

			else if (name.equals("AD_Client_ID"))
				s_AD_Client_ID = ((BigDecimal) element.getParameter())
						.intValue();
			else if (name.equals("DeleteOldImported")) s_deleteOldImported =
				"Y".equals(element.getParameter()); else log.log(Level.SEVERE,
				"Unknown Parameter: " + name);
		}
	}
	
	/**
	 * Perform process.
	 * 
	 * @return Message
	 * @throws Exception
	 */
	@Override
	protected String doIt() throws Exception {
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;
		
		// Delete Old Imported		
		if (s_deleteOldImported) { sql = new StringBuffer
		("DELETE FROM I_XX_C_ORCM20 " +
		" WHERE I_IsImported='Y'").append(clientCheck); no =
		DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Delete Old Imported =" + no); }

		// Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer("UPDATE I_XX_C_ORCM21 "
				+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(
				s_AD_Client_ID).append(
				")," + " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
				+ " IsActive = COALESCE (IsActive, 'Y'),"
				+ " Created = COALESCE (Created, SysDate),"
				+ " CreatedBy = COALESCE (CreatedBy, 0),"
				+ " Updated = COALESCE (Updated, SysDate),"
				+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
				+ " I_ErrorMsg = NULL," + " I_IsImported = 'N' "
				+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Reset=" + no);

		//TODO:Revisar los IDs
		// Updating PoLineRefProv ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM21 i "
				+ "SET XX_VMR_PO_LINEREFPROV_ID=(SELECT XX_VMR_PO_LINEREFPROV_ID " 
				+ "FROM XX_VMR_PO_LINEREFPROV p "
				+ "WHERE p.AD_CLIENT_ID=i.AD_CLIENT_ID AND p.VALUE = i.XX_REFPRO) "
				+ "WHERE i.XX_VCN_Inventory_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL " 
				+ "AND i.XX_REFPRO IS NOT NULL AND i.I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set PO Line Reference Prov ID=" + no);
			
		//	Updating UnitPurchase ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM21 i " 
			+ " SET XX_VMR_UnitPurchase_ID = (SELECT XX_VMR_UnitPurchase_ID " 
			+ " FROM XX_VMR_UnitPurchase c "
			+ " WHERE i.XX_UNICOM = c.Value and c.IsActive = 'Y') "
			+ " WHERE i.XX_UNICOM IS NOT NULL"
			+ " AND i.I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Purchase ID=" + no);
		
		//	Updating UnitPurchase ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM21 i " 
			+ " SET XX_UnitSale_ID = (SELECT XX_VMR_UnitPurchase_ID " 
			+ " FROM XX_VMR_UnitPurchase c "
			+ " WHERE i.XX_UNIVEN = c.Value and c.IsActive = 'Y') "
			+ " WHERE i.XX_UNIVEN IS NOT NULL"
			+ " AND i.I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Sale ID=" + no);
		
		//	Updating Department ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM21 i " 
			+ " SET XX_Characteristic1_ID = (SELECT M_Attribute_ID " 
			+ " FROM M_Attribute d "
			+ " WHERE i.XX_TIPCAR1 = d.Value and d.IsActive = 'Y') "
			+ " WHERE i.XX_TIPCAR1 IS NOT NULL"
			+ " AND i.I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Characteristic 1 ID=" + no);
		
		//	Updating Department ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM21 i " 
			+ " SET XX_Characteristic2_ID = (SELECT M_Attribute_ID " 
			+ " FROM M_Attribute d "
			+ " WHERE i.XX_TIPCAR2 = d.Value and d.IsActive = 'Y') "
			+ " WHERE i.XX_TIPCAR2 IS NOT NULL"
			+ " AND i.I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Characteristic 2 ID=" + no);
		
		//TODO: revisar pues hay que obtener el departamento del maestro de la O/C
		//	Updating Line ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM21 i " 
			+ " SET XX_VMR_LINE_ID = (SELECT XX_VMR_LINE_ID FROM XX_VMR_LINE l "
			+ " WHERE i.XX_CodLin = l.Value and l.IsActive = 'Y' " 
			+ " AND l.XX_VMR_DEPARTMENT_ID = i.XX_VMR_DEPARTMENT_ID)"
			+ " WHERE i.XX_CodLin IS NOT NULL AND i.XX_VMR_DEPARTMENT_ID IS NOT NULL"
			+ " AND i.I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Line ID=" + no);
		
		//	Updating Section ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM21 i " 
			+ " SET XX_VMR SECTION_ID = (SELECT XX_VMR_SECTION_ID FROM XX_VMR_SECTION s "
			+ " WHERE i.XX_Codsec = s.Value and s.IsActive = 'Y' "
			+ " AND s.XX_VMR_LINE_ID = i.XX_VMR_LINE_ID)"
			+ " WHERE i.XX_Codsec IS NOT NULL AND i.XX_VMR_LINE_ID IS NOT NULL"
			+ " AND i.I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Section ID=" + no);
		
		//	Updating Tax Category ID
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM21 i " 
			+ " SET C_TaxCategory_ID = (SELECT C_TaxCategory_ID FROM C_TaxCategory p "
			+ " WHERE i.XX_TIPIMP = p.Value and p.IsActive = 'Y') "
			+ " WHERE i.XX_TIPIMP IS NOT NULL"
			+ " AND I_IsImported='N'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Tax Category ID=" + no);
		
		//	Reference Error
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  
				//java bug, it could not be used directly
		
		sql = new StringBuffer ("UPDATE I_XX_C_ORCM21 "
			+ " SET I_IsImported='E', I_ErrorMsg='ERR=No se encontro referencia,'||"+ ts
			+ " WHERE ((XX_VMR_CATEGORY_ID IS NULL) OR"
			+ " (XX_VMR_DEPARTMENT_ID IS NULL) OR "
			+ " (XX_VMR_LINE_ID IS NULL) OR"
			+ " (XX_VMR_SECTION_ID IS NULL) OR"
			+ " (M_PRODUCT_ID IS NULL) OR"
			+ " (M_WAREHOUSE_ID IS NULL))"
			+ " AND I_IsImported<>'Y' ").append(clientCheck); 
		no = DB.executeUpdate(get_Trx(), sql.toString());
		if (no != 0)
			log.warning("Invalid referencia=" + no);
		
		commit();
		// -----------------------------------------------------------------------------------

		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_C_ORCM21 "
				+ " WHERE I_IsImported='N'").append(clientCheck);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Integer docNo = rs.getInt("NUMORD");
				X_I_XX_C_Orcm21 impIorder = new X_I_XX_C_Orcm21(getCtx(), rs, get_TrxName());
				 log.fine("I_XX_C_ORCM21_ID=" + impIorder.getI_XX_C_ORCM21_ID());

				X_XX_VMR_PO_LineRefProv impXVMRPOLineRefProv = null;

				String SQL0 = "SELECT p.XX_VMR_PO_LINEREFPROV_ID AS id "
					+ " FROM C_ORDER o, XX_VMR_PO_LINEREFPROV p " 
					+ " WHERE o.C_ORDER_ID = p.C_ORDER_ID AND o.DOCUMENTNO = " + docNo;

				PreparedStatement pstmt0 = DB.prepareStatement(SQL0, null);
				ResultSet rs0;
				try {
					rs0 = pstmt0.executeQuery();

					// Si existe el NUMORD en C_Order...
					if (rs0.next()) {

						Integer XVMRPOLineRefProv_ID = rs0.getInt("id");
						impXVMRPOLineRefProv = new X_XX_VMR_PO_LineRefProv(getCtx(), XVMRPOLineRefProv_ID, get_TrxName());

					} else {

						impXVMRPOLineRefProv = new X_XX_VMR_PO_LineRefProv(getCtx(), 0, get_TrxName());
					}
					rs0.close();
					pstmt0.close();
					
					impXVMRPOLineRefProv.setC_TaxCategory_ID(impIorder.getC_TaxCategory_ID());
					 
					impXVMRPOLineRefProv.setValue(impIorder.getXX_REFPRO());
					
					impXVMRPOLineRefProv.setXX_UnitPurchasePrice(BigDecimal.valueOf(impIorder.getXX_MCOSTO()));
					
					impXVMRPOLineRefProv.setPriceActual(BigDecimal.valueOf(impIorder.getXX_MCOSVE()));
					
					impXVMRPOLineRefProv.setSaleQty(impIorder.getXX_CANEMV());
					
					impXVMRPOLineRefProv.setXX_Margin(BigDecimal.valueOf(impIorder.getXX_MARGEN()));
					
					impXVMRPOLineRefProv.setXX_VMR_Line_ID(impIorder.getXX_VMR_Line_ID());
					
					impXVMRPOLineRefProv.setXX_PackageMultiple(impIorder.getXX_MULEMP());
					
					impXVMRPOLineRefProv.setXX_VMR_UnitConversion_ID(impIorder.getXX_CANEMC());

					impXVMRPOLineRefProv.setXX_PiecesBySale_ID(impIorder.getXX_CANEMV());
					
					impXVMRPOLineRefProv.setXX_VMR_UnitPurchase_ID(impIorder.getXX_VMR_UnitPurchase_ID());
					
					impXVMRPOLineRefProv.setXX_SaleUnit_ID(impIorder.getXX_SaleUnit_ID());
					
					impXVMRPOLineRefProv.setXX_Rebate1(BigDecimal.valueOf(impIorder.getXX_DESCU1()));
					
					impXVMRPOLineRefProv.setXX_Rebate2(BigDecimal.valueOf(impIorder.getXX_DESCU2()));
					
					impXVMRPOLineRefProv.setXX_Rebate3(BigDecimal.valueOf(impIorder.getXX_DESCU3()));
					
					impXVMRPOLineRefProv.setXX_Rebate4(BigDecimal.valueOf(impIorder.getXX_DESCU4()));
					
					impXVMRPOLineRefProv.setXX_SalePrice(BigDecimal.valueOf(impIorder.getXX_MVENTA()));
					
					impXVMRPOLineRefProv.setXX_Characteristic1_ID(impIorder.getXX_Characteristic1_ID());
					
					impXVMRPOLineRefProv.setXX_Characteristic2_ID(impIorder.getXX_Characteristic2_ID());
										
					impXVMRPOLineRefProv.setXX_TaxAmount(BigDecimal.valueOf(impIorder.getXX_IMPVEN()));
					
					//CAMPO GENERADO
					impXVMRPOLineRefProv.setXX_SalePricePlusTax(impXVMRPOLineRefProv.getXX_SalePrice().add(impXVMRPOLineRefProv.getXX_TaxAmount()));

					impXVMRPOLineRefProv.setXX_VMR_Section_ID(impIorder.getXX_VMR_Section_ID());
										
					/*OJO DATOS DE ORCD22
					int aux[][] = getCharacterist( impXVMRPOLineRefProv.get_ID(), impIorder.getTIPCAR1(), impIorder.getTIPCAR2());
					
					if (aux[0][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value1_ID(aux[0][0]);
					if (aux[1][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value2_ID(aux[1][0]);
					if (aux[2][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value3_ID(aux[2][0]);
					if (aux[3][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value4_ID(aux[3][0]);
					if (aux[4][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value5_ID(aux[4][0]);
					if (aux[5][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value6_ID(aux[5][0]);
					if (aux[6][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value7_ID(aux[6][0]);
					if (aux[7][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value8_ID(aux[7][0]);
					if (aux[8][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value9_ID(aux[8][0]);
					if (aux[9][0] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic1Value10_ID(aux[9][0]);
					if (aux[0][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value1_ID(aux[0][1]);
					if (aux[1][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value2_ID(aux[1][1]);
					if (aux[2][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value3_ID(aux[2][1]);
					if (aux[3][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value4_ID(aux[3][1]);
					if (aux[4][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value5_ID(aux[4][1]);
					if (aux[5][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value6_ID(aux[5][1]);
					if (aux[6][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value7_ID(aux[6][1]);
					if (aux[7][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value8_ID(aux[7][1]);
					if (aux[8][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value9_ID(aux[8][1]);
					if (aux[9][1] != -1)
						impXVMRPOLineRefProv.setXX_Characteristic2Value10_ID(aux[9][1]);
					*/				
					
					//OJO GENERAR DATOS DEL "LINE"
					
					if (impXVMRPOLineRefProv.save()) {
						System.out.println("datos salvados...");

						log.finest("Insert/Update Order - "
								+ impXVMRPOLineRefProv.getC_Order_ID());
						noInsert++;

						impIorder.setI_IsImported("'Y'");
						impIorder.setProcessed(true);
						impIorder.setProcessing(false);
						impIorder.save();

					} else {
						rollback();
						noInsert--;
						sql = new StringBuffer("UPDATE I_XX_C_ORCM21 i "
								+ "SET I_IsImported='E', I_ErrorMsg=" + ts
								+ "|| '").append("Cannot Insert Order...")
								.append("' WHERE I_XX_C_ORCM21_ID=").append(
										impIorder.getI_XX_C_ORCM21_ID());
						DB.executeUpdate(get_Trx(), sql.toString());
						// continue;
					}

					commit();
						
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}

			} // end while

			rs.close();
			pstmt.close();

		} // end try

		catch (SQLException e) {
			log.log(Level.SEVERE, "", e);
			rollback();
		}

		// Set Error to indicator to not imported
		sql = new StringBuffer("UPDATE I_XX_C_ORCM21 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), /* OJO */
				"@C_Order_ID@: @Inserted@");

		//System.out.println(sql);
		return "";

	}
	
	public int[][] getCharacterist(Integer id, Integer value1, Integer value2) {
		
		int aux[][] = new int[9][2];
		
		String sql = "SELECT i.CARAC1 as val1, i.CARAC2 as val2 " 
			+ " FROM I_XX_C_ORCD22 i, C_ORDER o, XX_VMR_PO_LINEREFPROV l"
			+ " WHERE o.C_ORDER_ID = l.C_ORDER_ID AND o.DOCUMENTNO = i.NUMORD " 
			+ " AND l.XX_VMR_PO_LINEREFPROV_ID = " + id;
		System.out.println(sql);	
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();
	
			Integer val1, val2, i = 0;
			while (rs.next()) {								
				val1 = rs.getInt("val1");
				val2 = rs.getInt("val2");
				
				aux[i][0] = val1;
				aux[i][1] = val2;
				i++;
				
				//System.out.println(i + " val1 " + val1 + " val2 " + val2);
			}	
			rs.close();
			pstmt.close();
			
			for (int j = i; j < aux.length; j++) {
				aux[j][0] = -1;
				aux[j][1] = -1;
				
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return aux;
	}
	
}
