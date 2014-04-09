<%@ include file="/WEB-INF/jspf/page.jspf" %>
<html>
<!--
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<script type="text/javascript" src="cart.js"></script> 
<c:if test="${ctx.compiereWS == 'Y'}"> 
	<script type="text/javascript" src="compiere/cart.js"></script> 
</c:if>  
<title><c:out value='${ctx.name}'/> - Shopping Cart</title>
</head>
<body>
<div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>
<div id="main">
	<%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <div id="content">
      <h2>Shopping Cart</h2>      
      <c:if test='${empty webBasket}'> 
      <p>Empty Basket (timeout) - Please go back, refresh the page and add products 
        again.</p>
      </c:if>  
      <p id="msgBox"><b><c:out value='${webBasket.message}'/></b></p>
      <c:if test='${not empty webBasket}'> 
      <form action="cartServlet" method="post" enctype="application/x-www-form-urlencoded" 
      	name="cartForm" onsubmit="return validateCartForm()">
      	<input id="act" name="act" type="hidden" value="u"/>
      	<input id="agreeLicense" name="agreeLicense" type="hidden" value="n"/>
      	<input id="linesCount" name="act" type="hidden" 
      	  value="<c:out value='${webBasket.lineCount}'/>"/>
        <table class="contentTable">      
          <tr> 
            <th>Product</th>
            <th class="amount">Price</th>
            <th class="quantity">Quantity</th>
            <th class="amount">Total (<c:out value='${priceList.currency}'/>)</th>
            <th class="center">Remove</th>
          </tr>
          <c:forEach items='${webBasket.lines}' var='line' varStatus='status'> 
        	<jsp:useBean id="status" type="javax.servlet.jsp.jstl.core.LoopTagStatus" />
        	<c:choose>
        		<c:when test="<%= status.getCount() %2 == 0 %>">
	        		<c:set var="rowClass" value="evenRow"/>
        		</c:when>
        		<c:otherwise>
	        		<c:set var="rowClass" value="oddRow"/>
        		</c:otherwise>
        	</c:choose>          
        	<tr class="<c:out value='${rowClass}'/>" > 
            <td ><c:out value='${line.name}'/></td>
	    		<input id="inProdId<c:out value='${status.count}'/>" type="hidden"  size="10" 
		 		 name="prod_<c:out value='${status.count}'/>"
		 		 value="<c:out value='${line.m_Product_ID}'/>"
		 		 />
            <td class="amount"><fmt:formatNumber value='${line.price}' type="currency" currencySymbol=""/></td>
            <td class="quantity">
				<input type="text" maxlength="6" size="6" 
				 <c:if test='${line.readOnly}'>
				 	readonly="readonly"
				 </c:if>
		 		 onkeypress="return numeralsOnly(event)"
		 		 name="qty_<c:out value='${status.count}'/>"
		 		 value="<fmt:formatNumber value='${line.quantity}'/>"
			 	 />
            </td>
            <td class="amount"><fmt:formatNumber value='${line.total}' type="currency" currencySymbol=""/></td>
            <td class="center">
				<input type="checkbox"  
		 		 name="del_<c:out value='${status.count}'/>"
		 		 value="y"
			 	 />
            </td>
          </tr>
          </c:forEach> 
          <tr> 
            <th>&nbsp;</th>
            <th>&nbsp;</th>
            <th>&nbsp;</th>
            <th class="amount">
              <fmt:formatNumber value='${webBasket.total}' type="currency" currencySymbol=""/>
            </th>
            <th>
            
            </th>
          </tr>
          <tr>
          	<td colspan="3" >
          	</td>
          	<td colspan="1">				    	
          	</td>
          	<td colspan="1" >
	    	</td>
          </tr>
        </table>
      </form>
      </c:if>
	  <table style="width: 100%;">	        
          <tr>
          	<td style="width: 5em;">
          		<input class="bluebutton" type="button" name="update" value="Update" onClick="updateCart()"/>	
          	</td>
          	<td>				    	
      			<cws:homeLink type="button" styleClass="bluebutton" label="Continue Shopping"/>	
          	</td>
          	<td align="right">
	  			<input class="bluebutton" type="button" name="Checkout" id="Checkout" value="Check Out" onClick="startCheckout()"/>
	    	</td>
          </tr>
        </table>	        
      </div>
  </div>
 <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div>
<div id="licensePopup" class="modalPage" style="display: none;">
    <div class="modalBackground">
    </div>    
    <div class="modalContainer">
        <div class="modal">
            <div class="modalBody">
                <textarea rows="16" cols="68" readonly="readonly"><%@ include file="/compiere/license.txt" %>
                </textarea>
			    <!--  iframe src ="http://www.compiere.com/terms/csa/csa.pdf" width="100%" height="300"/ -->
			    
                <p><input id="cbRead" type="checkbox" onclick="checkLicense();"
                	/>I have read and agree to the license agreement. [<a href="http://www.compiere.com/terms/csa/csa.pdf" target="_blank">Printable Format</a>]</p>
				<p>
					<input id="btnAgree" type="button" value="Agree" onclick="acceptLicense();"
						disabled="true"/>
					<input type="button" value="Cancel" onclick="hideModal('licensePopup');"/>
				</p>
			</div>            
		</div>            
	</div>
</div>
</body>
</html>
