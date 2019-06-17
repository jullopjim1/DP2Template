<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:hidden path="id" />
<form:hidden path="version" />
<form:hidden path="userAccount" />

<acme:out code="rookie.name" value="${rookie.name}"/>
<acme:out code="rookie.middleName" value="${rookie.middleName}"/>
<acme:out code="rookie.surname" value="${rookie.surname}"/>
<acme:out code="rookie.photo" value="${rookie.photo}"/>
<acme:out code="rookie.email" value="${rookie.email}"/>

<acme:out code="rookie.phone" value="${rookie.phone}"/>
<acme:out code="rookie.address" value="${rookie.address}"/>

<security:authorize access="hasRole('ADMIN')">
<jstl:choose>
	<jstl:when test="${rookie.spammer}">
		<jstl:out value="${rookie.spammer}" />
	</jstl:when>
	<jstl:otherwise>
		<jstl:out value="N/A" />
	</jstl:otherwise>
</jstl:choose>
<acme:out code="rookie.score" value="${rookie.score}"/>
<acme:out code="rookie.vatnumber" value="${rookie.VATNumber}" />
</security:authorize>




<security:authentication property="principal.username" var="username" />
	<jstl:if
		test='${customer.userAccount.username == username || customer.id == 0}'>
		
<input type="button" name="edit" value="<spring:message code="rookie.edit"></spring:message>" onclick="javascript:relativeRedir('rookie/any/edit.do?rookieId=${rookie.id}')"/>	
	</jstl:if>

<input type="button" name="cancel" value="<spring:message code="rookie.cancel"></spring:message>" onclick="javascript:relativeRedir('rookie/any/list.do')" />	










