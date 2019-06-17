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

<%-- <form:form action="sponsorship/searchResult.do">


	<input title="keyword" name="keyword" size="40" />

	<input type="submit" name="search" class="button"
		value="<spring:message code="search.button" />" />


</form:form>

<br />
<br /> --%>

<display:table name="sponsorships" id="sponsorship" requestURI="sponsorship/provider/list.do"
	pagesize="5" class="displaytag">

	<security:authorize access="hasRole('PROVIDER')">
		<security:authentication property="principal.username" var="username" />

		<display:column>
			<jstl:if test='${sponsorship.provider.userAccount.username == username}'>
					<a href="sponsorship/provider/edit.do?sponsorshipId=${sponsorship.id}"><spring:message
							code="sponsorship.edit"></spring:message></a>
			</jstl:if>
		</display:column>


	</security:authorize>

	<%--  La columna que va a la vista display de las SPONSORSHIP --%>
	<display:column>
		<a href="sponsorship/display.do?sponsorshipId=${sponsorship.id}"><spring:message
				code="sponsorship.display"></spring:message></a>
	</display:column>



	<acme:column code="sponsorship.target" value="${sponsorship.target}"></acme:column>
	<display:column titleKey="sponsorship.banner">
			<img src="${sponsorship.banner}" height="50px" width="50px" />
	</display:column>
	<acme:column code="sponsorship.provider" value="${sponsorship.provider.name}"></acme:column>
	<acme:column code="sponsorship.position" value="${sponsorship.position.title}"></acme:column>



</display:table>


<%--  Boton de creacion --%>
<security:authorize access="hasRole('PROVIDER')">

	<input type="button" name="create"
		value="<spring:message code="sponsorship.create"></spring:message>"
		onclick="javascript:relativeRedir('sponsorship/provider/create.do')" />
</security:authorize>



