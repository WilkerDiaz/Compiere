package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_XX_VMR_PO_LineRefProv;

/**
 * Import Reference Matrix from ORCD22
 * 
 * @author Patricia Ayuso
 */
public class ImportRefMatrixOrcd22 extends SvrProcess {
	
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
		sql = new StringBuffer("UPDATE I_XX_C_ORCD22 "
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

		commit();
		// -----------------------------------------------------------------------------------

		//Updating ProdcutID 
		sql = new StringBuffer ("UPDATE I_XX_C_ORCD22 i"
				+ " SET XX_CODPRO = (SELECT M_PRODUCT_ID FROM M_PRODUCT m"
				+ " WHERE m.VALUE = i.XX_CODPRO "
				+ " AND m.AD_Client_ID = i.AD_Client_ID) "
				+ " WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Set Product ID = " + no);		

		int noInsert = 0;

		// Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer("SELECT * FROM I_XX_C_ORCD22 "
				+ " WHERE I_IsImported='N'").append(clientCheck);

		System.out.println(sql);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				//Getting data...
				Integer numOrd = rs.getInt("DOCUMENTNO");
				System.out.println("numOrd " + numOrd);
				Integer prodID = rs.getInt("XX_CODPRO");
				String carac1 = rs.getString("XX_CARAC1");
				String carac2 = rs.getString("XX_CARAC2");
				Integer canCom = rs.getInt("XX_CANCOM");
				Integer canVen = rs.getInt("XX_CANVEN");
				Integer canObs = rs.getInt("XX_CANOBS");
				
				//Finding ID
				String SQL0 = "SELECT p.XX_VMR_PO_LINEREFPROV_ID AS ID "
					+ " FROM C_ORDER o, XX_VMR_PO_LINEREFPROV p " 
					+ " WHERE o.C_ORDER_ID = p.C_ORDER_ID AND o.DOCUMENTNO = " + numOrd;

				//System.out.println(SQL0);
	
				PreparedStatement pstmt0 = DB.prepareStatement(SQL0, null);
				ResultSet rs0;
				try {
					rs0 = pstmt0.executeQuery();
	
					if (rs0.next()) {						
						Integer poLineRefProvID = rs0.getInt("ID");
						//System.out.println("id " + poLineRefProvID);
						
						//Converting carac1 and carac2 to ID
						String value1, value2;
						value1 = (carac1.subSequence(3, carac1.length()).toString());
						value2 = (carac2.subSequence(3, carac2.length()).toString());
						
						//Initialising to avoid compiling errors
						Integer value1_ID = Integer.parseInt(value1);
						Integer value2_ID= Integer.parseInt(value2);
						
						//Finding Characteristic Value IDs
						sql = new StringBuffer ("SELECT M_ATTRIBUTEVALUE_ID " 
								+ " FROM M_ATTRIBUTEVALUE" 
								+ " WHERE VALUE = " + value1);
						
						pstmt = DB.prepareStatement(sql.toString(), get_Trx());
						rs = pstmt.executeQuery();
						if(rs.next()){
							value1_ID = rs.getInt("M_ATTRIBUTEVALUE_ID");
							
							//Updating ID 
							sql = new StringBuffer ("UPDATE I_XX_C_ORCD22 i"
									+ " SET CARAC1 = " + value1_ID
									+ " WHERE I_IsImported<>'Y' AND CARAC1 = "
									+ carac1);
								no = DB.executeUpdate(get_Trx(), sql.toString());
								log.fine("Set Value ID = " + no);		
							
						}
						rs.close();
						pstmt.close();
						sql = new StringBuffer ("SELECT M_ATTRIBUTEVALUE_ID " 
								+ " FROM M_ATTRIBUTEVALUE" 
								+ " WHERE VALUE = " + value2);
						
						pstmt = DB.prepareStatement(sql.toString(), get_Trx());
						rs = pstmt.executeQuery();
						if(rs.next()){
							value2_ID = rs.getInt("M_ATTRIBUTEVALUE_ID");
							
							//Updating ID 
							sql = new StringBuffer ("UPDATE I_XX_C_ORCD22 i"
									+ " SET CARAC2 = " + value2_ID
									+ " WHERE I_IsImported<>'Y' AND CARAC2 = "
									+ carac2);
								no = DB.executeUpdate(get_Trx(), sql.toString());
								log.fine("Set Value ID = " + no);		
							
						}
						rs.close();
						pstmt.close();
						System.out.println("Value1 " + value1_ID + " Value2 " + value2_ID);
						
						//Verifing instance of XX_COLUMN, XX_ROW con el mismo XX_VMR_PO_LINEREFPROV_ID
						//otherwise counting XX_VMR_PO_LINEREFPROV_ID(s)
						int cant = verifyMatrix (poLineRefProvID, value1_ID, value2_ID);
						int cantCol = 0, cantRow = 0;
						
						if(cant != -1){
							if(cant == 0)	cantCol = cant; 	else 	cantCol = cant + 1;
							cantRow = cantCol * 3;
							//System.out.println("cantCol " + cantCol + " cantRow " + cantRow);	
							
							String SQL1 = "INSERT INTO XX_VMR_REFERENCEMATRIX (CREATED, CREATEDBY, " 
								+ "M_PRODUCT_ID, UPDATED, UPDATEDBY, XX_COLUMN, XX_QUANTITYC, XX_QUANTITYO, " 
								+ "XX_QUANTITYV, XX_ROW, XX_VALUE1_ID, XX_VALUE2_ID, XX_VMR_PO_LINEREFPROV_ID) " 
								+ "VALUES (SysDate, 0, " + prodID + ", SysDate, 0, " + cantCol + ", " 
								+ canCom + "," + canObs + "," + canVen + "," + cantRow + "," + value1_ID 
								+ "," + value2_ID + "," + poLineRefProvID + ")";
							
							//System.out.println(SQL1);
						
							try {
								PreparedStatement pstmt1 = DB.prepareStatement(SQL1, null);
								pstmt1.executeQuery();
								
								//Inserting Characteristic data on POLineRefProv
								X_XX_VMR_PO_LineRefProv poLineRefProv = new X_XX_VMR_PO_LineRefProv(
										getCtx(), poLineRefProvID, get_TrxName());
								if (cantCol == 0){
									poLineRefProv.setXX_Characteristic1Value1_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value1_ID(value2_ID);
								}
								if (cantCol == 1){
									poLineRefProv.setXX_Characteristic1Value2_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value2_ID(value2_ID);
								}
								if (cantCol == 2){
									poLineRefProv.setXX_Characteristic1Value3_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value3_ID(value2_ID);
								}
								if (cantCol == 3){
									poLineRefProv.setXX_Characteristic1Value4_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value4_ID(value2_ID);
								}
								if (cantCol == 4){
									poLineRefProv.setXX_Characteristic1Value5_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value5_ID(value2_ID);
								}
								if (cantCol == 5){
									poLineRefProv.setXX_Characteristic1Value6_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value6_ID(value2_ID);
								}
								if (cantCol == 6){
									poLineRefProv.setXX_Characteristic1Value7_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value7_ID(value2_ID);
								}
								if (cantCol == 7){
									poLineRefProv.setXX_Characteristic1Value8_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value8_ID(value2_ID);
								}
								if (cantCol == 8){
									poLineRefProv.setXX_Characteristic1Value9_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value9_ID(value2_ID);
								}
								if (cantCol == 9){
									poLineRefProv.setXX_Characteristic1Value10_ID(value1_ID);
									poLineRefProv.setXX_Characteristic2Value10_ID(value2_ID);
								}	
								
								//Calculating LineQty, LinePVPAmount & LineNetAmount
								int aux = getAllLineQty(poLineRefProvID) + canCom;
								BigDecimal lineQty = new BigDecimal(aux);
								BigDecimal linePVPAmount = poLineRefProv.getXX_SalePrice().multiply(lineQty);
								BigDecimal linePVPPlusTax = poLineRefProv.getXX_TaxAmount().multiply(linePVPAmount);
								BigDecimal lineNetAmount = poLineRefProv.getXX_UnitPurchasePrice().multiply(lineQty);
								
								poLineRefProv.setXX_LineQty(aux);
								poLineRefProv.setXX_LinePVPAmount(linePVPAmount);
								poLineRefProv.setXX_LinePlusTaxAmount(linePVPPlusTax);
								poLineRefProv.setLineNetAmt(lineNetAmount);
								
								poLineRefProv.save();
								
							} catch (SQLException e) {
								System.out.println(e.getMessage());
							} catch (Exception e){
								System.out.println(e.getMessage());
							}
						}
					}
					rs0.close();
					pstmt0.close();
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
		sql = new StringBuffer("UPDATE I_XX_C_ORCD22 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog(0, null, new BigDecimal(no), "@Errors@");
		addLog(0, null, new BigDecimal(noInsert), "@C_Order_ID@: @Inserted@");

		//System.out.println(sql);
		return "";
	}
	
	/**
	 * @param id
	 * @param value1
	 * @param value2
	 * @return cantidad de datos con el mismo ID que existen en la tabla
	 * Si no existe se retorna 0, si existe con los mismos valores de fila
	 * y columna, se retorna -1. De lo contrario se devuelve la cantidad
	 * de instacias que existen en la BD de ese ID
	 */
	@SuppressWarnings("finally")
	public int verifyMatrix (Integer id, Integer value1, Integer value2){
		
		Integer poId, val1, val2;
		int cont = 0;
		String sql = "SELECT XX_VMR_PO_LINEREFPROV_ID, XX_VALUE1_ID, XX_VALUE2_ID " 
				+ " FROM XX_VMR_REFERENCEMATRIX "
				+ " WHERE XX_VMR_PO_LINEREFPROV_ID = " + id;
		//System.out.println(sql);

		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {								
				poId = rs.getInt("XX_VMR_PO_LINEREFPROV_ID");
				val1 = rs.getInt("XX_VALUE1_ID");
				val2 = rs.getInt("XX_VALUE2_ID");

				if((poId == id) && (val1 == value1) && (val2 == value2)){ return -1;					
				}else if(poId == id){	cont++;	}
				
			}
			rs.close();
			pstmt.close();
			System.out.println(cont);
			return cont;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			return cont;
		}
	}
	
	/**
	 * @param id
	 * @return variable aux con la suma de las cantidades
	 * de compra de las referencias con el id dado
	 */
	public int getAllLineQty(Integer id){
		
		int aux = 0;
		String sql = "SELECT XX_QUANTITYC " 
			+ " FROM XX_VMR_REFERENCEMATRIX "
			+ " WHERE XX_VMR_PO_LINEREFPROV_ID = " + id;
	
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();
	
			while (rs.next()) {								
				int qtyC = rs.getInt("XX_QUANTITYC");
	
				aux = aux + qtyC;
			}
			rs.close();
			pstmt.close();
			System.out.println(aux);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return aux;	
	}

}
