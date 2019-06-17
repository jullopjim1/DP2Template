<%--
 * create.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>


<security:authentication property="principal.username" var="username" />
<jstl:if
	test='${flugot.auditor.userAccount.username == username || flugot.id == 0}'>
	<security:authorize access="hasRole('AUDITOR')">
		<div>

			<form:form action="${requestURI}" modelAttribute="flugot">
				
				<form:hidden path="id"/>
				<form:hidden path="version"/>
				<form:hidden path="auditor"/>

				<fieldset>
					<acme:textarea code="flugot.body" path="body" />
					<br/>
					<acme:textbox code="flugot.picture" path="picture" />
					<br/>
					<acme:select items="${audits}" itemLabel="text" code="flugot.audit" path="audit"/>
				</fieldset>


		<jstl:if test="${flugot.finalMode == false}">

				<div>
					<form:label path="finalMode">
						<b><spring:message code="flugot.finalMode"></spring:message>:</b>
					</form:label>

					<div>
						<input type="radio" id="finalMode" name="finalMode" value="false" checked>
						<label for="false"><spring:message code="flugot.false"></spring:message></label>
					</div>

					<div>
						<input type="radio" id="finalMode" name="finalMode" value="true">
						<label for="true"><spring:message code="flugot.true"></spring:message></label>
					</div>
					  <form:errors cssClass="error" path="finalMode" />
					
					<br />
				</div>

	</jstl:if>
	

				<jstl:if test="${flugot.finalMode == false || flugot.id == 0}">

				<acme:submit name="save" code="flugot.save"/>

				<jstl:if test="${flugot.id != 0}">
				
					<input type="submit" name="delete"
						value="<spring:message code="flugot.delete" />"
						onclick="return confirm('<spring:message code="flugot.confirm.delete" />')" />&nbsp;
				</jstl:if>
				
				</jstl:if>
			</form:form>
			<acme:cancel url="flugot/auditor/list.do"
				code="flugot.cancel" />

		</div>





	</security:authorize>

</jstl:if>
<jstl:if test='${flugot.auditor.userAccount.username != username && flugot.id != 0}'>
	<h1>
		<b><spring:message code="flugot.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />

	<acme:cancel url="" code="flugot.back" />

</jstl:if>