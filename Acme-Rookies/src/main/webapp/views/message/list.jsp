
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<fieldset><legend><spring:message code="message.filterform" /></legend>
	<form:form action="message/actor/list.do">
		<label>
			<spring:message code="message.tag" />
		</label>
		<input type="text" id="tag" name="tag" />
		
		<input type="submit" value="<spring:message code="message.filter" />" />
	</form:form>
</fieldset>

<display:table name="messages" id="message" requestURI="${requestURI}" pagesize="5" class="displaytag">

	<fmt:formatDate value="${message.moment}" pattern="dd/MM/yyyy" var="formattedMoment" />
	<acme:column value="${formattedMoment}" code="message.moment" />
	<acme:column value="${message.subject}" code="message.subject" />
	<acme:column value="${message.body}" code="message.body" />
	<acme:column value="${message.sender.name} ${message.sender.surname}" code="message.sender" />
	<acme:column value="${message.receiver.name} ${message.receiver.surname}" code="message.receiver" />
	
	<spring:message code="message.tags" var="titleTags" />
	<display:column title="${titleTags}">
		<ul>
			<jstl:forEach items="${message.tags}" var="tag">
				<li>${tag}</li>
			</jstl:forEach>
		</ul>
	</display:column>
	
	<spring:message code="message.show" var="titleShow" />
 	<acme:column value="message/actor/show.do?messageId=${message.id}" alt="${titleShow}" url="true" />
	
	<spring:message code='message.delete' var="deleteMessageAlt" />
	<acme:column url="true" value="message/actor/delete.do?messageId=${message.id}" alt="${deleteMessageAlt}" />

</display:table>