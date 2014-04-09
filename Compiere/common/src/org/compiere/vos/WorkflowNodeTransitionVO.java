package org.compiere.vos;


public class WorkflowNodeTransitionVO extends ResponseVO 
{
	
	private static final long serialVersionUID = -3607553026209923813L;
	
	public WorkflowNodeTransitionVO()
	{
		super();
	}
	
	// clone constructor
	public WorkflowNodeTransitionVO(WorkflowNodeTransitionVO vo) {
		AD_Client_ID = vo.AD_Client_ID;
		AD_WF_Next_ID = vo.AD_WF_Next_ID;
		if (vo.description == null)
			description = "";
		else
			description = vo.description;
	}
	
	public int getClientId() {
		return AD_Client_ID;
	}

	public void setClientId(int client_ID) {
		AD_Client_ID = client_ID;
	}

	public int getNextNodeId() {
		return AD_WF_Next_ID;
	}

	public void setNextNodeId(int next_ID) {
		AD_WF_Next_ID = next_ID;
	}

	public int getTransitionId() {
		return AD_WF_NodeNext_ID;
	}

	public void setTransitionId(int nodeNext_ID) {
		AD_WF_NodeNext_ID = nodeNext_ID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private int AD_Client_ID = 0;
	private int AD_WF_Next_ID;
	private int AD_WF_NodeNext_ID;
	private String description;
	
}
