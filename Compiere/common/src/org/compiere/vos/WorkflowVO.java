package org.compiere.vos;

import java.util.HashSet;
import java.util.Set;

public class WorkflowVO extends ResponseVO 
{
	
	private static final long serialVersionUID = -7892260244132317562L;

	public WorkflowVO()
	{
		super();
	}
	
	// clone constructor
	public WorkflowVO(WorkflowVO vo) {
		setClientId(vo.AD_Client_ID);
		setWorkflowId(vo.AD_Workflow_ID);
		setName(vo.name);
		setDescription(vo.description);
		nodes = new HashSet<WorkflowNodeVO>();
		for (WorkflowNodeVO node : vo.nodes) {
			WorkflowNodeVO copy = new WorkflowNodeVO(node);
			nodes.add(copy);
		}
	}
	
	
	private int AD_Client_ID = 0;
	private int AD_Workflow_ID = 0;
	private String name;
	private String description;
	
	private Set<WorkflowNodeVO> nodes;
	
	public int getClientId() {
		return AD_Client_ID;
	}

	public void setClientId(int client_ID) {
		AD_Client_ID = client_ID;
	}

	public int getWorkflowId() {
		return AD_Workflow_ID;
	}

	public void setWorkflowId(int workflow_ID) {
		AD_Workflow_ID = workflow_ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null)
			this.name = "";
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null)
			this.description = "";
		else
			this.description = description;
	}
	
	public Set<WorkflowNodeVO> getNodeVOs() {
		return nodes;
	}
	
	public void setNodeVOs(Set<WorkflowNodeVO> nodeVOs) {
		nodes = nodeVOs;
	}
	
	
}
