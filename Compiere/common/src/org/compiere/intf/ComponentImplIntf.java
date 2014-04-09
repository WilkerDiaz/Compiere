package org.compiere.intf;

import org.compiere.common.*;
import org.compiere.vos.*;

/**
 * This is the interface to implement when you want to create a custom component
 * for a custom window.
 * 
 * @author gwu
 * 
 */
public interface ComponentImplIntf {

	/**
	 * Implement this method to populate and return a ComponentVO to describe
	 * the desired behavior for the component.
	 * <p>
	 * 
	 * The minimum fields in the ComponentVO that should be populated are:
	 * <ul>
	 * <li>componentType</li>
	 * <li>name</li>
	 * <li>tableName</li>
	 * <li>fieldVOs</li>
	 * </ul>
	 * 
	 * @return The ComponentVO that describes the behavior of the implemented
	 *         component.
	 */
	public abstract ComponentVO getComponentVO();

	/**
	 * Implement this method to return the result set for this component.
	 * 
	 * @param queryVO
	 *            The QueryVO passed in from the client.
	 * @param windowImpl
	 *            The WindowImplIntf that contains this component.
	 * @param windowCtx
	 *            The WindowCtx client-side window context passed in from the
	 *            client.
	 * @param fieldVO
	 *            The FieldVO of to the window tab field that initiated window.
	 *            This is used to to constrain the result set using the
	 *            FieldVO's validationCode.
	 * @param startRow
	 *            The zero-based starting row number.
	 * @param rowCount
	 *            The number of rows of results to retrieve.
	 * @param countOnly
	 *            Indicates whether only a record count is requested. If <b>true</b>,
	 *            the TableModel returned should be populated with only a single
	 *            row and single column containing the record count.
	 * @param asc
	 *            If <b>true</b>, sort in ascending order. Otherwise, sort in
	 *            descending order.
	 * @param sortCol
	 *            The zero-based column number to perform the sort on.
	 * @return The TableModel containing the result set for tabular result set
	 *         components, or <b>null</b> for SEARCH components.
	 */
	public abstract TableModel getQueryResults(QueryVO queryVO,
			WindowImplIntf windowImpl, WindowCtx windowCtx, FieldVO fieldVO,
			int startRow, int rowCount, boolean countOnly, boolean asc,
			int sortCol);

}
