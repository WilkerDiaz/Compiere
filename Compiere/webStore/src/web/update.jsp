<%@ include file="/WEB-INF/jspf/page.jspf" %>
<c:if test='${empty webUser || !webUser.loggedIn}'>
  <c:redirect url='loginServlet?ForwardTo=update.jsp'/>
</c:if>
<html>
<!--
- Author: Jorg Janke
- Version: $Id: update.jsp,v 1.3 2006/05/07 18:11:51 jjanke Exp $
- Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
- - -
- Web Update
-->

<head>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>
    <title><c:out value='${ctx.name}'/> - Update</title>
    <script type="text/javascript">
        function validateEMail()
        {
            if(document.UpdateEMail.EMail.value == document.UpdateEMail.EMailNew.value)
            {
                alert("Change of e-mail requires old and new e-mail addresses.")
                return false;
            }
            if(document.UpdateEMail.EMailNew.value != document.UpdateEMail.EMailConfirm.value)
            {
                alert("New e-mail address and confirm new e-mail address do not match.");
                return false;
            }

            return true;
        }
        function validatePassword()
        {
            if(document.UpdatePassword.Password.value == document.UpdatePassword.PasswordNew.value)
            {
                alert("Change of password requirea old and new passwords.");
                return false;
            }
            if(document.UpdatePassword.PasswordNew.value != document.UpdatePassword.PasswordConfirm.value)
            {
                alert("New password and confirm new password do not match.");
                return false;
            }

            return true;
        }
        function validateAddress()
        {
            return true;
        }
    </script>
</head>

<body><div id="page">
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <div id="main">
        <%@ include file="/WEB-INF/jspf/menu.jspf" %>
        <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
        <div id="content">
            <h2>User Information</h2>
            <div id="formstyles">
            <form action="updateServlet" method="post" enctype="application/x-www-form-urlencoded" name="UpdateEMail" target="_top" onSubmit="return validateEMail()">
                <input name="AD_Client_ID" type="hidden" value='<c:out value="${initParam.#AD_Client_ID}" default="0"/>'/>
                <input name="Source" type="hidden" value=""/>
                <input name="Info" type="hidden" value=""/>
                <input name="Mode" type="hidden" value="email"/>
                <script language="Javascript">
                    document.UpdateEMail.Source.value = document.referrer;
                    document.UpdateEMail.Info.value = document.lastModified;
                </script>
                
                <a class="mandatory">* </a>Required field.  
                <br /><br />
               
                <div class="left">
                    <label class="labelcell">Old e-mail:</label>
                    <a class="mandatory">*</a>
                    <div class="right">
	                    <input class="fieldcell" size="40" value='<c:out value="${webUser.email}"/>' name="EMail" maxlength="60" type="text"/>	                    
                    </div>
                    <br/>
                </div>
               
                <div class="left">
            		<div class="right"> 
	            	<a class="labelcell">E-mail verified</a>
	            	<input name="validated" type="checkbox" id="validated" value="validated" disabled
	                <c:if test='${webUser.EMailVerified}'> checked</c:if>>
	                </div>  
                	<br/>             	                
            	</div>
               
				<div class="left">
					<label class="labelcell">New e-mail:</label>
                    <a class="mandatory">*</a>
                    <div class="right">
                    	<input class="fieldcell" size="40" value='' name="EMailNew" maxlength="60" type="text" />
                    </div>
                    <br/>				 
				 </div>
				
				 <div class="left">				 	
                    <label class="labelcell">New e-mail confirmation:</label>
                    <a class="mandatory">*</a>
                    <div class="right">
                    	<input class="fieldcell" size="40" value='' name="EMailConfirm" maxlength="60" type="text"/>
                    </div>
                    <br/>
				 </div>
				 
                 <c:if test="${not empty webUser.saveErrorMessage}">
                 	<div id="errorLocation">
                    	<div class="error"><c:out value="${webUser.saveErrorMessage}"/></div>
                    </div>
                 </c:if>
				
				<br /><br />
                 <input class="bluebutton" type="reset" name="Reset" value="Reset">
                 <input class="bluebutton" name="AddressConfirm" type="hidden" value="N">
                 <input class="bluebutton" type="submit" name="Submit" value="Submit new e-mail address"/>
            </form>
			<br /><br />
            <form action="updateServlet" method="post" enctype="application/x-www-form-urlencoded" name="UpdatePassword" target="_top" onSubmit="return validatePassword()">
                    <input name="AD_Client_ID" type="hidden" value='<c:out value="${initParam.#AD_Client_ID}" default="0"/>'/>
                    <input name="Source" type="hidden" value=""/>
                    <input name="Info" type="hidden" value=""/>
                    <input name="Mode" type="hidden" value="password"/>
                    <script language="Javascript">
                        document.UpdatePassword.Source.value = document.referrer;
                        document.UpdatePassword.Info.value = document.lastModified;
                    </script>
				<div class="left">
	                <label class="labelcell" id="LBL_Password" for="Password">
	                Old Password:</label>
	                <a class="mandatory">*</a>
	                <div class="right">
		                <input class="fieldcell" size="20" type="password" id="ID_Password" value="" name="Password" maxlength="40"/>
		                <font color="#FF0000">&nbsp;<c:out value="${webUser.passwordMessage}"/></font>
	                </div>
	                <br/>		
				</div>
				<div class="left">
	                <label class="labelcell" id="LBL_PasswordNew" for="PasswordNew">
	                New Password:</label>
	                <a class="mandatory">*</a>
	                <div class="right">
	                	<input class="fieldcell" size="20" id="ID_PasswordNew" value="" name="PasswordNew" maxlength="40" type="password"/>
	                </div>
	                <br/>
				</div>
				<div class="left">
	                <label class="labelcell" id="LBL_PasswordConfirm" for="PasswordConfirm">
	                New Password confirmation:</label>
	                <a class="mandatory">*</a>
	                <div class="right">
	                	<input class="fieldcell" size="20" id="ID_PasswordConfirm" value="" name="PasswordConfirm" maxlength="40" type="password"/>
	                </div>
	                <br/>
				</div>
	
	            <c:if test="${not empty webUser.saveErrorMessage}">
	                <div class="error"><c:out value="${webUser.saveErrorMessage}"/></div>
	            </c:if>
				
				<br /><br />
	            <input class="bluebutton" type="reset" name="Reset" value="Reset">
	            <input class="bluebutton" type="submit" name="Submit" value="Submit new password"/>
	 
            </form>
			<br /><br />
            <form action="updateServlet" method="post" enctype="application/x-www-form-urlencoded" name="UpdateAddress" target="_top" onSubmit="return validateAddress()">
                <input name="AD_Client_ID" type="hidden" value='<c:out value="${initParam.#AD_Client_ID}" default="0"/>'/>
                <input name="Source" type="hidden" value=""/>
                <input name="Info" type="hidden" value=""/>
                <input name="Mode" type="hidden" value="address"/>
                <script language="Javascript">
                    document.UpdateAddress.Source.value = document.referrer;
                    document.UpdateAddress.Info.value = document.lastModified;
                </script>
                 
				<div class="left">
                    <label class="labelcell" id="LBL_Name" for="Name">Name:</label>
                    <a class="mandatory">*</a>
                    <div class="right" class="fieldcell">
                    	<input size="40" id="ID_Name" value="<c:out value='${webUser.name}'/>" name="Name" maxlength="60" type="text"/>
                    </div>
                    <br/>
				</div>
				
				<div class="left">
                    <label class="labelcell" id="LBL_Company" for="Company">Company:</label>
                    <div class="right">
                    	<input class="fieldcell" size="40" id="ID_Company" value="<c:out value='${webUser.company}'/>" name="Company" maxlength="60" type="text"/>
                    </div>
                    <br/>
                </div>
				<div class="left">
                   <label class="labelcell" id="LBL_Title" for="Title">Title:</label>
                    <div class="right">
                    	<input class="fieldcell" size="40" id="ID_Title" value='<c:out value="${webUser.title}"/>' name="Title" maxlength="60" type="text"/>
                    </div>
                    <br/>				
				</div>
				<div class="left">
                    <label class="labelcell" id="LBL_Address" for="Address">Address:</label>
                    <a class="mandatory">*</a>
                    <div class="right" class="fieldcell">
                    	<input size="40" id="ID_Address" value="<c:out value='${webUser.address}'/>" name="Address" maxlength="60" type="text"/>
                    </div>
                    <br/>				
				</div>
				<div class="left">
                    <label class="labelcell" id="LBL_Address2" for="Address2">Address2: </label>
                    <div class="right" class="fieldcell">
                    	<input size="40" id="ID_Address2" value='<c:out value="${webUser.address2}"/>' name="Address2" maxlength="60" type="text"/>
                    </div>
                    <br/>				
				</div>
				
				<div id="locationinfo">
					<cws:location countryID='${webUser.countryID}' regionID='${webUser.regionID}' regionName='${webUser.regionName}'
                                  city='${webUser.city}' postal='${webUser.postal}'/>
                </div>
                                  
 				<div class="left"> 					
                    <label class="labelcell" id="LBL_Phone" for="Phone">Phone:</label>
                    <div class="right" class="fieldcell">
                    	<input size="20" id="ID_Phone" value='<c:out value="${webUser.phone}"/>' name="Phone" maxlength="20" type="text"/>
                    </div>
                    <br/> 				
 				</div>
 				<div class="left">
                    <label class="labelcell" id="LBL_Fax" for="Fax">Fax:</label>
                    <div class="right" class="fieldcell">
                    	<input size="20" id="ID_Fax" value='<c:out value="${webUser.fax}"/>' name="Fax" maxlength="20" type="text"/>
                    </div>
                    <br/> 				
 				</div>

                 <c:if test="${not empty webUser.saveErrorMessage}">
                     <div class="error"><c:out value="${webUser.saveErrorMessage}"/></div>
                 </c:if>

				<br /><br />
                <input class="bluebutton" type="reset" name="Reset" value="Reset" />
                <input name="AddressConfirm" type="hidden" id="AddressConfirm" value="N" />
                <input class="bluebutton" type="submit" name="Submit" id="Submit" value="Submit new contact info"/>
			</div>
            </form>
            <div id="processingDiv" style="display:none"><strong>Processing ...</strong></div>
            <br/>

        </div>
    </div>
    <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
