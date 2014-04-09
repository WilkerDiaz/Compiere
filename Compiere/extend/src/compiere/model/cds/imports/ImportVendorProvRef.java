package compiere.model.cds.imports;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import compiere.model.cds.X_I_XX_VMR_VendorProdRef;
import compiere.model.cds.X_XX_VMR_VendorProdRef;

public class ImportVendorProvRef extends SvrProcess{

	/**	Client to be imported to		*/
	private int				p_AD_Client_ID = 0;
	/**	Delete old Imported				*/
	private boolean			p_deleteOldImported = false;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null);
			else if (name.equals("AD_Client_ID"))
				p_AD_Client_ID = ((BigDecimal) element.getParameter())
						.intValue();
			
			else if (name.equals("DeleteOldImported")) 
				p_deleteOldImported = "Y".equals(element.getParameter());
			 
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}//end prepare
	
	@Override
	protected String doIt() throws Exception {
		
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + p_AD_Client_ID;

		//	****	Prepare	**** //
/**
		//	Delete Old Imported
		if (p_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_XX_VMR_VENDORPRODREF "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
		
		//	Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF "
			+ " SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(p_AD_Client_ID).append("),"
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
		commit();
		
		//
/**		
		sql = new StringBuffer("UPDATE I_XX_VMR_VENDORPRODREF i "
				+ "SET XX_VMR_VendorProdRef_ID=(SELECT XX_VMR_VendorProdRef_ID " 
				+ "FROM XX_VMR_VendorProdRef "
				+ "WHERE i.Value=Value AND AD_CLIENT_ID=i.AD_CLIENT_ID)"
				+ "WHERE XX_VMR_VendorProdRef_ID IS NULL AND AD_CLIENT_ID IS NOT NULL " 
				+ "AND IsImported='N' AND Value IS NOT NULL")
				.append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Vendor Product Reference ID=" + no);
*/		
/**	

		//	Updating Business Partner ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
			+ " SET C_BPARTNER_ID = (SELECT C_BPARTNER_ID FROM C_BPARTNER b "
			+ " WHERE i.COEMPE = b.Value) "
			+ " WHERE i.COEMPE IS NOT NULL and I_IsImported<>'Y'"
			+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set BPartner ID=" + no);
		commit();
		System.out.println("proveedor");

		
		//	Updating Department ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
			+ " SET XX_VMR_DEPARTMENT_ID = (SELECT XX_VMR_DEPARTMENT_ID FROM XX_VMR_DEPARTMENT d "
			+ " WHERE i.XX_DEPARTMENT_VALUE = d.Value) "
			+ " WHERE i.XX_DEPARTMENT_VALUE IS NOT NULL and I_IsImported<>'Y'"
			+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Assistant ID=" + no);
		commit();
		System.out.println("department");
		
		//	Updating Line ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
			+ " SET XX_VMR_LINE_ID = (SELECT XX_VMR_LINE_ID FROM XX_VMR_LINE l "
			+ " WHERE i.XX_LINE_VALUE = l.Value " 
			+ " AND i.XX_VMR_DEPARTMENT_ID = l.XX_VMR_DEPARTMENT_ID) "
			+ " WHERE i.XX_LINE_VALUE IS NOT NULL and I_IsImported<>'Y'"
			+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Assistant ID=" + no);
		commit();
		System.out.println("line");
		
		//	Updating Section ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
			+ " SET XX_VMR_SECTION_ID = (SELECT XX_VMR_SECTION_ID FROM XX_VMR_SECTION s "
			+ " WHERE i.XX_SECTION_VALUE = s.Value "
			+ " AND i.XX_VMR_LINE_ID = s.XX_VMR_LINE_ID) "
			+ " WHERE i.XX_SECTION_VALUE IS NOT NULL and I_IsImported<>'Y'"
			+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Assistant ID=" + no);
		commit();
		System.out.println("section");
		
		//	Updating Brand ID
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
				+ " SET XX_VMR_BRAND_ID = (SELECT XX_VMR_BRAND_ID FROM XX_VMR_BRAND b "
				+ " WHERE i.XX_MARCA_VALUE = b.Value ) "
				+ " WHERE i.XX_MARCA_VALUE IS NOT NULL and I_IsImported<>'Y'"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Brand ID=" + no);	
		commit();
		System.out.println("marca");
		
		//	Updating Unit Purchase
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
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
				+ " WHERE UNICOM IS NOT NULL and I_IsImported<>'Y'"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Purchase =" + no);	
		commit();
		System.out.println("unidad compra");
		
		//	Updating Unit Sale
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
				+ " SET XX_SALEUNIT_ID = " 
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
				+ " WHERE UNIVEN IS NOT NULL and I_IsImported<>'Y'"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Sale =" + no);	
		commit();
		System.out.println("unidad venta");
	
		//	Updating Unit Conversion Purchase
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
				+ " SET XX_VMR_UnitConversion_ID = " 
				+ " (CASE WHEN UNICOM ='BO' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='BU' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CA' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='DO' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='12')"
				+ "       WHEN UNICOM ='EM' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='ES' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='JG' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='PA' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='PR' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='PZ' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='IN' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='SE' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C2' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='2'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C3' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='3'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C4' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='4'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C5' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='5'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C8' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='8'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='C6' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='6'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CB' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='20'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CC' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='24'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CD' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='36'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='CE' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='48'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNICOM ='2P' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ " ELSE null END)"
				+ " WHERE UNICOM IS NOT NULL and I_IsImported<>'Y'"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Conversion Purchase =" + no);	
		commit();		
		System.out.println("conversion compra");
		
		//Updating Unit Conversion Sale
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
				+ " SET XX_PIECESBYSALE_ID = " 
				+ " (CASE WHEN UNIVEN ='BO' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='BU' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CA' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='DO' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='12'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='EM' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='ES' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='JG' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='PA' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='PR' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='PZ' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='IN' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='SE' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C2' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='2'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C3' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='3'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C4' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='4'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C5' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='5'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C8' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='8'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='C6' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='6'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CB' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='20'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CC' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='24'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CD' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='36'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='CE' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='48'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ "       WHEN UNIVEN ='2P' THEN (SELECT XX_VMR_UnitConversion_ID FROM XX_VMR_UnitConversion WHERE XX_UNITCONVERSION='1'and i.XX_VMR_UNITPURCHASE_ID = XX_VMR_UNITPURCHASE_ID)"
				+ " ELSE null END)"
				+ " WHERE UNIVEN IS NOT NULL and I_IsImported<>'Y'"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Conversion Sale =" + no);	
		commit();	
		System.out.println("conversion venta");
		
	
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
				+ " SET C_TaxCategory_ID = (SELECT C_TaxCategory_ID FROM C_TaxCategory WHERE name='IVA')" 
				+ " WHERE TIPIMP ='C9' and I_IsImported<>'Y'"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Purchase =" + no);	
		commit();
		System.out.println("tax category 1");
		
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
				+ " SET C_TaxCategory_ID = (SELECT C_TaxCategory_ID FROM C_TaxCategory WHERE name='EXENTO')" 
				+ " WHERE TIPIMP ='C5' and I_IsImported<>'Y'"
				+ " ");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Purchase =" + no);	
		commit();
		System.out.println("tax category 1");

	
		// updateo de la caracteristica larga
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
				+ " SET XX_VMR_LONGCHARACTERISTIC_ID = (SELECT t.XX_VMR_LONGCHARACTERISTIC_ID FROM I_XX_TABM02 t WHERE i.CARACT=t.XX_CARACT and i.xx_vmr_department_ID=t.xx_vmr_department_ID "
				+ " and i.xx_vmr_line_id=t.xx_vmr_line_id and i.xx_vmr_section_id=t.xx_vmr_section_id and rownum = 1)"
				+ " where I_ISIMPORTED='N'");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set Unit Purchase =" + no);	
		commit();
		System.out.println("caracteristica larga");
	
		
		// updateo de la caracteristica dinamica
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i " 
				+ " SET m_attributeset_ID = (SELECT t.m_attributeset_ID FROM XX_VMR_DYNAMICCHARACT t WHERE  t.xx_vmr_section_id=i.xx_vmr_section_id)"
				+ " where I_IsImported<>'Y'");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		log.fine("Set caracteristica dinamica =" + no);	
		commit();
		System.out.println("caracteristica dinamica");
		
		
		
		
		
		
		
		/**		
		//Error de referencia
		String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF "
			+ " SET I_IsImported='E', I_ErrorMsg='ERR=No se encontro referencia,||"+ ts
			+ " ' WHERE ((C_BPARTNER_ID IS NULL) OR"
			+ " (XX_VMR_DEPARTMENT_ID IS NULL) OR "
			+ " (XX_VMR_LINE_ID IS NULL) OR "
			+ " (XX_VMR_SECTION_ID IS NULL) OR "
			+ " (XX_VMR_BRAND_ID IS NULL)) "
			+ " AND I_IsImported<>'Y' ").append(clientCheck); 
		no = DB.executeUpdate(get_Trx(), sql.toString());
		if (no != 0)
			log.warning("Invalid referencia=" + no);
		*/
		commit();
		//-----------------------------------------------------------------------------------
		
		int noInsert = 0;

		//	Go through Records
		log.fine("start inserting...");
		sql = new StringBuffer ("SELECT * FROM I_XX_VMR_VENDORPRODREF "
							+ " WHERE I_ISIMPORTED='N'");
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				X_I_XX_VMR_VendorProdRef impIvendProvRef = new X_I_XX_VMR_VendorProdRef(
						getCtx(), rs, get_TrxName());
				log.fine("I_XX_VMR_VendorProdRef_ID=" + impIvendProvRef.getI_XX_VMR_VENDORPRODREF_ID());
				
				X_XX_VMR_VendorProdRef impXvendProvRef = new X_XX_VMR_VendorProdRef(
						getCtx(), 0, get_TrxName());
								
				impXvendProvRef.setAD_Org_ID(0);
				impXvendProvRef.setC_BPartner_ID(impIvendProvRef.getC_BPartner_ID());
				impXvendProvRef.setValue(impIvendProvRef.getREFPRO());
				impXvendProvRef.setXX_EnglishDescription(impIvendProvRef.getName2());
				impXvendProvRef.setName(impIvendProvRef.getName());
				impXvendProvRef.setXX_VMR_Department_ID(impIvendProvRef.getXX_VMR_Department_ID());
				impXvendProvRef.setXX_VMR_Line_ID(impIvendProvRef.getXX_VMR_Line_ID());
				impXvendProvRef.setXX_VMR_Section_ID(impIvendProvRef.getXX_VMR_Section_ID());
				impXvendProvRef.setXX_VMR_LongCharacteristic_ID(impIvendProvRef.getXX_VMR_LongCharacteristic_ID());
				impXvendProvRef.setXX_VMR_Brand_ID(impIvendProvRef.getXX_VMR_Brand_ID());
				impXvendProvRef.setXX_PackageMultiple(Integer.parseInt(impIvendProvRef.getMULEMP()));
				impXvendProvRef.setXX_VMR_UnitConversion_ID(impIvendProvRef.getXX_VMR_UnitConversion_ID());
				impXvendProvRef.setXX_VMR_UnitPurchase_ID(impIvendProvRef.getXX_VMR_UnitPurchase_ID());
				impXvendProvRef.setXX_SaleUnit_ID(impIvendProvRef.getXX_SaleUnit_ID());
				impXvendProvRef.setXX_PiecesBySale_ID(impIvendProvRef.getXX_PiecesBySale_ID());
				impXvendProvRef.setM_AttributeSet_ID(impIvendProvRef.getM_AttributeSet_ID());
				System.out.println("Proveedor " + impIvendProvRef.getC_BPartner_ID());
				System.out.println("Value " + impIvendProvRef.getREFPRO());
				System.out.println("English description " + impIvendProvRef.getName2());
				System.out.println("Name " + impIvendProvRef.getName());
				System.out.println("department " + impIvendProvRef.getXX_VMR_Department_ID());
				System.out.println("line " +impIvendProvRef.getXX_VMR_Line_ID());
				System.out.println("section " + impIvendProvRef.getXX_VMR_Section_ID());
				System.out.println("longcharact " + impIvendProvRef.getXX_VMR_LongCharacteristic_ID());
				System.out.println("brand " + impIvendProvRef.getXX_VMR_Brand_ID());
				System.out.println("package " + Integer.parseInt(impIvendProvRef.getMULEMP()));
				System.out.println("unit conversion " + impIvendProvRef.getXX_VMR_UnitConversion_ID());
				System.out.println("unit purchase " + impIvendProvRef.getXX_VMR_UnitPurchase_ID());
				System.out.println("sale unit " + impIvendProvRef.getXX_SaleUnit_ID());
				System.out.println("pieces sale " + impIvendProvRef.getXX_PiecesBySale_ID());
				System.out.println("attribute set " + impIvendProvRef.getM_AttributeSet_ID());
				if(impXvendProvRef.save())
				{
					log.finest("Insert/Update XX_VMR_VendorProdRef - " 
							+  impXvendProvRef.getXX_VMR_VendorProdRef_ID());
					noInsert++;
									
					impIvendProvRef.setI_IsImported(true);
					impIvendProvRef.setProcessed(true);
					impIvendProvRef.setProcessing(false);
					impIvendProvRef.setXX_VMR_VendorProdRef_ID(impXvendProvRef.getXX_VMR_VendorProdRef_ID());
					impIvendProvRef.save();
				}
				else
				{		
					rollback();
					noInsert--;						
					sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF i "
							+ "SET I_IsImported='E', I_ErrorMsg= ")
							.append(" 'Cannot Insert I_XX_VMR_VENDORPRODREF...'")
							.append(" WHERE I_XX_VMR_VENDORPRODREF_ID=")
							.append(impIvendProvRef.getI_XX_VMR_VENDORPRODREF_ID());
						DB.executeUpdate(get_Trx(), sql.toString());
						//continue;							
				}

				commit();
			}// end while
			
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			
			log.log(Level.SEVERE, "", e);
			rollback();
		}
	
		System.out.println("Seteando description");
		
		sql = new StringBuffer ("update xx_vmr_vendorprodref ref set ref.description=" +
								"substr(value||'_'||ref.name||"+
								"'_Line:'||(select l.value from xx_vmr_line l where l.xx_vmr_line_id=ref.xx_vmr_line_id)||"+
								"'_Sec:'||(select l.value from xx_vmr_section l where l.xx_vmr_section_id=ref.xx_vmr_section_id)|| "+
								"'_Caract:'||(select name from XX_VMR_LONGCHARACTERISTIC c where c.XX_VMR_LONGCHARACTERISTIC_id=ref.XX_VMR_LONGCHARACTERISTIC_id),0,90) "+
								"where description is null  and ref.XX_VMR_LONGCHARACTERISTIC_id is not null");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		commit();
		
		sql = new StringBuffer ("update xx_vmr_vendorprodref ref set ref.description=" +
				"value||'_'||ref.name||" +
				"'_Line:'||(select l.value from xx_vmr_line l where l.xx_vmr_line_id=ref.xx_vmr_line_id)||" +
				"'_Sec:'||(select l.value from xx_vmr_section l where l.xx_vmr_section_id=ref.xx_vmr_section_id) " +
				"where description is null  and ref.XX_VMR_LONGCHARACTERISTIC_id is null");
		no = DB.executeUpdate(get_Trx(), sql.toString());
		commit();
		
		

		
		//	Set Error to indicator to not imported		
		sql = new StringBuffer ("UPDATE I_XX_VMR_VENDORPRODREF "
			+ "SET I_IsImported='N', Updated=SysDate "
			+ "WHERE I_IsImported<>'Y'").append(clientCheck);
		no = DB.executeUpdate(get_Trx(), sql.toString());
		addLog (0, null, new BigDecimal (no), "@Errors@");
		addLog (0, null, new BigDecimal (noInsert), 
				"@I_XX_VMR_VENDORPRODREF_ID@: @Inserted@");

		return "";
	
	}//end doIt

}