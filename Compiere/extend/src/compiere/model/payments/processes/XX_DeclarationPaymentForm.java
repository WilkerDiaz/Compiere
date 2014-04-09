package compiere.model.payments.processes;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.compiere.model.X_AD_Client;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess; 
import org.compiere.util.DB;
import org.compiere.util.Env;

import compiere.model.birt.BIRTReport;

public class XX_DeclarationPaymentForm extends SvrProcess {

	String reportPeriod = "";
	String reportPeriodXML = "";
	String fileRoute = "C:\\ISLR.xml";
	String clientRIF = "";
	String date1 = "";
	String date2 = "";
	String format = "";
	
	@Override
	protected void prepare() {

		X_AD_Client client = new X_AD_Client( Env.getCtx(), Env.getCtx().getAD_Client_ID(), null);
		String clientName = client.getName();
		clientName = clientName.replace(',', ' ');
		clientName = clientName.replace('.', ' ');
		clientName = clientName.replace(String.valueOf(' '), "");
		clientName = clientName.trim();
		
		clientRIF = client.get_ValueAsString("XX_CI_RIF");
		clientRIF = clientRIF.replace("-", "");
		
		ProcessInfoParameter[] parameter = getParameter();
		String month = "";
		String year = "";
		String route = "";
		
		for (ProcessInfoParameter element : parameter) {
			String name = element.getParameterName();			
			if (element.getParameter() == null) ;
			else if (name.equals("FileRoute")) { 
				 route = element.getParameter().toString();
			}
			else if (name.equals("Month")) {
				month = element.getParameter().toString();
			}
			else if (name.equals("Year")) {
				year = element.getParameter().toString();
			}
			else if (name.equals("Format")) {
				format = element.getParameter().toString();
			}else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		if(month.toString().length()==1)
			month = "0" + month.toString();
		else
			month = month.toString();
		
		reportPeriod = month + "/" + year;
		reportPeriodXML = year + month;
		
		fileRoute = route + "/retISLR" + year + month+ "SalariosOtros" + clientName + ".xml";
		
		//Fechas
		Calendar myDate = Calendar.getInstance();
		myDate.set(new Integer(year),new Integer(month)-1,1);
		
		String myMonth1 = "";
		int lastDayMonth  = myDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		if(month.toString().length()==1)
			myMonth1 = "0" + month.toString();
		else
			myMonth1 = month.toString();
		
		date1 = "01/" + myMonth1 + "/" + year;
		date2 = lastDayMonth+ "/" + myMonth1 + "/" + year;
	}

	@Override
	protected String doIt() throws Exception {
	
		saveConfig(fileRoute);
		
		generateReport(date1, date2, reportPeriod);
		
		return "";
	}

	public void saveConfig(String configFile) throws Exception {
		
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		XMLEventWriter eventWriter = outputFactory
				.createXMLEventWriter(new FileOutputStream(configFile), "UTF-8");
	
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent header = eventFactory.createDTD("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");

		// Se crea el documento
		eventWriter.add(header);
		
		//Atributos de la etiqueta inicial (RelacionRetencionesISLR)
		ArrayList<Object> al = new ArrayList<Object>();
		al.add(eventFactory.createAttribute("Periodo", reportPeriodXML));
		al.add(eventFactory.createAttribute("RifAgente", clientRIF));
		
		Iterator<Object> itr = al.iterator();
		
		//Etiqueta inicial
		StartElement configStartElement = eventFactory.createStartElement
		("", "", "RelacionRetencionesISLR", itr, itr);
		
		eventWriter.add(end);
		eventWriter.add(configStartElement);
		//eventWriter.add(end);
		
		//SQL que captura todas
		String SQL = "SELECT E.XX_TYPEPERSON||E.XX_CI_RIF AS XX_CI_RIF, A.DOCUMENTNO, NVL(A.XX_CONTROLNUMBER, ' '), C.XX_SENIATCODE " +			 
				 	 ",TO_CHAR(sum(B.LINENETAMT), '99999999D99'), TO_CHAR(C.XX_PERCENTOFRETENTION, '9999D99') " +
					 "FROM COMPIERE.C_INVOICE A, COMPIERE.C_INVOICELINE B, COMPIERE.XX_VCN_ISLRRETENTION C, " +
					 "C_ORDER D, C_BPARTNER E, XX_VCN_ISLRAMOUNT F " +
					 "WHERE A.C_INVOICE_ID = B.C_INVOICE_ID AND F.XX_RETAINEDAMOUNT <> 0 " +
					 "AND C.XX_VCN_ISLRRETENTION_ID = B.XX_VCN_ISLRRETENTION_ID AND A.C_ORDER_ID = D.C_ORDER_ID (+) " +
					 "AND A.C_BPARTNER_ID=E.C_BPARTNER_ID " +
					 "AND NVL(TO_CHAR(D.XX_INVOICEDATE, 'YYYYMM'), TO_CHAR(A.XX_APPROVALDATE, 'YYYYMM')) = '"+ reportPeriodXML +"' " +
					 "AND B.C_INVOICE_ID = F.C_INVOICE_ID AND B.XX_VCN_ISLRRETENTION_ID = F.XX_VCN_ISLRRETENTION_ID " +
					 "GROUP BY A.C_BPARTNER_ID, A.C_INVOICE_ID, E.XX_TYPEPERSON||E.XX_CI_RIF, A.DOCUMENTNO, " +
					 "A.XX_CONTROLNUMBER, C.XX_SENIATCODE, TO_CHAR(C.XX_PERCENTOFRETENTION, '9999D99') " + 
					 "ORDER BY A.C_BPARTNER_ID, A.C_INVOICE_ID";
		
		PreparedStatement pstmt_ISLR = null;
		ResultSet rs_ISLR = null;
		
		try {
			
			pstmt_ISLR = DB.prepareStatement(SQL, null);
			rs_ISLR = pstmt_ISLR.executeQuery();
			
			while (rs_ISLR.next()){
				
				StartElement detail = eventFactory.createStartElement
				("", "", "DetalleRetencion");
				eventWriter.add(detail);
				//eventWriter.add(end);
				
				createNode(eventWriter, "RifRetenido", rs_ISLR.getString(1));
				createNode(eventWriter, "NumeroFactura", rs_ISLR.getString(2).replaceAll("[-|a-z|A-Z]", ""));
				
				String controlNum = rs_ISLR.getString(3).replace("-", "");
				
				//Control Number Truncado a 8 (SENIAT) (de derecha a izquierda)
				if(controlNum.length()>8)
					controlNum = controlNum.substring(controlNum.length()-8, controlNum.length());
				
				createNode(eventWriter, "NumeroControl", controlNum);
				
				if(rs_ISLR.getString(4)!=null)
					createNode(eventWriter, "CodigoConcepto", rs_ISLR.getString(4));
				
				createNode(eventWriter, "MontoOperacion", rs_ISLR.getString(5).replace(',', '.').trim());
				createNode(eventWriter, "PorcentajeRetencion", rs_ISLR.getString(6).replace(',', '.').trim());
				
				eventWriter.add(eventFactory.createEndElement("", "", "DetalleRetencion"));
				//eventWriter.add(end);
			}
	
		}catch (SQLException e){
			log.log(Level.SEVERE, SQL, e);
		}finally{
			
			try {
				rs_ISLR.close();
				pstmt_ISLR.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		eventWriter.add(eventFactory.createEndElement("", "", "ISLR"));
		//eventWriter.add(end);
		eventWriter.add(eventFactory.createEndDocument());
		eventWriter.close();
	}
	
	private void createNode(XMLEventWriter eventWriter, String name,
			String value) throws XMLStreamException {

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		//XMLEvent end = eventFactory.createDTD("\n");
		//XMLEvent tab = eventFactory.createDTD("\t");
		// Create Start node
		StartElement sElement = eventFactory.createStartElement("", "", name);
		//eventWriter.add(tab);
		eventWriter.add(sElement);
		// Create Content
		Characters characters = eventFactory.createCharacters(value);
		eventWriter.add(characters);
		// Create End node
		EndElement eElement = eventFactory.createEndElement("", "", name);
		eventWriter.add(eElement);
		//eventWriter.add(end);
	}
	
	/**
	 * Se encarga de generar el reporte de declaracion mensual
	 * @param fechas, client
	 */
	public void generateReport(String date1, String date2, String period){
		
		String designName = "ISLRMonthlyStatement";

		//Intanciar reporte
		BIRTReport myReport = new BIRTReport();
		
		//Agregar parametro
		myReport.parameterName.add("client");
		myReport.parameterValue.add(Env.getCtx().getAD_Client_ID());
		
		myReport.parameterName.add("date1");
		myReport.parameterValue.add(date1);
		
		myReport.parameterName.add("date2");
		myReport.parameterValue.add(date2);
		
		myReport.parameterName.add("monthyear");
		myReport.parameterValue.add(period);
		
		//Correr Reporte
		if(format.equalsIgnoreCase("pdf"))
			myReport.runReport(designName,"pdf");
		else if(format.equalsIgnoreCase("xls"))
			myReport.runReport(designName,"xls");
	}
}
