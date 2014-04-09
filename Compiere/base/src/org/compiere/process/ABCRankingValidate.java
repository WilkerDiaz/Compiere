package org.compiere.process;
import java.math.BigDecimal;

import org.compiere.model.MABCAnalysisGroup;
import org.compiere.model.MABCRank;
import org.compiere.model.X_M_ABCRank;
import org.compiere.model.X_M_ABCRankSort;

public class ABCRankingValidate extends SvrProcess{
	
	private int p_M_ABCAnalysisGroup_ID= 0;
	@Override
	protected void prepare ()
	{
		p_M_ABCAnalysisGroup_ID = getRecord_ID();
	}
	
	@Override
	protected String doIt() throws Exception
	{
		if (p_M_ABCAnalysisGroup_ID ==0 )
			return "Nothing to do";
		
		MABCAnalysisGroup group = new MABCAnalysisGroup(getCtx(),p_M_ABCAnalysisGroup_ID,get_Trx());
		group.setProcessing(true);
		
		if(group.getDaysForAnalysis()<=0)
		{
			group.setIsValid(false);
			group.setProcessing(false);
			group.save(get_Trx());
			return "Analysis Days not defined";
		}
		
		// get Lines of the rank
		MABCRank lines[] = MABCRank.getLines(getCtx(), group, get_Trx());

		// If there are no lines for the rank, mark the rank is Not Valid
		if(lines.length == 0)
		{
			group.setIsValid(false);
			group.setProcessing(false);
			group.save(get_Trx());
			return "No Rank Lines";
		}	
		
		// Check the sum of percentage of all lines is hundred or not
		BigDecimal percentage = BigDecimal.ZERO;
		for(MABCRank line :lines)
		{
			percentage = percentage.add(line.getPercentage());
		}
		
		if(percentage.intValue() != 100)
		{
			group.setIsValid(false);
			group.setProcessing(false);
			group.save(get_Trx());
			return "Total percentage of Lines is not 100";
		}
			
		// Verify that cycle count duration is properly setup
		for(MABCRank line:lines)
		{
			if(line.getDuration().intValue()==0)
			{
				group.setIsValid(false);
				group.setProcessing(false);
				group.save(get_Trx());
				return "Cycle Count frequenct is not properly setup";
			}
			if(line.getM_ABCRankSort_ID()==0)
				return "Sort rule not defined for rank line";
			
			X_M_ABCRankSort sort = new X_M_ABCRankSort(getCtx(),line.getM_ABCRankSort_ID(),get_Trx());
			if(sort.isBasedOnPriceList() && line.getM_PriceList_Version_ID()==0)
				return "Price List needs to be defined for the Rank '" + line.getABCRank() + "'";
			
			if(line.getFrequency().equals(X_M_ABCRank.FREQUENCY_Daily))
			{
				if(line.getDuration().compareTo(BigDecimal.ONE)>0)
				{
					group.setIsValid(false);
					group.setProcessing(false);
					group.save(get_Trx());
					return "Cycle count frequency can't be more then One per Day";
				}
			}
			else if (line.getFrequency().equals(X_M_ABCRank.FREQUENCY_Weekly))
			{
				if(line.getDuration().compareTo(new BigDecimal(7))>0)
				{
					group.setIsValid(false);
					group.setProcessing(false);
					group.save(get_Trx());
					return "Cycle count frequency can't be more then One per Day";
				}
			}
			else if (line.getFrequency().equals(X_M_ABCRank.FREQUENCY_Monthly))
			{
				if(line.getDuration().compareTo(new BigDecimal(31))>0)
				{
					group.setIsValid(false);
					group.setProcessing(false);
					group.save(get_Trx());
					return "Cycle count frequency can't be more then One per Day";
				}
			}
			
			else if(line.getFrequency().equals(X_M_ABCRank.FREQUENCY_Quarterly))
			{
				if(line.getDuration().compareTo(new BigDecimal(92))>0)
				{
					group.setIsValid(false);
					group.setProcessing(false);
					group.save(get_Trx());
					return "Cycle count frequency can't be more then One per Day";
				}
			}
			
			else if(line.getFrequency().equals(X_M_ABCRank.FREQUENCY_Yearly))
			{
				if(line.getDuration().compareTo(new BigDecimal(366))>0)
				{
					group.setIsValid(false);
					group.setProcessing(false);
					group.save(get_Trx());
					return "Cycle count frequency can't be more then One per Day";
				}
			}

		}
	
		group.setIsValid(true);
		group.setProcessing(false);
		group.save(get_Trx());
		return "@IsValid@: This Analysis Group has been successfully validated";
	}
	

}
