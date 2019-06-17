<%--
 * action-1.jsp
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<fmt:formatDate value="${messageObject.moment}" pattern="dd/MM/yyyy" var="formattedMoment" />
<acme:out value="${formattedMoment}" code="message.moment" />
<acme:out value="${messageObject.subject}" code="message.subject" />
<acme:out value="${messageObject.body}" code="message.body" />

<fieldset><legend><spring:message code="message.tags" /></legend>
	<ul>
		<jstl:forEach items="${messageObject.tags}" var="tag">
			<li><jstl:out value="${tag}" /></li>
		</jstl:forEach>
	</ul>
</fieldset>

<acme:out value="${messageObject.sender.name} ${messageObject.sender.surname}" code="message.sender" />
<acme:out value="${messageObject.receiver.name} ${messageObject.receiver.surname}" code="message.receiver" />

<acme:cancel url="/message/actor/list.do" code="master.page.profile.listmessages" />



