<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=expenses.jsp'/>
</c:if>
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: expenses.jsp,v 1.9 2006/05/26 20:06:25 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2004 Jorg Janke
  - - -
  - Web Store Expenses
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Expenses</title>
<script type="text/javascript">
function updateClientList()
{
    /**
     * call searchServlet
     * param: get = 'clientList'
     *
     * get back:
     * <clients>
	 *   <client id='0'>ANY</client>
	 *   <client id='1'>GardenWorld</client>
	 *   <client id='2'>ComPiere, Inc.</client>
	 * </clients>
     */

	var params = new Array();
	params['get']='clientList';
	var loader = new AJAX.AjaxLoader("searchServlet", updateClientListCallback, null, "GET", params);
}
function updateClientListCallback()
{
	updateSelect(this, "clients", "ID_C_Client_ID");
	updateOrgList();
}
addOnLoadListener(updateClientList);

function updatePartnerList()
{
    /**
     * call searchServlet
     * param: get = 'partnerList'
     *
     * get back:
     * <partners>
	 *   <partner id='0'>ANY</partner>
	 *   <partner id='1'>Joe Block</partner>
	 * </partners>
     */

	var params = new Array();
	params['get']='partnerList';
	var loader = new AJAX.AjaxLoader("searchServlet", updatePartnerListCallback, null, "GET", params);
}
function updatePartnerListCallback()
{
	updateSelect(this, "partners", "ID_C_Partner_ID");
}
addOnLoadListener(updatePartnerList);

function updateOrgList()
{
    var clientSelect = document.getElementById('ID_C_Client_ID');
    var clientOption = clientSelect.options[clientSelect.selectedIndex];
    var clientId = clientOption.attributes.getNamedItem('value').value;

    /**
     * call searchServlet
     * param: get = 'orgList'
     * param: clientID = '123'
     *
     * get back:
	 * <orgs clientID='123'>
	 *   <org id='0'>ANY</client>
	 *   <org id='1'>HQ</org>
	 *   <org id='2'>Sales</org>
	 *   <org id='3'>Support</org>
	 * </orgs>
     */

	var params = new Array();
	params['get']='orgList';
	params['clientID']=clientId;
	var loader = new AJAX.AjaxLoader("searchServlet", updateOrgListCallback, null, "GET", params);
}
function updateOrgListCallback()
{
	updateSelect(this, "orgs", "ID_C_Organization_ID");
}

function updateCampaignList()
{
    /**
     * call searchServlet
     * param: get = 'campaignList'
     *
     * get back:
     * <campaigns>
	 *   <campaign id='0'>ANY</campaign>
	 *   <campaign id='1'>Rose Festival</campaign>
	 * </campaigns>
     */

	var params = new Array();
	params['get']='campaignList';
	var loader = new AJAX.AjaxLoader("searchServlet", updateCampaignListCallback, null, "GET", params);
}
function updateCampaignListCallback()
{
	updateSelect(this, "campaigns", "ID_C_Campaign_ID");
}
addOnLoadListener(updateCampaignList);

function updateProjectList()
{
    /**
     * call searchServlet
     * param: get = 'projectList'
     *
     * get back:
     * <projects>
	 *   <project id='0'>ANY</project>
	 *   <project id='1'>Landscaping New Office</project>
	 * </projects>
     */

	var params = new Array();
	params['get']='projectList';
	var loader = new AJAX.AjaxLoader("searchServlet", updateProjectListCallback, null, "GET", params);
}
function updateProjectListCallback()
{
	updateSelect(this, "projects", "ID_C_Project_ID");
	updatePhaseList();
}
addOnLoadListener(updateProjectList);

function updatePhaseList()
{
    var projectSelect = document.getElementById('ID_C_Project_ID');
    var projectOption = projectSelect.options[projectSelect.selectedIndex];
    var projectID = projectOption.attributes.getNamedItem('value').value;

    /**
     * call searchServlet
     * param: get = 'phaseList'
     * param: projectID = '123'
     *
     * get back:
	 * <phases projectID='123'>
	 *   <phase id='0'>ANY</phase>
	 *   <phase id='1'>Planning</phase>
	 * </phases>
     */

	var params = new Array();
	params['get']='phaseList';
	params['projectID']=projectID;
	var loader = new AJAX.AjaxLoader("searchServlet", updatePhaseListCallback, null, "GET", params);
}
function updatePhaseListCallback()
{
	updateSelect(this, "phases", "ID_C_Phase_ID");
	updateTaskList();
}

function updateTaskList()
{
    var projectSelect = document.getElementById('ID_C_Project_ID');
    var projectOption = projectSelect.options[projectSelect.selectedIndex];
    var projectID = projectOption.attributes.getNamedItem('value').value;

    var phaseSelect = document.getElementById('ID_C_Phase_ID');
    var phaseOption = phaseSelect.options[phaseSelect.selectedIndex];
    var phaseID = phaseOption.attributes.getNamedItem('value').value;

    /**
     * call searchServlet
     * param: get = 'phaseList'
     * param: projectID = '123'
     * param: phaseID = '456'
     *
     * get back:
	 * <tasks projectID='123' phaseID='456'>
	 *   <task id='0'>ANY</task>
	 *   <task id='1'>Contact Owner</task>
	 * </tasks>
     */

	var params = new Array();
	params['get']='taskList';
	params['projectID']=projectID;
	params['phaseID']=phaseID;
	var loader = new AJAX.AjaxLoader("searchServlet", updateTaskListCallback, null, "GET", params);
}
function updateTaskListCallback()
{
	updateSelect(this, "tasks", "ID_C_Task_ID");
}

</script>
</head>
<body><div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
	<div id="content">
      <h2>My Expenses </h2>
      <c:if test='${not empty info.info}'>
	    <b><c:out value='${info.message}'/></b>
	  </c:if>
	  <a href="expense.jsp">New Expense</a>
	  <br /><br />
	  <a href="expense.jsp?W_Expense_ID=100002">Edit Most Recent Expense</a>
	  <br /><br />
	  
	      <c:choose>
    	<c:when test="${info.expenses}.size() > 0">
			<div class="tableNav">
				<img src="res/wfStart24.gif" onclick="alert('CLICK');">
				<img src="res/wfBack24.gif" onclick="alert('CLICK');">
				Page 1
				<img src="res/wfNext24.gif" onclick="alert('CLICK');">
				<img src="res/wfEnd24.gif" onclick="alert('CLICK');">
			</div>
		</c:when>
	</c:choose>
      <table class="contentTable">
        <tr> 
          <th class="left">Document #</th>
          <th class="left">Date</th>
          <th class="left">Description</th>
          <th class="amount">Amount</th>
          <th class="left">Approved</th>
        </tr>
        <c:forEach items='${info.expenses}' var='report' varStatus='status'> 
        	<jsp:useBean id="status" type="javax.servlet.jsp.jstl.core.LoopTagStatus" />
        	<c:choose>
        		<c:when test="<%= status.getCount() %2 == 0 %>">
	        		<c:set var="rowClass" value="evenRow"/>
        		</c:when>
        		<c:otherwise>
	        		<c:set var="rowClass" value="oddRow"/>
        		</c:otherwise>
        	</c:choose>
        <tr> 
          <td class="<c:out value='${rowClass}' /> left"><a href="expense.jsp?W_Expense_ID=<c:out value='${report.documentNo}'/>"><c:out value='${report.documentNo}'/></a>&nbsp;</td>
          <td class="<c:out value='${rowClass}' /> left"><fmt:formatDate value='${report.dateReport}'/>&nbsp;</td>
          <td class="<c:out value='${rowClass}' /> left"><c:out value='${report.description}'/>&nbsp;</td>
          <td class="<c:out value='${rowClass}' /> amount"><c:out value='${report.approvalAmt}'/>&nbsp;</td>
          <td class="<c:out value='${rowClass}' /> left">
          	<c:choose>
          		<c:when test='${report.approved}'>
        		  	<input type="checkbox" checked="checked" disabled="true" />
          		</c:when>
          		<c:otherwise>
		          	<input type="checkbox" disabled="true" />
          		</c:otherwise>
          	</c:choose>
          </td>
        </tr>
        </c:forEach> 
      </table>
    <c:choose>
    	<c:when test="${info.expenses}.size() > 0">
			<div class="tableNav">
				<img src="res/wfStart24.gif" onclick="alert('CLICK');">
				<img src="res/wfBack24.gif" onclick="alert('CLICK');">
				Page 1
				<img src="res/wfNext24.gif" onclick="alert('CLICK');">
				<img src="res/wfEnd24.gif" onclick="alert('CLICK');">
			</div>
		</c:when>
	</c:choose>
	<br /><br />
	  
	  <div id="formstyles">
      <form action="expenseServlet" method="post" enctype="application/x-www-form-urlencoded" name="search" id="search">
		<div class="left">
			<label class="labelcell">Client:</label>
			<div class="right">
				<select class="fieldcell" id="ID_C_Client_ID" name="ID_C_Client_ID" onchange="updateOrgList()">
					<option selected="selected">Any</option>
				</select>			
			</div>
			<br />
		</div>
		<div class="left">
			<label class="labelcell">Organization:</label>
			<div class="right">
				<select class="fieldcell" id="ID_C_Organization_ID" name="ID_C_Organization_ID">
					<option selected="selected">Any</option>
				</select>
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Business Partner:</label>
			<div class="right">
				<select class="fieldcell" id="ID_C_Partner_ID" name="ID_C_Partner_ID">
					<option selected="selected">Any</option>
				</select>
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Campaign:</label>
			<div class="right">
				<select class="fieldcell" id="ID_C_Campaign_ID" name="ID_C_Campaign_ID">
					<option selected="selected">Any</option>
				</select>
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell" >Start Date:</label>
			<div class="right">
				<input class="fieldcell" type="text" size="8"/>
				<!--  comment out calendar icon because it doesn't do anything -->
				<!-- <img src="res/Calendar16.gif" border="0"/>-->
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell" >End Date:</label>
			<div class="right">
				<input class="fieldcell"  type="text" size="8"/>
				<!--  comment out calendar icon because it doesn't do anything -->
				<!-- <img src="res/Calendar16.gif" border="0"/>-->
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Project:</label>
			<div class="right">
				<select class="fieldcell"  id="ID_C_Project_ID" name="ID_C_Project_ID" onchange="updatePhaseList()">
					<option selected="selected">Any</option>
				</select>
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell" >Phase:</label>
			<div class="right">
				<select class="fieldcell" id="ID_C_Phase_ID" name="ID_C_Phase_ID" onchange="updateTaskList()">
					<option>Any</option>
				</select>
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Task:</label>
			<div class="right">
				<select class="fieldcell" id="ID_C_Task_ID" name="ID_C_Task_ID">
					<option>Any</option>
				</select>
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Document Status:</label>
			<div class="right">
				<select class="fieldcell"  id="ID_C_Status_ID" name="ID_C_Status_ID">
					<option selected="selected">Any</option>
					<option>Drafted</option>
					<option>Submitted</option>
					<option>Approved</option>
				</select>
			</div>
			<br/>
		</div>
		<div class="left">
			<label class="labelcell">Amount:</label>
			<div class="right">
				<input class="fieldcell"  type="text"/>
				<!--  comment out calculator icon because it doesn't go anything -->
				<!-- <img src="res/Calculator16.gif" border="0"/>-->
			</div>
			<br/>		
		</div>
		<div class="left">
			<label class="labelcell">Text in Description:</label>
			<div class="right">
				<input class="fieldcell" type="text"/>
			</div>
			<br/>									
		</div>						

		<input class="bluebutton"  type="button" name="Search" value="Search Expenses"/>
		<input class="bluebutton" type="reset"/>
      </form>
      </div>
      <br/>

    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
