package compiere.model.promociones.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JScrollPane;



import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import compiere.model.cds.forms.XX_ProductFilter;

public class XX_PromoCondWarehouseForm extends CPanel implements FormPanel,
ActionListener {

		private static final long serialVersionUID = 1L;
		
		/** Inicializar la forma
		 *  @param WindowNo window
		 *  @param frame frame
		 */ 
		public void init (int WindowNo, FormFrame frame){
			log.info("");
			m_WindowNo = WindowNo;
			m_frame = frame;
			try	{
				jbInit();
				frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
				frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
			}
			catch(Exception e)	{
				log.log(Level.SEVERE, "", e);
			}
		}	// Fin init
		
		/**	Window No */
		private int m_WindowNo = 0;
		/**	FormFrame */
		private FormFrame m_frame;
		/**	Logger */
		private static CLogger log = CLogger.getCLogger(XX_ProductFilter.class);
			
		//PANEL
		private CPanel mainPanel = new CPanel();
		private BorderLayout mainLayout = new BorderLayout();
		private CPanel parameterPanel = new CPanel();
		private CPanel commandPanel = new CPanel();
		private GridBagLayout parameterLayout = new GridBagLayout();
		private JScrollPane dataPane = new JScrollPane();
		private FlowLayout commandLayout = new FlowLayout();
		
		//BOTONES
		private JButton bGenerate = ConfirmPanel.createProcessButton(true);	
		
		private ArrayList<VCheckBox> checks = new ArrayList<VCheckBox>();
		private ArrayList<KeyNamePair> checkObjects = new ArrayList<KeyNamePair>();
		private int warehouseAmount = 0;
		// Elemento sobre el cuál se realizan las modificaciones
		 private int conditionID = Integer.parseInt(Env.getCtx().getContext("#XX_VMR_Condition_ID"));

		/** Static Init
		 *  @throws Exception
		 */
		private void jbInit() throws Exception {	
			ArrayList<Integer> promoWarehouses = new ArrayList<Integer>();
			mainPanel.setLayout(mainLayout);
			parameterPanel.setLayout(parameterLayout);	
			//setear labels
	
			// PANEL CREATION
			//mainPanel.add(parameterPanel, BorderLayout.NORTH);		

			//checks
			parameterPanel.setLayout(parameterLayout);
			String sql = "SELECT VALUE, NAME FROM M_WAREHOUSE WHERE VALUE <> '000' AND VALUE <> '001' AND XX_ISSTORE = 'Y'";
			String sql2 = "SELECT XX_WAREHOUSEBECONUMBER FROM XX_VMR_PromoCondWarehouse WHERE XX_VMR_PromoConditionValue_ID = "+conditionID;
			//Preparar la tabla	
			PreparedStatement pstmt = null, pstmt2 = null;
			ResultSet rs = null, rs2 = null;
			VCheckBox check;
			int i = 0, j, line =0, warehouse;
			String name;

			try {
				pstmt = DB.prepareStatement(sql,null);
				pstmt2 = DB.prepareStatement(sql2, null);
				rs = pstmt.executeQuery();	
				rs2 = pstmt2.executeQuery();
				while(rs2.next()){
					promoWarehouses.add(Integer.parseInt(rs2.getString("XX_WAREHOUSEBECONUMBER")));
				} 
				
				while(rs.next()){
					warehouse =Integer.parseInt(rs.getString("VALUE"));
					name = rs.getString("NAME");
					check = new VCheckBox();
					check.setText(name);
					if (promoWarehouses.size()==0){
						check.setSelected(true);
					}else if(promoWarehouses.size()==1 && (promoWarehouses.get(0)==0||promoWarehouses.get(0)==1)){
						check.setSelected(true);
					}else{
						j = 0;
						while(j < promoWarehouses.size()){
							if(promoWarehouses.get(j)==warehouse){
								check.setSelected(true);
							}
							j++;
						} 
					}
					checks.add(check);
					checkObjects.add(new KeyNamePair(warehouse,name));
					
					if ((i%2)==0){
						parameterPanel.add(check, new GridBagConstraints(0, line, 1, 1, 0.0, 0.0
								,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
					}else{
						parameterPanel.add(check, new GridBagConstraints(1, line, 1, 1, 0.0, 0.0
								,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
						line++;
					}

					i++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
				DB.closeStatement(pstmt);
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt2);
				DB.closeResultSet(rs2);
			}
			
		
			// BP
		
			mainPanel.add(dataPane, BorderLayout.CENTER);
			dataPane.setPreferredSize(new Dimension(300, 200));
			dataPane.getViewport().add(parameterPanel, BorderLayout.NORTH);	

	

			//PANEL INFERIOR
			commandPanel.setLayout(commandLayout);
			commandLayout.setAlignment(FlowLayout.RIGHT);
			commandLayout.setHgap(10);
			commandPanel.add(bGenerate, null);
		
			bGenerate.addActionListener(this);
		}   //  jbInit
		
		
		/** loadBasicInfo
		 *  Table initial state 
		 */
		public final void loadBasicInfo() {
		} // loadBasicInfo
		
		/**
		 * 	Dispose
		 */
		public void dispose() {
			if (m_frame != null)
				m_frame.dispose();
			m_frame = null;
		}	//	dispose
		

		/** processSelection
		 *  Configura las cantidades de acuerdo a lo definido por el usuario
		 * */
		public void processSelection () {
			String wh = "";
			String delete = "DELETE FROM XX_VMR_PROMOCONDWAREHOUSE WHERE XX_VMR_PROMOCONDITIONVALUE_ID = "+conditionID;
			DB.executeUpdate(null, delete);
			String sql = "INSERT INTO XX_VMR_PROMOCONDWAREHOUSE "
					+ "(AD_CLIENT_ID, AD_ORG_ID, CREATEDBY, UPDATEDBY, XX_VMR_PROMOCONDITIONVALUE_ID, XX_WAREHOUSEBECONUMBER, XX_VMR_PROMOCONDWAREHOUSE_ID)"
					+ " VALUES (?,?,?,?,?,?,?)";		
			ArrayList<Object> params;
			Integer cliente = Env.getCtx().getAD_Client_ID(), 
					org = Env.getCtx().getAD_Org_ID(), 
					user = Env.getCtx().getAD_User_ID(),
					ids = idPromoCondWarehouse();
			int i =0;
			int count=0;
			
			List<Object[]> bulkParams = new ArrayList<Object[]>();
			VCheckBox check;

			try {
				Iterator<VCheckBox> it = checks.iterator();
				while(it.hasNext()){
					check = it.next();
					if(check.isSelected()){
						count++;
					}
					i++;
				}
				if (checks.size()==count){
					params = new ArrayList<Object>();
					params.add(cliente);
					params.add(org);
					params.add(user);
					params.add(user);
					params.add(conditionID);
					params.add("000");
					params.add(ids);
					bulkParams.add(params.toArray());
					ids++;
				}else{
					it = checks.iterator();
					i =0;
					while(it.hasNext()){
						check = it.next();
						if(check.isSelected()){
							params = new ArrayList<Object>();
							params.add(cliente);
							params.add(org);
							params.add(user);
							params.add(user);
							params.add(conditionID);
							wh = String.valueOf(checkObjects.get(i).getKey());
							if(wh.length()==1) wh = "00"+wh;
							else if(wh.length()==2) wh = "0"+wh;
							params.add(wh);
							params.add(ids);
							bulkParams.add(params.toArray());
							ids++;
						}
						i++;
					}
				}
				
				DB.executeBulkUpdate(null, sql, bulkParams, true, true);
		}catch(Exception e){
			e.printStackTrace();
		}
	
		
			dispose();
		} // Fin processSelection
		
	
		public static void setAllWarehouses (int conditionID) {
			String sql = "INSERT INTO XX_VMR_PROMOCONDWAREHOUSE "
					+ "(AD_CLIENT_ID, AD_ORG_ID, CREATEDBY, UPDATEDBY, XX_VMR_PROMOCONDITIONVALUE_ID, XX_WAREHOUSEBECONUMBER, XX_VMR_PROMOCONDWAREHOUSE_ID)"
					+ " VALUES (?,?,?,?,?,?,?)";
			ArrayList<Object> params;
			Integer cliente = Env.getCtx().getAD_Client_ID(), 
					org = Env.getCtx().getAD_Org_ID(), 
					user = Env.getCtx().getAD_User_ID(),
					ids = idPromoCondWarehouse();

			params = new ArrayList<Object>();
			params.add(cliente);
			params.add(org);
			params.add(user);
			params.add(user);
			params.add(conditionID);
			params.add("000");
			params.add(ids);
			DB.executeUpdate(null, sql, params);
		} // Fin processSelection
		
			
		/**
		 *  ActionListener
		 *  @param e event
		 */
		public void actionPerformed (ActionEvent e) {
			//Generate Selection
			 if (e.getSource() == bGenerate ) {
				processSelection();
			} // bGenerate

		}   //  actionPerformed
		
		//Función que busca el último código de la tabla detalle promoción,
		public static Integer idPromoCondWarehouse()
		{
			String query= "select max(XX_VMR_PromoCondWarehouse_ID) from XX_VMR_PromoCondWarehouse";
			PreparedStatement pstmt = DB.prepareStatement(query, null);
			ResultSet rs = null;
			Integer lastConditionValue = 0;
			try {
				rs = pstmt.executeQuery();
				while (rs.next()) {
					lastConditionValue = rs.getInt(1); 
					System.out.println("Ultima condicion de la tabla Warehouse: " +lastConditionValue);
				}
				if(lastConditionValue == 0) lastConditionValue = 1000000;
				else lastConditionValue++;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			finally{
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
			return lastConditionValue;
		}
		
		
		
	} // Fin XX_VME_ModifyReferencesForm
