/*
 * =====================================================================
 * Material Propiedad Ferretería EPA C.A. 
 *
 * Proyecto		: extend
 * Paquete		: com.compiere.client
 * Programa	    : CECStartComponent.java
 * Creado por	: e00jcomp1
 * Creado el 	: 25/09/2009 08:24:32
 *
 * (C) Copyright 2009 Ferretería EPA C.A. Todos los Derechos Reservados
 * =====================================================================
 */
package com.compiere.client;


import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * TODO EPA_La descripción de la clase viene aqui.
 * 
 * @author e00jcomp1 - $Author$
 * @version $Rev$ - $Date$
 * @since 25/09/2009
 */
public class CECStartComponent 

	{
		/**	Static Logger				*/
		private static CLogger	cLogger	= CLogger.getCLogger (CECStartComponent.class);
		
		/** Data Migration Flag	- see also ComponentCreate		*/
		private static final String	DATAMIGRATION = "xxxx";
		
		/**
		 * 	Start Component
		 */
		public CECStartComponent()
		{
			//
			CLogErrorBuffer eb = CLogErrorBuffer.get(true);
			if (eb != null && eb.isIssueError())
				eb.setIssueError(false);
		}	//	CECStartComponent
		
		/**
		 * If it is build machine, ignore license check
		 * @return true if can be used locally
		 */
		private boolean checkCompiere()
		{
			try
			{
				String buildMachine = java.net.InetAddress.getLocalHost().toString().toUpperCase();
				if (buildMachine.equals("DEV1/10.104.139.111") 
					|| buildMachine.equals("DEV2/10.104.139.112") 
					|| buildMachine.equals("RC/10.104.139.75"))
					return true;
			}
			catch (Exception ex)
			{
				return false;
			}
//*** Cambiado para no verificar licencia
			return true;
		}  // checkCompiere()
		
		/**
		 * Create component.
		 * 	Called from CreateComponent.main, EntityTypeComponent.doIt
		 *	@param entityName entity type name
		 *	@param jars jar file included in the component car
		 *	@param targetDir target directory
		 *	@return info messages
		 */
		public String createComponent(String entityName, List<String> jars, String targetDir)
		{		
			if (entityName.equals("D") || entityName.startsWith("C"))
				return "You cannot create a Dictionary Component";
			//TODO EPA CAR: ... para no validar si existe o no el componente
			/*
			if (!checkCompiere())
			{
				String retVal = contactLicenseServerNowCreate(entityName);
				if (retVal.startsWith("ERROR"))
					return retVal;
			}
			*/
			Object info = null;
			final String classname = "com.compiere.migrate.Component";
			try
			{
				URL url = new URL (CECSupport.getJarURL());
				ClassLoader sysCL = Thread.currentThread ().getContextClassLoader();
				URLClassLoader cl = new URLClassLoader (new URL[] {url}, sysCL);
				Class<?> clazz = null;
	///////////////////////////////////////////////////////////////////////////////
				if (CECSupport.INTERNALTEST)
					clazz = Class.forName(classname);		//	Test
				else if (cl != null)
					clazz = cl.loadClass(classname);		//	Production
	///////////////////////////////////////////////////////////////////////////////
				// Create the new instance
				Object instance = clazz.newInstance();
				
				// Create the parameter list
				Class<?>[] parameterTypes = new Class[]{
						String.class, List.class, String.class
					};
				Object[] args = new Object[]{
						entityName, jars, targetDir
					};
					
				// Execute
				Method method = clazz.getMethod ("create", parameterTypes);
				info = method.invoke (instance, args);
			}
			catch (Exception e)
			{
				if (CECSupport.INTERNALTEST)
					e.printStackTrace();
			}
			
			if (info != null)
				return info.toString();
			
			return null;
		}  // createComponent

		/**
		 * Un-install component
		 * @param entityName entity type name
		 * @param isCascade if true, delete cascade records; false set active flag to false
		 * @param deleteFile if true, delete the ear/war
		 * @return info messages
		 */
		public String removeComponent(String entityName, Boolean isCascade, Boolean deleteFile)
		{
			Object info = null;
			final String classname = "com.compiere.migrate.Component";
			try
			{
				URL url = new URL (CECSupport.getJarURL());
				ClassLoader sysCL = Thread.currentThread ().getContextClassLoader();
				URLClassLoader cl = new URLClassLoader (new URL[] {url}, sysCL);
				Class<?> clazz = null;
	///////////////////////////////////////////////////////////////////////////////
				if (CECSupport.INTERNALTEST)
					clazz = Class.forName(classname);		//	Test
				else if (cl != null)
					clazz = cl.loadClass(classname);		//	Production
	///////////////////////////////////////////////////////////////////////////////
				// Create the new instance
				Object instance = clazz.newInstance();
				
				// Create the parameter list
				Class<?>[] parameterTypes = new Class[]{
						String.class, Boolean.class, Boolean.class
					};
				Object[] args = new Object[]{
						entityName, isCascade, deleteFile
					};
					
				// Execute
				Method method = clazz.getMethod ("remove", parameterTypes);
				info = method.invoke (instance, args);
			}
			catch (Exception e)
			{
				if (CECSupport.INTERNALTEST)
					e.printStackTrace();
			}
			
			if (info != null)
				return info.toString();
			
			return null;
		}  // removeComponent
		
		

}
