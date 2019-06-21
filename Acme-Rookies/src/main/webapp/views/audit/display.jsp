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


<acme:out code="audit.auditor" value="${audit.auditor.userAccount.username}" />
<acme:out code="audit.moment" value="${audit.moment}" />
<acme:out code="audit.score" value="${audit.score}" />
<acme:out code="audit.finalMode" value="${audit.finalMode}" />
<acme:out code="audit.text" value="${audit.text}" />

<acme:out code="audit.position" value="${audit.position.title}" />
<%-- <acme:out code="audit.auditor" value="${audit.auditor.name}" />


 --%>
 
 		<a href="position/display.do?positionId=${audit.position.id}"><spring:message
				code="audit.position"></spring:message></a>
				<br>
				<br>
				




<%-- <security:authentication property="principal.username" var="username" />
 --%>
<jstl:if test='${audit.auditor.userAccount.username == username}'>
	<jstl:if test="${audit.finalMode == false}">

		<input type="button" name="edit"
			value="<spring:message code="audit.edit"></spring:message>"
			onclick="javascript:relativeRedir('audit/edit.do?auditId=${audit.id}')" />
	</jstl:if>
</jstl:if>



<jstl:if test="${audit.auditor.userAccount.username == username}">
	<acme:cancel url="audit/myList.do" code="audit.back" />
</jstl:if>


<jstl:if test="${audit.auditor.userAccount.username != username}">
	<acme:cancel url="position/list.do" code="audit.backTo" />
</jstl:if>








