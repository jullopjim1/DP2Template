<%--
 * index.jsp
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

<h3>${nameSys}</h3>
<br />
<p>${welcomeMessage}</p>
<!-- Mensaje De Seguridad -->

<security:authorize access="hasRole('ADMIN')">
	<fieldset>
		<legend>
			<spring:message code="master.security.message1" />
		</legend>

		<jstl:if test="${isFailSystemA==true}">
			<p style="color: red">
				<spring:message code="master.security.message.admin1" />
			</p>
		</jstl:if>
		<jstl:if test="${isFailSystemA==false}">
			<p style="color: red">
				<spring:message code="master.security.message.admin2" />
			</p>
		</jstl:if>
		<br />
		<form:form>
			<button style="background-color: red;" name="activate">
				<spring:message code="master.security.activar" />
			</button>
			<br />
			<br />
			<button style="background-color: yellow;" name="desactivate">
				<spring:message code="master.security.desactivar" />
			</button>
		</form:form>
		<br />
	</fieldset>
</security:authorize>

<p>
	<spring:message code="welcome.greeting.current.time" />
	${moment}
</p>

<!-- Mensaje de Seguridad -->



<security:authorize
	access="hasAnyRole('COMPANY','ROOKIE','PROVIDER','AUDITOR')">



	<jstl:if test="${isFailSystemO == true}">
		<div class="modal-wrapper" id="popup">

			<div class="popup-contenedor" align="center">

				<h2>
					<spring:message code="master.security.message1" />
				</h2>
				<p style="font-size: 15px;">${securityMessage}</p>

				<p style="font-size: 15px;">
					<spring:message code="master.security.message2" />
				</p>
				<%-- <form:form>
					<button class="popup-cerrar" name="cerrar">X</button>
				</form:form> --%>
				<a class="popup-cerrar" href="welcome/indexCerrar.do">X</a>
			</div>

		</div>
	</jstl:if>
</security:authorize>
