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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>




<security:authentication property="principal.username" var="username" />
<jstl:if
	test='${problem.company.userAccount.username == username || problem.id == 0}'>
	<security:authorize access="hasRole('COMPANY')">
		<div>

			<form:form action="problem/company/edit.do" method="post" id="formCreate"
				name="formCreate" modelAttribute="problem">

				<!-- Atributos hidden-->

				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="company" />




				<fieldset>
					<!-------------------Form ------------------------------------>

					<acme:labelForm code="problem.title" path="title" />
					<acme:labelForm code="problem.statement" path="statement" />
					<acme:labelForm code="problem.hint" path="hint" />
					<acme:textarea code="problem.attachments" path="attachments" readonly="false" />
					

			

					<div>
						<form:label path="position">
							<spring:message code="problem.position"></spring:message>
						</form:label>
						<form:select path="position">
							<form:option value="0">-----</form:option>
							<jstl:forEach items="${positions}" var="position">
								<form:option value="${position.id}"><jstl:out value="${position.title}" /></form:option>
							</jstl:forEach>
						</form:select>
						<form:errors cssClass="error" path="position"></form:errors>
					</div>


				</fieldset>

				<div>
					<form:label path="finalMode">
						<spring:message code="problem.finalMode"></spring:message>
					</form:label>

					<div>
						<input type="radio" id="finalMode" name="finalMode" value="false" checked>
						<label for="false"><spring:message code="problem.false"></spring:message></label>
					</div>

					<div>
						<input type="radio" id="finalMode" name="finalMode" value="true">
						<label for="true"><spring:message code="problem.true"></spring:message></label>
					</div>
					  <form:errors cssClass="error" path="finalMode" />
					
					<br />
				</div>

			









				<!--  Los botones de crear y cancelar -->

				<input type="submit" name="save"
					value="<spring:message code="problem.save"></spring:message>" />


				<jstl:if test="${problem.id != 0}">
					<input type="submit" name="delete"
						value="<spring:message code="problem.delete" />"
						onclick="return confirm('<spring:message code="problem.confirm.delete" />')" />&nbsp;
	</jstl:if>
			</form:form>
			<acme:cancel url="problem/company/list.do"
				code="problem.cancel" />

		</div>





	</security:authorize>

</jstl:if>
<jstl:if test='${problem.company.userAccount.username != username}'>
	<h1>
		<b><spring:message code="problem.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />

	<acme:cancel url="" code="problem.back" />

</jstl:if>