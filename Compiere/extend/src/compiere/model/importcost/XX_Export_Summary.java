package compiere.model.importcost;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.As400DbManager;
import compiere.model.importcost.X_E_XX_VLO_NRAM04;

public class XX_Export_Summary extends SvrProcess {


	protected String doIt() throws Exception {

	
		//String numOrden ="";
		int summaryID =0;
		int numRegistrosNRAM04=0;
		BigDecimal moncos=new BigDecimal("0.0");
		BigDecimal mespro=new BigDecimal("0.0");
		BigDecimal aÑopro=new BigDecimal("0.0");
	
		X_E_XX_VLO_NRAM04 summaryNram04 = null;
		

		System.out.println("Borro las E");
		  //Borrado de la NRAM04
		  String sql_delete = "DELETE E_XX_VLO_NRAM04 WHERE XX_STATUS_SINC = 'Y'";
		  PreparedStatement ps_delete = DB.prepareStatement(sql_delete, null);
		  ps_delete.executeUpdate();
		  ps_delete.close();
		  
		  
		//Sincronizacion de la tabla SUMMARY (Ajustes a las compras importadas) 

		String sql_update = " UPDATE XX_VLO_SUMMARY SET XX_STATUS_SINC = 'Y' WHERE XX_VLO_SUMMARY_ID = ?";
		//Marcar el ajuste como actualizado
		PreparedStatement ps_update = DB.prepareStatement(sql_update, get_TrxName());
		
		
		String sql8= "SELECT XX_VLO_SUMMARY_ID, (EXTRACT (YEAR FROM A.CREATED)) AÑO, (EXTRACT (MONTH FROM A.CREATED)) MES, A.DATATYPE," +
				" B.VALUE TIENDA, C.VALUE DPTO, D.DOCUMENTNO OC, ROUND(A.XX_COSANT, 2) COSTO, E.VALUE PROV, F.VALUE PAIS" +
				" FROM XX_VLO_SUMMARY A INNER JOIN M_WAREHOUSE B ON A.M_WAREHOUSE_ID = B.M_WAREHOUSE_ID" +
				" INNER JOIN XX_VMR_DEPARTMENT C ON A.XX_VMR_DEPARTMENT_ID = C.XX_VMR_DEPARTMENT_ID " +
				" INNER JOIN C_ORDER D ON A.C_ORDER_ID = D.C_ORDER_ID " +
				" INNER JOIN C_BPARTNER E ON A.C_BPARTNER_ID = E.C_BPARTNER_ID " +
				" INNER JOIN C_COUNTRY F ON A.C_COUNTRY_ID = F.C_COUNTRY_ID " +
				" WHERE A.XX_STATUS_SINC = 'N'" ;

	//	X_E_XX_VLO_NRAM04 summaryNram04 = null;
		PreparedStatement ps_NRAM04 = DB.prepareStatement(sql8,null);
		
		try {
			ResultSet rs7 = ps_NRAM04.executeQuery();
			
			int i=0;
			while (rs7.next()) {
				
				System.out.println("Ajuste: " + rs7.getString("XX_VLO_SUMMARY_ID"));
				System.out.println("i: " + i++);
				
				numRegistrosNRAM04++;
				summaryNram04 = new X_E_XX_VLO_NRAM04(getCtx(), 0, get_TrxName());

				moncos=rs7.getBigDecimal("COSTO");
			//    numOrden=rs7.getString("OC");
			//	orderID=rs7.getInt("OC");
			    summaryID = rs7.getInt("XX_VLO_SUMMARY_ID");
				aÑopro = rs7.getBigDecimal("AÑO");
				mespro = rs7.getBigDecimal("MES");
			//	summaryNram04.setC_Order_ID(orderID);
				summaryNram04.setPAIS(rs7.getString("PAIS"));
				summaryNram04.setAÑOPRO(aÑopro);
				summaryNram04.setMESPRO(mespro);
				summaryNram04.setTienda(rs7.getString("TIENDA"));
				summaryNram04.setCODDEP(rs7.getString("DPTO"));
				summaryNram04.setNUMORD(rs7.getString("OC"));
				summaryNram04.setXX_COSANT(moncos);
				summaryNram04.setCOEMPE(rs7.getString("PROV"));
				summaryNram04.setPAIS(rs7.getString("PAIS"));
				summaryNram04.setXX_DATATYPE(rs7.getString("DATATYPE"));
				summaryNram04.save();
				
				//Actualizar como sinc					
				ps_update.setInt(1, summaryID);
				if (ps_update.executeUpdate() == 0) {
					log.saveError("XX_DatabaseAccessError", "XX_DatabaseAccessError");						
				}
	
			}

			
			rs7.close();
			ps_NRAM04.close();
			/*
			 * CIERRO EL PREPARE STAMENTE DEL UPDATE
			 */
			ps_update.close();
			
			
			
		} catch (SQLException e){
			System.out.println("Error al sincronizar la tabla NRAM04 " + e);			
		} catch (Exception e) {
			System.out.println("Error NRAM04: " + e);	
			System.out.println("Ajuste numero: "+summaryID);
		}
		
		
		System.out.println("Exportando la NRAM04 al AS");
		commit();
		exportE_XX_VLO_NRAM04DB2();
		System.out.println("ya exportó NRAM04");
						
		System.out.println("FIN!");
		
		return "FIN";
	}

	protected void prepare() {

	}
	
	/*
	 * Exporta la E_XX_VLO_NRAM04 de Compiere al AS/400
	 */
	public int exportE_XX_VLO_NRAM04DB2()
	{
		As400DbManager As = new As400DbManager();
		String SQL_Compiere="";
		String SQL_AS;
		int r = 0;
        int r2 = 0;
        
        //Conexión con el AS/400
        As.conectar();
        	
        try
        {    
        	
        	//Borra la data de la tabla de sumaria en el AS/400
        	SQL_AS = "DELETE FROM BECOFILE.NRAM04C";
        	Statement sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            r = As.realizarSentencia(SQL_AS, sentencia);
            System.out.println("YA BORRO LA TABLA EN EL AS  " +SQL_AS);
            
           if(r>=0) //Si se borró la tabla correctamente
           {
	        	SQL_Compiere = "SELECT NUMORD, PAIS, COEMPE, "
	        				   + " TIENDA, CODDEP, AÑOPRO, MESPRO, XX_COSANT, NVL(XX_DATATYPE, ' ') XX_DATATYPE, "     
							   + " XX_STATUS_SINC FROM E_XX_VLO_NRAM04 ";
	        	
	        	PreparedStatement ps_Compiere = DB.prepareStatement(SQL_Compiere,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE,null);
	        	ResultSet rs = ps_Compiere.executeQuery();
	        	
	        	System.out.println("SELECCIÓN DE LA TABLA TEMPORAL  " +SQL_Compiere);
	        	int i=0;
	        	while(rs.next()){
	        	    i++;
		            try
		            {
		            	//Query que permite hacer un registro nuevo en el AS/400
		            	SQL_AS = "INSERT INTO BECOFILE.NRAM04C (AÑOPRO, MESPRO, TIPDAT, TIENDA, CODDEP,"        
				            	+ " NUMREC, MONCOS, COEMPE, PAIS)" 
				            	+ " VALUES("
			                	    + rs.getBigDecimal("AÑOPRO") +", " 
			                	    + rs.getBigDecimal("MESPRO") +", '" 
			                	    + rs.getString("XX_DATATYPE") +"', '" 
			                	    + rs.getString("TIENDA") +"', '" 
			                	    + rs.getString("CODDEP") +"', '" 
			                	    + rs.getString("NUMORD") +"', " 
			                	   	+ rs.getBigDecimal("XX_COSANT") +", '"
			                	   	+ rs.getString("COEMPE") +"', '"
			                		+ rs.getString("PAIS") 
			                		+ "')";
			            
			          System.out.println("ESTA REALIZANDO EL INSERT EN EL AS  " +SQL_AS);
			            if(i==9001 || i==18000 || i==27000 || i==36000 || i==45000 || i==54000 || i==63000 || i==72000 
					            			|| i==81000 || i==90000 || i==99000 || i==108000 || i==117000 || i==126000 || i==135000 
					            			|| i==144000 || i==153000 || i==162000 || i==171000){
							As.desconectar();
							As.conectar();
						}
						            
			            sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			            r2 = As.realizarSentencia(SQL_AS, sentencia);
			            
			            System.out.println("ACTUALIZA LA TABLA TEMPORAL A SINCRONIZADA ");
			            //Si se inserto correctamente lo actualizo en Y
			            if(r2==1){
			            	rs.updateString("XX_STATUS_SINC", "Y");
			            	rs.updateRow();
			            }
			            
			            if(r2<0) //Si la inserción da error
			    			return r2;   	                     	       
			        }
		            catch (Exception e) {
		            	System.out.println("SQL CON ERROR " +SQL_AS);
						e.printStackTrace();
						log.log(Level.SEVERE, e.getMessage());
					}	           
	        	}
        	}
        	
        	sentencia.close();
        
        }catch (Exception e) {
        	System.out.println("ERROR");
        	e.printStackTrace();
		}
	
		return r;
	}
	
	
}








