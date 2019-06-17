
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<display:table name="actors" id="row" requestURI="${requestURI}"
	pagesize="20" class="displaytag">

	<display:column property="userAccount.username"
		titleKey="actor.username" />

	<display:column titleKey="actor.profile">
		<jstl:if test="${row.userAccount.enabled == true }"></jstl:if>
		<a href="actor/administrator/show.do?actorId=${row.id}"> <spring:message
				code="actor.profile" />
		</a>
	</display:column>

	<security:authorize access="hasRole('ADMIN')">
	
		<acme:column code="actor.score" value="${ row.score}"></acme:column>
		<display:column>
			<jstl:if test="${row.banned == false }">
				<a href="actor/administrator/ban.do?actorId=${row.id}"> <spring:message
						code="actor.ban" />
				</a>
			</jstl:if>
			<jstl:if test="${row.banned == true}">
				<a href="actor/administrator/unban.do?actorId=${row.id}"> <spring:message
						code="actor.unban" />
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>
</display:table>