	/**
 * 
 */
package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MAttributeInstance;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.X_I_Product;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPrice;
//import org.compiere.model.X_I_Product;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;


/**
 * @author Patricia Ayuso (basada en AAvila)
 *
 */
public class ImportMProduct extends SvrProcess{
	/**	Client to be imported to		*/
	private int				m_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			m_deleteOldImported = false;

	/** Effective						*/
	private Timestamp		m_DateValue = null;
	/** Pricelist to Update				*/
	private int 			p_M_PriceList_Version_ID = 0;
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
				m_AD_Client_ID = ((BigDecimal)element.getParameter()).intValue();
			else if (name.equals("DeleteOldImported"))
				m_deleteOldImported = "Y".equals(element.getParameter());
			else if (name.equals("M_PriceList_Version_ID"))
				p_M_PriceList_Version_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		if (m_DateValue == null)
			m_DateValue = new Timestamp (System.currentTimeMillis());
	}
	@Override
	protected String doIt() throws Exception {
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + m_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		/*if (m_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_Product "
				+ "WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.info("Delete Old Impored =" + no);
		}*/

		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly

		//	Set Client, Org, IaActive, Created/Updated, 	ProductType
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(m_AD_Client_ID).append("),"
			+ " AD_Org_ID = 0,"
			+ " IsActive = COALESCE (IsActive, 'Y'),"
			+ " Created = COALESCE (Created, SysDate),"
			+ " CreatedBy = COALESCE (CreatedBy, 0),"
			+ " Updated = COALESCE (Updated, SysDate),"
			+ " UpdatedBy = COALESCE (UpdatedBy, 0),"
			+ " ProductType = COALESCE (ProductType, 'I'),"
			+ " I_ErrorMsg = NULL,"
			+ " I_IsImported = 'N' "
			+ "WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Reset=" + no);
		commit();
		
		//Vendor
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET C_BPARTNER_ID=(SELECT C_BPARTNER_ID FROM C_BPARTNER B"
				+ " WHERE i.XX_COEMPE = b.Value ) "
				+ " WHERE XX_COEMPE IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set vendor=" + no);
		commit();
		System.out.println("proveedor");
		
		
		
		System.out.println("inicio");
		//Department
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET XX_VMR_DEPARTMENT_ID=(SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT d"
				+ " WHERE i.XX_Departamento_value = d.Value) "
				+ " WHERE XX_Departamento_value IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Department=" + no);
		commit();
		System.out.println("dep");
		
		//Category
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET XX_VMR_CATEGORY_ID=(SELECT XX_VMR_CATEGORY_ID FROM XX_VMR_DEPARTMENT d"
				+ " WHERE i.XX_Departamento_value = d.Value) "
				+ " WHERE XX_Departamento_value IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Category=" + no);
		commit();
		System.out.println("cat");
		
		//Linea 
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET XX_VMR_LINE_ID=(SELECT XX_VMR_LINE_ID FROM XX_VMR_LINE l"
				+ " WHERE i.XX_Linea_value = l.Value " 
				+ " AND l.XX_VMR_DEPARTMENT_ID = i.XX_VMR_DEPARTMENT_ID) "
				+ " WHERE XX_Linea_value IS NOT NULL AND XX_VMR_DEPARTMENT_ID IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Line=" + no);
		commit();
		System.out.println("lin");
			
		//Seccion
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET XX_VMR_SECTION_ID=(SELECT XX_VMR_SECTION_ID FROM XX_VMR_SECTION s"
				+ " WHERE i.XX_Seccion_value=s.Value AND XX_VMR_LINE_ID = i.XX_VMR_LINE_ID"
				+ " ) "
				+ " WHERE XX_Seccion_value IS NOT NULL AND XX_VMR_LINE_ID IS NOT NULL"
				+ " AND XX_VMR_DEPARTMENT_ID IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Section=" + no);
		commit();
		System.out.println("sec");
	
		//Brand
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET XX_VMR_BRAND_ID=(SELECT XX_VMR_BRAND_ID FROM XX_VMR_BRAND b"
				+ " WHERE i.XX_MARCA = b.Value ) "
				+ " WHERE XX_MARCA IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Brand=" + no);
		commit();
		System.out.println("marca");
		
		
		// Concept Value
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET i.XX_VME_CONCEPTVALUE_ID=(SELECT c.XX_VME_CONCEPTVALUE_ID FROM XX_VMR_CONCEPTVALDPTBRAND c"
				+ " WHERE c.XX_VMR_BRAND_ID = i.XX_VMR_BRAND_ID AND c.XX_VMR_DEPARTMENT_ID = i.XX_VMR_DEPARTMENT_ID) where  I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set concept value=" + no);
		commit();
		System.out.println("concepto");
		
		// concept value continuation
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET i.XX_VME_CONCEPTVALUE_ID=(SELECT c.XX_VME_CONCEPTVALUE_ID FROM XX_VME_CONCEPTVALUE c"
				+ " WHERE c.name='BUENO') where i.XX_VME_CONCEPTVALUE_ID is null and (I_IsImported<>'Y' OR I_IsImported IS NULL)" );
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set concept value=" + no);
		commit();
		System.out.println("2");
		
		
		//Tax Category
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET C_TAXCATEGORY_ID=(SELECT C_TAXCATEGORY_ID FROM C_TAXCATEGORY d"
				+ " WHERE name='IVA' ) "
				+ " WHERE XX_TIPIMP='2' and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Tax Category=" + no);
		commit();
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET C_TAXCATEGORY_ID=(SELECT C_TAXCATEGORY_ID FROM C_TAXCATEGORY d"
				+ " WHERE name='EXENTO' ) "
				+ " WHERE XX_TIPIMP='1' and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Tax Category=" + no);
		commit();
		System.out.println("tax category");
		
		/**
		// updateo de la caracteristica larga
		sql = new StringBuffer ("UPDATE I_product i " 
				+ " SET XX_VMR_LONGCHARACTERISTIC_ID = (SELECT t.XX_VMR_LONGCHARACTERISTIC_ID FROM I_XX_TABM02 t WHERE i.XX_CARACT=t.XX_CARACT and i.xx_vmr_section_id=t.xx_vmr_section_id and rownum=1) "
				+ " where I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Purchase =" + no);	
		commit();
		System.out.println("caracteristica larga");
		*/
		
		//Attribute Set
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET i.M_ATTRIBUTESET_ID = " 
				+ " (SELECT d.m_attributeset_ID " 
				+ " FROM XX_VMR_DynamicCharact d "
				+ " WHERE i.XX_VMR_Section_ID = d.XX_VMR_Section_ID)" 
				+ " WHERE i.XX_VMR_Section_ID IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Attribute Set=" + no);
		commit();
		System.out.println("6");


		//Product class
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET XX_VMR_PRODUCTCLASS_ID=(select xx_vmr_productclass_id from xx_vmr_productclass where name='NO TEXTIL') where XX_TIPOPRODUCTO ='NT' and (I_IsImported<>'Y' OR I_IsImported IS NULL)" 
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Product Class=" + no);
		commit();
		System.out.println("no textil");
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET XX_VMR_PRODUCTCLASS_ID=(select xx_vmr_productclass_id from xx_vmr_productclass where name='TEXTIL') where XX_TIPOPRODUCTO ='TE' and (I_IsImported<>'Y' OR I_IsImported IS NULL)" 
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Product Class=" + no);
		commit();
		System.out.println("textil");
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET XX_VMR_PRODUCTCLASS_ID=(select xx_vmr_productclass_id from xx_vmr_productclass where name='COMBINADO') where XX_TIPOPRODUCTO ='CO' and (I_IsImported<>'Y' OR I_IsImported IS NULL)" 
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Product Class=" + no);
		commit();
		System.out.println("combinado");
		
		

		//Country
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET C_COUNTRY_ID=(SELECT C_COUNTRY_ID FROM C_COUNTRY d"
				+ " WHERE i.XX_COUNTRY_VALUE = d.Value and d.isactive='Y') "
				+ " WHERE XX_COUNTRY_VALUE IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Country=" + no);
		commit();
		System.out.println("pais");
		
		//Currency
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET C_CURRENCY_ID=(SELECT C_CURRENCY_ID FROM C_COUNTRY d"
				+ " WHERE i.C_COUNTRY_ID = d.C_COUNTRY_ID ) "
				+ " WHERE C_COUNTRY_ID IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Currency=" + no);
		System.out.println("moneda");
		commit();
		
		//Label Type
		sql = new StringBuffer ("UPDATE I_Product i " 
				+ " SET XX_VMR_TypeLabel_ID= (CASE WHEN (XX_TIPOETIQUETA ='EN') " 
				+ " THEN (select XX_VMR_TypeLabel_ID from XX_VMR_TypeLabel where name='ENGOMADA') ELSE (select XX_VMR_TypeLabel_ID from XX_VMR_TypeLabel where name='COLGANTE') END)" 
				+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Label Type=" + no);
		System.out.println("tipo etiqueta");
		commit();
		
		//Inventory Type
		sql = new StringBuffer ("UPDATE I_Product i " 
				+ " SET XX_VMR_TYPEINVENTORY_ID = (CASE WHEN (XX_TIPOINVENTARIO ='BA') " 
				+ " THEN (select XX_VMR_TYPEINVENTORY_ID from XX_VMR_TYPEINVENTORY where value='B') ELSE (select XX_VMR_TYPEINVENTORY_ID from XX_VMR_TYPEINVENTORY where value='T') END)" 
				+ " WHERE I_IsImported<>'Y' OR I_IsImported IS NULL");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Inventory Type=" + no);	
		System.out.println("tipo inv");
		commit();	
				

//		Updating Unit Purchase
		sql = new StringBuffer ("UPDATE i_product i " 
				+ " SET XX_VMR_UNITPURCHASE_ID = " 
				+ " (CASE WHEN UNICOM ='BO' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='BO')"
				+ "       WHEN UNICOM ='BU' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='BU')"
				+ "       WHEN UNICOM ='CA' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='DO' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='DO')"
				+ "       WHEN UNICOM ='EM' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='EM')"
				+ "       WHEN UNICOM ='ES' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='ES')"
				+ "       WHEN UNICOM ='JG' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='JG')"
				+ "       WHEN UNICOM ='PA' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='PA')"
				+ "       WHEN UNICOM ='PR' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='PR')"
				+ "       WHEN UNICOM ='PZ' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='PZ')"
				+ "       WHEN UNICOM ='IN' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='IN')"
				+ "       WHEN UNICOM ='SE' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='SE')"
				+ "       WHEN UNICOM ='C2' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='C3' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='C4' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='C5' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='C8' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='C6' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='CB' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='CC' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='CD' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='CE' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNICOM ='2P' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='2P')"
				+ " ELSE 0 END)"
				+ " WHERE UNICOM IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Purchase =" + no);	
		commit();
		System.out.println("unit purchase");
		
		//	Updating Unit Sale
		sql = new StringBuffer ("UPDATE i_product i " 
				+ " SET XX_VMR_UNITSALE = " 
				+ " (CASE WHEN UNIVEN ='BO' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='BO')"
				+ "       WHEN UNIVEN ='BU' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='BU')"
				+ "       WHEN UNIVEN ='CA' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='DO' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='DO')"
				+ "       WHEN UNIVEN ='EM' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='EM')"
				+ "       WHEN UNIVEN ='ES' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='ES')"
				+ "       WHEN UNIVEN ='JG' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='JG')"
				+ "       WHEN UNIVEN ='PA' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='PA')"
				+ "       WHEN UNIVEN ='PR' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='PR')"
				+ "       WHEN UNIVEN ='PZ' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='PZ')"
				+ "       WHEN UNIVEN ='IN' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='IN')"
				+ "       WHEN UNIVEN ='SE' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='SE')"
				+ "       WHEN UNIVEN ='C2' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='C3' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='C4' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='C5' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='C8' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='C6' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='CB' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='CC' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='CD' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='CE' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='CA')"
				+ "       WHEN UNIVEN ='2P' THEN (SELECT XX_VMR_UNITPURCHASE_ID FROM XX_VMR_UNITPURCHASE WHERE VALUE='2P')"
				+ " ELSE 0 END)"
				+ " WHERE UNIVEN IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Sale =" + no);	
		commit();
		System.out.println("unit sale");
		
//		Updating Unit Conversion Purchase
		sql = new StringBuffer ("UPDATE I_product i " 
				+ " SET XX_VMR_UnitConversion_ID = " 
				+ " (CASE WHEN UNICOM ='BO' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='BU' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CA' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='DO' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='12')"
				+ "       WHEN UNICOM ='EM' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='ES' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='JG' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='PA' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='PR' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='PZ' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='IN' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='SE' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C2' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='2'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C3' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='3'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C4' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='4'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C5' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='5'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C8' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='8'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C6' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='6'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CB' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='20'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CC' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='24'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CD' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='36'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CE' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='48'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='2P' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = u.XX_VMR_UNITPURCHASE_ID)"
				+ " ELSE null END)"
				+ " WHERE UNICOM IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Conversion Purchase =" + no);	
		commit();		
		System.out.println("unit conversion purchase");
		
		//Updating Unit Conversion Sale
		sql = new StringBuffer ("UPDATE I_product i " 
				+ " SET XX_PIECESBYSALE_ID = " 
				+ " (CASE WHEN UNIVEN ='BO' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='BU' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CA' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='DO' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='12'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='EM' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='ES' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='JG' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='PA' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='PR' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='PZ' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='IN' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='SE' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C2' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='2'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C3' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='3'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C4' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='4'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C5' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='5'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C8' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='8'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C6' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='6'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CB' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='20'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CC' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='24'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CD' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='36'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CE' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='48'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='2P' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion u WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITSALE = u.XX_VMR_UNITPURCHASE_ID)"
				+ " ELSE null END)"
				+ " WHERE UNIVEN IS NOT NULL and (I_IsImported<>'Y' OR I_IsImported IS NULL)"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Conversion Sale =" + no);	
		commit();		
		System.out.println("unit conversion sale");
		
		
		
		
		
				System.out.println("va a hacer referencias con caracteristica larga");
		//Vendor Product Reference
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET i.XX_VMR_VENDORPRODREF_ID=(SELECT b.XX_VMR_VENDORPRODREF_ID " 
				+ " FROM XX_VMR_VENDORPRODREF b"
				+ " WHERE i.XX_VENDORPRODUCTREFERENCE_ID = b.Value "
				+ " AND b.xx_vmr_section_id = i.xx_vmr_section_id"
				+ " and  b.XX_VMR_BRAND_ID  = i.XX_VMR_BRAND_ID" 
				+ " and b.name = i.name" 
				+ " AND b.C_BPARTNER_ID=i.c_bpartner_id  "
				+ " and b.xx_vmr_longcharacteristic_ID=i.xx_vmr_longcharacteristic_ID "
				+ " and b.XX_VMR_UNITPURCHASE_ID=i.XX_VMR_UNITPURCHASE_ID" 
				+ " and b.XX_SALEUNIT_ID=i.XX_VMR_UNITSALE" 
				+ " group by b.xx_vmr_vendorprodref_id )"
				+ " where i.xx_vmr_vendorprodref_id is null and (I_IsImported<>'Y' OR I_IsImported IS NULL)");
		System.out.println(sql);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Vendor Reference=" + no);
		commit();
		System.out.println("referencias con carac");

		//Vendor Product Reference sin caracteristica larga y nombre iguales que hacen match
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET i.XX_VMR_VENDORPRODREF_ID=(SELECT b.XX_VMR_VENDORPRODREF_ID " 
				+ " FROM XX_VMR_VENDORPRODREF b"
				+ " WHERE i.XX_VENDORPRODUCTREFERENCE_ID = b.Value "
				+ " AND b.xx_vmr_section_id = i.xx_vmr_section_id"
				+ " and  b.XX_VMR_BRAND_ID  = i.XX_VMR_BRAND_ID" 
				+ " and b.name = i.name" 
				+ " AND b.C_BPARTNER_ID=i.c_bpartner_id  "
				+ " and b.xx_vmr_longcharacteristic_ID is null "
				+ " and b.XX_VMR_UNITPURCHASE_ID=i.XX_VMR_UNITPURCHASE_ID" 
				+ " and b.XX_SALEUNIT_ID=i.XX_VMR_UNITSALE" 
				+ " group by b.xx_vmr_vendorprodref_id )"
				+ " where i.xx_vmr_vendorprodref_id is null and (I_IsImported<>'Y' OR I_IsImported IS NULL)");
		System.out.println(sql);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Vendor Reference=" + no);
		commit();
		System.out.println("referencias sin caract pero con nombres iguales");
		
				//Vendor Product Reference sin caracteristica larga con nombres diferentes
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET i.XX_VMR_VENDORPRODREF_ID=(SELECT b.XX_VMR_VENDORPRODREF_ID " 
				+ " FROM XX_VMR_VENDORPRODREF b"
				+ " WHERE i.XX_VENDORPRODUCTREFERENCE_ID = b.Value "
				+ " AND b.xx_vmr_section_id = i.xx_vmr_section_id"
				+ " and  b.XX_VMR_BRAND_ID  = i.XX_VMR_BRAND_ID" 
				+ " AND b.C_BPARTNER_ID=i.c_bpartner_id  "
				+ " and b.xx_vmr_longcharacteristic_ID is null "
				+ " and b.XX_VMR_UNITPURCHASE_ID=i.XX_VMR_UNITPURCHASE_ID" 
				+ " and b.XX_SALEUNIT_ID=i.XX_VMR_UNITSALE" 
				+ " group by b.xx_vmr_vendorprodref_id )"
				+ " where i.xx_vmr_vendorprodref_id is null and (I_IsImported<>'Y' OR I_IsImported IS NULL)");
		System.out.println(sql);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Vendor Reference=" + no);
		commit();
		System.out.println("referencias sin caract y con nombres distintos");
		
		System.out.println("va a hacer referencias con caracteristica larga y nombres distintos");
		//Vendor Product Reference
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET i.XX_VMR_VENDORPRODREF_ID=(SELECT b.XX_VMR_VENDORPRODREF_ID " 
				+ " FROM XX_VMR_VENDORPRODREF b"
				+ " WHERE i.XX_VENDORPRODUCTREFERENCE_ID = b.Value "
				+ " AND b.xx_vmr_section_id = i.xx_vmr_section_id"
				+ " and  b.XX_VMR_BRAND_ID  = i.XX_VMR_BRAND_ID" 
				+ " AND b.C_BPARTNER_ID=i.c_bpartner_id  "
				+ " and b.xx_vmr_longcharacteristic_ID=i.xx_vmr_longcharacteristic_ID "
				+ " and b.XX_VMR_UNITPURCHASE_ID=i.XX_VMR_UNITPURCHASE_ID" 
				+ " and b.XX_SALEUNIT_ID=i.XX_VMR_UNITSALE" 
				+ " group by b.xx_vmr_vendorprodref_id )"
				+ " where i.xx_vmr_vendorprodref_id is null and (I_IsImported<>'Y' OR I_IsImported IS NULL)");
		System.out.println(sql);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Vendor Reference=" + no);
		commit();
		System.out.println("referencias con carac");
	
		
		//Vendor productNo que contiene la misma vendorprodref
		sql = new StringBuffer ("UPDATE I_Product i "
				+ " SET i.VENDORPRODUCTNO=i.XX_VMR_VendorProdRef_ID where I_IsImported<>'Y' OR I_IsImported IS NULL");
		System.out.println(sql);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.info("Set Vendor Reference=" + no);
		commit();
		System.out.println("update");
		
		
	

		
		//Error de referencia
		sql = new StringBuffer ("UPDATE I_Product "
							+ " SET I_IsImported='E', I_ErrorMsg='ERR=No se encontro referencia,'||"+ ts
							+ " WHERE ((XX_VMR_CATEGORY_ID IS NULL) OR"
							+ " (XX_VMR_DEPARTMENT_ID IS NULL) OR "
							+ " (XX_VMR_LINE_ID IS NULL) OR"
							+ " (XX_VMR_SECTION_ID IS NULL) OR"
							+ " (XX_VMR_VENDORPRODREF_ID IS NULL))"
							+ " AND I_IsImported<>'Y' "); //AND issummary='N'
		no = DB.executeUpdate(get_Trx(), sql.toString());
		if (no != 0)
			log.warning("Invalid referencia=" + no);
		commit();
		System.out.println("11");
	
		
		//	-------------------------------------------------------------------

		int noInsert = 0;
		int noUpdate = 0;
		int noInsertPO = 0;
		int noUpdatePO = 0;
		
		
		//	Go through Records
		log.fine("start inserting/updating ...");
		sql = new StringBuffer ("SELECT * FROM I_Product WHERE I_IsImported='N'  and xx_vmr_vendorprodref_id is not null");// WHERE I_IsImported='N' ")	
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt_setImported = null;
		
		try {
			// Set Imported = Y
			pstmt_setImported = DB.prepareStatement   
			("UPDATE I_Product SET I_IsImported='Y', M_Product_ID=?, "
			+ "Updated=SysDate, Processed='Y' WHERE I_Product_ID=?", get_TrxName());
			
			pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			rs = pstmt.executeQuery();
			System.out.println("ya hizo el query");
	
			while(rs.next()){


				PreparedStatement pstmt_insertProductPO = DB.prepareStatement
				("INSERT INTO M_Product_PO (M_Product_ID,C_BPartner_ID, "
				+ "AD_Client_ID,AD_Org_ID,IsActive,Created,CreatedBy,Updated,UpdatedBy,"
				+ "IsCurrentVendor,C_UOM_ID,C_Currency_ID,UPC,"
				+ "PriceList,PricePO,RoyaltyAmt,PriceEffective,"
				+ "VendorProductNo,VendorCategory,Manufacturer,"
				+ "Discontinued,DiscontinuedBy,Order_Min,Order_Pack,"
				+ "CostPerOrder,DeliveryTime_Promised) "
				+ "SELECT ?,?, "
				+ "AD_Client_ID,AD_Org_ID,'Y',SysDate,CreatedBy,SysDate,UpdatedBy,"
				+ "'Y',C_UOM_ID,C_Currency_ID,UPC,"
				+ "PriceList,PricePO,RoyaltyAmt,PriceEffective,"
				+ "VendorProductNo,VendorCategory,Manufacturer,"
				+ "Discontinued,DiscontinuedBy,Order_Min,Order_Pack,"
				+ "CostPerOrder,DeliveryTime_Promised "
				+ "FROM I_Product "
				+ "WHERE I_Product_ID=?", get_TrxName());
				
				// Getting data from I_Product
				X_I_Product imp = new X_I_Product(getCtx(), rs, get_TrxName());
				int I_Product_ID = imp.getI_Product_ID();
				int M_Product_ID = imp.getM_Product_ID();
				int C_BPartner_ID = imp.getC_BPartner_ID();
				boolean newProduct = M_Product_ID == 0;
				log.fine("I_Product_ID=" + I_Product_ID + ", M_Product_ID=" + M_Product_ID 
					+ ", C_BPartner_ID=" + C_BPartner_ID);
			
				// Creating AttributeSetInstance and AttributeInstance
				String sql1 = new String("select p.M_AttributeSet_ID attSet, a.M_attribute_id att, " 
						+ " v.M_attributevalue_id attVal, v.name name "
						+ " from i_product p, M_Attribute a, M_AttributeValue v" 
						+ " where substr(p.XX_CARACTERISTICA1, 1, 3) = a.value " 
						+ " and substr(p.XX_CARACTERISTICA1, 4, 3) = v.value"
						+ " and a.M_Attribute_id = v.M_Attribute_id"
						+ " and p.I_product_id = " + I_Product_ID 
						+ " group by p.M_AttributeSet_ID, a.M_attribute_id, v.M_attributevalue_id, v.name");

				PreparedStatement pstmt_setAttributeSetInstance = DB.prepareStatement(sql.toString(), get_Trx());
				ResultSet rs_setAttributeSetInstance = null;
				try {
					rs_setAttributeSetInstance = pstmt_setAttributeSetInstance.executeQuery(sql1);
					
					if(rs_setAttributeSetInstance.next()){
						
						MAttributeSetInstance attSetInst = new MAttributeSetInstance(getCtx(), 0, get_TrxName());
						attSetInst.setM_AttributeSet_ID(rs_setAttributeSetInstance.getInt("attSet"));
						// TODO: add description to Attribute Set Instance
						if (attSetInst.save()){
							commit();
							MAttributeInstance attInst = new MAttributeInstance(getCtx(), 
									rs_setAttributeSetInstance.getInt("att"), attSetInst.get_ID(), 
									rs_setAttributeSetInstance.getInt("attVal"),
									rs_setAttributeSetInstance.getString("name"), get_TrxName());
							
							sql1 = new String("select p.M_AttributeSet_ID attSet, a.M_attribute_id att, " 
									+ " v.M_attributevalue_id attVal, v.name name "
									+ " from i_product p, M_Attribute a, M_AttributeValue v" 
									+ " where substr(p.XX_CARACTERISTICA2, 1, 3) = a.value " 
									+ " and substr(p.XX_CARACTERISTICA2, 4, 3) = v.value"
									+ " and a.M_Attribute_id = v.M_Attribute_id"
									+ " and p.I_product_id = " + I_Product_ID 
									+ " group by p.M_AttributeSet_ID, a.M_attribute_id, v.M_attributevalue_id, v.name");
							//System.out.println(sql1);
							PreparedStatement pstmt_attInst2 = DB.prepareStatement(sql.toString(), get_Trx());
							ResultSet rs_attInst2 = null;
							try {
								rs_attInst2 = pstmt_attInst2.executeQuery(sql1);
								
								if(rs_attInst2.next()){
									MAttributeInstance attInst2 = new MAttributeInstance(getCtx(), 
											rs_attInst2.getInt("att"), attSetInst.get_ID(), 
											rs_attInst2.getInt("attVal"),
											rs_attInst2.getString("name"), get_TrxName());
									
									attInst2.save();
								}
								rs_attInst2.close();
							} catch (SQLException e) {
								System.out.println("ERROR en attInst2 con I_Product_ID = " + I_Product_ID);
								System.out.println(e.getMessage());
								rs_attInst2.close();
								continue;
							}
							
							pstmt_attInst2.close();
							
							
							sql1 = new String("select p.M_AttributeSet_ID attSet, a.M_attribute_id att, " 
									+ " v.M_attributevalue_id attVal, v.name name "
									+ " from i_product p, M_Attribute a, M_AttributeValue v" 
									+ " where substr(p.XX_CARACTERISTICA3, 1, 3) = a.value " 
									+ " and substr(p.XX_CARACTERISTICA3, 4, 3) = v.value"
									+ " and a.M_Attribute_id = v.M_Attribute_id"
									+ " and p.I_product_id = " + I_Product_ID 
									+ " group by p.M_AttributeSet_ID, a.M_attribute_id, v.M_attributevalue_id, v.name");
							//System.out.println(sql1);
							PreparedStatement pstmt_attInst3 = DB.prepareStatement(sql.toString(), get_Trx());
							ResultSet rs_attInst3 = null;
							try {
								rs_attInst3 = pstmt_attInst3.executeQuery(sql1);
								
								if(rs_attInst3.next()){
									MAttributeInstance attInst3 = new MAttributeInstance(getCtx(), 
											rs_attInst3.getInt("att"), attSetInst.get_ID(), 
											rs_attInst3.getInt("attVal"),
											rs_attInst3.getString("name"), get_TrxName());
									attInst3.save();
								}
								rs_attInst3.close();
							} catch (SQLException e) {
								System.out.println("ERROR en attInst3 con I_Product_ID = " + I_Product_ID);
								System.out.println(e.getMessage());
								rs_attInst3.close();
								continue;
							}
							
							pstmt_attInst3.close();
							
							
							if (attInst.save()){
								sql1 = new String("update i_product set m_attributesetinstance_id = " + attSetInst.get_ID()
										+ " where i_product_id = " + I_Product_ID );
								System.out.println(sql1);
								try {
									no = DB.executeUpdate(get_Trx(), sql1.toString());
									log.info("Set Attribute Set Instance=" + no);
									
								} catch (Exception e) {
									log.warning("Update Product - " + e.toString());
									StringBuffer sql0 = new StringBuffer ("UPDATE I_Product i "
										+ "SET I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Update Product: " + e.toString()))
										.append(" WHERE I_Product_ID=").append(I_Product_ID);
									DB.executeUpdate(get_Trx(), sql0.toString());
									System.out.println("entra al catch " + e);
									continue;
								}				
								
							} //	attInst.save()
							
						} //	attSetInst.save()
						
					} //	rs_setAttributeSetInstance.next()
					
				} catch (SQLException e) {
					System.out.println("Error al setear attribute set instance " + e);
				}			
				rs_setAttributeSetInstance.close();
				pstmt_setAttributeSetInstance.close();
				
				//	Product
				if (newProduct) //	Insert new Produ
				{
					//cambio realizado en la migracion a 362. verificar si lo que se realizo es lo que se queria o hacia la linea de comando.
					//MProduct product = new MProduct(imp);
					MProduct product = new MProduct(getCtx(), imp.get_ID(), null);
					//TODO: add setM_AttributeSetInstance_ID from i_product to m_product
					//product.setC_TaxCategory_ID(C_TaxCategory_ID);
					//product.setXX_SaleUnit_ID(imp.get);
					if (product.save())
					{

						M_Product_ID = product.getM_Product_ID();						
						log.finer("Insert Product = " + M_Product_ID);		
						noInsert++;
						
						String sqlt = "UPDATE M_PRODUCT "
							+ "SET M_AttributeSetInstance_id = "
							+ "(Select M_AttributeSetInstance_id from i_product" 
							+ " where I_Product_ID="+I_Product_ID+")," 
							+ " XX_SALEUNIT_ID = "
							+ "(Select XX_VMR_UNITSALE from i_product" 
							+ " where I_Product_ID="+I_Product_ID+")," 
							+ " c_taxcategory_ID = "
							+ "(Select c_taxcategory_ID from i_product" 
							+ " where I_Product_ID="+I_Product_ID+")" +
									"where m_product_id=" + M_Product_ID;
						
						PreparedStatement pstmt_updateProduct = DB.prepareStatement
							(sqlt, get_TrxName());
						pstmt_updateProduct.executeUpdate();
						
						pstmt_updateProduct.close();
					

					}
					else
					{
						Exception ex = CLogger.retrieveException();
						String exMsg = ex == null ? "" : ex.toString();
						StringBuffer sql0 = new StringBuffer ("UPDATE I_Product i "
							+ "SET I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Insert Product failed - " + exMsg ))
							.append("WHERE I_Product_ID=").append(I_Product_ID);
						DB.executeUpdate(get_Trx(), sql0.toString());
						continue;
					}
				} //	newProduct
				else					//	Update Product
				{

					
					String sqlt = "UPDATE M_PRODUCT "
						+ "SET (Value,Name,Description,DocumentNote,Help,"
						+ "UPC,SKU,C_UOM_ID,M_Product_Category_ID,Classification,ProductType,"
						+ "Volume,Weight,ShelfWidth,ShelfHeight,ShelfDepth,UnitsPerPallet,"
						+ "Discontinued,DiscontinuedBy,Updated,UpdatedBy)= "
						+ "(SELECT Value,Name,Description,DocumentNote,Help,"
						+ "UPC,SKU,C_UOM_ID,M_Product_Category_ID,Classification,ProductType,"
						+ "Volume,Weight,ShelfWidth,ShelfHeight,ShelfDepth,UnitsPerPallet,"
						+ "Discontinued,DiscontinuedBy,SysDate,UpdatedBy"
						+ " FROM I_Product WHERE I_Product_ID="+I_Product_ID+") "
						+ "WHERE M_Product_ID="+M_Product_ID;
		
					sqlt = "UPDATE M_PRODUCT "
						+ "SET M_AttributeSetInstance_id = "
						+ "(Select M_AttributeSetInstance_id from i_product" 
						+ " where I_Product_ID="+I_Product_ID+")";
					
					PreparedStatement pstmt_updateProduct = DB.prepareStatement
						(sqlt, get_TrxName());

					try
					{
						no = pstmt_updateProduct.executeUpdate();
						log.finer("Update Product = " + no);
												
						noUpdate++;
					}
					catch (SQLException ex)
					{
						log.warning("Update Product - " + ex.toString());
						StringBuffer sql0 = new StringBuffer ("UPDATE I_Product i "
							+ "SET I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Update Product: " + ex.toString()))
							.append("WHERE I_Product_ID=").append(I_Product_ID);
						DB.executeUpdate(get_Trx(), sql0.toString());
						System.out.println("entra al catch " + ex);
						continue;
					}
					pstmt_updateProduct.close();
					
				} //	else

				
				//	Do we have PO Info
				if (C_BPartner_ID != 0)
				{
					no = 0;
					//	If Product existed, Try to Update first
					if (!newProduct)
					{
						
						String sqlt = "UPDATE M_Product_PO "
							+ "SET (IsCurrentVendor,C_UOM_ID,C_Currency_ID,UPC,"
							+ "PriceList,PricePO,RoyaltyAmt,PriceEffective,"
							+ "VendorProductNo,VendorCategory,Manufacturer,"
							+ "Discontinued,DiscontinuedBy,Order_Min,Order_Pack,"
							+ "CostPerOrder,DeliveryTime_Promised,Updated,UpdatedBy)= "
							+ "(SELECT CAST('Y' AS CHAR), C_UOM_ID, C_Currency_ID, UPC, "    //jz fix EDB unknown datatype error
							+ "PriceList, PricePO, RoyaltyAmt, PriceEffective, "
							+ "VendorProductNo, VendorCategory, Manufacturer, "
							+ "Discontinued, DiscontinuedBy, Order_Min, Order_Pack, "
							+ "CostPerOrder, DeliveryTime_Promised, SysDate, UpdatedBy"
							+ " FROM I_Product"
							+ " WHERE I_Product_ID="+I_Product_ID+") "
							+ "WHERE M_Product_ID="+M_Product_ID+" AND C_BPartner_ID="+C_BPartner_ID;
						PreparedStatement pstmt_updateProductPO = DB.prepareStatement
							(sqlt, get_TrxName());
						try
						{
							no = pstmt_updateProductPO.executeUpdate();
							log.finer("Update Product_PO = " + no);
							noUpdatePO++;
						}
						catch (SQLException ex)
						{
							log.warning("Update Product_PO - " + ex.toString());
							noUpdate--;
							rollback();
							StringBuffer sql0 = new StringBuffer ("UPDATE I_Product i "
								+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Update Product_PO: " + ex.toString()))
								.append("WHERE I_Product_ID=").append(I_Product_ID);
							DB.executeUpdate(get_Trx(), sql0.toString());
							System.out.println("entra al catch " + ex);
							continue;
						}
						pstmt_updateProductPO.close();
					}

					if (no == 0)		//	Insert PO
					{
						pstmt_insertProductPO.setInt(1, M_Product_ID);
						pstmt_insertProductPO.setInt(2, C_BPartner_ID);
						pstmt_insertProductPO.setInt(3, I_Product_ID);
						try
						{
							no = pstmt_insertProductPO.executeUpdate();
							log.finer("Insert Product_PO = " + no);
							noInsertPO++;
							pstmt_insertProductPO.close();
						}
						catch (SQLException ex)
						{
							log.warning("Insert Product_PO - " + ex.toString());
							noInsert--;			//	assume that product also did not exist
							rollback();
							StringBuffer sql0 = new StringBuffer ("UPDATE I_Product i "
								+ "SET I_IsImported='E', I_ErrorMsg="+ts +"||").append(DB.TO_STRING("Insert Product_PO: " + ex.toString()))
								.append("WHERE I_Product_ID=").append(I_Product_ID);
							DB.executeUpdate(get_Trx(), sql0.toString());
							System.out.println("entra al catch");
							continue;
						}
					}
					pstmt_insertProductPO.close();
				}	//	C_BPartner_ID != 0
				
				//	Price List
				if (p_M_PriceList_Version_ID != 0)
				{
					BigDecimal PriceList = imp.getPriceList();
					BigDecimal PriceStd = imp.getPriceStd();
					BigDecimal PriceLimit = imp.getPriceLimit();
					if (PriceStd.signum() != 0 && PriceLimit.signum() != 0 && PriceList.signum() != 0)
					{
						MProductPrice pp = MProductPrice.get(getCtx(), 
							p_M_PriceList_Version_ID, M_Product_ID, get_TrxName());
						if (pp == null)
							pp = new MProductPrice (getCtx(), 
								p_M_PriceList_Version_ID, M_Product_ID, get_TrxName());
						pp.setPrices(PriceList, PriceStd, PriceLimit);
						pp.save();
					}
				} //	p_M_PriceList_Version_ID != 0				
				
				//	Update I_Product
				pstmt_setImported.setInt(1, M_Product_ID);
				pstmt_setImported.setInt(2, I_Product_ID);
				no = pstmt_setImported.executeUpdate();
				//
				pstmt_insertProductPO.close();
				
			} //	rs.next()
			
			rs.close();
			pstmt.close();

			
		} catch (SQLException e) {
			System.out.println("entra al catch. Error: " + e);
			rs.close();
			pstmt.close();
		}
		pstmt_setImported.close();
		
		
		//TODO revisar el codigo verdadero
		
		/*String sqlt = "update m_attributesetinstance "+
				"set m_attributeset_id = (select b.m_attributeset_id from m_attributesetinstance a inner	join m_product b on "+
				"a.m_attributesetinstance_id = b.m_attributesetinstance_id "+
				"where a.m_attributeset_id = 0 and rownum = 1) "+
				"where m_attributeset_id = 0 and description is null";
		
		System.out.println(sqlt);
		
		PreparedStatement pstmt_updateProduct = DB.prepareStatement
			(sqlt, get_TrxName());
		pstmt_updateProduct.executeUpdate();
		commit();
		pstmt_updateProduct.close();
		
		*/
	    System.out.println("updateo de la descripcion del attribute set instance");
	    
		// updateo de la descripcion del attribute set instance
		String sqlt = "update m_attributesetinstance  i "
			+ "set description =    (select description || '_' || (select description from M_ATTRIBUTEVALUE where M_ATTRIBUTEVALUE_id=( select M_ATTRIBUTEVALUE_ID from M_ATTRIBUTEINSTANCE where m_attributesetinstance_id=i.m_attributesetinstance_id and M_ATTRIBUTEVALUE_ID not in (select M_ATTRIBUTEVALUE_ID from M_ATTRIBUTEINSTANCE where m_attributesetinstance_id=i.m_attributesetinstance_id and rownum=1) and rownum=1)) || '_' || (select description from M_ATTRIBUTEVALUE where M_ATTRIBUTEVALUE_id=( select M_ATTRIBUTEVALUE_ID from M_ATTRIBUTEINSTANCE where m_attributesetinstance_id=i.m_attributesetinstance_id and M_ATTRIBUTEVALUE_ID not in (select M_ATTRIBUTEVALUE_ID from M_ATTRIBUTEINSTANCE where m_attributesetinstance_id=i.m_attributesetinstance_id and rownum=1) and M_ATTRIBUTEVALUE_ID not in (select M_ATTRIBUTEVALUE_ID from M_ATTRIBUTEINSTANCE where m_attributesetinstance_id=i.m_attributesetinstance_id and M_ATTRIBUTEVALUE_ID not in (select M_ATTRIBUTEVALUE_ID from M_ATTRIBUTEINSTANCE where m_attributesetinstance_id=i.m_attributesetinstance_id and rownum=1) and rownum=1))) from M_ATTRIBUTEVALUE where M_ATTRIBUTEVALUE_id=( select M_ATTRIBUTEVALUE_ID from M_ATTRIBUTEINSTANCE where m_attributesetinstance_id=i.m_attributesetinstance_id and rownum=1)) "
			+ " where description is null and m_attributeset_id <> 0";
		
		PreparedStatement pstmt_updateProduct = DB.prepareStatement
		(sqlt, get_TrxName());
		
		pstmt_updateProduct.executeUpdate();
		commit();
		pstmt_updateProduct.close();
		
		System.out.println("updateo del taxcategory en las referencias");
		
		String sqlt3 = "update xx_vmr_vendorprodref set c_taxcategory_id = ( " +
						"select b.c_taxcategory_id from XX_VMR_VENDORPRODREF a inner join m_product b " +
						"on a.xx_vmr_vendorprodref_id = b.xx_vmr_vendorprodref_id and rownum=1) " +
						"where c_taxcategory_id is null";

		System.out.println(sqlt3);
		
		PreparedStatement pstmt3_updateProduct = DB.prepareStatement
			(sqlt3, get_TrxName());
		pstmt3_updateProduct.executeUpdate();
		commit();
		pstmt3_updateProduct.close();
		
		String sqlt4 = "update m_product set M_ATTRIBUTESET_ID = (SELECT M_ATTRIBUTESET_ID FROM M_ATTRIBUTESET WHERE" +
						" NAME = 'STANDARD') WHERE M_ATTRIBUTESET_ID IS NULL AND M_ATTRIBUTESETINSTANCE_ID IS NULL";

		System.out.println(sqlt4);

		PreparedStatement pstmt4_updateProduct = DB.prepareStatement
		(sqlt4, get_TrxName());
		pstmt4_updateProduct.executeUpdate();
		commit();
		pstmt4_updateProduct.close();
		
		//	Set Error to indicator to not imported
		sql = new StringBuffer ("UPDATE I_Product "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), "@M_Product_ID@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdate), "@M_Product_ID@: @Updated@");
		addLog (0, null, new BigDecimal (noInsertPO), "@M_Product_ID@ @Purchase@: @Inserted@");
		addLog (0, null, new BigDecimal (noUpdatePO), "@M_Product_ID@ @Purchase@: @Updated@");
		

		return "";
	}	//	doIt
}
