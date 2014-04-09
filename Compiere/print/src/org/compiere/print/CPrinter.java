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

import java.awt.event.*;
import java.awt.print.*;

import javax.print.*;

import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Compiere Printer Selection
 *
 *  @author     Jorg Janke
 *  @version    $Id: CPrinter.java,v 1.3 2006/07/30 00:53:02 jjanke Exp $
 */
public class CPrinter extends CComboBox implements ActionListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	/**
	 *  Get Print (Services) Names
	 *  @return Printer Name array
	 */
	public static String[] getPrinterNames()
	{
		if (s_services == null)
			return new String[]{};
		String[] retValue = new String[s_services.length];
		for (int i = 0; i < s_services.length; i++)
		{
			if (s_services[i] == null)
			{
				retValue[i] = "";
				log.warning("Empty print service returned from O/S  #" + i);
			}
			else
				retValue[i] = s_services[i].getName();
		}
		return retValue;
	}   //  getPrintServiceNames


	/**
	 *  Return default PrinterJob
	 *  @return PrinterJob
	 */
	public static PrinterJob getPrinterJob()
	{
		return getPrinterJob(Env.getCtx().getPrinterName());
	}   //  getPrinterJob

	/**
	 *  Return PrinterJob with selected printer name.
	 *  @param printerName if null, get default printer (Ini)
	 *  @return PrinterJob
	 */
	public static PrinterJob getPrinterJob (String printerName)
	{
		PrinterJob pj = null;
		PrintService ps = null;
		try
		{
			pj = PrinterJob.getPrinterJob();

			//  find printer service
			if ((printerName == null) || (printerName.length() == 0))
				printerName = Env.getCtx().getPrinterName();
			if ((printerName != null) && (printerName.length() != 0))
			{
			//	System.out.println("CPrinter.getPrinterJob - searching " + printerName);
				for (PrintService element : s_services) {
					String serviceName = element.getName();
					if (printerName.equals(serviceName))
					{
						ps = element;
					//	System.out.println("CPrinter.getPrinterJob - found " + printerName);
						break;
					}
				//	System.out.println("CPrinter.getPrinterJob - not: " + serviceName);
				}
			}   //  find printer service

			try
			{
				if (ps != null)
					pj.setPrintService(ps);
			}
			catch (Exception e)
			{
				log.warning("Could not set Print Service: " + e.toString());
			}
			//
			PrintService psUsed = pj.getPrintService();
			if (psUsed == null)
				log.warning("Print Service not Found");
			else
			{
				String serviceName = psUsed.getName();
				if ((printerName != null) && !printerName.equals(serviceName))
					log.warning("Not found: " + printerName + " - Used: " + serviceName);
			}
		}
		catch (Exception e)
		{
			log.warning("Could not create for " + printerName + ": " + e.toString());
		}
		return pj;
	}   //  getPrinterJob


	/** Available Printer Services  */
//	private static PrintService[]   s_services = PrinterJob.lookupPrintServices();
	private static PrintService[]   s_services = PrintServiceLookup.lookupPrintServices(null,null);

	/**	Logger	*/
	private static CLogger log = CLogger.getCLogger (CPrinter.class);


	/**************************************************************************
	 *  Create PrinterJob
	 */
	public CPrinter()
	{
		super(getPrinterNames());
		//  Set Default
		setValue(Env.getCtx().getPrinterName());
		this.addActionListener(this);
	}   //  CPrinter

	/**
	 * 	Action Listener
	 * 	@param e event
	 */
	@Override
	public void actionPerformed (ActionEvent e)
	{

	}	//	actionPerformed

	/**
	 * 	Get PrintService
	 * 	@return print service
	 */
	public PrintService getPrintService()
	{
		String currentService = (String)getSelectedItem();
		for (PrintService element : s_services)
		{
			if (element.getName().equals(currentService))
				return element;
		}
		return PrintServiceLookup.lookupDefaultPrintService();
	}	//	getPrintService

}   //  CPrinter
