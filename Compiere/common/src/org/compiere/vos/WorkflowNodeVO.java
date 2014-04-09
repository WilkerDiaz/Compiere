package org.compiere.vos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class WorkflowNodeVO implements Serializable 
{
	
	private static final long serialVersionUID = 4396318959152685789L;

	public WorkflowNodeVO()
	{
		super();
	}

	public WorkflowNodeVO(int client, int id, String nName, String desc, int x, int y, 
			Set<WorkflowNodeTransitionVO> nextNodes)
	{
		this(client, id, nName, desc, x, y, nextNodes, null, false);
	}

	public WorkflowNodeVO(int client, int id, String nName, String desc, int x, int y, 
			Set<WorkflowNodeTransitionVO> nextNodes, 
			Set<Integer> nextNodeIds, 
			boolean newNode)
	{
		super();
		setClientId(client);
		setNodeId(id);
		setName(nName);
		setDescription(desc);
		setPosX(x);
		setPosY(y);
		setTransitionVOs(nextNodes);
		setNew(newNode);
		setDeleted(false);
		setAction("Z");    //default: wait/sleep
		setSearchKey(nName);
	}
	
	// clone constructor
	public WorkflowNodeVO (WorkflowNodeVO vo) {
		
		setClientId(vo.AD_Client_ID);
		setNodeId(vo.AD_WF_NODE_ID);
		setName(vo.name);
		setDescription(vo.description);
		setPosX(vo.posX);
		setPosY(vo.posY);
		setNew(vo.isNew);
		setDeleted(vo.isDeleted);
		setSearchKey(vo.searchKey);
		
		transitionVOs = new HashSet<WorkflowNodeTransitionVO>();
		for (WorkflowNodeTransitionVO next : vo.transitionVOs)
			transitionVOs.add(new WorkflowNodeTransitionVO(next));
		
	}
	
	private int AD_Client_ID;
	private int AD_WF_NODE_ID;
	private String name;
	private String searchKey;
	private String description;
	
	private int posX;
	private int posY;
	
	private boolean isNew;
	private boolean isDeleted;
	
	private String action;
	private int targetId;
	
	private Set<Integer> nextNodeIds = null;
	private Set<WorkflowNodeTransitionVO> transitionVOs;
	
	
	
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	public int getClientId() {
		return AD_Client_ID;
	}

	public void setClientId(int client_ID) {
		AD_Client_ID = client_ID;
	}

	public int getNodeId() {
		return AD_WF_NODE_ID;
	}

	public void setNodeId(int workflowNode_ID) {
		AD_WF_NODE_ID = workflowNode_ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null)
			this.name = "";
		this.name = name;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public Set<WorkflowNodeTransitionVO> getTransitionVOs() {
		return transitionVOs;
	}

	public void setTransitionVOs(Set<WorkflowNodeTransitionVO> transitions) {
		this.transitionVOs = transitions;
		this.nextNodeIds = null;
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

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getSearchKey() {
		return searchKey;
	}
	
	public Set<Integer> getNextNodeIds() {
		if (nextNodeIds == null) {
			nextNodeIds = new HashSet<Integer>();
			for (WorkflowNodeTransitionVO line : transitionVOs) {
				nextNodeIds.add(line.getNextNodeId());
			}
		}
		return nextNodeIds;
	}
	
	public void setNextNodeIds(Set<Integer> nextIds) {
		nextNodeIds = nextIds;
	}

	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}

	public int getTargetId() {
		return targetId;
	}
	
	transient private NodeVO nodeVO;
	
	public NodeVO getNodeVO() {
		if (nodeVO == null) {
			nodeVO = new NodeVO();
			nodeVO.entityID = getTargetId();
			//
			if ("B".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_WORKBENCH;
			else if ("C".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_USERCHOICE;
			else if ("D".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_DOCACTION;
			else if ("F".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_WORKFLOW;
			else if ("M".equals(getAction())) //email
				nodeVO.nodeType = NodeVO.TYPE_OTHER;
			else if ("P".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_PROCESS;
			else if ("R".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_REPORT;
			else if ("T".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_TASK;
			else if ("V".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_SETVARIABLE;
			else if ("W".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_WINDOW;
			else if ("X".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_FORM;
			else if ("Z".equals(getAction()))
				nodeVO.nodeType = NodeVO.TYPE_OTHER; //wait
			else
				nodeVO.nodeType = NodeVO.TYPE_OTHER;
			
		}
		return nodeVO;
	}

}
