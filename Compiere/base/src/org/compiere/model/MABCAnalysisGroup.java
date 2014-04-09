package org.compiere.model;

import java.sql.ResultSet;
import org.compiere.util.CLogger;
import org.compiere.util.Ctx;
import org.compiere.util.Trx;

public class MABCAnalysisGroup extends X_M_ABCAnalysisGroup{

		private static final long serialVersionUID = 1L;

		/**
		 * Logger
		 */
		private static CLogger	s_log	= CLogger.getCLogger (MABCAnalysisGroup.class);

		/**
		 * @param ctx
		 * @param M_ABCRank_ID
		 * @param trx
		 */
		public MABCAnalysisGroup(Ctx ctx, int MABCAnalysisGroup_ID, Trx trx) {
			super(ctx, MABCAnalysisGroup_ID, trx);

		}

		/**
		 * @param ctx
		 * @param rs
		 * @param trx
		 */
		public MABCAnalysisGroup(Ctx ctx, ResultSet rs, Trx trx) {
			super(ctx, rs, trx);
		}
		
		
}
