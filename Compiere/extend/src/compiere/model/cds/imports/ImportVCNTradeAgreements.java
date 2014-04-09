package compiere.model.cds.imports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.logging.Level;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import compiere.model.cds.As400DbManager;
import compiere.model.cds.X_XX_VCN_TradeAgreements;

public class ImportVCNTradeAgreements extends SvrProcess {

	
	@Override
	protected void prepare() 
	{
		
	}
	
	@Override
	protected String doIt() throws Exception {
		
		//Borro de Compiere
		String sql_delete = "DELETE XX_VCN_TradeAgreements";
		DB.executeUpdate(null,sql_delete);
		  
		As400DbManager As = new As400DbManager();
		As.conectar();
		
		Statement sentencia=null;
		//String sqlAs = "Select * from becofile.invm4" ;
		
		sentencia = As.conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = null;
		String sql = "SELECT * FROM becofile.COPD07 WHERE estreg = 1 ORDER BY COEMPE";   ;	
        
		try
        {          
			rs= As.realizarConsulta(sql,sentencia);
			
			X_XX_VCN_TradeAgreements tradeAgreement = null;
			Hashtable<Integer,Integer> category = loadCategorys();
			Integer vendor=0;
			Calendar longTimeAgo = Calendar.getInstance();
			longTimeAgo.set(1971, 01, 01);
			Calendar forever = Calendar.getInstance();
			forever.set(2050, 01, 01);
			while (rs.next())
			{	
				tradeAgreement = new X_XX_VCN_TradeAgreements( getCtx(), 0, null); 
				
				vendor = getVendorID(rs.getString("COEMPE"));
				
				if(vendor==null)
					continue;
				
				tradeAgreement.setXX_DATEEFFECTIVEFROM(rs.getTimestamp("FECVIGD"));
				
				if(rs.getTimestamp("FECVIGH").before(longTimeAgo.getTime()))
					tradeAgreement.setXX_EFFECTIVEDATETO(new Timestamp(forever.getTimeInMillis()));
				else
					tradeAgreement.setXX_EFFECTIVEDATETO(rs.getTimestamp("FECVIGH"));
					
				tradeAgreement.setC_BPartner_ID(vendor);
				tradeAgreement.setXX_VMR_Category_ID(category.get(rs.getInt("CODCAT")));
				
				tradeAgreement.setXX_DISCOUNTGOSAMOUNT(rs.getBigDecimal("DESGOSM"));
				tradeAgreement.setXX_DISCOUNTGOSPERCENTAGE(rs.getBigDecimal("DESGOSP"));
				
				tradeAgreement.setXX_CENTRADISCDELIAMOUNT(rs.getBigDecimal("DESCENEM"));
				tradeAgreement.setXX_CENTRADISCDELIPERCEN(rs.getBigDecimal("PDESCENE"));
				
				tradeAgreement.setXX_DISCRECOGDECLAMOUNT(rs.getBigDecimal("DESRECMM"));
				tradeAgreement.setXX_DISCRECOGDECLPERCEN(rs.getBigDecimal("PDESRECM"));
				
				tradeAgreement.setXX_DISCAFTERSALEAMOUNT(rs.getBigDecimal("DESPOSVM"));
				tradeAgreement.setXX_DISCAFTERSALEPERCEN(rs.getBigDecimal("PDESPOSV"));
				
				tradeAgreement.setXX_FIXVOLDISCOPERCEN(rs.getBigDecimal("DESVOLFP"));
				tradeAgreement.setXX_FIXVOLDISCOAMOUNT(rs.getBigDecimal("DESVOLFM"));
				
				tradeAgreement.setXX_FIRSTVARVOLDISCOPERCEN(rs.getBigDecimal("PDESVOLVP"));
				
				tradeAgreement.setXX_SECONDVARVOLDISCOPERCEN(rs.getBigDecimal("SDESVOLVP"));
				
				tradeAgreement.save();
			}
			
			rs.close();
			sentencia.close();
			
        }catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}
		
		return "End";
		
		/*
		StringBuffer sql = null;
		int no = 0;
		String clientCheck = " AND AD_Client_ID=" + s_AD_Client_ID;

		//	****	Prepare	****

		//	Delete Old Imported
		if (s_deleteOldImported)
		{
			sql = new StringBuffer ("DELETE FROM I_XX_VCN_COPD07 "
				+ " WHERE I_IsImported='Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Delete Old Imported =" + no);
		}
		
		
		//
//		Set Client, Org, IsActive, Created/Updated
		sql = new StringBuffer ("UPDATE I_XX_VCN_COPD07 "
			+ "SET AD_Client_ID = COALESCE (AD_Client_ID, ").append(s_AD_Client_ID).append("),"
			+ " AD_Org_ID = COALESCE (AD_Org_ID, 0),"
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
		sql = new StringBuffer ("UPDATE I_XX_VCN_COPD07 i "
				+ "SET XX_VCN_TradeAgreements_ID=(SELECT XX_VCN_TradeAgreements_ID FROM XX_VCN_TradeAgreements "
				+ "WHERE i.XX_CODACU=XX_AgreementCode and AD_CLIENT_ID=i.AD_CLIENT_ID) "
				+ "WHERE i.XX_VCN_TradeAgreements_ID IS NULL AND i.AD_CLIENT_ID IS NOT NULL AND I_IsImported='N'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			log.fine("Found Solm1=" + no);
			
			String ts = DB.isPostgreSQL()?"COALESCE(I_ErrorMsg,'')":"I_ErrorMsg";  //java bug, it could not be used directly
			
			commit();
		//-----------------------------------------------------------------------------------
		
			int noInsert = 0;

			//	Go through Records
			log.fine("start inserting...");
			sql = new StringBuffer ("SELECT * FROM I_XX_VCN_COPD07 "
				+ " WHERE I_IsImported='N'").append(clientCheck);
			
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), get_Trx());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next())
			{
				X_I_XX_VCN_COPD07 impICopd07 = new X_I_XX_VCN_COPD07 (getCtx(), rs, get_TrxName());
				log.fine("I_XX_VCN_COPD07_ID=" + impICopd07.getI_XX_VCN_COPD07_ID());
				
				X_XX_VCN_TradeAgreements impXTradeAgree = null;
				
				// Pregunto por ID de la tabla compiere que es igual al de la tabla I //
								
						if(impICopd07.getXX_VCN_TRADEAGREEMENTS_ID() == new BigDecimal(0))
						{
							impXTradeAgree = new X_XX_VCN_TradeAgreements(getCtx(), 0, get_TrxName());
						}	
						else  
						{
							impXTradeAgree = new X_XX_VCN_TradeAgreements(getCtx(), impICopd07.getXX_VCN_TRADEAGREEMENTS_ID().intValue(), get_TrxName());
						}
						
						//impXTradeAgree.setC_BPartner_ID(impICopd07.getXX_COEMPE());
						//impXTradeAgree.setXX_DATEEFFECTIVEFROM(impICopd07.getXX_FECVIGD());
						//impXTradeAgree.setXX_EFFECTIVEDATETO(impICopd07.getXX_FECVIGH());
						//impXTradeAgree.setXX_VMR_Category_ID(impICopd07.getXX_CodCat());
						//impXTradeAgree.setXX_BECOREPRESENTATIVE(impICopd07.getXX_PORBECO());
						//impXTradeAgree.setXX_NATIONALITYBECOREPRESE(impICopd07.getXX_PORBECONAC());
						//impXTradeAgree.setXX_CEDULABECOREPRESE(impICopd07.getXX_PORBECOCED());
						//impXTradeAgree.setXX_VENDORNAME(impICopd07.getXX_PROVEEDOR());
						//impXTradeAgree.setXX_VENDORREPRESE(impICopd07.getXX_PORPROV());
						//impXTradeAgree.setXX_NATIONALITYVENDORREPRESE(impICopd07.getXX_PORPROVNAC());
						//impXTradeAgree.setXX_CEDULAVENDORREPRESE(impICopd07.getXX_PORPROVCED());
						impXTradeAgree.setXX_FIXVOLDISCOPERCEN(impICopd07.getXX_DESVOLFP());
						impXTradeAgree.setXX_FIXVOLDISCOAMOUNT(impICopd07.getXX_DESVOLFM());
						//impXTradeAgree.setXX_APPLYFIXEDDISCOUNT(impICopd07.isXX_APLICADES());
						impXTradeAgree.setXX_FIRSTVARVOLDISCOFROM(impICopd07.getXX_PDESVOLVD());
						impXTradeAgree.setXX_FIRSTVARVOLDISCOTO(impICopd07.getXX_PDESVOLVH());
						impXTradeAgree.setXX_FIRSTVARVOLDISCOPERCEN(impICopd07.getXX_PDESVOLVP());
						impXTradeAgree.setXX_SECONDVARVOLDISCOFROM(impICopd07.getXX_SDESVOLVD());
						impXTradeAgree.setXX_SECONDVARVOLDISCOTO(impICopd07.getXX_SDESVOLVH());
						impXTradeAgree.setXX_SECONDVARVOLDISCOPERCEN(impICopd07.getXX_SDESVOLVP());
						//impXTradeAgree.setXX_THIRDVARVOLDISCOFROM(impICopd07.getXX_TDESVOLVD());
						//impXTradeAgree.setXX_THIRDVARVOLDISCOTO(impICopd07.getXX_TDESVOLVH());
						//impXTradeAgree.setXX_THIRDVARVOLDISCOPERCEN(impICopd07.getXX_TDESVOLVP());	
						impXTradeAgree.setXX_DISCOUNTGOSPERCENTAGE(impICopd07.getXX_DESGOSP());
						impXTradeAgree.setXX_DISCOUNTGOSAMOUNT(impICopd07.getXX_DESGOSM());
						//impXTradeAgree.setXX_CREDITSTOREOPENING(impICopd07.getXX_DESINAT());
						//impXTradeAgree.setXX_TYPECREDITSTOREOPENING(impICopd07.getXX_TDESINAT());
						impXTradeAgree.setXX_DISCRECOGDECLPERCEN(impICopd07.getXX_PDESRECM());
						impXTradeAgree.setXX_DISCRECOGDECLAMOUNT(impICopd07.getXX_DESRECMM());
						impXTradeAgree.setXX_DISCAFTERSALEPERCEN(impICopd07.getXX_PDESPOSV());
						impXTradeAgree.setXX_DISCAFTERSALEAMOUNT(impICopd07.getXX_DESPOSVM());
						//impXTradeAgree.setXX_PARTICIBECOBROCHPERCEN(impICopd07.getXX_PDESPARF());
						//impXTradeAgree.setXX_PARTICIBECOBROCHAMOUNT(impICopd07.getXX_DESPARFM());
						impXTradeAgree.setXX_CENTRADISCDELIPERCEN(impICopd07.getXX_PDESCENE());
						impXTradeAgree.setXX_CENTRADISCDELIAMOUNT(impICopd07.getXX_DESCENEM());
						//impXTradeAgree.setXX_PAYMENTRULE(impICopd07.getXX_FORPAGA());
						//impXTradeAgree.setXX_PRODTRADEAGREEMENT(impICopd07.getXX_PROACU());
						impXTradeAgree.setXX_DATEVALIDEXCLFROM(impICopd07.getXX_FECEXCD());
						impXTradeAgree.setXX_DATEVALIDEXCLTO(impICopd07.getXX_FECEXCH());
						//impXTradeAgree.setXX_CURRENCYCODE(impICopd07.getXX_CODMON());
						impXTradeAgree.setXX_Observation(impICopd07.getXX_OBSERV());
						//impXTradeAgree.setXX_STATEREGISTER(impICopd07.getXX_ESTREG());
						impXTradeAgree.setXX_AGREEMENTCODE(impICopd07.getXX_CODACU());
						//impXTradeAgree.setXX_MARK(impICopd07.getXX_MARCA());

						//impXTradeAgree.setXX_CreatedOn(impICopd07.getXX_FECREG());
						//impXTradeAgree.setXX_CreatedBy(impICopd07.getXX_USRREG());
						
						//
						if(impXTradeAgree.save())
						{
							System.out.println("ENtro al if");
							log.finest("Insert/Update Prld01 - " +  impXTradeAgree.getXX_VCN_TradeAgreements_ID());
							noInsert++;
											
							impICopd07.setI_IsImported(true);
							impICopd07.setProcessed(true);
							impICopd07.setProcessing(false);
							impICopd07.save();
							/*if(impICopd07.save())
								System.out.println("ENtro al if2");
							else
								System.out.println("ENtro al else2");*//*
								
						}
						else
						{		
							//System.out.println("ENtro al else");
							rollback();
							noInsert--;						
							sql = new StringBuffer ("UPDATE I_XX_VCN_COPD07 i "
									+ "SET I_IsImported='E', I_ErrorMsg="+ts +"|| '")
									.append("Cannot Insert Copd07...")
									.append("' WHERE I_XX_VCN_COPD07_ID=").append(impICopd07.getI_XX_VCN_COPD07_ID());
							//System.out.println(sql);
								DB.executeUpdate(get_Trx(), sql.toString());
								//continue;							
						}

						commit();
						//						
								
			}//end while
			
			rs.close();
			pstmt.close();
			
		} //end try
		
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "", e);
			rollback();
		}	
			
			//	Set Error to indicator to not imported
			
			sql = new StringBuffer ("UPDATE I_XX_VCN_COPD07 "
				+ "SET I_IsImported='N', Updated=SysDate "
				+ "WHERE I_IsImported<>'Y'").append(clientCheck);
			no = DB.executeUpdate(get_Trx(), sql.toString());
			addLog (0, null, new BigDecimal (no), "@Errors@");
			addLog (0, null, new BigDecimal (noInsert), "@XX_VCN_TradeAgreements_ID@: @Inserted@");
			return "";
			
			*/
			
	}
	
	/*
	 * Metodo que obtiene todos los id de las categorias
	 */
	private Hashtable<Integer,Integer> loadCategorys(){
	
		Hashtable<Integer,Integer> ht = new Hashtable<Integer,Integer>();
		
		String sql = "SELECT XX_VMR_CATEGORY_ID, VALUE " +
					 "FROM XX_VMR_CATEGORY " +
					 "ORDER BY XX_VMR_CATEGORY_ID";

		ResultSet rs = null;
		PreparedStatement pstmt=null;
		try{
		
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				ht.put(rs.getInt("VALUE"), rs.getInt("XX_VMR_CATEGORY_ID"));
			}
			
			rs.close();
			pstmt.close();
		
		}catch (Exception e) {
			System.out.println("Error" + e.getMessage());
		}
		
		return ht;
	}
	
	/*
	 * Metodo que devuelve el id del proveedor
	 */
	private Integer getVendorID(String value){
			
		String sql = "SELECT C_BPARTNER_ID " +
					 "FROM C_BPARTNER " +
					 "WHERE VALUE = '" + value + "'";

		ResultSet rs = null;
		PreparedStatement pstmt=null;
		try{
		
			pstmt = DB.prepareStatement(sql, null);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				int o = rs.getInt("C_BPARTNER_ID");
				rs.close();
				pstmt.close();
				return o;
			}
			
			rs.close();
			pstmt.close();
		
		}catch (Exception e) {
			System.out.println("Error" + e.getMessage());
		}
		
		return null;
	}

}
