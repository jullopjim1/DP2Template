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
<script>
	$(function() {
		$("#datepicker1").datepicker({
			dateFormat : "yy/mm/dd",
			yearRange : "1700:2500",
			changeMonth : true,
			changeYear : true
		});
		$("#datepicker2").datepicker({
			dateFormat : "yy/mm/dd",
			yearRange : "1700:2500",
			changeMonth : true,
			changeYear : true
		});
	});
</script>



<jstl:if test="${positionData.original == true }">

<security:authentication property="principal.username" var="username" />
<jstl:if
	test='${positionData.curricula.rookie.userAccount.username == username || positionData.id == 0}'>
	<security:authorize access="hasRole('ROOKIE')">
		<div>

			<form:form action="positionData/edit.do" method="post"
				id="formCreate" name="formCreate" modelAttribute="positionData">

				<!-- Atributos hidden-->

				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="curricula" />
				<form:hidden path="original" />



				<fieldset>
					<!-------------------Form ------------------------------------>
					<acme:labelForm code="positionData.title" path="title" />
					<acme:labelForm code="positionData.description" path="description" />
					
					
					<form:label path="startDate">
						<spring:message code="positionData.startDate"></spring:message>
					</form:label>
					<form:input path="startDate" id="datepicker1" />
					<form:errors cssClass="error" path="startDate" />
					<br>

					<form:label path="endDate">
						<spring:message code="positionData.endDate"></spring:message>
					</form:label>
					<form:input path="endDate" id="datepicker2" />
					<form:errors cssClass="error" path="endDate" />
					<br>



				</fieldset>


				<!--  Los botones de crear y cancelar -->

				<input type="submit" name="save"
					value="<spring:message code="positionData.save"></spring:message>" />
					
					<jstl:if test="${positionData.id != 0}">
					<input type="submit" name="delete"
						value="<spring:message code="positionData.delete" />"
						onclick="return confirm('<spring:message code="positionData.confirm.delete" />')" />&nbsp;
					</jstl:if>

				<button type="button"
				onclick="javascript: relativeRedir('curricula/display.do?curriculaId=${positionData.curricula.id}')">
				<spring:message code="positionData.cancel" />
			</button>

			</form:form>
			
		</div>
	</security:authorize>

</jstl:if>
<jstl:if
	test='${positionData.curricula.rookie.userAccount.username != username}'>
	<h1>
		<b><spring:message code="curricula.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />
	<acme:cancel url="" code="positionData.back" />
</jstl:if>

</jstl:if>