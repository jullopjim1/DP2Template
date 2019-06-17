<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${actionURL}" method="post" modelAttribute="messageForm" >

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox path="subject" code="message.subject" />
	<acme:textbox path="body" code="message.body" />
	
	<jstl:if test="${isBroadcast == false}">
		<acme:select path="receiver" items="${actors}" itemLabel="name" code="message.receiver" />
	</jstl:if>
	
	<acme:textbox code="message.tags" path="tags" readonly="true" />
	
	<acme:textbox code="message.addtag" path="newTag" /><input type="submit" name="addTag" value="<spring:message code='message.addtag' />" />
	<br>
	<jstl:if test="${messageForm.tags.length() > 0}">
		<input type="submit" name="removeTag" value="<spring:message code='message.removetag' />" />
	</jstl:if>
	
	<input type="submit" name="save" value="<spring:message code='message.send' />" />
	<acme:cancel code="message.cancel" url="message/actor/list.do" />

</form:form>