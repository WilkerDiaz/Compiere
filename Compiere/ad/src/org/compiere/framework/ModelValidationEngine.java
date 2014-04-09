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
package org.compiere.framework;

import java.util.*;
import java.util.logging.*;

import org.compiere.api.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	Model Validation Engine
 *
 *  @author Jorg Janke
 *  @version $Id: ModelValidationEngine.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class ModelValidationEngine
{
	/**
	 * 	Get Singleton
	 *	@return engine
	 */
	public static ModelValidationEngine get()
	{
		if (s_engine == null)
			s_engine = new ModelValidationEngine();
		return s_engine;
	}	//	get

	/** Engine Singleton				*/
	private static ModelValidationEngine s_engine = null;


	/**************************************************************************
	 * 	Constructor.
	 * 	Creates Model Validators
	 */
	private ModelValidationEngine ()
	{
		super();
		addClasses();
		log.config(toString());
	}	//	ModelValidatorEngine

	/**
	 * 	Add Model Validator Classes
	 */
	private void addClasses ()
	{
		Ctx ctx = Env.getCtx();
		//	Go through all Clients and start Validators
		MClient[] clients = MClient.getAll(ctx);
		for (MClient element : clients)
		{
			String classNames = element.getModelValidationClasses();
			if (classNames == null || classNames.length() == 0)
				continue;

			StringTokenizer st = new StringTokenizer(classNames, ";: ");
			while (st.hasMoreTokens())
			{
				String className = null;
				try
				{
					className = st.nextToken();
					if (className == null)
						continue;
					className = className.trim();
					if (className.length() == 0)
						continue;
					//
					Class<?> clazz = Class.forName(className);
					ModelValidator validator = (ModelValidator)clazz.newInstance();
					validator.initialize(element.getAD_Client_ID(), this);
					m_validators.add(validator);
				}
				catch (Throwable e)
				{
					log.log(Level.WARNING, element.getName() + ": " + className + " - " + e.toString());
				}
			}
		}	//	allClients

		//	Entity Types
		MEntityType[] entityTypes = MEntityType.getEntityTypes(ctx, false);
		for (MEntityType element : entityTypes)
		{
			String classNames = element.getModelValidationClasses();
			if (classNames == null || classNames.length() == 0)
				continue;

			StringTokenizer st = new StringTokenizer(classNames, ";: ");
			while (st.hasMoreTokens())
			{
				String className = null;
				try
				{
					className = st.nextToken();
					if (className == null)
						continue;
					className = className.trim();
					if (className.length() == 0)
						continue;
					//
					Class<?> clazz = Class.forName(className);
					ModelValidator validator = (ModelValidator)clazz.newInstance();
					validator.initialize(0, this);
					m_validators.add(validator);
				}
				catch (Exception e)
				{
					log.log(Level.WARNING, element.getName() + ": " + className + " - " + e.toString());
				}
			}
		}	//	all EntityTypes
	}	//	addClasses


	/**	Logger					*/
	private static CLogger log = CLogger.getCLogger(ModelValidationEngine.class);

	/**	Validators						*/
	private ArrayList<ModelValidator>	m_validators = new ArrayList<ModelValidator>();
	/**	Model Change Listeners			*/
	private Hashtable<String,ArrayList<ModelValidator>>	m_modelChangeListeners = new Hashtable<String,ArrayList<ModelValidator>>();
	/**	Document Validation Listeners			*/
	private Hashtable<String,ArrayList<ModelValidator>>	m_docValidateListeners = new Hashtable<String,ArrayList<ModelValidator>>();


	/**
	 * 	Called when login is complete
	 * 	@param AD_Client_ID client
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String loginComplete (int AD_Client_ID, int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		for (int i = 0; i < m_validators.size(); i++)
		{
			ModelValidator validator = m_validators.get(i);
			if (validator.getAD_Client_ID() == 0 || validator.getAD_Client_ID() == AD_Client_ID)
			{
				String error = validator.login(AD_Org_ID, AD_Role_ID, AD_User_ID);
				if (error != null && error.length() > 0)
					return error;
			}
		}
		return null;
	}	//	loginComplete


	/**************************************************************************
	 * 	Add Model Change Listener
	 *	@param tableName table name
	 *	@param listener listener
	 */
	public void addModelChange (String tableName, ModelValidator listener)
	{
		if (tableName == null || listener == null)
			return;
		//
		String propertyName = tableName + listener.getAD_Client_ID();
		ArrayList<ModelValidator> list = m_modelChangeListeners.get(propertyName);
		if (list == null)
		{
			list = new ArrayList<ModelValidator>();
			list.add(listener);
			m_modelChangeListeners.put(propertyName, list);
		}
		else
			list.add(listener);
	}	//	addModelValidator

	/**
	 * 	Remove Model Change Listener
	 *	@param tableName table name
	 *	@param listener listener
	 */
	public void removeModelChange (String tableName, ModelValidator listener)
	{
		if (tableName == null || listener == null)
			return;
		String propertyName = tableName + listener.getAD_Client_ID();
		ArrayList<ModelValidator> list = m_modelChangeListeners.get(propertyName);
		if (list == null)
			return;
		list.remove(listener);
		if (list.size() == 0)
			m_modelChangeListeners.remove(propertyName);
	}	//	removeModelValidator

	/**
	 * 	Fire Model Change.
	 * 	Call modelChange method of added validators
	 *	@param po persistent objects
	 *	@param changeType ModelValidator.CHANGETYPE_*
	 *	@return error message or NULL for no veto
	 */
	public String fireModelChange (PO po, int changeType)
	{
		if (po == null || m_modelChangeListeners.size() == 0)
			return null;
		//
		String propertyName = po.get_TableName() + po.getAD_Client_ID();
		ArrayList<ModelValidator> list = m_modelChangeListeners.get(propertyName);
		String propertyName0 = po.get_TableName() + "0";
		ArrayList<ModelValidator> list0 = m_modelChangeListeners.get(propertyName0);
		if (list == null && list0 == null)
			return null;
		if (list == null && list0 != null)
			list = list0;
		else if (list != null && list0 != null)
			list.addAll(list0);
		else if (list != null && list0 == null)
			;

		//
		for (int i = 0; i < list.size(); i++)
		{
			try
			{
				ModelValidator validator = list.get(i);
				if (validator.getAD_Client_ID() == 0 || validator.getAD_Client_ID() == po.getAD_Client_ID())
				{
					String error = validator.modelChange(po, changeType);
					if (error != null && error.length() > 0)
						return error;
				}
			}
			catch (Exception e)
			{
				String error = e.getMessage();
				if (error == null)
					error = e.toString();
				return error;
			}
		}
		return null;
	}	//	fireModelChange


	/**************************************************************************
	 * 	Add Document Validation Listener
	 *	@param tableName table name
	 *	@param listener listener
	 */
	public void addDocValidate (String tableName, ModelValidator listener)
	{
		if (tableName == null || listener == null)
			return;
		//
		String propertyName = tableName + listener.getAD_Client_ID();
		ArrayList<ModelValidator> list = m_docValidateListeners.get(propertyName);
		if (list == null)
		{
			list = new ArrayList<ModelValidator>();
			list.add(listener);
			m_docValidateListeners.put(propertyName, list);
		}
		else
			list.add(listener);
	}	//	addDocValidate

	/**
	 * 	Remove Document Validation Listener
	 *	@param tableName table name
	 *	@param listener listener
	 */
	public void removeDocValidate (String tableName, ModelValidator listener)
	{
		if (tableName == null || listener == null)
			return;
		String propertyName = tableName + listener.getAD_Client_ID();
		ArrayList<ModelValidator> list = m_docValidateListeners.get(propertyName);
		if (list == null)
			return;
		list.remove(listener);
		if (list.size() == 0)
			m_docValidateListeners.remove(propertyName);
	}	//	removeModelValidator

	/**
	 * 	Fire Document Validation.
	 * 	Call docValidate method of added validators
	 *	@param po persistent objects
	 *	@param docTiming see ModelValidator.DOCTIMING_ constants
     *	@return error message or null
	 */
	public String fireDocValidate (PO po, int docTiming)
	{
		if (po == null || m_docValidateListeners.size() == 0)
			return null;
		//
		String propertyName = po.get_TableName() + po.getAD_Client_ID();
		ArrayList<ModelValidator> list = m_docValidateListeners.get(propertyName);
		String propertyName0 = po.get_TableName() + "0";
		ArrayList<ModelValidator> list0 = m_docValidateListeners.get(propertyName0);
		if (list == null && list0 == null)
			return null;
		if (list == null && list0 != null)
			list = list0;
		else if (list != null && list0 != null)
			list.addAll(list0);
		else if (list != null && list0 == null)
			;

		//
		for (int i = 0; i < list.size(); i++)
		{
			ModelValidator validator = null;
			try
			{
				validator = list.get(i);
				if (validator.getAD_Client_ID() == 0 || validator.getAD_Client_ID() == po.getAD_Client_ID())
				{
					String error = validator.docValidate(po, docTiming);
					if (error != null && error.length() > 0)
						return error;
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, validator.toString(), e);
			}
		}
		return null;
	}	//	fireModelChange


	/**
	 * 	Update Info Window Columns.
	 * 	- add new Columns
	 * 	- remove columns
	 * 	- change display sequence
	 * 	@param AD_Client_ID client
	 *	@param columns array of columns
	 *	@param sqlFrom from clause, can be modified
	 *	@param sqlOrder order by clause, can me modified
	 *	@return true if you updated columns, sequence or sql From clause
	 */
	public boolean updateInfoColumns (int AD_Client_ID, ArrayList<Info_Column> columns,
		StringBuffer sqlFrom, StringBuffer sqlOrder)
	{
		boolean retValue = true;
		for (int i = 0; i < m_validators.size(); i++)
		{
			ModelValidator validator = m_validators.get(i);
			if (validator.getAD_Client_ID() == 0 || validator.getAD_Client_ID() == AD_Client_ID)
			{
				try
				{
					boolean bb = validator.updateInfoColumns (columns, sqlFrom, sqlOrder);
					if (bb)
						retValue = true;
				}
				catch (Exception e)
				{
					log.warning (validator.toString() + ": " + e);
				}
			}
		}
		return retValue;
	}	//	updateInfoColumns

	/**
	 * 	String Representation
	 *	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("ModelValidationEngine[");
		sb.append("Validators=#").append(m_validators.size())
			.append(", ModelChange=#").append(m_modelChangeListeners.size())
			.append(", DocValidate=#").append(m_docValidateListeners.size())
			.append("]");
		return sb.toString();
	}	//	toString

}	//	ModelValidatorEngine
