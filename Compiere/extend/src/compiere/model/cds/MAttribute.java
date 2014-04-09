package compiere.model.cds;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.util.Ctx;
import org.compiere.util.DB;
import org.compiere.util.Msg;
import org.compiere.util.Trx;

public class MAttribute extends org.compiere.model.MAttribute{
	
	private static final long serialVersionUID = 1L;
	private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MAttribute.class);
	
	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_Attribute_ID id
	 *	@param trxName transaction
	 */
	public MAttribute (Ctx ctx, int M_Attribute_ID, Trx trx)
	{
		super (ctx, M_Attribute_ID, trx);
	}	//	MAttribute

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MAttribute (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MAttribute
	
    /** Set Characteristic Type.
    @param Number identifier of the entity */
    public void setValue (String CharType)  //// CDS ////
    {
    	
    	if (CharType.equals("") ) throw new IllegalArgumentException ("Value is mandatory.");
        set_Value ("Value", CharType);
        
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
		
		String attName= getName().toUpperCase();
		String attDesc= getDescription();
		String sql = "";
		
		setName(attName);
		if (attDesc == (null)){
			setDescription(attName);
			
		}else{
			setDescription(attDesc.toUpperCase());
		}
		
		sql = "SELECT M_Attribute_ID " +
			   "FROM M_Attribute "+
			   "WHERE Name = '"+attName+"'";
			try {
				PreparedStatement pstmt = DB.prepareStatement(sql, null);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()){
					if(rs.getInt(1)!=(getM_Attribute_ID())){
						log.saveError("Error", Msg.getMsg(getCtx(), "M_AttributeName"));
						return false;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.saveError("ErrorSql", Msg.getMsg(getCtx(), e.getMessage()));
			}
			return true;
	}//beforeSave
    
}
