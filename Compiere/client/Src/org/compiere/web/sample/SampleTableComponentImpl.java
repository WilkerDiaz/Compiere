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
public class SampleTableComponentImpl implements ComponentImplIntf {

	/**
	 * 
	 */
	public SampleTableComponentImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.compiere.intf.ComponentImplIntf#getComponentVO()
	 */
	public ComponentVO getComponentVO() {
		ComponentVO cVO = new ComponentVO();
		cVO.componentType = ComponentType.TABLE_SINGLE;
		cVO.name = "Results Region";
		cVO.tableName = "C_BPartner";
		cVO.fieldVOs = new ArrayList<FieldVO>();
		FieldVO fVO_BPartner_ID = new FieldVO("C_BPartner_ID", "ID", DisplayTypeConstants.ID, false);
		fVO_BPartner_ID.IsReadOnly = true;
		cVO.fieldVOs.add(fVO_BPartner_ID);
		FieldVO fVO_BPartner_Name = new FieldVO("Name", "Name", DisplayTypeConstants.String, false);
		fVO_BPartner_Name.IsReadOnly = true;
		cVO.fieldVOs.add(fVO_BPartner_Name);
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

		TableModel result = new TableModel();

		if (countOnly) {
			try {
				PreparedStatement pstmt = DB.prepareStatement(
						"SELECT COUNT(*) FROM C_BPartner WHERE C_BPartner_ID = ?", (Trx) null);
				pstmt.setInt(1, windowCtx.getAsInt("C_BPartner_ID"));
				ResultSet rs = pstmt.executeQuery();
				int row = 0;
				while (rs.next()) {
					result.addRow(row, new String[] { rs.getString(1) });
					++row;
				}
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				PreparedStatement pstmt = DB.prepareStatement("SELECT C_BPartner_ID, Name FROM C_BPartner "
						+ queryVO.getWhereClause() + " ORDER BY " + sortCol + (asc ? "" : " DESC"), (Trx) null);
				int i = 1;
				for (Object param : queryVO.getParams()) {
					pstmt.setObject(i, param);
					++i;
				}

				ResultSet rs = pstmt.executeQuery();
				int row = 0;
				while (rs.next() && row < startRow + rowCount) {
					if (row >= startRow)
						result.addRow(row, new String[] { rs.getString(1), rs.getString(2) });
					++row;
				}
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
