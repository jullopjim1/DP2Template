<%--
 * action-1.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="isAuthenticated()">
<security:authentication property="principal.username" var="username" />
<jstl:if test="${problem.finalMode == true ||problem.company.userAccount.username == username}">



<acme:out code="problem.title" value="${problem.title}" />

<acme:out code="problem.statement" value="${problem.statement}" />
<acme:out code="problem.hint" value="${problem.hint}" />
<acme:out code="problem.company" value="${problem.company.name}" />
<acme:out code="problem.position" value="${problem.position.title}" />
<jstl:if test="${problem.finalMode== true}">
	<b><spring:message code="problem.finalMode"></spring:message>:</b>
	<spring:message code="problem.true"></spring:message>

</jstl:if>


<jstl:if test="${problem.finalMode== false}">
	<b><spring:message code="problem.finalMode"></spring:message>:</b>
	<spring:message code="problem.false"></spring:message>
</jstl:if>

<br />
<br />


<fieldset>
	<legend>
		<b><spring:message code="problem.attachments" /></b>
	</legend>

	<jstl:forEach items="${problem.attachments}" var="attachment">


		<a href="${attachment }">${attachment }</a>
		<br />

	</jstl:forEach>

</fieldset>

<security:authorize access="hasRole('COMPANY')">

	<security:authentication property="principal.username" var="username" />

	<jstl:if test='${problem.company.userAccount.username == username}'>
<jstl:if test="${problem.finalMode == false}">
		<input type="button" name="edit"
			value="<spring:message code="problem.edit"></spring:message>"
			onclick="javascript:relativeRedir('problem/company/edit.do?problemId=${problem.id}')" />
			</jstl:if>
	</jstl:if>
	<acme:cancel url="problem/company/list.do" code="problem.myCompanysProblems" />
</security:authorize>
<%-- <acme:cancel url="problem/list.do" code="problem.allProblems" /> --%>

</jstl:if>
<security:authentication property="principal.username" var="username" />
<jstl:if test='${problem.finalMode != true && problem.company.userAccount.username != username}'>
	<h1>
		<b><spring:message code="problem.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />

	<acme:cancel url="" code="problem.back" />

</jstl:if>
</security:authorize>


<security:authorize access="isAnonymous()">
<jstl:if test="${problem.finalMode == true }">

<acme:out code="problem.title" value="${problem.title}" />

<acme:out code="problem.statement" value="${problem.statement}" />
<acme:out code="problem.hint" value="${problem.hint}" />
<acme:out code="problem.company" value="${problem.company.name}" />
<acme:out code="problem.position" value="${problem.position.title}" />
<jstl:if test="${problem.finalMode== true}">
	<b><spring:message code="problem.finalMode"></spring:message>:</b>
	<spring:message code="problem.true"></spring:message>

</jstl:if>


<jstl:if test="${problem.finalMode== false}">
	<b><spring:message code="problem.finalMode"></spring:message>:</b>
	<spring:message code="problem.false"></spring:message>
</jstl:if>

<br />
<br />


<fieldset>
	<legend>
		<b><spring:message code="problem.attachments" /></b>
	</legend>

	<jstl:forEach items="${problem.attachments}" var="attachment">


		<a href="${attachment }">${attachment }</a>
		<br />

	</jstl:forEach>

</fieldset>
<acme:cancel url="" code="problem.back" />
</jstl:if>
</security:authorize>






