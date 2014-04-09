package compiere.model.cds.processes;


import java.awt.Container;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.logging.*;

import org.compiere.apps.ADialog;
import org.compiere.excel.Excel;
import org.compiere.grid.ed.VFile;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;


import compiere.model.cds.MiniTablePreparator;



/**
 * Clase que generará un archivo en Excel que 
 * permitirá observar los productos susceptibles a promoción de exhibición
 * 
 * @author Diana Rozo
 * */

public class XX_VisualMerchandisingPromo extends SvrProcess {

	/**	Logger			*/
	protected static CLogger log = CLogger.getCLogger(XX_VisualMerchandisingPromo.class);
	/**Tabla para guardar los datos */
	protected MiniTablePreparator miniTable = new MiniTablePreparator();
	
	/** Contexto general*/ 
	protected Ctx ctx = Env.getCtx();
	
	private String archivo = null;		
	Calendar c = Calendar.getInstance();
	String dia = Integer.toString(c.get(Calendar.DATE));
	int mes1= (c.get(Calendar.MONTH)+1);
	String mes =Integer.toString(c.get(Calendar.MONTH)+1);
	
	
	
	/**tabla resultado se calcula el query y se guarda en una minitable*/
	private MiniTablePreparator tablaResultado(){
		
		miniTable = new MiniTablePreparator();
		
		if (mes1< 10){
			
			 mes ='0'+mes;
			
		}
		if ((c.get(Calendar.DATE))< 10){
			
			 dia ='0'+dia;
			
		}
		
		
		// mes = Integer.toString(c.get(Calendar.MONTH)+1);
		String annio = Integer.toString(c.get(Calendar.YEAR));
		
		
		
		String FechaActual=annio+mes+dia;
		System.out.println("fecha "+FechaActual);
		String query = "WITH UNO AS (select(select (case when " +
				"\n(sum(pr.XX_INVAMOUNTORIGBUDGETED + pr.XX_FINALINVAMOUNTBUD)/2)>0 and " +
				"\n(sum(pr.XX_INVAMOUNTORIGBUDGETED + pr.XX_FINALINVAMOUNTBUD)/2)<1 then 1 " +
				"\nwhen (sum(pr.XX_INVAMOUNTORIGBUDGETED + pr.XX_FINALINVAMOUNTBUD)/2)=0 then 0 " +
				"\nelse round((sum(pr.XX_SALESAMOUNTBUD)*12)/(sum(pr.XX_INVAMOUNTORIGBUDGETED + pr.XX_FINALINVAMOUNTBUD)/2),2)end) " +
				"\nas rot_dept from XX_VMR_PRLD01 pr where pr.XX_VMR_DEPARTMENT_ID=inv.XX_VMR_DEPARTMENT_ID " +
				"\nand (SUBSTR(pr.XX_BUDGETYEARMONTH,5,7)=inv.XX_INVENTORYMONTH) " +
				"\nand (SUBSTR(pr.XX_BUDGETYEARMONTH,1,4) = inv.XX_INVENTORYYEAR) " +
				"\ngroup by XX_VMR_DEPARTMENT_ID) as h,sum(XX_INITIALINVENTORYAMOUNT) as MontoIni, " +
				"\nsum(inv.XX_INITIALINVENTORYQUANTITY + inv.XX_PREVIOUSADJUSTMENTSQUANTITY + " +
				"\ninv.XX_SHOPPINGQUANTITY - inv.XX_SALESQUANTITY + inv.XX_MOVEMENTQUANTITY  + " +
				"\ninv.XX_ADJUSTMENTSQUANTITY )as CantPromocionar,sum(inv.XX_INITIALINVENTORYAMOUNT + " +
				"\ninv.XX_PREVIOUSADJUSTMENTSAMOUNT + inv.XX_SHOPPINGAMOUNT - inv.XX_SALESAMOUNT " +
				"\n+inv.XX_MOVEMENTAMOUNT+inv.XX_ADJUSTMENTSAMOUNT) as MontoPromocionar, " +
				"\ninv.XX_VMR_DEPARTMENT_ID,inv.M_PRODUCT_ID,sum(inv.XX_INITIALINVENTORYQUANTITY) as invIni, " +
				"\ninv.XX_INVENTORYMONTH as mes, sum(inv.XX_SALESQUANTITY) as ventasMes, " +
				"\nsum(inv.XX_INITIALINVENTORYQUANTITY + inv.XX_PREVIOUSADJUSTMENTSQUANTITY + " +
				"\ninv.XX_SHOPPINGQUANTITY - inv.XX_SALESQUANTITY + inv.XX_MOVEMENTQUANTITY  + " +
				"\ninv.XX_ADJUSTMENTSQUANTITY )as inventario_final,(sum( (inv.XX_INITIALINVENTORYQUANTITY + " +
				"\ninv.XX_PREVIOUSADJUSTMENTSQUANTITY + inv.XX_SHOPPINGQUANTITY - inv.XX_SALESQUANTITY + " +
				"\ninv.XX_MOVEMENTQUANTITY + inv.XX_ADJUSTMENTSQUANTITY )+ inv.XX_INITIALINVENTORYQUANTITY )/2) as inv_promedio, " +
				"\n(Case when (sum( (inv.XX_INITIALINVENTORYQUANTITY + inv.XX_PREVIOUSADJUSTMENTSQUANTITY " +
				"\n+ inv.XX_SHOPPINGQUANTITY - inv.XX_SALESQUANTITY + inv.XX_MOVEMENTQUANTITY " +
				"\n+ inv.XX_ADJUSTMENTSQUANTITY )+ inv.XX_INITIALINVENTORYQUANTITY )/2)=0 then 0 " +
				"\nelse round((sum(inv.XX_SALESQUANTITY)*12)/(sum( (inv.XX_INITIALINVENTORYQUANTITY + " +
				"\ninv.XX_PREVIOUSADJUSTMENTSQUANTITY + inv.XX_SHOPPINGQUANTITY - inv.XX_SALESQUANTITY + " +
				"\ninv.XX_MOVEMENTQUANTITY + inv.XX_ADJUSTMENTSQUANTITY )+ inv.XX_INITIALINVENTORYQUANTITY )/2),2) end ) as rotacion " +
				"\nfrom XX_VCN_INVENTORY inv where inv.XX_INVENTORYMONTH="+mes+" and inv.XX_INVENTORYYEAR="+annio+" and " +
				"\ninv.M_PRODUCT_ID IN (select p.M_PRODUCT_ID from M_PRODUCT p, M_STORAGE m " +
				"\nwhere p.M_PRODUCT_ID = m.M_PRODUCT_ID and (m.QTYONHAND >0) group by(p.M_PRODUCT_ID)) " +
				"\n group by inv.M_PRODUCT_ID,inv.XX_VMR_DEPARTMENT_ID,inv.XX_INVENTORYMONTH,inv.XX_INVENTORYYEAR " +
				"\n), DOS AS (SELECT UNO.M_PRODUCT_ID,UNO.XX_VMR_DEPARTMENT_ID,UNO.H,UNO.ROTACION,UNO.invIni,UNO.MontoIni, " +
				"\nUNO.MontoPromocionar,UNO.CantPromocionar " +
				"\nFROM UNO WHERE (UNO.H > UNO.ROTACION) GROUP BY(UNO.M_PRODUCT_ID,UNO.XX_VMR_DEPARTMENT_ID,UNO.H,UNO.ROTACION,UNO.invIni,UNO.MontoIni,UNO.MontoPromocionar,UNO.CantPromocionar) " +
				"\n),TRES AS (select m.M_PRODUCT_ID,m.value from XX_VMR_PRICECONSECUTIVE pr,M_PRODUCT m,XX_VMR_RANGEDAYSPROMOTE rg,XX_VCN_INVENTORY inv " +
				"\nwhere pr.M_PRODUCT_ID= m.M_PRODUCT_ID and rg.XX_VMR_TYPEINVENTORY_ID=m.XX_VMR_TYPEINVENTORY_ID " +
				"\nand m.XX_VMR_CATEGORY_ID= rg.XX_VMR_CATEGORY_ID and inv.M_PRODUCT_ID =m.M_PRODUCT_ID and --D.M_PRODUCT_ID=m.M_PRODUCT_ID AND " +
				"\npr.M_PRODUCT_ID=inv.M_PRODUCT_ID and  ('"+FechaActual+"'- to_char(pr.CREATED, 'YYYYMMDD') >= rg.XX_RANGE1) " +
				"\nand ('"+FechaActual+"' - to_char(pr.CREATED, 'YYYYMMDD') >= rg.XX_RANGE2) and inv.XX_INVENTORYMONTH="+mes+" " +
				"\nand inv.XX_INVENTORYYEAR="+annio+" group by(m.M_PRODUCT_ID,inv.XX_CONSECUTIVEPRICE,m.value)" +
				"\nhaving (sum(inv.XX_INITIALINVENTORYQUANTITY + inv.XX_PREVIOUSADJUSTMENTSQUANTITY + inv.XX_SHOPPINGQUANTITY - " +
				"\ninv.XX_SALESQUANTITY + inv.XX_MOVEMENTQUANTITY  + inv.XX_ADJUSTMENTSQUANTITY )>0)) " +
				"\nSELECT TRES.M_PRODUCT_ID,tres.value,M.NAME,dept.NAME,DOS.H,DOS.ROTACION,DOS.invIni,DOS.MontoIni,DOS.MontoPromocionar,DOS.CantPromocionar " +
				"\nFROM TRES, DOS,XX_VMR_Department dept,M_PRODUCT m " +
				"\nWHERE DOS.M_PRODUCT_ID = TRES.M_PRODUCT_ID AND DOS.XX_VMR_Department_ID= dept.XX_VMR_Department_ID " +
				"\nand DOS.M_PRODUCT_ID=m.M_PRODUCT_ID AND TRES.M_PRODUCT_ID=m.M_PRODUCT_ID " +
				"\nGROUP BY (TRES.M_PRODUCT_ID,M.NAME,dept.NAME,DOS.H,DOS.ROTACION,DOS.invIni,tres.value,DOS.MontoIni,DOS.MontoPromocionar,DOS.CantPromocionar)" ;
						
		String select=" ";
		
		Vector<ColumnInfo> columnasAgregadas = new Vector<ColumnInfo>();
		
		ColumnInfo colProdId = new ColumnInfo(Msg.translate(ctx, "ProductKey"),
				"\nTRES.M_PRODUCT_ID",String.class);
		ColumnInfo colValue = new ColumnInfo(Msg.translate(ctx, "Value"),
				"\nTRES.Value",String.class);
		ColumnInfo colProd = new ColumnInfo(Msg.translate(ctx, "XX_Product"),
				"\nM.NAME",String.class);
		ColumnInfo colDept = new ColumnInfo(Msg.translate(ctx, "XX_Department_I"),
				"\ndept.NAME",String.class);
		ColumnInfo colCantInvIni = new ColumnInfo(Msg.translate(ctx, "XX_InventoryQtyIni"),
				"\nDOS.invIni",Integer.class);
		ColumnInfo colMontoInvIni = new ColumnInfo(Msg.translate(ctx, "XX_InitialAmount"),
				"\nDOS.MontoIni",Double.class);
		ColumnInfo colRot = new ColumnInfo(Msg.translate(ctx, "XX_rotation"),
				"\nDOS.ROTACION",Double.class);
		ColumnInfo colRotDept = new ColumnInfo(Msg.translate(ctx, "XX_DeptBudgRotation"),
				"\nDOS.H",Double.class);
		ColumnInfo colCantPromo = new ColumnInfo(Msg.translate(ctx, "XX_QtyPromote"),
				"\nDOS.CantPromocionar",Integer.class);
		ColumnInfo colMontoPromo = new ColumnInfo(Msg.translate(ctx, "XX_PromoteAmount"),
				"\nDOS.MontoPromocionar",Double.class);
		
		
		//Agrego las columnas 
		columnasAgregadas.add(colProdId);
		columnasAgregadas.add(colValue);
		columnasAgregadas.add(colProd);		
		columnasAgregadas.add(colDept);
		columnasAgregadas.add(colCantInvIni);
		columnasAgregadas.add(colMontoInvIni);
		columnasAgregadas.add(colRot);
		columnasAgregadas.add(colRotDept);
		columnasAgregadas.add(colCantPromo);
		columnasAgregadas.add(colMontoPromo);
		
		ColumnInfo [] layout = new ColumnInfo [columnasAgregadas.size()];
		columnasAgregadas.toArray(layout);
		
		
		select = "\n" + miniTable.prepareTable(layout, null, null, false, null);
	
		String sql =  query;		
		System.out.println(sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			miniTable.loadTable(rs);
			miniTable.repaint();
		} catch (SQLException e) {			
			log.log(Level.SEVERE, sql, e);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql, e);			
		} finally {
			//Cerrar los statements
			if (rs != null)
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {}
			
			if (ps != null)
				try {
					ps.close();
					ps = null;
				} catch (SQLException e) {}		
		}			
		return(miniTable);
	
	}
	
	@Override
	protected String doIt() throws Exception {
		//System.out.println(fecha.format(fechaActual));		
		 boolean flag = false;
		 boolean archivoExistente = false;
		//String direccion= "C:/Documents and Settings/soporte/Mis documentos/h3.xls ";
		
		File addressFile = new File(archivo);		
		
		String msg = "";
		try {			
			
			if (archivo == null) {
				msg =  Msg.translate( getCtx(), "File Not Loaded");
				return msg;
			} else {
				
				if(!archivo.substring(archivo.length()-4, archivo.length()).equals(".xls"))
					if(!archivo.substring(archivo.length()-5, archivo.length()).equals(".xlsx")){
						msg =Msg.translate( getCtx(), "Not Excel");
						return msg;
						
				}else if (addressFile.exists()) {
					
					archivoExistente = ADialog.ask(1, new Container(), "XX_msgArchiveExist");
					flag =true;
					}
				
				if ((flag && archivoExistente ) || ((flag == false) && (archivoExistente==false)))
					{					
						msg = imprimirArchivo(tablaResultado(),addressFile);
					}else
					{
						msg= "Operacion sin exito";
					}
				
			
			}	
			
		} catch (Exception ex) {
			log.log(Level.SEVERE, "", ex);
		}	
		
		return msg;	
	}

	@Override
	protected void prepare() {
		ProcessInfoParameter[] parameter = getParameter();

		for (ProcessInfoParameter element : parameter) {
			
			if (element.getParameter()!=null) {
				if (element.getParameterName().equals("File") ) {
					archivo = element.getParameter().toString();
				}
			}			
		}

	}
	
	
	
	/**Metodo que imprime la tabla de resultados en el nuevo libro de excel*/
	public static String imprimirArchivo (MiniTablePreparator tabla, File archivo) {		
		//Crea el libro excel
		Excel archivoGenerado = new Excel(archivo);
		//Escribe la tabla de resultados en el libro de excel
		createEXCEL(archivoGenerado, tabla);						
		
		return "Proceso completado";
	}
	
	/** Usando el método de Crear Excel en ReporEngine con modificaciones*/ 
	public static Excel createEXCEL (Excel excel, MiniTablePreparator miniTable) {
		final int COTA_EXCEL = Short.MAX_VALUE; 
		try {
					
						
			for (int row = -1; row < miniTable.getRowCount(); row++) {
				if (row % COTA_EXCEL == COTA_EXCEL - 1) {
					excel.createAndSetSheet("Export Compiere " + ( 1 + row / COTA_EXCEL));
				}				
				//Para todas las columnas
				int colPos = 0;				
				for (int col = 0; col < miniTable.getColumnCount(); col++){
					if (row == -1 || row % COTA_EXCEL == COTA_EXCEL - 1) {														
						excel.createRow(
								(short)0, 
								(short)colPos, 
								miniTable.getColumnName(col), 
								null,
								Excel.CELLSTYLE_HEADER,
								Excel.DISPLAY_TYPE_STRING);							
					} else 	{							
						int displayType = Excel.DISPLAY_TYPE_STRING;
						Object obj = miniTable.getValueAt(row, col);
						String valor = "";
						if (obj == null)
							;
						else  {
							if (obj instanceof Number) {							
								displayType = Excel.DISPLAY_TYPE_NUMBER;
								valor = DisplayType.getNumberFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else if (obj instanceof Integer) {
								displayType = Excel.DISPLAY_TYPE_INTEGER;
								valor = DisplayType.getNumberFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else if (obj instanceof Date) {																
								displayType = Excel.DISPLAY_TYPE_DATE;
								valor = DisplayType.getDateFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else if (obj instanceof Timestamp) {
								displayType = Excel.DISPLAY_TYPE_DATE;
								valor = DisplayType.getDateFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else {
								valor = obj.toString();
							}
						}						
						/* Hecho por Javier Pino, sustituyendo la llamada por otro método overloaded  */														
						excel.createRow(
								//(row + 1), -- Comentado y modificado para que imprima desde el principio de las hojas sucesivas
								(short)(row % COTA_EXCEL + 1),
								(short)colPos, 
								valor, 
								valor,
								Excel.CELLSTYLE_NONE,
								displayType);							
					}
					colPos++;
				}	//	printed
			}	//	for all columns
		}	//	for all rows
		catch (Exception e){
			log.log(Level.SEVERE, "createCSV(w)", e);
		}
		excel.close();
		return excel;
	}

}
