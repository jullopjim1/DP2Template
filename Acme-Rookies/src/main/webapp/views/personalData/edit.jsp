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



<jstl:if test="${personalData.original == true }">

<security:authentication property="principal.username" var="username" />
<jstl:if
	test='${personalData.curricula.rookie.userAccount.username == username || personalData.id == 0}'>
	<security:authorize access="hasRole('ROOKIE')">
		<div>

			<form:form action="personalData/edit.do" method="post"
				id="formCreate" name="formCreate" modelAttribute="personalData">

				<!-- Atributos hidden-->

				<form:hidden path="id" />
				<form:hidden path="version" />
				<form:hidden path="curricula" />
								<form:hidden path="original" />
				



				<fieldset>
					<!-------------------Form ------------------------------------>
					<acme:labelForm code="personalData.fullName" path="fullName" />
					<acme:labelForm code="personalData.statement" path="statement" />
					<acme:labelForm code="personalData.phoneNumber" path="phoneNumber" />
					<acme:labelForm code="personalData.gitHubProfile" path="gitHubProfile" />
					<acme:labelForm code="personalData.linkedInProfile" path="linkedInProfile" />
				</fieldset>


				<!--  Los botones de crear y cancelar -->

				<input type="submit" name="save"
					value="<spring:message code="personalData.save"></spring:message>" />
<jstl:if test='${personalData.id !=0  }'>
				<button type="button"
				onclick="javascript: relativeRedir('curricula/display.do?curriculaId=${personalData.curricula.id}')">
				<spring:message code="personalData.cancel" />
			</button>
</jstl:if>

<jstl:if test='${personalData.id ==0  }'>
				<button type="button"
				onclick="javascript: relativeRedir('curricula/list.do')">
				<spring:message code="personalData.cancel" />
			</button>
</jstl:if>


			</form:form>
			
		</div>
	</security:authorize>

</jstl:if>
<jstl:if
	test='${personalData.curricula.rookie.userAccount.username != username}'>
	<h1>
		<b><spring:message code="curricula.permissions"></spring:message></b>
	</h1>

	<img
		src="http://lymediseaseuk.com/wp-content/uploads/2018/07/oops-300x300.png"
		alt="Cuestionario Picture" style="width: 10%; height: 10%;">

	<br />
	<br />
	<acme:cancel url="" code="personalData.back" />
</jstl:if>

</jstl:if>