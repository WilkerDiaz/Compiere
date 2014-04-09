/**
 *  
 */
package compiere.model.cds.processes;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.Utilities;

/**
 * @author Jorge E. Pires G.
 *
 */
public class AutoMailDefinitivePriceProcess extends SvrProcess {

	private Integer 	m_XX_Hours_Spent = 0;
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#prepare()
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("m_XX_Hours_Spent"))
				m_XX_Hours_Spent = ((BigDecimal)element.getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.compiere.process.SvrProcess#doIt()
	 */
	@Override
	protected String doIt() throws Exception {
		Integer contadorOC = new Integer(0);
		Integer contadorJefeCategoria = new Integer(0);

		String SQL = "select XX_OrderBecoCorrelative, C_ORDER_ID " +
					 "from XX_VMR_Order " +
					 "where XX_OrderRequestStatus = (Select to_char(XX_OrderRequestStatus_ID) from XX_VMR_OrderRequestStatus where upper(XX_VMR_OrderRequestStatus) = 'POR FIJAR PRECIO') " +
			 		 "and iscomplete = 'N' " +
			 		 "and round((sysdate-Created)*24, 2) >= "+ m_XX_Hours_Spent + "";

		PreparedStatement pstmt = DB.prepareStatement(SQL, null);
		ResultSet rs = pstmt.executeQuery();
		
		System.out.println(SQL);
		while (rs.next()){
			contadorOC++;
			String auxOrderBecoCorrelative = null;
			auxOrderBecoCorrelative = rs.getString("XX_OrderBecoCorrelative");

			String auxC_Order_ID = null;
 			auxC_Order_ID = rs.getString("C_ORDER_ID");

			String SQL1 = "select XX_CATEGORYMANAGER_ID " +
			 			  "from XX_VMR_CATEGORY " +
						  "where XX_VMR_CATEGORY_ID in ( " +
							 "select XX_VMR_CATEGORY_ID from XX_VMR_ORDERREQUESTDETAIL " +
							 "where XX_ORDERBECOCORRELATIVE = '" + auxOrderBecoCorrelative + "') ";

			PreparedStatement pstmt1= DB.prepareStatement(SQL1, null);
			ResultSet rs1 = pstmt1.executeQuery();
			
			while (rs1.next()){
				Integer auxCategoryManager = null;
				auxCategoryManager = rs1.getInt("XX_CATEGORYMANAGER_ID");

				String Attachment = null;
				if( auxC_Order_ID == null || auxC_Order_ID.equals("null") ){
					EnviarCorreosFijacionDefinitiva(auxCategoryManager, null, auxOrderBecoCorrelative,  Attachment, 2);
					contadorJefeCategoria++;
				}else{
					EnviarCorreosFijacionDefinitiva(auxCategoryManager, auxC_Order_ID, auxOrderBecoCorrelative,  Attachment, 1);
					contadorJefeCategoria++;
				}
			}
			rs1.close();
			pstmt1.close();
		}
		rs.close();
		pstmt.close();
		return Msg.getMsg( getCtx(), 
				 "XX_Order_ManagerCategory", 
				 new String[]{ contadorOC.toString(),
							   contadorJefeCategoria.toString()
							 });
	}
	
	/**
	 * Jorge E. Pires G. --> Funcion 108. Envio de Correos para que fije Definitivamente el Precio 
	 * 
	 **/	
	private boolean EnviarCorreosFijacionDefinitiva(Integer BPartner, String NumeroOC , String XX_OrderBecoCorrelative , String Attachment, Integer IsPedido) {
		String sql = "SELECT u.AD_User_ID "
				   + "FROM AD_User u "
				   + "where IsActive = 'Y' and "
				   + "C_BPARTNER_ID = " + BPartner;
		
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				Integer UserAuxID = rs.getInt("AD_User_ID");
				if (IsPedido == 1){
					String Mensaje = Msg.getMsg( Env.getCtx(), 
							 "XX_FixedDefinitivePrice", 
							 new String[]{ XX_OrderBecoCorrelative, 
										   NumeroOC
										 });
					Utilities f = new Utilities(Env.getCtx(), null,1000006, Mensaje, -1, 1019149 ,-1, UserAuxID, Attachment);
					f.ejecutarMail(); 
					f = null;
				}else{
					String Mensaje = Msg.getMsg( getCtx(), 
							 "XX_FixedDefinitivePrice1", 
							 new String[]{ XX_OrderBecoCorrelative
										 });
					Utilities f = new Utilities(getCtx(), null,1000006, Mensaje, -1, 1019149 ,-1, UserAuxID, Attachment);
					f.ejecutarMail();
					f = null;
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e){
			log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
		}
		return true;
	}//EnviarCorreosFijacionDefinitiva
	/**
	 * Fin Jorge E. Pires G.
	 * */
}
