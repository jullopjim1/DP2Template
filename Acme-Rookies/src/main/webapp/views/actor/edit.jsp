<%--
 * edit.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<spring:message code="confirm.phone" var="confirmPhoneMessage" />

<jstl:if test="${isPrincipalAuthorizedEdit}">
	<form:form action="actor/edit.do" method="post" id="formEdit"
		name="formEdit" modelAttribute="actorForm">

		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="authority" />

		<acme:textbox path="username" code="useraccount.username" />
		<acme:password path="password" code="useraccount.password" />
		<acme:password path="confirmPassword"
			code="useraccount.confirmPassword" />

		<acme:textbox path="name" code="company.name" />
		<acme:textbox path="surname" code="company.surname" />
		<acme:textbox path="email" code="company.email" />
		<acme:textbox path="phone" code="company.phone" id="phone" />
		<acme:textbox path="address" code="company.address" />
		<acme:textbox path="photo" code="company.photo" />
		<acme:textbox path="holderName" code="creditcard.holdername" />

		<form:label path="makeName">
			<spring:message code="creditcard.makename" />
		</form:label>
		<form:select path="makeName">
			<form:option label="" value="" />
			<jstl:forEach items="${makes}" var="make">
				<form:option label='${make}' value='${make}' />
			</jstl:forEach>
		</form:select>
		<form:errors path="makeName" cssClass="error" />

		<acme:textbox path="number" code="creditcard.number" />
		<acme:textbox path="expirationMonth" code="creditcard.expirationmonth" />
		<acme:textbox path="expirationYear" code="creditcard.expirationyear" />
		<acme:textbox path="CVVCode" code="creditcard.cvvcode" />
		<acme:textbox path="VATNumber" code="creditcard.vatnumber" />

		<jstl:if test="${actor.id == 0}">
			<acme:checkbox code="company.accept" path="accept" />
		</jstl:if>

		<input type="submit" name="save"
			value="<spring:message code="company.save"></spring:message>"
			onclick="return patternPhone(document.getElementById('phone').value, '${confirmPhoneMessage}');" />

		<acme:cancel url="" code="company.cancel" />

		<br />
		<br />





	</form:form>

	<a href="actor/export.do?actorId=${actorId }"
		onclick="javascript:confirm('<spring:message code="actor.export.ask"/>')"><spring:message
			code="actor.export" /></a>

</jstl:if>