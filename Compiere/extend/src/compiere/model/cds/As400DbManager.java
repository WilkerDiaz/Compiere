package compiere.model.cds;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class As400DbManager {
	public Connection conexion;

	public final String user = "FTPUSER" ;
	public final String password = "FTPUSER";
	public final String IP = "192.168.1.2"; 
	

	public final String url = "jdbc:as400://".concat(IP);
	
	
	/**
	 * Método conectar.
	 * 		Realiza la conexion a la base de Datos.
	 * @throws ConexionExcepcion
	 */
	public void conectar(){
		try {
			// Creamos la nueva conexion
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
			Properties p = new Properties();
			p.put("user", user);
			p.put("password", password);
			p.put("translate binary", "true");
			conexion = DriverManager.getConnection(url, p);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			conexion=null;
		}
	}
	
	
	
	public synchronized void ejecutarLoteSentencias(Statement consultas) {
		
		try{
			conectar();
			consultas.executeBatch();
		}catch(SQLException e){
			e.printStackTrace();
			try{
				conexion.rollback();
				conexion.setAutoCommit(true);
			}catch(SQLException e1){
				e.printStackTrace();
			}

		}
		finally{desconectar();}
	}
	
	

	/**
	 * Método desconectar.
	 * 		Realiza la desconexion de la base de Datos.
	 * @throws ConexionExcepcion
	 */
	public  void desconectar() {
		try {
			conexion.close();
		} catch (Exception ex) {
			// No existian conexiones abiertas
			ex.printStackTrace();
		}
	}
	

	/**
	 * Método realizarSentencia.
	 * 		Realiza las sentencias de insersion y actualizacion en la base de datos
	 * @param String sentenciaSql 
	 * @return int negativo = error
	 * 				positivo = correcto		
	 */
	public  int realizarSentencia(String sentenciaSql,Statement sentencia) throws SQLException {
		// Realizamos la operacion
		int num = 0;
		try {
			num = sentencia.executeUpdate(sentenciaSql);
		} catch (SQLException exSQL) {
			System.out.println("SQL con error: " + sentenciaSql);
			exSQL.printStackTrace();
		}	
		return num;
	}
	

	/**
	 * Método realizarConsulta.
	 * 		Ejecuta una sentencia SQL de consulta de registros (Selects) con la capacidad de desplazamiento
	 * y actualización o no de los datos resultantes.
	 */
	public  ResultSet realizarConsulta(String sentenciaSQL, Statement sentencia) {
		ResultSet resultado = null;

		try {						
			resultado = sentencia.executeQuery(sentenciaSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e1) { 
			e1.printStackTrace();
		}
		return resultado;
	}
	
	
}
