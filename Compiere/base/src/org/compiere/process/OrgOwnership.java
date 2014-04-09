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
package org.compiere.process;

import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

import org.compiere.util.*;

import com.compiere.client.SysEnv;

/**
 *	Org Ownership Process
 *	
 *  @author Jorg Janke
 *  @version $Id: OrgOwnership.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class OrgOwnership extends SvrProcess
{
	/**	Organization Parameter		*/
	private int		p_AD_Org_ID = -1;
	
	private int		p_M_Warehouse_ID = -1;
	
	private int		p_M_Product_Category_ID = -1;
	private int		p_M_Product_ID = -1;
	
	private int		p_C_BP_Group_ID = -1;
	private int		p_C_BPartner_ID = -1;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("AD_Org_ID"))
				p_AD_Org_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("M_Warehouse_ID"))
				p_M_Warehouse_ID = ((BigDecimal)element.getParameter()).intValue();
				
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("M_Product_ID"))
				p_M_Product_ID = ((BigDecimal)element.getParameter()).intValue();

			else if (name.equals("C_BP_Group_ID"))
				p_C_BP_Group_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = ((BigDecimal)element.getParameter()).intValue();

			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info ("doIt - AD_Org_ID=" + p_AD_Org_ID);
		if (p_AD_Org_ID < 0)
			throw new IllegalArgumentException ("OrgOwnership - invalid AD_Org_ID=" + p_AD_Org_ID);
			
		generalOwnership();	
			
		if (p_M_Warehouse_ID > 0)
			return warehouseOwnership();
			
		if (p_M_Product_ID > 0 || p_M_Product_Category_ID > 0)
			return productOwnership();
			
		if (p_C_BPartner_ID > 0 || p_C_BP_Group_ID > 0)
			return bPartnerOwnership();
		else
			throw new CompiereUserException ("No Business Partner/Group selected");

	}	//	doIt

	/**
	 * 	Set Warehouse Ownership
	 *	@return ""
	 */
	private String warehouseOwnership ()
	{
		log.info ("warehouseOwnership - M_Warehouse_ID=" + p_M_Warehouse_ID);
		if (p_AD_Org_ID == 0)
			throw new IllegalArgumentException ("Warehouse - Org cannot be * (0)");

		//	Set Warehouse
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE M_Warehouse "
			+ " SET AD_Org_ID= ? " 
			+ " WHERE M_Warehouse_ID= ? "
			+ " AND AD_Client_ID= ? "
			+ " AND AD_Org_ID<> ? ");
		int no = DB.executeUpdate(get_TrxName(), sql.toString(),
				p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_Warehouse_ID"));
		
		//	Set Accounts
		sql = new StringBuffer();
		sql.append("UPDATE M_Warehouse_Acct "
			+ " SET AD_Org_ID= ? "
			+ " WHERE M_Warehouse_ID= ? "
			+ " AND AD_Client_ID="
			+ " AND AD_Org_ID<> ? ");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),
				p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));

		//	Set Locators
		sql = new StringBuffer();
		sql.append("UPDATE M_Locator "
			+ " SET AD_Org_ID= ? "
			+ " WHERE M_Warehouse_ID= ? "
			+ " AND AD_Client_ID= ?"
			+ " AND AD_Org_ID<> ?");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),
				p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_Locator_ID"));

		// Update WMS tables if WMS is licensed
		SysEnv se = SysEnv.get("CWMS");
		if (!(se==null) && se.checkLicense())
        {
			//	Set Zone Locators
			sql = new StringBuffer();
			sql.append("UPDATE M_ZoneLocator zl "
				+ " SET AD_Org_ID= ? "
				+ " WHERE EXISTS (SELECT 1 "
				              + " FROM M_Zone z "
				              + " WHERE z.M_Zone_ID = zl.M_Zone_ID "
				              + " AND z.M_Warehouse_ID= ? "
				              + " AND z.AD_Client_ID= ? "
				              + " AND z.AD_Org_ID<> ?)");
			no = DB.executeUpdate(get_TrxName(), sql.toString(),
					p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
			addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_ZoneLocator_ID"));
	
			//	Set Zone Relationship
			sql = new StringBuffer();
			sql.append("UPDATE M_ZoneRelationship zr "
				+ "SET AD_Org_ID= ? "
				+ " WHERE EXISTS (SELECT 1 "
				              + " FROM M_Zone z "
				              + " WHERE z.M_Zone_ID = zr.M_Zone_ID "
				              + " AND z.M_Warehouse_ID= ? "
				              + " AND z.AD_Client_ID= ? "
				              + " AND z.AD_Org_ID<> ? )");
			no = DB.executeUpdate(get_TrxName(), sql.toString(),
					p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
			addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_ZoneRelationship_ID"));
	
			//	Set Zones
			sql = new StringBuffer();
			sql.append("UPDATE M_Zone "
				+ " SET AD_Org_ID= ? "
				+ " WHERE M_Warehouse_ID= ? "
				+ " AND AD_Client_ID= ? "
				+ " AND AD_Org_ID<> ? ");
			no = DB.executeUpdate(get_TrxName(), sql.toString(),
					p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
			addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_Zone_ID"));

			//	Set WMS Rules
			sql = new StringBuffer();
			sql.append("UPDATE M_MMRule "
				+ " SET AD_Org_ID= ? "
				+ " WHERE M_Warehouse_ID= ? "
				+ " AND AD_Client_ID= ? "
				+ " AND AD_Org_ID<> ? ");
			no = DB.executeUpdate(get_TrxName(), sql.toString(),
					p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
			addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_MMRule_ID"));
        
			//	Set Strategy Lines
			sql = new StringBuffer();
			sql.append("UPDATE M_MMStrategyLine sl "
				+ " SET AD_Org_ID= ? "
				+ " WHERE EXISTS (SELECT 1 "
				              + " FROM M_MMStrategy s "
				              + " WHERE s.M_MMStrategy_ID = sl.M_MMStrategy_ID "
				              + " AND s.M_Warehouse_ID= ? "
				              + " AND s.AD_Client_ID= ? "
				              + " AND s.AD_Org_ID<> ? )");
			no = DB.executeUpdate(get_TrxName(), sql.toString(),
					p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
			addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_MMStrategyLine_ID"));

			// Set Strategy
			sql = new StringBuffer();
			sql.append("UPDATE M_MMStrategy "
				+ " SET AD_Org_ID= ? "
				+ " WHERE M_Warehouse_ID= ?"
				+ " AND AD_Client_ID= ? "
				+ " AND AD_Org_ID<> ? ");
			no = DB.executeUpdate(get_TrxName(), sql.toString(),
					p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
			addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_MMStrategy_ID"));

			//	Set Strategy Set Lines
			sql = new StringBuffer();
			sql.append("UPDATE M_MMStrategySetLine sl "
				+ " SET AD_Org_ID= ? "
				+ " WHERE EXISTS (SELECT 1 "
				              + " FROM M_MMStrategySet s "
				              + " WHERE s.M_MMStrategySet_ID = sl.M_MMStrategySet_ID "
				              + " AND s.M_Warehouse_ID= ? "
				              + " AND s.AD_Client_ID= ? "
				              + " AND s.AD_Org_ID<> ? )");
			no = DB.executeUpdate(get_TrxName(), sql.toString(),
					p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
			addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_MMStrategySetLine_ID"));

			// Set Strategy Set
			sql = new StringBuffer();
			sql.append("UPDATE M_MMStrategySet "
				+ " SET AD_Org_ID= ? "
				+ " WHERE M_Warehouse_ID= ? "
				+ " AND AD_Client_ID= ? "
				+ " AND AD_Org_ID<> ? ");
			no = DB.executeUpdate(get_TrxName(), sql.toString(),
					p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
			addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_MMStrategySet_ID"));

        }

		//	Set Storage
		sql = new StringBuffer();
		sql.append("UPDATE M_StorageDetail s "
			+ " SET AD_Org_ID= ? "
			+ " WHERE EXISTS (SELECT 1 "
			              + " FROM M_Locator l "
			              + " WHERE l.M_Locator_ID=s.M_Locator_ID "
				          + " AND l.M_Warehouse_ID= ? ) "
			+ " AND AD_Client_ID= ? "
			+ " AND AD_Org_ID<> ? ");
		no = DB.executeUpdate(get_TrxName(), sql.toString(),
				p_AD_Org_ID,p_M_Warehouse_ID,getAD_Client_ID(),p_AD_Org_ID);
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_Storage"));
			
		return "";
	}	//	warehouseOwnership

	/**
	 * 	Product Ownership
	 *	@return ""
	 */
	private String productOwnership ()
	{
		log.info ("productOwnership - M_Product_Category_ID=" + p_M_Product_Category_ID
			+ ", M_Product_ID=" + p_M_Product_ID);
		List<Object> params = new ArrayList<Object>();
			
		String set = " SET AD_Org_ID= ? ";
		params.add(p_AD_Org_ID);

		if (p_M_Product_Category_ID > 0)
		{
			set += " WHERE EXISTS (SELECT 1 FROM M_Product p"
				+ " WHERE p.M_Product_ID=x.M_Product_ID AND p.M_Product_Category_ID = ? )";
			params.add(p_M_Product_Category_ID);
		}
		else
		{
			set += " WHERE M_Product_ID= ? ";
			params.add(p_M_Product_ID);
		}
		set += " AND AD_Client_ID= ? AND AD_Org_ID<> ? ";
		params.add(getAD_Client_ID());
		params.add(p_AD_Org_ID);
		log.fine("productOwnership - " + set);
		
		//	Product
		String sql = "UPDATE M_Product x " + set;
		int no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_Product_ID"));
		
		//	Acct
		sql = "UPDATE M_Product_Acct x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));
		
		//	Old BOM
		sql = "UPDATE M_Product_BOM x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_Product_BOM_ID"));
		
		//	New BOM
		sql = "UPDATE M_BOM x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_BOM_ID"));

		//	PO
		sql = "UPDATE M_Product_PO x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "Purchase"));

		//	Trl
		sql = "UPDATE M_Product_Trl x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "AD_Language"));
		
		params.clear();
		
		//	New BOM Component
		sql = "UPDATE M_BOMProduct x " +
			  "SET AD_Org_ID= ? " +
			  "WHERE EXISTS (SELECT * FROM M_BOM b, M_Product p " +
			  "WHERE b.M_BOM_ID = x.M_BOM_ID " +
			  "AND b.M_Product_ID = p.M_Product_ID ";
		params.add(p_AD_Org_ID);
		
		if (p_M_Product_Category_ID > 0)
		{
			sql += "AND p.M_Product_Category_ID= ? )";
			params.add(p_M_Product_Category_ID);
		}
		else
		{
			sql += "AND p.M_Product_ID= ? )" ;
			params.add(p_M_Product_ID);
		}

		sql += " AND AD_Client_ID= ?  AND AD_Org_ID<> ? ";
		params.add(getAD_Client_ID());
		params.add(p_AD_Org_ID);
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "M_BOMProduct_ID"));



		return "";
	}	//	productOwnership

	/**
	 * 	Business Partner Ownership
	 *	@return ""
	 */
	private String bPartnerOwnership ()
	{
		log.info ("bPartnerOwnership - C_BP_Group_ID=" + p_C_BP_Group_ID
			+ ", C_BPartner_ID=" + p_C_BPartner_ID);
			
		List<Object> params = new ArrayList<Object>();
		String set = " SET AD_Org_ID= ? " ;
		params.add(p_AD_Org_ID);
		
		if (p_C_BP_Group_ID > 0)
		{
			set += " WHERE EXISTS (SELECT * FROM C_BPartner bp "
				               + " WHERE bp.C_BPartner_ID=x.C_BPartner_ID "
				               + " AND bp.C_BP_Group_ID= ? )" ;
			params.add(p_C_BP_Group_ID);
		}
		else
		{
			set += " WHERE C_BPartner_ID=  ? ";
			params.add(p_C_BPartner_ID);
		}
		set += " AND AD_Client_ID= ? AND AD_Org_ID<> ? " ;
		params.add(getAD_Client_ID());
		params.add(p_AD_Org_ID);
		log.fine("bPartnerOwnership - " + set);

		//	BPartner
		String sql = "UPDATE C_BPartner x " + set;
		int no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "C_BPartner_ID"));
		//	Acct xxx
		sql = "UPDATE C_BP_Customer_Acct x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));
		sql = "UPDATE C_BP_Employee_Acct x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));
		sql = "UPDATE C_BP_Vendor_Acct x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));
		
		//	Location
		sql = "UPDATE C_BPartner_Location x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "C_BPartner_Location_ID"));

		//	Contcat/User
		sql = "UPDATE AD_User x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "AD_User_ID"));
		
		//	BankAcct
		sql = "UPDATE C_BP_BankAccount x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,params.toArray());
		addLog (0,null, new BigDecimal(no), Msg.translate(getCtx(), "C_BP_BankAccount_ID"));

		return "";
	}	//	bPartnerOwnership

	/**
	 * 	Set General Ownership (i.e. Org to 0).
	 * 	In general for items with two parents
	 */
	private void generalOwnership ()
	{
		String set = "SET AD_Org_ID=0 WHERE AD_Client_ID=  ? " 
			+ " AND AD_Org_ID<>0"; 
			
		//	R_ContactInterest
		String sql = "UPDATE R_ContactInterest " + set;
		int no = DB.executeUpdate(get_TrxName(), sql,getAD_Client_ID());
		if (no != 0)
			log.fine("generalOwnership - R_ContactInterest=" + no);

		//	AD_User_Roles
		sql = "UPDATE AD_User_Roles " + set;
		no = DB.executeUpdate(get_TrxName(), sql,getAD_Client_ID());
		if (no != 0)
			log.fine("generalOwnership - AD_User_Roles=" + no);
		
		//	C_BPartner_Product
		sql = "UPDATE C_BPartner_Product " + set;
		no = DB.executeUpdate(get_TrxName(), sql,getAD_Client_ID());
		if (no != 0)
			log.fine("generalOwnership - C_BPartner_Product=" + no);

		//	Withholding
		sql = "UPDATE C_BP_Withholding x " + set;
		no = DB.executeUpdate(get_TrxName(), sql,getAD_Client_ID());
		if (no != 0)
			log.fine("generalOwnership - C_BP_Withholding=" + no);

		//	Costing
		sql = "UPDATE M_Product_Costing " + set;
		no = DB.executeUpdate(get_TrxName(), sql,getAD_Client_ID());
		if (no != 0)
			log.fine("generalOwnership - M_Product_Costing=" + no);

		//	Replenish
		sql = "UPDATE M_Replenish " + set;
		no = DB.executeUpdate(get_TrxName(), sql,getAD_Client_ID());
		if (no != 0)
			log.fine("generalOwnership - M_Replenish=" + no);
	
	}	//	generalOwnership


}	//	OrgOwnership
