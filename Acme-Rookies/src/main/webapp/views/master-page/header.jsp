<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>



<!--//BLOQUE COOKIES-->
<div id="barraaceptacion">
	<div class="inner">
		<spring:message code="aviso.cookies" />
		<a href="javascript:void(0);" class="ok" onclick="PonerCookie();"><b>OK</b></a>
		| <a href="law/politicaCookies.do" target="_blank" class="info"><spring:message
				code="aviso.cookies.information" /></a>
	</div>
</div>

<script>
	function getCookie(c_name) {
		var c_value = document.cookie;
		var c_start = c_value.indexOf(" " + c_name + "=");
		if (c_start == -1) {
			c_start = c_value.indexOf(c_name + "=");
		}
		if (c_start == -1) {
			c_value = null;
		} else {
			c_start = c_value.indexOf("=", c_start) + 1;
			var c_end = c_value.indexOf(";", c_start);
			if (c_end == -1) {
				c_end = c_value.length;
			}
			c_value = unescape(c_value.substring(c_start, c_end));
		}
		return c_value;
	}

	function setCookie(c_name, value, exdays) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + exdays);
		var c_value = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
		document.cookie = c_name + "=" + c_value;
	}

	if (getCookie('tiendaaviso') != "1") {
		document.getElementById("barraaceptacion").style.display = "block";
	} else {
		document.getElementById("barraaceptacion").style.display = "none";
	}

	function PonerCookie() {
		setCookie('tiendaaviso', '1', 365);
		document.getElementById("barraaceptacion").style.display = "none";
	}
</script>
<!--//FIN BLOQUE COOKIES-->




<div align="center">
	<a href="#"><img src="${banner}" alt="Acme Rookie Rank Co., Inc."
		width="20%" height="20%" /></a>
</div>
<form:form action="${requestURI}" modelAttribute="warning">
	<security:authorize access="isAuthenticated()">




	</security:authorize>
</form:form>



<!--//fin aviso de vulnerabilidad-->



<div align="center">
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->

		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv" href="configuration/administrator/list.do"><spring:message
						code="master.page.configuration" /></a></li>
			<li><a class="fNiv" href="administrator/dashboard.do"><spring:message
						code="master.page.dashboard" /></a>
			<li><a href="actor/administrator/listToBan.do"><spring:message
						code="master.page.administrator.listToBan" /></a></li>
			<li><a href="actor/administrator/companiesScores.do"><spring:message
						code="master.page.administrator.companiesScores" /></a></li>
			<li><a href="register/administrator/newActor.do?authority=ADMIN"><spring:message
						code="master.page.register.admin" /></a></li>
			<li><a href="auditor/administrator/register.do"><spring:message
						code="master.page.registerasauditor" /></a></li>
			<li><a class="fNiv" href="position/list.do"><spring:message
						code="master.page.position" /></a></li>



		</security:authorize>


		<security:authorize access="hasRole('AUDITOR')">
			<li><a class="fNiv" href="audit/list.do"><spring:message
						code="master.page.audit" /></a></li>
						
			<li><a class="fNiv" href="flugot/auditor/list.do"><spring:message
						code="master.page.flugots" /></a></li>

			<li><a class="fNiv" href="position/listToAudit.do"><spring:message
						code="master.page.listToAudit" /></a></li>
			<li><a href="position/list.do"><spring:message
						code="master.page.position" /></a></li>




		</security:authorize>
		<security:authorize access="hasRole('PROVIDER')">
			<li><a href="sponsorship/provider/list.do"><spring:message
						code="master.page.provider.sponsorships" /></a></li>

		</security:authorize>
		<li><a class="fNiv" href="item/list.do"><spring:message
					code="master.page.items" /></a>
			<ul>

				<li class="arrow"></li>
				<security:authorize access="hasRole('PROVIDER')">
					<li><a href="item/provider/list.do"><spring:message
								code="master.page.provider.items" /></a></li>

				</security:authorize>
			</ul></li>



		<security:authorize access="hasRole('ROOKIE')">
			<li><a href="application/rookie/list.do"><spring:message
						code="master.page.applications" /></a></li>

			<li><a href="position/list.do"><spring:message
						code="master.page.position" /></a></li>

			<li><a href="curricula/list.do"><spring:message
						code="master.page.curricula" /></a></li>


			<li><a class="fNiv"><spring:message
						code="master.page.finder" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="finder/rookie/update.do"><spring:message
								code="master.page.finder.update" /></a></li>
					<li><a href="finder/rookie/list.do"><spring:message
								code="master.page.finder.result" /></a></li>

				</ul></li>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
		</security:authorize>

		<li><a class="fNiv" href="company/any/list.do"><spring:message
					code="master.page.listcompanys" /></a></li>

		<security:authorize access="isAnonymous()">
			<li><a href="position/list.do"><spring:message
						code="master.page.position" /></a>
		</security:authorize>

		<security:authorize access="hasRole('COMPANY')">
			<li><a href="application/company/list.do"><spring:message
						code="master.page.applications" /></a></li>
			<li><a class="fNiv" href="position/myList.do"><spring:message
						code="master.page.myPosition" /></a>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<%-- 			<li><a href="position/list.do"><spring:message
						code="master.page.position" /></a> --%>

			<li><a class="fNiv"><spring:message
						code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="company/none/create.do"><spring:message
								code="master.page.registerascompany" /></a></li>
					<li><a href="register/actor.do?authority=ROOKIE"><spring:message
								code="master.page.registerasrookie" /></a></li>
					<li><a href="provider/none/register.do"><spring:message
								code="master.page.registerasprovider" /></a></li>
				</ul></li>
		</security:authorize>

		<li><a class="fNiv" href="provider/any/list.do"><spring:message
					code="master.page.listproviders" /></a></li>

		<security:authorize access="hasRole('COMPANY')">
			<li><a class="fNiv" href="position/list.do"><spring:message
						code="master.page.position" /></a></li>
			<!-- li><a class="fNiv" href="position/myList.do?companyId"><spring:message
						code="master.page.myPosition" /></a></li -->
			<li><a class="fNiv" href="problem/company/list.do"><spring:message
						code="master.page.problems" /></a></li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a href="socialProfile/list.do"> <spring:message
						code="master.page.socialProfile" />
			</a></li>
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="!hasRole('COMPANY') && !hasRole('PROVIDER')">
						<li><a href="actor/edit.do"><spring:message
									code="master.page.profile.edit" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('COMPANY')">
						<li><a href="company/company/edit.do"><spring:message
									code="master.page.profile.edit" /></a></li>
					</security:authorize>
					
					<security:authorize access="hasRole('PROVIDER')">
						<li><a href="provider/provider/edit.do"><spring:message
									code="master.page.profile.edit" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('ADMIN')">
						<li><a href="administrator/deleteAdmin.do"
							style="color: yellow"><spring:message
									code="master.page.profile.deleteActor" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('COMPANY')">
						<li><a href="company/deleteCompany.do" style="color: yellow"><spring:message
									code="master.page.profile.deleteActor" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('ROOKIE')">
						<li><a href="rookie/deleteRookie.do" style="color: yellow"><spring:message
									code="master.page.profile.deleteActor" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('AUDITOR')">
						<li><a href="auditor/deleteAuditor.do" style="color: yellow"><spring:message
									code="master.page.profile.deleteActor" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('PROVIDER')">
						<li><a href="provider/deleteProvider.do"
							style="color: yellow"><spring:message
									code="master.page.profile.deleteActor" /></a></li>
					</security:authorize>

					<li><a href="message/actor/list.do"><spring:message
								code="master.page.profile.listmessages" /></a></li>

					<li><a href="message/actor/send.do"><spring:message
								code="master.page.profile.sendmessage" /></a></li>

					<security:authorize access="hasRole('ADMIN')">
						<li><a href="message/administrator/broadcast.do"><spring:message
									code="master.page.profile.broadcastmessage" /></a></li>
					</security:authorize>
					
					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>
		</security:authorize>
	</ul>
</div>

<div align="center">
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>


