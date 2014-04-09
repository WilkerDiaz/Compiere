
package compiere.model.cds;

import java.awt.Container;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import org.compiere.apps.ADialog;
import org.compiere.apps.ProcessCtl;
import org.compiere.common.constants.EnvConstants;
import org.compiere.excel.Excel;
import org.compiere.framework.PrintInfo;
import org.compiere.framework.Query;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MClient;
import org.compiere.model.MMailText;
import org.compiere.model.MPInstance;
import org.compiere.model.MUserMail;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_AD_Client;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_C_Invoice;
import org.compiere.model.X_M_Movement;
import org.compiere.model.X_Ref__Document_Status;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.print.Viewer;
import org.compiere.process.DocumentEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

import compiere.model.payments.X_XX_VCN_EstimatedAPayable;
import compiere.model.tag.X_XX_VCN_DetailAdvice;

public class Utilities {
	
	private int				m_R_MailText_ID = -1; /** What to send*/
	private int				m_AD_User_ID = -1; /**	From (sender)*/
	private MClient			m_client = null; /** Client Info*/
	private Integer			m_C_BP_Group_ID = -1; /** To Customer Type*/
	protected CLogger	    log = CLogger.getCLogger (getClass());
	private Integer			m_BPartner = -1;
	private Integer		    m_UserTo = -1;
	private String 			m_Attachment = null;
	
	private Ctx getCtx= null;
	private String get_TrxName = null ;
	private String ConcatenarMensaje = null ;
	
	
	private class envioCorreos extends Thread{

		private int				m_R_MailText_ID = -1; /** What to send*/
		private MMailText		m_MailText = null; /**	Mail Text*/	
		private int				m_AD_User_ID = -1; /**	From (sender)*/
		private MClient			m_client = null; /** Client Info*/
		private MUser			m_from = null; /**	From*/
		private int 			m_counter = 0;
		private int 			m_errors = 0;
		private ArrayList<Integer>	m_list = new ArrayList<Integer>(); /** Recipient List to prevent duplicate mails*/
		
		private Integer			m_C_BP_Group_ID = -1; /** To Customer Type*/
		protected CLogger	    log = CLogger.getCLogger (getClass());
		private Integer			m_BPartner = -1;
		private Integer		    m_UserTo = -1;
		private String 			m_Attachment = null;
		
		private Ctx getCtx= null;
		private String get_TrxName = null ;
		private String ConcatenarMensaje = null ;
		
		public envioCorreos(Ctx ctx, String trx, int MailTemplate
				, String MensajeaConcatenar, int CodGrupo
				, int From , int BPartner, int UserTo, String Attachment){
			this.getCtx = ctx;
			this.get_TrxName = null;
			this.ConcatenarMensaje = MensajeaConcatenar;
			this.m_R_MailText_ID = MailTemplate;
			this.m_C_BP_Group_ID = CodGrupo;
			this.m_AD_User_ID = From;
			this.m_BPartner = BPartner;
			this.m_UserTo = UserTo;
			this.m_Attachment = Attachment;
		}
		
		public void run(){
			String aux = "";
			try {
				aux = ejecutarMail2();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//System.out.println("Termino el envio de correos. Dentro del Hilo. "+aux);
		}
		
		private String ejecutarMail2() throws Exception{ 
			this.log.info("R_MailText_ID=" + this.m_R_MailText_ID);
			//	Mail Test
			this.m_MailText = new MMailText (this.getCtx, this.m_R_MailText_ID, null);
			if (this.m_MailText.getR_MailText_ID() == 0)
				throw new Exception ("Not found @R_MailText_ID@=" + this.m_R_MailText_ID);
			//	Client Info
			this.m_client = MClient.get (this.getCtx);
			if (this.m_client.getAD_Client_ID() == 0)
				throw new Exception ("Not found @AD_Client_ID@");
			if (this.m_client.getSmtpHost() == null || this.m_client.getSmtpHost().length() == 0)
				throw new Exception ("No SMTP Host found");
			//
			if (this.m_AD_User_ID > 0)
			{
				this.m_from = new MUser (this.getCtx, this.m_AD_User_ID, null);
				if (this.m_from.getAD_User_ID() == 0)
					throw new Exception ("No found @AD_User_ID@=" + this.m_AD_User_ID);
			}
			this.log.fine("From " + this.m_from);
			long start = System.currentTimeMillis();
			

			if (this.m_C_BP_Group_ID > 0){
				sendBPGroup(this.m_Attachment);			
			}

			if (this.m_BPartner > 0){
				try {
					MBPartner BPartnerAux = new MBPartner(this.getCtx, this.m_BPartner, null);
					sendIndividualMailtoBPartner(BPartnerAux.getName(), BPartnerAux.getXX_VendorEmail(), null, this.m_Attachment);
				} catch (Exception e){
					System.out.println("Error al mandar correo al proveedor");
				}
			}
			if (this.m_UserTo > 0){
				try {
					MUser UserAux = new MUser(this.getCtx, this.m_UserTo, null);
					sendIndividualMail (UserAux.getName(), UserAux.getAD_User_ID(), null, this.m_Attachment);
				} catch (Exception e){
					System.out.println("Error al mandar correo al usuario ");
				}


			}
			
			//System.out.println("Dentro del Hilo. Envio Correo");
			return "@Created@=" + m_counter + ", @Errors@=" + m_errors + " - "
				+ (System.currentTimeMillis()-start) + "ms";
		}
		
		/*
		 * 	Send to BPGroup
		 */
		private void sendBPGroup(String Attachment)
		{
			this.log.info("C_BP_Group_ID=" + this.m_C_BP_Group_ID);
			
			String sql = "SELECT u.Name, u.EMail, u.AD_User_ID "
				+ "FROM AD_User u"
				+ " INNER JOIN C_BPartner bp ON (u.C_BPartner_ID=bp.C_BPartner_ID) "
				+ "WHERE u.IsActive='Y' AND bp.IsActive='Y'"
				+ " AND u.EMail IS NOT NULL"
				+ " AND bp.C_BP_Group_ID=?";
			PreparedStatement pstmt = null;
			try
			{
				pstmt = DB.prepareStatement(sql, null);
				pstmt.setInt(1, this.m_C_BP_Group_ID);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next())
				{
					Boolean ok = sendIndividualMail (rs.getString(1), rs.getInt(3), null, this.m_Attachment);
					if (ok == null)
						;
					else if (ok.booleanValue())
						m_counter++;
					else
						m_errors++;
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (SQLException ex)
			{
				this.log.log(Level.SEVERE, sql, ex);
			}
			//	Clean Up
			try
			{
				if (pstmt != null)
					pstmt.close();
			}
			catch (SQLException ex1)
			{
			}
			pstmt = null;
		}
		
		private Boolean sendIndividualMail (String Name, int AD_User_ID, String unsubscribe, String Attachment)
		{
			//	Prevent two email
			Integer ii = Integer.valueOf (AD_User_ID);
			if (this.m_list.contains(ii))
				return null;
			this.m_list.add(ii);
			//
			MUser to = new MUser (this.getCtx, AD_User_ID, null);
			if (to.isEMailBounced())			//	ignore bounces
				return null;
			this.m_MailText.setUser(AD_User_ID);		//	parse context
			String message = this.m_MailText.getMailText(true)+" "+this.ConcatenarMensaje;
			//	Unsubscribe
			if (unsubscribe != null)
				message += unsubscribe;
			//
			EMail email = this.m_client.createEMail(this.m_from, to, this.m_MailText.getMailHeader(), message);
			if (email == null)
				return Boolean.FALSE;
			if (this.m_MailText.isHtml())
				email.setMessageHTML(this.m_MailText.getMailHeader(), message);
			else
			{
				email.setSubject (this.m_MailText.getMailHeader());
				email.setMessageText (message);
			}
			if (!email.isValid() && !email.isValid(true))
			{
				this.log.warning(email.toString());
				to.setIsActive(false);
				to.addDescription("Invalid EMail");
				to.save();
				return Boolean.FALSE;
			}
		
			if (Attachment != null){
				email.addAttachment(new File(Attachment));			
			}
			
			boolean OK = EMail.SENT_OK.equals(email.send());
			new MUserMail(m_MailText, AD_User_ID, email).save();
			//
			if (OK)
				this.log.fine(to.getEMail());
			else
				this.log.warning("FAILURE - " + to.getEMail());
			//addLog(0, null, null, (OK ? "@OK@" : "@ERROR@") + " - " + to.getEMail());
			return Boolean.valueOf(OK);
		}	//	sendIndividualMail
		
		private Boolean sendIndividualMailtoBPartner(String Name, String ToMail, String unsubscribe, String Attachment)
		{
			String message = this.m_MailText.getMailText(true)+" "+this.ConcatenarMensaje;
			//	Unsubscribe
			if (unsubscribe != null)
				message += unsubscribe;
			//
			EMail email = this.m_client.createEMail(this.m_from, ToMail, Name,  this.m_MailText.getMailHeader(), message);
			if (email == null)
				return Boolean.FALSE;
			if (this.m_MailText.isHtml())
				email.setMessageHTML(this.m_MailText.getMailHeader(), message);
			else
			{
				email.setSubject (this.m_MailText.getMailHeader());
				email.setMessageText (message);
			}
			if (!email.isValid() && !email.isValid(true))
			{
				this.log.warning(email.toString());
				return Boolean.FALSE;
			}
			
			if (Attachment != null){
				email.addAttachment(new File(Attachment));			
			}
			
			boolean OK = EMail.SENT_OK.equals(email.send());
//			new MUserMail(m_MailText, AD_User_ID, email).save();
			//
			if (OK)
				this.log.fine(ToMail);
			else
				this.log.warning("FAILURE - " + ToMail);
			//addLog(0, null, null, (OK ? "@OK@" : "@ERROR@") + " - " + to.getEMail());
			return Boolean.valueOf(OK);
		}	//	sendIndividualMail2
		/*
		 * hasta aqui envio de correos
		 */
	}
	
	
	/***************************************************/
	/*
	 *  constructores
	 */
	public Utilities(){}
	
	public Utilities(Ctx ctx, String trx, int MailTemplate
			, String MensajeaConcatenar, int CodGrupo
			, int From , int BPartner, int UserTo, String Attachment){
		getCtx = ctx;
		get_TrxName = null;
		ConcatenarMensaje = MensajeaConcatenar;
		m_R_MailText_ID = MailTemplate;
		m_C_BP_Group_ID = CodGrupo;
		m_AD_User_ID = From;
		m_BPartner = BPartner;
		m_UserTo = UserTo;
		m_Attachment = Attachment;
	}
	/*
	 * hasta aqui constructores 
	 */
	
	
	/***************************************************/
	
	/*
	 * envio de correos
	 */
	public String ejecutarMail() throws Exception{
		new envioCorreos(ctx, get_TrxName, m_R_MailText_ID, ConcatenarMensaje, m_C_BP_Group_ID, m_AD_User_ID, m_BPartner, m_UserTo, m_Attachment).start();
		//System.out.println("Fuera del Hilo. Envio el correo.");
		return "";
	}
	
	
	/*
	 * envio de correos
	 *
	public String ejecutarMail2() throws Exception
	{ 
		log.info("R_MailText_ID=" + m_R_MailText_ID);
		//	Mail Test
		m_MailText = new MMailText (getCtx, m_R_MailText_ID, null);
		if (m_MailText.getR_MailText_ID() == 0)
			throw new Exception ("Not found @R_MailText_ID@=" + m_R_MailText_ID);
		//	Client Info
		m_client = MClient.get (getCtx);
		if (m_client.getAD_Client_ID() == 0)
			throw new Exception ("Not found @AD_Client_ID@");
		if (m_client.getSmtpHost() == null || m_client.getSmtpHost().length() == 0)
			throw new Exception ("No SMTP Host found");
		//
		if (m_AD_User_ID > 0)
		{
			m_from = new MUser (getCtx, m_AD_User_ID, null);
			if (m_from.getAD_User_ID() == 0)
				throw new Exception ("No found @AD_User_ID@=" + m_AD_User_ID);
		}
		log.fine("From " + m_from);
		long start = System.currentTimeMillis();
		

		if (m_C_BP_Group_ID > 0){
			sendBPGroup(m_Attachment);			
		}
		if (m_BPartner > 0){
			MBPartner BPartnerAux = new MBPartner(getCtx, m_BPartner, null);
			sendIndividualMailtoBPartner(BPartnerAux.getName(), BPartnerAux.getXX_VendorEmail(), null, m_Attachment);
		}
		if (m_UserTo > 0){
			MUser UserAux = new MUser(getCtx, m_UserTo, null);
			sendIndividualMail (UserAux.getName(), UserAux.getAD_User_ID(), null, m_Attachment);
		}
		
		//System.out.println("Dentro del Hilo. Envio Correo");
		return "@Created@=" + m_counter + ", @Errors@=" + m_errors + " - "
			+ (System.currentTimeMillis()-start) + "ms";
	}
	*/
	
	
	/***************************************************/
	/**
	 * Calcula la puntuación del Proveedor
	 */
	public Double calculateVendorRating (Integer m_C_BPartner_ID){
		Double auxCampo = new Double(0);
		String sql =  "SELECT AVG(XX_POINTS) "+ 
	      "FROM XX_VCN_VENDORRATING "+
	      "WHERE C_BPARTNER_ID = "+m_C_BPartner_ID+" and "+
	      	    "XX_VCN_EvaluationCriteria_ID = "+Env.getCtx().getContext("#XX_L_EC_TOTALSCOREOC_ID")+" and " +
	      	    "IsActive = 'Y' and " +
	      	    "XX_C_ORDER_ID in (select C_ORDER_ID " +
	      	    				  "from C_ORDER " +
	      	    				  "where C_ORDER_ID is not null " +
	      	    				  "AND XX_EVALUATED = 'Y' " +
	      	    				  "and round(sysdate-XX_ENTRANCEDATE,2) < "+Env.getCtx().getContext("#XX_L_DAYSCALCULAVENDORRATING")+")";
		
		System.out.println(sql);
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				auxCampo = rs.getDouble("AVG(XX_POINTS)");				
			}
			
			if(auxCampo == null){
				auxCampo = new Double(0);			
			}
			
			rs.close();
			pstmt.close();
			return auxCampo;			
		}catch (SQLException e) {
			return new Double(0);
		}
	}
	/**
	 * Hasta aqui Calcula la puntuación del Proveedor
	 */
	
	
	/***************************************************/	
	/**
	 * Calcula los beneficios del Proveedor
	 */
	public String retornaSqlBenefitVendor(Integer m_C_BPartner_ID) {
		String sql = new String();
		sql = 	"select (select a.Name from XX_VCN_Benefits a where a.XX_VCN_Benefits_ID = XX_VCN_BENEFITSMATRIX.XX_VCN_Benefits_ID) Name, XX_VCN_BENEFITSMATRIX.XX_VCN_Benefits_ID , ValueNumber " +
				"FROM XX_VCN_BENEFITSMATRIX " +
				"WHERE XX_VCN_BENEFITSMATRIX.XX_VENDORTYPE_ID = (Select bp.XX_VENDORTYPE_ID from C_BPartner bp where bp.C_BPartner_ID = "+m_C_BPartner_ID+") and " +
				"         (XX_VCN_BENEFITSMATRIX.XX_ISALLVENDOR = 'Y' AND " +
				"         (SELECT AVG(venRa.XX_POINTS) FROM XX_VCN_VENDORRATING venRa " +
				"          WHERE venRa.C_BPARTNER_ID = "+m_C_BPartner_ID+" and " +
				"                venRa.XX_VCN_EvaluationCriteria_ID = "+Env.getCtx().getContext("#XX_L_EC_TOTALSCOREOC_ID")+" and " +
				"                venRa.XX_C_ORDER_ID in (select C_ORDER_ID " +
				"                                  from C_ORDER " +
				"                                  where C_ORDER_ID is not null " +
				"								   AND XX_EVALUATED = 'Y'" +
				"                                  and round(sysdate-XX_ENTRANCEDATE,2) < "+Env.getCtx().getContext("#XX_L_DAYSCALCULAVENDORRATING")+") ) between (XX_VCN_BENEFITSMATRIX.XX_LOWERLIMIT*100) and (XX_VCN_BENEFITSMATRIX.XX_HIGHERLIMIT*100) ) or " +
				"         (XX_VCN_BENEFITSMATRIX.XX_ISALLVENDOR = 'N' AND " +
				"         XX_VCN_BENEFITSMATRIX.C_BPARTNER_ID = "+m_C_BPartner_ID+" and " +
				"         (SELECT AVG(venRa2.XX_POINTS) FROM XX_VCN_VENDORRATING venRa2 " +
				"          WHERE venRa2.C_BPARTNER_ID = "+m_C_BPartner_ID+" and " +
				"                venRa2.XX_VCN_EvaluationCriteria_ID = "+Env.getCtx().getContext("#XX_L_EC_TOTALSCOREOC_ID")+" and " +
				"                venRa2.XX_C_ORDER_ID in (select C_ORDER_ID " +
				"                                  from C_ORDER " +
				"                                  where C_ORDER_ID is not null " +
				"								   AND XX_EVALUATED = 'Y'" +
				"                                  and round(sysdate-XX_ENTRANCEDATE,2) < "+Env.getCtx().getContext("#XX_L_DAYSCALCULAVENDORRATING")+") ) between (XX_VCN_BENEFITSMATRIX.XX_LOWERLIMIT * 100) and (XX_VCN_BENEFITSMATRIX.XX_HIGHERLIMIT * 100) )";
		return sql;
	}	
	
	public Vector<KeyNamePair> viewBenefitVendor (Integer m_C_BPartner_ID){
		Vector<KeyNamePair> vectorBeneficios = new Vector<KeyNamePair>();
		String sql = retornaSqlBenefitVendor(m_C_BPartner_ID);
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				KeyNamePair aux = new KeyNamePair(rs.getInt("XX_Benefit_ID"), rs.getString("Name"));
				vectorBeneficios.add(aux);
			}
			rs.close();
			pstmt.close();
			return vectorBeneficios;			
		}catch (SQLException e) {
			return new Vector<KeyNamePair>();
		}
	}

	public Integer benefitVendorDiasFechaEntrega (Integer m_C_BPartner_ID){
		String sql = retornaSqlBenefitVendor(m_C_BPartner_ID);
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()){
				if(rs.getInt("XX_VCN_BENEFITS_ID") == Env.getCtx().getContextAsInt("#XX_L_BENEFITMODFECHENTRE_ID")){
					return rs.getInt("ValueNumber");
				}
			}

			return 0;			
		}catch (SQLException e) {
			return 0;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
	}
	
	/**
	 * 
	 * @param m_C_BPartner_ID
	 * @return
	 */
	public Integer benefitVendorDelayDiscount(Integer m_C_BPartner_ID){
		String sql = retornaSqlBenefitVendor(m_C_BPartner_ID);
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()){
				if(rs.getInt("XX_VCN_BENEFITS_ID") == Env.getCtx().getContextAsInt("#XX_L_BENEFITPORCTOTALDESCU_ID")){
					return rs.getInt("ValueNumber");
				}
			}

			return 0;			
		}catch (SQLException e) {
			return 0;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
	}

	/**
	 * 
	 * @param m_C_BPartner_ID
	 * @return Los dias de adelanto de pagos continuos para el beneficio de los proveedores
	 */
	public Integer benefitVendorDayAdvance(Integer m_C_BPartner_ID){
		String sql = retornaSqlBenefitVendor(m_C_BPartner_ID);
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			while (rs.next()){
				if(rs.getInt("XX_VCN_BENEFITS_ID") == Env.getCtx().getContextAsInt("#XX_L_BENEFITADEPAG_ID")){
					return rs.getInt("ValueNumber");
				}
			}
			
			return 0;			
		}catch (SQLException e) {
			return 0;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
	}

	/**
	 * Hasta aqui Calcula los beneficios del Proveedor
	 */

	
	/***************************************************/
	/**
	 * Empieza el Calculo de la puntuacion del Proveedor
	 */
	/** Order to Copy				*/
	private int 	p_C_Order_ID = 0;
	private int 	p_M_INOUT_ID = 0;

	private String BuscarValor(String sql, String Campo){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String AuxCampo = null; 
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				AuxCampo = rs.getString(Campo);				
			}
			if(AuxCampo == null){
				AuxCampo = new String("0");
			}
			return AuxCampo;			
		}catch (SQLException e) {
			return null;
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	
		
	private boolean InsertXX_VCN_VendorRating(Ctx ctx, Integer OrderID, Integer XX_EvaluationCriteria_ID, Integer XX_C_BPartner_ID, BigDecimal XX_Points){	
		X_XX_VCN_VendorRating AuxVendorRating = new X_XX_VCN_VendorRating(ctx, 0, null);
		
		AuxVendorRating.setXX_C_Order_ID(OrderID);
		AuxVendorRating.setXX_VCN_EvaluationCriteria_ID(XX_EvaluationCriteria_ID);
		AuxVendorRating.setC_BPartner_ID(XX_C_BPartner_ID);
		AuxVendorRating.setXX_Points(XX_Points);
		
		if (AuxVendorRating.save()){
			return true;			
		}
		else{
			return false;
		}
	}
	
	private Vector<KeyNamePair> selectEvaluationCriteria() {
		String AuxName = new String();
		Integer AuxID = null;
		Vector<KeyNamePair> AuxEvaluationCriteria = new Vector<KeyNamePair>();
		
		String sql1 =  "SELECT upper(Name),  XX_VCN_EVALUATIONCRITERIA_ID "+ 
					   "FROM XX_VCN_EVALUATIONCRITERIA "+
					   "WHERE IsActive = 'Y' AND XX_VCN_EVALUATIONCRITERIA_ID <>" + Env.getCtx().getContextAsInt("#XX_L_EC_TOTALSCOREOC_ID") + " " +
					   " AND XX_VCN_EVALUATIONCRITERIA_ID <> " + Env.getCtx().getContextAsInt("#XX_L_EC_SCOREACCUMUPREVI_ID");
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql1, null);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()){
				AuxName = rs.getString("upper(Name)");
				AuxID = rs.getInt("XX_VCN_EVALUATIONCRITERIA_ID");
				AuxEvaluationCriteria.add(new KeyNamePair(AuxID,AuxName));
			}
			rs.close();
			pstmt.close();
			return AuxEvaluationCriteria;
			
		} catch (Exception e) {
			return null;
		}
	}

	private void InsertPromedio(int C_BPartner_ID) {
		String sql =  "SELECT AVG(XX_POINTS) "+ 
				      "FROM XX_VCN_VENDORRATING "+
				      "WHERE XX_C_ORDER_ID != "+ p_C_Order_ID +" and "+
				       	    "C_BPARTNER_ID = "+C_BPartner_ID+" and "+
				      	    "XX_VCN_EvaluationCriteria_ID = "+Env.getCtx().getContext("#XX_L_EC_TOTALSCOREOC_ID")+" and " +
				      	    "IsActive = 'Y' and " +
				      	    "XX_C_ORDER_ID in (select C_ORDER_ID " +
				      	    				  "from C_ORDER " +
				      	    				  "where C_ORDER_ID is not null " +
				      	    				  "AND XX_EVALUATED = 'Y'" +
				      	    				  "and round(sysdate-XX_ENTRANCEDATE,2) < "+Env.getCtx().getContext("#XX_L_DAYSCALCULAVENDORRATING")+")";
		
		
		String avg = new String();
		//System.out.println(sql);
		avg = BuscarValor(sql, "AVG(XX_POINTS)");
		
		if(avg == null){
			avg = new String("0");			
		}
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, Env.getCtx().getContextAsInt("#XX_L_EC_SCOREACCUMUPREVI_ID") ,C_BPartner_ID, new BigDecimal(avg) ) ;
	}
	
	private String BuscarValorAcuerdos(String sql){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			BigDecimal zero = BigDecimal.ZERO;
			int NumDescuentos = 0;
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if (rs.next()){
				
				if(zero.compareTo(rs.getBigDecimal("XX_CENTRADISCDELIAMOUNT"))<0 || zero.compareTo(rs.getBigDecimal("XX_CENTRADISCDELIPERCEN"))<0)
					NumDescuentos++;
				if(zero.compareTo(rs.getBigDecimal("XX_DISCOUNTGOSAMOUNT"))<0 || zero.compareTo(rs.getBigDecimal("XX_DISCOUNTGOSPERCENTAGE"))<0)
					NumDescuentos++;
				if(zero.compareTo(rs.getBigDecimal("XX_DISCRECOGDECLAMOUNT"))<0 || zero.compareTo(rs.getBigDecimal("XX_DISCRECOGDECLPERCEN"))<0)
					NumDescuentos++;
				if(zero.compareTo(rs.getBigDecimal("XX_DISCAFTERSALEAMOUNT"))<0 || zero.compareTo(rs.getBigDecimal("XX_DISCAFTERSALEPERCEN"))<0)
					NumDescuentos++;
				if(zero.compareTo(rs.getBigDecimal("XX_FIXVOLDISCOPERCEN"))<0 || zero.compareTo(rs.getBigDecimal("XX_FIXVOLDISCOAMOUNT"))<0)
					NumDescuentos++;
				if(zero.compareTo(rs.getBigDecimal("XX_FIRSTVARVOLDISCOPERCEN"))<0)
					NumDescuentos++;
				if(zero.compareTo(rs.getBigDecimal("XX_SECONDVARVOLDISCOPERCEN"))<0)
					NumDescuentos++;
				
				/* DESCOMENTAR A FUTURO
				if(zero.compareTo(rs.getBigDecimal("XX_PUBAMOUNT"))<0 || zero.compareTo(rs.getBigDecimal("XX_PUBPERCENT"))<0)
					NumDescuent++;
				if(zero.compareTo(rs.getBigDecimal("XX_DISCOPENSTORAMOUNT"))<0 || zero.compareTo(rs.getBigDecimal("XX_DISCOPENSTORPERCENT"))<0)
					NumDescuent++;
				*/
			}
			return new Integer(NumDescuentos).toString();			
		}catch (SQLException e) {
			return null;
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	

	private boolean buscarIdentificacionProducto(Integer M_INOUT_ID){
		String sql = "SELECT * " +
					 "from M_INOUTLINE " +
					 "where M_INOUT_ID = "+M_INOUT_ID + " AND XX_ProductIdentification = 'N' ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				return false;
			}
			return true;			
		}catch (SQLException e) {
			return false;
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}		
	
	private Double buscarTotalPiezas(Integer M_INOUT_ID){
		String sql = "SELECT sum(TARGETQTY) TOTAL " +
					 "from M_INOUTLINE " +
					 "where M_INOUT_ID = "+M_INOUT_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Double auxCampo = new Double(0); 
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				auxCampo = rs.getDouble("TOTAL");				
			}
			
			if(auxCampo == null){
				auxCampo = new Double(0);			
			}
			
			return auxCampo;			
		}catch (SQLException e) {
			return new Double(0);
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}	
	
	private Double buscarTotalPiezasRecibida(Integer M_INOUT_ID){
		String sql = "SELECT sum(PICKEDQTY) RECIBIDA " +
					 "from M_INOUTLINE " +
					 "where M_INOUT_ID = "+M_INOUT_ID;
		try {
			Double auxCampo = new Double(0); 
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				auxCampo = rs.getDouble("RECIBIDA");				
			}
			if(auxCampo == null){
				auxCampo = new Double(0);			
			}
			
			rs.close();
			pstmt.close();
			return auxCampo;			
		}catch (SQLException e) {
			return new Double(0);
		}
	}	
	
	private Double buscarTotalPiezasFaltantes(Integer M_INOUT_ID){
		String sql = "SELECT sum(XX_MissingPieces) FALTANTES " +
					 "from M_INOUTLINE " +
					 "where M_INOUT_ID = "+M_INOUT_ID;
		try {
			Double auxCampo = new Double(0); 
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				auxCampo = rs.getDouble("FALTANTES");				
			}
			
			if(auxCampo == null){
				auxCampo = new Double(0);			
			}
			
			rs.close();
			pstmt.close();
			return auxCampo;			
		}catch (SQLException e) {
			return new Double(0);
		}
	}
	
	
	private Double buscarTotalPiezasSobrantes(Integer M_INOUT_ID){
		String sql = "SELECT sum(XX_ExtraPieces) SOBRANTES " +
					 "from M_INOUTLINE " +
					 "where M_INOUT_ID = "+M_INOUT_ID;
		try {
			Double auxCampo = new Double(0); 
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				auxCampo = rs.getDouble("SOBRANTES");				
			}
			
			if(auxCampo == null){
				auxCampo = new Double(0);			
			}
			
			rs.close();
			pstmt.close();
			return auxCampo;			
		}catch (SQLException e) {
			return new Double(0);
		}
	}

	private Double searchReferencesTotal(Integer C_Order_ID){
		
		String sql = "SELECT count(DISTINCT(PRO.XX_VMR_VENDORPRODREF_ID)) TOTAL " +
					 "FROM C_ORDERLINE OL, XX_VMR_VENDORPRODREF VR, M_PRODUCT PRO " +
					 "WHERE PRO.XX_VMR_VENDORPRODREF_ID = VR.XX_VMR_VENDORPRODREF_ID AND " +
					 "OL.M_PRODUCT_ID = PRO.M_PRODUCT_ID AND " +
					 "OL.C_Order_ID = " + C_Order_ID;
		
		Double auxCampo = new Double(0);
		try {
			 
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()){
				auxCampo = rs.getDouble("TOTAL");
				rs.close();
				pstmt.close();
				return auxCampo;
			}
			else{
				rs.close();
				pstmt.close();
				return auxCampo;
			}
			
		}catch (SQLException e) {
			return new Double(0);
		}
	}	
	
	private Double searchReceivedReferencesTotal(Integer C_Order_ID){
		
		String sql = "SELECT count(DISTINCT(PRO.XX_VMR_VENDORPRODREF_ID)) TOTAL " +
		 "FROM M_INOUT IO, M_INOUTLINE IOL, XX_VMR_VENDORPRODREF VR, M_PRODUCT PRO " +
		 "WHERE IO.M_INOUT_ID = IOL.M_INOUT_ID AND " +
		 "PRO.XX_VMR_VENDORPRODREF_ID = VR.XX_VMR_VENDORPRODREF_ID AND " +
		 "IOL.M_PRODUCT_ID = PRO.M_PRODUCT_ID AND " +
		 "IO.C_Order_ID = " + C_Order_ID;

		Double auxCampo = new Double(0);
		try {
		
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				auxCampo = rs.getDouble("TOTAL");
				rs.close();
				pstmt.close();
				return auxCampo;
			}
			else{
				rs.close();
				pstmt.close();
				return auxCampo;
			}
			
		}catch (SQLException e) {
			return new Double(0);
		}
	}
	
	private Double buscarTotalPiezasCalidad(Integer M_INOUT_ID){
		String sql = "SELECT sum(PICKEDQTY) - sum(SCRAPPEDQTY) CALIDAD " +
					 "from M_INOUTLINE " +
					 "where M_INOUT_ID = "+M_INOUT_ID;
		try {
			Double auxCampo = new Double(0); 
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				auxCampo = rs.getDouble("CALIDAD");				
			}
			
			if(auxCampo == null){
				auxCampo = new Double(0);			
			}
			
			rs.close();
			pstmt.close();
			return auxCampo;			
		}catch (SQLException e) {
			return new Double(0);
		}
	}
	
	private BigDecimal calcularDescuentosAcuerdoComercial (Integer evaluationCriteriaID, MOrder AuxOrder ){
		String queComparo;
		String sql;
		String porcentaje;
		String Puntos;
		
		X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(Env.getCtx(), evaluationCriteriaID, null);
		
		sql = "select * " +
			  "from XX_VCN_TRADEAGREEMENTS " +
			  "where C_BPARTNER_ID = " + AuxOrder.getC_BPartner_ID() + " and "+
			  		"XX_VMR_CATEGORY_ID = " + AuxOrder.getXX_Category_ID() + " and "+
					"XX_DATEEFFECTIVEFROM <= SYSDATE and "+
					"XX_EFFECTIVEDATETO >= SYSDATE" +
					" AND AD_Client_ID = " + 
					Env.getCtx().getContext("#XX_L_CLIENTCENTROBECO_ID");
		
		queComparo = BuscarValorAcuerdos(sql);
		
		sql = "SELECT XX_Percentage " +
			   "FROM XX_VCN_WeightCalculation " +
			   "WHERE XX_EvaluationCriteria_ID = "+Env.getCtx().getContextAsInt("#XX_L_EC_DISCOTRADAGRE_ID")+" AND " +
			   		  queComparo + " <= XX_MaxValueRange AND " +
			   		  queComparo + " >= XX_MinValueRange" +
			   		" AND AD_Client_ID = " + 
					Env.getCtx().getContext("#XX_L_CLIENTCENTROBECO_ID");
		
		porcentaje = BuscarValor(sql, "XX_Percentage");
		
		Puntos = AuxEval.getXX_POINT().toString();
						
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, evaluationCriteriaID, AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

		return (new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) );
	}
	
	private BigDecimal calcularCumplimientoFechadeEntrega (Integer evaluationCriteriaID, MOrder AuxOrder ){
		String sql;
		String porcentaje;
		String Puntos;
		
		X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(Env.getCtx(), evaluationCriteriaID, null);
		
		Calendar dateReal;
		Calendar dateEstimated;
		//Captura de fechas
		if (AuxOrder.getXX_VLO_TypeDelivery().equals(X_Ref_XX_Ref_TypeDelivery.DESPACHO_DIRECTO .getValue())){

			dateReal = Calendar.getInstance();
			dateReal.setTimeInMillis(AuxOrder.getXX_ReceptionDate().getTime());
			dateEstimated = Calendar.getInstance();
			dateEstimated.setTimeInMillis(AuxOrder.getXX_EstimatedDate().getTime());
			
		}else{
			
			dateReal = Calendar.getInstance();
			dateReal.setTimeInMillis(AuxOrder.getXX_EntranceDate().getTime());
			dateEstimated = Calendar.getInstance();
			dateEstimated.setTimeInMillis(AuxOrder.getXX_EstimatedDate().getTime());
		}
		
		//Realizo la operación
		long time = dateReal.getTime().getTime() - dateEstimated.getTime().getTime();
		//Muestro el resultado en días
		Long time1 = time/(3600*24*1000);
		
		//Si la entrega llega antes, le asigno el % que tenga Cero
		if(time1 < 0){
			time1 = new Long(0);
		}
		
		sql = "SELECT XX_Percentage " +
			   "FROM XX_VCN_WeightCalculation " +
			   "WHERE XX_EvaluationCriteria_ID = "+Env.getCtx().getContextAsInt("#XX_L_EC_COMPIDELIVEDATE_ID")+" AND " +
			   		  time1.toString() + " <= XX_MaxValueRange AND " +
			   		  time1.toString() + " >= XX_MinValueRange";
		
		
		porcentaje = BuscarValor(sql, "XX_Percentage");
		
		Puntos = AuxEval.getXX_POINT().toString();
						
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, evaluationCriteriaID, AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

		return (new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) );
	}
	
	private BigDecimal calcularCumplimientoCantidad (Integer evaluationCriteriaID, MOrder AuxOrder ){
		String Puntos;
		
		Double totalPiezasOrdenadas = buscarTotalPiezas(p_M_INOUT_ID);
		Double totalPiezasFaltantes = buscarTotalPiezasFaltantes(p_M_INOUT_ID);
		Double totalPiezasSobrantes = buscarTotalPiezasSobrantes(p_M_INOUT_ID);
		Double totalPiezasCantidad = 0.0;
		
		X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(Env.getCtx(), evaluationCriteriaID, null);
		
		
		if(totalPiezasSobrantes > totalPiezasOrdenadas){
			totalPiezasSobrantes =  totalPiezasOrdenadas;
		} 
		
		totalPiezasCantidad = (totalPiezasOrdenadas - totalPiezasFaltantes )*0.6 + (totalPiezasOrdenadas - totalPiezasSobrantes)*0.4;
		totalPiezasCantidad = totalPiezasCantidad / totalPiezasOrdenadas;
		
		Double porcentajeUsar = totalPiezasCantidad * 100;
		
		Puntos = AuxEval.getXX_POINT().toString();
						
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, evaluationCriteriaID, AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentajeUsar).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

		return( (new BigDecimal(porcentajeUsar).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) ));
	}
	
	private BigDecimal calcularCumplimientoCalidad(Integer evaluationCriteriaID, MOrder AuxOrder ){
		String Puntos;
		
		Double totalPiezas = buscarTotalPiezas(p_M_INOUT_ID);
		Double totalPiezasCalidad = buscarTotalPiezasCalidad(p_M_INOUT_ID);
		X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(Env.getCtx(), evaluationCriteriaID, null);
		
		if(totalPiezasCalidad > totalPiezas){
			totalPiezasCalidad =  totalPiezas;
		} 
		
		Double porcentajeUsar = totalPiezasCalidad / totalPiezas * 100;
		
		Puntos = AuxEval.getXX_POINT().toString();
						
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, evaluationCriteriaID, AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentajeUsar).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

		return( (new BigDecimal(porcentajeUsar).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) ));
	}
	
	private BigDecimal calcularCumplimientoSurtido (Integer evaluationCriteriaID, MOrder AuxOrder ){
		String Puntos;
		
		Double totalReferencias = searchReferencesTotal(AuxOrder.get_ID());
		Double totalReferenciasRecibidas = searchReceivedReferencesTotal(AuxOrder.get_ID());
		
		X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(Env.getCtx(), evaluationCriteriaID, null);
		
		Double porcentajeUsar = new Double(0);
		if(totalReferencias!=0)
			porcentajeUsar = totalReferenciasRecibidas / totalReferencias * 100;
		
		Puntos = AuxEval.getXX_POINT().toString();
						
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, evaluationCriteriaID, AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentajeUsar).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

		return( (new BigDecimal(porcentajeUsar).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) ));
	}
	
	private BigDecimal calcularCondicionPago(Integer evaluationCriteriaID, MOrder AuxOrder ){
		String sql;
		String porcentaje;
		String Puntos;
	
		X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(Env.getCtx(), evaluationCriteriaID, null);
		
		Integer AuxPaymentTerm = AuxOrder.getC_PaymentTerm_ID();
		
		sql = "SELECT XX_Percentage " +
			   "FROM XX_VCN_WeightCalculation " +
			   "WHERE XX_EvaluationCriteria_ID = "+Env.getCtx().getContextAsInt("#XX_L_EC_PAYMENTTERM_ID")+" AND " +
			   		  AuxPaymentTerm + " = PO_PaymentTerm_ID";
		porcentaje = BuscarValor(sql, "XX_Percentage");

		Puntos = AuxEval.getXX_POINT().toString();
						
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, evaluationCriteriaID, AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

		return( (new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) ));
	}
	
	private BigDecimal calcularIdentificacionDocumentos(Integer evaluationCriteriaID, MOrder AuxOrder ){
		
		String sql;
		String porcentaje;
		String Puntos;
		String queComparo;
		BigDecimal valor = new BigDecimal(0);
		
		Boolean identificacionProductos = buscarIdentificacionProducto(p_M_INOUT_ID);
		Boolean identificacionPackage = (new MInOut(Env.getCtx(), p_M_INOUT_ID, null)).isXX_PackageIdentification() ;
		
		X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(Env.getCtx(), evaluationCriteriaID, null);
		
		//Si se rechazo la entrada, es porque la factura vino mala en primera instancia AuxOrder.isXX_NotAuthorized()		
		if(identificacionProductos && identificacionPackage && !AuxOrder.isXX_NotAuthorized()){
			queComparo = "Y";
		}else{
			queComparo = "N";
		}
		
		sql = "SELECT XX_Percentage " +
			   "FROM XX_VCN_WeightCalculation " +
			   "WHERE XX_EvaluationCriteria_ID = "+Env.getCtx().getContextAsInt("#XX_L_EC_DIRESTODELI_ID")+" AND '" +
			   		  queComparo + "' = XX_ATRIBUTTEVALUE ";
		porcentaje = BuscarValor(sql, "XX_Percentage");

		Puntos = AuxEval.getXX_POINT().toString();
			
		valor = (new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) );
		
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, evaluationCriteriaID, AuxOrder.getC_BPartner_ID(), valor  );
		return valor;
	}

	private BigDecimal calcularDirectoTienda(Integer evaluationCriteriaID, MOrder AuxOrder ){
		String sql;
		String porcentaje;
		String Puntos;
		String queComparo;
		
		X_XX_VCN_EvaluationCriteria AuxEval = new X_XX_VCN_EvaluationCriteria(Env.getCtx(), evaluationCriteriaID, null);
		
		queComparo = AuxOrder.getXX_VLO_TypeDelivery();
		
		if (X_Ref_XX_Ref_TypeDelivery.DESPACHO_DIRECTO.getValue().equals(queComparo)){
			queComparo = "Y";
		}else{
			queComparo = "N";	
		}
		
		sql = "SELECT XX_Percentage " +
			   "FROM XX_VCN_WeightCalculation " +
			   "WHERE XX_EvaluationCriteria_ID = "+Env.getCtx().getContextAsInt("#XX_L_EC_DIRESTODELI_ID")+" AND '" +
			   		  queComparo + "' = XX_ATRIBUTTEVALUE ";
		porcentaje = BuscarValor(sql, "XX_Percentage");

		Puntos = AuxEval.getXX_POINT().toString();
						
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, evaluationCriteriaID, AuxOrder.getC_BPartner_ID(), new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100))  );

		return( (new BigDecimal(porcentaje).multiply(new BigDecimal(Puntos)).divide(new BigDecimal(100)) ));
	}
	
	private Vector<Integer> buscarEvaluados(Integer order_ID){
		
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		
		MOrder OC = new MOrder(Env.getCtx(), order_ID, null);
		String sql = "SELECT * " +
					 "FROM XX_VCN_VENDORRATING V " +
					 "WHERE V.XX_C_ORDER_ID = "+ order_ID;
		Vector<Integer> aux = new Vector<Integer>();
		
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				
				aux.add(order_ID);
				
				sql =   "SELECT XX_C_ORDER_ID " +
						"FROM XX_VCN_VENDORRATING V " +
						"WHERE V.XX_VCN_EVALUATIONCRITERIA_ID = "+Env.getCtx().getContext("#XX_L_EC_TOTALSCOREOC_ID")+" " +
						"AND V.C_BPARTNER_ID = "+OC.getC_BPartner_ID()+" " +
						"AND V.CREATED >= " +
						"(   SELECT CREATED" +
						"    FROM XX_VCN_VENDORRATING" +
						"    WHERE XX_C_ORDER_ID = "+order_ID+" " +
						"    AND XX_VCN_EVALUATIONCRITERIA_ID = "+Env.getCtx().getContext("#XX_L_EC_TOTALSCOREOC_ID")+"" +
						") ";
				//System.out.println(sql);
					try{
						pstmt1 = DB.prepareStatement(sql, null);
						rs1 = pstmt1.executeQuery();
						while (rs1.next()){
							if(aux.get(0)!=rs1.getInt("XX_C_ORDER_ID"))
								aux.add(new Integer(rs1.getInt("XX_C_ORDER_ID")));
						}
					}catch (SQLException e) {
						return new Vector<Integer>();
					}
					finally{
						DB.closeResultSet(rs1);
						DB.closeStatement(pstmt1);
					}
			}
			
			if(! aux.isEmpty()){
				sql = "DELETE FROM XX_VCN_VENDORRATING WHERE XX_C_ORDER_ID IN (";
				for (int i = 0; i < aux.size(); i++) {
					if(i==0)
						sql = sql + aux.elementAt(i);
					else
						sql = sql + ","+aux.elementAt(i);
				}
				sql = sql + ")";
				//System.out.println(sql);

				try {
					DB.executeUpdate(null,sql);
				
				}catch (Exception e) {
					return new Vector<Integer>();
				}

			}
			return aux;

		}catch (SQLException e) {
			return new Vector<Integer>();
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
	
	private Integer buscarMInOut(Integer C_Order_ID){
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Integer M_INOUT_ID = new Integer(0);
		String sql = "SELECT M_INOUT_ID " +
					 "FROM M_INOUT MIO " +
					 "WHERE MIO.C_ORDER_ID = "+ C_Order_ID;
		//System.out.println(sql);
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if (rs.next()){
				M_INOUT_ID = rs.getInt("M_INOUT_ID");
			}
			
			return M_INOUT_ID;
			
		}catch (SQLException e) {
			return M_INOUT_ID;
		}
		finally{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
	}
	
	private String ejecutarWeigthNoAutorizado(Integer C_Order_ID){
		p_C_Order_ID = C_Order_ID;
		
		Vector<KeyNamePair> AuxEvaluationCriteria = selectEvaluationCriteria();
		BigDecimal Suma = new BigDecimal(0);
		
		MOrder AuxOrder = new MOrder(Env.getCtx(), p_C_Order_ID, null);
		
		InsertPromedio(AuxOrder.getC_BPartner_ID());

		for (int i = 0; i < AuxEvaluationCriteria.size(); i++){
			if ( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_DISCOTRADAGRE_ID") ){
				Suma = Suma.add( calcularDescuentosAcuerdoComercial(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );	
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_COMPIDELIVEDATE_ID") ){
				if (AuxOrder.getXX_ReceptionDate() == null){
					InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder.getC_BPartner_ID(), new BigDecimal(0)  );
					Suma = Suma.add( new BigDecimal(0) );
				}else{
					Suma = Suma.add( calcularCumplimientoFechadeEntrega(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
				}
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_COMPIDELIVEQUANTI_ID") ){
				InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder.getC_BPartner_ID(), new BigDecimal(0)  );
				Suma = Suma.add( new BigDecimal(0) );
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_COMPIDELIVEQUALI_ID") ){				
				InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder.getC_BPartner_ID(), new BigDecimal(0)  );
				Suma = Suma.add( new BigDecimal(0) );
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_PAYMENTTERM_ID") ){
				Suma = Suma.add( calcularCondicionPago(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_IDENTIMERCDOC_ID") ){
				InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder.getC_BPartner_ID(), new BigDecimal(0)  );
				Suma = Suma.add( new BigDecimal(0) );
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_DIRESTODELI_ID") ){
				Suma = Suma.add( calcularDirectoTienda(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
			}
			else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_COMPIDELIVEASSORT_ID") ){				
				InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder.getC_BPartner_ID(), new BigDecimal(0)  );
				Suma = Suma.add( new BigDecimal(0) );	
			}
			
		}
		
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID, Env.getCtx().getContextAsInt("#XX_L_EC_TOTALSCOREOC_ID"), AuxOrder.getC_BPartner_ID(), Suma );

		return Suma.toString();
	}

	private String ejecutarWeigthIndividual(Integer C_Order_ID, Integer M_INOUT_ID){
		
		p_C_Order_ID = C_Order_ID;
		p_M_INOUT_ID = M_INOUT_ID;
		
		Vector<KeyNamePair> AuxEvaluationCriteria = selectEvaluationCriteria();
		BigDecimal Suma = new BigDecimal(0);
		
		//Actualizo la Orden de Compra en Chequeada
		MOrder AuxOrder = new MOrder(Env.getCtx(), p_C_Order_ID, null);
		/*AuxOrder.setXX_OrderStatus("CH");
		AuxOrder.save();*/
		
		/*
		 * se hace el insert de la del promedio de los anteriores. El valor no toma en cuenta los valores obtenidos en
		 * esta orden de compra. Es la puntuacion que se tiene para esta orden de compra
		 */
		InsertPromedio(AuxOrder.getC_BPartner_ID());
		
		//recorrer el vector de Criterios de Evaluacion
		for (int i = 0; i < AuxEvaluationCriteria.size(); i++){
			if ( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_DISCOTRADAGRE_ID") ){
				Suma = Suma.add( calcularDescuentosAcuerdoComercial(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_COMPIDELIVEDATE_ID") ){
				Suma = Suma.add( calcularCumplimientoFechadeEntrega(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );				
			
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_COMPIDELIVEQUANTI_ID") ){
				Suma = Suma.add( calcularCumplimientoCantidad(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_COMPIDELIVEQUALI_ID") ){
				 Suma = Suma.add( calcularCumplimientoCalidad(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_PAYMENTTERM_ID") ){
				Suma = Suma.add( calcularCondicionPago(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
								
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_IDENTIMERCDOC_ID") ){
				 Suma = Suma.add( calcularIdentificacionDocumentos(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
			
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_DIRESTODELI_ID") ){
				Suma = Suma.add( calcularDirectoTienda(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
				
			}else if( AuxEvaluationCriteria.elementAt(i).getKey() == Env.getCtx().getContextAsInt("#XX_L_EC_COMPIDELIVEASSORT_ID") ){
				 Suma = Suma.add( calcularCumplimientoSurtido(AuxEvaluationCriteria.elementAt(i).getKey(), AuxOrder) );
			}

		}
		
		InsertXX_VCN_VendorRating(Env.getCtx(), p_C_Order_ID,  Env.getCtx().getContextAsInt("#XX_L_EC_TOTALSCOREOC_ID"), AuxOrder.getC_BPartner_ID(), Suma );
		/*
		 * se hace el insert de la del promerio de los anteriores. El valor no toma en cuenta los valores obtenidos en
		 * esta orden de compra. Es la puntuacion que se tiene para esta orden de compra
		 */
		//InsertPromedio(AuxOrder.getC_BPartner_ID(), SumaID, Suma);
		return Suma.toString();
	}	
	
	public String ejecutarWeigth(Integer C_Order_ID, Integer M_INOUT_ID) {
		Vector<Integer> reevaluar = buscarEvaluados(C_Order_ID);
		Integer M_InOut_ID = new Integer(0);
		String Suma = new String();
		
		if(!reevaluar.isEmpty()){
			//Suma = ejecutarWeigthIndividual(C_Order_ID, M_INOUT_ID);
	
			for (int i = 0; i < reevaluar.size(); i++) {
				M_InOut_ID = buscarMInOut(reevaluar.elementAt(i));
				MInOut auxMInOut = new MInOut(Env.getCtx(), M_InOut_ID, null);
				
				//evaluo normal si tiene sus MINOUT creados
				if (M_InOut_ID.equals(new Integer(0)) ){
					ejecutarWeigthNoAutorizado(reevaluar.elementAt(i));
				}else{
					//si la orden va a tienda y queda invalida la evaluo como no autorizada
					//en caso contrario la evaluo bien
					if(auxMInOut.getDocStatus().equals(X_Ref__Document_Status.INVALID.getValue())){
						ejecutarWeigthNoAutorizado(reevaluar.elementAt(i));
					}else{
						ejecutarWeigthIndividual(reevaluar.elementAt(i), M_InOut_ID);						
					}
				}
			}
			return Suma;
		}else {
			if(M_INOUT_ID == 0){
				return ejecutarWeigthNoAutorizado(C_Order_ID);
			}else{
				return ejecutarWeigthIndividual(C_Order_ID, M_INOUT_ID);
			}
		}
	}
	
	/**
	 * Termina el Calculo de la puntuacion del Proveedor
	 */	
		
	/*
	 * Avisos de debito y credito! 
	 */
	
	Timestamp fechaActual = new Timestamp(new Date().getTime());
	Ctx ctx = Env.getCtx();
	MInvoice 	DebtCreditQuantity = null, 
				DebtCreditPrice = null;
	//Trx transCosto = Trx.get("transaccion",true);
	//Trx transCant = Trx.get("transaccion",true);
	
	/**
	 * Genera la retención de IVA de una factura, en el libro de compra e imprime el reporte de retención
	 * @author modificado por Jessica Mendoza, JTrias
	 * @param cOrderID id de la orden de compra
	 * @param invoiceID id de la factura especifica
	 */
	public boolean generateInvoiceReport(int cOrderID, int invoiceID, boolean ret){
		boolean flagPBook = false;
		MVCNPurchasesBook pBookInvoice = null;	
		String sql;
		
		if (cOrderID != 0){
			sql = "select C_Invoice_ID from C_Invoice where C_Order_ID = " + cOrderID + " " +
			  	"and DocStatus in ('CO', 'CL') AND C_DocTypeTarget_ID = "+Env.getCtx().getContext("#XX_C_DOCTYPE_ID")+" ";
		}else{
			sql = "select C_Invoice_ID from C_Invoice where C_Invoice_ID = " + invoiceID + " " +
			"and DocStatus in ('CO', 'CL') AND C_DocTypeTarget_ID IN ("+Env.getCtx().getContext("#XX_C_DOCTYPE_ID")+", "+Env.getCtx().getContext("#XX_L_C_DOCTYPECREDIT_ID")+", "+Env.getCtx().getContext("#XX_L_C_DOCTYPEDEBIT_ID")+")";
		}
		
		try {
			PreparedStatement pstmt1 = DB.prepareStatement(sql, null);
			ResultSet rs1 = pstmt1.executeQuery();
			while (rs1.next()){
				MInvoice invoice = new MInvoice(Env.getCtx(), rs1.getInt(1), null);
				// Setting Purchase's Book values 
		        pBookInvoice = createPurchasesBook(invoice, ret, flagPBook, null); 
		        if(pBookInvoice == null)
					log.log(Level.WARNING, 
							"Purchases' Book wasn't successfully created");
				else
					flagPBook = true;							        				
		     }
			rs1.close();
			pstmt1.close();
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);		
			return false;
		}
		
		if(cOrderID==0){			
			showReport(pBookInvoice);
		}
		
		return true;
	}
	
	public boolean generateDebtCreditNotifies(int cOrderID, Trx transaction)
	{
		MOrder mOrder = new MOrder(ctx, cOrderID, null);
		
		String SQL1 = "Select C_INVOICE_ID from C_INVOICE " +
		"where C_ORDER_ID = " + cOrderID + " AND C_DocTypeTarget_ID = "+Env.getCtx().getContext("#XX_C_DOCTYPE_ID")+" " +
		"AND DOCSTATUS in ('CO', 'CL')";

		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		try {
			pstmt1 = DB.prepareStatement(SQL1, null);
			rs1 = pstmt1.executeQuery();
			while (rs1.next()){
				MInvoice invoice = new MInvoice(
						Env.getCtx(), rs1.getInt(1), transaction);
				
				if(createDebtCreditNotify(invoice, transaction)){
		        	
					// Setting Purchase's Book values 
					if(DebtCreditQuantity != null){
						MVCNPurchasesBook pBookDebt = createPurchasesBook(
								DebtCreditQuantity, false, true, transaction); 
						if(pBookDebt == null)
							log.log(Level.WARNING, 
									"Purchases' Book wasn't successfully created");
					}
						
					// Setting Purchase's Book values 
					if(DebtCreditPrice != null){
						MVCNPurchasesBook pBookCredit = createPurchasesBook(
								DebtCreditPrice, false, true, transaction); 
						if(pBookCredit == null)
							log.log(Level.WARNING, 
									"Purchases' Book wasn't successfully created");
					}
										
				}
				invoice.setXX_PrintDefinitive("Y");
				if(!invoice.save())
					log.log(Level.WARNING, "Print Definitive Button wasn't successfully updated");
				
				DebtCreditPrice = null; 
				DebtCreditQuantity = null;
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, SQL1, e);	
			e.printStackTrace();
			return false;
		}finally
		{
			DB.closeStatement(pstmt1);
			DB.closeResultSet(rs1);
		}
				
		String SQL2 = "Select M_INOUT_ID from M_INOUT " +
		"where C_ORDER_ID = " + cOrderID  + " AND DOCSTATUS in ('CO', 'CL')";
		PreparedStatement pstmt2 = null;
		ResultSet rs2 = null;
		try {
			pstmt2 = DB.prepareStatement(SQL2, null);
			   rs2 = pstmt2.executeQuery();
			while (rs2.next()){
				MInOut mInOut = new MInOut(
						Env.getCtx(), rs2.getInt(1), null);
				
				mInOut.setXX_PrintDefinitive("Y");
				if(!mInOut.save())
					log.log(Level.WARNING, 
							"Print Definitive Button wasn't successfully updated");
				
			}			
		} catch (SQLException e) {
			log.log(Level.SEVERE, SQL2, e);
		}finally
		{
			DB.closeStatement(pstmt2);
			DB.closeResultSet(rs2);
		}
		
		//Aviso de Penalizacion por retraso en entrega de la mercancia
		if(mOrder.getXX_OrderType().equals("Nacional"))
			if(mOrder.getXX_PurchaseType()==null)
				discountDelay(mOrder);
			else
				if(!mOrder.getXX_PurchaseType().equals("SE"))
					discountDelay(mOrder);
		
		mOrder.setDocAction(MOrder.DOCACTION_Close);
		mOrder.setDocStatus(MOrder.DOCACTION_Close);
		
		return true;		
	}
	
	/**
	 * Acuerdo comercial
	 * @param mOrder
	 */
	public void tradeAgreement(int cOrderID, Trx transaction){
		
		BigDecimal centralDelivDisc = BigDecimal.ZERO; //Descuento por centralización de entregas
		BigDecimal afterSaleDisc = BigDecimal.ZERO;	   //Descuento post venta
		BigDecimal recogDisc = BigDecimal.ZERO;		   //Descuento por reconocimiento de merma		
		BigDecimal iva = BigDecimal.ZERO;
		BigDecimal totalLines = BigDecimal.ZERO;
		BigDecimal sumTotalLines = BigDecimal.ZERO;
		BigDecimal sumCentralDelivDisc = BigDecimal.ZERO;
		BigDecimal sumAfterSaleDisc = BigDecimal.ZERO;
		BigDecimal sumRecogDisc = BigDecimal.ZERO;
		
		BigDecimal multiplyRate = BigDecimal.ZERO;
		boolean isPercent = false;
		boolean hasRs = false;
		boolean hasRs3 = true;
		boolean nationalCurrency = false;
		boolean flagDiscount = false;
		boolean globalFlag = false;
		int warehouseID = 0;
		int currencyID =0;
		X_XX_VCN_DetailAdvice detailAdvice = null;
		X_XX_CreditNotifyReturn creditNotify = new X_XX_CreditNotifyReturn(ctx, 0, transaction);
		
		MOrder mOrder = new MOrder(ctx, cOrderID, null);
		
		//Se revisa si el socio del negocio tiene acuerdo comercial vigente
		String SQL3 = "Select t.XX_VCN_TRADEAGREEMENTS_ID"
					+ " from XX_VCN_TRADEAGREEMENTS t, XX_VCN_TRADEAGRDEPARTMENT d" 
					+ " where t.C_BPARTNER_ID = " + mOrder.getC_BPartner_ID()
					+ " and t.XX_STATUS = 'VIGENTE' and d.XX_DEPARTAMENTO_ID = " + mOrder.getXX_VMR_DEPARTMENT_ID()
					+ " and d.ISACTIVE='Y'"
					+ " and d.XX_VCN_TRADEAGREEMENTS_ID = t.XX_VCN_TRADEAGREEMENTS_ID AND XX_AgreementType = 'AC' "
					+ " AND t.AD_CLIENT_ID="+  ctx.getAD_Client_ID();
				
				//Si hay varios vencidos se elige el de initDate mas reciente
		String SQL4 = "Select t.XX_VCN_TRADEAGREEMENTS_ID"
					+ " from XX_VCN_TRADEAGREEMENTS t, C_ORDER c, XX_VCN_TRADEAGRDEPARTMENT d" 
					+ " where t.C_BPARTNER_ID = " + mOrder.getC_BPartner_ID()
					+ " and t.XX_STATUS = 'VENCIDO' and c.C_BPARTNER_ID= "+mOrder.getC_BPartner_ID()
					+ " and d.XX_DEPARTAMENTO_ID = " + mOrder.getXX_VMR_DEPARTMENT_ID()
					+ " and d.XX_VCN_TRADEAGREEMENTS_ID = t.XX_VCN_TRADEAGREEMENTS_ID AND XX_AgreementType = 'AC' "
					+ " and d.ISACTIVE='Y'"
					+ " AND t.AD_CLIENT_ID="+  ctx.getAD_Client_ID()
					+ " and XX_APPROVALDATE > XX_INITDATE order by XX_APPROVALDATE asc";

		try {
			PreparedStatement pstmt3 = DB.prepareStatement(SQL3, null);
			ResultSet rs3 = pstmt3.executeQuery();//AC vigente
			PreparedStatement pstmt4 = DB.prepareStatement(SQL4, null);
			ResultSet rs4 = pstmt4.executeQuery();//AC vencido
			
			//Si no existe AC vigente se usa el vencido
			if (!rs3.next()){
				rs3=rs4;
				hasRs3=false;
			}
			if(rs4.next()) 
				hasRs=true;
					
			//Corregir si no existe ningún AC
			if (hasRs || hasRs3){
				X_XX_VCN_TradeAgreements agreement = new X_XX_VCN_TradeAgreements(Env.getCtx(), rs3.getInt(1), null);
				
				//Valido que sea un Acuerdo Comercial
				if(agreement.getXX_AgreementType().equals("AC")){
					
					//Valido que la orden de compra fue está en la fecha de vigencia del AC
					if((mOrder.getXX_ApprovalDate().after(agreement.getXX_InitDate()))){
						
						String SQL = "SELECT TOTALLINES, C_CURRENCY_ID, M_WAREHOUSE_ID," +
									 "C_INVOICE_ID, XX_VMR_DEPARTMENT_ID " +
									 "FROM C_INVOICE " +
									 "WHERE C_ORDER_ID = " + mOrder.get_ID() + " " +
									 "AND AD_CLIENT_ID="+  ctx.getAD_Client_ID()+ " "+
									 "AND C_DOCTYPETARGET_ID = " 
									 + Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID");
						try{	
							
							PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
							ResultSet rs = pstmt.executeQuery();
							
							//DETALLES
							int i = 0;
							while(rs.next()){
								i++;
								
								//CABECERA
								if(i==1){
									creditNotify.setC_Order_ID(mOrder.get_ID());
									creditNotify.setXX_NotificationType("ACC");
									creditNotify.setDescription("Acuerdo Comercial");
									creditNotify.save();
									
									//CALCULO DE TASA
									//Si hay tasa de cambio calculo el costo original en Bs
									if(mOrder.getXX_ConversionRate_ID()!=0){
										
										//creditNotify.setC_Conversion_Rate_ID(mOrder.getXX_ConversionRate_ID());
										
										String SQL6 = "SELECT MULTIPLYRATE "+
												      "FROM C_CONVERSION_RATE " +
												      "WHERE C_CONVERSION_RATE_ID="+ mOrder.getXX_ConversionRate_ID() +
												      " AND AD_CLIENT_ID="+  ctx.getAD_Client_ID();
											
										try{	
											
											PreparedStatement pstmt6 = DB.prepareStatement(SQL6, null); 
											ResultSet rs6 = pstmt6.executeQuery();
											
											if(rs6.next()){
												multiplyRate = rs6.getBigDecimal("MULTIPLYRATE");
											}
											
											rs6.close();
											pstmt6.close();
												   
										}
										catch (SQLException e) {
											log.log(Level.SEVERE, SQL6, e);
										}
									}
								}
								
								detailAdvice = new X_XX_VCN_DetailAdvice(ctx, 0, transaction);
								
								totalLines = rs.getBigDecimal("TOTALLINES");
								sumTotalLines = sumTotalLines.add(totalLines);
								warehouseID = rs.getInt("M_WAREHOUSE_ID");
								currencyID = rs.getInt("C_CURRENCY_ID");
								detailAdvice.setC_Invoice_ID(rs.getInt("C_INVOICE_ID"));
								detailAdvice.setXX_VMR_Department_ID(rs.getInt("XX_VMR_DEPARTMENT_ID"));
								
								//Si la moneda es nacional
								if (isPercent && rs.getInt("C_CURRENCY_ID")==205)
								    nationalCurrency = true;
								
								if (!isPercent && agreement.getC_Currency_ID()==205)
								    nationalCurrency = true;
							
							
								//Si el AC tiene descuento por centralización de entregas
								if(agreement.getXX_CENTRADISCDELIPERCEN().compareTo(BigDecimal.ZERO)!=0){
									centralDelivDisc = agreement.getXX_CENTRADISCDELIPERCEN().multiply(totalLines);
									centralDelivDisc = centralDelivDisc.divide(new BigDecimal(100));
									sumCentralDelivDisc = sumCentralDelivDisc.add(centralDelivDisc);
									flagDiscount=true;
									isPercent = true;
								}
								
								if (agreement.getXX_CENTRADISCDELIAMOUNT().compareTo(BigDecimal.ZERO)!=0){
									centralDelivDisc = agreement.getXX_CENTRADISCDELIAMOUNT();
									sumCentralDelivDisc = sumCentralDelivDisc.add(centralDelivDisc);
									flagDiscount=true;
								}
								
								//Si el AC tiene descuento post venta
								if(agreement.getXX_DISCAFTERSALEPERCEN().compareTo(BigDecimal.ZERO)!=0){
									afterSaleDisc = agreement.getXX_DISCAFTERSALEPERCEN().multiply(totalLines);
									afterSaleDisc = afterSaleDisc.divide(new BigDecimal(100));
									sumAfterSaleDisc = sumAfterSaleDisc.add(afterSaleDisc);
									flagDiscount=true;
									isPercent = true;
								}
								
								if (agreement.getXX_DISCAFTERSALEAMOUNT().compareTo(BigDecimal.ZERO)!=0){
									afterSaleDisc = agreement.getXX_DISCAFTERSALEAMOUNT();
									sumAfterSaleDisc = sumAfterSaleDisc.add(afterSaleDisc);
									flagDiscount=true;
								}
								
								//Si el AC tiene descuento por reconocimiento de merma
								if(agreement.getXX_DISCRECOGDECLPERCEN().compareTo(BigDecimal.ZERO)!=0){
									recogDisc = agreement.getXX_DISCRECOGDECLPERCEN().multiply(totalLines);
									recogDisc = recogDisc.divide(new BigDecimal(100));
									sumRecogDisc = sumRecogDisc.add(recogDisc);
									flagDiscount=true;
									isPercent = true;
								}
								
								if (agreement.getXX_DISCRECOGDECLAMOUNT().compareTo(BigDecimal.ZERO)!=0){
									recogDisc = agreement.getXX_DISCRECOGDECLAMOUNT();
									sumRecogDisc = sumRecogDisc.add(recogDisc);
									flagDiscount=true;
								}
							
								detailAdvice.setXX_CreditNotifyReturn_ID(creditNotify.getXX_CreditNotifyReturn_ID());
								detailAdvice.setM_Warehouse_ID(warehouseID);
								
								if(mOrder.getXX_ConversionRate_ID()!=0)
									detailAdvice.setXX_UnitPurchasePrice(totalLines.multiply(multiplyRate));
								else
									detailAdvice.setXX_UnitPurchasePrice(totalLines);
									
								detailAdvice.setXX_CENTRADISCDELIAMOUNT(centralDelivDisc);
								detailAdvice.setXX_DISCAFTERSALEAMOUNT(afterSaleDisc);
								detailAdvice.setXX_DISCRECOGDECLAMOUNT(recogDisc);
								detailAdvice.setXX_Month(detailAdvice.getCreated().getMonth()+1);
								detailAdvice.setXX_Year(detailAdvice.getCreated().getYear()+1900);
								detailAdvice.setC_Order_ID(mOrder.get_ID());
								
								if(flagDiscount)
									if (!detailAdvice.save())
										log.log(Level.WARNING,"Detail Advice wasn't successfully saved");
							
							}
								    rs.close();
								    pstmt.close();
								    
						}
						catch (Exception e) {
							System.out.println("ERROR GENERANDO EL AVISO DE DESCUENTO");
						}
	
						
						//Si hay algún descuento
						if(flagDiscount){
							
							globalFlag = true;
							
							//Si es nacional, se aplica el iva
							if (nationalCurrency){

								String SQL5 = "SELECT A.RATE, A.C_TAX_ID, B.C_TAXCATEGORY_ID " +
											  "FROM C_TAX A, C_TAXCATEGORY B " +
											  "WHERE " +
											  "VALIDFROM = (SELECT MAX(C.VALIDFROM) FROM C_TAX C WHERE C.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID) " +
											  " AND A.AD_CLIENT_ID="+  ctx.getAD_Client_ID()+
											  "AND A.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID AND B.NAME LIKE 'IVA'";
						
								try{	
							
									PreparedStatement pstmt5 = DB.prepareStatement(SQL5, null); 
									ResultSet rs5 = pstmt5.executeQuery();
										   
									while(rs5.next()){
								
										creditNotify.setC_TaxCategory_ID(rs5.getInt("C_TAXCATEGORY_ID"));
										iva = rs5.getBigDecimal("RATE");
										iva = iva.divide(new BigDecimal(100));
										creditNotify.setC_Tax_ID(rs5.getInt("C_TAX_ID"));			    	
									}
							
									rs5.close();
									pstmt5.close();				   
								}
								catch (Exception e) {
									System.out.println("ERROR GENERANDO EL AVISO DE DESCUENTO");
								}
						
								//Se calcula el IVA de los descuentos y se guarda
								creditNotify.setXX_Amount_IVA(sumAfterSaleDisc.multiply(iva).add(
								sumCentralDelivDisc.multiply(iva).add(sumRecogDisc.multiply(iva))));
							}
							else{
								creditNotify.setC_Tax_ID(ctx.getContextAsInt("#XX_L_TAX_EXENTO_ID")); //Si no es moneda nacional esta EXENTO(1000021) de iva
								creditNotify.setC_TaxCategory_ID(ctx.getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID"));
								creditNotify.setXX_Amount_IVA(BigDecimal.ZERO);
							}
										
		
							//Se guarda el monto total de los descuentos sin IVA
							if(mOrder.getXX_ConversionRate_ID()!=0){
								creditNotify.setXX_UnitPurchasePriceBs(sumTotalLines.multiply(multiplyRate));		
							}else
								creditNotify.setXX_UnitPurchasePriceBs(sumTotalLines);
							
							creditNotify.setXX_UnitPurchasePrice(sumTotalLines);
							creditNotify.setXX_Amount(sumAfterSaleDisc.add(sumCentralDelivDisc.add(sumRecogDisc)));
							
							
							//Guardo el tipo de moneda de la factura si el descuento es en %
							if(isPercent && currencyID!=0)
								creditNotify.setC_Currency_ID(currencyID);
							else // Sino guardo el tipo de moneda del AC
								creditNotify.setC_Currency_ID(agreement.getC_Currency_ID());
							
							if (!creditNotify.save())
								log.log(Level.WARNING,"Credit Notify wasn't successfully saved");
							
						}
					}
				}
			}	
			
			if(!globalFlag)
				creditNotify.delete(true);
				
					
			rs3.close();
			pstmt3.close();
			rs4.close();
			pstmt4.close();
					
		} catch (SQLException e) {
			log.log(Level.SEVERE, SQL3 + " - " +SQL4, e);
		}
				
	}
	
	/**
	 * Demora en entrega
	 * @param order
	 */
	public void discountDelay(MOrder order)
	{
		long days = 0;
		BigDecimal montoDesc = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal iva = BigDecimal.ZERO;
		BigDecimal ivaDesc = BigDecimal.ZERO;
		BigDecimal hundred = new BigDecimal(100);
		
		if(order.getXX_EstimatedDate().compareTo(order.getXX_ArrivalDate()) > 0){ //Si se modifico la fecha estimada
			
			if(order.getXX_MotivChanEstimDate_ID() == Env.getCtx().getContextAsInt("#XX_L_MOTIVECHANGEDBYVENDOR_ID")){  //Si el motivo del cambio es por proveedor
								
				long diff = order.getXX_EntranceDate().getTime() - order.getXX_ArrivalDate().getTime();
				days = (diff / (24 * 60 * 60 * 1000));
			}
			else{ //Si el motivo del cambio es por comprador u otro
				
				long diff = order.getXX_EntranceDate().getTime() - order.getXX_EstimatedDate().getTime();
				days = (diff / (24 * 60 * 60 * 1000));
			}
		}
		else if (order.getXX_EstimatedDate().compareTo(order.getXX_ArrivalDate()) == 0){ //Si no se modifico la fecha estimada de llegada pero igual el proveedor llegó tarde (por beneficio)
			
			Calendar cal = Calendar.getInstance();      
			cal.setTime(order.getXX_EntranceDate());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			Date entranceDate = cal.getTime();
			
			if(order.getXX_EstimatedDate().compareTo(entranceDate) < 0){ //Si la fecha de llegada a CD es mayor a la estimada (pudo llegar antes)
				
				long diff = order.getXX_EntranceDate().getTime() - order.getXX_EstimatedDate().getTime();
				days = (diff / (24 * 60 * 60 * 1000));
			}	
		}	
		
		if(days>=7){
			
			MCreditNotifyReturn notice = new MCreditNotifyReturn( Env.getCtx(), 0, null);
	  
			String SQL = "SELECT TOTALLINES, C_CURRENCY_ID " +
						 "FROM C_INVOICE " +
						 "WHERE C_ORDER_ID = " + order.get_ID() + " " +
						 "AND C_DOCTYPETARGET_ID = " 
						 + Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID");
						 
			PreparedStatement pstmt = null;
		    ResultSet rs = null;
			try
			{	
				pstmt = DB.prepareStatement(SQL, null); 
			       rs = pstmt.executeQuery();
			    
			    while(rs.next())
			    {
			    	total = rs.getBigDecimal("TOTALLINES");
			    	notice.setC_Currency_ID(rs.getInt("C_CURRENCY_ID"));			    	
			    }
			    
			}
			catch (Exception e) {
				System.out.println("ERROR GENERANDO EL AVISO DE DESCUENTO");
			}finally
			{
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
			}
			
			//Ahora obtengo el iva actual
			SQL = "SELECT A.RATE, A.C_TAX_ID, B.C_TAXCATEGORY_ID " +
				  "FROM C_TAX A, C_TAXCATEGORY B " +
				  "WHERE " +
			      "VALIDFROM = (SELECT MAX(C.VALIDFROM) FROM C_TAX C WHERE C.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID) " +
			      "AND A.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID AND B.NAME LIKE 'IVA'";
			try
			{	
				pstmt = DB.prepareStatement(SQL, null); 
				   rs = pstmt.executeQuery();
			   
				while(rs.next())
				{
					notice.setC_TaxCategory_ID(rs.getInt("C_TAXCATEGORY_ID"));
			    	iva = rs.getBigDecimal("RATE"); 
			    	notice.setC_Tax_ID(rs.getInt("C_TAX_ID"));			    	
				}
			}
			catch (Exception e) {
				System.out.println("ERROR GENERANDO EL AVISO DE DESCUENTO");
			}finally
			{
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
			}
			
			iva = iva.divide(hundred);
			
			notice.setC_Order_ID(order.get_ID());
			montoDesc = montoDesc.setScale(2,BigDecimal.ROUND_UP);
			notice.setXX_UnitPurchasePrice(total);
			
			//Calculo del IVA  con el monto
			if(days >=35)
	    	{
	    		//Penalizacion de 5%
	    		montoDesc = total.multiply(new BigDecimal(0.05));
	    		notice.setXX_WeekCreated(new BigDecimal(5));
	    	}
	    	else if(days >=28)
	    	{
	    		//Penalizacion de 4%
	    		montoDesc = total.multiply(new BigDecimal(0.04));
		    	notice.setXX_WeekCreated(new BigDecimal(4));		    		
	    	}
	    	else if(days >=21)
	    	{
	    		//Penalizacion de 3%
	    		montoDesc = total.multiply(new BigDecimal(0.03));
		    	notice.setXX_WeekCreated(new BigDecimal(3));
	    	}
	    	else if(days >=14)
	    	{
	    		//Penalizacion de 2%
	    		montoDesc = total.multiply(new BigDecimal(0.02));
		    	notice.setXX_WeekCreated(new BigDecimal(2));
	    	}
	    	else if(days >= 7)
	    	{
	    		//Penalizacion de 1%
	    		montoDesc = total.multiply(new BigDecimal(0.01));
		    	notice.setXX_WeekCreated(BigDecimal.ONE);
	       	}
	    	
	    	int percentage = reducePenalty(order);
	    	
	    	if(percentage > 0)
	    		montoDesc = montoDesc.multiply(new BigDecimal(percentage).divide(hundred));
	    	
			notice.setXX_Amount(montoDesc);
	    	ivaDesc = montoDesc.multiply(iva);
    		ivaDesc=ivaDesc.setScale(2,BigDecimal.ROUND_UP);
	    	notice.setXX_Amount_IVA(ivaDesc);
	    	notice.setXX_Status("ACT");
	    	notice.setXX_NotificationType(X_Ref_XX_NotificationType.DEMORAENENTREGA.getValue()); 
	    	notice.setDescription("Descuento por Retraso en la Entrega Nacional");
	    	
	    	notice.save();
		}
	}
	
	/**
	 *  Order
	 *	@return valor del beneficio de rebaja del porcentaje de penalizacion
	 */
	private Integer reducePenalty(MOrder order){
				
		//Tengo que buscar en la matriz por su  Tipo de prov y su Rating
		MBPartner cBPartner = new  MBPartner( Env.getCtx(), order.getC_BPartner_ID(), null);
		Utilities ut = new Utilities();
		return ut.benefitVendorDelayDiscount(cBPartner.get_ID());
	}
	
		
	/**
	 * Create Purchases' Book
	 * @return 	true if it was created
	 * 			false if it wasn't created WDIAZ
	 */
 	public synchronized MVCNPurchasesBook createPurchasesBook(MInvoice mInvoice, boolean ret, boolean flag, Trx transaction)
	{
 		String sql_pb = "";
 		PreparedStatement pstmt_pb = null;
 		ResultSet rs_pb = null;
 		MVCNPurchasesBook result = null;
 		//Trx trans = Trx.get("trans"); WDIAZCAMBIO
 		
 		if(mInvoice.getC_DocTypeTarget_ID() == Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEDEBIT_ID") || mInvoice.getC_DocTypeTarget_ID() == Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID"))
 			sql_pb = "SELECT XX_VCN_PurchasesBook_ID FROM XX_VCN_PurchasesBook WHERE XX_Status='CO' and XX_DocumentNo_ID = "+ mInvoice.getC_Invoice_ID();
 		else
 			sql_pb = "SELECT XX_VCN_PurchasesBook_ID FROM XX_VCN_PurchasesBook WHERE XX_Status='CO' and c_invoice_id = "+ mInvoice.getC_Invoice_ID();
 		
 		try{
 			pstmt_pb = DB.prepareStatement(sql_pb, transaction);
			rs_pb = pstmt_pb.executeQuery();
			if(rs_pb.next()){
				MVCNPurchasesBook pBook1 = new MVCNPurchasesBook(ctx, rs_pb.getInt("XX_VCN_PurchasesBook_ID"), transaction);
				result = pBook1;
			}else{
 				MVCNPurchasesBook pBook = new MVCNPurchasesBook(ctx, 0, transaction);
 				int C_Order_ID = mInvoice.getC_Order_ID();
 				
 				if(C_Order_ID != 0)
 					pBook.setC_Order_ID(C_Order_ID);
 				
 				pBook.setXX_NotSubjectBase(new BigDecimal(0));
 				int C_Invoice_ID = mInvoice.getC_Invoice_ID();
 				pBook.setC_Invoice_ID(C_Invoice_ID);
 				if (mInvoice.getRef_Invoice_ID() != 0){
 					/*Modificado por Jorge Pires*/
 					/*MInvoice refInvoice = new MInvoice(ctx, 
					mInvoice.getRef_Invoice_ID(), null);
					pBook.setXX_DocumentNo_ID(refInvoice.getDocumentNo());*/
 					MInvoice refInvoice = new MInvoice(ctx, 
 							mInvoice.getRef_Invoice_ID(), null); //WDIAZCAMBIO
 					pBook.setXX_DocumentNo_ID(C_Invoice_ID);
 					
 					//JTRIAS XX_DocumentNo
 					pBook.setXX_DocumentNo(mInvoice.getDocumentNo());
 					
 					pBook.setC_Invoice_ID(refInvoice.get_ID());			
 				}
 				pBook.setM_Warehouse_ID(mInvoice.getM_Warehouse_ID());
 				int C_BPartner_ID = mInvoice.getC_BPartner_ID();
 				pBook.setC_BPartner_ID(C_BPartner_ID);
 				pBook.setXX_TaxAmount(mInvoice.getXX_TaxAmount());
 				pBook.setXX_TotalInvCost(mInvoice.getGrandTotal());
 				/*pBook.setXX_TaxableBase(mInvoice.getGrandTotal().
				subtract(mInvoice.getXX_TaxAmount()));*/
 				
 				//Organizacion, si es contrato org principal
 				if(mInvoice.getC_Order_ID()==0)
 					pBook.setAD_Org_ID(getPrincipalAccOrg());
		
 				String sql0 = "select C_Tax_ID from C_InvoiceLine where c_invoice_id = " 
 					+ C_Invoice_ID + " group by C_Tax_ID ";
	
 				PreparedStatement pstmt0 = null; //WDIAZCAMBIO
				ResultSet rs0 = null;
 				try {
 					pstmt0 = DB.prepareStatement(sql0, transaction); //WDIAZCAMBIO
 					   rs0 = pstmt0.executeQuery();
 					if (rs0.next()){
 						//TODO: verificar cuando sea Exento o no
 						pBook.setC_Tax_ID(rs0.getInt(1));
 					}else 
 						pBook.setC_Tax_ID(0);
			
 				} catch (SQLException e) {
 					//log.log(Level.SEVERE, sql0, e);
 				}finally{
 					DB.closeStatement(pstmt0);
 					DB.closeResultSet(rs0);
 				}
		
 				BigDecimal sum = null;
 				String sql2 = "select SUM(LineTotalAmt) from c_invoiceline where c_invoice_id = " 
				+ C_Invoice_ID +" and TaxAmt = 0 ";
		
 				PreparedStatement pstmt2 = null;
				ResultSet rs2 = null;
 				try {
 					pstmt2 = DB.prepareStatement(sql2, transaction);
 					   rs2 = pstmt2.executeQuery();
 					if (rs2.next())
 						sum = rs2.getBigDecimal(1);
			
 				} catch (SQLException e) {
 					log.log(Level.SEVERE, sql2, e);
 				}finally{
 					DB.closeStatement(pstmt2);
 					DB.closeResultSet(rs2);
 				}
 				if(sum != null)
 					pBook.setXX_ExemptBase(sum);
 				else
 					pBook.setXX_ExemptBase(new BigDecimal(0));
		
 				//pBook.setXX_NotSubjectBase();
 				//Cambio para restar el monto Exento de la Base Imponible
 				pBook.setXX_TaxableBase(mInvoice.getGrandTotal().subtract(mInvoice.getXX_TaxAmount()).subtract(pBook.getXX_ExemptBase()));
		
 				Date utilDate = new Date(); //actual date
 				long lnMilisegundos = utilDate.getTime();
 				Timestamp fechaActual = new Timestamp(lnMilisegundos);
 				pBook.setXX_DATE(fechaActual);
		
 				pBook.setXX_isManual(false);
 				pBook.setXX_DocumentDate(mInvoice.getDateInvoiced());
 				pBook.setXX_ControlNumber(mInvoice.getXX_ControlNumber());
		
 				BigDecimal percent = null;
 				String sql = "select p.XX_PERCENRETEN from XX_VCN_PERCENRETEN p, C_BPARTNER b " 
				+ " where b.c_BPartner_ID = " + C_BPartner_ID + " and XX_TypeTaxPayer_ID <> " +Env.getCtx().getContext("#XX_L_TYPETAXPAYERFOR_ID")        
				+ " and b.xx_percentajeretention_id = p.XX_VCN_PERCENRETEN_ID";
		
 				PreparedStatement pstmt = null;
				ResultSet rs = null;
 				try {
 					pstmt = DB.prepareStatement(sql, transaction);
 					  rs = pstmt.executeQuery();
 					if (rs.next())
 						percent = rs.getBigDecimal(1);
			
 				} catch (SQLException e) {
			//log.log(Level.SEVERE, sql, e);
 				}finally{
 					DB.closeStatement(pstmt);
 					DB.closeResultSet(rs);
 				}
 					
 				//Verificamos si la Forma de pago, y el total de la factura sea menor a 20 UT no genere Retencion de IVA
 				//Rubi Cornejo 25/02/2014
 				X_AD_Client client = new X_AD_Client( Env.getCtx(), Env.getCtx().getAD_Client_ID(), null);
 				BigDecimal ut = new BigDecimal(client.get_Value("XX_UT").toString());
 				boolean amountUT = true; 
 				BigDecimal qtyUT = BigDecimal.valueOf(20);
 				if(((ut.multiply(qtyUT)).compareTo(mInvoice.getTotalLines())==1) && (mInvoice.getPaymentRule().equalsIgnoreCase("C"))) //Se cambia comparacion a == 1 RC 12032014
 					amountUT = false;
 					
 				if (clientRetentionAgent(mInvoice.getAD_Client_ID()) && (!mInvoice.getPaymentRule().equalsIgnoreCase("C")) && amountUT){ 
 				//if (clientRetentionAgent(mInvoice.getAD_Client_ID()) && !mInvoice.getPaymentRule().equalsIgnoreCase("K")){
 					if ((percent != null) & (ret) 
 						& (mInvoice.getXX_TaxAmount().compareTo(new BigDecimal(0)) != 0)){ 
 						pBook.setXX_WithholdingTax((mInvoice.
 						getXX_TaxAmount().multiply(percent)).divide(new BigDecimal(100)));
			
 						// Generating Application Number
 						MVCNApplicationNumber applNum = new MVCNApplicationNumber(ctx, 0, null);
 						int apNum = applNum.generateApplicationNumber(fechaActual, C_Order_ID, flag, transaction);
 						pBook.setXX_Withholding(apNum);			
			
	 				}else
	 					pBook.setXX_WithholdingTax(new BigDecimal(0));
 				/****Jessica Mendoza****/
 				}else{
 					pBook.setXX_WithholdingTax(new BigDecimal(0));
 				}
 				/****Fin código - Jessica Mendoza****/
 				
 				if(pBook.save())
 					result = pBook;
 				else
 					result = null;					
			}
 		}catch(SQLException e){
 			log.log(Level.SEVERE, sql_pb, e);
 		}finally{
 			DB.closeResultSet(rs_pb);
			DB.closeStatement(pstmt_pb);
 		}
 		
		return result;
	}
 	
 	/*
 	 * Devuelve la Organizacion marcada como principal de contabilidad
 	 */
 	private int getPrincipalAccOrg(){
 		
 		int orgID = 0;
 		
 		String sql = "SELECT AD_ORG_ID FROM AD_ORG WHERE XX_ACCPRINCIPAL = 'Y'";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			if (rs.next())
				orgID = rs.getInt("AD_ORG_ID");
			}
		catch (SQLException e) {
	
		}finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
 				
 		return orgID;
 	}
		
 	/**
	 * Method that generate the Debt or Credit Notify Report
	 */
	public void showReport(MVCNPurchasesBook pBook){
		if(pBook.getXX_Withholding() != 0){	
			Calendar date = Calendar.getInstance();
			
			date.setTimeInMillis(pBook.getXX_DATE().getTime());
			
			Integer numMonth = date.get(Calendar.MONTH)+1;
			Integer numYear = date.get(Calendar.YEAR);
			String month = numMonth.toString();
			if(numMonth < 10)
				month = "0".concat(month);
			String year = numYear.toString();
			String yearMonth = (year.concat(month));
			
			String XX_NOCOMPROBANTE = yearMonth + pBook.getXX_Withholding();
					
			log.info("XX_NOCOMPROBANTE=" + XX_NOCOMPROBANTE);
			if (XX_NOCOMPROBANTE == "")
				throw new IllegalArgumentException("@NotFound@ @XX_NOCOMPROBANTE@");
			
			// Obtain the Active Record of M_Invoice Table
			Query q = new Query("XX_VCN_PURCHASESBOOK_ID");
			q.addRestriction("XX_VCN_PURCHASESBOOK_ID", Query.EQUAL, pBook.get_ID());
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Comprobante de Retención", 1000339, pBook.get_ID(), 0);
			MPrintFormat f = MPrintFormat.get (Env.getCtx(), 1000311, false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
		}
	}
	
	
 	/**
	 * este metodo genera el reporte de Traspaso
	 */
	public void showReportTransfer(MMovement transfer){	

			// Obtain the Active Record of X_XX_VMR_Order Table
			Query q = new Query("M_Movement_ID");
			q.addRestriction("M_Movement_ID", Query.EQUAL, ""+transfer.get_ID());
			int table_ID = X_XX_VMR_Order.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Product Transfer", table_ID, transfer.get_ID(), 0);
			MPrintFormat f = MPrintFormat.get (Env.getCtx(), 1000487, false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
	}
	
 	/**
	 * este metodo genera el reporte de Traspaso
	 */
	public void showReportMovement(MMovement transfer){	

			// Obtain the Active Record of X_XX_VMR_Order Table
			Query q = new Query("M_Movement_ID");
			q.addRestriction("M_Movement_ID", Query.EQUAL, ""+transfer.get_ID());
			int table_ID = X_XX_VMR_Order.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Inventory Movement", table_ID, transfer.get_ID(), 0);
			MPrintFormat f = MPrintFormat.get (Env.getCtx(), 1011943, false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
	}

	
 	/**
	 * este metodo genera el reporte de Devoluciones
	 */
	public void showReportReturn(MMovement returns){	

			// Obtain the Active Record of X_XX_VMR_Order Table
			Query q = new Query("M_Movement_ID");
			q.addRestriction("M_Movement_ID", Query.EQUAL, ""+returns.get_ID());
			int table_ID = X_XX_VMR_Order.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Product Return", table_ID, returns.get_ID(), 0);
			MPrintFormat f = MPrintFormat.get (Env.getCtx(), 1000478, false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
	}

	
 	/**
	 * este metodo genera el reporte de distribucion fisica
	 */
	public void showReportPlacedOrder(X_XX_VMR_Order placedOrder){	

			// Obtain the Active Record of X_XX_VMR_Order Table
			Query q = new Query("XX_VMR_Order_ID");
			q.addRestriction("XX_VMR_Order_ID", Query.EQUAL, ""+placedOrder.get_ID());
			int table_ID = X_XX_VMR_Order.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Physical Distribution", table_ID, placedOrder.get_ID(), 0);
			MPrintFormat f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("XX_R_PHYSICALDISTRIREPORT_ID"), false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
	}
	
	
 	/**
	 * este metodo genera el reporte de Guia de Despacho de Movimiento entre Cds
	 */
	public void showReportDispatchGuideMov(X_XX_VLO_DispatchGuide dispatchGuide){	

			// Obtain the Active Record of X_XX_VLO_DispatchGuide Table
			Query q = new Query("XX_VLO_DispatchGuide");
			q.addRestriction("XX_VLO_DispatchGuide_ID", Query.EQUAL, ""+dispatchGuide.get_ID());
			int table_ID = X_XX_VLO_DispatchGuide.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Guía de Despacho", table_ID, dispatchGuide.get_ID(), 0);
			MPrintFormat f = MPrintFormat.get (Env.getCtx(), 1011945, false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
	}
	
	
 	/**
	 * este metodo genera el reporte de Guia de Despacho
	 */
	public void showReportDispatchGuide(X_XX_VLO_DispatchGuide dispatchGuide){	

			// Obtain the Active Record of X_XX_VLO_DispatchGuide Table
			Query q = new Query("XX_VLO_DispatchGuide");
			q.addRestriction("XX_VLO_DispatchGuide_ID", Query.EQUAL, ""+dispatchGuide.get_ID());
			int table_ID = X_XX_VLO_DispatchGuide.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Guía de Despacho", table_ID, dispatchGuide.get_ID(), 0);
			MPrintFormat f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("XX_R_DISPATCHGUIDEREPORT_ID"), false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
	}
	
 	/**
	 * este metodo genera el reporte de autorización de salida
	 */
	public void showReportExitAuthorization(X_XX_VLO_DispatchGuide dispatchGuide){	

			// Obtain the Active Record of X_XX_VLO_DispatchGuide Table
			Query q = new Query("XX_VLO_DispatchGuide");
			q.addRestriction("XX_VLO_DispatchGuide_ID", Query.EQUAL, ""+dispatchGuide.get_ID());
			int table_ID = X_XX_VLO_DispatchGuide.Table_ID;
			
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Autorización de Salida", table_ID, dispatchGuide.get_ID(), 0);
			MPrintFormat f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("XX_R_EXITAUTHOREPORT_ID"), false);
			
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
	}
	
	
	/**
	 * Create Debt/Credit Notify
	 * @param from invoice
	 * @return
	 */
	public boolean createDebtCreditNotify(MInvoice from, Trx transaction){
		boolean flag = false;
		
		MInvoiceLine mInvoiceLine = null;
		MInOutLine mInOutLine = null;
		MOrderLine orderLine = null;
		MVMRPOLineRefProv refOrder = null;
		Vector<MInvoiceLine> vectorIL = new Vector<MInvoiceLine>();
		Vector<MInOutLine> vectorOL = new Vector<MInOutLine>();
		Vector<MOrderLine> vectorOrderLine = new Vector<MOrderLine>();
		Vector<MVMRPOLineRefProv> vectorPL = new Vector<MVMRPOLineRefProv>();
		BigDecimal unitConv = null, unitSale = null, N = null;;
		//String sql0 = "";
		Vector<BigDecimal> cantidadesAlgoritmo = new Vector<BigDecimal>();
		
		String sql0 = "Select i.C_INVOICELINE_ID, io.M_INOUTLINE_ID, i.C_ORDERLINE_ID, o.XX_VMR_PO_LINEREFPROV_ID " +
				"from C_INVOICELINE i " +
				"left outer join M_INOUTLINE io on io.C_ORDERLINE_ID = i.C_ORDERLINE_ID " +
				"left outer join C_ORDERLINE o on i.C_ORDERLINE_ID = o.C_ORDERLINE_ID " +
				"where i.C_INVOICE_ID = " + from.getC_Invoice_ID();
		
		PreparedStatement pstmt = null;
			ResultSet rs = null;
		try {
			 pstmt = DB.prepareStatement(sql0, null);
			    rs = pstmt.executeQuery();
			while (rs.next()){
				// Vector con líneas de la factura
				vectorIL.add(new MInvoiceLine(ctx, rs.getInt(1), null));
				// Vector con líneas del chequeo
				vectorOL.add(new MInOutLine(ctx, rs.getInt(2), null));
				//Vector con las líneas de la OC
				vectorOrderLine.add(new MOrderLine(ctx, rs.getInt(3), null));
				// Vector con Ref líneas de la O/C
				vectorPL.add(new MVMRPOLineRefProv(ctx, rs.getInt(4), null));
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql0, e);
		}finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}
		
		for (int i = 0; i < vectorIL.size(); i++) {
			unitConv = new BigDecimal(1);
			unitSale = new BigDecimal(1);
			N = new BigDecimal(1);
			mInvoiceLine = vectorIL.get(i);
			mInOutLine = vectorOL.get(i);
			orderLine = vectorOrderLine.get(i);
			refOrder = vectorPL.get(i);
				if (refOrder!= null && refOrder.getXX_VMR_VendorProdRef_ID() > 0){
				    unitConv = new BigDecimal(new X_XX_VMR_UnitConversion(ctx, refOrder.getXX_VMR_UnitConversion_ID(), null).getXX_UnitConversion());
				    unitSale = new BigDecimal(new X_XX_VMR_UnitConversion(ctx, refOrder.getXX_PiecesBySale_ID(), null).getXX_UnitConversion());
				    N = unitConv.divide(unitSale);
				}
			BigDecimal 	CantidadNota = new BigDecimal(0), 
						price = (mInvoiceLine.getC_OrderLine_ID() > 0) ? (mInvoiceLine.getPriceEntered().divide(N, 4, BigDecimal.ROUND_HALF_UP)) : mInvoiceLine.getPriceActual(),  // Precio de o/c por Linea y si es producto no solicitado es el precio de la Factura
						cantidadFactura = ((mInvoiceLine.getC_OrderLine_ID() > 0) ? (mInvoiceLine.getQtyInvoiced().multiply(unitConv)).divide(unitSale) : mInvoiceLine.getQtyInvoiced()),    //Cantidad de Factura por Linea
						cantidadLineaOrden = (orderLine != null)? ((orderLine.getQtyOrdered().multiply(unitConv)).divide(unitSale)): Env.ZERO,
						cantidadRecepcion = (mInOutLine != null)? mInOutLine.getPickedQty() : Env.ZERO,
						cantidadDevolucion =(mInOutLine != null)? mInOutLine.getScrappedQty() : Env.ZERO,
								
						//diffQty = mInOutLine.getMovementQty().subtract(qty), //Cantidad Recepcion - Factura
						//diffPrice = orderLine.getPriceActual().subtract(mInvoiceLine.getPriceActual());
						diffPrice = (orderLine.getPriceActual().divide(N, 4, BigDecimal.ROUND_HALF_UP)).subtract(mInvoiceLine.getPriceActual().divide(N, 4, BigDecimal.ROUND_HALF_UP));
			
			cantidadesAlgoritmo = PartsReceivedAlgorithm(cantidadLineaOrden, cantidadRecepcion, cantidadFactura, cantidadDevolucion);
	
			CantidadNota = cantidadesAlgoritmo.elementAt(6); // El Algoritmo devuelve una Cantidad(Posicion 6) si hay diferencias en piezas. Sea Positiva(Debito) o negativa(Credito)
			
			//if(diffQty.compareTo(zero) != 0){
			
			if(diffPrice.compareTo(Env.ZERO) != 0 && mInvoiceLine.getC_OrderLine_ID() > 0 ){
				// Creating Debt Notify
				if(DebtCreditPrice == null)
					DebtCreditPrice = new MInvoice(ctx, copyFrom(from, DebtCreditPrice, fechaActual, true, true, transaction), transaction);
				copyLinesFrom(DebtCreditPrice, mInvoiceLine, cantidadFactura, diffPrice, false, transaction);
				DebtCreditPrice.load(DebtCreditPrice.get_ID(), transaction);
				flag = true;
			}
			
			if(CantidadNota.compareTo(Env.ZERO) != 0){
				// Creating Debt Notify 
				if(DebtCreditQuantity == null)
					DebtCreditQuantity = new MInvoice(ctx, copyFrom(from, DebtCreditQuantity, fechaActual, true, false, transaction), transaction);	
				//copyLinesFrom(DebtCreditQuantity, mInvoiceLine, diffQty, price);
				copyLinesFrom(DebtCreditQuantity, mInvoiceLine, CantidadNota, price, true, transaction);
				DebtCreditQuantity.load(DebtCreditQuantity.get_ID(), transaction);
				flag = true;
				
			}
		
		}	
		if(DebtCreditQuantity != null){
			if(DebtCreditQuantity.getTotalLines().compareTo(Env.ZERO) > 0){
				DebtCreditQuantity.setC_DocTypeTarget_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEDEBIT_ID"));
				DebtCreditQuantity.save();
			}else 
				if(DebtCreditQuantity.getTotalLines().compareTo(Env.ZERO) < 0){
					DebtCreditQuantity.setC_DocTypeTarget_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID"));
					DebtCreditQuantity.save();
				  }else{
					eliminarFactura(DebtCreditQuantity.get_ID(), transaction);
					DebtCreditQuantity = null;
				  }
		if(DebtCreditQuantity != null){
			/****Setea la fecha de vencimiento, fecha de pago y tipo de factura, 
			 * segun los datos de la factura original correspondiente a la misma orden de compra ****/
			DebtCreditQuantity.setXX_DueDate(searchfechaVenc(DebtCreditQuantity.get_ID(), Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")));
			DebtCreditQuantity.setXX_DatePaid(searchfechaVenc(DebtCreditQuantity.get_ID(),Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")));
			DebtCreditQuantity.setXX_InvoiceType(searchPOType(DebtCreditQuantity.get_ID(), transaction));
			DebtCreditQuantity.setXX_AccountPayableStatus("A");
			/****Se cambia el status de documento por completado, para 
			 * que se vean reflejados en la planificacion de la semana de pago****/
			DebtCreditQuantity.setDocAction(MInvoice.DOCACTION_Complete);
			DocumentEngine.processIt(DebtCreditQuantity, MInvoice.DOCACTION_Complete); 
			DebtCreditQuantity.save();
		}
		}
		
		if(DebtCreditPrice != null){
			if(DebtCreditPrice.getTotalLines().compareTo(Env.ZERO) > 0){
				DebtCreditPrice.setC_DocTypeTarget_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPEDEBIT_ID"));
				DebtCreditPrice.save();
			}else if(DebtCreditPrice.getTotalLines().compareTo(Env.ZERO) < 0){
					DebtCreditPrice.setC_DocTypeTarget_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID"));
					DebtCreditPrice.save();
				}else{
					eliminarFactura(DebtCreditPrice.get_ID(), transaction);
					DebtCreditPrice = null;
				}
			
			if(DebtCreditPrice != null){
				/****Setea la fecha de vencimiento, fecha de pago y tipo de factura, 
				 * segun los datos de la factura original correspondiente a la misma orden de compra ****/
				DebtCreditPrice.setXX_DueDate(searchfechaVenc(DebtCreditPrice.get_ID(), Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")));
				DebtCreditPrice.setXX_DatePaid(searchfechaVenc(DebtCreditPrice.get_ID(), Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")));
				DebtCreditPrice.setXX_InvoiceType(searchPOType(DebtCreditPrice.get_ID(), transaction));
				DebtCreditPrice.setXX_AccountPayableStatus("A");
				/****Se cambia el status de documento por completado, para 
				 * que se vean reflejados en la planificacion de la semana de pago****/
				DebtCreditPrice.setDocAction(MInvoice.DOCACTION_Complete);
				DocumentEngine.processIt(DebtCreditPrice, MInvoice.DOCACTION_Complete);
				DebtCreditPrice.save();
			}
		}		
			
		return flag;
	}
	
	/**
 	 * Se encarga de buscar si la compañia es agente de retencion
 	 * @author Jessica Mendoza
 	 * @param idClient
 	 * @return
 	 */
 	public Boolean clientRetentionAgent(int idClient){
 		String sql = "select XX_RetentionAgent " +
 					 "from AD_Client " +
 					 "where AD_Client_ID = " + idClient;
 		boolean bool = false;
 		PreparedStatement pstmt = null; 
		ResultSet rs = null;
 		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getString(1).equalsIgnoreCase("Y"))
					bool = true;
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bool;
 	}
 	
	/**
	 * Busca la fecha de vencimiento asociada a la nota
	 * creada, para guardarla en la fecha de vencimiento y 
	 * pago de dicha nota
	 * @author Jessica Mendoza
	 * @param idFactura
	 * @param idDocType
	 * @return la fecha de vencimiento
	 */
	public Timestamp searchfechaVenc(int idFactura, int idDocType){
		String sql = "select XX_DueDate " +
						   "from C_Invoice " +
						   "where C_Order_ID = " +
						   		"(select C_Order_ID " +
						   		"from C_Invoice " +
						   		"where C_Invoice_ID = " + idFactura + ")" +
						   "and C_DocTypeTarget_ID = "+ idDocType;
		Timestamp fecha = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				fecha = rs.getTimestamp("XX_DueDate");
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fecha;
	}
	
	/**
	 * Busca toda la informacion de una condicion de pago específica
	 * @author Jessica Mendoza
	 * @param idCondicion identificador de la condicion
	 * @return
	 */
	public Vector<Integer> infoCondicionPago(int idCondicion){
		Vector<Integer> condicion = new Vector<Integer>(6);
		String sql = "select Percentage, NetDays, XX_DateFirstPay_ID, " +
					 "XX_PercentageRemainingTwo, XX_DaysFundingTwo, XX_DateSecondePay_ID," +
					 "XX_PercentageRemainingThree, XX_DaysFundingThree, XX_DateThirdPay_ID " +
				   	 "from C_PaySchedule " +
				   	 "where C_PaymentTerm_ID = " + idCondicion;

		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				condicion.add(rs.getInt(1));
				condicion.add(rs.getInt(2));
				condicion.add(rs.getInt(3));
				condicion.add(rs.getInt(4));
				condicion.add(rs.getInt(5));
				condicion.add(rs.getInt(6));
				condicion.add(rs.getInt(7));
				condicion.add(rs.getInt(8));
				condicion.add(rs.getInt(9));
			}
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return condicion;
	}
	
	/**
	 * Calcula la retencion de un documento específico
	 * @author Jessica Mendoza
	 * @param idInvoice id del documento
	 * @return retencion
	 */
	public float retencion (int idInvoice){
		String sql = "select round(WithholdingTaxInvoice("+ idInvoice +"),2) from dual";
		float retencion = 0;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next())
				retencion = rs.getFloat(1);
		}catch (Exception e){
			log.log(Level.SEVERE, sql);
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retencion;
	}
	
	/**
	 * Busca el tipo de orden de compra de la factura, para guardarselo 
	 * a la nota 
	 * @author Jessica Mendoza
	 * @param int idFactura id de la factura
	 * @return
	 */
	public String searchPOType(int idFactura, Trx transaction){
		String sql = "select XX_POType, XX_PurchaseType " +
					 "from C_Order " +
					 "where C_Order_ID = " +
		   			 	"(select C_Order_ID " +
		   				"from C_Invoice " +
		   				"where C_Invoice_ID = " + idFactura + ")";
		String tipoOrder = null;
		String tipoCompra = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, transaction); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				tipoOrder = rs.getString("XX_POType");
				tipoCompra = rs.getString("XX_PurchaseType");
			}
			
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (tipoOrder.equals("POM")){
			tipoOrder = "I";
		}else{ 
			if (tipoOrder.equals("POA")){ 
				if (tipoCompra.equals("SE")){
					tipoOrder = "S";
				}else 
					tipoOrder = "A";		
			}
		}
		return tipoOrder;
	}

	/**
	 * Calcula la fecha por medio de la condición de pago establecida.
	 * Dicha fecha puede ser utilizada tanto para la fecha de vencimiento 
	 * en la factura, como para la fecha estimada para la estimación de 
	 * las cuentas por pagar
	 * @author Jessica Mendoza
	 * @param idPaymentTerm id de la condición de pago
	 * @return fecha
	 */
	public Vector<Timestamp> calcularFecha(int idPaymentTerm, int idOrder, String typeProcedure){	
		Vector<Timestamp> fecha = new Vector<Timestamp>(3);
		String sql = "select XX_PercentageRemainingTwo, XX_PercentageRemainingThree, " +
					 "XX_DateFirstPay_ID, XX_DateSecondePay_ID, XX_DateThirdPay_ID " +
					 "from C_PaySchedule " +
					 "where C_PaymentTerm_ID = " + idPaymentTerm;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next()){
				if (rs.getBigDecimal("XX_PercentageRemainingThree").compareTo(new BigDecimal(0)) != 0){
					fecha.add(fechaCondicion(rs.getInt("XX_DateFirstPay_ID"), "", idOrder, typeProcedure));
					fecha.add(fechaCondicion(rs.getInt("XX_DateSecondePay_ID"), "", idOrder, typeProcedure));
					fecha.add(fechaCondicion(rs.getInt("XX_DateThirdPay_ID"), "", idOrder, typeProcedure));
				}else if (rs.getBigDecimal("XX_PercentageRemainingTwo").compareTo(new BigDecimal(0)) != 0){
					fecha.add(fechaCondicion(rs.getInt("XX_DateFirstPay_ID"), "", idOrder, typeProcedure));
					fecha.add(fechaCondicion(rs.getInt("XX_DateSecondePay_ID"), "", idOrder, typeProcedure));
					fecha.add(null);
				}else if (rs.getInt("XX_DateFirstPay_ID") > 0){
					fecha.add(fechaCondicion(rs.getInt("XX_DateFirstPay_ID"), "", idOrder, typeProcedure));
					fecha.add(null);
					fecha.add(null);
				}else{
					fecha.add(null);
					fecha.add(null);
					fecha.add(null);
				}
			}else{
				//System.out.println("La condicion de pago no tiene una descripción establecida"); 
				ADialog.info(0, new Container(), Msg.getMsg(Env.getCtx(), "XX_NotSchedule"));
			}
			rs.close();
			pstmt.close();
		}catch (Exception e) {
			log.log(Level.SEVERE, sql);
		}
		return fecha;
	}
	
	/**
	 * Busca el nombre del ad column 
	 * @author Jessica Mendoza
	 * @param adColumn identificador del ad column
	 * @return nombre de la fecha correspondiente
	 */
	public String nameFechaCondition(int adColumn){
		String sql = "select columnName " +
					 "from AD_Column " +
					 "where AD_Column_ID = " + adColumn;
		
		String nameDate = "";
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next())
				nameDate = rs.getString("columnName");
		}catch (Exception e){
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return nameDate;
	}

	/**
	 * Busca el valor de la fecha de la OC, de una condicion específica
	 * @author Jessica Mendoza
	 * @param adColumn id de la columna asociada a la fecha de la condición
	 * @param nameFecha nombre de la columna
	 * @param idOrder identificador de la O/C
	 * @param typeProcedure tipo de procedimiento (estimada,factura)
	 * @return
	 */
	public Timestamp fechaCondicion(int adColumn, String nameFecha, int idOrder, String typeProcedure){
		int i = 0;
		String sql = "";
		
		if (nameFecha.equals(""))
			nameFecha = nameFechaCondition(adColumn);
		
		if (typeProcedure.equals("estimada")){
			if (nameFecha.equals("XX_EntranceDate")){
				nameFecha = "XX_EstimatedDate";
			}
		}else{ //factura 
			if (nameFecha.equals("XX_SHIPPREALESTEEMEDDATE")){
				nameFecha = "XX_SHIPPINGREALDATE";
				i = 1;
			}else if (nameFecha.equals("XX_VZLAARRIVALESTEMEDDATE")){
				nameFecha = "XX_VZLAARRIVALREALDATE";
				i = 1;
			}
		}

		if (i == 0){
			sql = "select " + nameFecha + " " +
			 	  "from C_Order " +
			 	  "where C_Order_ID = " + idOrder;
		}else{
			sql = "select bg." + nameFecha + " " +
				  "from XX_VLO_BoardingGuide bg, C_Order ord " +
				  "where ord.C_Order_ID = " + idOrder + " " +
				  "and ord.XX_VLO_BoardingGuide_ID = bg.XX_VLO_BoardingGuide_ID ";
		}

		Timestamp fecha = null;
		PreparedStatement pstmt = null; 
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null); 
			rs = pstmt.executeQuery();
			if(rs.next())
				fecha = rs.getTimestamp(1);
			else{
				MOrder order = new MOrder(Env.getCtx(),idOrder,null);
				fecha = order.getXX_EntranceDate();
			}
		}catch (Exception e){
			log.log(Level.SEVERE, sql);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fecha;
	}

	/**
	 * Crea el registro en la tabla de estimacion de cuentas por pagar
	 * @author Jessica Mendoza
	 * @param vecI vector que contine los ID que se almacenan en la tabla
	 * @param vecS vector que contine los String que se almacenan en la tabla
	 * @param grandTotal total de la orden de compra
	 * @param fechaEstimada fecha estimada
	 * @param tipo tipo de creacion de la estimacion
	 * @param description descripcion de la CxPE
	 */
	public void crearEstimacion(Vector<Integer> vecI, Vector<String> vecS, BigDecimal grandTotal, 
			Timestamp fechaEstimada, String tipo, String description, int XX_PayContract_ID){
		BigDecimal cero = new BigDecimal(0);
		GregorianCalendar fecha = new GregorianCalendar();
		fecha.setTime(fechaEstimada);

		X_XX_VCN_EstimatedAPayable estimacion = new X_XX_VCN_EstimatedAPayable(Env.getCtx(), 0, null);
		estimacion.setAD_Client_ID(Env.getCtx().getAD_Client_ID());
		estimacion.setAD_Org_ID(Env.getCtx().getAD_Org_ID());
		estimacion.setC_BPartner_ID(vecI.get(1));
		estimacion.setC_PaymentTerm_ID(vecI.get(2));
		estimacion.setC_Currency_ID(vecI.get(3));
		estimacion.setXX_VendorType_ID(vecI.get(4));
		estimacion.setXX_DateEstimated(fechaEstimada);
		estimacion.setDescription(description);
		
		if(XX_PayContract_ID>0)
			estimacion.set_CustomColumn("XX_PayContract_ID", XX_PayContract_ID);
		
		if (tipo.equals("Orden")){		
			MOrder orden = new MOrder(Env.getCtx(),vecI.get(0),null);
			if ((orden.getXX_ImportingCompany_ID() == 0) && 
					(orden.getXX_POType().equals("POM"))
					){
				estimacion.setAD_Client_ID(orden.getAD_Client_ID());
			}
			estimacion.setC_Order_ID(vecI.get(0));
			estimacion.setXX_VMR_Department_ID(vecI.get(5));
			estimacion.setXX_POType(vecS.get(0));
			estimacion.setC_Country_ID(vecI.get(6));
			estimacion.setXX_VMR_Category_ID(vecI.get(7));
			if (vecI.get(8) == 0)
				estimacion.setXX_ImportingCompany_ID(orden.getAD_Client_ID());
			else
				estimacion.setXX_ImportingCompany_ID(vecI.get(8));
			estimacion.setXX_OrderType(vecS.get(1));		
		}else{
			estimacion.setXX_Contract_ID(vecI.get(0));
			estimacion.setXX_ImportingCompany_ID(vecI.get(5));
		}
		
		if (vecI.get(3) == 205){
			estimacion.setXX_AmountLocal(grandTotal);
			estimacion.setXX_AmountForeign(cero);
		}else{
			estimacion.setXX_AmountForeign(grandTotal);
			estimacion.setXX_AmountLocal(cero);
		}
		
		estimacion.setXX_WeekEstimated(fecha.get(GregorianCalendar.WEEK_OF_YEAR)+"/"+fecha.get(Calendar.YEAR));
		estimacion.save();	
	}
	
	/**
	 * Calcula el total del monto y de piezas de las devoluciones por Socio de Negocio
	 * @author Jessica Mendoza
	 * @param idPartner
	 * @return
	 */
	public Vector<Integer> totalDevolucion(int idPartner){
		Vector<Integer> vector = new Vector<Integer>(2);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlMov = "select sum(a.PriceActual * a.QtyRequired) total, b.C_BPartner_ID, " +
						"coalesce( sum(case when (XX_Status != 'CR' and XX_ApprovedQty < QtyRequired ) " +
						"then XX_ApprovedQty " +
						"else QtyRequired " +
						"end),0) piezas " +
						"from M_MovementLine a, M_Movement b " + 
						"where a.XX_QuantityReceived is not null " +
						"and a.M_Movement_ID = b.M_Movement_ID " +
						"and b.XX_Status = 'PE' " +
						"and b.C_BPartner_ID = " + idPartner + " " +
						"group by b.C_BPartner_ID ";

		try {
			pstmt = DB.prepareStatement(sqlMov, null);
			rs = pstmt.executeQuery();
			if (rs.next()){
				vector.add(rs.getInt(1));
				vector.add(rs.getInt(3));
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlMov, e);
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return vector;	
	}
	
	/**
	 * Busca si el producto tiene una cuenta contable asociada
	 * @author Jessica Mendoza
	 * @param idProducto
	 */
	public boolean cuentaContable(int idProducto){
		String sql = "select XX_PExpenses_ID, XX_PInventory_ID " +
		  			 "from M_Product_Acct " +
		  			 "where M_Product_ID = " + idProducto;
		boolean bool = false;
		PreparedStatement pstmt = null; 
	    ResultSet rs = null;
	    try{
	    	pstmt = DB.prepareStatement(sql, null); 
		    rs = pstmt.executeQuery();
		    if(rs.next()){
		    	if ((rs.getInt("XX_PExpenses_ID") != 0) || (rs.getInt("XX_PInventory_ID") != 0))
		    		bool = true; //el producto tiene una cuenta contable asociada
		    	else
		    		bool = false;
		    }
	    }catch(Exception e){
	    	log.saveError("ErrorSql", Msg.getMsg(Env.getCtx(), e.getMessage()));
	    }finally{
	    	DB.closeResultSet(rs);
	    	DB.closeStatement(pstmt);
		}
		return bool;
	}
	
	/**
	 * Calcula el último dia del mes correspondiente.
	 * Recordar que si se está trabajando con Calendar al mes se le deb sumar uno 
	 * para poder ejecutar dicho este proceso
	 * @author Jessica Mendoza
	 * @param mes
	 * @return
	 */
	public int ultimoDiaMes(int mes){
		Calendar fecha = Calendar.getInstance();
		fecha.set(Calendar.DAY_OF_MONTH, 01);
		String sql = "select last_day('"+ fecha.get(Calendar.DATE) + "/" + mes + "/" +
					  fecha.get(Calendar.YEAR) +"') from dual";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {	
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();			
			if(rs.next()) {
				fecha.setTime(rs.getDate(1));
			}
		}catch(SQLException e){	
			e.getMessage();
		} 
		finally{
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fecha.get(Calendar.DATE);
	}
	
	/**
	 * Nombre del mes
	 * @author Jessica Mendoza
	 * @param month
	 * @return
	 */
	public String meses(int month){
		String mes = null;
		switch(month) { 
			case 1:
				mes = "Enero";
				break;
			case 2:
				mes = "Febrero";
				break;
			case 3:
				mes = "Marzo";
				break;
			case 4:
				mes = "Abril";
				break;
			case 5:
				mes = "Mayo";
				break;
			case 6:
				mes = "Junio";
				break;
			case 7:
				mes = "Julio";
				break;
			case 8:
				mes = "Agosto";
				break;
			case 9:
				mes = "Septiembre";
				break;
			case 10:
				mes = "Octubre";
				break;
			case 11:
				mes = "Noviembre";
				break;
			case 12:
				mes = "Diciembre";
				break;
		}
		return mes;
	}

	/**
	 * Se encarga de setear los parámetros y valores de un proceso específico
	 * @author Jessica Mendoza
	 * @param parameterName Nombre del parámetro
	 * @param parameter primer valor del parámetro
	 * @param parameterTo null
	 * @param info segundo valor del parámetro
	 * @param infoTo null
	 * @return
	 */
	public ProcessInfoParameter[] setParameter(String parameterName, Object parameter, Object parameterTo, String info, String infoTo){
		ArrayList<ProcessInfoParameter> list = new ArrayList<ProcessInfoParameter>();
		list.add (new ProcessInfoParameter(parameterName, parameter, null, info, null));
        ProcessInfoParameter[] pars = new ProcessInfoParameter[list.size()];
        list.toArray(pars);
        return pars;	
	}
	
	/**
	 * Devuelve las fechas de las semanas entre lunes y domingo de un mes específico
	 * @author Jessica Mendoza
	 * @param month mes
	 * @param year año
	 * @return
	 */
	public List datesWeekMonth(int month, int year){
		List datesList = new ArrayList();
		ArrayList<String> dates;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calFirst = Calendar.getInstance();
		Calendar calLast = Calendar.getInstance();
		
		Calendar date1 = Calendar.getInstance();
		date1.set(Calendar.MONTH, month);
		date1.set(Calendar.YEAR, year);
		String dateOne;
		Calendar date2 = Calendar.getInstance();
		date2.set(Calendar.MONTH, month);
		date2.set(Calendar.YEAR, year);
		String dateTwo;
		
		int temporalDay = 0;
		int dayWeek;

		calFirst.set(Calendar.DATE, 01);
		calFirst.set(Calendar.MONTH, month);
		calFirst.set(Calendar.YEAR, year);
		
		calLast.set(Calendar.DAY_OF_MONTH, ultimoDiaMes(month+1));
		calLast.set(Calendar.MONTH, month);		
		calLast.set(Calendar.YEAR, year);
		
		dayWeek = calFirst.get(Calendar.DAY_OF_WEEK);
		
		while (calFirst.compareTo(calLast) <= 0){ 
			if ((calLast.get(Calendar.DAY_OF_MONTH) - calFirst.get(Calendar.DAY_OF_MONTH)) <= 6){
				temporalDay = calLast.get(Calendar.DAY_OF_MONTH) - calFirst.get(Calendar.DAY_OF_MONTH);
			}else{
				switch(dayWeek){
				case 1: //Domingo
					temporalDay = 0;
					break;
				case 2: //Lunes
					temporalDay = 6;
					break;
				case 3: //Martes
					temporalDay = 5;
					break;
				case 4: //Miércoles
					temporalDay = 4;
					break;
				case 5: //Jueves
					temporalDay = 3;
					break;
				case 6: //Viernes
					temporalDay = 2;
					break;
				case 7: //Sábado
					temporalDay = 1;
					break;
				}
			}
			date1.set(Calendar.DAY_OF_MONTH, calFirst.get(Calendar.DAY_OF_MONTH));		
			date2.set(Calendar.DAY_OF_MONTH, calFirst.get(Calendar.DAY_OF_MONTH) + temporalDay);
			
			if (date2.compareTo(calLast) <= 0){
				dates = new ArrayList<String>();
				dateOne = sdf.format(date1.getTime());
				dates.add(dateOne);
				dateTwo = sdf.format(date2.getTime());
				dates.add(dateTwo);
				datesList.add(dates);
			}
			calFirst = date2;
			calFirst.set(Calendar.DAY_OF_MONTH, calFirst.get(Calendar.DAY_OF_MONTH) + 1);
			dayWeek = calFirst.get(Calendar.DAY_OF_WEEK);
		}
		
		return datesList;
	}	

	/**
	 * 	Create new Invoice by copying
	 * 	@param from invoice
	 * 	@param dateDoc date of the document date
	 * 	@param C_DocTypeTarget_ID target doc type
	 *	@return Invoice
	 */
	public int copyFrom (MInvoice from, MInvoice notify, Timestamp dateDoc,
		boolean reference, boolean IsDiferentPrice, Trx transaction)
	{
		notify = new MInvoice (Env.getCtx(), 0, transaction);
		notify.set_ValueNoCheck ("C_Invoice_ID", Integer.valueOf(0));
		notify.set_ValueNoCheck ("DocumentNo", null);
		//
		notify.setDocStatus (MInvoice.DOCSTATUS_Drafted);		//	Draft
		notify.setDocAction(MInvoice.DOCACTION_Complete);
		notify.setC_DocType_ID(0);
		//
		notify.setIsApproved (false);
		notify.setIsPaid (false);
		notify.setIsInDispute(false);
		//
		notify.setIsTransferred (false);
		notify.setPosted (false);
		notify.setProcessed (false);		
		//
		notify.setDateInvoiced (dateDoc);
		notify.setDateAcct (dateDoc);
		notify.setDatePrinted(null);
		notify.setIsPrinted (false);
		//
		notify.setSalesRep_ID(from.getSalesRep_ID());
		notify.setC_BPartner_ID(from.getC_BPartner_ID());
		notify.setC_Order_ID(from.getC_Order_ID());
		notify.setRef_Invoice_ID(from.get_ID());
		notify.setXX_ControlNumber(" ");
		//
		notify.setC_Currency_ID(from.getC_Currency_ID());
		//notify.setC_DocTypeTarget_ID(C_DocTypeTarget_ID);
		 
		notify.setIsSOTrx(false);
		
		notify.setXX_IsDiferentPrice(IsDiferentPrice);
		
		/****Jessica Mendoza****/
		notify.setXX_DueDate(from.getXX_DueDate());
		notify.setXX_DatePaid(from.getXX_DatePaid());
		notify.setXX_AccountPayableStatus("A");
		notify.setXX_InvoiceType(from.getXX_InvoiceType());
		/****Fin código - Jessica Mendoza****/
		
		notify.save(transaction);
		
		return notify.get_ID();
	}
	
	
	/**
	 * 	Copy Lines From other Invoice.
	 *	@param otherInvoice invoice
	 * 	@param counter create counter links
	 * 	@param setOrder set order links
	 *	@return number of lines copied
	 */
	public int copyLinesFrom (MInvoice invoice, MInvoiceLine fromLine, 
			BigDecimal qty, BigDecimal price, boolean isQuantity, Trx transaction)
	{
		if (invoice == null)
			return 0;
		MOrderLine mOrderLine = null;
		//Trx trans = Trx.get("trans");
		MInvoiceLine line = new MInvoiceLine (ctx, 0, transaction);
		line.setC_Invoice_ID(invoice.getC_Invoice_ID());
		line.setInvoice(invoice);
		line.set_ValueNoCheck ("C_InvoiceLine_ID", Integer.valueOf(0));	// new
		
		
			line.setRef_InvoiceLine_ID(fromLine.getC_InvoiceLine_ID());
			line.setC_OrderLine_ID(fromLine.getC_OrderLine_ID());
			line.setM_InOutLine_ID(fromLine.getM_InOutLine_ID());
			//
			line.setM_Product_ID(fromLine.getM_Product_ID());
			line.setM_AttributeSetInstance_ID(fromLine.getM_AttributeSetInstance_ID());
			line.setC_Tax_ID(fromLine.getC_Tax_ID());
		//
			line.setA_Asset_ID(0);
			line.setS_ResourceAssignment_ID(0);
			line.setProcessed(false);
			
			if ((fromLine.getC_OrderLine_ID() > 0)){
				mOrderLine = new MOrderLine(ctx, fromLine.getC_OrderLine_ID(), null);
				line.setPriceEntered(mOrderLine.getPriceActual());
			}
			
			line.setPriceEntered(price);
			line.setPriceActual(price);
			line.set_Value("XX_PriceActualInvoice", price); 
			line.set_Value("XX_PriceEnteredInvoice", price);
			
			line.setQtyInvoiced(qty);
			line.setQtyEntered(qty);
			line.setXX_InvoicePrice(fromLine.getPriceActual());
		
		/*if (line.getQtyInvoiced().multiply(line.getPriceActual()).compareTo(Env.ZERO) == 0)
			{trans.rollback();
			 return 0;
			 }
		*/
		if(!line.save(transaction))
		{
			//trans.rollback(); WDIAZCAMBIO
			return 0;
		}
		//trans.commit(); WDIAZCAMBIO
		return line.get_ID();
	}	//	copyLinesFrom
	
	/**
	 * @param invoiceAviso Invoice que tiene el aviso. Donde busca las lineas a copiar
	 * @param invoiceNota Invoice donde se copian las Lineas
	 * @return
	 */
	public boolean copyLinesConvertir (MInvoice invoiceAviso, MInvoice invoiceNota){
		Trx trans = Trx.get("trans");
		MInvoiceLine lineaParaCopiar = null;
		MInvoiceLine lineaNueva= null;
		
		String sql = "SELECT C_INVOICELINE_ID " +
				     "FROM C_INVOICELINE " +
				     "WHERE C_INVOICE_ID = "+invoiceAviso.get_ID()+" " ;
		
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				lineaParaCopiar = new MInvoiceLine(Env.getCtx(), rs.getInt(1), trans);
				lineaNueva = new MInvoiceLine(Env.getCtx(), 0, trans);
				
				lineaNueva.setC_Invoice_ID(invoiceNota.getC_Invoice_ID());
				lineaNueva.setInvoice(invoiceNota);
				lineaNueva.setRef_InvoiceLine_ID(lineaParaCopiar.getC_InvoiceLine_ID());
				lineaNueva.setC_OrderLine_ID(lineaParaCopiar.getC_OrderLine_ID());
				lineaNueva.setM_InOutLine_ID(lineaParaCopiar.getM_InOutLine_ID());
				//
				lineaNueva.setM_Product_ID(lineaParaCopiar.getM_Product_ID());
				lineaNueva.setM_AttributeSetInstance_ID(lineaParaCopiar.getM_AttributeSetInstance_ID());
				lineaNueva.setC_Tax_ID(lineaParaCopiar.getC_Tax_ID());
				//
				lineaNueva.setPriceEntered(lineaParaCopiar.getPriceEntered());
				lineaNueva.setPriceActual(lineaParaCopiar.getPriceActual());
				
				lineaNueva.setXX_InvoicePrice(lineaParaCopiar.getXX_InvoicePrice());
				lineaNueva.setQtyInvoiced(lineaParaCopiar.getQtyInvoiced());
				lineaNueva.setQtyEntered(lineaParaCopiar.getQtyEntered());
				//
				lineaNueva.setA_Asset_ID(lineaParaCopiar.getA_Asset_ID());
				lineaNueva.setS_ResourceAssignment_ID(lineaParaCopiar.getS_ResourceAssignment_ID());
				lineaNueva.setProcessed(false);
				
				lineaNueva.save();
			}
			rs.close();
			pstmt.close();
		
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql, e);
			trans.rollback();
			return false;
		}
		
		return trans.commit();
	}	//	copyLinesConvertir
	
	
	/*
	 * FIN Avisos de debito y credito! 
	 */
	
	public MMovement GenerarMovimiento (int Desde, int Hacia, Trx transaccion)
	{
		try{
			MMovement  movimiento = new MMovement(Env.getCtx(), 0, transaccion);
			MLocator   locator = new MLocator(ctx, Desde, null);
				   movimiento.setAD_Org_ID(locator.getAD_Org_ID());
				   movimiento.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_MOVEMENTCD_ID"));
				   movimiento.setIsApproved(false);
				   movimiento.setIsActive(true);
				   movimiento.setIsInTransit(false);
				   movimiento.setPosted(false);
				   movimiento.setApprovalAmt(new BigDecimal(0));
				   movimiento.setM_Locator_ID(Desde);
				   movimiento.setM_LocatorTo_ID(Hacia);
				  // movimiento.setMovementDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
				   movimiento.save();
					   
		
		    return movimiento;
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public MMovementLine GenerarLineaMov (int deDonde, int haciaDonde, int movi, X_XX_VMR_OrderRequestDetail lineaPedido, Trx transaccion)
	{ 				   
		MMovementLine lineaMovimiento = new MMovementLine(Env.getCtx() , 0, transaccion);
		MLocator      locator = new MLocator(ctx, deDonde, transaccion);
		
		lineaMovimiento.setAD_Org_ID(locator.getAD_Org_ID());
		lineaMovimiento.setM_Movement_ID(movi);
		lineaMovimiento.set_ValueNoCheck("M_Product_ID", lineaPedido.getM_Product_ID());
		lineaMovimiento.setM_Locator_ID(deDonde);
		lineaMovimiento.setM_LocatorTo_ID(haciaDonde);
		lineaMovimiento.setXX_PriceConsecutive(lineaPedido.getXX_PriceConsecutive());

		//JPINO - Esta linea utiliza la cantidad del pedido no modificar ya que el completar asume esta cantidad
		lineaMovimiento.setMovementQty(new BigDecimal(lineaPedido.getXX_ProductQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP));
		lineaMovimiento.setIsActive(true);
		lineaMovimiento.setM_AttributeSetInstance_ID(lineaPedido.getXX_ProductBatch_ID());					 
		lineaMovimiento.save();
		
		//JPINO - En completar movimiento se necesita este objeto
		return lineaMovimiento;
		
	}
	
	public boolean ActuaInvPedido(X_XX_VMR_Order pedido, int pg, int Desde, int Hacia, Trx transaccion)
	{
		// pg = pedido o Guia de Despacho 0 si es pedido 1 si es por guia de despacho
		X_XX_VMR_OrderRequestDetail lineaPedido = null;
		int almDesde = Desde, almHacia = Hacia;
		PreparedStatement Pstmt = null;
	    ResultSet rs = null;
		try{
			MMovement movimiento = new Utilities().GenerarMovimiento(Desde, Hacia, transaccion);
			String sql = "Select * from XX_VMR_ORDERREQUESTDETAIL where XX_VMR_Order_ID = "+pedido.get_ID()+"";
			Pstmt = DB.prepareStatement(sql, null);
		       rs = Pstmt.executeQuery();
		    while (rs.next())
		    {
		    	lineaPedido = new X_XX_VMR_OrderRequestDetail(Env.getCtx(), rs, transaccion);
		    	new Utilities().GenerarLineaMov(almDesde, almHacia, movimiento.getM_Movement_ID(), lineaPedido, transaccion);
		    }
		    if (pg == 0)
		    	pedido.setXX_MovementRequest_ID(movimiento.getM_Movement_ID());
		    else
		        if(pg == 1)
		        	pedido.setXX_MovementDispatch_ID(movimiento.getM_Movement_ID());
		    pedido.save();
		    
		    movimiento.setDocAction(MMovement.DOCACTION_Complete);
		    DocumentEngine.processIt(movimiento, MMovement.DOCACTION_Complete);
			//movimiento.processIt (MMovement.DOCACTION_Complete); Version 321
			movimiento.save();
			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally
		{
			DB.closeStatement(Pstmt);
			DB.closeResultSet(rs);
		}
		
		return true;
	}
	
	public void GenerarConsecutivo(X_XX_VMR_Order header)
	{
		 PreparedStatement pstmt = null, pstmt1 = null, pstmt2 = null;
		  ResultSet rs = null,rs1 = null, rs2 = null;
		  int consecutivo = 1;
		  Calendar dateProd = Calendar.getInstance(), dateCons = Calendar.getInstance();
		  MAttributeSetInstance lote = null;
		  MOrder order = null; /* Agregado GHUCHET para O/C de DD*/
		  
		 try{
			 //Se generaran todos los consecutivos del pedido asi no lo hallan seleccionado y se le asociaran al pedido
					X_XX_VMR_OrderRequestDetail detallePedido= null, detallePedidoSiguiente = null;
					X_XX_VMR_PriceConsecutive ConsecutivoPrecio= null;
					Vector<Integer> VectorPedidosID = new Vector<Integer>();
					order = new MOrder(Env.getCtx(), header.getC_Order_ID(), null); /* Agregado GHUCHET para O/C de DD*/
					String sql=null;
						
			/**Se Buscaran todas los pedidos relacionados por distribucion del pedido que se va a imprimir wdiaz**/
					String ObtenerDistribucionID = (header.getXX_OrderBecoCorrelative().substring(4));
					
					String SqlPedidos = "select XX_VMR_Order_ID as pedidos " +
								                "from XX_VMR_Order where SUBSTR(XX_OrderBecoCorrelative , 5) =" +
								                "'"+ObtenerDistribucionID+"'";
					pstmt = DB.prepareStatement(SqlPedidos, null);
					rs = pstmt.executeQuery();
					 // if(rs.next()){
					//	  rs.beforeFirst();
						while (rs.next())
						{
							int PedidosID = rs.getInt("pedidos");
							VectorPedidosID.addElement(PedidosID);
							//VectorPedidosID = new Vector<Integer>();
						}
						DB.closeStatement(pstmt);
						DB.closeResultSet(rs);
					for(int i=0; i< VectorPedidosID.size();i++){
						int Pedidos =VectorPedidosID.elementAt(i);
						//int DistribucionID = Integer.parseInt(rs.getString("distribucion"));
						//ponerle la restriccion de que este vacio el consecutivoID del pedido
						String SqlDetallesPedido = "SELECT XX_VMR_ORDERREQUESTDETAIL_ID as detalle " +
								"FROM XX_VMR_ORDERREQUESTDETAIL WHERE XX_VMR_ORDER_ID = " + Pedidos + " and XX_PRICECONSECUTIVE is null";
						pstmt = DB.prepareStatement(SqlDetallesPedido, null);
						rs = pstmt.executeQuery();
						while(rs.next())
						{		
							/**genero el consecutivo*/
							detallePedido= new X_XX_VMR_OrderRequestDetail(Env.getCtx(), rs.getInt("detalle"), null);
							//RefLineOrder = BuscarRefOrder(detallePedido.getM_Product_ID(), detallePedido.getXX_ProductBatch_ID());
							consecutivo = 1;
						    sql = "select * from XX_VMR_PriceConsecutive where XX_PriceConsecutive = " +
						    		"(select max(XX_PriceConsecutive) from XX_VMR_PriceConsecutive where " +
						    		"M_Product_ID = "+detallePedido.getM_Product_ID()+") and  M_Product_ID = "+detallePedido.getM_Product_ID()+" " +
						    		"order by M_ATTRIBUTESETINSTANCE_ID desc";
							pstmt1 = DB.prepareStatement(sql, null);
							rs1 = pstmt1.executeQuery();
							if (rs1.next())
							  {
								 //Ultimo Consecutivo del Producto
								ConsecutivoPrecio = new X_XX_VMR_PriceConsecutive(Env.getCtx(), rs1, null);
								consecutivo = ConsecutivoPrecio.getXX_PriceConsecutive();
								dateCons.setTimeInMillis(ConsecutivoPrecio.getCreated().getTime());
								 //comparar precio de venta, costo y fecha en que se genera
								//RefLineOrder = BuscarRefOrder(detallePedido.getM_Product_ID(), detallePedido.getXX_ProductBatch_ID());
								lote = new MAttributeSetInstance(ctx, detallePedido.getXX_ProductBatch_ID(), null);
								
								/*Agregado por GHUCHET para 0/C de Despacho Directo*/    					
								if(order.getXX_VLO_TypeDelivery().compareTo(X_Ref_XX_Ref_TypeDelivery.DESPACHO_DIRECTO.getValue())==0){
									dateProd.setTimeInMillis(order.getXX_EstimatedDate().getTime());
								} /*Hasta aqui GHUCHET*/
								
								if ((ConsecutivoPrecio.getXX_UnitPurchasePrice().compareTo(lote.get_ValueAsBigDecimal("PriceActual")) == 0)
									&&(ConsecutivoPrecio.getXX_SalePrice().compareTo(detallePedido.getXX_SalePrice()) == 0)
									&&((dateProd.get(Calendar.YEAR) == dateCons.get(Calendar.YEAR)) 
									&&((dateProd.get(Calendar.MONTH)+1) == (dateCons.get(Calendar.MONTH)+1))))
								 {
									 //Creamos el producto con el ultimo consecutivo si solo son dostintos los lotes
									 if (ConsecutivoPrecio.getM_AttributeSetInstance_ID() != detallePedido.getXX_ProductBatch_ID())
									 {
										 ConsecutivoPrecio = CrearConsecutivo(detallePedido, consecutivo, header.getC_Order_ID(), Pedidos);
									 }
										 
								}
								 else
								 { 
									 //creamos otro consecutivo para el producto
									 consecutivo = (consecutivo) + 1;
									 ConsecutivoPrecio = CrearConsecutivo(detallePedido, consecutivo, header.getC_Order_ID(), Pedidos);
								}
							}
							 else
							 {
								  //Se crea un primer consecutivo para dicho producto
								 ConsecutivoPrecio = CrearConsecutivo(detallePedido, consecutivo, header.getC_Order_ID(), Pedidos);
							 }    
							/**fin de generar consecutivo**/
							DB.closeStatement(pstmt1);
							DB.closeResultSet(rs1);
							
							/**Buscar y Actualizar los otros pedidos (el ID del Consecutivo)**/
							for(int j=i+1; j< VectorPedidosID.size();j++)
							{
								int PedidoIDSiguiente =VectorPedidosID.elementAt(j);
								String SqlDetallesPedidoSiguiente = "SELECT * " +
								"FROM XX_VMR_ORDERREQUESTDETAIL WHERE XX_VMR_ORDER_ID = " + PedidoIDSiguiente +" and XX_PRICECONSECUTIVE is null " +
								"and M_Product_ID = "+detallePedido.getM_Product_ID()+" and XX_ProductBatch_ID = "+ detallePedido.getXX_ProductBatch_ID();
								pstmt2 = DB.prepareStatement(SqlDetallesPedidoSiguiente, null);
								rs2 = pstmt2.executeQuery();
								while(rs2.next())
								{
									detallePedidoSiguiente= new X_XX_VMR_OrderRequestDetail(Env.getCtx(), rs2, null);
									//realizamos la comparacion si es el mismo producto con el mismo lote
									//asignamos el ID del Consecutivo ya creado al producto del detallePedidoSiguiente
									detallePedidoSiguiente.setXX_PriceConsecutive(ConsecutivoPrecio.getXX_PriceConsecutive());
									detallePedidoSiguiente.save();
								}
								DB.closeStatement(pstmt2);
								DB.closeResultSet(rs2);
							}
							/**fin de actualizar**/
							detallePedido.setXX_PriceConsecutive(ConsecutivoPrecio.getXX_PriceConsecutive());
							detallePedido.save();
						}
					}
			    }catch (Exception e) {
					e.printStackTrace();
					log.saveError("Error", Msg.getMsg(ctx, "XX_Support"));
				}finally
				{
					DB.closeStatement(pstmt);
					DB.closeResultSet(rs);
				}
					  //Fin del codigo de Consecutivo de Precio
	}
	
	private X_XX_VMR_PriceConsecutive CrearConsecutivo(X_XX_VMR_OrderRequestDetail detallePedido, int consecutivo, int order, int pedido)
	{
			//MVMRPOLineRefProv RefLineOrder = null;
			X_XX_VMR_PriceConsecutive ConsecutivoPrecio= null;
			MAttributeSetInstance lote = new MAttributeSetInstance(ctx, detallePedido.getXX_ProductBatch_ID(), null);
			ConsecutivoPrecio = new X_XX_VMR_PriceConsecutive(Env.getCtx(), 0, null);
		    ConsecutivoPrecio.setM_Product_ID(detallePedido.getM_Product_ID());
			ConsecutivoPrecio.setXX_PriceConsecutive(consecutivo);
			ConsecutivoPrecio.setXX_VMR_GeneratedBy_ID(pedido);
			ConsecutivoPrecio.setXX_ConsecutiveOrigin(X_Ref_XX_ConsecutiveOrigin.PEDIDO.getValue());
			if (order > 0)
				ConsecutivoPrecio.setC_Order_ID(order);
			ConsecutivoPrecio.setXX_SalePrice(detallePedido.getXX_SalePrice().setScale(2, BigDecimal.ROUND_HALF_UP));
			//RefLineOrder = BuscarRefOrder(detallePedido.getM_Product_ID(), detallePedido.getXX_ProductBatch_ID());
			ConsecutivoPrecio.setXX_UnitPurchasePrice(lote.get_ValueAsBigDecimal("PriceActual"));
			ConsecutivoPrecio.setM_AttributeSetInstance_ID(detallePedido.getXX_ProductBatch_ID());

			/*Agregado por GHUCHET para 0/C de Despacho Directo*/    					
			MOrder p_order = new MOrder(ctx, order, null); 
			if(p_order.getXX_VLO_TypeDelivery().compareTo(X_Ref_XX_Ref_TypeDelivery.DESPACHO_DIRECTO.getValue())==0){
				ConsecutivoPrecio.set_ValueNoCheck("Created",p_order.getXX_EstimatedDate());
			} /*Hasta aqui GHUCHET*/
			
			ConsecutivoPrecio.save();
		return ConsecutivoPrecio;
	}
	
	public MVMRPOLineRefProv BuscarRefOrder(int producto, int atributo)
	{
		String sql = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MVMRPOLineRefProv RefLineOrder = null;
		MOrderLine lineaOrder = null;
		
		sql = "select m.* from M_INOUTLINE m  join C_ORDERLINE l on (l.C_ORDERLINE_ID = m.C_ORDERLINE_ID) " +
			  "join C_Order c  on (c.c_order_id = l.c_order_id) where m.M_Product_ID = "+producto+"" +
			  "and m.M_AttributeSetInstance_ID = "+atributo+" and c.ISSOTRX = 'N'";
		
		try{
			 pstmt = DB.prepareStatement(sql, null);
			 rs = pstmt.executeQuery();
			 if (rs.next()){
		    //Tomamos La Linea de la ORden de Compra Anterior de este producto por el codigo que posee la linea de la recepcion
				 lineaOrder= new MOrderLine(Env.getCtx(), rs.getInt("C_OrderLine_ID"), null); 
		     //tomamos la ref de la linea que esta en almacen para comparar con la actual linea de la recepcion
				 RefLineOrder = new MVMRPOLineRefProv (Env.getCtx(), lineaOrder.getXX_VMR_PO_LINEREFPROV_ID(), null);
				 return RefLineOrder;	 
			 }else
				 return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
			}
	}
	
	public boolean isNonBusinessDay(Timestamp date){
		
		//Verifico si es un sabado o un domingo
		if(date.getDay()==6 || date.getDay()==0){
			return true;
		}
		
		//Verifico si es un dia feriado
		String SQL = "SELECT * FROM C_NonBusinessDay " +
					 "WHERE DATE1 = TO_DATE('"+date.toString().substring(0, 10)+"','YYYY-MM-DD')";
		
			PreparedStatement pstmt = null; 
			ResultSet rs = null;
			try{
					
				pstmt = DB.prepareStatement(SQL, null); 
				rs = pstmt.executeQuery();
					 
				if(rs.next())
				{
				   return true;
				}
	   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}finally{
				
				try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
				try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		
		return false;
	}
	//Algoritmo que calcula las piezas que se van a Aceptar(Posicion 3), FaltantesConCosto(posicion 0), Excedente(posicion 1), Devolucion(posicion 2), DevolucionConCosto(posicion 4), DevolucionSinCosto(posicion 5), CreditoDebito(posicion 6)
	public Vector<BigDecimal> PartsReceivedAlgorithm(BigDecimal cantOrdenada , BigDecimal cantChequeada , BigDecimal cantFacturada, BigDecimal cantDevuelta)
	{
	BigDecimal cantDevConCosto = new BigDecimal(0), cantDevSinCosto = new BigDecimal(0), ajuste = new BigDecimal(0),
	           ajuste1 = new BigDecimal(0), ajuste2 = new BigDecimal(0);
	Vector<BigDecimal> cantidades = new Vector<BigDecimal>(); 
	
		if(cantOrdenada.compareTo(cantFacturada) == 0 && cantOrdenada.compareTo(cantChequeada) == 0)// caso 1
		{
			cantDevConCosto = cantDevuelta;
			cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
		}
		else // caso 2
			if(cantOrdenada.compareTo(cantFacturada) == 0 && cantFacturada.compareTo(cantChequeada) > 0) 
			{
				cantDevConCosto = cantDevuelta;
				cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
			}
			else // caso 3
				if(cantOrdenada.compareTo(cantFacturada) == 0 && cantFacturada.compareTo(cantChequeada) < 0 
				   && cantDevuelta.compareTo(cantFacturada) <= 0) 
				{
					cantDevSinCosto = cantChequeada.subtract(cantFacturada);
					cantDevConCosto = cantDevuelta; 
					cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
				}
				else // caso 5
					if (cantOrdenada.compareTo(cantChequeada) == 0 && cantFacturada.compareTo(cantOrdenada) < 0 
						&& cantDevuelta.compareTo(new BigDecimal(0))> 0 && cantDevuelta.compareTo(cantFacturada)> 0)
					{
						ajuste =  cantChequeada.subtract(cantFacturada);
							if (ajuste.compareTo(cantDevuelta) >= 0)
								cantDevSinCosto = cantDevuelta;
							else
							{
								cantDevConCosto = cantFacturada;
								cantDevSinCosto = cantDevuelta.subtract(cantDevConCosto);
							}
							cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
					}
					else // caso 12
						if (cantOrdenada.compareTo(cantFacturada) > 0 && cantFacturada.compareTo(cantChequeada) < 0 
							&& cantChequeada.compareTo(cantOrdenada)<= 0 && cantDevuelta.compareTo(cantFacturada)<= 0)
						{
							cantDevConCosto = cantDevuelta;
							cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
						}   
						else //caso 6
							if (cantOrdenada.compareTo(cantChequeada) == 0 && cantFacturada.compareTo(cantOrdenada) < 0 
								&& cantDevuelta.compareTo(new BigDecimal(0)) > 0 && cantDevuelta.compareTo(cantFacturada) < 0)                                       
							              
							{
								ajuste = cantChequeada.subtract(cantFacturada);
									if (ajuste.compareTo(cantDevuelta) >= 0)
										cantDevSinCosto = cantDevuelta;
									else
									{
										cantDevSinCosto = cantChequeada.subtract(cantFacturada);
										cantDevConCosto = cantDevuelta.subtract(cantDevSinCosto);
									}
									cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
							}
							else // caso 7
								if(cantOrdenada.compareTo(cantChequeada) == 0 && cantFacturada.compareTo(cantOrdenada)< 0 
									&& cantDevuelta.compareTo(new BigDecimal(0)) == 0)
								{
									cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
								}
								else // caso 8
									if(cantOrdenada.compareTo(cantFacturada) > 0 && cantFacturada.compareTo(cantChequeada)>= 0)
									{
										cantDevConCosto = cantDevuelta;
										cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
									}
									else // caso 9
										if(cantOrdenada.compareTo(cantFacturada) < 0 && cantFacturada.compareTo(cantChequeada)<= 0
											&& cantDevuelta.compareTo(new BigDecimal(0)) == 0)
										{
											cantDevSinCosto = cantChequeada.subtract(cantFacturada);
											cantDevConCosto = cantFacturada.subtract(cantOrdenada);
											cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
										}
										else // caso 10
											if (cantOrdenada.compareTo(cantFacturada) < 0 && cantFacturada.compareTo(cantChequeada) <= 0 
													&& cantDevuelta.compareTo(new BigDecimal(0)) != 0 && cantDevuelta.compareTo(cantFacturada) > 0)
											{
												cantDevConCosto = cantFacturada.subtract(cantOrdenada);
												cantDevSinCosto = cantChequeada.subtract(cantFacturada);
												cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
											}
											else //caso 11
												if (cantOrdenada.compareTo(cantFacturada) < 0 && cantFacturada.compareTo(cantChequeada) <= 0
														&& cantDevuelta.compareTo(new BigDecimal(0)) != 0 && cantDevuelta.compareTo(cantFacturada) <= 0)
												{
													ajuste = cantFacturada.subtract(cantOrdenada);
													if (ajuste.compareTo(cantDevuelta) >= 0)
													{
														cantDevConCosto = cantDevuelta.add(ajuste);
														cantDevSinCosto = cantChequeada.subtract(cantFacturada);
													}
													else
													{
														cantDevConCosto = cantFacturada.subtract(cantOrdenada);
														cantDevConCosto = cantDevConCosto.add(cantDevuelta); //ADD       COD61         CANDCC 
														cantDevSinCosto = cantChequeada.subtract(cantFacturada);
														
													}
													cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
												}
												else// caso 13
													if (cantOrdenada.compareTo(cantFacturada) > 0 && cantFacturada.compareTo(cantChequeada) < 0
															&& cantChequeada.compareTo(cantOrdenada) < 0 && cantDevuelta.compareTo(cantFacturada) > 0)
													{
														cantDevSinCosto = cantDevuelta;
														cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
													}
													else// caso 14
														if (cantOrdenada.compareTo(cantFacturada) > 0 && cantFacturada.compareTo(cantChequeada) < 0
																&& cantChequeada.compareTo(cantOrdenada) > 0 && cantDevuelta.compareTo(new BigDecimal(0)) == 0)
														{
															cantDevSinCosto = cantChequeada.subtract(cantOrdenada);
															cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
														}
														else //caso 15
															if(cantOrdenada.compareTo(cantFacturada) > 0 && cantFacturada.compareTo(cantChequeada) < 0
																	&& cantChequeada.compareTo(cantOrdenada) > 0 && cantDevuelta.compareTo(cantFacturada) > 0
																	&& cantDevuelta.compareTo(cantFacturada)!= 0)
															{
																cantDevConCosto = cantFacturada; //WDIAZ Cambio 18-06-2012 en ajuste 1 se restaba la OC y no la Dev.Con.Costo
																//cantDevConCosto = cantDevuelta;
																ajuste1 = cantDevuelta.subtract(cantDevConCosto);
																ajuste2 = cantChequeada.subtract(cantOrdenada);
																cantDevSinCosto = ajuste1.add(ajuste2);
																cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
															}
															else // caso 16
																if(cantOrdenada.compareTo(cantFacturada) > 0 && cantFacturada.compareTo(cantChequeada) < 0
																		&& cantChequeada.compareTo(cantOrdenada) > 0 && cantDevuelta.compareTo(cantFacturada)!= 0
																		&& cantDevuelta.compareTo(cantFacturada) <= 0)
																{
																	cantDevConCosto = cantDevuelta;
																	cantDevSinCosto = cantChequeada.subtract(cantOrdenada);
																	cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
																}
																else //17
																	if(cantOrdenada.compareTo(cantFacturada) < 0 && cantFacturada.compareTo(cantChequeada) > 0
																			&& cantChequeada.compareTo(cantOrdenada) <= 0)
																	{
																		cantDevConCosto = cantDevuelta;
																		cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
																	}
																	else //18
																		if(cantOrdenada.compareTo(cantFacturada) < 0 && cantFacturada.compareTo(cantChequeada) > 0
																				&& cantChequeada.compareTo(cantOrdenada) > 0)
																		{
																			cantDevConCosto = cantChequeada.subtract(cantOrdenada);
																			cantDevConCosto = cantDevConCosto.add(cantDevuelta);
																			cantidades = Rutina(cantFacturada, cantChequeada, cantDevConCosto, cantDevSinCosto);
																		}
                      		
							
		 return cantidades;                                                                                          
		      
	}
	
	private Vector<BigDecimal> Rutina(BigDecimal cantFacturada, BigDecimal cantChequeada, BigDecimal cantDevConCosto, BigDecimal cantDevSinCosto)
	{  
		BigDecimal cantFaltanteConCosto = new BigDecimal(0), cantExcedente = new BigDecimal(0), devolucion = new BigDecimal(0),
					cantAceptada = new BigDecimal(0), cantNSQS = new BigDecimal(0) ;
		Vector<BigDecimal> cantidades = new Vector<BigDecimal>(); 
		
		
		if (cantFacturada.compareTo(cantChequeada) > 0)
			cantFaltanteConCosto = cantFacturada.subtract(cantChequeada);
		else
			cantExcedente = cantChequeada.subtract(cantFacturada);
		
		devolucion = cantDevConCosto.add(cantDevSinCosto);
		cantAceptada = cantChequeada.subtract(devolucion);
		cantNSQS = cantAceptada.subtract(cantFacturada);
		
		cantidades.add(cantFaltanteConCosto);
		cantidades.add(cantExcedente);
		cantidades.add(devolucion);
		cantidades.add(cantAceptada);
		cantidades.add(cantDevConCosto);
		cantidades.add(cantDevSinCosto);
		cantidades.add(cantNSQS);		
		return cantidades;
	}
	
	/** Retorna el locator en transito para un almacen determinado*/
	public static MLocator obtenerLocatorEnTransito(int m_warehouse_id) {
		
		//Hacer la búsqueda en la BD
		String sql = "select M_Locator_ID from M_Locator where M_Warehouse_ID = "
			+ m_warehouse_id + " and upper(VALUE) like '%TRANSITO%'";
		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		MLocator locator_transito = null;
		try {
			rs = ps.executeQuery();
			if (rs.next()) {
				locator_transito =
					new MLocator(Env.getCtx(), rs.getInt("M_Locator_ID"), null);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return (locator_transito);
	}
	
	/** Retorna el locator chequeado para un almacen determinado*/
	public static MLocator obtenerLocatorChequeado(int m_warehouse_id) {
		
		//Hacer la búsqueda en la BD
		String sql = "select M_Locator_ID from M_Locator where M_Warehouse_ID = "
			+ m_warehouse_id + " and upper(VALUE) like '%CHEQUEADO%'";
		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		MLocator locator_chequeado = null;
		try {
			rs = ps.executeQuery();
			if (rs.next()) {
				locator_chequeado =
					new MLocator(Env.getCtx(), rs.getInt("M_Locator_ID"), null);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return (locator_chequeado);
	}
	
	/** Retorna el locator en transito para un almacen determinado*/
	public static MLocator obtenerLocatorDevolucion(int m_warehouse_id) {
		
		//Hacer la búsqueda en la BD
		String sql = "select M_Locator_ID from M_Locator where M_Warehouse_ID = "
			+ m_warehouse_id + " and upper(VALUE) like '%DEVOLUCION%'";
		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		MLocator locator_transito = null;
		try {
			rs = ps.executeQuery();
			if (rs.next()) {
				locator_transito =
					new MLocator(Env.getCtx(), rs.getInt("M_Locator_ID"), null);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return (locator_transito);
	}	
	
	/** Retorna el locator en transito para un almacen determinado*/
	public static MLocator obtenerLocatorEnTienda(int m_warehouse_id) {
		
		//Hacer la búsqueda en la BD
		String sql = "select M_Locator_ID from M_Locator where M_Warehouse_ID = "
			+ m_warehouse_id + " and ISDEFAULT = 'Y'";
		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		MLocator locator_transito = null;
		try {
			rs = ps.executeQuery();
			if (rs.next()) {
				locator_transito =
					new MLocator(Env.getCtx(), rs.getInt("M_Locator_ID"), null);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return (locator_transito);
	}
	
	/** Retorna el locator en transito para un almacen determinado*/
	public static MWarehouse obtenerWarehouse(int m_locator_id) {
		
		//Hacer la búsqueda en la BD
		String sql = "select M_Warehouse_ID from M_Locator where M_Locator_ID = "
			+ m_locator_id;		
		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		MWarehouse warehouse = null;
		try {
			rs = ps.executeQuery();
			if (rs.next()) {
				warehouse = 
					new MWarehouse(Env.getCtx(), rs.getInt("M_Warehouse_ID"), null);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return (warehouse);
	}
	
	/** Retorna true si el locator es  Centro de Distribución Devolución
	  * @author ghuchet */
	public static boolean esCDDevolucion(int m_locator_id) {
		
		//Hacer la búsqueda en la BD
		String sql = "select M_Locator_ID from M_Locator where M_Locator_ID = "
			+ m_locator_id + " and upper(VALUE) like '%DEVOLUCION%'";
		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		boolean cd_devolucion = false;
		try {
			rs = ps.executeQuery();
			if (rs.next()) {
				cd_devolucion= true;
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return (cd_devolucion);
	}	
	
	/** Retorna true si el almacen es un Centro de Distribución
	 * @author ghuchet */
	public static boolean esCD(int m_warehouse_id) {
		
		//Hacer la búsqueda en la BD
		String sql = "SELECT (CASE WHEN XX_ISSTORE = 'Y' THEN 0 ELSE 1 END) ESCD FROM M_WAREHOUSE WHERE M_WAREHOUSE_ID = "+ m_warehouse_id;		
		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		boolean cd = false;
		try {
			rs = ps.executeQuery();
			if (rs.next()) {
				cd = rs.getBoolean(1);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return cd;
	}
	
	
	/** Retorna Centro de Distribución de una distribución dada
	 * @author ghuchet */
	public static int  obtenerDistribucionCD(int distID) {
		
		//Hacer la búsqueda en la BD
		String sql = "SELECT M_WAREHOUSE_ID FROM XX_VMR_DISTRIBUTIONHEADER WHERE XX_VMR_DISTRIBUTIONHEADER_ID = "+distID;		
		PreparedStatement ps = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		int warehouse = 0;
		try {
			rs = ps.executeQuery();
			if (rs.next()) {
				warehouse= rs.getInt("M_Warehouse_ID");
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return warehouse;
	}
	

	/*
	 * 
	 * 
	 */
	public boolean recibirGuiaDespacho (Integer dispatchGuide_ID, Trx trx) throws SQLException{
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null, rs1 = null;
		X_XX_VMR_Order pedido = null;
		int locatorHacia;
		int locatorDesde;
		Trx trxAux = trx;
				
		
		X_XX_VLO_DispatchGuide guiaDespacho = new X_XX_VLO_DispatchGuide(Env.getCtx(), dispatchGuide_ID, trx);
		System.out.println("Recibiendo GD# "+guiaDespacho.getDocumentNo()+" ");
		if (true){
			String sql2 = "SELECT DDG.M_MOVEMENTR_ID, DDG.M_MOVEMENTT_ID, DDG.M_MOVEMENTM_ID " +
						  "FROM XX_VLO_DETAILDISPATCHGUIDE DDG " +
						  "WHERE (DDG.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.STORE_RETURNS.getValue()+"' " +
						        " OR DDG.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.INVENTORY_MOVEMENT.getValue()+"' " +
						  "OR DDG.XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.TRANSFERS_BETWEEN_STORES.getValue()+"' )" +
						  "AND DDG.XX_VLO_DISPATCHGUIDE_ID = "+guiaDespacho.get_ID()+"";
			try{
				PreparedStatement pstmt2 = DB.prepareStatement(sql2, trx);
				ResultSet rs2 = pstmt2.executeQuery();
				while(rs2.next()){
					Integer movimientor = null;
					Integer movimientot = null;
					Integer movimientom = null;
					
					movimientor = rs2.getInt("M_MOVEMENTR_ID");
					movimientot = rs2.getInt("M_MOVEMENTT_ID");
					movimientom = rs2.getInt("M_MOVEMENTM_ID");
					
					//Si el movimiento es de tipo devolucion de productos
					if(movimientor != 0) {
						MMovement movimiento = new MMovement(Env.getCtx(), movimientor, null);
						movimiento.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENTIENDA.getValue());
						movimiento.save();
						
						/*String create_returns = "SELECT * FROM M_MOVEMENTLINE WHERE M_MOVEMENT_ID = " + movimientor; 
				    	PreparedStatement ps_returns = DB.prepareStatement(create_returns, TrxName);
				    	ResultSet rs_returns = ps_returns.executeQuery();
				    	MMovementLine linea = null;
				    	while (rs_returns.next()) {
				    		linea = new MMovementLine(ctx, rs_returns.getInt("M_MOVEMENTLINE_ID"), null);
				    		linea.set_Value("XX_QuantityReceived", linea.getXX_ApprovedQty());
				    		linea.save();
				    	}*/
						/*MMovement movimiento_viejo = new MMovement(Env.getCtx(), movimientor, TrxName);						
						MMovement movimiento = new MMovement(Env.getCtx(), 0, TrxName);
						System.out.println("	Dev# "+movimiento_viejo.getDocumentNo()+" ");
						
						//Se deben copiar los movimientos
						movimiento.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_MOVEMENTCD_ID"));
						movimiento.setDescription(movimiento_viejo.getDescription());
						movimiento.setMovementDate(movimiento_viejo.getMovementDate());
						
						
						//Hallar el locator desde
						MLocator desde = Utilities.obtenerLocatorEnTransito(
											movimiento_viejo.getM_WarehouseTo_ID());						
						movimiento.setM_Locator_ID(desde.get_ID());
						
						//El locator hasta dado que es retorno es el locator devolucion
						movimiento.setM_LocatorTo_ID(
								Env.getCtx().getContextAsInt("#XX_L_LOCATORCDDEVOLUCION_ID")						
						);
						//El resto de los campos
						movimiento.setXX_VMR_Department_ID(movimiento_viejo.getXX_VMR_Department_ID());
						movimiento.setXX_DispatchDate(movimiento_viejo.getXX_DispatchDate());
						movimiento.setXX_PackageQuantity(movimiento_viejo.getXX_PackageQuantity());
						movimiento.setXX_RequestDate(movimiento_viejo.getXX_RequestDate());
						movimiento.setM_WarehouseFrom_ID(movimiento_viejo.getM_WarehouseFrom_ID());
						movimiento.setM_WarehouseTo_ID(movimiento_viejo.getM_WarehouseTo_ID());
						movimiento.save(TrxName);
						trxAux.commit();
						
						
						movimiento.load(TrxName);
											
						//Agregado por Javier - Crea los detalles de devolucion -No aplica a traspasos
				    	String create_returns = "SELECT * FROM M_MOVEMENTLINE WHERE M_MOVEMENT_ID = " + movimientor; 
				    	PreparedStatement ps_returns = DB.prepareStatement(create_returns, TrxName);
				    	ResultSet rs_returns = ps_returns.executeQuery();
				    	int return_of_product = 0;
				    	MMovementLine linea = null;
				    	while (rs_returns.next()) {
				    		
				    		MProduct product = new MProduct(Env.getCtx(), rs_returns.getInt("M_PRODUCT_ID"), TrxName);					    		
				    		if (return_of_product == 0) {
				    			X_XX_VLO_ReturnOfProduct preturn = 
					    			new X_XX_VLO_ReturnOfProduct(Env.getCtx(), 0 ,TrxName);					    		
					    		preturn.setXX_Status(X_Ref_XX_StatusReturn.PENDIENTE_POR_RETIRAR.getValue());
					    		preturn.setC_BPartner_ID(product.getC_BPartner_ID());						    		
					    		preturn.setXX_ReturnedFrom("ST");
					    		preturn.setM_Warehouse_ID(movimiento_viejo.getM_WarehouseFrom_ID());
					    		preturn.setM_Movement_ID(movimientor);					    		
					    		preturn.save();
					    		preturn.load(TrxName);
					    		return_of_product = preturn.get_ID();
				    		}

				    		//Se llena el detalle de devolucion				    		
				    		X_XX_VLO_ReturnDetail detail = 
				    			new X_XX_VLO_ReturnDetail(Env.getCtx(), 0, TrxName);
				    		detail.setXX_VLO_ReturnOfProduct_ID(return_of_product);
				    		detail.setM_Product_ID(product.get_ID());
				    		detail.setXX_VMR_CancellationMotive_ID(rs_returns.getInt("XX_RETURNMOTIVE_ID"));
				    		detail.setXX_TotalPieces(rs_returns.getInt("MOVEMENTQTY"));
				    		detail.setC_TaxCategory_ID(rs_returns.getInt("C_TaxCategory_ID"));
				    		detail.setPriceActual(rs_returns.getBigDecimal("PRICEACTUAL"));
				    		detail.setTaxAmt(rs_returns.getBigDecimal("TAXAMT"));
				    		detail.setM_AttributeSetInstance_ID(rs_returns.getInt("M_ATTRIBUTESETINSTANCETO_ID"));
				    		detail.save();
				    					    		
				    		linea = new MMovementLine(Env.getCtx(), 0, TrxName);
				    		linea.setDescription(rs_returns.getString("DESCRIPTION"));
				    		linea.setConfirmedQty(rs_returns.getBigDecimal("CONFIRMEDQTY"));
				    		linea.setM_AttributeSetInstanceTo_ID(rs_returns.getInt("M_ATTRIBUTESETINSTANCETO_ID"));
				    		linea.setM_AttributeSetInstance_ID(rs_returns.getInt("M_ATTRIBUTESETINSTANCETO_ID"));
				    		linea.setM_LocatorTo_ID(movimiento.getM_LocatorTo_ID());				    		
				    		linea.setM_Locator_ID(movimiento.getM_Locator_ID());
				    		linea.setM_Movement_ID(movimiento.get_ID());
				    		linea.setM_Product_ID(rs_returns.getInt("M_PRODUCT_ID"));
				    		linea.setMovementQty(rs_returns.getBigDecimal("MOVEMENTQTY"));
				    		
				    		if (rs_returns.getInt("XX_VMR_BRAND_ID") != 0)
				    			linea.setXX_VMR_Brand_ID(rs_returns.getInt("XX_VMR_BRAND_ID"));
				    		linea.setXX_VMR_Line_ID(rs_returns.getInt("XX_VMR_Line_ID"));
				    		linea.setXX_VMR_Section_ID(rs_returns.getInt("XX_VMR_Section_ID"));
				    		linea.setXX_SalePrice(rs_returns.getBigDecimal("XX_SalePrice"));
				    		linea.setXX_ReturnMotive_ID(rs_returns.getInt("XX_ReturnMotive_ID"));
				    		linea.setQtyRequired(rs_returns.getBigDecimal("QtyRequired"));
				    		linea.setC_TaxCategory_ID(rs_returns.getInt("C_TaxCategory_ID"));
				    		linea.setPriceActual(rs_returns.getBigDecimal("PRICEACTUAL"));
				    		linea.setTaxAmt(rs_returns.getBigDecimal("TAXAMT"));
				    		linea.save();
				    	}
				    	rs_returns.close();
				    	ps_returns.close();
				    	
				    	
				    	//Ahora se completa el movimiento				    	
						movimiento.setXX_DispatchDate(movimiento.getUpdated());
						movimiento.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENTIENDA.getValue());
						
						//Aprueba el movimento para que que pase al locator en transito de la tienda
						movimiento.setDocAction(X_M_Movement.DOCACTION_Complete);
					    movimiento.processIt (X_M_Movement.DOCACTION_Complete);				    
					    movimiento.save();
					    
					    if (movimiento.getDocStatus().equals(MMovement.DOCSTATUS_Completed)) {
					    	//Modificar el viejo
						    movimiento_viejo.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENTIENDA.getValue());
						    movimiento_viejo.save();
						 */   
							//Generar Factura y Purchase's Book JTrias
						    //TODO Colocar ("setear") Movimiento en Invoice
						    /*int invoiceAux = generateInvoice(TrxName,movimiento_viejo,linea.getM_Product_ID());
						    generatePurchaseBook( TrxName, movimiento_viejo, linea.getM_Product_ID(), invoiceAux);*/
						    
					   /* } else {					    	
					    	log.saveError("Error", Msg.translate(Env.getCtx(), "XX_ReceptionFail"));
					    }
					    */
					} else if (movimientot != 0 || movimientom != 0) {
						MMovement movimiento_viejo = null;
						// En el caso de que sea un traspaso o movimiento entre Centros de Distribucion
						if (movimientot!=0)
							movimiento_viejo = new MMovement(Env.getCtx(), movimientot, trx);	
						else
							movimiento_viejo = new MMovement(Env.getCtx(), movimientom, trx);	
						MMovement movimiento = new MMovement(Env.getCtx(), 0, trx);
						System.out.println("	Tra# "+movimiento_viejo.getDocumentNo()+" ");						
						
//						//Se deben copiar los movimientos 
//						if (movimientot!=0)
//							movimiento.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_MOVEMENTCD_ID"));
//						else
//							movimiento.setC_DocType_ID(1000335);
						movimiento.setC_DocType_ID(Env.getCtx().getContextAsInt("#XX_L_MOVEMENTCD_ID"));
						
						movimiento.setDescription(movimiento_viejo.getDescription());
						movimiento.setMovementDate(movimiento_viejo.getMovementDate());

						//Hallar el locator desde, que es el locator en transito de la tienda destino
						MLocator desde = Utilities.obtenerLocatorEnTransito(
											movimiento_viejo.getM_WarehouseTo_ID());						
						movimiento.setM_Locator_ID(desde.get_ID());
						MLocator hasta = null;
						if (movimientot!=0)
						{
							//El locator hasta, que es el locator en tienda del almacen destino
							hasta = Utilities.obtenerLocatorEnTienda(movimiento_viejo.getM_WarehouseTo_ID());					
						} else
						{
							//El locator hasta, que es el locator chequeado del almacen destino
							hasta = Utilities.obtenerLocatorChequeado(movimiento_viejo.getM_WarehouseTo_ID());		
						}
						movimiento.setM_LocatorTo_ID(hasta.get_ID());
						//El resto de los campos
						movimiento.setXX_VMR_Department_ID(movimiento_viejo.getXX_VMR_Department_ID());
						movimiento.setXX_TransferMotive_ID(movimiento_viejo.getXX_TransferMotive_ID());
						movimiento.setXX_DispatchDate(movimiento_viejo.getXX_DispatchDate());
						movimiento.setXX_PackageQuantity(movimiento_viejo.getXX_PackageQuantity());
						movimiento.setXX_RequestDate(movimiento_viejo.getXX_RequestDate());
						
						//Comparten el mismo warehouse
						movimiento.setM_WarehouseFrom_ID(movimiento_viejo.getM_WarehouseTo_ID());
						movimiento.setM_WarehouseTo_ID(movimiento_viejo.getM_WarehouseTo_ID());
						movimiento.save(trx);
						trx.commit();
						movimiento.load(trx);
						String create_returns = "";
						if (movimientot!=0)
							create_returns = "SELECT * FROM M_MOVEMENTLINE WHERE M_MOVEMENT_ID = " + movimientot; 
						else
							create_returns = "SELECT * FROM M_MOVEMENTLINE WHERE M_MOVEMENT_ID = " + movimientom; 
				    	PreparedStatement ps_returns = DB.prepareStatement(create_returns, trx);
				    	ResultSet rs_returns = ps_returns.executeQuery();
				    	
				    	//Se copian cada una de las lineas al movimiento
				    	MMovementLine linea = null;
				    	while (rs_returns.next()) {
				    						    					    		
				    		linea = new MMovementLine(Env.getCtx(), 0, trx);
				    		linea.setM_Movement_ID(movimiento.get_ID());
				    		linea.setDescription(rs_returns.getString("DESCRIPTION"));
				    		linea.setConfirmedQty(rs_returns.getBigDecimal("CONFIRMEDQTY"));
				    		linea.setM_AttributeSetInstanceTo_ID(rs_returns.getInt("M_ATTRIBUTESETINSTANCETO_ID"));
				    		if (movimientot!=0) //MODIFICADO POR GHUCHET
				    			linea.setM_AttributeSetInstance_ID(rs_returns.getInt("M_ATTRIBUTESETINSTANCETO_ID"));
							else
								linea.setM_AttributeSetInstance_ID(rs_returns.getInt("M_ATTRIBUTESETINSTANCE_ID")); 
				    		linea.setM_LocatorTo_ID(movimiento.getM_LocatorTo_ID());				    		
				    		linea.setM_Locator_ID(movimiento.getM_Locator_ID());				    		
				    		linea.setM_Product_ID(rs_returns.getInt("M_PRODUCT_ID"));
				    		linea.setMovementQty(rs_returns.getBigDecimal("MOVEMENTQTY"));
				    		linea.setXX_PriceConsecutive(rs_returns.getInt("XX_PRICECONSECUTIVE"));
				    		if (rs_returns.getInt("XX_VMR_BRAND_ID") != 0)
				    			linea.setXX_VMR_Brand_ID(rs_returns.getInt("XX_VMR_BRAND_ID"));
				    		linea.setXX_VMR_Line_ID(rs_returns.getInt("XX_VMR_Line_ID"));
				    		linea.setXX_VMR_Section_ID(rs_returns.getInt("XX_VMR_Section_ID"));
				    		linea.setXX_SalePrice(rs_returns.getBigDecimal("XX_SalePrice"));				    		
				    		linea.setQtyRequired(rs_returns.getBigDecimal("QtyRequired"));
				    		linea.setC_TaxCategory_ID(rs_returns.getInt("C_TaxCategory_ID"));
				    		linea.setPriceActual(rs_returns.getBigDecimal("PRICEACTUAL"));
				    		linea.setTaxAmt(rs_returns.getBigDecimal("TAXAMT"));				    		
				    		linea.save();
				    	}
				    	rs_returns.close();
				    	ps_returns.close();
				    	
				    	
				    	//Ahora se completa el movimiento				    	
						movimiento.setXX_DispatchDate(movimiento.getUpdated());
						if (movimientot!=0)
							movimiento.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENTIENDA.getValue());
						else
							movimiento.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENCD.getValue());
						
						//Aprueba el movimento para que que pase al locator en transito o chequeado de la tienda
						movimiento.setDocAction(X_M_Movement.DOCACTION_Complete);
						DocumentEngine.processIt(movimiento, MMovement.DOCACTION_Complete);
					    movimiento.save();

					    if (movimiento.getDocStatus().equals(MMovement.DOCSTATUS_Completed)) {
					    	//Modificar el viejo
					    	if (movimientot!=0)
					    		movimiento_viejo.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENTIENDA.getValue());
					    	else
					    		movimiento_viejo.setXX_Status(X_Ref_XX_TransferStatus.APROBADOENCD.getValue());
						    movimiento_viejo.save();
						    
					    } else {					    	
					    	log.saveError("Error", Msg.translate(Env.getCtx(), "XX_ReceptionFail"));
					    }
					} //fin del else
				}
				DB.closeStatement(pstmt2);
				DB.closeResultSet(rs2);
			}catch (Exception e) {
				e.printStackTrace();
				log.log(Level.SEVERE, Msg.translate(Env.getCtx(), "XX_ReceptionFail"), e);
				return false;
			}
			
			String sql = "select XX_VMR_ORDER_ID from XX_VLO_DetailDispatchGuide where XX_VMR_ORDER_ID IS NOT NULL AND XX_VLO_DispatchGuide_ID="+guiaDespacho.get_ID()+" and XX_TYPEDETAILDISPATCHGUIDE = '"+X_Ref_XX_Ref_TypeDispatchGuide.MERCHANDISE_DISTRIBUTION_CENTER.getValue()+"'";
			ps = DB.prepareStatement(sql, null);
			rs = ps.executeQuery();
			while (rs.next())
			{
				System.out.println("	Ped(ID)# "+rs.getInt(1)+" ");						
				/*
				 * MARCO EL CAMPO XX_SYNCHRONIZED EN 'N' DE ESTOS REGISTROS PARA LA EXPORTACION DE DATOS AL AS400
				 */
				String sql10 = "UPDATE XX_VMR_ORDERREQUESTDETAIL SET XX_SYNCHRONIZED = 'N' WHERE " +
							   "XX_VMR_ORDER_ID = " + rs.getInt(1);
				int delete = DB.executeUpdate(trxAux,sql10);
				
				sql10 = "UPDATE XX_VMR_ORDER SET XX_SYNCHRONIZED = 'N' WHERE XX_VMR_ORDER_ID = " + rs.getInt(1);

				delete = delete + DB.executeUpdate(trxAux,sql10);

				if(verificarPedido(rs.getInt(1))){
					pedido = new X_XX_VMR_Order(Env.getCtx(), rs.getInt(1), trx);
					sql = "select M_Locator_ID from M_Locator where M_Warehouse_ID = (select M_Warehouse_ID from M_Warehouse where M_Warehouse_ID = "+pedido.getM_Warehouse_ID()+") and ISDEFAULT = 'Y'";
					ps1 = DB.prepareStatement(sql, trx);
					rs1 = ps1.executeQuery();
					if (rs1.next())
						locatorHacia = rs1.getInt(1);
					else{
						rs1.close();
						ps1.close();
						return false;
					}
					
					sql = "select M_Locator_ID from M_Locator where M_Warehouse_ID = "+pedido.getM_Warehouse_ID()+" and upper(VALUE) like '%TRANSITO%'";
					ps1 = DB.prepareStatement(sql, trx);
					rs1 = ps1.executeQuery();
					if (rs1.next())
						locatorDesde = rs1.getInt(1);
					else{
						rs1.close();
						ps1.close();
						return false;
					}
					
					
					new Utilities().ActuaInvPedido(pedido, 1, locatorDesde, locatorHacia, trx);
					
					rs1.close();
					ps1.close();
					
					pedido.setXX_OrderRequestStatus(X_Ref_XX_VMR_OrderStatus.EN_TIENDA.getValue());
					// agregado por vlomonaco
					// actualiza la fecha del estado cuando se pone en tienda
					Calendar cal = Calendar.getInstance();
				    Timestamp t = new Timestamp(cal.getTime().getTime());
				    
				    if(pedido.getXX_DateStatusOnStore()==null)
				    	pedido.setXX_DateStatusOnStore(t);
					// fin vlomonaco					
					pedido.save();
				}
			}
			rs.close();
			ps.close();
		}
		
		guiaDespacho.setXX_DispatchGuideStatus(X_Ref_XX_Ref_DispatchGuideStatus.EN_TIENDA.getValue());
		guiaDespacho.setXX_ReceiveDispatchGuide("N");
		
		// Agregado por VLOMONACO
		// Actualiza la fecha en que se cambio el estado
		Calendar cal = Calendar.getInstance();
	    Timestamp t = new Timestamp(cal.getTime().getTime());
		guiaDespacho.setXX_DateStatusOnStore(t);
		// fin VLOMONACO		
		
		guiaDespacho.save();
		
		return true;
	}
	
	
	private boolean verificarPedido(int pedido) {
		String sql = "select XX_PACKAGEQUANTITY - " +
								       	"(select COALESCE (sum(dgd.XX_PACKAGESSENT), 0) " +
								       	"from XX_VLO_DISPATCHGUIDE DG, XX_VLO_DETAILDISPATCHGUIDE dgd " +
								       	"where DG.XX_VLO_DISPATCHGUIDE_ID = dgd.XX_VLO_DISPATCHGUIDE_ID " +
								       	"and ((DG.XX_DISPATCHGUIDESTATUS = '"+X_Ref_XX_Ref_DispatchGuideStatus.EN_TRÁNSITO.getValue()+"' AND dgd.XX_VMR_ORDER_ID = "+pedido+") " +
								       	"or (DG.XX_DISPATCHGUIDESTATUS = '"+X_Ref_XX_Ref_DispatchGuideStatus.EN_TIENDA.getValue()+"' AND dgd.XX_VMR_ORDER_ID = "+pedido+"))) as faltanBultos " +
       	"from XX_VMR_ORDER where XX_VMR_ORDER_ID = "+pedido+"";
       
       	PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
			  if(rs.getInt("faltanBultos")> 0)
				return false;
			  else
				return true;
			}

		}catch (Exception e) {
			return false;
		}finally{
			
			try {rs.close();} catch (SQLException e1){e1.printStackTrace();}
			try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		}

		return true;
	}
	
	public int generateInvoice(Trx trx, MMovement oldMovement, int product, X_XX_VLO_ReturnOfProduct returnOfProduct){
		
		//Genera la Factura de la devolución
		MInvoice invoice = new MInvoice(Env.getCtx(),0,null);
		MInvoiceLine invoiceLine = null;
		MProduct producto = null;
		invoice.setM_Warehouse_ID(oldMovement.getM_WarehouseFrom_ID());
		invoice.setXX_VMR_Department_ID(oldMovement.getXX_VMR_Department_ID());
		
		invoice.setC_DocTypeTarget_ID(Env.getCtx().getContextAsInt("#XX_L_C_DOCTYPECREDIT_ID"));		
		MWarehouse store = new MWarehouse( Env.getCtx(), oldMovement.getM_WarehouseFrom_ID(), null);
		//Jessica Mendoza
		//La modificación del nombre de la descripción, implica cambios en las consultas de: planificación de pago, 
		//movimiento de cuentas por pagar, y en los reportes: definitivo de la planificación de pago, 
		//cierre consolidado y detallado de las cuentas por pagar, etc...
		//Fin comentario - Jessica Mendoza
		invoice.setDocumentNo(returnOfProduct.get_ValueAsString("Value"));
		invoice.setDescription("Devolución N° "+ returnOfProduct.get_ValueAsString("Value") +" de Tienda " + store.getValue() + " (" + store.getName() + ")");
		
		Date utilDate = new Date();
		long lnMilisegundos = utilDate.getTime();
		Timestamp actualDate = new Timestamp(lnMilisegundos);
		
		invoice.setXX_DueDate(actualDate);
		invoice.setXX_DatePaid(actualDate);	
		invoice.setXX_InvoicingStatusContract("AP");
		invoice.setDateInvoiced(actualDate);
		invoice.setDateAcct(actualDate);
		invoice.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCONTROL_ID")); 
		
		MAttributeSetInstance atributo = null;
			
		//Obtengo el Proveedor del Movimiento
		String SQL = "Select pro.c_bpartner_id, part.PAYMENTRULEPO,part.PO_PaymentTerm_ID, loc.C_BPartner_Location_ID, use.ad_user_id " +
					 "from m_product pro, c_bpartner part, C_BPartner_Location loc, AD_User use " +
		             "where " +
					 "pro.m_product_id="+ product +" and " +
		             "pro.c_bpartner_id=part.c_bpartner_id and " +
		             "loc.c_bpartner_id=part.c_bpartner_id and " +
		             "use.c_bpartner_id=part.c_bpartner_id and " +
					 "use.xx_contacttype=" + X_Ref_XX_Ref_ContactType.SALES.getValue(); 
    	
		PreparedStatement psAux = DB.prepareStatement(SQL, trx);
		ResultSet rsAux = null;
		
		try {
			rsAux = psAux.executeQuery();

	    	if (rsAux.next()) {
	    						    				
	    		invoice.setC_BPartner_ID(rsAux.getInt("C_BPartner_ID"));
	    		invoice.setC_BPartner_Location_ID(rsAux.getInt("C_BPartner_Location_ID"));
	    		invoice.setC_PaymentTerm_ID(rsAux.getInt("PO_PaymentTerm_ID"));
	    		invoice.setAD_User_ID(rsAux.getInt("ad_user_id"));
	    		invoice.setSalesRep_ID(rsAux.getInt("ad_user_id"));
	    		invoice.set_ValueNoCheck("PaymentRule",rsAux.getString("PAYMENTRULEPO"));
	    		
	    	}else{
	    		System.out.println("SQL no retorna rows");
	    		return 0;
	    	}
	    	
	    	rsAux.close();
	    	psAux.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				rsAux.close();
				psAux.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return 0;
		}
		
		int ref_invoice = getRefInvoice(oldMovement.get_ID());
		
		if(ref_invoice>0)
			invoice.setRef_Invoice_ID(ref_invoice);
		
    	invoice.setC_Currency_ID(205); //Bolivares
    	invoice.setIsApproved (false);
		invoice.setIsPaid (false);
		invoice.setIsInDispute(false);
		invoice.setIsTransferred (false);
		invoice.setPosted (false);
		invoice.setProcessed (false);
		invoice.setDatePrinted(null);
		invoice.setIsPrinted (false);
		invoice.setIsSOTrx(false);
		invoice.setXX_InvoiceType("I");
		invoice.save();
		
		/****Jessica Mendoza****/
		/****Setea la fecha de vencimiento, fecha de pago y tipo de factura, 
		 * segun los datos de la factura original correspondiente a la misma orden de compra ****/
		if(invoice.getC_Order_ID()>0){
			invoice.setXX_DueDate(searchfechaVenc(invoice.get_ID(),
					Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")));
			invoice.setXX_DatePaid(searchfechaVenc(invoice.get_ID(),
					Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")));
		 
				invoice.setXX_InvoiceType(searchPOType(invoice.get_ID(), null));
		}
		/****Se cambia el status de documento por completado, para 
		 * que se vean reflejados en la planificacion de la semana de pago****/
		invoice.setXX_AccountPayableStatus("A");
		invoice.save();
		
		//Ahora creo las lineas
		SQL = " select " +
			  " mo.m_Product_id,  mo.XX_QuantityReceived, mo.priceactual, mo.M_ATTRIBUTESETINSTANCE_ID as atributo " +
			  " from m_movementline mo, m_product pro " +
			  " where mo.m_movement_id =" + oldMovement.get_ID() +
			  " and pro.m_product_id=mo.m_product_id ";
		
		PreparedStatement ps = DB.prepareStatement(SQL, trx);
		ResultSet rs = null;
	
		try {
			rs = ps.executeQuery();

	    	while (rs.next()) {
	    		
	    		atributo = new MAttributeSetInstance(Env.getCtx(), rs.getInt("atributo"), null);
	    		Env.getCtx().setContext("#FECHA", atributo.getCreated());
	    		
	    	    invoiceLine = new MInvoiceLine(Env.getCtx(), 0, null);
	    	    producto = new MProduct(ctx, rs.getInt("M_Product_ID"), null);
	    		invoiceLine.setAD_Org_ID(Env.getCtx().getContextAsInt("#XX_L_ORGANIZATIONCONTROL_ID")); 
	    		invoiceLine.setC_Invoice_ID(invoice.get_ID());
	    		invoiceLine.setM_Product_ID(producto.get_ID());
	    		invoiceLine.setXX_Product_ID(producto.get_ID());
	    		invoiceLine.setXX_VMR_VendorProdRef_ID(producto.getXX_VMR_VendorProdRef_ID());
	    		//invoiceLine.setC_Tax_ID(producto.getC_TaxCategory_ID());  		
	    		invoiceLine.setQty((rs.getBigDecimal("XX_QuantityReceived")).multiply(new BigDecimal(-1)));
	    		invoiceLine.setPriceEntered(rs.getBigDecimal("priceactual"));
	    		invoiceLine.setPriceActual(rs.getBigDecimal("priceactual"));
	    		
	    		invoiceLine.setXX_PriceEnteredInvoice(rs.getBigDecimal("priceactual"));
	    		invoiceLine.setXX_PriceActualInvoice(rs.getBigDecimal("priceactual"));
	    		
	    		invoiceLine.save();
	    	}
	    	Env.getCtx().remove("#FECHA");
	    	
		
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			DB.closeResultSet(rs);
			DB.closeStatement(ps);
		}
		
		invoice.setDocAction(MInvoice.DOCACTION_Complete);
	    DocumentEngine.processIt(invoice, MInvoice.DOCACTION_Complete);
		invoice.save();
		
		return invoice.get_ID();
	}
	
	public boolean generatePurchaseBook(Trx trx, MMovement oldMovement, int product, int invoice){
		
		//Genera la Factura de la devolución
		MVCNPurchasesBook pBook = new MVCNPurchasesBook( Env.getCtx(), 0, null);
		
		pBook.setM_Warehouse_ID(oldMovement.getM_WarehouseFrom_ID());
		
		Date utilDate = new Date();
		long lnMilisegundos = utilDate.getTime();
		Timestamp actualDate = new Timestamp(lnMilisegundos);
	
		pBook.setXX_DATE(actualDate);
		pBook.setXX_isManual(false);
		pBook.setXX_DocumentNo_ID(invoice); //se asocia la devolucion (invoice)
		
		//Obtengo el Proveedor del Movimiento
		String SQL = "Select pro.c_bpartner_id, part.PAYMENTRULEPO,part.PO_PaymentTerm_ID, loc.C_BPartner_Location_ID, use.ad_user_id " +
					 "from m_product pro, c_bpartner part, C_BPartner_Location loc, AD_User use " +
		             "where " +
					 "pro.m_product_id="+ product +" and " +
		             "pro.c_bpartner_id=part.c_bpartner_id and " +
		             "loc.c_bpartner_id=part.c_bpartner_id and " +
		             "use.c_bpartner_id=part.c_bpartner_id and " +
					 "use.xx_contacttype=" + X_Ref_XX_Ref_ContactType.SALES.getValue(); 
    	
		PreparedStatement psAux = DB.prepareStatement(SQL, trx);
		ResultSet rsAux = null;
		
		try {
			rsAux = psAux.executeQuery();

	    	if (rsAux.next()) {				    				
	    		pBook.setC_BPartner_ID(rsAux.getInt("C_BPartner_ID"));
	    	}else{
	    		System.out.println("SQL no retorna rows");
	    		return false;
	    	}
	    	
	    	rsAux.close();
	    	psAux.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				rsAux.close();
				psAux.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		
		//inicializo los montos en 0
		pBook.setXX_ExemptBase(new BigDecimal(0));
		pBook.setXX_TaxableBase(new BigDecimal(0));
		pBook.setXX_TaxAmount(new BigDecimal(0));
		
		//Calculamos el monto exento		
		SQL = "select " +
			  "(Case when sum(mo.priceactual) is null then 0 " +
			  "else sum(mo.xx_saleprice) " +
		      "end) exempprice " +
		      "from m_movementline mo, m_product pro " +
		      "where mo.m_movement_id = " + oldMovement.get_ID()+ " " +
		      "and pro.m_product_id = mo.m_product_id " +
		      "and pro.C_TaxCategory_ID = " + Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID");
		
		PreparedStatement ps1Aux = DB.prepareStatement(SQL, trx);
		ResultSet rs1Aux = null;
		
		try {
			rs1Aux = ps1Aux.executeQuery();

	    	if (rs1Aux.next()) {				    				
	    		pBook.setXX_ExemptBase(rs1Aux.getBigDecimal("exempprice").multiply(new BigDecimal(-1)));
	    	}
	    	
	    	rs1Aux.close();
	    	ps1Aux.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				rs1Aux.close();
				ps1Aux.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
			
		//Calculamos el monto con impuesto y el impuesto
		//OJO EL IMPUESTO SE CALCULA 
		
		SQL = "Select sum(mo.priceactual * mo.XX_QuantityReceived) saleprice, " +
				"sum(((mo.priceactual * mo.XX_QuantityReceived) * " +
				"(select rate from c_tax where ValidFrom = " +
				"(select max(validfrom) " +
				"from c_tax " +
				"where VALIDFROM <= pro.created " +
				"AND C_TAXCATEGORY_ID = pro.C_TaxCategory_ID " +
				"group by C_TaxCategory_ID))/100)) taxableamount " +
				"from m_movementline mo, m_product pro, M_AttributeSetInstance att  " +
				"where mo.m_movement_id = " + oldMovement.get_ID()+ " and pro.m_product_id = mo.m_product_id and " +
				"pro.C_TaxCategory_ID <> " + Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID") + " and att.M_AttributeSetInstance_ID = mo.M_AttributeSetInstance_ID";
		
		

		PreparedStatement ps2Aux = DB.prepareStatement(SQL, trx);
		ResultSet rs2Aux = null;
		
		try {
			rs2Aux = ps2Aux.executeQuery();
	
		  	if (rs2Aux.next()) {				    				
		  		pBook.setXX_TaxableBase(rs2Aux.getBigDecimal("saleprice").multiply(new BigDecimal(-1)));
		  		pBook.setXX_TaxAmount(rs2Aux.getBigDecimal("taxableamount").multiply(new BigDecimal(-1)));
		  	}
		  	
		  	rs2Aux.close();
		  	ps2Aux.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				rs2Aux.close();
				ps2Aux.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
		
		//si todos los productos son exentos de iva
		if(pBook.getXX_ExemptBase().compareTo(BigDecimal.ZERO)!=0 && pBook.getXX_TaxableBase().compareTo(BigDecimal.ZERO)==0){
			pBook.setC_Tax_ID(Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID"));
		}
		else if(pBook.getXX_TaxableBase().compareTo(BigDecimal.ZERO)!=0){
			
			int tax = getTaxForPBook(trx, oldMovement);
			if(tax!=0){
				pBook.setC_Tax_ID(tax);
			}
		}
		
		//Sumo la base de impuesto + la base sin impuesto + el monto con impuesto
		pBook.setXX_TotalInvCost((pBook.getXX_ExemptBase().add(pBook.getXX_TaxableBase())).add(pBook.getXX_TaxAmount()));
		
		if(pBook.save()){
			//System.out.println("ID P BOOK: "+pBook.get_ID());
			return true;
		}
		else
			return false;   
	}
	
	private int getTaxForPBook(Trx trx, MMovement oldMovement){
		
		String SQL = "Select  " +
			  "(select c_tax_id from c_tax where ValidFrom = " +
			  "(select max(validfrom) " +
			  "from c_tax " +
			  "where VALIDFROM <= att.created " +
			  "AND C_TAXCATEGORY_ID = pro.C_TaxCategory_ID " + 
			  "group by C_TaxCategory_ID)) c_tax_id " +
		      "from m_movementline mo, m_product pro, XX_VMR_PRICECONSECUTIVE pric, M_ATTRIBUTESETINSTANCE att " +
		      "where mo.m_movement_id = " + oldMovement.get_ID()+ " " +
		      "and pro.m_product_id = mo.m_product_id " +
		      "and pro.C_TaxCategory_ID <> " + Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID") + " " +
			  "and pric.XX_PRICECONSECUTIVE = mo.XX_PRICECONSECUTIVE and pric.m_product_id = mo.m_product_id " +
			  "and att.M_ATTRIBUTESETINSTANCE_ID=pric.M_ATTRIBUTESETINSTANCE_ID";

		PreparedStatement psAux = DB.prepareStatement(SQL, trx);
		ResultSet rsAux = null;
		
		int tax=0;
		try {
			rsAux = psAux.executeQuery();
	
			//TODO Hay que validar que todos los tax sean iguales, faltó eso
		  	while (rsAux.next()) {				    				
		  		tax = rsAux.getInt("C_Tax_ID");
		  	}
		  	
		  	rsAux.close();
		  	psAux.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				rsAux.close();
				psAux.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			return 0;
		}
		
		return tax;
	}
	
	/** Imprime la etiqueta de un producto dado 
	 * Si se imprime desde un detalle de pedido no hace falta el valor de warehouse_id 
	 * Si se imprime desde producto no es necesario detail
	 * 
	 * */
	public static void print_labels (
			PrintService psZebra,
			KeyNamePair knp_product,
			KeyNamePair knp_att,			
			String correlative,
			X_XX_VMR_OrderRequestDetail detail,
			int cantidadEtiquetas,
			int warehouse_id,
			boolean glued) {
		try {  									
			MProduct producto = new MProduct(Env.getCtx(), knp_product.getKey(), null);
			
			/*
			 * Caracteristica larga
			 * */
			X_XX_VMR_LongCharacteristic caracLarga = new X_XX_VMR_LongCharacteristic(Env.getCtx(), producto.getXX_VMR_LongCharacteristic_ID(), null);
			
			
			X_XX_VMR_Order header = null;
			BigDecimal SalePricePlusTax = Env.ZERO;
			MWarehouse warehouse = null;
			Date fecha_usada = null;
			String week_created = null;
			BigDecimal taxAux = BigDecimal.ZERO;
			
			if (detail != null ) { 
				
				SalePricePlusTax = detail.getXX_SalePricePlusTax().setScale(2, RoundingMode.HALF_EVEN);
				
				header = new X_XX_VMR_Order(Env.getCtx(), detail.getXX_VMR_Order_ID(), null);
				
				//ADD GHUCHET PARA ETIQUETAS DE O/C DE DESPACHO DIRECTO
				MOrder order = new MOrder(Env.getCtx(),header.getC_Order_ID(),null);
				if(order.getXX_VLO_TypeDelivery().compareTo(X_Ref_XX_Ref_TypeDelivery.DESPACHO_DIRECTO.getValue())==0){
					fecha_usada = new Date((order.getXX_EstimatedDate()).getTime());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(order.getXX_EstimatedDate());
					//week_created = "" +calendar.get(Calendar.WEEK_OF_YEAR); //COMENTADO GHUCHET
					week_created = "" +getWeekOfYear(calendar); //AGREGADO GHUCHET
				}else {
					fecha_usada = new Date((header.getCreated()).getTime());
					week_created = "" + header.getXX_WeekCreated(); 
				}
				//END AGREGADO GHUCHET
				warehouse = new MWarehouse(Env.getCtx(), header.getM_Warehouse_ID(), null);
				taxAux = getTaxForLabel(detail.getCreated(), detail.getC_TaxCategory_ID());
				
			} else {				
				fecha_usada = Calendar.getInstance().getTime();
				//week_created = "" +  Calendar.getInstance().get(Calendar.WEEK_OF_YEAR); //COMENTADO GHUCHET
				week_created = "" +  getWeekOfYear(Calendar.getInstance()); //AGREGADO GHUCHET
				warehouse = new MWarehouse(Env.getCtx(), warehouse_id, null);
				
				//Usando el priceconsecutive y el precio deberia calcularse el costo
				String sql = " SELECT XX_SALEPRICE " +				
					" FROM XX_VMR_PRICECONSECUTIVE " + 
					" WHERE M_PRODUCT_ID = " + knp_product.getKey() + 
					" AND XX_PRICECONSECUTIVE = " + correlative ;
				try {
					PreparedStatement pstmt = DB.prepareStatement(sql, null);
					ResultSet rs = pstmt.executeQuery();
					if (rs.next()) {						
						SalePricePlusTax = rs.getBigDecimal(2);						
					} 
					rs.close();
					pstmt.close();
				}catch (Exception e) {}
								
				//Calcular el impuesto		
				Integer TaxCategory_ID = producto.getC_TaxCategory_ID();		
				sql = "SELECT rate"
							+ " FROM C_Tax"
							+ " WHERE ValidFrom="			
							+ " (SELECT MAX(ValidFrom)"											
							+ " FROM C_Tax"
							+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+")";	
				PreparedStatement prst = DB.prepareStatement(sql,null);
				BigDecimal Tax = new BigDecimal(1);
				try {
					ResultSet rs = prst.executeQuery();
					if (rs.next()){
						Tax = rs.getBigDecimal("rate");
					}
					rs.close();
					prst.close();
				} catch (SQLException e){}
				
				SalePricePlusTax = SalePricePlusTax.add(SalePricePlusTax.multiply(Tax.multiply(new BigDecimal(0.01))));
				SalePricePlusTax = SalePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
				
				//SalePricePlusTax= getpriceBECO(SalePricePlusTax, producto);
			}

			//Categoria del impuesto
			boolean exento = false;
			if (producto.getC_TaxCategory_ID() == Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID")) {
				exento = true;				
			}
			
			String name = producto.getName();
			DateFormat fechaformato = new SimpleDateFormat("MM yyyy");
			DecimalFormat formato = new DecimalFormat("###,###,###.00");
			
			X_XX_VMR_Department dep = new X_XX_VMR_Department(Env.getCtx(), producto.getXX_VMR_Department_ID() , null);			
			String departmentCode = dep.getValue();
					
			X_XX_VMR_Line lin = new X_XX_VMR_Line(Env.getCtx(), producto.getXX_VMR_Line_ID(), null);
			String lineCode = lin.getValue();
			
			X_XX_VMR_Section sec = new X_XX_VMR_Section(Env.getCtx(), producto.getXX_VMR_Section_ID(), null);
			String seccionCode = sec.getValue();  
			
			String precioFuerte = formato.format(SalePricePlusTax);
			while (precioFuerte.length() < 14) {
				precioFuerte = " " + precioFuerte;
			}

			String product_plus_correlative = producto.getValue() + correlative;
			while (product_plus_correlative.length() < 12) {
				product_plus_correlative = "0" + product_plus_correlative;
			}
			String s = "";
			DocPrintJob job = psZebra.createPrintJob();			
			if (glued) {
				//Glued labels
				s =  
				"^XA^PRD^XZ" +
				"^XA^JMB^" + 
				"^LH00,02^FS" +
				"^FO20,05^BE,25,N^BY2.48,50,^FD" + product_plus_correlative + "^FS" +
				"^FO20,45^AA,13,07^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative + "       ^FS" +
				"^FO20,55^AA,13,07^FD" + (name.length() > 30 ? name.substring(0, 29) : name) + "^FS";
				
				if(!knp_att.getName().isEmpty()){
					s=s+"^FO20,65^AA,13,07^FD" + (knp_att.getName().length() > 30 ? knp_att.getName().substring(0,29) : knp_att.getName()) + "^FS";
				}else if (caracLarga != null && caracLarga.getName() != null && !caracLarga.getName().isEmpty()){
					s=s+"^FO20,65^AA,13,07^FD" + (caracLarga.getName().length() > 30 ? caracLarga.getName().substring(0,29) : caracLarga.getName()) + "^FS";
				}
				s=s+"^FO20,82^AA,13,07^FDPRECIO BS^FS" +
				"^FO20,110^AA,10,07^CI10^FD" + week_created + " "+ fechaformato.format(fecha_usada) + "^FS" +
				"^FO120,105^AD,12,15^FDBECO^FS" +
				"^FO45,75^AO,15,10^CI10^FD" + precioFuerte + "^FS" +
				"^FO20,120^AO,13,07^FDRIF J-00046517-7 " + (exento ? "" : "Incluye IVA "+ taxAux +"%") + "^FS" +
				"^PQ" + cantidadEtiquetas + "^FS" + 
				"^XZ" +
				"^XA^PRD^XZ" +
				"^XA^JMB^" +
				"^LH00,15^FS" + 
				"^FO2,0^A0,38,20^FD*CONTROL*     " + week_created + " "+ fechaformato.format(fecha_usada) +  "^FS" +
				"^FO28,33^A0,15,14^FDN/P:" + header.getXX_OrderBecoCorrelative() +"      TDA:" + warehouse.getValue() + 	"^FS" +	
				//"^FO28,33^A0,15,14^FDN/P: R0011000000     TDA:" + warehouse.getValue() + 	"^FS" +	
				"^FO28,48^A0,15,14^FDCANT:     " + cantidadEtiquetas + 	"      PRECIO" + precioFuerte +	"^FS" +
				"^FO28,63^A0,15,14^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"-"+ product_plus_correlative + "^FS" +
				"^FO28,78^A0,15,10^FD" + (name.length() > 25 ? name.substring(0, 24) : name) + "^FS" +
				"^FO28,93^A0,15,14^FD" + (knp_att.getName().length() > 50 ? knp_att.getName().substring(0,49) : knp_att.getName()) + "^FS" +
				"^PQ1^FS" +
				"^XZ";				
			} else {
				//Hanging labels
				s = 
				"^XA^PRD^FS" +
				"^LH07,38^FS" +
				"^FO120,30^BER,25,N^BY2.48,50,^FD" + product_plus_correlative + "^FS" +
				"^FO90,35^AAR,13,07^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative + "^FS" + 
				"^FO80,35^AAR,13,07^FD" + (name.length() > 30 ? name.substring(0, 29) : name) +	"^FS";
				if(!knp_att.getName().isEmpty()){
					s=s+"^FO70,35^AAR,13,07^FD" + (knp_att.getName().length() > 30 ? knp_att.getName().substring(0,29) : knp_att.getName()) + "^FS";	
				}else if (caracLarga != null && caracLarga.getName() != null && !caracLarga.getName().isEmpty()){
					s=s+"^FO70,35^AAR,13,07^FD" + (caracLarga.getName().length() > 30 ? caracLarga.getName().substring(0,29) : caracLarga.getName()) + "^FS";
				}
				
				s=s+"^FO52,35^AAR,13,07^FDPRECIO BS^FS" +
				"^FO20,035^AAR,10,07^CI10^FD" + week_created + " "+ fechaformato.format(fecha_usada) + "^FS" +					
				"^FO18,128^ADR,12,15^FDBECO^FS" +
				"^FO50,55^AOR,15,10^CI10^FD" + precioFuerte + "^FS" +
				"^FO09,035^AOR,13,07^FDRIF J-00046517-7 " + (exento ? "" : "Incluye IVA "+ taxAux +"%") + "^FS" + 
				"^PQ" + cantidadEtiquetas+  ",0,1,Y^XZ" +
				"^XA^PRD^XZ" +
				//Control Label
				"^XA" +
				"^LH05,25^FS" +
				"^FO102,6^A0R,20,18^FD*CONTROL*           " + week_created + " "+ fechaformato.format(fecha_usada) +"^FS" +
				"^FO80,6^A0R,20,18^FDN/P:" + header.getXX_OrderBecoCorrelative() + "      TDA:" + warehouse.getValue() + "^FS" +
				"^FO60,6^A0R,20,18^FDCANT:     " + cantidadEtiquetas + "      PRECIO" + precioFuerte + "^FS" +					
				"^FO42,6^A0R,20,18^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative + "^FS" +
				"^FO25,6^A0R,20,18^FD" + (name.length() > 30 ? name.substring(0, 29) : name) + "^FS" +
				"^FO05,6^A0R,20,18^FD" + (knp_att.getName().length() > 30 ? knp_att.getName().substring(0,29) : knp_att.getName()) + "^FS" +
				"^PQ1^FS" +
				"^XZ";		
			}
			byte[] by = s.getBytes();   
			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; 
			Doc doc = new SimpleDoc(by, flavor, null);   			   
			job.print(doc, null);  
		}
		catch (Exception e)
		{   
			e.printStackTrace();  
		}		
	}
	

	private static BigDecimal getpriceBECO(BigDecimal price, MProduct product) {
		
		BigDecimal temp = new BigDecimal(price.intValue());


		// Aca busco los ids de los departamentos a los que no le aplica precio Beco 45,46,47
		// y luego la linea 31 en los departamentos 59 y 26
		// y linea 37 del departamento 10

		MVMRDepartment department = new MVMRDepartment(Env.getCtx(), product.getXX_VMR_Department_ID(),null);
		Integer dept = Integer.parseInt(department.getValue());
		Integer line_value = 0;
		Integer section_value = 0;

		if (dept==59 || dept==26 || dept==10){
			MVMRLine line = new MVMRLine(Env.getCtx(), product.getXX_VMR_Line_ID(),null);
			line_value = Integer.parseInt(line.getValue());
			if(dept == 10){
				MVMRSection section = new MVMRSection(Env.getCtx(),  product.getXX_VMR_Section_ID() ,null);
				section_value= Integer.parseInt(section.getValue());
			}
		}

		if (dept!=45 && dept!=46 && dept!=47 && !(dept==59 && (line_value==31 || line_value==32) ) &&
				!(dept==26 && line_value==31) && !(dept==10 && line_value==37 && section_value ==2)){
		
			temp = price.subtract(temp);
			if(temp.compareTo(new BigDecimal(0.99))==0){
				price.add(new BigDecimal(0.01));
			}
			else if (temp.compareTo(new BigDecimal(0.01))==0){
				price.subtract(new BigDecimal(0.01));
			}
			else if (temp.compareTo(new BigDecimal(0.89))==0){
				price.add(new BigDecimal(0.01));
			}
			else if (temp.compareTo(new BigDecimal(0.91))==0){
				price.subtract(new BigDecimal(0.01));
			}
		}
		return price;
	}

	/** Imprime la etiqueta de un producto de una distribucion
	 * @author ghuchet
	 * @param week_created 
	 * @param fecha_usada 
	 * */
	public static void printDistributionLabels (
			PrintService psZebra,
			KeyNamePair knp_product,
			KeyNamePair knp_att,			
			String correlative,
			MVMRDistributionHeader header,
			int cantidadEtiquetas,
			boolean glued, Date fecha_usada, String week_created) {
		try {  									
			MProduct producto = new MProduct(Env.getCtx(), knp_product.getKey(), null);
			
			/*
			 * Caracteristica larga
			 * */
			X_XX_VMR_LongCharacteristic caracLarga = new X_XX_VMR_LongCharacteristic(Env.getCtx(), producto.getXX_VMR_LongCharacteristic_ID(), null);
			
			BigDecimal SalePricePlusTax = Env.ZERO;
			//Usando el priceconsecutive y el precio deberia calcularse el costo
			String sql = " SELECT XX_SALEPRICE " +				
					" FROM XX_VMR_PRICECONSECUTIVE " + 
					" WHERE M_PRODUCT_ID = " + knp_product.getKey() + 
					" AND XX_PRICECONSECUTIVE = " + correlative ;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql, null);
				rs = pstmt.executeQuery();
				if (rs.next()) {						
					SalePricePlusTax = rs.getBigDecimal(1);						
				} 
			}catch (Exception e) {
				
			}finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
							
			//Calcular el impuesto		
			Integer TaxCategory_ID = producto.getC_TaxCategory_ID();		
			sql = "SELECT rate"
						+ " FROM C_Tax"							
						+ " WHERE ValidFrom="			
						+ " (SELECT MAX(ValidFrom)"											
						+ " FROM C_Tax"
						+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+")";	
			PreparedStatement prst = DB.prepareStatement(sql,null);
			BigDecimal Tax = new BigDecimal(1);
			try {
				 rs = prst.executeQuery();
				if (rs.next()){
					Tax = rs.getBigDecimal("rate");
				}
			}catch (Exception e) {
				
			}finally {
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			SalePricePlusTax = SalePricePlusTax.add(SalePricePlusTax.multiply(Tax.multiply(new BigDecimal(0.01))));
			SalePricePlusTax = SalePricePlusTax.setScale (2,BigDecimal.ROUND_HALF_UP);
		

			//Categoria del impuesto
			boolean exento = false;
			if (producto.getC_TaxCategory_ID() == Env.getCtx().getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID")) {
				exento = true;				
			}
			
			String name = producto.getName();
			DateFormat fechaformato = new SimpleDateFormat("MM yyyy");
			DecimalFormat formato = new DecimalFormat("###,###,###.00");
			
			X_XX_VMR_Department dep = new X_XX_VMR_Department(Env.getCtx(), producto.getXX_VMR_Department_ID() , null);			
			String departmentCode = dep.getValue();
					
			X_XX_VMR_Line lin = new X_XX_VMR_Line(Env.getCtx(), producto.getXX_VMR_Line_ID(), null);
			String lineCode = lin.getValue();
			
			X_XX_VMR_Section sec = new X_XX_VMR_Section(Env.getCtx(), producto.getXX_VMR_Section_ID(), null);
			String seccionCode = sec.getValue();  
			
			String precioFuerte = formato.format(SalePricePlusTax);
			while (precioFuerte.length() < 14) {
				precioFuerte = " " + precioFuerte;
			}

			String product_plus_correlative = producto.getValue() + correlative;
			while (product_plus_correlative.length() < 12) {
				product_plus_correlative = "0" + product_plus_correlative;
			}
			String s = "";
			DocPrintJob job = psZebra.createPrintJob();			
			if (glued) {
				//Glued labels
				s =  
				"^XA^PRD^XZ" +
				"^XA^JMB^" + 
				"^LH00,02^FS" +
				"^FO20,05^BE,25,N^BY2.48,50,^FD" + product_plus_correlative + "^FS" +
				"^FO20,45^AA,13,07^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative + "       ^FS" +
				"^FO20,55^AA,13,07^FD" + (name.length() > 30 ? name.substring(0, 29) : name) + "^FS";
				
				if(!knp_att.getName().isEmpty()){
					s=s+"^FO20,65^AA,13,07^FD" + (knp_att.getName().length() > 30 ? knp_att.getName().substring(0,29) : knp_att.getName()) + "^FS";
				}else if (caracLarga != null && caracLarga.getName() != null && !caracLarga.getName().isEmpty()){
					s=s+"^FO20,65^AA,13,07^FD" + (caracLarga.getName().length() > 30 ? caracLarga.getName().substring(0,29) : caracLarga.getName()) + "^FS";
				}
				s=s+"^FO20,82^AA,13,07^FDPRECIO BS^FS" +
				"^FO20,110^AA,10,07^CI10^FD" + week_created + " "+ fechaformato.format(fecha_usada) + "^FS" +
				"^FO120,105^AD,12,15^FDBECO^FS" +
				"^FO45,75^AO,15,10^CI10^FD" + precioFuerte + "^FS" +
				"^FO20,120^AO,13,07^FDRIF J-00046517-7 " + (exento ? "" : "Incluye IVA "+ Tax +"%") + "^FS" +
				"^PQ" + cantidadEtiquetas + "^FS" + 
				"^XZ" +
				"^XA^PRD^XZ" +
				"^XA^JMB^" +
				"^LH00,15^FS" + 
				"^FO2,0^A0,38,20^FD*CONTROL*     " + week_created + " "+ fechaformato.format(fecha_usada) +  "^FS" +
				"^FO28,33^A0,15,14^FDN/D:" + header.get_ID() + 	"^FS" +	
				//"^FO28,33^A0,15,14^FDN/P: R0011000000     TDA:" + warehouse.getValue() + 	"^FS" +	
				"^FO28,48^A0,15,14^FDCANT:     " + cantidadEtiquetas + 	"      PRECIO" + precioFuerte +	"^FS" +
				"^FO28,63^A0,15,14^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"-"+ product_plus_correlative + "^FS" +
				"^FO28,78^A0,15,10^FD" + (name.length() > 25 ? name.substring(0, 24) : name) + "^FS" +
				"^FO28,93^A0,15,14^FD" + (knp_att.getName().length() > 50 ? knp_att.getName().substring(0,49) : knp_att.getName()) + "^FS" +
				"^PQ1^FS" +
				"^XZ";				
			} else {
				//Hanging labels
				s = 
				"^XA^PRD^FS" +
				"^LH07,38^FS" +
				"^FO120,30^BER,25,N^BY2.48,50,^FD" + product_plus_correlative + "^FS" +
				"^FO90,35^AAR,13,07^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative + "^FS" + 
				"^FO80,35^AAR,13,07^FD" + (name.length() > 30 ? name.substring(0, 29) : name) +	"^FS";
				if(!knp_att.getName().isEmpty()){
					s=s+"^FO70,35^AAR,13,07^FD" + (knp_att.getName().length() > 30 ? knp_att.getName().substring(0,29) : knp_att.getName()) + "^FS";	
				}else if (caracLarga != null && caracLarga.getName() != null && !caracLarga.getName().isEmpty()){
					s=s+"^FO70,35^AAR,13,07^FD" + (caracLarga.getName().length() > 30 ? caracLarga.getName().substring(0,29) : caracLarga.getName()) + "^FS";
				}
				
				s=s+"^FO52,35^AAR,13,07^FDPRECIO BS^FS" +
				"^FO20,035^AAR,10,07^CI10^FD" + week_created + " "+ fechaformato.format(fecha_usada) + "^FS" +					
				"^FO18,128^ADR,12,15^FDBECO^FS" +
				"^FO50,55^AOR,15,10^CI10^FD" + precioFuerte + "^FS" +
				"^FO09,035^AOR,13,07^FDRIF J-00046517-7 " + (exento ? "" : "Incluye IVA "+ Tax +"%") + "^FS" + 
				"^PQ" + cantidadEtiquetas+  ",0,1,Y^XZ" +
				"^XA^PRD^XZ" +
				//Control Label
				"^XA" +
				"^LH05,25^FS" +
				"^FO102,6^A0R,20,18^FD*CONTROL*           " + week_created + " "+ fechaformato.format(fecha_usada) +"^FS" +
				"^FO80,6^A0R,20,18^FDN/P:" + header.get_ID() + "^FS" +
				"^FO60,6^A0R,20,18^FDCANT:     " + cantidadEtiquetas + "      PRECIO" + precioFuerte + "^FS" +					
				"^FO42,6^A0R,20,18^FD" + departmentCode+"-"+lineCode+"-"+seccionCode+"- "+ product_plus_correlative + "^FS" +
				"^FO25,6^A0R,20,18^FD" + (name.length() > 30 ? name.substring(0, 29) : name) + "^FS" +
				"^FO05,6^A0R,20,18^FD" + (knp_att.getName().length() > 30 ? knp_att.getName().substring(0,29) : knp_att.getName()) + "^FS" +
				"^PQ1^FS" +
				"^XZ";		
			}
			byte[] by = s.getBytes();   
			DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; 
			Doc doc = new SimpleDoc(by, flavor, null);   			   
			job.print(doc, null);  
		}
		catch (Exception e)
		{   
			e.printStackTrace();  
		}		
	}
	
	
	public static BigDecimal getTaxForLabel(Timestamp date, int C_TaxCategory_ID){
		
		BigDecimal tax = BigDecimal.ZERO;
		
		Integer TaxCategory_ID = C_TaxCategory_ID;		
		String sql = "SELECT rate"
					+ " FROM C_Tax"
					+ " WHERE ValidFrom="			
					+ " (SELECT MAX(ValidFrom)"											
					+ " FROM C_Tax"
					+ " WHERE C_TaxCategory_ID=" + TaxCategory_ID+ " "
					+ "AND ValidFrom <= TO_DATE('"+date.toString().substring(0,10)+"','YYYY-MM-DD'))";	
		
		PreparedStatement prst = DB.prepareStatement(sql,null);
		try {
			ResultSet rs = prst.executeQuery();
			if (rs.next()){
				tax = rs.getBigDecimal("rate");
			}
			rs.close();
			prst.close();
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		
		return tax;
	}
	
	public static int getWeekOfYear(Calendar date){
		
		int week=0;
		date.setFirstDayOfWeek(Calendar.MONDAY);
		date.setMinimalDaysInFirstWeek(4);
		week = date.get(Calendar.WEEK_OF_YEAR);
		System.out.println(week);
		return  week;
		
	}
	private void eliminarFactura(int id, Trx transaction)
	{

		String delMatchInv = "Delete from m_matchinv where c_invoiceline_id in (select c_invoiceline_id from c_invoiceline where c_invoice_id="+id+") ";
		DB.executeUpdate(transaction, delMatchInv);
		
		String delMatchPO = "Delete from m_matchPO where c_invoiceline_id in (select c_invoiceline_id from c_invoiceline where c_invoice_id="+id+") ";
		DB.executeUpdate(transaction, delMatchPO);
		
		String delinvline = "delete from c_invoiceline where c_invoice_id="+id+" and processed='N'";
		DB.executeUpdate(transaction, delinvline);
		
		String delinvoice = "Delete From C_Invoice where C_Invoice_ID = "+id;
		DB.executeUpdate(transaction, delinvoice);
		
	}
	
	/** Verifica que el usuario tenga rol de planificacion - 
	 * Utilizada en completar movimiento
	 * */
	public static boolean esRolDePlanificacion(int rolActual) {		
 
		//Si es planificador
		if (rolActual == Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULER_ID")) { 
			return true;
		}
		
		//Si es jefe de planificación 
		if (rolActual == Env.getCtx().getContextAsInt("#XX_L_ROLESCHEDULEMANAGER_ID")) { 
			return true;
		}
		return false;
	}
	
	/** Verifica que el usuario tenga rol de Compras
	 * Utilizada en completar movimiento
	 * */
	public static boolean esRolDeCompras(int rolActual) {
		
		//Si es comprador
		if (rolActual == Env.getCtx().getContextAsInt("#XX_L_ROLEBUYER_ID")) {
			return true;				
		} 
		
		//Si es jefe de categoria
		if (rolActual == Env.getCtx().getContextAsInt("#XX_L_ROLECATEGORYMANAGER_ID")) {
			return true;
		}
		
		return false;
	}
	
	
	/** Verifica que el usuario tenga rol de tienda
	 * Utilizada en completar movimiento
	 * */
	public static boolean esRolDeTienda (int rolActual) {
			
		//Si es Asesor de Especialista de Depósito
		if (rolActual == Env.getCtx().getContextAsInt("#XX_L_STORAGEESPECIALIST_ID")) {
			return true;
		}
		
		//Gerente de Area de Mercadeo
		if (rolActual == Env.getCtx().getContextAsInt("#XX_L_AREAMARKETINGMANAGER_ID")) {
			return true;
		}
		
		//Si es Gerente de Area Administracion
		if (rolActual == Env.getCtx().getContextAsInt("#XX_L_AREAADMINMANAGER_ID")) {
			return true;
		}
		
		//Si es Gerente de Tienda
		if (rolActual == Env.getCtx().getContextAsInt("#XX_L_STOREMANAGER_ID")) {
			return true;	
		}
		
		return false;		
	}
	
	
	
	/** Verifica que el movimiento sea traspaso
	 * Utilizada en completar movimiento
	 * */
	public static boolean esTraspaso (int tipoMovimiento) {
		if (tipoMovimiento == Env.getCtx().getContextAsInt("#XX_L_DOCTYPETRANSFER_ID")) {
			return true;
		}
		return false;
	}
	
	/** Verifica que el movimiento sea entre Centros de distrribucion
	 * Utilizada en completar movimiento
	 * */
	public static boolean esMovimientoCD (int tipoMovimiento) {
		if (tipoMovimiento == 1000335) {
			return true;
		}
		return false;
	}
	
	/** Verifica que el movimiento sea una devolucion
	 * Utilizada en completar movimiento
	 * */
	public static boolean esDevolucion (int tipoMovimiento) {
		if (tipoMovimiento == Env.getCtx().getContextAsInt("XX_L_DOCTYPERETURN_ID")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Encuentra la cabecera de distribucion no anulada asociada a la orden de compra
	 * Retorna 0 sino la encuentra
	 */
	public static int getDistributionHeader(int orderID, Trx trx) {

		String distribution =
			" SELECT XX_VMR_DISTRIBUTIONHEADER_ID FROM XX_VMR_DISTRIBUTIONHEADER " +
			" WHERE C_ORDER_ID = " + orderID + " AND XX_DISTRIBUTIONSTATUS != '"
			+ X_Ref_XX_DistributionStatus.ANULADA.getValue()
			+ "'";
		PreparedStatement ps = DB.prepareStatement(distribution, trx);
		int found_header = 0;
		try {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				found_header = rs.getInt(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_DatabaseAccessError");
		}
		return found_header;
	}
	
	/**
	 * Encuentra la cabecera de distribucion asociada a la orden de compra
	 * */
	public static int approvedCheckedHeader(int c_order_id) {

		String distribution =
			" SELECT XX_VMR_DISTRIBUTIONHEADER_ID FROM XX_VMR_DISTRIBUTIONHEADER " +
			" WHERE C_ORDER_ID = " + c_order_id + " AND XX_DISTRIBUTIONSTATUS = '"
			+ X_Ref_XX_DistributionStatus.APROBADA__PENDIENTE_POR_CHEQUEO_DE_LA_OC.getValue() + "'";

		PreparedStatement ps = DB.prepareStatement(distribution, null);
		int found_header = 0;
		try {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				found_header = rs.getInt(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_DatabaseAccessError");
		}
		return found_header;
				
	}
	
	/**
	 * Indica el numero de pedidos asociados que tiene una distribucion
	 * */
	public static int associatedPlacedOrders(int xx_mr_distributionheader_id) {

		String distribution =
			" SELECT COUNT(*) FROM XX_VMR_ORDER " +
			" WHERE XX_VMR_DISTRIBUTIONHEADER_ID = " + xx_mr_distributionheader_id;

		PreparedStatement ps = DB.prepareStatement(distribution, null);
		int found_headers = 0;
		try {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				found_headers = rs.getInt(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			ADialog.error(EnvConstants.WINDOW_INFO, new Container(), "XX_DatabaseAccessError");
		}
		return found_headers ;				
	}
	
	/** Indica si una distribucion tiene pedidos asociados*/
	public static boolean hasAssociatedPlacedOrders (int xx_mr_distributionheader_id) {
		return (associatedPlacedOrders(xx_mr_distributionheader_id) > 0);
	}
	
	/*
	 * Genera alerta Asignar Precio de Venta y Pedido Pendiente por Etiquetar segun la distribucion
	 */
	public static void generatedAlert(Integer distribucion)
	{
		MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID"), distribucion); 
		mpi.save();
			
		ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_GENERATEDALERTCT_ID")); 
		pi.setClassName(""); 
		pi.setTitle(""); 
			
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
		
	}//fin generatedAlert
	
	/*
	 * Cierra alerta Asignar Precio de Venta segun la tarea correspondiente a la distribucion
	 */
	public static void closeAlert(Integer task)
	{
		MPInstance mpi = new MPInstance( Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID"), task); 
		mpi.save();
			
		ProcessInfo pi = new ProcessInfo("", Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
		pi.setRecord_ID(mpi.getRecord_ID());
		pi.setAD_PInstance_ID(mpi.get_ID());
		pi.setAD_Process_ID(Env.getCtx().getContextAsInt("#XX_L_CLOSEALERTCT_ID")); 
		pi.setClassName(""); 
		pi.setTitle(""); 
			
		ProcessCtl pc = new ProcessCtl(null ,pi,null); 
		pc.start();
	}//fin closeAlert
	//RArvelo

	 /*
	 *	Obtengo el ID de la tarea critica segun la DistributionHeader
	 */
	public static Integer getCriticalTaskForClose(Integer distriHeader){
		
		Integer criticalTask=0;
		String SQL = "SELECT XX_VMR_CriticalTaskForClose_ID FROM XX_VMR_CriticalTaskForClose "
			   + "WHERE XX_ActualRecord_ID="+distriHeader;
	try
	{
		PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
	    ResultSet rs = pstmt.executeQuery();			
	    
		while (rs.next())
		{				
			criticalTask = rs.getInt("XX_VMR_CriticalTaskForClose_ID");
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return criticalTask;
	}//fin getCriticalTaskForClose
	
	/**
	 * Method that generate the Debt or Credit Notify Report
	 * @return attachment
	 */
	public File reportDebCred(MInvoice invoice, boolean show){
		
		MPrintFormat f = null;
		int c_Invoice_ID = invoice.getC_Invoice_ID();
		log.info("C_Invoice_ID=" + c_Invoice_ID);
		if (c_Invoice_ID < 1)
			throw new IllegalArgumentException("@NotFound@ @M_Invoice_ID@");
		
		// Obtain the Active Record of M_Invoice Table
		Query q = new Query("C_Invoice");
		q.addRestriction("C_Invoice_ID", Query.EQUAL, Integer.valueOf(c_Invoice_ID));
		int table_ID = X_C_Invoice.Table_ID;
		
		// Create the Process Info Instance to generate the report
		PrintInfo i = new PrintInfo("Aviso Debito o Credito", table_ID, c_Invoice_ID, 0);
		 if (invoice.isXX_IsDiferentPrice())
			 f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("#XX_R_DEBTCREDITREPORT_ID"), false);
		 else
			 f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("#XX_R_DEBTCREDITPIECES_ID"), false);
		
		// Create the report
		ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i); 
		
		if(show)
			new Viewer(re);
		
		// Generate the PDF file
		File attachment = null;
		try
		{
			attachment = File.createTempFile("aviso_", ".pdf");
			re.getPDF(attachment);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		
		return attachment;
		
	}
	
	/**
	 * Method that sends an email to BPartner Vendor
	 * @param dontSendTo: 1 contacto de ventas, 2 solo contacto administrativo
	 * @return
	 */
	public String sendEmail(MOrder order, MBPartner vendor, Vector<File> attachments, int dontSendTo)
	{			
		// Send the PDF File by E-Mail 
		MBPartner vendorAux = new MBPartner( Env.getCtx(), vendor.getC_BPartner_ID(), null);
		String toVendor = vendorAux.getXX_VendorEmail();
		X_AD_User buyerUser = new X_AD_User( Env.getCtx(), getAD_User_ID(order.getXX_UserBuyer_ID()), null);
		String emailToBuyer = buyerUser.getEMail();
		MClient m_client = MClient.get(ctx);
		
		String orderDo = order!= null ?  order.getDocumentNo() : "" ;
		
		EMail email = m_client.createEMail(null, toVendor, vendor.getName(),
				"Aviso de Débito o Crédito - BECO: "+  orderDo, 
				"Anexo se le envía el(los) aviso(s) generado(s) por una factura entregada a BECO y hecha por ustedes.");
		
		//Contactos
		Vector<MUser> contacts = getVendorContacts(vendor.get_ID());	
		
		for(int i=0; i<contacts.size(); i++){
			
			if((dontSendTo!=1) && contacts.get(i).getXX_ContactType().equals(X_Ref_XX_Ref_ContactType.SALES.getValue())){
				email.addTo(contacts.get(i).getEMail(), contacts.get(i).getName());
			}
			
			if((dontSendTo!=2) && contacts.get(i).getXX_ContactType().equals(X_Ref_XX_Ref_ContactType.ADMINISTRATIVE.getValue())){
				email.addTo(contacts.get(i).getEMail(), contacts.get(i).getName());
			}	
		}
		
		if (email != null)
		{			
			//email.addTo("cuentasporpagar@beco.com.ve", "#Cuentas por Pagar"); //COMENTADO POR GHUCHET A PETICIÓN DE FINANZAS
			
			if(emailToBuyer!=null && !emailToBuyer.isEmpty()){
				email.addTo(emailToBuyer, "Comprador");
			}
			
			//	Attachment
			for(int i=0; i < attachments.size(); i++){
				email.addAttachment(attachments.get(i));
			}
			
			String status = email.send();
			//String status = "";
			log.info("Email Send status: " + status);
			
			//if (m_user != null)
			//	new MUserMail(m_user, m_user.getAD_User_ID(), email).save();
			if (email.isSentOK())
			{
				return "Correo enviado correctamente";
			}
			else
				return "No se pudo enviar el correo";
		}
		else
			return "No se pudo enviar el correo";
	}
	
	/**Obtener Id de User dado el ID de Partner*/
	private Integer getAD_User_ID(Integer CBPartner)
	{
		Integer AD_User_ID=0;
		
		String SQL = "Select AD_USER_ID FROM AD_USER " +
					 "WHERE C_BPartner_ID IN "+CBPartner + " "+
					 "AND ISACTIVE='Y'";
		
		try{
			
			PreparedStatement pstmt = DB.prepareStatement(SQL, null); 
			ResultSet rs = pstmt.executeQuery();
				 
			if(rs.next())
			{
				AD_User_ID = rs.getInt("AD_USER_ID");
			}
			
			rs.close();
			pstmt.close();
			   
			}catch (Exception e) {
				log.log(Level.SEVERE, SQL);
			}
		
		return AD_User_ID;
	}
	
	private Vector<MUser> getVendorContacts(int vendor) {

		Vector<MUser> contacts = new Vector<MUser>();
		
		String sql = "select AD_User_ID from AD_User where C_BPartner_ID = ?";
		PreparedStatement pstmt = DB.prepareStatement(sql, null);
		ResultSet rs = null;
		
		try {
			pstmt.setInt(1, vendor);
			rs = pstmt.executeQuery();
	
			while (rs.next())
			{
				 contacts.add(new MUser( Env.getCtx(), rs.getInt(1), null));
			}
			
		} catch (SQLException e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		finally{
			DB.closeStatement(pstmt);
			DB.closeResultSet(rs);
		}

		return contacts;
	}
	
	private int getRefInvoice(int movement){
		
		String sql = "select inv.c_invoice_id " +
					 "from m_inoutline iol, m_inout io, c_order ord, c_invoice inv " +
					 "where io.m_inout_id = iol.m_inout_id and io.issotrx='N' and io.c_order_id = ord.c_order_id " +
					 "and ord.issotrx='N' and inv.C_DOCTYPETARGET_ID = "+ Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID")+" " +
					 "and ord.c_order_id = inv.c_order_id " +
					 "and (M_ATTRIBUTESETINSTANCE_ID,m_product_id) in " +
					    "(select mo.M_ATTRIBUTESETINSTANCE_ID, pro.m_product_id " +
					    "from m_movementline mo, m_product pro " +
					    "where mo.m_movement_id = "+ movement +" and mo.M_ATTRIBUTESETINSTANCE_ID <> 0 " +
					    "and pro.m_product_id = mo.m_product_id) " +
					 "group by inv.c_invoice_id, inv.grandtotal " +
					 "order by inv.grandtotal DESC ";

		PreparedStatement ps = DB.prepareStatement( sql, null);
		ResultSet rs = null;
		int invoice = 0;
		
		try {
			
			rs = ps.executeQuery();

	    	if(rs.next()){
	    		invoice = rs.getInt("inv.C_Invoice_ID");
	    	}
		}
		catch (Exception e) {
			log.log( Level.SEVERE, e.getMessage());
		}
		finally {
				if (rs != null)
					try {
						rs.close();
						rs = null;
					} catch (SQLException e) {}
				
				if (ps != null)
					try {
						ps.close();
						ps = null;
					} catch (SQLException e) {}		
			}			
		
		return invoice;
	}
	
	/**Crea un documento Excel a partir de una MiniTablePreparator  
	 * Usando el método de Crear Excel en ReporEngine con modificaciones */ 
	public static Excel createEXCEL (Excel excel, MiniTablePreparator miniTable) {
		final int COTA_EXCEL = Short.MAX_VALUE; 
		try {
			//	for all rows (-1 = header row)			
			for (int row = -1; row < miniTable.getRowCount(); row++) {
				if (row % COTA_EXCEL == COTA_EXCEL - 1) {
					excel.createAndSetSheet("Export Compiere " + ( 1 + row / COTA_EXCEL));
				}				
				//Para todas las columnas
				int colPos = 0;				
				for (int col = 0; col < miniTable.getColumnCount(); col++){
					if (row == -1 || row % COTA_EXCEL == COTA_EXCEL - 1) {														
						excel.createRow(
								(short)0, 
								(short)colPos, 
								miniTable.getColumnName(col), 
								null,
								Excel.CELLSTYLE_HEADER,
								Excel.DISPLAY_TYPE_STRING);							
					} else 	{							
						int displayType = Excel.DISPLAY_TYPE_STRING;
						Object obj = miniTable.getValueAt(row, col);
						String valor = "";
						if (obj == null)
							;
						else  {
							if (obj instanceof Integer) {
								displayType = Excel.DISPLAY_TYPE_INTEGER;
								valor = DisplayType.getNumberFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else if (obj instanceof Number) {							
								displayType = Excel.DISPLAY_TYPE_NUMBER;
								valor = DisplayType.getNumberFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else if (obj instanceof Date) {																
								displayType = Excel.DISPLAY_TYPE_DATE;
								valor = DisplayType.getDateFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else if (obj instanceof Timestamp) {
								displayType = Excel.DISPLAY_TYPE_DATE;
								valor = DisplayType.getDateFormat(displayType, Env.getLanguage(Env.getCtx())).format(obj);
							} else {
								valor = obj.toString();
							}
						}						
						/* Hecho por Javier Pino, sustituyendo la llamada por otro método overloaded  */														
						excel.createRow(
								//(row + 1), -- Comentado y modificado para que imprima desde el principio de las hojas sucesivas
								(short)(row % COTA_EXCEL + 1),
								(short)colPos, 
								valor, 
								valor,
								Excel.CELLSTYLE_NONE,
								displayType);							
					}
					colPos++;
				}	//	printed
			}	//	for all columns
		}	//	for all rows
		catch (Exception e){
			e.printStackTrace();
		}
		excel.close();
		return excel;
	}
	
	/*
	 * Método que genera el reporte de Carta Compromiso de acuerdo con negocioción hecha con el proveedor 
	 * @param número del aviso 
	 * Carmen Capote
	 */ 
	public boolean reportEngagementLetter(int creditNotify1){	
			boolean sendEmail = false;
			sendEmail = ADialog.ask(1, new Container(), "XX_PubContribMailQuest");
	
			Query q = new Query("XX_CreditNotifyReturn");
			q.addRestriction("XX_CreditNotifyReturn_ID", Query.EQUAL, creditNotify1);
			
			int table_ID = X_XX_CreditNotifyReturn.Table_ID;
	
			MPrintFormat f = null ;
	
			// Create the Process Info Instance to generate the report
			PrintInfo i = new PrintInfo("Carta Compromiso ", table_ID, creditNotify1, 0);
					
			f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_REPORT_COMMITMENT_ID"), false);
				
			// Create the report
			ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
			new Viewer(re);
			
			// Generate the PDF file
			File attachment = null;
			
			try
			{
				attachment = File.createTempFile("aviso_credito_por_carta_compromiso", ".pdf");
				re.getPDF(attachment);
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, "", e);
				return false;
			}
			if(sendEmail)
				if(sendEmailToCtasAndContabilidad(attachment))
					return true;	//El correo sen envió
				else
					return false;	//No se pudo enviar el correo
			return true;

		}
	
	/*
	 * Método para enviar Email a cuentas por pagar y contabilidad unicamente
	 * @param attachments reportes a adjuntar
	 */
	public boolean sendEmailToCtasAndContabilidad(File attachment)
	{			
		// Send the PDF File by E-Mail 

		
		MClient m_client = MClient.get(ctx);
		
		EMail email = m_client.createEMail(null, "ccapote@beco.com.ve", "Contabilidad",
				"Aviso por Acuerdo Comercial - Aporte a Publicidad y/o Carta Compromiso: ", 
				"Anexo se les envía aviso(s) generado(s) por Acuerdo Comercial - Aporte a Publicidad y/o Carta Compromiso BECO.");

		
		if (email != null)
		{			
			email.addTo("cuentasporpagar@beco.com.ve", "#Cuentas por Pagar");
			
			//	Attachment
				email.addAttachment(attachment);
			
			
			String status = email.send();
			log.info("Email Send status: " + status);

			if (email.isSentOK())
			{
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}

	/**
	 * Calcula los descuentos de acuerdos comerciales por aporte a publicidad
	 * y se guardan en la tabla XX_CreditNotifyReturn
	 * 
	 * @author Luis García
	 * @param date fecha que contiene el mes y año sobre el cual se calcularán los descuentos
	 */
	public void calculateTradeAgreePubli(String date){
		
		BigDecimal pubContribution = BigDecimal.ZERO; //Descuento por aporte a publicidad
		BigDecimal pubContributionTotal = BigDecimal.ZERO; //Descuento por aporte a publicidad total
		BigDecimal iva = BigDecimal.ONE;
		BigDecimal totalLines = BigDecimal.ZERO;
		BigDecimal totalLinesTotal = BigDecimal.ZERO;
		BigDecimal multiplyRate = BigDecimal.ONE; //Si es Bs
		int partnerId=0;
		int creditNotifyID=0;
		int lastPartner=0;
		int departmentID=0;
		int invoiceID=0;
		boolean flagBPartner= true;
		int warehouseID = 0;
		int currencyID =0;
		boolean isPercent = false;
		boolean hasRs = false;
		boolean hasRs3 = true;
		boolean nationalCurrency = false;
		boolean flagDiscount = false;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		

		String SQL1 = "select C_ORDER_ID, C_BPARTNER_ID from C_ORDER" +
				" where TO_CHAR (XX_RECEPTIONDATE,'YYYYMM') ="+date +
				" and TO_CHAR (XX_INVOICEDATE,'YYYYMM') ="+ date +
				" AND AD_CLIENT_ID="+  ctx.getAD_Client_ID()+
				" AND ISSOTRX = 'N' AND XX_POTYPE = 'POM' " +
				" minus  select o.C_ORDER_ID, C_BPARTNER_ID" +
				" from C_ORDER o, XX_VCN_DETAILADVICE d, XX_CREDITNOTIFYRETURN cn" +
				" where TO_CHAR (o.XX_RECEPTIONDATE,'YYYYMM') ="+date +
				" and TO_CHAR (o.XX_INVOICEDATE,'YYYYMM') ="+date +
				" and d.XX_YEAR || d.XX_MONTH=" + date+ " and cn.XX_CREDITNOTIFYRETURN_ID=d.XX_CREDITNOTIFYRETURN_ID" +
				" and cn.XX_NOTIFICATIONTYPE='AAP'"+
				" and d.C_ORDER_ID=o.C_ORDER_ID "+
				" AND o.AD_Client_ID = " + 
				Env.getCtx().getContext("#XX_L_CLIENTCENTROBECO_ID")+
				" AND o.ISSOTRX = 'N' order by 2";

		try{	
			pstmt1 = DB.prepareStatement(SQL1, null); 
		    rs1 = pstmt1.executeQuery();
		    
		    int i=0;
		    
		    while(rs1.next()){
				MOrder mOrder = new MOrder(ctx, rs1.getInt(1), null);
				partnerId = mOrder.getC_BPartner_ID();

				//Se revisa si el socio del negocio tiene acuerdo comercial vigente
				String SQL3 = "Select t.XX_VCN_TRADEAGREEMENTS_ID"
					+ " from XX_VCN_TRADEAGREEMENTS t, C_ORDER c, XX_VCN_TRADEAGRDEPARTMENT d" 
					+ " where t.C_BPARTNER_ID = " + mOrder.getC_BPartner_ID()
					+ " and t.XX_STATUS = 'VIGENTE' and c.C_BPARTNER_ID= "+mOrder.getC_BPartner_ID()
					+ " and XX_APPROVALDATE > XX_INITDATE and d.XX_DEPARTAMENTO_ID = " + mOrder.getXX_VMR_DEPARTMENT_ID()
					+ " and d.ISACTIVE='Y'"
					+ " and d.XX_VCN_TRADEAGREEMENTS_ID = t.XX_VCN_TRADEAGREEMENTS_ID and (t.XX_PubPercent> 0 or XX_PubAmount > 0)"
					+ " AND t.AD_CLIENT_ID="+  ctx.getAD_Client_ID()
					+ " AND c.ISSOTRX = 'N'";
					
				
				//Si hay varios vencidos se elige el de initDate mas reciente
				String SQL4 = "Select t.XX_VCN_TRADEAGREEMENTS_ID"
					+ " from XX_VCN_TRADEAGREEMENTS t, C_ORDER c, XX_VCN_TRADEAGRDEPARTMENT d"  
					+ " where t.C_BPARTNER_ID = " + mOrder.getC_BPartner_ID()
					+ " and t.XX_STATUS = 'VENCIDO' and c.C_BPARTNER_ID= "+mOrder.getC_BPartner_ID()
					+ " and XX_APPROVALDATE > XX_INITDATE and d.XX_DEPARTAMENTO_ID = " + mOrder.getXX_VMR_DEPARTMENT_ID()
					+ " and d.ISACTIVE='Y'"
					+ " and d.XX_VCN_TRADEAGREEMENTS_ID = t.XX_VCN_TRADEAGREEMENTS_ID and (t.XX_PubPercent> 0 or XX_PubAmount > 0)"
					+ " AND t.AD_CLIENT_ID="+  ctx.getAD_Client_ID()
					+ " AND c.ISSOTRX = 'N'"
					+ " order by XX_APPROVALDATE asc";
				PreparedStatement pstmt3 = null;
				PreparedStatement pstmt4 = null;
				ResultSet rs3 = null;
				ResultSet rs4 = null;
				
				try {
					pstmt3 = DB.prepareStatement(SQL3, null);
					rs3 = pstmt3.executeQuery();//AC vigente
					pstmt4 = DB.prepareStatement(SQL4, null);
					rs4 = pstmt4.executeQuery();//AC vencido
					//Si no existe AC vigente se usa el vencido
					if (!rs3.next()){
						rs3=rs4;
						hasRs3=false;
					}else
						hasRs3 = true;
					
					if(rs4.next()) 
					 	hasRs=true;
					
					//Corregir si no existe ningún AC
					 if (hasRs || hasRs3){
						X_XX_VCN_TradeAgreements agreement = new X_XX_VCN_TradeAgreements(
									Env.getCtx(), rs3.getInt(1), null);
							//Valido que sea un Acuerdo Comercial
						if(agreement.getXX_AgreementType().equals("AC")){
							//Valido que la orden de compra fue está en la fecha de vigencia del AC
							if((mOrder.getXX_ApprovalDate().after(agreement.getXX_InitDate()))){
								
								//Es la 1er vez igualo el último proveedor al actual
								if(i==0)
									lastPartner=partnerId;
								else{
									if(lastPartner!=partnerId){
										flagBPartner = true;
										pubContributionTotal = BigDecimal.ZERO;
										totalLinesTotal = BigDecimal.ZERO;
									}
								}
								
								X_XX_CreditNotifyReturn creditNotify = new X_XX_CreditNotifyReturn(ctx, 0, null);
								String SQL =
											"SELECT SUM(inv.TOTALLINES) TOTALLINES, inv.C_CURRENCY_ID, ord.M_WAREHOUSE_ID, ord.XX_VMR_DEPARTMENT_ID " +
											"FROM C_INVOICE INV " +
											"inner join C_Order ord ON (inv.C_Order_ID = ord.C_Order_ID) " +
											"WHERE inv.C_ORDER_ID = " + mOrder.get_ID() + " " +
											"AND inv.C_DOCTYPETARGET_ID = " + Env.getCtx().getContextAsInt("#XX_C_DOCTYPE_ID") +" "+ 
											"AND inv.AD_CLIENT_ID= "+  ctx.getAD_Client_ID() +" "+
											"AND ord.ISSOTRX = 'N' " +
											"group by ord.C_ORDER_ID, inv.C_CURRENCY_ID, ord.M_WAREHOUSE_ID, ord.XX_VMR_DEPARTMENT_ID";
								
								
								PreparedStatement pstmt = null;
								ResultSet rs = 	null;
								try
								{	
									pstmt = DB.prepareStatement(SQL, null); 
								    rs = pstmt.executeQuery();
								    
								    while(rs.next())
								    {
								    	totalLines = rs.getBigDecimal("TOTALLINES");
								    	warehouseID = rs.getInt("M_WAREHOUSE_ID");
								    	currencyID = rs.getInt("C_CURRENCY_ID");
								    	departmentID = rs.getInt("XX_VMR_DEPARTMENT_ID");
								    	//Si la moneda es nacional
								    	if (rs.getInt("C_CURRENCY_ID")==205)
								    		nationalCurrency = true;
								    	if (!isPercent && agreement.getC_Currency_ID()==205)
								    		nationalCurrency = true;
								    }
								}
								catch (Exception e) {
									System.out.println("ERROR GENERANDO EL AVISO DE DESCUENTO");
								}
								finally{
									DB.closeResultSet(rs);
									DB.closeStatement(pstmt);
								}
												
								//Si el AC tiene descuento por aporte a publicidad
								if(agreement.getXX_PubPercent().compareTo(BigDecimal.ZERO)!=0){
									pubContribution = agreement.getXX_PubPercent().multiply(totalLines);
									pubContribution = pubContribution.divide(new BigDecimal(100));
									flagDiscount=true;
									isPercent = true;
								}
								if (agreement.getXX_PubAmount().compareTo(BigDecimal.ZERO)!=0){
									pubContribution = agreement.getXX_PubAmount();
									flagDiscount=true;
								}
								
								pubContribution = pubContribution.setScale(2, RoundingMode.HALF_UP);
								
								//Si hay algún descuento
								if(flagDiscount){
									//Si es nacional, se aplica el iva
									if (nationalCurrency){
										totalLinesTotal = totalLinesTotal.add(totalLines);
										pubContributionTotal= pubContributionTotal.add(pubContribution);
										String SQL5 = "SELECT A.RATE, A.C_TAX_ID, B.C_TAXCATEGORY_ID " +
												  "FROM C_TAX A, C_TAXCATEGORY B " +
												  "WHERE " +
											      "VALIDFROM = (SELECT MAX(C.VALIDFROM) FROM C_TAX C WHERE C.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID) " +
											      "AND A.C_TAXCATEGORY_ID = B.C_TAXCATEGORY_ID AND B.NAME LIKE 'IVA'"+
											      " AND A.AD_CLIENT_ID="+  ctx.getAD_Client_ID();
										PreparedStatement pstmt5 = null;
										ResultSet rs5 = null;
										try
										{	
											pstmt5 = DB.prepareStatement(SQL5, null); 
											rs5 = pstmt5.executeQuery();
										   
											while(rs5.next())
											{
												creditNotify.setC_TaxCategory_ID(rs5.getInt("C_TAXCATEGORY_ID"));
										    	iva = rs5.getBigDecimal("RATE");
										    	iva = iva.divide(new BigDecimal(100));
										    	creditNotify.setC_Tax_ID(rs5.getInt("C_TAX_ID"));			    	
											}
										}
										catch (Exception e) {
											System.out.println("ERROR GENERANDO EL AVISO DE DESCUENTO");
										}
										finally{
											DB.closeResultSet(rs5);
											DB.closeStatement(pstmt5);
										}
									}
									else{
										creditNotify.setC_Tax_ID(ctx.getContextAsInt("#XX_L_TAX_EXENTO_ID")); //Si no es moneda nacional esta EXENTO(1000021) de iva
										creditNotify.setC_TaxCategory_ID(ctx.getContextAsInt("#XX_L_TAXCATEGORY_EXEN_ID"));
										iva = BigDecimal.ZERO;
									}
								}
									//Se calcula el IVA y se guarda
									creditNotify.setXX_Amount_IVA(pubContribution.multiply(iva));
									//Si hay tasa de cambio calculo el costo original en Bs
									if(mOrder.getXX_ConversionRate_ID()!=0){
										String SQL6 = "SELECT MULTIPLYRATE "+
										  "FROM C_CONVERSION_RATE " +
										  "WHERE C_CONVERSION_RATE_ID="+mOrder.getXX_ConversionRate_ID()+
										  " AND AD_CLIENT_ID="+  ctx.getAD_Client_ID();
										PreparedStatement pstmt6 = null;
										ResultSet rs6 = null;
										try
										{	
											pstmt6 = DB.prepareStatement(SQL6, null); 
											rs6 = pstmt6.executeQuery();
											if(rs6.next()){
												multiplyRate = rs6.getBigDecimal("MULTIPLYRATE");
												creditNotify.setXX_UnitPurchasePriceBs(totalLines.multiply(multiplyRate));
											}
										}
										catch (SQLException e) {
											log.log(Level.SEVERE, SQL6, e);
										}
										finally{
											DB.closeResultSet(rs6);
											DB.closeStatement(pstmt6);
										}
									}
									//No hay tasa de cambio, el costo es en Bs.
									else    	
										creditNotify.setXX_UnitPurchasePriceBs(totalLines);
									if(lastPartner==partnerId || flagBPartner){ 		
										//Si es un proveedor distinto al anterior
										if(flagBPartner){
											flagBPartner = false;
									//Se guarda el monto total de los descuentos sin IVA
									creditNotify.setXX_Amount(pubContribution);
									creditNotify.setXX_UnitPurchasePrice(totalLines);
									creditNotify.setXX_NotificationType("AAP");
									creditNotify.setDescription("Aporte a Publicidad");
									if (isPercent)
										creditNotify.setC_Currency_ID(currencyID);
									else
										creditNotify.setC_Currency_ID(agreement.getC_Currency_ID());
									creditNotify.setC_Order_ID(mOrder.get_ID());
									if (!creditNotify.save())
										log.log(Level.WARNING,"Credit Notify wasn't successfully saved");
									creditNotifyID = creditNotify.getXX_CreditNotifyReturn_ID();
								}
									
								X_XX_VCN_DetailAdvice detailAdvice = new X_XX_VCN_DetailAdvice(ctx, 0, null);
								detailAdvice.setM_Warehouse_ID(warehouseID);
						    	//detailAdvice.setC_Invoice_ID(invoiceID);
						    	detailAdvice.setC_Order_ID(mOrder.getC_Order_ID());
						    	detailAdvice.setXX_VMR_Department_ID(departmentID);
								detailAdvice.setXX_PubAmount(pubContribution);
								detailAdvice.setXX_CreditNotifyReturn_ID(creditNotifyID);
								detailAdvice.setXX_Month(Integer.parseInt(date.substring(4)));
								creditNotify.setXX_Amount(pubContributionTotal);
								detailAdvice.setXX_Year(Integer.parseInt(date.substring(0,4)));
								detailAdvice.setXX_UnitPurchasePrice(totalLines); // En AAP se guarda el purchasePrice en Bs
								//CreditNotify que se usará para ir guardando el acumulado en un aviso de crédito
								X_XX_CreditNotifyReturn creditNotifyTotal = new X_XX_CreditNotifyReturn(ctx, creditNotifyID, null);
								creditNotifyTotal.setXX_Amount(pubContributionTotal);
								creditNotifyTotal.setXX_Amount_IVA(pubContributionTotal.multiply(iva));
								creditNotifyTotal.setXX_UnitPurchasePrice(totalLinesTotal);
								creditNotifyTotal.setXX_UnitPurchasePriceBs(totalLinesTotal.multiply(multiplyRate));
								creditNotifyTotal.save();									
								
								if (!detailAdvice.save())
									log.log(Level.WARNING,"Detail Advice wasn't successfully saved");	
							}
								
							if(i!=0)
								lastPartner = partnerId;
							
							i++;
							hasRs = false;
							hasRs3 = true;
							}			
						}		
					}	
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally{
					DB.closeResultSet(rs3);
					DB.closeStatement(pstmt3);
					DB.closeResultSet(rs4);
					DB.closeStatement(pstmt4);
				}
		    }
		    
		}catch  (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DB.closeResultSet(rs1);
			DB.closeStatement(pstmt1);
		}
	}
	
	/**
	 * Método que genera el reporte de Acuerdos Comerciales por Aporte a Publicidad
	 * 
	 * @author Luis García
	 * @param date Fecha que contiene el mes y año sobre la cual se generará el reporte.
	 * @return
	 */
	public boolean reportPublicityContrib(Calendar date){
		boolean sendEmail = false;

		sendEmail = ADialog.ask(1, new Container(), "XX_PubContribMailQuest");
	
		Query q = new Query("XX_YEAR");
		//q.addRestriction("XX_MONTH", Query.EQUAL, Integer.valueOf((date.get(Calendar.MONTH)+1)));
		q.addRestriction("XX_YEAR", Query.EQUAL, Integer.valueOf(date.get(Calendar.YEAR)));
		//q.addRestriction("XX_NOTIFICATIONTYPE", Query.EQUAL, "AAP");
		int table_ID = 1002558;//X_XX_VCN_DetailAdvice.Table_ID;
	
		MPrintFormat f = null ;
	
		// Create the Process Info Instance to generate the report
		PrintInfo i = new PrintInfo("Acuerdo Comercial - Aporte a Publicidad ", table_ID, 1050287);
					
		f = MPrintFormat.get (Env.getCtx(), Env.getCtx().getContextAsInt("#XX_L_REPORT_PUBCONTRIB_ID"), false);
				
		// Create the report
		ReportEngine re = new ReportEngine(Env.getCtx(), f, q, i);
		new Viewer(re);
			
		// Generate the PDF file
		File attachment = null;
			
		try{
			attachment = File.createTempFile("aviso_credito_por_acuerdo_comercial_publicidad", ".pdf");
			re.getPDF(attachment);
		}catch (Exception e){
			log.log(Level.SEVERE, "", e);
			return false;
		}
		if(sendEmail)
			if(sendEmailToCtasAndContabilidad(attachment))
				return true;	//El correo se envió
			else
				return false;	//No se pudo enviar el correo
		return true;
	}
	
	// Agregado por GMARQUES
	/**
	 * @return Fecha actual del servidor
	 */
	public static Date currentServerDate() {
		Date sd = new Date();
		try {
			PreparedStatement pstmt = DB.prepareStatement("SELECT sysdate from dual", null); 
		    ResultSet rs = pstmt.executeQuery();			
			while (rs.next()) {				
				sd = rs.getDate(1);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return sd;
	}
}
