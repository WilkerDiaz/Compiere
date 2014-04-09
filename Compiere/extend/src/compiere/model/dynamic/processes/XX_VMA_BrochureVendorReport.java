package compiere.model.dynamic.processes;

import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.compiere.apps.ADialog;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import compiere.model.dynamic.MVMABrochure;


/** Proceso que genera el reporte de proveedores de un folleto divididos por
 * Categoría y página del folleto.
 * @author mvintimilla
 * */

public class XX_VMA_BrochureVendorReport  extends SvrProcess{
	
	// Variables
	private boolean errorArchivo = false;
	private int m_Brochure_ID = 0;
	private MVMABrochure brochure = null;
	private String nombreArchivo = "";

	@Override
	protected String doIt() throws Exception {
		File archivo = new File(nombreArchivo);
		writeDataSheet(archivo);
		return "";

	}// Fin doIt
	
	/** writeDataSheet
	 * Se escribe en el archivo el resultado de la consulta
	 * @param s Hoja de Excel 
	 * @throws IOException **/
	private void writeDataSheet(File archivo) throws WriteException, IOException {
		WritableWorkbook workbook = Workbook.createWorkbook(archivo);
		WritableSheet s = workbook.createSheet("Reporte",0); 
		s.setColumnView(0, 70);
		s.setColumnView(1, 20);
		WritableFont boldf = new WritableFont(WritableFont.ARIAL,12, WritableFont.BOLD);
		boldf.setColour(Colour.WHITE);
		WritableCellFormat cf1 = new WritableCellFormat(boldf);
		cf1.setBackground(Colour.BLUE_GREY);
		cf1.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
		cf1.setWrap(true);
		WritableFont noboldf = new WritableFont(WritableFont.ARIAL,11, WritableFont.NO_BOLD);
		WritableCellFormat cf2 = new WritableCellFormat(noboldf);
		cf2.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
		cf2.setWrap(true);
		int fila = 2;

		brochure = new MVMABrochure(Env.getCtx(), m_Brochure_ID, null);
		/* Contenido del Reporte: Proveedor, Referencia,Categoría,Página */	
		Label brochureName = new Label(0, 0, "Folleto: "+brochure.getName(), cf1);
		s.addCell(brochureName); 
		Label prov = new Label(0, 1, "Proveedor", cf1);
		s.addCell(prov); 
		Label cat = new Label(1, 1, "Categoria", cf1);
		s.addCell(cat);
		Label pag = new Label(2, 1, "Pagina", cf1);
		s.addCell(pag); 
		
		// Se toman los datos de los proveedores, categoría y número de página
		String SQLReporte = " SELECT CB.NAME PROVE, " +
			" C.NAME CAT, " +
			" BP.XX_VMA_PAGENUMBER NUM " +
			" FROM XX_VME_PRODUCT P INNER JOIN XX_VME_REFERENCE R " +
			" ON (P.XX_VME_REFERENCE_ID = R.XX_VME_REFERENCE_ID) " +
			" INNER JOIN XX_VMR_VENDORPRODREF RR " +
			" ON (RR.XX_VMR_VENDORPRODREF_ID = R.XX_VMR_VENDORPRODREF_ID)" +
			" INNER JOIN XX_VME_ELEMENTS E " +
			" ON (R.XX_VME_ELEMENTS_ID = E.XX_VME_ELEMENTS_ID) " +
			" INNER JOIN C_BPARTNER CB " +
			" ON (P.C_BPARTNER_ID = CB.C_BPARTNER_ID)" +
			" INNER JOIN XX_VMA_BROCHUREPAGE BP " +
			" ON (E.XX_VMA_BROCHUREPAGE_ID = BP.XX_VMA_BROCHUREPAGE_ID) " +
			" INNER JOIN XX_VMR_DEPARTMENT D " +
			" ON (R.XX_VMR_DEPARTMENT_ID = D.XX_VMR_DEPARTMENT_ID) " +
			" INNER JOIN XX_VMR_CATEGORY C " +
			" ON (D.XX_VMR_CATEGORY_ID = C.XX_VMR_CATEGORY_ID) " +
			" WHERE E.XX_VMA_BROCHURE_ID = " + m_Brochure_ID +
			" AND E.ISACTIVE = 'Y' " +
			" AND E.XX_VME_TYPE IN ('P', 'M') " ;
//		System.out.println("SQLReporte: " + SQLReporte);

		PreparedStatement psQueryReporte = null;
		ResultSet rsQueryReporte = null;

		try{
			psQueryReporte = DB.prepareStatement(SQLReporte, null);
			rsQueryReporte = psQueryReporte.executeQuery();
			while(rsQueryReporte.next()){
				// Se escriben los valores en el archivo
				Label contentProv = new Label(0, fila, rsQueryReporte.getString("PROVE"),cf2);
				s.addCell(contentProv); 
				Label contentCat = new Label(1, fila, rsQueryReporte.getString("CAT"),cf2);
				s.addCell(contentCat);
				Label contentPag = new Label(2, fila, rsQueryReporte.getString("NUM"),cf2);
				s.addCell(contentPag);
				fila++;
			}// while

		}//try		
		catch(SQLException e){ e.printStackTrace(); }
		finally {
			DB.closeResultSet(rsQueryReporte);
			DB.closeStatement(psQueryReporte);
		}
		workbook.write();
		workbook.close(); 
		
		//El archivo fue creado
		String msg = Msg.getMsg(Env.getCtx(), "XX_FileCreated", new String [] {
			nombreArchivo
		});
		
		ADialog.info(1, new Container(), msg);	

	}// writeDataSheet
		
	@Override
	protected void prepare() {
		m_Brochure_ID = getRecord_ID();
		ProcessInfoParameter[] parameter = getParameter();
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();
			String extension = "";
			if (element.getParameter() == null) ;
			// Parametro Archivo a ser importado
			else if (name.equals("File")) { 
				if (element.getParameter() != null) {					
					String archivo = (String)element.getParameter();
					
					// Verifica si el archivo existe
					File archivoFisico = new File(archivo);
					if (archivoFisico.exists()){
						ADialog.error(1, new Container(), "XX_FileExist" );
						errorArchivo = true;
						return;	
					}
					
					// Se obtiene la extensión del archivo seleccionado
					extension = archivo.substring(archivo.length()-4, archivo.length());
					
					// Verifica la extensión del archivo seleccionado
					if (!extension.equals(".xls")) {
						ADialog.error(1, new Container(), "Not Excel" );
						errorArchivo = true;
						return;		
					}	
					
					nombreArchivo = (String) element.getParameter();
				}//getparameter 
			}//else if Archivo
		}// For
		
	}// Fin prepare	
}// Fin XX_VMA_BrochureVendorReport
