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




<display:table name="items" id="row" requestURI="${requestURI}"
	pagesize="10" class="displaytag">

	<security:authorize access="hasRole('PROVIDER')">
		<security:authentication property="principal.username" var="username" />


		<display:column>
			<jstl:if test='${row.provider.userAccount.username == username}'>
				<a href="item/provider/edit.do?itemId=${row.id}"><spring:message
						code="item.edit"></spring:message></a>
			</jstl:if>
		</display:column>


	</security:authorize>

	<display:column titleKey="item.provider">
		<a href="provider/any/display.do?providerId=${row.provider.id}">${row.provider.userAccount.username}</a>
	</display:column>

	<acme:column code="item.name" value="${row.name}" />
	<acme:column code="item.description" value="${row.description}" />
	<display:column titleKey="item.link">
		<jstl:forEach items="${row.link}" var="i">
			<a href="${i}">${i}</a>
		</jstl:forEach>
	</display:column>
	<display:column titleKey="item.pictures">
		<jstl:forEach items="${row.pictures}" var="i">
			<img src="${i}" height="50px" width="50px" />
		</jstl:forEach>
	</display:column>

</display:table>


<security:authorize access="hasRole('PROVIDER')">

	<a href="item/provider/create.do"> <spring:message
			code="item.create" />
	</a>


</security:authorize>