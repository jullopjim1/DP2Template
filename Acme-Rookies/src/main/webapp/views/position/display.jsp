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


 <jstl:if test="${sponsorship != null}">
   <div  style="margin-right:150px" class="child"><img src="${sponsorship.banner} "  style="width:200%;height:200%; display: inline-block;">
    </div>
</jstl:if>

<acme:out code="position.ticker" value="${position.ticker}" />
<acme:out code="position.title" value="${position.title}" />
<acme:out code="position.description" value="${position.description}" />
<acme:out code="position.deadLine" value="${position.deadLine}" />
<acme:out code="position.profile" value="${position.profile}" />
<acme:out code="position.skills" value="${position.skills}" />
<acme:out code="position.technologies" value="${position.technologies}" />
<acme:out code="position.salary" value="${position.salary} " />
<acme:out code="position.finalMode" value="${position.finalMode}" />
<acme:out code="position.cancel" value="${position.cancel}" />
<strong><spring:message code="position.audits"></spring:message>: </strong><a href="audit/listAudits.do?positionId=${position.id}"><spring:message
					code="position.audits"></spring:message></a>








<fieldset>
	<legend>
		<b><spring:message code="problem.problems"></spring:message></b>
	</legend>
	<display:table name="problems" id="problem" pagesize="5"
		class="displaytag">

		<display:column>
			<a href="problem/display.do?problemId=${problem.id}"><spring:message
					code="problem.display"></spring:message></a>
		</display:column>

		<acme:column code="problem.title" value="${problem.title}"></acme:column>
		<acme:column code="problem.hint" value="${problem.hint}"></acme:column>
		<acme:column code="problem.company" value="${problem.company.name}"></acme:column>
	</display:table>



</fieldset>





<%-- <security:authentication property="principal.username" var="username" />
 --%>
<jstl:if test='${position.company.userAccount.username == username}'>
	<jstl:if test="${position.finalMode == false}">

		<input type="button" name="edit"
			value="<spring:message code="position.edit"></spring:message>"
			onclick="javascript:relativeRedir('position/edit.do?positionId=${position.id}')" />
	</jstl:if>
</jstl:if>



<jstl:if test="${position.company.userAccount.username == username}">
	<acme:cancel url="position/myList.do" code="position.back" />
</jstl:if>


<jstl:if test="${position.company.userAccount.username != username}">
	<acme:cancel url="position/list.do" code="position.back" />
</jstl:if>








