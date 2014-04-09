package compiere.model.bank;

import java.sql.*;
import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Trx;

public class MBankAccountDoc extends org.compiere.model.X_C_BankAccountDoc {

	public MBankAccountDoc(Ctx ctx, int C_BankAccountDoc_ID, Trx trx) {
		super(ctx, C_BankAccountDoc_ID, trx);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
    /** Get Is XX_VCN_CheckNumber.
    @return valor is XX_VCN_CheckNumber */
    public Integer getXX_VCN_CheckNumber()
    {
    	return get_ValueAsInt("XX_VCN_CheckNumber");
    }
    
    /** Set VCN_CheckNumber.
    @param XX_VCN_CheckNumber VCN_CheckNumber */
    public void setXX_VCN_CheckNumber (int XX_VCN_CheckNumber)
    {
        set_Value ("XX_VCN_CheckNumber", Integer.valueOf(XX_VCN_CheckNumber));
        
    }
    
    /** Get Is XX_VCN_Stateofchecksbook.
    @return valor is XX_VCN_Stateofchecksbook */
    public String getXX_VCN_Stateofchecksbook()
    {
    	return (String)get_Value("XX_VCN_Stateofchecksbook");
    }
	
    /** Set VCN_XX_VCN_Stateofchecksbook
    @param XX_VCN_Stateofchecksbook XX_VCN_Stateofchecksbook */
    public void setXX_VCN_Stateofchecksbook (String XX_VCN_Stateofchecksbook)
    {
        if (!X_Ref_XX_VCN_StatesofCheckbook.isValid(XX_VCN_Stateofchecksbook))
        throw new IllegalArgumentException ("XX_VCN_Stateofchecksbook Invalid value - " + XX_VCN_Stateofchecksbook + " - Reference_ID=1003751 - 1 - 2 - 3 - 4");
        set_Value ("XX_VCN_Stateofchecksbook", XX_VCN_Stateofchecksbook);
        
    }
    
    /** Get VCN_CausesofCancellation.
    @return VCN_CausesofCancellation */
    public String GetXX_VCN_Stateofchecksbook() 
    {
        return (String)get_Value("XX_VCN_Stateofchecksbook");
        
    }    
    
    
    /*
     * METODO QUE VA A VALIDAR SI HAY CHEQUERAS EN STATUS EN USO
     * (non-Javadoc)
     * @see org.compiere.framework.PO#afterSave(boolean, boolean)
     */
    protected boolean beforeSave (boolean newRecord)
	{	
    	boolean save = super.beforeSave(newRecord);
		if(save){
			if(getXX_VCN_Stateofchecksbook().equals("4")){
				return save;
			}
			else{
					//OBTENGO EL ID DE LA CUENTA BANCARIA
					int cuenta = get_ValueAsInt("C_BankAccount_ID");
					int total=0;
					
					//BUSCO SI EXISTE UNA CHEQUERA EN STATUS EN USO
					String sql2= 
							"SELECT COUNT(*) AS Uso"+
							" FROM C_BankAccount Acc, C_BankAccountDoc Che"+
							" WHERE Acc.C_BankAccount_ID = "+cuenta+ 
							" AND Acc.C_BankAccount_ID=Che.C_BankAccount_ID"+ 
							" AND Che.XX_VCN_Stateofchecksbook ='1'";
							
							PreparedStatement prst2 = DB.prepareStatement(sql2,null);
						   	ResultSet rs2 = null;
						   	
						   	//ejecuto la sentencia SQL
							   	try {
							   		rs2 = prst2.executeQuery();
							   		if (rs2.next()){
							   			total= rs2.getInt("USO");
							   		}// Fin if
							   	} 
							   	catch (Exception e){
									System.out.println(e);
								}
							   	finally {
									DB.closeResultSet(rs2);
									DB.closeStatement(prst2);
								}
				   	
					//VALIDO EL NUMERO DE CHEQUERAS EN STATUS EN USO		   
					if(total == 0){
						setXX_VCN_Stateofchecksbook("1");
					}
			}
		}//FIN IF (SAVE)
	return save;
	}//afterSave
	
}
