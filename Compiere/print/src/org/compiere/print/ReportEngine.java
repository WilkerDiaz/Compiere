/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.print;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.print.*;
import java.awt.print.Printable;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.*;
import javax.xml.transform.stream.*;

import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import org.compiere.common.CompiereStateException;
import org.compiere.common.constants.*;
import org.compiere.excel.Excel;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.print.layout.*;
import org.compiere.process.*;
import org.compiere.util.*;

import com.compiere.itext.pdf.CompierePdfGraphics2D;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;


/*import com.qoppa.pdf.*;
import com.qoppa.pdfProcess.*;
*/
/**
 *	Report Engine.
 *  For a given PrintFormat,
 *  create a Report
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ReportEngine.java,v 1.4 2006/10/08 06:52:51 comdivision Exp $
 */
public class ReportEngine implements PrintServiceAttributeListener
{
	/**
	 *	Constructor
	 * 	@param ctx context
	 *  @param pf Print Format
	 *  @param query Optional Query
	 *  @param info print info
	 */
	public ReportEngine (Ctx ctx, MPrintFormat pf, Query query, PrintInfo info)
	{
		if (pf == null)
			throw new IllegalArgumentException("ReportEngine - no PrintFormat");
		log.info(pf + " -- " + query);
		m_ctx = ctx;
		m_printerName = m_ctx.getPrinterName();
		//
		m_printFormat = pf;
		m_info = info;
		setQuery(query);		//	loads Data
	}	//	ReportEngine

	/**	Static Logger	*/
	private static CLogger	log	= CLogger.getCLogger (ReportEngine.class);

	/**	Context					*/
	private Ctx				m_ctx;

	/**	Print Format			*/
	private MPrintFormat	m_printFormat;
	/** Print Info				*/
	private PrintInfo		m_info;
	/**	Query					*/
	private Query			m_query;
	/**	Query Data				*/
	private PrintData		m_printData;
	/** Layout					*/
	private LayoutEngine 	m_layout = null;
	/**	Printer					*/
	private String			m_printerName = null;
	/**	View					*/
	private View			m_view = null;

	/**
	 * 	Set PrintFormat.
	 *  If Layout was created, re-create layout
	 * 	@param pf print format
	 */
	protected void setPrintFormat (MPrintFormat pf)
	{
		m_printFormat = pf;
		if (m_layout != null)
		{
			setPrintData();
			m_layout.setPrintFormat(pf, false);
			m_layout.setPrintData(m_printData, m_query, true);	//	format changes data
		}
		if (m_view != null)
			m_view.revalidate();
	}	//	setPrintFormat

	/**
	 * 	Set Query and generate PrintData.
	 *  If Layout was created, re-create layout
	 * 	@param query query
	 */
	protected void setQuery (Query query)
	{
		m_query = query;
		if (query == null)
			return;
		//
		setPrintData();
		if (m_layout != null)
			m_layout.setPrintData(m_printData, m_query, true);
		if (m_view != null)
			m_view.revalidate();
	}	//	setQuery

	/**
	 * 	Get Query
	 * 	@return query
	 */
	public Query getQuery()
	{
		return m_query;
	}	//	getQuery

	/**
	 * 	Set PrintData for Format restricted by Query.
	 * 	Nothing set if there is no query
	 *  Sets m_printData
	 */
	private void setPrintData()
	{
		if (m_query == null)
			return;
		DataEngine de = new DataEngine(m_printFormat.getLanguage());
		setPrintData(de.getPrintData (m_ctx, m_printFormat, m_query));
	//	m_printData.dump();
	}	//	setPrintData

	/**
	 * 	Get PrintData
	 * 	@return print data
	 */
	public PrintData getPrintData()
	{
		return m_printData;
	}	//	getPrintData

	/**
	 * 	Set PrintData
	 * 	@param printData printData
	 */
	public void setPrintData (PrintData printData)
	{
		if (printData == null)
			return;
		m_printData = printData;
	}	//	setPrintData

	
	/**************************************************************************
	 * 	Layout
	 */
	private void layout()
	{
		if (m_printFormat == null)
			throw new CompiereStateException ("No print format");
		if (m_printData == null)
			throw new CompiereStateException ("No print data (Delete Print Format and restart)");
		m_layout = new LayoutEngine (m_printFormat, m_printData, m_query);
		//	Printer
		String printerName = m_printFormat.getPrinterName();
		if (printerName == null && m_info != null)
			printerName = m_info.getPrinterName();
		setPrinterName(printerName);
	}	//	layout

	/**
	 * 	Get Layout
	 *  @return Layout
	 */
	protected LayoutEngine getLayout()
	{
		if (m_layout == null)
			layout();
		return m_layout;
	}	//	getLayout

	/**
	 * 	Get PrintFormat (Report) Name
	 * 	@return name
	 */
	public String getName()
	{
		return m_printFormat.getName();
	}	//	getName

	/**
	 * 	Get PrintFormat
	 * 	@return print format
	 */
	public MPrintFormat getPrintFormat()
	{
		return m_printFormat;
	}	//	getPrintFormat

	/**
	 * 	Get Print Info
	 *	@return info
	 */
	public PrintInfo getPrintInfo()
	{
		return m_info;
	}	//	getPrintInfo
	
	/**
	 * 	Get PrintLayout (Report) Context
	 * 	@return context
	 */
	public Ctx getCtx()
	{
		return m_layout.getCtx();
	}	//	getCtx

	/**
	 * 	Get Row Count
	 * 	@return row count
	 */
	public int getRowCount()
	{
		return m_printData.getRowCount();
	}	//	getRowCount

	/**
	 * 	Get Column Count
	 * 	@return column count
	 */
	public int getColumnCount()
	{
		if (m_layout != null)
			return m_layout.getColumnCount();
		return 0;
	}	//	getColumnCount

	
	/**************************************************************************
	 * 	Get View Panel
	 * 	@return view panel
	 */
	public View getView()
	{
		if (m_layout == null)
			layout();
		if (m_view == null)
			m_view = new View (m_layout);
		return m_view;
	}	//	getView

	
	/**************************************************************************
	 * 	Print Report
	 */
	public void print ()
	{
		log.info(m_info.toString());
		if (m_layout == null)
			layout();
		
		//	Paper Attributes: 	media-printable-area, orientation-requested, media
		PrintRequestAttributeSet prats = m_layout.getPaper().getPrintRequestAttributeSet();
		//	add:				copies, job-name, priority
		if (m_info.isDocumentCopy() || m_info.getCopies() < 1)
			prats.add (new Copies(1));
		else
			prats.add (new Copies(m_info.getCopies()));
		Locale locale = Language.getLoginLanguage().getLocale();
		prats.add(new JobName(m_printFormat.getName(), locale));
		prats.add(PrintUtil.getJobPriority(m_layout.getNumberOfPages(), m_info.getCopies(), true));

		try
		{
			//	PrinterJob
			String printerName = m_printFormat.getPrinterName();
			if (printerName == null)
				printerName = m_info.getPrinterName();
			PrinterJob job = getPrinterJob(printerName);
		//	job.getPrintService().addPrintServiceAttributeListener(this);
			job.setPageable(m_layout.getPageable(false));	//	no copy
		//	Dialog
			try
			{
				if (m_info.isWithDialog() && !job.printDialog(prats))
					return;
			}
			catch (Exception e)
			{
				log.log(Level.WARNING, "Operating System Print Issue, check & try again", e);
				return;
			}

		//	submit
			boolean printCopy = m_info.isDocumentCopy() && m_info.getCopies() > 1;
			ArchiveEngine.get().archive(m_layout, m_info);
			PrintUtil.print(job, prats, false, printCopy);

			//	Document: Print Copies
			if (printCopy)
			{
				log.info("Copy " + (m_info.getCopies()-1));
				prats.add(new Copies(m_info.getCopies()-1));
				job = getPrinterJob(printerName);
			//	job.getPrintService().addPrintServiceAttributeListener(this);
				job.setPageable (m_layout.getPageable(true));		//	Copy
				PrintUtil.print(job, prats, false, false);
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
	}	//	print

	/**
	 * 	Print Service Attribute Listener.
	 * 	@param psae event
	 */
	public void attributeUpdate(PrintServiceAttributeEvent psae)
	{
		/**
PrintEvent on Win32 Printer : \\MAIN\HP LaserJet 5L
PrintServiceAttributeSet - length=2
queued-job-count = 0  (class javax.print.attribute.standard.QueuedJobCount)
printer-is-accepting-jobs = accepting-jobs  (class javax.print.attribute.standard.PrinterIsAcceptingJobs)
PrintEvent on Win32 Printer : \\MAIN\HP LaserJet 5L
PrintServiceAttributeSet - length=1
queued-job-count = 1  (class javax.print.attribute.standard.QueuedJobCount)
PrintEvent on Win32 Printer : \\MAIN\HP LaserJet 5L
PrintServiceAttributeSet - length=1
queued-job-count = 0  (class javax.print.attribute.standard.QueuedJobCount)
		**/
		log.fine("attributeUpdate - " + psae);
	//	PrintUtil.dump (psae.getAttributes());
	}	//	attributeUpdate


	/**
	 * 	Get PrinterJob based on PrinterName
	 * 	@param printerName optional Printer Name
	 * 	@return PrinterJob
	 */
	private PrinterJob getPrinterJob (String printerName)
	{
		if (printerName != null && printerName.length() > 0)
			return CPrinter.getPrinterJob(printerName);
		return CPrinter.getPrinterJob(m_printerName);
	}	//	getPrinterJob

	/**
	 * 	Show Dialog and Set Paper
	 *  Optionally re-calculate layout
	 */
	public void pageSetupDialog ()
	{
		if (m_layout == null)
			layout();
		m_layout.pageSetupDialog(getPrinterJob(m_printerName));
		if (m_view != null)
			m_view.revalidate();
	}	//	pageSetupDialog

	/**
	 * 	Set Printer (name)
	 * 	@param printerName valid printer name
	 */
	public void setPrinterName(String printerName)
	{
		if (printerName == null)
			m_printerName = m_ctx.getPrinterName();
		else
			m_printerName = printerName;
	}	//	setPrinterName

	/**
	 * 	Get Printer (name)
	 * 	@return printer name
	 */
	public String getPrinterName()
	{
		return m_printerName;
	}	//	getPrinterName

	
	/**************************************************************************
	 * 	Create HTML File
	 * 	@param file file
	 *  @param onlyTable if false create complete HTML document
	 *  @param language optional language - if null the default language is used to format nubers/dates
	 * 	@return true if success
	 */
	public boolean createHTML (File file, boolean onlyTable, Language language)
	{
		try
		{
			Language lang = language;
			if (lang == null)
				lang = Language.getLoginLanguage();
			FileWriter fw = new FileWriter (file, false);
			return createHTML (new BufferedWriter(fw), onlyTable, lang);
		}
		catch (FileNotFoundException fnfe)
		{
			log.log(Level.SEVERE, "(f) - " + fnfe.toString());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(f)", e);
		}
		return false;
	}	//	createHTML

	/**
	 * 	Write HTML to writer
	 * 	@param writer writer
	 *  @param onlyTable if false create complete HTML document
	 *  @param language optional language - if null nubers/dates are not formatted
	 * 	@return true if success
	 */
	public boolean createHTML (Writer writer, boolean onlyTable, Language language)
	{
		try
		{
			table table = new table();
			//
			//	for all rows (-1 = header row)
			for (int row = -1; row < m_printData.getRowCount(); row++)
			{
				tr tr = new tr();
				table.addElement(tr);
				if (row != -1)
					m_printData.setRowIndex(row);
				//	for all columns
				for (int col = 0; col < m_printFormat.getItemCount(); col++)
				{
					MPrintFormatItem item = m_printFormat.getItem(col);
					if (item.isPrinted())
					{
						//	header row
						if (row == -1)
						{
							th th = new th();
							tr.addElement(th);
							th.addElement(Util.maskHTML(item.getPrintName(language)));
						}
						else
						{
							td td = new td();
							tr.addElement(td);
							Object obj = m_printData.getNode(new Integer(item.getAD_Column_ID()));
							if (obj == null)
								td.addElement("&nbsp;");
							else if (obj instanceof PrintDataElement)
							{
								PrintDataColumn pdc = null;
								for (int ci = 0; ci < m_printData.getColumnInfo().length; ci++)
								{
									pdc = m_printData.getColumnInfo()[ci];
									if ( ((PrintDataElement)obj).getColumnName().equals(pdc.getColumnName()) )
									{
										int minFractionDigits = pdc.getMinFractionDigits();
										String value = ((PrintDataElement)obj).getValueDisplay(language, minFractionDigits);	//	formatted
										td.addElement(Util.maskHTML(value));
									}
								}
							}
							else if (obj instanceof PrintData)
							{
								//	ignore contained Data
							}
							else
								log.log(Level.SEVERE, "Element not PrintData(Element) " + obj.getClass());
						}
					}	//	printed
				}	//	for all columns
			}	//	for all rows

			//
			PrintWriter w = new PrintWriter(writer);
			if (onlyTable)
				table.output(w);
			else
			{
				XhtmlDocument doc = new XhtmlDocument();
				doc.appendBody(table);
				doc.output(w);
			}
			w.flush();
			w.close();
			return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(w)", e);
		}
		return false;
	}	//	createHTML


	/**************************************************************************
	 * 	Create CSV File
	 * 	@param file file
	 *  @param delimiter delimiter, e.g. comma, tab
	 *  @param language translation language
	 * 	@return true if success
	 */
	public boolean createCSV (File file, char delimiter, Language language)
	{
		try
		{
			FileWriter fw = new FileWriter (file, false);
			return createCSV (new BufferedWriter(fw), delimiter, language);
		}
		catch (FileNotFoundException fnfe)
		{
			log.log(Level.SEVERE, "(f) - " + fnfe.toString());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(f)", e);
		}
		return false;
	}	//	createCSV

	/**
	 * 	Write CSV to writer
	 * 	@param writer writer
	 *  @param delimiter delimiter, e.g. comma, tab
	 *  @param language translation language
	 * 	@return true if success
	 */
	public boolean createCSV (Writer writer, char delimiter, Language language)
	{
		if (delimiter == 0)
			delimiter = '\t';
		try
		{
			//	for all rows (-1 = header row)
			for (int row = -1; row < m_printData.getRowCount(); row++)
			{
				StringBuffer sb = new StringBuffer();
				if (row != -1)
					m_printData.setRowIndex(row);

				//	for all columns
				boolean first = true;	//	first column to print
				for (int col = 0; col < m_printFormat.getItemCount(); col++)
				{
					MPrintFormatItem item = m_printFormat.getItem(col);
					if (item.isPrinted())
					{
						//	column delimiter (comma or tab)
						if (first)
							first = false;
						else
							sb.append(delimiter);
						//	header row
						if (row == -1)
							createCSVvalue (sb, delimiter,
								m_printFormat.getItem(col).getPrintName(language));
						else
						{
							Object obj = m_printData.getNode(new Integer(item.getAD_Column_ID()));
							String data = "";
							if (obj == null)
								;
							else if (obj instanceof PrintDataElement)
							{
								PrintDataElement pde = (PrintDataElement)obj;
								if (pde.isPKey())
									data = pde.getValueAsString();
								else
								{
									PrintDataColumn pdc = null;
									for (int ci = 0; ci < m_printData.getColumnInfo().length; ci++)
									{
										pdc = m_printData.getColumnInfo()[ci];
										if ( pde.getColumnName().equals(pdc.getColumnName()) )
										{
											int minFractionDigits = pdc.getMinFractionDigits();
											data = pde.getValueDisplay(language, minFractionDigits);	//	formatted
										}
									}
								}
							}
							else if (obj instanceof PrintData)
							{
							}
							else
								log.log(Level.SEVERE, "Element not PrintData(Element) " + obj.getClass());
							createCSVvalue (sb, delimiter, data);
						}
					}	//	printed
				}	//	for all columns
				writer.write(sb.toString());
				writer.write(Env.NL);
			}	//	for all rows
			//
			writer.flush();
			writer.close();
			return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(w)", e);
		}
		return false;
	}	//	createCSV

	/**
	 * 	Add Content to CSV string.
	 *  Encapsulate/mask content in " if required
	 * 	@param sb StringBuffer to add to
	 * 	@param delimiter delimiter
	 * 	@param content column value
	 */
	private void createCSVvalue (StringBuffer sb, char delimiter, String content)
	{
		//	nothing to add
		if (content == null || content.length() == 0)
			return;
		//
		boolean needMask = false;
		StringBuffer buff = new StringBuffer();
		char chars[] = content.toCharArray();
		for (char c : chars) {
			if (c == '"')
			{
				needMask = true;
				buff.append(c);		//	repeat twice
			}	//	mask if any control character
			else if (!needMask && (c == delimiter || !Character.isLetterOrDigit(c)))
				needMask = true;
			buff.append(c);
		}

		//	Optionally mask value
		if (needMask)
			sb.append('"').append(buff).append('"');
		else
			sb.append(buff);
	}	//	addCSVColumnValue

	
	/**************************************************************************
	 * 	Create XML File
	 * 	@param file file
	 * 	@return true if success
	 */
	public boolean createXML (File file)
	{
		try
		{
			FileWriter fw = new FileWriter (file, false);
			return createXML (new BufferedWriter(fw));
		}
		catch (FileNotFoundException fnfe)
		{
			log.log(Level.SEVERE, "(f) - " + fnfe.toString());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(f)", e);
		}
		return false;
	}	//	createXML

	/**
	 * 	Write XML to writer
	 * 	@param writer writer
	 * 	@return true if success
	 */
	public boolean createXML (Writer writer)
	{
		try
		{
			m_printData.createXML(new StreamResult(writer));
			writer.flush();
			writer.close();
			return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(w)", e);
		}
		return false;
	}	//	createXML

	
	/**************************************************************************
	 * 	Create PDF file.
	 * 	(created in temporary storage)
	 *	@return PDF file
	 */
	public File getPDF()
	{
		return getPDF (null);
	}	//	getPDF

	public File getPDF (File file){
		return getPDF(file,null);
	}

	/**
	 * 	Create PDF file.
	 * 	@param file file
	 *  @param ProcessInfo process information
	 *	@return PDF file
	 */
	public File getPDF (File file,ProcessInfo pi)
	{
		try
		{
			if (file == null)
				file = File.createTempFile ("ReportEngine", ".pdf");
		}
		catch (IOException e)
		{
			log.log(Level.SEVERE, "", e);
		}
		if (createPDF (file,pi))
			return file;
		return null;
	}	//	getPDF

	
	public class MyPrintable implements Printable {

		public int print( Graphics graphics, PageFormat pageFormat, int pageIndex ) throws PrinterException
		{
			System.err.println( "MyPrintable.print pageIndex=" + pageIndex );
			if( pageIndex == 0 )
			{
				graphics.setColor( new Color(50, 100, 150) );
				graphics.fillRect( 100, 100, 200, 200 );
				graphics.fillRect( 0, 0, 100, 100 );
				return PAGE_EXISTS;
			}
			else
			{
				return NO_SUCH_PAGE;
			}
		}
	
	};
	
	public class MyPageable implements Pageable
	{

		public int getNumberOfPages()
		{
			System.err.println( "MyPageable.getNumberOfPages()" );
			return 1;
		}

		public PageFormat getPageFormat( int pageIndex ) throws IndexOutOfBoundsException
		{
			System.err.println( "MyPageable.getPageFormat("+pageIndex+")" );
			return new PageFormat();
		}

		public Printable getPrintable( int pageIndex ) throws IndexOutOfBoundsException
		{
			System.err.println( "MyPageable.getPrintable("+pageIndex+")" );
			return new MyPrintable();
		}
	};
	
	public boolean createPDF (File file){
		return createPDF(file,null);
	}
	
	/**
	 * 	Create PDF File
	 * 	@param file file
	 *  @param ProcessInfo process information
	 * 	@return true if success
	 */
	public boolean createPDF (File file,ProcessInfo pi)
	{
		String fileName = null;
		URI uri = null;

		try
		{
			if (file == null)
				file = File.createTempFile ("ReportEngine", ".pdf");
			fileName = file.getAbsolutePath();
			uri = file.toURI();
			if (file.exists())
				file.delete();

		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "file", e);
			return false;
		}
			
		log.fine(uri.toString());

		try
		{
			if (m_layout == null)
				layout ();
			ArchiveEngine.get().archive(m_layout, m_info);
			
			// the following line does not work when called from within JBoss.  extracted its contents below instead.
			// Document.getPDFAsFile(fileName, m_layout.getPageable(false));

			LayoutEngine layout = m_layout.getPageable( false );
			
			int numberOfFiles = 1;
			// for now, restrict the number of files to 1 as there are cases where more than 1 
			// PDF files created cannot be displayed to the client.
			/*
			if (pi != null){
				int numberOfPages = layout.getNumberOfPages();	
				
				int maxPages = Integer.parseInt(Ini.getProperty(Ini.P_MAX_PAGES_PER_REPORT));
				numberOfFiles = numberOfPages / maxPages;			
				if ((numberOfPages % maxPages) != 0)
					numberOfFiles++;
			}*/			
			CompierePdfGraphics2D.writePDF(file,numberOfFiles, layout,pi);
			//FileOutputStream out = new FileOutputStream( file );
			//CompierePdfGraphics2D.writePDF(out, layout,pi);
			
		}		
		catch (Exception e)
		{
			log.log(Level.SEVERE, "PDF", e);
			return false;
		}

		File file2 = new File(fileName);
		log.info(file2.getAbsolutePath() + " - " + file2.length());
		return file2.exists();
	}	//	createPDF

	/**
	 * 	Create PDF as Data array
	 *	@return pdf data
	 */
	public byte[] createPDFData ()
	{
		try
		{
			if (m_layout == null)
				layout ();
			return getPDFAsArray(m_layout.getPageable(false));
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "PDF", e);
		}
		return null;
	}	//	createPDFData
	
	/**************************************************************************
	 * 	Create PostScript File
	 * 	@param file file
	 * 	@return true if success
	 */
	public boolean createPS (File file)
	{
		try
		{
			return createPS (new FileOutputStream(file));
		}
		catch (FileNotFoundException fnfe)
		{
			log.log(Level.SEVERE, "(f) - " + fnfe.toString());
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(f)", e);
		}
		return false;
	}	//	createPS

	/**
	 * 	Write PostScript to writer
	 * 	@param fos file output stream
	 * 	@return true if success
	 */
	public boolean createPS (FileOutputStream fos)
	{
		try
		{
			String outputMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
			DocFlavor docFlavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
			StreamPrintServiceFactory[] spsfactories =
				StreamPrintServiceFactory.lookupStreamPrintServiceFactories(docFlavor, outputMimeType);
			if (spsfactories.length == 0)
			{
				log.log(Level.SEVERE, "(fos) - No StreamPrintService");
				return false;
			}
			//	just use first one - sun.print.PSStreamPrinterFactory
			//	System.out.println("- " + spsfactories[0]);
			StreamPrintService sps = spsfactories[0].getPrintService(fos);
			//	get format
			if (m_layout == null)
				layout();
			//	print it
			sps.createPrintJob().print(m_layout.getPageable(false), 
				new HashPrintRequestAttributeSet());
			//
			fos.flush();
			fos.close();
			return true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "(fos)", e);
		}
		return false;
	}	//	createPS


	/**************************************************************************
	 * 	Get Report Engine for process info 
	 *	@param ctx context
	 *	@param pi process info with AD_PInstance_ID
	 *	@return report engine or null
	 */
	static public ReportEngine get (Ctx ctx, ProcessInfo pi)
	{
		int AD_Client_ID = pi.getAD_Client_ID();
		//
		int AD_Table_ID = 0;
		int AD_ReportView_ID = 0;
		String TableName = null;
		String whereClause = "";
		int AD_PrintFormat_ID = 0;
		boolean IsForm = false;
		int Client_ID = -1;
		
		// Check Business View in Report
		BusinessView businessView = getBusinessView(pi);
		
		// if Business View not found, check for Report View
		if (businessView.getAD_BView_ID() == 0){

				//	Get AD_Table_ID and TableName
				String sql = "SELECT rv.AD_ReportView_ID,rv.WhereClause,"
					+ " t.AD_Table_ID,t.TableName, pf.AD_PrintFormat_ID, pf.IsForm, pf.AD_Client_ID "
					+ "FROM AD_PInstance pi"
					+ " INNER JOIN AD_Process p ON (pi.AD_Process_ID=p.AD_Process_ID)"
					+ " INNER JOIN AD_ReportView rv ON (p.AD_ReportView_ID=rv.AD_ReportView_ID)"
					+ " INNER JOIN AD_Table t ON (rv.AD_Table_ID=t.AD_Table_ID)"
					+ " LEFT OUTER JOIN AD_PrintFormat pf ON (p.AD_ReportView_ID=pf.AD_ReportView_ID AND pf.AD_Client_ID IN (0,?)) "
					+ "WHERE pi.AD_PInstance_ID=? "		//	#2
					+ "ORDER BY pf.AD_Client_ID DESC, pf.IsDefault DESC";	//	own first
				try
				{
					PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
					pstmt.setInt(1, AD_Client_ID);
					pstmt.setInt(2, pi.getAD_PInstance_ID());
					ResultSet rs = pstmt.executeQuery();
					//	Just get first
					if (rs.next())
					{
						AD_ReportView_ID = rs.getInt(1);		//	required
						whereClause = rs.getString(2);
						if (rs.wasNull())
							whereClause = "";
						//
						AD_Table_ID = rs.getInt(3);
						TableName = rs.getString(4);			//	required for query
						AD_PrintFormat_ID = rs.getInt(5);		//	required
						IsForm = "Y".equals(rs.getString(6));	//	required
						Client_ID = rs.getInt(7);
					}
					rs.close();
					pstmt.close();
				}
				catch (SQLException e1)
				{
					log.log(Level.SEVERE, "(1) - " + sql, e1);
				}
				//	Nothing found
				if (AD_ReportView_ID == 0)
				{
					//	Check Print format in Report Directly
					sql = "SELECT t.AD_Table_ID,t.TableName, pf.AD_PrintFormat_ID, pf.IsForm "
						+ "FROM AD_PInstance pi"
						+ " INNER JOIN AD_Process p ON (pi.AD_Process_ID=p.AD_Process_ID)"
						+ " INNER JOIN AD_PrintFormat pf ON (p.AD_PrintFormat_ID=pf.AD_PrintFormat_ID)"
						+ " INNER JOIN AD_Table t ON (pf.AD_Table_ID=t.AD_Table_ID) "
						+ "WHERE pi.AD_PInstance_ID=?";
					try
					{
						PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
						pstmt.setInt(1, pi.getAD_PInstance_ID());
						ResultSet rs = pstmt.executeQuery();
						if (rs.next())
						{
							whereClause = "";
							AD_Table_ID = rs.getInt(1);
							TableName = rs.getString(2);			//	required for query
							AD_PrintFormat_ID = rs.getInt(3);		//	required
							IsForm = "Y".equals(rs.getString(4));	//	required
							Client_ID = AD_Client_ID;
						}
						rs.close();
						pstmt.close();
					}
					catch (SQLException e1)
					{
						log.log(Level.SEVERE, "(2) - " + sql, e1);
					}
					if (AD_PrintFormat_ID == 0)
					{
						log.log(Level.SEVERE, "Report Info NOT found AD_PInstance_ID=" + pi.getAD_PInstance_ID() 
							+ ",AD_Client_ID=" + AD_Client_ID);
						return null;
					}
				}
		}
		else{
			AD_Table_ID = businessView.getAD_Table_ID();
			TableName = businessView.getTableName();
			AD_PrintFormat_ID = businessView.getAD_PrintFormat_ID();
			Client_ID = businessView.getAD_Client_ID();
			
			whereClause = businessView.getWhereClause();
		}

		//  Create Query from Parameters
		Query query = null;
		if (IsForm && pi.getRecord_ID() != 0)	//	Form = one record
		{
			String columnName = TableName +  "_ID";
			if (TableName.endsWith("_v")){
				columnName = getColumnName(TableName);
			}
					
			query = Query.getEqualQuery(columnName, pi.getRecord_ID());
		}
		else
			query = Query.get (ctx, pi.getAD_PInstance_ID(), TableName);
		
		//  Add to static where clause from ReportView
		if (whereClause != null && whereClause.length() != 0)
			query.addRestriction(whereClause);

		//	Get Print Format
		MPrintFormat format = null;
		Object so = pi.getSerializableObject();
		if (so instanceof MPrintFormat)
			format = (MPrintFormat)so;
		if (format == null && AD_PrintFormat_ID != 0)
		{
			if (pi.getAD_BView_ID() == 0){
				//	We have a PrintFormat with the correct Client
				if (Client_ID == AD_Client_ID)
					format = MPrintFormat.get (ctx, AD_PrintFormat_ID, false);
				else
					format = MPrintFormat.copyToClient (ctx, AD_PrintFormat_ID, AD_Client_ID);
			}
		}
		if (format != null && format.getItemCount() == 0)
		{
			log.info("No Items - recreating:  " + format);
			format.delete(true);
			format = null;
		}
		//	Create Format
		if (format == null && AD_ReportView_ID != 0 && pi.getAD_BView_ID() == 0)
			format = MPrintFormat.createFromReportView(ctx, AD_ReportView_ID, pi.getTitle());
		
		// if Business View has been defined, use Print Format on Business View
		if (format == null && businessView.getAD_BView_ID() != 0){
			 Constructor<?> intArgsConstructor;
			try
			{
				Class<?> myClass = Class.forName("org.compiere.eul.print.MBViewPrintFormat");
				Class<?>[] intArgsClass = new Class[] {org.compiere.util.Ctx.class, int.class, int.class };
				Object[] intArgs = new Object[] {ctx,businessView.getAD_BView_ID(),0 };					
				
			    intArgsConstructor = myClass.getConstructor(intArgsClass);
			    format = (MPrintFormat)  intArgsConstructor.newInstance(intArgs);
			    
			    pi.setBV_PrintFormat(true);
			}
			catch (Exception e)
			{
				
				log.log(Level.SEVERE, "", e);
			}
		}
		if (format == null)
			return null;
		//
		PrintInfo info = new PrintInfo (pi);
		info.setAD_Table_ID(AD_Table_ID);
		
		return new ReportEngine(ctx, format, query, info);
	}	//	get

	/*************************************************************************/

	private static String getColumnName(String tableName) {
		String columnName = tableName + "_ID";
		for (int i=0;i<DOC_TABLES.length;i++){
			if (DOC_TABLES[i].equals(tableName)){
				columnName = DOC_IDS[i];
				break;
			}
		}
		return columnName;
	}

	/** Order = 0				*/
	public static final int		ORDER = 0;
	/** Shipment = 1				*/
	public static final int		SHIPMENT = 1;
	/** Invoice = 2				*/
	public static final int		INVOICE = 2;
	/** Project = 3				*/
	public static final int		PROJECT = 3;
	/** RfQ = 4					*/
	public static final int		RFQ = 4;
	/** Remittance = 5			*/
	public static final int		REMITTANCE = 5;
	/** Check = 6				*/
	public static final int		CHECK = 6;
	/** Dunning = 7				*/
	public static final int		DUNNING = 7;
	/** Movement = 8            */
	public static final int     MOVEMENT = 8;
	/** Inventory = 9            */
	public static final int     INVENTORY = 9;
	/** WorkOrder = 10            */
	public static final int     WORKORDER = 10;
	/** TaskList = 11            */
	public static final int     TASKLIST = 11;
	/** WorkOrderTxn = 12        */
	public static final int     WORKORDERTXN = 12;
	/** StandardOperation = 13   */
	public static final int     STANDARDOPERATION = 13;
	/** Routing = 14             */
	public static final int     ROUTING = 14;

	private static final String[]	DOC_TABLES = new String[] {
		"C_Order_Header_v", "M_InOut_Header_v", "C_Invoice_Header_v", "C_Project_Header_v",
		"C_RfQResponse_v",
		"C_PaySelection_Check_v", "C_PaySelection_Check_v",  
		"C_DunningRunEntry_v", "M_Movement", "M_Inventory", "M_WorkOrder_Header_v", "M_TaskList",
		"M_WorkOrderTxn_Header_V", "M_StandardOperation_Header_v", "M_Routing_Header_v"};
	private static final String[]	DOC_BASETABLES = new String[] {
		"C_Order", "M_InOut", "C_Invoice", "C_Project",
		"C_RfQResponse",
		"C_PaySelectionCheck", "C_PaySelectionCheck", 
		"C_DunningRunEntry", "M_Movement", "M_Inventory", "M_WorkOrder", "M_TaskList",
		"M_WorkOrderTransaction", "M_StandardOperation", "M_Routing"};
	private static final String[]	DOC_IDS = new String[] {
		"C_Order_ID", "M_InOut_ID", "C_Invoice_ID", "C_Project_ID",
		"C_RfQResponse_ID",
		"C_PaySelectionCheck_ID", "C_PaySelectionCheck_ID", 
		"C_DunningRunEntry_ID", "M_Movement_ID",  "M_Inventory_ID", "M_WorkOrder_ID", "M_TaskList_ID",
		"M_WorkOrderTransaction_ID", "M_StandardOperation_ID", "M_Routing_ID"};
	private static final int[]	DOC_TABLE_ID = new int[] {
		X_C_Order.Table_ID, X_M_InOut.Table_ID, X_C_Invoice.Table_ID, X_C_Project.Table_ID,
		X_C_RfQResponse.Table_ID,
		X_C_PaySelectionCheck.Table_ID, X_C_PaySelectionCheck.Table_ID, 
		X_C_DunningRunEntry.Table_ID, X_M_Movement.Table_ID, X_M_Inventory.Table_ID, X_M_WorkOrder.Table_ID, X_M_TaskList.Table_ID,
		X_M_WorkOrderTransaction.Table_ID, X_M_StandardOperation.Table_ID, X_M_Routing.Table_ID};

	
	/**************************************************************************
	 * 	Get Document Print Engine for Document Type.
	 * 	@param ctx context
	 * 	@param type document type
	 * 	@param Record_ID id
	 * 	@return Report Engine or null
	 */
	public static ReportEngine get (Ctx ctx, int type, int Record_ID)
	{
		if (Record_ID < 1)
		{
			log.log(Level.WARNING, "No PrintFormat for Record_ID=" + Record_ID 
					+ ", Type=" + type);
			return null;
		}
		//	Order - Print Shipment or Invoice
		if (type == ORDER)
		{
			int[] what = getDocumentWhat (Record_ID);
			if (what != null)
			{
				type = what[0];
				Record_ID = what[1];
			}
		}	//	Order
		//
	//	String JobName = DOC_BASETABLES[type] + "_Print";
		int AD_PrintFormat_ID = 0;
		int C_BPartner_ID = 0;
		String DocumentNo = null;
		int copies = 1;

		//	Language
		//MClient client = MClient.get(ctx);
		Language language = Env.getLanguage(ctx);
		//	Get Document Info
		String sql = null;
		if (type == CHECK)
			sql = "SELECT bad.Check_PrintFormat_ID,"								//	1
				+ "	c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,d.DocumentNo "		//	2..5
				+ "FROM C_PaySelectionCheck d"
				+ " INNER JOIN C_PaySelection ps ON (d.C_PaySelection_ID=ps.C_PaySelection_ID)"
				+ " INNER JOIN C_BankAccountDoc bad ON (ps.C_BankAccount_ID=bad.C_BankAccount_ID AND d.PaymentRule=bad.PaymentRule)"
				+ " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
				+ " INNER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID) "
				+ "WHERE d.C_PaySelectionCheck_ID=?";		//	info from BankAccount
		else if (type == DUNNING)
			sql = "SELECT dl.Dunning_PrintFormat_ID,"
				+ " c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,dr.DunningDate "
				+ "FROM C_DunningRunEntry d"
				+ " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
				+ " INNER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID)"
				+ " INNER JOIN C_DunningRun dr ON (d.C_DunningRun_ID=dr.C_DunningRun_ID)"
				+ " INNER JOIN C_DunningLevel dl ON (dl.C_DunningLevel_ID=dr.C_DunningLevel_ID) "
				+ "WHERE d.C_DunningRunEntry_ID=?";			//	info from Dunning
		else if (type == REMITTANCE)
			sql = "SELECT pf.Remittance_PrintFormat_ID,"
				+ " c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,d.DocumentNo "
				+ "FROM C_PaySelectionCheck d"
				+ " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
				+ " INNER JOIN AD_PrintForm pf ON (c.AD_Client_ID=pf.AD_Client_ID)"
				+ " INNER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID) "
				+ "WHERE d.C_PaySelectionCheck_ID=?"		//	info from PrintForm
				+ " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) ORDER BY pf.AD_Org_ID DESC";
		else if (type == PROJECT)
			sql = "SELECT pf.Project_PrintFormat_ID,"
				+ " c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,d.Value "
				+ "FROM C_Project d"
				+ " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
				+ " INNER JOIN AD_PrintForm pf ON (c.AD_Client_ID=pf.AD_Client_ID)"
				+ " LEFT OUTER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID) "
				+ "WHERE d.C_Project_ID=?"					//	info from PrintForm
				+ " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) ORDER BY pf.AD_Org_ID DESC";
		else if (type == RFQ)
			sql = "SELECT COALESCE(t.AD_PrintFormat_ID, pf.AD_PrintFormat_ID),"
				+ " c.IsMultiLingualDocument,bp.AD_Language,bp.C_BPartner_ID,rr.Name "
				+ "FROM C_RfQResponse rr"
				+ " INNER JOIN C_RfQ r ON (rr.C_RfQ_ID=r.C_RfQ_ID)"
				+ " INNER JOIN C_RfQ_Topic t ON (r.C_RfQ_Topic_ID=t.C_RfQ_Topic_ID)"
				+ " INNER JOIN AD_Client c ON (rr.AD_Client_ID=c.AD_Client_ID)"
				+ " INNER JOIN C_BPartner bp ON (rr.C_BPartner_ID=bp.C_BPartner_ID),"
				+ " AD_PrintFormat pf "
				+ "WHERE pf.AD_Client_ID IN (0,rr.AD_Client_ID)"
				+ " AND pf.AD_Table_ID=725 AND pf.IsTableBased='N'"	//	from RfQ PrintFormat
				+ " AND rr.C_RfQResponse_ID=? "				//	Info from RfQTopic
				+ "ORDER BY t.AD_PrintFormat_ID, pf.AD_Client_ID DESC, pf.AD_Org_ID DESC";
        else if (type == MOVEMENT)
            sql = "SELECT pf.Movement_PrintFormat_ID,"
                + " c.IsMultiLingualDocument, COALESCE(dt.DocumentCopies,0), "
                + " dt.AD_PrintFormat_ID "
                + "FROM M_Movement d"
                + " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
                + " INNER JOIN AD_PrintForm pf ON (d.AD_Client_ID=pf.AD_Client_ID OR pf.AD_Client_ID=0)"
                + " LEFT OUTER JOIN C_DocType dt ON (d.C_DocType_ID=dt.C_DocType_ID) "
                + "WHERE d.M_Movement_ID=?"                 //  info from PrintForm
                + " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) AND pf.Movement_PrintFormat_ID IS NOT NULL "
                + "ORDER BY pf.AD_Client_ID DESC, pf.AD_Org_ID DESC";
        else if (type == INVENTORY)
            sql = "SELECT pf.Inventory_PrintFormat_ID,"
                + " c.IsMultiLingualDocument, COALESCE(dt.DocumentCopies,0), "
                + " dt.AD_PrintFormat_ID "
                + "FROM M_Inventory d"
                + " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
                + " INNER JOIN AD_PrintForm pf ON (d.AD_Client_ID=pf.AD_Client_ID OR pf.AD_Client_ID=0)"
                + " LEFT OUTER JOIN C_DocType dt ON (d.C_DocType_ID=dt.C_DocType_ID) "
                + "WHERE d.M_Inventory_ID=?"                 //  info from PrintForm
                + " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) AND pf.Inventory_PrintFormat_ID IS NOT NULL "
                + "ORDER BY pf.AD_Client_ID DESC,  pf.AD_Org_ID DESC";
        else if (type == WORKORDER)
            sql = "SELECT COALESCE(dt.AD_PrintFormat_ID,pf.WorkOrder_PrintFormat_ID), "
                + " c.IsMultiLingualDocument, COALESCE(dt.DocumentCopies,0), "
                + " dt.AD_PrintFormat_ID "
                + "FROM M_WorkOrder d"
                + " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
                + " INNER JOIN AD_PrintForm pf ON (d.AD_Client_ID=pf.AD_Client_ID OR pf.AD_Client_ID=0)"
                + " LEFT OUTER JOIN C_DocType dt ON (d.C_DocType_ID=dt.C_DocType_ID) "
                + "WHERE d.M_WorkOrder_ID=?"                 //  info from PrintForm
                + " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) "
                + "ORDER BY pf.AD_Client_ID DESC,  pf.AD_Org_ID DESC";
        else if (type == WORKORDERTXN)
            sql = "SELECT COALESCE(dt.AD_PrintFormat_ID,pf.WorkOrderTxn_PrintFormat_ID), "
                + " c.IsMultiLingualDocument, COALESCE(dt.DocumentCopies,0), "
                + " dt.AD_PrintFormat_ID "
                + "FROM M_WorkOrderTransaction d"
                + " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
                + " INNER JOIN AD_PrintForm pf ON (d.AD_Client_ID=pf.AD_Client_ID OR pf.AD_Client_ID=0)"
                + " LEFT OUTER JOIN C_DocType dt ON (d.C_DocType_ID=dt.C_DocType_ID) "
                + "WHERE d.M_WorkOrderTransaction_ID=?"                 //  info from PrintForm
                + " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) "
                + "ORDER BY pf.AD_Client_ID DESC,  pf.AD_Org_ID DESC";
        else if (type == STANDARDOPERATION)
        	sql = "SELECT pf.StdOperation_PrintFormat_ID, "
        		+ " c.IsMultiLingualDocument"
        		+ " FROM M_StandardOperation d"
        		+ " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
        		+ " INNER JOIN AD_PrintForm pf ON (d.AD_Client_ID=pf.AD_Client_ID OR pf.AD_Client_ID=0)"
        		+ " INNER JOIN M_Operation op ON (d.M_Operation_ID=op.M_Operation_ID) "
        		+ " WHERE d.M_StandardOperation_ID=?" // info from PrintForm
        		+ " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) "
        		+ "ORDER BY pf.AD_Client_ID DESC, pf.AD_Org_ID DESC";
        else if (type == ROUTING)
        	sql = "SELECT pf.Routing_PrintFormat_ID, "
        		+ " c.IsMultiLingualDocument"
        		+ " FROM M_Routing d"
        		+ " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
        		+ " INNER JOIN AD_PrintForm pf ON (d.AD_Client_ID=pf.AD_Client_ID OR pf.AD_Client_ID=0)"
        		+ " LEFT OUTER JOIN M_RoutingOperation ro ON (d.M_Routing_ID=ro.M_Routing_ID) "
        		+ " WHERE d.M_Routing_ID=?" // info from PrintForm
        		+ " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) "
        		+ "ORDER BY pf.AD_Client_ID DESC, pf.AD_Org_ID DESC";
        else if (type == TASKLIST)             
        	sql = " SELECT dt.DocBaseType, pf.RPL_TList_PrintFormat_ID, " 			//1..2
        		+ " pf.PUT_TList_PrintFormat_ID, pf.PCK_CluTList_PrintFormat_ID, "	//3..4
        		+ " pf.PCK_OrdTList_PrintFormat_ID, M.PickMethod, "					//5..6
        		+ " c.IsMultiLingualDocument, COALESCE(dt.DocumentCopies,0), "		//7..8
        		+ " dt.AD_PrintFormat_ID"											//9
        		+ " FROM M_TaskList M "
        		+ " INNER JOIN AD_Client c ON (M.AD_Client_ID=c.AD_Client_ID)"
        		+ " INNER JOIN AD_PrintForm pf ON (M.AD_Client_ID=pf.AD_Client_ID OR pf.AD_Client_ID=0)"
        		+ " LEFT OUTER JOIN C_DocType dt ON (M.C_DocType_ID=dt.C_DocType_ID) "
        		+ " WHERE M.M_TaskList_ID=?"
        		+ " AND pf.AD_Org_ID IN (0,M.AD_Org_ID) "
        		+ " ORDER BY pf.AD_Client_ID DESC,  pf.AD_Org_ID DESC";		
		else	//	Get PrintFormat from Org or 0 of document client
			sql = "SELECT " 
				+ "COALESCE(bp.Order_PrintFormat_ID,dt.AD_PrintFormat_ID,pf.Order_PrintFormat_ID)," //1 
				+ "COALESCE(dt.AD_PrintFormat_ID, pf.Shipment_PrintFormat_ID), "		//	2
				//	Prio: 1. BPartner 2. DocType, 3. PrintFormat (Org)	//	see InvoicePrint
				+ " COALESCE (bp.Invoice_PrintFormat_ID,dt.AD_PrintFormat_ID,pf.Invoice_PrintFormat_ID)," // 3
				+ " COALESCE(dt.AD_PrintFormat_ID,pf.Project_PrintFormat_ID), "			// 4
				+ " COALESCE(dt.AD_PrintFormat_ID,pf.Remittance_PrintFormat_ID), "		// 5
				+ " c.IsMultiLingualDocument, bp.AD_Language,"						//	6..7
				+ " COALESCE(dt.DocumentCopies,0)+COALESCE(bp.DocumentCopies,1), " 	// 	8
				+ " dt.AD_PrintFormat_ID,bp.C_BPartner_ID,d.DocumentNo "			//	9..11
				+ "FROM " + DOC_BASETABLES[type] + " d"
				+ " INNER JOIN AD_Client c ON (d.AD_Client_ID=c.AD_Client_ID)"
				+ " INNER JOIN AD_PrintForm pf ON (c.AD_Client_ID=pf.AD_Client_ID)"
				+ " INNER JOIN C_BPartner bp ON (d.C_BPartner_ID=bp.C_BPartner_ID)"
				+ " LEFT OUTER JOIN C_DocType dt ON (d.C_DocType_ID=dt.C_DocType_ID) "
				+ "WHERE d." + DOC_IDS[type] + "=?"			//	info from PrintForm
				+ " AND pf.AD_Org_ID IN (0,d.AD_Org_ID) "
				+ "ORDER BY pf.AD_Org_ID DESC";
		//
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, Record_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())	//	first record only
			{
				if (type == CHECK || type == DUNNING || type == REMITTANCE 
					|| type == PROJECT || type == RFQ)
				{
					AD_PrintFormat_ID = rs.getInt(1);
					copies = 1;
					//	Set Language when enabled
					String AD_Language = rs.getString(3);
					if (AD_Language != null)// && "Y".equals(rs.getString(2)))	//	IsMultiLingualDocument
						language = Language.getLanguage(AD_Language);
					C_BPartner_ID = rs.getInt(4);
					if (type == DUNNING)
					{
						Timestamp ts = rs.getTimestamp(5);
						DocumentNo = ts.toString();
					}
					else
						DocumentNo = rs.getString(5);
				}
                else if (type == MOVEMENT || type == INVENTORY) 
                {
                    int pfAD_PrintFormat_ID = rs.getInt(1);
                    AD_PrintFormat_ID = rs.getInt(4);
                    if(AD_PrintFormat_ID == 0)
                    	AD_PrintFormat_ID = pfAD_PrintFormat_ID;
                    
                    copies = rs.getInt(3);
                    if (copies == 0)
                    	copies = 1;
                }
                else if (type == WORKORDER || type == WORKORDERTXN)
                {
                    int pfAD_PrintFormat_ID = rs.getInt(1);
                    AD_PrintFormat_ID = rs.getInt(4);
                    if(AD_PrintFormat_ID == 0)
                    	AD_PrintFormat_ID = pfAD_PrintFormat_ID;
                    
                    copies = rs.getInt(3);
                    if (copies == 0)
                    	copies = 1;
					String AD_Language = rs.getString(2);
					if (AD_Language != null) // && "Y".equals(rs.getString(6)))	//	IsMultiLingualDocument
						language = Language.getLanguage(AD_Language);
                }
                else if (type == STANDARDOPERATION || type == ROUTING)
                {
                	AD_PrintFormat_ID = rs.getInt(1);
                	copies = 1;
                	/*String AD_Language = rs.getString(2);
                	if (AD_Language != null) // && "Y".equals(rs.getString(6))) // IsMultiLingualDocument
                	language = Language.getLanguage(AD_Language);*/
                }

                else if (type == TASKLIST)
                {
                	String docBaseType = rs.getString(1);
                	int replFormatID = rs.getInt(2);
                	int putFormatID = rs.getInt(3);
                	int cpickFormatID = rs.getInt(4);
                	int opickFormatID = rs.getInt(5);                	
                	String pmethod = rs.getString(6);
					copies = rs.getInt(8);
                    if (copies == 0)
                    	copies = 1;                 
                    AD_PrintFormat_ID = rs.getInt(9);
                    if(AD_PrintFormat_ID == 0)
                    {
	                    if(docBaseType.equalsIgnoreCase("RPL"))
	                    	AD_PrintFormat_ID = replFormatID;
	                    else if(docBaseType.equalsIgnoreCase("PUT"))
	                    	AD_PrintFormat_ID = putFormatID;
	                    else if(docBaseType.equalsIgnoreCase("PCK"))
	                    {
	                    	if( pmethod.equalsIgnoreCase("C"))
	                    		AD_PrintFormat_ID = cpickFormatID;
	                    	else
	                    		AD_PrintFormat_ID = opickFormatID;
	                    }
                    }
					
                }        
				else
				{
					//	Set PrintFormat
					AD_PrintFormat_ID = rs.getInt(type+1);
					if (type != INVOICE && type != ORDER && rs.getInt(9) != 0)		//	C_DocType.AD_PrintFormat_ID
						AD_PrintFormat_ID = rs.getInt(9);
					copies = rs.getInt(8);
					//	Set Language when enabled
					String AD_Language = rs.getString(7);
					if (AD_Language != null) // && "Y".equals(rs.getString(6)))	//	IsMultiLingualDocument
						language = Language.getLanguage(AD_Language);
					C_BPartner_ID = rs.getInt(10);
					DocumentNo = rs.getString(11);
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Record_ID=" + Record_ID + ", SQL=" + sql, e);
		}
		if (AD_PrintFormat_ID == 0)
		{
			log.log(Level.SEVERE, "No PrintFormat found for Type=" + type + ", Record_ID=" + Record_ID);
			log.saveError("NoDocPrintFormat", Msg.getMsg(ctx, "NoDocPrintFormat"));
			return null;
		}

		//	Get Format & Data
		MPrintFormat format = MPrintFormat.get (ctx, AD_PrintFormat_ID, false);
		format.setLanguage(language);		//	BP Language if Multi-Lingual
	//	if (!Env.isBaseLanguage(language, DOC_TABLES[type]))
			format.setTranslationLanguage(language);
		//	query
		Query query = new Query(DOC_TABLES[type]);
		query.addRestriction(DOC_IDS[type], Query.EQUAL, new Integer(Record_ID));
	//	log.config( "ReportCtrl.startDocumentPrint - " + format, query + " - " + language.getAD_Language());
		//
		if (DocumentNo == null || DocumentNo.length() == 0)
			DocumentNo = "DocPrint";
		PrintInfo info = new PrintInfo(
			DocumentNo,
			DOC_TABLE_ID[type],
			Record_ID,
			C_BPartner_ID);
		info.setCopies(copies);
		info.setDocumentCopy(false);		//	true prints "Copy" on second
		
		//	Engine
		ReportEngine re = new ReportEngine(ctx, format, query, info);
		return re;
	}	//	get

	/**
	 *	Determine what Order document to print.
	 *  @param C_Order_ID id
	 *	@return int Array with [printWhat, ID]
	 */
	private static int[] getDocumentWhat (int C_Order_ID)
	{
		int[] what = new int[2];
		what[0] = ORDER;
		what[1] = C_Order_ID;
		//
		String sql = "SELECT dt.DocSubTypeSO "
			+ "FROM C_DocType dt, C_Order o "
			+ "WHERE o.C_DocType_ID=dt.C_DocType_ID"
			+ " AND o.C_Order_ID=?";
		String DocSubTypeSO = null;
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_Order_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				DocSubTypeSO = rs.getString(1);
			rs.close();
			pstmt.close();
		}
		catch (SQLException e1)
		{
			log.log(Level.SEVERE, "(1) - " + sql, e1);
			return null;		//	error
		}
		if (DocSubTypeSO == null)
			DocSubTypeSO = "";
		//	WalkIn Receipt, WalkIn Invoice,
		if (DocSubTypeSO.equals("WR") || DocSubTypeSO.equals("WI"))
			what[0] = INVOICE;
		//	WalkIn Pickup,
		else if (DocSubTypeSO.equals("WP"))
			what[0] = SHIPMENT;
		//	Offer Binding, Offer Nonbinding, Standard Order
		else
			return what;

		//	Get Record_ID of Invoice/Receipt
		if (what[0] == INVOICE)
			sql = "SELECT C_Invoice_ID REC FROM C_Invoice WHERE C_Order_ID=?"	//	1
				+ " AND docstatus!='RE'"	//do not print reversed invoices
				+ " ORDER BY C_Invoice_ID DESC";
		else
			sql = "SELECT M_InOut_ID REC FROM M_InOut WHERE C_Order_ID=?" 	//	1
				+ " ORDER BY M_InOut_ID DESC";
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_Order_ID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
			{
			//	if (i == 1 && ADialog.ask(0, null, what[0] == INVOICE ? "PrintOnlyRecentInvoice?" : "PrintOnlyRecentShipment?")) break;
				what[1] = rs.getInt(1);
			}
			else	//	No Document Found
				what[0] = ORDER;

			rs.close();
			pstmt.close();
		}
		catch (SQLException e2)
		{
			log.log(Level.SEVERE, "(2) - " + sql, e2);
			return null;
		}
		log.fine("Order => " + what[0] + " ID=" + what[1]);
		return what;
	}	//	getDocumentWhat

	/**
	 * 	Print Confirm.
	 *  Update Date Printed
	 * 	@param type document type
	 * 	@param Record_ID record id
	 */
	public static void printConfirm (int type, int Record_ID)
	{
		StringBuffer sql = new StringBuffer();
		if (type == ORDER || type == SHIPMENT || type == INVOICE)
			sql.append("UPDATE ").append(DOC_BASETABLES[type])
				.append(" SET DatePrinted=SysDate, IsPrinted='Y' WHERE ")
				.append(DOC_IDS[type]).append("=").append(Record_ID);
		//
		if (sql.length() > 0)
		{
			int no = DB.executeUpdate((Trx) null, sql.toString());
			if (no != 1)
				log.log(Level.SEVERE, "Updated records=" + no + " - should be just one");
		}
	}	//	printConfirm
	
	public Excel createEXCEL (Excel excel, Language language)
	{
		final int COTA_EXCEL = Short.MAX_VALUE; 
		try {
			//	for all rows (-1 = header row)
			for (int row = -1; row < m_printData.getRowCount(); row++) {
				//StringBuffer sb = new StringBuffer();
				//Agregado por Centrobeco
				if (row % COTA_EXCEL == COTA_EXCEL - 1) {
					excel.createAndSetSheet("Export Compiere " + ( 1 + row / COTA_EXCEL));
				}				
				if (row != -1)
					m_printData.setRowIndex(row);

				//	for all columns
				//boolean first = true;	//	first column to print
				int colPos = 0;
				for (int col = 0; col < m_printFormat.getItemCount(); col++){
					MPrintFormatItem item = m_printFormat.getItem(col);
					if (item.isPrinted()){
						//	header row - Modificado para que lo imprima por cada hoja nueva
						//if (row == -1) {						
						if (row == -1 || row % COTA_EXCEL == COTA_EXCEL - 1) {														
							excel.createRow(
									(short)0, 
									(short)colPos, 
									m_printFormat.getItem(col).getPrintName(language), 
									null,
									Excel.CELLSTYLE_HEADER,
									Excel.DISPLAY_TYPE_STRING);
						} else 	{							
							int displayType = Excel.DISPLAY_TYPE_STRING;
							Object obj = m_printData.getNode(new Integer(item.getAD_Column_ID()));
							Object data = "";
							String dataStr = "";
							if (obj == null)
								;
							else if (obj instanceof PrintDataElement) {
								PrintDataElement pde = (PrintDataElement)obj;
								dataStr = pde.getValueDisplay(language,0);	//	formatted	
								data = pde.getValue();
								switch (pde.getDisplayType()) {
								case DisplayTypeConstants.Number:
									displayType = Excel.DISPLAY_TYPE_NUMBER;
									break;
								case DisplayTypeConstants.Amount:
									displayType = Excel.DISPLAY_TYPE_NUMBER;
									break;
								case DisplayTypeConstants.Quantity:
									displayType = Excel.DISPLAY_TYPE_INTEGER;
									break;
								case DisplayTypeConstants.CostPrice:
									displayType = Excel.DISPLAY_TYPE_NUMBER;
									break;
								case DisplayTypeConstants.Integer:
									displayType = Excel.DISPLAY_TYPE_INTEGER;
									break;
								case DisplayTypeConstants.Date:
									displayType = Excel.DISPLAY_TYPE_DATE;
									break;
								case DisplayTypeConstants.DateTime:
									displayType = Excel.DISPLAY_TYPE_DATE;
									break;
								case DisplayTypeConstants.Time:
									displayType = Excel.DISPLAY_TYPE_DATE;
									break;
								default:
									break;
								}
							}
							else if (obj instanceof PrintData){
							}
							else
								log.log(Level.SEVERE, "createCSV - Element not PrintData(Element) " + obj.getClass());
							/* Hecho por Javier Pino, sustituyendo la llamada por otro método overloaded  */														
							excel.createRow(
									//(row + 1), -- Comentado y modificado para que imprima desde el principio de las hojas sucesivas
									(short)(row % COTA_EXCEL + 1),
									(short)colPos, 
									data, 
									dataStr,
									Excel.CELLSTYLE_NONE,
									displayType);
						}
						colPos++;
					}	//	printed
				}	//	for all columns
			}	//	for all rows
		}
		catch (Exception e){
			log.log(Level.SEVERE, "createCSV(w)", e);
		}
		excel.close();
		return excel;
	}
	
	public boolean createEXCEL (File file, Language language){
		Excel excel = new Excel(file);
		excel = createEXCEL(excel, getPrintFormat().getLanguage());
		return true;
	}
	
	public static BusinessView getBusinessView(ProcessInfo pi){
		
		//	check if a business view has been defined for a report and process
		String sql = "SELECT bv.AD_BView_ID,bvs.SourceType,bvs.AD_Table_ID,bvs.AD_ReportView_ID,bvs.AD_Process_ID," //1..5
				+"pf.AD_PrintFormat_ID, pf.IsForm, pf.AD_Client_ID "		   //6..8
				+" FROM AD_PInstance pi "
				+ " INNER JOIN AD_Process p ON (pi.AD_Process_ID=p.AD_Process_ID)"			
				+ " LEFT OUTER JOIN AD_BView bv on (p.AD_BView_ID=bv.AD_BView_ID)"
				+" LEFT OUTER JOIN AD_BView_Source bvs on (bv.AD_BView_ID=bvs.AD_BView_ID)"
				+ " LEFT OUTER JOIN AD_PrintFormat pf ON (p.AD_BView_ID=pf.AD_BView_ID AND pf.AD_Client_ID IN (0,?)) "
				+ " WHERE pi.AD_PInstance_ID=? AND bvs.IsDefault = 'Y'"		//	#2		
				+ " ORDER BY pf.AD_Client_ID DESC, pf.IsDefault DESC";	//	own first
				;
		
		int AD_BView_ID = 0;
		int sourceType = 0;
		int source = 0;
		String tableName = null;
		int AD_Table_ID = 0;
		int AD_PrintFormat_ID = 0;
		int AD_Client_ID = 0;
		String whereClause="";
		try{
			PreparedStatement pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, pi.getAD_Client_ID());
			pstmt.setInt(2, pi.getAD_PInstance_ID());
			ResultSet rs = pstmt.executeQuery();
			//	Just get first
			if (rs.next())
			{				
				AD_BView_ID = rs.getInt(1);
				sourceType = rs.getInt(2);
				source = rs.getInt(3);
				AD_PrintFormat_ID=rs.getInt(6);
				AD_Client_ID = rs.getInt(8);
				
				if (sourceType != 0){
					String tableSql = null;
					if (sourceType == Integer.parseInt(X_AD_BView_Source.SOURCETYPE_TableOrView)){
						tableSql = "SELECT AD_Table_ID,TableName FROM AD_Table WHERE AD_Table_ID = ?";
						source = rs.getInt(3);
					}
					else if (sourceType == Integer.parseInt(X_AD_BView_Source.SOURCETYPE_ReportView)){
						tableSql = "SELECT t.AD_Table_ID,t.TableName,rv.WhereClause FROM AD_ReportView rv, AD_Table t WHERE rv.AD_Table_ID = t.AD_Table_ID AND rv.AD_ReportView_ID = ?";
						source = rs.getInt(4);
					}
					else if (sourceType == Integer.parseInt(X_AD_BView_Source.SOURCETYPE_ReportProcess)){
						tableSql = "SELECT t.AD_Table_ID,t.TableName FROM AD_Process p,AD_ReportView rv, AD_Table t WHERE p.AD_ReportView_ID = rv.AD_ReportView_Id AND rv.AD_Table_ID = t.AD_Table_ID AND p.AD_Process_ID = ?";
						source = rs.getInt(5);
					}
	
					PreparedStatement tableStmt = DB.prepareStatement(tableSql, (Trx) null);
					tableStmt.setInt(1, source);
					ResultSet tableResultSet = tableStmt.executeQuery();
					
					if (tableResultSet.next()){
						AD_Table_ID = tableResultSet.getInt(1);
						tableName = tableResultSet.getString(2);
						
						if (sourceType == Integer.parseInt(X_AD_BView_Source.SOURCETYPE_ReportView)){
							whereClause = tableResultSet.getString(3);
						}
					}
					
					tableResultSet.close();
					tableStmt.close();		
				}
			}
			
			rs.close();
			pstmt.close();
		}
		catch (SQLException e1)
		{
			log.log(Level.SEVERE, "(1) - " + sql, e1);
		}
		return new BusinessView(AD_BView_ID,tableName,AD_Table_ID,AD_PrintFormat_ID,AD_Client_ID,whereClause);
	}	
	
	public byte[] getPDFAsArray(LayoutEngine layout){
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();	
		try {
			CompierePdfGraphics2D.writePDF(bout, layout,null);
			return (bout.toByteArray());	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		return null;		
	}	
	
	public static Document createDocument(){
		Document doc = CompierePdfGraphics2D.createDocument();
		return doc;
	}
	
	public static PdfContentByte getContentByte(Document doc, File f){
		return CompierePdfGraphics2D.getContentByte(doc, f);
	}
	
	/*************************************************************************
	 * 	Test
	 * 	@param args args
	 */
	public static void main(String[] args)
	{
		org.compiere.Compiere.startup(true);
		//
		int AD_Table_ID = 100;
		Query q = new Query("AD_Table");
		q.addRestriction("AD_Table_ID", "<", 108);
		//
		MPrintFormat f = MPrintFormat.createFromTable(Env.getCtx(), AD_Table_ID);
		PrintInfo i = new PrintInfo("test", AD_Table_ID, 108, 0);
		i.setAD_Table_ID(AD_Table_ID);
		ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
		re.layout();
		/**
		re.createCSV(new File("C:\\Temp\\test.csv"), ',', Language.getLanguage());
		re.createHTML(new File("C:\\Temp\\test.html"), false, Language.getLanguage());
		re.createXML(new File("C:\\Temp\\test.xml"));
		re.createPS(new File ("C:\\Temp\\test.ps"));
		re.createPDF(new File("C:\\Temp\\test.pdf"));
		/****/
		re.print();
	//	re.print(true, 1, false, "Epson Stylus COLOR 900 ESC/P 2");		//	Dialog
	//	re.print(true, 1, false, "HP LaserJet 3300 Series PCL 6");		//	Dialog
	//	re.print(false, 1, false, "Epson Stylus COLOR 900 ESC/P 2");	//	Dialog
		System.exit(0);
	}	//	main

	public void consolidateInvoices(Document doc, PdfContentByte cb) throws PrinterException {
		// TODO Auto-generated method stub
		CompierePdfGraphics2D.consolidateDocument(doc, cb, getLayout());
	}
	
}	//	ReportEngine

class BusinessView{
	int AD_BView_ID =0;
	String tableName = null;
	int AD_Table_ID = 0;
	int AD_PrintFormat_ID=0;
	int AD_Client_ID=0;
	String whereClause="";
	
	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public int getAD_Client_ID() {
		return AD_Client_ID;
	}

	public void setAD_Client_ID(int client_ID) {
		AD_Client_ID = client_ID;
	}

	public int getAD_PrintFormat_ID() {
		return AD_PrintFormat_ID;
	}

	public void setAD_PrintFormat_ID(int printFormat_ID) {
		AD_PrintFormat_ID = printFormat_ID;
	}

	public BusinessView(){		
	}
	
	public int getAD_Table_ID() {
		return AD_Table_ID;
	}
	public void setAD_Table_ID(int table_ID) {
		AD_Table_ID = table_ID;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getAD_BView_ID() {
		return AD_BView_ID;
	}
	public void setAD_BView_ID(int view_ID) {
		AD_BView_ID = view_ID;
	}

	public BusinessView(int AD_BView_ID, String TableName, int AD_Table_ID,int AD_PrintFormat_ID, int AD_Client_ID,String whereClause) {
		super();
		this.AD_BView_ID = AD_BView_ID;
		this.tableName = TableName;
		this.AD_Table_ID = AD_Table_ID;
		this.AD_PrintFormat_ID = AD_PrintFormat_ID;
		this.AD_Client_ID = AD_Client_ID;
		this.whereClause=whereClause;
	}		
}