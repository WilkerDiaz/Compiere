package compiere.model;

import java.sql.ResultSet;

import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class X_M_Product extends org.compiere.model.X_M_Product{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3646518108657470596L;
	
    /** Standard Constructor
    @param ctx context
    @param M_Product_ID id
    @param trxName transaction
    */
    public X_M_Product (Ctx ctx, int M_Product_ID, Trx trxName)
    {
        super (ctx, M_Product_ID, trxName);
        
        /* The following are the mandatory fields for this object.
        
        if (M_Product_ID == 0)
        {
            setC_TaxCategory_ID (0);
            setC_UOM_ID (0);
            setIsBOM (false);	// N
            setIsDropShip (false);
            setIsExcludeAutoDelivery (false);	// N
            setIsInvoicePrintDetails (false);
            setIsPickListPrintDetails (false);
            setIsPurchased (true);	// Y
            setIsPurchasedToOrder (false);	// N
            setIsSelfService (true);	// Y
            setIsSold (true);	// Y
            setIsStocked (true);	// Y
            setIsSummary (false);
            setIsVerified (false);	// N
            setIsWebStoreFeatured (false);
            setM_Product_Category_ID (0);
            setM_Product_ID (0);
            setName (null);
            setProductType (null);	// I
            setValue (null);
            
        }
        */
        
    }
	
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trxName transaction
    */
    public X_M_Product (Ctx ctx, ResultSet rs, Trx trxName)
    {
        super (ctx, rs, trxName);
        
    }
    
    /** Set XX_LINEA_ID.
    @param XX_LINEA_ID Linea */
    public void setXX_LINEA_ID (int XX_LINEA_ID)
    {
        if (XX_LINEA_ID < 1) throw new IllegalArgumentException ("XX_LINEA_ID is mandatory.");
        set_Value ("XX_LINEA_ID", Integer.valueOf(XX_LINEA_ID));
        
    }
    
    /** Get XX_LINEA_ID.
    @return XX_LINEA_ID */
    public int getXX_LINEA_ID() 
    {
        return get_ValueAsInt("XX_LINEA_ID");
        
    }
    
    /** Set XX_DEPARTAMENTO_ID.
    @param XX_DEPARTAMENTO_ID Departamento */
    public void setXX_DEPARTAMENTO_ID (int XX_DEPARTAMENTO_ID)
    {
        if (XX_DEPARTAMENTO_ID < 1) throw new IllegalArgumentException ("XX_DEPARTAMENTO_IDD is mandatory.");
        set_Value ("XX_DEPARTAMENTO_ID", Integer.valueOf(XX_DEPARTAMENTO_ID));
        
    }
    
    /** Get XX_DEPARTAMENTO_ID.
    @return XX_DEPARTAMENTO_ID */
    public int getXX_DEPARTAMENTO_ID() 
    {
        return get_ValueAsInt("XX_DEPARTAMENTO_ID");
        
    }
    
    /** Set XX_SECCION_ID.
    @param XX_SECCION_ID Sección */
    public void setXX_SECCION_ID (int XX_SECCION_ID)
    {
        if (XX_SECCION_ID < 1) throw new IllegalArgumentException ("XX_SECCION_ID is mandatory.");
        set_Value ("XX_SECCION_ID", Integer.valueOf(XX_SECCION_ID));
        
    }
    
    /** Get XX_SECCION_ID.
    @return XX_SECCION_ID */
    public int getXX_SECCION_ID() 
    {
        return get_ValueAsInt("XX_SECCION_ID");
        
    }
    
    /** Set XX_CATEGORIA_ID.
    @param XX_CATEGORIA_ID Categoria */
    public void setXX_CATEGORIA_ID (int XX_CATEGORIA_ID)
    {
        if (XX_CATEGORIA_ID < 1) throw new IllegalArgumentException ("XX_CATEGORIA_ID is mandatory.");
        set_Value ("XX_CATEGORIA_ID", Integer.valueOf(XX_CATEGORIA_ID));
        
    }
    
    /** Get XX_CATEGORIA_ID.
    @return XX_CATEGORIA_ID */
    public int getXX_CATEGORIA_ID() 
    {
        return get_ValueAsInt("XX_CATEGORIA_ID");
        
    }
    
    /** Set XX_PRODUCTO_ID.
    @param XX_PRODUCTO_ID Producto sin consecutivo */
    public void setXX_PRODUCTO_ID (int XX_PRODUCTO_ID)
    {
        if (XX_PRODUCTO_ID < 1) throw new IllegalArgumentException ("XX_PRODUCTO_ID is mandatory.");
        set_Value ("XX_PRODUCTO_ID", Integer.valueOf(XX_PRODUCTO_ID));
        
    }
    
    /** Get XX_PRODUCTO_ID.
    @return XX_PRODUCTO_ID */
    public int getXX_PRODUCTO_ID() 
    {
        return get_ValueAsInt("XX_PRODUCTO_ID");
        
    }

}
