package org.compiere.vos;

import java.io.*;
import java.util.*;

public class TreeVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public List<TreeNodeVO> treeNodes = null;
	public List<TreeParentChildVO> parentChildRelations = null;
	public boolean isAllNodes = false;
	public int AD_Tree_ID = 0;

}
