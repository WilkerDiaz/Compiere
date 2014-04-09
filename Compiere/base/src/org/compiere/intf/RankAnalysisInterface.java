package org.compiere.intf;

import org.compiere.model.MABCAnalysisGroup;

public abstract interface RankAnalysisInterface {
	/**
	 * Implement this method to return a list of Products in proper order corresponding to
	 * the custom rule.
	 * 
	 * @param int M_Warehouse_ID
	 * @param MABCRankLine line
	 * @param int cycleCount
	 * @return MABCProductAssignment[] List of Products
	 */
	public abstract Integer[] rankAnalysis(MABCAnalysisGroup group, int M_PriceList_Version_ID,int AnalysisDays);

}
