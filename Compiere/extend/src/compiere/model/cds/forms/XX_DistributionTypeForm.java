package compiere.model.cds.forms;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.plaf.CompiereColor;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

public class XX_DistributionTypeForm extends CPanel 
       implements FormPanel, ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**	Window No			*/
	private int         	m_WindowNo = 0;
	/**	FormFrame			*/
	private FormFrame 		m_frame;	
	/**	Logger				*/
	static CLogger 			log = CLogger.getCLogger(XX_DistribByBudgetForm.class);
	
	private int     m_AD_Client_ID = Env.getCtx().getAD_Client_ID();
	private int     m_AD_Org_ID = Env.getCtx().getAD_Org_ID();
	private int     m_by = Env.getCtx().getAD_User_ID();
	
	private CPanel mainPanel = new CPanel();
	private CPanel commandPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	//private StatusBar statusBar = new StatusBar();
	private CComboBox distributions = new CComboBox();
	private CButton bAccept = ConfirmPanel.createOKButton(true);
	private CButton bCancel = ConfirmPanel.createCancelButton(true);
	private CLabel panelLabel = new CLabel();
	
	public static KeyNamePair selection;
	
	
	@Override
	public void init (int WindowNo, FormFrame frame) {
		// TODO Auto-generated method stub
		m_WindowNo = WindowNo;
		m_frame = frame;
		log.info("WinNo=" + m_WindowNo
			+ " - AD_Client_ID=" + m_AD_Client_ID + ", AD_Org_ID=" + m_AD_Org_ID + ", By=" + m_by);
		Env.getCtx().setIsSOTrx(m_WindowNo, false);

		try
		{
			//UI
			jbInit();
			dynInit();
			
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
			//

	}
	
	private void jbInit() {
		
		CompiereColor.setBackground(this);
		
		mainPanel.setLayout(mainLayout);
		
		mainPanel.add(panelLabel);		
		mainPanel.add(distributions);
		
		commandPanel.add(bAccept);
		commandPanel.add(bCancel);
		
		bAccept.addActionListener(this);
		bCancel.addActionListener(this);
	}
	
	private void dynInit() {
		// TODO Auto-generated method stub
		String sql = "SELECT XX_VMR_DISTRIBUTIONTYPE_ID, NAME FROM XX_VMR_DISTRIBUTIONTYPE ";
		KeyNamePair loadKNP = null;
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				
				loadKNP = new KeyNamePair(rs.getInt(1), rs.getString(2));
				distributions.addItem(loadKNP);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		panelLabel.setText("Seleccione el tipo de Distribución");
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		m_frame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bAccept){
			selection = (KeyNamePair)distributions.getSelectedItem();
			dispose();
		}
		else if(e.getSource() == bCancel){
			dispose();
		}
	}
}
