<%--
 * action-1.jsp
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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>




<%-- <security:authorize access="hasRole('ROOKIE')">
<security:authentication property="principal.username" var="username" />

	<jstl:if test='${history.brotherhood.userAccount.username == username}'>
<a
				href="history/brotherhood/edit.do?historyId=${history.id}"><spring:message
					code="history.editName"></spring:message></a>
					<br>
					
	</jstl:if>

</security:authorize> --%>


<%--  -----------------------------PERSONAL DATA----------------------------------- --%>
	<br>
		<fieldset>
			<legend>
				<b><spring:message code="curricula.personalData"></spring:message></b>
			</legend>
			<acme:out code="curr.personalData.fullName" value="${personalData.fullName}" />
			<acme:out code="curr.personalData.statement" value="${personalData.statement}" />
			<acme:out code="curr.personalData.phoneNumber" value="${personalData.phoneNumber}" />
			
			<b><spring:message code="curr.personalData.gitHubProfile"></spring:message>:</b>
			<a href="${personalData.gitHubProfile }">${personalData.gitHubProfile }</a>
			<br />
			<br />
			
			<b><spring:message code="curr.personalData.linkedInProfile"></spring:message>:</b>
			<a href="${personalData.linkedInProfile }">${personalData.linkedInProfile }</a>
			<br />
			<br />
			
			<jstl:if test="${personalData.original == true }">
			<security:authentication property="principal.username" var="username" />
			<jstl:if test='${curricula.rookie.userAccount.username == username}'>
			
				<spring:message code="curricula.edit" var="edit"></spring:message>
				<input type="button" name="edit" value="${edit}"
					onclick="javascript:relativeRedir('personalData/edit.do?personalDataId=${personalData.id}')" />
			</jstl:if>
			</jstl:if>


		</fieldset>

<%--  -----------------------------POSITION DATA----------------------------------- --%>

			<fieldset>
				<legend>
					<b><spring:message code="curricula.positionData"></spring:message></b>
				</legend>
				<display:table name="positionDatas" id="positionData"
					pagesize="5" class="displaytag">
					
					<jstl:if test="${positionData.original == true }">
					<display:column>
						<a href="positionData/edit.do?positionDataId=${positionData.id}"><spring:message
							code="curricula.edit"></spring:message></a>
					</display:column>
					</jstl:if>

					<acme:column code="curr.positionData.title" value="${positionData.title}"></acme:column>
					<acme:column code="curr.positionData.description" value="${positionData.description}"></acme:column>
					<spring:message code="curricula.dateFormat" var="dateFormat"></spring:message>
					<spring:message code="curr.positionData.startDate" var="startDate"></spring:message>
								<display:column property="startDate.time"
									title="${startDate}" format="{0,date,${dateFormat }}" />
					<spring:message code="curr.positionData.endDate" var="endDate"></spring:message>
								<display:column property="endDate.time"
									title="${endDate}" format="{0,date,${dateFormat }}" />
				</display:table>
				
				<%--  Boton de creacion --%>
				<security:authorize access="hasRole('ROOKIE')">
				<jstl:if test="${curricula.original == true }">
					<input type="button" name="create"
						value="<spring:message code="curricula.create"></spring:message>"
						onclick="javascript:relativeRedir('positionData/create.do?curriculaId=${curricula.id}')" />
						</jstl:if>
				</security:authorize>
	
				</fieldset>
				
<%--  -----------------------------EDUCATION DATA----------------------------------- --%>
				
				
			<fieldset>
				<legend>
					<b><spring:message code="curricula.educationData"></spring:message></b>
				</legend>
				<display:table name="educationDatas" id="educationData"
					pagesize="5" class="displaytag">
					
					<jstl:if test="${educationData.original == true }">
					<display:column>
						<a href="educationData/edit.do?educationDataId=${educationData.id}"><spring:message
							code="curricula.edit"></spring:message></a>
					</display:column>
					</jstl:if>

					<acme:column code="curr.educationData.degree" value="${educationData.degree}"></acme:column>
					<acme:column code="curr.educationData.institution" value="${educationData.institution}"></acme:column>
					<acme:column code="curr.educationData.mark" value="${educationData.mark}"></acme:column>

					<spring:message code="curricula.dateFormat" var="dateFormat"></spring:message>
					<spring:message code="curr.educationData.startDate" var="startDate"></spring:message>
								<display:column property="startDate.time" title="${startDate}" format="{0,date,${dateFormat }}" />
					<spring:message code="curr.educationData.endDate" var="endDate"></spring:message>
								<display:column property="endDate.time"
									title="${endDate}" format="{0,date,${dateFormat }}" />
				</display:table>
				
				<%--  Boton de creacion --%>
				<security:authorize access="hasRole('ROOKIE')">
				<jstl:if test="${curricula.original == true }">
					<input type="button" name="create"
						value="<spring:message code="curricula.create"></spring:message>"
						onclick="javascript:relativeRedir('educationData/create.do?curriculaId=${curricula.id}')" />
				</jstl:if>		
				</security:authorize>
	
				</fieldset>
				
<%--  -----------------------------MISCELLANEOUS DATA----------------------------------- --%>


			<fieldset>
				<legend>
					<b><spring:message code="curricula.miscellaneousData"></spring:message></b>
				</legend>
				<display:table name="miscellaneousDatas" id="miscellaneousData"
					pagesize="5" class="displaytag">
					<jstl:if test="${miscellaneousData.original == true }">
					<display:column>
						<a href="miscellaneousData/edit.do?miscellaneousDataId=${miscellaneousData.id}"><spring:message
							code="curricula.edit"></spring:message></a>
					</display:column>
					</jstl:if>

					<acme:column code="curr.miscellaneousData.text" value="${miscellaneousData.text}"></acme:column>
					<acme:column code="curr.miscellaneousData.attachments" value="${miscellaneousData.attachments}"></acme:column>
					
				</display:table>
				
				<%--  Boton de creacion --%>
				<security:authorize access="hasRole('ROOKIE')">
					<jstl:if test="${curricula.original == true }">
					<input type="button" name="create"
						value="<spring:message code="curricula.create"></spring:message>"
						onclick="javascript:relativeRedir('miscellaneousData/create.do?curriculaId=${curricula.id}')" />
					</jstl:if>
				</security:authorize>
	
				</fieldset>		
				
<!-- ------------------------------------------------------------------------------------ -->

<security:authentication property="principal.username" var="username" />

	<jstl:if test='${curricula.rookie.userAccount.username == username}'>
<%--  Boton de ATRAS --%>
<acme:cancel url="curricula/list.do" code="curricula.back" />	

</jstl:if>	


