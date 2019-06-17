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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table name="applications" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<security:authorize access="hasAnyRole('ROOKIE')">
		<display:column titleKey="app.edit">
			<jstl:if test="${row.status.equals('PENDING')}">

				<a href="application/rookie/edit.do?applicationId=${row.id}"> <spring:message
						code="app.edit" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>
	<security:authorize access="hasAnyRole('COMPANY')">
		<display:column titleKey="app.edit">
			<jstl:if
				test="${row.status.equals('PENDING') || row.status.equals('SUBMITTED') }">

				<a href="application/company/edit.do?applicationId=${row.id}"> <spring:message
						code="app.edit" />
				</a>

			</jstl:if>
		</display:column>
	</security:authorize>
	<security:authorize access="hasAnyRole('ROOKIE','COMPANY')">
		<display:column titleKey="app.publishMoment" property="publishMoment" />
		<display:column titleKey="app.submitMoment" property="submitMoment" />
		<display:column titleKey="app.status" property="status"
			sortable="true" />
		<display:column titleKey="app.problem" property="problem.title" />
		<display:column titleKey="app.position" property="position.title" />
		<security:authorize access="hasRole('ROOKIE')">
			<display:column titleKey="app.company"
				property="position.company.comercialName" />

			<display:column titleKey="app.show">
				<a href="application/rookie/show.do?applicationId=${row.id}"> <spring:message
						code="app.show" />
				</a>
			</display:column>
		</security:authorize>
		<display:column titleKey="app.view.problem">
			<a href="problem/listone.do?applicationId=${row.id}"> <spring:message
					code="app.view.problem" />
			</a>
		</display:column>
		<security:authorize access="hasRole('COMPANY')">
			<display:column titleKey="app.rookie" property="rookie.name" />
			<display:column titleKey="app.show">
				<a href="application/company/show.do?applicationId=${row.id}"> <spring:message
						code="app.show" />
				</a>
			</display:column>
		</security:authorize>

	</security:authorize>
</display:table>
<security:authorize access="hasRole('ROOKIE')">
	<a href="position/listtwo.do">Create application</a>
</security:authorize>