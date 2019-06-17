
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<security:authorize access="hasRole('ADMIN')">

	<display:table name="actors" id="actor" requestURI="administrator/listToBan.do" pagesize="5" class="displaytag">
	
		<spring:message code="actor.name" var="actorName"></spring:message>
		<display:column property="name" title="${actorName}" />
		
		<spring:message code="actor.surname" var="actorsurname"></spring:message>
		<display:column property="surname" title="${actorsurname}"/>
			
		<spring:message code="userAccount.authorities" var="userAccount.authorities"></spring:message>
		<display:column property="userAccount.authorities" title="Rol"/>
		
		<spring:message code="actor.email" var="actorEmail"></spring:message>
		<display:column property="email" title="${actorEmail}" />


		
		<display:column>

			<jstl:if test="${actor.spammer eq true}">
				<jstl:if test="${actor.userAccount.banned eq true}">
					<a href="actor/administrator/unBan.do?actorId=${actor.id}"><spring:message
							code="actor.unBan"></spring:message></a>
				</jstl:if>
				<jstl:if
					test="${actor.userAccount.banned eq false && actor.spammer eq true}">
					<a href="actor/administrator/ban.do?actorId=${actor.id}"><spring:message
							code="actor.ban"></spring:message></a>
				</jstl:if>

			</jstl:if>

		</display:column>
	
	
	</display:table>
<br>
<acme:cancel url="actor/administrator/spammers.do"
				code="actor.generateSpammers" />

</security:authorize>
