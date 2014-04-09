package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

//import org.compiere.model.MOrderLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_Prod02i;

public class XX_ReorderProd02 extends SvrProcess{

	
	/**	Client to be imported to*/
	private int	s_AD_Client_ID = 0;
	
	/**	Delete old Imported	*/
	private boolean s_deleteOldImported = false;

	/**
	 *  Prepare - e.g., get Parameters.
	 */	
	@Override
	protected String doIt() throws Exception {
		
		System.out.println("Entre al proceso");
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;
		sql = new StringBuffer("select distinct XX_CODDEP,XX_CODLIN,XX_CODSEC from I_XX_PROD02 ORDER BY XX_CODDEP,XX_CODLIN,XX_CODSEC");
		System.out.println(sql);
		try
		{
			int departamento=0;
			int linea=0;
			int seccion=0;
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				departamento=rs.getInt("XX_CODDEP");
				linea=rs.getInt("XX_CODLIN");
				seccion=rs.getInt("XX_CODSEC");
				X_I_XX_Prod02i registro_nuevo= new X_I_XX_Prod02i(getCtx(), 0, get_TrxName());
				StringBuffer sql2 = new StringBuffer ("SELECT * FROM I_XX_Prod02 "
						+ " WHERE IsImported='N' AND XX_CODDEP="+departamento+" AND XX_CODLIN="+linea+" AND XX_CODSEC="+seccion).append(clientCheck);
				PreparedStatement pstmt2=DB.prepareStatement(sql2.toString(), get_TrxName());
				ResultSet rs2=pstmt2.executeQuery();
				System.out.println(sql2.toString());
				while (rs2.next()){
					
					registro_nuevo.setXX_CodDep(Integer.toString(departamento));
					registro_nuevo.setXX_CodLin(Integer.toString(linea));
					registro_nuevo.setXX_Codsec(Integer.toString(seccion));
					if(rs2.getInt("XX_CONTBL")==1){
						registro_nuevo.setXX_TipCar(rs2.getInt("XX_TIPCAR"));
						registro_nuevo.setXX_NomCar(rs2.getString("XX_NOMCAR"));
					}
					else if(rs2.getInt("XX_CONTBL")==2){
						registro_nuevo.setXX_TIPCAR1(rs2.getInt("XX_TIPCAR"));
						registro_nuevo.setXX_NomCar1(rs2.getString("XX_NOMCAR"));
					}
					else if(rs2.getInt("XX_CONTBL")==3){
						registro_nuevo.setXX_TIPCAR2(rs2.getInt("XX_TIPCAR"));
						registro_nuevo.setXX_NomCar2(rs2.getString("XX_NOMCAR"));
					}
					registro_nuevo.setI_IsImported(false);
					registro_nuevo.setProcessed(false);
					registro_nuevo.setProcessing(false);
					
				}
				rs2.close();
				pstmt2.close();
				registro_nuevo.save();
				commit();
			} // end while
			rs.close();
			pstmt.close();
			
		}
		catch (SQLException e) 
		{ 
			log .log(Level. SEVERE , "" , e); 
			rollback(); 
		}//catch 
		

		//	Delete Old Imported
		if (s_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_XX_Prod02I "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_TrxName(), sql.toString() );
			log.fine("Delete Old Imported =" + no);
		}
		
		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_XX_Prod02i "
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
		no = DB.executeUpdate(get_TrxName(), sql.toString() );
		log.fine("Reset=" + no);
		
		/** Texto agregado por Patricia Ayuso para hacer una modificación que debió hacerse en la importacion de Productos
		//Getting M_ATTRIBUTESETINSTANCE_ID to change description
		StringBuffer sql = new StringBuffer(
				"select M_ATTRIBUTESETINSTANCE_ID from M_ATTRIBUTESETINSTANCE");
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				int mAttributeSetInstanceID = rs.getInt("M_ATTRIBUTESETINSTANCE_ID");
				String description = " ", value1 = " ", value2 = " ", value3 = " ";
				
				//Getting the values from M_ATTRIBUTEINSTANCE to set the description variable
				StringBuffer sql2 = new StringBuffer(
						"select a.value, b.value, c.value" + 
						" from M_ATTRIBUTEINSTANCE a, M_ATTRIBUTEINSTANCE b, M_ATTRIBUTEINSTANCE c" + 
						" where a.value <> b.value and a.value <> c.value and b.value <> c.value" + 
						" and a.M_ATTRIBUTESETINSTANCE_ID = " + mAttributeSetInstanceID + 
						" and b.M_ATTRIBUTESETINSTANCE_ID = " + mAttributeSetInstanceID +
						" and c.M_ATTRIBUTESETINSTANCE_ID = " + mAttributeSetInstanceID + 
						" group by a.value, b.value, c.value");
				try {
					PreparedStatement pstmt2 = DB.prepareStatement(sql2.toString(), get_TrxName());
					ResultSet rs2 = pstmt2.executeQuery();
					
					if(rs2.next()){
						value1 = rs2.getString(1);
						description = value1;
						value2 = rs2.getString(2);
						if (value2 != "")
							description = description + "_" + value2;
						value3 = rs2.getString(3);	
						if (value3 != "")
							description = description + "_" + value3;
						//description = value1 + "_" + value2 + "_" + value3;
					}				
					pstmt2.close();
					rs2.close();
					
				} catch (SQLException e) {
					
				}				
				
				//Setting DESCRIPTION from M_ATTRIBUTESETINSTANCE with description variable
				StringBuffer sql3 = new StringBuffer ("UPDATE M_ATTRIBUTESETINSTANCE " +
						" SET DESCRIPTION = '" + description +
						"' WHERE M_ATTRIBUTESETINSTANCE_ID = " + mAttributeSetInstanceID);
				
				int no = DB.executeUpdate(sql3.toString(), get_TrxName());
				log.fine("Reset=" + no);
				commit();	
				
			} //	while
			
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			
		}
		*/
		
		return "";

	}//Enviar

	@Override
	//
	public void prepare()
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
	
}
