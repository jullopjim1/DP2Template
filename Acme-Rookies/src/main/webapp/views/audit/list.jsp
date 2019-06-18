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




<display:table name="audits" id="audit" requestURI="audit/list.do"
	pagesize="${numResults}" class="displaytag">

	<%--  Primero compruebo que es un brotherhood --%>
	<security:authorize access="hasRole('AUDITOR')">
		<security:authentication property="principal.username" var="username" />

		<jstl:if test='${audit.auditor.userAccount.username == username}'>


			<%--  La columna que va a la vista edit de las miscellaneousRecord --%>
			<display:column>
				<jstl:if test="${audit.finalMode == false}">
					<a href="audit/edit.do?auditId=${audit.id}"><spring:message
							code="audit.edit"></spring:message></a>
				</jstl:if>
			</display:column>
		</jstl:if>

	</security:authorize>

	<%--  La columna que va a la vista display de las miscellaneousRecord --%>
	<display:column>
		<a href="audit/display.do?auditId=${audit.id}"><spring:message
				code="audit.display"></spring:message></a>
	</display:column>



	<acme:column code="audit.moment" value="${audit.moment}"></acme:column>
	<acme:column code="audit.score" value="${audit.score}"></acme:column>
	<acme:column code="audit.finalMode" value="${audit.finalMode}"></acme:column>
	<acme:column code="audit.text" value="${audit.text}"></acme:column>

	<%-- 	<acme:column code="audit.auditor" value="${audit.auditor.name}"></acme:column>--%>
	<jstl:if test="${audit.position.title != null}">
		<acme:column code="audit.position" value="${audit.position.title}"></acme:column>
	</jstl:if>
	<jstl:if test="${audit.position.title == null}">
		<acme:column code="audit.position" value="Position nula"></acme:column>
	</jstl:if>



	<security:authorize access="hasRole('AUDITOR')">
		<display:column titleKey="flugot.list">
			<a href="flugot/auditor/listAudit.do?auditId=${audit.id}"> <spring:message
					code="flugot.list" />
			</a>
		</display:column>
	</security:authorize>


	<security:authorize access="hasRole('COMPANY')">
		<display:column titleKey="flugot.list">
			<a href="flugot/company/list.do?auditId=${audit.id}"> <spring:message
					code="flugot.list" />
			</a>
		</display:column>
	</security:authorize>



</display:table>

<security:authorize access="hasRole('AUDITOR')">

	<input type="button" name="create"
		value="<spring:message code="audit.create"></spring:message>"
		onclick="javascript:relativeRedir('position/listToAudit.do')" />
</security:authorize>


<input type="button" name="back"
	value="<spring:message code="audit.backTo"></spring:message>"
	onclick="javascript:relativeRedir('position/list.do')" />
<%--  Boton de creacion --%>
