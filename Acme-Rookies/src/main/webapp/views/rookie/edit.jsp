<%--
 * action-2.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" uri="/WEB-INF/tags" %>

<jstl:if test="${isPrincipalAuthorizedEdit}">
		<form:form action="rookie/edit.do" method="post" id="formEdit"
			name="formEdit" modelAttribute="rookieForm">
			
			<form:hidden path="id" />
			<form:hidden path="version" />
			
			<acme:userAccount code="rookie.userAccount" />
			<acme:userAccount code="rookie.password" />
			
			<acme:textbox path="name" code="rookie.name" />
			<acme:textbox path="middleName" code="rookie.middleName" />
			<acme:textbox path="surname" code="rookie.surname" />
			<acme:textbox path="photo" code="rookie.photo" />
			<acme:textbox path="email" code="rookie.email" />
			<acme:textbox path="phone" code="rookie.phone" />
			<acme:textbox path="VATNumber" code="rookie.vatnumber" />
			
			
		</form:form>
	
</jstl:if>