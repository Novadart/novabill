<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<img class="errorImg" src='<spring:url value="images/error/404.png" />'>
<div class="errorMessage"><spring:message code="error.message404"></spring:message> </div>
<a class="errorGotohome" href="<spring:url value="/" />"><spring:message code="error.gotohome"></spring:message></a>