/**
 * 
 */
package org.compiere.web.sample;

import java.sql.*;
import java.util.*;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.intf.*;
import org.compiere.util.*;
import org.compiere.vos.*;
import org.compiere.vos.ComponentVO.*;

/**
 * @author gwu
 * 
 */
public class SampleSearchComponentImpl implements ComponentImplIntf {

	/**
	 * 
	 */
	public SampleSearchComponentImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.intf.ComponentImplIntf#getComponentVO()
	 */
	public ComponentVO getComponentVO() {
		ComponentVO cVO = new ComponentVO();
		cVO.componentType = ComponentType.SEARCH;
		cVO.name = "Search Region";
		cVO.tableName = "";
		cVO.fieldVOs = new ArrayList<FieldVO>();

		FieldVO fVO_BPartner = new FieldVO("C_BPartner_ID", "Business Partner", DisplayTypeConstants.ID, false);

		ArrayList<NamePair> bPartners = new ArrayList<NamePair>();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = DB.prepareStatement("SELECT C_BPartner_ID, Name FROM C_BPartner ORDER BY Name", (Trx) null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bPartners.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		fVO_BPartner.listBoxVO = new ListBoxVO(bPartners, null);
		cVO.fieldVOs.add(fVO_BPartner);

		FieldVO fVO_Search = new FieldVO("Search", "Search", DisplayTypeConstants.Button, false);
		cVO.fieldVOs.add(fVO_Search);
		return cVO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.intf.ComponentImplIntf#getQueryResults(org.compiere.common.QueryVO,
	 *      org.compiere.intf.WindowImplIntf, org.compiere.vos.WindowCtx,
	 *      org.compiere.vos.FieldVO, int, int, boolean, boolean, int)
	 */
	public TableModel getQueryResults(QueryVO queryVO, WindowImplIntf windowImpl, WindowCtx windowCtx, FieldVO fieldVO,
			int startRow, int rowCount, boolean countOnly, boolean asc, int sortCol) {
		return null;
	}

}
