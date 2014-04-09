package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VCN_INVM14;
import compiere.model.cds.X_XX_VCN_Inventory;

public class ImportVCMINVM14 extends SvrProcess {

	/**	Client to be imported to*/
	private int	s_AD_Client_ID = 0;
	
	/**	Delete old Imported	*/
	private boolean s_deleteOldImported = false;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	//
	
	@Override
	protected void prepare() 
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			String name = element.getParameterName();
			if (element.getParameter() == null);
			
			else if (name.equals("AD_Client_ID"))
				s_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				s_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}
	
	@Override
	protected String doIt() throws Exception 
	{
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (s_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_XX_VCN_INVM14 "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
/**		
		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(s_AD_Client_ID).append("),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Reset=" + no);**/
					
		//	Updating Category ID
	/*	sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 i " 
			+ " SET XX_VMR_CATEGORY_ID = (SELECT XX_VMR_CATEGORY_ID " 
			+ " FROM XX_VMR_CATEGORY c "
			+ " WHERE i.XX_CodCat = To_Number(c.Value)) "
			+ " WHERE i.XX_CodCat IS NOT NULL"
			+ " AND i.I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Category ID=" + no);*/
		
		//	Updating Department ID
	/*sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 i " 
			+ " SET XX_VMR_DEPARTMENT_ID = (SELECT XX_VMR_DEPARTMENT_ID " 
			+ " FROM XX_VMR_DEPARTMENT d "
			+ " WHERE i.XX_CodDep = To_Number(d.Value)) "
			+ " WHERE i.XX_CodDep IS NOT NULL"
			+ " AND i.I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Department ID=" + no);*/
		
		//	Updating Line ID
	/*	sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 i " 
			+ " SET XX_VMR_LINE_ID = (SELECT XX_VMR_LINE_ID FROM XX_VMR_LINE l "
			+ " WHERE i.XX_CodLin = To_Number(l.Value) " 
			+ " AND l.XX_VMR_DEPARTMENT_ID = i.XX_VMR_DEPARTMENT_ID)"
			+ " WHERE i.XX_CodLin IS NOT NULL AND i.XX_VMR_DEPARTMENT_ID IS NOT NULL"
			+ " AND i.I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Line ID=" + no);*/
		
		//	Updating Section ID
	/*	sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 i " 
			+ " SET XX_VMR_SECTION_ID = (SELECT XX_VMR_SECTION_ID FROM XX_VMR_SECTION s "
			+ " WHERE i.XX_Codsec = To_Number(s.Value) "
			+ " AND s.XX_VMR_LINE_ID = i.XX_VMR_LINE_ID)"
			+ " WHERE i.XX_Codsec IS NOT NULL AND i.XX_VMR_LINE_ID IS NOT NULL"
			+ " AND i.I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Section ID=" + no);*/
		
		//	Updating Warehouse ID
	/*	sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 i " 
			+ " SET M_WAREHOUSE_ID = (SELECT M_WAREHOUSE_ID FROM M_WAREHOUSE s "
			+ " WHERE i.XX_TIENDA = To_Number(s.Value)) "
			+ " WHERE i.XX_TIENDA IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Warehouse ID=" + no);
*/
		//	Updating Product ID
	/*	sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 i " 
			+ " SET M_PRODUCT_ID = (SELECT M_PRODUCT_ID FROM M_PRODUCT p "
			+ " WHERE to_char(i.XX_CODPRO) = p.Value) "
			+ " WHERE i.XX_CODPRO IS NOT NULL"
			+ " AND I_IsImported='N'");//.append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Product ID=" + no);
		System.out.println("listo producto");*/
		
		sql = new StringBuffer("UPDATE I_XX_VCN_INVM14 i "
				+ "  Set  M_AttributeSetInstance_ID = (select M_AttributeSetInstance_ID from XX_VMR_ProductBatch pb where pb.Lot = i.Lot and pb.ProductValue = to_char(i.XX_CODPRO)) "
				+ "WHERE i.Lot is not null and i.Lot > 0 and i.M_AttributeSetInstance_ID is null " 
				+ "AND i.XX_CODPRO IS NOT NULL and i.M_Product_ID is not null");
				no = DB.executeUpdate(get_Trx(), sql.toString());
				log.fine("SET M_Product_ID=" + no);
		
		//	Reference Error
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  
				//java bug, it could not be used directly
		
	/*	sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 "
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
		
			
	*/		commit();
		//-----------------------------------------------------------------------------------
	
			int noInsert = 0;

			//	Go through Records
			log.fine("start inserting...");
			sql = new StringBuffer ("SELECT * FROM I_XX_VCN_INVM14 "
				+ " WHERE I_IsImported='N'").append(clientCheck);
			
		try
		{
			/*PreparedStatement pstmt = DB.prepareStatement(get_Trx(), sql.toString());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_I_XX_VCN_INVM14 impIprld = new X_I_XX_VCN_INVM14 (getCtx(), rs, get_TrxName());
				
				log.fine("I_XX_VCN_INVM14_ID=" + impIprld.getI_XX_VCN_INVM14_ID());
				
				X_XX_VCN_Inventory impXprld =  new X_XX_VCN_Inventory(getCtx(), 0, get_TrxName());;
				
				// Pregunto por ID de la tabla compiere que es igual al de la tabla I //

				
						if(impIprld.getXX_VCN_INVENTORY_ID() == 0)
						{
				
							impXprld = new X_XX_VCN_Inventory(getCtx(), 0, get_TrxName());
						}	
						else  
						{
							impXprld = 	new X_XX_VCN_Inventory(getCtx(), impIprld.getXX_VCN_INVENTORY_ID(), get_TrxName());
					
						}
				 
						impXprld.setXX_VMR_Category_ID(impIprld.getXX_VMR_Category_ID());						
						
						impXprld.setXX_VMR_Department_ID(impIprld.getXX_VMR_Department_ID());
						
						impXprld.setXX_VMR_Line_ID(impIprld.getXX_VMR_Line_ID());
						
						impXprld.setXX_VMR_Section_ID(impIprld.getXX_VMR_Section_ID());
						
						impXprld.setM_Warehouse_ID(impIprld.getM_Warehouse_ID());
						
						impXprld.setM_Product_ID(impIprld.getM_Product_ID());
												
						impXprld.setXX_ConsecutivePrice(impIprld.getXX_CONPRE());
						impXprld.setXX_INVENTORYMONTH(impIprld.getXX_MESINV());
						impXprld.setXX_INVENTORYYEAR(impIprld.getXX_ANOINV());
						impXprld.setXX_INITIALINVENTORYQUANTITY(impIprld.getXX_CANTINVINI());
						impXprld.setXX_INITIALINVENTORYAMOUNT(impIprld.getXX_MONTINVINI());
						impXprld.setXX_SHOPPINGQUANTITY(impIprld.getXX_CANTCOMPRA());
						impXprld.setXX_SHOPPINGAMOUNT(impIprld.getXX_MONTCOMPRA());
						impXprld.setXX_SALESQUANTITY(impIprld.getXX_CANTVENTAS());
						impXprld.setXX_SALESAMOUNT(impIprld.getXX_MONTVENTAS());
						impXprld.setXX_MOVEMENTQUANTITY(impIprld.getXX_CANTMOVIMI());
						impXprld.setXX_MOVEMENTAMOUNT(impIprld.getXX_MONTMOVIMI());
						impXprld.setXX_ADJUSTMENTSQUANTITY(impIprld.getXX_CANTAJUSTE());
						impXprld.setXX_AdjustmentsAmount(impIprld.getXX_MONTAJUSTE());
						impXprld.setXX_PREVIOUSADJUSTMENTSQUANTITY(impIprld.getXX_CANTAJUANT());
						impXprld.setXX_PREVIOUSADJUSTMENTSAMOUNT(impIprld.getXX_MONTAJUANT());
	

						//
						if(impXprld.save())
						{
							log.finest("Insert/Update INVM14 - " +  impXprld.getXX_VCN_INVENTORY_ID());
							noInsert++;
											
							impIprld.setXX_VCN_INVENTORY_ID(impXprld.getXX_VCN_INVENTORY_ID());
							impIprld.setI_IsImported("Y");
							impIprld.setProcessed(true);
							impIprld.setProcessing(false);
							impIprld.save();
						}
						else
						{		
							rollback();
							noInsert--;						
							sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 i "
									+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
									.append("Cannot Insert INVM14...")
									.append("' WHERE I_XX_VCN_INVM14_ID=").append(impIprld.getI_XX_VCN_INVM14_ID());
								DB.executeUpdate(get_Trx(), sql.toString());
								//continue;							
						}

						commit();
						//						
								
			}//end while
			
			rs.close();
			pstmt.close();
			*/
		} 
		
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
			rollback();
			e.printStackTrace();
			return "Malo";
		}	
			
			//	Set Error to indicator to not imported
			
		/*	sql = new StringBuffer ("UPDATE I_XX_VCN_INVM14 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@XX_VCN_Inventory_ID@: @Inserted@");	
			*/
			return "";
			
	} //end doIt

}
