package org.compiere.vos;

import java.io.*;
import java.util.*;

public class GoalDetailVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NodeVO node;
	public String nameLabel;
	public String valueLabel;
	
	public GoalDetailVO(){
		rows = new ArrayList<GoalDetailRowVO>();
	}
	
	public void addDetailRow(NodeVO node, String name, double value){
		rows.add(new GoalDetailRowVO(node, name, value));
	}

	public List<org.compiere.vos.GoalDetailVO.GoalDetailRowVO> rows;
	public double maxValue;
	
	public static class GoalDetailRowVO implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public GoalDetailRowVO(){
			
		}
		public GoalDetailRowVO(NodeVO node, String name, double value) {
			this.node = node;
			this.name = name;
			this.value = value;
		}
		public NodeVO node;
		public String name;
		public double value;
	}
}
