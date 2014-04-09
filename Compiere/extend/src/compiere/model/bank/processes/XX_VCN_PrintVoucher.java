package compiere.model.bank.processes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import compiere.model.birt.BIRTReport;
import compiere.model.cds.MPayment;

public class XX_VCN_PrintVoucher extends SvrProcess {

	//VARIABLES GLOBALES
	MPayment pay = null;
	int org_ID=0;
	
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		//LOAD C_PAYMENT
		pay = new MPayment (getCtx(), getRecord_ID(), get_TrxName());
		
		//OBTENGO EL ID DE LA COMPAÑIA Y BUSCO EL NOMBRE
		org_ID=pay.getAD_Client_ID();
		String empresa= empresaEmisora(org_ID);
	
		//LLAMO AL REPORTE
		reporte(pay.getC_Payment_ID(),empresa);
		return null;
	}

	/*
	 * METODO QUE LLAMA AL REPORTE
	 */
	private void reporte(int pago, String empresa) {
		// TODO Auto-generated method stub
		
		String designName = "PrintVoucher";

		//INSTANCIAR REPORTE
		BIRTReport myReport = new BIRTReport();
		
		//AGREGAR PARAMETRO
		myReport.parameterName.add("pago");
		myReport.parameterValue.add(pago);
								
		myReport.parameterName.add("empresa");
		myReport.parameterValue.add(formatoEspacioReporte(empresa));
		
		//CORRER REPORTE
		myReport.runReport(designName,"pdf");
	}
	
	/*
	 * METODO QUE SE ENCARGA DE QUITAR CARACTERES ESPECIALES
	 */
	private String formatoEspacioReporte(String beneficiario) {
		// TODO Auto-generated method stub

		 beneficiario= beneficiario.replace(" ", "%20");
		 
		return beneficiario;
	}
	/*
	 * METODO ENCARGADO DE BUSCAR SI EL PAGO PERTENECE A UNA SELECION DE PAGO
	 */
	private String empresaEmisora(Integer Id) {

		// TODO Auto-generated method stub
		String empresa=null;
		
		String sql=
		"select NAME"
		+" from AD_CLIENT"
		+" where AD_CLIENT_ID = "+Id;


		PreparedStatement prst = DB.prepareStatement(sql,null);
	   	ResultSet rs = null;
	   	//EJECUTO LA SENTENCIA SQL
		   	try {
		   		rs = prst.executeQuery();
		   		if (rs.next()){
		   			empresa = rs.getString(1);
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
		
		return empresa;
	}

	
}