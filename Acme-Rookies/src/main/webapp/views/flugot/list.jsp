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




<display:table name="flugots" id="flugot" requestURI="${RequestURI}"
	pagesize="${numResults}" class="displaytag">

	<%--  Primero compruebo que es un auditor --%>
	<security:authorize access="hasRole('AUDITOR')">
		<security:authentication property="principal.username" var="username" />

		<jstl:if test='${flugot.auditor.userAccount.username == username}'>


			<%--  La columna que va a la vista edit de las miscellaneousRecord --%>
			<display:column>
				<jstl:if test="${flugot.finalMode == false}">
					<a href="flugot/auditor/edit.do?flugotId=${flugot.id}"><spring:message
							code="flugot.edit"></spring:message></a>
				</jstl:if>
			</display:column>
		</jstl:if>

	</security:authorize>

	<security:authorize access="hasAnyRole('AUDITOR', 'COMPANY')"></security:authorize>
	<display:column>
		<a href="flugot/auditor/show.do?flugotId=${flugot.id}"><spring:message
				code="flugot.show"></spring:message></a>
	</display:column>


	<display:column titleKey="flugot.ticker">
		<jstl:if test="${flugot.ticker == null }">
			<spring:message code="flugot.null"></spring:message>
		</jstl:if>
		<jstl:if test="${flugot.ticker != null }">
			<jstl:out value="${flugot.ticker}"></jstl:out>
		</jstl:if>
	</display:column>

	<jstl:if test="${flugot.publicationDate == null }">
		<spring:message code="flugot.null"></spring:message>
	</jstl:if>
	<jstl:if test="${flugot.publicationDate != null }">
		<jstl:if test="${flugotService.diferenciaMeses(flugot.id) < 0 }">
			<display:column property="publicationDate"
				titleKey="flugot.publicationDate" style="background-color:" />
		</jstl:if>
		<jstl:if
			test="${flugotService.diferenciaMeses(flugot.id) < 1 && flugotService.diferenciaMeses(flugot.id) >= 0  }">
			<spring:message code="event.format.date" var="pattern" />
			<display:column property="publicationDate"
				titleKey="flugot.publicationDate" style="background-color:Indigo"
				format="${pattern }" />
		</jstl:if>

		<jstl:if
			test="${2 > flugotService.diferenciaMeses(flugot.id) && flugotService.diferenciaMeses(flugot.id) >= 1 }">
			<spring:message code="event.format.date" var="pattern" />
			<display:column property="publicationDate"
				titleKey="flugot.publicationDate"
				style="background-color:DarkSlateGrey" format="${pattern }" />
		</jstl:if>

		<jstl:if test="${flugotService.diferenciaMeses(flugot.id) >= 2 }">
			<spring:message code="event.format.date" var="pattern" />
			<display:column property="publicationDate"
				titleKey="flugot.publicationDate"
				style="background-color:PapayaWhip" format="${pattern }" />
		</jstl:if>
	</jstl:if>

	<acme:column code="flugot.body" value="${flugot.body}" />

	<display:column titleKey="flugot.picture">
		<jstl:if test="${flugot.picture == null }">
			<spring:message code="flugot.null"></spring:message>
		</jstl:if>
		<jstl:if test="${flugot.picture != null }">
			<img src="${flugot.picture}" height="50px" width="50px" />
		</jstl:if>
	</display:column>

	<security:authorize access="hasRole('AUDITOR')">
		<display:column titleKey="flugot.finalMode">
			<jstl:if test="${flugot.finalMode == false }">
				<img
					src="https://d2gg9evh47fn9z.cloudfront.net/800px_COLOURBOX7656540.jpg"
					height="50px" width="50px" />
			</jstl:if>
			<jstl:if test="${flugot.finalMode != false }">
				<img
					src="http://www.nortedesantander.gov.co/Portals/0/xBlog/uploads/2016/10/19/stock-footage-stamp-final.jpg"
					height="50px" width="50px" />
			</jstl:if>
		</display:column>
	</security:authorize>

</display:table>

<security:authorize access="hasRole('AUDITOR')">
	<input type="button" name="create"
		value="<spring:message code="flugot.create"></spring:message>"
		onclick="javascript:relativeRedir('flugot/auditor/create.do')" />
</security:authorize>


<input type="button" name="back"
	value="<spring:message code="flugot.back"></spring:message>"
	onclick="javascript:relativeRedir('position/list.do')" />
