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

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 * 	Entity Type Model
 *
 *  @author Jorg Janke
 *  @version $Id: MEntityType.java 8780 2010-05-19 23:08:57Z nnayak $
 */
public class MEntityType extends X_AD_EntityType
{
    /** Logger for class MEntityType */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MEntityType.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Get Entity Type
	 * @param ctx
	 * @param entityType entity type
	 * @return entity type
	 */
	static public MEntityType getEntityType(Ctx ctx, String entityType)
	{
		String sql = "SELECT * FROM AD_EntityType WHERE IsActive='Y' AND EntityType = '" + entityType + "'";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MEntityType mEntityType = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				mEntityType = new MEntityType (ctx, rs, null);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return mEntityType;
	}  // getEntityType()

	/**
	 * Get Entity Type
	 * @param ctx
	 * @param adEntityTypeId AD_EntityType_ID
	 * @return entity type or null
	 */
	static public MEntityType getEntityType(Ctx ctx, int adEntityTypeId)
	{
		String sql = "SELECT * FROM AD_EntityType WHERE IsActive='Y' AND AD_EntityType_ID = " + adEntityTypeId;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MEntityType mEntityType = null;
		try
		{
			pstmt = DB.prepareStatement(sql, (Trx) null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
				mEntityType = new MEntityType (ctx, rs, null);
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}

		return mEntityType;
	}  // getEntityType()

	/**
	 * 	Get Entity Types
	 * 	@param ctx context
	 * 	@param refresh refresh
	 *	@return entity type array
	 */
	static public MEntityType[] getEntityTypes(Ctx ctx, boolean refresh)
	{
		if ((s_entityTypes != null) && !refresh)
			return s_entityTypes;
		ArrayList<MEntityType> list = new ArrayList<MEntityType>();
		String sql = "SELECT * FROM AD_EntityType WHERE IsActive='Y' ORDER BY RelativeLoadNo";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Trx trx = Trx.get("Entity Type");
		
		try
		{
			pstmt = DB.prepareStatement(sql, trx);
			rs = pstmt.executeQuery();
		}
		catch (Exception e)
		{
			//	Probable cause: RelativeLoadNo not defined
			try
			{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);

				sql = "SELECT * FROM AD_EntityType WHERE IsActive='Y' ORDER BY AD_EntityType_ID";
				pstmt = DB.prepareStatement(sql, trx);
				rs = pstmt.executeQuery();
			}
			catch (Exception ee)
			{
				s_log.log (Level.SEVERE, sql, e);
			}
			
		}
		
		if (rs == null)
		{
			s_log.config("#0 - no entity types");
			if (trx != null)
				trx.close();
			return new MEntityType[]{};
		}

		try
		{
			while (rs.next())
				list.add (new MEntityType (ctx, rs, null));
		}
		catch (Exception e) {
			s_log.log (Level.SEVERE, sql, e);
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
			if(trx != null)
				trx.close();	
		}
		s_entityTypes = new MEntityType[list.size()];
		list.toArray(s_entityTypes);
		s_log.fine("# " + s_entityTypes.length);
		return s_entityTypes;
	}	//	getEntityTypes

	/**
	 * 	Get Entity Type as String array
	 *	@param ctx context
	 *	@return entity type array
	 */
	static public String[] getEntityTypeStrings(Ctx ctx)
	{
		MEntityType[] entityTypes = getEntityTypes(ctx, false);
		ArrayList<String> list = new ArrayList<String>();	//	list capabilities
		String[] retValue = new String[entityTypes.length];
		for (int i = 0; i < entityTypes.length; i++)
		{
			String s = entityTypes[i].getEntityType().trim();
			list.add(s);
			retValue[i] = s;
		}
		s_log.finer(list.toString());
		return retValue;
	}	//	getEntityTypeStrings

	/**
	 * 	Get Entity Type Classpath array
	 *	@param ctx context
	 *	@return classpath array
	 */
	static public String[] getClasspaths(Ctx ctx)
	{
		boolean forWindows = Env.isWindows();
		MEntityType[] entityTypes = getEntityTypes(ctx, false);
		ArrayList<String> list = new ArrayList<String>();
		for (MEntityType element : entityTypes)
		{
			String classpath = element.getClasspath(forWindows);
			if ((classpath == null) || (classpath.length() == 0))
				continue;
			StringTokenizer st = new StringTokenizer(classpath, ";:, \t\n\r\f");
			while (st.hasMoreTokens())
			{
				String token = st.nextToken().trim();
				if (token.length() > 0)
				{
					if (!list.contains(token))
						list.add(token);
				}
			}
		}
		String[] retValue = new String[list.size()];
		list.toArray(retValue);
		s_log.finer(list.toString());
		return retValue;
	}	//	getClasspaths

	/**
	 * 	Get Entity Type Model Packages array
	 *	@param ctx context
	 *	@return packages array
	 */
	static public String[] getPackages(Ctx ctx)
	{
		MEntityType[] entityTypes = getEntityTypes(ctx, false);
		ArrayList<String> list = new ArrayList<String>();
		for (MEntityType element : entityTypes) {
			String modelPackage = element.getModelPackage();
			if ((modelPackage == null) || (modelPackage.length() == 0))
				continue;
			StringTokenizer st = new StringTokenizer(modelPackage, ";:, \t\n\r\f");
			while (st.hasMoreTokens())
			{
				String token = st.nextToken().trim();
				if (token.length() > 0)
				{
					if (!list.contains(token))
						list.add(token);
				}
			}
		}
		String[] retValue = new String[list.size()];
		list.toArray(retValue);
		s_log.finer(list.toString());
		return retValue;
	}	//	getPackages

	/**
	 * 	Get ModelValidationClass array
	 *	@param ctx context
	 *	@return ModelValidationClasses array
	 */
	static public String[] getModelValidationClasses(Ctx ctx)
	{
		MEntityType[] entityTypes = getEntityTypes(ctx, false);
		ArrayList<String> list = new ArrayList<String>();
		for (MEntityType element : entityTypes) {
			String classes = element.getModelValidationClasses();
			if ((classes == null) || (classes.length() == 0))
				continue;
			StringTokenizer st = new StringTokenizer(classes, ";:, \t\n\r\f");
			while (st.hasMoreTokens())
			{
				String token = st.nextToken().trim();
				if (token.length() > 0)
				{
					if (!list.contains(token))
						list.add(token);
				}
			}
		}
		String[] retValue = new String[list.size()];
		list.toArray(retValue);
		s_log.finer(list.toString());
		return retValue;
	}	//	getModelValidationClasses

	/**
	 * 	Get Entity Type Model Package array
	 *	@param ctx context
	 *	@return entity type array
	 */
	static public String[] getModelPackages(Ctx ctx)
	{
		MEntityType[] entityTypes = getEntityTypes(ctx, false);
		ArrayList<String> list = new ArrayList<String>();
		list.add("compiere.model");		//	default
		for (MEntityType element : entityTypes) {
			String modelPackage = element.getModelPackage();
			if ((modelPackage == null) || (modelPackage.length() == 0))
				continue;
			StringTokenizer st = new StringTokenizer(modelPackage, ";:, \t\n\r\f");
			while (st.hasMoreTokens())
			{
				String token = st.nextToken();
				if (token.length() > 0)
				{
					if (!list.contains(token))
						list.add(token);
				}
			}
		}
		String[] retValue = new String[list.size()];
		list.toArray(retValue);
		s_log.finer(list.toString());
		return retValue;
	}	//	getModelPackages

	/** Cached EntityTypes						*/
	private static MEntityType[] s_entityTypes = null;
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MEntityType.class);

	/**************************************************************************
	 * 	DO NOT USE - use get(ctx, ID)
	 *	@param ctx context
	 *	@param AD_EntityType_ID id
	 *	@param trx transaction
	 */
	public MEntityType (Ctx ctx, int AD_EntityType_ID, Trx trx)
	{
		super (ctx, AD_EntityType_ID, trx);
	}	//	MEntityType

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MEntityType (Ctx ctx, ResultSet rs, Trx trx)
	{
		super (ctx, rs, trx);
	}	//	MEntityType

	/**
	 * 	Set AD_EntityType_ID
	 */
	private void setAD_EntityType_ID()
	{
		int AD_EntityType_ID = getAD_EntityType_ID();
		if (AD_EntityType_ID == 0)
		{
			String sql = "SELECT NVL(MAX(AD_EntityType_ID), 999999) FROM AD_EntityType WHERE AD_EntityType_ID > 1000";
			AD_EntityType_ID= QueryUtil.getSQLValue (get_Trx(), sql);
			setAD_EntityType_ID(AD_EntityType_ID+1);
		}
	}	//	setAD_EntityType_ID

	/**
	 * 	Is System Maintained
	 *	@return true if D/C/U/CUST/A/EXT/XX
	 */
	public boolean isSystemMaintained()
	{
		/** 10=D, 20=C,  100=U, 110=CUST,  200=A, 210=EXT, 220=XX	*/
		String et = getEntityType();
		if (et.length() == 1)
			return true;
		return (et.equals("CUST") || et.equals("XX") || et.equals("EXT"));
	}	//	isSystemMaintained

	/**
	 * 	EntityType / Component is Licensable
	 *	@return true if it has a license, not system maintained, active and has license text
	 */
	public boolean isLicensable()
	{
		if (isSystemMaintained() || !isActive())
			return false;
		if (!Util.isEmpty(getSummary()))
			return true;
		return !Util.isEmpty(getLicenseText());
	}	//	isLicensable

	/**
	 * 	Get ClassPath
	 *	@param forWindows true if for Windows otherwise Unix
	 *	@return os specific classpath
	 */
	public String getClasspath (boolean forWindows)
	{
		String classPath = super.getClasspath();
		if (classPath == null)
			return "";
		classPath = Util.replace (classPath, " ", "");
		if (forWindows)
			classPath = Util.replace (classPath, ":", ";");
		else
			classPath = Util.replace (classPath, ";", ":");
		return classPath;
	}	//	getClassPath

	/**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return true if it can be saved
	 */
	@Override
	protected boolean beforeSave (boolean newRecord)
	{
		if (!newRecord)
		{
			int id = getAD_EntityType_ID();
			boolean systemMaintained = ((id == 10) || (id == 20));	//	C/D
			if (systemMaintained)
			{
				log.saveError("Error", "You cannot modify a System maintained entity");
				return false;
			}
			systemMaintained = is_ValueChanged("EntityType");
			if (systemMaintained)
			{
				log.saveError("Error", "You cannot modify EntityType");
				return false;
			}
			systemMaintained = isSystemMaintained()
				&&	(is_ValueChanged("Name") || is_ValueChanged("Description")
					|| is_ValueChanged("Help") || is_ValueChanged("IsActive"));
			if (systemMaintained)
			{
				log.saveError("Error", "You cannot modify Name,Description,Help");
				return false;
			}
		}
		else	//	new
		{
			String et = getEntityType().toUpperCase();		//	upper case only
			setEntityType(et);	//	upper case
			if (getEntityType().trim().length() < 4)
			{
				log.saveError("FillMandatory", Msg.getElement(getCtx(), "EntityType")
					+ " - 4 Characters");
				return false;
			}
			char[] cc = et.toCharArray();
			for (char c : cc) {
				if (Character.isDigit(c) || ((c >= 'A') && (c <= 'Z')))
					continue;
				//
				log.saveError("FillMandatory", Msg.getElement(getCtx(), "EntityType")
					+ " - Must be ASCII Letter or Digit");
				return false;
			}
			if (et.startsWith("C"))
			{
				log.saveError("Error", "EntityType starting with C are reserved");
				return false;
			}
			setAD_EntityType_ID();
		}	//	new
		s_entityTypes = null;	//	reset
		return true;
	}	//	beforeSave

	/**
	 * 	Before Delete
	 *	@return true if it can be deleted
	 */
	@Override
	protected boolean beforeDelete ()
	{
		if (isSystemMaintained())	//	all pre-defined
		{
			log.saveError("Error", "You cannot delete a System maintained entity");
			return false;
		}
		s_entityTypes = null;	//	reset
		return true;
	}	//	beforeDelete

	/**
	 * 	String Info
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MEntityType[");
		sb.append(getAD_EntityType_ID())
			.append("-").append(getEntityType())
			.append("-").append(getName())
			.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Process Registration Response
	 *	@param response response
	 *	@return summary
	 */
	public String processRegistrationResponse(String response)
	{
		if ((response == null) || (response.length() == 0))
			return "No Response";
		//
		boolean ok = response.startsWith(":" + getEntityType() + ":");
		if (ok)
		{
			String recordIDMarker = "RecordID=";
			int index = response.indexOf(recordIDMarker);
			if (index == -1)
			{
				ok = false;
				response = "Error No RecordID - " + response;
			}
			else
			{
				try
				{
					String id = response.substring(index+recordIDMarker.length());
					index = id.indexOf(";");
					id = id.substring(0,index);
					int Record_ID = Integer.parseInt(id);
					setRecord_ID(Record_ID);
				}
				catch (Exception e)
				{
				}
			}
		}
		//	Failure
		if (!ok)
		{
			setIsRegistered(false);
			setRecord_ID(0);
			return response;
		}
		//
		setIsRegistered(true);
		return response;
	}	//	processRegistrationResponse

}	//	MEntityType
