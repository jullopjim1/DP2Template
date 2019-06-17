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
	test='${audit.auditor.userAccount.username == username || audit.id == 0}'>
	<security:authorize access="hasRole('AUDITOR')">
		<div>

			<form:form action="audit/edit.do" method="post" id="formCreate"
				name="formCreate" modelAttribute="audit">

				<!-- Atributos hidden-->

				<form:hidden path="id" />
				<form:hidden path="version" />
				
				<form:hidden path="moment" />
				<form:hidden path="position" />
				<form:hidden path="auditor" />
				
				



				<fieldset>
					<!-------------------Form ------------------------------------>

					<%-- 					<acme:textbox code="audit.ticker" path="ticker" />
 --%>
					<acme:textarea code="audit.text" path="text" />
					<acme:textbox code="audit.score" path="score" />
					
<%-- 					<acme:textbox code="audit.finalMode" path="finalMode" />
 --%>

				</fieldset>



		<jstl:if test="${audit.finalMode == true}">
			<div>
				<form:hidden path="finalMode" value="${audit.finalMode}" />
			</div>
		</jstl:if>

		<jstl:if test="${audit.finalMode == false}">


				<div>
					<form:label path="finalMode">
						<b><spring:message code="audit.finalMode"></spring:message>:</b>
					</form:label>

					<div>
						<input type="radio" id="finalMode" name="finalMode" value="false" checked>
						<label for="false"><spring:message code="audit.false"></spring:message></label>
					</div>

					<div>
						<input type="radio" id="finalMode" name="finalMode" value="true">
						<label for="true"><spring:message code="audit.true"></spring:message></label>
					</div>
					  <form:errors cssClass="error" path="finalMode" />
					
					<br />
				</div>

	</jstl:if>
	
	
	



				<!--  Los botones de crear y cancelar -->

				<jstl:if test="${audit.finalMode == false}">

				<input type="submit" name="save"
					value="<spring:message code="audit.save"></spring:message>" />
				</jstl:if>



				<jstl:if test="${audit.id != 0 && audit.finalMode == false}">
					<input type="submit" name="delete"
						value="<spring:message code="audit.delete" />"
						onclick="return confirm('<spring:message code="audit.confirm.delete" />')" />&nbsp;
				</jstl:if>
			</form:form>
			<acme:cancel url="audit/list.do?auditorId=${audit.auditor.id}"
				code="audit.cancel" />

		</div>





	</security:authorize>

</jstl:if>
<jstl:if test='${audit.auditor.userAccount.username != username}'>
	<h1>
		<b><spring:message code="audit.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />

	<acme:cancel url="" code="audit.back" />

</jstl:if>