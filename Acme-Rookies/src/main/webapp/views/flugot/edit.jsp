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


	<security:authorize access="hasRole('AUDITOR')">
		<div>

			<form:form action="${requestURI}" modelAttribute="flugotForm">

				<form:hidden path="id" />
				<form:hidden path="ticker" />

				<fieldset>
					<acme:textarea code="flugot.body" path="body" />
					<br />
					<acme:textbox code="flugot.picture" path="picture" />
					<br />
					<acme:select items="${audits}" itemLabel="text" code="flugot.audit"
						path="audit" />
				</fieldset>


				<jstl:if test="${flugotForm.finalMode == false}">

					<div>
						<form:label path="finalMode">
							<b><spring:message code="flugot.finalMode"></spring:message>:</b>
						</form:label>

						<div>
							<input type="radio" id="finalMode" name="finalMode" value="false"
								checked> <label for="false"><spring:message
									code="flugot.false"></spring:message></label>
						</div>

						<div>
							<input type="radio" id="finalMode" name="finalMode" value="true">
							<label for="true"><spring:message code="flugot.true"></spring:message></label>
						</div>
						<form:errors cssClass="error" path="finalMode" />

						<br />
					</div>

				</jstl:if>


				<jstl:if test="${flugotForm.finalMode == false || edit == false}">

					<acme:submit name="save" code="flugot.save" />

				</jstl:if>
			</form:form>
			<acme:cancel url="flugot/auditor/list.do" code="flugot.cancel" />

		</div>





	</security:authorize>

