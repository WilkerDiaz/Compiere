package compiere.model.cds.processes;

import java.awt.Container;
import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import javax.swing.JFileChooser;

import org.compiere.apps.ADialog;
import org.compiere.excel.Excel;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.forms.indicator.XX_Indicator;


/** Reporte de Gasto de mercancía 
 * *@author ghuchet*/
public class XX_ExportExcelStoreCharge extends  SvrProcess{

	private String iniDate = null;
	private String endDate = null;
	private int categoryID = 0;
	
	/** La tabla donde se guardarán los datos */
	protected MiniTablePreparator miniTable = new MiniTablePreparator();
	
	private File bFile = null;
	private boolean fileError = false; 
	private String fileName = null;

	//Cabeceras de las columnas para tabla
		private ColumnInfo[] columns = new ColumnInfo[] {
				new ColumnInfo("NUMORD",".", String.class),
				new ColumnInfo("FECHA_EST_LLEGADA", ".", String.class),
				new ColumnInfo("COSTO_ORC", ".", String.class),
				new ColumnInfo("NUM_FACTURA", ".", String.class),
			    new ColumnInfo("FECHA_EMISION", ".", String.class),
				new ColumnInfo("COSTO_FACTURA", ".", String.class),
				new ColumnInfo("FECHA_FAC", ".", String.class),
				new ColumnInfo("NUMREC", ".", String.class),
				new ColumnInfo("FECHA_REC", ".", String.class),
				new ColumnInfo("FECHA_CHEQ", ".", String.class),
				new ColumnInfo("FACTOR_DEF", ".", String.class),
				new ColumnInfo("DEPARTAMENTO", ".", String.class),
				new ColumnInfo("GUIAEM", ".", String.class),
				new ColumnInfo("GMM_OC", ".", String.class),
				new ColumnInfo("FACT_GMM", ".", String.class),
				new ColumnInfo("COMPRAS_AL_COSTO", ".", String.class),	
		};

	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (name.equals("XX_DateFrom")) {
				iniDate = DB.TO_DATE((Timestamp)element.getParameter(), true);
			}else if (name.equals("XX_DateTo")) {
				endDate = DB.TO_DATE((Timestamp)element.getParameter(), true);	
			} else if (name.equals("XX_VMR_Category_ID")) {
				categoryID = element.getParameterAsInt();				
			} else if (name.equals("File")) {
				if (element.getParameter() != null) {					
					String extension = (String)element.getParameter();
					
					if(extension.length()>4){
						extension = extension.substring(extension.length()-4, extension.length());
					}else {
						ADialog.error(1, new Container(), "Not Excel" );
						fileError = true;
						return;
					}
					
					if (!extension.equals(".xls")) {
						ADialog.error(1, new Container(), "Not Excel" );
						fileError = true;
						return;		
					}

					File physicalFile = new File((String) element.getParameter());
					if (physicalFile.exists()) {
						ADialog.error(1, new Container(), "XX_FileExist" );					
						fileError = true;
						return;
					} 
					fileName = (String) element.getParameter();
					bFile = new File(fileName);
				} 
			} else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);			
		}
		
	}
	@Override
	protected String doIt() throws Exception {
		
		if (fileError){
			return "";
		}
		
		String sql = "SELECT b.documentno as NUMORD, "+
		"\nnvl(to_char(b.XX_ESTIMATEDDATE, 'DD/MM/YYYY'), 0) as fecha_Est_llegada, " +
		"\nto_char(b.TOTALLINES, '999G999G999D99') as Costo_ORC, " +
		"\nB.XX_VENDORINVOICE Num_factura, " +
		"\nnvl(to_char(B.XX_VENDINVOICEEMISIONDATE, 'DD/MM/YYYY'), 0) AS fecha_Emision, " +
		"\nto_char(b.XX_VendorInvoiceAmount, '999G999G999D99')  as Costo_Factura,  " +
		"\nnvl(to_char(b.xx_invoicedate, 'DD/MM/YYYY'), 0) as fecha_Fac, " +
		"\nh.documentno as numrec,  " +
		"\nnvl(to_char(b.xx_receptiondate, 'DD/MM/YYYY'), 0) as fecha_rec,  " +
		"\nnvl(to_char(b.xx_checkupdate, 'DD/MM/YYYY'), 0) as fecha_cheq, " +
		"\nto_char(b.XX_DEFINITIVEFACTOR, '999G999G999D99')  as Factor_Def,  " +
		"\n(z.value || '-' || z.name) as departamento, " +
		"\ni.value as GuiaEm, " +
		"\nTO_CHAR(b.XX_VendorInvoiceAmount * (i.XX_REALMERCHMANCOST / (SELECT SUM(XX_VendorInvoiceAmount) FROM C_ORDER B  " +
		"\nWHERE i.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_Id AND B.ISSOTRX = 'N')), '999G999G999D99') as GMM_OC, " +
		"\nTO_CHAR((i.XX_REALMERCHMANCOST / (SELECT SUM(XX_VendorInvoiceAmount) FROM C_ORDER B  " +
		"\nWHERE i.XX_VLO_BOARDINGGUIDE_ID = B.XX_VLO_BOARDINGGUIDE_Id AND B.ISSOTRX = 'N')), '999G999G999D999') as Fact_GMM, " +
		"\nto_char((b.TOTALLINES * b.XX_DEFINITIVEFACTOR) , '999G999G999D99') as Compras_al_Costo " +
		"\nFROM C_ORDER B,  C_COUNTRY f, m_inout h,  " +
		"\nXX_VLO_BOARDINGGUIDE i, xx_vmr_department z " +
		"\nwhere  " +
		"\nb.C_COUNTRY_ID = f.C_COUNTRY_ID and " +
		"\nb.c_order_id = h.c_order_id and " +
		"\nb.XX_VLO_BOARDINGGUIDE_id = i.XX_VLO_BOARDINGGUIDE_id and " +
		"\nb.xx_vmr_department_id = z.xx_vmr_department_id and " +
		"\n((trunc(b.xx_receptiondate) >=  " +iniDate+
		"\n   and trunc(b.xx_receptiondate) <= " +endDate+") or "+
		"\n(trunc(b.xx_checkupdate) >= " +iniDate+
		"\n   and trunc(b.xx_checkupdate) <= "+endDate+") or " +
		"\n(trunc(b.xx_invoicedate) >=   " +iniDate+
		"\n   and trunc(b.xx_invoicedate) <= "+endDate+")) " +
		"\nand b.xx_PoType = 'POM' AND " +
		"\nb.XX_ORDERSTATUS = 'CH' and b.issotrx = 'N' AND F.VALUE <> '1VE'  " +
		"\nand h.xx_inoutstatus <> 'AN' " +
		"\norder by b.documentno";
		
		//System.out.println(sql);
		PreparedStatement ps = DB.prepareStatement(sql, null); 
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			miniTable.prepareTable(columns,"", "", true, "");
			miniTable.loadTable(rs);
			miniTable.repaint();
		}catch (Exception e){
			e.printStackTrace();
			String msg = "Ocurrió un error al intentar crear el archivo. Contacte al administrador del sistema.";
			ADialog.error(1, new Container(), msg);
			return "No se pudo completar el proceso.";
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		if(miniTable.getRowCount()>0){
			Excel excelFile = new Excel(bFile);
			XX_Indicator.createEXCEL(excelFile, miniTable);
			//El archivo fue creado
			String msg = Msg.getMsg(Env.getCtx(), "XX_FileCreated", new String [] {
				fileName});
			ADialog.info(1, new Container(), msg);
		}else {
			String msg = "No hay Registros para la fecha y categoría escogida.";
			ADialog.info(1, new Container(), msg);
		}
	
		return "Proceso Completado!";
	}
	
	

}
