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



<br />
<br />

<display:table name="positions" id="position"
	requestURI="position/listToAudit.do" pagesize="${numResults}"  class="displaytag">

	<%--  Primero compruebo que es un brotherhood --%>
	<security:authorize access="hasAnyRole()">
		<security:authentication property="principal.username" var="username" />

		<jstl:if test='${position.company.userAccount.username == username}'>


			<%--  La columna que va a la vista edit de las miscellaneousRecord --%>
			<display:column>
				<a href="position/edit.do?positionId=${position.id}"><spring:message
						code="position.edit"></spring:message></a>
			</display:column>
		</jstl:if>

	</security:authorize>

	<%--  La columna que va a la vista display de las miscellaneousRecord --%>
	<display:column>
		<a href="position/display.do?positionId=${position.id}"><spring:message
				code="position.display"></spring:message></a>
	</display:column>



	<acme:column code="position.ticker" value="${position.ticker}"></acme:column>
	<acme:column code="position.title" value="${position.title}"></acme:column>
	<acme:column code="position.deadLine" value="${position.deadLine}"></acme:column>
	<acme:column code="position.salary" value="${position.salary}0 Euros"></acme:column>
	
	<acme:column code="position.company" value="${position.company.name}"></acme:column>
	<security:authorize access="hasRole('ROOKIE')">
		<display:column titleKey="app.application">
			<jstl:if test="${position.deadLine > now}">
				<a href="application/rookie/create.do?positionId=${position.id}"><spring:message
						code="app.create"></spring:message></a>
			</jstl:if>
		</display:column>
	</security:authorize>

	<display:column>
		<a href="company/any/display.do?companyId=${position.company.id}"><spring:message
				code="position.company"></spring:message></a>
	</display:column>

<security:authorize access="hasRole('AUDITOR')">

	<display:column>
				<a href="audit/create.do?positionId=${position.id}"><spring:message code="audit.assign"></spring:message></a>
	</display:column>
	
</security:authorize>
	


</display:table>


<%--  Boton de creacion --%>
<security:authorize access="hasRole('COMPANY')">

	<input type="button" name="create"
		value="<spring:message code="position.create"></spring:message>"
		onclick="javascript:relativeRedir('position/create.do')" />
</security:authorize>