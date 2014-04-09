package org.compiere.intf;
import org.compiere.model.MABCRank;

public interface RankSortInterface {

	/**
	 * Implement this method to return a list of Products in proper order corresponding to
	 * the custom rule.
	 * 
	 * @param int M_Warehouse_ID
	 * @param MABCRankLine line
	 * @param int cycleCount
	 * @return MABCProductAssignment[] List of Products
	 */
	public abstract Integer[] Sort(int M_Warehouse_ID, MABCRank line, int cycleCount);


}
