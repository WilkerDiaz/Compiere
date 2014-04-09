<%@ include file="/WEB-INF/jspf/page.jspf" %>
<cws:priceList priceList_ID="0"/>
<html>
<!--
  - Author:  Jorg Janke
  - Version: $Id: index.jsp,v 1.3 2006/05/19 22:17:32 mdeaelfweald Exp $
  - Compiere ERP & CRM Smart Business Solution - Copyright (c) 1999-2003 Jorg Janke
  - - -
  - Web Store Index
  -->
<head>
<%@ include file="/WEB-INF/jspf/head.jspf" %>
<script type="text/javascript">
	function addProductToCart(lineNo) {
		var pform = document.forms['productsForm'];
		var delInput = 'del_' + lineNo;
		if (pform == null || pform[delInput] == null)
			return false;
		pform[delInput].value = '';
		pform.submit();
	}

</script>
<title><c:out value='${ctx.name}'/> - Welcome</title>
</head>
<body>
<div id="page">
<%@ include file="/WEB-INF/jspf/header.jspf" %>

<div id="main">	
    <%@ include file="/WEB-INF/jspf/menu.jspf" %>
    <%@ include file="/WEB-INF/jspf/vendor.jspf" %>
	<div id="content">
		   <form class="searchForm" action="productServlet" method="post" enctype="application/x-www-form-urlencoded" name="search" id="search">  
	       <div id="productSearch">
		       <label class="labelcell" for="SearchString">Product:&nbsp; </label>
		       <input name="SearchString" type="text" id="SearchString">
		       <cws:productCategoryList/>
		       <input type="submit" name="Submit" value="Search">
		       <br>
		       <c:if test='${priceList.notAllPrices}'>
		         Not all products are displayed. 
		         Enter search criteria to limit selection results.
		       </c:if>
		       <c:if test='${priceList.noLines}'>
		         <i>No Products found - enter Search criteria</i>
		       </c:if>    
	       </div>  
	    </form>  
	  <div id="newsBlurb">
	    <c:out value='${ctx.webParam2}' escapeXml='false'/>
	  </div>  
	  <br /><br /><br />     
      <form action="cartServlet" method="post" enctype="application/x-www-form-urlencoded" name="productsForm" id="products">
		<input name="act" value="a1" type="hidden"/> 
        <input name="M_PriceList_ID" type="hidden" value="<c:out value='${priceList.priceList_ID}'/>">
        <input name="M_PriceList_Version_ID" type="hidden" value="<c:out value='${priceList.priceList_Version_ID}'/>">
        
        <table class="contentTable">
        <!--
  - Sunny change
  - 
  
          <tr> 
            <th colspan="2" align="left">Product</th>
            <th>Description</th>
            <th><c:out value='${priceList.currency}'/>&nbsp;Price</th>
            <th>Quantity</th>
            <th>UOM</th>
            <th>&nbsp;</th>
            <th class="availProduct"><cws:message txt="Availability"/></th>
          </tr>
   -->
             
          <tr> 
            <th colspan="2" width="27%" align="left">Product</th>
            <th width="34%">Description</th>
            <th width="9%" style="text-align:center"><c:out value='${priceList.currency}'/>&nbsp;Price</th>
            <th width="9%" style="text-align:center">Quantity</th>
            <th width="5%" style="text-align:center">UOM</th>
            <th width="6%">&nbsp;</th>
            <th class="availProduct"><cws:message txt="Availability"/></th>
          </tr>
          
          <c:forEach items='${priceList.prices}' var='product' varStatus='status'> 
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
            <td class="<c:out value='${rowClass}' />">
            	<c:if test='${not empty product.imageURL}'><img src="<c:out value='${product.imageURL}'/>"></c:if>
            </td>
            <td class="<c:out value='${rowClass}' />">
            	<input name="prod_<c:out value='${status.count}'/>" type="hidden" value="<c:out value='${product.id}'/>"/>
            	<input name="del_<c:out value='${status.count}'/>" type="hidden" value="y"/>
            	<c:if test='${not empty product.descriptionURL}'><a href="<c:out value='${product.descriptionURL}'/>" target="pd"></c:if>
				<c:out value="${product.name}"/>
            	<c:if test='${not empty product.descriptionURL}'></a></c:if>
			</td>
            <td class="<c:out value='${rowClass}' />"><c:out value='${product.description}'/> <c:if test="${empty product.description}">&nbsp;</c:if></td>
            <td class="<c:out value='${rowClass}' /> amount"> <input name="Price_<c:out value='${product.id}'/>" type="hidden" value="<c:out value='${product.price}'/>"> 
              <fmt:formatNumber value='${product.price}' type="currency" currencySymbol="" /> </td>
            <td class="<c:out value='${rowClass}' /> quantity"> 
            <input name="qty_<c:out value='${status.count}'/>" type="text" id="qty_<c:out value='${product.id}'/>" value="1" size="5" maxlength="5"></td>
            <td class="<c:out value='${rowClass}' />"><c:out value='${product.uomName}'/>&nbsp;</td>
            <td class="<c:out value='${rowClass}' />"> 
            	<input name="Add_<c:out value='${product.id}'/>" type="button" id="Add_<c:out value='${product.id}'/>" value="Add"
            	 onclick="addProductToCart(<c:out value='${status.count}'/>);"/>
            </td>
            <td class="<c:out value='${rowClass}'/> availProduct"><!-- c:out value='$ {product.available}'/ -->&nbsp;</td>
          </tr>
          </c:forEach> 
        </table>
      </form>
      <!--  
	  <p><font size="-1">Price List: <c:out value='${priceList.name}'/>  (<c:out value='${priceList.priceCount}'/>) - <c:out value='${priceList.searchInfo}'/></font></p> 
	  -->
      <p>&nbsp;</p></div>
</div>
<%@ include file="/WEB-INF/jspf/footer.jspf" %>
</div></body>
</html>
