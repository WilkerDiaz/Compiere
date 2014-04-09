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

package org.compiere.api;

/**
 * 	License Interface
 *  
 *  Implement this interface if you want to enable license checking for your
 *  Compiere Application Extension.
 *  
 *  The implementation class should be named LicenseXXXX where XXXX is the entity
 *  type code of your application extension.
 *  
 *  The implementation class should be placed in the model package that you have
 *  specified for your application extension.
 *   
 *	@author Jorg Janke
 */
public interface LicenseInterface
{
	/**
	 * 	Get Actual Units #1
	 *	@return units
	 */
	int getUnitOne();
	
	/**
	 * 	Get Actual Units #2
	 *	@return units
	 */
	int getUnitTwo();

	/**
	 * 	Get Actual Status #1
	 *	@return status
	 */
	String getStatusOne();
	
	/**
	 * 	Get Actual Status #2
	 *	@return status
	 */
	String getStatusTwo();
	
}	//	LicenseInterface
