<%--
 * create.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>




<form:form action="${requestURI}" modelAttribute="item">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="provider" />


	<acme:textbox code="item.name" path="name" />
	<acme:textbox code="item.description" path="description" />
	<acme:textarea code="item.link" path="link" />
	<acme:textarea code="item.pictures" path="pictures" />




	<input type="submit" name="save"
		value="<spring:message code="item.save"></spring:message>" />



	<jstl:if test="${item.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="item.delete" />"
			onclick="return confirm('<spring:message code="item.confirm.delete" />')" />&nbsp;
				</jstl:if>

	<acme:cancel url="item/provider/list.do" code="item.cancel" />
</form:form>



