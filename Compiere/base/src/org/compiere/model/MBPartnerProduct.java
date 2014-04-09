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

package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Business Partner Product Model
 *	@author Jorg Janke
 */
public class MBPartnerProduct extends X_C_BPartner_Product
{
    /** Logger for class MBPartnerProduct */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MBPartnerProduct.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Get Minimum Guarantee Date for BPartner / Product
	 *	@param ctx context
	 *	@param shipDate date of shipment 
	 *	@param C_BPartner_ID bpartner
	 *	@param M_Product_ID product
	 *	@return Minimum guarantee date
	 */
	public static Timestamp getMinDate(Ctx ctx, Timestamp shipDate, 
			int C_BPartner_ID, int M_Product_ID)
	{
		Timestamp retValue = shipDate;
		//
		String sql = "SELECT bpp.ShelfLifeMinPct,bpp.ShelfLifeMinDays,"	//	1..2
			+ " p.M_Product_ID, p.GuaranteeDays, p.GuaranteeDaysMin,"	//	3..5
			+ " bp.C_BPartner_ID, bp.ShelfLifeMinPct "					//	6..7
			+ "FROM M_Product p"
			+ " INNER JOIN C_BPartner bp ON (bp.C_BPartner_ID=?)"
			+ " LEFT OUTER JOIN C_BPartner_Product bpp ON (p.M_Product_ID=bpp.M_Product_ID"
				+ " AND bp.C_BPartner_ID=bpp.C_BPartner_ID)"
			+ "WHERE p.M_Product_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			pstmt.setInt(1, C_BPartner_ID);
			pstmt.setInt(2, M_Product_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//	Product
				int GuaranteeDays = rs.getInt(4);
				int GuaranteeDaysMin = rs.getInt(5);
				if (GuaranteeDaysMin > 0)
				{
					retValue = TimeUtil.addDays(shipDate, GuaranteeDaysMin);
					log.fine("GuaranteeDaysMin=" + GuaranteeDaysMin 
						+ " - ShipDate=" + shipDate + " -> " + retValue);
				}
				//	Product BPartner
				boolean foundBPP = false;
				BigDecimal ShelfLifeMinPct = rs.getBigDecimal(1);
				if (ShelfLifeMinPct != null && ShelfLifeMinPct.signum() > 0)
				{
					if (GuaranteeDays == 0)
						log.warning("No GuaranteeDays in Product to calculate BPP ShelfLifeMinPct=" + ShelfLifeMinPct);
					else
					{
						int days = GuaranteeDays * 100 / ShelfLifeMinPct.intValue();
						Timestamp newDate = TimeUtil.addDays(shipDate, days);
						log.fine("ShelfLifeMinPct=" + ShelfLifeMinPct
								+ ",GuaranteeDays=" + GuaranteeDays
								+ "->Days=" + days 
								+ " - ShipDate=" + shipDate + " -> " + newDate);
						if (newDate.before(retValue))
							log.fine("BPP ShelfLifeMinPct overwrote previous calculation");
						retValue = newDate;
					}
					foundBPP = true;
				}
				int ShelfLifeMinDays = rs.getInt(2);
				if (ShelfLifeMinDays > 0)
				{
					Timestamp newDate = TimeUtil.addDays(shipDate, ShelfLifeMinDays);
					log.fine("ShelfLifeMinDays=" + ShelfLifeMinDays 
							+ " - ShipDate=" + shipDate + " -> " + newDate);
					if (newDate.before(retValue))
						log.fine("BPP ShelfLifeMinDays overwrote previous calculation");
					retValue = newDate;
					foundBPP = true;
				}
				//	BPartner
				if (!foundBPP)
				{
					ShelfLifeMinPct = rs.getBigDecimal(7);
					if (ShelfLifeMinPct != null && ShelfLifeMinPct.signum() > 0)
					{
						if (GuaranteeDays == 0)
							log.warning("No GuaranteeDays in Product to calculate BP ShelfLifeMinPct=" + ShelfLifeMinPct);
						else
						{
							int days = GuaranteeDays * 100 / ShelfLifeMinPct.intValue();
							Timestamp newDate = TimeUtil.addDays(shipDate, days);
							log.fine("ShelfLifeMinPct=" + ShelfLifeMinPct
								+ ",GuaranteeDays=" + GuaranteeDays
								+ "->Days=" + days 
								+ " - ShipDate=" + shipDate + " -> " + newDate);
							if (newDate.before(retValue))
								log.fine("BP ShelfLifeMinPct overwrote previous calculation");
							retValue = newDate;
						}
					}
				}
			}
			else
				log.warning("No Record Found for C_BPartner_ID=" + C_BPartner_ID 
					+ ",M_Product_ID=" + M_Product_ID);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		return retValue;
	}	//	getMinDate
	
	/**
	public static MBPartnerProduct get(Ctx ctx, int C_BPartner_ID, int M_Product_ID)
	{
		MBPartnerProduct retValue = null;
		
		return retValue;
	}	//	get
	**/
	
	

	/**************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param invalid not used (0 ok) 
	 *	@param trx p_trx
	 */
	public MBPartnerProduct(Ctx ctx, int invalid, Trx trx)
	{
		super(ctx, invalid, trx);
	}	//	MBPartnerProduct

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx p_trx
	 */
	public MBPartnerProduct(Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MBPartnerProduct

}	//	MBPartnerProduct
