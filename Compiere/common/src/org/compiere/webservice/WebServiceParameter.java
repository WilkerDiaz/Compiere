package org.compiere.webservice;

import java.math.BigDecimal;

public class WebServiceParameter {
	
		private BigDecimal position;
		private String parameterName,parameterValue,operator,sortParameter,sortOperator=null;
		public String getOperator() {
			return operator;
		}
		public void setOperator(String operator) {
			this.operator = operator;
		}
		public String getParameterName() {
			return parameterName;
		}
		public void setParameterName(String parameterName) {
			this.parameterName = parameterName;
		}
		public String getParameterValue() {
			return parameterValue;
		}
		public void setParameterValue(String parameterValue) {
			this.parameterValue = parameterValue;
		}
		public BigDecimal getPosition() {
			return position;
		}
		public void setPosition(BigDecimal position) {
			this.position = position;
		}
		
		@Override
		public String toString(){
			return parameterName+","+parameterValue+","+operator;
		}
		public String getSortOperator() {
			return sortOperator;
		}
		public void setSortOperator(String sortOperator) {
			this.sortOperator = sortOperator;
		}
		public String getSortParameter() {
			return sortParameter;
		}
		public void setSortParameter(String sortParameter) {
			this.sortParameter = sortParameter;
		}
		
}
