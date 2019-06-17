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
	test='${sponsorship.provider.userAccount.username == username || sponsorship.id == 0}'>
	<security:authorize access="hasRole('PROVIDER')">
		<div>

			<form:form action="sponsorship/provider/edit.do" method="post" id="formCreate"
				name="formCreate" modelAttribute="sponsorship">

				<!-- Atributos hidden-->

				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="provider" />
				<form:hidden path="creditCard" />




				<fieldset>
					<!-------------------Form ------------------------------------>

					<acme:labelForm code="sponsorship.target" path="target" />
					<acme:labelForm code="sponsorship.banner" path="banner" />
					

			
					<div>
						<form:label path="position">
							<spring:message code="sponsorship.position"></spring:message>
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



				<!--  Los botones de crear y cancelar -->

				<input type="submit" name="save"
					value="<spring:message code="sponsorship.save"></spring:message>" />


				<jstl:if test="${sponsorship.id != 0}">
					<input type="submit" name="delete"
						value="<spring:message code="sponsorship.delete" />"
						onclick="return confirm('<spring:message code="sponsorship.confirm.delete" />')" />&nbsp;
	</jstl:if>
			</form:form>
			<acme:cancel url="sponsorship/provider/list.do"
				code="sponsorship.cancel" />

		</div>





	</security:authorize>

</jstl:if>
<jstl:if test='${sponsorship.provider.userAccount.username != username}'>
	<h1>
		<b><spring:message code="sponsorship.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />

	<acme:cancel url="" code="sponsorship.back" />

</jstl:if>