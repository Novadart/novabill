<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<c:url value="/private/upgrade/send-paypal-supscription-request"
	var="upgrade_url" />
<c:url value="/register" var="register_url" />
<c:url value="/images/check.png" var="check_url" />


<table>
	<thead>
		<tr>
			<td></td>
			<td class="header_free">Free Account</td>
			<td class="header_premium">Premium Account</td>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.invoiceLimit" /></td>
			<td class="free">30</td>
			<td class="premium"><spring:message code="premiumInfo.unlimited" />
			</td>
		</tr>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.estimationLimit" /></td>
			<td class="free">30</td>
			<td class="premium"><spring:message code="premiumInfo.unlimited" /></td>
		</tr>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.clientLimit" /></td>
			<td class="free">10</td>
			<td class="premium"><spring:message code="premiumInfo.unlimited" /></td>
		</tr>
		<tr>
			<td class="feature"><spring:message code="premiumInfo.exportPDF" /></td>
			<td class="free"><img class="check" src="${check_url}"></td>
			<td class="premium"><img class="check" src="${check_url}"></td>
		</tr>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.basicStats" /></td>
			<td class="free"><img class="check" src="${check_url}"></td>
			<td class="premium"><img class="check" src="${check_url}"></td>
		</tr>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.anywhereAccess" /></td>
			<td class="free"><img class="check" src="${check_url}"></td>
			<td class="premium"><img class="check" src="${check_url}"></td>
		</tr>
		<tr>
			<td class="feature"><spring:message code="premiumInfo.backup" /></td>
			<td class="free"><img class="check" src="${check_url}"></td>
			<td class="premium"><img class="check" src="${check_url}"></td>
		</tr>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.dataExport" /></td>
			<td class="free"></td>
			<td class="premium"><img class="check" src="${check_url}"></td>
		</tr>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.pdfWithoutWatermark" /></td>
			<td class="free"></td>
			<td class="premium"><img class="check" src="${check_url}"></td>
		</tr>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.expiringInvoicesNotifications" /></td>
			<td class="free"></td>
			<td class="premium"><img class="check" src="${check_url}"></td>
		</tr>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.trackingPayedInvoices" /></td>
			<td class="free"></td>
			<td class="premium"><img class="check" src="${check_url}"></td>
		</tr>
		<tr>
			<td class="feature"><spring:message
					code="premiumInfo.advancedStats" /></td>
			<td class="free"></td>
			<td class="premium"><img class="check" src="${check_url}"></td>
		</tr>
	</tbody>
	<tfoot>
		<tr>
			<td class="feature"></td>
			<td class="free"><sec:authorize access="isAnonymous()">
					<a class="freeButton action2-button" href="${register_url}">Free
						Account</a>
				</sec:authorize></td>
			<td class="premium"><sec:authorize
					access="not hasRole('ROLE_BUSINESS_PREMIUM')">
					<a class="premiumButton action-button" href="${upgrade_url}">Premium
						Account </a>
				</sec:authorize></td>
		</tr>
	</tfoot>
</table>


