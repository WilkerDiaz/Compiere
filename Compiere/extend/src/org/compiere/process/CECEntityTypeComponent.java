/*
 * =====================================================================
 * Material Propiedad Ferretería EPA C.A. 
 *
 * Proyecto		: extend
 * Paquete		: org.compiere.process
 * Programa	    : CECEntityTypeComponent.java
 * Creado por	: e00jcomp1
 * Creado el 	: 25/09/2009 08:20:36
 *
 * (C) Copyright 2009 Ferretería EPA C.A. Todos los Derechos Reservados
 * =====================================================================
 */
package org.compiere.process;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.compiere.model.MEntityType;
import org.compiere.util.CompiereSystemException;
import org.compiere.util.CompiereUserException;
import org.compiere.util.Env;
import org.compiere.util.Ini;

/**
 * TODO EPA_La descripción de la clase viene aqui.
 * 
 * @author e00jcomp1 - $Author$
 * @version $Rev$ - $Date$
 * @since 25/09/2009
 */
public class CECEntityTypeComponent extends SvrProcess
{
	/** Entity Type			*/
	protected int		p_AD_EntityType_ID = 0;
	/** Directory			*/
	protected String 	p_directory = null;
	
	/**
	 * 	Prepare
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("JarDirectory"))
				p_directory = (String)element.getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_AD_EntityType_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return summary
	 *	@throws Exception
	 */
	@Override
	protected String doIt() throws Exception
	{
		log.info("AD_EntityType_ID=" + p_AD_EntityType_ID 
			+ ", JarDirectory=" + p_directory);
		MEntityType et = MEntityType.getEntityType(getCtx(), p_AD_EntityType_ID);
		String EntityType = et.getEntityType();
		if (EntityType.equals("D"))
			throw new CompiereUserException("You cannot create a Dictionary Component");
		
		//	Directory
		if (p_directory == null || p_directory.length() == 0)
			p_directory = "";	//	current
		else if (!p_directory.endsWith("/") && !p_directory.endsWith("\\"))
				p_directory += File.separator;
		
		//	Check Jar Files
		boolean forWindows = Env.isWindows();
		String classPath = et.getClasspath(forWindows);
		StringTokenizer st = new StringTokenizer(classPath, ",;: ", false);
		List<String> jars = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		while (st.hasMoreTokens())
		{
			String jarName = st.nextToken();
			if (jarName.length() == 0)
				continue;
			String fileName = p_directory + jarName;
			File file = new File(fileName);
			if (file.exists() && file.isFile())
				;
			else
				throw new CompiereUserException("Cannot find: " + fileName);
			jars.add(fileName);
			if (!first)
				sb.append(",");
			else
				first = false;
			sb.append(fileName);
		}
		log.info("Deploy file: " + sb.toString());
		
		//	Call
		Class<?>[] parameterTypes = new Class[]{
			String.class, List.class, String.class
		};
		String targetDir = Ini.getCompiereHome() + File.separator + "data";
		Object[] args = new Object[]{EntityType, jars, targetDir};
		Object info = null;
		try
		{
			Class<?> clazz = Class.forName("com.compiere.client.CECStartComponent");
			Object instance = clazz.newInstance();
			Method method = clazz.getMethod ("createComponent", parameterTypes);
			info = method.invoke (instance, args);
		}
		catch (Exception e)
		{
			throw new CompiereSystemException(e.toString());
		}
		
		if (info != null)
			return info.toString();
		throw new CompiereSystemException("Please check Log");
	}	//	doIt


}
