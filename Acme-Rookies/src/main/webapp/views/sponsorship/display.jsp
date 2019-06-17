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
<jstl:if test="${sponsorship.provider.userAccount.username == username}">



<acme:out code="sponsorship.target" value="${sponsorship.target}" />

<spring:message code="sponsorship.banner"></spring:message>
<br />
<img src="${sponsorship.banner} "  style="width:15%;height:15%; display: inline-block;">

<acme:out code="sponsorship.creditCard.holderName" value="${sponsorship.creditCard.holderName}" />
<acme:out code="sponsorship.creditCard.makeName" value="${sponsorship.creditCard.makeName}" />
<acme:out code="sponsorship.creditCard.number" value="${sponsorship.creditCard.number}" />

<acme:out code="sponsorship.provider" value="${sponsorship.provider.name}" />
<acme:out code="sponsorship.position" value="${sponsorship.position.title}" />







<security:authorize access="hasRole('PROVIDER')">

	<security:authentication property="principal.username" var="username" />

	<jstl:if test='${sponsorship.provider.userAccount.username == username}'>
		<input type="button" name="edit"
			value="<spring:message code="sponsorship.edit"></spring:message>"
			onclick="javascript:relativeRedir('sponsorship/provider/edit.do?sponsorshipId=${sponsorship.id}')" />
	</jstl:if>
	<acme:cancel url="sponsorship/provider/list.do" code="sponsorship.mySponsorships" />
</security:authorize>
<%-- <acme:cancel url="sponsorship/list.do" code="sponsorship.allSponsorships" /> --%>

</jstl:if>
<security:authentication property="principal.username" var="username" />
<jstl:if test='${sponsorship.provider.userAccount.username != username}'>
	<h1>
		<b><spring:message code="sponsorship.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Sponsorship Picture" style="width: 10%; height: 10%;">

	

	<acme:cancel url="" code="sponsorship.back" />

</jstl:if>
</security:authorize>


<security:authorize access="isAnonymous()">

<acme:out code="sponsorship.target" value="${sponsorship.target}" />

<spring:message code="sponsorship.banner"></spring:message>
<br />
<img src="${sponsorship.banner} "  style="width:50%;height:50%; display: inline-block;">
<acme:out code="sponsorship.provider" value="${sponsorship.provider.name}" />
<acme:out code="sponsorship.position" value="${sponsorship.position.title}" />





<acme:cancel url="" code="sponsorship.back" />
</security:authorize>






