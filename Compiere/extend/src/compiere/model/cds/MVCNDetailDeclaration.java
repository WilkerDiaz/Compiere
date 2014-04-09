package compiere.model.cds;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.payments.X_XX_VCN_DetailDeclaration;

/**
 * Modelo del detalle de las declaraciones
 * @author jmendoza
 *
 */
public class MVCNDetailDeclaration extends X_XX_VCN_DetailDeclaration{

	public MVCNDetailDeclaration(Ctx ctx, int XX_VCN_DetailDeclaration_ID,
			Trx trx) {
		super(ctx, XX_VCN_DetailDeclaration_ID, trx);
	}
	
	public MVCNDetailDeclaration(Ctx ctx, ResultSet rs, Trx trx) {
		super(ctx, rs, trx);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MInvoice.class);
	
	int numDeclaracion;
	int mes;
	int year;
	
	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){
		String yearMonth;
		Date fechaActualD = new Date();		
		boolean bool = false;
		Timestamp fechaActualT = new Timestamp(fechaActualD.getTime());
		if(newRecord){
			//Verifica si ya está creada dicha declaración
			if (registrosDeclaracion(getXX_PeriodYear_ID(),getXX_Month(),getXX_NumberDeclaration_ID())){
				//System.out.println("La declaracion ya ha sido creada");
				ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_DeclarationCreated"));
			}else{				
				//Verifica si la declaración anterior fue creada
				numDeclaracion = numeroDeclaracion(getXX_NumberDeclaration_ID());
				if ((numDeclaracion - 1) == 0){
					numDeclaracion = maximoNumeroDeclaracion();
					// CCAPOTE Se pregunta si el mes es enero para poder preguntar por la declaración de diciembre
					if (getXX_Month().equals("07")){
						mes = 06;
						year = getXX_PeriodYear_ID();
					}else{
						if (!getXX_Month().equals("01")){
							mes = Integer.parseInt(getXX_Month()) - 1;
							year = getXX_PeriodYear_ID();
					
						}
					}
				}else{
					numDeclaracion = numDeclaracion - 1;
					mes = Integer.parseInt(getXX_Month());
					year = getXX_PeriodYear_ID();
				}
				// CCapote Si el mes es Julio no validar si existe registro anterior
				if (!getXX_Month().equals("01")){
					if (registrosDeclaracion(year, String.valueOf(mes), numDeclaracion)){
						yearMonth = periodo(String.valueOf(getXX_PeriodYear_ID()), Integer.parseInt(getXX_Month()));
						setXX_YearMonth(yearMonth);
						if (isXX_CheckEndPeriod()){
							//Verifica si la declaracion anterior fue cerrada
							if (cierreDeclaracion(year, String.valueOf(mes), numDeclaracion)){
								setXX_DateClose(fechaActualT);
								setXX_CheckCompleted(true);
							}else{
								setXX_CheckEndPeriod(false);
								setXX_DateClose(null);
								//System.out.println("No está cerrada la declaracion anterior");
								ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_CloseDeclaration"));
							}
						}
						bool = true;
					}else{
						//System.out.println("No existe la declaracion anterior");
						ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_DeclarationNotCreated"));
						bool = false;
					}
				} else
					bool = true;
			}	
		}else{
			if (isXX_CheckEndPeriod()){
				//Verifica si la declaracion anterior fue cerrada
				numDeclaracion = numeroDeclaracion(getXX_NumberDeclaration_ID());
				if ((numDeclaracion - 1) == 0){
					numDeclaracion = maximoNumeroDeclaracion();
					if (getXX_Month().equals("07")){
						mes = 12;
						year = getXX_PeriodYear_ID() - 1;
					}else{
						mes = Integer.parseInt(getXX_Month()) - 1;
						year = getXX_PeriodYear_ID();
					}
				}else{
					numDeclaracion = numDeclaracion - 1;
					mes = Integer.parseInt(getXX_Month());
					year = getXX_PeriodYear_ID();
				}
				
				if (cierreDeclaracion(year, String.valueOf(mes), numDeclaracion)){
					setXX_DateClose(fechaActualT);
					setXX_CheckCompleted(true);
				}else{
					setXX_CheckEndPeriod(false);
					setXX_DateClose(null);
					log.saveError("Error", Msg.getMsg(Env.getCtx(), "XX_CloseDeclaration"));
					return false;
				}
				bool = true;
			}
		}
		return bool;
	}

	/**
	 * Se encarga de concatenar el Año/Mes en un determinado periodo
	 * @param periodYear año
	 * @param periodMonth mes
	 * @return
	 */
	public String periodo(String periodYear, int periodMonth){
		String periodo = null;
		Timestamp fecha = null;
		Calendar cal = Calendar.getInstance(); 		
		String sql = "select per.startDate " +
					 "from C_Period per, C_Year yea " +
					 "where per.C_Year_ID = yea.C_Year_ID " +
					 "and yea.C_Year_ID = " + periodYear + " " +
					 "and per.PeriodNo = " + periodMonth;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				fecha = rs.getTimestamp(1);
				cal.setTime(fecha);
				if (String.valueOf(cal.get(Calendar.MONTH)+1).length() == 1)
					periodo = String.valueOf(cal.get(Calendar.YEAR)) + "0" + String.valueOf(cal.get(Calendar.MONTH)+1);
				else
					periodo = String.valueOf(cal.get(Calendar.YEAR)) + String.valueOf(cal.get(Calendar.MONTH)+1);				
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return periodo;
	}
	
	/**
	 * Verifica si ya está creada la declaración
	 * @param year perido del año
	 * @param mes mes
	 * @param numDeclaracion número de la declaración
	 * @return
	 */
	public Boolean registrosDeclaracion(int year, String mes, int numDeclaracion){
		if (mes.length() == 1)
			mes = "0" + mes;
		boolean bool = false;
		String sql = "select * " +
		 			 "from XX_VCN_DetailDeclaration " +
		 			 "where AD_Client_ID = " + getAD_Client_ID() + " " +
		 			 "and XX_Month = '" + mes + "' " +
		 			 "and XX_PeriodYear_ID = " + year + " ";
		if (String.valueOf(numDeclaracion).length() == 1){
			sql = sql + "and XX_NumberDeclaration_ID = (select XX_VCN_TaxPeriod_ID from XX_VCN_TaxPeriod where XX_NumberDeclaration = " + numDeclaracion + ") ";
		}else{
			sql = sql + "and XX_NumberDeclaration_ID = " + numDeclaracion;
		}
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next())
				bool = true;
			else
				bool = false;
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bool;
	}
	
	/**
	 * 
	 * @return numero máximo de la declaración
	 */
	public Integer maximoNumeroDeclaracion(){
		int maximo = 0;
		String sql = "select max(XX_NumberDeclaration) " +
					 "from XX_VCN_TaxPeriod ";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				maximo = rs.getInt(1);
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return maximo;
	}
	
	/**
	 * numero de la declaracion
	 * @param numDec
	 * @return
	 */
	public Integer numeroDeclaracion(int idNumDec){
		int numero = 0;
		String sql = "select XX_NumberDeclaration " +
					 "from XX_VCN_TaxPeriod " +
					 "where XX_VCN_TaxPeriod_ID = " + idNumDec;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				numero = rs.getInt(1);
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return numero;
	}
	
	/**
	 * Verifica si una declaracion ha sido cerrada
	 * @param year periodo del año
	 * @param mes mes
	 * @param numDeclaracion número de la declaración
	 * @return
	 */
	public Boolean cierreDeclaracion(int year, String mes, int numDeclaracion){
		if (mes.length() == 1)
			mes = "0" + mes;
		boolean bool = false;
		
		String sql = "select XX_CheckEndPeriod " +
		 			 "from XX_VCN_DetailDeclaration " +
		 			 "where AD_Client_ID = " + getAD_Client_ID() + " " +
		 			 "and XX_Month = '" + mes + "' " +
		 			 "and XX_PeriodYear_ID = " + year + " ";
		if (String.valueOf(numDeclaracion).length() == 1){
			sql = sql + "and XX_NumberDeclaration_ID = (select XX_VCN_TaxPeriod_ID from XX_VCN_TaxPeriod where XX_NumberDeclaration = " + numDeclaracion + ") ";
		}else{
			sql = sql + "and XX_NumberDeclaration_ID = " + numDeclaracion;
		}
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getString(1).equalsIgnoreCase("Y"))
					bool = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bool;
	}
}
