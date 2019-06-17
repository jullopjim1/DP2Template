<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<img src="<jstl:out value='${company.photo}' />"
	alt="<jstl:out value='${company.photo}' />" />

<p>
	<spring:message code="company.name" />
	:
	<jstl:out value="${company.name}" />
</p>
<p>
	<spring:message code="company.surname" />
	:
	<jstl:out value="${company.surname}" />
</p>
<p>
	<spring:message code="company.email" />
	:
	<jstl:out value="${company.email}" />
</p>
<p>
	<spring:message code="company.phone" />
	:
	<jstl:out value="${company.phone}" />
</p>
<p>
	<spring:message code="company.address" />
	:
	<jstl:out value="${company.address}" />
</p>
<p>
	<spring:message code="company.spammer" />
	:
	<jstl:choose>
		<jstl:when test="${company.spammer}">
			<jstl:out value="${company.spammer}" />
		</jstl:when>
		<jstl:otherwise>
			<jstl:out value="N/A" />
		</jstl:otherwise>
	</jstl:choose>
</p>
<p>
	<spring:message code="company.banned" />
	:
	<jstl:out value="${company.banned}" />
</p>
<p>
	<spring:message code="company.companyname" />
	:
	<jstl:out value="${company.comercialName}" />
</p>
<p>
	<spring:message code="company.vatnumber"/>
	:
	<jstl:out value="${company.VATNumber}" />
</p>
<p>
	<spring:message code="company.score"/>
	:
	<jstl:if test="${company.score == null}">
		<jstl:out value="N/A" />
	</jstl:if>
	<jstl:if test="${company.score != null}">
		<jstl:out value="${company.score}" />
	</jstl:if>
</p>


<jstl:if test="${isPrincipalAuthorizedEdit}">
	<a
		href="company/company/edit.do?companyId=${company.id}"><spring:message
			code="company.edit" /></a>


</jstl:if>

	<acme:cancel url="/position/list.do" code="company.backPosition" />
	<acme:cancel url="/company/any/list.do" code="company.backCompanies" />



