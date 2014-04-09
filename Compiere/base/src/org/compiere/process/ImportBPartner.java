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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.compiere.Compiere;
import org.compiere.common.CompiereStateException;
import org.compiere.framework.PO;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MContactInterest;
import org.compiere.model.MLocation;
import org.compiere.model.MPInstance;
import org.compiere.model.MUser;
import org.compiere.model.X_I_BPartner;
import org.compiere.util.CLogMgt;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Ini;
import org.compiere.util.Login;

/**
 *	Import BPartners from I_BPartner
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: ImportBPartner.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class ImportBPartner extends SvrProcess
{
	/**	Client to be imported to		*/
	private int				p_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			p_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		p_DateValue = null;

	private static final String STD_CLIENT_CHECK = " AND AD_Client_ID=? " ;
	
	private static final boolean TESTMODE = false;
	/** Commit every 100 entities	*/
	private static final int	COMMITCOUNT = TESTMODE?100:Integer.parseInt(Ini.getProperty(Ini.P_IMPORT_BATCH_SIZE));

	
	/**
	 *  Prepare - e.g., get Parameters.
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
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				p_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (p_DateValue == null)
			p_DateValue = new Timestamp (System.currentTimeMillis());
	}	//	prepare


	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */
	@Override
	protected String doIt() throws java.lang.Exception
	{
		String sql = null;
		int no = 0;

		//	****	Prepare	****

		//	Delete Old Imported
		if (p_deleteOldImported)
		{
			sql = "DELETE FROM I_BPartner "
				+ "WHERE I_IsImported='Y'" 
				+ STD_CLIENT_CHECK;
			no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
			log.fine("Delete Old Impored =" + no);
		}

		//	Set Client, Org, IsActive, Created/Updated
		sql = "UPDATE I_BPartner "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID,?),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL";
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Reset=" + no);

		//	Set BP_Group
		sql = "UPDATE I_BPartner i "
			+ "SET GroupValue=(SELECT MAX(Value) FROM C_BP_Group g WHERE g.IsDefault='Y'"
			+ " AND g.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE GroupValue IS NULL AND C_BP_Group_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Set Group Default=" + no);
		//
		sql = "UPDATE I_BPartner i "
			+ "SET C_BP_Group_ID=(SELECT C_BP_Group_ID FROM C_BP_Group g"
			+ " WHERE i.GroupValue=g.Value AND g.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE C_BP_Group_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Set Group=" + no);
		//
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = "UPDATE I_BPartner "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Group, ' "
			+ "WHERE C_BP_Group_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.config("Invalid Group=" + no);

		//	Set Country
		/**
		sql = "UPDATE I_BPartner i "
			+ "SET CountryCode=(SELECT CountryCode FROM C_Country c WHERE c.IsDefault='Y'"
			+ " AND c.AD_Client_ID IN (0, i.AD_Client_ID) AND ROWNUM=1) "
			+ "WHERE CountryCode IS NULL AND C_Country_ID IS NULL"
			+ " AND I_IsImported<>'Y'" + STD_CLIENT_CHECK;
		no = DB.executeUpdate(sql, new Object[] {p_AD_Client_ID}, get_Trx());
		log.fine("Set Country Default=" + no);
		**/
		//
		sql = "UPDATE I_BPartner i "
			+ "SET C_Country_ID=(SELECT C_Country_ID FROM C_Country c"
			+ " WHERE i.CountryCode=c.CountryCode AND c.IsSummary='N' AND c.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ "WHERE C_Country_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Set Country=" + no);
		//
		sql = "UPDATE I_BPartner "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Country, ' "
			+ "WHERE C_Country_ID IS NULL AND (City IS NOT NULL OR Address1 IS NOT NULL)"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.config("Invalid Country=" + no);

		//	Set Region
		sql = "UPDATE I_BPartner i "
			+ "Set RegionName=(SELECT Name FROM C_Region r"
			+ " WHERE r.IsDefault='Y' AND r.C_Country_ID=i.C_Country_ID"
			+ " AND r.AD_Client_ID IN (0, i.AD_Client_ID)) " 
			+ "WHERE RegionName IS NULL AND C_Region_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Set Region Default=" + no);
		//
		sql = "UPDATE I_BPartner i "
			+ "Set C_Region_ID=(SELECT C_Region_ID FROM C_Region r"
			+ " WHERE r.Name=i.RegionName AND r.C_Country_ID=i.C_Country_ID"
			+ " AND r.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ "WHERE C_Region_ID IS NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Set Region=" + no);
		//
		sql = "UPDATE I_BPartner i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Region, ' "
			+ "WHERE C_Region_ID IS NULL "
			+ " AND EXISTS (SELECT * FROM C_Country c"
			+ " WHERE c.C_Country_ID=i.C_Country_ID AND c.HasRegion='Y')"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.config("Invalid Region=" + no);

		//	Set Greeting
		sql = "UPDATE I_BPartner i "
			+ "SET C_Greeting_ID=(SELECT C_Greeting_ID FROM C_Greeting g"
			+ " WHERE i.BPContactGreeting=g.Name AND g.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ "WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Set Greeting=" + no);
		//
		sql = "UPDATE I_BPartner i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Greeting, ' "
			+ "WHERE C_Greeting_ID IS NULL AND BPContactGreeting IS NOT NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.config("Invalid Greeting=" + no);

		//	Existing User ?
		sql = "UPDATE I_BPartner i "
			+ "SET (C_BPartner_ID,AD_User_ID)="
				+ "(SELECT u.C_BPartner_ID,u.AD_User_ID "
				+ "FROM AD_User u "
				+ "WHERE i.EMail=u.EMail AND u.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE i.EMail IS NOT NULL AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Found EMail User=" + no);
		
		//		Update Name/Value if missing?
		sql = "UPDATE I_BPartner i "
			+ "SET Value= COALESCE(Email, ContactName)"
			+ "WHERE Value IS NULL AND C_BPartner_ID IS NULL"
			+ " AND (Email IS NOT NULL OR ContactName IS NOT NULL)"
			+ "AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Updated Value =" + no);

		//	Existing BPartner ? Match Value
		sql = "UPDATE I_BPartner i "
			+ "SET (C_BPartner_ID, Name)=(SELECT C_BPartner_ID, Name FROM C_BPartner p"
			+ " WHERE i.Value=p.Value AND p.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE C_BPartner_ID IS NULL AND Value IS NOT NULL"
			+ " AND EXISTS (SELECT 1 FROM C_BPartner p "
			+ " WHERE i.Value=p.Value AND p.AD_Client_ID=i.AD_Client_ID) "
			+ " AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Found BPartner=" + no);


		//	Existing Contact ? Match Name
		sql = "UPDATE I_BPartner i "
			+ "SET AD_User_ID=(SELECT AD_User_ID FROM AD_User c"
			+ " WHERE i.ContactName=c.Name AND i.C_BPartner_ID=c.C_BPartner_ID AND c.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE C_BPartner_ID IS NOT NULL AND AD_User_ID IS NULL AND ContactName IS NOT NULL"
			+ " AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Found Contact=" + no);

		//	Existing Location ? Exact Match
		sql = "UPDATE I_BPartner i "
			+ "SET C_BPartner_Location_ID=(SELECT C_BPartner_Location_ID"
			+ " FROM C_BPartner_Location bpl INNER JOIN C_Location l ON (bpl.C_Location_ID=l.C_Location_ID)"
			+ " WHERE i.C_BPartner_ID=bpl.C_BPartner_ID AND bpl.AD_Client_ID=i.AD_Client_ID"
			+ " AND COALESCE(i.Address1,N' ')=COALESCE(l.Address1,N' ') "
			+ " AND COALESCE(i.Address2,N' ')=COALESCE(l.Address2,N' ')"
			+ " AND COALESCE(i.Address3,N' ')=COALESCE(l.Address3,N' ') "
			+ " AND COALESCE(i.Address4,N' ')=COALESCE(l.Address4,N' ')"
			+ " AND COALESCE(i.City,N' ')=COALESCE(l.City,N' ') "
			+ " AND COALESCE(i.Postal,N' ')=COALESCE(l.Postal,N' ') "
			+ " AND COALESCE(i.Postal_Add,N' ')=COALESCE(l.Postal_Add,N' ')"
			+ " AND COALESCE(i.C_Region_ID,0)=COALESCE(l.C_Region_ID,0)"
			+ " AND COALESCE(i.C_Country_ID,0)=COALESCE(l.C_Country_ID,0)) "
			+ "WHERE C_BPartner_ID IS NOT NULL AND C_BPartner_Location_ID IS NULL"
			+ " AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Found Location=" + no);

		//	Interest Area
		sql = "UPDATE I_BPartner i " 
			+ "SET R_InterestArea_ID=(SELECT R_InterestArea_ID FROM R_InterestArea ia "
				+ "WHERE i.InterestAreaName=ia.Name AND ia.AD_Client_ID=i.AD_Client_ID) "
			+ "WHERE R_InterestArea_ID IS NULL AND InterestAreaName IS NOT NULL"
			+ " AND I_IsImported='N'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Set Interest Area=" + no);

		//	Set Consolidation Ref
		sql = "UPDATE I_BPartner i "
			+ "SET C_ConsolidationReference_ID=(SELECT c.C_ConsolidationReference_ID "
			+ " FROM C_ConsolidationReference c"
			+ " WHERE c.Name=i.ConsolidationRefName AND c.AD_Client_ID IN (0, i.AD_Client_ID)) "
			+ "WHERE i.C_ConsolidationReference_ID IS NULL AND i.ConsolidationRefName IS NOT NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.fine("Set Consolidation Reference =" + no);
		//
		sql = "UPDATE I_BPartner i "
			+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Consolidation Reference, ' "
			+ "WHERE C_ConsolidationReference_ID IS NULL AND ConsolidationRefName IS NOT NULL"
			+ " AND I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.config("Invalid Consolidation Reference=" + no);

		//		Update Name/Value if missing?
		sql = "UPDATE I_BPartner i "
				+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||'ERR=Invalid Value/Name ' "
				+ "WHERE Value IS NULL AND C_BPartner_ID IS NULL"
				+ " AND I_IsImported<>'Y'" 
				+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql, new Object[] {p_AD_Client_ID});
		log.config("Invalid Value/Name=" + no);


		commit();
		//	-------------------------------------------------------------------
		int noInsert = 0;
		int noUpdate = 0;
		boolean repeatChecks = false;

		/****List of Import Records */
		List<X_I_BPartner> ImpBPartner = new ArrayList<X_I_BPartner>(); 
		/***Map of import records for BP Value */
		Map<String,List<X_I_BPartner>> importPartnerMap = new HashMap<String,List<X_I_BPartner>>();
		/*** Map of BP objects for BP Value */
		Map<String,MBPartner> bpartnerMap = new HashMap<String,MBPartner>();
		/*** Map of List of Locations for BP Value */
		Map<String,List<MLocation>> locationMap = new HashMap<String,List<MLocation>>();
		/*** Map of Location for Import Business Partner */
		Map<Integer,MLocation> locationMapForImpBP = new HashMap<Integer,MLocation>();
		/*** Map of List of Users for BP Value */
		Map<String,List<MUser>> userMap = new HashMap<String, List<MUser>>();
		/*** Map of List of Users for Import Business partner */
		Map<Integer,MUser> userMapForImpBP = new HashMap<Integer, MUser>();
		
		//	Go through Records
		sql = "SELECT * FROM I_BPartner "
			+ "WHERE I_IsImported='N' " 
			+ STD_CLIENT_CHECK
			+ " ORDER BY Value, I_BPartner_ID ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			pstmt.setInt(1, p_AD_Client_ID);
			rs = pstmt.executeQuery();
			MBPartner oldBP = null;
			while (rs.next())
			{
				X_I_BPartner impBP = new X_I_BPartner (getCtx(), rs, get_Trx());
				log.fine("I_BPartner_ID=" + impBP.getI_BPartner_ID()
					+ ", C_BPartner_ID=" + impBP.getC_BPartner_ID()
					+ ", C_BPartner_Location_ID=" + impBP.getC_BPartner_Location_ID()
					+ ", AD_User_ID=" + impBP.getAD_User_ID());

				//	****	Create/Update BPartner	****
				MBPartner bp = null;
				List<X_I_BPartner> importPartnerList = null;
				importPartnerList = importPartnerMap.get(impBP.getValue());
				if(importPartnerList == null)
				{
					importPartnerList = new ArrayList<X_I_BPartner>();
					importPartnerMap.put(impBP.getValue(),importPartnerList);
				}
				
				//Check for duplicate
				if(oldBP!=null && oldBP.getValue().equals(impBP.getValue()))
				{
					bp=oldBP;
					repeatChecks = true;
				}
				
				if (impBP.getC_BPartner_ID()== 0 && bp == null)	//	Insert new BPartner
				{
					if(bpartnerMap.size()>COMMITCOUNT)
					{
						saveBPartner(bpartnerMap,importPartnerMap,ImpBPartner,
								     locationMap,locationMapForImpBP,userMap,userMapForImpBP);
						ImpBPartner.clear();
						importPartnerMap.clear();
						bpartnerMap.clear();
						locationMap.clear();
						locationMapForImpBP.clear();
						userMap.clear();
						userMapForImpBP.clear();
					}
					
					bp = new MBPartner(impBP);
					if(impBP.getIsCustomer() != null)
						bp.setIsCustomer(impBP.getIsCustomer().equals("Y"));
					
					if(impBP.getIsVendor() != null)
						bp.setIsVendor(impBP.getIsVendor().equals("Y"));
					
					if(impBP.getIsEmployee() != null)
						bp.setIsEmployee(impBP.getIsEmployee().equals("Y"));
					bpartnerMap.put(impBP.getValue(),bp);
					noInsert++;
				}
				else				//	Update existing BPartner
				{
					if(bp == null)
						bp = new MBPartner(getCtx(), impBP.getC_BPartner_ID(), get_Trx());

					if (impBP.getName() != null)
					{
						bp.setName(impBP.getName());
						bp.setName2(impBP.getName2());
					}
					if (impBP.getDUNS() != null)
						bp.setDUNS(impBP.getDUNS());
					if (impBP.getTaxID() != null)
						bp.setTaxID(impBP.getTaxID());
					if (impBP.getNAICS() != null)
						bp.setNAICS(impBP.getNAICS());
					if (impBP.getC_BP_Group_ID() != 0)
						bp.setC_BP_Group_ID(impBP.getC_BP_Group_ID());
					if (impBP.getDescription() != null)
						bp.setDescription(impBP.getDescription());
					if(impBP.getIsCustomer()!= null)
						bp.setIsCustomer(impBP.getIsCustomer().equals("Y"));
					if(impBP.getIsVendor() != null)
						bp.setIsVendor(impBP.getIsVendor().equals("Y"));
					if(impBP.getIsEmployee() != null)
						bp.setIsEmployee(impBP.getIsEmployee().equals("Y"));
					bp.setC_ConsolidationReference_ID(impBP.getC_ConsolidationReference_ID());
					
					bpartnerMap.put(impBP.getValue(),bp);
					noUpdate++;
				}
				ImpBPartner.add(impBP);
				importPartnerList.add(impBP);
				oldBP=bp;

				// Location
				MBPartnerLocation bpl = null;
				List<MLocation> locationList = null;
				MLocation location = null;
	
				locationList = locationMap.get(impBP.getValue());
				if(locationList==null)
				{
					locationList = new ArrayList<MLocation>();
					locationMap.put(impBP.getValue(),locationList);
				}

				//Update Location if Business Partner location is entered
				if (impBP.getC_BPartner_Location_ID() != 0)		
				{
					bpl = new MBPartnerLocation(getCtx(), impBP.getC_BPartner_Location_ID(), get_Trx());
					for(MLocation lo : locationList)
					{
						/*  this will work because if the location is there in the list 
							it's Location ID will be stamped */
						if(lo.getC_Location_ID() == bpl.getC_Location_ID()) 
						{
							location = lo;
							break;
						}
					}
					if(location==null)
					{
						location = new MLocation(getCtx(), bpl.getC_Location_ID(), get_Trx());
						locationList.add(location);
						locationMapForImpBP.put(impBP.getI_BPartner_ID(),location);
					}
					//Update Location details.
					location.setC_Country_ID(impBP.getC_Country_ID());
					location.setC_Region_ID(impBP.getC_Region_ID());
					location.setCity(impBP.getCity());
					location.setAddress1(impBP.getAddress1());
					location.setAddress2(impBP.getAddress2());
					location.setAddress3(impBP.getAddress3());
					location.setAddress4(impBP.getAddress4());
					location.setPostal(impBP.getPostal());
					location.setPostal_Add(impBP.getPostal_Add());
					location.setRegionName(impBP.getRegionName());

				}
				else if	(impBP.getC_Country_ID() != 0 && 
						( impBP.getAddress1() != null 
								|| impBP.getAddress2() != null
							    || impBP.getAddress3() != null 
							    || impBP.getAddress4() != null
					            || impBP.getPostal() != null 
					            || impBP.getCity() != null))
				{
					if(repeatChecks)
					{
						locationList = locationMap.get(impBP.getValue());	
						for(MLocation loc1 : locationList)
						{
							if(loc1.equals(impBP.getC_Country_ID(), impBP.getC_Region_ID(), impBP.getPostal(),
									impBP.getPostal_Add(), impBP.getCity(), impBP.getAddress1(), 
									impBP.getAddress2()))
							{
								location = loc1;
								locationMapForImpBP.put(impBP.getI_BPartner_ID(), location);
							}
						}
					}
					if(location==null)
					{
						location = new MLocation(getCtx(), impBP.getC_Country_ID(), 
							impBP.getC_Region_ID(), impBP.getCity(), get_Trx());
						location.setAddress1(impBP.getAddress1());
						location.setAddress2(impBP.getAddress2());
						location.setAddress3(impBP.getAddress3());
						location.setAddress4(impBP.getAddress4());
						location.setPostal(impBP.getPostal());
						location.setPostal_Add(impBP.getPostal_Add());
						location.setRegionName(impBP.getRegionName());
						locationList.add(location);
						locationMapForImpBP.put(impBP.getI_BPartner_ID(),location);
					}
				}
			
				//	****	Create/Update Contact	****				
				List<MUser> userList = null;
				MUser user = null;
				userList = userMap.get(impBP.getValue());
				if(userList == null)
				{
					userList = new ArrayList<MUser>();
					userMap.put(impBP.getValue(),userList);
				}
				
				if (impBP.getAD_User_ID() != 0)
				{
					for(MUser u :userList)
					{
						if(u.getAD_User_ID()==impBP.getAD_User_ID())
						{
							user = u;
							break;
						}
					}
					if(user==null)
					{
						user = new MUser (getCtx(), impBP.getAD_User_ID(), get_Trx());
						userList.add(user);
						userMapForImpBP.put(impBP.getI_BPartner_ID(),user);
					}
					if (user.getC_BPartner_ID() != bp.getC_BPartner_ID())
					{
						sql = "UPDATE I_BPartner i "
							+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||"
							+ "'BP of User <> BP, ' "
							+ "WHERE I_BPartner_ID=? ";
						DB.executeUpdate(get_Trx(), sql.toString(), new Object[]{impBP.getI_BPartner_ID()});
						importPartnerList.remove(impBP);
						ImpBPartner.remove(impBP);
						locationList.remove(location);
						locationMapForImpBP.remove(impBP.getI_BPartner_ID());
						continue;
					}
					if (impBP.getC_Greeting_ID() != 0)
						user.setC_Greeting_ID(impBP.getC_Greeting_ID());
					String name = impBP.getContactName();
					if (name == null || name.length() == 0)
						name = impBP.getEMail();
					user.setName(name);
					if (impBP.getTitle() != null)
						user.setTitle(impBP.getTitle());
					if (impBP.getContactDescription() != null)
						user.setDescription(impBP.getContactDescription());
					if (impBP.getComments() != null)
						user.setComments(impBP.getComments());
					if (impBP.getPhone() != null)
						user.setPhone(impBP.getPhone());
					if (impBP.getPhone2() != null)
						user.setPhone2(impBP.getPhone2());
					if (impBP.getFax() != null)
						user.setFax(impBP.getFax());
					if (impBP.getEMail() != null)
						user.setEMail(impBP.getEMail());
					if (impBP.getBirthday() != null)
						user.setBirthday(impBP.getBirthday());
					if (bpl != null)
						user.setC_BPartner_Location_ID(bpl.getC_BPartner_Location_ID());
				}
				else 	//	New Contact
				if (impBP.getContactName() != null || impBP.getEMail() != null)
				{
					if(repeatChecks)
					{
						for(MUser u : userList)
						{
							if(u.equals(impBP.getContactName(),impBP.getEMail()))
							{
								user = u;
								userMapForImpBP.put(impBP.getI_BPartner_ID(),user);
							}
						}
					}
					
					if(user==null)
					{
						user = MUser.getUser(getCtx(),impBP.getEMail(),impBP.getContactName(),get_Trx());
						if(user!=null)
						{	userList.add(user);
							userMapForImpBP.put(impBP.getI_BPartner_ID(),user);
						}
					}

					if(user==null)
					{
						user = new MUser (bp);
						userList.add(user);
						userMapForImpBP.put(impBP.getI_BPartner_ID(),user);
					}
					if (impBP.getC_Greeting_ID() != 0)
						user.setC_Greeting_ID(impBP.getC_Greeting_ID());
					String name = impBP.getContactName();
					if (name == null || name.length() == 0)
						name = impBP.getEMail();
					user.setName(name);
					user.setTitle(impBP.getTitle());
					user.setDescription(impBP.getContactDescription());
					user.setComments(impBP.getComments());
					user.setPhone(impBP.getPhone());
					user.setPhone2(impBP.getPhone2());
					user.setFax(impBP.getFax());
					user.setEMail(impBP.getEMail());
					user.setBirthday(impBP.getBirthday());
				}
			}
			rs.close();
			pstmt.close();
		}
		catch (Exception e)	
		{
			log.log(Level.SEVERE, "Business Partner - " + sql.toString(), e);
		}
		finally 
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		saveBPartner(bpartnerMap, importPartnerMap, ImpBPartner,locationMap,
				     locationMapForImpBP, userMap,userMapForImpBP);

		// Create update the accounting records for the business partners
		updateAccounting(getAD_User_ID());
		insertAccounting();
		
		sql = "UPDATE I_BPartner imp "
			+ "SET I_IsImported='Y', Processed='Y' "
			+ "WHERE I_IsImported='N'" 
			+ "AND C_BPartner_ID IS NOT NULL "
			+ "AND EXISTS (SELECT 1 FROM C_BPartner bp "
			           + " WHERE bp.C_BPartner_ID = imp.C_BPartner_ID "
			           + " AND bp.AD_Client_ID = imp.AD_Client_ID )"
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(),new Object[]{p_AD_Client_ID});

		//	Set Error to indicator to not imported
		sql = "UPDATE I_BPartner "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'" 
			+ STD_CLIENT_CHECK;
		no = DB.executeUpdate(get_Trx(), sql.toString(),new Object[]{p_AD_Client_ID});
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@C_BPartner_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@C_BPartner_ID@: @Updated@");
		return "";
				
	}	//	doIt
	
	public void saveBPartner(Map<String,MBPartner> bpartnerMap, 
			                 Map<String,List<X_I_BPartner>> importPartnerMap, 
			                 List<X_I_BPartner> impBPartner,
							 Map<String,List<MLocation>> locationMap, 
							 Map<Integer,MLocation> locationMapForImpBP,
							 Map<String,List<MUser>> userMap,
							 Map<Integer,MUser> userMapForImpBP) throws Exception
	{
		List<MBPartner> bpartnersToSave = new ArrayList<MBPartner>(bpartnerMap.values());
		if(!PO.saveAll(get_Trx(), bpartnersToSave))
			throw new CompiereStateException("Unable to Save Business Partner");
		
		List<MLocation> mlocationsToSave = new ArrayList<MLocation>();
		for(List<MLocation> locations : locationMap.values())
			mlocationsToSave.addAll(locations);
		
		if(!PO.saveAll(get_Trx(),mlocationsToSave)) 
			throw new CompiereStateException("Could not save Locations");

		List <MBPartnerLocation> bplocationsToSave = new ArrayList<MBPartnerLocation> ();
		Map <Integer,MBPartnerLocation> bplocationMap = new HashMap<Integer,MBPartnerLocation>();
		for(X_I_BPartner impBP : impBPartner)
		{
			MLocation location = locationMapForImpBP.get(impBP.getI_BPartner_ID());
			MBPartner bp = bpartnerMap.get(impBP.getValue());
			MBPartnerLocation bpl=null;
			if(impBP.getC_BPartner_Location_ID() !=0)
			{
				bpl = new MBPartnerLocation(getCtx(),impBP.getC_BPartner_Location_ID(),get_Trx());
				if (impBP.getBPLPhone() != null)
					bpl.setPhone(impBP.getBPLPhone());
				if (impBP.getBPLPhone2() != null)
					bpl.setPhone2(impBP.getBPLPhone2());
				if (impBP.getBPLFax() != null)
					bpl.setFax(impBP.getBPLFax());
				if(impBP.getIsBillTo() != null)
					bpl.setIsBillTo(impBP.getIsBillTo().equals("Y"));
				if(impBP.getIsPayFrom() != null)
					bpl.setIsPayFrom(impBP.getIsPayFrom().equals("Y"));
				if(impBP.getIsShipTo() != null)
					bpl.setIsShipTo(impBP.getIsShipTo().equals("Y"));
				if(impBP.getIsRemitTo() != null)
					bpl.setIsRemitTo(impBP.getIsRemitTo().equals("Y"));
				bpl.setC_Location_ID(location.getC_Location_ID());
				bpl.setC_BPartner_ID(bp.getC_BPartner_ID());
				bplocationsToSave.add(bpl);
				bplocationMap.put(impBP.getI_BPartner_ID(),bpl);
			}
			else
			{
				bpl = new MBPartnerLocation(bp);
				bpl.setPhone(impBP.getBPLPhone());
				bpl.setPhone2(impBP.getBPLPhone2());
				bpl.setFax(impBP.getBPLFax());
				if(impBP.getIsBillTo() != null)
					bpl.setIsBillTo(impBP.getIsBillTo().equals("Y"));
				if(impBP.getIsPayFrom() != null)
					bpl.setIsPayFrom(impBP.getIsPayFrom().equals("Y"));
				if(impBP.getIsShipTo() != null)
					bpl.setIsShipTo(impBP.getIsShipTo().equals("Y"));
				if(impBP.getIsRemitTo() != null)
					bpl.setIsRemitTo(impBP.getIsRemitTo().equals("Y"));
				bpl.setC_Location_ID(location.getC_Location_ID());
				bplocationsToSave.add(bpl);
				bplocationMap.put(impBP.getI_BPartner_ID(),bpl);
			}
		}

		if(!PO.saveAll(get_Trx(),bplocationsToSave)) 
			throw new CompiereStateException("Could not save Business Partner Location");
		
		List <MUser> usersToSave = new ArrayList<MUser>();
		for(Map.Entry<String, List<MUser>> entry : userMap.entrySet()) 
		{
			MBPartner bp = bpartnerMap.get(entry.getKey());
			for(MUser user : entry.getValue()) 
			{
				user.setC_BPartner_ID(bp.getC_BPartner_ID());
				usersToSave.add(user);
			}
		}

		if(!PO.saveAll(get_Trx(), usersToSave)) 
			throw new CompiereStateException("Could not save Users");
		
		List<MContactInterest> interestsToSave = new ArrayList<MContactInterest>();
		for(X_I_BPartner impBP : impBPartner)
		{
			MUser user = userMapForImpBP.get(impBP.getI_BPartner_ID());
			if(impBP.getR_InterestArea_ID() !=0 && user != null)
			{
				MContactInterest ci = MContactInterest.get(getCtx(),
						impBP.getR_InterestArea_ID(),
						user.getAD_User_ID(), true, get_Trx());
				interestsToSave.add(ci);
			}
		}

		if(!PO.saveAll(get_Trx(),interestsToSave))
			throw new CompiereStateException("Could not save Contact Interests");

		for(X_I_BPartner imp : impBPartner)
		{
			MBPartner bp = bpartnerMap.get(imp.getValue());
			imp.setC_BPartner_ID(bp.getC_BPartner_ID());
		}
		
		if(!PO.saveAll(get_Trx(),impBPartner))
			throw new CompiereStateException("Could not save Import Business Partner Records");
		commit();
	}

	public static void main(String[] args)
    {
		System.setProperty ("PropertyFile", "C:\\Documents and Settings\\admin\\Compiere.properties");
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
		Ini.setProperty(Ini.P_IMPORT_BATCH_SIZE, "100");
		
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
		int AD_Process_ID = 194;
		int AD_Table_ID = 0;
		int Record_ID = 0;

		//	Step 1: Setup Process
		MPInstance instance = new MPInstance(Env.getCtx(), AD_Process_ID, Record_ID);
		instance.save();
		
		ProcessInfo pi = new ProcessInfo("Import", AD_Process_ID, AD_Table_ID, Record_ID);
		pi.setAD_Client_ID(AD_Client_ID);
		pi.setAD_User_ID(AD_User_ID);
		pi.setIsBatch(false);  //  want to wait for result
		pi.setAD_PInstance_ID (instance.getAD_PInstance_ID());

		DB.startLoggingUpdates();
		
		// Step 3: Run the process directly
		ImportBPartner test = new ImportBPartner();
		test.p_AD_Client_ID = ctx.getAD_Client_ID();
		test.p_deleteOldImported = true;
		
		long start = System.currentTimeMillis();
		
		test.startProcess(ctx, pi, null);
		
		long end = System.currentTimeMillis();
		long durationMS = end - start;
		long duration = durationMS/1000;
		System.out.println("Total: " + duration + "s");

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
	
	private void insertAccounting()
	{
		String sql = " INSERT INTO C_BP_Customer_Acct (C_BPartner_ID, C_AcctSchema_ID, AD_Client_ID, "
			       + " AD_Org_ID,IsActive, Created,CreatedBy,Updated,UpdatedBy ,C_Prepayment_Acct, "
			       + " C_Receivable_Acct,C_Receivable_Services_Acct) " 
		           + " SELECT a.C_BPartner_ID, p.C_AcctSchema_ID, p.AD_Client_ID,0,'Y', SysDate,0,SysDate,0, "
		           + " p.C_Prepayment_Acct,p.C_Receivable_Acct,p.C_Receivable_Services_Acct " 
		           + " FROM C_BPartner a, " 
		                + " C_BP_Group_Acct p, "
		                + " I_BPartner imp " 
		           + " WHERE p.AD_Client_ID=? " 
		           + " AND  a.C_BP_Group_ID = p.C_BP_Group_ID "  
		           + " AND imp.AD_Client_ID = p.AD_Client_ID "
		           + " AND imp.I_IsImported = 'N' "
		           + " AND a.C_BPartner_ID = imp.C_BPartner_ID "
		           + " AND NOT EXISTS (SELECT 1 FROM C_BP_Customer_Acct acct " 
		                           + " WHERE acct.C_AcctSchema_ID = p.C_AcctSchema_ID "
		                           + " AND acct.C_BPartner_ID = a.C_BPartner_ID "
		                           + " AND   acct.AD_Client_ID = p.AD_Client_ID) ";
		int no = DB.executeUpdate(get_Trx(), sql,new Object[]{p_AD_Client_ID});
		if(no<0)
			throw new CompiereStateException("Could not Create Accounting records in C_BP_Customer_Acct");
		
		sql =    " INSERT INTO C_BP_Vendor_Acct (C_BPartner_ID, C_AcctSchema_ID, AD_Client_ID,AD_Org_ID,IsActive, "
			   + " Created,CreatedBy,Updated,UpdatedBy ,V_Liability_Acct,V_Liability_Services_Acct, "
			   + " V_Prepayment_Acct) "
	           + " SELECT a.C_BPartner_ID, p.C_AcctSchema_ID, p.AD_Client_ID,0,'Y', SysDate, "
	           + " 0,SysDate,0,p.V_Liability_Acct,p.V_Liability_Services_Acct,p.V_Prepayment_Acct "
	           + " FROM C_BPartner a, " 
	                + " C_BP_Group_Acct p, "
	                + " I_BPartner imp " 
	           + " WHERE p.AD_Client_ID=? " 
	           + " AND  a.C_BP_Group_ID = p.C_BP_Group_ID "  
	           + " AND imp.AD_Client_ID = p.AD_Client_ID "
	           + " AND imp.I_IsImported = 'N' "
	           + " AND a.C_BPartner_ID = imp.C_BPartner_ID "
	           + " AND NOT EXISTS (SELECT 1 FROM C_BP_Vendor_Acct acct " 
	                           + " WHERE acct.C_AcctSchema_ID = p.C_AcctSchema_ID "
	                           + " AND acct.C_BPartner_ID = a.C_BPartner_ID "
	                           + " AND   acct.AD_Client_ID = p.AD_Client_ID) ";
		no = DB.executeUpdate(get_Trx(), sql,new Object[]{p_AD_Client_ID});
		if(no<0)
			throw new CompiereStateException("Could not Create Accounting records in C_BP_Vendor_Acct");
		
		sql =    " INSERT INTO C_BP_Employee_Acct (C_BPartner_ID, C_AcctSchema_ID, AD_Client_ID,AD_Org_ID, "
			   + " IsActive, Created,CreatedBy,Updated,UpdatedBy ,E_Expense_Acct,E_Prepayment_Acct)  "
	           + " SELECT a.C_BPartner_ID, p.C_AcctSchema_ID, p.AD_Client_ID,0,'Y', SysDate,0,SysDate,0, "
	           + " p.E_Expense_Acct,p.E_Prepayment_Acct  "
	           + " FROM C_BPartner a, " 
	                + " C_AcctSchema_Default p, "
	                + " I_BPartner imp " 
	           + " WHERE p.AD_Client_ID=? " 
	           + " AND imp.AD_Client_ID = p.AD_Client_ID "
	           + " AND imp.I_IsImported = 'N' "
	           + " AND a.C_BPartner_ID = imp.C_BPartner_ID "
	           + " AND NOT EXISTS (SELECT 1 FROM C_BP_Employee_Acct acct " 
	                           + " WHERE acct.C_AcctSchema_ID = p.C_AcctSchema_ID "
	                           + " AND acct.C_BPartner_ID = a.C_BPartner_ID "
	                           + " AND   acct.AD_Client_ID = p.AD_Client_ID) ";
		no = DB.executeUpdate(get_Trx(), sql,new Object[]{p_AD_Client_ID});
		if(no<0)
			throw new CompiereStateException("Could not Create Accounting records in C_BP_Vendor_Acct");
	
	}
	
	private void updateAccounting(int updatedBy)
	{
		String sql = " UPDATE C_BP_Customer_Acct acct  "
			       + " SET ( Updated,C_Prepayment_Acct,C_Receivable_Acct,C_Receivable_Services_Acct) = "
			       + " (SELECT  SysDate,p.C_Prepayment_Acct,p.C_Receivable_Acct,p.C_Receivable_Services_Acct " 
					+ " FROM C_BPartner a, C_BP_Group_Acct p " 
					+ " WHERE p.AD_Client_ID= acct.AD_Client_ID " 
					+ " AND p.C_AcctSchema_ID = acct.C_AcctSchema_ID " 
					+ " AND  a.C_BP_Group_ID = p.C_BP_Group_ID " 
					+ " AND acct.C_BPartner_ID = a.C_BPartner_ID), "
					+ " UpdatedBy = ? "
				   + " WHERE EXISTS (SELECT 1 FROM C_BP_Group_Acct acct1, C_BPartner bp, I_BPartner imp "
					             + " WHERE acct1.AD_Client_ID = acct.AD_Client_ID "
					             + " AND   acct1.C_AcctSchema_ID = acct.C_AcctSchema_ID "
					             + " AND   acct1.C_BP_Group_ID = bp.C_BP_Group_ID "
					             + " AND   bp.C_BPartner_ID = acct.C_BPartner_ID"
					             + " AND   bp.C_BPartner_ID = imp.C_BPartner_ID "
					             + " AND   imp.I_IsImported = 'N' "
					             + " AND   imp.AD_Client_ID = acct.AD_Client_ID) ";
		int no = DB.executeUpdate(get_Trx(), sql,new Object[]{updatedBy});
		if(no<0)
			throw new CompiereStateException("Could not update Accounting records in C_BP_Customer_Acct");

		sql =    " UPDATE C_BP_Vendor_Acct acct  "
		       + " SET ( Updated,V_Liability_Acct,V_Liability_Services_Acct,V_Prepayment_Acct) = "
		       + " (SELECT  SysDate,p.V_Liability_Acct,p.V_Liability_Services_Acct,p.V_Prepayment_Acct " 
				+ " FROM C_BPartner a, C_BP_Group_Acct p " 
				+ " WHERE p.AD_Client_ID= acct.AD_Client_ID " 
				+ " AND p.C_AcctSchema_ID = acct.C_AcctSchema_ID " 
				+ " AND  a.C_BP_Group_ID = p.C_BP_Group_ID " 
				+ " AND acct.C_BPartner_ID = a.C_BPartner_ID), "
				+ " UpdatedBy = ? "
			   + " WHERE EXISTS (SELECT 1 FROM C_BP_Group_Acct acct1, C_BPartner bp, I_BPartner imp "
				             + " WHERE acct1.AD_Client_ID = acct.AD_Client_ID "
				             + " AND   acct1.C_AcctSchema_ID = acct.C_AcctSchema_ID "
				             + " AND   acct1.C_BP_Group_ID = bp.C_BP_Group_ID "
				             + " AND   bp.C_BPartner_ID = imp.C_BPartner_ID "
				             + " AND   bp.C_BPartner_ID = acct.C_BPartner_ID"
				             + " AND   imp.I_IsImported = 'N' "
				             + " AND   imp.AD_Client_ID = acct.AD_Client_ID) ";
		no = DB.executeUpdate(get_Trx(), sql,new Object[]{updatedBy});
		if(no<0)
			throw new CompiereStateException("Could not update Accounting records in C_BP_Vendor_Acct");

		sql =    " UPDATE C_BP_Employee_Acct acct  "
		       + " SET ( Updated ,E_Expense_Acct,E_Prepayment_Acct) = "
		       + " (SELECT  SysDate,p.E_Expense_Acct,p.E_Prepayment_Acct " 
				+ " FROM C_BPartner a, C_AcctSchema_Default p " 
				+ " WHERE p.AD_Client_ID= acct.AD_Client_ID " 
				+ " AND p.C_AcctSchema_ID = acct.C_AcctSchema_ID " 
				+ " AND acct.C_BPartner_ID = a.C_BPartner_ID), "
				+ " UpdatedBy = ? "
			   + " WHERE EXISTS (SELECT 1 FROM C_AcctSchema_Default acct1, I_BPartner imp "
				             + " WHERE acct1.AD_Client_ID = acct.AD_Client_ID "
				             + " AND   acct1.C_AcctSchema_ID = acct.C_AcctSchema_ID "
				             + " AND   acct.C_BPartner_ID = imp.C_BPartner_ID "
				             + " AND   imp.C_BPartner_ID = acct.C_BPartner_ID "
				             + " AND   imp.I_IsImported = 'N' "
				             + " AND   imp.AD_Client_ID = acct.AD_Client_ID) ";
		no = DB.executeUpdate(get_Trx(), sql,new Object[]{updatedBy});
		if(no<0)
			throw new CompiereStateException("Could not update Accounting records in C_BP_Employee_Acct");
	}
	
}	//	ImportBPartner

