package compiere.model.cds;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MAttributeValue extends org.compiere.model.MAttributeValue{

	private static final long serialVersionUID = 1L;
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttributeValue.class);
	
	/**
	 * 	Constructor
	 *	@param ctx context
	 *	@param M_AttributeValue_ID id
	 *	@param trxName transaction
	 */
	public MAttributeValue (Ctx ctx, int M_AttributeValue_ID, Trx trx)
	{
		super (ctx, M_AttributeValue_ID, trx);
	}	//	MAttributeValue

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MAttributeValue (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAttributeValue
    
    /** Set Value.
    @param Number identifier of the entity */
    public void setValue (int CharType)  //// CDS ////
    {
    	if (CharType == 0) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", new BigDecimal(CharType));
        
    }
    
    /** Get Characteristic Type.
    @return Alphanumeric identifier of the entity */
    public String getValue() 
    {
        return get_ValueAsString("Value");
        
    }
    
    /**
	 * 	Before Save
	 *	@param newRecord new
	 *	@return 
	 */
	@Override
	protected boolean beforeSave (boolean newRecord){
		
		String sql_AttValue = "";
		String sql_AttName = "";
		String sql_Att = "";
		ResultSet rs = null;
		ResultSet rsId = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtId = null;
		Integer attValue = 0;
		String attName= getName().toUpperCase();
		String attDesc= getDescription();
		
		int attId = getM_Attribute_ID();
		
		setName(attName);
		if (attDesc == (null)){
			setDescription(attName);
			
		}else{
			setDescription(attDesc.toUpperCase());
		}
		
		sql_AttValue = "SELECT MAX(CAST(Value AS INT)) " + 
			"FROM M_AttributeValue WHERE "+
			"M_Attribute_ID = "+attId+"";
		
		sql_AttName = "SELECT M_AttributeValue_ID, Name " +
			   "FROM M_AttributeValue "+
			   "WHERE Name = '"+attName+"' "+
			   "AND M_Attribute_ID = '"+attId+"' ";
		
		sql_Att = "SELECT M_AttributeValue_ID" +
		   "FROM M_AttributeValue "+
		   "WHERE M_AttributeValue_ID = "+getM_AttributeValue_ID()+"";
		
		
			try {
				pstmt = DB.prepareStatement(sql_AttName, null);
				rs = pstmt.executeQuery();
				if(rs.next()){
					if(rs.getInt(1)!= (getM_AttributeValue_ID())){
						log.saveError("Error", Msg.getMsg(getCtx(), "M_AttributeValueName"));
						return false;
					}
				}else{
					//pstmtId = DB.prepareStatement(sql_Att, null);
					//rsId = pstmtId.executeQuery();
					//if(!rsId.next()){
						pstmtId = DB.prepareStatement(sql_AttValue, null);
						rsId = pstmtId.executeQuery();
						if(rsId.next()){
							attValue = (rsId.getInt(1));
							attValue++;
							if(attValue < 10){
								setValue("00"+attValue.toString());
							}else if(attValue < 100){
								setValue("0"+attValue.toString());
							}else{
								setValue(attValue.toString());
							}
						}
					//}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			} finally 
			{
				DB.closeResultSet(rsId);
				DB.closeStatement(pstmtId);
				DB.closeResultSet(rs);
				DB.closeStatement(pstmt);
			}
		
			return true;
	}//beforeSave
}
