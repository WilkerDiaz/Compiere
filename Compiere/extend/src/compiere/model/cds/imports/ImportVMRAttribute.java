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
package compiere.model.cds.imports;
import java.math.*; 
import java.sql.*; 
import java.util.logging.*; 

import org.compiere.process.ProcessInfoParameter; 
import org.compiere.process.SvrProcess; 
import org.compiere.util.*; 

import compiere.model.cds.MAttribute;
import compiere.model.cds.X_I_XX_Prom02;

/**
 *	Import Master Characteristic from I_XX_Prom02
 *  Attribute (M_attribute)
 *  @author Cadena de Suministros
 */

public class ImportVMRAttribute  extends SvrProcess
{
	/**	Client to be imported to */
	private int	s_AD_Client_ID = 0;
	
	/**	Delete old Imported	*/
	private boolean	s_deleteOldImported = false;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	public void prepare() 
	{ 

		ProcessInfoParameter[] para = getParameter(); 

		for (ProcessInfoParameter element : para) 
		{ 
			String name = element.getParameterName(); 
			if (element.getParameter() == null )
				; 
			
			else if (name.equals( "AD_Client_ID" )) 
				s_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue(); 

			else if (name.equals( "DeleteOldImported" )) 
				s_deleteOldImported = "Y" .equals(element.getParameter()); 

			else 
				log .log(Level. SEVERE , "Unknown Parameter: " + name); 
		}//for 

	} //Prepare 

	/**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception
	 */	
	@Override 

	protected String doIt() throws Exception 
	{ 
		// TODO Auto-generated method stub 
		StringBuffer sql = null ; 
		int no = 0; 
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID ; 

/**		// **** Prepare **** 
		// Delete Old Imported 
		if ( s_deleteOldImported ) 
		{ 
			sql = new StringBuffer ( "DELETE FROM I_XX_Prom02 " 
					+ " WHERE IsImported='Y'" ).append(clientCheck); 
			no = DB. executeUpdate (get_Trx(), sql.toString()); 
			log .fine( "Delete Old Imported =" + no); 
		} //If DeleteOldImported
*/
		// Set Client, Org , IsActive, Created/Updated 
		sql = new StringBuffer ( "UPDATE I_XX_Prom02 " 
	+ "SET AD_Client_ID = COALESCE (AD_Client_ID, " ).append( s_AD_Client_ID ).append( ")," 
	+ " AD_Org_ID = COALESCE (AD_Org_ID, 0)," 
	+ " IsActive = COALESCE (IsActive, 'Y')," 
	+ " Created = COALESCE (Created, SysDate)," 
	+ " CreatedBy = COALESCE (CreatedBy, 0)," 
	+ " Updated = COALESCE (Updated, SysDate)," 
	+ " UpdatedBy = COALESCE (UpdatedBy, 0)," 
	+ " I_ErrorMsg = NULL," 
	+ " I_IsImported = 'N' " 
	+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL" ); 
		no = DB. executeUpdate (get_Trx(), sql.toString()); 
		log .fine( "Reset=" + no); 
		// 
/**
	sql = new StringBuffer ( "UPDATE I_XX_Prom02 i " 
	+ "SET M_Attribute_ID=(SELECT M_ATTRIBUTE_ID FROM M_ATTRIBUTE " 
	+ "WHERE i.XX_TIPCAR = M_ATTRIBUTE_ID AND i.XX_NOMCAR = NAME AND AD_CLIENT_ID = i.AD_CLIENT_ID)"
	+ "WHERE i.M_ATTRIBUTE_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL AND IsImported='N'" ).append(clientCheck);
	System.out.println(sql);
	no = DB. executeUpdate (get_Trx(), sql.toString()); 
	log .fine( "Found Section=" + no); 
*/	
/**	// colocar en dos y tres digitos el codigo del atributo	
		sql = new StringBuffer ( "UPDATE I_XX_Prom02 i " 
				+ "SET XX_TipCar = concat('0',XX_TipCar) " +
				"where LENGTH(XX_TipCar)=2");
				System.out.println(sql);
				no = DB. executeUpdate (get_Trx(), sql.toString()); 
		sql = new StringBuffer ( "UPDATE I_XX_Prom02 i " 
				+ "SET XX_TipCar = concat('00',XX_TipCar) " +
				"where LENGTH(XX_TipCar)=1");
				System.out.println(sql);
				no = DB. executeUpdate (get_Trx(), sql.toString()); 
*/	
		
		
	String ts = DB. isPostgreSQL ()? "COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg" ; //java bug, it could not be used directly 
	commit(); 

	//----------------------------------------------------------------------------------- 

	int noInsert = 0; 

	// Go through Records 
	log .fine( "start inserting..." ); 
	sql = new StringBuffer ( "SELECT * FROM I_XX_Prom02" 
	+ " WHERE I_IsImported='N'" ).append(clientCheck); 

	try 
	{ 
		PreparedStatement pstmt = DB. prepareStatement (sql.toString(), get_Trx()); 
		ResultSet rs = pstmt.executeQuery(); 

		while (rs.next()) 
		{ 
			X_I_XX_Prom02 impItab = new X_I_XX_Prom02 (getCtx(), rs, get_TrxName()); 
			log .fine( "I_XX_Prom02_ID=" + impItab.getI_XX_PROM02_ID()); 

			// **** Create/Update Category **** 
			MAttribute impXtab = null ; 

			// Pregunto por ID de la tabla compiere que es igual al de la tabla I // 
			if (impItab.getM_Attribute_ID().intValue() == 0) 
			{ 
				impXtab = new MAttribute(getCtx(), 0, get_TrxName()); 
			} 
			else 
			{ 
				impXtab = new MAttribute(getCtx(), impItab.getM_Attribute_ID().intValue(), get_TrxName()); 
			} 
			
	// Get and Set de los campos
			//impXtab.setM_Attribute_ID(impItab.getXX_TipCar().intValue());
			impXtab.setName(impItab.getXX_NomCar());
			impXtab.setDescription(impItab.getXX_NomCar());
			impXtab.setAttributeValueType(MAttribute.ATTRIBUTEVALUETYPE_List);
			impXtab.setValue(impItab.getXX_TipCar().toString());

	// 

			if (impXtab.save()) 
			{ 
				log .finest( "Insert/Update Master Characteristic - " + impXtab.getM_Attribute_ID()); 
				noInsert++; 
				impItab.setI_IsImported( true ); 
				impItab.setProcessed( true ); 
				impItab.setProcessing( false ); 
				impItab.save(); 
			} //If save
			else 
			{ 
				rollback(); 
				noInsert--; 
				sql = new StringBuffer ( "UPDATE I_XX_Prom02  " 
	+ "SET I_IsImported='E', I_ErrorMsg=" +ts + "|| '" ) 
	.append( "Cannot Insert Prom02..." ) 
	.append( "' WHERE I_XX_Prom02_ID=" ).append(impItab.getI_XX_PROM02_ID()); 
				DB. executeUpdate (get_Trx(), sql.toString()); 
				//continue; 
			}//else safe 
			
			commit(); 
			// 
		} // end while 

		rs.close(); 
		pstmt.close(); 
	}//try 
	
	catch (SQLException e) 
	{ 
		log .log(Level. SEVERE , "" , e); 
		rollback(); 
	}//catch 

	// Set Error to indicator to not imported 
	sql = new StringBuffer ( "UPDATE I_XX_Prom02 " 
	+ "SET I_IsImported='N', Updated=SysDate " 
	+ "WHERE I_IsImported<>'Y'" ).append(clientCheck); 
	no = DB. executeUpdate (get_Trx(), sql.toString()); 
	addLog (0, null , new BigDecimal (no), "@Errors@" ); 
	addLog (0, null , new BigDecimal (noInsert), "@M_Attribute_ID@: @Inserted@" ); 
	return "" ; 
	}//doIt 

	
}//fin Import
