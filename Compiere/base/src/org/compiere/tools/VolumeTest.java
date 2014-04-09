/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.tools;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.process.*;
import org.compiere.util.*;


/**
 * 	Volume Test
 *	@author Jorg Janke
 */
public class VolumeTest extends SvrProcess
{
	/** Prefix for created entities		*/
	String	p_Prefix = null;
	/** Create number of BP				*/
	int		p_VolumeBPartner = 1;
	/** Create Number of Users per BP	*/
	int		p_VolumeUser = 0;
	/** Role for User					*/
	int		p_AD_Role_ID = 0;
	/** Create number of Products		*/
	int		p_VolumeProduct = 17;
	/** Create number of Orders			*/
	int		p_VolumeOrder = 1;
	/** Create lines per order			*/
	int		p_VolumeLine = 1000;
	/** Document Type					*/
	int		p_C_DocTypeTarget_ID = 135;		//	POS Gardenworld
	
    int C_Tax_ID = 105;
	/** Doc Action						*/
	String	p_DocAction = null;

	/** Price List						*/
	private	MPriceList				m_pl = null;
	private MPriceListVersion		m_plv = null;

	private ArrayList<MBPartner>	m_bpartners = new ArrayList<MBPartner>();
	private ArrayList<MProduct>		m_products = new ArrayList<MProduct>();


	/** Commit every 100 entities	*/
	private static int		COMMITCOUNT = 100;

	/**
	 * 	Prepare parameters
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para)
		{
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("Prefix"))
				p_Prefix = (String)element.getParameter();
			else if (name.equals("VolumeBPartner"))
				p_VolumeBPartner = element.getParameterAsInt();
			else if (name.equals("VolumeUser"))
				p_VolumeUser = element.getParameterAsInt();
			else if (name.equals("AD_Role_ID"))
				p_AD_Role_ID = element.getParameterAsInt();
			//
			else if (name.equals("VolumeProduct"))
				p_VolumeProduct = element.getParameterAsInt();
			else if (name.equals("VolumeOrder"))
				p_VolumeOrder = element.getParameterAsInt();
			else if (name.equals("VolumeLine"))
				p_VolumeLine = element.getParameterAsInt();
			//
			else if (name.equals("DocAction"))
				p_DocAction = (String)element.getParameter();
			else if (name.equals("C_DocTypeTarget_ID"))
				p_C_DocTypeTarget_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (Util.isEmpty(p_Prefix))
			p_Prefix = String.valueOf(System.currentTimeMillis()) + "_";
	}	//	prepare

	/**
	 * 	Execute
	 * 	@return info
	 */
	@Override
	protected String doIt() throws Exception
	{
		
		log.info("Prefix=" + p_Prefix
			+ ",VolumeBPartner=" + p_VolumeBPartner
			+ ",VolumeUser=" + p_VolumeUser + ",AD_Role_ID=" + p_AD_Role_ID
			+ ",VolumeProduct=" + p_VolumeProduct
			+ ",VolumeOrder=" + p_VolumeOrder
			+ ",VolumeLine=" + p_VolumeLine
			+ ",C_DocTypeTarget_ID=" + p_C_DocTypeTarget_ID
			+ ",DocAction" + p_DocAction);
		//	Verify
		if (getCtx().getAD_Client_ID() == 0)
			throw new IllegalArgumentException("Cannot run in Client=System");
		MDocType dt = MDocType.get(getCtx(), p_C_DocTypeTarget_ID);
		if (dt.getC_DocType_ID() != p_C_DocTypeTarget_ID)
			throw new IllegalArgumentException("@NotFound@ @C_DocTypeTarget_ID@ - ID=" + p_C_DocTypeTarget_ID);
		if (dt.getAD_Client_ID() != getCtx().getAD_Client_ID())
			throw new IllegalArgumentException("Client conflict: " + dt);

		m_pl = MPriceList.getDefault(getCtx(), true);
		if (m_pl == null)
			throw new IllegalArgumentException("@NotFound@ @M_PriceList_ID@");
		m_plv = m_pl.getPriceListVersion(null);
		if (m_pl == null)
			throw new IllegalArgumentException("@NotFound@ @M_PriceListVersion_ID@");

		//
		long start = System.currentTimeMillis();
		createBPartners(dt.isSOTrx());
		createProducts(dt.isSOTrx());
		
		DB.startLoggingUpdates();
		createOrders();
		//
		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		return "Total: " + duration + "s";
	}	//	doIt

	/**
	 * 	Create B.Partners
	 */
	private void createBPartners(boolean isSOTrx)
	{
		long start = System.currentTimeMillis();
		int AD_Client_ID = getCtx().getAD_Client_ID();
		int count = 0;
		int error = 0;
		for (int i = 0; i < p_VolumeBPartner; i++)
		{
			MBPartner bp = MBPartner.getTemplate(getCtx(), AD_Client_ID);
			bp.setValue (p_Prefix + "_BP_" + i);
			bp.setName (p_Prefix + "_BP_" + i);
			if (isSOTrx)
				bp.setIsCustomer(true);
			else
				bp.setIsVendor(true);
			if (!bp.save(get_TrxName()))
			{
				error++;
				continue;
			}
			MLocation addr = new MLocation(getCtx(), 0, get_TrxName());
			addr.setAddress1(p_Prefix + "_addr_" + i);
			addr.setCity("City_" + i);
			if (!addr.save())
			{
				error++;
				continue;
			}
			MBPartnerLocation loc = new MBPartnerLocation(bp);
			loc.setC_Location_ID(addr.getC_Location_ID());
			if (!loc.save())
			{
				error++;
				continue;
			}
			//	Users
			for (int u = 0; u < p_VolumeUser; u++)
			{
				MUser user = new MUser(bp);
				user.setValue(p_Prefix + "_U_" + i + "-" + u);
				user.setValue(p_Prefix + "_User_" + i + "-" + u);
				user.setEMail(p_Prefix + i + u + "@compieretest.com");
				user.setPassword(user.getValue());
				if (!user.save(get_TrxName()))
				{
					error++;
					continue;
				}
				if (p_AD_Role_ID != 0)
				{
					MUserRoles ur = new MUserRoles(getCtx(), user.getAD_User_ID(), p_AD_Role_ID, get_TrxName());
					if (!ur.save())
					{
						error++;
						continue;
					}
				}
			}

			//
			m_bpartners.add(bp);
			count++;
			if (i > 0 && i % COMMITCOUNT == 0)
				commit();
		}
		commit();
		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		String msg = "BPartner #" + count + "(Errors=" + error + ") in " + duration + "s = "
			+ durationMS/count + "ms/BPartner";
		log.info(msg);
		addLog(msg);
	}	//	createBPartners

	/**
	 * 	Create Products
	 */
	private void createProducts(boolean isSOTrx)
	{
		long start = System.currentTimeMillis();
		int count = 0;
		int error = 0;
		BigDecimal factorList = new BigDecimal(1.2);
		BigDecimal factorLimit = new BigDecimal(0.8);
		Random random = new Random();
		//
		for (int i = 0; i < p_VolumeProduct; i++)
		{
			MProduct p = new MProduct(getCtx(), 0, get_TrxName());
			p.setValue (p_Prefix + "_P_" + i);
			p.setName (p_Prefix + "_P_" + i);
			p.setIsStocked(true);
			if (isSOTrx)
				p.setIsSold(true);
			else
				p.setIsPurchased(true);
			//
			if (!p.save())
			{
				error++;
				continue;
			}
			//	Price
			MProductPrice pp = new MProductPrice(getCtx(),
				m_plv.getM_PriceList_Version_ID(),
				p.getM_Product_ID(), get_TrxName());
			double dd = random.nextDouble() * 10;
			BigDecimal price = new BigDecimal(dd);
			pp.setPrices(price.multiply(factorList), price,
				price.multiply(factorLimit));
			if (!pp.save())
			{
				error++;
				continue;
			}
			m_products.add(p);
			count++;
			if (i > 0 && i % COMMITCOUNT == 0)
				commit();
		}
		commit();
		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		String msg = "BProduct #" + count + "(Errors=" + error + ") in " + duration + "s = "
			+ durationMS/count + "ms/Product";
		log.info(msg);
		addLog(msg);
	}	//	createProducts

	/**
	 * 	Create Orders
	 */
	private void createOrders()
	{
		long start = System.currentTimeMillis();

		Random random = new Random();
		int SalesRep_ID = getCtx().getAD_User_ID();
		ArrayList<MBPartner> bpartners = getBPartners();
		if (bpartners == null)
			throw new IllegalArgumentException("No BPartner found");
		int indexBP = 0;
		ArrayList<MProduct> products = getProducts();
		if (products == null)
			throw new IllegalArgumentException("No Product found");
		int indexProduct = 0;
		//
		int countOrder = 0;
		int countLine = 0;
		int errorOrder = 0;
		int errorProcess = 0;
		//	Create
		ArrayList<MOrder> orders = new ArrayList<MOrder>();
		
		for (int i = 0; i < p_VolumeOrder; i++)
		{
			MBPartner bp = bpartners.get(indexBP++);
			if (indexBP >= bpartners.size())
				indexBP = 0;

			MOrder order = new MOrder(getCtx(), 0, get_TrxName());
			order.setDescription(p_Prefix + "_O_" + i );
			order.setC_DocTypeTarget_ID(p_C_DocTypeTarget_ID);
			order.setBPartner(bp);
			order.setSalesRep_ID(SalesRep_ID);
			order.setDeliveryRule(X_C_Order.DELIVERYRULE_Force);
			if (!order.save())
			{
				log.warning("Order #" + i + ": Not saved(1)");
				errorOrder++;
				continue;
			}
			ArrayList<MOrderLine> lines = new ArrayList<MOrderLine>();
			//	Lines
			for (int j = 0; j < p_VolumeLine; j++)
			{
				MProduct prod = products.get(indexProduct++);
				if (indexProduct >= products.size())
					indexProduct = 0;
				MOrderLine line = new MOrderLine(order);
				line.setProduct(prod);
				int qty = random.nextInt(10) + 1;
				line.setQty(new BigDecimal(qty));
				line.setDescription(p_Prefix + "_O_" + i + "_L_" + j);
				line.setC_Tax_ID(C_Tax_ID);
				lines.add(line);
				countLine++;
			}
			if (!PO.saveAll(order.get_Trx(), lines))
			{
				log.warning("#" + i + ": Lines not saved");
				continue;
			}
			orders.add(order);
			countOrder++;
			if (i > 0 && i % COMMITCOUNT == 0)
				commit();
		}

		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		String msg = "Order #" + countOrder + " | " + countLine + " in " + duration + "s = "
			+ durationMS/countOrder + "ms/Order = "
			+ durationMS/countLine + "ms/Line";
		log.info(msg);
		addLog(msg);
		
		//	Process
		if (!Util.isEmpty(p_DocAction))
		{
			int i = 0;
			for( MOrder order : orders ) {
				order.setDocAction(p_DocAction);
				if (!DocumentEngine.processIt(order, p_DocAction))
				{
					log.warning("#" + i + ": Not processed");
					errorProcess++;
					++i;
					continue;
				}
				if (!order.save())
				{
					log.warning("#" + i + ": Not saved(2)");
					errorProcess++;
					++i;
					continue;
				}
				++i;
			}
		}
		commit();
		long end2 = System.currentTimeMillis();
		long durationMS2 = end2 - end;
		long duration2 = durationMS2/1000;
		String msg2 = "DocAction " + p_DocAction + " Order #" + countOrder + " | " + countLine + " in " + duration2 + "s = "
			+ durationMS2/countOrder + "ms/Order = "
			+ durationMS2/countLine + "ms/Line";
		log.info(msg2);
		addLog(msg2);
		String errorMsg = "  #Errors: Order=" + errorOrder + ", Process=" + errorProcess;
		log.info(errorMsg);
		addLog(errorMsg);
	}	//	createOrders


	/**
	 * 	Get Existing BPartners
	 *	@return bpartners
	 */
	private ArrayList<MBPartner> getBPartners()
	{
		if (m_bpartners.size() > 0)
			return m_bpartners;
		String sql = "SELECT * FROM C_BPartner "
			+ "WHERE IsActive='Y' AND (IsCustomer='Y' OR IsVendor='Y') AND AD_Client_ID=?";
        PreparedStatement pstmt = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, get_TrxName());
	        pstmt.setInt(1, getCtx().getAD_Client_ID());
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next())
	        	m_bpartners.add(new MBPartner(getCtx(), rs, null));
	        rs.close();
	        pstmt.close();
	        pstmt = null;
        }
        catch (Exception e)
        {
	        log.log(Level.SEVERE, sql, e);
        }
        try
        {
	        if (pstmt != null)
		        pstmt.close();
	        pstmt = null;
        }
        catch (Exception e)
        {
	        pstmt = null;
        }
        log.info("Loaded #" + m_bpartners.size());
		if (m_bpartners.size() == 0)
			return null;
		return m_bpartners;
	}	//	getBPartners

	/**
	 * 	Get existing Products on price list
	 *	@return products
	 */
	private ArrayList<MProduct> getProducts()
	{
		if (m_products.size() > 0)
			return m_products;
		String sql = "SELECT * FROM M_Product p "
			+ "WHERE IsActive='Y' AND AD_Client_ID=?"
			+ " AND EXISTS (SELECT * FROM M_ProductPrice pp "
				+ "WHERE pp.M_PriceList_Version_ID=? AND pp.M_Product_ID=p.M_Product_ID)";
        PreparedStatement pstmt = null;
        try
        {
	        pstmt = DB.prepareStatement(sql, get_TrxName());
	        pstmt.setInt(1, getCtx().getAD_Client_ID());
	        pstmt.setInt(2, m_plv.getM_PriceList_Version_ID());
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next())
	        	m_products.add(new MProduct(getCtx(), rs, null));
	        rs.close();
	        pstmt.close();
	        pstmt = null;
        }
        catch (Exception e)
        {
	        log.log(Level.SEVERE, sql, e);
        }
        try
        {
	        if (pstmt != null)
		        pstmt.close();
	        pstmt = null;
        }
        catch (Exception e)
        {
	        pstmt = null;
        }
        log.info("Loaded #" + m_products.size());
		if (m_products.size() == 0)
			return null;
		return m_products;
	}	//	getProducts


	/**************************************************************************
	 * 	Volume Test in GardenWorld
	 *	@param args noBPartner noProducts noOrders noLinesPerOrder
	 */
	public static void main(String[] args)
    {
		System.setProperty ("PropertyFile", "/home/namitha/Useful/Compiere.properties");
		Compiere.startup(true);
		CLogMgt.setLoggerLevel(Level.INFO, null);
		CLogMgt.setLevel(Level.INFO);
		//	Same Login entries as entered
		Ini.setProperty(Ini.P_UID, "GardenAdmin");
		Ini.setProperty(Ini.P_PWD, "GardenAdmin");
		Ini.setProperty(Ini.P_ROLE, "GardenWorld Admin");
		Ini.setProperty(Ini.P_CLIENT, "GardenWorld");
		Ini.setProperty(Ini.P_ORG, "HQ");
		Ini.setProperty(Ini.P_WAREHOUSE, "HQ Warehouse");
		Ini.setProperty(Ini.P_LANGUAGE, "English");
		Ctx ctx = Env.getCtx();
		Login login = new Login(ctx);
		if (!login.batchLogin(null, null))
			System.exit(1);

		//	Reduce Log level for performance
		CLogMgt.setLoggerLevel(Level.WARNING, null);
		CLogMgt.setLevel(Level.WARNING);

		//	Data from Login Context
		int AD_Client_ID = ctx.getAD_Client_ID();
		int AD_User_ID = ctx.getAD_User_ID();
		//	Hardcoded
		int AD_Process_ID = 412;
		int AD_Table_ID = 0;
		int Record_ID = 0;

		//	Step 1: Setup Process
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, Record_ID);
		instance.save();
		/* Parameters
		MPInstancePara para1 = new MPInstancePara(instance, 10);
		para1.setParameter("AD_Client_ID", AD_Client_ID);
		para1.save();
		/* */
		// Step 2: Process Info - the parameter for Processes
		ProcessInfo pi = new ProcessInfo("VolumeTest", AD_Process_ID, AD_Table_ID, Record_ID);
		pi.setAD_Client_ID(AD_Client_ID);
		pi.setAD_User_ID(AD_User_ID);
		pi.setIsBatch(false);  //  want to wait for result
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		
		// start logging database updates
		//DB.startLoggingUpdates();
		
		// Step 3: Run the process directly
		VolumeTest test = new VolumeTest();
		/**	Initialize parameter (will be overwritten by parameters above	*/
		if (args.length > 0)
			test.p_VolumeBPartner = PO.convertToInt(args[0]);
		if (args.length > 1)
			test.p_VolumeProduct = PO.convertToInt(args[1]);
		if (args.length > 2)
			test.p_VolumeOrder = PO.convertToInt(args[2]);
		if (args.length > 3)
			test.p_VolumeLine = PO.convertToInt(args[3]);
		test.p_DocAction = X_C_Order.DOCACTION_Complete;
		test.startProcess(ctx, pi, null);

		// Step 4: get results
		if (pi.isError())
			System.err.println("Error: " + pi.getSummary());
		else
			System.out.println("OK: " + pi.getSummary());
		System.out.println(pi.getLogInfo());

		// stop logging database updates
		String logResult = DB.stopLoggingUpdates(0);
		System.out.println(logResult);
		
		
    }	//	main

}	//	VolumeTest
