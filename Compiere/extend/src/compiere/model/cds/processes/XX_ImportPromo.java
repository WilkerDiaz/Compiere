package compiere.model.cds.processes;

import java.awt.Container;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.compiere.apps.ADialog;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.X_XX_VMR_PromProdAS;
import compiere.model.cds.As400DbManager;
/**
 * Proceso para importar las promociones del AS400
 * @author Trinimar Acevedo.
 *
 */
public class XX_ImportPromo extends SvrProcess{

	@Override
	protected String doIt() throws Exception {
		
		
		String promAS;
		String repeated;
		ResultSet rs = null;	
		Statement ps_s = null;
		ResultSet rs2 = null;	
		PreparedStatement ps2 = null;
		X_XX_VMR_PromProdAS promoASR = null;
		As400DbManager as400 = new As400DbManager();
		as400.conectar();
		try {
			ps_s = as400.conexion.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			promAS= "SELECT C.COPROM, C.DEPROM, C.DDEPRO, C.MDEPRO, C.ADEPRO, C.DHAPRO, C.MHAPRO, C.AHAPRO, " +
						" A.REFPRO, B.CODPRO, A.PREPRM, A.DSCPRM, A.STAPRO FROM ictfile.orcd55 a, ictfile.prom01 b, ictfile.ORCM50 C"+
						" WHERE A.REFPRO= B.REFPRO and A.COEMPE= B.COEMPE and A.CODDEP="+
						" B.CODDEP and A.CODLIN = B.CODLIN and A.SECCIO= B.SECCIO and "+
						" A.STAPRO <> 9 and A.COPROM = C.COPROM";
			
			rs= as400.realizarConsulta(promAS, ps_s);
			
			
			while (rs.next()) {
				X_XX_VMR_PromProdAS promoAS = new X_XX_VMR_PromProdAS(Env.getCtx(), 0, null);
				promoAS.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
				promoAS.setAD_Client_ID(getAD_Client_ID());
				promoAS.setcodProm(rs.getInt(1));
				promoAS.setdescriptionPrm(rs.getString(2));
				promoAS.setinitialDate(getDate(rs.getString(3), rs.getString(4),rs.getString(5)));
				promoAS.setfinalDateP(getDate(rs.getString(6), rs.getString(7),rs.getString(8)));
				promoAS.setcodProd(rs.getInt(10));
				promoAS.setpriceProm(rs.getBigDecimal(11));
				promoAS.setdiscProm(rs.getBigDecimal(12));
				promoAS.setstatusProm(rs.getInt(13));
				
				repeated = "SELECT XX_VMR_PromProdAS_ID FROM XX_VMR_PromProdAS WHERE "+
						"CODPROM = "+rs.getInt(1)+" AND CODPROD = "+rs.getInt(10);
				try{
					
					ps2 = DB.prepareStatement(repeated, null);
					rs2 = ps2.executeQuery();
					if(rs2.next()){
						promoASR = new X_XX_VMR_PromProdAS(Env.getCtx(), rs2.getInt(1), null);
						promoASR.delete(true);
					}
				}catch(Exception e1){
					e1.printStackTrace();
				}finally{
					ps2.close();
					rs2.close();
				}
				promoAS.save();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			as400.desconectar();
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				ps_s.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		ADialog.info(1, new Container(), "Inserto los datos en la carteleria");
		return null;
	}
	
	private Timestamp getDate(String day, String month, String year){
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
		String strFecha = year+"-"+month+"-"+day;
		Date fecha = null;
		Timestamp tdate= null;
		try {
			fecha = formatoDelTexto.parse(strFecha);
			tdate= new Timestamp(fecha.getTime());	
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return tdate;
	}

	@Override
	protected void prepare() {
				
	}
}
