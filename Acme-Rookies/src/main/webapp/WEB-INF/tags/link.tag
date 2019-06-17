<%@ tag language="java" body-content="empty"%>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<%-- Attributes --%>

<%@ attribute name="code" required="false"%>
<%@ attribute name="value" required="false"%>
<%@ attribute name="url" required="true" %>

<%@ attribute name="readonly" required="false"%>

<%-- Definition --%>

<jstl:choose>
	<jstl:when test="${code != null}">
		<a href="${url}"><spring:message code="${code}" /></a>
	</jstl:when>
	<jstl:when test="${value != null}">
		<a href="${url}"><jstl:out value="${value}" /></a>
	</jstl:when>
	<jstl:otherwise>
		<a href="${url}"><jstl:out value="${url}" /></a>
	</jstl:otherwise>
</jstl:choose>	
<!-- 
	<jstl:if test="${formatDate != null}">
						<fmt:formatDate pattern = '${formatDate}' value = '${value}' />
					</jstl:if>
 -->
