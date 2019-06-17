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
<script>
	$(function() {
		$("#datepicker1").datepicker({
			dateFormat : "yy/mm/dd",
			yearRange : "2018:2099",
			changeMonth : true,
			changeYear : true
		});

	});
</script>



<security:authentication property="principal.username" var="username" />
<jstl:if
	test='${position.company.userAccount.username == username || position.id == 0}'>
	<security:authorize access="hasRole('COMPANY')">
		<div>

			<form:form action="position/edit.do" method="post" id="formCreate"
				name="formCreate" modelAttribute="position">

				<!-- Atributos hidden-->

				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="ticker" />
				<form:hidden path="company" />
				
				



				<fieldset>
					<!-------------------Form ------------------------------------>

					<%-- 					<acme:textbox code="position.ticker" path="ticker" />
 --%>
					<acme:textbox code="position.title" path="title" />
					<acme:textbox code="position.description" path="description" />
					<acme:textbox code="position.profile" path="profile" />
					<acme:textbox code="position.skills" path="skills" />
					<acme:textbox code="position.technologies" path="technologies" />
					<acme:textbox code="position.salary" path="salary" />
<%-- 					<acme:textbox code="position.cancel" path="cancel" />
 --%>
			<jstl:if test="${position.finalMode == false}">
					<form:label path="deadLine">
						<b><spring:message code="position.deadLine" ></spring:message>:</b>
					</form:label>
					<form:input path="deadLine" id="datepicker1" readonly="${readonly}"  />
					<form:errors cssClass="error" path="deadLine" />
					<br>
			</jstl:if>
			<jstl:if test="${position.finalMode == true}">
					<acme:out code="position.deadLine" value="${position.deadLine}" />
					<fmt:formatDate value="${position.deadLine}" pattern="yyyy/MM/dd" var="formatedDeadline" />
					<div>
						<form:hidden path="deadLine" value="${formatedDeadline}" />
					</div>
			</jstl:if>

				</fieldset>


<jstl:if test="${position.id != 0}">

		<jstl:if test="${position.finalMode == true}">
			<div>
				<form:hidden path="finalMode" value="${position.finalMode}" />
			</div>
		</jstl:if>

		<jstl:if test="${position.finalMode == false}">


				<div>
					<form:label path="finalMode">
						<b><spring:message code="position.finalMode"></spring:message>:</b>
					</form:label>

					<div>
						<input type="radio" id="finalMode" name="finalMode" value="false" checked>
						<label for="false"><spring:message code="position.false"></spring:message></label>
					</div>

					<div>
						<input type="radio" id="finalMode" name="finalMode" value="true">
						<label for="true"><spring:message code="position.true"></spring:message></label>
					</div>
					  <form:errors cssClass="error" path="finalMode" />
					
					<br />
				</div>

	</jstl:if>
	

		<jstl:if test="${position.cancel == false}">

				<div>
					<form:label path="cancel">
						<b><spring:message code="position.cancel"></spring:message>:</b>
					</form:label>

					<div>
						<input type="radio" id="cancel" name="cancel" value="false" checked>
						<label for="false"><spring:message code="position.false"></spring:message></label>
					</div>

					<div>
						<input type="radio" id="cancel" name="cancel" value="true">
						<label for="true"><spring:message code="position.true"></spring:message></label>
					</div>
				   <form:errors cssClass="error" path="cancel" />
					
					<br />
				</div>
				
				</jstl:if>
				
						<jstl:if test="${position.cancel == true}">

				<div>
					<form:label path="cancel">
						<b><spring:message code="position.cancel"></spring:message>:</b>
					</form:label>

					<div>
						<input type="radio" id="cancel" name="cancel" value="false" >
						<label for="false"><spring:message code="position.false"></spring:message></label>
					</div>

					<div>
						<input type="radio" id="cancel" name="cancel" value="true" checked>
						<label for="true"><spring:message code="position.true"></spring:message></label>
					</div>
				   <form:errors cssClass="error" path="cancel" />
					
					<br />
				</div>
				
				</jstl:if>
				
	


</jstl:if>


				<!--  Los botones de crear y cancelar -->

				<input type="submit" name="save"
					value="<spring:message code="position.save"></spring:message>" />


				<jstl:if test="${position.id != 0 && tieneApps == false && position.finalMode == false}">
					<input type="submit" name="delete"
						value="<spring:message code="position.delete" />"
						onclick="return confirm('<spring:message code="position.confirm.delete" />')" />&nbsp;
				</jstl:if>
			</form:form>
			<acme:cancel url="position/myList.do?companyId=${position.company.id}"
				code="position.cancel" />

		</div>





	</security:authorize>

</jstl:if>
<jstl:if test='${position.company.userAccount.username != username}'>
	<h1>
		<b><spring:message code="position.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />

	<acme:cancel url="" code="position.back" />

</jstl:if>