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

<display:table name="personalDatas" id="personalData"
	requestURI="curricula/list.do" pagesize="5" class="displaytag">



	<%--  La columna que va a la vista display --%>
	<display:column>
		<a href="curricula/display.do?curriculaId=${personalData.curricula.id}"><spring:message
				code="curricula.display"></spring:message></a>
	</display:column>



	<acme:column code="curricula.personalData.fullName" value="${personalData.fullName}"></acme:column>
	<acme:column code="curricula.personalData.statement" value="${personalData.statement}"></acme:column>
	
	<jstl:if test="${personalData.curricula.original == true }"> 
	<display:column>
		<a href="curricula/delete.do?curriculaId=${personalData.curricula.id}"><spring:message
				code="curricula.delete"></spring:message></a>
	</display:column>
	 </jstl:if>




</display:table>

<security:authentication property="principal.username" var="username" />

	<jstl:if test='${personalData.curricula.rookie.userAccount.username == username}'>
	
			<input type="button" name="create"
				value="<spring:message code="curricula.create"></spring:message>"
				onclick="javascript:relativeRedir('curricula/create.do')" />
	</jstl:if>