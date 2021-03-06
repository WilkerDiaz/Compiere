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
import java.sql.*;
import java.util.logging.*;


import org.compiere.process.ProcessInfoParameter;
import java.math.BigDecimal;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

import compiere.model.cds.MAttributeSet;
import compiere.model.cds.MAttributeUse;
import compiere.model.cds.X_I_XX_Prod02i;
import compiere.model.cds.X_XX_VMR_DynamicCharact;

/**
 *	Import Dynamic Characteristic from I_XX_Prod02i
 *  @author Cadena de Suministros
 */
public class ImportVMRDynamicCharacteristic extends SvrProcess {

	/**	Client to be imported to*/
	private int	s_AD_Client_ID = 0;
	
	/**	Delete old Imported	*/
	private boolean s_deleteOldImported = false;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	
	//
	public void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) 
		{
			Double laog=0.50;
			BigDecimal algo= new BigDecimal(laog);
			algo.setScale(0, BigDecimal.ROUND_HALF_DOWN);
			//int lago= algo.intValue();
			//System.out.println(lago);
			String name = element.getParameterName();
			if (element.getParameter() == null);
			
			else if (name.equals("AD_Client_ID"))
				s_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				s_deleteOldImported = "Y".equals(element.getParameter());
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}


	@Override
	protected String doIt() throws Exception {
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (s_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_XX_Prod02i "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
		
		// Esta primera parte consiste en hacer la insercion en su propia tabla i_xx_prod02i con los datos de la i_xx_prod02
/**		
		// primero insertamos las que tienen tres caracteristicas
		String sql1 = "select a.XX_CODDEP,a.XX_CODLIN,a.XX_CODSEC,a.xx_tipcar, b.xx_tipcar, c.xx_tipcar " +
						"from " +
							"(select xx_coddep, xx_codlin, xx_codsec, xx_tipcar from i_xx_prod02 where xx_vmr_section_id in (select xx_vmr_section_id from  I_XX_PROD02 group by xx_vmr_section_id having count(*)=3) order by xx_coddep, xx_codlin, xx_codsec) a, " +
							"(select xx_coddep, xx_codlin, xx_codsec, xx_tipcar from i_xx_prod02 where xx_vmr_section_id in (select xx_vmr_section_id from  I_XX_PROD02 group by xx_vmr_section_id having count(*)=3) order by xx_coddep, xx_codlin, xx_codsec) b, " +
							"(select xx_coddep, xx_codlin, xx_codsec, xx_tipcar from i_xx_prod02 where xx_vmr_section_id in (select xx_vmr_section_id from  I_XX_PROD02 group by xx_vmr_section_id having count(*)=3) order by xx_coddep, xx_codlin, xx_codsec) c " +
							"where a.xx_coddep=b.xx_coddep " +
							"and a.XX_CODLIN = b.XX_CODLIN " +
							"and a.XX_CODSEC = b.XX_CODSEC " +
							"and a.xx_tipcar != b.xx_tipcar " +
							"and a.xx_coddep= c.xx_coddep " +
							"and a.XX_CODLIN = c.XX_CODLIN  " +
							"and a.XX_CODSEC = c.XX_CODSEC " +
							"and a.xx_tipcar != c.xx_tipcar " +
							"and b.xx_tipcar != c.xx_tipcar " +
							"and a.xx_tipcar != b.xx_tipcar " +
						"order by a.XX_CODDEP,a.XX_CODLIN,a.XX_CODSEC";
		
		String dep = "";
		String lin = "";
		String sec = "";

		PreparedStatement prst = DB.prepareStatement(sql1,null);
		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next()){
				if (!rs.getString(1).equals(dep) || !rs.getString(2).equals(lin) || !rs.getString(3).equals(sec))
				{
					dep = rs.getString(1);
					lin = rs.getString(2);
					sec = rs.getString(3);
					X_I_XX_Prod02i impItab = new X_I_XX_Prod02i (getCtx(), 0, get_TrxName());
					impItab.setXX_CodDep(rs.getString(1));
					impItab.setXX_CodLin(rs.getString(2));
					impItab.setXX_Codsec(rs.getString(3));
					impItab.setXX_TipCar(rs.getInt(4));
					impItab.setXX_TIPCAR1(rs.getInt(5));
					impItab.setXX_TIPCAR2(rs.getInt(6));
					impItab.save();		
				}
				
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al crear las caracteristicas de tres " + e);			
		}	
*/		
/**		// insertamos las que tienen dos caracteristicas
		String sql1 = "select a.XX_CODDEP,a.XX_CODLIN,a.XX_CODSEC,a.xx_tipcar, b.xx_tipcar " +
						"from " +
							"(select xx_coddep, xx_codlin, xx_codsec, xx_tipcar from i_xx_prod02 where xx_vmr_section_id in (select xx_vmr_section_id from  I_XX_PROD02 group by xx_vmr_section_id having count(*)=3) order by xx_coddep, xx_codlin, xx_codsec) a, " +
							"(select xx_coddep, xx_codlin, xx_codsec, xx_tipcar from i_xx_prod02 where xx_vmr_section_id in (select xx_vmr_section_id from  I_XX_PROD02 group by xx_vmr_section_id having count(*)=3) order by xx_coddep, xx_codlin, xx_codsec) b " +
							"where a.xx_coddep=b.xx_coddep " +
							"and a.XX_CODLIN = b.XX_CODLIN " +
							"and a.XX_CODSEC = b.XX_CODSEC " +
							"and a.xx_tipcar != b.xx_tipcar " +
							"and a.xx_tipcar != b.xx_tipcar " +
						"order by a.XX_CODDEP,a.XX_CODLIN,a.XX_CODSEC";
		
		String dep = "";
		String lin = "";
		String sec = "";

		PreparedStatement prst = DB.prepareStatement(sql1,null);
		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next()){
				if (!rs.getString(1).equals(dep) || !rs.getString(2).equals(lin) || !rs.getString(3).equals(sec))
				{
					dep = rs.getString(1);
					lin = rs.getString(2);
					sec = rs.getString(3);
					X_I_XX_Prod02i impItab = new X_I_XX_Prod02i (getCtx(), 0, get_TrxName());
					impItab.setXX_CodDep(rs.getString(1));
					impItab.setXX_CodLin(rs.getString(2));
					impItab.setXX_Codsec(rs.getString(3));
					impItab.setXX_TipCar(rs.getInt(4));
					impItab.setXX_TIPCAR1(rs.getInt(5));
					impItab.save();		
				}
				
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al crear las caracteristicas de dos " + e);			
		}
		
		// insertamos las que tienen una caracteristica
		sql1 = "select a.XX_CODDEP,a.XX_CODLIN,a.XX_CODSEC,a.xx_tipcar " +
						"from " +
							"(select xx_coddep, xx_codlin, xx_codsec, xx_tipcar from i_xx_prod02 where xx_vmr_section_id in (select xx_vmr_section_id from  I_XX_PROD02 group by xx_vmr_section_id having count(*)=3) order by xx_coddep, xx_codlin, xx_codsec) a " +
						"order by a.XX_CODDEP,a.XX_CODLIN,a.XX_CODSEC";
		
		dep = "";
		lin = "";
		sec = "";

		prst = DB.prepareStatement(sql1,null);
		try {
			ResultSet rs = prst.executeQuery();
			while (rs.next()){
				if (!rs.getString(1).equals(dep) || !rs.getString(2).equals(lin) || !rs.getString(3).equals(sec))
				{
					dep = rs.getString(1);
					lin = rs.getString(2);
					sec = rs.getString(3);
					X_I_XX_Prod02i impItab = new X_I_XX_Prod02i (getCtx(), 0, get_TrxName());
					impItab.setXX_CodDep(rs.getString(1));
					impItab.setXX_CodLin(rs.getString(2));
					impItab.setXX_Codsec(rs.getString(3));
					impItab.setXX_TipCar(rs.getInt(4));
					impItab.save();		
				}
				
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println("Error al crear las caracteristicas de una " + e);			
		}
*/		
		
			
		String ts = DB. isPostgreSQL ()? "COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg" ; //java bug, it could not be used directly 	
		
		// Ahora pasamos a los updates para la asignacion de ids
		
/**		
		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_XX_Prod02i "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(s_AD_Client_ID).append("),"
			+ " AD_Org_ID = 0,"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Reset=" + no);
		//

		commit();
		sql= new StringBuffer ("UPDATE I_XX_Prod02i i "
				+ "SET XX_VMR_DEPARTMENT_ID=(SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT d "
				+ "WHERE i.XX_CODDEP = d.VALUE)"
				+ " WHERE i.XX_VMR_DEPARTMENT_ID IS NULL AND i.XX_CODDEP IS NOT NULL"
				+ " "); 
		no = DB. executeUpdate (get_Trx(), sql.toString()); 
		log .fine( "Found Department=" + no); 
		commit();
		sql= new StringBuffer ("UPDATE I_XX_Prod02i i "
				+ "SET XX_VMR_LINE_ID=(SELECT XX_VMR_LINE_ID FROM XX_VMR_LINE l "
				+ "WHERE i.XX_CODLIN = l.VALUE AND l.XX_VMR_DEPARTMENT_ID= i.XX_VMR_DEPARTMENT_ID )"
				+ " WHERE i.XX_VMR_LINE_ID IS NULL AND i.XX_CODLIN IS NOT NULL "
				+ " "); 
		no = DB. executeUpdate (get_Trx(), sql.toString()); 
		log .fine( "Found Line=" + no); 
		ts = DB. isPostgreSQL ()? "COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg" ; //java bug, it could not be used directly
		commit();
		
		sql= new StringBuffer ("UPDATE I_XX_Prod02i i "
				+ "SET XX_VMR_SECTION_ID=(SELECT XX_VMR_SECTION_ID FROM XX_VMR_SECTION s "
				+ "WHERE i.XX_CODSEC = s.VALUE AND s.XX_VMR_LINE_ID= i.XX_VMR_LINE_ID)"
				+ " WHERE i.XX_VMR_SECTION_ID IS NULL AND i.XX_CODSEC IS NOT NULL"
				+ " "); 
		no = DB. executeUpdate (get_Trx(), sql.toString()); 
		log .fine( "Found Section=" + no); 
		ts = DB. isPostgreSQL ()? "COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg" ; //java bug, it could not be used directly
		commit();
		
		*/
/**		// Seteo de los nombres de las caracteristicas
		sql= new StringBuffer ("UPDATE I_XX_Prod02i i "
				+ "SET XX_NOMCAR=(SELECT XX_NOMCAR FROM I_XX_Prod02 p "
				+ "WHERE p.xx_tipcar = i.xx_tipcar and rownum=1)"
				+ " WHERE  i.xx_tipcar IS NOT NULL"
				+ " "); 
		no = DB. executeUpdate (get_Trx(), sql.toString()); 
		log .fine( "Found nommcar=" + no); 
		ts = DB. isPostgreSQL ()? "COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg" ; //java bug, it could not be used directly
		commit();
		
		// Seteo de los nombres de las caracteristicas
		sql= new StringBuffer ("UPDATE I_XX_Prod02i i "
				+ "SET XX_NOMCAR1=(SELECT XX_NOMCAR FROM I_XX_Prod02 p "
				+ "WHERE p.xx_tipcar = i.xx_tipcar1 and rownum=1)"
				+ " WHERE i.xx_tipcar1 IS NOT NULL"
				+ " "); 
		no = DB. executeUpdate (get_Trx(), sql.toString()); 
		log .fine( "Found nommcar=" + no); 
		ts = DB. isPostgreSQL ()? "COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg" ; //java bug, it could not be used directly
		commit();
		
		// Seteo de los nombres de las caracteristicas
		sql= new StringBuffer ("UPDATE I_XX_Prod02i i "
				+ "SET XX_NOMCAR2=(SELECT XX_NOMCAR FROM I_XX_Prod02 p "
				+ "WHERE p.xx_tipcar = i.xx_tipcar2 and rownum=1)"
				+ " WHERE  i.xx_tipcar2 IS NOT NULL"
				+ " "); 
		no = DB. executeUpdate (get_Trx(), sql.toString()); 
		log .fine( "Found nommcar=" + no); 
		ts = DB. isPostgreSQL ()? "COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg" ; //java bug, it could not be used directly
		commit();
*/		
		
/**		
		sql = new StringBuffer ("UPDATE I_XX_Prod02i i "
				+ "SET XX_VMR_DYNAMICCHARACT_ID=(SELECT XX_VMR_DYNAMICCHARACT_ID FROM XX_VMR_DYNAMICCHARACT "
				+ "WHERE i.XX_VMR_DEPARTMENT_ID = XX_VMR_DEPARTMENT_ID AND i.XX_VMR_LINE_ID = XX_VMR_LINE_ID AND i.XX_VMR_SECTION_ID = XX_VMR_SECTION_ID AND AD_CLIENT_ID=i.AD_CLIENT_ID)"
				+ "WHERE i.XX_VMR_DYNAMICCHARACT_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL AND I_IsImported='N'" ).append(clientCheck); 
		no = DB. executeUpdate (get_Trx(), sql.toString()); 
		log .fine( "Found Dynamic Characteristic=" + no); 
		ts = DB. isPostgreSQL ()? "COALESCE(I_ErrorMsg,'')" : "I_ErrorMsg" ; //java bug, it could not be used directly 
		commit(); 
		
		
		sql = new StringBuffer ("UPDATE I_XX_Prod02i i "
				+ "SET XX_ISFIELDERROR = 'E' "
				+ "WHERE ((i.XX_VMR_DEPARTMENT_ID is null) or (i.XX_VMR_LINE_ID is null) or (i.XX_VMR_SECTION_ID is null))"
				+ " AND I_IsImported='N'" ).append(clientCheck); 
		no = DB. executeUpdate (get_Trx(), sql.toString()); 
*/		
			//	Go through Records
			log.fine("start inserting...");
			sql = new StringBuffer ("SELECT distinct XX_TIPCAR,XX_TIPCAR1,XX_TIPCAR2,XX_NOMCAR,XX_NOMCAR1,XX_NOMCAR2 FROM I_XX_Prod02i"
				+ " WHERE I_IsImported='N' AND (XX_ISFIELDERROR!='E' or xx_isfielderror is null) ").append(clientCheck)
				.append(" GROUP BY XX_TIPCAR,XX_TIPCAR1,XX_TIPCAR2,XX_NOMCAR,XX_NOMCAR1,XX_NOMCAR2");
			System.out.println(sql);

			
			try
			{
				PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
				ResultSet rs = pstmt.executeQuery();	
				while(rs.next())
				{
					
					//-----------------------------------------------------------------------------------
					
/**					////////// IMPORTACION DE ATTRIBUTE SET/USE
					MAttributeSet set= new MAttributeSet(getCtx(), 0, get_TrxName());
					String atributos=" ";
					if(rs.getString("XX_NOMCAR")!=null){
						atributos=rs.getString("XX_NOMCAR");
						//System.out.println("Nomcar "+rs.getString("XX_NOMCAR") + " TipCar " + rs.getBigDecimal("XX_TIPCAR"));
						set.setXX_TipCar(rs.getBigDecimal("XX_TIPCAR"));
					}
					if(rs.getString("XX_NOMCAR1")!=null){
						atributos=atributos+"_"+rs.getString("XX_NOMCAR1");
						//System.out.println("Nomcar1 "+rs.getString("XX_NOMCAR1") + " TipCar1 " + rs.getBigDecimal("XX_TIPCAR1"));
						set.setXX_Tipcar1(rs.getBigDecimal("XX_TIPCAR1"));
					}
					if(rs.getString("XX_NOMCAR2")!=null){
						atributos=atributos+"_"+rs.getString("XX_NOMCAR2");
						//System.out.println("Nomcar2 "+rs.getString("XX_NOMCAR2") + " TipCar2 " + rs.getBigDecimal("XX_TIPCAR2"));
						set.setXX_TipCar2(rs.getBigDecimal("XX_TIPCAR2"));
					}
					//set.setName(atributos);
					//set.setDescription(atributos);
					set.setXX_Importing(true);
					set.save();
					commit();
					
					
					if(rs.getString("XX_NOMCAR")!=null){
						StringBuffer sql2 = new StringBuffer("SELECT * FROM M_Attribute WHERE VALUE="+rs.getString("XX_TIPCAR")).append(clientCheck);
						PreparedStatement pstmt2=DB.prepareStatement(sql2.toString(), get_TrxName());
						ResultSet rs2 = pstmt2.executeQuery();
						if(rs2.next()){
							MAttributeUse algo= new MAttributeUse(getCtx(), 0, get_TrxName());
							algo.setM_AttributeSet_ID(set.getM_AttributeSet_ID());
							algo.setM_Attribute_ID(rs2.getInt("M_Attribute_ID"));
							algo.setSeqNo(10);
							algo.setValue(rs.getString("XX_TIPCAR"));
							algo.save();
							commit();
							
						}
						rs2.close();
						pstmt2.close();
					}
					if(rs.getString("XX_NOMCAR1")!=null){
						StringBuffer sql2 = new StringBuffer("SELECT * FROM M_Attribute WHERE VALUE="+rs.getString("XX_TIPCAR1")).append(clientCheck);
						PreparedStatement pstmt2=DB.prepareStatement(sql2.toString(), get_TrxName());
						ResultSet rs2 = pstmt2.executeQuery();
						if(rs2.next()){
							MAttributeUse algo= new MAttributeUse(getCtx(), 0, get_TrxName());
							algo.setM_AttributeSet_ID(set.getM_AttributeSet_ID());
							algo.setM_Attribute_ID(rs2.getInt("M_Attribute_ID"));
							algo.setSeqNo(20);
							algo.setValue(rs.getString("XX_TIPCAR1"));
							algo.save();
							commit();
							
						}
						rs2.close();
						pstmt2.close();
					}
					
					if(rs.getString("XX_NOMCAR2")!=null){
						StringBuffer sql2 = new StringBuffer("SELECT * FROM M_Attribute WHERE VALUE="+rs.getString("XX_TIPCAR2")).append(clientCheck);
						PreparedStatement pstmt2=DB.prepareStatement(sql2.toString(), get_TrxName());
						ResultSet rs2 = pstmt2.executeQuery();
						if(rs2.next()){
							MAttributeUse algo= new MAttributeUse(getCtx(), 0, get_TrxName());
							algo.setM_AttributeSet_ID(set.getM_AttributeSet_ID());
							algo.setM_Attribute_ID(rs2.getInt("M_Attribute_ID"));
							algo.setSeqNo(30);
							algo.setValue(rs.getString("XX_TIPCAR2"));
							algo.save();
							commit();
							
						}
						rs2.close();
						pstmt2.close();
					}
					
*/		
					////////// IMPORTACION DE DYNAMIC CHARACTERISTICS
					//-----------------------------------------------------------------------------------
				} // end while*/
				int noInsert = 0;		
/**				sql = new StringBuffer ("SELECT * FROM I_XX_Prod02i");
				pstmt = DB.prepareStatement(get_Trx(), sql.toString());
				rs = pstmt.executeQuery();
				
				X_XX_VMR_DynamicCharact impXtab = null;
				
				while(rs.next())
				{
					X_I_XX_Prod02i impItab = new X_I_XX_Prod02i (getCtx(), rs, get_TrxName());
					log.fine("I_XX_Prod02i_ID=" + impItab.getI_XX_PROD02I_ID());
					
                         //***	Create/Update Solm01	****
					
					
					// Pregunto por ID de la tabla compiere que es igual al de la tabla I //
					if(impItab.getXX_VMR_DynamicCharact_ID() == 0)
					{
						impXtab = new X_XX_VMR_DynamicCharact(getCtx(), 0, get_TrxName());
					}	
					else  
					{
						impXtab = 	new X_XX_VMR_DynamicCharact(getCtx(), impItab.getXX_VMR_DynamicCharact_ID(), get_TrxName());
					}
					
					// Get and set
					String car = null;//M_AttributSet_ID de dynamic
					String name =  new String();//Name del Attbset en M_AttrSet
					String nombre =  new String();//Nombre caracteristica en prod02	
					//String name2 =  new String();//Name del Attbset en M_AttrSet
					nombre = impItab.getXX_NomCar();
					



					//****Selecciono el M_ATTRIBUTESET_ID correspondiente a la combinacion de c�digos de dpto, linea y seccion*****
					String sql1 = new String ( "SELECT M_ATTRIBUTESET_ID FROM XX_VMR_DYNAMICCHARACT" 
							+ " WHERE XX_VMR_DEPARTMENT_ID= "+ impItab.getXX_VMR_Department_ID()+" " 
							+"AND XX_VMR_LINE_ID = "+ impItab.getXX_VMR_Line_ID()+ " " 
							+"AND XX_VMR_SECTION_ID = "+ impItab.getXX_VMR_Section_ID() );
					////
					PreparedStatement pstmt1 = DB. prepareStatement (sql1.toString(), get_TrxName()); 
					ResultSet rs1 = pstmt1.executeQuery();		
					System.out.println("aca " + sql1);
					if(rs1.next()){
						car = rs1.getString("M_ATTRIBUTESET_ID");//Obtengo el ID del Attribute Set
						
					}
					rs1.close();
					pstmt1.close();
					////
					//System.out.println(car);
					if(car != null){
						String sql2 = new String ( "SELECT NAME FROM XX_VMR_DYNAMICCHARACT" 
							+ " WHERE M_ATTRIBUTESET_ID= "+ car );
						System.out.println("aca2 " + sql2);
						PreparedStatement pstmt2 = DB. prepareStatement (sql2.toString(), get_TrxName()); 
						ResultSet rs2 = pstmt2.executeQuery();				
						if(rs2.next()){
							name = rs2.getString("NAME");//Obtengo el name del Attribute Set
							
						}
						rs2.close();
						pstmt2.close();
						//Si son distintos los nombres de tabla de importacion y compiere
						if(!name.equals(nombre)){
							String nfinal2 = new String();
							nfinal2 = name+" "+nombre;
							String sql3 = new String ( "UPDATE XX_VMR_DYNAMICCHARACT" 
									+ " SET NAME ='"+ nfinal2+"'"
									+" WHERE XX_VMR_DEPARTMENT_ID=" +impItab.getXX_VMR_Department_ID()
									+" AND XX_VMR_LINE_ID="+impItab.getXX_VMR_Line_ID()
									+" AND XX_VMR_SECTION_ID="+impItab.getXX_VMR_Section_ID()		
							);
							System.out.println("aca3 " + sql3);
							no = DB.executeUpdate(sql3.toString(), get_TrxName()); 
							log.finest("Insert/Update Dynamic Characteristic - " +  impXtab.getXX_VMR_DynamicCharact_ID());
							noInsert++;
							impItab.setI_IsImported(true);
							impItab.setProcessed(true);
							impItab.setProcessing(false);
							impItab.save();		
						}//if name
						
					}//if car
					else {
						// updateo la tabla attribute set para que si una tipcar es null ponga cero en su lugar
						String sql3 = new String ( "UPDATE m_attributeset" 
								+ " SET xx_tipcar1=0 where xx_tipcar1 is null"
						);
						System.out.println("aca3 " + sql3);
						no = DB.executeUpdate(sql3.toString(), get_TrxName()); 
						commit();
						
						sql3 = new String ( "UPDATE m_attributeset" 
								+ " SET xx_tipcar2=0 where xx_tipcar2 is null"
						);
						System.out.println("aca3 " + sql3);
						no = DB.executeUpdate(sql3.toString(), get_TrxName()); 
						commit();
						
						// busco el m_attribute set correcto
						String sql2 = new String ( "SELECT m_attributeset_id FROM m_attributeset" 
								+ " WHERE xx_tipcar= "+ impItab.getXX_TipCar() + " and xx_tipcar1=coalesce("+ impItab.getXX_TIPCAR1()+ ",0) and xx_tipcar2=coalesce("+ impItab.getXX_TIPCAR2()+",0)");
							System.out.println(sql2);
							Integer attributeset = new Integer(0);
							PreparedStatement pstmt2 = DB. prepareStatement (sql2.toString(), get_TrxName()); 
							ResultSet rs2 = pstmt2.executeQuery();							
							if(rs2.next()){
								attributeset = rs2.getInt(1);
								
							}
							rs2.close();
							pstmt2.close();

							
						System.out.println("attributeset " + attributeset);
						impXtab.setName(impItab.getXX_NomCar()+'_'+impItab.getXX_NomCar1()+'_'+impItab.getXX_NomCar2());
						impXtab.setValue(impItab.getXX_TipCar()+"_"+impItab.getXX_TIPCAR1()+"_"+impItab.getXX_TIPCAR2());
						impXtab.setXX_VMR_Department_ID(impItab.getXX_VMR_Department_ID());
						impXtab.setXX_VMR_Line_ID(impItab.getXX_VMR_Line_ID());
						impXtab.setXX_VMR_Section_ID(impItab.getXX_VMR_Section_ID());	
						impXtab.setM_AttributeSet_ID(attributeset);
						System.out.println("impXtab.setName " + impXtab.getName());
						System.out.println("impXtab.setValue() " + impXtab.getValue());
						System.out.println("impXtab.department " + impXtab.getXX_VMR_Department_ID());
						System.out.println("impXtab.line " + impXtab.getXX_VMR_Line_ID());
						System.out.println("impXtab.section " + impXtab.getXX_VMR_Section_ID());
						/////
						if(impXtab.save())
						{
							
							System.out.println("impXtab " + impXtab.get_ID());
							System.out.println("salvo ");
							log.finest("Insert/Update Dynamic Characteristic - " +  impXtab.getXX_VMR_DynamicCharact_ID());
							noInsert++;
							impItab.setI_IsImported(true);
							impItab.setProcessed(true);
							impItab.setProcessing(false);
							impItab.save();
							commit();
						}//if save
						else
						{		System.out.println("error");
							rollback();
							noInsert--;						
							sql = new StringBuffer ("UPDATE I_XX_Prod02i i "
							+ "SET XX_ISFIELDERROR='E', I_ErrorMsg=" +ts + "|| '" )
							.append( "Cannot Insert Prod02..." )
							.append( "' WHERE I_XX_Prod02i_ID=" ).append(impItab.getI_XX_PROD02I_ID());
							DB.executeUpdate (get_Trx(), sql.toString());
							//continue;		
						}//else save
					}//else car
					
 				 //Get and set
					
				}//end while2
*/				
				commit();
				
				rs.close();
				pstmt.close();

			}//try
			catch (SQLException e) 
			{ 
				log .log(Level. SEVERE , "" , e); 
				rollback(); 
			}//catch 
			
			// Aca modifico el name en la xx_vmr_dynamic charac para que tenga el mismo tama�o que el del attribute set
			
			sql = new StringBuffer ( "update XX_VMR_DYNAMICCHARACT d " +
					"set name = substr(name,0,(select length(name) from m_attributeset m where d.m_attributeset_id=m.m_attributeset_id)) " ); 
			no = DB. executeUpdate (get_Trx(), sql.toString());
			
			

			// Set Error to indicator to not imported 
			sql = new StringBuffer ( "UPDATE I_XX_Prod02i " 
			+ "SET I_IsImported='N', Updated=SysDate " 
			+ "WHERE I_IsImported<>'Y'" ).append(clientCheck); 
			no = DB. executeUpdate (get_Trx(), sql.toString()); 
			addLog (0, null , new BigDecimal (no), "@Errors@" ); 
//			addLog (0, null , new BigDecimal (noInsert), "@XX_VMR_DynamicCharact_ID@: @Inserted@" ); 
			return "" ; 
	}//doIt 
	
}//fin
