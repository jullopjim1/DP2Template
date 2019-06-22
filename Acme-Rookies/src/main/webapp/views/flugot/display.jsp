<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jstl:if test="${flugot.picture != null }">
<img src="${flugot.picture}" height="100px" width="100px"/>
</jstl:if>

<acme:out code="flugot.ticker" value="${flugot.ticker}" />
<acme:out code="flugot.publicationDate" value="${flugot.publicationDate}" />
<acme:out code="flugot.body" value="${flugot.body}" />

<security:authorize access="hasRole('AUDITOR')">
<acme:out code="flugot.finalMode" value="${flugot.finalMode}" />
</security:authorize>

				
<security:authorize access="hasRole('AUDITOR')">
<acme:cancel url="flugot/auditor/list.do" code="flugot.back" />
</security:authorize>

<security:authorize access="hasRole('COMPANY')">
<acme:cancel url="flugot/company/list.do?auditId=${flugot.audit.id}" code="flugot.back" />
</security:authorize>








