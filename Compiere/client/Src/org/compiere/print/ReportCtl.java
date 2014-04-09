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

import java.util.logging.Level;

import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.MPaySelectionCheck;
import org.compiere.model.MPayment;
import org.compiere.model.MProcess;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.Msg;
import org.compiere.vos.DocActionConstants;

/**
 *	Report Controller.
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ReportCtl.java,v 1.3 2006/10/08 07:05:08 comdivision Exp $
 */
public class ReportCtl
{
	/**
	 *	Constructor - prevent instance
	 */
	private ReportCtl()
	{
	}	//	ReportCtrl

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (ReportCtl.class);
	
	/**
	 *	Create Report.
	 *	Called from ProcessCtl.
	 *	- Check special reports first, if not, create standard Report
	 *
	 *  @param pi process info
	 *  @param IsDirectPrint if true, prints directly - otherwise View
	 *  @return true if created
	 */
	static public ReportEngine start( Ctx ctx, ProcessInfo pi, boolean IsDirectPrint )
	{
		s_log.info("" + pi);

		/**
		 *	Order Print
		 */
		if (pi.getAD_Process_ID() == 110)			//	C_Order
			return startDocumentPrint( ctx, ReportEngine.ORDER, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 116)		//	C_Invoice
			return startDocumentPrint( ctx, ReportEngine.INVOICE, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 117)		//	M_InOut
			return startDocumentPrint( ctx, ReportEngine.SHIPMENT, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 217)		//	C_Project
			return startDocumentPrint( ctx, ReportEngine.PROJECT, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 276)		//	C_RfQResponse
			return startDocumentPrint( ctx, ReportEngine.RFQ, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 313)		//	C_Payment
			return startCheckPrint( ctx, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 290)      // 	Movement
            return startDocumentPrint( ctx, ReportEngine.MOVEMENT, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID () == 291)		//	Inventory
            return startDocumentPrint( ctx, ReportEngine.INVENTORY, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 415)		//	M_WorkOrder
			return startDocumentPrint( ctx, ReportEngine.WORKORDER, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 1481)		//	M_WorkOrderTransaction 
			return startDocumentPrint( ctx, ReportEngine.WORKORDERTXN, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 1504)		//	M_StandardOperation
			return startDocumentPrint( ctx, ReportEngine.STANDARDOPERATION, pi.getRecord_ID(), IsDirectPrint );
		else if (pi.getAD_Process_ID() == 1503)		//	M_Routing
			return startDocumentPrint( ctx, ReportEngine.ROUTING, pi.getRecord_ID(), IsDirectPrint );
		
		/**
		else if (pi.AD_Process_ID == 9999999)	//	PaySelection
			return startDocumentPrint(CHECK, pi, IsDirectPrint);
		else if (pi.AD_Process_ID == 9999999)	//	PaySelection
			return startDocumentPrint(REMITTANCE, pi, IsDirectPrint);
		**/
		else if (pi.getAD_Process_ID() == 159)		//	Dunning
			return startDocumentPrint( ctx, ReportEngine.DUNNING, pi.getRecord_ID(), IsDirectPrint );
	   else if (pi.getAD_Process_ID() == 202			//	Financial Report
			|| pi.getAD_Process_ID() == 204)			//	Financial Statement
		   return startFinReport (ctx, pi);
	   else if (pi.getAD_Process_ID() == MProcess.getIDByValue(ctx, "PrintTaskList"))
		   return startDocumentPrint( ctx, ReportEngine.TASKLIST, pi.getRecord_ID(), IsDirectPrint );
		/********************
		 *	Standard Report
		 *******************/
		return startStandardReport (ctx, pi, IsDirectPrint);
	}	//	create

	
	/**************************************************************************
	 *	Start Standard Report.
	 *  - Get Table Info & submit
	 *  @param pi Process Info
	 *  @param IsDirectPrint if true, prints directly - otherwise View
	 *  @return true if OK
	 */
	static public ReportEngine startStandardReport( Ctx ctx, ProcessInfo pi, boolean IsDirectPrint )
	{
		ReportEngine re = ReportEngine.get(ctx, pi);
		if (re == null)
		{
			pi.setSummary("No ReportEngine");
			return null;
		}
		if (IsDirectPrint)
		{
			re.print();
		}
		return re;
	}	//	startStandardReport

	/**
	 *	Start Financial Report.
	 *  @param pi Process Info
	 *  @return true if OK
	 */
	static public ReportEngine startFinReport (Ctx ctx, ProcessInfo pi)
	{
		//  Create Query from Parameters
		String TableName = pi.getAD_Process_ID() == 202 ? "T_Report" : "T_ReportStatement";
		Query query = Query.get (ctx, pi.getAD_PInstance_ID(), TableName);

		//	Get PrintFormat
		MPrintFormat format = (MPrintFormat)pi.getTransientObject();
		if (format == null)
			format = (MPrintFormat)pi.getSerializableObject();
		if (format == null)
		{
			s_log.log(Level.SEVERE, "startFinReport - No PrintFormat");
			return null;
		}
		PrintInfo info = new PrintInfo(pi);

		ReportEngine re = new ReportEngine(ctx, format, query, info);
		return re;
	}	//	startFinReport


	/**
	 * 	Start Document Print for Type.
	 *  	Called also directly from ProcessDialog, VInOutGen, VInvoiceGen, VPayPrint
	 * 	@param type document type in ReportEngine
	 * 	@param Record_ID id
	 * 	@param IsDirectPrint if true, prints directly - otherwise View
	 * 	@return true if success
	 */
	public static ReportEngine startDocumentPrint( Ctx ctx, int type, int Record_ID, boolean IsDirectPrint )
	{
		ReportEngine re = ReportEngine.get (ctx, type, Record_ID);
		if (re == null)
		{
			//if(!ctx.getContext(CtxConstants.WEB_SESSION).equals("WebApp"))
				//ADialog.error(0, null, "NoDocPrintFormat");
			return null;
		}
		if (IsDirectPrint)
		{
			re.print();
			ReportEngine.printConfirm (type, Record_ID);
		}
		return re;
	}	//	StartDocumentPrint

	
	/**
	 * 	Start Check Print.
	 * 	Find/Create
	 *	@param C_Payment_ID Payment
	 * 	@param IsDirectPrint if true, prints directly - otherwise View
	 * 	@return true if success
	 */
	public static ReportEngine startCheckPrint ( Ctx ctx, int C_Payment_ID, boolean IsDirectPrint)
	{
		int C_PaySelectionCheck_ID = 0;
		MPaySelectionCheck psc = MPaySelectionCheck.getOfPayment(ctx, C_Payment_ID, null);
		if (psc != null)
		{
			C_PaySelectionCheck_ID = psc.getC_PaySelectionCheck_ID();

			// this is to catch allocations, if they are done after the first printing
			psc.updateForPayment(C_Payment_ID);
		}
		else
		{
			//if the document is not completed, closed, reversed, or voided, don't print
			MPayment pm= new MPayment(ctx, C_Payment_ID,null);
			if(pm.getDocStatus().equals(DocActionConstants.STATUS_Completed)||pm.getDocStatus().equals(DocActionConstants.STATUS_Closed)
					||pm.getDocStatus().equals(DocActionConstants.STATUS_Reversed)||pm.getDocStatus().equals(DocActionConstants.STATUS_Voided)) 
			{ 
				psc = MPaySelectionCheck.createForPayment(ctx, C_Payment_ID, null);
				if (psc != null)
				{
					C_PaySelectionCheck_ID = psc.getC_PaySelectionCheck_ID();

					// this is to catch allocations, if they are done after the first printing
					psc.updateForPayment(C_Payment_ID);
				}
			}
			else
			{
				s_log.saveError("PaymentNotCompleted", Msg.getMsg(ctx, "PaymentNotCompleted"));
				return null;
			}
		}
		return startDocumentPrint ( ctx, ReportEngine.CHECK, C_PaySelectionCheck_ID, IsDirectPrint);
	}	//	startCheckPrint
}	//	ReportCtl
