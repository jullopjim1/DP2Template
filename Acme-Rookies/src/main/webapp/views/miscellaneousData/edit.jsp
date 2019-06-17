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





<jstl:if test="${miscellaneousData.original == true }">

<security:authentication property="principal.username" var="username" />
<jstl:if
	test='${miscellaneousData.curricula.rookie.userAccount.username == username || miscellaneousData.id == 0}'>
	<security:authorize access="hasRole('ROOKIE')">
		<div>

			<form:form action="miscellaneousData/edit.do" method="post"
				id="formCreate" name="formCreate" modelAttribute="miscellaneousData">

				<!-- Atributos hidden-->

				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="curricula" />
				<form:hidden path="original" />



				<fieldset>
					<!-------------------Form ------------------------------------>
					<acme:labelForm code="miscellaneousData.text" path="text" />
					<acme:textarea code="miscellaneousData.attachments" path="attachments" readonly="false" />
				


				</fieldset>


				<!--  Los botones de crear y cancelar -->

				<input type="submit" name="save"
					value="<spring:message code="miscellaneousData.save"></spring:message>" />

				<jstl:if test="${miscellaneousData.id != 0}">
					<input type="submit" name="delete"
						value="<spring:message code="miscellaneousData.delete" />"
						onclick="return confirm('<spring:message code="miscellaneousData.confirm.delete" />')" />&nbsp;
					</jstl:if>

				<button type="button"
					onclick="javascript: relativeRedir('curricula/display.do?curriculaId=${miscellaneousData.curricula.id}')">
					<spring:message code="miscellaneousData.cancel" />
				</button>

			</form:form>

		</div>
	</security:authorize>

</jstl:if>
<jstl:if
	test='${miscellaneousData.curricula.rookie.userAccount.username != username}'>
	<h1>
		<b><spring:message code="curricula.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />
	<acme:cancel url="" code="miscellaneousData.back" />
</jstl:if>

</jstl:if>