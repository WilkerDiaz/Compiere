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
package org.compiere.util;

import java.io.*;
import java.util.*;

/**
 * Utility class to support view import process
 * 
 * @author wwong
 *
 */
public class ViewUtil 
{
	/**
	 * 	Read the sql statements from the input stream
	 *	@param in InputStream
	 *	@param addSemicolon add semicolon at end of functions
	 *	@return ArrayList<String> of the sql commands
	 */
	public static ArrayList<String> readSqlFromFile(InputStream in)
	{
		if (in == null)
			return null;
		
		// Initialization
		BufferedReader input = null;
		String line = null;
		String command = null;
		StringBuffer sqlStatement = null;
		int commentIndex = -1;
		ArrayList<String> retStr = new ArrayList<String>();
		try
		{	
			// Create the reader to read the input sql statements line by line
			input = new BufferedReader(new InputStreamReader(in));
			while ((line = input.readLine()) != null)
			{
				// Trim the line
				line = line.trim();

				// Zero length.  Can't start with space
				if (line.length() == 0)
					continue;

				// Handle C style comments which "/*" has to be at beginning of line
				if (line.startsWith("/*"))
				{
					// Ensure it isn't one line comment
					if (line.indexOf("*/") != -1)
						continue;
					// Ignore all the comments
					while ((line = input.readLine()) != null && line.indexOf("*/") == -1)
						;
					// Start with next line
					continue;  
				}

				//  Comment or separator
				if (line.startsWith("--") || line.startsWith(";") || line.startsWith("/"))
					continue;

				// Don't care about the PL/SQL stuff
				if (line.toUpperCase().startsWith("SET SERVEROUTPUT ON"))
					continue;

				// Exit 
				if (line.toUpperCase().startsWith("EXIT"))
					break;

				// Strip off the comments - isn't at beginning, must be at the end
				commentIndex = line.indexOf("--");
				if (commentIndex != -1)
					line = line.substring(0, commentIndex);

				// Trim the line
				line = line.trim();

				// Single line command
				if (line.endsWith(";") || line.endsWith("/"))
				{
					// Strip out the ";" before send thru jdbc
					command = line.substring(0, line.length() - 1);
				}
				else
				{
					// Check what kind of commands it is
					boolean notProcedure = true;
					if (line.toUpperCase().startsWith("CREATE OR REPLACE PROCEDURE") ||
							line.toUpperCase().startsWith("CREATE OR REPLACE PACKAGE") ||
							line.toUpperCase().startsWith("CREATE OR REPLACE FUNCTION") ||
							line.toUpperCase().startsWith("CREATE OR REPLACE TRIGGER") ||
							line.toUpperCase().startsWith("CREATE PROCEDURE") ||
							line.toUpperCase().startsWith("CREATE FUNCTION") ||
							line.toUpperCase().startsWith("DECLARE") ||
							line.toUpperCase().startsWith("BEGIN"))
						notProcedure = false;

					// Loop thru the multi-line or nested statements
					sqlStatement = new StringBuffer(line);
					while ((line = input.readLine()) != null)
					{
						// Trim the line
						//line = line.trim();

						// Check empty
						if (line.length() == 0)
							continue; 

						// Ignore the comments
						if (line.startsWith("--"))
							continue;

						// Handle C style comments which "/*" has to be at beginning of line
						if (line.startsWith("/*"))
						{
							// Ensure it isn't one line comment
							if (line.indexOf("*/") != -1)
								continue;
							// Ignore all the comments
							while ((line = input.readLine()) != null && line.indexOf("*/") == -1)
								;
							// Start with next line
							continue;  
						}

						// End of sql statement
						if (line.startsWith(";") || line.startsWith("/") || line.startsWith("GO") && !line.startsWith("GOTO"))
							break;

						// Strip off the comments
						commentIndex = line.indexOf("--");
						if (commentIndex != -1 && line.charAt(commentIndex - 1) != '\'')
							line = line.substring(0, commentIndex);

						// Trim the line again after removed the "--" comments
						//line = line.trim();

						sqlStatement.append(" ").append(line);

						// For non function/procedure, ";" is end of statement
						if (notProcedure && (line.endsWith(";") || line.startsWith("GO") && DB.isMSSQLServer()))
							break;
					} // while ((line = input.readLine()) != null)
					command = sqlStatement.toString();

					// For non function/procedure, strip off the trailing ";"
					if (!command.toUpperCase().startsWith("CREATE OR REPLACE PROCEDURE") 
						&& !command.toUpperCase().startsWith("CREATE OR REPLACE PACKAGE") 
						&& !command.toUpperCase().startsWith("CREATE OR REPLACE FUNCTION") 
						&& !command.toUpperCase().startsWith("CREATE OR REPLACE TRIGGER")
						&& !command.toUpperCase().startsWith("DECLARE") 
						&& !command.toUpperCase().startsWith("BEGIN") 
						&& command.endsWith(";"))
						command = command.substring(0, command.length() - 1);
				}  // if (line.endsWith(";") || line.endsWith("/"))	
				
				retStr.add(command);
			} // while ((line = input.readLine()) != null)
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try 
			{
				if (input != null) 
					input.close();
			}
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
		}
		
		return retStr;
	}  // readSqlFromFile()
}  // ViewUtil
