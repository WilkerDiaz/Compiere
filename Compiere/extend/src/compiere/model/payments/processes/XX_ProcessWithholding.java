package compiere.model.payments.processes;

import java.awt.Container;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * Proceso que se encarga de generar el archivo txt de las retenciones de IVA
 * @author Jessica Mendoza
 * 
 * 			compiere.model.payments.processes.XX_ProcessWithholding
 *
 */
public class XX_ProcessWithholding extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		String yearMonth = null;
		String ruta = "";
		int diaPrimero = 0;
		int diaSegundo = 0;
		
		String sql = "select dd.XX_YearMonth, pt.XX_DateFromDeclaration, " +
					 "(CASE WHEN XX_NUMBERDECLARATION = 2 THEN " +
					 "TO_CHAR(last_day(TO_DATE(XX_YEARMONTH||'01','yyyyMMdd')),'DD') "+
					 "ELSE TO_CHAR(XX_DateTODeclaration) END) XX_DateTODeclaration "+
					 "from XX_VCN_DetailDeclaration dd, XX_VCN_TaxPeriod pt " +
					 "where dd.XX_VCN_DetailDeclaration_ID = " + getRecord_ID() + " " +
					 "and dd.XX_NumberDeclaration_ID = pt.XX_VCN_TaxPeriod_ID " ;

		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				yearMonth = rs.getString(1);
				diaPrimero = rs.getInt(2);
				diaSegundo = rs.getInt(3);
				ruta = retenciones(yearMonth, diaPrimero, diaSegundo);
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
		
		//System.out.println("Se ha generado el archivo de retenciones en la ruta: " + ruta);
		ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_FileGenerated") + ruta); 
		return Msg.getMsg(Env.getCtx(), "XX_ProcessComplete");
	}

	@Override
	protected void prepare() {
		
	}
	
	/**
	 * Busca todas las retenciones generadas en un mes del período impositivo específico,
	 * donde la retención es distinto de cero
	 * @param yearMonth
	 * @param diaPrimero
	 * @param diaSegundo
	 * @return
	 */
	public String retenciones(String yearMonth, int diaPrimero, int diaSegundo){
		int año = Integer.parseInt(yearMonth.substring(0, 4));
		int mes = Integer.parseInt(yearMonth.substring(4, yearMonth.length()));

		String sql = "select to_char(pb.XX_DocumentDate,'yyyy-MM-dd') as fecDoc, " +
					 "pb.XX_DocumentNo_ID as numDoc, pb.XX_ControlNumber as ctlDoc, " +
					 "pb.XX_TotalInvCost as mtoDoc, pb.XX_TaxableBase as basDoc, " +
					 "pb.XX_WithholdingTax as mtoRet, pb.XX_Withholding as numCom, " +
					 "(pb.XX_ExemptBase + pb.XX_NotSubjectBase) as mtoExe, ta.Rate as aliCuo, " +
					 "ac.XX_CI_Rif as rifRet, cb.XX_CI_Rif as rifPro, cb.XX_TypePerson as tipoPersona, " +
					 "ci.DocumentNo as docInvoice, pb.XX_DOCUMENTNO as docNota " +
					 "from XX_VCN_PurchasesBook pb " +
					 "inner join C_Tax ta on (pb.C_Tax_ID = ta.C_Tax_ID) " +
					 "inner join AD_Client ac on (pb.AD_Client_ID = ac.AD_Client_ID) " +
					 "inner join C_BPartner cb on (pb.C_BPartner_ID = cb.C_BPartner_ID) " +
					 "inner join C_Invoice ci on (pb.C_Invoice_ID = ci.C_Invoice_ID) " +
					 "where pb.XX_Status='CO' and pb.XX_WithholdingTax <> 0 " +
					 "and pb.XX_DATE >= to_date('" + diaPrimero + "/" + mes + "/" + año + "','dd/MM/yyyy') " +
					 "and pb.XX_DATE <= to_date('" + diaSegundo + "/" + mes + "/" + año + "','dd/MM/yyyy') " +
					 "order by pb.XX_Withholding asc ";
		return generarTxt(sql,yearMonth);

	}

	/**
	 * Generación del archivo txt, de retención de IVA
	 * @param sqlRetencion
	 * @param yearMonth
	 * @return
	 */
	public String generarTxt(String sqlRetencion, String yearMonth){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date sysdate = new Date();
		String archivo = "";
		String nombreArchivo = "";
		String espacio = "\t";
		NumberFormat formatoOcho = new DecimalFormat("#00000000");
		
		 /****Se crea la ruta donde se guardan los archivos .txt si no existe la carpeta****/
		String ruta = System.getProperty("user.home") + "\\RetencionIVA\\";
		File file=new File(ruta);
		if (!file.exists()){	
			(new File(ruta)).mkdir();
		}		
		ruta = ruta + dateFormat.format(sysdate)+"\\";
		File carpeta_fecha = new File(ruta);
		if (!carpeta_fecha.exists()){
			(new File(ruta)).mkdir();
		}
		
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sqlRetencion, null); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				
				/****Concatenar los registros del impuesto retenido****/
				String rifRet = rs.getString("rifRet");
				rifRet = rifRet.replace("-", "");				
				archivo = archivo + 
					rifRet + espacio +
					yearMonth + espacio +
					rs.getString("fecDoc") + espacio + 
					"C" + espacio;
				
				String numDoc = "";
				String tipDoc = "";
				String mtoDoc = "";
				if (rs.getString("numDoc") == null){
					numDoc = rs.getString("docInvoice");
					tipDoc = "01";
					mtoDoc = String.valueOf(rs.getBigDecimal("mtoDoc"));
				}else{
					numDoc = rs.getString("docNota");
					if (rs.getBigDecimal("mtoDoc").compareTo(new BigDecimal(0)) == 1){
						tipDoc = "02";
						mtoDoc = String.valueOf(rs.getBigDecimal("mtoDoc").setScale(2, RoundingMode.HALF_UP));
					}else{
						tipDoc = "03";
						mtoDoc = String.valueOf(rs.getBigDecimal("mtoDoc").multiply(new BigDecimal(-1)).setScale(2, RoundingMode.HALF_UP));
					}
				}		

				String tipoPersona = rs.getString("tipoPersona");
				String rif = rs.getString("rifPro");
				rif = rif.replace("N", "");
				rif = rif.replace(" ", "");	
				
				archivo = archivo + 
					tipDoc + espacio +
					tipoPersona + rif + espacio +
					numDoc + espacio;
				
				String ctlDoc = rs.getString("ctlDoc");
				if (ctlDoc == null){
					ctlDoc = "-";
				}
				
				archivo = archivo + 
					      	  ctlDoc + espacio;
				
				mtoDoc = new BigDecimal(mtoDoc).setScale(2, RoundingMode.HALF_UP).toString();				
				archivo = archivo + 
						  mtoDoc + espacio;
				
				String basDoc = "";
				if (rs.getBigDecimal("basDoc").compareTo(new BigDecimal(0)) == -1)
					basDoc = (rs.getBigDecimal("basDoc").multiply(new BigDecimal(-1))).setScale(2, RoundingMode.HALF_UP).toString();
				else
					basDoc = rs.getBigDecimal("basDoc").setScale(2, RoundingMode.HALF_UP).toString();				
				archivo = archivo + 
						  basDoc + espacio;

				String mtoRet = "";
				if (rs.getBigDecimal("mtoRet").compareTo(new BigDecimal(0)) == -1)
					mtoRet = (rs.getBigDecimal("mtoRet").multiply(new BigDecimal(-1))).setScale(2, RoundingMode.HALF_UP).toString();
				else
					mtoRet = rs.getBigDecimal("mtoRet").setScale(2, RoundingMode.HALF_UP).toString();			
				archivo = archivo +  
						  mtoRet + espacio;
				
				String docAfe = "0";
				if(!tipDoc.equals("01"))
					docAfe = rs.getString("docInvoice");
				
				archivo = archivo + 
						  docAfe + espacio;
				
				String numCom = yearMonth + formatoOcho.format(Double.valueOf(rs.getString("numCom")));
				archivo = archivo +
						  numCom + espacio;
				
				String mtoExe = "";
				if (rs.getBigDecimal("mtoExe").compareTo(new BigDecimal(0)) == -1)
					mtoExe = (rs.getBigDecimal("mtoExe").multiply(new BigDecimal(-1))).setScale(2, RoundingMode.HALF_UP).toString();
				else
					mtoExe = rs.getBigDecimal("mtoExe").setScale(2, RoundingMode.HALF_UP).toString();
				archivo = archivo + 
						  mtoExe + espacio;
				
				String aliCuo = rs.getBigDecimal("aliCuo").setScale(2, RoundingMode.HALF_UP).toString();
				archivo = archivo + 
						  aliCuo + espacio;
				
				String numExp = "0";
			
				archivo = archivo + 
						  numExp + "\r\n";				
			}

			/****Construcción del archivo txt****/
			nombreArchivo = "retenciones" + yearMonth + ".txt";
			FileWriter fw = new FileWriter(ruta + nombreArchivo);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter salArch = new PrintWriter(bw);
			salArch.println(archivo);
			salArch.close();
			bw.close();
			fw.close();
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sqlRetencion);
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
		return ruta + nombreArchivo;
	}	
}
