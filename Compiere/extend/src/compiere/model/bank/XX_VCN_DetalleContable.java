package compiere.model.bank;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.compiere.util.DB;

import compiere.model.cds.MPayment;

public class XX_VCN_DetalleContable {
	
	public ArrayList<String> DetalleContabilidad (MPayment pay, int account)
	{
		//CREO EL ARRAY LIST QUE VA A TENER LOS VALORES
		ArrayList<String> contable = new ArrayList<String>();
		
		//HAGO EL SELECT PARA EMPEZAR A ASIGNAR VALORES
					String sql=
							"SELECT *"+
							" FROM C_ELEMENTVALUE"+
							" WHERE C_ELEMENTVALUE_ID = "+account; 
							
					PreparedStatement prst = DB.prepareStatement(sql,null);
				   	ResultSet rs = null;
				   	//EJECUTO LA SENTENCIA SQL
					   	try {
					   		rs = prst.executeQuery();
					   		if (rs.next()){
					   			
					   			//AQUI EMPIEZO A ASIGNAR VALORES AL ARRAY LIST
					   				
					   				//OBTENGO EL XX_AUX
					   				if(rs.getString("XX_AUX").equals("Y"))
					   					contable.add(0, obtenerAux(pay, rs.getString("VALUE")));
					   				else contable.add(0, null);
					   				
					   				//OBTENGO EL DEPARTAMENTO
					   				if(rs.getString("XX_DEP").equals("Y"))
					   				contable.add(1, obtenerDep(pay));
					   				else contable.add(1, null);
					   				
					   				//OBTENGO LA DIVISION
					   				if(rs.getString("XX_DIV").equals("Y"))
					   				contable.add(2, obtenerDiv(pay));
					   				else contable.add(2, null);
					   				
					   				//OBTENGO EL DOCUMENTDATE
					   				if(rs.getString("XX_FDO").equals("Y"))
					   				contable.add(3, obtenerDocumentDate(pay));
					   				else contable.add(3, null);
					   				
					   				//OBTENGO EL DOCUMENTTYPE 
					   				if(rs.getString("XX_DOC").equals("Y"))
					   				contable.add(4, obtenerDocumentType(pay));
					   				else contable.add(4, null);
					   				
					   				//OBTENGO EL DUEDATE
					   				if(rs.getString("XX_FEV").equals("Y"))
					   				contable.add(5, obtenerDueDate(pay));
					   				else contable.add(5, null);
					   				
					   				//OBTENGO EL DOCUMENTNO
					   				if(rs.getString("XX_DOC").equals("Y"))
					   				contable.add(6, obtenerDocumentNo(pay));
					   				else contable.add(6, null);
					   				
					   				//OBTENGO OFICINA
					   				if(rs.getString("XX_SUC").equals("Y"))
					   				contable.add(7,obtenerOffice(pay));
					   				else contable.add(7, null);
					   				
					   				//OBTENGO SECTION CODE
					   				if(rs.getString("XX_SEC").equals("Y"))
					   				contable.add(8,obtenerSectionCode(pay));
					   				else contable.add(8, null);
					   		
					   		}// Fin if
					   	} 
					   	catch (Exception e){
							System.out.println(e);
						}
					   	finally {
					   	//CERRAR CONEXION
							DB.closeResultSet(rs);
							DB.closeStatement(prst);
						}
		
		

		return contable;
	
	}

	/*
	 * METODO QUE OBTIENE EL AUXILIAR DE LA CONTABILIDAD	
	 */
		private String obtenerAux(MPayment pay, String valueBank) {
			// TODO Auto-generated method stub
			String aux=null;
			if(valueBank.substring(0, 6).equalsIgnoreCase("111.02")){
				
				String sql=
						"SELECT XX_VCN_AUXILIARY"+
						" FROM C_BANKACCOUNT"+
						" WHERE C_BANKACCOUNT_ID = "+pay.getC_BankAccount_ID(); 
						
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		if (rs.next()){
				   			aux = rs.getString("XX_VCN_AUXILIARY");
				   		}// Fin if
				   	} 
				   	catch (Exception e){
						System.out.println(e);
					}
				   	finally {
				   	//CERRAR CONEXION
						DB.closeResultSet(rs);
						DB.closeStatement(prst);
					}

			} else {
					String sql=
						"SELECT VALUE FROM C_BPARTNER"+
						" WHERE C_BPARTNER_ID = "+pay.getC_BPartner_ID(); 
						
				PreparedStatement prst = DB.prepareStatement(sql,null);
			   	ResultSet rs = null;
			   	//EJECUTO LA SENTENCIA SQL
				   	try {
				   		rs = prst.executeQuery();
				   		if (rs.next()){
				   			aux = rs.getString("VALUE");
				   		}// Fin if
				   	} 
				   	catch (Exception e){
						System.out.println(e);
					}
				   	finally {
				   	//CERRAR CONEXION
						DB.closeResultSet(rs);
						DB.closeStatement(prst);
					}
			}
			return aux;
		}
	
	
/*
 * METODO QUE OBTIENE EL DEPARTAMENTO 	
 */
private String obtenerDep(MPayment pay) {
		// TODO Auto-generated method stub
	
		String dep="00";
		String sql=
			"SELECT XX_CODDEPACCOUNT"+
			" FROM AD_ORG"+
			" WHERE XX_ACCPRINCIPAL = 'Y' AND AD_CLIENT_ID = "+pay.getAD_Client_ID();
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			dep = rs.getString("XX_CODDEPACCOUNT");
		   		}// Fin if
		   	} 
		   	catch (Exception e){
				System.out.println(e);
			}
		   	finally {
		   	//CERRAR CONEXION
				DB.closeResultSet(rs);
				DB.closeStatement(prst);
			}
		return dep;
	}

/*
 * METODO QUE OBTIENE LA DIVISION 	
 */
private String obtenerDiv(MPayment pay) {
		// TODO Auto-generated method stub
	
		String div="00";
		String sql=
			"SELECT XX_DIVISION"+
			" FROM AD_CLIENTINFO"+
			" where AD_CLIENT_ID = "+pay.getAD_Client_ID();
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			div = rs.getString("XX_DIVISION");
		   		}// Fin if
		   	} 
		   	catch (Exception e){
				System.out.println(e);
			}
		   	finally {
		   	//CERRAR CONEXION
				DB.closeResultSet(rs);
				DB.closeStatement(prst);
			}
		return div;
	}
/*
 * METODO QUE OBTIENE EL DOCUMENT DATE 	
 */
private String obtenerDocumentDate(MPayment pay) {
		// TODO Auto-generated method stub
		return pay.getXX_DateFinalPay().toString();
	}
/*
 * METODO QUE OBTIENE EL DOCUMENTTYPE DE LA CONTABILIDAD	
 */
private String obtenerDocumentType(MPayment pay) {
		// TODO Auto-generated method stub
		String aux="";
			if (pay.getTenderType().equals("K")){
				aux="CHQ";
			}
			if (pay.getTenderType().equals("T")){
				aux="TRF";
			}
		return aux;
	}
/*
 * METODO QUE OBTIENE EL DOCUMENTNO DE LA CONTABILIDAD	
 */
private String obtenerDueDate(MPayment pay) {
	// TODO Auto-generated method stub
	return pay.getXX_DateFinalPay().toString();
}
/*
 * METODO QUE OBTIENE EL DOCUMENTNO DE LA CONTABILIDAD
 */
private String obtenerDocumentNo(MPayment pay) {
		// TODO Auto-generated method stub
	return pay.getCheckNo();
	}
/*
 * METODO QUE OBTIENE LA OFICINA 	
 */
private String obtenerOffice(MPayment pay) {
		// TODO Auto-generated method stub
	
		String off="00";
		String sql=
			"SELECT XX_SUC"+
			" FROM AD_ORG"+
			" WHERE XX_ACCPRINCIPAL = 'Y' AND AD_CLIENT_ID = " + pay.getAD_Client_ID();
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			off = rs.getString("XX_SUC");
		   		}// Fin if
		   	} 
		   	catch (Exception e){
				System.out.println(e);
			}
		   	finally {
		   	//CERRAR CONEXION
				DB.closeResultSet(rs);
				DB.closeStatement(prst);
			}
		return off;
	}

/*
 * METODO QUE OBTIENE EL SECTION CODE 	
 */
private String obtenerSectionCode(MPayment pay) {
		// TODO Auto-generated method stub
	
		String sc="00";
		String sql=
			"SELECT XX_SECTIONCODE"+
			" FROM AD_ClientInfo"+
			" where AD_Client_ID = " + pay.getAD_Client_ID();
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			sc = rs.getString("XX_SECTIONCODE");
		   		}// Fin if
		   	} 
		   	catch (Exception e){
				System.out.println(e);
			}
		   	finally {
		   	//CERRAR CONEXION
				DB.closeResultSet(rs);
				DB.closeStatement(prst);
			}
		return sc;
	}

}
