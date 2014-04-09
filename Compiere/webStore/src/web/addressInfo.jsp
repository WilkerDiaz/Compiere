<%@ include file="/WEB-INF/jspf/page.jspf" %>
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: addressInfo.jsp,v 1.2 2006/05/06 00:41:33 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Address Info
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<title><c:out value='${ctx.name}'/> - My Address Info</title>
</head>
<body>
<div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
	<div id="content">	
	<h2>Address Confirmation</h2>	      
	<div id="formstyles">
      <form action="loginServlet" method="post" enctype="application/x-www-form-urlencoded" name="addressForm" id="address"
	    onSubmit="checkForm(this, new Array ('Name','Address'));">	    	    
	    <a class="mandatory">* </a>Required field.         
        <input name="Mode" type="hidden" value="Submit"/>
        <br /><br />
        <div class="left">
        	<label class="labelcell" id="LBL_Name" for="Name">Name:</label><a class="mandatory">*</a>
        	<div class="right">        
        		<input class="fieldcell" size="30" id="ID_Name" value='<c:out value="${webUser.name}"/>' name="Name" maxlength="60" type="text"/>
        	</div>
        <br/>
        </div>
        <div class="left">
			<label class="labelcell" id="LBL_Company" for="Company">Company:</label>
			<div class="right"> 
				<input class="fieldcell" size="30" id="ID_Company" value='<c:out value="${webUser.company}"/>' name="Company" maxlength="60" type="text"/>
		    </div>
		<br/>
		</div>
		
		<div class="left">
			<label class="labelcell" id="LBL_Title" for="Title">Title:</label>
			<div class="right">
				<input class="fieldcell" size="30" id="ID_Title" value='<c:out value="${webUser.title}"/>' name="Title" maxlength="60" type="text"/>
			</div>
		<br/>
		</div>

        <div class="left">
			<label class="labelcell" id="LBL_Address" for="Address">Address:</label>
			<a class="mandatory">*</a>
			<div class="right">
				<input class="fieldcell" size="30" id="ID_Address" value='<c:out value="${webUser.address}"/>' name="Address" maxlength="60" type="text"/>
			</div>	 
		<br/>
		</div>
     
        <div class="left">
			<label class="labelcell" id="LBL_Address2" for="Address">Address 2:</label>
			<div class="right">
				<input class="fieldcell" size="30" id="ID_Address2" value='<c:out value="${webUser.address2}"/>' name="Address2" maxlength="60" type="text"/>
			</div>	 
		<br/>
		</div>
        <div id="locationinfo">
			<cws:location countryID="${webUser.countryID}" regionID="${webUser.regionID}" regionName="${webUser.regionName}" city="${webUser.city}" postal="${webUser.postal}" /> 
		</div>
		
		<div class="left">
			<label class="labelcell" id="LBL_Phone" for="Phone">Phone:</label>
			<a class="mandatory">*</a>
			<div class="right">
				<input class="fieldcell" size="20" id="ID_Phone" value='<c:out value="${webUser.phone}"/>' name="Phone" maxlength="20" type="text"/>
			</div>	
		<br/>
		</div>
		
		<div class="left">
			<label class="labelcell" id="LBL_Fax" for="Fax">Fax:</label>
			<div class="right">
				<input class="fieldcell" size="20" id="ID_Fax" value='<c:out value="${webUser.fax}"/>' name="Fax" maxlength="20" type="text"/>
            </div>				
		<br/>
		</div>
		<br style="clear:both"/>

		<input class="bluebutton" type="reset" name="Reset" value="Reset">
		<input name="AddressConfirm" type="hidden" id="AddressConfirm" value="Y">
		<input class="bluebutton" type="submit" name="Submit" id="Submit" value="Submit Info"> 

        <c:if test="${not empty webUser.saveErrorMessage}">
            <div class="error"><c:out value="${webUser.saveErrorMessage}"/></div>
        </c:if>
		<div id="processingDiv" style="display:none"><strong>Processing ...</strong></div>
        <br/><br />
      </form>
      </div>
      <p>&nbsp;</p>
    </div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
