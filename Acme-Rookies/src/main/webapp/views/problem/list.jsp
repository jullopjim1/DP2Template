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

<%-- <form:form action="problem/searchResult.do">


	<input title="keyword" name="keyword" size="40" />

	<input type="submit" name="search" class="button"
		value="<spring:message code="search.button" />" />


</form:form>

<br />
<br /> --%>

<display:table name="problems" id="problem" requestURI="problem/company/list.do"
	pagesize="5" class="displaytag">

	<security:authorize access="hasRole('COMPANY')">
		<security:authentication property="principal.username" var="username" />

		<display:column>
			<jstl:if test='${problem.company.userAccount.username == username}'>
				<jstl:if test='${problem.finalMode==false}'>
					<a href="problem/company/edit.do?problemId=${problem.id}"><spring:message
							code="problem.edit"></spring:message></a>
				</jstl:if>
			</jstl:if>
		</display:column>


	</security:authorize>

	<%--  La columna que va a la vista display de las miscellaneousRecord --%>
	<display:column>
		<a href="problem/display.do?problemId=${problem.id}"><spring:message
				code="problem.display"></spring:message></a>
	</display:column>



	<acme:column code="problem.title" value="${problem.title}"></acme:column>
	<acme:column code="problem.statement" value="${problem.statement}"></acme:column>
	<acme:column code="problem.hint" value="${problem.hint}"></acme:column>
	<acme:column code="problem.company" value="${problem.company.name}"></acme:column>
	<acme:column code="problem.position" value="${problem.position.title}"></acme:column>

	<spring:message code="problem.finalMode" var="finalMode" />
	<display:column title="${finalMode}">
		<jstl:if test='${problem.finalMode==true}'>
			<spring:message code="problem.true"></spring:message>
		</jstl:if>
		<jstl:if test='${problem.finalMode==false}'>
			<spring:message code="problem.false"></spring:message>
		</jstl:if>
	</display:column>









</display:table>


<%--  Boton de creacion --%>
<security:authorize access="hasRole('COMPANY')">

	<input type="button" name="create"
		value="<spring:message code="problem.create"></spring:message>"
		onclick="javascript:relativeRedir('problem/company/create.do')" />
</security:authorize>



