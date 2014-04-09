package compiere.model.cds.forms.indicator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.common.constants.DisplayTypeConstants;
import org.compiere.excel.Excel;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VFile;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.swing.CLabel;
import org.compiere.swing.CTextField;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

import compiere.model.cds.MiniTablePreparator;
import compiere.model.cds.X_Ref_XX_OrderType;

public class XX_TimeWarehouseDischarge extends XX_Indicator {

	/**Indicador de Tiempo de Descarga en Almacén
	 *  @author Gabrielle Huchet
	 *  @version 
	 */
	private static final long serialVersionUID = 829143095550133166L;
	//Campos necesarios para este indicador 
	private CLabel labelOCType = new CLabel(Msg.translate(ctx, "XX_OrderType"));
	private VComboBox comboOCType= new VComboBox();
	private CLabel labelDate = new CLabel(Msg.translate(ctx, "XX_Date"));
	private VDate dateFrom = new VDate(Msg.translate(Env.getCtx(), "DateFrom"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateFrom"));
	private VDate dateTo = new VDate(Msg.translate(Env.getCtx(), "DateTo"),
			false, false, true, DisplayTypeConstants.Date, Msg.translate(Env
					.getCtx(), "DateTo"));
	
	private CLabel labelTotalGoal= new CLabel("Meta Tiempo de Descarga");  
	private CTextField totalGoal = new CTextField();   // Meta anual (importada o nacional)
	private CLabel labelTimeAccumulatedAvg= new CLabel("Tiempo Promedio Acumulado");  
	private CTextField timeAccumulatedAvg = new CTextField();   // Tiempo Promedio Acumulado
	private CLabel labelComplianceRate= new CLabel("% Cumplimiento");
	private CTextField complianceRate = new CTextField();   // Porcentaje de Cumplimiento
	
	private Double goal = 0.0; 
	private Double compliance = 0.0;
	private Double timeAccumulated = 0.0;
	
	private ColumnInfo[] layout = new ColumnInfo[] {
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_Order_ID"),".", String.class),
			new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"),".", String.class),
			new ColumnInfo("Cantidad de Productos",".", Integer.class),
		    new ColumnInfo("Fecha Entrada",".", String.class),
			new ColumnInfo("Hora Entrada",".", String.class),
			new ColumnInfo("Fecha Salida",".", String.class),
			new ColumnInfo("Hora Salida",".", String.class),
			new ColumnInfo("Tiempo Descarga",".", String.class),
			
	};
	
	protected void ocultarParametrosDefecto() {
		
		for (int i = 0; i < ELEMENTOS; i++) {
			ocultarParametro(i);
		}		
	}

	
	protected void agregarParametros() {

		//Agregar Tipo de Orden de Compra, fecha inicio, fecha fin
		agregarParametro(labelOCType);
		agregarParametro(comboOCType,2);
		agregarParametro(labelDate);
		agregarParametro(dateFrom);
		agregarParametro(dateTo);
		
		totalGoal.setEditable(false);
		timeAccumulatedAvg.setEditable(false);
		complianceRate.setEditable(false);
		
		totalGoal.setPreferredSize(new Dimension(80,20));
		timeAccumulatedAvg.setPreferredSize(new Dimension(80,20));
		complianceRate.setPreferredSize(new Dimension(80,20));
		
		//Agregar los labels en el panel de resultados	
		resultPanel.add(labelTotalGoal,new GridBagConstraints(
				0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(totalGoal,new GridBagConstraints(
				2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelTimeAccumulatedAvg,new GridBagConstraints(
				4, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(timeAccumulatedAvg,new GridBagConstraints(
				6, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(labelComplianceRate,new GridBagConstraints(
				8, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		resultPanel.add(complianceRate,new GridBagConstraints(
				9, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		miniTable.setAutoResizeMode(4);
	}
	
	
	protected void personalizar() {
		uploadOrderType();
		configurar(bPrint,false);
		configurar(bFile,false);

	}

	/** En este método agrego acciones a las acciones predeterminadas */
	public void actionPerformed(ActionEvent e) {
		
		//Ejecutará las acciones por defecto
		desactivarActionListeners();
		//Imprimir la seleccion
		if(e.getSource() == bSearch){
			if(miniTable.getRowCount()<1){
				configurar(bPrint,false);
				configurar(bFile,false);
			
			}else {
				configurar(bPrint,true);
				configurar(bFile,true);
			}
		}
		if (e.getSource() == bPrint)
		{
			Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
			Cursor defaultCursor = Cursor.getDefaultCursor();
			m_frame.setCursor(waitCursor);
			try {
				String[][]totals = new String[3][2];
				totals[0][0]= labelTotalGoal.getText();
				totals[0][1]= totalGoal();
				totals[1][0]=labelTimeAccumulatedAvg.getText();
				totals[1][1]=timeAccumulatedAvg.getText();
				totals[2][0]=labelComplianceRate.getText();
				totals[2][1]=complianceRate.getText();
				imprimirArchivoWithTotals(miniTable, totals, bFile, m_WindowNo, m_frame); 
			} catch (Exception ex) {
				log.log(Level.SEVERE, "", ex);
			}			
			m_frame.setCursor(defaultCursor);
		}
		else {
			super.actionPerformed(e);
		}
		activarActionListeners();

	}
	
	

	protected void llenarTabla() {
		if(dateFrom.getValue()== null || dateTo.getValue()== null){
			String msg = "Debe llenar los campos de fecha antes de realizar la búsqueda";
			ADialog.error(m_WindowNo, m_frame, msg);
			return;
		}
		m_frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		//Si no se ha cargado el header previamente
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		dataPane.getViewport().add(miniTable);
		miniTable.prepareTable(layout, "", "", false, "");
		
		//Calcular el query
		try {
			calcularQuery();
	
			timeAccumulated = timeAccumulatedAvg();
			String timeAccumulatedText= timeFormat(timeAccumulated);
			//LLamada al calculo de porc de los indicadores
			totalGoal.setText(totalGoal());
			timeAccumulatedAvg.setText(timeAccumulatedText);
			if(goal!=0.0){
				compliance = (double) Math.rint(timeAccumulated/goal * 100 * 100)/100;
			}
			complianceRate.setText(compliance.toString());
					
			System.out.println("\n"+goal);
			System.out.println("\n"+timeAccumulated);
			System.out.println("\n"+compliance);
			miniTable.setRowSelectionAllowed(true);
			miniTable.setSelectionBackground(Color.white);
			miniTable.setAutoResizeMode(4);
			miniTable.setRowHeight(miniTable.getRowHeight() + 2 );
			miniTable.getTableHeader().setReorderingAllowed(false);			
			if (miniTable.getRowCount() > 0) {
				configurar(bFile, true);				
				configurar(bPrint, true);
			} else {
				configurar(bFile, false);
				configurar(bPrint, false);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "", e);
		}
		
		m_frame.setCursor(Cursor.getDefaultCursor());
		
		
	}
	private String timeFormat(double hours) {
		
		String result ="";
		double min = (hours - (int)(hours))*60;
		int sec = (int) Math.round((min-(int)(min))*60);
		int hou = (int)(hours);
		
		String h =""+hou;
		String m =""+(int)min;
		String s =""+sec;
		if(hou <10)
			h="0"+hou;
		if(min <10)
			m ="0"+(int)min;
		if(sec <10)
			s ="0"+sec;
		
		result=h+":"+m+":"+s;
		return result;
	}


	private void calcularQuery() {
		
		//Se libera las filas de la tabla, se crea una nueva y se agrega a la ventana
	
		Timestamp from = (Timestamp) dateFrom.getValue();
		Timestamp to = (Timestamp) dateTo.getValue();
		
		String orderType = ((KeyNamePair)comboOCType.getSelectedItem()).getName();
		System.out.println("\n"+orderType);
		String sql = "\nselect o.documentno as orden, " +
			"\n(v.value||'-'||v.name) as proveedor,  " +
			"\nsum(iol.PICKEDQTY) cantidad_productos, " +
			"\nTO_CHAR(o.xx_entrancedate,'dd/mm/yyyy') as fecha_entrada,  " +
			"\nTO_CHAR(o.xx_entrancedate,'HH:mi:ss am')  as horaentrada,  " +
			"\nTO_CHAR(o.xx_receptiondate,'dd/mm/yyyy') as fecha_salida,  " +
			"\nTO_CHAR(o.xx_receptiondate,'HH:mi:ss am') as horasalida,  " +
			"\nCASE WHEN SUBSTR(numtodsinterval(o.xx_receptiondate-o.xx_entrancedate,'day'),10,1) = 1 THEN SUBSTR(numtodsinterval(o.xx_receptiondate-o.xx_entrancedate,'day'),10,1)||' dia ' " +
			"\nWHEN SUBSTR(numtodsinterval(o.xx_receptiondate-o.xx_entrancedate,'day'),10,1) > 1 THEN SUBSTR(numtodsinterval(o.xx_receptiondate-o.xx_entrancedate,'day'),10,1)||' dias ' END " +
			"\n||SUBSTR(numtodsinterval(o.xx_receptiondate-o.xx_entrancedate,'day'),12,8) as tiempodescarga " +
			"\nfrom c_order o, c_bpartner v, m_inout io, m_inoutline iol " +
			"\nwhere o.c_bpartner_id=v.c_bpartner_id  " +
			"\nand o.c_order_id = io.c_order_id " +
			"\nand iol.m_inout_id = io.m_inout_id " +
			"\nand o.xx_orderstatus in ('RE','CH') " +
			"\nand o.xx_entrancedate between "+DB.TO_DATE(from, true)+ " and "+DB.TO_DATE(to, true);
			
			if(orderType.compareTo("Importada")==0)
				sql +="\nand o.xx_ordertype = 'Importada' ";
			else if(orderType.compareTo("Nacional")==0)
				sql +="\nand o.xx_ordertype = 'Nacional' ";
					
		sql +="\ngroup by o.documentno,(v.value||'-'||v.name), o.xx_entrancedate, o.xx_receptiondate";
		
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
			
	}
	
	private String totalGoal() {
		String goalText = "";
		
		String orderType = ((KeyNamePair)comboOCType.getSelectedItem()).getName();
		String sql = null;
		if(orderType.compareTo("Nacional")==0)
			sql= "\nselect xx_nationalgoalhours+(xx_nationalgoalmins/60)+(xx_nationalgoalseconds/3600),(case when xx_nationalgoalhours<10 then ('0'||xx_nationalgoalhours)  " +
			"\nelse to_char(xx_nationalgoalhours) end)||':'||(case when xx_nationalgoalmins<10 then ('0'||xx_nationalgoalmins)  " +
			"\nelse to_char(xx_nationalgoalmins) end)||':'||(case when xx_nationalgoalseconds<10 then ('0'||xx_nationalgoalseconds)  " +
			"\nelse to_char(xx_nationalgoalseconds) end) " +
			"\nfrom xx_vlo_unloadtimegoal where rownum=1";
		else if(orderType.compareTo("Importada")==0)
			sql = "\nselect xx_importedgoalhours+(xx_importedgoalmins/60)+(xx_importedgoalseconds/3600),(case when xx_importedgoalhours<10 then ('0'||xx_importedgoalhours)  " +
			"\nelse to_char(xx_importedgoalhours) end)||':'||(case when xx_importedgoalmins<10 then ('0'||xx_importedgoalmins)  " +
			"\nelse to_char(xx_importedgoalmins) end)||':'||(case when xx_importedgoalseconds<10 then ('0'||xx_importedgoalseconds)  " +
			"\nelse to_char(xx_importedgoalseconds) end) " +
			"\nfrom xx_vlo_unloadtimegoal where rownum=1";
			
		System.out.println("\n"+sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next())
			{
				 goal= rs.getDouble(1);	
				 goalText = rs.getString(2);	
				
			}			
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
		return goalText;
	}


	private Double timeAccumulatedAvg() {
		Double result=0.0;
		Timestamp from = (Timestamp) dateFrom.getValue();
		Timestamp to = (Timestamp) dateTo.getValue();
		
		String orderType = ((KeyNamePair)comboOCType.getSelectedItem()).getName();
		
		String sql = "\nWITH HORAS_PRODUCTOS as (select sum(iol.PICKEDQTY) cant_prod, " +
			"\n(o.xx_receptiondate-o.xx_entrancedate)*24 as horas  " +
			"\nfrom c_order o, m_inout io, m_inoutline iol " +
			"\nwhere o.c_order_id = io.c_order_id " +
			"\nand iol.m_inout_id = io.m_inout_id " +
			"\nand o.xx_orderstatus in ('RE','CH') " +
			"\nand o.xx_entrancedate between "+DB.TO_DATE(from, true)+ " and "+DB.TO_DATE(to, true);
			
		if(orderType.compareTo("Importada")==0)
			sql +="\nand o.xx_ordertype = 'Importada' ";
		else if(orderType.compareTo("nacional")==0)
			sql +="\nand o.xx_ordertype = 'Nacional' ";
		
		sql +="\ngroup by o.documentno, o.xx_entrancedate, o.xx_receptiondate)"+
			"SELECT sum(cant_prod*horas)/sum(cant_prod) "+
			"FROM HORAS_PRODUCTOS ";
		
		System.out.println("\n"+sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = DB.prepareStatement(sql, null);			
			rs = ps.executeQuery();
			while (rs.next())
			{
				result = rs.getDouble(1);		
			}			
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
	
		return result;
	}

	public void limpiarFiltro(){
		super.limpiarFiltro();
		
		configurar(comboOCType, true);
		dateFrom.setValue(null);
		dateTo.setValue(null);
		miniTable.setRowCount(0);
		miniTable = new MiniTablePreparator();
		totalGoal.setText("");
		timeAccumulatedAvg.setText("");
		complianceRate.setText("");
		goal =0.0;
		timeAccumulated = 0.0;
		compliance =0.0;
		
	}

	
	/** Carga datos del filtro de estado de O/C */
	private void uploadOrderType() {
		comboOCType.removeAllItems();
		int i=0;
		KeyNamePair knp;
		try{
			for (X_Ref_XX_OrderType  v : X_Ref_XX_OrderType.values()) {
				knp = new KeyNamePair(i,v.getValue());
				comboOCType.addItem(knp);
				i++;
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, null, e);
		}
	
		comboOCType.setEnabled(true);		
		comboOCType.setEditable(true);
		comboOCType.setSelectedIndex(0);
	}
	
	private void imprimirArchivoWithTotals(MiniTablePreparator tabla,
			String[][] totals, VFile archivo, int ventana, FormFrame contenedor) {

		if (archivo.getValue() == null || archivo.getValue().equals("")) {
			ADialog.error(ventana, contenedor, "XX_FileForExport");
			return;
		}
		File archivoFisico = new File((String)archivo.getValue());
		
		//Verificar la extension
		String extension = (String)archivo.getValue();
		
		if(extension.length()>4){
			extension = extension.substring(extension.length()-4, extension.length());
		}else {
			ADialog.error(ventana, contenedor, "Not Excel" );
			return;
		}
		//System.out.println(extension);
		
		if (!extension.equals(".xls")) {
				ADialog.error(ventana, contenedor, "Not Excel" );
				return;		
		}
			
		if (archivoFisico.exists()) {
			ADialog.error(ventana, contenedor, "XX_FileExist" );
			return;			
		} 
		Excel archivoGenerado = new Excel(archivoFisico);
		createEXCELWithTotals(archivoGenerado, tabla, totals);
		
		//El archivo fue creado
		String msg = Msg.getMsg(Env.getCtx(), "XX_FileCreated", new String [] {
			(String)archivo.getValue()
		});
		ADialog.info(ventana, contenedor, msg);
		archivo.setValue(null);
		return;
	}


	private Excel createEXCELWithTotals(Excel excel,
			MiniTablePreparator tabla, String[][] totals) {
		final int COTA_EXCEL = Short.MAX_VALUE; 
		int rowsTotal = 0;
		try {
			//	for all rows (-1 = header row)			
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
				rowsTotal++;
			}	//	for all columns
			rowsTotal++;
			for (int i = 0; i < totals.length; i++) {
				excel.createRow(
						(short)rowsTotal, 
						(short)i, 
						totals[i][0], 
						null,
						Excel.CELLSTYLE_HEADER,
						Excel.DISPLAY_TYPE_STRING);		
				excel.createRow(
						(short)(rowsTotal % COTA_EXCEL + 1),
						(short)i, 
						totals[i][1], 
						totals[i][1],
						Excel.CELLSTYLE_NONE,
						Excel.DISPLAY_TYPE_NUMBER);	
			}
			
			
		}	//	for all rows
		catch (Exception e){
			log.log(Level.SEVERE, "createCSV(w)", e);
		}
		excel.close();
		return excel;
		
	}


}
