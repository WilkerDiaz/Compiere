package compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MInterestArea;
import org.compiere.model.MMailText;
import org.compiere.model.MStore;
import org.compiere.model.MUser;
import org.compiere.model.MUserMail;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class EnvioEmailPromociones extends SvrProcess {
	/** What to send			*/
	private int				m_R_MailText_ID = -1;
	/**	Mail Text				*/
	private MMailText		m_MailText = null;

	/**	From (sender)			*/
	private int				m_AD_User_ID = -1;
	/** Client Info				*/
	private MClient			m_client = null;
	/**	From					*/
	private MUser			m_from = null;
	/** Recipient List to prevent duplicate mails	*/
	private ArrayList<Integer>	m_list = new ArrayList<Integer>();

	
	private int 			m_counter = 0;
	private int 			m_errors = 0;
	/**	To Subscribers 			*/
	private int				m_R_InterestArea_ID = -1;
	/** Interest Area			*/
	private MInterestArea 	m_ia = null;
	/** To Customer Type		*/
	private int				m_C_BP_Group_ID = -1;
	private String			nombrePromocion = "";
	private int				codigoPromocion = -1;
	/** To Purchaser of Product	*/
	//	comes here
	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		//ProcessInfoParameter[] para = getParameter();
		X_XX_PROMOCIONES x_promociones = new X_XX_PROMOCIONES(getCtx(), getRecord_ID(), get_TrxName());
		
		
		/*for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)*/
				//;
			//else if (name.equals("R_InterestArea_ID"))
		
				m_R_InterestArea_ID = 1000001;//*/element.getParameterAsInt();
			//else if (name.equals("R_MailText_ID"))
				m_R_MailText_ID = 1000001;//*/element.getParameterAsInt();
			//else if (name.equals("C_BP_Group_ID"))
				if (x_promociones.getTipoPromocion().equals("1000100") || x_promociones.getTipoPromocion().equals("1000200") 
					|| x_promociones.getTipoPromocion().equals("1000300") || x_promociones.getTipoPromocion().equals("1000500")
					|| x_promociones.getTipoPromocion().equals("1000600")  || x_promociones.getTipoPromocion().equals("1000700") 
					|| x_promociones.getTipoPromocion().equals("1001300") || x_promociones.getTipoPromocion().equals("1001350"))
					m_C_BP_Group_ID = 1000012;
				if (x_promociones.getTipoPromocion().equals("1000750") || x_promociones.getTipoPromocion().equals("1000800") 
					|| x_promociones.getTipoPromocion().equals("1000900") || x_promociones.getTipoPromocion().equals("1001000")
					|| x_promociones.getTipoPromocion().equals("1001100")  || x_promociones.getTipoPromocion().equals("1001200") 
					|| x_promociones.getTipoPromocion().contains("1001400"))
					m_C_BP_Group_ID = 1000011;
				if (x_promociones.getTipoPromocion().equals("1000400")) 
					m_C_BP_Group_ID = 1000013;
			//else if (name.equals("AD_User_ID"))
				m_AD_User_ID = Env.getCtx().getAD_User_ID();//*/element.getParameterAsInt();
				codigoPromocion = x_promociones.getXX_PROMOCIONES_ID();
				nombrePromocion = x_promociones.getName();
				
			//else
				//log.log(Level.SEVERE, "Unknown Parameter: " + name);
		//}
	}
	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		log.info("R_MailText_ID=" + m_R_MailText_ID);
		//	Mail Test
		m_MailText = new MMailText (getCtx(), m_R_MailText_ID, get_TrxName());
		if (m_MailText.getR_MailText_ID() == 0)
			throw new Exception ("Not found @R_MailText_ID@=" + m_R_MailText_ID);
		//	Client Info
		m_client = MClient.get (getCtx());
		if (m_client.getAD_Client_ID() == 0)
			throw new Exception ("Not found @AD_Client_ID@");
		if (m_client.getSmtpHost() == null || m_client.getSmtpHost().length() == 0)
			throw new Exception ("No SMTP Host found");
		//
		if (m_AD_User_ID > 0)
		{
			m_from = new MUser (getCtx(), m_AD_User_ID, get_TrxName());
			if (m_from.getAD_User_ID() == 0)
				throw new Exception ("No found @AD_User_ID@=" + m_AD_User_ID);
		}
		log.fine("From " + m_from);
		long start = System.currentTimeMillis();
		
		if (m_R_InterestArea_ID > 0)
			sendInterestArea();
		if (m_C_BP_Group_ID > 0)
			sendBPGroup();

		return "@Created@=" + m_counter + ", @Errors@=" + m_errors + " - "
			+ (System.currentTimeMillis()-start) + "ms";
	}


	
	private void sendInterestArea()
	{
		log.info("R_InterestArea_ID=" + m_R_InterestArea_ID);
		m_ia = MInterestArea.get(getCtx(), m_R_InterestArea_ID);
		String unsubscribe = null;
		if (m_ia.isSelfService())
		{
			unsubscribe = "\n\n---------.----------.----------.----------.----------.----------\n"
				+ Msg.getElement(getCtx(), "R_InterestArea_ID")
				+ ": " + m_ia.getName()
				+ "\n" + Msg.getMsg(getCtx(), "UnsubscribeInfo")
				+ "\n";
			MStore[] wstores = MStore.getOfClient(m_client);
			int index = 0;
			for (int i = 0; i < wstores.length; i++)
			{
				if (wstores[i].isDefault())
				{
					index = i;
					break;
				}
			}
			if (wstores.length > 0)
				unsubscribe += wstores[index].getWebContext(true);
		}

		//
		String sql = "SELECT u.Name, u.EMail, u.AD_User_ID "
			+ "FROM R_ContactInterest ci"
			+ " INNER JOIN AD_User u ON (ci.AD_User_ID=u.AD_User_ID) "
			+ "WHERE ci.IsActive='Y' AND u.IsActive='Y'"
			+ " AND ci.OptOutDate IS NULL"
			+ " AND u.EMail IS NOT NULL"
			+ " AND ci.R_InterestArea_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, m_R_InterestArea_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Boolean ok = sendIndividualMail (rs.getString(1), rs.getInt(3), unsubscribe);
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
			log.log(Level.SEVERE, sql, ex);
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
		m_ia = null;
	}	//	sendInterestArea
	
	private void sendBPGroup()
	{
		log.info("C_BP_Group_ID=" + m_C_BP_Group_ID);
		String sql = "SELECT u.Name, u.EMail, u.AD_User_ID "
			+ "FROM AD_User u"
			+ " INNER JOIN C_BPartner bp ON (u.C_BPartner_ID=bp.C_BPartner_ID) "
			+ "WHERE u.IsActive='Y' AND bp.IsActive='Y'"
			+ " AND u.EMail IS NOT NULL"
			+ " AND bp.C_BP_Group_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, m_C_BP_Group_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				Boolean ok = sendIndividualMail (rs.getString(1), rs.getInt(3), null);
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
			log.log(Level.SEVERE, sql, ex);
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
	}	//	sendBPGroup
	
	private Boolean sendIndividualMail (String Name, int AD_User_ID, String unsubscribe)
	{
		//	Prevent two email
		Integer ii = Integer.valueOf (AD_User_ID);
		if (m_list.contains(ii))
			return null;
		m_list.add(ii);
		//
		MUser to = new MUser (getCtx(), AD_User_ID, null);
		if (to.isEMailBounced())			//	ignore bounces
			return null;
		m_MailText.setUser(AD_User_ID);		//	parse context

		String message = m_MailText.getMailText(true)+ ".\nPromocion " + nombrePromocion + " codigo: "+codigoPromocion ;
		//	Unsubscribe
		if (unsubscribe != null)
			message += unsubscribe;
		//
		EMail email = m_client.createEMail(m_from, to, m_MailText.getMailHeader(), message);
		if (email == null)
			return Boolean.FALSE;
		if (m_MailText.isHtml())
			email.setMessageHTML(m_MailText.getMailHeader(), message);
		else
		{
			email.setSubject (m_MailText.getMailHeader());
			email.setMessageText (message);
		}
		if (!email.isValid() 
				&& !email.isValid(true))
		{
			log.warning(email.toString());
			to.setIsActive(false);
			to.addDescription("Invalid EMail");
			to.save();
			return Boolean.FALSE;
		}
		boolean OK = EMail.SENT_OK.equals(email.send());
		new MUserMail(m_MailText, AD_User_ID, email).save();
		//
		if (OK)
			log.fine(to.getEMail());
		else
			log.warning("FAILURE - " + to.getEMail());
		addLog(0, null, null, (OK ? "@OK@" : "@ERROR@") + " - " + to.getEMail());
		return Boolean.valueOf(OK);
	}	//	sendIndividualMail
}
